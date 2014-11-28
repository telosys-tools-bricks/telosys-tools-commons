package org.telosys.tools.tests.commons;

import java.util.LinkedList;

import junit.framework.TestCase;

import org.junit.Test;
import org.telosys.tools.commons.ObjectUtil;

public class ObjectUtilTest extends TestCase {

	@Test
	public void test1() {
		LinkedList<String> list1 = new LinkedList<String>();
		list1.add("aa");
		list1.add("bb");
		list1.add("cc");
		LinkedList<String> list2 = ObjectUtil.deepCopy(list1);
		list1.add("dd");
		assertTrue( list1.size() == 4 ); 
		assertTrue( list2.size() == 3 ); 
		
	}
}
