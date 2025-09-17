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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * YAML file utility class 
 * 
 * @author Laurent GUERIN
 * 
 */

public class YamlFileManager {

	protected final File file ;
	
	/**
	 * Constructor 
	 * @param file the YAML file used for save/load operations
	 */
	public YamlFileManager(File file) {
		super();
		if ( file == null ) throw new IllegalArgumentException("File is null");
		this.file = file;
	}

	/**
	 * Map to YAML
	 * @param map
	 * @return
	 * @throws TelosysToolsException
	 */
	protected byte[] mapToYamlBytes(Map<String, Object> map) throws TelosysToolsException  {
        try {
    		Yaml yaml = new Yaml();
    		//--- Serialize to YAML  
            String yamlString = yaml.dump(map);
			return yamlString.getBytes(StandardCharsets.UTF_8); // throws UnsupportedEncodingException
		} catch (Exception e) {
			throw new TelosysToolsException("Cannot serialize to YAML", e);
		}
	}
	
	/**
	 * YAML to map
	 * @param yamlBytes
	 * @return
	 * @throws TelosysToolsException
	 */
	protected Map<String, Object> yamlBytesToMap(byte[] yamlBytes) throws TelosysToolsException  {
		try {
			Yaml yaml = new Yaml();
			//--- Deserialize from YAML  
			String yamlString = new String(yamlBytes, StandardCharsets.UTF_8);
			return yaml.load(yamlString);
		} catch (Exception e) {
			throw new TelosysToolsException("Cannot deserialize from YAML", e);
		}
	}
	protected void saveYamlBytes(byte[] yamlBytes) throws TelosysToolsException {
        try {
			Files.write(file.toPath(), yamlBytes ); // throws IOException
		} catch (Exception e) {
			throw new TelosysToolsException("Cannot save YAML file", e);
		}
	}
	protected byte[] loadYamlBytes() throws TelosysToolsException {
		try {
			return Files.readAllBytes(file.toPath() ); // throws IOException
		} catch (Exception e) {
			throw new TelosysToolsException("Cannot load YAML file", e);
		}
	}
	
//	/**
//	 * Loads an instance of the given class from the given YAML file
//	 * @param clazz
//	 * @return
//	 * @throws TelosysToolsException
//	 */
//	public <T> T loadObject(Class<T> clazz) throws TelosysToolsException {
//		try (InputStream inputStream = new FileInputStream(file)) {
//			Yaml yaml = new Yaml(new Constructor(clazz));
//			return yaml.load(inputStream);
//
//		} catch ( Exception e) { 
//			// FileNotFoundException
//			// IOException is thrown by automatic close() invocation on inputStream
//			throw new TelosysToolsException(file, e);
//		}
//	}

//	/**
//	 * Saves the given data in a YAML file
//	 * @param data
//	 */
//	public void saveObject(Object data) throws TelosysToolsException {
//		
//		// Get file writer
//		FileUtil.createParentFolderIfNecessary(file);
//		Writer writer;
//		try {
//			writer = new FileWriter(file);
//		} catch (IOException e) {
//			throw new TelosysToolsException(file, e );
//		}
//
//		// Write data in YAML file writer
//		DumperOptions options = new DumperOptions();
//		options.setIndent(2);
//		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
//		
//		Yaml yaml = new Yaml( options ) ;
//		try {
//			yaml.dump(data, writer);
//		} catch (Exception e) {
//			throw new TelosysToolsException(file, e );
//		}
//		
//		// Close file writer
//		try {
//			writer.close();
//		} catch (IOException e) {
//			throw new TelosysToolsException(file, e );
//		}
//	}

	/**
	 * Serialize the given map and save in the YAML file
	 * @param map
	 * @throws TelosysToolsException
	 */
	public void saveMap(Map<String, Object> map) throws TelosysToolsException {
		byte[] yamlBytes = mapToYamlBytes(map);
		saveYamlBytes(yamlBytes);
	}
	
	/**
	 * Load from the YAML file and deserialize to map
	 * @return
	 * @throws TelosysToolsException
	 */
	public Map<String, Object> loadMap() throws TelosysToolsException {
		byte[] yamlBytes = loadYamlBytes();
		return yamlBytesToMap(yamlBytes);

	}
	
}