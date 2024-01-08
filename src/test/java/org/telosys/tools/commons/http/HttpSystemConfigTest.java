package org.telosys.tools.commons.http;

import java.util.List;
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
	
	private void print(Properties p ) {
		System.out.println("Properties (size = " + p.size() + ") :");
		for (Map.Entry<Object,Object> entry : p.entrySet())  { 
			System.out.println(" . " + entry.getKey() + " = " + entry.getValue() );
		}
	}
	private void print(List<String> list) {
		for (String s : list )  { 
			System.out.println(s);
		}
	}

	//==========================================================================================
	
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
		initProp.setProperty("http.proxyHost", " my.host   ");
		initProp.setProperty("http.proxyPort", " 8080 ");
		HttpSystemConfig.init(initProp);
		Properties p = HttpSystemConfig.getHttpSystemProperties();
		print(p);
		assertNull(p.getProperty("aa"));
		assertEquals("my.host", p.getProperty("http.proxyHost"));
		assertEquals("8080",    p.getProperty("http.proxyPort"));
	}

	@Test
	public void testGetCurrentHttpConfig() {
		System.out.println("--- Test : getCurrentHttpConfig --- ");
		//HttpSystemConfig.init();
		List<String> r = HttpSystemConfig.getCurrentHttpConfig();
		print(r);
	}

	@Test
	public void testGetCurrentProxyConfig() {
		System.out.println("--- Test : getCurrentProxyConfig() --- ");
		System.setProperty("https.proxyHost", "myhost");
		System.setProperty("https.proxyPort", "8080");
		System.setProperty("ftp.proxyHost", "myFTPproxyhost");
		System.setProperty("ftp.proxyPort", "1234");
		System.setProperty("socksProxyHost", "mysocksproxyhost");
		System.setProperty("socksProxyPort", "1080");
		
		List<String> r = HttpSystemConfig.getCurrentProxyConfig();
		print(r);
		// NB clear 'socks' properties to avoid impact on other tests
		System.clearProperty("socksProxyHost");
		System.clearProperty("socksProxyPort");
	}

}
