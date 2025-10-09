package org.telosys.tools.commons.depot;

import org.junit.Test;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DepotTest {

	@Test
	public void testGitHubNominalCases() throws TelosysToolsException {
		Depot depot;
		
		//--- GitHub organization 
		depot = new Depot("github_org:my-org");
		assertTrue(depot.isGitHubDepot());
		assertFalse(depot.isGitLabDepot());
		assertEquals("github_org", depot.getType());
		assertEquals("my-org", depot.getNamespace());
		assertEquals("", depot.getSpecificRootURL()); 
		assertEquals("https://github.com", depot.getGitHubRootUrl() ); 
		assertEquals("https://github.com", depot.getRootURL());
		assertEquals("https://github.com/my-org", depot.getBaseURL());
		
		// url with default values
		// API 
		assertEquals("https://api.github.com", depot.getGitHubApiRootUrl() );
		assertEquals("https://api.github.com/orgs/my-org/repos?per_page=100", depot.getApiUrl());
		assertEquals("https://api.github.com/rate_limit", depot.getApiRateLimitUrl());

		depot = new Depot(" github_org : my-org   ");
		assertTrue(depot.isGitHubDepot());
		assertFalse(depot.isGitLabDepot());
		assertEquals("github_org", depot.getType());
		assertEquals("my-org", depot.getNamespace());
		assertEquals("", depot.getSpecificRootURL());
		assertEquals("https://api.github.com/orgs/my-org/repos", StrUtil.removeFrom(depot.getApiUrl(), '?')); // without "?per_page=nn"
		assertEquals("https://api.github.com/rate_limit", depot.getApiRateLimitUrl());

		depot = new Depot("github_org:my-templates ( https://myhost:9090 ) ");
		assertTrue(depot.isGitHubDepot());
		assertFalse(depot.isGitLabDepot());
		assertEquals("github_org", depot.getType());
		assertEquals("my-templates", depot.getNamespace());
		assertEquals("https://myhost:9090", depot.getSpecificRootURL());
		assertEquals("https://myhost:9090", depot.getGitHubApiRootUrl() );
		assertEquals("https://myhost:9090/orgs/my-templates/repos", StrUtil.removeFrom(depot.getApiUrl(), '?')); // without "?per_page=nn"
		assertEquals("https://myhost:9090/rate_limit", depot.getApiRateLimitUrl());

		//--- GitHub user 
		depot = new Depot(" github_user : toto   ");
		assertTrue(depot.isGitHubDepot());
		assertFalse(depot.isGitLabDepot());
		assertEquals("github_user", depot.getType());
		assertEquals("toto", depot.getNamespace());
		assertEquals("", depot.getSpecificRootURL());
		assertEquals("https://api.github.com/users/toto/repos", StrUtil.removeFrom(depot.getApiUrl(), '?')); // without "?per_page=nn"
		assertEquals("https://api.github.com/rate_limit", depot.getApiRateLimitUrl());

		depot = new Depot(" github_user :toto(http://myhost/ )  "); // trailing "/" is removed
		assertTrue(depot.isGitHubDepot());
		assertFalse(depot.isGitLabDepot());
		assertEquals("github_user", depot.getType());
		assertEquals("toto", depot.getNamespace());
		assertEquals("http://myhost", depot.getSpecificRootURL());
		assertEquals("http://myhost/users/toto/repos", StrUtil.removeFrom(depot.getApiUrl(), '?')); // without "?per_page=nn"
		assertEquals("http://myhost/rate_limit", depot.getApiRateLimitUrl());

		// GitHub current user 
		depot = new Depot(" github_current_user ");
		assertTrue(depot.isGitHubDepot());
		assertFalse(depot.isGitLabDepot());
		assertEquals("github_current_user", depot.getType());
		assertEquals("", depot.getNamespace());
		assertEquals("", depot.getSpecificRootURL());
		assertEquals("https://api.github.com/user/repos", StrUtil.removeFrom(depot.getApiUrl(), '?')); // without "?per_page=nn"

		depot = new Depot(" github_current_user( https://myhost:8888) ");
		assertTrue(depot.isGitHubDepot());
		assertFalse(depot.isGitLabDepot());
		assertEquals("github_current_user", depot.getType());
		assertEquals("", depot.getNamespace());
		assertEquals("https://myhost:8888", depot.getSpecificRootURL());
		assertEquals("https://myhost:8888/user/repos", StrUtil.removeFrom(depot.getApiUrl(), '?')); // without "?per_page=nn"
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
	
	@Test
	public void testBuildGitRepositoryURL() throws TelosysToolsException {
		Depot depot ;
		// GitHub
		depot = new Depot("github_org:telosys-templates");
		assertEquals("https://github.com/telosys-templates/myrepo.git", depot.buildGitRepositoryURL("myrepo") );
		depot = new Depot("github_org:telosys-templates");
		assertEquals("https://github.com/telosys-templates/myrepo.git", depot.buildGitRepositoryURL("myrepo") );
		depot = new Depot("github_org:myorga");
		assertEquals("https://github.com/myorga/myrepo.git", depot.buildGitRepositoryURL("myrepo") );
		depot = new Depot("github_org:myorga (https://foo.org)");
		assertEquals("https://foo.org/myorga/myrepo.git", depot.buildGitRepositoryURL("myrepo") );
		depot = new Depot("github_org:myorga (https://foo.org/)");
		assertEquals("https://foo.org/myorga/myrepo.git", depot.buildGitRepositoryURL("myrepo") );
		// GitLab
		depot = new Depot("gitlab_user:telosys-templates");
		assertEquals("https://gitlab.com/telosys-templates/myrepo.git", depot.buildGitRepositoryURL("myrepo") );
	}
}
