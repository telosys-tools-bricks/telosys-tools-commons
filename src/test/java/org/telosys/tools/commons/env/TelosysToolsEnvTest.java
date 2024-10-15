package org.telosys.tools.commons.env;

import junit.framework.TestCase;

public class TelosysToolsEnvTest extends TestCase {

	
	public void test1() {
		assertEquals("TelosysTools", TelosysToolsEnv.getTelosysToolsFolder() );
		
		assertEquals("TelosysTools/downloads", TelosysToolsEnv.getDownloadsFolder() );
		assertEquals("TelosysTools/lib", TelosysToolsEnv.getLibrariesFolder() );
		assertEquals("TelosysTools/models", TelosysToolsEnv.getModelsFolder() );
		assertEquals("TelosysTools/templates", TelosysToolsEnv.getTemplatesFolder() );
		
		assertEquals("telosys-tools.cfg",              TelosysToolsEnv.getTelosysToolsConfigFileName() );
		assertEquals("TelosysTools/telosys-tools.cfg", TelosysToolsEnv.getTelosysToolsConfigFilePath() );

		assertEquals("databases.yaml",                TelosysToolsEnv.getDatabasesDbCfgFileName() );
		assertEquals("TelosysTools/databases.yaml",   TelosysToolsEnv.getDatabasesDbCfgFilePath() );
	}
}
