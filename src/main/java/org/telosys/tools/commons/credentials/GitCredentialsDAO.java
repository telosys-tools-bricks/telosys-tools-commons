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
import org.telosys.tools.commons.YamlFileManager;

public class GitCredentialsDAO {

	private static final String GIT_CREDENTIALS_FILE_NAME = "git_user";
			
	private YamlFileManager getYamlFileManager() throws TelosysToolsException {
		File file = UserHomeUtil.getFileInTelosysDir(GIT_CREDENTIALS_FILE_NAME);
		return new YamlFileManager(file);
	}
	
	public GitCredentialsHolder load() throws TelosysToolsException {
		YamlFileManager yamlFileManager = getYamlFileManager(); 
		return new GitCredentialsHolder( yamlFileManager.loadMap() );
	}
	
	public void save(GitCredentialsHolder gitCredentials) throws TelosysToolsException {
		YamlFileManager yamlFileManager = getYamlFileManager();
		yamlFileManager.saveMap(gitCredentials.getMap());
	}
	
}
