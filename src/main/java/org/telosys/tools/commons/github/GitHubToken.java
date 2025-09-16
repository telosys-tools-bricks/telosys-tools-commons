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

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.credentials.TokenUtil;

/**
 * GitHub token utility class 
 * 
 * @author L. Guerin
 *
 */
public class GitHubToken {

	private static final String GITHUB_TOKEN_NAME = "gh" ;
	private static final boolean ENCRYPTION = true ;
	
	/**
	 * Private constructor
	 */
	private GitHubToken() {
	}

	/**
	 * Set the current token
	 * @param token
	 * @throws TelosysToolsException 
	 */
	public static void set(String token) throws TelosysToolsException {
		if ( token != null ) {
			TokenUtil.saveToken(GITHUB_TOKEN_NAME, token, ENCRYPTION) ;
		}
		else {
			throw new TelosysToolsException("Token argument is null");
		}
	}

	/**
	 * Clear the current token
	 * @throws TelosysToolsException
	 */
	public static void clear() throws TelosysToolsException {
		TokenUtil.deleteToken(GITHUB_TOKEN_NAME, ENCRYPTION);
	}

	/**
	 * Returns true is a current token is defined
	 * @return
	 * @throws TelosysToolsException 
	 */
	public static boolean isSet() throws TelosysToolsException {
		return TokenUtil.tokenExists(GITHUB_TOKEN_NAME);
	}
	
	/**
	 * Returns the current token 
	 * @return
	 * @throws TelosysToolsException
	 */
	public static String get() throws TelosysToolsException {
		return TokenUtil.loadToken(GITHUB_TOKEN_NAME, ENCRYPTION) ;
	}
	
}
