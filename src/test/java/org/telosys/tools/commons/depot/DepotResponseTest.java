package org.telosys.tools.commons.depot;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DepotResponseTest {

	@Test
	public void testGetElementByName() {
		
		DepotRateLimit depotRateLimit = new DepotRateLimit("", "", "");
		List<DepotElement> elements = new LinkedList<>();
		elements.add(new DepotElement(1, "element-aaa", "desc", 963, "master", "public"));
		elements.add(new DepotElement(2, "element-bbb", "desc", 963, "master", "public"));
		elements.add(new DepotElement(3, "element-ccc", "desc", 963, "master", "public"));
		DepotResponse depotResponse = new DepotResponse("my-depot", "my-url", 200, elements, depotRateLimit, 1) ;

		assertEquals( 3, depotResponse.getElementNames().size());
		
		// not found
		assertNull( depotResponse.getElementByName("foo"));
		
		// found
		assertEquals( 2L, depotResponse.getElementByName("element-bbb").getId() );
		assertEquals( "element-aaa", depotResponse.getElementByName("element-aaa").getName() );
	}

	@Test
	public void testFilterElementsByName() {
		
		DepotRateLimit depotRateLimit = new DepotRateLimit("", "", "");
		List<DepotElement> elements = new LinkedList<>();
		elements.add(new DepotElement(1, "element-aaa", "desc", 963, "master", "public"));
		elements.add(new DepotElement(2, "element-bbb", "desc", 963, "master", "public"));
		elements.add(new DepotElement(3, "element-ccc", "desc", 963, "master", "public"));
		DepotResponse depotResponse = new DepotResponse("my-depot", "my-url", 200, elements, depotRateLimit, 1) ;

		List<DepotElement> r = depotResponse.filterElementsByName(Arrays.asList("","*",""));
		assertEquals( 3, r.size());

		r = depotResponse.filterElementsByName(Arrays.asList("aa","bb","*"));
		assertEquals( 3, r.size());

		r = depotResponse.filterElementsByName(Arrays.asList("aa"));
		assertEquals( 1, r.size());

		r = depotResponse.filterElementsByName(Arrays.asList("element-"));
		assertEquals( 3, r.size());

		r = depotResponse.filterElementsByName(Arrays.asList("aa", "bbb"));
		assertEquals( 2, r.size());
	}
}
