/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.commons.jdbc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Tool to run database SQL scripts ( SQL files ) <br>
 * The default configuration is : <br> 
 *  - SQL command delimiter = ';' <br>
 *  - stop on error  = true <br>
 *  - auto-commit    = true <br>
 *  
 */
public class SqlScriptRunner {

    private final Connection connection;

    private String      delimiter   = ";";
    //private final boolean fullLineDelimiter = false;
    private boolean     stopOnError = true ;
    private boolean     autoCommit  = true ;
    private PrintWriter logWriter      = new PrintWriter(System.out);
    private PrintWriter errorLogWriter = new PrintWriter(System.err);

    /**
     * Constructor
     * @param connection the JDBC connection to be used (not closed at the end)
     */
    public SqlScriptRunner(Connection connection ) {
    	if ( connection == null ) {
    		throw new IllegalArgumentException("Connection parameter is null");
    	}
        this.connection = connection;
    }

    /**
     * Set the "auto-commit" mode (true by default)
     * @param autoCommit  
     */
    public void setAutoCommit( boolean autoCommit ) {
        this.autoCommit = autoCommit;
    }

    /**
     * Set the "stop on error" flag (true by default)
     * @param stopOnError  
     */
    public void setStopOnError( boolean stopOnError ) {
        this.stopOnError = stopOnError;
    }

    /**
     * Set a specific delimiter
     * @param delimiter 
     */
    public void setDelimiter( String delimiter ) {
        this.delimiter = delimiter;
    }

    /**
     * Set a specific log writer (the default is System.out)
     * @param logWriter the specific log writer (or null for none)
     */
    public void setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    /**
     * Set a specific error log writer (the default is System.err)
     * @param errorLogWriter the specific error log writer (or null for none)
     */
    public void setErrorLogWriter(PrintWriter errorLogWriter) {
        this.errorLogWriter = errorLogWriter;
    }

    /**
     * Runs an SQL script (using the file name of the script)
     * @param fileName the file name of the script to be executed
     * @throws IOException
     * @throws SQLException
     */
    public void runScript(String fileName) throws IOException, SQLException 
    {
		FileReader fileReader = new FileReader(fileName);
		runScript(fileReader);
		fileReader.close();
    }
    
    /**
     * Runs an SQL script ( using the given file )
     * @param file
     * @throws IOException
     * @throws SQLException
     */
    public void runScript(File file) throws IOException, SQLException 
    {
//    	if ( ! file.exists()  ) {
//    		throw new IllegalArgumentException("File " + file.getAbsolutePath() + " doesn't exist");
//    	}
//    	if ( ! file.isFile() ) {
//    		throw new IllegalArgumentException("'" + file.getAbsolutePath() + "' is not a file");
//    	}
		FileReader fileReader = new FileReader(file);
		runScript(fileReader);
		fileReader.close();
    }
    
    /**
     * Runs an SQL script (using the Reader parameter)
     * @param reader the SQL script provided as a Reader instance
     * @throws IOException
     * @throws SQLException
     */
    public void runScript(Reader reader) throws IOException, SQLException {
        try {
            boolean originalAutoCommit = connection.getAutoCommit();
            try {
                if (originalAutoCommit != this.autoCommit) {
                    connection.setAutoCommit(this.autoCommit);
                }
                runScript(connection, reader);
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (IOException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }

    /**
     * Runs the SQL script using the given input
     * @param conn the connection to be used
     * @param reader  the source of the script
     * @throws IOException if an error occurs on the Reader (FileNotFoundException, etc)
     * @throws SQLException if any SQL errors occur 
     */
    private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
        StringBuffer command = null;
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) 
                {
                    println(trimmedLine);
                } 
                else if (trimmedLine.length() < 1
                        || trimmedLine.startsWith("//")) 
                {
                    // Do nothing
                } 
                else if (trimmedLine.length() < 1
                        || trimmedLine.startsWith("--")) 
                {
                    // Do nothing
                } 
//                else if (!fullLineDelimiter 
//                        && trimmedLine.endsWith(getDelimiter())
//                        || fullLineDelimiter
//                        && trimmedLine.equals(getDelimiter())) 
                else if ( trimmedLine.endsWith(getDelimiter() ) )
                {
                    command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
                    command.append(" ");
                    
                    executeSqlCommand(conn, command.toString());
//                    Statement statement = conn.createStatement();
//
//                    println(command);
//
//                    boolean hasResults = false;
//                    if (stopOnError) 
//                    {
//                        hasResults = statement.execute(command.toString());
//                    } 
//                    else 
//                    {
//                        try {
//                            statement.execute(command.toString());
//                        } catch (SQLException e) {
//                            e.fillInStackTrace();
//                            printlnError("Error executing: " + command);
//                            printlnError(e);
//                        }
//                    }
//
//                    if (autoCommit && !conn.getAutoCommit()) {
//                        conn.commit();
//                    }
//
//                    ResultSet rs = statement.getResultSet();
//                    if (hasResults && rs != null) 
//                    {
//                        ResultSetMetaData md = rs.getMetaData();
//                        int cols = md.getColumnCount();
//                        for (int i = 0; i < cols; i++) {
//                            String name = md.getColumnLabel(i);
//                            print(name + "\t");
//                        }
//                        println("");
//                        while (rs.next()) {
//                            for (int i = 0; i < cols; i++) {
//                                String value = rs.getString(i);
//                                print(value + "\t");
//                            }
//                            println("");
//                        }
//                    }
//
//                    command = null;
//                    try {
//                        statement.close();
//                    } catch (Exception e) {
//                        // Ignore to workaround a bug in Jakarta DBCP
//                    }
//                    Thread.yield();
                    
                    command = null ;
                    Thread.yield();
                } else {
                    command.append(line);
                    command.append(" ");
                }
            }
            if (!autoCommit) {
                conn.commit();
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
            printlnError("Error executing: " + command);
            printlnError(e);
            throw e;
        } catch (IOException e) {
            e.fillInStackTrace();
            printlnError("Error executing: " + command);
            printlnError(e);
            throw e;
        } finally {
            conn.rollback();
            flush();
        }
    }

    private void executeSqlCommand(Connection conn, String command) throws SQLException {
    	
        println(command);

        Statement statement = conn.createStatement();

        boolean resultIsResultSet  = false;
        if (stopOnError) 
        {
        	resultIsResultSet = statement.execute(command);
        } 
        else 
        {
            try {
            	resultIsResultSet = statement.execute(command);
            } catch (SQLException e) {
                e.fillInStackTrace();
                printlnError("Error executing: " + command);
                printlnError(e);
            }
        }

        if (autoCommit && !conn.getAutoCommit()) {
            conn.commit();
        }

        // If there's a ResultSet print it
        ResultSet rs = statement.getResultSet();
        if (resultIsResultSet && rs != null) 
        {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                String name = md.getColumnLabel(i);
                print(name + "\t");
            }
            println("");
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    String value = rs.getString(i);
                    print(value + "\t");
                }
                println("");
            }
            rs.close();
        }

        try {
            statement.close();
        } catch (Exception e) {
            // Ignore to workaround a bug in Jakarta DBCP
        }
    }

    private String getDelimiter() {
        return delimiter;
    }

    private void print(Object o) {
        if (logWriter != null)  logWriter.print(o);
    }

    private void println(Object o) {
        if (logWriter != null)  logWriter.println(o);
    }

    private void printlnError(Object o) {
        if (errorLogWriter != null)  errorLogWriter.println(o);
    }

    private void flush() {
        if (logWriter      != null)  logWriter.flush();
        if (errorLogWriter != null)  errorLogWriter.flush();
    }
}
