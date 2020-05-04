package org.telosys.tools.commons.github;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * This class is "IT" ( Integration Test) 
 * 
 * See : https://developer.github.com/v3/#rate-limiting 
 *  
 * @author Laurent Guerin
 *
 */
public class GitHubRateLimitTest {

	@Test
	public void test0()  {
		Date d = new Date(12345678L);
		assertEquals(12345678L, d.getTime());
	}
	
	@Test
	public void test1() {
		GitHubRateLimit gitHubRateLimit = new GitHubRateLimit("60","45","1588622736");
		assertEquals(60, gitHubRateLimit.getLimitAsInt());
		assertEquals(45, gitHubRateLimit.getRemainingAsInt());
		java.util.Date date = gitHubRateLimit.getResetAsDate();
		assertNotNull(date);
		long milliseconds = 1588622736L * 1000 ;
		assertEquals(milliseconds, date.getTime());
	}
	
	@Test
	public void test2()  {
		GitHubRateLimit gitHubRateLimit = new GitHubRateLimit(null, null, null);
		
		assertNull(gitHubRateLimit.getLimit());
		assertNull(gitHubRateLimit.getRemaining());
		assertNull(gitHubRateLimit.getReset());
		
		assertEquals(0, gitHubRateLimit.getLimitAsInt());
		assertEquals(0, gitHubRateLimit.getRemainingAsInt());
		java.util.Date date = gitHubRateLimit.getResetAsDate();
		assertNotNull(date);
		assertEquals(0L, date.getTime());
	}
	
}
