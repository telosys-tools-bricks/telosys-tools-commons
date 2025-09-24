package org.telosys.tools.commons.credentials;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.TelosysToolsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import junit.env.telosys.tools.commons.TestsEnv;

public class GitCredentialsManagetTest {

	@Test
	public void test1() throws TelosysToolsException {
		File specificDirectory = TestsEnv.getTmpFileOrFolder("dir-cred-data");
		System.out.println("Dir = " + specificDirectory);
		DirUtil.deleteDirectory(specificDirectory);
		assertFalse(specificDirectory.exists() );
		
		GitCredentialsDAO gitCredentialsDAO = new GitCredentialsDAO(specificDirectory);
		assertTrue(specificDirectory.exists() ); // created by GitCredentialsDAO
		// Be sure the files do not exist
		gitCredentialsDAO.deleteFiles();

		GitCredentialsManager gitCredentialsManager = new GitCredentialsManager(specificDirectory);

		assertNull(gitCredentialsManager.getCredentials(GitCredentialsScope.GLOBAL));
		assertNull(gitCredentialsManager.getCredentials(GitCredentialsScope.MODELS));
		assertNull(gitCredentialsManager.getCredentials(GitCredentialsScope.BUNDLES));
		
		assertNull(gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.BUNDLES));
		assertNull(gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.MODELS));
		assertNull(gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.GLOBAL));

		
		gitCredentialsManager.setCredentials(GitCredentialsScope.GLOBAL, new GitCredentials("glob", "GGGGGGGGGGGGGG"));
		assertNotNull(gitCredentialsManager.getCredentials(GitCredentialsScope.GLOBAL));
		assertNull(gitCredentialsManager.getCredentials(GitCredentialsScope.MODELS));
		assertNull(gitCredentialsManager.getCredentials(GitCredentialsScope.BUNDLES));
		assertNotNull(gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.BUNDLES));
		assertNotNull(gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.MODELS));
		assertNotNull(gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.GLOBAL));
		
		
		gitCredentialsManager.setCredentials(GitCredentialsScope.MODELS, new GitCredentials("mod", "MMMMMMMM"));
		assertNotNull(gitCredentialsManager.getCredentials(GitCredentialsScope.GLOBAL));
		assertNotNull(gitCredentialsManager.getCredentials(GitCredentialsScope.MODELS));
		assertNull(gitCredentialsManager.getCredentials(GitCredentialsScope.BUNDLES));
		
		GitCredentials cred = gitCredentialsManager.getCredentials(GitCredentialsScope.MODELS);
		assertEquals("mod",cred.getUserName() );
		assertEquals("MMMMMMMM",cred.getPasswordOrToken());
		
		cred = gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.MODELS);
		assertEquals("mod",cred.getUserName() );
		assertEquals("MMMMMMMM",cred.getPasswordOrToken());

		cred = gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.BUNDLES);
		assertEquals("glob",cred.getUserName() );
		assertEquals("GGGGGGGGGGGGGG",cred.getPasswordOrToken());

		gitCredentialsManager.removeCredentials(GitCredentialsScope.GLOBAL);
		assertNull(gitCredentialsManager.getCredentials(GitCredentialsScope.GLOBAL));
		assertNotNull(gitCredentialsManager.getCredentials(GitCredentialsScope.MODELS));
		assertNull(gitCredentialsManager.getCredentials(GitCredentialsScope.BUNDLES));
		assertNull(gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.BUNDLES));
		assertNotNull(gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.MODELS));
		assertNull(gitCredentialsManager.searchUsableCredentials(GitCredentialsScope.GLOBAL));
	}


}
