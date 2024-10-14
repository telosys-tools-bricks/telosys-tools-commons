package org.telosys.tools.commons.github;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
public class GitHubClientTest {

	private GitHubClient buildGitHubClient() {
		return new GitHubClient( TestsEnv.getTestFile("cfg/telosys-tools.cfg").getAbsolutePath() );
	}
	
	@Test
	public void testDownloadRepositoryURL() {
		GitHubClient gitHubClient = buildGitHubClient(); 
		
		String url = gitHubClient.buildDownloadBranchURL("telosys-templates", "php7-web-mvc", "master");
		assertEquals("https://github.com/telosys-templates/php7-web-mvc/archive/refs/heads/master.zip", url);

		url = gitHubClient.buildDownloadBranchURL("telosys-models", "foo-model", "main");
		assertEquals("https://github.com/telosys-models/foo-model/archive/refs/heads/main.zip", url);
	}

}
