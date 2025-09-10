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
	
	//--- GitHub 
	private static final String GITHUB_ORG          = "github_org";  // GitHub "organization" type
	private static final String GITHUB_USER         = "github_user"; // GitHub "user" type
	private static final String GITHUB_CURRENT_USER = "github_current_user"; //  GitHub "current user" type
	private static final String GITHUB_DEFAULT_ROOT_URL     = "https://github.com" ; 
	private static final String GITHUB_DEFAULT_API_ROOT_URL = "https://api.github.com" ;
	private static final String GITHUB_PER_PAGE             = "per_page=100" ;

	//--- GitLab 
	private static final String GITLAB_GROUP  = "gitlab_group"; // GitLab "group" or "subgroup" type
	private static final String GITLAB_USER   = "gitlab_user";  // GitLab "user" type
	private static final String GITLAB_DEFAULT_ROOT_URL     = "https://gitlab.com" ; 

	private static final List<String> VALID_TYPES = Arrays.asList(GITHUB_ORG,   GITHUB_USER, GITHUB_CURRENT_USER,  
																  GITLAB_GROUP, GITLAB_USER );
	
	private final String definition;
	private final String type;
	private final String name;
	private final String rootUrl;    // specific root URL 
	private final String apiRootUrl; // specific API root URL 

	/**
	 * Constructor
	 * @param depotDefinition depot syntax "type:name (root-url)" (eg "github_org:telosys-templates" ) 
	 * @throws TelosysToolsException
	 */
	public Depot(String depotDefinition) throws TelosysToolsException {
		super();
		if (depotDefinition == null) {
			throw new TelosysToolsException("Depot argument is null");
		}
		this.definition = depotDefinition;
		// parse depot definition 
		this.type = parseDepotType(depotDefinition);
		this.name = parseDepotName(depotDefinition);
		this.rootUrl = parseRootUrl(depotDefinition);
		this.apiRootUrl = this.rootUrl ; // temporary ( in the future add "(api-root-url)" after "(root-url)" in depot definition ? )
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
            	String s = depotString.substring(start + 1, end).trim();
            	// no trailing "/" at the end 
            	return StrUtil.removeEnd(s, "/");
            }
            else {
            	throw invalidDepotError(depotString, "closing ')' expected");
            }
        }
        else {
            return "";
        }
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
			return buildGitHubApiURL( getGitHubApiRootUrl() );
		}
		else if ( isGitLabDepot() ) {
			return buildGitLabApiURL();
		}
		else {
			throw new TelosysToolsException("Cannot build depot URL (unexpected depot type)");
		}
	}
	private String buildGitHubApiURL(String root) throws TelosysToolsException {
		if ( this.isGitHubOrganization() ) {
			// example: https://api.github.com/orgs/telosys-models/repos  
			return root + "/orgs/" + getName() + "/repos?" + GITHUB_PER_PAGE ; 
		}
		else if ( this.isGitHubUser()) {
			// example: https://api.github.com/users/telosys-models/repos  
			return root + "/users/" + getName() + "/repos?" + GITHUB_PER_PAGE ; 
		}
		else if ( this.isGitHubCurrentUser() ) {
			// fixed URL
			return root + "/user/repos?" + GITHUB_PER_PAGE ;
		}
		else {
			throw new TelosysToolsException("Cannot build GitHub depot URL (invalid depot type)");
		}		
	}
	private String buildGitLabApiURL() throws TelosysToolsException {
		throw new TelosysToolsException("Cannot build depot URL - GitLab not yet supported");
	}

	protected String getGitHubRootUrl() { // v 4.3.0
		return this.rootUrl.isEmpty() ? GITHUB_DEFAULT_ROOT_URL : this.rootUrl ;
	}
	protected String getGitLabRootUrl() { // v 4.3.0
		return this.rootUrl.isEmpty() ? GITLAB_DEFAULT_ROOT_URL : this.rootUrl ;
	}
	protected String getGitHubApiRootUrl() { // v 4.3.0
		return this.apiRootUrl.isEmpty() ? GITHUB_DEFAULT_API_ROOT_URL : this.apiRootUrl ;
	}
	
	public String getApiRateLimitUrl() throws TelosysToolsException {
		if ( isGitHubDepot() ) {
			return getGitHubApiRootUrl() + "/rate_limit" ;
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

	/**
	 * Builds the repository URL in the depot 
	 * @param repositoryName
	 * @return
	 * @throws TelosysToolsException
	 * @since 4.3.0
	 */
	public String buildGitRepositoryURL(String repositoryName) throws TelosysToolsException { // v 4.3.0
		if ( this.isGitHubDepot() ) {
			// With GitHub the URL structure is the same for "user" and "organization" 
			//  - HTTPS:  https://github.com/<owner>/<repo>.git
			//  - SSH:    git@github.com:<owner>/<repo>.git
			// where <owner> = either a username (for personal repos) or an organization name
			// example: https://github.com/telosys-models/cars 
			return getGitHubRootUrl() + "/" + getName() + "/" + repositoryName + ".git" ; 
			// ".git" suffix is optional (can be used or not) 
			// ".git" suffix is used by "git clone" => use it too
		}
		else if ( this.isGitLabDepot() ) {
			// On GitLab, the pattern is "https://gitlab.com/<namespace>/<repository>"
			// where <namespace> = either 
			//  . a user (personal repo), 
			//  . a group (equivalent of a GitHub organization) or 
			//  . a subgroup (group inside group)
			return getGitLabRootUrl() + "/" + getName() + "/" + repositoryName + ".git" ; 
			// ".git" suffix is optional (can be used or not) 
			// ".git" suffix is used by "git clone" => use it too
		}
		else {
			throw new TelosysToolsException("Cannot build Git repository URL (invalid depot type)");
		}		
	}
	
}
