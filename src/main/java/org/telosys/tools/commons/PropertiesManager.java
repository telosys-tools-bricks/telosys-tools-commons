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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Properties file management <br>
 * Load/Save properties in a properties file 
 */

public class PropertiesManager {

	private static final String ERR_CANNOT_LOAD = "Cannot load properties" ;
	
	private static final String ERR_CANNOT_SAVE = "Cannot save properties" ;
	
	private final File propertiesFile ;	
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Constructor 
	 * @param fileName
	 */
	public PropertiesManager( String fileName ) {
		super();
		if ( fileName == null ) {
			throw new IllegalArgumentException("File name is null");
		}
		propertiesFile = new File(fileName);
	}
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param file
	 */
	public PropertiesManager( File file ) {
		super();
		if ( file == null ) {
			throw new IllegalArgumentException("File is null");
		}
		propertiesFile = file ;
	}
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Loads the properties from the current file 
	 * @return the properties loaded (or null if the file doesn't exist)
	 */
	public Properties load() {
		return load( propertiesFile );
	}
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Saves the given properties in the current file 
	 * @param properties
	 */
	public void save(Properties properties) {
		save(propertiesFile, properties, "");
	}
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Saves the given properties in the current file with comments
	 * @param properties
	 * @param comments
	 */
	public void save(Properties properties, String comments) {
		save(propertiesFile, properties, comments);
	}
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Returns the absolute path of the properties file
	 * @return
	 */
	public String getFileAbsolutePath() {
		return propertiesFile.getAbsolutePath() ; 
	}
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Returns true if the properties file exists (else false)
	 * @return
	 */
	public boolean fileExists() {
		return propertiesFile.exists() ; 
	}
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Loads the properties from the given file 
	 * @param file
	 * @return
	 */
	private Properties load( File file ) {
		//--- If the file doesn't exist ... return null (it's not an error)
		if ( ! file.exists() ) {
			return null ;
		}

		//--- The file exists => load it !  
		Properties props = new Properties();
		try ( FileInputStream fis = new FileInputStream(file) ){
			props.load(fis);
		} catch (IOException ioe) {
			String msg = ERR_CANNOT_LOAD + " : " + ioe.getMessage();
			throw new RuntimeException(msg, ioe);
		}
		return props;
	}

	//--------------------------------------------------------------------------------------------
	/**
	 * Saves the given properties in the given file
	 * @param file
	 * @param properties
	 */
	private void save(File file, Properties properties, String comments) {
		if ( properties == null ) {
			throw new IllegalArgumentException("Properties parameter is null" );
		}
		
		try ( FileOutputStream fos = new FileOutputStream(file) ) {
			properties.store(fos, comments);
		} catch (IOException ioe) {
			String msg = ERR_CANNOT_SAVE + " : " + ioe.getMessage();
			throw new RuntimeException(msg, ioe );
		}
	}

}