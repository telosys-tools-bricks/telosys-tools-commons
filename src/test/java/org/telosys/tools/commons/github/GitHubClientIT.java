package org.telosys.tools.commons.github;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.depot.DepotElement;
import org.telosys.tools.commons.depot.DepotResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

	private static final String TELOSYS_TEMPLATES = "telosys-templates" ;

	private void log(String msg) {
		System.out.println(msg);
	}
	private void printJavaVersion() {
		log("Setting system property 'https.protocols'... ");

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
	public void testGetRepositories() throws GitHubClientException  {
		log("Getting repositories with properties ... ");
		printJavaVersion() ;
		getRepositories();
	}
	
	@Test
	public void testGetRepositoriesWithoutProperties() throws GitHubClientException  {		
		log("Getting repositories without properties (null argument) ... ");
		printJavaVersion() ;
		getRepositories();
	}
	
	private GitHubClient buildGitHubClient() {
		return new GitHubClient( TestsEnv.getTestFile("cfg/telosys-tools.cfg").getAbsolutePath() ); // v 4.1.1
	}
	
	private void getRepositories() throws GitHubClientException  {
		log("Getting repositories... ");
		printJavaVersion() ;

		GitHubClient gitHubClient = buildGitHubClient();
		
		DepotResponse githubResponse = gitHubClient.getRepositories(TELOSYS_TEMPLATES);
		
		log( githubResponse.getResponseBody() );
		
		List<DepotElement> repositories = githubResponse.getElements();
		log("Repositories (" + repositories.size() + ") : ");
		for ( DepotElement repo : repositories ) {
			log(" .  '" + repo.getName() + "' / " 
					+ repo.getId() + " / '" + repo.getDescription() + "' / " + repo.getSize() );
		}
	}
	
	@Test
	public void testDownloadRepository() throws GitHubClientException {

		printJavaVersion() ;
		
		GitHubClient gitHubClient = buildGitHubClient(); 
		
		String repoName = "plantuml" ;
		String destinationFile = TestsEnv.getTmpDownloadFolderFullPath() + "/" + repoName + ".zip" ;
		log("Download repository " + repoName );
		log("                 to " + destinationFile );
		gitHubClient.downloadRepository(TELOSYS_TEMPLATES, repoName, destinationFile);
		log("Done.");		
	}

	@Test
	public void testGetRateLimitWithoutAuthentication() throws TelosysToolsException, GitHubClientException {
		GitHubToken.clear(); // No token => no authentication
		GitHubClient gitHubClient = buildGitHubClient();
		GitHubRateLimitResponse rateLimit = gitHubClient.getRateLimit();
		log("Response body : \n" + rateLimit.getResponseBody());
		log("Reset date : " + rateLimit.getResetDate()); 
		assertEquals("60", rateLimit.getLimit()); // no authentication => default rate-limit 
	}

	@Test 
	public void testGetRateLimitWithBadAuthentication() throws TelosysToolsException {
		GitHubToken.set("invalid-token-for-test"  );
		GitHubClient gitHubClient = buildGitHubClient(); 
		try {
			gitHubClient.getRateLimit();
			fail();
		} catch (GitHubClientException e) {
			// HTTP status code = 401 (UNAUTHORIZED)
			e.printStackTrace();
		} 
	}

	@Test @Ignore // ignore until valid token is set 
	public void testGetRateLimitWithValidAuthentication() throws TelosysToolsException, GitHubClientException {
		GitHubToken.set("replace-by-a-valid-token-for-test" ); // Don't forget to remove it after test
		GitHubClient gitHubClient = buildGitHubClient(); 
		GitHubRateLimitResponse rateLimit = gitHubClient.getRateLimit();
		assertEquals("5000", rateLimit.getLimit()); // authentication ok => rate-limit is  
	}

}
