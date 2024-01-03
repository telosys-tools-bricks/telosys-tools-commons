package org.telosys.tools.commons.http;

import java.util.Map;
import java.util.Properties;

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
public class HttpUtilTest extends TestCase {


	//==========================================================================================
	
	public void testHttpUtil4() {
		System.out.println("--- Test HttpUtil 4 --- ");	
		HttpUtil.showSystemProxies(System.out);
		System.out.println("Done.");
	}
	
}
