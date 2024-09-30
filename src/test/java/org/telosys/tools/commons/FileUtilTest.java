package org.telosys.tools.commons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.telosys.tools.commons.exception.TelosysRuntimeException;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

public class FileUtilTest extends TestCase {

	private void print(String s) {
		System.out.println(s);
	}
	private void print(File file) {
		print("File or folder : " + file);
		print(" . getAbsolutePath() : " + file.getAbsolutePath() );
		print(" . getPath()         : " + file.getPath() );
		print(" . getParent()       : " + file.getParent() );
		print(" . getName()         : " + file.getName() );
		print(" . exists()          : " + file.exists() );
	}
	
	public void testCopyWithFileName() {
		
		String fileName = "/testfilecopy/origin/file1.txt" ;
		print("Searching file '" + fileName + "' by classpath..." );
		File file = FileUtil.getFileByClassPath(fileName);
		print(file);
		assertTrue ( file.exists()) ;
		
		// Original file
		String originalFullFileName = file.getAbsolutePath();
		print("Original file    : " + originalFullFileName );

		// Destination file in inexistent folder 
		String destFullFileName = FileUtil.buildFilePath(file.getParentFile().getParent()+"/newfolder", "newfile1.txt");
		print("Destination file : " + destFullFileName );
		
		FileUtil.copyFileToFile(originalFullFileName, destFullFileName, true);
	}
	
	public void testCopyWithFileObject() {
		File sourceFile = TestsEnv.getTestFile("file1.txt");
		File destinationFile = TestsEnv.getTmpFile("file1-bis.txt");
		assertTrue ( sourceFile.exists()) ;
		FileUtil.copyFileToFile(sourceFile, destinationFile, true);
	}
	
	public void testCopyWithFileObject2() {
		File sourceFile = TestsEnv.getTestFile("file1.txt");
		assertTrue ( sourceFile.exists()) ;
		File destinationFile = TestsEnv.getTmpFile("foo/bar/file1-bis.txt");		
		try {
			// Copy to non existent destination, without 'create folder' flag
			FileUtil.copyFileToFile(sourceFile, destinationFile, false);
			fail("Expected exception not thrown");
		} catch (Exception e) {
			assertTrue(e instanceof TelosysRuntimeException);
		}
	}
	
	public void testCopyDataToFile() throws IOException  {
		
		File destinationFile = TestsEnv.getTmpFile("foo/bar2/file-copy-from-string.txt");
		print("Copy string content to '" + destinationFile.getAbsolutePath() + "'" );
		
		String content = "This is the first line \n" +
				"line 2 aaaaaaa\n" +
				"line 3 bbbbbb " ;
			
		long n = FileUtil.copyDataToFile(content, destinationFile, true);
		assertTrue(destinationFile.exists());
		assertEquals(content.length(), n);
		assertEquals(content.length(), Files.size(Paths.get(destinationFile.getAbsolutePath())));
	}

	public void testCopyFileInDirectory() throws IOException {
		
		String sourceFileName = TestsEnv.getTestFileAbsolutePath("file1.txt");
		String destinationDirectoryName = TestsEnv.getTmpExistingFolderFullPath("mydir/dest");
		print("Copy from file '" + sourceFileName + "' to '" + destinationDirectoryName + "'" );

		File sourceFile = TestsEnv.getTestFile("file1.txt");
		File destinationDirectory = new File(destinationDirectoryName);
		print("Copy from file '" + sourceFile.getAbsolutePath() + "' to '" + destinationDirectory.getAbsolutePath() + "'" );
		assertTrue ( sourceFile.exists()) ;
		
		File destFile = FileUtil.copyFileInDirectory(sourceFile, destinationDirectory, true);
		assertTrue ( destFile.exists()) ;
		assertEquals(sourceFile.getName(), destFile.getName());
		assertEquals( Files.size(Paths.get(sourceFile.getAbsolutePath())),  Files.size(Paths.get(destFile.getAbsolutePath()))  );
	}
	
	public void testFolderCopy() {
		File originFolder = TestsEnv.getTestFolder("resources-origin");
		assertTrue (originFolder.exists()) ;
		File destinationFolder = TestsEnv.getTmpExistingFolder("copy-dest");
		assertTrue (destinationFolder.exists()) ;
		int n = FileUtil.copyFolderToFolder(originFolder, destinationFolder, true) ;
		assertEquals(6, n); // nb files copied expected
		assertTrue(destinationFolder.isDirectory());
		assertTrue((new File(destinationFolder, "foo/bar")).isDirectory());
		// do not test void directory (ex: "foo/bar-void" ) as not pushed in Git and not exist in CI
		assertTrue((new File(destinationFolder, "foo/bar/file2.txt")).exists());
	}
	
	public void testCopyFileFromMetaInf() throws TelosysToolsException, IOException {
		
		Exception exception = null ;
		try {
			testCopyFileFromMetaInf("resource-file0.txt");
		} catch (Exception e) {
			exception = e ;
			print("Expected exception : " + e.getMessage() );
			assertNotNull(exception);
		}
		
		testCopyFileFromMetaInf("resource-file1.txt"); 

		testCopyFileFromMetaInf("dir2/resource-file2.txt"); 
	}
	
