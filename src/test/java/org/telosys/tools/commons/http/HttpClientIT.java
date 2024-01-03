package org.telosys.tools.commons.http;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
public class HttpClientIT extends TestCase {

	private static final String URL_GOOGLE_FR  = "https://www.google.fr" ;
	private static final String URL_GITHUB_COM = "https://github.com/telosys-templates" ;
	private static final String URL_GITHUB_API = "https://api.github.com/users/telosys-templates/repos";
	private static final boolean PRINT_HEADERS = false ;
	
	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Cache-Control",   "max-age=0");
		headers.put("Accept-Encoding", "gzip,deflate");		
		// headers.put("User-Agent",      "Telosys-HttpClient"); // Set by default
		return headers ;
	}

	private HttpClient getHttpClient() {
		Properties properties = TestsEnv.loadSpecificProxyProperties();
		return new HttpClient(properties);
	}
	
	private void checkResponse(HttpResponse response, int expectedRetCode) {
		assertEquals(expectedRetCode, response.getStatusCode());
		println(" Ret Code = " +response.getStatusCode() );
		
		if ( PRINT_HEADERS ) {
			println("Header keys : ");
			Map<String,List<String>> map = response.getHeaderMap();
			for ( String key : map.keySet() ) {
				println(" . '" + key + "'");
			}
			println(" Header 'cache-control' : " + response.getHeader("Cache-Control") );
			println(" Header 'content-encoding' : " + response.getHeader("Content-Encoding") );
			println(" Header 'xx-yy-zz' : " + response.getHeader("xx-yy-zz") );
		}
		
		println(" ---------- " );
	}
	
	private void println(String s) {
		System.out.println(s);
	}
	private void printStatus(HttpResponse httpResponse) {
		println("Status code : " + httpResponse.getStatusCode() );
	}
	private void printHeader(HttpResponse httpResponse, String headerName) {
		println("Header '" + headerName + "' : " + httpResponse.getHeader(headerName) );
	}

	private void printBody(HttpResponse httpResponse) {
		println("Body : " );
		String body = new String(httpResponse.getBodyContent(), StandardCharsets.UTF_8);
		println(body);
	}
	

	//==========================================================================================

	private HttpRequest buildRequest(String url) {
		return new HttpRequest(url, getHeaders() );
	}
	private void httpGet(HttpClient httpClient) throws Exception {
		println("--- Test http GET : " + URL_GOOGLE_FR );
		checkResponse(httpClient.get(URL_GOOGLE_FR, getHeaders()), 200);
		
		println("--- Test http GET : " + URL_GOOGLE_FR );
		checkResponse(httpClient.get(buildRequest(URL_GOOGLE_FR)), 200);
		
		println("--- Test http GET : " + URL_GITHUB_COM );
		checkResponse(httpClient.get(buildRequest(URL_GITHUB_COM)), 200);
	}
	private void httpGetGitHubAPI(HttpClient httpClient) throws Exception {
		HashMap<String,String> headers = new HashMap<>();
		println("--- Test http GET : " + URL_GITHUB_API );
		HttpResponse httpResponse = httpClient.get(URL_GITHUB_API, headers);
		checkResponse(httpResponse, 200);
		
		// printBody(httpResponse);
		printStatus(httpResponse);
		printHeader(httpResponse, "X-RateLimit-Limit");
		printHeader(httpResponse, "X-RateLimit-Used");
		printHeader(httpResponse, "X-RateLimit-Remaining");
		printHeader(httpResponse, "X-RateLimit-Resource");
		printHeader(httpResponse, "X-RateLimit-Reset");
	}
	
	public void testHttpGet() throws Exception {
		httpGet(getHttpClient());
		httpGet(new HttpClient());
	}
	
	public void testGetGitHubAPI() throws Exception {
		httpGetGitHubAPI(getHttpClient());
		httpGetGitHubAPI(new HttpClient());
	}
	
	//==========================================================================================
	
	public void testHead1() throws Exception {
		println("--- Test http HEAD 1 --- ");		
		HttpClient httpClient = getHttpClient();
		checkResponse(httpClient.head(URL_GOOGLE_FR, getHeaders()), 200);
	}
	
	public void testHead2() throws Exception {
		println("--- Test http HEAD 2 --- ");		
		HttpClient httpClient = getHttpClient();		
		HttpRequest request = new HttpRequest(URL_GOOGLE_FR, getHeaders() );
		checkResponse(httpClient.head(request), 200);
	}
	
	public void testHead3() throws Exception {
		println("--- Test http HEAD 3 --- ");		
		HttpClient httpClient = getHttpClient();
		checkResponse(httpClient.head(URL_GITHUB_COM, getHeaders()), 200);
	}
	
	//==========================================================================================
	
	public void testDelete1() throws Exception {
		println("--- Test http DELETE 1 --- ");		
		HttpClient httpClient = getHttpClient();
		checkResponse(httpClient.delete(URL_GOOGLE_FR, getHeaders()), 405); // HTTP 405 : Method not allowed
	}
	
	public void testDelete2() throws Exception {
		println("--- Test http DELETE 2 --- ");		
		HttpClient httpClient = getHttpClient();
		HttpRequest request = new HttpRequest(URL_GOOGLE_FR, getHeaders() );
		checkResponse(httpClient.delete(request), 405); // HTTP 405 : Method not allowed
	}
	
	//==========================================================================================
	public void testDownload1() throws Exception {
		println("Test DOWNLOAD 1 via HTTP ... ");
		
		String urlString = "http://www.telosys.org/index.html";
		String downloadFileName = TestsEnv.getTmpExistingFolderFullPath("download") + "/file1.tmp" ;

		HttpClient httpClient = getHttpClient();
		long r = httpClient.downloadFile(urlString, downloadFileName);
		println("File " + urlString + " donwloaded (" + r + " bytes)");
	}

	public void testDownload2() throws Exception {
		println("Test DOWNLOAD 2 via HTTPS (SSL)... ");
		
		// NB : https can raise an error ( javax.net.ssl.SSLException ) 
		HttpUtil.disableCertificateValidation(); // To avoid SSL error (due to self signed certificate)
		
		String urlString = "https://www.telosys.org/index.html";
		String downloadFileName = TestsEnv.getTmpExistingFolderFullPath("download") + "/file2.tmp" ;

		HttpClient httpClient = getHttpClient();
		long r = httpClient.downloadFile(urlString, downloadFileName);
		println("File " + urlString + " donwloaded (" + r + " bytes)");
	}
}
