package org.telosys.tools.commons.github;

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.depot.Depot;
import org.telosys.tools.commons.depot.DepotElement;
import org.telosys.tools.commons.depot.DepotResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import junit.env.telosys.tools.commons.TestsEnv;

/**
 * This class is "IT" ( Integration Test) due to potential connection problem with https on GitHub
 * Error "javax.net.ssl.SSLException: Received fatal alert: protocol_version" when run with Java 7
 * OK when run with Java 8 
 * https://stackoverflow.com/questions/16541627/javax-net-ssl-sslexception-received-fatal-alert-protocol-version 
 *  
 * @author Laurent Guerin
 *
 */
public class GitHubClientIT {

	private void log(String msg) {
		System.out.println(msg);
	}
	private void printJavaVersionAndHttpCfg() {
//		log("Setting system property 'https.protocols'... ");
//		// OK    on testGetRepositories(), testGetRepositoriesWithoutProperties()
//		// Error on testDownloadRepository() : SSLException: Received fatal alert: protocol_version
//		//System.setProperty("https.protocols", "SSLv3,TLSv1,TLSv1.1,TLSv1.2"); 
//		
//		// System.setProperty("https.protocols", "SSLv3"); // 3 ERRORS
//		
//		//System.setProperty("https.protocols", "TLSv1.1,TLSv1.2"); // 2 OK, Error on testDownloadRepository()
//		System.setProperty("https.protocols", "TLSv1.2"); // 2 OK, Error on testDownloadRepository()
//		
////		System.setProperty("deployment.security.SSLv2Hello", "false");
////		System.setProperty("deployment.security.TLSv1.2", "true"); // 
		
		log("Java version    : " + System.getProperty("java.runtime.version")); // eg : 1.6.0_45-b06
		log("Https protocols : " + System.getProperty("https.protocols"));
	}

	@Test
	public void testGetRepositoriesForBundles() throws TelosysToolsException  {
		log("Getting repositories with properties ... ");
		List<DepotElement> repositories = getRepositories("github_user:telosys-templates");
		assertNotNull(repositories);
		for ( DepotElement repo : repositories ) {
			assertEquals("public", repo.getVisibility() );
			assertEquals("master", repo.getDefaultBranch() );
		}
	}
	
	@Test
	public void testGetRepositoriesForModels() throws TelosysToolsException  {		
		log("Getting repositories without properties (null argument) ... ");
		List<DepotElement> repositories = getRepositories("github_org:telosys-models");
		assertNotNull(repositories);
		for ( DepotElement repo : repositories ) {
			assertEquals("public", repo.getVisibility() );
			assertEquals("master", repo.getDefaultBranch() );
		}
	}
	
	private GitHubClient buildGitHubClient() {
		return new GitHubClient( TestsEnv.getTestFile("cfg/telosys-tools.cfg").getAbsolutePath() ); // v 4.1.1
	}
	
	private List<DepotElement> getRepositories(String depotDefinition) throws TelosysToolsException  {
		log("Getting repositories... ");
		printJavaVersionAndHttpCfg() ;

		GitHubClient gitHubClient = buildGitHubClient();
		
		DepotResponse githubResponse = gitHubClient.getRepositories(new Depot(depotDefinition));
		
		log( githubResponse.getResponseBody() );
		
		List<DepotElement> repositories = githubResponse.getElements();
		log("Repositories (" + repositories.size() + ") : ");
		for ( DepotElement repo : repositories ) {
			log(" .  '" + repo.getName() + "'" 
					+ " / " + repo.getId() + " / '" + repo.getDescription() + "' / " + repo.getSize()
					+ " / '" + repo.getDefaultBranch() + "' / '" + repo.getVisibility() + "'");
		}
		return repositories;
	}
	
	@Test
	public void testDownloadRepositoryBranch() throws TelosysToolsException {

		printJavaVersionAndHttpCfg() ;
		
		GitHubClient gitHubClient = buildGitHubClient(); 
		
		String repoName = "plantuml" ;
		String destinationFile = TestsEnv.getTmpDownloadFolderFullPath() + "/" + repoName + ".zip" ;
		log("Download repository " + repoName );
		log("                 to " + destinationFile );
		gitHubClient.downloadRepositoryBranch(new Depot("github_user:telosys-templates"), repoName, "master", destinationFile);
		log("Done.");		
		assertTrue(new File(destinationFile).exists());
	}

	@Test
	public void testGetRateLimitWithoutAuthentication() throws TelosysToolsException {
		GitHubToken.clear(); // No token => no authentication
		GitHubClient gitHubClient = buildGitHubClient();
		GitHubRateLimitResponse rateLimit = gitHubClient.getRateLimit(new Depot("github_org:myorg"));
		log("Response body : \n" + rateLimit.getResponseBody());
		log("Reset date : " + rateLimit.getResetDate()); 
		assertEquals("60", rateLimit.getLimit()); // no authentication => default rate-limit 
	}

	@Test 
	public void testGetRateLimitWithBadAuthentication() throws TelosysToolsException {
		GitHubToken.set("invalid-token-for-test"  ); // Define an invalid token on the current workstation
		GitHubClient gitHubClient = buildGitHubClient(); 
		GitHubRateLimitResponse r = gitHubClient.getRateLimit(new Depot("github_org:myorg"));		
		assertEquals(401, r.getHttpStatusCode());  // HTTP status code = 401 (UNAUTHORIZED)
		assertTrue(Integer.parseInt(r.getLimit()) <= 60 );     // "0" to "60"
		assertTrue(Integer.parseInt(r.getRemaining()) <= 60 ); // "0" to "60"
		GitHubToken.clear(); // Remove invalid token to keep current workstation in a stable state
	}

	@Test @Ignore // ignore until valid token is set 
	public void testGetRateLimitWithValidAuthentication() throws TelosysToolsException {
		GitHubToken.set("replace-by-a-valid-token-for-test" ); // Don't forget to remove it after test
		GitHubClient gitHubClient = buildGitHubClient(); 
		GitHubRateLimitResponse rateLimit = gitHubClient.getRateLimit(new Depot("github_org:myorg"));
		assertEquals("5000", rateLimit.getLimit()); // authentication ok => rate-limit is  
	}

}
