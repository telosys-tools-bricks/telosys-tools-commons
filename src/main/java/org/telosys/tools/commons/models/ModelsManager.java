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
package org.telosys.tools.commons.models;

import java.io.File;

import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.ZipUtil;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.depot.Depot;
import org.telosys.tools.commons.depot.DepotClient;
import org.telosys.tools.commons.depot.DepotClientProvider;
import org.telosys.tools.commons.depot.DepotResponse;

/**
 * Utility class for models management (including GitHub access)
 * 
 * @author Laurent GUERIN
 * @since 4.2.0
 */
public class ModelsManager {
	
	public static final String MODEL_YAML = "model.yaml" ;

	private final TelosysToolsCfg telosysToolsCfg ;
	
	/**
	 * Constructor 
	 * @param telosysToolsCfg
	 */
	public ModelsManager(TelosysToolsCfg telosysToolsCfg) {
		super();
		if ( telosysToolsCfg == null ) throw new IllegalArgumentException("TelosysToolsCfg is null");
		this.telosysToolsCfg = telosysToolsCfg;
	}

	/**
	 * Returns a File object for the given model's name <br>
	 * ( e.g. 'X:/dir/myproject/TelosysTools/models/{modelName}' )<br>
	 * There's no guarantee the returned file/directory exists
	 * @param modelName
	 * @return
	 */
	public File getModelFolder(String modelName) {
		return new File(telosysToolsCfg.getModelFolderAbsolutePath(modelName)) ;
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Returns true if the model's folder exists
	 * @param modelName
	 * @return
	 */
	private boolean modelFolderExists( String modelName ) {
		return getModelFolder(modelName).exists();
	}
	
	//--------------------------------------------------------------------------------------------------
	/**
	 * Return a list of models available in a depot 
	 * @param depot
	 * @return
	 * @throws TelosysToolsException
	 */
	public DepotResponse getModelsFromDepot( Depot depot ) throws TelosysToolsException {
		DepotClient depotClient = DepotClientProvider.getDepotClient(depot, telosysToolsCfg);
		return depotClient.getRepositories(depot);
	}

	/**
	 * @param depot
	 * @param modelName
	 * @param branch
	 * @return
	 * @throws TelosysToolsException
	 */
	public String downloadModelBranch(Depot depot, String modelName, String branch) throws TelosysToolsException {
		String downloadedFile = FileUtil.buildFilePath(telosysToolsCfg.getDownloadsFolderAbsolutePath(), modelName + ".zip");
		DepotClient depotClient = DepotClientProvider.getDepotClient(depot, telosysToolsCfg);
		depotClient.downloadRepositoryBranch(depot, modelName, branch, downloadedFile);
		return downloadedFile;
	}
	
	/**
	 * @param depot
	 * @param modelName
	 * @param branch
	 * @return
	 * @throws TelosysToolsException
	 */
	public boolean downloadAndInstallModelBranch(Depot depot, String modelName, String branch) throws TelosysToolsException {
		String downloadedFile = downloadModelBranch(depot, modelName, branch ) ;
		return installModel(downloadedFile, modelName);
	}


	//--------------------------------------------------------------------------------------------------
	/**
	 * Install (unzip) the given zip file in the model's destination folder 
	 * @param zipFileName
	 * @param modelName
	 * @return
	 */
	private boolean installModel(String zipFileName, String modelName) throws TelosysToolsException {
		if ( modelFolderExists(modelName) ) {
			return false ;
		}
		else {
			String modelFolder = telosysToolsCfg.getModelFolderAbsolutePath(modelName) ;
			try {
				ZipUtil.unzip(zipFileName, modelFolder, true ) ;
				return true;
			} catch (Exception e) {
				throw new TelosysToolsException("Cannot unzip "+ zipFileName, e);
			}
		}
	}

	/**
	 * Deletes the given model
	 * @param modelName
	 * @return true if found and deleted, false if not found
	 * @throws TelosysToolsException
	 */
	public boolean deleteModel( String modelName ) throws TelosysToolsException {
		String modelFolder = telosysToolsCfg.getModelFolderAbsolutePath(modelName);
		File file = new File(modelFolder);
		if ( file.isDirectory() ) {  // file exists and is a directory
			// directory found => deleted it 
			DirUtil.deleteDirectory(file);
			return true ;
		}
		return false ;
	}
	
	/**
	 * Returns true if the given file/folder is a 'model' ( a directory containing a 'model.yaml' file )
	 * @param file
	 * @return
	 */
	public boolean isModel(File file) {
		return file != null && file.isDirectory() && modelFolderContainsModelYamlFile(file) ;
	}
	
	/**
	 * Returns true if the model is already installed (if the model's folder exists)
	 * @param modelName
	 * @return
	 */
	public boolean isModelAlreadyInstalled(String modelName) {
		File file = getModelFolder(modelName) ;
		return file.exists();
	}
	
	/**
	 * Returns true if the 'model.yaml' file exists in the given folder
	 * @param modelFolder
	 * @return
	 */
	private boolean modelFolderContainsModelYamlFile(File modelFolder) {
		File file = getModelYamlFile(modelFolder);
		return ( file != null && file.exists() );
	}

	/**
	 * Returns the 'model.yaml' File object for the given model folder <br>
	 * There's no guarantee the returned file exists
	 * @param modelFolder
	 * @return
	 */
	public File getModelYamlFile(File modelFolder) {
		return new File( FileUtil.buildFilePath(modelFolder.getAbsolutePath(), MODEL_YAML ) ) ;
	}	

}
