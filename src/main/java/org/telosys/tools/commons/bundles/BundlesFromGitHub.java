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
package org.telosys.tools.commons.bundles;

import java.util.Date;
import java.util.List;

import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.commons.github.GitHubRateLimit;

public class BundlesFromGitHub {

	/**
	 * HTTP Status Code returned by GitHub
	 */
	private final int httpStatusCode ;
	
	/**
	 * Bundles names retrieved from GitHub
	 */
	private final List<String> bundles ;
	
	/**
	 * API rate limit
	 */
	private final GitHubRateLimit githubRateLimit ;
	
	/**
	 * Constructor
	 * @param httpStatusCode
	 * @param bundles
	 * @param githubRateLimit
	 */
	public BundlesFromGitHub(int httpStatusCode, List<String> bundles, GitHubRateLimit githubRateLimit) {
		super();
		this.httpStatusCode = httpStatusCode ;
		
		if ( bundles == null ) {
			throw new TelosysRuntimeException("bundlesList is null");
		}
		if ( githubRateLimit == null ) {
			throw new TelosysRuntimeException("githubRateLimit is null");
		}
		this.bundles         = bundles ;
		this.githubRateLimit = githubRateLimit  ;
	}

	/**
	 * Returns the HTTP Status Code ( e.g. : 200, 403, etc)
	 * @return
	 */
	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	
	/**
	 * Returns the bundles names object
	 * @return
	 */
	public List<String> getBundles() {
		return bundles;
	}

	/**
	 * Returns GitHub API rate 'limit' 
	 * @return
	 */
	public int getLimit() {
		return githubRateLimit.getLimitAsInt();
	}

	/**
	 * Returns GitHub API rate 'remaining' 
	 * @return
	 */
	public int getRemaining() {
		return githubRateLimit.getRemainingAsInt();
	}

	/**
	 * Returns GitHub API rate 'reset' date-time
	 * @return
	 */
	public Date getReset() {
		return githubRateLimit.getResetAsDate();
	}
	
	public String getRateLimitMessage() {
		return githubRateLimit.getStandardMessage();
	}
	
}
