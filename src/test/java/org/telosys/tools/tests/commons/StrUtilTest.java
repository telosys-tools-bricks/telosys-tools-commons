package org.telosys.tools.tests.commons;

import org.telosys.tools.commons.StrUtil;

import junit.framework.TestCase;

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
}
