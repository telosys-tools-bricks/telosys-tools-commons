package org.telosys.tools.commons.github;

import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.telosys.tools.commons.depot.DepotElement;
import org.telosys.tools.commons.depot.DepotResponse;

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

	private static final String GITHUB_USER = "telosys-templates" ;

	private void printJavaVersion() {
		System.out.println("Setting system property 'https.protocols'... ");

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
		
		System.out.println("Java version    : " + System.getProperty("java.runtime.version")); // eg : 1.6.0_45-b06
		System.out.println("Https protocols : " + System.getProperty("https.protocols"));
	}

	@Test
	public void testGetRepositories() throws Exception  {
		System.out.println("Getting repositories with properties ... ");
		printJavaVersion() ;
		getRepositories( TestsEnv.loadSpecificProxyProperties() );
	}
	
	@Test
	public void testGetRepositoriesWithoutProperties() throws Exception  {		
		System.out.println("Getting repositories without properties (null argument) ... ");
		printJavaVersion() ;
		getRepositories( null );
	}
	
	private GitHubClient buildGitHubClient() {
		return new GitHubClient( TestsEnv.getTestFile("cfg/telosys-tools.cfg").getAbsolutePath() ); // v 4.1.1
	}
	
	private void getRepositories( Properties properties ) throws Exception  {
		System.out.println("Getting repositories... ");
		printJavaVersion() ;

		GitHubClient gitHubClient = buildGitHubClient();
		
		DepotResponse githubResponse = gitHubClient.getRepositories(GITHUB_USER);
		
		System.out.println( githubResponse.getResponseBody() );
		
		List<DepotElement> repositories = githubResponse.getElements();
		System.out.println("Repositories (" + repositories.size() + ") : ");
		for ( DepotElement repo : repositories ) {
			System.out.println(" .  '" + repo.getName() + "' / " 
					+ repo.getId() + " / '" + repo.getDescription() + "' / " + repo.getSize() );
		}
	}
	
	@Test
	public void testDownloadRepository() throws Exception {

		printJavaVersion() ;
		
		GitHubClient gitHubClient = buildGitHubClient(); 
		
		String repoName = "plantuml" ;
		String destinationFile = TestsEnv.getTmpDownloadFolderFullPath() + "/" + repoName + ".zip" ;
		System.out.println("Download repository " + repoName );
		System.out.println("                 to " + destinationFile );
		gitHubClient.downloadRepository(GITHUB_USER, repoName, destinationFile);
		System.out.println("Done.");		
	}

	@Test
	public void testGetRateLimit() throws Exception {

		GitHubUser.clear();

		GitHubClient gitHubClient = buildGitHubClient();
		GitHubRateLimitResponse rateLimit = gitHubClient.getRateLimit();
		
		System.out.println(rateLimit);
		System.out.println("Response body : \n" + rateLimit.getResponseBody());
		System.out.println("Reset date : " + rateLimit.getResetDate()); 
	}

	@Test // NO ERROR for unknown user // (expected=Exception.class)
	public void testGetRateLimitWithBadUser() throws Exception {

		// User + password
		GitHubUser.set("fake-user-azer7766-OuPMK", "xxxxx");
		
		GitHubClient gitHubClient = buildGitHubClient(); 
		GitHubRateLimitResponse rateLimit = gitHubClient.getRateLimit(); 
		
		System.out.println(rateLimit); // limit='60', remaining='60'
		System.out.println("Response body : \n" + rateLimit.getResponseBody());
		System.out.println("Reset date : " + rateLimit.getResetDate()); 
	}

}
