package org.telosys.tools.commons.http;

import org.telosys.tools.commons.http.HttpUtil;

public class HttpUtilMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Getting current system proxies ...");
		
		HttpUtil.showSystemProxies(System.out);
		
		System.out.println("Done.");
	}

}
