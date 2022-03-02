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
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

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
	 * Loads a map from the given YAML file
	 * @param file
	 * @return
	 */
	public Map<String, Object> load(File file) {
		try (InputStream inputStream = new FileInputStream(file)) {
			return loadYaml(file.getName(), inputStream);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("No YAML file " + file.getName() + " (file not found)" );
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot close YAML file " + file.getName() );
		}
	}
	private Map<String, Object> loadYaml(String fileName, InputStream inputStream) {
		try {
			Yaml yaml = new Yaml(); // no constructor => Map as default type
			return yaml.load(inputStream);
		} catch (Exception e) {
			throw new TelosysRuntimeException("Cannot load map from YAML file " + fileName, e );
		}
	}
	
	/**
	 * Loads an instance of the given class from the given YAML file
	 * @param file
	 * @param clazz
	 * @return
	 */
	public <T> T load(File file, Class<T> clazz) {
		try (InputStream inputStream = new FileInputStream(file)) {
			return loadYaml(file.getName(), inputStream, clazz);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("No YAML file " + file.getName() + " (file not found)" );
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot close YAML file " + file.getName() );
		}
	}
	private <T> T loadYaml(String fileName, InputStream inputStream, Class<T> clazz) {
		try {
			Yaml yaml = new Yaml(new Constructor(clazz));
			return yaml.load(inputStream);
		} catch (Exception e) {
			throw new TelosysRuntimeException("Cannot load instance of " + clazz.getSimpleName()
				+ " from YAML file " + fileName, e );
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
		} catch (IOException e1) {
			throw new IllegalArgumentException("Yaml save : IOException : " + e1.getMessage() );
		}

		// Write data in YAML file writer
		DumperOptions options = new DumperOptions();
		options.setIndent(2);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		
		Yaml yaml = new Yaml( options ) ;
		try {
			yaml.dump(data, writer);
		} catch (Exception e) {
			throw new IllegalArgumentException("Yaml save : Cannot write file " + file.getName() );
		}
		
		// Close file writer
		try {
			writer.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("Yaml save : Cannot close file " + file.getName() );
		}
	}
}