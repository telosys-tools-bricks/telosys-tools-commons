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
package org.telosys.tools.commons.http;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class HttpSystemConfig {
	
	/**
	 * Private constructor
	 */
	private HttpSystemConfig() {
		throw new IllegalStateException("Do not use constructor");
	}

	//------------------------------------------------------------------------------
	// TLS ver 1.2 configuration ( TLS 1.2 not defined by default with Java 7 )  
	// TLS 1.2 is required for GitHub https URLs
	//------------------------------------------------------------------------------
	private static final String HTTPS_PROTOCOLS = "https.protocols" ;
	private static final String TLS_VER_1_2     = "TLSv1.2" ;

	private static final void configTLSv2() {
		String httpsProtocols = System.getProperty(HTTPS_PROTOCOLS);
		if ( httpsProtocols != null && httpsProtocols.contains(TLS_VER_1_2) ) {
			// TLS v 1.2 is already set in the system property
			return ; // Nothing to do
		}
		else {
			System.setProperty(HTTPS_PROTOCOLS, TLS_VER_1_2);
		}
	}

	private static final String JAVA_NET_USE_SYSTEM_PROXIES = "java.net.useSystemProxies" ;
	
	private static final List<String> HTTP_PROP_KEYS = Arrays.asList(
			"http.proxySet",  "http.proxyHost",  "http.proxyPort",  "http.proxyUser",  "http.proxyPassword",  "http.nonProxyHosts",
			"https.proxySet", "https.proxyHost", "https.proxyPort", "https.proxyUser", "https.proxyPassword", "https.nonProxyHosts",
			JAVA_NET_USE_SYSTEM_PROXIES,
			HTTPS_PROTOCOLS
			);
		
	/**
	 * Init HTTP System Properties with default values
	 */
	protected static final void init() {
		System.setProperty(JAVA_NET_USE_SYSTEM_PROXIES, "true");
		configTLSv2();
	}
	
	/**
	 * Init HTTP System Properties with the given properties <br>
	 * (only predefined keys are used : '"http.proxyHost', 'http.proxyPort', etc) 
	 * @param properties
	 */
	protected static final void init(Properties properties) {
		for (String key : properties.stringPropertyNames()) {
			// only known properties are set
			if ( HTTP_PROP_KEYS.contains(key) ) {
				System.setProperty(key, properties.getProperty(key));
			}
		}
		configTLSv2();
	}
	
	/**
	 * Returns the current HTTP System Properties <br>
	 * (only predefined keys : '"http.proxyHost', 'http.proxyPort', etc) 
	 * @return
	 */
	public static final Properties getHttpSystemProperties() {
		Properties result = new Properties();
		Properties systemProperties = System.getProperties();
		for (String key : HTTP_PROP_KEYS )  { 
			String value = systemProperties.getProperty(key) ;
			if ( value != null ) {
				result.setProperty(key, value);
			}
		}		
		return result;
	}
}
