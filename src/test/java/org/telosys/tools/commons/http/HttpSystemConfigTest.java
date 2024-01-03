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
public class HttpSystemConfigTest extends TestCase {


	//==========================================================================================
	
	private void print(Properties p ) {
		System.out.println("Properties (size = " + p.size() + ") :");
		for (Map.Entry<Object,Object> entry : p.entrySet())  { 
			System.out.println(" . " + entry.getKey() + " = " + entry.getValue() );
		}
	}
	
	@Test
	public void test1() {
		System.out.println("--- Test 1 --- ");
		Properties p = HttpSystemConfig.getHttpSystemProperties();
		print(p);
	}

	@Test
	public void test2() {
		System.out.println("--- Test 2 --- ");
		HttpSystemConfig.init();
		Properties p = HttpSystemConfig.getHttpSystemProperties();
		print(p);
	}

	@Test
	public void test3() {
		System.out.println("--- Test 3 --- ");
		Properties initProp = new Properties();
		initProp.setProperty("aa", "aaaaa");
		initProp.setProperty("bb", "bbbbb");
		initProp.setProperty("http.proxyPort", "8080");
		HttpSystemConfig.init(initProp);
		Properties p = HttpSystemConfig.getHttpSystemProperties();
		print(p);
		assertNull(p.getProperty("aa"));
		assertEquals("8080", p.getProperty("http.proxyPort"));
	}
	
}
