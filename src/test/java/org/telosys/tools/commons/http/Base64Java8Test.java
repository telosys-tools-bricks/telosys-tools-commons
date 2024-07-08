package org.telosys.tools.commons.http;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 
 */
public class Base64Java8Test {
	
	private void log(String msg) {
		System.out.println(msg);
	}

	public void encodeDecode(String s0) {
		log("---");
		String s1 = java.util.Base64.getEncoder().encodeToString(s0.getBytes());		
		String s2 = new String( java.util.Base64.getDecoder().decode(s1) );
		log("[" + s0 + "] --> Encoded --> [" + s1 + "] --> Decoded --> [" + s2 + "]" );
		
		assertEquals(s0, s2);
	}
		
	@Test
	public void testEncodeDecode()   {
		encodeDecode("abcdefghij");
		encodeDecode("abc def ghi");
		encodeDecode("a-b+c/d*e f.g:h,i!j?k,l$m^");
		encodeDecode(" AZERT WW ZZ  ");
	}
	
}
