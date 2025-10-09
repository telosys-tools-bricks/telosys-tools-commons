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
import org.telosys.tools.commons.exception.TelosysRuntimeException;

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
	private final String namespace;
	private final String specificRootUrl;  // specific root URL 
	private final String apiRootUrl;       // API root URL 

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
		this.type = parseType(depotDefinition);
		this.namespace = parseNamespace(depotDefinition);
		this.specificRootUrl = parseSpecificRootUrl(depotDefinition);
		this.apiRootUrl = this.specificRootUrl ; // temporary ( in the future add "(api-root-url)" after "(root-url)" in depot definition ? )
		checkParts();
	}
	
	private TelosysToolsException invalidDepotError(String depotString, String error) {
		return new TelosysToolsException(INVALID_DEPOT + " '" + depotString + "' - " + error);
	}
	private String parseType(String depotString) throws TelosysToolsException {
		// left part before ":"  
		String depotWithoutRootUrl = StrUtil.removeFrom(depotString, '(');
		String[] parts = depotWithoutRootUrl.split(":");
		if (parts.length > 0  ) {
			return parts[0].trim() ;
		} 
		else {
			throw invalidDepotError(depotString, "bad syntax");
		}
	}
	private String parseNamespace(String depotString) {
		// part after ":" and before "(" if any 
		String depotWithoutRootUrl = StrUtil.removeFrom(depotString, '(');
		String[] parts = depotWithoutRootUrl.split(":");
		if (parts.length > 1  ) {
			return parts[1].trim() ;
		} 
		else {
			return "";
		}
	}
	private String parseSpecificRootUrl(String depotString) throws TelosysToolsException {
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
		if ( ! GITHUB_CURRENT_USER.equals(type) && namespace.isEmpty() ) {
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
	 * Returns the depot namespace, the part after the root URL (user, organization, owner, group, subgroup )
	 * @return
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Returns the specific depot root URL (https://myhost:9090, etc ) 
	 * @return
	 */
	public String getSpecificRootURL() {
		return specificRootUrl;
	}
	
	/**
	 * Returns the depot root URL (eg "https://github.com", "https://gitlab.com",  or specific root URL ) 
	 * @return
	 */
	public String getRootURL() {
		if ( this.isGitHubDepot() ) {
			// With GitHub the URL structure is the same for "user" and "organization" 
			//  - HTTPS:  https://github.com/<owner>/<repo>.git
			//  - SSH:    git@github.com:<owner>/<repo>.git
			// where <owner> = either a username (for personal repos) or an organization name
			// example: https://github.com/telosys-models
			return getGitHubRootUrl() ; 
		}
		else if ( this.isGitLabDepot() ) {
			// On GitLab, the pattern is "https://gitlab.com/<namespace>/<repository>"
			// where <namespace> = either 
			//  . a user (personal repo), 
			//  . a group (equivalent of a GitHub organization) or 
			//  . a subgroup (group inside group)
			return getGitLabRootUrl() ; 
		}
		else {
			throw new TelosysRuntimeException("Cannot get root URL (invalid depot type)");
		}		
	}
	
	/**
	 * Returns the depot base URL : {root-URL} + "/" + {namespace} 
	 * (eg "https://github.com/mybundles", "https://gitlab.com/group/subgroup" ) 
	 * @return
	 */
	public String getBaseURL() {
		return getRootURL() + "/" + getNamespace() ; 
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
			return root + "/orgs/" + getNamespace() + "/repos?" + GITHUB_PER_PAGE ; 
		}
		else if ( this.isGitHubUser()) {
			// example: https://api.github.com/users/telosys-models/repos  
			return root + "/users/" + getNamespace() + "/repos?" + GITHUB_PER_PAGE ; 
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
		return this.specificRootUrl.isEmpty() ? GITHUB_DEFAULT_ROOT_URL : this.specificRootUrl ;
	}
	protected String getGitLabRootUrl() { // v 4.3.0
		return this.specificRootUrl.isEmpty() ? GITLAB_DEFAULT_ROOT_URL : this.specificRootUrl ;
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
			return getGitHubRootUrl() + "/" + getNamespace() + "/" + repositoryName + ".git" ; 
			// ".git" suffix is optional (can be used or not) 
			// ".git" suffix is used by "git clone" => use it too
		}
		else if ( this.isGitLabDepot() ) {
			// On GitLab, the pattern is "https://gitlab.com/<namespace>/<repository>"
			// where <namespace> = either 
			//  . a user (personal repo), 
			//  . a group (equivalent of a GitHub organization) or 
			//  . a subgroup (group inside group)
			return getGitLabRootUrl() + "/" + getNamespace() + "/" + repositoryName + ".git" ; 
			// ".git" suffix is optional (can be used or not) 
			// ".git" suffix is used by "git clone" => use it too
		}
		else {
			throw new TelosysToolsException("Cannot build Git repository URL (invalid depot type)");
		}		
	}
	
}
