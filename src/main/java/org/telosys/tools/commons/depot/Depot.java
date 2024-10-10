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
package org.telosys.tools.commons.depot;

import java.util.Arrays;
import java.util.List;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;

/** 
 * Depot 
 * Syntax:
 *  depot-type : [ depot-name ] [ ( root-url ) ]
 *  Examples : 
 *   - github_org : org-name
 *   - github_org : org-name ( http://myhost:9090 )
 *   - github_user : user-name
 *   - github_current_user
 *   
 *  
 * @author L. Guerin
 *
 */
public class Depot {

	private static final String INVALID_DEPOT = "Invalid depot"; 
	
	// GitHub 
	private static final String GITHUB_ORG          = "github_org"; 
	private static final String GITHUB_USER         = "github_user"; 
	private static final String GITHUB_CURRENT_USER = "github_current_user"; 
	private static final String GITHUB_DEFAULT_ROOT_URL = "https://api.github.com" ;


	// GitLab 
	private static final String GITLAB_GROUP  = "gitlab_group"; // GitLab "group" or "subgroup" 
	private static final String GITLAB_USER   = "gitlab_user"; 
	
	private static final List<String> VALID_TYPES = Arrays.asList(GITHUB_ORG,   GITHUB_USER, GITHUB_CURRENT_USER,  
																  GITLAB_GROUP, GITLAB_USER );
	
	private final String definition;
	private final String type;
	private final String name;
	private final String rootUrl;

	public Depot(String depotString) throws TelosysToolsException {
		super();
		if (depotString == null) {
			throw new TelosysToolsException("Depot argument is null");
		}
		this.definition = depotString;
		// parse depot definition 
		this.type = parseDepotType(depotString);
		this.name = parseDepotName(depotString);
		this.rootUrl = parseRootUrl(depotString);
		checkParts();
	}
	
	private TelosysToolsException invalidDepotError(String depotString, String error) {
		return new TelosysToolsException(INVALID_DEPOT + " '" + depotString + "' - " + error);
	}
	private String parseDepotType(String depotString) throws TelosysToolsException {
		String depotWithoutRootUrl = StrUtil.removeFrom(depotString, '(');
		String[] parts = depotWithoutRootUrl.split(":");
		if (parts.length > 0  ) {
			return parts[0].trim() ;
		} 
		else {
			throw invalidDepotError(depotString, "bad syntax");
		}
	}
	private String parseDepotName(String depotString) {
		String depotWithoutRootUrl = StrUtil.removeFrom(depotString, '(');
		String[] parts = depotWithoutRootUrl.split(":");
		if (parts.length > 1  ) {
			return parts[1].trim() ;
		} 
		else {
			return "";
		}
	}
	private String parseRootUrl(String depotString) throws TelosysToolsException {
        int start = depotString.indexOf('(');
        if ( start >= 0 ) {
            int end = depotString.indexOf(')', start);
            if ( end > start ) {
            	return depotString.substring(start + 1, end).trim();
            }
            else {
            	throw invalidDepotError(depotString, "closing ')' expected");
            }
        }
        return "";
	}
	
	private void checkParts() throws TelosysToolsException {
		if ( ! VALID_TYPES.contains(type) ) {
			throw invalidDepotError(definition, "invalid type");
		}
		if ( ! GITHUB_CURRENT_USER.equals(type) && name.isEmpty() ) {
			throw invalidDepotError(definition, "name required");
		}
	}

	/**
	 * Returns the original depot deifinition 
	 * @return
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Returns the depot type (github_org, github_user, etc)
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the depot name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the depot root URL (https://myhost:9090, etc ) 
	 * @return
	 */
	public String getRootURL() {
		return rootUrl;
	}
	
	/**
	 * Returns the URL to be used for API requests 
	 * @return
	 * @throws TelosysToolsException
	 */
	public String getApiUrl() throws TelosysToolsException {
		if ( isGitHubDepot() ) {
			return buildGitHubURL( this.rootUrl.isEmpty() ? GITHUB_DEFAULT_ROOT_URL : this.rootUrl );
		}
		else if ( isGitLabDepot() ) {
			return buildGitLabURL();
		}
		else {
			throw new TelosysToolsException("Cannot build depot URL (unexpected depot type)");
		}
	}
	private String buildGitHubURL(String root) throws TelosysToolsException {
		if ( this.isGitHubOrganization() ) {
			// example: https://api.github.com/orgs/telosys-models/repos  
			return root + "/orgs/" + getName() + "/repos" ; 
		}
		else if ( this.isGitHubUser()) {
			// example: https://api.github.com/users/telosys-models/repos  
			return root + "/users/" + getName() + "/repos" ; 
		}
		else if ( this.isGitHubCurrentUser() ) {
			// fixed URL
			return root + "/user/repos";
		}
		else {
			throw new TelosysToolsException("Cannot build GitHub depot URL (invalid depot type)");
		}		
	}
	private String buildGitLabURL() throws TelosysToolsException {
		throw new TelosysToolsException("Cannot build depot URL - GitLab not yet supported");
	}

	public String getApiRateLimitUrl() throws TelosysToolsException {
		if ( isGitHubDepot() ) {
			String root = this.rootUrl.isEmpty() ? GITHUB_DEFAULT_ROOT_URL : this.rootUrl ;
			return root + "/rate_limit" ;
		}
		else if ( isGitLabDepot() ) {
			throw new TelosysToolsException("Cannot build rate limit URL - GitLab not yet supported");
		}
		else {
			throw new TelosysToolsException("Cannot build rate limit URL (unexpected depot type)");
		}
	}

	public boolean isGitHubDepot() {
		return type.startsWith("github_");
	}
	public boolean isGitHubOrganization() {
		return GITHUB_ORG.equals(type);
	}
	public boolean isGitHubUser() {
		return GITHUB_USER.equals(type);
	}
	public boolean isGitHubCurrentUser() {
		return GITHUB_CURRENT_USER.equals(type);
	}
	
	public boolean isGitLabDepot() {
		return type.startsWith("gitlab_");
	}
	public boolean isGitLabGroup() {
		return GITLAB_GROUP.equals(type);
	}
	public boolean isGitLabUser() {
		return GITLAB_USER.equals(type);
	}

	@Override
	public String toString() {
		return definition;
	}
	
}
