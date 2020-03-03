package org.telosys.tools.commons.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

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

	private final static String URL_GET_PLAIN_TEXT  = "http://www.mocky.io/v2/5e5d2089320000c8ec43c6a1" ;
	
	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cache-Control",   "max-age=0");
		headers.put("Accept-Encoding", "gzip,deflate");		
		// headers.put("User-Agent",      "Telosys-HttpClient"); // Set by default
		return headers ;
	}

	private HttpClient getHttpClient() {
		return HttpTestConfig.getHttpClient();
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
		HttpClient httpClient = getHttpClient();
		HttpResponse response = httpClient.get(URL_GET_PLAIN_TEXT, getHeaders());
		checkResponse(response, 200);
		printResponseBody(response);
	}
	
}
