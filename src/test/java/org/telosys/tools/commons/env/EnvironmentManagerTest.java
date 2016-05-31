package org.telosys.tools.commons.env;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.commons.variables.Variable;

public class EnvironmentManagerTest extends TestCase {

	public void printSeparator() {
		System.out.println("==============================================================" );
	}
	public void print(File file) {
		System.out.println("File   : " + file.toString());
		System.out.println("Parent : " + file.getParent());
	}
	private EnvironmentManager getEnvironmentManager() {
		return new EnvironmentManager( TestsEnv.getTmpExistingFolderFullPath("") );
	}
	private EnvironmentManager getEnvironmentManager(String subFolder) {
		return new EnvironmentManager( TestsEnv.getTmpExistingFolderFullPath(subFolder) );
	}
	
	public void testGetEnvironmentDirectory() throws TelosysToolsException {
		printSeparator();
		System.out.println("getEnvironmentDirectory()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String dir = em.getEnvironmentFolderFullPath();
		System.out.println(dir);
		assertEquals(TestsEnv.getTmpRootFolderFullPath() , dir);
	}

	public void testGetTelosysToolsConfigFile() throws TelosysToolsException {
		printSeparator();
		System.out.println("getTelosysToolsConfigFile()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String fullPath = em.getTelosysToolsConfigFileFullPath();
		System.out.println(fullPath);
//		assertEquals(TestsEnv.getTmpRootFolderFullPath()+"/telosys-tools.cfg" , cfgFile);
		assertEquals(TestsEnv.getTmpRootFolderFullPath()+"/TelosysTools/telosys-tools.cfg" , fullPath);
	}
	
	public void testGetTelosysToolsFolderFullPath() throws TelosysToolsException {
		printSeparator();
		System.out.println("getTelosysToolsConfigFile()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String folder = em.getTelosysToolsFolderFullPath();
		System.out.println(folder);
		assertEquals(TestsEnv.getTmpRootFolderFullPath()+"/TelosysTools" , folder);
	}
	
	public void testGetDatabasesDbCfgFullPath() throws TelosysToolsException {
		printSeparator();
		System.out.println("getDatabasesDbCfgFullPath()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String fullPath = em.getDatabasesDbCfgFullPath() ;
		System.out.println(fullPath);
		assertEquals(TestsEnv.getTmpRootFolderFullPath()+"/TelosysTools/databases.dbcfg" , fullPath);
	}
	
	public void testCreateFolder() throws TelosysToolsException {
		printSeparator();
		System.out.println("createFolder('foo1')...");
		EnvironmentManager em = getEnvironmentManager() ;
		StringBuffer sb = new StringBuffer();
		em.createFolder("foo1", sb);
		System.out.println(sb.toString());
	}

	public void testInitTelosysToolsConfigFile() throws TelosysToolsException {
		printSeparator();
		System.out.println("testInitTelosysToolsConfigFile()...");

		EnvironmentManager em = getEnvironmentManager();

		String filePath = em.getTelosysToolsConfigFileFullPath();
		System.out.println("File : " + filePath);
		File file = new File(filePath);
		if ( file.exists() ) {
			System.out.println("Delete " + filePath);
			file.delete();
		}
		
		StringBuffer sb = new StringBuffer();
		em.initTelosysToolsConfigFile(sb, null); // no variables to initialize
		String result = sb.toString();
		System.out.println(result);
		assertFalse( result.contains("not created"));
		assertTrue( file.exists() );
	}

	public void testInitDatabasesConfigFile() throws TelosysToolsException {
		printSeparator();
		System.out.println("testInitDatabasesConfigFile()...");

		EnvironmentManager em = getEnvironmentManager();
		
		
		String filePath = em.getDatabasesDbCfgFullPath() ;
		System.out.println("File : " + filePath);
		File file = new File(filePath);
		if ( file.exists() ) {
			System.out.println("Delete " + filePath);
			file.delete();
		}

		StringBuffer sb = new StringBuffer();
		em.initDatabasesConfigFile(sb);
		System.out.println(sb.toString());
		
		assertTrue( file.exists() );
		
//		StringBuffer sb = new StringBuffer();
//		em.initDatabasesConfigFile("aaaa", sb);
//		String result = sb.toString();
//		System.out.println(result);
//		assertTrue( result.contains("cannot create"));

//		TestsEnv.getTmpExistingFolder("tmp-folder");
//		sb = new StringBuffer();
//		em.initDatabasesConfigFile("tmp-folder", sb);
//		result = sb.toString();
//		System.out.println(result);
//		assertTrue( result.contains("created"));
	}

	public void testInitEnvironment() throws TelosysToolsException {
		printSeparator();
		System.out.println("testInitEnvironment()...");

		EnvironmentManager em = new EnvironmentManager( TestsEnv.getTmpExistingFolderFullPath("myproject1") );
		cleanTelosysToolsEnvironment(em);
		
		StringBuffer sb = new StringBuffer();
		em.initEnvironment(sb);
		System.out.println(sb.toString());
		
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/lib").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/downloads").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/templates").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/databases.dbcfg").exists() );
//		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/telosys-tools.cfg").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/telosys-tools.cfg").exists() );
	}
	
	public void testInitEnvironmentWithVariables() throws TelosysToolsException {
		printSeparator();
		System.out.println("testInitEnvironmentWithVariables()...");

		String projectFullPath = TestsEnv.getTmpExistingFolderFullPath("myproject2") ;
		EnvironmentManager em = new EnvironmentManager( projectFullPath);
		cleanTelosysToolsEnvironment(em);
		
		List<Variable> variables = new LinkedList<Variable>();
		variables.add(new Variable("PROJECT_NAME","myproject2") );
		variables.add(new Variable("MY_VAR1","value1") );
		
		StringBuffer sb = new StringBuffer();
		em.initEnvironment(sb, variables);
		System.out.println(sb.toString());
		
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/lib").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/downloads").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/templates").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/databases.dbcfg").exists() );
//		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/telosys-tools.cfg").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/telosys-tools.cfg").exists() );
		
		TelosysToolsCfgManager telosysToolsCfgManager = new TelosysToolsCfgManager(projectFullPath) ;
		TelosysToolsCfg telosysToolsCfg = telosysToolsCfgManager.loadTelosysToolsCfg();
		Variable var ;

		var = telosysToolsCfg.getSpecificVariable("MY_VAR1");
		assertNotNull(var);
		assertEquals("value1", var.getValue());
		
		var = telosysToolsCfg.getSpecificVariable("PROJECT_NAME");
		assertNotNull(var);
		assertEquals("myproject2", var.getValue());
	}
	
	public void testIsInitialized() throws TelosysToolsException {
		printSeparator();
		System.out.println("testIsInitialized()...");
		EnvironmentManager em = getEnvironmentManager("/myproject");
		System.out.println("EnvironmentManager ready : " + em.getEnvironmentFolderFullPath() );
		//--- Initial state
		cleanTelosysToolsEnvironment(em);
		
		//--- Is initialized : NO
		//boolean envInitialized = em.isEnvironmentInitialized();
		assertFalse(em.isEnvironmentInitialized());

		//--- Initialize...
		StringBuffer sb = new StringBuffer();
		em.initEnvironment(sb);
		System.out.println(sb.toString());

		//--- Is initialized : YES
		assertTrue(em.isEnvironmentInitialized());
		
		cleanTelosysToolsDirectory(em);
		//--- Is initialized : NO
		assertFalse(em.isEnvironmentInitialized());

		//--- Initialize...
		sb = new StringBuffer();
		em.initEnvironment(sb);
		System.out.println(sb.toString());

		//--- Is initialized : YES
		assertTrue(em.isEnvironmentInitialized());

		cleanTelosysToolsCfg(em);
		//--- Is initialized : NO
		assertFalse(em.isEnvironmentInitialized());
	}
	
	public void cleanTelosysToolsEnvironment(EnvironmentManager em) throws TelosysToolsException {
		System.out.println("cleaning TelosysTools environment ..." );
		cleanTelosysToolsDirectory(em);
		cleanTelosysToolsCfg(em);
	}
	public void cleanTelosysToolsDirectory(EnvironmentManager em) throws TelosysToolsException {
		File telosysToolsDirectory = new File(em.getTelosysToolsFolderFullPath() );
		if ( telosysToolsDirectory.exists() ) {
			DirUtil.deleteDirectory( telosysToolsDirectory ) ;
			System.out.println("'TelosysTools' directory deleted." );
		}
	}
	public void cleanTelosysToolsCfg(EnvironmentManager em) throws TelosysToolsException {
		File telosysToolsCfg = new File(em.getTelosysToolsConfigFileFullPath() );
		if ( telosysToolsCfg.exists() ) {
			telosysToolsCfg.delete() ;
			System.out.println("'telosys-tools.cfg' file deleted." );
		}
	}
}
