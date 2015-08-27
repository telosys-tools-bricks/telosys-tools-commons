package org.telosys.tools.commons.cfg;

import java.io.File;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.variables.Variable;

public class TelosysToolsCfgTest extends TestCase {

	public void printSeparator() {
		System.out.println("==============================================================" );
	}
	public void print(File file) {
		System.out.println("File   : " + file.toString());
		System.out.println("Parent : " + file.getParent());
	}
	
	public void print(TelosysToolsCfg telosysToolsCfg) {
		System.out.println( "getProjectAbsolutePath = " + telosysToolsCfg.getProjectAbsolutePath() );
		System.out.println( "getCfgFileAbsolutePath = " + telosysToolsCfg.getCfgFileAbsolutePath() );
		System.out.println( "getDatabasesDbCfgFile             = " + telosysToolsCfg.getDatabasesDbCfgFile());
		System.out.println( "getDatabasesDbCfgFileAbsolutePath = " + telosysToolsCfg.getDatabasesDbCfgFileAbsolutePath());
		
		print(telosysToolsCfg.getAllVariables());
		
		print(telosysToolsCfg.getProperties());
	}
	
	public void print(Variable[] variables) {
		System.out.println("VARIABLES : ");
		for ( Variable v : variables ) {
			System.out.println(" . " + v.getName() + " = " + v.getValue() + " ( " + v.getSymbolicName() + " )");
		}
	}
	
	public void print(Properties properties) {
		System.out.println("PROPERTIES : ");
		Set<Object> keys = properties.keySet();
		for ( Object k : keys ) {
			System.out.println(" . " + k + " = " + properties.get(k) );
		}
	}
	
	public String toString(String[] array) {
		StringBuffer sb = new StringBuffer();
		for ( String s : array ) {
			sb.append("'");
			sb.append(s);
			sb.append("' ");
		}
		return sb.toString();
	}
	
	public void test1() throws TelosysToolsException {
		printSeparator();
		
		String projectFolderAbsolutePath = "X:/foo/bar/myproject" ;
		String telosysToolCfgAbsolutePath = "X:/foo/bar/myproject/telosys-tools.cfg" ;
		
		TelosysToolsCfg telosysToolsCfg = new TelosysToolsCfg(projectFolderAbsolutePath, telosysToolCfgAbsolutePath, null);
		
		assertEquals(projectFolderAbsolutePath, telosysToolsCfg.getProjectAbsolutePath() );
		assertEquals(telosysToolCfgAbsolutePath, telosysToolsCfg.getCfgFileAbsolutePath() );
		
		// Default packages
		assertEquals("org.demo", telosysToolsCfg.getRootPackage() );
		assertEquals("org.demo.bean", telosysToolsCfg.getEntityPackage() );
		
		// Variables 
		assertEquals(0, telosysToolsCfg.getSpecificVariables().length ); 
		assertEquals(9, telosysToolsCfg.getAllVariables().length ); 
		
		assertEquals(projectFolderAbsolutePath + "/TelosysTools/templates", telosysToolsCfg.getTemplatesFolderAbsolutePath() );
		assertEquals(projectFolderAbsolutePath + "/TelosysTools/templates/mybundle", 
				telosysToolsCfg.getTemplatesBundleFolderAbsolutePath("mybundle") );
	}

}
