package org.telosys.tools.tests.commons.http;

import org.telosys.tools.commons.http.HttpClient;

public class HttpDownloaderMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Downloading...");
		
		String urlString = "https://api.github.com/users/telosys-tools/repos" ;
		String destination = "D:/tmp/download/file2.tmp" ;

		HttpClient httpClient = HttpTestConfig.getHttpClient() ;
		httpClient.downloadFile(urlString, destination);
		
		System.out.println("Done.");
	}

}
