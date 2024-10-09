/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.commons.github;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.depot.Depot;
import org.telosys.tools.commons.depot.DepotClient;
import org.telosys.tools.commons.depot.DepotElement;
import org.telosys.tools.commons.depot.DepotRateLimit;
import org.telosys.tools.commons.depot.DepotResponse;
import org.telosys.tools.commons.http.HttpClient;
import org.telosys.tools.commons.http.HttpResponse;
import org.telosys.tools.commons.variables.VariablesManager;

/**
 * Client side for GitHub API 
 * 
 * @author L. Guerin
 *
 */
public class GitHubClient implements DepotClient {

	public static final String VERSION = "3.6 (2024-10-08)" ; // GitHub Client Version (for checking in CLI and Download)
	
	protected static final String GIT_HUB_HOST_URL = "https://api.github.com" ;
	
	public static final String GIT_HUB_DOWNLOAD_URL_PATTERN = "https://github.com/${USER}/${REPO}/archive/master.zip" ;

	private final String propertiesFileAbsolutePath ;
	
	/**
	 * Constructor
	 * @param propertiesFileAbsolutePath
	 */
	public GitHubClient(String propertiesFileAbsolutePath) {
		super();
		if ( propertiesFileAbsolutePath == null ) {
			throw new IllegalArgumentException("File path argument is mandatory");
		}
		this.propertiesFileAbsolutePath = propertiesFileAbsolutePath;
	}
	
	private HttpClient buildHttpClient() {
		if ( propertiesFileAbsolutePath != null ) {
			return new HttpClient(new File(propertiesFileAbsolutePath));
		}
		else {
			return new HttpClient();
		}
	}
	
	private Map<String, String> buildRequestHeaders() throws TelosysToolsException {
		if ( GitHubToken.isSet() ) {
			Map<String, String> headers = new HashMap<>();
			// since ver 4.2.0 : use GitHub Personal Access Token (PAT) instead of user+password 
			// GitHub API doc : 
			//  In most cases, you can use "Authorization: Bearer" or "Authorization: token" to pass a token. 
			//  However, if you are passing a JSON web token (JWT), you must use Authorization: Bearer
			headers.put("Authorization", "Bearer " + GitHubToken.get());
			return headers ;
		}
		else {
			return null ;
		}
	}
	
	/**
	 * @param url
	 * @return
	 * @throws TelosysToolsException
	 */
	private HttpResponse httpGet( String url ) throws TelosysToolsException {
		HttpClient httpClient = buildHttpClient();
		try {
			// Sometimes GitHub return a 403 status code 
			return httpClient.get(url, buildRequestHeaders() );
		} catch (Exception e) {
			throw new TelosysToolsException("HTTP 'GET' error " + e.getMessage(), e);
		}
	}
	
	private List<DepotElement> getDepotElementsFromJSON(String responseBody) throws TelosysToolsException {

		// JSON parsing
		List<DepotElement> repositories = new LinkedList<>();
		JSONParser parser = new JSONParser();
		try {
			Object oList = parser.parse(responseBody);
			if ( oList instanceof JSONArray ) {
				JSONArray repositoriesArray = (JSONArray) oList ;
				for ( Object repositoryObject: repositoriesArray ) {
					JSONObject repo = (JSONObject) repositoryObject ; 
					long   id   = getLongAttribute(repo, "id");
					String name = getStringAttribute(repo, "name", "(#"+id+"-no-name)");
					String description = getStringAttribute(repo, "description", "(no-description)");
					long   size = getLongAttribute(repo, "size");
					// Add the repository in the list
					repositories.add( new DepotElement(id, name, description, size ) );
				}
			}
			else {
				throw new TelosysToolsException("JSON error : array expected as root");
			}
		} catch (ParseException e) {
			throw new TelosysToolsException("JSON error : cannot parse the JSON response.");
		}
		return repositories;
	}
	
	protected String buildGitHubApiUrl(Depot depot) throws TelosysToolsException {
		if ( depot.isGitHubOrganization() ) {
			// example: https://api.github.com/orgs/telosys-models/repos  
			return GIT_HUB_HOST_URL + "/orgs/" + depot.getName() + "/repos" ; 
		}
		else if ( depot.isGitHubUser()) {
			// example: https://api.github.com/users/telosys-models/repos  
			return GIT_HUB_HOST_URL + "/users/" + depot.getName() + "/repos" ; 
		}
		else if ( depot.isGitHubCurrentUser() ) {
			// fixed URL
			return "https://api.github.com/user/repos";
		}
		else {
			throw new TelosysToolsException("Depot error: invalid GitHub type ");
		}
	}

