package org.telosys.tools.commons.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.telosys.tools.commons.TelosysToolsException;

public class H2Util {

	private static final String H2_JDBC_DRIVER = "org.h2.Driver" ;
	
	//private static final String H2_JDBC_URL = "jdbc:h2:~/test" ;
	private static final String H2_JDBC_URL = "jdbc:h2:mem:testdb" ; // in memory db
	
	private H2Util() {
	}

	public static final Connection getH2Connection() throws TelosysToolsException, SQLException {
		ConnectionManager cm = new ConnectionManager();
		return getH2Connection(cm);
	}
	
	public static final Connection getH2Connection(ConnectionManager cm) throws TelosysToolsException, SQLException {
		System.out.println("Getting connection for 'H2 in memory' ...");
		Connection conn = cm.getConnection(H2_JDBC_DRIVER, H2_JDBC_URL, new Properties());
		if ( conn != null ) {
			//System.out.println("Connection OK.");
			return conn;
		}
		else {
			throw new RuntimeException("Cannot get H2 connection");
		}
	}
	
}
