package org.telosys.tools.commons.http;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 
 */
public class Base64Test {

	public void encodeDecodeBytes(String s0) {
		
		byte[] r1 = Base64.encode(s0.getBytes());
		String s1 = new String(r1) ;
		System.out.println(s0 + " --> Encoded --> " + s1 );
		
		byte[] r2 = Base64.decode(r1);
		String s2 = new String(r2);
		System.out.println(s1 + " --> Decoded --> " + s2 );
		
		assertEquals(s0, s2);
		System.out.println("---");
	}
	
	public void encodeDecodeString(String s0) {
		
		String s1 = Base64.encode(s0);
		System.out.println(s0 + " --> Encoded --> " + s1 );
		
		String s2 = Base64.decode(s1);
		System.out.println(s1 + " --> Decoded --> " + s2 );
		
		assertEquals(s0, s2);
		System.out.println("---");
	}
	
	public void encodeDecode(String s) {
		encodeDecodeBytes(s);
		encodeDecodeString(s);
	}
	
	@Test
	public void testEncodeDecode()   {
		encodeDecode("abcdefghij");
		encodeDecode("abc def ghi");
		encodeDecode("a-b+c/d*e f.g:h,i!j?k,l$m^");
		
	}
	
}
