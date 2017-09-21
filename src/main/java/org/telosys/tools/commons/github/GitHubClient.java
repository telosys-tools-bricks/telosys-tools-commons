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

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.telosys.tools.commons.http.HttpClient;
import org.telosys.tools.commons.http.HttpResponse;

/**
 * Client side for GitHub API 
 * 
 * @author L. Guerin
 *
 */
public class GitHubClient {

	public final static String GIT_HUB_HOST_URL = "https://api.github.com" ;
	
	public final static String GIT_HUB_REPO_URL_PATTERN =  "https://github.com/${USER}/${REPO}/archive/master.zip" ;

	
//	private final String     gitHubURLPattern ;
	private final Properties proxyProperties ;
	
	/**
	 * Constructor
	 * @param proxyProperties
	 */
	public GitHubClient(Properties proxyProperties) {
		super();
		this.proxyProperties = proxyProperties;
	}
//	public GitHubClient(String gitHubURLPattern, Properties proxyProperties) {
//		super();
//		this.gitHubURLPattern = gitHubURLPattern ;
//		this.proxyProperties = proxyProperties;
//	}

	/**
	 * Returns the GitHub response in JSON format (REST API)
	 * @param userName
	 * @return
	 */
	public String getRepositoriesJSON( String userName ) {

		String urlString = GIT_HUB_HOST_URL + "/users/" + userName + "/repos" ;
//		HttpClientConfig httpClientConfig = null ;
//		if ( proxyProperties != null ) {
//			httpClientConfig = new HttpClientConfig(proxyProperties);
//		}
//		HttpClient httpClient = new HttpClient(httpClientConfig);
		HttpClient httpClient = new HttpClient(proxyProperties);
		HttpResponse response;
		try {
			response = httpClient.get(urlString, null);
		} catch (Exception e) {
			throw new RuntimeException ("Http error", e);
		}
		return new String(response.getBodyContent());
	}

	/**
	 * Returns the repositories available on GitHub for the given user name 
	 * @param userName
	 * @return
	 */
	public List<GitHubRepository> getRepositories( String userName ) throws Exception{

		List<GitHubRepository> repositories = new LinkedList<GitHubRepository>();
		String json = getRepositoriesJSON( userName );
		JSONParser parser = new JSONParser();
		try {
			Object oList = parser.parse(json);
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
		GitHubUtil.sortByName(repositories);
		return repositories ;
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
	 * Download a GitHub repository (zip file)
	 * @param userName GitHub user name
	 * @param repoName GitHub repository name
	 * @param destinationFile the full file name on the filesystem 
	 * @return file size (bytes count)
	 */
	public final long downloadRepository(String userName, String repoName, String destinationFile) throws Exception {
		String url = GitHubUtil.buildGitHubURL(userName, repoName, GIT_HUB_REPO_URL_PATTERN);

		long bytesCount = 0 ;
		HttpClient httpClient = new HttpClient(proxyProperties);
//		try {
//			bytesCount = httpClient.downloadFile(url, destinationFile);
//		} catch (Exception e) {
//			throw new Exception ("Cannot download file (http error)", e);
//		}
		bytesCount = httpClient.downloadFile(url, destinationFile);
		return bytesCount ;
	}
}
