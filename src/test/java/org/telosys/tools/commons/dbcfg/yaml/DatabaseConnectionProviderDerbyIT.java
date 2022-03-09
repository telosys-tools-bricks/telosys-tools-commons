package org.telosys.tools.commons.dbcfg.yaml;

import java.io.File;
import java.sql.Connection;
import java.util.Properties;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.cfg.TelosysToolsCfgManager;
import org.telosys.tools.commons.dbcfg.DbConnectionStatus;

import static org.junit.Assert.fail;

import junit.env.telosys.tools.commons.TestsEnv;

public class DatabaseConnectionProviderDerbyIT  {
	
	private DatabaseConnectionProvider getDatabaseConnectionProvider() throws TelosysToolsException {
		File projectFolder = TestsEnv.getTestFolder("project2");
		TelosysToolsCfgManager cfgManager = new TelosysToolsCfgManager(projectFolder.getAbsolutePath());
		TelosysToolsCfg telosysToolsCfg = cfgManager.loadTelosysToolsCfg();
		return new DatabaseConnectionProvider(telosysToolsCfg);
	}
	
	private void print(String s) {
		System.out.println(s);
	}
	
	private void testConnection(String databaseId) throws TelosysToolsException {
		print("=============================" );
		DatabaseConnectionProvider databaseConnectionProvider = getDatabaseConnectionProvider(); 
		print("getConnection("+databaseId+")...");
		Connection con = databaseConnectionProvider.getConnection(databaseId);

		print("Connection ready");
		print("testConnection()...");
		DbConnectionStatus dbConnectionStatus = DatabaseConnectionTool.getConnectionStatus(con);
		
		print(". product name    : " + dbConnectionStatus.getProductName() ) ;
		print(". product version : " + dbConnectionStatus.getProductVersion() ) ;
		print(". catalog         : " + dbConnectionStatus.getCatalog() ) ;
		print(". schema          : " + dbConnectionStatus.getSchema() ) ;
		print(". auto-commit     : " + dbConnectionStatus.isAutocommit() ) ;
		print(". client info     : " ) ;
		
		Properties clientInfo = dbConnectionStatus.getClientInfo();
		if ( clientInfo.isEmpty() ) {
			print("no client info (empty properties) " ) ;
		}
		else {
			clientInfo.list(System.out);
		}

		print("closeConnection()...");
		DatabaseConnectionTool.closeConnection(con);
	}

	@Test
	public void test1() throws TelosysToolsException {
		testConnection("database1") ;
	}

	@Test
	public void test2() {  // No driver JDBC => Cannot load class xxx
		try {
			testConnection("database2") ;
			fail("Exception expected");
		} catch (TelosysToolsException e) {
			print("Exception : " + e.getMessage() );
		} 
	}

	@Test 
	public void test3() { // No configuration for database 3 => unknown database
		try {
			testConnection("database3") ;
			fail("Exception expected");
		} catch (TelosysToolsException e) {
			print("Exception : " + e.getMessage() );
		} 
	}

}
