package org.telosys.tools.commons.http;

import junit.env.telosys.tools.commons.TestsEnv;

import org.telosys.tools.commons.http.HttpClient;
import org.telosys.tools.commons.http.HttpClientConfig;
import org.telosys.tools.commons.http.HttpProxy;

public class HttpTestConfig {

	/**
	 * Returns a configuration for a specific proxy<br>
	 * 
	 * @return
	 */
	public static HttpClientConfig getSpecificProxyConfig() {
		//return new HttpClientConfig( getSpecificProxyProperties() );		
		return new HttpClientConfig( TestsEnv.loadSpecificProxyProperties() );		
	}

	/**
	 * Returns a configuration for a 'localhost:8888' proxy<br>
	 * Useful for Fiddler 
	 * @return
	 */
	public static HttpClientConfig getLocalhostProxyConfig() {
		HttpProxy httpProxy  = new HttpProxy(HttpProxy.HTTP,  "localhost", 8888);
		HttpProxy httpsProxy = new HttpProxy(HttpProxy.HTTPS, "localhost", 8888);
		return new HttpClientConfig(httpProxy, httpsProxy);
	}

	/**
	 * Returns a configuration for no proxy<br>
	 * 
	 * @return
	 */
	public static HttpClientConfig getNoProxyConfig() {
		return new HttpClientConfig();
	}
	
	/**
	 * Returns an instance of HttpClient with the default configuration <br>
	 * 
	 * @return
	 */
	public static HttpClient getHttpClient() {
		//HttpClientConfig config = getNoProxyConfig();
		HttpClientConfig config = getSpecificProxyConfig();
		return new HttpClient(config);		
	}
	
}
