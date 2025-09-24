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
package org.telosys.tools.commons.credentials;

import java.io.File;

import org.telosys.tools.commons.TelosysToolsException;

public class GitCredentialsManager {

	private final GitCredentialsDAO gitCredentialsDAO ;
	
	/**
	 * Default constructor (to use the standard YAML file)
	 */
	public GitCredentialsManager() {
		super();
		this.gitCredentialsDAO = new GitCredentialsDAO();
	}

	/**
	 * Constructor to use a specific directory (for tests only)
	 * @param specificDirectory
	 */
	protected GitCredentialsManager(File specificDirectory) {
		super();
		this.gitCredentialsDAO = new GitCredentialsDAO(specificDirectory);
	}

	/**
	 * Set the given credentials for the given scope (load, update and save the credentials in a file) 
	 * @param scope
	 * @param credentials
	 * @throws TelosysToolsException
	 */
	public final void setCredentials(GitCredentialsScope scope, GitCredentials credentials) throws TelosysToolsException {
		GitCredentialsHolder holder = gitCredentialsDAO.load();
		// holder is never null (empty holder if no YAML file)
		holder.setCredentials(scope, credentials);
		gitCredentialsDAO.save(holder);
	}
	
	/**
	 * Get the credentials for the given scope (load the credentials from a file)
	 * @param scope
	 * @return
	 * @throws TelosysToolsException
	 */
	public final GitCredentials getCredentials(GitCredentialsScope scope) throws TelosysToolsException {
		GitCredentialsHolder holder = gitCredentialsDAO.load();
		return holder.getCredentials(scope);
	}

	/**
	 * Removes the credentials for the given scope (load, remove and save the credentials in a file)
	 * @param scope
	 * @return true if found and removed, false if not found
	 * @throws TelosysToolsException
	 */
	public final boolean removeCredentials(GitCredentialsScope scope) throws TelosysToolsException {
		GitCredentialsHolder holder = gitCredentialsDAO.load();
		// holder is never null (empty holder if no YAML file)
		if ( holder.removeCredentials(scope) ) {
			// removed => save
			gitCredentialsDAO.save(holder);
			return true;
		}
		else {
			return false;
		}
	}

	public final GitCredentials searchUsableCredentials(GitCredentialsScope scope) throws TelosysToolsException {
		GitCredentialsHolder holder = gitCredentialsDAO.load();
		// Search first at specific scope level (models/bundles)
		GitCredentials credentials = holder.getCredentials(scope);
		if ( credentials != null ) {
			return credentials;
		}
		else {
			// Nothing at specific scope level => search at 'global' scope level 
			return holder.getCredentials(GitCredentialsScope.GLOBAL);
		}
	}
}
