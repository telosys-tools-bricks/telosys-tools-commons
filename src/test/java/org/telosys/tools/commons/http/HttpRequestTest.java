package org.telosys.tools.commons.http;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 
 */
public class HttpRequestTest {

	@Test
	public void test1()   {
		HttpRequest r = new HttpRequest("https://www.telosys.org/");
		
		assertEquals("https://www.telosys.org/", r.getURL());
		
		Map<String,String> headers = r.getHeadersMap();
		assertNotNull(headers);
		assertEquals(0, headers.size());
		
		byte[] content = r.getContent();
		assertNotNull(content);
		assertEquals(0, content.length);
		
		r.setContent("ABCD");
		content = r.getContent();
		assertNotNull(content);
		assertEquals(4, content.length);

		r.setContent("XYZ".getBytes());
		content = r.getContent();
		assertNotNull(content);
		assertEquals(3, content.length);

		r.setContent((String)null);
		content = r.getContent();
		assertNotNull(content);
		assertEquals(0, content.length);
	}

	@Test
	public void test2Headers()   {
		Map<String, String> initialHeaders = new HashMap<>();
		initialHeaders.put("A", "aaaa");
		initialHeaders.put("B", "bbb");
		HttpRequest r = new HttpRequest("http://doc.telosys.org/", initialHeaders);
		
		Map<String,String> headers = r.getHeadersMap();
		assertNotNull(headers);
		assertEquals(2, headers.size());
		
		assertEquals("aaaa", r.getHeader("A"));
		assertEquals("bbb", r.getHeader("B"));
		
		r.setHeader("C", "ccc");
		assertEquals(3, headers.size());
		assertEquals("ccc", r.getHeader("C"));
		
	}
}
