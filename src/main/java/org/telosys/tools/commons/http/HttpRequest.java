/**
 *  Copyright (C) 2008-2014  Telosys project org. ( http://www.telosys.org/ )
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

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

	private final static byte[] VOID_CONTENT = new byte[0];
	
	private final String url ;

	private final Map<String, String> headers ;
	
	private byte[]  content = VOID_CONTENT ;
	
	/**
	 * Constructor
	 * @param url
	 */
	public HttpRequest(String url) 
	{
		this.url = url ;
		this.headers = new HashMap<String, String>();
	}
	
	/**
	 * Constructor
	 * @param url
	 * @param initialHeaders http headers (copied in the request)
	 */
	public HttpRequest(String url, Map<String, String> initialHeaders ) 
	{
		this.url = url ;
		this.headers = new HashMap<String, String>();
		for ( String name : initialHeaders.keySet() ) {
			headers.put(name, initialHeaders.get(name) ) ;
		}
	}
	
	/**
	 * Returns the request URL
	 * @return
	 */
	public String getURL() {
		return url ;
	}
	
	/**
	 * Set a request http header
	 * @param name
	 * @param value
	 */
	public void setHeader(String name, String value) {
		this.headers.put(name, value);
	}
	
	/**
	 * Returns the current http headers
	 * @return
	 */
	public Map<String, String> getHeadersMap() {
		return  this.headers;
	}

	/**
	 * Set the request content (data to  be transmitted to the server)
	 * @param contentToSet
	 */
	public void setContent(String contentToSet) {
		if ( contentToSet != null ) {
			this.content = contentToSet.getBytes() ;
		}
		else {
			this.content = VOID_CONTENT ;
		}
	}
	
	/**
	 * Set the request content (data to  be transmitted to the server)
	 * @param contentToSet
	 */
	public void setContent(byte[] contentToSet) {
		if ( contentToSet != null ) {
			this.content = contentToSet ;
		}
		else {
			this.content = VOID_CONTENT ;
		}
	}
	
	/**
	 * Returns the request content (data to  be transmitted to the server)
	 * @return
	 */
	public byte[] getContent() {
		return  this.content;
	}

	@Override
	public String toString() {
		return "HttpRequest : " + url + "(content length = " + content.length + ")";
	}

}
