package org.telosys.tools.commons;

import java.io.File;
import java.io.IOException;

import org.telosys.tools.commons.exception.TelosysRuntimeException;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

public class FileUtilTest extends TestCase {

	private void print(String s) {
		System.out.println(s);
	}
	
	public void testCopyWithFileName() {
		
		String fileName = "/testfilecopy/origin/file1.txt" ;
		print("Searching file '" + fileName + "' by classpath..." );
		File file = FileUtil.getFileByClassPath(fileName);
		if ( file.exists() ) {
			print("File found : " + file);
			print(" . getAbsolutePath()  : " + file.getAbsolutePath() );
			print(" . getName()          : " + file.getName() );
			print(" . getPath()          : " + file.getPath() );
			print(" . getParent()        : " + file.getParent() );
		}
		else {
			print("File not found " );
		}
		assertTrue ( file.exists()) ;
		
		// Original file
		String originalFullFileName = file.getAbsolutePath();
		print("Original file    : " + originalFullFileName );

		// Destination file in inexistent folder 
		String destFullFileName = FileUtil.buildFilePath(file.getParentFile().getParent()+"/newfolder", "newfile1.txt");
		print("Destination file : " + destFullFileName );
		
		FileUtil.copy(originalFullFileName, destFullFileName, true);
	}
	
	public void testCopyWithFileObject() {
		
		File sourceFile = TestsEnv.getTestFile("file1.txt");
		File destinationFile = TestsEnv.getTmpFile("file1-bis.txt");
		print("Copy from file '" + sourceFile.getAbsolutePath() + "' to '" + destinationFile.getAbsolutePath() + "'" );
		assertTrue ( sourceFile.exists()) ;
		
		FileUtil.copy(sourceFile, destinationFile, true);
	}
	
	public void testCopyWithFileObject2() {
		
		File sourceFile = TestsEnv.getTestFile("file1.txt");
		File destinationFile = TestsEnv.getTmpFile("foo/bar/file1-bis.txt");
		print("Copy from file '" + sourceFile.getAbsolutePath() + "' to '" + destinationFile.getAbsolutePath() + "'" );
		assertTrue ( sourceFile.exists()) ;
		
		TelosysRuntimeException exception = null ;
		try {
			// Copy to non existent destination, without 'create folder' flag
			FileUtil.copy(sourceFile, destinationFile, false);
		} catch (TelosysRuntimeException e) {
			exception = e ;
		}
		assertNotNull(exception);
	}
	
	public void testCopyStringToFile() {
		
		File destinationFile = TestsEnv.getTmpFile("foo/bar2/file-copy-from-string.txt");
		print("Copy string content to '" + destinationFile.getAbsolutePath() + "'" );
		
		String content = "This is the first line \n" +
				"line 2 aaaaaaa\n" +
				"line 3 bbbbbb " ;
			
		FileUtil.copy(content, destinationFile, true);
		assertTrue(destinationFile.exists());
	}

	public void testCopyFileToDirectory() {
		
		String sourceFileName = TestsEnv.getTestFileAbsolutePath("file1.txt");
		String destinationDirectoryName = TestsEnv.getTmpExistingFolderFullPath("mydir/dest");
		print("Copy from file '" + sourceFileName + "' to '" + destinationDirectoryName + "'" );

		File sourceFile = TestsEnv.getTestFile("file1.txt");
		File destinationDirectory = new File(destinationDirectoryName);
		print("Copy from file '" + sourceFile.getAbsolutePath() + "' to '" + destinationDirectory.getAbsolutePath() + "'" );
		assertTrue ( sourceFile.exists()) ;
		
		FileUtil.copyToDirectory(sourceFile, destinationDirectory, true);
	}
	
	public void testFolderCopy() throws IOException, Exception {
		
		String folderName = "/testfilecopy" ;
		print("Searching folder '" + folderName + "' by classpath..." );
		File folder = FileUtil.getFileByClassPath(folderName);
		if ( folder.exists() ) {
			print("Folder found : " + folder);
			print(" . getAbsolutePath()  : " + folder.getAbsolutePath() );
			print(" . getCanonicalPath() : " + folder.getCanonicalPath() );
			print(" . getName()          : " + folder.getName() );
			print(" . getPath()          : " + folder.getPath() );
			print(" . getParent()        : " + folder.getParent() );
		}
		else {
			print("Folder not found " );
		}
		assertTrue ( folder.exists()) ;
		
		for ( String fileName : folder.list() ) {
			print(" . " + fileName );
		}
	
		for ( File file : folder.listFiles() ) {
			print(" . " + file );
			if ( "origin".equals( file.getName() ) ) {
				print("'origin' folder found.");
				File originFolder = file ;
				
				File destinationFolder = new File(folder.getAbsolutePath(), "dest");
				FileUtil.copyFolder(originFolder, destinationFolder, false) ;
			}
		}
	}
	
