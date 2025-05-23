package org.telosys.tools.commons;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.telosys.tools.commons.beans.Course;
import org.telosys.tools.commons.beans.Student;
import org.telosys.tools.commons.exception.TelosysYamlException;
import org.yaml.snakeyaml.emitter.EmitterException;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.serializer.SerializerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoadMap() throws TelosysYamlException  {
		print("--- testLoadMap");
		
		YamlFileManager yaml = new YamlFileManager();
		
		Map<String,Object> data = yaml.load(FileUtil.getFileByClassPath("/yaml/model.yaml"));
		Map<String,Object> model = (Map<String,Object>) data.get("model"); // LinkedHashMap
		print(model.getClass().getName());
		print(model.toString());
		
		print("  name : " + model.get("name")) ;
		print("  version : " + model.get("version")) ;
		assertEquals("MyModelName", model.get("name"));
		assertEquals(1.0, model.get("version")); // Double
	}

	@Test
	public void testLoadBean() throws TelosysYamlException {
		print("--- testLoadBean");
		YamlFileManager yaml = new YamlFileManager();
		Student student = yaml.load(FileUtil.getFileByClassPath("/yaml/student.yaml"), Student.class);
		print(student.toString());
		assertEquals("History", student.getDepartment());
		assertEquals(2020, student.getYear());
		assertEquals(2, student.getCourses().size());
	}

	@Test
	public void testLoadBeanInvalid() {
		print("--- testLoadBeanInvalid");
		YamlFileManager yaml = new YamlFileManager();
		try {
			yaml.load(FileUtil.getFileByClassPath("/yaml/student-invalid.yaml"), Student.class);
		} catch (TelosysYamlException  e) {
			assertTrue(e.getCause() instanceof YAMLException);
			assertTrue(e.getCause() instanceof MarkedYAMLException);
			printYamlExceptionType((YAMLException)e.getCause());
		}
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
	public void testSaveMap() {
		print("--- testSaveMap");
		
		Map<String,Object> innerMap = new HashMap<>();
		innerMap.put("firstName", "Bob");
		innerMap.put("age", 12);

		Map<String,Object> map1 = new HashMap<>();
		map1.put("aa", "aa-value");
		map1.put("bb", true);
		map1.put("cc", 123);
		map1.put("void-map", new HashMap<>() );
		map1.put("innerMap", innerMap);

		List<String> list = new LinkedList<>();
		list.add("element1");
		list.add("element2");

		Map<String,Object> data = new HashMap<>();
		data.put("map1", map1);
		data.put("list", list);
		
		File file1 = TestsEnv.getTmpFile("yaml/data1.yaml");
		save(file1, map1);
		assertTrue(file1.exists());
		
		File file2 = TestsEnv.getTmpFile("yaml/data2.yaml");
		save(file2, data);
		assertTrue(file2.exists());
	}

	@Test
	public void testSaveBean() {
		print("--- testSaveBean");
		List<Course> courses = new LinkedList<>();
		Student student = new Student();
		student.setYear(2022);
		student.setDepartment("Math");
		student.setCourses(courses);

		File file = TestsEnv.getTmpFile("yaml/data3-student.yaml");
		save(file, student);
		assertTrue(file.exists());
	}

	private void save(File file, Object data) {
		print("Saving data : " + data);
		print("in file : " + file.getAbsolutePath() );
		YamlFileManager yaml = new YamlFileManager();
		yaml.save(file, data);
	}

}
