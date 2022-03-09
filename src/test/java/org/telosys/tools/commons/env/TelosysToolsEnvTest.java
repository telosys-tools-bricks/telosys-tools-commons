package org.telosys.tools.commons.env;

import junit.framework.TestCase;

import org.telosys.tools.commons.TelosysToolsException;

public class TelosysToolsEnvTest extends TestCase {

	public void printSeparator() {
		System.out.println("==============================================================" );
	}
	
	public void test1() {
		printSeparator();
		System.out.println("Test TelosysToolsEnv ...");
		TelosysToolsEnv telosysToolsEnv = TelosysToolsEnv.getInstance();
		
		assertEquals("TelosysTools", telosysToolsEnv.getTelosysToolsFolder() );
		assertEquals("TelosysTools/downloads", telosysToolsEnv.getDownloadsFolder() );
		assertEquals("TelosysTools/lib", telosysToolsEnv.getLibrariesFolder() );
//		assertEquals("TelosysTools", telosysToolsEnv.getModelsFolder() );
		assertEquals("TelosysTools/models", telosysToolsEnv.getModelsFolder() ); // v 3.4.0
		assertEquals("TelosysTools/templates", telosysToolsEnv.getTemplatesFolder() );
		
		assertEquals("telosys-tools.cfg",              telosysToolsEnv.getTelosysToolsConfigFileName() );
		assertEquals("TelosysTools/telosys-tools.cfg", telosysToolsEnv.getTelosysToolsConfigFilePath() );

		assertEquals("databases.yaml",                telosysToolsEnv.getDatabasesDbCfgFileName() );
		assertEquals("TelosysTools/databases.yaml",   telosysToolsEnv.getDatabasesDbCfgFilePath() );
		
	}

}
