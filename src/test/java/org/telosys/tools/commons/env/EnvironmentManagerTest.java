package org.telosys.tools.commons.env;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.commons.variables.Variable;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

public class EnvironmentManagerTest extends TestCase {

	private static final String MYPROJECT2 = "myproject2";
	
	public void println(String s) {
		System.out.println(s);
	}
	public void printSeparator() {
		println("==============================================================" );
	}
	public void print(File file) {
		println("File   : " + file.toString());
		println("Parent : " + file.getParent());
	}
	private EnvironmentManager getEnvironmentManager() {
		return new EnvironmentManager( TestsEnv.getTmpExistingFolderFullPath("") );
	}
	private EnvironmentManager getEnvironmentManager(String subFolder) {
		return new EnvironmentManager( TestsEnv.getTmpExistingFolderFullPath(subFolder) );
	}
	
	public void testGetEnvironmentDirectory() {
		printSeparator();
		println("getEnvironmentDirectory()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String dir = em.getEnvironmentFolderFullPath();
		println(dir);
		assertEquals(TestsEnv.getTmpRootFolderFullPath() , dir);
	}

	public void testGetTelosysToolsConfigFile() {
		printSeparator();
		println("getTelosysToolsConfigFile()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String fullPath = em.getTelosysToolsConfigFileFullPath();
		println(fullPath);
		assertEquals(TestsEnv.getTmpRootFolderFullPath()+"/TelosysTools/telosys-tools.cfg" , fullPath);
	}
	
	public void testGetTelosysToolsFolderFullPath() {
		printSeparator();
		println("getTelosysToolsConfigFile()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String folder = em.getTelosysToolsFolderFullPath();
		println(folder);
		assertEquals(TestsEnv.getTmpRootFolderFullPath()+"/TelosysTools" , folder);
	}
	
	public void testGetDatabasesDbCfgFullPath() {
		printSeparator();
		println("getDatabasesDbCfgFullPath()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String fullPath = em.getDatabasesDbCfgFullPath() ;
		println(fullPath);
		assertEquals(TestsEnv.getTmpRootFolderFullPath()+"/TelosysTools/databases.yaml" , fullPath);
	}
	
	public void testCreateFolder() {
		printSeparator();
		println("createFolder('foo1')...");
		EnvironmentManager em = getEnvironmentManager() ;
		StringBuilder sb = new StringBuilder();
		em.createFolder("foo1", sb);
		println(sb.toString());
	}

	public void testInitTelosysToolsConfigFile() {
		printSeparator();
		println("testInitTelosysToolsConfigFile()...");

		EnvironmentManager em = getEnvironmentManager();

		String filePath = em.getTelosysToolsConfigFileFullPath();
		println("File : " + filePath);
		File file = new File(filePath);
		if ( file.exists() ) {
			println("Delete " + filePath);
			FileUtil.delete(file);
		}
		
		StringBuilder sb = new StringBuilder();
//		em.initTelosysToolsConfigFile(sb, null); // no variables to initialize
		em.initTelosysToolsConfigFile(sb); // no variables to initialize
		String result = sb.toString();
		println(result);
		assertFalse( result.contains("not created"));
		assertTrue( file.exists() );
	}

	public void testInitDatabasesConfigFile() {
		printSeparator();
		println("testInitDatabasesConfigFile()...");

		EnvironmentManager em = getEnvironmentManager();
		
		String filePath = em.getDatabasesDbCfgFullPath() ;
		println("File : " + filePath);
		File file = new File(filePath);
		if ( file.exists() ) {
			println("Delete " + filePath);
			FileUtil.delete(file);
		}

		StringBuilder sb = new StringBuilder();
		em.initDatabasesConfigFile(sb);
		println(sb.toString());
		
		assertTrue( file.exists() );
	}

	public void testInitEnvironment() {
		printSeparator();
		println("testInitEnvironment()...");

		EnvironmentManager em = new EnvironmentManager( TestsEnv.getTmpExistingFolderFullPath("myproject1") );
		cleanTelosysToolsEnvironment(em);
		
		StringBuilder sb = new StringBuilder();
		em.initEnvironment(sb);
		println(sb.toString());
		
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/lib").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/downloads").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/templates").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/models").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/databases.yaml").exists() );
		assertTrue( TestsEnv.getTmpFileOrFolder("myproject1/TelosysTools/telosys-tools.cfg").exists() );
	}
	
// removed v 4.2.0
//	public void testInitEnvironmentWithVariables() {
//		printSeparator();
//		println("testInitEnvironmentWithVariables()...");
//
//		String projectFullPath = TestsEnv.getTmpExistingFolderFullPath(MYPROJECT2) ;
//		EnvironmentManager em = new EnvironmentManager( projectFullPath);
//		cleanTelosysToolsEnvironment(em);
//		
//		List<Variable> variables = new LinkedList<>();
//		variables.add(new Variable("PROJECT_NAME",MYPROJECT2) );
//		variables.add(new Variable("MY_VAR1","value1") );
//		
//		StringBuilder sb = new StringBuilder();
//		em.initEnvironment(sb, variables);
//		println(sb.toString());
//		
//		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools").exists() );
//		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/lib").exists() );
//		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/downloads").exists() );
//		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/templates").exists() );
//		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/models").exists() );
//		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/databases.yaml").exists() );
//		assertTrue( TestsEnv.getTmpFileOrFolder("myproject2/TelosysTools/telosys-tools.cfg").exists() );
//		
//		TelosysToolsCfgManager telosysToolsCfgManager = new TelosysToolsCfgManager(projectFullPath) ;
//		TelosysToolsCfg telosysToolsCfg = telosysToolsCfgManager.loadTelosysToolsCfg();
//		Variable var ;
//
//		var = telosysToolsCfg.getSpecificVariable("MY_VAR1");
//		assertNotNull(var);
//		assertEquals("value1", var.getValue());
//		
//		var = telosysToolsCfg.getSpecificVariable("PROJECT_NAME");
//		assertNotNull(var);
//		assertEquals(MYPROJECT2, var.getValue());
//	}
	
	public void testIsInitialized() {
		printSeparator();
		println("testIsInitialized()...");
		EnvironmentManager em = getEnvironmentManager("/myproject");
		println("EnvironmentManager ready : " + em.getEnvironmentFolderFullPath() );
		//--- Initial state
		cleanTelosysToolsEnvironment(em);
		
		//--- Is initialized : NO
		assertFalse(em.isEnvironmentInitialized());

		//--- Initialize...
		StringBuilder sb = new StringBuilder();
		em.initEnvironment(sb);
		println(sb.toString());

		//--- Is initialized : YES
		assertTrue(em.isEnvironmentInitialized());
		
		cleanTelosysToolsDirectory(em);
		//--- Is initialized : NO
		assertFalse(em.isEnvironmentInitialized());

		//--- Initialize...
		sb = new StringBuilder();
		em.initEnvironment(sb);
		println(sb.toString());

		//--- Is initialized : YES
		assertTrue(em.isEnvironmentInitialized());

		cleanTelosysToolsCfg(em);
		//--- Is initialized : NO
		assertFalse(em.isEnvironmentInitialized());
	}
	
	public void cleanTelosysToolsEnvironment(EnvironmentManager em) {
		println("cleaning TelosysTools environment ..." );
		cleanTelosysToolsDirectory(em);
		cleanTelosysToolsCfg(em);
	}
	public void cleanTelosysToolsDirectory(EnvironmentManager em) {
		File telosysToolsDirectory = new File(em.getTelosysToolsFolderFullPath() );
		if ( telosysToolsDirectory.exists() ) {
			try {
				DirUtil.deleteDirectory( telosysToolsDirectory ) ;
			} catch (TelosysToolsException e) {
				throw new TelosysRuntimeException("Cannot delete directory " + telosysToolsDirectory.getName(), e);
			}
			println("'TelosysTools' directory deleted." );
		}
	}
	public void cleanTelosysToolsCfg(EnvironmentManager em) {
		File telosysToolsCfg = new File(em.getTelosysToolsConfigFileFullPath() );
		if ( telosysToolsCfg.exists() ) {
			FileUtil.delete(telosysToolsCfg);
			println("'telosys-tools.cfg' file deleted." );
		}
	}
}
