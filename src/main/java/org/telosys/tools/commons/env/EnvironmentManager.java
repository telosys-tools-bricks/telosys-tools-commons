/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;

/**
 * Telosys Tools environment manager <br>
 * For environment initialization (folders creation, files initialization, etc) <br>
 * All the methods are designed to log the actions result in a StringBuffer.
 * 
 * @author Laurent GUERIN
 *
 */
public class EnvironmentManager {

	//private final static String TELOSYS_TOOLS_CFG         = "telosys-tools.cfg" ;
	
	//private final static String DATABASES_DBCFG           = "databases.dbcfg" ;

	// private final static String TELOSYS_TOOLS_FOLDER_NAME = "TelosysTools" ;

	private final TelosysToolsEnv telosysToolsEnv ;
	private final String          environmentDirectory ;
	
	//-----------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param environmentDirectory the directory where the environment is located (OS full path)
	 */
	public EnvironmentManager(String environmentDirectory) {
		super();
		this.environmentDirectory = environmentDirectory;
		this.telosysToolsEnv = TelosysToolsEnv.getInstance();
	}

	//-----------------------------------------------------------------------------------------------------
	/**
	 * Initializes the environment using the standard default folders and configuration files
	 * @param sb
	 */
	public void initStandardEnvironment(StringBuffer sb) {
//		createFolder( TELOSYS_TOOLS_FOLDER_NAME, sb );
//		createFolder( TELOSYS_TOOLS_FOLDER_NAME + "/downloads", sb );
//		createFolder( TELOSYS_TOOLS_FOLDER_NAME + "/lib", sb );
//		createFolder( TELOSYS_TOOLS_FOLDER_NAME + "/templates", sb );
		createFolder( telosysToolsEnv.getTelosysToolsFolder(), sb );
		createFolder( telosysToolsEnv.getDownloadsFolder(), sb );
		createFolder( telosysToolsEnv.getLibrariesFolder(), sb );
		createFolder( telosysToolsEnv.getTemplatesFolder(), sb );
		initDatabasesConfigFile(sb);
		initTelosysToolsConfigFile(sb);
	}
	
//	//-----------------------------------------------------------------------------------------------------	
// Removed in v 3.0.0
//	/**
//	 * Initializes the environment using the folder names defined in the given configuration
//	 * @param telosysToolsCfg
//	 * @param sb
//	 */
//	public void initStandardEnvironment(TelosysToolsCfg telosysToolsCfg, StringBuffer sb) {
//		createFolder( telosysToolsCfg.getRepositoriesFolder(), sb ); // e.g. 'TelosysTools' 
//		createFolder( telosysToolsCfg.getDownloadsFolder(), sb );    // e.g. 'TelosysTools/downloads' 
//		createFolder( telosysToolsCfg.getLibrariesFolder(), sb );    // e.g. 'TelosysTools/lib' 
//		createFolder( telosysToolsCfg.getTemplatesFolder(), sb );    // e.g. 'TelosysTools/templates' 
//		initDatabasesConfigFile(sb);
//		initTelosysToolsConfigFile(sb);
//	}
	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Returns the environment directory (OS full path)
	 * @return
	 */
	protected String getEnvironmentFolderFullPath() {
		return environmentDirectory;
	}

//	/**
//	 * Returns the TelosysTools configuration file name<br>
//	 * ( e.g. 'telosys-tools.cfg' )
//	 * @return
//	 */
//	protected String getTelosysToolsConfigFileName() {
//		return TELOSYS_TOOLS_CFG ;
//	}

	/**
	 * Returns the TelosysTools configuration file path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/telosys-tools.cfg' )
	 * @return
	 */
	protected String getTelosysToolsConfigFileFullPath() {
		//return FileUtil.buildFilePath(environmentDirectory, TELOSYS_TOOLS_CFG) ;
		return FileUtil.buildFilePath(environmentDirectory, telosysToolsEnv.getTelosysToolsConfigFileName()) ;
	}

	/**
	 * Returns the 'databases.dbcfg' full path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools/databases.dbcfg' )
	 * @return
	 */
	protected String getDatabasesDbCfgFullPath() {
		return FileUtil.buildFilePath(environmentDirectory, telosysToolsEnv.getDatabasesDbCfgFilePath()) ;
	}

//	/**
//	 * Returns the TelosysTools folder name ( e.g. "TelosysTools" )
//	 * @return
//	 */
//	protected String getTelosysToolsFolderName() {
//		return TELOSYS_TOOLS_FOLDER_NAME ;
//	}

	/**
	 * Returns the TelosysTools folder full path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools' )
	 * @return
	 */
	protected String getTelosysToolsFolderFullPath() {
		//return FileUtil.buildFilePath(environmentDirectory, TELOSYS_TOOLS_FOLDER_NAME) ;
		return FileUtil.buildFilePath(environmentDirectory, telosysToolsEnv.getTelosysToolsFolder()) ;
	}

