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

import java.util.List;

public class BundlesFromGitHub {

	private final List<String> bundlesNames ;
	
	private final String       rateLimitMessage ;

	public BundlesFromGitHub(List<String> bundlesNames, String rateLimitMessage) {
		super();
		if ( bundlesNames == null ) {
			throw new IllegalStateException("bundlesNames is null");
		}
		if ( rateLimitMessage == null ) {
			throw new IllegalStateException("rateLimitMessage is null");
		}
		this.bundlesNames = bundlesNames ;
		this.rateLimitMessage = rateLimitMessage  ;
	}

	public List<String> getBundlesNames() {
		return bundlesNames;
	}

	public String getRateLimitMessage() {
		return rateLimitMessage;
	}
	
}
