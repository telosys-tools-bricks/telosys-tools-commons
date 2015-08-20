package org.telosys.tools.commons.variables;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class VariableTest extends TestCase {

	public void testInstance() {
		Variable var = new Variable("ID", "12");
		assertEquals("ID", var.getName());
		assertEquals("12", var.getValue());
		assertEquals("${ID}", var.getSymbolicName());
	}
	
	public void testCompare() {
		Variable v1 = new Variable("V1", "1");
		Variable v2 = new Variable("V2", "2");
		assertTrue( v1.compareTo(v2) < 0 ) ;
		assertTrue( v2.compareTo(v1) > 0 ) ;

		Variable v3 = new Variable("V2", "2");
		assertTrue( v3.compareTo(v2) == 0 ) ;
		assertTrue( v2.compareTo(v3) == 0 ) ;
	}

	public void testCompareList() {
		LinkedList<Variable> variables = new LinkedList<Variable>();
		variables.add(new Variable("V1", "1"));
		variables.add(new Variable("A", "Value A"));
		variables.add(new Variable("V3", "3"));
		variables.add(new Variable("V2", "2"));
		variables.add(new Variable("B", "Value B"));
		
		print(variables);
		print(variables.toArray(new Variable[0]));
		Collections.sort(variables);
		print(variables);
		print(variables.toArray(new Variable[0]));
	}
	
	private void print(List<Variable> variables ) {
		System.out.println("List of " + variables.size() + " variable(s) (List)" );
		for ( Variable var : variables ) {
			System.out.println(" . " + var.getName() + " = " + var.getValue() );
		}
	}
	private void print(Variable[] variables ) {
		System.out.println("List of " + variables.length + " variable(s) (Array)" );
		for ( Variable var : variables ) {
			System.out.println(" . " + var.getName() + " = " + var.getValue() );
		}
	}
}
