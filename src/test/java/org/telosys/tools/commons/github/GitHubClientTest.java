package org.telosys.tools.commons.github;

import java.util.List;
import java.util.Properties;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

import org.telosys.tools.commons.github.GitHubClient;
import org.telosys.tools.commons.github.GitHubRepository;

public class GitHubClientTest extends TestCase {

	private final static String GITHUB_USER = "telosys-tools" ;
	// "telosys-tools-beta" ;
		
	public void testGetRepositories() throws Exception  {
		
		System.out.println("Getting repositories with properties ... ");
		getRepositories( TestsEnv.loadSpecificProxyProperties() );
	}
	
	public void testGetRepositoriesWithoutProperties() throws Exception  {		
		System.out.println("Getting repositories without properties (null argument) ... ");
		getRepositories( null );
	}
	
	public void getRepositories( Properties properties ) throws Exception  {
		
		System.out.println("Getting repositories... ");

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
		String destinationFile = TestsEnv.getTmpDownloadFolder() + "/" + repoName + ".zip" ;
		System.out.println("Download repository " + repoName );
		System.out.println("                 to " + destinationFile );
		gitHubClient.downloadRepository(GITHUB_USER, repoName, destinationFile);
		System.out.println("Done.");		
	}
}
