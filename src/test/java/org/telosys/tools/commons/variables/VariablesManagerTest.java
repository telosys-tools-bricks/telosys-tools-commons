package org.telosys.tools.commons.variables;

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
		
		// No variable(s) in the original string
		replaceVariables(variablesManager, null, null);
		replaceVariables(variablesManager, "",  "");
		replaceVariables(variablesManager, "aa", "aa");		
		replaceVariables(variablesManager, "aabbcc",  "aabbcc");
		replaceVariables(variablesManager, " a b ccc ddd", " a b ccc ddd");
		replaceVariables(variablesManager, "     ",  "     ");
		replaceVariables(variablesManager, "aaa${}zzz", "aaa${}zzz");

		// 1 variable in the original string
		replaceVariables(variablesManager, " aa ${VAR1} ",  " aa VALUE1 ");
		replaceVariables(variablesManager, "${VAR1}",       "VALUE1");
		replaceVariables(variablesManager, "${VAR1}zzz",    "VALUE1zzz" );
		replaceVariables(variablesManager, "aaa${VAR1}",    "aaaVALUE1");
		replaceVariables(variablesManager, "aaa${VAR1}zzz", "aaaVALUE1zzz");
		
		// 2 variables in the original string		
		replaceVariables(variablesManager, "aaa${VAR1}bbb${VAR2}zzz", "aaaVALUE1bbbVALUE2zzz");

		// Multiple same variable in the original string
		replaceVariables(variablesManager, "aaa${VAR1}bbb${VAR1}zzz", "aaaVALUE1bbbVALUE1zzz");
		replaceVariables(variablesManager, "aaa${VAR1}bbb${VAR1}ccc${VAR2}zzz", "aaaVALUE1bbbVALUE1cccVALUE2zzz");
	}
	
	public void testReplaceVarForTargetFolder() {
// Example :
//		# Entities_form
//		Entity form.component.ts	; ${BEANNAME_LC}-form.component.ts		; ${SRC}/app/entities/${BEANNAME_LC}/${BEANNAME_LC}-form 	; src/app/entities/Entity_form/ENTITY-form_component_ts.vm		; *
//		Entity form.component.html	; ${BEANNAME_LC}-form.component.html	; ${SRC}/app/entities/${BEANNAME_LC}/${BEANNAME_LC}-form 	; src/app/entities/Entity_form/ENTITY-form_component_html.vm	; *
//		Entity form.component.css	; ${BEANNAME_LC}-form.component.css		; ${SRC}/app/entities/${BEANNAME_LC}/${BEANNAME_LC}-form 	; src/app/entities/Entity_form/ENTITY-form_component_css.vm     ; *
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("${SRC}", "/foo/bar");
		hm.put("${BEANNAME}", "Car");		
		hm.put("${BEANNAME_LC}", "car");
		hm.put("${BEANNAME_UC}", "CAR");
		VariablesManager variablesManager = new VariablesManager(hm) ;
		
		replaceVariables(variablesManager, "${BEANNAME_LC}-form.component.ts", "car-form.component.ts");
		replaceVariables(variablesManager, "${BEANNAME_UC}-form.component.ts", "CAR-form.component.ts");

		replaceVariables(variablesManager, "${SRC}/app/entities/${BEANNAME_LC}/${BEANNAME_LC}-form", "/foo/bar/app/entities/car/car-form");
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
