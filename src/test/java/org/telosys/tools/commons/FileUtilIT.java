package org.telosys.tools.commons;

import java.nio.file.Paths;

import junit.framework.TestCase;

/**
 * "IT" test because "path management" do not have the same behavior between Windows and Linux 
 * 
 * @author laguerin
 *
 */
public class FileUtilIT extends TestCase {

	public void testgetAbsolutePath() {
		// Absolute Path => keep is as is 
		String path = "D:\\aa\\bb\\foo\\bar";
		assertTrue(Paths.get(path).isAbsolute());
		assertEquals(Paths.get(path), FileUtil.getAbsolutePath(path, "not-used") ) ;
		path = "/home/aa/foo/bar";
//		assertTrue(Paths.get(path).isAbsolute());
		assertEquals(Paths.get(path), FileUtil.getAbsolutePath(path, "not-used") ) ;

		// Relative path - Windows 
		String currentDir = "D:\\aa\\bb";
		assertEquals(Paths.get("D:\\aa\\bb\\foo\\bar"), FileUtil.getAbsolutePath("foo/bar", currentDir) ) ;
		assertEquals(Paths.get("D:\\aa\\bb\\foo\\bar"), FileUtil.getAbsolutePath("foo\\bar", currentDir) ) ;
		assertEquals(Paths.get("D:\\aa\\bb\\foo\\bar"), FileUtil.getAbsolutePath(".\\foo\\bar", currentDir) ) ;
		assertEquals(Paths.get("D:\\aa\\foo\\bar"), FileUtil.getAbsolutePath("..\\foo\\bar", currentDir) ) ;

		// Relative path - Linux like 
		currentDir = "/home/aa";
		assertEquals(Paths.get("/home/aa/foo/bar"), FileUtil.getAbsolutePath("foo/bar", currentDir) ) ;
		assertEquals(Paths.get("/home/aa/foo/bar"), FileUtil.getAbsolutePath("./foo/bar", currentDir) ) ;
		assertEquals(Paths.get("/home/z/foo.txt"), FileUtil.getAbsolutePath("../z/foo.txt", currentDir) ) ;
		assertEquals(Paths.get("/z/foo.txt"), FileUtil.getAbsolutePath("../../z/foo.txt", currentDir) ) ;
	}
}
