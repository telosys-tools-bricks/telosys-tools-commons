package org.telosys.tools.tests.commons.jdbc;

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
		System.out.println("--- Test 1");
		testH2Connection1();
		System.out.println("--- Test 2");
		testH2Connection2();
		System.out.println("--- Test 3");
		testH2ConnectionWithDriverLoader();
	}

	public static void testH2Connection1() throws ClassNotFoundException, SQLException{
		Class.forName("org.h2.Driver");
		System.out.println("Driver loaded.");
		Connection conn = DriverManager.getConnection("jdbc:h2:~/test");
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
