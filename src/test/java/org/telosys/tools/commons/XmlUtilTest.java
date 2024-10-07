package org.telosys.tools.commons;

import junit.framework.TestCase;

import org.telosys.tools.commons.XmlUtil;

public class XmlUtilTest extends TestCase {

	public void testEscapeXml() {
		
		assertEquals("", XmlUtil.escapeXml(null) ) ;
		
		assertEquals("", XmlUtil.escapeXml("") ) ;

		assertEquals("abcd", XmlUtil.escapeXml("abcd") ) ;

		assertEquals("ab &lt;cd&gt;", XmlUtil.escapeXml("ab <cd>") ) ;

		assertEquals("ab&amp;cd", XmlUtil.escapeXml("ab&cd") ) ;

		assertEquals("ab&#034;cd&#034;", XmlUtil.escapeXml("ab\"cd\"") ) ;

		assertEquals("ab&#039;cd&#039;", XmlUtil.escapeXml("ab'cd'") ) ;
	}


}
