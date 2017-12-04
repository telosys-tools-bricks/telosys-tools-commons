package org.telosys.tools.commons.jdbc;

import java.sql.Driver;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.jdbc.DriverLoader;

public class DriverLoaderlTest extends TestCase {

//	private void getDriverFromResource(String jarFileName) throws TelosysToolsException  {
//
//		// DOESN'T WORK  ( due to "%20" for "SPACE" char in file path )
//		System.out.println("get driver from test/resources : '" + jarFileName + "'");
//		URL url = getClass().getResource(jarFileName);
//		assertNotNull(url);
//		
//		String file = url.getFile() ;
//		getDriverFromFile(file);
//	}
	
	private void getDriverFromFile(String file) throws TelosysToolsException  {
		
		System.out.println("get driver from file : '" + file + "'");
		
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
	
	public void testGetDriver1() throws TelosysToolsException {
		// Test without SPACE in file path
		
		//getDriverFromFile("D:/workspaces/wks36-telosys-plugin/telosys-tools-commons/target/test-classes/myfolder1/derbyclient.jar");
		getDriverFromFile( TestsEnv.getTestFileAbsolutePath("/myfolder1/derbyclient.jar") );
	}

	public void testGetDriver2() throws TelosysToolsException {
		// Test with SPACE in file path
		//getDriverFromFile("D:/workspaces/wks36-telosys-plugin/telosys-tools-commons/target/test-classes/my folder/derbyclient.jar");
		getDriverFromFile( TestsEnv.getTestFileAbsolutePath("/my folder/derbyclient.jar") );
	}
//	public void testGetDriver3() throws TelosysToolsException {
//		getDriverFromResource("/my folder/derbyclient.jar");
//	}

}
