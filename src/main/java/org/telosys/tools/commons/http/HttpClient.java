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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public class HttpClient {
	
	private final HttpClientConfig configuration ;
	private boolean isConfigured = false ;
	
	/**
	 * Constructor without http configuration (no proxy)
	 */
	public HttpClient() {
		super();
		this.configuration = null;
	}
	
	/**
	 * Constructor with http configuration (proxy)
	 * @param configuration
	 */
	public HttpClient(HttpClientConfig configuration) {
		super();
		this.configuration = configuration;
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
	}
	
	private final void config( HttpClientConfig configuration ) {
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
	public HttpResponse get(HttpRequest request)  throws Exception  
	{
		return get(request.getURL(), request.getHeadersMap());
	}
	
	public HttpResponse get(String url, Map<String, String> headers)  throws Exception  
	{
		return get(getURL(url), headers);
	}
	
	private HttpResponse get(URL url, Map<String, String> headers) throws Exception 
	{
		return process(url, "GET", headers, null);
	}

	//---------------------------------------------------------------------
	// HEAD
	//---------------------------------------------------------------------
	public HttpResponse head(HttpRequest request)  throws Exception  
	{
		return head(request.getURL(), request.getHeadersMap());
	}
	
	public HttpResponse head(String url, Map<String, String> headers)  throws Exception  
	{
		return head(getURL(url), headers);
	}
	
	private HttpResponse head(URL url, Map<String, String> headers ) throws Exception 
	{
		return process(url, "HEAD", headers, null);
	}

	//---------------------------------------------------------------------
	// POST
	//---------------------------------------------------------------------
	public HttpResponse post(HttpRequest request)  throws Exception  
	{
		return post(request.getURL(), request.getHeadersMap(), request.getContent());
	}
	
//	public HttpResponse post(String url, Map<String, String> headers, String data) throws Exception 
//	{
//		return post(getURL(url), headers, data.getBytes() );
//	}
	public HttpResponse post(String url, Map<String, String> headers, byte[] data) throws Exception 
	{
		return post(getURL(url), headers, data );
	}
	private HttpResponse post(URL url, Map<String, String> headers, byte[] data) throws Exception 
	{
		return process(url, "POST", headers, data);
	}

	//---------------------------------------------------------------------
	// PUT
	//---------------------------------------------------------------------
	public HttpResponse put(HttpRequest request)  throws Exception  
	{
		return put(request.getURL(), request.getHeadersMap(), request.getContent());
	}
//	public HttpResponse put(String url, Map<String, String> headers, String data) throws Exception 
//	{
//		return put(getURL(url), headers, data.getBytes() );
//	}
	public HttpResponse put(String url, Map<String, String> headers, byte[] data) throws Exception 
	{
		return put(getURL(url), headers, data );
	}
	private HttpResponse put(URL url, Map<String, String> headers, byte[] data) throws Exception 
	{
		return process(url, "PUT", headers, data);
	}

	//---------------------------------------------------------------------
	// DELETE
	//---------------------------------------------------------------------
	public HttpResponse delete(HttpRequest request)  throws Exception  
	{
		return delete(request.getURL(), request.getHeadersMap());
	}
	
	public HttpResponse delete(String url, Map<String, String> headers ) throws Exception 
	{
		return delete(getURL(url), headers);
	}
	private HttpResponse delete(URL url, Map<String, String> headers ) throws Exception 
	{
		return process(url, "DELETE", headers, null);
	}

	//---------------------------------------------------------------------
	// Private methods
	//---------------------------------------------------------------------
	private HttpResponse process(URL url, String method, Map<String, String> headers, byte[] data) throws Exception 
	{
		config(configuration);
		
		HttpURLConnection connection = connect(url, method, headers);
		if ( data != null )
		{
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
	private HttpURLConnection connect(URL url, String method, Map<String, String> headers) throws Exception {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod(method);
			
			//--- Set http headers if any
			if ( headers != null ) {
				for ( String name : headers.keySet() ) {
					connection.setRequestProperty(name, headers.get(name));
				}
			}
			
			connection.connect();
			return connection;
		} catch (Exception e) {
			throw new Exception("Connection failed");
		}
	}

	//---------------------------------------------------------------------
//	public void disconnect() {
//		connection.disconnect();
//	}

	//---------------------------------------------------------------------
//	public void displayResponse() throws Exception {
//		String line;
//
//		try {
//			BufferedReader s = new BufferedReader(new InputStreamReader(
//					connection.getInputStream()));
//			line = s.readLine();
//			while (line != null) {
//				System.out.println(line);
//				line = s.readLine();
//			}
//			s.close();
//		} catch (Exception e) {
//			throw new Exception("Unable to read input stream");
//		}
//	}
//
	//---------------------------------------------------------------------
	private void postData(HttpURLConnection connection, byte[] data) throws Exception 
	{
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
//	private void postData(HttpURLConnection connection, String s) throws Exception 
//	{
//		try {
//			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
//					connection.getOutputStream()));
//			bw.write(s, 0, s.length());
//			bw.flush();
//			bw.close();
//		} catch (Exception e) {
//			throw new Exception("Cannot post date (unable to write in output stream)");
//		}
//	}
	
	/**
	 * Downloads a file from the given URL to the given file name 
	 * @param url the URL to be downloaded
	 * @param destFileName the destination for the downloaded file 
	 * @return the number of bytes (file size)
	 */
	public long downloadFile(String url, String destFileName ) throws Exception {
		return downloadFile(getURL(url), destFileName );
	}
	
	private long downloadFile(URL url, String destFileName ) throws Exception {

		config(configuration);
		checkDestination(destFileName);
		
        long totalBytesRead = 0L;		
        try {
			url.openConnection();
			InputStream reader = url.openStream();
			
	        FileOutputStream writer = new FileOutputStream(destFileName);
	        int BUFFER_SIZE = 128 * 1024 ;
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int bytesRead = 0;
	 
	        while ((bytesRead = reader.read(buffer)) > 0)
	        {  
	           writer.write(buffer, 0, bytesRead);
	           buffer = new byte[BUFFER_SIZE];
	           totalBytesRead += bytesRead;
	        }

	        writer.close();
	        reader.close();
	 
		} catch (IOException e) {
			throw new Exception ("IOException", e);
		}
		return totalBytesRead ;
	}
	
	private void checkDestination(String destFileName) throws Exception {
		File file = new File (destFileName) ;
		File parent = file.getParentFile();
		if ( parent.exists() == false ) {
			throw new Exception("Download folder doesn't exist '" + parent.getAbsolutePath() + "' ");
		}
	}
}
