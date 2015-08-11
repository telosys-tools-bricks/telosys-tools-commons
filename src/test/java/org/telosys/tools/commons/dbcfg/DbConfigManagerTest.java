package org.telosys.tools.commons.dbcfg;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.commons.dbcfg.DbConfigManager;

public class DbConfigManagerTest extends TestCase {

	public void printSeparator() {
		System.out.println("==============================================================" );
	}
	public void print(File file) {
		System.out.println("Databases configuration file : " + file.toString());
	}
	
	public void print(DatabasesConfigurations databasesConfigurations) {
		System.out.println( "Number of databases = " + databasesConfigurations.getNumberOfDatabases() );
		System.out.println( "Default id = " + databasesConfigurations.getDatabaseDefaultId() );
		System.out.println( "Max id = " + databasesConfigurations.getDatabaseMaxId() );

		List<DatabaseConfiguration> list = databasesConfigurations.getDatabaseConfigurationsList();
		for ( DatabaseConfiguration databaseConfiguration : list ) {
			System.out.println("-----");
			print(databaseConfiguration);
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
	
	public void print(DatabaseConfiguration databaseConfiguration) {
		System.out.println(". id        = " + databaseConfiguration.getDatabaseId() );
		System.out.println(". name      = " + databaseConfiguration.getDatabaseName() );
		System.out.println(". url       = " + databaseConfiguration.getJdbcUrl() );
		System.out.println(". driver    = " + databaseConfiguration.getDriverClass() );
		System.out.println(". user      = " + databaseConfiguration.getUser() );
		System.out.println(". password  = " + databaseConfiguration.getPassword() );

		System.out.println(". isolation = " + databaseConfiguration.getIsolationLevel() );
		System.out.println(". pool size = " + databaseConfiguration.getPoolSize() );

		System.out.println(". metadata : "  );
		System.out.println("  . catalog = " + databaseConfiguration.getMetadataCatalog() );
		System.out.println("  . schema  = " + databaseConfiguration.getMetadataSchema() );
		System.out.println("  . pattern = " + databaseConfiguration.getMetadataTableNamePattern() );
		System.out.println("  . types   = " + databaseConfiguration.getMetadataTableTypes() );
		System.out.println("  . types[] = " + toString(databaseConfiguration.getMetadataTableTypesArray()) );
	}

	/**
	 * ZERO database configuration
	 * @throws TelosysToolsException
	 */
	public void testLoad0() throws TelosysToolsException {
		printSeparator();
		//File file = FileUtil.getFileByClassPath("/dbcfg/databases-test0.dbcfg");
		File file = new File("src/test/resources/dbcfg/databases-test0.dbcfg");
		print(file);
		
		DbConfigManager dbDonfigManager = new DbConfigManager(file);
		DatabasesConfigurations databasesConfigurations = dbDonfigManager.load();
		
		print(databasesConfigurations);
		
		assertEquals(0, databasesConfigurations.getDatabaseDefaultId() ) ;
		assertEquals(4, databasesConfigurations.getDatabaseMaxId() ) ;

		assertEquals(0, databasesConfigurations.getNumberOfDatabases() ) ;
		
		DatabaseConfiguration databaseConfiguration = databasesConfigurations.getDatabaseConfiguration(0);
		assertNull(databaseConfiguration);
		
	}

	/**
	 * ONE database configuration
	 * @throws TelosysToolsException
	 */
	public void testLoad1() throws TelosysToolsException {
		
		printSeparator();
		//--- Load 
		File file = FileUtil.getFileByClassPath("/dbcfg/databases-test1.dbcfg");
		print(file);
		
		DbConfigManager dbDonfigManager = new DbConfigManager(file);
		DatabasesConfigurations databasesConfigurations = dbDonfigManager.load();
		
		print(databasesConfigurations);
		
		assertEquals(0, databasesConfigurations.getDatabaseDefaultId() ) ;
		assertEquals(4, databasesConfigurations.getDatabaseMaxId() ) ;

		assertEquals(1, databasesConfigurations.getNumberOfDatabases() ) ;
		
		DatabaseConfiguration databaseConfiguration = databasesConfigurations.getDatabaseConfiguration(0);
		assertNotNull(databaseConfiguration);
		assertEquals(0, databaseConfiguration.getDatabaseId());
		
		DatabaseConfiguration defaultDatabaseConfiguration = databasesConfigurations.getDatabaseConfiguration();
		assertNotNull(defaultDatabaseConfiguration);
		assertEquals(0, defaultDatabaseConfiguration.getDatabaseId());
		
		//--- Update 
		System.out.println("UPDATED CONFIG : ");
		databaseConfiguration.setDatabaseName("New name");
		databaseConfiguration.setDriverClass("my.new.driver");
		databaseConfiguration.setUser(databaseConfiguration.getUser()+"-new") ;
		databaseConfiguration.setPassword(databaseConfiguration.getPassword()+"-new") ;
		print(databaseConfiguration) ;
		
		//--- Save 
		System.out.println("SAVING...");
		File out = FileUtil.getFileByClassPath("/dbcfg/databases-test1-out.dbcfg");
		print(out);
		
		dbDonfigManager = new DbConfigManager(out);
		dbDonfigManager.save(databasesConfigurations);
		System.out.println("SAVED.");
	}
	
	/**
	 * TWO databases configurations
	 * @throws TelosysToolsException
	 */
	public void testLoad2() throws TelosysToolsException {
		
		printSeparator();
		File file = FileUtil.getFileByClassPath("/dbcfg/databases-test2.dbcfg");
		print(file);
		
		DbConfigManager dbDonfigManager = new DbConfigManager(file);
		DatabasesConfigurations databasesConfigurations = dbDonfigManager.load();
		
		print(databasesConfigurations);
		
		assertEquals(0, databasesConfigurations.getDatabaseDefaultId() ) ;
		assertEquals(0, databasesConfigurations.getDatabaseMaxId() ) ;

		assertEquals(2, databasesConfigurations.getNumberOfDatabases() ) ;
		
		DatabaseConfiguration databaseConfiguration = databasesConfigurations.getDatabaseConfiguration(1);
		assertNotNull(databaseConfiguration);
		assertEquals(1, databaseConfiguration.getDatabaseId());
		
		databaseConfiguration = databasesConfigurations.getDatabaseConfiguration(2);
		assertNotNull(databaseConfiguration);
		assertEquals(2, databaseConfiguration.getDatabaseId());
		
		//--- Save 
		System.out.println("SAVING...");
		File out = FileUtil.getFileByClassPath("/dbcfg/databases-test2-out.dbcfg");
		print(out);
		
		dbDonfigManager = new DbConfigManager(out);
		dbDonfigManager.save(databasesConfigurations);
		System.out.println("SAVED.");
	}
	
}
