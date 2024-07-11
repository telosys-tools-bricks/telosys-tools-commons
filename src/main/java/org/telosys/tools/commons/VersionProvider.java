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

import java.io.IOException;
import java.util.Properties;

import org.telosys.tools.commons.exception.TelosysRuntimeException;

/**
 * Telosys-CLI version provider
 *  
 * New: since ver 4.1.0 version and build id are provided via a properties file
 * updated at each Maven build using resources filtering
 * 
 * @author Laurent Guerin
 *
 */
public class VersionProvider {

	/**
	 * Property name to get the NAME from properties file
	 */
	private static final String PROJECT_NAME = "project.name" ;
	
	/**
	 * Property name to get the VERSION from properties file
	 */
	private static final String PROJECT_VERSION = "project.version" ;
	
	/**
	 * Property name to get the BUILD-ID from properties file
	 */
	private static final String PROJECT_BUILD = "project.build" ;

	/**
	 * Properties file name in "src/main/resources"
	 */
	private final String propertiesFileName ;

	private Properties properties = null ;

	private String propertiesLoadingError = null ;
	

	/**
	 * Constructor 
	 * @param propertiesFileName file name containing Maven build properties 
	 */
	public VersionProvider(String propertiesFileName) {
		if ( propertiesFileName.startsWith("/") ) {
			this.propertiesFileName = propertiesFileName;
		}
		else {
			throw new TelosysRuntimeException("Invalid properties file name '" + propertiesFileName + "' (must start with '/')");
		}
	}
	
	/**
	 * Returns the Maven 'project.version' and 'build.id' <br>
	 * @return
	 */
	public final String getVersionWithBuilId() {
		String version = getVersion() ;
		if ( propertiesLoadingError != null ) { // test error on first access
			return propertiesLoadingError;
		}
		else {
			return version + " (build: " + getBuildId() + ")";
		}
	}
	
	/**
	 * Returns the property for 'project.name' 
	 * @return
	 */
	public final String getName() {
		return getProperty(PROJECT_NAME, "unknown name");
	}

	/**
	 * Returns the property for 'project.version' 
	 * @return
	 */
	public final String getVersion() {
		return getProperty(PROJECT_VERSION, "unknown version");
	}

	/**
	 * Returns the property for 'project.build' = Maven 'build.id' 
	 * @return
	 */
	public final String getBuildId() {
		return getProperty(PROJECT_BUILD, "unknown build");
	}

	
	private final String getProperty(String propertyName, String defaultValue) {
	    Properties prop = getBuildProperties();
	    if ( propertiesLoadingError != null ) {
	    	// error detected => return error message instead
		    return propertiesLoadingError;
	    }
	    else {
	    	// ok: properties found and loaded
		    return prop.getProperty(propertyName, defaultValue);	
	    }
	}

	private final Properties getBuildProperties() {
		if ( properties == null ) {
			// not loaded yet
			properties = loadBuildProperties();
		}
		return properties ;
	}
	
	private final Properties loadBuildProperties() {
	    Properties prop = new Properties();
	    try {
	    	prop.load(VersionProvider.class.getResourceAsStream(propertiesFileName));
	    	propertiesLoadingError = null;
	    } 
	    catch (IOException e) {	    	
	    	propertiesLoadingError = "ERROR: cannot load build properties, error: " + e.getMessage() ;
	    }
    	return prop;
	}
}
