package org.telosys.tools.commons.cfg;

import java.io.File;
import java.util.Properties;
import java.util.Set;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

import org.telosys.tools.commons.DirUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.commons.variables.Variable;

public class TelosysToolsCfgManagerTest extends TestCase {

	public void printSeparator() {
		System.out.println("==============================================================" );
	}
	public void print(File file) {
		System.out.println("File   : " + file.toString());
		System.out.println("exists ? " + file.exists() );
		System.out.println("Parent : " + file.getParent());
	}
	
	public void print(TelosysToolsCfg telosysToolsCfg) {
		System.out.println( "getProjectAbsolutePath = " + telosysToolsCfg.getProjectAbsolutePath() );
		System.out.println( "getCfgFileAbsolutePath = " + telosysToolsCfg.getCfgFileAbsolutePath() );
		System.out.println( "getDatabasesDbCfgFile             = " + telosysToolsCfg.getDatabasesDbCfgFile());
		System.out.println( "getDatabasesDbCfgFileAbsolutePath = " + telosysToolsCfg.getDatabasesDbCfgFileAbsolutePath());
		
		print(telosysToolsCfg.getAllVariables());
		
		print(telosysToolsCfg.getProperties());
	}
	
	public void print(Variable[] variables) {
		System.out.println("VARIABLES : ");
		for ( Variable v : variables ) {
			System.out.println(" . " + v.getName() + " = " + v.getValue() + " ( " + v.getSymbolicName() + " )");
		}
	}
	
	public void print(Properties properties) {
		System.out.println("PROPERTIES : ");
		Set<Object> keys = properties.keySet();
		for ( Object k : keys ) {
			System.out.println(" . " + k + " = " + properties.get(k) );
		}
	}
	
	public String toString(String[] array) {
		StringBuffer sb = new StringBuffer();
		for ( String s : array ) {
			sb.append("'");
			sb.append(s);
			sb.append("' ");
		}
		return sb.toString();
	}
	
	/**
	 * Returns a File pointing on a non-existent 'telosys-tools.cfg' file
	 * @return
	 * @throws TelosysToolsException
	 */
	private File getTelosysToolCfgFile() throws TelosysToolsException {
		String fileName = TestsEnv.getTmpRootFolderFullPath() + "/TelosysTools/telosys-tools.cfg" ;
		File file = new File(fileName) ;
		if ( file.exists() ) {
			file.delete() ;
		}
		return file ;
	}
	
	private boolean createParentFolderIfNotExist(File file) throws TelosysToolsException {
		File parent = file.getParentFile() ;
		if ( ! DirUtil.isValidDirectory(parent) ) {
			DirUtil.createDirectory(parent);
			return true;
		}
		return false;
	}
	
	public void testLoad0() throws TelosysToolsException {
		printSeparator();
		File file = getTelosysToolCfgFile();
		String projectFolder = file.getParent();
		print(file);
		
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager(projectFolder);
		TelosysToolsCfg telosysToolsCfg = cfgManager.loadTelosysToolsCfg();
		
		print(telosysToolsCfg);
		
		assertEquals("org.demo", telosysToolsCfg.getRootPackage() );
		//assertEquals("org.demo.bean", telosysToolsCfg.getEntityPackage() );
		
		//assertEquals("8080", telosysToolsCfg.getProperties().getProperty("http.proxyPort") );
	}

// removed in v 4.2.0 (cfgManager.saveTelosysToolsCfg removed)
//	public void testLoadUpdateSave() throws TelosysToolsException {
//		printSeparator();
//		System.out.println("Test : Load, Update, Save and ReLoad" );
//		File file = getTelosysToolCfgFile();
//		System.out.println("config file    : " + file.getAbsolutePath() );
//		String projectFolder = file.getParentFile().getParent();
//		System.out.println("project folder : " + projectFolder );
//		print(file);
//		if ( createParentFolderIfNotExist(file) ) {
//			System.out.println("Parent directory created" );
//		}
//		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager(projectFolder);
//
//		System.out.println("config manager file path : " + cfgManager.getCfgFileAbsolutePath() );
//		 
//		System.out.println("Load");
//		TelosysToolsCfg telosysToolsCfg = cfgManager.loadTelosysToolsCfg();
//		assertEquals("", telosysToolsCfg.getSpecificDestinationFolder() );
//
//		System.out.println("Update");
//		telosysToolsCfg.setEntityPackage("org.demo.entity");
//		assertEquals("org.demo.entity", telosysToolsCfg.getEntityPackage() );
//		//assertEquals("8080", telosysToolsCfg.getProperties().getProperty("http.proxyPort") );
//		telosysToolsCfg.setSpecificDestinationFolder("x:/foo/bar/dest");
//
//		System.out.println("Save");
//		cfgManager.saveTelosysToolsCfg(telosysToolsCfg);
//		
//		System.out.println("Re-Load");
//		telosysToolsCfg = cfgManager.loadTelosysToolsCfg();
//		assertEquals("org.demo.entity", telosysToolsCfg.getEntityPackage() );
//		//assertEquals("8080", telosysToolsCfg.getProperties().getProperty("http.proxyPort") );
//		assertEquals("x:/foo/bar/dest", telosysToolsCfg.getSpecificDestinationFolder() );
//		
//	}

}
