package org.telosys.tools.commons;

import java.math.BigDecimal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StrUtilTest {

	@Test
	public void testCapitalize() {
		assertEquals("A", StrUtil.capitalize("a") );
		assertEquals("A", StrUtil.capitalize("A") );

		assertEquals("Abc", StrUtil.capitalize("abc") );
		assertEquals("Abc", StrUtil.capitalize("Abc") );
		assertEquals("ABC", StrUtil.capitalize("ABC") );
		assertEquals("AbcDDD", StrUtil.capitalize("abcDDD") );

		assertEquals("", StrUtil.capitalize("") );
		assertEquals(" ", StrUtil.capitalize(" ") );
		assertEquals(" a", StrUtil.capitalize(" a") );
		assertNull(StrUtil.capitalize(null));
		
	}
	
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
		String result = StrUtil.replaceVar(s, varName, varValue) ;
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
		assertEquals( Integer.valueOf( 123), StrUtil.getIntegerObject("123") ) ;
		assertEquals( Integer.valueOf( 123), StrUtil.getIntegerObject(" 123") ) ;
		assertEquals( Integer.valueOf( 123), StrUtil.getIntegerObject("123  ") ) ;
		assertEquals( Integer.valueOf( 123), StrUtil.getIntegerObject("  123  ") ) ;
		assertEquals( Integer.valueOf(-123), StrUtil.getIntegerObject("-123  ") ) ;
		assertEquals( Integer.valueOf(-123), StrUtil.getIntegerObject("  -123  ") ) ;
		assertEquals( Integer.valueOf( 123), StrUtil.getIntegerObject("+123") ) ;
		assertEquals( Integer.valueOf( 123), StrUtil.getIntegerObject("  +123") ) ;
		assertNull( StrUtil.getIntegerObject("12.34") ) ;
		assertNull( StrUtil.getIntegerObject("- 2") ) ;

		//--- Check reverse conversion
		assertEquals( "123", (Integer.valueOf(123)).toString() ) ;
		assertEquals( "-123", (Integer.valueOf(-123)).toString() ) ;
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
	public void testRemodeFrom() {
		assertEquals( "ab",  StrUtil.removeFrom("ab:cd", ':') ) ;
		assertEquals( "abcd",  StrUtil.removeFrom("abcd", ':') ) ;
		assertEquals( "ab",  StrUtil.removeFrom("ab:cd:ddd:ddd", ':') ) ;
		assertEquals( "",  StrUtil.removeFrom("", ':') ) ;
		assertNull(StrUtil.removeFrom(null, ':') ) ;
	}

	@Test
	public void testIsFirstCharAlpha() {
		assertTrue (StrUtil.isFirstCharAlpha("abcd") ) ;
		assertTrue (StrUtil.isFirstCharAlpha("Zer") ) ;
		assertTrue (StrUtil.isFirstCharAlpha("Azee") ) ;
		assertTrue (StrUtil.isFirstCharAlpha("z") ) ;
		
		assertFalse (StrUtil.isFirstCharAlpha(null) ) ;
		assertFalse (StrUtil.isFirstCharAlpha("") ) ;
		assertFalse (StrUtil.isFirstCharAlpha(" ") ) ;
		assertFalse (StrUtil.isFirstCharAlpha(" ahhjq") ) ;
		assertFalse (StrUtil.isFirstCharAlpha("2ahhjq") ) ;
		assertFalse (StrUtil.isFirstCharAlpha("+ahhjq") ) ;
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
		assertEquals( "'abcde'", StrUtil.removeQuotes("'abcde'", '"') ) ;
		assertEquals( "", StrUtil.removeQuotes("", '"') ) ;
		assertEquals( "", StrUtil.removeQuotes("\"\"", '"') ) ;
		assertNull( StrUtil.removeQuotes(null, '"') ) ;

		assertEquals( "abcd", StrUtil.removeQuotes("'abcd'", '\'') ) ;
	}		

	@Test
	public void testGetProtectedString() {
		assertEquals( "", StrUtil.getProtectedString(null) ) ;
		assertEquals( "", StrUtil.getProtectedString("") ) ;
		assertEquals( " ", StrUtil.getProtectedString(" ") ) ;
		assertEquals( "abcd", StrUtil.getProtectedString("abcd") ) ;
		assertEquals( "&quot;abcde&quot;", StrUtil.getProtectedString("\"abcde\"") ) ;
		assertEquals( "ab&quot;cd", StrUtil.getProtectedString("ab\"cd") ) ;
		assertEquals( "&quot;", StrUtil.getProtectedString("\"") ) ;
		assertEquals( "&quot;&quot;", StrUtil.getProtectedString("\"\"") ) ;
	}		

	@Test
	public void testRepeat() {
		assertEquals( "", StrUtil.repeat('a', -2)) ;
		assertEquals( "", StrUtil.repeat('a', -1)) ;
		assertEquals( "", StrUtil.repeat('a', 0)) ;
		assertEquals( "a", StrUtil.repeat('a', 1)) ;
		assertEquals( "aa", StrUtil.repeat('a', 2)) ;
		assertEquals( "aaaa", StrUtil.repeat('a', 4)) ;
	}
	
	@Test
	public void testIsUpperCase() {
		assertTrue(StrUtil.isUpperCase("ABC")) ;
		assertTrue(StrUtil.isUpperCase("ABC12")) ;
		
		assertFalse(StrUtil.isUpperCase(null)) ;
		assertFalse(StrUtil.isUpperCase("")) ;
		assertFalse(StrUtil.isUpperCase("abcde")) ;
		assertFalse(StrUtil.isUpperCase("ABC-DE")) ;
		assertFalse(StrUtil.isUpperCase("AB_CDE")) ;
		assertFalse(StrUtil.isUpperCase("AB CDE")) ;
		assertFalse(StrUtil.isUpperCase(" AB")) ;
		assertFalse(StrUtil.isUpperCase("AB ")) ;
	}
	
	@Test
	public void testIsLowerCase() {
		assertTrue(StrUtil.isLowerCase("a")) ;
		assertTrue(StrUtil.isLowerCase("abcd")) ;
		assertTrue(StrUtil.isLowerCase("abcdef12")) ;
		
		assertFalse(StrUtil.isLowerCase(null)) ;
		assertFalse(StrUtil.isLowerCase("")) ;
		assertFalse(StrUtil.isLowerCase("ABCD")) ;
		assertFalse(StrUtil.isLowerCase("aBcdE")) ;
		assertFalse(StrUtil.isLowerCase("abc-def")) ;
		assertFalse(StrUtil.isLowerCase("ab_cde")) ;
		assertFalse(StrUtil.isLowerCase("ab cde")) ;
		assertFalse(StrUtil.isLowerCase(" abc")) ;
		assertFalse(StrUtil.isLowerCase("abc ")) ;
	}

	@Test
	public void testIsQuoted() {
		assertFalse(StrUtil.isQuoted(null));
		assertFalse(StrUtil.isQuoted(""));
		assertFalse(StrUtil.isQuoted("\""));
		assertFalse(StrUtil.isQuoted("abc"));
		
		assertTrue(StrUtil.isQuoted("\"\""));
		assertTrue(StrUtil.isQuoted("\"abczz\""));
		assertTrue(StrUtil.isQuoted("\"abc\"def\""));
	}
	@Test
	public void testIsQuotedWithArg() {
		char quote = '\'';
		assertFalse(StrUtil.isQuoted(null, quote));
		assertFalse(StrUtil.isQuoted("", quote));
		assertFalse(StrUtil.isQuoted("'", quote));
		assertFalse(StrUtil.isQuoted("abc", quote));
		
		assertTrue(StrUtil.isQuoted("''", quote));
		assertTrue(StrUtil.isQuoted("'abc'", quote));
		assertTrue(StrUtil.isQuoted("'abc'def'", quote));
	}
	@Test
	public void testQuote() {
		assertNull(StrUtil.quote(null));
		assertEquals("\"abc\"",   StrUtil.quote("abc"));
		assertEquals("\" abc \"", StrUtil.quote(" abc "));
		assertEquals("\" a\\bc \"",   StrUtil.quote(" a\\bc ")); // [ a\bc ] --> [" a\bc "]
		assertEquals("\" a\\\"bc \"", StrUtil.quote(" a\"bc ")); // [ a"bc ] --> [" a\"bc "]
	}
	
	@Test
	public void testUnquote() {
		assertNull(StrUtil.unquote(null));
		assertEquals("", StrUtil.unquote(""));
		assertEquals(" ", StrUtil.unquote(" "));
		assertEquals("abc", StrUtil.unquote("abc"));
		
		assertEquals("abc", StrUtil.unquote("\"abc\""));
		assertEquals(" abc ", StrUtil.unquote("\" abc \""));
		assertEquals(" a\\bc ", StrUtil.unquote("\" a\\bc \""));
		assertEquals(" a\"bc ", StrUtil.unquote("\" a\\\"bc \""));
		assertEquals("abc\\", StrUtil.unquote("\"abc\\\""));
	}
	
	@Test
	public void testQuoteUnquote() {
		quoteUnquote("");
		quoteUnquote("a");
		quoteUnquote("abc");
		quoteUnquote("a\\b\\c");
		quoteUnquote("  ab  ");
		quoteUnquote("a : \"bc\"");
		quoteUnquote("a\\\\b");
		quoteUnquote("abc\\");
	}
	public void quoteUnquote(String s1) {
		String s2 = StrUtil.quote(s1) ;
		String s3 = StrUtil.unquote( s2);
		assertEquals(s1, s3);
	}
	
}
