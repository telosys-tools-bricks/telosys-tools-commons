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

import java.util.Properties;

import org.telosys.tools.commons.StrUtil;

public class HttpClientConfig {

	private final boolean   useSystemProxies ;
	private final HttpProxy httpProxy ;
	private final HttpProxy httpsProxy ;
	
	public HttpClientConfig() {
		super();
		this.httpProxy  = null ;
		this.httpsProxy = null;
		this.useSystemProxies = true ;
	}

	public HttpClientConfig(HttpProxy httpProxy) {
		super();
		this.httpProxy  = httpProxy;
		this.httpsProxy = null;
		this.useSystemProxies = false ;
	}

	public HttpClientConfig(HttpProxy httpProxy, HttpProxy httpsProxy) {
		super();
		this.httpProxy  = httpProxy;
		this.httpsProxy = httpsProxy;
		this.useSystemProxies = false ;
	}
	
	public HttpClientConfig(Properties properties) {
		super();
		if (properties != null) {
			this.httpProxy  = getProxy(properties, "http");
			this.httpsProxy = getProxy(properties, "https");
			this.useSystemProxies = false ;
		}
		else {
			this.httpProxy  = null ;
			this.httpsProxy = null;
			this.useSystemProxies = true ;
		}
	}

	private HttpProxy getProxy(Properties properties, String protocol) {
		String host     = properties.getProperty( protocol+ ".proxyHost");
		String port     = properties.getProperty( protocol+ ".proxyPort");
		String user     = properties.getProperty( protocol+ ".proxyUser"); // can be null
		String password = properties.getProperty( protocol+ ".proxyPassword"); // can be null
		if ( host != null && port != null ) {
			int numPort = StrUtil.getInt(port,0);
			return new HttpProxy(protocol, host, numPort, user, password) ;
		}
		return null ;
	}
	
	public boolean isUseSystemProxies() {
		return useSystemProxies;
	}

//	public void setUseSystemProxies(boolean useSystemProxies) {
//		this.useSystemProxies = useSystemProxies;
//	}

	public HttpProxy getHttpProxy() {
		return httpProxy;
	}

	public HttpProxy getHttpsProxy() {
		return httpsProxy;
	}
	
	
}
