package org.telosys.tools.commons;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.telosys.tools.commons.beans.Course;
import org.telosys.tools.commons.beans.Student;

import static org.junit.Assert.assertEquals;

import junit.env.telosys.tools.commons.TestsEnv;

public class YamlFileManagerTest {

	public void print(String s) {
		System.out.println(s);
	}
	
	@Test
	public void testLoadMap() {
		print("--- testLoadMap");
		
		YamlFileManager yaml = new YamlFileManager();
		
		Map<String,Object> data = yaml.load(FileUtil.getFileByClassPath("/yaml/model.yaml"));
		Map<String,Object> model = (Map<String,Object>) data.get("model"); // LinkedHashMap
		print(model.getClass().getName());
		print(model.toString());
		
		print("  name : " + model.get("name")) ;
		print("  version : " + model.get("version")) ;
	}

	@Test
	public void testLoadBean() {
		print("--- testLoadBean");
		YamlFileManager yaml = new YamlFileManager();
		Student student = yaml.load(FileUtil.getFileByClassPath("/yaml/student.yaml"), Student.class);
		print(student.toString());
		assertEquals("History", student.getDepartment());
		assertEquals(2020, student.getYear());
		assertEquals(2, student.getCourses().size());
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
		
		save(TestsEnv.getTmpFile("yaml/data1.yaml"), map1);
		save(TestsEnv.getTmpFile("yaml/data2.yaml"), data);
	}

	@Test
	public void testSaveBean() {
		print("--- testSaveBean");
		List<Course> courses = new LinkedList<>();
		Student student = new Student();
		student.setYear(2022);
		student.setDepartment("Math");
		student.setCourses(courses);
		save(TestsEnv.getTmpFile("yaml/data3-student.yaml"), student);
	}

	//	private void saveMap(File file, Map<String,Object> data) {
	private void save(File file, Object data) {
		print("Saving data : " + data);
		print("in file : " + file.getAbsolutePath() );
		YamlFileManager yaml = new YamlFileManager();
		yaml.save(file, data);
	}


//	private void saveBean(File file, Map<String,Object> data) {
//		print("Saving data : " + data);
//		print("in file : " + file.getAbsolutePath() );
//		YamlFileManager yaml = new YamlFileManager();
//		yaml.save(file, data);
//	}

}
