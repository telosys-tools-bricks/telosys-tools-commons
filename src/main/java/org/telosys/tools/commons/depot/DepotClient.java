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

import org.telosys.tools.commons.TelosysToolsException;

public interface DepotClient {

	/**
	 * Get all repositories available in the given depot
	 * @param depot
	 * @return
	 * @throws TelosysToolsException
	 */
	DepotResponse getRepositories(Depot depot) throws TelosysToolsException ;
	
	/**
	 * Download a repository from the given depot 
	 * @param depot
	 * @param repoName repository to download 
	 * @param branch   repository branch to download
	 * @param downloadedFile
	 * @return
	 * @throws TelosysToolsException
	 */
	long downloadRepositoryBranch(Depot depot, String repoName, String branch, String downloadedFile) throws TelosysToolsException;

}