	public void testCopyFileFromMetaInf() throws Exception {
		
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
	
	private void testCopyFileFromMetaInf(String srcFileName) throws Exception {

		print("-----");
		print("Test with '" +srcFileName+"'");
		final String destFolderName = "/copied-from-metainf" ;
		String destFileFullPath   = TestsEnv.getTmpFileOrFolderFullPath(destFolderName + "/" + srcFileName );
		String destFolderFullPath = TestsEnv.getTmpFileOrFolderFullPath(destFolderName);

		//--- Initial state : no directory
		DirUtil.deleteDirectory( new File(destFolderFullPath) ) ;
		
		//--- 1rst try to copy : folder doesn't exist => exception expected
		print("Copying from META-INF : " + srcFileName + " to " + destFileFullPath);
		Exception exception = null ;
		try {
			FileUtil.copyFileFromMetaInf(srcFileName, destFileFullPath, false);
		} catch (Exception e) {
			exception = e ;
			print("Expected exception : " + e.getMessage() );
		}
		assertNotNull(exception);
		
		//--- 2nd try to copy : folder doesn't exist but flag is 'create folder' => no exception expected
		FileUtil.copyFileFromMetaInf(srcFileName, destFileFullPath, true);
		checkFileExistence(destFileFullPath);
		
		//--- 3nd try to copy : copy same file again
		FileUtil.copyFileFromMetaInf(srcFileName, destFileFullPath, false);
		checkFileExistence(destFileFullPath);
	}

	//------------------------------------------------------------------------------------------
	// Utilities
	//------------------------------------------------------------------------------------------
	private void checkFileExistence(String fileName)  {
		File file = new File(fileName);
		if ( file.exists() ) {
			print("File found : " + file);
			print(" . getAbsolutePath()  : " + file.getAbsolutePath() );
			print(" . getName()          : " + file.getName() );
			print(" . getPath()          : " + file.getPath() );
			print(" . getParent()        : " + file.getParent() );
		}
		else {
			print("File not found " );
		}
		assertTrue ( file.exists()) ;
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
		String file = "/xxx/yyy/zzz.txt" ;
		String s = FileUtil.buildFilePath(dir, file);
		print("s = " + s );
		assertEquals(dir+"xxx/yyy/zzz.txt", s);
	}
	public void testBuildFilePath2b()  {
		String dir = "D:\\aaa\\bbb/ccc" ;
		String file = "/xxx/yyy/zzz.txt" ;
		String s = FileUtil.buildFilePath(dir, file);
		print("s = " + s );
		assertEquals("D:\\aaa\\bbb/ccc/xxx/yyy/zzz.txt", s);
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


	public void testReadNull()  {
		Exception exception = null ;
		try {
			FileUtil.read(null);
		} catch (Exception e) {
			print("Exception : " + e.getMessage());
			exception = e ;
		}
		assertNotNull(exception);
	}
	
	public void testReadFileNotFound()  {
		Exception exception = null ;
		try {
			FileUtil.read(new File("xxx/yyy/zzz/toto.txt"));
		} catch (Exception e) {
			print("Exception : " + e.getMessage());
			exception = e ;
		}
		assertNotNull(exception);
	}
	
	public void testRead1()  {
		String fileName = "files/file1.txt" ;
		print("read file " + fileName);
		File file = TestsEnv.getTestFile(fileName);
		print("file.getParent() : " + file.getParent() );
		print("file.getName()   : " + file.getName() );
		byte[] content = new byte[0];
		try {
			content = FileUtil.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		printFileContent(content);
		assertEquals(file.length(), content.length);
	}
	
	public void testRead2()  {
		String fileName = "files/file2.txt" ;
		print("read file " + fileName);
		File file = TestsEnv.getTestFile(fileName);
		byte[] content = new byte[0];
		try {
			content = FileUtil.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		printFileContent(content);
		assertEquals(file.length(), content.length);
	}
	
	private void printFileContent(byte[] content)  {
		print("File content :");
		for ( byte b : content ) {
			System.out.print((char)b);
		}
	}
}
