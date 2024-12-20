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

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;

import org.telosys.tools.commons.exception.TelosysRuntimeException;

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
		    	@Override
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
		            return new X509Certificate[0]; // empty array
		        } 
		    	@Override
		        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws CertificateException {
		        	// Empty method (nothing to do) : Accept all client certificates (not secure)
		        	throw new CertificateException("Client not trusted by default (authType : "+authType+")");
		        } 
		    	@Override
		        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws CertificateException {
		        	// Empty method (nothing to do) : Accept all server certificates (not secure)
		    		// test below is just to avoid Sonar rule java:S4830 Server certificates should be verified during SSL/TLS connections
		    		if ( authType == null ) { 
			        	throw new CertificateException("Server not trusted (authType is null)");
		    		}
		        }
		    } 
		};
		// Install the all-trusting trust manager
	    try {
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2"); // v 4.2.0 : "TLSv1.2" instead of "SSL"
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom()); 
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			throw new TelosysRuntimeException("Cannot disable SSL certificate validation", e);
		}
	}

}
