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
		
		//--- GitHub organization 
		d = new Depot("github_org:my-org");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_org", d.getType());
		assertEquals("my-org", d.getName());
		assertEquals("", d.getRootURL());
		assertEquals("https://api.github.com/orgs/my-org/repos", d.getApiUrl());
		assertEquals("https://api.github.com/rate_limit", d.getApiRateLimitUrl());

		d = new Depot(" github_org : my-org   ");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_org", d.getType());
		assertEquals("my-org", d.getName());
		assertEquals("", d.getRootURL());
		assertEquals("https://api.github.com/orgs/my-org/repos", d.getApiUrl());
		assertEquals("https://api.github.com/rate_limit", d.getApiRateLimitUrl());

		d = new Depot("github_org:my-templates ( https://myhost:9090 ) ");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_org", d.getType());
		assertEquals("my-templates", d.getName());
		assertEquals("https://myhost:9090", d.getRootURL());
		assertEquals("https://myhost:9090/orgs/my-templates/repos", d.getApiUrl());
		assertEquals("https://myhost:9090/rate_limit", d.getApiRateLimitUrl());

		//--- GitHub user 
		d = new Depot(" github_user : toto   ");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_user", d.getType());
		assertEquals("toto", d.getName());
		assertEquals("", d.getRootURL());
		assertEquals("https://api.github.com/users/toto/repos", d.getApiUrl());
		assertEquals("https://api.github.com/rate_limit", d.getApiRateLimitUrl());

		d = new Depot(" github_user :toto(http://myhost)  ");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_user", d.getType());
		assertEquals("toto", d.getName());
		assertEquals("http://myhost", d.getRootURL());
		assertEquals("http://myhost/users/toto/repos", d.getApiUrl());
		assertEquals("http://myhost/rate_limit", d.getApiRateLimitUrl());

		// GitHub current user 
		d = new Depot(" github_current_user ");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_current_user", d.getType());
		assertEquals("", d.getName());
		assertEquals("", d.getRootURL());
		assertEquals("https://api.github.com/user/repos", d.getApiUrl());

		d = new Depot(" github_current_user( https://myhost:8888) ");
		assertTrue(d.isGitHubDepot());
		assertFalse(d.isGitLabDepot());
		assertEquals("github_current_user", d.getType());
		assertEquals("", d.getName());
		assertEquals("https://myhost:8888", d.getRootURL());
		assertEquals("https://myhost:8888/user/repos", d.getApiUrl());

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
	public void testInvalid100() throws TelosysToolsException {
		new Depot("github_org:myorg ( gdgd ");
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
