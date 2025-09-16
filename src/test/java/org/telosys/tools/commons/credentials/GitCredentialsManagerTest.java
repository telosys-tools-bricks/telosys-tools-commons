package org.telosys.tools.commons.credentials;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GitCredentialsManagerTest {

	@Test
	public void testSaveAndLoad() throws TelosysToolsException {
		GitCredentialsManager gitCredentialsManager = new GitCredentialsManager();

		Map<String, Object> map = new HashMap<>();
		map.put("models.user",   "alice");
		map.put("models.token",  "ABABABABAABABAB");
		map.put("bundles.user",  "bob");
		map.put("bundles.token", "ZXZXZXZXZXXZXZXZZ");		
		GitCredentials gitCredentials = new GitCredentials(map);
		
		// SAVE 
		gitCredentialsManager.save(gitCredentials);
		
		// LOAD 
		GitCredentials gitCredentials2 = gitCredentialsManager.load();
		
		// Check result
		GitUserToken utForModels = gitCredentials2.getCredentialsForModels();
		assertEquals("alice",           utForModels.getUser() );
		assertEquals("ABABABABAABABAB", utForModels.getToken() );
		
		GitUserToken utForBundles = gitCredentials2.getCredentialsForBundles();
		assertEquals("bob",               utForBundles.getUser() );
		assertEquals("ZXZXZXZXZXXZXZXZZ", utForBundles.getToken() );
		
		// not found
		assertNull( gitCredentials2.getGlobalCredentials() );
	}


}
