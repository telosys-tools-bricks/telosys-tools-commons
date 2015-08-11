package org.telosys.tools.commons.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.jdbc.DriverLoader;

public class TestH2Database {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, TelosysToolsException{
		System.out.println("Test H2 connection...");

		//testRemoteConnections();

		testEmbeddedConnections();
	}

	public static void testRemoteConnections() throws ClassNotFoundException, SQLException {
		// The server is supposed to be started
		// User/password required for all filesystem access
		testH2ConnectionRemote("my-h2-database-remote",           "sa", ""); // Creates  "(h2-directory)/bin/xxxx.h2.db
		testH2ConnectionRemote("./my-h2-database-remote",         "sa", ""); // Creates  "(current-dir)/xxxx.h2.db"
		testH2ConnectionRemote("~/my-h2-database-remote",         "sa", ""); // Creates  "D:/User/(user-name)/xxxx.h2.db
		testH2ConnectionRemote("D:/H2DATA/my-h2-database-remote", "sa", "");
		testH2ConnectionRemote("mem:my-h2-database-remote",        null, null); // No file, "in-memory"
	}

	public static void testEmbeddedConnections() throws ClassNotFoundException, SQLException {
		// Embedded => the server is not started
		/* 
		 * A file path that is implicitly relative to the current working directory 
		 * is not allowed in the database URL "jdbc:h2:myh2database3". 
		 * Use an absolute path, ~/name, ./name, or the baseDir setting instead. [90011-181]
		 */
		//testH2EmbeddedConnection("my-h2-database-embedded", "sa", "");   // ERROR : not allowed, no "h2 directory"

		testH2EmbeddedConnection("./my-h2-database-embedded",         null, null); // Creates  "(current-dir)/xxxx.h2.db
		testH2EmbeddedConnection("~/my-h2-database-embedded",         null, null); // Creates  "D:/User/(user-name)/xxxx.h2.db
		testH2EmbeddedConnection("D:/H2DATA/my-h2-database-embedded", "sa", "");   // User and Password required
		testH2EmbeddedConnection("mem:my-h2-database-embedded",        null, null); // No file, "in-memory"
	}

	public static void testH2EmbeddedConnection(String dbPath, String user, String password) throws ClassNotFoundException, SQLException {
		connect("jdbc:h2:" + dbPath, user, password);
	}

	
	public static void testH2ConnectionRemote(String dbPath, String user, String password) throws ClassNotFoundException, SQLException {
//		Class.forName("org.h2.Driver");
//		System.out.println("Driver loaded.");
//		Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/" + dbPath, "sa", "");
//		System.out.println("Connection OK.");
//		conn.close();
//		System.out.println("Connection closed.");
		connect("jdbc:h2:tcp://localhost/" + dbPath, user, password);
	}

	public static void connect(String jdbcURL, String user, String password) throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		System.out.println("Driver loaded.");
		Connection conn = null ;
		if ( user != null ) {
			System.out.println("Try to connect : " + jdbcURL + "(user=" + user + ", password="+ password+") ...");
			conn = DriverManager.getConnection(jdbcURL, user, password);
		}
		else {
			System.out.println("Try to connect : " + jdbcURL + "(no user/password) ...");
			conn = DriverManager.getConnection(jdbcURL);
		}
		System.out.println("Connection OK.");
		conn.close();
		System.out.println("Connection closed.");
	}


	public static void testH2Connection2() throws ClassNotFoundException, SQLException{
		ClassLoader classLoader = TestH2Database.class.getClassLoader();
		classLoader.loadClass("org.h2.Driver");
		//Class.forName("org.h2.Driver");
		System.out.println("Driver loaded.");
		Connection conn = DriverManager.getConnection("jdbc:h2:~/test");
		System.out.println("Connection OK.");
		conn.close();
		System.out.println("Connection closed.");
	}

	public static void testH2ConnectionWithDriverLoader() throws TelosysToolsException, SQLException {
		DriverLoader driverLoader = new DriverLoader( new ConsoleLogger() );
		Driver driver = driverLoader.getDriver("org.h2.Driver");
		System.out.println("Driver loaded.");

		System.out.println("Getting connection...");
		//Connection conn = DriverManager.getConnection("jdbc:h2:~/test");
		Connection conn = driver.connect("jdbc:h2:~/test", new Properties());
		
		System.out.println("Connection OK.");
		conn.close();
		System.out.println("Connection closed.");
	}
}
