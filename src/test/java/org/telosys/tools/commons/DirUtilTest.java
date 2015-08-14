package org.telosys.tools.commons;

import java.io.File;
import java.io.IOException;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

public class DirUtilTest extends TestCase {

	//---------------------------------------------------------------------------------------------
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
}
