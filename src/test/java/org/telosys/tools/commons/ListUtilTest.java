package org.telosys.tools.commons;

import java.util.LinkedList;

import org.junit.Test;

import junit.framework.TestCase;

public class ListUtilTest extends TestCase {

	@Test
	public void testNull() {
		assertEquals("", ListUtil.join(null, ",") );
		assertEquals("", ListUtil.join(null, null) );
	}

	@Test
	public void test0() {
		LinkedList<String> list = new LinkedList<>();
		assertEquals("", ListUtil.join(list, ",") );
	}

	@Test
	public void test1() {
		LinkedList<String> list = new LinkedList<>();
		list.add("aa");
		assertEquals("aa", ListUtil.join(list, ",") );
		assertEquals("aa", ListUtil.join(list, "") );
		assertEquals("aa", ListUtil.join(list, null) );
	}

	@Test
	public void test3() {
		LinkedList<String> list = new LinkedList<>();
		list.add("aa");
		list.add("bb");
		list.add("cc");
		assertEquals("aa,bb,cc", ListUtil.join(list, ","));
		assertEquals("aa, bb, cc", ListUtil.join(list, ", "));
		assertEquals("aa-bb-cc", ListUtil.join(list, "-"));
		assertEquals("aabbcc", ListUtil.join(list, ""));
		assertEquals("aabbcc", ListUtil.join(list, null));
	}
}
