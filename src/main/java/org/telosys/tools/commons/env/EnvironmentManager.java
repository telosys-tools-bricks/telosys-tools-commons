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

import java.io.File;
import java.util.List;

import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.commons.variables.Variable;

/**
 * Telosys Tools environment manager <br>
 * For environment initialization (folders creation, files initialization, etc) <br>
 * All the methods are designed to log the actions result in a StringBuffer.
 * 
 * @author Laurent GUERIN
 *
 */
public class EnvironmentManager {
	
	private final TelosysToolsEnv telosysToolsEnv ;
	private final String          environmentDirectory ; // project folder full path
	
	//-----------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param environmentDirectory the directory where the environment is located (project folder OS full path)
	 */
	public EnvironmentManager(String environmentDirectory) {
		super();
		this.environmentDirectory = environmentDirectory;
		this.telosysToolsEnv = TelosysToolsEnv.getInstance();
	}
	//-----------------------------------------------------------------------------------------------------
	/**
	 * Checks if the Telosys Tools environment is initialized <br>
	 * Check "TelosysTools" folder existence and "telosys-tools.cfg" file existence 
	 * @return
	 */
	public boolean isEnvironmentInitialized() {
		//--- Check "TelosysTools" folder existence
		File telosysToolsFolder = new File( getTelosysToolsFolderFullPath() ) ; 
		if ( ! telosysToolsFolder.exists() ) {
			return false ;
		}
		if ( ! telosysToolsFolder.isDirectory() ) {
			return false ;
		}
		//--- Check "telosys-tools.cfg" file existence
		File telosysToolsCfgFile = new File( getTelosysToolsConfigFileFullPath() ) ; 
		if ( ! telosysToolsCfgFile.exists() ) {
			return false ;
		}
		if ( ! telosysToolsCfgFile.isFile() ) {
			return false ;
		}
		//--- OK, both exist
		return true ;
	}
	//-----------------------------------------------------------------------------------------------------
	/**
	 * Initializes the environment using the standard default folders and configuration files
	 * @param sb
	 */
	public void initEnvironment(StringBuilder sb) {
		initEnvironment(sb, null);
	}
	
