package org.telosys.tools.tests.commons.jdbc;

import java.io.File;

public class JdbcTestCommons {

	//private final static String root = "D:/workspaces/wks36-telosys-plugin/telosys-tools-commons/target/test-classes/" ;
	//private final static String root = "D:/workspaces/wks36-telosys-plugin/telosys-tools-commons/src/test/resources/" ;
	private final static String root = new File("src/test/resources/").getAbsolutePath() ;
	
	public static String[] getLibraries1a() {
		// Test without SPACE in file path
		String[] libs = {
			root + "/myfolder1/derbyclient.jar", // for standard JDBC client driver 
			root + "/myfolder1/derby.jar" // for embedded JDBC driver 
		} ;
		return libs ;
	}

	public static String[] getLibraries1b() {
		// Test with SPACE in file path
		String[] libs = {
			root + "/my folder/derbyclient.jar", // for standard JDBC client driver 
			root + "/my folder/derby.jar" // for embedded JDBC driver 
		} ;
		return libs ;
	}
}
