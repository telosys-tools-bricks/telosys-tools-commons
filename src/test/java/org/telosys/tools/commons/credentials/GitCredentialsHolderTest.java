package org.telosys.tools.commons.credentials;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GitCredentialsHolderTest {

	@Test
	public void test1()  {
		Map<String, Object> map = new HashMap<>();
		map.put("models.user",   "alice");
		map.put("models.token",  "ABABABABAABABAB");
		map.put("bundles.user",  "bob");
		map.put("bundles.token", "ZXZXZXZXZXXZXZXZZ");		
		GitCredentialsHolder gitCredentialsHolder = new GitCredentialsHolder(map);
		
		// Check 
		GitCredentials utForModels = gitCredentialsHolder.getCredentials(GitCredentialsScope.MODELS);
		assertEquals("alice",           utForModels.getUser() );
		assertEquals("ABABABABAABABAB", utForModels.getToken() );
		
		GitCredentials utForBundles = gitCredentialsHolder.getCredentials(GitCredentialsScope.BUNDLES);
		assertEquals("bob",               utForBundles.getUser() );
		assertEquals("ZXZXZXZXZXXZXZXZZ", utForBundles.getToken() );
		
		// not found
		assertNull( gitCredentialsHolder.getCredentials(GitCredentialsScope.GLOBAL) );
	}

	@Test
	public void test2()  {
		Map<String, Object> map = new HashMap<>();
		map.put("global.user",   "alice");
		map.put("global.token",  "ABABABABAABABAB");
		GitCredentialsHolder gitCredentialsHolder = new GitCredentialsHolder(map);
		
		// Check 
		GitCredentials cred = gitCredentialsHolder.getCredentials(GitCredentialsScope.GLOBAL);
		assertEquals("alice",           cred.getUser() );
		assertEquals("ABABABABAABABAB", cred.getToken() );

		cred = gitCredentialsHolder.getCredentials(GitCredentialsScope.MODELS);
		assertEquals("alice",           cred.getUser() );
		assertEquals("ABABABABAABABAB", cred.getToken() );
		
		cred = gitCredentialsHolder.getCredentials(GitCredentialsScope.BUNDLES);
		assertEquals("alice",           cred.getUser() );
		assertEquals("ABABABABAABABAB", cred.getToken() );
	}

	@Test
	public void test3()  {
		Map<String, Object> map = new HashMap<>();
		GitCredentialsHolder holder = new GitCredentialsHolder(map);
		// Check 
		assertNull( holder.getCredentials(GitCredentialsScope.GLOBAL));
		assertNull( holder.getCredentials(GitCredentialsScope.MODELS) );
		assertNull( holder.getCredentials(GitCredentialsScope.BUNDLES) );

		holder.setCredentials( GitCredentialsScope.MODELS, new GitCredentials("bob", "XYXY") );
		assertNull( holder.getCredentials(GitCredentialsScope.GLOBAL) );
		assertNull( holder.getCredentials(GitCredentialsScope.BUNDLES) );
		assertEquals("bob",  holder.getCredentials(GitCredentialsScope.MODELS).getUser() );
		assertEquals("XYXY", holder.getCredentials(GitCredentialsScope.MODELS).getToken() );

		holder.setCredentials(GitCredentialsScope.BUNDLES, new GitCredentials("foo", "ABABAB") );
		assertNull( holder.getCredentials(GitCredentialsScope.GLOBAL) );
		assertEquals("foo",    holder.getCredentials(GitCredentialsScope.BUNDLES).getUser() );
		assertEquals("ABABAB", holder.getCredentials(GitCredentialsScope.BUNDLES).getToken() );
		
		holder.setCredentials( GitCredentialsScope.GLOBAL, new GitCredentials("glob", "GGGG") );
		assertEquals("glob",  holder.getCredentials(GitCredentialsScope.GLOBAL).getUser() );
		assertEquals("GGGG",  holder.getCredentials(GitCredentialsScope.GLOBAL).getToken() );

		// remove model level => use global 
		holder.removeCredentials(GitCredentialsScope.MODELS);
		assertEquals("glob",   holder.getCredentials(GitCredentialsScope.MODELS).getUser() );
		assertEquals("GGGG",   holder.getCredentials(GitCredentialsScope.MODELS).getToken() );
		assertEquals("foo",    holder.getCredentials(GitCredentialsScope.BUNDLES).getUser() );
		assertEquals("ABABAB", holder.getCredentials(GitCredentialsScope.BUNDLES).getToken() );

		holder.removeCredentials(GitCredentialsScope.BUNDLES);
		assertEquals("glob", holder.getCredentials(GitCredentialsScope.MODELS).getUser() );
		assertEquals("GGGG", holder.getCredentials(GitCredentialsScope.MODELS).getToken() );
		assertEquals("glob", holder.getCredentials(GitCredentialsScope.BUNDLES).getUser() );
		assertEquals("GGGG", holder.getCredentials(GitCredentialsScope.BUNDLES).getToken() );

		holder.removeCredentials(GitCredentialsScope.GLOBAL);
		assertNull( holder.getCredentials(GitCredentialsScope.GLOBAL) );
		assertNull( holder.getCredentials(GitCredentialsScope.MODELS) );
		assertNull( holder.getCredentials(GitCredentialsScope.BUNDLES) );
	}

}
