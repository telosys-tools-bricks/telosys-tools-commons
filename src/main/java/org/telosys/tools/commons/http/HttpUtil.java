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
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtil {
	
	/**
	 * Private constructor
	 */
	private HttpUtil() {
		throw new IllegalStateException("Do not use constructor");
	}

	/**
	 * Accessing an HTTPS URL using the URL class results in an exception if the server's certificate chain cannot be validated <br>
	 * (not previously been installed in the truststore). <br>
	 * This method allows to disable the validation of certificates.<br>
	 * It overrides the default trust manager with one that trusts all certificates.<br>
	 * NB: <br>
	 *  Validation of X.509 certificates is essential to create secure SSL/TLS sessions not vulnerable to 'man-in-the-middle' attacks. <br>
	 *  Use this method only for tests or in very special cases! <br>
	 */
	public static final void disableCertificateValidation() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { 
		    new X509TrustManager() {     
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
		            return new X509Certificate[0];
		        } 
		        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		        } 
		        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		        }
		    } 
		};
		// Install the all-trusting trust manager
		try {
		    SSLContext sc = SSLContext.getInstance("SSL"); 
		    sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (GeneralSecurityException e) {
		} 
	}
	
	//------------------------------------------------------------------------------------------
	public static final  void showSystemProxies(PrintStream out) {
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
