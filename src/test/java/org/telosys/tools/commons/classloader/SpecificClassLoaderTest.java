package org.telosys.tools.commons.classloader;

import java.net.URL;

import junit.env.telosys.tools.commons.TestsEnv;

import org.junit.Assert;
import org.junit.Test;

public class SpecificClassLoaderTest {
	
	private void print(URL[] urls) {
    	System.out.println( urls.length + " url(s) ");
        for ( URL url : urls ) {
        	System.out.println(" . " + url);
        }
	}
	
	@Test
	public void test0() {
		String jarsPaths[] = {};
        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);
        print (loader.getURLs() ) ;
	}

	@Test(expected=RuntimeException.class)
	public void test1() {
		String jarsPaths[] = { null, null };
        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);
        print (loader.getURLs() ) ;
	}

	@Test(expected=RuntimeException.class)
	public void test2() {
		String jarsPaths[] = { "", "" };
        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);
        print (loader.getURLs() ) ;
	}

	@Test(expected=RuntimeException.class)
	public void test3() {
		String jarsPaths[] = { "aaaa.jar", "" };
        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);
        print (loader.getURLs() ) ;
	}

	@Test
	public void test4() {
		String jarsPaths[] = { "aaaa.jar", "bbb.jar" };
        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);
        print (loader.getURLs() ) ;
	}

	@Test(expected=ClassNotFoundException.class)
	public void test5() throws ClassNotFoundException {
		String jarsPaths[] = { "D:/aa/bb/ccc/ddd/aaaa.jar", "X:/xxx/yyy/zzzz/bbb.jar" };
        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);
        print (loader.getURLs() ) ;
		Class<?> clazz = loader.loadClass("org.demo.Foo");
		Assert.assertNotNull(clazz);
	}
	
	@Test
	public void test6() throws ClassNotFoundException {
		
		String jarsPaths[] = { 
			TestsEnv.getTestFileAbsolutePath("lib/javax.inject-1.jar") // existing JAR
		};
//		SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, ClassLoader.getSystemClassLoader());
        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths);
        print (loader.getURLs() ) ;
		
        Class<?> clazz = loader.loadClass("javax.inject.Singleton");
		Assert.assertNotNull(clazz);
		System.out.println("Class loaded : " + clazz.getCanonicalName() );

		clazz = loader.loadClass("javax.inject.Inject");
		Assert.assertNotNull(clazz);
		System.out.println("Class loaded : " + clazz.getCanonicalName() );
	}
	
	@Test
	public void test7() throws ClassNotFoundException {
		
		String jarsPaths[] = { 
			"D:/aa/bb/ccc/ddd/aaaa.jar", 
			TestsEnv.getTestFileAbsolutePath("lib/javax.inject-1.jar") // existing JAR
		};
//		SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, ClassLoader.getSystemClassLoader());
        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths);
        print (loader.getURLs() ) ;

        Class<?> clazz = loader.loadClass("javax.inject.Singleton");
		Assert.assertNotNull(clazz);
		System.out.println("Class loaded : " + clazz.getCanonicalName() );
		
		clazz = loader.loadClass("javax.inject.Inject");
		Assert.assertNotNull(clazz);
		System.out.println("Class loaded : " + clazz.getCanonicalName() );
	}
	
}
