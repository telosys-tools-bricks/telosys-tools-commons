package org.telosys.tools.tests.commons.cfg;

import java.io.File;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.commons.variables.Variable;
import org.telosys.tools.tests.TestsEnv;

public class TelosysToolsCfgManagerTest extends TestCase {

	public void printSeparator() {
		System.out.println("==============================================================" );
	}
	public void print(File file) {
		System.out.println("File   : " + file.toString());
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
	
	private File getTelosysToolCfgFile() throws TelosysToolsException {
		//return FileUtil.getFileByClassPath("/cfg/telosys-tools.cfg");
		String fileName = TestsEnv.getTmpRootFolder() + "/telosys-tools.cfg" ;
		return new File(fileName) ;
	}
	
	public void testLoad0() throws TelosysToolsException {
		printSeparator();
		File file = getTelosysToolCfgFile();
		String projectFolder = file.getParent();
		print(file);
		
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager(projectFolder);
		TelosysToolsCfg telosysToolsCfg = cfgManager.loadProjectConfig();
		
		print(telosysToolsCfg);
		
		assertEquals("org.demo", telosysToolsCfg.getRootPackage() );
		//assertEquals("org.demo.bean", telosysToolsCfg.getEntityPackage() );
		
		//assertEquals("8080", telosysToolsCfg.getProperties().getProperty("http.proxyPort") );
	}

	public void testLoadUpdateSave() throws TelosysToolsException {
		printSeparator();
		File file = getTelosysToolCfgFile();
		String projectFolder = file.getParent();
		print(file);
		
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager(projectFolder);
		
		System.out.println("Load");
		TelosysToolsCfg telosysToolsCfg = cfgManager.loadProjectConfig();

		System.out.println("Update");
		telosysToolsCfg.setEntityPackage("org.demo.entity");
		assertEquals("org.demo.entity", telosysToolsCfg.getEntityPackage() );
		//assertEquals("8080", telosysToolsCfg.getProperties().getProperty("http.proxyPort") );

		System.out.println("Save");
		cfgManager.saveProjectConfig(telosysToolsCfg);
		
		System.out.println("Re-Load");
		telosysToolsCfg = cfgManager.loadProjectConfig();
		assertEquals("org.demo.entity", telosysToolsCfg.getEntityPackage() );
		//assertEquals("8080", telosysToolsCfg.getProperties().getProperty("http.proxyPort") );
		
		
	}

//	/**
//	 * ONE database configuration
//	 * @throws TelosysToolsException
//	 */
//	public void testLoad1() throws TelosysToolsException {
//		
//		printSeparator();
//		//--- Load 
//		File file = FileUtil.getFileByClassPath("/dbcfg/databases-test1.dbcfg");
//		print(file);
//		
//		DbConfigManager dbDonfigManager = new DbConfigManager(file);
//		DatabasesConfigurations databasesConfigurations = dbDonfigManager.load();
//		
//		print(databasesConfigurations);
//		
//		assertEquals(0, databasesConfigurations.getDatabaseDefaultId() ) ;
//		assertEquals(4, databasesConfigurations.getDatabaseMaxId() ) ;
//
//		assertEquals(1, databasesConfigurations.getNumberOfDatabases() ) ;
//		
//		DatabaseConfiguration databaseConfiguration = databasesConfigurations.getDatabaseConfiguration(0);
//		assertNotNull(databaseConfiguration);
//		assertEquals(0, databaseConfiguration.getDatabaseId());
//		
//		//--- Update 
//		System.out.println("UPDATED CONFIG : ");
//		databaseConfiguration.setDatabaseName("New name");
//		databaseConfiguration.setDriverClass("my.new.driver");
//		databaseConfiguration.setUser(databaseConfiguration.getUser()+"-new") ;
//		databaseConfiguration.setPassword(databaseConfiguration.getPassword()+"-new") ;
//		print(databaseConfiguration) ;
//		
//		//--- Save 
//		System.out.println("SAVING...");
//		File out = FileUtil.getFileByClassPath("/dbcfg/databases-test1-out.dbcfg");
//		print(out);
//		
//		dbDonfigManager = new DbConfigManager(out);
//		dbDonfigManager.save(databasesConfigurations);
//		System.out.println("SAVED.");
//	}
//	
//	/**
//	 * TWO databases configurations
//	 * @throws TelosysToolsException
//	 */
//	public void testLoad2() throws TelosysToolsException {
//		
//		printSeparator();
//		File file = FileUtil.getFileByClassPath("/dbcfg/databases-test2.dbcfg");
//		print(file);
//		
//		DbConfigManager dbDonfigManager = new DbConfigManager(file);
//		DatabasesConfigurations databasesConfigurations = dbDonfigManager.load();
//		
//		print(databasesConfigurations);
//		
//		assertEquals(0, databasesConfigurations.getDatabaseDefaultId() ) ;
//		assertEquals(0, databasesConfigurations.getDatabaseMaxId() ) ;
//
//		assertEquals(2, databasesConfigurations.getNumberOfDatabases() ) ;
//		
//		DatabaseConfiguration databaseConfiguration = databasesConfigurations.getDatabaseConfiguration(1);
//		assertNotNull(databaseConfiguration);
//		assertEquals(1, databaseConfiguration.getDatabaseId());
//		
//		databaseConfiguration = databasesConfigurations.getDatabaseConfiguration(2);
//		assertNotNull(databaseConfiguration);
//		assertEquals(2, databaseConfiguration.getDatabaseId());
//		
//		//--- Save 
//		System.out.println("SAVING...");
//		File out = FileUtil.getFileByClassPath("/dbcfg/databases-test2-out.dbcfg");
//		print(out);
//		
//		dbDonfigManager = new DbConfigManager(out);
//		dbDonfigManager.save(databasesConfigurations);
//		System.out.println("SAVED.");
//	}
	
}
