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

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ExternalConfiguration {
	
	/**
	 * Private constructor for static class
	 */
	public ExternalConfiguration() {	
	}
	
	/**
	 * Try to find an external configuration value in the following order : <br>
	 *  1) Java System Property <br>
	 *  2) JNDI Object <br>
	 *  3) Environment Variable <br>
	 * Return the value found or NULL if not found
	 * @param name
	 * @return
	 */
	public static String getValue(String name) {
		String value = null;

		//--- Try to get Java System Property ( App Server Level )
		value = getJavaSystemProperty(name) ;
		if ( value != null ) {
			return value ;
		}
		
		//--- Try to get JNDI Object  ( App Server Level )
		value = getJNDIStringObject(name) ;
		if ( value != null ) {
			return value ;
		}
		
		//--- Try to get Environment Variable  ( Operating System Level )
		value = getEnvironmentVariable(name) ;
		if ( value != null ) {
			return value ;
		}
		
		//--- Not found :-(
		return null;
	}

	protected static String getEnvironmentVariable(String name) {
		return System.getenv(name);
	}

	protected static String getJavaSystemProperty(String name) {
		return System.getProperty(name);
	}

	protected static String getJNDIStringObject(String name) {
		try {
			Object value = InitialContext.doLookup("java:comp/env/"+name);
			if ( value instanceof String ) {
				return (String) value ;
			}
		} catch (NamingException e) {
			// Object not found or invalid type => considered as "not found"
			return null ;
		}
		return null ;
	}

}