	/**
	 * Creates a folder in the environment folder
	 * @param folderToBeCreated 
	 * @param sb
	 */
	protected void createFolder(String folderToBeCreated, StringBuffer sb){

		if ( ! StrUtil.nullOrVoid(folderToBeCreated) )  {
			folderToBeCreated = folderToBeCreated.trim() ;
			String newDirPath = FileUtil.buildFilePath(environmentDirectory, folderToBeCreated);
			File newDir = new File(newDirPath) ; 
			if ( newDir.exists() ) {
				sb.append(". folder '" + folderToBeCreated + "' exists (not created)");
			}
			else {
				boolean created = newDir.mkdirs();
				//boolean created = EclipseProjUtil.createFolder(project, folderName ) ;	
				if ( created ) {
					sb.append(". folder '" + folderToBeCreated + "' created");
				}
				else {
					sb.append(". folder '" + folderToBeCreated + "' not created (ERROR)");
				}
			}
		}
		sb.append("\n");
	}	
	

	/**
	 * Initializes the Telosys Tools configuration file <br>
	 * Copy the default configuration file in the environment folder
	 * @param sb
	 */
	protected void initTelosysToolsConfigFile( StringBuffer sb ) {
		//String telosysToolsCfgFileName = telosysToolsEnv.getTelosysToolsConfigFileName() ;
		//String destinationFullPath = FileUtil.buildFilePath(environmentDirectory, telosysToolsCfgFileName ) ; 
		// copyFileFromMetaInfIfNotExists(destinationFullPath, telosysToolsCfgFileName, sb);
		copyFileFromMetaInfIfNotExists( getTelosysToolsConfigFileFullPath(), 
				telosysToolsEnv.getTelosysToolsConfigFileName() , sb);
	}
	
//	/**
//	 * Initializes the Telosys Tools databases configuration file <br>
//	 * Copy the default databases configuration file in the standard folder ( in "TelosysTools" )
//	 * If the destination file already exists it is not copied 
//	 * @param sb 
//	 */
//	protected void initDatabasesConfigFile( StringBuffer sb ) {
//		initDatabasesConfigFile( TELOSYS_TOOLS_FOLDER_NAME, sb );
//	}
	
	/**
	 * Initializes the Telosys Tools databases configuration file <br>
	 * Copy the default databases configuration file in the given environment sub-folder <br>
	 * If the destination file already exists it is not copied 
	 * @param sb
	 */
////	protected void initDatabasesConfigFile( String folderName, StringBuffer sb ) {
//	protected void initDatabasesConfigFile( StringBuffer sb ) {
//		String databasesDbCfgFileName = telosysToolsEnv.getDatabasesDbCfgFileName() ;
//		String folderName = telosysToolsEnv.getTelosysToolsFolder() ;
//		
//		String dirFullPath = fullPathInEnvironmentDir(folderName);
//		File destinationDir = new File (dirFullPath) ;
//		if ( destinationDir.exists() != true ) {
//			sb.append("ERROR : cannot create '" + databasesDbCfgFileName + "' file, directory '" + folderName + "' doesn't exist ! \n");
//			return ;
//		}
//		if ( destinationDir.isDirectory() != true ) {
//			sb.append("ERROR : cannot create '" + databasesDbCfgFileName + "' file, '" + folderName + "' is not a directory ! \n");
//			return ;
//		}
//		
//		String outsideFilePath = FileUtil.buildFilePath(dirFullPath, databasesDbCfgFileName) ; 
//		copyFileFromMetaInfIfNotExists(outsideFilePath, databasesDbCfgFileName, sb);
//	}
	protected void initDatabasesConfigFile( StringBuffer sb ) {
		copyFileFromMetaInfIfNotExists( getDatabasesDbCfgFullPath(), 
				telosysToolsEnv.getDatabasesDbCfgFileName(), sb);
	}

//	private String fullPathInEnvironmentDir(String dir){		
//		String dir2 = "" ;
//		if ( ! StrUtil.nullOrVoid(dir) )  {
//			dir2 = dir.trim() ;
//		}
//		return FileUtil.buildFilePath(environmentDirectory, dir2);
//	}
	
	//private final static String INSIDE_FILE = "/META-INF/files/" + Const.DATABASES_DBCFG ;
    private static final int BUFFER_SIZE = 1024 ; // 1 kb   
	private final static String META_INF_FILES = "/META-INF/files/" ;
	private void copyFileFromMetaInfIfNotExists(String destFullPath, String fileName, StringBuffer sb){
		File destFile = new File (destFullPath) ;
		if ( destFile.exists() ) {
			sb.append(". file '" + destFullPath + "' exists (not created) \n");
		}
		else {
			String fileNameInMetaInf = META_INF_FILES + fileName ;
			//--- Get input stream (file in JAR)
			InputStream is = EnvironmentManager.class.getResourceAsStream(fileNameInMetaInf);
			if ( is != null ) {
				//--- Open output stream
				FileOutputStream fos = null;
		        try
		        {
		            fos = new FileOutputStream(destFullPath);
		            try {
						copyAndClose(is, fos);
						sb.append(". file '" + destFullPath + "' created. \n");
					} catch (IOException ioex) {
						sb.append("ERROR : IOException : " + ioex.getMessage() + "\n");
					}
		        } catch (FileNotFoundException ex)
		        {
		            sb.append("ERROR : cannot create output file '" + destFullPath + "' ! \n");
		        }
			}
			else {
				sb.append("ERROR : '" + fileName + "' input file not found in jar ! \n");
			}
		}
	}
	
	private void copyAndClose(InputStream is, FileOutputStream fos) throws IOException {
		byte buffer[] = new byte[BUFFER_SIZE];
		int len = 0;
		
        while ((len = is.read(buffer)) > 0)
        {
        	fos.write(buffer, 0, len);
        }
        is.close();
        fos.close();
	}
}
