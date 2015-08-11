package org.telosys.tools.commons;

import junit.framework.TestCase;

import org.telosys.tools.commons.StrUtil;

public class StrUtilTest extends TestCase {

	public void testDifferent() {
		assertTrue( StrUtil.different("aa", "bb") ) ;
		assertTrue( StrUtil.different(null, "bb") ) ;
		assertTrue( StrUtil.different(null, "") ) ;
		assertTrue( StrUtil.different("aa", null) ) ;
		assertTrue( StrUtil.different("", null) ) ;

		assertFalse( StrUtil.different("aa", "aa") ) ;
		assertFalse( StrUtil.different("",   "") ) ;
		assertFalse( StrUtil.different(null, null) ) ;

	}

	private void replaceVar(String s, String varName, String varValue, String expectedResult ) {
		System.out.println("Replace var " + varName + " in '" + s + "'");
		String result = StrUtil.replaceVar(s, varName, varValue) ;
		System.out.println("  result : '" + result + "'");
		assertEquals(expectedResult, result);
	}
	
	public void testReplaceVar() {
		replaceVar("aaaaabbbccccc",   "bbb",     "BBB",      "aaaaaBBBccccc") ;
		replaceVar("aaaaa[x]ccccc",   "[x]",     "X",        "aaaaaXccccc") ;
		replaceVar("aaaaa$TOTObbb",   "$TOTO",   "MyValue",  "aaaaaMyValuebbb") ;
		replaceVar("aaaaa${TOTO}bbb", "${TOTO}", "MyValue",  "aaaaaMyValuebbb") ;
		replaceVar("aaaaa${TOTO}bbb", "${TOTO}", "",         "aaaaabbb") ;
		replaceVar("aaaaa$bbb",       "$",       "DOLLAR",   "aaaaaDOLLARbbb") ;
	}

	public void testIdentical() {
		assertTrue( StrUtil.identical(null, null) ) ;
		assertTrue( StrUtil.identical("", "") ) ;
		assertTrue( StrUtil.identical("a", "a") ) ;
		assertTrue( StrUtil.identical("a ", "a ") ) ;
		assertTrue( StrUtil.identical("ab", "ab") ) ;

		assertFalse( StrUtil.identical("ab", "AB") ) ;
		assertFalse( StrUtil.identical("a ", " a") ) ;
		assertFalse( StrUtil.identical("", " ") ) ;
		assertFalse( StrUtil.identical(null, " ") ) ;
		assertFalse( StrUtil.identical(null, "") ) ;
		assertFalse( StrUtil.identical("",  null) ) ;
		assertFalse( StrUtil.identical(" ", null) ) ;
	}

	public void testGetIntegerObject() {
		assertNull( StrUtil.getIntegerObject(null) ) ;
		assertNull( StrUtil.getIntegerObject("") ) ;
		assertNull( StrUtil.getIntegerObject("  ") ) ;
		assertEquals( new Integer(123), StrUtil.getIntegerObject("123") ) ;
		assertEquals( new Integer(123), StrUtil.getIntegerObject(" 123") ) ;
		assertEquals( new Integer(123), StrUtil.getIntegerObject("123  ") ) ;
		assertEquals( new Integer(123), StrUtil.getIntegerObject("  123  ") ) ;
		assertEquals( new Integer(-123), StrUtil.getIntegerObject("-123  ") ) ;
		assertEquals( new Integer(-123), StrUtil.getIntegerObject("  -123  ") ) ;
		assertNull( StrUtil.getIntegerObject("12.34") ) ;
		assertNull( StrUtil.getIntegerObject("- 2") ) ;
		assertNull( StrUtil.getIntegerObject("+123") ) ;
		assertNull( StrUtil.getIntegerObject("  +123") ) ;

		//--- Check reverse conversion
		assertEquals( "123", (new Integer(123)).toString() ) ;
		assertEquals( "-123", (new Integer(-123)).toString() ) ;
	}
}
