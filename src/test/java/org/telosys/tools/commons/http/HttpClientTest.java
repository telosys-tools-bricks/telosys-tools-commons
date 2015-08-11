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

	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cache-Control",   "max-age=0");
		headers.put("Accept-Encoding", "gzip,deflate");		
		headers.put("User-Agent",      "Apache-HttpClient/4.1.3 (java 1.5)");
		return headers ;
	}

	private HttpClient getHttpClient() {
//		//HttpClientConfig config = getNoProxyConfig();
//		HttpClientConfig config = getSpecificProxyConfig();
//		return new HttpClient(config);		
		return HttpTestConfig.getHttpClient();
	}
	
	//==========================================================================================

	public void testGet1() throws Exception {
		//showSystemProxies();
		System.out.println("--- Test http GET 1 --- ");		
		HttpClient c = getHttpClient();
		Map<String, String> headers = getHeaders();
		
		doGet(c, "http://myhttp.info/", headers, 200);
		doGet(c, "https://www.google.fr", headers, 200);
	}
	
	public void doGet(HttpClient httpClient, String url, Map<String, String> headers, int expectedRetCode) throws Exception {
		System.out.println("GET " + url + " ...");
		HttpResponse response = httpClient.get(url, headers);
		assertEquals(expectedRetCode, response.getStatusCode());
		System.out.println(" Ret Code = " +response.getStatusCode() );
		System.out.println(" ---------- " );
	}
	
	//==========================================================================================

	public void testGet2() throws Exception {
		System.out.println("--- Test http GET 2 --- ");		
		HttpClient c = getHttpClient();
		Map<String, String> headers = getHeaders();
		
		doGet(c, "http://myhttp.info/", headers, 200);
		doGet(c, "https://www.google.fr", headers, 200);
	}
	
	public void doGet(HttpClient httpClient, HttpRequest request, int expectedRetCode) throws Exception {
		System.out.println("GET " + request + " ...");
		HttpResponse response = httpClient.get(request);
		assertEquals(expectedRetCode, response.getStatusCode());
		System.out.println(" Ret Code = " +response.getStatusCode() );
		System.out.println(" ---------- " );
	}
	
	//==========================================================================================
	
	public void testHead1() throws Exception {
		//showSystemProxies();
		System.out.println("--- Test http HEAD 1 --- ");		
		HttpClient c = getHttpClient();
		Map<String, String> headers = getHeaders();
		
		doHead(c, "http://myhttp.info/", headers, 200);
		doHead(c, "https://www.google.fr", headers, 200);
	}
	
	public void doHead(HttpClient httpClient, String url, Map<String, String> headers, int expectedRetCode) throws Exception {
		System.out.println("HEAD " + url + " ...");
		HttpResponse response = httpClient.head(url, headers);
		assertEquals(expectedRetCode, response.getStatusCode());
		System.out.println(" Ret Code = " +response.getStatusCode() );
		System.out.println(" ---------- " );
	}
	
	//==========================================================================================
	
	public void testHead2() throws Exception {
		//showSystemProxies();
		System.out.println("--- Test http HEAD 2 --- ");		
		HttpClient c = getHttpClient();
		Map<String, String> headers = getHeaders();

		doHead(c, new HttpRequest("http://myhttp.info/",   headers), 200);
		doHead(c, new HttpRequest("https://www.google.fr", headers), 200);
	}
	
	public void doHead(HttpClient httpClient, HttpRequest request, int expectedRetCode) throws Exception {
		System.out.println("HEAD " + request + " ...");
		HttpResponse response = httpClient.head(request);
		assertEquals(expectedRetCode, response.getStatusCode());
		System.out.println(" Ret Code = " +response.getStatusCode() );
		System.out.println(" ---------- " );
	}
	
	//==========================================================================================
	
	public void testDelete2() throws Exception {
		//showSystemProxies();
		System.out.println("--- Test http DELETE 2 --- ");		
		HttpClient c = getHttpClient();
		Map<String, String> headers = getHeaders();
		
		doDelete(c, new HttpRequest("http://myhttp.info/", headers), 200);
	}
	
	public void doDelete(HttpClient httpClient, HttpRequest request, int expectedRetCode) throws Exception {
		System.out.println("DELETE 2 " + request + " ...");
		HttpResponse response = httpClient.delete(request);
		assertEquals(expectedRetCode, response.getStatusCode());
		System.out.println(" Ret Code = " +response.getStatusCode() );
		System.out.println(" ---------- " );
	}
	
	//==========================================================================================
	public void testDownload() throws Exception {
		//showSystemProxies();
		System.out.println("Test DOWNLOAD... ");
		HttpClient c = getHttpClient();
		
		String urlString = "https://api.github.com/users/telosys/repos" ;
		//long r = c.downloadFile(urlString, "D:/tmp/download/file1.tmp");
		String downloadFileName = TestsEnv.getTmpExistingFolderFullPath("download") + "/file1.tmp" ;
		long r = c.downloadFile(urlString, downloadFileName);
		System.out.println("File " + urlString + " donwloaded (" + r + " bytes)");
	}
}
