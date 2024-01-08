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

import java.io.File;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.telosys.tools.commons.PropertiesManager;

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

	private static final boolean isTLSv12AlreadySet() {
		String httpsProtocols = System.getProperty(HTTPS_PROTOCOLS);
		return ( httpsProtocols != null && httpsProtocols.contains(TLS_VER_1_2) );
	}
	
	private static final void setTLSv12() {
		if ( ! isTLSv12AlreadySet() ) {
			System.setProperty(HTTPS_PROTOCOLS, TLS_VER_1_2);
		}
	}

	private static final List<String> HTTP_PROP_KEYS = Arrays.asList(
			// see https://docs.oracle.com/javase/8/docs/api/java/net/doc-files/net-properties.html
			// Proxy config for HTTP
			"http.proxySet",  "http.proxyHost",  "http.proxyPort",  "http.proxyUser",  "http.proxyPassword",  "http.nonProxyHosts",
			// Proxy config for HTTPS
			"https.proxySet", "https.proxyHost", "https.proxyPort", "https.proxyUser", "https.proxyPassword", "https.nonProxyHosts",
			// Proxy config for SOCKETS (config for all 'sockets', can be overriden by HTTP/HTTPS/FTP configuration) 
			"socksProxyVersion", "socksProxyHost", "socksProxyPort", "java.net.socks.username", "java.net.socks.password",
			// Proxy config for FTP
			// FTP is useless for Telosys
			// Proxy config based on system properties 
			"java.net.useSystemProxies", // just for "getProperties" (property check only once at JVM startup)
			// TLS version (for Java 7)
			HTTPS_PROTOCOLS
			);
		
	/**
	 * Init HTTP System Properties with default values
	 */
	protected static final void init() {
		setTLSv12();
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
				String value = properties.getProperty(key);
				if ( value != null ) {
					String trimedValue = value.trim();
					if ( trimedValue.isEmpty() ) {
						System.clearProperty(key); // No value (explicit 'void' in properties file)
					}
					else {
						System.setProperty(key, trimedValue.trim()); // Value to use 
					}
				}
			}
		}
		setTLSv12();
	}
	
	/**
	 * Init HTTP System Properties from the given properties file 
	 * @param propertiesFile
	 */
	public static final void init(File propertiesFile) {
		PropertiesManager propertiesManager = new PropertiesManager(propertiesFile) ;
		Properties properties = propertiesManager.load(); // Return NULL if file not found
		if ( properties != null ) {
			// Properties loaded 
			init(properties);
		}
		else {
			// Properties file not found, no properties loaded : use default values
			init();
		}
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
	
	
	/**
	 * Returns the current http configuration after reloading the given properties file.
	 * @param propertiesFile
	 * @return
	 */
	public static List<String> getCurrentHttpConfig(File propertiesFile) {
		init(propertiesFile);
		return getCurrentHttpConfig();
	}
	
	/**
	 * Returns current http configuration (as is at current time)
	 * @return
	 */
	public static List<String> getCurrentHttpConfig() {
		List<String> lines = new LinkedList<>();
		lines.add("Http configuration (system properties) :");
		Properties systemProperties = System.getProperties();
		for (String k : HTTP_PROP_KEYS) {
			String v = systemProperties.getProperty(k);
			String v2 = "";
			if ( v != null ) {
				v2 = "'" + v + "'" ;
			}
			else {
				v2 = "(undefined)";
			}
			lines.add(" . '" + k + "' = " + v2);
		}
		return lines;
	}
	
	/**
	 * Get current proxy configuration (depends on current System Properties)
	 * @param protocol
	 */
	private static void getCurrentProxyConfig(String protocol, List<String> lines) {
		lines.add("Proxies for '" + protocol + "' :");
		URI uri;
		try {
			uri = new URI(protocol + "://foo");
			List<Proxy> proxies = ProxySelector.getDefault().select(uri);
			if ( proxies != null ) {
				for ( Proxy p : proxies ) {
					String address = "" ;
					if ( p.address() != null ) {
						address = "address '" + p.address() + "'";
					} 
					else {
						address = "(no address)";
					}
					// proxy type : enum : DIRECT, HTTP, SOCKS
					// "DIRECT" : no proxy 
					// "SOCKS" : low level proxy type used by default for all protocols => must be overriden for high level protocols
					// "HTTP" : proxy type used for high level protocols : "http", "https" and "ftp"
					lines.add(" - type '" + p.type() + "' : " + address);
				}
			}
			else {
				lines.add(" - no proxy");
			}
 		} catch (Exception e) {
			lines.add("Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
		}
	}

	/**
	 * Returns the current proxy configuration after reloading the given properties file.
	 * @param propertiesFile
	 * @return
	 */
	public static List<String> getCurrentProxyConfig(File propertiesFile) {
		init(propertiesFile);
		return getCurrentProxyConfig();
	}
	
	/**
	 * Returns current proxy configuration (as is at current time)
	 * @return
	 */
	public static List<String> getCurrentProxyConfig() {
		List<String> lines = new LinkedList<>();
		getCurrentProxyConfig("http", lines);
		getCurrentProxyConfig("https", lines);
		getCurrentProxyConfig("socket", lines);
		getCurrentProxyConfig("ftp", lines);
		return lines;
	}
}
