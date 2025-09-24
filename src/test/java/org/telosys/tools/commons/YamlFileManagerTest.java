package org.telosys.tools.commons;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.yaml.snakeyaml.emitter.EmitterException;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.serializer.SerializerException;

import static org.junit.Assert.assertEquals;

import junit.env.telosys.tools.commons.TestsEnv;

public class YamlFileManagerTest {

	public void print(String s) {
		System.out.println(s);
	}

	@BeforeClass
	public static void initAll() {
		// Fix PR : Make sure that tests doesn't rely on specific execution order
		// Currently test `testSaveBean` and `testSaveAndLoadBean` defined in `YamlSnakeYamlTest` failed if `yaml` folder doesn't exist in `target/tests-tmp`. 
		// It might exist if `YamlFileManagerTest` is executed before `YamlSnakeYamlTest` but there is no such guaranty.
		// Adding the creation of the folder before all tests in `YamlSnakeYamlTest` avoid the issue.
		TestsEnv.getTmpExistingFolder("/yaml");
	}
	
	@Test
	public void testLoadMap1() throws TelosysToolsException  {
		YamlFileManager yaml = new YamlFileManager(FileUtil.getFileByClassPath("/yaml/model.yaml"));
		
		Map<String,Object> data = yaml.loadMap();
		@SuppressWarnings("unchecked")
		Map<String,Object> modelMap = (Map<String,Object>) data.get("model"); // LinkedHashMap
		assertEquals("java.util.LinkedHashMap", modelMap.getClass().getName());
		assertEquals("MyModelName", modelMap.get("name"));
		assertEquals(1.0, modelMap.get("version")); // No quotes => type = Double for value 1.0
	}
	
	@Test(expected = TelosysToolsException.class)
	public void testLoadMapWithNoFile() throws TelosysToolsException  {
		YamlFileManager yaml = new YamlFileManager(TestsEnv.getTmpFile("/yaml/aa/bb/no-file.yaml")); 
		yaml.loadMap(); // throws exception
	}		

	protected void printYamlExceptionType(YAMLException ye) {
		if ( ye instanceof MarkedYAMLException ) {
			print("--- YAMLException is instance of MarkedYAMLException"); 
			MarkedYAMLException mye = (MarkedYAMLException) ye ;
			print("MarkedYAMLException class : " + mye.getClass().getCanonicalName() );
			print("MarkedYAMLException msg   : " + mye.getMessage() );
			Mark mark = mye.getProblemMark();
			int line = mark.getLine() + 1;
			// mark.getName() : 'reader'
			print("--- line " + line + " snippet '" + mark.get_snippet() + "'");
			//mark.getBuffer()
		}
		else if ( ye instanceof EmitterException ){
			print("--- YAMLException is instance of EmitterException"); 
		}
		else if ( ye instanceof SerializerException  ){
			print("--- YAMLException is instance of SerializerException "); 
		}
		else {
			print("--- YAMLException : no subclass "); 
		}
		if (ye.getCause() != null) {
			Throwable cause = ye.getCause();
			print("Cause class : " + cause.getClass().getCanonicalName());
			print("Cause msg   : " + cause.getMessage());
		}
	}

	@Test
	public void testSaveAndLoadMap() throws TelosysToolsException  {
		File file = TestsEnv.getTmpFile("yaml/data-map-1.yaml");
		YamlFileManager yaml = new YamlFileManager(file);
		// Init and save
		Map<String,Object> map = new HashMap<>();
		map.put("k1", "v1");
		map.put("k2", "v2");
		yaml.saveMap(map);
		// Load
		YamlFileManager yaml2 = new YamlFileManager(file);
		Map<String,Object> map2 = yaml2.loadMap();
		// Check same maps
		assertEquals(map, map2);
	}

}
