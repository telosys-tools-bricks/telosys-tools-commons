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
import org.telosys.tools.commons.http.Base64;
import org.telosys.tools.commons.http.HttpClient;
import org.telosys.tools.commons.http.HttpResponse;

/**
 * Client side for GitHub API 
 * 
 * @author L. Guerin
 *
 */
public class GitHubClient {

//	public static final String VERSION = "2.1" ; // GitHub Client Version (for checking in CLI and Download)
	public static final String VERSION = "3.2 (2024-01-08)" ; // GitHub Client Version (for checking in CLI and Download)
	
	public static final String GIT_HUB_HOST_URL = "https://api.github.com" ;
	
	public static final String GIT_HUB_REPO_URL_PATTERN =  "https://github.com/${USER}/${REPO}/archive/master.zip" ;

	
//	private final Properties proxyProperties ;
	private final String propertiesFileAbsolutePath ;
	
//	/**
//	 * Constructor
//	 * @param proxyProperties
//	 */
//	public GitHubClient(Properties proxyProperties) {
//		super();
//		this.proxyProperties = proxyProperties;
//	}
	public GitHubClient(String propertiesFileAbsolutePath) {
		super();
		if ( propertiesFileAbsolutePath == null ) {
			throw new IllegalArgumentException("File path argument is mandatory");
		}
		this.propertiesFileAbsolutePath = propertiesFileAbsolutePath;
	}
	
	private HttpClient buildHttpClient() {
//		PropertiesManager propertiesManager = new PropertiesManager(propertiesFileAbsolutePath) ;
//		Properties properties = propertiesManager.load(); // Ret NULL if file not found
//		if ( properties != null ) {
//			return new HttpClient(properties);
//		}
//		else {
//			// Properties file not found, no properties loaded : use default values
//			return new HttpClient();
//		}
		if ( propertiesFileAbsolutePath != null ) {
			return new HttpClient(new File(propertiesFileAbsolutePath));
		}
		else {
			return new HttpClient();
		}
	}
	
	private Map<String, String> buildRequestHeaders() {
		if ( GitHubUser.isSet() ) {
			Map<String, String> headers = new HashMap<>();
			// Build User-Password string - Base64 encoded
			String userPassword = GitHubUser.getUser() + ":" + GitHubUser.getPassword() ;
			String userPasswordBase64 = Base64.encode(userPassword);
			// Set "Authorization" header in the map
			headers.put("Authorization", "Basic " + userPasswordBase64);
			return headers ;
		}
		else {
			return null ;
		}
	}
	
	private HttpResponse httpGet( String url ) throws Exception {
//		HttpClient httpClient = new HttpClient(proxyProperties);
		HttpClient httpClient = buildHttpClient();
		HttpResponse response;
		try {
			// Sometimes GitHub return a 403 status code 
			response = httpClient.get(url, buildRequestHeaders() );
		} catch (Exception e) {
			throw new Exception ("HTTP 'GET' error", e);
		}
		return response ;
	}
	
