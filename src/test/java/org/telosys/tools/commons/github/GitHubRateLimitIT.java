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
	
	private void print(HttpResponse response) {
		print("getStatusCode    : " + response.getStatusCode() ) ;
		print("getStatusMessage : " + response.getStatusMessage() ) ;
		
		print("getContentEncoding : " + response.getContentEncoding() ) ;
		print("getContentType     : " + response.getContentType() ) ;
		print("getContentLength   : " + response.getContentLength() ) ;

		// OLD HEADER NAMES ( not work since May 2020  ? )
		print("Header 'X-RateLimit-Limit'     OLD : " + response.getHeader("X-RateLimit-Limit") ) ;
		print("Header 'X-RateLimit-Remaining' OLD : " + response.getHeader("X-RateLimit-Remaining") ) ;
		print("Header 'X-RateLimit-Reset'     OLD : " + response.getHeader("X-RateLimit-Reset") ) ;
		
		// Try with ""X-Ratelimit" ( "l" lowercase ) : new names since May 2020 ??? : OK 
		print("Header 'X-Ratelimit-Limit'     NEW : " + response.getHeader("X-Ratelimit-Limit") ) ;
		print("Header 'X-Ratelimit-Remaining' NEW : " + response.getHeader("X-Ratelimit-Remaining") ) ;
		print("Header 'X-Ratelimit-Reset'     NEW : " + response.getHeader("X-Ratelimit-Reset") ) ;
		
		print("----- BODY ----- : ") ;
		print(new String(response.getBodyContent())) ;
	}

	private void getRateLimit( String urlString ) throws Exception {

		HttpClient httpClient = new HttpClient();
		HttpResponse response;
		print("HTTP GET URL : " + urlString ) ;
		try {
			response = httpClient.get(urlString, null);
		} catch (Exception e) {
			throw new Exception ("HTTP 'GET' error", e);
		}
		print(response);
		GitHubRateLimit gitHubRateLimit = new GitHubRateLimit(response);
		
		print("GitHubRateLimit : " + gitHubRateLimit.getLimit() ) ;
		print(" . getLimit()     -> " + gitHubRateLimit.getLimit() ) ;
		print(" . getRemaining() -> " + gitHubRateLimit.getRemaining()) ;
		print(" . getReset()     -> " + gitHubRateLimit.getReset()) ;
		print(" . getLimitAsInt()     -> " + gitHubRateLimit.getLimitAsInt() ) ;
		print(" . getRemainingAsInt() -> " + gitHubRateLimit.getRemainingAsInt() ) ;
		print(" . getResetAsDate()    -> " + gitHubRateLimit.getResetAsDate() ) ;
	}
	
	@Test
	public void testGetRateLimitFromUserInfo() throws Exception  {
		getRateLimit( GitHubClient.GIT_HUB_HOST_URL + "/users/telosys-templates" );
	}
	
	@Test
	public void testGetRateLimit() throws Exception  {
		getRateLimit( GitHubClient.GIT_HUB_HOST_URL + "/rate_limit"); // 200 OK
	}
	
}
