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

/**
 * This class holds a list of bubdles provided by GitHub API <br>
 * it also contains "API Rate Limit" information
 * 
 * 
 * @author Laurent Guerin
 *
 */
public class GitHubRepositoriesResponse {

	/**
	 * List of GitHub repositories 
	 */
	private final List<GitHubRepository> repositories ;
	
	/**
	 * API rate limit 
	 */
	private final GitHubRateLimit rateLimit ;

	/**
	 * Response body
	 */
	private final String responseBody ;

	/**
	 * Constructor
	 * @param repositories
	 * @param rateLimit
	 * @param responseBody
	 */
	public GitHubRepositoriesResponse(List<GitHubRepository> repositories, GitHubRateLimit rateLimit, String responseBody) {
		super();
		this.repositories = repositories;
		GitHubUtil.sortByName(this.repositories);
		
		this.rateLimit = rateLimit ;
		this.responseBody = responseBody ;
	}

	/**
	 * Returns a list containing all the GitHubRepository objects
	 * @return
	 */
	public List<GitHubRepository> getRepositories() {
		return repositories;
	}

	/**
	 * Returns a list of bundles names
	 * @return
	 */
	public List<String> getBundlesNames() {
		List<String> bundles = new LinkedList<>();
		if ( repositories != null ) {
			for ( GitHubRepository repo : repositories ) {
				bundles.add( repo.getName() );				
			}
		}
		return bundles ;
	}

	public GitHubRateLimit getRateLimit() {
		return rateLimit;
	}

	public String getResponseBody() {
		return responseBody;
	}

}
