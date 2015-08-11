package org.telosys.tools.commons.jdbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.jdbc.ConnectionManager;
import org.telosys.tools.commons.jdbc.SqlScriptRunner;

public class SqlScriptRunnerTest extends TestCase {

	private Connection getH2Connection() throws TelosysToolsException, SQLException {
		ConnectionManager cm = new ConnectionManager( new ConsoleLogger() );
		System.out.println("Getting connection for 'H2 in memory' ...");
		Connection conn = cm.getConnection("org.h2.Driver", "jdbc:h2:~/test", new Properties());
		assertNotNull(conn);
		System.out.println("Connection OK.");
		return conn;
	}

	public void testNoFileWithPath() throws TelosysToolsException, IOException, SQLException {
		
		Connection conn = getH2Connection() ;
		SqlScriptRunner scriptRunner = new SqlScriptRunner(conn);
		String scriptFileFullPath = TestsEnv.buildAbsolutePath("sql/script1-inex.sql");
		System.out.println("Script file : " + scriptFileFullPath );
		
		Exception error = null ;
		try {
			System.out.println("Running SQL script ...");
			scriptRunner.runScript( scriptFileFullPath); // FileNotFound Exception expected
			System.out.println("SQL script executed.");
		} catch (FileNotFoundException e) {
			error = e ;
			System.out.println("Expected exception : " + e.toString());
		} 

		conn.close();
		assertNotNull(error);
	}

	public void testNoFileWithReader() throws TelosysToolsException, IOException, SQLException {
		
		Connection con = getH2Connection() ;
		SqlScriptRunner scriptRunner = new SqlScriptRunner(con);
		
		File scriptFile = new File( TestsEnv.buildAbsolutePath("sql/script1-inex.sql") ) ;
		System.out.println("Script file : " + scriptFile.getAbsolutePath() ); 
		
		Exception error = null ;
		try {
			FileReader fileReader = new FileReader(scriptFile) ; 
			System.out.println("Running SQL script ...");
			scriptRunner.runScript( fileReader); // FileNotFound Exception expected
			System.out.println("SQL script executed.");
		} catch (FileNotFoundException e) {
			error = e ;
			System.out.println("Expected exception : " + e.toString());
		}
		
		con.close();
		assertNotNull(error);
	}

	public void testNoFileWithFile() throws TelosysToolsException, IOException, SQLException {
		
		Connection con = getH2Connection() ;
		SqlScriptRunner scriptRunner = new SqlScriptRunner(con);
		
		File scriptFile = new File( TestsEnv.buildAbsolutePath("sql/script1-inex.sql") ) ;
		System.out.println("Script file : " + scriptFile.getAbsolutePath() ); 
		
		Exception error = null ;
		try {
			System.out.println("Running SQL script ...");
			scriptRunner.runScript( scriptFile); // FileNotFound Exception expected
			System.out.println("SQL script executed.");
		} catch (FileNotFoundException e) {
			error = e ;
			System.out.println("Expected exception : " + e.toString());
		}
		
		con.close();
		assertNotNull(error);
	}

	private int doSQLSelectCount(Connection con) throws SQLException {
		int count = 0 ;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("Select count(*) from employee") ;
		while ( rs.next() ) {
			count = rs.getInt(1) ;
			System.out.println("Count(*) = " + count );
		}
		stmt.close();
		return count ;
	}
	
	public void testWithFileReader() throws TelosysToolsException, IOException, SQLException {
		
		Connection conn = getH2Connection() ;
		SqlScriptRunner scriptRunner = new SqlScriptRunner(conn);
		
		File scriptFile = TestsEnv.getTestFile("sql/script1-H2.sql");
		FileReader fileReader = new FileReader(scriptFile) ; 
		
		System.out.println("Running SQL script ...");
		scriptRunner.runScript( fileReader);
		System.out.println("SQL script executed.");
		
		int count = doSQLSelectCount(conn) ;
		System.out.println("SQL count = " + count );
		conn.close();
	}

	public void testWithFile() throws TelosysToolsException, IOException, SQLException {
		
		Connection conn = getH2Connection() ;
		SqlScriptRunner scriptRunner = new SqlScriptRunner(conn);
		
		File scriptFile = TestsEnv.getTestFile("sql/script1-H2.sql");
		
		System.out.println("Running SQL script ...");
		scriptRunner.runScript(scriptFile);
		System.out.println("SQL script executed.");
		
		int count = doSQLSelectCount(conn) ;
		System.out.println("SQL count = " + count );
		conn.close();
	}

	public void testWithFileName() throws TelosysToolsException, IOException, SQLException {
		
		Connection conn = getH2Connection() ;
		SqlScriptRunner scriptRunner = new SqlScriptRunner(conn);
		
		String scriptFileFullPath = TestsEnv.getTestFileAbsolutePath("sql/script1-H2.sql"); 
		
		System.out.println("Running SQL script ...");
		scriptRunner.runScript( scriptFileFullPath );
		System.out.println("SQL script executed.");
		
		// doSQLSelect(conn);
		conn.close();
	}

}
