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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class HttpResponse {

	private int    statusCode    = 0 ;
	private String statusMessage = "" ;
	
	private int    contentLength   = 0 ;
	private String contentType     = "" ;
	private String contentEncoding = "" ;

	private Map<String, List<String>> headerFields = null ;
	
	private byte[]  bodyContent = new byte[0];
	private boolean bodyContentAccessible = false ;
	
	/**
	 * Constructor
	 * @param connection
	 * @throws IOException
	 */
	public HttpResponse(HttpURLConnection connection) throws IOException 
	{
		contentType     = connection.getContentType();
		
		// the content length of the resource that this connection's URL references, 
		// -1 if the content length is not known, or if the content length is greater than Integer.MAX_VALUE.
		contentLength   = connection.getContentLength(); // Can be -1 even if there's a content in the response (?)
		
		contentEncoding = connection.getContentEncoding();
		
		statusCode      = connection.getResponseCode();
		statusMessage   = connection.getResponseMessage();
			
		// Always try to read response body (whatever the response code)
		bodyContent = readResponseBody(connection);
		contentLength = bodyContent.length ;
		
		headerFields = connection.getHeaderFields();
	}
	
	private byte[] readResponseBody( HttpURLConnection connection ) throws IOException
	{
		byte[] body = new byte[0];
		InputStream is = getInputStream(connection);
		if ( is != null ) {
			body = readResponseBody(is);
			is.close();
		}
		return body;
	}
	
	/**
	 * Tries to get an InputStream to read the response body
	 * @param connection
	 * @return
	 */
	private InputStream getInputStream( HttpURLConnection connection ) 
	{
		InputStream is = null ;
		try {
			if ( connection.getResponseCode() >= 200 && connection.getResponseCode() <= 299 ) {
				is = connection.getInputStream();
				bodyContentAccessible = true ;
			}
			else {
				// Try to get the body from "ErrorStream"
				is = connection.getErrorStream(); // Typically for response code "4xx" 
				bodyContentAccessible = true ;
			}
		} catch (Exception e) {
			// Throws:
			//    . IOException - if an I/O error occurs while creating the input stream.
			//                    eg "FileNotFound" if http 404
			//    . UnknownServiceException - if the protocol does not support input.
			is = null ;
			bodyContentAccessible = false ;
		}
		return is ;
	}

	private byte[] readResponseBody( InputStream is ) throws IOException
	{
		byte[] buffer = new byte[1024] ; 
		int totalLength = 0 ;

		ByteArrayOutputStream baos = new ByteArrayOutputStream (1024);
		int len = 0 ;
		
		while ( ( len = is.read(buffer) ) > 0 )
		{
			baos.write(buffer, 0, len);
			totalLength = totalLength + len ;
		}
		baos.close();
		
		return baos.toByteArray();
	}
	
	public int getStatusCode() {
		return statusCode ;
	}
	
	public String getStatusMessage() {
		return statusMessage ;
	}
	
	public int getContentLength() {
		return contentLength ;
	}
	
	public String getContentType() {
		return contentType ;
	}
	
	public String getContentEncoding() {
		return contentEncoding ;
	}
	
	public byte[] getBodyContent() {
		return bodyContent ;
	}
	public boolean isBodyContentAccessible() {
		return bodyContentAccessible ;
	}
	
	public String getHeader(String name) {
		List<String> values = headerFields.get(name);
		if ( values != null  && ! values.isEmpty() ) {
			return values.get(0);
		}
		return null ;
	}
	
	public Map<String, List<String>> getHeaderMap() {
		return  headerFields;
	}
}
