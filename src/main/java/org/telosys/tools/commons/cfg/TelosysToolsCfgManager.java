/**
 *  Copyright (C) 2008-2014  Telosys project org. ( http://www.telosys.org/ )
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
import org.telosys.tools.commons.TelosysToolsException;

/**
 * Projects configuration manager <br>
 * . save and load the projects configuration<br>
 * . hold each project configuration in a cache<br>
 */
public class TelosysToolsCfgManager {

    private final static String TELOSYS_TOOLS_CFG_FILE = "telosys-tools.cfg";

    private final String projectAbsolutePath ;
    private final String cfgFileAbsolutePath ;
    
	//-------------------------------------------------------------------------------------------------
	public static String getConfigFileName() 
	{
		return TELOSYS_TOOLS_CFG_FILE ;
	}

	//-------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param projectAbsolutePath the absolute path of the current project (project's directory)
	 */
	public TelosysToolsCfgManager(String projectAbsolutePath) {
		super();
		if ( projectAbsolutePath == null ) {
			throw new IllegalArgumentException("Project path is null");
		}		
		this.projectAbsolutePath = projectAbsolutePath;
		this.cfgFileAbsolutePath = FileUtil.buildFilePath(projectAbsolutePath, TELOSYS_TOOLS_CFG_FILE);
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
	 * Loads the project's configuration from the properties file  <br>
	 * If the properties file doesn't exist the configuration contains the default values
	 * @param project
	 * @return the configuration (never null, use default values if the doesn't exist) 
	 */
	public TelosysToolsCfg loadProjectConfig() throws TelosysToolsException
	{
		//PluginLogger.log("ProjectConfigManager.loadProjectConfig(p)..." );

		PropertiesManager propManager = new PropertiesManager( this.cfgFileAbsolutePath ) ;
		Properties prop = propManager.load(); // Ret NULL if file not found
		if ( prop != null )
		{
			TelosysToolsCfg projectConfig = new TelosysToolsCfg(this.projectAbsolutePath, this.cfgFileAbsolutePath, prop );
			return projectConfig ;
		}
		else
		{
			// Properties file not found, no properties loaded : use default values
			TelosysToolsCfg projectConfig = new TelosysToolsCfg(this.projectAbsolutePath, this.cfgFileAbsolutePath, null );
			return projectConfig ;
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public void saveProjectConfig( TelosysToolsCfg telosysToolsCfg ) throws TelosysToolsException
	{
		PropertiesManager propManager = new PropertiesManager( this.cfgFileAbsolutePath ) ;
		// Save the configuration as a set of properties
		propManager.save( telosysToolsCfg.getProperties() );
	}
//	//-------------------------------------------------------------------------------------------------
//	public void saveProjectConfig( Properties prop ) throws TelosysToolsException
//	{
//		//PluginLogger.log("ProjectConfigManager.saveProjectConfig(project, prop)..." );
//		PropertiesManager propManager = new PropertiesManager( this.cfgFileAbsolutePath ) ;
//		propManager.save(prop);
//	}
	
}
