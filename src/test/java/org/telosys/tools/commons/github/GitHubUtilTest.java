package org.telosys.tools.commons.github;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class is "IT" ( Integration Test) due to potential connection problem with https on GitHub
 * Error "javax.net.ssl.SSLException: Received fatal alert: protocol_version" when run with Java 7
 * OK when run with Java 8 
 * https://stackoverflow.com/questions/16541627/javax-net-ssl-sslexception-received-fatal-alert-protocol-version 
 *  
 * @author Laurent Guerin
 *
 */
public class GitHubUtilTest {

	private void print(String msg) {
		System.out.println(msg);
	}
	
	@Test
	public void testURLBuilder() throws Exception  {
		String pattern = GitHubClient.GIT_HUB_REPO_URL_PATTERN ;
		print("URL pattern : " + pattern ) ;
		String url = GitHubUtil.buildGitHubURL(GitHubTestsConst.GITHUB_USER, "php7-web-mvc", pattern);
		print("URL result  : " + url ) ;
		assertEquals("https://github.com/telosys-templates-v3/php7-web-mvc/archive/master.zip", url);
	}
	
}
