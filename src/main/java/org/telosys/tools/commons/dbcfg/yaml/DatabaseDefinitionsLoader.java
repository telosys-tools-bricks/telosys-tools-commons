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
package org.telosys.tools.commons.dbcfg.yaml;

import java.io.File;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.YamlFileManager;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.commons.exception.TelosysYamlException;

public class DatabaseDefinitionsLoader {

	public DatabaseDefinitionsLoader() {
		super();
	}
	
	/**
	 * Loads databases definitions for the given project configuration
	 * @param telosysToolsCfg
	 * @return
	 */
	public DatabaseDefinitions load(TelosysToolsCfg telosysToolsCfg) throws TelosysYamlException {
		String databasesFilePath = telosysToolsCfg.getDatabasesDbCfgFileAbsolutePath();
		if ( ! StrUtil.nullOrVoid(databasesFilePath) ) {
			// databases file is defined 
			return load(new File(databasesFilePath));
		}
		else {
			// databases file is not defined (not supposed to happen) 
			return null;
		}
	}
	
	/**
	 * Loads databases definitions from the given YAML file
	 * @param databasesFile
	 * @return
	 */
	public DatabaseDefinitions load(File databasesFile) throws TelosysYamlException {
		if ( databasesFile == null ) {
			throw new TelosysRuntimeException("Databases file is null");
		}
    	if ( ! databasesFile.exists() ) {
    		throw new TelosysRuntimeException("File '"+databasesFile.getName()+"' not found");
    	}
    	if ( ! databasesFile.isFile() ) {
    		throw new TelosysRuntimeException("'"+databasesFile.getName()+"' is not a file");
    	}
    	return loadYaml(databasesFile);
	}
	
	private DatabaseDefinitions loadYaml(File file) throws TelosysYamlException {
		YamlFileManager yamlFileManager = new YamlFileManager(file);
		return yamlFileManager.loadObject(DatabaseDefinitions.class);
	}

}
