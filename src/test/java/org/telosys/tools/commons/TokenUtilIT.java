package org.telosys.tools.commons;

import java.io.File;

import org.junit.Test;
import org.telosys.tools.commons.credentials.TokenUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenUtilIT {

	@Test
	public void testSaveLoadNoEncryption() throws TelosysToolsException {
		String tokenName = "test1";
		String tokenValue = "abcdefgh";
		final boolean no_encryption = false;
		
		// PRE : DELETE 
		TokenUtil.deleteToken(tokenName, no_encryption);
		assertFalse(TokenUtil.tokenExists(tokenName));

		// SAVE
		File file = TokenUtil.saveToken(tokenName, tokenValue, no_encryption);
		System.out.println("File : " + file.getAbsolutePath());
		
		// LOAD
		String t = TokenUtil.loadToken(tokenName, no_encryption);
		
		// CHECK
		assertEquals(tokenValue, t);
		assertTrue(TokenUtil.tokenExists(tokenName));

		TokenUtil.deleteToken(tokenName, no_encryption);
		assertFalse(TokenUtil.tokenExists(tokenName));
	}
	
	@Test
	public void testSaveLoadWithEncryption() throws TelosysToolsException {
		String tokenName = "test2";
		String tokenValue = "my-token-for-test";
		final boolean encryption = true;
		
		TokenUtil.deleteToken(tokenName, encryption);
		assertFalse(TokenUtil.tokenExists(tokenName));
		assertFalse(TokenUtil.tokenKeyExists(tokenName));

		TokenUtil.saveToken(tokenName, tokenValue, encryption);
		
		String t = TokenUtil.loadToken(tokenName, encryption);
		
		assertEquals(tokenValue, t);
		assertTrue(TokenUtil.tokenExists(tokenName));
		assertTrue(TokenUtil.tokenKeyExists(tokenName));

		TokenUtil.deleteToken(tokenName, encryption);
		assertFalse(TokenUtil.tokenExists(tokenName));
		assertFalse(TokenUtil.tokenKeyExists(tokenName));
	}
	
}
