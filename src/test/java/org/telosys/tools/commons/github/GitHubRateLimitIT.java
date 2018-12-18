package org.telosys.tools.commons.github;

import org.junit.Test;
import org.telosys.tools.commons.http.HttpClient;
import org.telosys.tools.commons.http.HttpResponse;

/**
 * This class is "IT" ( Integration Test) 
 * 
 * See : https://developer.github.com/v3/#rate-limiting 
 *  
 * @author Laurent Guerin
 *
 */
public class GitHubRateLimitIT {

	private void print(String msg) {
		System.out.println(msg);
	}
	
	protected void getRateLimit( String urlString ) throws Exception {

		HttpClient httpClient = new HttpClient();
		HttpResponse response;
		print("HTTP GET URL : " + urlString ) ;
		try {
			response = httpClient.get(urlString, null);
		} catch (Exception e) {
			throw new Exception ("HTTP 'GET' error", e);
		}
		
		print("getStatusCode    : " + response.getStatusCode() ) ;
		print("getStatusMessage : " + response.getStatusMessage() ) ;
		
		print("getContentEncoding : " + response.getContentEncoding() ) ;
		print("getContentType     : " + response.getContentType() ) ;
		print("getContentLength   : " + response.getContentLength() ) ;

		print("Header 'X-RateLimit-Limit'     : " + response.getHeader("X-RateLimit-Limit") ) ;
		print("Header 'X-RateLimit-Remaining' : " + response.getHeader("X-RateLimit-Remaining") ) ;
		print("Header 'X-RateLimit-Reset'     : " + response.getHeader("X-RateLimit-Reset") ) ;
		
		print("----- BODY ----- : ") ;
		print(new String(response.getBodyContent())) ;
		
	}
	
	@Test
	public void testGetRateLimitFromUserInfo() throws Exception  {
		getRateLimit( GitHubClient.GIT_HUB_HOST_URL + "/users/" + GitHubTestsConst.GITHUB_USER );
	}
	
	@Test
	public void testGetRateLimit() throws Exception  {
		getRateLimit( GitHubClient.GIT_HUB_HOST_URL + "/rate_limit"); // 200 OK
	}
	
}
