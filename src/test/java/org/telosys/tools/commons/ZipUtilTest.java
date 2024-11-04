package org.telosys.tools.commons;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import junit.env.telosys.tools.commons.TestsEnv;

public class ZipUtilTest {

	private static final String ZIP_FILE = "zip/basic-templates-TT210.zip" ;
			
	private void print(String s) {
		System.out.println(s);
	}
	private void printFileAndDir(String zipFileName, String destinationFolder) {
		print(" Zip file    = " + zipFileName );
		print(" Dest folder = " + destinationFolder );
	}

	@Test
	public void testCutEntryName() {
		assertEquals("toto",    ZipUtil.cutEntryRootDir("scala-templates-TT204/toto") );
		assertEquals("",        ZipUtil.cutEntryRootDir("scala-templates-TT204") );
		assertEquals("",        ZipUtil.cutEntryRootDir("scala-templates-TT204/") );
		assertEquals("foo/bar", ZipUtil.cutEntryRootDir("scala-templates-TT204/foo/bar") );
	}

	@Test
	public void testUnZipInExistingFolder() throws TelosysToolsException {
		print("Unzip file in an existing folder with 'create folder' = false");
		String zipFileName = TestsEnv.getTestFileAbsolutePath(ZIP_FILE);
		String destinationFolder = TestsEnv.getTmpExistingFolderFullPath("unzip/basic-templates-TT210");
		printFileAndDir(zipFileName, destinationFolder);
		
		ZipUtil.unzip(zipFileName, destinationFolder, false );
		
		File destDir = new File(destinationFolder);
		assertTrue(destDir.isDirectory());
		assertEquals(12, destDir.listFiles().length); 
		assertTrue((new File(destinationFolder, "templates.cfg")).exists());
		assertTrue((new File(destinationFolder, "pom_xml.vm")).exists());
	}

	@Test
	public void testUnZipInNonExistingFolderWithCreateFolderTrue() throws TelosysToolsException {
		print("Unzip file in a non existing folder  with 'create folder' = true... ");
		String zipFileName = TestsEnv.getTestFileAbsolutePath(ZIP_FILE);
		String destinationFolder = TestsEnv.getTmpFileOrFolderFullPath("unzip/inex-dir1");
		printFileAndDir(zipFileName, destinationFolder);
		
		ZipUtil.unzip(zipFileName, destinationFolder, true );

		File destDir = new File(destinationFolder);
		assertTrue(destDir.isDirectory());
		assertEquals(12, destDir.listFiles().length); 
		assertTrue((new File(destinationFolder, "templates.cfg")).exists());
		assertTrue((new File(destinationFolder, "pom_xml.vm")).exists());
	}

	@Test
	public void testUnZipInNonExistingFolderWithCreateFolderFalse() {
		print("Unzip file in a non existing folder with 'create folder' = false ... ");
		String zipFileName = TestsEnv.getTestFileAbsolutePath(ZIP_FILE);
		String destinationFolder = TestsEnv.getTmpFileOrFolderFullPath("unzip/inex-dir2");
		printFileAndDir(zipFileName, destinationFolder);
		
		Exception exception = null ;
		try {
			ZipUtil.unzip(zipFileName, destinationFolder, false );
			fail("Exception expected");
		} catch (TelosysToolsException e) {
			exception = e ;
			print(" Expected exception : " + e.getMessage() );
		}
		assertNotNull(exception);
	}

}
