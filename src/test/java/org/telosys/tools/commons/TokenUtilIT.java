package org.telosys.tools.commons;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenUtilIT {

	@Test
	public void test1() throws TelosysToolsException {
		String tokenName = "gh";
		String tokenValue = "abcdefgh";
		
		TokenUtil.deleteToken(tokenName);
		assertFalse(TokenUtil.tokenExists(tokenName));

		File file = TokenUtil.saveToken(tokenName, tokenValue);
		System.out.println("File : " + file.getAbsolutePath());
		
		String t = TokenUtil.loadToken(tokenName);
		assertEquals(tokenValue, t);

		assertTrue(TokenUtil.tokenExists(tokenName));

		TokenUtil.deleteToken(tokenName);
		assertFalse(TokenUtil.tokenExists(tokenName));
	}
	
}
