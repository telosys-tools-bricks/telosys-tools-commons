package org.telosys.tools.commons.variables;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class VariablesUtilTest extends TestCase {

//	public void testListToArray1() {
//		List<Variable> list = new LinkedList<>();
//		list.add(new Variable("V1", "1"));
//		list.add(new Variable("V2", "2"));
//		list.add(new Variable("V3", "3"));
//		Variable[] array = VariablesUtil.listToArray(list);
//		assertEquals(3, array.length);
//		List<Variable> list2 = Arrays.asList(array);
//		// check 'contains' by reference comparison (no 'equals' method in 'Variable') 
//		assertTrue(list2.contains(list.get(0)) );
//		assertTrue(list2.contains(list.get(1)) );
//		assertTrue(list2.contains(list.get(2)) );
//	}
//
//	public void testListToArray2() {
//		Variable[] array = VariablesUtil.listToArray(null);
//		assertEquals(0, array.length);
//		array = VariablesUtil.listToArray(new LinkedList<>());
//		assertEquals(0, array.length);
//	}

	public void testSort() {
		List<Variable> list = new LinkedList<>();
		list.add(new Variable("BBB", "1"));
		list.add(new Variable("AA", "2"));
		list.add(new Variable("A", "3"));
		list.add(new Variable("ZZ", "3"));
		list.add(new Variable("DEV", "3"));
		VariablesUtil.sortByVariableName(list);
		assertEquals("A",   list.get(0).getName());
		assertEquals("AA",  list.get(1).getName());
		assertEquals("BBB", list.get(2).getName());
		assertEquals("DEV", list.get(3).getName());
	}
	
}
