package org.telosys.tools.commons;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CommonsVersionTest {

	@Test
	public void test1() {
		assertEquals("telosys-tools-commons", CommonsVersion.getName());
		assertEquals("4.2.0", CommonsVersion.getVersion());
		assertNotNull(CommonsVersion.getBuildId());
		assertNotNull(CommonsVersion.getVersionWithBuilId());
	}
}
