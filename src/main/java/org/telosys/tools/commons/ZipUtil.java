/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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

public class ZipUtil {

	//---------------------------------------------------------------------------------------------
	/**
	 * Unzip the given ZIP file in the output folder, without the root folder part 
	 * @param zipFile
	 * @param outputFolder
	 * @param createFolder
	 */
	public static void unzip(final String zipFile, final String outputFolder,
			final boolean createFolder) throws Exception {

		log("UnZip file '" + zipFile + "'");
		log("        in '" + outputFolder + "'");

		//--- Check output directory existence
		File folder = new File(outputFolder);
		if (!folder.exists()) {
			if (createFolder) {
				// folder.mkdirs(); // creates all parent directories 
				DirUtil.createDirectory( folder ); // v 3.0.0
			} else {
				throw new Exception("UnZip error : folder '" + outputFolder + "' doesn't exist");
			}
		}

		try {

			//--- Read each entry in the zip file
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {

				final String zipEntryName = zipEntry.getName();
				log(" . unzip entry '" + zipEntryName + "'");

				// cut the root folder ( remove "basic-templates-TT207-master" ) 
				String entryDestination = cutEntryName( zipEntryName ) ;
				if ( entryDestination.length() > 0 ) {
					//--- Install this entry
					// build the destination file name
					File destinationFile = new File(outputFolder + File.separator + entryDestination );
					// unzip ( file or directory )
					log("   install : " + zipEntryName );
					log("        in : " + destinationFile.getAbsolutePath() );
					if ( zipEntry.isDirectory() ) {
						//destinationFile.mkdirs(); // create directory (including parents)
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
			zis.close();

			log("Done");

		} catch (IOException ex) {
			log("IOException : " + ex.getMessage() );
			throw new Exception("UnZip Error (IOException)", ex);
		}
	}
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Zip the given directory
	 * @param directory
	 * @param zipFile
	 * @throws Exception
	 */
	public static void zipDirectory( final File directory, final File zipFile ) throws Exception {
		if ( directory.isDirectory() == false ) {
			throw new IllegalArgumentException("The given file is not a directory");
		}
		//--- Build the list of files
		List<String> fileNames = DirUtil.getDirectoryFiles(directory, true) ;
		List<File> files = new LinkedList<File>() ;
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
	 * @throws Exception
	 */
	public static void zip( List<File> files, File zipFile, File baseDir ) throws Exception {

		if ( baseDir.isDirectory() == false ) {
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
	 * @throws Exception
	 */
	private static void zip(final File file, final ZipOutputStream zout, File baseDir ) throws Exception {
		
		String baseDirAbsolutePath = baseDir.getCanonicalPath();
		System.out.println("baseDirAbsolutePath = " + baseDirAbsolutePath );
		
		byte[] buffer = new byte[1024];
		
		FileInputStream fileInputStream = new FileInputStream(file);
		//--- Step 1 : create a zip entry 
		String fileAbsolutePath = file.getCanonicalPath();
		System.out.println("fileAbsolutePath = " + fileAbsolutePath );
		String fileEntryName = fileAbsolutePath.substring(baseDirAbsolutePath.length()+1);
		System.out.println("fileEntryName = " + fileEntryName );
		
		ZipEntry zipEntry = new ZipEntry(fileEntryName);
		zout.putNextEntry(zipEntry);
		//--- Step 2 : zip the file
		int length ;
		while( ( length = fileInputStream.read(buffer) ) > 0 ) {
			zout.write(buffer, 0, length);
		}
		zout.closeEntry();
		fileInputStream.close();
	}
	
	//---------------------------------------------------------------------------------------------
	private static int getFirstSeparator(final String entryName) {
		
		for ( int i = 0 ; i < entryName.length() ; i++ ) {
			char c = entryName.charAt(i);
			if ( c == '/' || c == '\\' ) {
				return i ;
			}
		}
		return -1 ; // Not found
	}
	//---------------------------------------------------------------------------------------------
	public static String cutEntryName(final String entryName) {
		
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
	
	//---------------------------------------------------------------------------------------------
	/**
	 * Unzip the given entry (a single file stored in the zip file)
	 * @param zis entry input stream
	 * @param newFile the new file to be created
	 * @throws IOException
	 */
	private static void unzipEntry(ZipInputStream zis, File newFile) throws IOException {

		// create non existent parent folders (to avoid FileNotFoundException) ???

		byte[] buffer = new byte[1024];
		FileOutputStream fos = new FileOutputStream(newFile);
		int len;
		while ((len = zis.read(buffer)) > 0) {
			fos.write(buffer, 0, len);
		}
		fos.close();
	}

//	//---------------------------------------------------------------------------------------------
//	/**
//	 * Return the substring located AFTER the first occurrence of the given separator. <br>
//	 * . substringAfter("abcd",   "b")   : "cd"  <br>
//	 * . substringAfter("aaa/bb", "/") : "bb"  <br>
//	 * 
//	 * @param str
//	 * @param separator
//	 * @return
//	 */
//	public static String substringAfter(final String str, final String separator) {
//        if ( str == null ) {
//            return null;
//        }
//        if ( str.length() == 0 ) {
//            return str;
//        }
//        if (separator == null) {
//        	// no separator => nothing before
//            return "";
//        }
//        final int pos = str.indexOf(separator); 
//        if (pos < 0 ) {
//        	// separator not found => nothing before
//            return "";
//        }
//        else {
//        	// separator found => cut before
//            return str.substring(pos + separator.length());
//        }
//    }
	//---------------------------------------------------------------------------------------------
	private static void log(String msg) {
		//System.out.println(msg);
	}
}