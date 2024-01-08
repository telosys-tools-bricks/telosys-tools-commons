package org.telosys.tools.commons.http;

import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.Test;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

/**
 * HttpClient test case.
 * 
 * NB : if HttpClient is created without configuration the last configuration is still active 
 * ( because stored as System Properties )
 *  
 * @author L. Guerin
 *
 */
public class HttpClientWithMockServerIT extends TestCase {

	private static final String URL_GET_PLAIN_TEXT  = "https://run.mocky.io/v3/63c64be4-fce9-4adf-a778-3db08c1338f9" ;
	
	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Cache-Control",   "max-age=0");
		headers.put("Accept-Encoding", "gzip,deflate");		
		// headers.put("User-Agent",      "Telosys-HttpClient"); // Set by default
		return headers ;
	}

	private HttpClient getHttpClient() {
//		Properties properties = TestsEnv.loadSpecificProxyProperties();
//		return new HttpClient(properties);	
		return new HttpClient(TestsEnv.getTestFile("proxy.properties"));
	}
	
	private void checkResponse(HttpResponse response, int expectedRetCode) {
		assertEquals(expectedRetCode, response.getStatusCode());
		System.out.println(" Ret Code = " +response.getStatusCode() );
		
		System.out.println("Header keys : ");
		Map<String,List<String>> map = response.getHeaderMap();
		for ( String key : map.keySet() ) {
			System.out.println(" . '" + key + "'");
		}
		
		System.out.println(" ---------- " );
	}
	
	private void printResponseBody(HttpResponse response) {
		String body = new String(response.getBodyContent());
		System.out.println(body);
	}	
	

	
	//==========================================================================================

	@Test
	public void testGet1() throws Exception {
		System.out.println("--- Test http GET 1 --- ");		
		HttpUtil.disableCertificateValidation(); // To avoid SSL error due to self signed certificate 
		
		HttpClient httpClient = new HttpClient();
		HttpResponse response = httpClient.get(URL_GET_PLAIN_TEXT, getHeaders());
		checkResponse(response, 200);
		printResponseBody(response);
	}
	
	@Test
	public void testGet2() throws Exception {
		System.out.println("--- Test http GET 2 --- ");		
		HttpUtil.disableCertificateValidation(); // To avoid SSL error due to self signed certificate 
		
		HttpClient httpClient = getHttpClient();
		HttpResponse response = httpClient.get(URL_GET_PLAIN_TEXT, null);
		checkResponse(response, 200);
		printResponseBody(response);
	}
	
}
