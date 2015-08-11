package org.telosys.tools.commons;

import org.junit.Test;
import org.telosys.tools.commons.ExceptionUtil;

import static org.junit.Assert.*;

public class ExceptionUtilTest  {

	@Test
	public void test1()  {
		System.out.println("--- TEST ");
		Exception e = null ;
		String s = ExceptionUtil.getStackTraceAsString(e);
		System.out.println(s);
		assertNotNull(s);
	}

	public void throwException()  {
		throw new RuntimeException("My test exception");
	}
	
	public void throwExceptionCause()  {
		throw new IllegalArgumentException("Illegal value");
	}
	
	public void throwExceptionWithCause()  {
		try {
			throwExceptionCause();
		}
		catch(Exception e) {
			throw new RuntimeException("My test exception", e);
		}
	}
	
	public byte throwNullPointerException()  {
//		Integer i = null ;
//		byte b = i.byteValue();
//		return b;
		throw new NullPointerException();
	}
	
	@Test
	public void test2()  {
		System.out.println("--- TEST ");
		try {
			throwNullPointerException() ;
		}
		catch( Exception e) {
			System.out.println("Exception catched");
			assertNotNull(e);
			String s = ExceptionUtil.getStackTraceAsString(e);
			System.out.println(s);
			assertNotNull(s);
		}
	}
	
	@Test
	public void test3()  {
		System.out.println("--- TEST ");
		try {
			throwException();
		}
		catch( Exception e) {
			System.out.println("Exception catched");
			String s = ExceptionUtil.getStackTraceAsString(e, 3);
			System.out.println(s);
			assertNotNull(s);
		}
	}
	
	@Test
	public void test4()  {
		System.out.println("--- TEST ");
		try {
			throwException();
		}
		catch( Exception e) {
			System.out.println("Exception catched");
			String s = ExceptionUtil.getStackTraceAsString(e.getStackTrace(), 3);
			System.out.println(s);
			assertNotNull(s);
		}
	}
	
	@Test
	public void test5()  {
		System.out.println("--- TEST ");
		try {
			throwException();
		}
		catch( Exception e) {
			System.out.println("Exception catched");
			String s = ExceptionUtil.getExceptionSummary(e);
			System.out.println(s);
			assertNotNull(s);
		}
	}
	
	@Test
	public void test6()  {
		System.out.println("--- TEST ");
		try {
			throwExceptionWithCause();
		}
		catch( Exception e) {
			System.out.println("Exception catched");
			String s = ExceptionUtil.getExceptionSummary(e);
			System.out.println(s);
			assertNotNull(s);
		}
	}
	
}
