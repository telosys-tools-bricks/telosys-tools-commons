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

import org.telosys.tools.commons.TelosysToolsException;

public class GitCredentialsManager {

	private static final GitCredentialsDAO gitCredentialsDAO = new GitCredentialsDAO();
	
	private GitCredentialsManager() {
	}

	public static final void setCredentials( GitCredentialsScope scope, GitCredentials credentials ) throws TelosysToolsException {
		GitCredentialsHolder holder = gitCredentialsDAO.load();
		holder.setCredentials(scope, credentials);
		gitCredentialsDAO.save(holder);
	}
	
	public static final GitCredentials getCredentials( GitCredentialsScope scope ) throws TelosysToolsException {
		GitCredentialsHolder holder = gitCredentialsDAO.load();
		return holder.getCredentials(scope);
	}

	public static final void removeCredentials( GitCredentialsScope scope ) throws TelosysToolsException {
		GitCredentialsHolder holder = gitCredentialsDAO.load();
		holder.removeCredentials(scope);
		gitCredentialsDAO.save(holder);
	}
}
