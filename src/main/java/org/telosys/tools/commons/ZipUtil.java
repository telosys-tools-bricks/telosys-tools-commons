/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Zip utility 
 * NB : Work In Progress ( 'unzip' is OK, 'zip' is not yet finished )
 * 
 * @author Laurent GUERIN
 *
 */
public class ZipUtil {

	/**
	 * Private constructor for static class
	 */
	private ZipUtil() {
	}
	
	/**
	 * Unzip the given ZIP file in the output folder, without the root folder part 
	 * @param zipFile
	 * @param outputFolder
	 * @param createFolder
	 * @throws TelosysToolsException
	 */
	public static void unzip(final String zipFile, final String outputFolder,
			final boolean createFolder) throws TelosysToolsException {

		log("UnZip file '" + zipFile + "'");
		log("        in '" + outputFolder + "'");

		//--- Check output directory existence
		File folder = new File(outputFolder);
		if (!folder.exists()) {
			if (createFolder) {
				// Create all parent directories 
				DirUtil.createDirectory( folder );
			} else {
				throw new TelosysToolsException("UnZip error : folder '" + outputFolder + "' doesn't exist");
			}
		}

		try ( ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile)) ) {
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {

				final String zipEntryName = zipEntry.getName();
				log(" . unzip entry '" + zipEntryName + "'");

				// cut the root folder (e.g.  remove "basic-templates-TT207-master" ) 
				String entryDestination = cutEntryName(zipEntryName) ;
				if ( entryDestination.length() > 0 ) {
					//--- Install this entry
					// build the destination file name
					File destinationFile = new File(outputFolder + File.separator + entryDestination );
					// unzip ( file or directory )
					log("   install : " + zipEntryName );
					log("        in : " + destinationFile.getAbsolutePath() );
					if ( zipEntry.isDirectory() ) {
						DirUtil.createDirectory(destinationFile); // v 3.0.0
					}
					else {
						unzipEntry(zis, destinationFile); // extract to file
					}
				}
				else {
					log("   root entry => do not extract");
				}
				
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			log("Done");

		} catch (IOException ex) {
			log("IOException : " + ex.getMessage() );
			throw new TelosysToolsException("UnZip Error (IOException)", ex);
		}
	}
	
	/**
	 * Unzip the given entry (a single file stored in the zip file)
	 * @param zis entry input stream
	 * @param destinationFile the new file to be created
	 * @throws IOException
	 */
	private static void unzipEntry(ZipInputStream zis, File destinationFile) throws IOException {

		// create non existent parent folders (to avoid FileNotFoundException) ???

		byte[] buffer = new byte[1024];
		try ( FileOutputStream fos = new FileOutputStream(destinationFile) ) {
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
		}
	}


	//---------------------------------------------------------------------------------------------
	/**
	 * Zip the given directory
	 * @param directory
	 * @param zipFile
	 * @throws IOException
	 */
	public static void zipDirectory( final File directory, final File zipFile ) throws IOException {
		if ( ! directory.isDirectory() ) {
			throw new IllegalArgumentException("The given file is not a directory");
		}
		//--- Build the list of files
		List<String> fileNames = DirUtil.getDirectoryFiles(directory, true) ;
		List<File> files = new LinkedList<>() ;
		for ( String fileAbsolutePath : fileNames ) {
			files.add( new File(fileAbsolutePath) ) ;
		}
		//--- Zip the files
		zip(files, zipFile, directory);
	}
	
	/**
	 * Zip the given files in the given zip file name
	 * @param files
	 * @param zipFile
	 * @param baseDir
	 * @throws IOException
	 */
	public static void zip( List<File> files, File zipFile, File baseDir ) throws IOException {

		if ( ! baseDir.isDirectory() ) {
			throw new IllegalArgumentException("The base directory is not a directory");
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
		ZipOutputStream zout = new ZipOutputStream(fileOutputStream);
		
		//--- Zip each given file
		for ( File file : files ) {
			if ( file.isDirectory() ) {
				// TODO : recursive 
				zip(file, zout, baseDir);
			}
			else {
				zip(file, zout, baseDir);
			}
		}
		
		zout.close();
	}

	/**
	 * Zip the given file in the given ZipOutputStream
	 * @param file
	 * @param zout
	 * @param baseDir
	 * @throws IOException
	 */
	private static void zip(File file, ZipOutputStream zout, File baseDir) throws IOException {
		
		String baseDirAbsolutePath = baseDir.getCanonicalPath();
		log("baseDirAbsolutePath = " + baseDirAbsolutePath );
		
		byte[] buffer = new byte[1024];
		
		try ( FileInputStream fileInputStream = new FileInputStream(file) ) {
			//--- Step 1 : create a zip entry 
			String fileAbsolutePath = file.getCanonicalPath();
			log("fileAbsolutePath = " + fileAbsolutePath );
			String fileEntryName = fileAbsolutePath.substring(baseDirAbsolutePath.length()+1);
			log("fileEntryName = " + fileEntryName );
			
			ZipEntry zipEntry = new ZipEntry(fileEntryName);
			zout.putNextEntry(zipEntry);
			//--- Step 2 : zip the file
			int length ;
			while( ( length = fileInputStream.read(buffer) ) > 0 ) {
				zout.write(buffer, 0, length);
			}
			zout.closeEntry();
		}
	}
	
	//---------------------------------------------------------------------------------------------
	protected static String cutEntryName(String entryName) {
		
        final int pos = getFirstSeparator(entryName) ;
        if ( pos < 0 ) {
        	// separator not found => nothing after
            return "" ;
        }
        else {
        	// separator found => cut before
            return entryName.substring(pos + 1);
        }
	}
	private static int getFirstSeparator(String entryName) {
		
		for ( int i = 0 ; i < entryName.length() ; i++ ) {
			char c = entryName.charAt(i);
			if ( c == '/' || c == '\\' ) {
				return i ;
			}
		}
		return -1 ; // Not found
	}
	
	//---------------------------------------------------------------------------------------------
	private static void log(String msg) {
		// Log here if necessary
	}
}