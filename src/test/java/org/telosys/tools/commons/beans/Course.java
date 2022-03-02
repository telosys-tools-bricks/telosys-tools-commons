package org.telosys.tools.commons.beans;

public class Course {
    private String name;
    private double credits;

    public Course() {
		super();    	
	}

    public Course(String name, double credits) {
		super();
		this.name = name;
		this.credits= credits;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public double getCredits() {
		return credits;
	}
	public void setCredits(double credits) {
		this.credits = credits;
	}

	@Override
	public String toString() {
		return "Course:name=" + name + ",credits=" + credits ;
	}

}
