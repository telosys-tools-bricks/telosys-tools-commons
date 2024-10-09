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

import org.telosys.tools.commons.TelosysToolsException;

/**
 * @author L. Guerin
 *
 */
public class Depot {

	private static final String INVALID_DEPOT = "Invalid depot"; 
	
	// GitHub 
	private static final String GITHUB_ORG          = "github_org"; 
	private static final String GITHUB_USER         = "github_user"; 
	private static final String GITHUB_CURRENT_USER = "github_current_user"; 

	// GitLab 
	private static final String GITLAB_GROUP  = "gitlab_group"; // GitLab "group" or "subgroup" 
	private static final String GITLAB_USER   = "gitlab_user"; 
	
	private static final List<String> VALID_TYPES = Arrays.asList(GITHUB_ORG,   GITHUB_USER, GITHUB_CURRENT_USER,  
																  GITLAB_GROUP, GITLAB_USER );
	
	private final String definition;
	private final String type;
	private final String name;

	public Depot(String depot) throws TelosysToolsException {
		super();
		if (depot == null) {
			throw new TelosysToolsException(INVALID_DEPOT + " depot is null");
		}
		this.definition = depot;
		// parse depot definition 
		String[] parts = depot.split(":");
		if (parts.length == 1 ) {
			this.type = parts[0].trim() ;
			this.name = "";
		}
		else if ( parts.length == 2 ) {
			this.type = parts[0].trim() ;
			this.name = parts[1].trim() ;
		}
		else {
			throw new TelosysToolsException(INVALID_DEPOT + " '" + depot + "' (bad syntax)");
		}
		checkParts();
	}
	
	private void checkParts() throws TelosysToolsException {
		if ( ! VALID_TYPES.contains(type) ) {
			throw new TelosysToolsException(INVALID_DEPOT + " '" + definition + "', invalid type");
		}
		if ( ! GITHUB_CURRENT_USER.equals(type) && name.isEmpty() ) {
			throw new TelosysToolsException(INVALID_DEPOT + " '" + definition + "', name required");
		}
	}

	public String getDefinition() {
		return definition;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
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
