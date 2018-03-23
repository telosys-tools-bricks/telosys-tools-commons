package org.telosys.tools.commons.github;

import java.util.List;
import java.util.Properties;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

import org.telosys.tools.commons.github.GitHubClient;
import org.telosys.tools.commons.github.GitHubRepository;

/**
 * This class is "IT" ( Integration Test) due to potential connection problem with https on GitHub
 * Error "javax.net.ssl.SSLException: Received fatal alert: protocol_version" when run with Java 7
 * OK when run with Java 8 
 * https://stackoverflow.com/questions/16541627/javax-net-ssl-sslexception-received-fatal-alert-protocol-version 
 *  
 * @author Laurent Guerin
 *
 */
public class GitHubClientIT extends TestCase {

	private final static String GITHUB_USER = "telosys-tools" ;
		
	private void printJavaVersion() {
		System.out.println("Java version : " + System.getProperty("java.runtime.version")); // eg : 1.6.0_45-b06
	}

	public void testGetRepositories() throws Exception  {
		System.out.println("Getting repositories with properties ... ");
		printJavaVersion() ;
		getRepositories( TestsEnv.loadSpecificProxyProperties() );
	}
	
	public void testGetRepositoriesWithoutProperties() throws Exception  {		
		System.out.println("Getting repositories without properties (null argument) ... ");
		printJavaVersion() ;
		getRepositories( null );
	}
	
	public void getRepositories( Properties properties ) throws Exception  {
		System.out.println("Getting repositories... ");
		printJavaVersion() ;

		GitHubClient gitHubClient = new GitHubClient( properties );
		String jsonResult = gitHubClient.getRepositoriesJSON(GITHUB_USER);
		System.out.println(jsonResult);
		
		List<GitHubRepository> repositories = gitHubClient.getRepositories(GITHUB_USER);
		System.out.println("Repositories (" + repositories.size() + ") : ");
		for ( GitHubRepository repo : repositories ) {
			System.out.println(" .  '" + repo.getName() + "' / " 
					+ repo.getId() + " / '" + repo.getDescription() + "' / " + repo.getSize() );
		}
	}
	
	public void testDownloadRepository() throws Exception {
		
		//Properties properties = HttpTestConfig.getSpecificProxyProperties();	
		Properties properties = TestsEnv.loadSpecificProxyProperties() ;
		GitHubClient gitHubClient = new GitHubClient( properties);
		
		String repoName = "basic-templates-TT210" ;
		String destinationFile = TestsEnv.getTmpDownloadFolderFullPath() + "/" + repoName + ".zip" ;
		System.out.println("Download repository " + repoName );
		System.out.println("                 to " + destinationFile );
		gitHubClient.downloadRepository(GITHUB_USER, repoName, destinationFile);
		System.out.println("Done.");		
	}
}
