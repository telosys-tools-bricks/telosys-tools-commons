package org.telosys.tools.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
public class MethodInvokerTest {

	private class Person {
		private int code ;
		private String name ;
		private boolean ok ;
		
		public Person(int code, String name, boolean ok) {
			super();
			this.code = code;
			this.name = name;
			this.ok = ok;
		}
		
		public int getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
		public boolean isOk() {
			return ok;
		}
	}
	
	@Test
	public void test1() {
		Person p = new Person(1, "Bart", true);
		Object code = MethodInvoker.invokeGetter(p, "getCode");
		assertNotNull(code);
		assertEquals(1, code);
		assertEquals(p.getCode(), code);
		
		Object name = MethodInvoker.invokeGetter(p, "getName");
		assertNotNull(name);
		assertEquals("Bart", name);
		assertEquals(p.getName(), name);
		
		Object ok   = MethodInvoker.invokeGetter(p, "isOk");
		assertNotNull(ok);
		assertEquals(p.isOk(), ok);
	}

	@Test
	public void test2() {
		Person p = new Person(2, "Foo", false);
		Object code = MethodInvoker.invokeGetter(p, "getCode");
		assertNotNull(code);
		assertEquals(p.getCode(), code);
		
		Object name = MethodInvoker.invokeGetter(p, "getName");
		assertNotNull(name);
		assertEquals(p.getName(), name);
		
		Object ok   = MethodInvoker.invokeGetter(p, "isOk");
		assertNotNull(ok);
		assertEquals(p.isOk(), ok);
	}

	@Test
	public void testBooleanGetter() {
		Person p = new Person(1, "Bart", true);
		boolean ok   = MethodInvoker.invokeBooleanGetter(p, "isOk");
		assertEquals(ok, true);
		assertTrue(ok);
	}

	@Test (expected=RuntimeException.class)
	public void testBooleanGetterInvalid() {
		Person p = new Person(1, "Bart", true);
		MethodInvoker.invokeBooleanGetter(p, "isBlaBla");
	}
}
