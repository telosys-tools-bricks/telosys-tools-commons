package org.telosys.tools.commons;

import java.io.File;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import junit.env.telosys.tools.commons.TestsEnv;

public class PropertiesManagerTest {

	public void print(String s) {
		System.out.println(s);
	}

	@BeforeClass
	public static void initAll() {
		// Fix PR : Make sure that tests doesn't rely on specific execution order
		// Currently test `testSaveBean` and `testSaveAndLoadBean` defined in `YamlSnakeYamlTest` failed if `yaml` folder doesn't exist in `target/tests-tmp`. 
		// It might exist if `YamlFileManagerTest` is executed before `YamlSnakeYamlTest` but there is no such guaranty.
		// Adding the creation of the folder before all tests in `YamlSnakeYamlTest` avoid the issue.
	}
	
	@Test
	public void test1()  {
		File propertiesFile = TestsEnv.getTestFile("file1.properties");
		PropertiesManager propertiesManager = new PropertiesManager(propertiesFile) ;
		Properties properties = propertiesManager.load(); // Return NULL if file not found
		assertEquals("AA   ", properties.getProperty("aa")); // NB: Trailing blanks are not removed
		assertEquals("", properties.getProperty("bb")); // NB: "" void even if blanks 
		assertEquals("", properties.getProperty("cc")); //
		assertNull(properties.getProperty("no-key")); //
	}

}
