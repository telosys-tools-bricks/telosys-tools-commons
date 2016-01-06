package org.telosys.tools.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StrUtilTest {

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
	
	@Test
	public void testReplaceVar() {
		replaceVar("aaaaabbbccccc",   "bbb",     "BBB",      "aaaaaBBBccccc") ;
		replaceVar("aaaaa[x]ccccc",   "[x]",     "X",        "aaaaaXccccc") ;
		replaceVar("aaaaa$TOTObbb",   "$TOTO",   "MyValue",  "aaaaaMyValuebbb") ;
		replaceVar("aaaaa${TOTO}bbb", "${TOTO}", "MyValue",  "aaaaaMyValuebbb") ;
		replaceVar("aaaaa${TOTO}bbb", "${TOTO}", "",         "aaaaabbb") ;
		replaceVar("aaaaa$bbb",       "$",       "DOLLAR",   "aaaaaDOLLARbbb") ;
	}

	@Test
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

	@Test
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
// Different behavior between different JDK :-(
//		assertNull( StrUtil.getIntegerObject("+123") ) ;
//		assertNull( StrUtil.getIntegerObject("  +123") ) ;

		//--- Check reverse conversion
		assertEquals( "123", (new Integer(123)).toString() ) ;
		assertEquals( "-123", (new Integer(-123)).toString() ) ;
	}

	@Test
	public void testRemodeEnd() {
		assertEquals( "ab",  StrUtil.removeEnd("abcd",      "cd"     ) ) ;
		assertEquals( "abc", StrUtil.removeEnd("abcd",      "d") ) ;
		assertEquals( "foo", StrUtil.removeEnd("foo_model", "_model" ) ) ;

		assertEquals( "abcd", StrUtil.removeEnd("abcd", ""  ) ) ;
		assertEquals( "abcd", StrUtil.removeEnd("abcd", null) ) ;
		assertEquals( "abcd", StrUtil.removeEnd("abcd", "ab") ) ;
		assertEquals( "",     StrUtil.removeEnd("abcd", "abcd") ) ;
		
		assertNull( StrUtil.removeEnd(null, "a") ) ;
		assertNull( StrUtil.removeEnd(null, null) ) ;
	}
}
