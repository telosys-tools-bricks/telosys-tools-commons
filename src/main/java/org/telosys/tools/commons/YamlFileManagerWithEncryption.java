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
package org.telosys.tools.commons;

import java.io.File;
import java.util.Map;

import javax.crypto.SecretKey;

import org.telosys.tools.commons.exception.TelosysYamlException;

/**
 * YAML file utility class 
 * 
 * @author Laurent GUERIN
 * 
 */

public class YamlFileManagerWithEncryption extends YamlFileManager {

	private final SecretKey secretKey;
	
	/**
	 * Constructor 
	 * @param file the YAML file
	 * @param secretKey the secret key used for encryption/decryption
	 */
	public YamlFileManagerWithEncryption(File file, SecretKey secretKey) {
		super(file);
		if ( secretKey == null ) throw new IllegalArgumentException("SecretKey is null");
		this.secretKey = secretKey ;
	}

	/* (non-Javadoc)
	 * @see org.telosys.tools.commons.YamlFileManager#saveMap(java.util.Map)
	 */
	@Override
	public void saveMap(Map<String, Object> map) throws TelosysYamlException {
		byte[] yamlBytes = mapToYamlBytes(map);
		byte[] encryptedYamlBytes = encrypt(yamlBytes);
		saveYamlBytes(encryptedYamlBytes);
	}
	
	/* (non-Javadoc)
	 * @see org.telosys.tools.commons.YamlFileManager#loadMap()
	 */
	@Override
	public Map<String, Object> loadMap() throws TelosysYamlException {
		byte[] encryptedYamlBytes = loadYamlBytes();
		byte[] yamlBytes = decrypt(encryptedYamlBytes);
		return yamlBytesToMap(yamlBytes);
	}

	private byte[] encrypt(byte[] data) throws TelosysYamlException {
		try {
			return CryptoAES.encrypt(data, secretKey);
		} catch (Exception e) {
			throw new TelosysYamlException(file, e);
		}
	}

	private byte[] decrypt(byte[] data) throws TelosysYamlException {
		try {
			return CryptoAES.decrypt(data, secretKey);
		} catch (Exception e) {
			throw new TelosysYamlException(file, e);
		}
	}
	
}