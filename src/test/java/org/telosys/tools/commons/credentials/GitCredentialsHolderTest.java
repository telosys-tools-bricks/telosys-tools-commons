package org.telosys.tools.commons.credentials;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GitCredentialsHolderTest {

	private void checkEmpty(GitCredentialsHolder gitCredentialsHolder)  {
		assertTrue( gitCredentialsHolder.isEmpty() ) ;
		assertFalse( gitCredentialsHolder.credentialsExist(GitCredentialsScope.MODELS));
		assertFalse( gitCredentialsHolder.credentialsExist(GitCredentialsScope.BUNDLES));
		assertFalse( gitCredentialsHolder.credentialsExist(GitCredentialsScope.GLOBAL));
		assertNull( gitCredentialsHolder.getCredentials(GitCredentialsScope.GLOBAL));
		assertNull( gitCredentialsHolder.getCredentials(GitCredentialsScope.MODELS) );
		assertNull( gitCredentialsHolder.getCredentials(GitCredentialsScope.BUNDLES) );
	}

	@Test
	public void testEmpty1()  {
		checkEmpty( new GitCredentialsHolder() );
	}
	@Test
	public void testEmpty2()  {
		checkEmpty( new GitCredentialsHolder(new HashMap<>()) );
	}
	
	@Test
	public void testGet1()  {
		Map<String, Object> map = new HashMap<>();
		map.put("models.user",   "alice");
		map.put("models.token",  "ABABABABAABABAB");
		map.put("bundles.user",  "bob");
		map.put("bundles.token", "ZXZXZXZXZXXZXZXZZ");		
		GitCredentialsHolder gitCredentialsHolder = new GitCredentialsHolder(map);
		
		// Check 
		assertTrue( gitCredentialsHolder.credentialsExist(GitCredentialsScope.MODELS));
		assertTrue( gitCredentialsHolder.credentialsExist(GitCredentialsScope.BUNDLES));
		assertFalse( gitCredentialsHolder.credentialsExist(GitCredentialsScope.GLOBAL));
		GitCredentials utForModels = gitCredentialsHolder.getCredentials(GitCredentialsScope.MODELS);
		assertEquals("alice",           utForModels.getUserName() );
		assertEquals("ABABABABAABABAB", utForModels.getPasswordOrToken() );
		
		GitCredentials utForBundles = gitCredentialsHolder.getCredentials(GitCredentialsScope.BUNDLES);
		assertEquals("bob",               utForBundles.getUserName() );
		assertEquals("ZXZXZXZXZXXZXZXZZ", utForBundles.getPasswordOrToken() );
		
		// not found
		assertNull( gitCredentialsHolder.getCredentials(GitCredentialsScope.GLOBAL) );
	}

	@Test
	public void testGet2()  {
		Map<String, Object> map = new HashMap<>();
		map.put("global.user",   "alice");
		map.put("global.token",  "ABABABABAABABAB");
		GitCredentialsHolder gitCredentialsHolder = new GitCredentialsHolder(map);
		
		// Check 
		GitCredentials gitCredentials = gitCredentialsHolder.getCredentials(GitCredentialsScope.GLOBAL);
		assertEquals("alice",           gitCredentials.getUserName() );
		assertEquals("ABABABABAABABAB", gitCredentials.getPasswordOrToken() );

		assertFalse(gitCredentialsHolder.credentialsExist(GitCredentialsScope.MODELS));
		assertNull( gitCredentialsHolder.getCredentials(GitCredentialsScope.MODELS));
//		assertNull( gitCredentials.getUserName() );
//		assertNull( gitCredentials.getPasswordOrToken() );
		
		assertFalse(gitCredentialsHolder.credentialsExist(GitCredentialsScope.BUNDLES));
		assertNull( gitCredentialsHolder.getCredentials(GitCredentialsScope.BUNDLES));
//		assertEquals("alice",           gitCredentials.getUserName() );
//		assertEquals("ABABABABAABABAB", gitCredentials.getPasswordOrToken() );
	}

	@Test
	public void testSetGetRemove()  {
		Map<String, Object> map = new HashMap<>();
		GitCredentialsHolder holder = new GitCredentialsHolder(map);
		// Check 
		assertNull( holder.getCredentials(GitCredentialsScope.GLOBAL));
		assertNull( holder.getCredentials(GitCredentialsScope.MODELS) );
		assertNull( holder.getCredentials(GitCredentialsScope.BUNDLES) );

		//--- SET ----------
		holder.setCredentials( GitCredentialsScope.MODELS, new GitCredentials("bob", "XYXY") );
		assertNull( holder.getCredentials(GitCredentialsScope.GLOBAL) );
		assertNull( holder.getCredentials(GitCredentialsScope.BUNDLES) );
		assertTrue( holder.credentialsExist(GitCredentialsScope.MODELS));
		assertFalse( holder.credentialsExist(GitCredentialsScope.BUNDLES));
		assertFalse( holder.credentialsExist(GitCredentialsScope.GLOBAL));
		assertEquals("bob",  holder.getCredentials(GitCredentialsScope.MODELS).getUserName() );
		assertEquals("XYXY", holder.getCredentials(GitCredentialsScope.MODELS).getPasswordOrToken() );

		holder.setCredentials(GitCredentialsScope.BUNDLES, new GitCredentials("foo", "ABABAB") );
		assertNull( holder.getCredentials(GitCredentialsScope.GLOBAL) );
		assertEquals("foo",    holder.getCredentials(GitCredentialsScope.BUNDLES).getUserName() );
		assertEquals("ABABAB", holder.getCredentials(GitCredentialsScope.BUNDLES).getPasswordOrToken() );
		
		holder.setCredentials( GitCredentialsScope.GLOBAL, new GitCredentials("glob", "GGGG") );
		assertEquals("glob",  holder.getCredentials(GitCredentialsScope.GLOBAL).getUserName() );
		assertEquals("GGGG",  holder.getCredentials(GitCredentialsScope.GLOBAL).getPasswordOrToken() );

		//--- REMOVE ----------
		// remove model level => use global 
		assertTrue( holder.removeCredentials(GitCredentialsScope.MODELS) );
		assertFalse(holder.credentialsExist(GitCredentialsScope.MODELS));
		assertNull(holder.getCredentials(GitCredentialsScope.MODELS) );
//		assertEquals("glob",   holder.getCredentials(GitCredentialsScope.MODELS).getUserName() );
//		assertEquals("GGGG",   holder.getCredentials(GitCredentialsScope.MODELS).getPasswordOrToken() );
//		assertEquals("foo",    holder.getCredentials(GitCredentialsScope.BUNDLES).getUserName() );
//		assertEquals("ABABAB", holder.getCredentials(GitCredentialsScope.BUNDLES).getPasswordOrToken() );
		assertFalse( holder.removeCredentials(GitCredentialsScope.MODELS) );

		assertTrue( holder.removeCredentials(GitCredentialsScope.BUNDLES) );
		assertFalse(holder.credentialsExist(GitCredentialsScope.BUNDLES));
		assertNull(holder.getCredentials(GitCredentialsScope.BUNDLES) );
//		assertEquals("glob", holder.getCredentials(GitCredentialsScope.MODELS).getUserName() );
//		assertEquals("GGGG", holder.getCredentials(GitCredentialsScope.MODELS).getPasswordOrToken() );
//		assertEquals("glob", holder.getCredentials(GitCredentialsScope.BUNDLES).getUserName() );
//		assertEquals("GGGG", holder.getCredentials(GitCredentialsScope.BUNDLES).getPasswordOrToken() );
		assertFalse( holder.removeCredentials(GitCredentialsScope.BUNDLES) );

		assertTrue( holder.removeCredentials(GitCredentialsScope.GLOBAL));
		assertNull( holder.getCredentials(GitCredentialsScope.GLOBAL) );
		assertNull( holder.getCredentials(GitCredentialsScope.MODELS) );
		assertNull( holder.getCredentials(GitCredentialsScope.BUNDLES) );
		assertFalse(holder.removeCredentials(GitCredentialsScope.GLOBAL));
	}

}
