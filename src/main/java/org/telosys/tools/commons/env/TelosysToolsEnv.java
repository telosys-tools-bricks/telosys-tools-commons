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
package org.telosys.tools.commons.env;


/**
 * Telosys Tools standard environment. <br>
 * Singleton. <br>
 * Immutable files names and folders names <br>
 *   
 * @author Laurent GUERIN
 *
 */
public class TelosysToolsEnv 
{
	//==============================================================================
    // SINGLETON MANAGEMENT 
    //==============================================================================
	private static final TelosysToolsEnv instance = new TelosysToolsEnv();
	
	public static final TelosysToolsEnv getInstance() {
		return instance ;
	}
	
	//==============================================================================
    // INSTANCE 
    //==============================================================================
	
	private static final String TELOSYS_TOOLS_FOLDER = "TelosysTools" ;
	
	private static final String MODELS_FOLDER        = TELOSYS_TOOLS_FOLDER + "/models" ;  // v 3.4.0
	private static final String TEMPLATES_FOLDER     = TELOSYS_TOOLS_FOLDER + "/templates" ; 
	private static final String DOWNLOADS_FOLDER     = TELOSYS_TOOLS_FOLDER + "/downloads" ; 
	private static final String LIBRARIES_FOLDER     = TELOSYS_TOOLS_FOLDER + "/lib" ; 
	
	private static final String TELOSYS_TOOLS_CFG_FILE_NAME = "telosys-tools.cfg" ;
	private static final String TELOSYS_TOOLS_CFG_FILE_PATH = TELOSYS_TOOLS_FOLDER + "/telosys-tools.cfg" ;
	
	private static final String DATABASES_CONFIG_FILE_NAME   = "databases.yaml"; // v 3.4.0
	private static final String DATABASES_CONFIG_FILE_PATH   = TELOSYS_TOOLS_FOLDER + "/databases.yaml"; // v 3.4.0
	
	//----------------------------------------------------------------------------------------
    /**
     * Constructor 
     */
    private TelosysToolsEnv ()  {
    }
    
	//==============================================================================
    // FILES 
    //==============================================================================
	/**
	 * Returns the TelosysTools configuration file name<br>
	 * ( e.g. 'telosys-tools.cfg' )
	 * @return
	 */
    public String getTelosysToolsConfigFileName() {
		return TELOSYS_TOOLS_CFG_FILE_NAME ;
	}

	/**
	 * Returns the TelosysTools configuration file path in the current project (relative path in the project) <br>
	 * ( e.g. 'TelosysTools/telosys-tools.cfg' )
	 * @return
	 */
//  v 3.0.0
    public String getTelosysToolsConfigFilePath() {
		return TELOSYS_TOOLS_CFG_FILE_PATH ;
	}

    /**
     * Returns the "databases.yaml" file name 
     * @return
     */
    public String getDatabasesDbCfgFileName() {
    	return DATABASES_CONFIG_FILE_NAME ;
	}
    
    /**
     * Returns the "databases.yaml" file path in the current project (relative path in the project) <br>
     * ( e.g. 'TelosysTools/databases.yaml' )
     * @return
     */
    public String getDatabasesDbCfgFilePath() {
    	return DATABASES_CONFIG_FILE_PATH ;
	}
    
	//==============================================================================
    // FOLDERS  
    //==============================================================================
	/**
	 * Returns the TelosysTools folder name in the current project (relative path in the project) <br>
	 * ( e.g. 'TelosysTools' )
	 * @return
	 */
    public String getTelosysToolsFolder() {
		return TELOSYS_TOOLS_FOLDER ;
	}
    
    //------------------------------------------------------------------------------------------------------
    /**
     * Returns the templates folder in the current project (relative path in the project) <br>
     * ( e.g. 'TelosysTools/templates' )
     * @return
     */
    public String getTemplatesFolder() {
    	return TEMPLATES_FOLDER ;
	}
    //------------------------------------------------------------------------------------------------------
    /**
     * Returns the downloads folder in the current project (relative path in the project) <br>
     * ( e.g. 'TelosysTools/downloads' )
     * @return
     */
    public String getDownloadsFolder() {
    	return DOWNLOADS_FOLDER ;
    }
    //------------------------------------------------------------------------------------------------------
    /**
     * Returns the libraries folder in the current project (relative path in the project) <br>
     * ( e.g. 'TelosysTools/lib' )
     * @return
     */
    public String getLibrariesFolder() {
    	return LIBRARIES_FOLDER ;
    }
    //------------------------------------------------------------------------------------------------------
    /**
     * Returns the models folder in the current project (relative path in the project) <br>
     * ( e.g. 'TelosysTool' until v 3.3.0, 'TelosysTools/models' since 3.4.0 )
     * @return
     */
    public String getModelsFolder()
	{
    	return MODELS_FOLDER ;
	}
}
