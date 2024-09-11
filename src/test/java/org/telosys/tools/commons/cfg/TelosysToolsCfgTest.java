package org.telosys.tools.commons.cfg;

import java.util.Properties;
import java.util.Set;

import org.junit.Test;
import org.telosys.tools.commons.variables.Variable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

public class TelosysToolsCfgTest {

	private void println(String s) {
		System.out.println(s);
	}
	private void printSeparator() {
		println("==============================================================" );
	}
	
	private void print(TelosysToolsCfg telosysToolsCfg) {
		println( "getProjectAbsolutePath = " + telosysToolsCfg.getProjectAbsolutePath() );
		println( "getCfgFileAbsolutePath = " + telosysToolsCfg.getCfgFileAbsolutePath() );
		println( "getDatabasesDbCfgFile             = " + telosysToolsCfg.getDatabasesDbCfgFile());
		println( "getDatabasesDbCfgFileAbsolutePath = " + telosysToolsCfg.getDatabasesDbCfgFileAbsolutePath());
		
		print(telosysToolsCfg.getAllVariables());
		
		print(telosysToolsCfg.getProperties());
	}
	
	private void print(Variable[] variables) {
		println("VARIABLES : ");
		for ( Variable v : variables ) {
			println(" . " + v.getName() + " = " + v.getValue() + " ( " + v.getSymbolicName() + " )");
		}
	}
	
	private void print(Properties properties) {
		println("PROPERTIES : ");
		Set<Object> keys = properties.keySet();
		for ( Object k : keys ) {
			println(" . " + k + " = " + properties.get(k) );
		}
	}
	
	private static final String PROJECT_ABSOLUTE_PATH     = "X:/foo/bar/myproject" ;
	private static final String CONFIG_FILE_ABSOLUTE_PATH = "X:/foo/bar/myproject/telosys-tools.cfg" ;
	
	private TelosysToolsCfg getTelosysToolsCfgWithoutProperties() {
		TelosysToolsCfg telosysToolsCfg = new TelosysToolsCfg(PROJECT_ABSOLUTE_PATH, CONFIG_FILE_ABSOLUTE_PATH, null);
//		print(telosysToolsCfg);
		return telosysToolsCfg ;
	}
	
	@Test
	public void testDefaultPackages() {
		printSeparator();
		
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfgWithoutProperties() ;
		
		assertEquals(PROJECT_ABSOLUTE_PATH,     telosysToolsCfg.getProjectAbsolutePath() );
		assertEquals(CONFIG_FILE_ABSOLUTE_PATH, telosysToolsCfg.getCfgFileAbsolutePath() );
		
		// Default packages
		assertEquals("org.demo", telosysToolsCfg.getRootPackage() );
		assertEquals("org.demo.bean", telosysToolsCfg.getEntityPackage() );
		
		// Variables 
		assertEquals(0, telosysToolsCfg.getSpecificVariables().length ); 
		assertEquals(9, telosysToolsCfg.getAllVariables().length ); 
		
	}

