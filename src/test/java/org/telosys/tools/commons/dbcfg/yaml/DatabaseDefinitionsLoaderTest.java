package org.telosys.tools.commons.dbcfg.yaml;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.FileUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
		
		assertNull( databaseDefinitions.getDatabaseDefinition("invalid-db-id") );
		
		//--- test database "aaa"
		DatabaseDefinition dd = databaseDefinitions.getDatabaseDefinition("aaa");
		assertTrue(dd.isLinksManyToOne());
		assertTrue(dd.isLinksManyToMany());
		assertTrue(dd.isLinksOneToMany());
		
		print("isDatabaseDefaultValue : " + dd.isDatabaseDefaultValue());
		print("isDatabaseComment      : " + dd.isDatabaseComment());
		assertFalse(dd.isDatabaseDefaultValue());
		assertTrue(dd.isDatabaseComment());
		
		//-- test database "bbb" 
		dd = databaseDefinitions.getDatabaseDefinition("bbb");
		// following properties are not defined in YAML file => check default values
		assertTrue(dd.isLinksManyToOne());
		assertFalse(dd.isLinksManyToMany());
		assertFalse(dd.isLinksOneToMany());
		assertTrue(dd.isDatabaseDefaultValue());
		assertTrue(dd.isDatabaseComment());
	}

}
