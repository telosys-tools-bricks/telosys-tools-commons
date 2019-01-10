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

/**
 * Client side for GitHub API 
 * 
 * @author L. Guerin
 *
 */
public class GitHubUser {

	private static String USER     =  null ;
	private static String PASSWORD =  null ;

	/**
	 * Private constructor
	 */
	private GitHubUser() {
	}

	/**
	 * Set the current user/password
	 * @param user
	 * @param password
	 */
	public static void set(String user, String password) {
		if ( user != null && password != null ) {
			USER = user ;
			PASSWORD = password ;
		}
		else {
			throw new IllegalArgumentException("User or password argument is null");
		}
	}

	/**
	 * Clear the current user/password (reset to undefined)
	 */
	public static void clear() {
		USER = null ;
		PASSWORD = null ;
	}

	/**
	 * Returns true is a current user/password is defined
	 * @return
	 */
	public static boolean isSet() {
		return USER != null && PASSWORD != null ;
	}
	
	public static String getUser() {
		return USER ;
	}
	
	public static String getPassword() {
		return PASSWORD;
	}
}
