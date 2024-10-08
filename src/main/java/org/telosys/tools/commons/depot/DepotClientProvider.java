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
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.github.GitHubClient;

/**
 * @author L. Guerin
 *
 */
public class DepotClientProvider {

	private DepotClientProvider(){
	}
	
	/**
	 * Return an instance of DepotClient ( GitHubClient or other) for the given Depot
	 * @param depot
	 * @param telosysToolsCfg
	 * @return
	 * @throws TelosysToolsException
	 */
	public static DepotClient getDepotClient(Depot depot, TelosysToolsCfg telosysToolsCfg) throws TelosysToolsException {
		if (depot == null) {
			throw new TelosysToolsException("getDepotClient: depot arg is null");
		}
		if (depot.isGitHubDepot()) {
			return new GitHubClient(telosysToolsCfg.getCfgFileAbsolutePath());
		}
		else if (depot.isGitLabDepot()) {
			throw new TelosysToolsException("GitLab depot is not yet supported");
		}
		else {
			throw new TelosysToolsException("getDepotClient: unknown depot type");
		}
	}

}
