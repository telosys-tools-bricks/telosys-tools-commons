package org.telosys.tools.commons.variables;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class VariablesUtilTest extends TestCase {

	public void test1() {
		List<Variable> list = new LinkedList<Variable>();
		list.add(new Variable("V1", "1"));
		list.add(new Variable("V2", "2"));
		list.add(new Variable("V3", "3"));
		print(list);
		Variable[] array = VariablesUtil.listToArray(list);
		print(array);
//		array[1].setValue("New value 1");
//		print(list);
//		print(array);

		assertEquals(3, array.length);
	}

	public void test2() {
		Variable[] array = VariablesUtil.listToArray(null);
		assertEquals(0, array.length);

		array = VariablesUtil.listToArray(new LinkedList<Variable>());
		assertEquals(0, array.length);
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
