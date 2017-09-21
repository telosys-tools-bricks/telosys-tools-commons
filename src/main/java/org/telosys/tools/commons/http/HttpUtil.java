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

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

public class HttpUtil {
	
	//------------------------------------------------------------------------------------------
	/**
	 * Return multi-line string containing all the current system properties regarding http/https proxy 
	 * @param separator separator line to be put between http and https (or null if no separator)
	 * @return
	 */
	public final static String getSystemProxyPropertiesAsString(String separator) {
		StringBuffer sb = new StringBuffer();
		getSystemProxyPropertiesAsString("http",  sb);
		if ( separator != null ) {
			sb.append(separator+"\n");
		}
		getSystemProxyPropertiesAsString("https", sb);
		return sb.toString();
	}
	//------------------------------------------------------------------------------------------
	private final static void getSystemProxyPropertiesAsString(String prefix, StringBuffer sb) {
		String[] names = { "proxyHost", "proxyPort", "proxySet", "nonProxyHosts"} ;
		Properties prop = System.getProperties();
		for ( String name : names ) {
			String fullName = prefix + "." + name ;
			String value = prop.getProperty(fullName) ;
			if ( value != null ) {
				sb.append(fullName + "=" + value + "\n");
			}
		}
	}
	
	//------------------------------------------------------------------------------------------
	public final static void showSystemProxies(PrintStream out) {
		// Show system proxies on Windows : 
		// > netsh winhttp show proxy" 
		// The proxy settings for WinHTTP are not the proxy settings for Microsoft Internet Explorer

		out.println("Current system proxies : ");
		//System.setProperty("java.net.useSystemProxies", "true");
		
		List<Proxy> list = null;
		try {
		    list = ProxySelector.getDefault().select(new URI("http://foo/bar"));
		} 
		catch (URISyntaxException e) {
		    e.printStackTrace();
		}
		if (list != null) {
			int n = 0 ;
		    for ( Proxy proxy : list ) {
		    	n++;
		    	out.println("Proxy #" + n + " : " );
		    	// Type type = proxy.type();
		    	// . DIRECT : Represents a direct connection, or the absence of a proxy.
		    	// . HTTP   : Represents proxy for high level protocols such as HTTP or FTP.
		    	// . SOCKS  : Represents a SOCKS (V4 or V5) proxy.
	    		out.println(" proxy type (DIRECT|HTTP|SOCKS) : " + proxy.type());
	    		
		    	InetSocketAddress addr = (InetSocketAddress) proxy.address();
		    	if (addr == null) {
		    		out.println(" no address (InetSocketAddress)");
		    	} else {
		    		out.println(" proxy host : " + addr.getHostName());
		    		out.println(" proxy port : " + addr.getPort());
		    	}
		    }
		}
	}
}
