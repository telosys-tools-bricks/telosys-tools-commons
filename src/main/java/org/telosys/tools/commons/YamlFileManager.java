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
import java.io.Writer;
import java.util.Map;

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

	/**
	 * Constructor 
	 */
	public YamlFileManager() {
		super();
	}

	/**
	 * Loads an instance of the given class from the given YAML file
	 * @param file
	 * @param clazz
	 * @return
	 * @throws TelosysYamlException
	 */
	public <T> T load(File file, Class<T> clazz) throws TelosysYamlException {
		try (InputStream inputStream = new FileInputStream(file)) {
			return loadSpecificObjectFromYaml(file.getName(), inputStream, clazz);
		} catch (FileNotFoundException e) {
			throw new TelosysRuntimeException("No YAML file " + file.getName() + " (file not found)" );
		} catch (IOException e) {
			// IOException is thrown by automatic close() invocation on inputStream
			throw new TelosysRuntimeException("Cannot close YAML file " + file.getName() );
		}
	}

	/**
	 * Loads a map from the given YAML file
	 * @param file
	 * @return
	 */
	public Map<String, Object> load(File file) throws TelosysYamlException {
		try (InputStream inputStream = new FileInputStream(file)) {
			return loadMapFromYaml(file.getName(), inputStream);
		} catch (FileNotFoundException e) {
			throw new TelosysRuntimeException("No YAML file " + file.getName() + " (file not found)" );
		} catch (IOException e) { 
			// IOException is thrown by automatic close() invocation on inputStream
			throw new TelosysRuntimeException("Cannot close YAML file " + file.getName() );
		}
	}
	
	/**
	 * Loads a map from the given YAML file using Snake Yaml 
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws TelosysYamlException
	 */
	private Map<String, Object> loadMapFromYaml(String fileName, InputStream inputStream) throws TelosysYamlException {
		try {
			Yaml yaml = new Yaml(); // no constructor => Map as default type
			return yaml.load(inputStream);
		} catch (YAMLException yamlException) {
			// YAML error in the file
			throw new TelosysYamlException(fileName, yamlException);
		} catch (Exception e) {
			// any other unexpected exception
			throw new TelosysRuntimeException("Cannot load map from YAML file " + fileName, e );
		}
	}
	
	/**
	 * Loads an instance of the given class from the given YAML file using Snake Yaml 
	 * @param fileName
	 * @param inputStream
	 * @param clazz
	 * @return
	 * @throws TelosysYamlException
	 */
	private <T> T loadSpecificObjectFromYaml(String fileName, InputStream inputStream, Class<T> clazz) throws TelosysYamlException {
		try {
			Yaml yaml = new Yaml(new Constructor(clazz));
			return yaml.load(inputStream);
		} catch (YAMLException yamlException) {
			// YAML error in the file
			throw new TelosysYamlException(fileName, yamlException);
		} catch (Exception e) {
			// any other unexpected exception
			throw new TelosysRuntimeException("Cannot load '" + clazz.getSimpleName() + "' from YAML file '" + fileName + "'", e );
		}
	}
	
	/**
	 * Saves the given data in a YAML file
	 * @param file
	 * @param data
	 */
	public void save(File file, Object data) {
		
		// Get file writer
		FileUtil.createParentFolderIfNecessary(file);
		Writer writer;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			throw new TelosysRuntimeException("Yaml save : Cannot create FileWriter (IOException) : " + e.getMessage() );
		}

		// Write data in YAML file writer
		DumperOptions options = new DumperOptions();
		options.setIndent(2);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		
		Yaml yaml = new Yaml( options ) ;
		try {
			yaml.dump(data, writer);
		} catch (Exception e) {
			throw new TelosysRuntimeException("Yaml save : Cannot write file " + file.getName(), e );
		}
		
		// Close file writer
		try {
			writer.close();
		} catch (IOException e) {
			throw new TelosysRuntimeException("Yaml save : Cannot close file " + file.getName() + " (IOException)", e );
		}
	}
}