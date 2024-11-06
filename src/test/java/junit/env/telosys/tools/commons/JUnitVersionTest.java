package junit.env.telosys.tools.commons;

import junit.framework.TestCase;

public class JUnitVersionTest extends TestCase {

	public void testVersion() {
		assertEquals("4.13.1", junit.runner.Version.id());
	}
}
