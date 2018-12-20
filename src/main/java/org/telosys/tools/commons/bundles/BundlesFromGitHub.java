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

import org.telosys.tools.commons.github.GitHubRateLimit;

public class BundlesFromGitHub {

	private final BundlesNames bundlesNames ;
	
	private final GitHubRateLimit githubRateLimit ;
	
	public BundlesFromGitHub(BundlesNames bundlesNames, GitHubRateLimit githubRateLimit) {
		super();
		if ( bundlesNames == null ) {
			throw new IllegalStateException("bundlesList is null");
		}
		if ( githubRateLimit == null ) {
			throw new IllegalStateException("githubRateLimit is null");
		}
		this.bundlesNames    = bundlesNames ;
		this.githubRateLimit = githubRateLimit  ;
	}

	public BundlesNames getBundlesNames() {
		return bundlesNames;
	}

	public int getLimit() {
		return githubRateLimit.getLimitAsInt();
	}

	public int getRemaining() {
		return githubRateLimit.getRemainingAsInt();
	}

	public Date getReset() {
		return githubRateLimit.getResetAsDate();
	}
	
	public String getRateLimitMessage() {
		return githubRateLimit.getStandardMessage();
	}
	
}
