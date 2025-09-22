package org.telosys.tools.commons.credentials;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GitCredentialsDAOTest {

	@Test
	public void testSaveAndLoad() throws TelosysToolsException {
		GitCredentialsDAO gitCredentialsDAO = new GitCredentialsDAO();

		Map<String, Object> map = new HashMap<>();
		map.put("models.user",   "alice");
		map.put("models.token",  "ABABABABAABABAB");
		map.put("bundles.user",  "bob");
		map.put("bundles.token", "ZXZXZXZXZXXZXZXZZ");		
		
		// SAVE 
		gitCredentialsDAO.save( new GitCredentialsHolder(map) );
		
		// LOAD 
		GitCredentialsHolder gitCredentialsHolder = gitCredentialsDAO.load();
		
		// Check result
		GitCredentials credForModels = gitCredentialsHolder.getCredentials(GitCredentialsScope.MODELS);
		assertEquals("alice",           credForModels.getUser() );
		assertEquals("ABABABABAABABAB", credForModels.getToken() );
		
		GitCredentials credForBundles = gitCredentialsHolder.getCredentials(GitCredentialsScope.BUNDLES);
		assertEquals("bob",               credForBundles.getUser() );
		assertEquals("ZXZXZXZXZXXZXZXZZ", credForBundles.getToken() );
		
		// not found
		assertNull( gitCredentialsHolder.getCredentials(GitCredentialsScope.GLOBAL) );
	}


}
