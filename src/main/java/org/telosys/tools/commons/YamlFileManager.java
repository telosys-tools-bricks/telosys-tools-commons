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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import javax.crypto.SecretKey;

import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.commons.exception.TelosysYamlException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * YAML file utility class 
 * 
 * @author Laurent GUERIN
 * 
 */

public class YamlFileManager {

	private final File file ;
	
	/**
	 * Constructor 
	 */
	public YamlFileManager(File file) {
		super();
		this.file = file;
	}

	/**
	 * Loads an instance of the given class from the given YAML file
	 * @param clazz
	 * @return
	 * @throws TelosysYamlException
	 */
	public <T> T loadObject(Class<T> clazz) throws TelosysYamlException {
		try (InputStream inputStream = new FileInputStream(file)) {
			//return loadSpecificObjectFromYaml(inputStream, clazz);
//			try {
//				Yaml yaml = new Yaml(new Constructor(clazz));
//				return yaml.load(inputStream);
//			} catch (Exception e) {
//				throw new TelosysYamlException(file, e);
//			}
			Yaml yaml = new Yaml(new Constructor(clazz));
			return yaml.load(inputStream);

		} catch ( Exception e) { 
			// FileNotFoundException
			// IOException is thrown by automatic close() invocation on inputStream
			throw new TelosysYamlException(file, e);
		}
	}

//	/**
//	 * Loads a map from the given YAML file
//	 * @param file
//	 * @return
//	 */
//	public Map<String, Object> load(File file) throws TelosysYamlException {
//		try (InputStream inputStream = new FileInputStream(file)) {
//			return loadMapFromYaml(file.getName(), inputStream);
//		} catch (FileNotFoundException e) {
//			throw new TelosysRuntimeException("No YAML file " + file.getName() + " (file not found)" );
//		} catch (IOException e) { 
//			// IOException is thrown by automatic close() invocation on inputStream
//			throw new TelosysRuntimeException("Cannot close YAML file " + file.getName() );
//		}
//	}
	
//	/**
//	 * Loads a map from the given YAML file using Snake Yaml 
//	 * @param fileName
//	 * @param inputStream
//	 * @return
//	 * @throws TelosysYamlException
//	 */
//	private Map<String, Object> loadMapFromYaml(String fileName, InputStream inputStream) throws TelosysYamlException {
//		try {
//			Yaml yaml = new Yaml(); // no constructor => Map as default type
//			return yaml.load(inputStream);
//		} catch (YAMLException yamlException) {
//			// YAML error in the file
//			throw new TelosysYamlException(fileName, yamlException);
//		} catch (Exception e) {
//			// any other unexpected exception
//			throw new TelosysRuntimeException("Cannot load map from YAML file " + fileName, e );
//		}
//	}
	
//	/**
//	 * Loads an instance of the given class from the given YAML file using Snake Yaml 
//	 * @param inputStream
//	 * @param clazz
//	 * @return
//	 * @throws TelosysYamlException
//	 */
//	private <T> T loadSpecificObjectFromYaml(InputStream inputStream, Class<T> clazz) throws TelosysYamlException {
//		try {
//			Yaml yaml = new Yaml(new Constructor(clazz));
//			return yaml.load(inputStream);
//		} catch (Exception e) {
//			throw new TelosysYamlException(file, e);
//		}
//	}
	
	/**
	 * Saves the given data in a YAML file
	 * @param data
	 */
	public void saveObject(Object data) throws TelosysYamlException {
		
		// Get file writer
		FileUtil.createParentFolderIfNecessary(file);
		Writer writer;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			throw new TelosysYamlException(file, e );
		}

		// Write data in YAML file writer
		DumperOptions options = new DumperOptions();
		options.setIndent(2);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		
		Yaml yaml = new Yaml( options ) ;
		try {
			yaml.dump(data, writer);
		} catch (Exception e) {
			throw new TelosysYamlException(file, e );
		}
		
		// Close file writer
		try {
			writer.close();
		} catch (IOException e) {
			throw new TelosysYamlException(file, e );
		}
	}

	/**
	 * @param map
	 * @since 4.3.0
	 */
	public void saveMap(Map<String, Object> map) throws TelosysYamlException {
		saveMap(map, null);
	}
	
	/**
	 * @param map
	 * @param secretKey key for encryption (or null if file is not encrypted)
	 * @since 4.3.0
	 */
	public void saveMap(Map<String, Object> map, SecretKey secretKey) throws TelosysYamlException {
		Yaml yaml = new Yaml();
		//--- Serialize to YAML string 
        String yamlString = yaml.dump(map);
        try {
			byte[] yamlBytes = yamlString.getBytes(StandardCharsets.UTF_8); // throws UnsupportedEncodingException
			Files.write(file.toPath(), encryptIfKey(yamlBytes, secretKey) ); // throws IOException
		} catch (Exception e) {
			throw new TelosysYamlException(file, e);
		}
	}
	
	/**
	 * @since 4.3.0
	 * @return
	 */
	public Map<String, Object> loadMap() throws TelosysYamlException {
		return loadMap(null);
	}
	
	/**
	 * @param secretKey key for decryption (or null if file is not encrypted)
	 * @since 4.3.0
	 * @return
	 */
	public Map<String, Object> loadMap(SecretKey secretKey) throws TelosysYamlException {
		byte[] data = null;
		try {
			byte[] rawData = Files.readAllBytes(file.toPath() );
			data = decryptIfKey(rawData, secretKey);
		} catch (IOException e) {
			throw new TelosysYamlException(file, e);
		}
		String yamlString = new String(data, StandardCharsets.UTF_8);
		Yaml yaml = new Yaml();
		//--- Deserialize from YAML string 
		return yaml.load(yamlString);
	}

	private byte[] encryptIfKey(byte[] data, SecretKey secretKey) throws TelosysYamlException {
		if ( secretKey != null ) {
			try {
				return CryptoAES.encrypt(data, secretKey);
			} catch (Exception e) {
				throw new TelosysYamlException(file, e);
			}
		}
		else {
			return data;
		}
	}

	private byte[] decryptIfKey(byte[] data, SecretKey secretKey) throws TelosysYamlException {
		if ( secretKey != null ) {
			try {
				return CryptoAES.decrypt(data, secretKey);
			} catch (Exception e) {
				throw new TelosysYamlException(file, e);
			}
		}
		else {
			return data;
		}
	}
	
}