	/**
	 * Initializes the environment using the standard default folders and configuration files <br>
	 * Initializes the given variables in the 'cfg' file (if any)
	 * @param sb
	 * @param variables list of specific variables to set in the '.cfg' file (can be null if none)
	 */
	public void initEnvironment(StringBuilder sb, List<Variable> variables) {
		//--- Create folders
		createFolder( telosysToolsEnv.getTelosysToolsFolder(), sb );
		createFolder( telosysToolsEnv.getDownloadsFolder(), sb );
		createFolder( telosysToolsEnv.getLibrariesFolder(), sb );
		createFolder( telosysToolsEnv.getTemplatesFolder(), sb );
		createFolder( telosysToolsEnv.getModelsFolder(), sb ); // v 3.4.0
		//--- Init 'databases.yaml' file
		initDatabasesConfigFile(sb);
		//--- Init 'telosys-tools.cfg' file
		try {
			initTelosysToolsConfigFile(sb, variables);
		} catch (TelosysToolsException e) {
			throw new RuntimeException("Cannot init 'cfg' file ", e);
		}
	}
	
	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Returns the environment directory (OS full path)
	 * @return
	 */
	protected String getEnvironmentFolderFullPath() {
		return environmentDirectory;
	}

	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Returns the TelosysTools configuration file path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools/telosys-tools.cfg' )
	 * @return
	 */
	protected String getTelosysToolsConfigFileFullPath() {
		return FileUtil.buildFilePath(environmentDirectory, telosysToolsEnv.getTelosysToolsConfigFilePath()) ; 
	}

	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Returns the 'databases.yaml' full path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools/databases.yaml' )
	 * @return
	 */
	protected String getDatabasesDbCfgFullPath() {
		return FileUtil.buildFilePath(environmentDirectory, telosysToolsEnv.getDatabasesDbCfgFilePath()) ;
	}

	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Returns the TelosysTools folder full path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools' )
	 * @return
	 */
	protected String getTelosysToolsFolderFullPath() {
		return FileUtil.buildFilePath(environmentDirectory, telosysToolsEnv.getTelosysToolsFolder()) ;
	}

	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Creates a folder in the environment folder
	 * @param folderToBeCreated 
	 * @param sb
	 */
	protected void createFolder(String folderToBeCreated, StringBuilder sb) {

		if ( ! StrUtil.nullOrVoid(folderToBeCreated) )  {
			folderToBeCreated = folderToBeCreated.trim() ;
			String newDirPath = FileUtil.buildFilePath(environmentDirectory, folderToBeCreated);
			File newDir = new File(newDirPath) ; 
			if ( newDir.exists() ) {
				sb.append(". folder '" + folderToBeCreated + "' exists (not created)");
			}
			else {
				DirUtil.createDirectory( newDir ); 
				sb.append(". folder '" + folderToBeCreated + "' created");
			}
		}
		sb.append("\n");
	}	
	
	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Initializes the Telosys Tools configuration file <br>
	 * Copy the default configuration file in the environment folder <br>
	 * If the destination file already exists it is not copied <br>
	 * Initializes the given variables in the file if any 
	 * @param sb
	 * @param variables list of specific variables to set in the '.cfg' file (can be null if none)
	 */
	protected void initTelosysToolsConfigFile( StringBuilder sb, List<Variable> variables ) throws TelosysToolsException {
		//--- Initialize the file (from META-INF) in the project environment
		initFileFromMetaInf(telosysToolsEnv.getTelosysToolsConfigFileName(), getTelosysToolsConfigFileFullPath(), sb );
		//--- Set specific variables if any
		if ( ( variables != null ) && ( ! variables.isEmpty() ) ) {
			TelosysToolsCfgManager telosysToolsCfgManager = new TelosysToolsCfgManager(environmentDirectory);
			//--- Load the configuration
			TelosysToolsCfg telosysToolsCfg = telosysToolsCfgManager.loadTelosysToolsCfg();
			//--- Set the given specific variables values
			for ( Variable var : variables ) {
				telosysToolsCfg.setSpecificVariable(var);
			}
			//--- Save the configuration
			telosysToolsCfgManager.saveTelosysToolsCfg(telosysToolsCfg);
		}
	}
	
	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Initializes the Telosys Tools databases configuration file <br>
	 * Copy the default databases configuration file in the environment folder <br>
	 * If the destination file already exists it is not copied 
	 * @param sb
	 */
	protected void initDatabasesConfigFile( StringBuilder sb ) {
		initFileFromMetaInf(telosysToolsEnv.getDatabasesDbCfgFileName(), getDatabasesDbCfgFullPath(), sb );
	}
	
	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Initializes a Telosys Tools configuration file by copying a file from 'META-INF'
	 * @param shortFileName  the file name in 'META-INF/files' ( eg 'telosys-tools.cfg' )
	 * @param destinationFullPath  the destination file full path 
	 * @param sb
	 */
	private void initFileFromMetaInf(String shortFileName, String destinationFullPath, StringBuilder sb ) {
		//--- File path inside "META-INF" folder
		String filePathInMetaInf = FileUtil.buildFilePath("/files/", shortFileName) ; 
		try {
			boolean copied = FileUtil.copyFileFromMetaInfIfNotExist( filePathInMetaInf, destinationFullPath, true);
			if (copied) {
				sb.append(". file '" + shortFileName + "' created. \n");
			}
			else {
				sb.append(". file '" + shortFileName + "' already exists (not created) \n");
			}
		} catch (Exception e) {
			sb.append("ERROR : cannot copy file '" + shortFileName + "' \n");
			sb.append("EXCEPTION : " + e.getClass().getSimpleName() + " : " + e.getMessage() + " \n");
		}
	}
}
