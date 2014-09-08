package org.telosys.tools.tests.commons.env;

import java.io.File;

import junit.framework.TestCase;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.env.EnvironmentManager;
import org.telosys.tools.tests.TestsEnv;

public class EnvironmentManagerTest extends TestCase {

	public void printSeparator() {
		System.out.println("==============================================================" );
	}
	public void print(File file) {
		System.out.println("File   : " + file.toString());
		System.out.println("Parent : " + file.getParent());
	}
	private EnvironmentManager getEnvironmentManager() {
		return new EnvironmentManager( TestsEnv.getTmpRootFolder() );
	}
	
	public void testGetEnvironmentDirectory() throws TelosysToolsException {
		printSeparator();
		System.out.println("getEnvironmentDirectory()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String dir = em.getEnvironmentFolderFullPath();
		System.out.println(dir);
		assertEquals(TestsEnv.getTmpRootFolder() , dir);
	}

	public void testGetTelosysToolsConfigFile() throws TelosysToolsException {
		printSeparator();
		System.out.println("getTelosysToolsConfigFile()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String cfgFile = em.getTelosysToolsConfigFileFullPath();
		System.out.println(cfgFile);
		assertEquals(TestsEnv.getTmpRootFolder()+"/telosys-tools.cfg" , cfgFile);
	}
	
	public void testGetTelosysToolsFolderFullPath() throws TelosysToolsException {
		printSeparator();
		System.out.println("getTelosysToolsConfigFile()...");
		EnvironmentManager em = getEnvironmentManager() ;
		String folder = em.getTelosysToolsFolderFullPath();
		System.out.println(folder);
		assertEquals(TestsEnv.getTmpRootFolder()+"/TelosysTools" , folder);
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
		em.initTelosysToolsConfigFile(sb);
		String result = sb.toString();
		System.out.println(result);
		assertFalse( result.contains("not created"));
		assertTrue( file.exists() );
	}

	public void testInitDatabasesConfigFile() throws TelosysToolsException {
		printSeparator();
		System.out.println("testInitDatabasesConfigFile()...");

		EnvironmentManager em = getEnvironmentManager();

//		String filePath = em.getTelosysToolsConfigFile();
//		System.out.println("File : " + filePath);
//		File file = new File(filePath);
//		if ( file.exists() ) {
//			System.out.println("Delete " + filePath);
//			file.delete();
//		}
		
		StringBuffer sb = new StringBuffer();
		em.initDatabasesConfigFile("aaaa", sb);
		String result = sb.toString();
		System.out.println(result);
		assertTrue( result.contains("cannot create"));

		TestsEnv.getTmpExistingFolder("tmp-folder");
		sb = new StringBuffer();
		em.initDatabasesConfigFile("tmp-folder", sb);
		result = sb.toString();
		System.out.println(result);
		assertTrue( result.contains("created"));
	}

}
