package org.foo ;

public class FooData {
	
	// Set my data values here 
	private int    number1   = 100 ;
	private int    number2   =  22 ;
	
	public FooData() {
		
	}
	
	public int getNumber1() {
		return number1 ;
	}
	public int getNumber2() {
		return number2 ;
	}

	public int sum() {
		return number1 + number2 ;
	}
	public void addNumber1(int value) {
		number1 = number1 + value ;
	}
}