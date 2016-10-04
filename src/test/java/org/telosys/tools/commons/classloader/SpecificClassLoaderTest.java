package org.telosys.tools.commons.classloader;

import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import junit.env.telosys.tools.commons.TestsEnv;

import org.junit.Assert;
import org.junit.Test;

public class SpecificClassLoaderTest {
	
	private File getLoaderDir() {
		return TestsEnv.getTestFolder("loader"); // Is a directory
	}
	private File getLibDir() {
		return TestsEnv.getTestFolder("loader/lib"); // Is a directory
	}
	private File getClassesDir() {
		return TestsEnv.getTestFolder("loader/classes"); // Is a directory
	}
	private File getLibJar() {
		return new File( TestsEnv.getTestFileAbsolutePath("loader/lib/javax.inject-1.jar")) ;
	}
	
	private SpecificClassLoader buildClassLoader() {
		SpecificClassPath classpath = buildClassPath();
		SpecificClassLoader loader = new SpecificClassLoader(buildClassPath());
        print (loader.getURLs() ) ;
        Assert.assertEquals(3, loader.getURLs().length);
        return loader;
	}
	
	private SpecificClassPath buildClassPath() {
		SpecificClassPath classpath = new SpecificClassPath();
		classpath.addDirectory(getClassesDir()); // "classes"
		classpath.addJarFilesInDirectory(getLibDir()); // "lib/*.jar"
		Assert.assertEquals(3, classpath.size());
		return classpath ;
	}
	
	public Class<?> loadClass(SpecificClassLoader loader, String className) throws ClassNotFoundException {
        Class<?> clazz = loader.loadClass(className);
		Assert.assertNotNull(clazz);
		System.out.println("Class loaded : " + clazz.getCanonicalName() );
		return clazz ;
	}

	private void print(URL[] urls) {
    	System.out.println( urls.length + " url(s) ");
        for ( URL url : urls ) {
        	System.out.println(" . " + url);
        }
	}
	
	@Test
	public void test0() {
//		//String jarsPaths[] = {};
//		List<String> jarsPaths = new LinkedList<String>();
//        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
//        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);

        SpecificClassPath classpath = new SpecificClassPath();
		SpecificClassLoader loader = new SpecificClassLoader(classpath);
        print (loader.getURLs() ) ;
        Assert.assertEquals(0, loader.getURLs().length);
	}

//	@Test(expected=RuntimeException.class)
//	public void test1() {
//		//String jarsPaths[] = { null, null };
//		List<String> jarsPaths = new LinkedList<String>();
//		jarsPaths.add(null);
//		jarsPaths.add(null);
//        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
//        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);
//        print (loader.getURLs() ) ;
//	}

	@Test
	public void test2() {
		//String jarsPaths[] = { "", "" };
		
//		List<String> jarsPaths = new LinkedList<String>();
//		jarsPaths.add("");
//		jarsPaths.add("");
//        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
//        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);
        
        SpecificClassPath classpath = new SpecificClassPath();
		classpath.addJarFile(new File("")) ; //  
		classpath.addJarFile(new File("")) ; //  
		SpecificClassLoader loader = new SpecificClassLoader(classpath);
        print (loader.getURLs() ) ;
        Assert.assertEquals(0, loader.getURLs().length);
	}

	@Test
	public void test3() {
		//String jarsPaths[] = { "aaaa.jar", "" };
		
//		List<String> jarsPaths = new LinkedList<String>();
//		jarsPaths.add("aaaa.jar");
//		jarsPaths.add("");
//        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
//        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);
        
        SpecificClassPath classpath = new SpecificClassPath();
		classpath.addJarFile(new File("aaaa.jar")) ; //  
		classpath.addJarFile(new File("")) ; //  
		SpecificClassLoader loader = new SpecificClassLoader(classpath);
        
        print (loader.getURLs() ) ;
        Assert.assertEquals(0, loader.getURLs().length);
	}

	@Test
	public void test4() {
		//String jarsPaths[] = { "aaaa.jar", "bbb.jar" };
//		List<String> jarsPaths = new LinkedList<String>();
//		jarsPaths.add("aaaa.jar");
//		jarsPaths.add("bbbb.jar");
//        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
//        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);

        SpecificClassPath classpath = new SpecificClassPath();
		classpath.addJarFile(new File("aaaa.jar")) ; //  
		classpath.addJarFile(new File("bbb.jar")) ; //  
		classpath.addJarFile(new File("ccc.zip")) ; //  
		SpecificClassLoader loader = new SpecificClassLoader(classpath);

        print (loader.getURLs() ) ;
        Assert.assertEquals(0, loader.getURLs().length);
	}

	@Test(expected=ClassNotFoundException.class)
	public void test5() throws ClassNotFoundException {
		//String jarsPaths[] = { "D:/aa/bb/ccc/ddd/aaaa.jar", "X:/xxx/yyy/zzzz/bbb.jar" };
		
//		List<String> jarsPaths = new LinkedList<String>();
//		jarsPaths.add("D:/aa/bb/ccc/ddd/aaaa.jar");
//		jarsPaths.add("X:/xxx/yyy/zzzz/bbb.jar");
//        ClassLoader parentLoader = ClassLoader.getSystemClassLoader();
//        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, parentLoader);

        SpecificClassPath classpath = new SpecificClassPath();
		classpath.addJarFile(new File("D:/aa/bb/ccc/ddd/aaaa.jar")) ; //  
		classpath.addJarFile(new File("X:/xxx/yyy/zzzz/bbb.jar")) ; //  
		classpath.addJarFile(new File("X:/xxx/yyy/zzzz/ccc.zip")) ; //  
		SpecificClassLoader loader = new SpecificClassLoader(classpath);
        
        print (loader.getURLs() ) ;
        Assert.assertEquals(0, loader.getURLs().length);

        Class<?> clazz = loader.loadClass("org.demo.Foo");
		Assert.assertNotNull(clazz);
	}
	
