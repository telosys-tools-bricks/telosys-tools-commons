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
import java.io.IOException;
import java.nio.file.Files;

import javax.crypto.SecretKey;

import org.telosys.tools.commons.CryptoAES;
import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.YamlFileManager;
import org.telosys.tools.commons.YamlFileManagerWithEncryption;

public class GitCredentialsDAO {

	private static final String GIT_CREDENTIALS_FILE_NAME = "git_cfg";
	private static final String GIT_SECRET_KEY_FILE_NAME  = "git_k";
	
	private final File specificDirectoryForTests ;
	
	/**
	 * Default constructor for standard file location (in USER-HOME)
	 */
	public GitCredentialsDAO() {
		super();
		this.specificDirectoryForTests = null; // default standard file in USER-HOME 
	}

	/**
	 * Specific constructor defining a specific files directory (for tests only)
	 * @param specificDirectory
	 */
	protected GitCredentialsDAO(File specificDirectory) {
		super();
		if ( specificDirectory == null ) {
			throw new IllegalArgumentException("Arg directory is null");
		}
		else if ( specificDirectory.exists()  && ! specificDirectory.isDirectory() ) {
			throw new IllegalArgumentException("Arg directory '" + specificDirectory.getName() + "' is not a directory");
		}
		if ( ! specificDirectory.exists() ) {
			DirUtil.createDirectory(specificDirectory);
		}
		this.specificDirectoryForTests = specificDirectory;
	}

	/**
	 * Loads the GitCredentialsHolder from the YAML file
	 * @return credentials holder (holder is empty if no YAML file)
	 * @throws TelosysToolsException
	 */
	public GitCredentialsHolder load() throws TelosysToolsException {
		File yamlDataFile = getYamlDataFile();
		if ( yamlDataFile.exists() ) {
			// Load secret key (file must exist if Yaml file exists)
			SecretKey secretKey = CryptoAES.readSecretKey(getSecretKeyFile());
			// Load the credentials from the YAML file
			YamlFileManager yamlFileManager = getYamlFileManager(secretKey); 
			return new GitCredentialsHolder( yamlFileManager.loadMap() );
		}
		else {
			// never set => no YAML file => return empty holder
			return new GitCredentialsHolder();
		}
	}
	
	/**
	 * Saves the given GitCredentialsHolder in the YAML file
	 * @param gitCredentialsHolder
	 * @throws TelosysToolsException
	 */
	public void save(GitCredentialsHolder gitCredentialsHolder) throws TelosysToolsException {
		// Build a new secret key and save it in file
		SecretKey secretKey = CryptoAES.buildSecretKey(); // new AES key
		CryptoAES.writeSecretKey(getSecretKeyFile(), secretKey); // save secret key in file
		// Build a YamlFileManager with secret key and use it to save credentials
		YamlFileManager yamlFileManager = getYamlFileManager(secretKey);
		yamlFileManager.saveMap(gitCredentialsHolder.getMap());
	}

	/**
	 * Delete the 2 files located in the directory 
	 * @throws TelosysToolsException
	 */
	public void deleteFiles() throws TelosysToolsException {
		deleteFile(getYamlDataFile());
		deleteFile(getSecretKeyFile());
	}
	private void deleteFile(File file) throws TelosysToolsException {
		try {
			if ( file.exists() ) {
				Files.delete(file.toPath());
			}
		} catch (IOException e) {
			throw new TelosysToolsException("Cannot delete file '" + file.getName() + "'", e);
		}
	}
	
	
	private YamlFileManager getYamlFileManager(SecretKey secretKey) throws TelosysToolsException {
		return new YamlFileManagerWithEncryption(getYamlDataFile(), secretKey);
	}
	
	private File getYamlDataFile() throws TelosysToolsException {
		return getFileInDirectory(GIT_CREDENTIALS_FILE_NAME);
	}
	
	private File getSecretKeyFile() throws TelosysToolsException {
		return getFileInDirectory(GIT_SECRET_KEY_FILE_NAME);
	}
	
	private File getFileInDirectory(String fileName) throws TelosysToolsException {
		if ( this.specificDirectoryForTests != null ) {
			// specific directory 
			return new File(specificDirectoryForTests, fileName);
		}
		else {
			// user home directory 
			return UserHomeUtil.getFileInTelosysDir(fileName);
		}
	}	
}
