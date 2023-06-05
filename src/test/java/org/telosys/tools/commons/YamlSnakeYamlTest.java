package org.telosys.tools.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.telosys.tools.commons.beans.Course;
import org.telosys.tools.commons.beans.Student;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import junit.env.telosys.tools.commons.TestsEnv;

public class YamlSnakeYamlTest {

	public void print(String s) {
		System.out.println(s);
	}

	@BeforeClass
	public static void initAll() {
		TestsEnv.getTmpExistingFolder("/yaml");
	}
	
	@Test
	public void testLoadPrometheusCfg() throws IOException {
		print("--- testLoad");
		File file = FileUtil.getFileByClassPath("/yaml/prometheus-cfg.yaml");
		
		Yaml yaml = new Yaml();
//		InputStream inputStream = this.getClass()
//		  .getClassLoader()
//		  .getResourceAsStream("customer.yaml");
		InputStream inputStream = new FileInputStream(file);
		Map<String, Object> map = yaml.load(inputStream);
		
		Map<String, Object> global = (Map<String, Object>) map.get("global");
		print("global : " );
		print("  scrape_interval : " + global.get("scrape_interval")) ;
		print("  scrape_timeout  : " + global.get("scrape_timeout")) ;
		
		
//		Map<String, Object> alerting = (Map<String, Object>) map.get("alerting");
//		print(alerting);
//		
//		List<Object> ruleFiles = (List<Object>) map.get("rule_files");
//		print(ruleFiles);

		List<Map<String, Object>> scrapeConfigs = (List<Map<String, Object>>) map.get("scrape_configs");
		//print(scrapeConfigs);
		print("scrape_configs count = " + scrapeConfigs.size());
		for ( Map<String, Object> scrapeConfig : scrapeConfigs ) {
			// print("- " +scrapeConfig);
			print("  " + scrapeConfig.get("job_name") + " : " + scrapeConfig.get("scrape_interval") );
			
		}
	}

	@Test
	public void testLoadBean() throws IOException {
		print("--- testLoadBean");
		File file = TestsEnv.getTestFile("/yaml/student.yaml");		
		Student student = (Student) loadBean(file, Student.class);
		assertNotNull(student);
	}
	private Object loadBean(File file, Class<? extends Object> clazz) throws IOException {
		print("Loading from : " + file.getAbsolutePath());
		Yaml yaml = new Yaml(new Constructor(clazz));
		Object object = yaml.load(new FileInputStream(file));
		print("Object loaded :");
		print(object.toString());
		return object;
	}	

	private Student buildStudent() {
		Student student = new Student();
		student.setDepartment("The department");
		student.setYear(2010);
		student.setFoo("fooValue");
		List<Course> courses = new LinkedList<>();
		courses.add(new Course("Math", 24.8));
		courses.add(new Course("Literature", 234.90));
		student.setCourses(courses);
		return student;
	}
	
	@Test
	public void testSaveBean() throws IOException {
		print("--- testSaveBean");
		File file = TestsEnv.getTmpFile("yaml/student-dump.yaml");
		saveBean(file, buildStudent());
	}
	private void saveBean(File file, Object object) throws IOException {
		print("Saving bean in : " + file.getAbsolutePath());
		Writer writer = new FileWriter(file);
		DumperOptions options = new DumperOptions();
		options.setIndent(2);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		//options.setCanonical(true);
		//options.setPrettyFlow(true);
		Yaml yaml = new Yaml(options);
		yaml.dump(object, writer);
	}
	
	@Test
	public void testSaveAndLoadBean() throws IOException {
		print("--- testSaveAndLoadBean");
		File file = TestsEnv.getTmpFile("yaml/student-dump.yaml");
		saveBean(file, buildStudent());
		Student student = (Student) loadBean(file, Student.class);
		assertNotNull(student);
		assertEquals("The department", student.getDepartment());
		assertEquals(2010, student.getYear());
		assertEquals(2, student.getCourses().size());
	}
}
