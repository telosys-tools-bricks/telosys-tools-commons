package org.telosys.tools.commons.classloader;

import java.io.File;

import junit.env.telosys.tools.commons.TestsEnv;

import org.junit.Assert;
import org.junit.Test;

public class SpecificClassLoaderHelperTest {
	
	private void print(String[] jars) {
    	System.out.println( jars.length + " jar(s) ");
        for ( String s : jars ) {
        	System.out.println(" . " + s);
        }
	}
	
	@Test
	public void test0() {
		String[] jarPaths = SpecificClassLoaderHelper.getJarPaths("X:/xxx/yyyy/zzz");
		print(jarPaths);
		Assert.assertEquals(0, jarPaths.length);
	}

	@Test(expected=IllegalArgumentException.class)
	public void test1() {
		String dir = TestsEnv.getTestFileAbsolutePath("lib/javax.inject-1.jar"); // Not a directory
		String[] jarPaths = SpecificClassLoaderHelper.getJarPaths( dir );
		print(jarPaths);
		Assert.assertEquals(0, jarPaths.length);
	}

	@Test
	public void test2() {
		File dir = TestsEnv.getTestFolder("lib"); // Is a directory
		String[] jarPaths = SpecificClassLoaderHelper.getJarPaths( dir );
		print(jarPaths);
		Assert.assertEquals(1, jarPaths.length);
	}

	@Test
	public void test3() {
		File dir = TestsEnv.getTestFolder("lib"); // Is a directory
		String[] jarPaths = SpecificClassLoaderHelper.getJarPaths( dir.getAbsolutePath() );
		print(jarPaths);
		Assert.assertEquals(1, jarPaths.length);
	}

}