	@Test
	public void test6() throws ClassNotFoundException {
		
//		String jarsPaths[] = { 
//			TestsEnv.getTestFileAbsolutePath("lib/javax.inject-1.jar") // existing JAR
//		};
//		List<String> jarsPaths = new LinkedList<String>();
//		jarsPaths.add( TestsEnv.getTestFileAbsolutePath("loader/lib/javax.inject-1.jar") ); // existing JAR

//		SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, ClassLoader.getSystemClassLoader());
//        SpecificClassLoader loader = new SpecificClassLoader(jarsPaths);
		
        SpecificClassPath classpath = new SpecificClassPath();
		classpath.addJarFile(TestsEnv.getTestFile("loader/lib/javax.inject-1.jar")) ; //  existing JAR
		SpecificClassLoader loader = new SpecificClassLoader(classpath);

		print (loader.getURLs() ) ;
        Assert.assertEquals(1, loader.getURLs().length);

		
        Class<?> clazz = loader.loadClass("javax.inject.Singleton");
		Assert.assertNotNull(clazz);
		System.out.println("Class loaded : " + clazz.getCanonicalName() );

		clazz = loader.loadClass("javax.inject.Inject");
		Assert.assertNotNull(clazz);
		System.out.println("Class loaded : " + clazz.getCanonicalName() );
	}
	
	@Test
	public void test7() throws ClassNotFoundException {
		
//		String jarsPaths[] = { 
//			"D:/aa/bb/ccc/ddd/aaaa.jar", 
//			TestsEnv.getTestFileAbsolutePath("lib/javax.inject-1.jar") // existing JAR
//		};
		
//		List<String> jarsPaths = new LinkedList<String>();
//		jarsPaths.add( "D:/aa/bb/ccc/ddd/aaaa.jar" ); // existing JAR
//		jarsPaths.add( TestsEnv.getTestFileAbsolutePath("loader/lib/javax.inject-1.jar") ); // existing JAR

		SpecificClassPath classpath = new SpecificClassPath();
		classpath.addJarFile(new File("D:/aa/bb/ccc/ddd/aaaa.jar")) ; // Not exists
		classpath.addJarFile(TestsEnv.getTestFile("loader/lib/javax.inject-1.jar")) ; //  existing JAR
		
//		SpecificClassLoader loader = new SpecificClassLoader(jarsPaths, ClassLoader.getSystemClassLoader());
        SpecificClassLoader loader = new SpecificClassLoader(classpath);
        print (loader.getURLs() ) ;
        Assert.assertEquals(1, loader.getURLs().length);

        Class<?> clazz = loader.loadClass("javax.inject.Singleton");
		Assert.assertNotNull(clazz);
		System.out.println("Class loaded : " + clazz.getCanonicalName() );
		
		clazz = loader.loadClass("javax.inject.Inject");
		Assert.assertNotNull(clazz);
		System.out.println("Class loaded : " + clazz.getCanonicalName() );
	}
	
	@Test
	public void test8() throws ClassNotFoundException {
		File libFolder = getLibDir();
		// ClassPath with only "lib/*.jar"
		SpecificClassPath classpath = new SpecificClassPath(); 
		classpath.addJarFilesInDirectory(libFolder);
		SpecificClassLoader loader = new SpecificClassLoader(classpath);
        print (loader.getURLs() ) ;
        Assert.assertEquals(2, loader.getURLs().length);

        loadClass(loader, "javax.inject.Singleton");
        loadClass(loader, "javax.inject.Inject");
        
        try {
			loadClass(loader, "MyData");
			fail("Class not found expected");
		} catch (ClassNotFoundException e) {
		}
	}

	@Test
	public void test9() throws ClassNotFoundException {
		// Only "classes" directory
		SpecificClassPath classpath = new SpecificClassPath(); 
		classpath.addDirectory(getClassesDir());
		SpecificClassLoader loader = new SpecificClassLoader(classpath);
        print (loader.getURLs() ) ;
        Assert.assertEquals(1, loader.getURLs().length);

		loadClass(loader, "MyData"); 

		try {
            loadClass(loader, "javax.inject.Singleton");
			fail("Class not found expected");
		} catch (ClassNotFoundException e) {
		}
	}

	@Test
	public void test10() throws ClassNotFoundException {
		SpecificClassLoader loader = buildClassLoader();

		loadClass(loader, "MyData");
        loadClass(loader, "javax.inject.Singleton");
        loadClass(loader, "javax.inject.Inject");
	}
	
	@Test
	public void test11() throws ClassNotFoundException, NoSuchMethodException, 
						InvocationTargetException, IllegalAccessException, InstantiationException {
		SpecificClassLoader loader = buildClassLoader();

        loadClass(loader, "MyData");
        loadClass(loader, "org.foo.FooData");
        loadClass(loader, "javax.inject.Singleton");
        loadClass(loader, "org.apache.commons.lang3.BooleanUtils");

        // Load, instanciate and invoke method
        Class<?> c = loadClass(loader, "org.foo.MyFooClass"); // This method uses "org.apache.commons.lang3.BooleanUtils"
        Object instance = c.newInstance();
        Method m = c.getMethod("negate", Boolean.class);
        Assert.assertNotNull(m);
        Object r = m.invoke(instance, Boolean.valueOf(false));
        System.out.println("invocation result = " + r );
	}	

}
