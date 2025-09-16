package org.telosys.tools.commons.credentials;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GitCredentialsTest {

	@Test
	public void test1()  {
		Map<String, Object> map = new HashMap<>();
		map.put("models.user",   "alice");
		map.put("models.token",  "ABABABABAABABAB");
		map.put("bundles.user",  "bob");
		map.put("bundles.token", "ZXZXZXZXZXXZXZXZZ");		
		GitCredentials gitCredentials = new GitCredentials(map);
		
		// Check 
		GitUserToken utForModels = gitCredentials.getCredentialsForModels();
		assertEquals("alice",           utForModels.getUser() );
		assertEquals("ABABABABAABABAB", utForModels.getToken() );
		
		GitUserToken utForBundles = gitCredentials.getCredentialsForBundles();
		assertEquals("bob",               utForBundles.getUser() );
		assertEquals("ZXZXZXZXZXXZXZXZZ", utForBundles.getToken() );
		
		// not found
		assertNull( gitCredentials.getCredentialsForGlobal() );
	}

	@Test
	public void test2()  {
		Map<String, Object> map = new HashMap<>();
		map.put("global.user",   "alice");
		map.put("global.token",  "ABABABABAABABAB");
		GitCredentials gitCredentials = new GitCredentials(map);
		
		// Check 
		assertEquals("alice",           gitCredentials.getCredentialsForGlobal().getUser() );
		assertEquals("ABABABABAABABAB", gitCredentials.getCredentialsForGlobal().getToken() );

		assertEquals("alice",           gitCredentials.getCredentialsForModels().getUser() );
		assertEquals("ABABABABAABABAB", gitCredentials.getCredentialsForModels().getToken() );
		
		assertEquals("alice",           gitCredentials.getCredentialsForBundles().getUser() );
		assertEquals("ABABABABAABABAB", gitCredentials.getCredentialsForBundles().getToken() );
	}

	@Test
	public void test3()  {
		Map<String, Object> map = new HashMap<>();
		GitCredentials gitCredentials = new GitCredentials(map);
		// Check 
		assertNull( gitCredentials.getCredentialsForGlobal() );
		assertNull( gitCredentials.getCredentialsForModels() );
		assertNull( gitCredentials.getCredentialsForBundles() );

		gitCredentials.setCredentialsForModels( new GitUserToken("bob", "XYXY") );
		assertNull( gitCredentials.getCredentialsForGlobal() );
		assertNull( gitCredentials.getCredentialsForBundles() );
		assertEquals("bob",  gitCredentials.getCredentialsForModels().getUser() );
		assertEquals("XYXY", gitCredentials.getCredentialsForModels().getToken() );

		gitCredentials.setCredentialsForBundles( new GitUserToken("foo", "ABABAB") );
		assertNull( gitCredentials.getCredentialsForGlobal() );
		assertEquals("foo",    gitCredentials.getCredentialsForBundles().getUser() );
		assertEquals("ABABAB", gitCredentials.getCredentialsForBundles().getToken() );
		
		gitCredentials.setCredentialsForGlobal( new GitUserToken("glob", "GGGG") );
		assertEquals("glob",  gitCredentials.getCredentialsForGlobal().getUser() );
		assertEquals("GGGG",  gitCredentials.getCredentialsForGlobal().getToken() );

		// remove model level => use global 
		gitCredentials.removeCredentialsForModels();
		assertEquals("glob", gitCredentials.getCredentialsForModels().getUser() );
		assertEquals("GGGG", gitCredentials.getCredentialsForModels().getToken() );
		assertEquals("foo",    gitCredentials.getCredentialsForBundles().getUser() );
		assertEquals("ABABAB", gitCredentials.getCredentialsForBundles().getToken() );

		gitCredentials.removeCredentialsForBundles();
		assertEquals("glob", gitCredentials.getCredentialsForModels().getUser() );
		assertEquals("GGGG", gitCredentials.getCredentialsForModels().getToken() );
		assertEquals("glob", gitCredentials.getCredentialsForBundles().getUser() );
		assertEquals("GGGG", gitCredentials.getCredentialsForBundles().getToken() );

		gitCredentials.removeCredentialsForGlobal();
		assertNull( gitCredentials.getCredentialsForGlobal() );
		assertNull( gitCredentials.getCredentialsForModels() );
		assertNull( gitCredentials.getCredentialsForBundles() );
	}

}
