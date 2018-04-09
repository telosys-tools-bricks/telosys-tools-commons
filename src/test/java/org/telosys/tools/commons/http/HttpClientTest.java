package org.telosys.tools.commons.http;

import java.util.HashMap;
import java.util.Map;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

import org.telosys.tools.commons.http.HttpClient;
import org.telosys.tools.commons.http.HttpRequest;
import org.telosys.tools.commons.http.HttpResponse;

/**
 * HttpClient test case.
 * 
 * NB : if HttpClient is created without configuration the last configuration is still active 
 * ( because stored as System Properties )
 *  
 * @author L. Guerin
 *
 */
public class HttpClientTest extends TestCase {

	private final static String URL_GOOGLE_FR  = "https://www.google.fr" ;
	private final static String URL_GITHUB_COM = "https://github.com/telosys-templates-v3" ;
	
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
		System.out.println(" ---------- " );
	}
	
	//==========================================================================================

	public void testGet1() throws Exception {
		System.out.println("--- Test http GET 1 --- ");		
		HttpClient httpClient = getHttpClient();
		checkResponse(httpClient.get(URL_GOOGLE_FR, getHeaders()), 200);
	}
	
	public void testGet2() throws Exception {
		System.out.println("--- Test http GET 2 --- ");		
		HttpClient httpClient = getHttpClient();
		HttpRequest request = new HttpRequest(URL_GOOGLE_FR, getHeaders() );
		checkResponse(httpClient.get(request), 200);
	}

	public void testGet3() throws Exception {
		System.out.println("--- Test http GET 3 --- ");		
		HttpClient httpClient = getHttpClient();
		checkResponse(httpClient.get(URL_GITHUB_COM, getHeaders()), 200);
	}
	
	//==========================================================================================
	
	public void testHead1() throws Exception {
		System.out.println("--- Test http HEAD 1 --- ");		
		HttpClient httpClient = getHttpClient();
		checkResponse(httpClient.head(URL_GOOGLE_FR, getHeaders()), 200);
	}
	
	public void testHead2() throws Exception {
		System.out.println("--- Test http HEAD 2 --- ");		
		HttpClient httpClient = getHttpClient();		
		HttpRequest request = new HttpRequest(URL_GOOGLE_FR, getHeaders() );
		checkResponse(httpClient.head(request), 200);
	}
	
	public void testHead3() throws Exception {
		System.out.println("--- Test http HEAD 3 --- ");		
		HttpClient httpClient = getHttpClient();
		checkResponse(httpClient.head(URL_GITHUB_COM, getHeaders()), 200);
	}
	
	//==========================================================================================
	
	public void testDelete1() throws Exception {
		System.out.println("--- Test http DELETE 1 --- ");		
		HttpClient httpClient = getHttpClient();
		checkResponse(httpClient.delete(URL_GOOGLE_FR, getHeaders()), 405); // HTTP 405 : Method not allowed
	}
	
	public void testDelete2() throws Exception {
		System.out.println("--- Test http DELETE 2 --- ");		
		HttpClient httpClient = getHttpClient();
		HttpRequest request = new HttpRequest(URL_GOOGLE_FR, getHeaders() );
		checkResponse(httpClient.delete(request), 405); // HTTP 405 : Method not allowed
	}
	
	//==========================================================================================
	public void testDownload1() throws Exception {
		System.out.println("Test DOWNLOAD 1 via HTTP ... ");
		
		String urlString = "http://www.telosys.org/index.html";
		String downloadFileName = TestsEnv.getTmpExistingFolderFullPath("download") + "/file1.tmp" ;

		HttpClient httpClient = getHttpClient();
		long r = httpClient.downloadFile(urlString, downloadFileName);
		System.out.println("File " + urlString + " donwloaded (" + r + " bytes)");
	}

	public void testDownload2() throws Exception {
		System.out.println("Test DOWNLOAD 2 via HTTPS ... ");
		
		String urlString = "https://api.github.com/users/telosys/repos" ;
		// NB : https can raise an error ( javax.net.ssl.SSLException ) => use http  for tests
		String downloadFileName = TestsEnv.getTmpExistingFolderFullPath("download") + "/file2.tmp" ;

		HttpClient httpClient = getHttpClient();
		long r = httpClient.downloadFile(urlString, downloadFileName);
		System.out.println("File " + urlString + " donwloaded (" + r + " bytes)");
	}
}