	private void testCopyFileFromMetaInf(String srcFileName) throws TelosysToolsException, IOException {

		print("-----");
		print("Test with '" +srcFileName+"'");
		final String destFolderName = "/copied-from-metainf" ;
		String destFileFullPath   = TestsEnv.getTmpFileOrFolderFullPath(destFolderName + "/" + srcFileName );
		String destFolderFullPath = TestsEnv.getTmpFileOrFolderFullPath(destFolderName);

		//--- Initial state : no directory
		DirUtil.deleteDirectory( new File(destFolderFullPath) ) ;
		
		//--- 1rst try to copy : folder doesn't exist => exception expected
		print("Copying from META-INF : " + srcFileName + " to " + destFileFullPath);
//		Exception exception = null ;
//		try {
//			FileUtil.copyFileFromMetaInf(srcFileName, destFileFullPath, false);
//		} catch (Exception e) {
//			exception = e ;
//			print("Expected exception : " + e.getMessage() );
//		}
//		assertNotNull(exception);
		try {
			FileUtil.copyFileFromMetaInf(srcFileName, destFileFullPath, false);
			fail("Expected exception not thrown");
		} catch (Exception e) {
			assertNotNull(e);
		}		
		
		//--- 2nd try to copy : folder doesn't exist but flag is 'create folder' => no exception expected
		FileUtil.copyFileFromMetaInf(srcFileName, destFileFullPath, true);
		assertFileExists(destFileFullPath);
		
		//--- 3nd try to copy : copy same file again
		FileUtil.copyFileFromMetaInf(srcFileName, destFileFullPath, false);
		assertFileExists(destFileFullPath);

		//--- copyFileFromMetaInfIfNotExist
		assertFalse( FileUtil.copyFileFromMetaInfIfNotExist(srcFileName, destFileFullPath, false) ); // exists => not copied
		File destFile = new File(destFileFullPath);
		assertTrue(destFile.exists());
		Files.delete(destFile.toPath());
		assertFalse(destFile.exists());		
		assertTrue( FileUtil.copyFileFromMetaInfIfNotExist(srcFileName, destFileFullPath, false) ); // not exists => copied
		assertTrue(destFile.exists());		
	}

	//------------------------------------------------------------------------------------------
	// Utilities
	//------------------------------------------------------------------------------------------
	private void assertFileExists(String fileName)  {
		File file = new File(fileName);
		print(file);
		assertTrue(file.exists()) ;
	}
	
	public void testBuildFilePath1()  {
		String dir = "D:\\workspaces\\myapp/TelosysTools/templates/front-springmvc-TT210-R2/resources" ;
		String file = "/src/main/webapp" ;
		String s = FileUtil.buildFilePath(dir, file);
		print("s = " + s );
		assertEquals(dir+file, s);
	}
	
	public void testBuildFilePath2a()  {
		String dir = "D:\\aaa\\bbb/ccc/ddd/" ;
		String file = "/x/y/zzz.txt" ;
		String s = FileUtil.buildFilePath(dir, file);
		print("s = " + s );
		assertEquals("D:\\aaa\\bbb/ccc/ddd/x/y/zzz.txt", s);
	}
	public void testBuildFilePath2b()  {
		String dir = "D:\\aaa\\bbb/ccc" ;
		String file = "/x1/yyy/zzz.txt" ;
		String s = FileUtil.buildFilePath(dir, file);
		print("s = " + s );
		assertEquals("D:\\aaa\\bbb/ccc/x1/yyy/zzz.txt", s);
	}

	public void testBuildFilePath3()  {
		String dir = "D:\\aaa\\bbb/ccc/ddd" ;
		String file = "/xxx/yyy/zzz.txt" ;
		String s = FileUtil.buildFilePath(dir, file);
		print("s = " + s );
		assertEquals(dir+file, s);
	}
	public void testBuildFilePath4()  {
		String dir = "D:\\aaa\\bbb/ccc/ddd" ;
		String file = "\\xxx/yyy/zzz.txt" ;
		String s = FileUtil.buildFilePath(dir, file);
		print("s = " + s );
		assertEquals(dir+"/xxx/yyy/zzz.txt", s);
	}
	public void testBuildFilePath5()  {
		String dir = "aaaa" ;
		String file = "foo.txt" ;
		String s = FileUtil.buildFilePath(dir, file);
		print("s = " + s );
		assertEquals("aaaa/foo.txt", s);
	}

	public void testCreateParentFolderIfNecessaryNullArg()  {
		try {
			FileUtil.createParentFolderIfNecessary(null);
			fail("Expected exception not thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	public void testCreateParentFolderIfNecessaryCreated()  {
		TestsEnv.deleteTmpFileOrFolder("aa/bb/cc");
		File file = TestsEnv.getTmpFileOrFolder("aa/bb/cc/foo.txt");
		print("File : " + file );
		assertFalse(file.getParentFile().exists());
		assertTrue(FileUtil.createParentFolderIfNecessary(file)); 
		assertTrue(file.getParentFile().exists());
	}
	public void testCreateParentFolderIfNecessaryNotCreated()  {
		File file = TestsEnv.getTmpFileOrFolder("aa/bb/cc/foo.txt");
		DirUtil.createDirectory(file.getParentFile());
		print("File : " + file );
		assertTrue(file.getParentFile().exists());
		assertFalse(FileUtil.createParentFolderIfNecessary(file)); 
		assertTrue(file.getParentFile().exists());
	}
	
	public void testReadWriteDelete() {
		File destinationFolder = TestsEnv.getTmpExistingFolder("write-test-dest");
		assertTrue (destinationFolder.exists()) ;
		File file = new File(destinationFolder, "foo.txt");
		
		// write #1
		String content = "Abcdefgh" ;
		FileUtil.writeString(file, content);
		assertTrue(file.isFile());
		assertEquals(content, FileUtil.readString(file)); 
		
		// write #2
		content = "Xyz123456Azer" ;
		FileUtil.writeString(file, content);
		assertTrue(file.isFile());
		assertEquals(content, FileUtil.readString(file)); 
		
		FileUtil.delete(file);
		assertFalse(file.exists());
		
		try {
			FileUtil.readString(file); // No file => exception
			fail();
		} catch (TelosysRuntimeException e) {
			// Exception expected 
		}
	}
}