	public DepotResponse getRepositories(Depot depot) throws TelosysToolsException {	
		// Call GitHub API via HTTP
		// String url = GIT_HUB_HOST_URL + "/users/" + depotName + "/repos" ; // for GitHub 'depotName' = 'GitHub user name'
		String url = buildGitHubApiUrl(depot);
		HttpResponse response = httpGet(url);

		// Rate Limit from http headers 
		GitHubRateLimit rl = new GitHubRateLimit(response);
		DepotRateLimit rateLimit = new DepotRateLimit(rl.getLimit(), rl.getRemaining(), rl.getReset());
		
		// Repositories from http response body  
		String responseBody = new String(response.getBodyContent());
		
		List<DepotElement> depotElements = new LinkedList<>();
		if ( response.getStatusCode() == 200 ) { 
			// Parse the response body (repositories list in JSON format) 
			depotElements = getDepotElementsFromJSON(responseBody);
		}
		// If status is 403 : noting to do (the ratelimit is provided in the result)

		// Return the result
		return new DepotResponse(depot.getDefinition(), url, response.getStatusCode(), depotElements, rateLimit, responseBody);
	}
	
	private static final String JSON_ERR_ATTRIBUTE = "JSON error : attribute ";

	/**
	 * Returns the String value for the given attribute name
	 * @param jsonObject
	 * @param attributeName
	 * @param defaultValue
	 * @return
	 * @throws TelosysToolsException
	 */
	private String getStringAttribute( JSONObject jsonObject, String attributeName, String defaultValue ) throws TelosysToolsException {
		Object oAttributeValue = jsonObject.get( attributeName );
		if ( oAttributeValue != null ) {
			if ( oAttributeValue instanceof String) {
				return (String)oAttributeValue ;
			}
			else {
				throw new TelosysToolsException( JSON_ERR_ATTRIBUTE + "'" + attributeName + "' is not a String");
			}
		}
		else {
			if ( defaultValue != null ) {
				return defaultValue ;
			}
			else {
				throw new TelosysToolsException( JSON_ERR_ATTRIBUTE + "'" + attributeName + "' not found");
			}
		}
	}

	/**
	 * Returns the integer value for the given attribute name
	 * @param jsonObject
	 * @param attributeName
	 * @return
	 * @throws TelosysToolsException
	 */
	private long getLongAttribute( JSONObject jsonObject, String attributeName ) throws TelosysToolsException {
		Object oAttributeValue = jsonObject.get( attributeName );
		if ( oAttributeValue != null ) {
			if ( oAttributeValue instanceof Long) {
				return ((Long)oAttributeValue).longValue();
			}
			else {
				throw new TelosysToolsException( "JSON error : attribute '" + attributeName 
						+ "' is not a Integer ("+oAttributeValue.getClass().getCanonicalName()+")");
			}
		}
		else {
			throw new TelosysToolsException( "JSON error : attribute '" + attributeName + "' not found");
		}
	}

	protected String buildGitHubDownloadURL(String userName, String repoName) {
		HashMap<String,String> hmVariables = new HashMap<>();
		hmVariables.put("${USER}", userName);
		hmVariables.put("${REPO}", repoName);
		VariablesManager variablesManager = new VariablesManager(hmVariables);
		return variablesManager.replaceVariables(GIT_HUB_DOWNLOAD_URL_PATTERN);
	}
	
	/**
	 * Downloads a GitHub repository (e.g. "https://github.com/telosys-templates/{REPOSITORY}-master.zip" )<br>
	 * Simple HTTP download, doesn't use the GitHub API <br>
	 * @param depot
	 * @param repoName GitHub repository name ( e.g. "php7-web-mvc" or "python-web-rest-bottle" )
	 * @param destinationFile the full file name on the filesystem 
	 * @return file size (bytes count)
	 * @throws TelosysToolsException
	 */
	public final long downloadRepository(Depot depot, String repoName, String destinationFile) throws TelosysToolsException {
		String url = buildGitHubDownloadURL(depot.getName(), repoName);
		HttpClient httpClient = buildHttpClient();
		try {
			return httpClient.downloadFile(url, destinationFile);
		} catch (Exception e) {
			throw new TelosysToolsException("Download error (URL="+url+") "+e.getMessage() , e);
		}
	}
	
	/**
	 * @return
	 * @throws TelosysToolsException
	 */
	public GitHubRateLimitResponse getRateLimit() throws TelosysToolsException {
		String url = GIT_HUB_HOST_URL + "/rate_limit" ;
		HttpResponse response = httpGet(url);
		// v 4.2.0 : http status code in GitHubRateLimitResponse
		return new GitHubRateLimitResponse(new GitHubRateLimit(response), response.getStatusCode(), new String(response.getBodyContent() ));
	}
	
}
