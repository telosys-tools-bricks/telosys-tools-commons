package org.telosys.tools.commons.http;

public class HttpUtilMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Getting current system proxies ...");
		
		HttpUtil.showSystemProxies(System.out);
		
		System.out.println("Done.");
	}

}