	@Test
	public void testTelosysToolsFolders() {
		printSeparator();
		
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfgWithoutProperties() ;
		
		assertEquals("TelosysTools",            telosysToolsCfg.getTelosysToolsFolder() );
		assertEquals("TelosysTools/downloads",  telosysToolsCfg.getDownloadsFolder() );
		// No getTemplatesFolder() : only absolute path
		assertEquals("TelosysTools/lib",        telosysToolsCfg.getLibrariesFolder() );
		assertEquals("TelosysTools/models",     telosysToolsCfg.getModelsFolder() );

		assertEquals("TelosysTools/databases.yaml", telosysToolsCfg.getDatabasesDbCfgFile() );

		assertEquals(PROJECT_ABSOLUTE_PATH,  telosysToolsCfg.getProjectAbsolutePath() );
		assertEquals(CONFIG_FILE_ABSOLUTE_PATH, telosysToolsCfg.getCfgFileAbsolutePath() );

		assertEquals(PROJECT_ABSOLUTE_PATH+"/TelosysTools",            telosysToolsCfg.getTelosysToolsFolderAbsolutePath() );
		assertEquals(PROJECT_ABSOLUTE_PATH+"/TelosysTools/downloads",  telosysToolsCfg.getDownloadsFolderAbsolutePath() );
		assertEquals(PROJECT_ABSOLUTE_PATH+"/TelosysTools/templates",  telosysToolsCfg.getTemplatesFolderAbsolutePath() );
		assertEquals(PROJECT_ABSOLUTE_PATH+"/TelosysTools/lib",        telosysToolsCfg.getLibrariesFolderAbsolutePath() );
//		assertEquals(PROJECT_ABSOLUTE_PATH+"/TelosysTools",            telosysToolsCfg.getModelsFolderAbsolutePath() );
		assertEquals(PROJECT_ABSOLUTE_PATH+"/TelosysTools/models",     telosysToolsCfg.getModelsFolderAbsolutePath() ); // v 3.4.0

		assertEquals(PROJECT_ABSOLUTE_PATH + "/TelosysTools/templates/mybundle", telosysToolsCfg.getTemplatesFolderAbsolutePath("mybundle") );
//		assertEquals(PROJECT_ABSOLUTE_PATH + "/TelosysTools/foo.model", telosysToolsCfg.getDslModelFileAbsolutePath("foo.model") );
		// v 3.4.0
//		assertEquals(PROJECT_ABSOLUTE_PATH + "/TelosysTools/models/foo.model", telosysToolsCfg.getDslModelFileAbsolutePath("foo.model") );
		assertEquals(PROJECT_ABSOLUTE_PATH + "/TelosysTools/models/foo", telosysToolsCfg.getModelFolderAbsolutePath("foo") );
		
		assertFalse( telosysToolsCfg.hasSpecificDestinationFolder() );
		assertFalse( telosysToolsCfg.hasSpecificTemplatesFolders());
		assertFalse( telosysToolsCfg.hasSpecificModelsFolders());
	}

	@Test
	public void testDestinationFolder() {
		printSeparator();
		
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfgWithoutProperties() ;
		
		//--- Without specific destination
		println("specific destination folder = " + telosysToolsCfg.getSpecificDestinationFolder() );
		assertFalse(telosysToolsCfg.hasSpecificDestinationFolder());
		assertEquals("", telosysToolsCfg.getSpecificDestinationFolder() );

		String destinationFolder = telosysToolsCfg.getDestinationFolderAbsolutePath();
		println("destinationFolder = " + destinationFolder );
		assertEquals(telosysToolsCfg.getProjectAbsolutePath(), destinationFolder);
		
		//--- With specific destination
		telosysToolsCfg.setSpecificDestinationFolder("X:/foo/mydestination") ;
		println("specific destination folder = " + telosysToolsCfg.getSpecificDestinationFolder() );
		assertTrue(telosysToolsCfg.hasSpecificDestinationFolder());
		assertEquals("X:/foo/mydestination", telosysToolsCfg.getSpecificDestinationFolder() );
		
		destinationFolder = telosysToolsCfg.getDestinationFolderAbsolutePath();
		println("destinationFolder = " + destinationFolder );
		assertEquals(telosysToolsCfg.getSpecificDestinationFolder(), destinationFolder);
	}

	@Test
	public void testTemplatesFolder() {
		printSeparator();
		
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfgWithoutProperties() ;
		
		//--- Without specific templates folder
		assertFalse(telosysToolsCfg.hasSpecificTemplatesFolders());
		assertEquals("", telosysToolsCfg.getSpecificTemplatesFolderAbsolutePath() );

		//--- With specific templates folder
		final String FOLDER = "X:/foo/mytemplates";
		telosysToolsCfg.setSpecificTemplatesFolderAbsolutePath(FOLDER) ;
		assertTrue(telosysToolsCfg.hasSpecificTemplatesFolders());
		assertEquals(FOLDER, telosysToolsCfg.getSpecificTemplatesFolderAbsolutePath() );
//		
//		destinationFolder = telosysToolsCfg.getDestinationFolderAbsolutePath();
//		println("destinationFolder = " + destinationFolder );
//		assertEquals(telosysToolsCfg.getSpecificDestinationFolder(), destinationFolder);
	}
	
	@Test
	public void testSpecificVariables() {
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfgWithoutProperties() ;
		assertFalse(telosysToolsCfg.hasSpecificVariables());
		assertNull(telosysToolsCfg.getSpecificVariable("FOO"));
		
		telosysToolsCfg.setSpecificVariable(new Variable("FOO", "Foo-value"));
		assertTrue(telosysToolsCfg.hasSpecificVariables());
		assertEquals("Foo-value", telosysToolsCfg.getSpecificVariable("FOO").getValue());
	}
}
