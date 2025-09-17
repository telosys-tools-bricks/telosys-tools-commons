package org.telosys.tools.commons;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import junit.env.telosys.tools.commons.TestsEnv;

public class YamlFileManagerWithEncryptionTest {

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
	public void testSaveAndLoadMapWithEncryption() throws TelosysToolsException {
		File file = TestsEnv.getTmpFile("yaml/data-map-encr.yaml");
		SecretKey secretKey = CryptoAES.buildSecretKey();
		
		Map<String,Object> map = new HashMap<>();
		map.put("k1", "v1");
		map.put("k2", "v2");

		// Save
		YamlFileManagerWithEncryption yaml = new YamlFileManagerWithEncryption(file, secretKey);
		yaml.saveMap(map);
		
		// Load
		YamlFileManagerWithEncryption yaml2 = new YamlFileManagerWithEncryption(file, secretKey);
		Map<String,Object> map2 = yaml2.loadMap();
		assertEquals(map, map2);
	}
	
}
