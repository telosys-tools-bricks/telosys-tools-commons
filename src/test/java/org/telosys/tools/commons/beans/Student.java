package org.telosys.tools.commons.beans;

import java.util.List;

public class Student {

    private int year;
    private String department;
    private String foo;
    private List<Course> courses;
    
	public Student() {
		super();
	}

	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}

	public String getFoo() {
		return foo;
	}
	public void setFoo(String foo) {
		this.foo = foo;
	}

	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return "Student [year=" + year + ", department=" + department + ", courses=" + courses + "]";
	}
	
}
