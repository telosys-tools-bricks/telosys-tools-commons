package org.telosys.tools.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

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
	public void testGetBigDecimalObject() {
		assertNull( StrUtil.getBigDecimalObject(null) ) ;
		assertNull( StrUtil.getBigDecimalObject("") ) ;
		assertNull( StrUtil.getBigDecimalObject("  ") ) ;
		assertEquals( new BigDecimal("123"), StrUtil.getBigDecimalObject("123") ) ;
		assertEquals( new BigDecimal("123"), StrUtil.getBigDecimalObject(" 123") ) ;
		assertEquals( new BigDecimal("123"), StrUtil.getBigDecimalObject("123  ") ) ;
		assertEquals( new BigDecimal("123"), StrUtil.getBigDecimalObject("  123  ") ) ;
		assertEquals( new BigDecimal("-123"), StrUtil.getBigDecimalObject("-123  ") ) ;
		assertEquals( new BigDecimal("-123"), StrUtil.getBigDecimalObject("  -123  ") ) ;
		assertEquals( new BigDecimal("123.456"), StrUtil.getBigDecimalObject("  123.456  ") ) ;
		assertEquals( new BigDecimal("-123.456"), StrUtil.getBigDecimalObject("  -123.456  ") ) ;
		//--- Check reverse conversion
		assertEquals( "123", (new BigDecimal("123")).toString() ) ;
		assertEquals( "-123", (new BigDecimal("-123")).toString() ) ;
		// new BigDecimal("  123  ") => NumberFormatException
		// new BigDecimal("  -123  ") => NumberFormatException
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

	@Test
	public void testSplit1() {
		String[] tokens = StrUtil.split("aa;b;ccc;", ';');
		assertNotNull( tokens ) ;
		assertEquals( 4,  tokens.length ) ;
		assertEquals( "aa",  tokens[0] ) ;
		assertEquals( "b",  tokens[1] ) ;
		assertEquals( "ccc",  tokens[2] ) ;
		assertEquals( "",  tokens[3] ) ;
	}		

	@Test
	public void testSplit2() {
		String[] tokens = StrUtil.split("", ';');
		assertNotNull( tokens ) ;
		assertEquals( 1,  tokens.length ) ;
	}
	
	@Test
	public void testSplit3() {
		String[] tokens = StrUtil.split("aaa", ';');
		assertNotNull( tokens ) ;
		assertEquals( 1,  tokens.length ) ;
		assertEquals( "aaa",  tokens[0] ) ;
	}

	@Test
	public void testSplit4() {
		String[] tokens = StrUtil.split(null, ';');
		assertNotNull( tokens ) ;
		assertEquals( 0,  tokens.length ) ;
	}

	@Test
	public void testSplit5() {
		String[] tokens = StrUtil.split(";b;;", ';');
		assertNotNull( tokens ) ;
		assertEquals( 4,  tokens.length ) ;
		assertEquals( "",  tokens[0] ) ;
		assertEquals( "b",  tokens[1] ) ;
		assertEquals( "",  tokens[2] ) ;
		assertEquals( "",  tokens[3] ) ;
	}		

	@Test
	public void testBackslash() {
		assertEquals( "a\\;b\\;c\\;ddd", StrUtil.backslash("a;b;c;ddd", ';') ) ;
		assertEquals( "a\\\"b\\\"ccccc", StrUtil.backslash("a\"b\"ccccc", '"') ) ;
		assertEquals( "a\\'b\\'ccccc", StrUtil.backslash("a'b'ccccc", '\'') ) ;
	}		

	@Test
	public void testRemoveQuotes() {
		assertEquals( "abcd", StrUtil.removeQuotes("abcd", '"') ) ;
		assertEquals( "\"abcd", StrUtil.removeQuotes("\"abcd", '"') ) ;
		assertEquals( "abcd\"", StrUtil.removeQuotes("abcd\"", '"') ) ;
		assertEquals( "abcd", StrUtil.removeQuotes("\"abcd\"", '"') ) ;
		assertEquals( "'abcd'", StrUtil.removeQuotes("'abcd'", '"') ) ;
		assertEquals( "", StrUtil.removeQuotes("", '"') ) ;
		assertEquals( "", StrUtil.removeQuotes("\"\"", '"') ) ;
		assertNull( StrUtil.removeQuotes(null, '"') ) ;

		assertEquals( "abcd", StrUtil.removeQuotes("'abcd'", '\'') ) ;
	}		
}
