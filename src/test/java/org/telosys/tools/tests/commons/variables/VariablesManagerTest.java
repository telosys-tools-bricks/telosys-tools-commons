package org.telosys.tools.tests.commons.variables;

import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.telosys.tools.commons.variables.VariablesManager;

public class VariablesManagerTest extends TestCase {

	private VariablesManager getVariablesManagerInstance() {
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("${ROOT_PKG}",   "org.demo.foo.bar");
		hm.put("${ENTITY_PKG}", "org.demo.foo.bar.bean");
		hm.put("${VAR1}", "VALUE1");
		hm.put("${VAR2}", "VALUE2");
		hm.put("${VAR3}", "VALUE3");
		return new VariablesManager(hm) ;
	}
	
	private void replaceVariables(VariablesManager variablesManager, String s, String expectedResult ) {
		System.out.println("==========");
		String result = variablesManager.replaceVariables(s);
		System.out.println("'" + s + "' --> '" + result + "'");
		assertEquals(expectedResult, result);
	}
	
	public void testReplaceVar() {
//		HashMap<String,String> hm = new HashMap<String,String>();
//		hm.put("${VAR1}", "VALUE1");
//		hm.put("${VAR2}", "VALUE2");
//		hm.put("${VAR3}", "VALUE3");		
//		VariablesManager variablesManager = new VariablesManager(hm) ;
		VariablesManager variablesManager = getVariablesManagerInstance();
		
		replaceVariables(variablesManager, null, null);
		replaceVariables(variablesManager, "",  "");
		replaceVariables(variablesManager, "aa", "aa");
		
		replaceVariables(variablesManager, " aa ${VAR1} ",  " aa VALUE1 ");
		replaceVariables(variablesManager, "${VAR1}",       "VALUE1");
		replaceVariables(variablesManager, "${VAR1}zzz",    "VALUE1zzz" );
		replaceVariables(variablesManager, "aaa${VAR1}",    "aaaVALUE1");
		replaceVariables(variablesManager, "aaa${VAR1}zzz", "aaaVALUE1zzz");
		
		replaceVariables(variablesManager, "aaa${}zzz", "aaa${}zzz");
		
		replaceVariables(variablesManager, "aaa${VAR1}bbb${VAR2}zzz", "aaaVALUE1bbbVALUE2zzz");
	}
	
	public void testGetVariableNames() {
//		HashMap<String,String> hm = new HashMap<String,String>();
//		hm.put("${ROOT_PKG}", "org.demo.foo.bar");
//		hm.put("${VAR1}", "VALUE1");
//		hm.put("${VAR2}", "VALUE2");
//		hm.put("${VAR3}", "VALUE3");
//		VariablesManager variablesManager = new VariablesManager(hm) ;
		VariablesManager variablesManager = getVariablesManagerInstance();
		List<String> names = variablesManager.getVariablesNames();
		System.out.println("List of variable names : size = " + names.size() );
		for ( String s : names ) {
			System.out.println(" . " + s);
		}
		assertEquals(5, names.size());
	}
	
	public void testChangeVariableValue() {
//		HashMap<String,String> hm = new HashMap<String,String>();
//		hm.put("${ROOT_PKG}", "org.demo.foo.bar");
//		hm.put("${VAR2}", "VALUE2");
//		hm.put("${VAR3}", "VALUE3");
//		VariablesManager variablesManager = new VariablesManager(hm) ;
		VariablesManager variablesManager = getVariablesManagerInstance();

		String s = "Variable ROOT_PKG = ${ROOT_PKG}" ;
		String result = null ;
		result = variablesManager.replaceVariables(s);
		System.out.println("'" + s + "' --> '" + result + "'");
		assertEquals("Variable ROOT_PKG = org.demo.foo.bar", result);
		
		variablesManager.changeVariableValue("${ROOT_PKG}", "org/demo/foo/bar" ) ;
		
		result = variablesManager.replaceVariables(s);
		System.out.println("'" + s + "' --> '" + result + "'");
		assertEquals("Variable ROOT_PKG = org/demo/foo/bar", result);
	}
	
	public void testTransformPackageToDir() {
		VariablesManager variablesManager = getVariablesManagerInstance();
		List<String> names = variablesManager.getVariablesNames();
		for ( String name : names ) {
			if ( name.endsWith("_PKG}" ) ) {
				String value = variablesManager.getVariableValue(name) ;
				String newValue = value.replace('.', '/');
				System.out.println(" . var '" + name + "' : '" + value + "' --> '" + newValue + "'");
				variablesManager.changeVariableValue(name, newValue);
			}
		}

		String s = "Variable ROOT_PKG = ${ROOT_PKG}" ;
		String result = null ;
		result = variablesManager.replaceVariables(s);
		System.out.println("'" + s + "' --> '" + result + "'");
		assertEquals("Variable ROOT_PKG = org/demo/foo/bar", result);
	}	
}
