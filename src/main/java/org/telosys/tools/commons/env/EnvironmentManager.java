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

import org.telosys.tools.commons.DirUtil;
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
	public void initEnvironment(StringBuffer sb) {
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

	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Returns the TelosysTools configuration file path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/telosys-tools.cfg' )
	 * @return
	 */
	protected String getTelosysToolsConfigFileFullPath() {
		return FileUtil.buildFilePath(environmentDirectory, telosysToolsEnv.getTelosysToolsConfigFileName()) ;
	}

	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Returns the 'databases.dbcfg' full path (OS full path)<br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools/databases.dbcfg' )
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
	protected void createFolder(String folderToBeCreated, StringBuffer sb) {

		if ( ! StrUtil.nullOrVoid(folderToBeCreated) )  {
			folderToBeCreated = folderToBeCreated.trim() ;
			String newDirPath = FileUtil.buildFilePath(environmentDirectory, folderToBeCreated);
			File newDir = new File(newDirPath) ; 
			if ( newDir.exists() ) {
				sb.append(". folder '" + folderToBeCreated + "' exists (not created)");
			}
			else {
//				boolean created = newDir.mkdirs();
//				if ( created ) {
//					sb.append(". folder '" + folderToBeCreated + "' created");
//				}
//				else {
//					sb.append(". folder '" + folderToBeCreated + "' not created (ERROR)");
//				}
				DirUtil.createDirectory( newDir ); // v 3.0.0
				sb.append(". folder '" + folderToBeCreated + "' created");
			}
		}
		sb.append("\n");
	}	
	
	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Initializes the Telosys Tools configuration file <br>
	 * Copy the default configuration file in the environment folder <br>
	 * If the destination file already exists it is not copied 
	 * @param sb
	 */
	protected void initTelosysToolsConfigFile( StringBuffer sb ) {
//		copyFileFromMetaInfIfNotExists( getTelosysToolsConfigFileFullPath(), 
//				telosysToolsEnv.getTelosysToolsConfigFileName(), sb);
		initFileFromMetaInf(telosysToolsEnv.getTelosysToolsConfigFileName(), getTelosysToolsConfigFileFullPath(), sb );
	}
	
	//-----------------------------------------------------------------------------------------------------	
	/**
	 * Initializes the Telosys Tools databases configuration file <br>
	 * Copy the default databases configuration file in the environment folder <br>
	 * If the destination file already exists it is not copied 
	 * @param sb
	 */
	protected void initDatabasesConfigFile( StringBuffer sb ) {
//		copyFileFromMetaInfIfNotExists( getDatabasesDbCfgFullPath(), 
//				telosysToolsEnv.getDatabasesDbCfgFileName(), sb);		
		initFileFromMetaInf(telosysToolsEnv.getDatabasesDbCfgFileName(), getDatabasesDbCfgFullPath(), sb );
	}
	
	/**
	 * Initializes a Telosys Tools configuration file by copying a file from 'META-INF'
	 * @param shortFileName  the file name in 'META-INF/files' ( eg 'telosys-tools.cfg' )
	 * @param destinationFullPath  the destination file full path 
	 * @param sb
	 */
	private void initFileFromMetaInf(String shortFileName, String destinationFullPath, StringBuffer sb ) {
		//--- File path inside "META-INF" folder
		String filePathInMetaInf = FileUtil.buildFilePath("/files/", shortFileName) ; 
		try {
			boolean copied = copyFileFromMetaInfIfNotExist( filePathInMetaInf, destinationFullPath );
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
//	//----------------------------------------------------------------------------------------------------
//	private String buildFilePathInMetaInfFolder(String fileName)  {
//		return FileUtil.buildFilePath("/files/", fileName) ;
//	}
//	//----------------------------------------------------------------------------------------------------
//	private void reportStatus(boolean copied, Exception e, String fileName, StringBuffer sb )  {
//		if (e != null ) {
//			sb.append("ERROR : cannot copy file '" + fileName + "' \n");
//			sb.append("EXCEPTION : " + e.getClass().getSimpleName() + " : " + e.getMessage() + " \n");
//		}
//		else {
//			if (copied) {
//				sb.append(". file '" + fileName + "' created. \n");
//			}
//			else {
//				sb.append(". file '" + fileName + "' already exists (not created) \n");
//			}
//		}
//	}
	

	//-----------------------------------------------------------------------------------------------------	
//	private void copyFileFromMetaInfIfNotExists(String destFullPath, String fileName, StringBuffer sb) {
//		File destFile = new File (destFullPath) ;
//		if ( destFile.exists() ) {
//			sb.append(". file '" + destFullPath + "' exists (not created) \n");
//		}
//		else {
//			String fileNameInMetaInf = META_INF_FILES + fileName ;
//			//--- Get input stream (file in JAR)
//			InputStream is = EnvironmentManager.class.getResourceAsStream(fileNameInMetaInf);
//			if ( is != null ) {
//				//--- Open output stream
//				FileOutputStream fos = null;
//		        try
//		        {
//		            fos = new FileOutputStream(destFullPath);
//		            try {
//						copyAndClose(is, fos);
//						sb.append(". file '" + destFullPath + "' created. \n");
//					} catch (IOException ioex) {
//						sb.append("ERROR : IOException : " + ioex.getMessage() + "\n");
//					}
//		        } catch (FileNotFoundException ex)
//		        {
//		            sb.append("ERROR : cannot create output file '" + destFullPath + "' ! \n");
//		        }
//			}
//			else {
//				sb.append("ERROR : '" + fileName + "' input file not found in jar ! \n");
//			}
//		}
//	}

	//----------------------------------------------------------------------------------------------------
    private final static int    BUFFER_SIZE    = 1024 ; // 1 kb   
//	private final static String META_INF = "/META-INF/files/" ;

	private String buildMetaInfPath(String filePathInMetaInf) throws Exception {
		return FileUtil.buildFilePath("/META-INF/", filePathInMetaInf) ;
	}
	
	private boolean copyFileFromMetaInfIfNotExist(String filePathInMetaInf, String destFullPath) throws Exception {
		File destFile = new File (destFullPath) ;
		if ( destFile.exists() ) {
			return false ; // Not copied
		}
		else {
			copyFileFromMetaInf(filePathInMetaInf, destFullPath) ;
			return true ; // Copied
		}
	}
	
	private void copyFileFromMetaInf(String filePathInMetaInf, String destFullPath) throws Exception {
		//--- Build the full file name ( e.g. "META-INF/mydir/myfile" )
		String fullFileNameInMetaInf = buildMetaInfPath(filePathInMetaInf) ;

		//--- Get input stream (file in JAR)
		InputStream is = EnvironmentManager.class.getResourceAsStream(fullFileNameInMetaInf);
		if ( is == null ) {
			throw new Exception("File '" + filePathInMetaInf + "' not found in 'META-INF' \n");
		}
		
		//--- Open output stream
		FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destFullPath);
        } catch (FileNotFoundException ex)
        {
            //sb.append("ERROR : cannot create output file '" + destFullPath + "' ! \n");
            throw new Exception("Cannot create output file '" + destFullPath + "' \n");
        }

        //--- Copy 
//        try {
//			copyAndClose(is, fos);
//			sb.append(". file '" + destFullPath + "' created. \n");
//		} catch (IOException ioex) {
//			sb.append("ERROR : IOException : " + ioex.getMessage() + "\n");
//		}
		copyAndClose(is, fos);
		
	}
	
	private void copyAndClose(InputStream is, FileOutputStream fos) throws Exception {
		byte buffer[] = new byte[BUFFER_SIZE];
		int len = 0;
		
        try {
			while ((len = is.read(buffer)) > 0)
			{
				fos.write(buffer, 0, len);
			}
			is.close();
			fos.close();
		} catch (IOException ioex) {
			throw new Exception("IO error", ioex);
		}
	}
}
