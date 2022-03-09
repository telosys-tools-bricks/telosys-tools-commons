package org.telosys.tools.commons.cfg;

import java.util.Properties;
import java.util.Set;

import org.junit.Test;
import org.telosys.tools.commons.variables.Variable;

import static org.junit.Assert.assertEquals;

public class TelosysToolsCfgTest {

	private void printSeparator() {
		System.out.println("==============================================================" );
	}
	
	private static final String PROJECT_ABSOLUTE_PATH     = "X:/foo/bar/myproject" ;
	private static final String CONFIG_FILE_ABSOLUTE_PATH = "X:/foo/bar/myproject/telosys-tools.cfg" ;
	
	private TelosysToolsCfg getTelosysToolsCfg() {
		TelosysToolsCfg telosysToolsCfg = new TelosysToolsCfg(PROJECT_ABSOLUTE_PATH, CONFIG_FILE_ABSOLUTE_PATH, null);
		print(telosysToolsCfg);
		return telosysToolsCfg ;
	}
	
	private void print(TelosysToolsCfg telosysToolsCfg) {
		System.out.println( "getProjectAbsolutePath = " + telosysToolsCfg.getProjectAbsolutePath() );
		System.out.println( "getCfgFileAbsolutePath = " + telosysToolsCfg.getCfgFileAbsolutePath() );
		System.out.println( "getDatabasesDbCfgFile             = " + telosysToolsCfg.getDatabasesDbCfgFile());
		System.out.println( "getDatabasesDbCfgFileAbsolutePath = " + telosysToolsCfg.getDatabasesDbCfgFileAbsolutePath());
		
		print(telosysToolsCfg.getAllVariables());
		
		print(telosysToolsCfg.getProperties());
	}
	
	private void print(Variable[] variables) {
		System.out.println("VARIABLES : ");
		for ( Variable v : variables ) {
			System.out.println(" . " + v.getName() + " = " + v.getValue() + " ( " + v.getSymbolicName() + " )");
		}
	}
	
	private void print(Properties properties) {
		System.out.println("PROPERTIES : ");
		Set<Object> keys = properties.keySet();
		for ( Object k : keys ) {
			System.out.println(" . " + k + " = " + properties.get(k) );
		}
	}
	
	@Test
	public void testDefaultPackages() {
		printSeparator();
		
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfg() ;
		
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
		
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfg() ;
		
		assertEquals("TelosysTools",            telosysToolsCfg.getTelosysToolsFolder() );
		assertEquals("TelosysTools/downloads",  telosysToolsCfg.getDownloadsFolder() );
//		assertEquals("TelosysTools/templates",  telosysToolsCfg.getTemplatesFolder() ); // only absolute path
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
	}

	@Test
	public void testDestinationFolder() {
		printSeparator();
		
		TelosysToolsCfg telosysToolsCfg = getTelosysToolsCfg() ;
		
		//--- Without specific destination
		System.out.println("specific destination folder = " + telosysToolsCfg.getSpecificDestinationFolder() );
		assertEquals("", telosysToolsCfg.getSpecificDestinationFolder() );

		String destinationFolder = telosysToolsCfg.getDestinationFolderAbsolutePath();
		System.out.println("destinationFolder = " + destinationFolder );
		assertEquals(telosysToolsCfg.getProjectAbsolutePath(), destinationFolder);
		
		//--- With specific destination
		telosysToolsCfg.setSpecificDestinationFolder("X:/foo/mydestination") ;
		System.out.println("specific destination folder = " + telosysToolsCfg.getSpecificDestinationFolder() );
		assertEquals("X:/foo/mydestination", telosysToolsCfg.getSpecificDestinationFolder() );
		
		destinationFolder = telosysToolsCfg.getDestinationFolderAbsolutePath();
		System.out.println("destinationFolder = " + destinationFolder );
		assertEquals(telosysToolsCfg.getSpecificDestinationFolder(), destinationFolder);
		
	}

}
