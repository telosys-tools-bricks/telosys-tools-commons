package org.telosys.tools.commons.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.commons.dbcfg.DbConfigManager;

import junit.framework.TestCase;

public class ConnectionManagerTest extends TestCase {

	public static String generateH2JDBCURL() {
		return "jdbc:h2:~/test-" + System.getProperty("mavenSurefireForkNumber");
	}

	public void getH2Connection(ConnectionManager cm) throws TelosysToolsException, SQLException {
		System.out.println("Getting connection for 'H2 in memory' ...");
		Connection conn = cm.getConnection("org.h2.Driver", ConnectionManagerTest.generateH2JDBCURL(), new Properties());
		assertNotNull(conn);
		System.out.println("Connection OK.");
		conn.close();
		System.out.println("Connection closed.");
	}
	
	public void getDerbyConnection(ConnectionManager cm) throws TelosysToolsException, SQLException {
		System.out.println("Getting connection for 'Derby in memory' ...");
		Connection conn = cm.getConnection("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:memory:myDB;create=true", new Properties());
		assertNotNull(conn);
		System.out.println("Connection OK.");
		conn.close();
		System.out.println("Connection closed.");
	}
	
	public void test1() throws TelosysToolsException, SQLException  {
		System.out.println("--- Test 1");
		ConnectionManager cm = new ConnectionManager();
		String[] libraries = cm.getLibraries();
		assertEquals(0, libraries.length);
		
		getH2Connection(cm);
	}

//	public void test2() throws TelosysToolsException, SQLException {
//		System.out.println("--- Test 2");
//		String[] libs = JdbcTestCommons.getLibraries1a();
//		ConnectionManager cm = new ConnectionManager( libs, new ConsoleLogger() );
//		String[] libraries = cm.getLibraries();
//		printLibraries(libraries);
//		assertEquals(2, libraries.length);		
//
//		getH2Connection(cm);
//		
//		getDerbyConnection(cm);
//	}

	public void test3() throws TelosysToolsException, SQLException  {
		System.out.println("--- Test 1");
		ConnectionManager cm = new ConnectionManager();
		String[] libraries = cm.getLibraries();
		assertEquals(0, libraries.length);
		
		DbConfigManager dbDonfigManager = new DbConfigManager( new File("src/test/resources/dbcfg/databases-test-H2.dbcfg") );
		DatabasesConfigurations databasesConfigurations = dbDonfigManager.load();
		
		//--- Get a connection for the database #0
		Connection conn1 = cm.getConnection( databasesConfigurations.getDatabaseConfiguration(0) );
		System.out.println("Connection 1 OK.");
		Connection conn2 = cm.getConnection( databasesConfigurations.getDatabaseConfiguration(0) );
		System.out.println("Connection 2 OK.");
		conn1.close();
		conn2.close();
	}

//	private void printLibraries(String[] libraries) {
//		System.out.println("Libraries : ");
//		for ( String s : libraries ) {
//			System.out.println(". " + s);
//		}
//	}
}
