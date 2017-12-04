package org.telosys.tools.commons.dbcfg;

import java.io.File;
import java.sql.Connection;
import java.util.Properties;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;

import static org.junit.Assert.fail;

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
		DbConnectionStatus dbConnectionStatus = dbConnectionManager.testConnection(con);
		
		System.out.println(". product name    : " + dbConnectionStatus.getProductName() ) ;
		System.out.println(". product version : " + dbConnectionStatus.getProductVersion() ) ;
		System.out.println(". catalog         : " + dbConnectionStatus.getCatalog() ) ;
		System.out.println(". schema          : " + dbConnectionStatus.getSchema() ) ;
		System.out.println(". auto-commit     : " + dbConnectionStatus.isAutocommit() ) ;
		System.out.println(". client info     : " ) ;
		
		Properties clientInfo = dbConnectionStatus.getClientInfo();
		if ( clientInfo.isEmpty() ) {
			System.out.println("no client info (empty properties) " ) ;
		}
		else {
			clientInfo.list(System.out);
		}

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

	@Test 
	public void test2() { // No JDBC driver
		try {
			testConnection(2) ;
			fail("Exception expected");
		} catch (TelosysToolsException e) {
			System.out.println("Exception : " + e.getMessage() );
		} 
	}

	@Test 
	public void test3() { // No configuration for database 3
		try {
			testConnection(3) ;
			fail("Exception expected");
		} catch (TelosysToolsException e) {
			System.out.println("Exception : " + e.getMessage() );
		} 
	}

}
