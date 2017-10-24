package org.telosys.tools.commons.dbcfg;

import java.io.File;
import java.sql.Connection;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;

import junit.env.telosys.tools.commons.TestsEnv;

public class DbConnectionManagerDerbyIT  {

	private DbConnectionManager getDbConnectionManager() throws TelosysToolsException {
		File projectFolder = TestsEnv.getTestFolder("project2");
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager(projectFolder.getAbsolutePath());
		TelosysToolsCfg telosysToolsCfg = cfgManager.loadTelosysToolsCfg();
		DbConnectionManager dbConnectionManager = new DbConnectionManager(telosysToolsCfg);
		return dbConnectionManager ;
	}
	
	private void testConnection(Integer id) throws TelosysToolsException {
		System.out.println("=============================" );
		DbConnectionManager dbConnectionManager = getDbConnectionManager(); 
		Connection con ;
		if ( id != null ) {
			System.out.println("getConnection("+id+")...");
			con = dbConnectionManager.getConnection(id);
		}
		else {
			System.out.println("getConnection()...");
			con = dbConnectionManager.getConnection();
		}
		System.out.println("Connection ready");
		System.out.println("testConnection()...");
		dbConnectionManager.testConnection(con);
		
		System.out.println("getDatabaseInfo()...");
		DbInfo dbInfo = dbConnectionManager.getDatabaseInfo(con);
		System.out.println(" . database Product Name    : " + dbInfo.getDatabaseProductName() );
		System.out.println(" . database Product Version : " + dbInfo.getDatabaseProductVersion() );
		
		System.out.println("closeConnection()...");
		dbConnectionManager.closeConnection(con);
	}

	@Test
	public void testDefaultDb() throws TelosysToolsException {
		testConnection(null) ;
	}

	@Test
	public void test1() throws TelosysToolsException {
		testConnection(1) ;
	}

	@Test(expected=Exception.class) // No driver
	public void test2() throws TelosysToolsException {
		testConnection(2) ; 
	}

	@Test(expected=Exception.class) // No database 3
	public void test3() throws TelosysToolsException {
		testConnection(3) ; 
	}


}
