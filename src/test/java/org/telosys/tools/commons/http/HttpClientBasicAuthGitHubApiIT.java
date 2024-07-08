package org.telosys.tools.commons.http;

import java.util.Base64;
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
public class HttpClientBasicAuthGitHubApiIT extends TestCase {

	private void printResponse(HttpResponse response) {
		System.out.println(" Ret Code = " +response.getStatusCode() );
		
		System.out.println("Header keys : ");
		Map<String,List<String>> map = response.getHeaderMap();
		for ( String key : map.keySet() ) {
			System.out.println(" . '" + key + "' = '" + map.get(key) + "'");
		}
//		System.out.println(" Header 'cache-control' : " + response.getHeader("Cache-Control") );
//		System.out.println(" Header 'content-encoding' : " + response.getHeader("Content-Encoding") );
//		System.out.println(" Header 'xx-yy-zz' : " + response.getHeader("xx-yy-zz") );
		
		System.out.println(" ---------- " );
	}
	
	//==========================================================================================

	public void test(String user, String password, int expectedStatusCode ) throws Exception {
		System.out.println("--- Test http GET with Basic Auth --- ");	
		
		String userPassword = user + ":" + password ;
		String userPasswordBase64 = Base64.getEncoder().encodeToString(userPassword.getBytes()); 
		
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Basic " + userPasswordBase64);
		
		HttpClient httpClient = new HttpClient();	
		HttpResponse response = httpClient.get("https://api.github.com/rate_limit", headers);
		printResponse(response);
		
		assertEquals(expectedStatusCode, response.getStatusCode());
	}

	@Test
	public void testGet1() throws Exception {
		System.out.println("--- Test http GET 1 --- ");	
		
		//test("aaa", "bbb", 401); // Bad : 401 Unauthorized
		test("aaa", "bbb", 200); // 200 OK it works in new GitHub API version
		
		// Test with a real user password below
		// test("xxx", "xxx", 200); // 200 OK
	}
	
	//==========================================================================================
}