	/**
	 * @param json response body in JSON format
	 * @return
	 * @throws Exception
	 */
	private List<GitHubRepository> getRepositoriesFromJSON(String responseBody) throws Exception {

		// JSON parsing
		List<GitHubRepository> repositories = new LinkedList<>();
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
					repositories.add( new GitHubRepository(id, name, description, size ) );
				}
			}
			else {
				throw new Exception ( "JSON error : array expected as root");
			}
		} catch (ParseException e) {
			throw new Exception ( "JSON error : cannot parse the JSON response.");
		}
		return repositories;
	}
	
	/**
	 * Returns the GitHub response containing the repositories list for a given user 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public GitHubRepositoriesResponse getRepositories(String userName) throws Exception {

		// Call GitHub API via HTTP
		//HttpResponse response = getApiHttpResponse(userName);
		String url = GIT_HUB_HOST_URL + "/users/" + userName + "/repos" ;
		HttpResponse response = httpGet(url);

		
		GitHubRateLimit rateLimit = new GitHubRateLimit(response);
		
		String responseBody = new String(response.getBodyContent());
		
		List<GitHubRepository> repositories = new LinkedList<>();
		if ( response.getStatusCode() == 200 ) { 
			// Parse the response body (repositories list in JSON format) 
			repositories = getRepositoriesFromJSON(responseBody);
		}
		// If status is 403 : noting to do (the ratelimit is provided in the result)

		// Return the result
		return new GitHubRepositoriesResponse(response.getStatusCode(), repositories, rateLimit, responseBody);
	}

	/**
	 * Returns the String value for the given attribute name
	 * @param jsonObject
	 * @param attributeName
	 * @param defaultValue
	 * @return
	 */
	private String getStringAttribute( JSONObject jsonObject, String attributeName, String defaultValue ) throws Exception {
		Object oAttributeValue = jsonObject.get( attributeName );
		if ( oAttributeValue != null ) {
			if ( oAttributeValue instanceof String) {
				return (String)oAttributeValue ;
			}
			else {
				throw new Exception ( "JSON error : attribute '" + attributeName + "' is not a String");
			}
		}
		else {
			if ( defaultValue != null ) {
				return defaultValue ;
			}
			else {
				throw new Exception ( "JSON error : attribute '" + attributeName + "' not found");
			}
		}
	}

	/**
	 * Returns the integer value for the given attribute name
	 * @param jsonObject
	 * @param attributeName
	 * @return
	 */
	private long getLongAttribute( JSONObject jsonObject, String attributeName ) throws Exception{
		Object oAttributeValue = jsonObject.get( attributeName );
		if ( oAttributeValue != null ) {
			if ( oAttributeValue instanceof Long) {
				return ((Long)oAttributeValue).longValue();
			}
			else {
				throw new Exception ( "JSON error : attribute '" + attributeName 
						+ "' is not a Integer ("+oAttributeValue.getClass().getCanonicalName()+")");
			}
		}
		else {
			throw new Exception ( "JSON error : attribute '" + attributeName + "' not found");
		}
	}
	
	/**
	 * Downloads a GitHub repository (e.g. "https://github.com/telosys-templates/{REPOSITORY}-master.zip" )<br>
	 * Simple HTTP download, doesn't use the GitHub API <br>
	 * 
	 * @param userName GitHub user name or organization name ( e.g. "telosys-templates" )
	 * @param repoName GitHub repository name ( e.g. "php7-web-mvc" or "python-web-rest-bottle" )
	 * @param destinationFile the full file name on the filesystem 
	 * @return file size (bytes count)
	 */
	public final long downloadRepository(String userName, String repoName, String destinationFile) throws Exception {
		String url = GitHubUtil.buildGitHubURL(userName, repoName, GIT_HUB_REPO_URL_PATTERN);

		long bytesCount = 0 ;
//		HttpClient httpClient = new HttpClient(proxyProperties);
		HttpClient httpClient = buildHttpClient();
		
		bytesCount = httpClient.downloadFile(url, destinationFile);
		return bytesCount ;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public GitHubRateLimitResponse getRateLimit() throws Exception {
		String url = GitHubClient.GIT_HUB_HOST_URL + "/rate_limit" ;
		HttpResponse response = httpGet(url);
		
		if ( response.getStatusCode() == 200 ) {
			return new GitHubRateLimitResponse(new GitHubRateLimit(response), new String(response.getBodyContent() ));
		}
		else {
			throw new Exception ("Cannot get GitHub rate limit. HTTP status code = " + response.getStatusCode() );
		}
		// Example of response body :
		//   {
		//      "resources":
		//         {
		//            "core"   : { "limit":60, "remaining":0,  "reset":1545125728 },
		//            "search" : { "limit":10, "remaining":10, "reset":1545124912 },
		//            "graphql": { "limit":0,  "remaining":0,  "reset":1545128452 }
		//         },
		//
		//      "rate": { "limit":60, "remaining":0, "reset":1545125728 }
		//   }
	}
	
}
