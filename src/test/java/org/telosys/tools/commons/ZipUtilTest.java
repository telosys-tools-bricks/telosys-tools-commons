package org.telosys.tools.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import junit.env.telosys.tools.commons.TestsEnv;

import org.junit.Test;

public class ZipUtilTest { // extends TestCase {

	@Test
	public void testCutEntryName() {
		System.out.println("Getting repositories... ");

		String s ;
		s = cutEntryName("scala-templates-TT204/toto") ;
		assertEquals("toto", s);
		
		s = cutEntryName("scala-templates-TT204") ;
		assertEquals("", s);

		s = cutEntryName("scala-templates-TT204/") ;
		assertEquals("", s);

		s = cutEntryName("scala-templates-TT204/foo/bar") ;
		assertEquals("foo/bar", s);
	}
	
	private String cutEntryName ( String s ) {
		System.out.println("cut '" + s + "'");
		String r = ZipUtil.cutEntryName(s) ;
		System.out.println("result : '" + r + "'");
		return r ;
	}

	@Test
	public void testUnZipInExistingFolder() throws Exception {
		System.out.println("Unzip file in an existing folder... ");
//		ZipUtil.unzip("D:/tmp/telosys-tools-tests/TelosysTools/downloads/persistence-jpa-TT210-R2.zip", 
//				"D:/tmp/telosys-tools-tests/TelosysTools/templates/persistence-jpa-TT210-R2", true);
		String zipFileName = TestsEnv.getTestFileAbsolutePath("zip/basic-templates-TT210.zip");
		String destinationFolder = TestsEnv.getTmpExistingFolderFullPath("unzip/basic-templates-TT210");
		System.out.println("Zip file    = " + zipFileName );
		System.out.println("Dest folder = " + destinationFolder );
		ZipUtil.unzip(zipFileName, destinationFolder, false );
	}

	@Test
	public void testUnZipInPotentialNonExistingFolder1() throws Exception {
		System.out.println("Unzip file in a potential non existing folder... ");
		String zipFileName = TestsEnv.getTestFileAbsolutePath("zip/basic-templates-TT210.zip");
		String destinationFolder = TestsEnv.getTmpFileOrFolderFullPath("unzip/inex-dir");
		System.out.println("Zip file    = " + zipFileName );
		System.out.println("Dest folder = " + destinationFolder );
		ZipUtil.unzip(zipFileName, destinationFolder, true );
	}

	@Test
	public void testUnZipInPotentialNonExistingFolder2() throws Exception {
		System.out.println("Unzip file in a potential non existing folder... ");
		String zipFileName = TestsEnv.getTestFileAbsolutePath("zip/basic-templates-TT210.zip");
		String destinationFolder = TestsEnv.getTmpFileOrFolderFullPath("unzip/inex-dir2");
		System.out.println("Zip file    = " + zipFileName );
		System.out.println("Dest folder = " + destinationFolder );
		Exception error = null ;
		try {
			ZipUtil.unzip(zipFileName, destinationFolder, false );
		} catch (Exception e) {
			error = e ;
			System.out.println("Expected excception : " + e.getMessage() );
		}
		assertNotNull(error);
	}

//	public void testUnZip2() throws Exception {
//		System.out.println("Unzip file... ");
//		Exception error = null ;
//		try {
//			ZipUtil.unzip("D:/tmp/telosys-tools-tests/TelosysTools/downloads/persistence-jpa-TT210-R2.zip", 
//					"D:/tmp/telosys-tools-tests/TelosysTools/templates/inex", false);
//		} catch (Exception e) {
//			error = e ;
//		}
//		System.out.println("Expected error : " + error.getMessage() );
//		assertNotNull(error);
//	}
	@Test
	public void testZipFiles() throws Exception {
		List<File> files = new LinkedList<File>();
		files.add( new File("src/test/resources/telosys-tools.cfg") );
		files.add( new File("src/test/resources/dbcfg/databases.dbcfg") );
			
		File zipFile = TestsEnv.getTmpFile("archive1.zip");
		zipFile.delete();
		ZipUtil.zip(files, zipFile, new File ("src/test/resources/") );
		
		assertTrue(zipFile.exists());
		assertTrue(zipFile.isFile());
	}
	
//	@Test
//	public void testZipZeroFiles() throws Exception {
//		List<File> files = new LinkedList<File>();
//			
//		File zipFile = TestsEnv.getTmpFile("archive2.zip");
//		zipFile.delete();
//		ZipUtil.zip(files, zipFile, new File ("src/test/resources/") );
//		
//		assertTrue(zipFile.exists());
//		assertTrue(zipFile.isFile());
//	}	
}
