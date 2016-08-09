package org.telosys.tools.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.env.telosys.tools.commons.TestsEnv;

import org.junit.Test;

public class DirUtilTest {

	//---------------------------------------------------------------------------------------------
	@Test
	public void testCreateDirectory() throws IOException {
		System.out.println("===== testCreateDirectory() ");
		//TestsEnv.getTmpRootFolder() ;
		File file = TestsEnv.getTmpFileOrFolder("tests-dir/mydir");
		System.out.println("Dir : " + file.getAbsolutePath() );
		
		//--- Delete if exists
		if ( file.exists() ) {
			System.out.println("deleting dir "  + file.getAbsolutePath() );
			if ( file.isDirectory() ) {
				DirUtil.deleteDirectory(file);
			}
			else {
				file.delete();
			}
		}
		assertFalse(file.exists());
		
		//--- Create new directory without sub folders
		createDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo1") ) ;
		// Again
		createDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo1") ) ;
		
		//--- Create new directory with sub folders
		createDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo2/bar") ) ;
		// Again
		createDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo2/bar") ) ;
		
		//--- Create new directory with an existing file with the same name
		createVoidFile( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo3") ) ;
		createDirWithExceptionExpected( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo3") ) ;
	}

	//---------------------------------------------------------------------------------------------
	@Test
	public void testDeleteDirectory() throws IOException  {
		System.out.println("===== testDeleteDirectory() ");
		
		createDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo999/bar1/bar2") ) ;
		deleteDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo999/bar1/bar2") );

		createDir( TestsEnv.getTmpFileOrFolder(     "tests-dir/mydir/foo999/bar1/bar2") ) ;
		createVoidFile( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo999/bar1/bar2/aaa.txt") ); 
		createVoidFile( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo999/bar1/bbbb.txt") ); 
		deleteDir( TestsEnv.getTmpFileOrFolder(     "tests-dir/mydir/foo999/bar1") ) ;

		createDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo999/bar1/bar2") ) ;
		deleteDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo999/bar1/") ) ; // ending "/"

		createDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo999/bar1/bar2") ) ;
		deleteDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo999") ) ;
	}
	
	//---------------------------------------------------------------------------------------------
	@Test
	public void testGetDirectoryFiles() throws IOException  {
		System.out.println("===== testGetDirectoryFiles() ");
		
		//createDir( TestsEnv.getTmpFileOrFolder(     "tests-dir/mydir/foo888/aaa/bbb") ) ;
		
		File directory = TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888");
		DirUtil.deleteDirectory(directory);
		createDir( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888/aaa/bbb/ccc") ) ;
		
		createVoidFile( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888/aaa/ooo.txt") ); 
		createVoidFile( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888/aaa/bbb/aaa.txt") ); 
		createVoidFile( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888/aaa/bbb/ccc/xxx.txt") ); 
		createVoidFile( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888/aaa/bbb/ccc/yyy.txt") ); 
		createVoidFile( TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888/aaa/bbb/ccc/zzz.txt") ); 
		
		getDirFiles(TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888/aaa"), true,  5 );
		getDirFiles(TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888/aaa"), false, 1 );
		getDirFiles(TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888"), true,  5 );
		getDirFiles(TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo888"), false, 0 );
	}
	
	//---------------------------------------------------------------------------------------------
	private void getDirFiles(File dir, boolean recursively, int expectedNbFiles ) {
		List<String> files = DirUtil.getDirectoryFiles(dir, recursively );
		System.out.println("Dir '" + dir.getAbsolutePath()+ " contains " + files.size() + " file(s)" );
		for ( String fileName : files ) {
			System.out.println(" . " + fileName);
		}
		assertEquals(expectedNbFiles, files.size());
	}
	
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	private void createDir(File newDir) {
		System.out.println("creating dir " + newDir.getAbsolutePath() );
		DirUtil.createDirectory( newDir );
		assertTrue ( newDir.exists() ) ;
		assertTrue ( newDir.isDirectory() ) ;
	}
	
	private void createDirWithExceptionExpected(File newDir) {
		System.out.println("creating dir with exception expected" + newDir.getAbsolutePath() );
		RuntimeException rte = null ;
		try {
			createDir(      TestsEnv.getTmpFileOrFolder("tests-dir/mydir/foo3") ) ;
		} catch (RuntimeException e) {
			rte = e ;
			System.out.println("Exception catched : " + e);
		}
		assertNotNull(rte);
	}
	
	private void deleteDir(File dir) {
		System.out.println("deleting dir " + dir.getAbsolutePath() );
		DirUtil.deleteDirectory( dir );
		assertFalse ( dir.exists() ) ;
	}

	private void createVoidFile(File file) throws IOException {
		System.out.println("creating void file " + file.getAbsolutePath() );
		file.createNewFile();
	}

	@Test(expected=TelosysToolsException.class)
	public void testCheckIsValidDirectoryNotOK1() throws TelosysToolsException  {
		System.out.println("===== testCheckIsValidDirectoryNotOK() ");		
		DirUtil.checkIsValidDirectory("/invalid/folder");
	}	

	@Test(expected=TelosysToolsException.class)
	public void testCheckIsValidDirectoryNotOK2() throws TelosysToolsException  {
		System.out.println("===== testCheckIsValidDirectoryNotOK() ");		
		File f = TestsEnv.getTestFile("foo.txt");
		DirUtil.checkIsValidDirectory(f.getAbsolutePath());
	}	

	@Test()
	public void testCheckIsValidDirectoryOk() throws TelosysToolsException  {
		System.out.println("===== testCheckIsValidDirectoryOk() ");		
		File f = TestsEnv.getTestRootFolder();
		DirUtil.checkIsValidDirectory(f.getAbsolutePath());
	}	
}
