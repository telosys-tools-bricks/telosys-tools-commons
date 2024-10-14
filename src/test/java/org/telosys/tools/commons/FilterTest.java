package org.telosys.tools.commons;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

public class FilterTest extends TestCase {

	@Test
	public void testNoCriterion() {
		assertTrue(Filter.noCriterion(null));		
		assertTrue(Filter.noCriterion("")); 
		assertTrue(Filter.noCriterion("*")); 
		
		assertFalse(Filter.noCriterion("aa"));
		assertFalse(Filter.noCriterion(" "));
	}

	@Test
	public void testNoCriteria() {

		assertTrue(Filter.noCriteria(null));
		
		List<String> criteria = new LinkedList<>();
		assertTrue(Filter.noCriteria(criteria)); // void => no criteria
		
		criteria.add("aaa");
		criteria.add("*"); // all => no criteria
		criteria.add("bbb");
		assertTrue(Filter.noCriteria(criteria));

		criteria = new LinkedList<>();
		criteria.add("aaa");
		assertFalse(Filter.noCriteria(criteria));
		criteria.add("bb");
		assertFalse(Filter.noCriteria(criteria));
	}

	@Test
	public void testCriteriaNull() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, (List<String>)null);
		assertEquals(2, list2.size()); 
	}
	@Test
	public void testCriterionNull() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, (String)null);
		assertEquals(2, list2.size()); 
	}
	@Test
	public void testCriteriaVoid() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, new LinkedList<>());
		assertEquals(2, list2.size()); 
	}
	@Test
	public void testCriterionEmpty() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, "");
		assertEquals(2, list2.size()); 
	}
	@Test
	public void testCriteriaWithStar() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, Arrays.asList("*"));
		assertEquals(2, list2.size()); 
	}	
	@Test
	public void testCriteriaWithStar2() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, Arrays.asList("fo", "aa", "*"));
		assertEquals(2, list2.size()); 
	}
	@Test
	public void testCriterionStar() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, "*");
		assertEquals(2, list2.size()); 
	}	
	
	@Test
	public void testFilter1() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, Arrays.asList("foo"));
		assertEquals(1, list2.size()); 
		assertTrue( list2.contains("foo")); 
	}
	@Test
	public void testFilter2() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, Arrays.asList("fo"));
		assertEquals(1, list2.size()); 
		assertTrue( list2.contains("foo")); 
	}
	@Test
	public void testFilter3() {
		List<String> list = Arrays.asList("foo", "bar", "zzz", "foo", "arch");
		List<String> list2 = Filter.filter(list, Arrays.asList("fo", "ar"));
		assertEquals(4, list2.size()); 
		assertTrue( list2.contains("foo")); 
		assertTrue( list2.contains("bar")); 
	}
	
	@Test
	public void testFilter11() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, "oo");
		assertEquals(1, list2.size()); 
		assertTrue( list2.contains("foo")); 
	}
	@Test
	public void testFilter12() {
		List<String> list = Arrays.asList("foo", "bar");
		List<String> list2 = Filter.filter(list, "fo");
		assertEquals(1, list2.size()); 
		assertTrue( list2.contains("foo")); 
	}
	@Test
	public void testFilter13() {
		List<String> list = Arrays.asList("foo", "bar", "zzz", "foo", "arch");
		List<String> list2 = Filter.filter(list, "ar");
		assertEquals(2, list2.size()); 
		assertTrue( list2.contains("arch")); 
		assertTrue( list2.contains("bar")); 
	}
}
