package org.telosys.tools.commons.bundles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.YamlFileManager;
import org.telosys.tools.commons.exception.TelosysRuntimeException;
import org.telosys.tools.commons.exception.TelosysYamlException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BundleYamlLoaderTest {

	public void print(String s) {
		System.out.println(s);
	}

	private String getString(Map<String,Object> map, String key) {
		Object value = map.get(key);
		if (value != null ) {
			if ( value instanceof String) {
				return (String) value;
			}
			else {
				throw new TelosysRuntimeException("String value expected");
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Object> toMap(Object object) {
		if ( object instanceof Map) {
			return (Map<String,Object>) object;
		}
		else {
			throw new TelosysRuntimeException("Map object expected");
		}
	}
	
	private Map<String,Object> getMap(Map<String,Object> map, String key) {
		Object value = map.get(key);
		if (value != null ) {
			return toMap(value);
//			if ( value instanceof Map) {
//				return (Map<String,Object>) value;
//			}
//			else {
//				throw new TelosysRuntimeException("Map value expected");
//			}
		}
		return new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	private List<Object> getList(Map<String,Object> map, String key) {
		Object value = map.get(key);
		if (value != null ) {
			if ( value instanceof List) {
				return (List<Object>) value;
			}
			else {
				throw new TelosysRuntimeException("List value expected");
			}
		}
		return new ArrayList<>();
	}
	
	@Test
	public void testYaml1() throws TelosysYamlException {
		YamlFileManager yaml = new YamlFileManager();
		// Load YAML as a "map"
		Map<String,Object> rootMap = yaml.load(FileUtil.getFileByClassPath("/yaml/bundle.yaml"));
		assertNotNull(rootMap);
		
		// First map level
		String title = getString(rootMap,"title");
		assertEquals("My bundle", title);
		
		String version = getString(rootMap,"version");
		assertEquals("1.0.0", version);
		
		String description = getString(rootMap,"description");
		assertEquals("Basic bundle example", description);

		// LEVEL 2 : "requirements" map
		System.out.println(rootMap.get("requirements").getClass().getCanonicalName());
		Map<String,Object> requirements = getMap(rootMap, "requirements"); // getClass() -> java.util.LinkedHashMap
		assertNotNull(requirements);
		
		String telosysVersion = getString(requirements, "telosys-version");
		assertEquals("4.2", telosysVersion);
		
		// LEVEL 2 : "targets" list of maps
		System.out.println(rootMap.get("targets").getClass().getCanonicalName());
		List<Object> targets = getList(rootMap, "targets"); // getClass() -> java.util.ArrayList
		assertNotNull(targets);
		assertEquals(2, targets.size());
		for ( Object o : targets ) {
			Map<String,Object> target = toMap(o);
			String name = getString(target,"name");
			assertNotNull(name);
			System.out.println("target.name : " + name);
			String template = getString(target,"template");
			assertNotNull(template);
			System.out.println("target.template : " + template);
		}

		// LEVEL 2 : "variables" map
		System.out.println(rootMap.get("variables").getClass().getCanonicalName());
		Map<String,Object> variables = getMap(rootMap, "variables"); //  getClass() -> java.util.LinkedHashMap
		assertNotNull(variables);
		assertEquals(8, variables.size());
        for (Map.Entry<String,Object> entry : variables.entrySet()) {
            System.out.println(" . " + entry.getKey() + " : " + entry.getValue() + " - " + entry.getValue().getClass().getCanonicalName() );
        }
        assertTrue(variables.get("AAA_INT") instanceof Integer);
        assertTrue(variables.get("BBB_FLOAT") instanceof Double);
        assertTrue(variables.get("CCC_BOOL") instanceof Boolean);
        assertTrue(variables.get("DDD_STR") instanceof String);
        assertTrue(variables.get("EEE_LIST") instanceof List); // java.util.ArrayList
        assertTrue(variables.get("HHH_OBJ") instanceof Map); // java.util.LinkedHashMap

	}

}
