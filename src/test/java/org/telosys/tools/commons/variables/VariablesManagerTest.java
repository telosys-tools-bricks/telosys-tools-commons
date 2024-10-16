package org.telosys.tools.commons.variables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import junit.framework.TestCase;

public class VariablesManagerTest extends TestCase {

	private VariablesManager getVariablesManagerInstance() {
		HashMap<String,String> hm = new HashMap<>();
		hm.put("ROOT_PKG",   "org.demo.foo.bar");
		hm.put("ENTITY_PKG", "org.demo.foo.bar.bean");
		hm.put("VAR1",       "VALUE1");
		hm.put("VAR2",       "VALUE2");
		hm.put("VAR3",       "VALUE3");
		return new VariablesManager(hm) ;
	}

	@Test
	public void testConstructorWithMap() {
		Map<String,String> hm = new HashMap<>();
		hm.put("ROOT_PKG",   "org.demo");
		hm.put("ENTITY_PKG", "org.demo.foo.bean");
		hm.put("VAR1",       "VALUE1");
		VariablesManager vm = new VariablesManager(hm);
		assertEquals(3, vm.getVariablesNames().size() );
		assertEquals("VALUE1", vm.getVariableValue("VAR1"));
		assertEquals("org.demo", vm.getVariableValue("ROOT_PKG"));
	}
	
	@Test
	public void testConstructorWithArray() {
		Variable[] variables = { new Variable("V1", "1"), new Variable("V22", "2"), new Variable("ROOT_PKG", "org.demo")};
		VariablesManager vm = new VariablesManager(variables);
		assertEquals(3, vm.getVariablesNames().size() );
		assertEquals("1", vm.getVariableValue("V1"));
		assertEquals("org.demo", vm.getVariableValue("ROOT_PKG"));
	}
	
	@Test
	public void testExtractVariableName() {
		VariablesManager variablesManager = getVariablesManagerInstance();
		assertEquals("ABC", variablesManager.extractVariableName("${ABC}"));
	}
	
	@Test
	public void testReplaceVariables() {
		VariablesManager variablesManager = getVariablesManagerInstance();
		
		// No variable(s) in the original string
		assertNull(variablesManager.replaceVariables(null));
		assertEquals("", variablesManager.replaceVariables(""));
		assertEquals("   ", variablesManager.replaceVariables("   "));
		assertEquals("aa", variablesManager.replaceVariables("aa"));
		assertEquals(" a b $ c ", variablesManager.replaceVariables(" a b $ c "));
		assertEquals("aaa${}zzz", variablesManager.replaceVariables("aaa${}zzz"));

		// 1 variable in the original string
		assertEquals(" aa VALUE1 ", variablesManager.replaceVariables(" aa ${VAR1} "));
		assertEquals("VALUE1", variablesManager.replaceVariables("${VAR1}"));
		assertEquals("VALUE1zzz", variablesManager.replaceVariables("${VAR1}zzz"));
		assertEquals("aaaVALUE1", variablesManager.replaceVariables("aaa${VAR1}"));
		assertEquals("aaaVALUE1zzz", variablesManager.replaceVariables("aaa${VAR1}zzz"));
				
		// 2 variables in the original string		
		assertEquals("aaaVALUE1bbbVALUE2zzz", variablesManager.replaceVariables("aaa${VAR1}bbb${VAR2}zzz"));

		// Multiple same variable in the original string
		assertEquals("aaaVALUE1bbbVALUE1zzz", variablesManager.replaceVariables("aaa${VAR1}bbb${VAR1}zzz"));
		assertEquals("aaaVALUE1bbbVALUE1cccVALUE2zzz", variablesManager.replaceVariables("aaa${VAR1}bbb${VAR1}ccc${VAR2}zzz"));
	}
	
	@Test
	public void testReplaceVarForTargetFolder() {
		HashMap<String,String> hm = new HashMap<>();
		hm.put("SRC", "/foo/bar");
		hm.put("BEANNAME",    "Car");		
		hm.put("BEANNAME_LC", "car");
		hm.put("BEANNAME_UC", "CAR");
		VariablesManager variablesManager = new VariablesManager(hm) ;
		
		assertEquals("car-form.component.ts", variablesManager.replaceVariables("${BEANNAME_LC}-form.component.ts"));
		assertEquals("CAR-form.component.ts", variablesManager.replaceVariables("${BEANNAME_UC}-form.component.ts"));
		assertEquals("/foo/bar/app/entities/car/car-form", variablesManager.replaceVariables("${SRC}/app/entities/${BEANNAME_LC}/${BEANNAME_LC}-form"));
	}
	
	@Test
	public void testGetVariableNames() {
		VariablesManager variablesManager = getVariablesManagerInstance();
		List<String> names = variablesManager.getVariablesNames();
		assertEquals(5, names.size());
		assertTrue(names.contains("ROOT_PKG"));
		assertTrue(names.contains("VAR1"));
	}
	
	@Test
	public void testChangeVariableValue() {
		VariablesManager variablesManager = getVariablesManagerInstance();

		String s = "Variable = ${VAR1}" ;		
		assertEquals("Variable = VALUE1", variablesManager.replaceVariables(s));
		// change var value
		variablesManager.setVariable("VAR1", "new-val1"); 
		assertEquals("Variable = new-val1", variablesManager.replaceVariables(s));
	}
	
	@Test
	public void testCopy() {
		VariablesManager variablesManager = getVariablesManagerInstance();
		VariablesManager copy = variablesManager.copy();
		assertEquals(variablesManager.getVariableValue("VAR1"), copy.getVariableValue("VAR1"));
		copy.setVariable("VAR1", "new-val-var1");
		assertEquals("new-val-var1", copy.getVariableValue("VAR1")); // changed
		assertEquals("VALUE1", variablesManager.getVariableValue("VAR1")); // unchanged
	}	
}
