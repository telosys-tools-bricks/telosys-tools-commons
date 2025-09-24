package org.telosys.tools.commons.credentials;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.TelosysToolsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import junit.env.telosys.tools.commons.TestsEnv;

public class GitCredentialsDAOTest {

	@Test
	public void testSaveAndLoad() throws TelosysToolsException {
		
		File specificDirectory = TestsEnv.getTmpFileOrFolder("dir-foo/aa/bb");
		System.out.println("Dir = " + specificDirectory);
		DirUtil.deleteDirectory(specificDirectory);
		assertFalse(specificDirectory.exists() );
		
		GitCredentialsDAO gitCredentialsDAO = new GitCredentialsDAO(specificDirectory);
		assertTrue(specificDirectory.exists() ); // created by GitCredentialsDAO
		// Be sure the files do not exist
		gitCredentialsDAO.deleteFiles();

		// LOAD 
		GitCredentialsHolder gitCredentialsHolder = gitCredentialsDAO.load();
		assertTrue(gitCredentialsHolder.isEmpty());

		// SAVE 
		Map<String, Object> map = new HashMap<>();
		map.put("models.user",   "alice");
		map.put("models.token",  "ABABABABAABABAB");
		map.put("bundles.user",  "bob");
		map.put("bundles.token", "ZXZXZXZXZXXZXZXZZ");				
		gitCredentialsDAO.save( new GitCredentialsHolder(map) );
		
		// LOAD 
		gitCredentialsHolder = gitCredentialsDAO.load();
		assertFalse(gitCredentialsHolder.isEmpty());
		
		// Check result
		GitCredentials credForModels = gitCredentialsHolder.getCredentials(GitCredentialsScope.MODELS);
		assertEquals("alice",           credForModels.getUserName() );
		assertEquals("ABABABABAABABAB", credForModels.getPasswordOrToken() );
		
		GitCredentials credForBundles = gitCredentialsHolder.getCredentials(GitCredentialsScope.BUNDLES);
		assertEquals("bob",               credForBundles.getUserName() );
		assertEquals("ZXZXZXZXZXXZXZXZZ", credForBundles.getPasswordOrToken() );
		
		// not found
		assertNull( gitCredentialsHolder.getCredentials(GitCredentialsScope.GLOBAL) );
	}
}
