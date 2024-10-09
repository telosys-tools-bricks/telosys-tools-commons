package org.telosys.tools.commons.depot;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DepotTest {

	@Test
	public void testGitHubNominalCases() throws TelosysToolsException {
		Depot d;
		d = new Depot("github_org:my-org");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_org", d.getType());
		assertEquals("my-org", d.getName());

		d = new Depot(" github_org : my-org   ");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_org", d.getType());
		assertEquals("my-org", d.getName());

		d = new Depot(" github_user : toto   ");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_user", d.getType());
		assertEquals("toto", d.getName());

		d = new Depot(" github_current_user ");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_current_user", d.getType());
		assertEquals("", d.getName());
	}

	@Test(expected = TelosysToolsException.class)
	public void testInvalid1() throws TelosysToolsException {
		new Depot("github_org:");
	}
	@Test(expected = TelosysToolsException.class)
	public void testInvalid2() throws TelosysToolsException {
		new Depot("github_user:");
	}
	@Test(expected = TelosysToolsException.class)
	public void testInvalid3() throws TelosysToolsException {
		new Depot("github_user");
	}
	
	@Test(expected = TelosysToolsException.class)
	public void testInvalid900() throws TelosysToolsException {
		new Depot("");
	}
	@Test(expected = TelosysToolsException.class)
	public void testInvalid901() throws TelosysToolsException {
		new Depot(null);
	}
	@Test(expected = TelosysToolsException.class)
	public void testInvalid902() throws TelosysToolsException {
		new Depot(":");
	}
}
