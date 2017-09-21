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

public class HttpProxy {

	public final static String ALL   = "*" ;
	public final static String HTTP  = "http" ;
	public final static String HTTPS = "https" ;
	public final static String FTP   = "ftp" ;
	
	private final String protocol ;
	private final String host ;
	private final int    port ;
	private final String user ;
	private final String password ;
	private final String nonProxyHosts ;
	
	public HttpProxy(String protocol, String host, int port) {
		super();
		this.protocol = protocol ;
		this.host = host;
		this.port = port;
		this.user     = null;
		this.password = null;
		this.nonProxyHosts = null ;
	}
	
	public HttpProxy(String protocol, String host, int port, String nonProxyHosts) {
		super();
		this.protocol = protocol ;
		this.host = host;
		this.port = port;
		this.user     = null;
		this.password = null;
		this.nonProxyHosts = nonProxyHosts ;
	}
	
	public HttpProxy(String protocol, String host, int port, String user, String password ) {
		super();
		this.protocol = protocol ;
		this.host     = host;
		this.port     = port;
		this.user     = user;
		this.password = password;
		this.nonProxyHosts = null ;
	}
	
	public HttpProxy(String protocol, String host, int port, String user, String password, String nonProxyHosts ) {
		super();
		this.protocol = protocol ;
		this.host     = host;
		this.port     = port;
		this.user     = user;
		this.password = password;
		this.nonProxyHosts = nonProxyHosts ;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getNonProxyHosts() {
		return nonProxyHosts;
	}
}
