package org.telosys.tools.commons.jdbc;

import java.sql.Driver;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import junit.env.telosys.tools.commons.TestsEnv;

public class DriverLoaderlTest {

	private void loadDriverWithSpecificLib(String file) throws TelosysToolsException  {
		
		System.out.println("Specific library file : '" + file + "'");
		
		String[] paths = { file } ;
		DriverLoader driverLoader = new DriverLoader(paths);   
		assertNotNull(driverLoader);
		System.out.println("DriverLoader created.");
		
		// Derby JDBC driver 
		Driver driver = driverLoader.getDriver("org.apache.derby.jdbc.ClientDriver");
		assertNotNull(driver);
		System.out.println("Driver loaded : " + driver.getClass() );
		System.out.println("-----" );
	}

	@Test
	public void test01() throws TelosysToolsException {
		// Test without SPACE in file path
		loadDriverWithSpecificLib( TestsEnv.getTestFileAbsolutePath("/myfolder1/derbyclient.jar") );
	}

	@Test
	public void test02() throws TelosysToolsException {
		// Test with SPACE in file path
		loadDriverWithSpecificLib( TestsEnv.getTestFileAbsolutePath("/my folder/derbyclient.jar") );
	}

	@Test(expected=TelosysToolsException.class)
	public void test03() throws TelosysToolsException {
		// No specific libraries
		DriverLoader driverLoader = new DriverLoader();
		// Not supposed to found JDBC driver => TelosysToolsException
		driverLoader.getDriver("org.apache.derby.jdbc.ClientDriver");
	}

	@Test // (expected=TelosysToolsException.class)
	public void test04() throws TelosysToolsException {
		// No specific libraries
		DriverLoader driverLoader = new DriverLoader();
		// H2 lib in ClassPath (scope test) => supposed to found the JDBC driver
		Driver driver = driverLoader.getDriver("org.h2.Driver");
		assertNotNull(driver);
	}

	@Test (expected=TelosysToolsException.class)
	public void test10() throws TelosysToolsException {
		// Void specific libraries
		String[] libraries = { } ;
		DriverLoader driverLoader = new DriverLoader(libraries);   
		// Not supposed to found JDBC driver => TelosysToolsException
		driverLoader.getDriver("org.apache.derby.jdbc.ClientDriver");
	}

	@Test
	public void test11() throws TelosysToolsException {
		// Non existent library files
		String[] libraries = { 
				TestsEnv.buildAbsolutePath("/myfolder1/nofile.jar"),
				TestsEnv.buildAbsolutePath("/foo/bar.jar") } ;
		DriverLoader driverLoader = new DriverLoader(libraries);
		assertEquals(2, driverLoader.getLibraries().length );
		// H2 lib in ClassPath (scope test) => supposed to found the JDBC driver
		Driver driver = driverLoader.getDriver("org.h2.Driver");
		assertNotNull(driver);
	}

}
