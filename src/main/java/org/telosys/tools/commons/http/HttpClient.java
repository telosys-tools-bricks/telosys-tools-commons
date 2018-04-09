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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public class HttpClient {

	public static final String VERSION = "4 (2018-04-09)" ;

	// Default "User-Agent" value 
	private static final String USER_AGENT       = "User-Agent" ;
	private static final String USER_AGENT_VALUE = "Telosys-HttpClient" ;

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
	//------------------------------------------------------------------------------
	
	private final HttpClientConfig configuration ;
	private boolean isConfigured = false ;
	
	/**
	 * Constructor without http configuration (no proxy)
	 */
	public HttpClient() {
		super();
		this.configuration = null;
		init();
	}
	
	/**
	 * Constructor with http configuration (proxy)
	 * @param configuration
	 */
	public HttpClient(HttpClientConfig configuration) {
		super();
		this.configuration = configuration;
		init();
	}
	
	/**
	 * Constructor with http configuration defined by properties
	 * @param proxyProperties proxy properties or null if none
	 */
	public HttpClient(Properties proxyProperties) {
		super();
		if ( proxyProperties != null ) {
			this.configuration = new HttpClientConfig(proxyProperties);
		}
		else {
			this.configuration = null ;
		}
		init();
	}
	
	/**
	 * Post construction initialization
	 */
	private final void init() {
		if ( ! isConfigured ) {
			if ( configuration != null ) {
				if ( configuration.isUseSystemProxies() ) {
					useSystemProxies() ;
				}
				else {
					proxyConfig( configuration.getHttpProxy() );
					proxyConfig( configuration.getHttpsProxy() );
				}
			}
			else {
				useSystemProxies() ;
			}
			isConfigured = true ;
		}
		configTLSv2();
	}
	
	private void proxyConfig(HttpProxy proxy ) {
		if ( proxy != null ) {
			Properties systemProperties = System.getProperties();		
			systemProperties.setProperty( proxy.getProtocol() + ".proxySet",  "true");
			systemProperties.setProperty( proxy.getProtocol() + ".proxyHost", proxy.getHost() );
			systemProperties.setProperty( proxy.getProtocol() + ".proxyPort", String.valueOf(proxy.getPort()) );
			if ( proxy.getUser() != null ) {
				systemProperties.setProperty( proxy.getProtocol() + ".proxyUser", String.valueOf(proxy.getUser()) );
				if ( proxy.getPassword() != null ) {
					systemProperties.setProperty( proxy.getProtocol() + ".proxyPassword", String.valueOf(proxy.getPassword()) );
				}
			}
		}
	}
	
	private void useSystemProxies() {
		Properties systemProperties = System.getProperties();
		systemProperties.setProperty( "java.net.useSystemProxies",  "true");
	}	

	//---------------------------------------------------------------------
	// GET
	//---------------------------------------------------------------------
	public HttpResponse get(HttpRequest request)  throws Exception {
		return get(request.getURL(), request.getHeadersMap());
	}
	
	public HttpResponse get(String url, Map<String, String> headers)  throws Exception {
		return get(getURL(url), headers);
	}
	
	private HttpResponse get(URL url, Map<String, String> headers) throws Exception {
		return process(url, "GET", headers, null);
	}

	//---------------------------------------------------------------------
	// HEAD
	//---------------------------------------------------------------------
	public HttpResponse head(HttpRequest request)  throws Exception {
		return head(request.getURL(), request.getHeadersMap());
	}
	
	public HttpResponse head(String url, Map<String, String> headers)  throws Exception {
		return head(getURL(url), headers);
	}
	
	private HttpResponse head(URL url, Map<String, String> headers ) throws Exception {
		return process(url, "HEAD", headers, null);
	}

	//---------------------------------------------------------------------
	// POST
	//---------------------------------------------------------------------
	public HttpResponse post(HttpRequest request)  throws Exception {
		return post(request.getURL(), request.getHeadersMap(), request.getContent());
	}
	
	public HttpResponse post(String url, Map<String, String> headers, byte[] data) throws Exception {
		return post(getURL(url), headers, data );
	}

	private HttpResponse post(URL url, Map<String, String> headers, byte[] data) throws Exception {
		return process(url, "POST", headers, data);
	}

	//---------------------------------------------------------------------
	// PUT
	//---------------------------------------------------------------------
	public HttpResponse put(HttpRequest request)  throws Exception {
		return put(request.getURL(), request.getHeadersMap(), request.getContent());
	}
	
	public HttpResponse put(String url, Map<String, String> headers, byte[] data) throws Exception {
		return put(getURL(url), headers, data );
	}
	
	private HttpResponse put(URL url, Map<String, String> headers, byte[] data) throws Exception {
		return process(url, "PUT", headers, data);
	}

	//---------------------------------------------------------------------
	// DELETE
	//---------------------------------------------------------------------
	public HttpResponse delete(HttpRequest request)  throws Exception {
		return delete(request.getURL(), request.getHeadersMap());
	}
	
	public HttpResponse delete(String url, Map<String, String> headers ) throws Exception {
		return delete(getURL(url), headers);
	}
	
	private HttpResponse delete(URL url, Map<String, String> headers ) throws Exception {
		return process(url, "DELETE", headers, null);
	}

	//---------------------------------------------------------------------
	// Private methods
	//---------------------------------------------------------------------
	/**
	 * Processes the given http method and returns the HttpResponse
	 * @param url the URL to be used
	 * @param method the http method ( GET, POST, PUT, DELETE, HEAD, etc)
	 * @param headers http headers 
	 * @param data the data to be posted  
	 * @return
	 * @throws Exception
	 */
	private HttpResponse process(URL url, String method, Map<String, String> headers, byte[] data) throws Exception {
		HttpURLConnection connection = connect(url, method, headers);
		if ( data != null ) {
			postData(connection, data);
		}
		HttpResponse response = new HttpResponse(connection);
		connection.disconnect();
		return response ;
	}
	
	//---------------------------------------------------------------------
	private URL getURL(String sUrl) throws Exception {
		URL url = null;
		try {
			url = new URL(sUrl);
		} catch (Exception e) {
			throw new Exception("Invalid URL");
		}
		return url;
	}

	//---------------------------------------------------------------------
	/**
	 * Establishes the network connection for the given URL and retursn a HttpURLConnection instance
	 * @param url
	 * @param method the http method to be used 
	 * @param headers the headers to be set (can be null)
	 * @return 
	 * @throws Exception
	 */
	private HttpURLConnection connect(URL url, String method, Map<String, String> headers) throws Exception {
		HttpURLConnection connection = null;
		try {
			// url.openConnection() :
			//  Creates a new instance of URLConnection 
			//  It should be noted that a URLConnection instance does not establish the actual network connection on creation. 
			//  This will happen only when calling URLConnection.connect().
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod(method);
			
			//--- Set the default User-Agent value if not defined in the given headers
			setDefaultUserAgentIfNecessary(connection, headers); 
			
			//--- Set http headers if any
			if ( headers != null ) {
				for ( String name : headers.keySet() ) {
					connection.setRequestProperty(name, headers.get(name));
				}
			}
			
			// connection.connect() : 
			//  Opens a communications link to the resource referenced by the URL, 
			//  if such a connection has not already been established. 
			connection.connect();
			return connection;
		} catch (Exception e) {
			throw new Exception("Connection failed");
		}
	}
	
	//---------------------------------------------------------------------
	/**
	 * Set the default User-Agent if not defined in the given headers
	 * NB : for GitHub API the User-Agent is required : https://developer.github.com/v3/#user-agent-required 
	 * @param connection
	 * @param headers
	 */
	private void setDefaultUserAgentIfNecessary(HttpURLConnection connection, Map<String, String> headers) {
		if ( headers != null && headers.containsKey(USER_AGENT) ) {
			// User-Agent is defined in the given headers => nothing to do
			return ;
		}
		// User-Agent is not defined in the given headers => set the default value
		connection.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);		
	}
	
	//---------------------------------------------------------------------
	private void postData(HttpURLConnection connection, byte[] data) throws Exception {
		try {
			OutputStream os = connection.getOutputStream();
			os.write(data);
			os.flush();
			os.close();
		} catch (Exception e) {
			throw new Exception("Cannot post date (unable to write in output stream)");
		}
	}
	//---------------------------------------------------------------------
	/**
	 * Downloads a file from the given URL to the given file name 
	 * @param url the URL to be downloaded
	 * @param destFileName the destination for the downloaded file 
	 * @return the number of bytes (file size)
	 */
	public long downloadFile(String url, String destFileName ) throws Exception {
		return downloadFileV2(getURL(url), destFileName );
	}
	
	private static final int BUFFER_SIZE = 128 * 1024 ; // 128 k
	
	/**
	 * Dowloads a file from the given URL and store it in the given filename
	 * @param url
	 * @param destFileName
	 * @return
	 * @throws Exception, IOException
	 */
	private long downloadFileV2(URL url, String destFileName) throws Exception {

		checkDestination(destFileName);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		setDefaultUserAgentIfNecessary(connection, null);
		
		// connection.connect() : 
		//  Opens a communications link to the resource referenced by the URL, 
		//  if such a connection has not already been established. 
		//  NB : If Java 7 without TLS v1.2 on GitHub URL : "SSL exception : Received fatal alert protocol_exception"
		connection.connect(); 
		int responseCode = connection.getResponseCode(); 
		
		// check HTTP response code first
		if ( responseCode != HttpURLConnection.HTTP_OK ) {
			throw new Exception ("Unexpected HTTP Response Code " + responseCode);
		}
		
		// String contentType = connection.getContentType();
		
        long totalBytesRead = 0L;		
		try (	InputStream      inputStream  = connection.getInputStream(); 
				FileOutputStream outputStream = new FileOutputStream(destFileName) ) {
			
			int bytesRead = -1;
	        byte[] buffer = new byte[BUFFER_SIZE];
	        while ( (bytesRead = inputStream.read(buffer)) != -1 ) {
	            outputStream.write(buffer, 0, bytesRead);
	            totalBytesRead += bytesRead;
	        }
		}		        
		return totalBytesRead ;
	}
	
//	private long downloadFileViaURL(URL url, String destFileName ) throws Exception {
//
//		checkDestination(destFileName);
//		
//        long totalBytesRead = 0L;		
//        try {
//			url.openConnection();
//			InputStream reader = url.openStream();
//			
//	        FileOutputStream writer = new FileOutputStream(destFileName);
//	        byte[] buffer = new byte[BUFFER_SIZE];
//	        int bytesRead = 0;
//	 
//	        while ((bytesRead = reader.read(buffer)) > 0)
//	        {  
//	           writer.write(buffer, 0, bytesRead);
//	           buffer = new byte[BUFFER_SIZE];
//	           totalBytesRead += bytesRead;
//	        }
//
//	        writer.close();
//	        reader.close();
//	 
//		} catch (IOException e) {
//			throw new Exception ("IOException", e);
//		}
//		return totalBytesRead ;
//	}
	
	private void checkDestination(String destFileName) throws Exception {
		File file = new File (destFileName) ;
		File parent = file.getParentFile();
		if ( ! parent.exists() ) {
			throw new Exception("Download folder doesn't exist '" + parent.getAbsolutePath() + "' ");
		}
	}
}
