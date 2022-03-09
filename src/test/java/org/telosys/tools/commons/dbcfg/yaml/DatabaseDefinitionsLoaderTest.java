package org.telosys.tools.commons.dbcfg.yaml;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.FileUtil;

import static org.junit.Assert.assertEquals;

public class DatabaseDefinitionsLoaderTest {

	public void print(String s) {
		System.out.println(s);
	}
	
	@Test(expected=Exception.class)
	public void testLoad0() {
		print("--- testLoad0");
		DatabaseDefinitionsLoader loader = new DatabaseDefinitionsLoader();
		loader.load(FileUtil.getFileByClassPath("/yaml/no-file.yaml"));
	}
	
	@Test
	public void testLoad1() {
		print("--- testLoad1");
		
		File file = FileUtil.getFileByClassPath("/yaml/databases.yaml") ;
		DatabaseDefinitionsLoader loader = new DatabaseDefinitionsLoader();
		DatabaseDefinitions databaseDefinitions = loader.load(file);
		
		List<DatabaseDefinition> dbList = databaseDefinitions.getDatabases();
		assertEquals(2, dbList.size());
		
		for ( DatabaseDefinition dd : dbList ) {
			print("  id   : " + dd.getId()) ;
			print("  name : " + dd.getName()) ;
			print("  type : " + dd.getType()) ;
		}
		
		assertEquals("aaa", dbList.get(0).getId() );
		assertEquals("H2", dbList.get(0).getType() );
		assertEquals("org.h2.Driver", dbList.get(0).getDriver() );
		assertEquals("CUSTOMERS", dbList.get(0).getCatalog() );
		
		assertEquals("bbb", dbList.get(1).getId() );
		assertEquals("", dbList.get(1).getType() ); // not defined in file
	}

}
