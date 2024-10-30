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

import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;

/**
 * Telosys Tools environment manager <br>
 * For environment initialization (folders creation, files initialization, etc) <br>
 * All the methods are designed to log the actions result in a StringBuffer.
 * 
 * @author Laurent GUERIN
 *
 */
public class EnvironmentManager {
	
	private final String  environmentDirectory ; // project folder full path
	
	/**
	 * Constructor
	 * @param environmentDirectory the directory where the environment is located (project folder OS full path)
	 */
	public EnvironmentManager(String environmentDirectory) {
		super();
		this.environmentDirectory = environmentDirectory;
	}

	/**
	 * Checks if the Telosys Tools environment is initialized <br>
	 * Check "TelosysTools" folder existence and "telosys-tools.cfg" file existence 
	 * @return
	 */
	public boolean isEnvironmentInitialized() {
		//--- Check "TelosysTools" folder existence
		File telosysToolsFolder = new File( getTelosysToolsFolderFullPath() ) ; 
		if ( ! telosysToolsFolder.isDirectory() ) { // isDirectory() : true if exists and is a directory
			return false ;
		}
		//--- Check "telosys-tools.cfg" file existence
		File telosysToolsCfgFile = new File( getTelosysToolsConfigFileFullPath() ) ; 
		return telosysToolsCfgFile.isFile(); // isFile() : true if file exists and is a normal file
	}
	
	/**
	 * Initializes the environment using the standard default folders and configuration files
	 * @param sb
	 */
	public void initEnvironment(StringBuilder sb) {
		//--- Create folders
		createFolder( TelosysToolsEnv.getTelosysToolsFolder(), sb );
		createFolder( TelosysToolsEnv.getDownloadsFolder(), sb );
		createFolder( TelosysToolsEnv.getLibrariesFolder(), sb );
		createFolder( TelosysToolsEnv.getTemplatesFolder(), sb );
		createFolder( TelosysToolsEnv.getModelsFolder(), sb );
		//--- Init 'databases.yaml' file
		initDatabasesConfigFile(sb);
		//--- Init 'telosys-tools.cfg' file
		initTelosysToolsConfigFile(sb);
	}
	
	/**
	 * Returns the environment directory (OS full path)
	 * @return
	 */
	protected String getEnvironmentFolderFullPath() {
		return environmentDirectory;
	}

	/**
	 * Returns the TelosysTools configuration file path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools/telosys-tools.cfg' )
	 * @return
	 */
	protected String getTelosysToolsConfigFileFullPath() {
		return FileUtil.buildFilePath(environmentDirectory, TelosysToolsEnv.getTelosysToolsConfigFilePath()) ; 
	}

	/**
	 * Returns the 'databases.yaml' full path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools/databases.yaml' )
	 * @return
	 */
	protected String getDatabasesDbCfgFullPath() {
		return FileUtil.buildFilePath(environmentDirectory, TelosysToolsEnv.getDatabasesDbCfgFilePath()) ;
	}

	/**
	 * Returns the TelosysTools folder full path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools' )
	 * @return
	 */
	protected String getTelosysToolsFolderFullPath() {
		return FileUtil.buildFilePath(environmentDirectory, TelosysToolsEnv.getTelosysToolsFolder()) ;
	}

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
	
	protected void initTelosysToolsConfigFile(StringBuilder sb) {
		//--- Initialize the file (from META-INF) in the project environment
		initFileFromMetaInf(TelosysToolsEnv.getTelosysToolsConfigFileName(), getTelosysToolsConfigFileFullPath(), sb );
	}
	
	/**
	 * Initializes the Telosys Tools databases configuration file <br>
	 * Copy the default databases configuration file in the environment folder <br>
	 * If the destination file already exists it is not copied 
	 * @param sb
	 */
	protected void initDatabasesConfigFile( StringBuilder sb ) {
		initFileFromMetaInf(TelosysToolsEnv.getDatabasesDbCfgFileName(), getDatabasesDbCfgFullPath(), sb );
	}
	
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
		} catch (TelosysToolsException e) {
			sb.append("ERROR : cannot copy file '" + shortFileName + "' \n");
			sb.append("EXCEPTION : " + e.getClass().getSimpleName() + " : " + e.getMessage() + " \n");
		}
	}
}
