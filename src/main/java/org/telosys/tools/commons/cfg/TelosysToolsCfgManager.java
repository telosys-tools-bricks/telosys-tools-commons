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
package org.telosys.tools.commons.cfg;

import java.util.Properties;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.PropertiesManager;
import org.telosys.tools.commons.env.TelosysToolsEnv;

/**
 * Projects configuration manager <br>
 * . save and load the projects configuration<br>
 * 
 */
public class TelosysToolsCfgManager {

    private final String projectAbsolutePath ;
    private final String cfgFileAbsolutePath ;
    
	/**
	 * Constructor
	 * @param projectAbsolutePath the absolute path of the current project (project's directory)
	 */
	public TelosysToolsCfgManager(String projectAbsolutePath) {
		super();
		if ( projectAbsolutePath == null ) {
			throw new IllegalArgumentException("Project path is null");
		}
		// 'telosys-tools.cfg' file path in the project
		String telosysToolsCfgFilePathInProject = TelosysToolsEnv.getTelosysToolsConfigFilePath(); // v 3.0.0
		
		this.projectAbsolutePath = projectAbsolutePath;
		this.cfgFileAbsolutePath = FileUtil.buildFilePath(projectAbsolutePath, telosysToolsCfgFilePathInProject);
	}
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Returns the absolute path of the project directory
	 * @return
	 */
	public String getProjectAbsolutePath() {
		return this.projectAbsolutePath ; 
	}
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Returns the absolute path of the configuration file for the current project
	 * @return
	 */
	public String getCfgFileAbsolutePath() {
		return this.cfgFileAbsolutePath ; 
	}
	
	//-------------------------------------------------------------------------------------------------
	/**
	 * Loads a TelosysToolsCfg instance initialized from the 'telosys-tools.cfg' file <br>
	 * If the properties file doesn't exist the configuration contains the default values
	 * @return the configuration (never null, use default values if the doesn't exist) 
	 */
	public TelosysToolsCfg loadTelosysToolsCfg() {
		PropertiesManager propManager = new PropertiesManager( this.cfgFileAbsolutePath ) ;
		Properties prop = propManager.load(); // Ret NULL if file not found
		if ( prop != null ) {
			return new TelosysToolsCfg(this.projectAbsolutePath, this.cfgFileAbsolutePath, prop );
		}
		else {
			// Properties file not found, no properties loaded : use default values
			return new TelosysToolsCfg(this.projectAbsolutePath, this.cfgFileAbsolutePath, null );
		}
	}
}
