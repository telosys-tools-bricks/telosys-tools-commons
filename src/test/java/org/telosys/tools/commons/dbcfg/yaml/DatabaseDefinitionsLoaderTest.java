package org.telosys.tools.commons.dbcfg.yaml;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.commons.exception.TelosysYamlException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DatabaseDefinitionsLoaderTest {

	public void print(String s) {
		System.out.println(s);
	}

	@Test(expected = Exception.class)
	public void testLoadErrorNoFile() throws TelosysYamlException {
		print("--- testLoad0");
		DatabaseDefinitionsLoader loader = new DatabaseDefinitionsLoader();
		loader.load(FileUtil.getFileByClassPath("/yaml/no-file.yaml"));
	}

	/**
	 * Check YAML syntax for initial file "META-INF/files/databases.yaml"
	 * to be sure the example file is valid
	 * @throws URISyntaxException
	 * @throws TelosysYamlException
	 */
	@Test
	public void testLoadInitialDatabasesYaml() throws URISyntaxException, TelosysYamlException {
		URL resource = getClass().getClassLoader().getResource("META-INF/files/databases.yaml");
		if (resource == null) {
			throw new TelosysRuntimeException("file not found!");
		} else {
			File file = new File(resource.toURI());
			DatabaseDefinitionsLoader loader = new DatabaseDefinitionsLoader();
			DatabaseDefinitions databaseDefinitions = loader.load(file);
			databaseDefinitions.containsDatabase("h2");
		}
	}

	@Test
	public void testLoadDatabasesYaml() throws TelosysYamlException {
		print("--- testLoad1");

		File file = FileUtil.getFileByClassPath("/yaml/databases.yaml");
		DatabaseDefinitionsLoader loader = new DatabaseDefinitionsLoader();
		DatabaseDefinitions databaseDefinitions = loader.load(file);

		List<DatabaseDefinition> dbList = databaseDefinitions.getDatabases();
		assertEquals(2, dbList.size());

		for (DatabaseDefinition dd : dbList) {
			print("  id   : " + dd.getId());
			print("  name : " + dd.getName());
			print("  type : " + dd.getType());
		}

		assertEquals("aaa", dbList.get(0).getId());
		assertEquals("H2", dbList.get(0).getType());
		assertEquals("org.h2.Driver", dbList.get(0).getDriver());
		assertEquals("CUSTOMERS", dbList.get(0).getCatalog());

		assertEquals("bbb", dbList.get(1).getId());
		assertEquals("", dbList.get(1).getType()); // not defined in file

		assertNull(databaseDefinitions.getDatabaseDefinition("invalid-db-id"));

		// --- test database "aaa"
		DatabaseDefinition dd = databaseDefinitions.getDatabaseDefinition("aaa");
		assertTrue(dd.isLinksManyToOne());
		assertTrue(dd.isLinksManyToMany());
		assertTrue(dd.isLinksOneToMany());

		print("isDbDefaultValue : " + dd.isDbDefaultValue());
		print("isDbComment      : " + dd.isDbComment());
		assertFalse(dd.isDbDefaultValue());
		assertFalse(dd.isDbComment());
		assertFalse(dd.isDbDefaultValue());
		assertFalse(dd.isDbComment());
		assertFalse(dd.isDbCatalog());
		assertFalse(dd.isDbDefaultValue());
		assertFalse(dd.isDbName());
		assertFalse(dd.isDbSchema());
		assertFalse(dd.isDbTable());
		assertFalse(dd.isDbView());

		// -- test database "bbb"
		dd = databaseDefinitions.getDatabaseDefinition("bbb");
		// following properties are not defined in YAML file => check default
		// values
		assertTrue(dd.isLinksManyToOne());
		assertFalse(dd.isLinksManyToMany());
		assertFalse(dd.isLinksOneToMany());

		// TRUE for all by default
		assertTrue(dd.isDbDefaultValue());
		assertTrue(dd.isDbComment());
		assertTrue(dd.isDbCatalog());
		assertTrue(dd.isDbDefaultValue());
		assertTrue(dd.isDbName());
		assertTrue(dd.isDbSchema());
		assertTrue(dd.isDbTable());
		assertTrue(dd.isDbView());
	}

}
