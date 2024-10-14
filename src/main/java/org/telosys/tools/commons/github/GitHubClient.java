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

	public static final String VERSION = "3.6 (2024-10-14)" ; // GitHub Client Version (for checking in CLI and Download)
	
	// v 4.2.0 (with branch parameter)
	private static final String DOWNLOAD_BRANCH_URL_PATTERN = "https://github.com/${DEPOT}/${REPO}/archive/refs/heads/${BRANCH}.zip" ;

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
	
	private DepotElement buildDepotElementFromJSON(JSONObject jsonObject) throws TelosysToolsException {		
		long   id   = getLongAttribute(jsonObject, "id");
		String name = getStringAttribute(jsonObject, "name", "(#"+id+"-no-name)");
		String description = getStringAttribute(jsonObject, "description", "");
		long   size = getLongAttribute(jsonObject, "size");
		String defaultBranch = getStringAttribute(jsonObject, "default_branch", ""); // v 4.2.0
		String visibility = getStringAttribute(jsonObject, "visibility", ""); // v 4.2.0
		return new DepotElement(id, name, description, size, defaultBranch, visibility );
	}
	
	private List<DepotElement> getDepotElementsFromJSON(String responseBody) throws TelosysToolsException {

		// JSON parsing
		List<DepotElement> repositories = new LinkedList<>();
		JSONParser parser = new JSONParser();
		try {
			Object object = parser.parse(responseBody);
			if ( object instanceof JSONArray ) {
				JSONArray jsonArray = (JSONArray) object ;
				for ( Object repositoryObject: jsonArray ) {
					JSONObject jsonObject = (JSONObject) repositoryObject ; 
					repositories.add( buildDepotElementFromJSON(jsonObject) );
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
	
	/* (non-Javadoc)
	 * @see org.telosys.tools.commons.depot.DepotClient#getRepositories(org.telosys.tools.commons.depot.Depot)
	 */
	@Override
	public DepotResponse getRepositories(Depot depot) throws TelosysToolsException {
		checkDepotIsGitHub(depot);

		// Call GitHub API via HTTP
		String url = depot.getApiUrl();
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

	protected String buildDownloadBranchURL(String userName, String repoName, String branch) {
		HashMap<String,String> variables = new HashMap<>();
		variables.put("${DEPOT}", userName);
		variables.put("${REPO}", repoName);
		variables.put("${BRANCH}", branch);
		VariablesManager variablesManager = new VariablesManager(variables);
		return variablesManager.replaceVariables(DOWNLOAD_BRANCH_URL_PATTERN);
	}
	
	/* (non-Javadoc)
	 * @see org.telosys.tools.commons.depot.DepotClient#downloadRepositoryBranch(org.telosys.tools.commons.depot.Depot, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public final long downloadRepositoryBranch(Depot depot, String repoName, String branch, String destinationFile) throws TelosysToolsException {
		checkDepotIsGitHub(depot);
		String url = buildDownloadBranchURL(depot.getName(), repoName, branch);
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
	public GitHubRateLimitResponse getRateLimit(Depot depot) throws TelosysToolsException {
		checkDepotIsGitHub(depot);
		String url = depot.getApiRateLimitUrl() ;
		HttpResponse response = httpGet(url);
		// v 4.2.0 : http status code in GitHubRateLimitResponse
		return new GitHubRateLimitResponse(url, response.getStatusCode(), new GitHubRateLimit(response), new String(response.getBodyContent() ));
	}
	
	private void checkDepotIsGitHub(Depot depot) throws TelosysToolsException {
		if ( ! depot.isGitHubDepot() ) {
			throw new TelosysToolsException("Invalid Depot argument (" + depot.getDefinition() + ") not a GitHub depot");
		}
	}
}
