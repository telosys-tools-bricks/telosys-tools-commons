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

	public void testCompareList() {
		LinkedList<Variable> variables = new LinkedList<>();
		variables.add(new Variable("V1", "1"));
		variables.add(new Variable("A", "Value A"));
		variables.add(new Variable("V3", "3"));
		variables.add(new Variable("V2", "2"));
		variables.add(new Variable("B", "Value B"));
		
		print(variables);
		print(variables.toArray(new Variable[0]));
		Collections.sort(variables, new VariableNameComparator());
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
