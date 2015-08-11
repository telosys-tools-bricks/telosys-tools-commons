package org.telosys.tools.commons;

import junit.framework.TestCase;

import org.telosys.tools.commons.XmlUtil;

public class XmlUtilTest extends TestCase {

	public void testEscapeXml() {
		
		String s = null ;
		
		s = XmlUtil.escapeXml(null);
		assertTrue( "".equals(s) ) ;
		System.out.println(s);
		
		s = XmlUtil.escapeXml("");
		assertTrue( "".equals(s) ) ;
		System.out.println(s);

		s = XmlUtil.escapeXml("abcd");
		assertTrue( "abcd".equals(s) ) ;
		System.out.println(s);

		s = XmlUtil.escapeXml("ab <cd>");
		assertTrue( "ab &lt;cd&gt;".equals(s) ) ;
		System.out.println(s);

		s = XmlUtil.escapeXml("ab&cd");
		assertTrue( "ab&amp;cd".equals(s) ) ;
		System.out.println(s);

		s = XmlUtil.escapeXml("ab\"cd\"");
		assertTrue( "ab&#034;cd&#034;".equals(s) ) ;
		System.out.println(s);

		s = XmlUtil.escapeXml("ab'cd'");
		assertTrue( "ab&#039;cd&#039;".equals(s) ) ;
		System.out.println(s);
	}


}
