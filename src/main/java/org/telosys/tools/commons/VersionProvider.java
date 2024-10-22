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
import java.io.InputStream;
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

	private final Properties versionProperties ;

	/**
	 * Constructor 
	 * @param propertiesFileName file name containing Maven build properties 
	 */
	public VersionProvider(String propertiesFileName) {
		if ( propertiesFileName.startsWith("/") ) {
			this.propertiesFileName = propertiesFileName;
			this.versionProperties = loadBuildProperties();
		}
		else {
			throw new TelosysRuntimeException("Invalid version properties file name '" + propertiesFileName + "' (must start with '/')");
		}
	}
	
	/**
	 * Returns the Maven 'project.version' and 'build.id' <br>
	 * @return
	 */
	public final String getVersionWithBuilId() {
		return getVersion() + " (build: " + getBuildId() + ")";
	}
	
	/**
	 * Returns the property for 'project.name' 
	 * @return
	 */
	public final String getName() {
		return getProperty(PROJECT_NAME);
	}

	/**
	 * Returns the property for 'project.version' 
	 * @return
	 */
	public final String getVersion() {
		return getProperty(PROJECT_VERSION);
	}

	/**
	 * Returns the property for 'project.build' = Maven 'build.id' 
	 * @return
	 */
	public final String getBuildId() {
		return getProperty(PROJECT_BUILD);
	}

	
	private final String getProperty(String propertyName) {
		if ( versionProperties != null) {
			String s = versionProperties.getProperty(propertyName);
			return s != null ? s : "unknown ("+propertyName+")";
		}
		else {
			return "no properties (file:"+propertiesFileName+")";
		}
	}

	private final Properties loadBuildProperties() {
		
		try ( InputStream is = VersionProvider.class.getResourceAsStream(propertiesFileName) ) {
			// InputStream can be null if file not found
			if ( is != null) {
			    Properties properties = new Properties();
		    	properties.load(is);
		    	return properties;
			}
			else {
				throw new TelosysRuntimeException("Version properties file not found ("+propertiesFileName+")");
			}
		}
	    catch (IOException e) {	    	
			throw new TelosysRuntimeException("Cannot read version properties file IOException ("+propertiesFileName+")");
	    }
	}
}
