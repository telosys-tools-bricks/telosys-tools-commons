package org.telosys.tools.commons.http;

import junit.framework.TestCase;

import org.telosys.tools.commons.http.HttpUtil;

/**
 * HttpClient test case.
 * 
 * NB : if HttpClient is created without configuration the last configuration is still active 
 * ( because stored as System Properties )
 *  
 * @author L. Guerin
 *
 */
public class HttpUtilTest extends TestCase {


	//==========================================================================================
	
	public void testHttpUtil1() {
		System.out.println("--- Test HttpUtil 1 --- ");	
		String s = HttpUtil.getSystemProxyPropertiesAsString(null);
		System.out.println(s);
		System.out.println("Done.");
	}
	
	public void testHttpUtil2() {
		System.out.println("--- Test HttpUtil 2 --- ");	
		String s = HttpUtil.getSystemProxyPropertiesAsString("---");
		System.out.println(s);
		System.out.println("Done.");
	}
	
	public void testHttpUtil3() {
		System.out.println("--- Test HttpUtil 3 --- ");	
		String s = HttpUtil.getSystemProxyPropertiesAsString("");
		System.out.println(s);
		System.out.println("Done.");
	}
	
	public void testHttpUtil4() {
		System.out.println("--- Test HttpUtil 4 --- ");	
		HttpUtil.showSystemProxies(System.out);
		System.out.println("Done.");
	}
	
}
