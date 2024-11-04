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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Zip utility 
 * 
 * @author Laurent GUERIN
 *
 */
public class ZipUtil {

	private static final int MAX_ENTRIES   =      10000; // 10 KB - Secutity threshold to avoid "Zip Bomb" risk
	private static final int MAX_FILE_SIZE = 1000000000; //  1 GB - Secutity threshold to avoid "Zip Bomb" risk
	
	/**
	 * Private constructor for static class
	 */
	private ZipUtil() {
	}
	
	/**
	 * Unzip the given ZIP file in the given output dir <br>
	 * The root part of each file path is removed ( eg 'my-bundle-master/aaa/bbb' -> 'aaa/bbb' ) 
	 * @param zipFilePath
	 * @param outputDirPath
	 * @param createOutputDir
	 * @throws TelosysToolsException
	 */
	public static void unzip(String zipFilePath, String outputDirPath, boolean createOutputDir) throws TelosysToolsException {

		//--- Check output directory existence
		File folder = new File(outputDirPath);
		if (!folder.exists()) {
			if (createOutputDir) {
				// Create all parent directories 
				DirUtil.createDirectory( folder );
			} else {
				throw new TelosysToolsException("Cannot UnZip : destination dir '" + outputDirPath + "' doesn't exist");
			}
		}

		File file = new File(zipFilePath);
		if ( ! file.isFile() ) {
			throw new TelosysToolsException("Cannot UnZip : '" + zipFilePath + "' is not a file");
		}
		
		int numberOfEntries = 0 ;
		try ( ZipFile zipFile = new ZipFile(file) ) {
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			
			while (zipEntries.hasMoreElements()) {
				numberOfEntries++;
				if ( numberOfEntries > MAX_ENTRIES ) {
					throw new TelosysToolsException("Too much entries in zip file ('zip bomb' risk, max="+ MAX_ENTRIES+")"); 
				}
				ZipEntry zipEntry = zipEntries.nextElement();

				String zipEntryName = zipEntry.getName();

				// cut the root dir (e.g. for "basic-templates-master/aaa/bbb"
				// return "aaa/bbb" )
				String entryDestination = cutEntryRootDir(zipEntryName);
				if (entryDestination.length() > 0) {
					// --- Install this entry
					// build the destination file name
					File destinationFile = new File(outputDirPath + File.separator + entryDestination);
					// unzip ( file or directory )
					if (zipEntry.isDirectory()) {
						DirUtil.createDirectory(destinationFile); // v 3.0.0
					} else {
						InputStream is = zipFile.getInputStream(zipEntry);
						unzipEntry(is, destinationFile); // extract to file
					}
				}
			}
		}
		catch ( IOException ex) {
			throw new TelosysToolsException("UnZip Error (IOException)", ex); 
		}
	}
	private static void unzipEntry(InputStream is, File destinationFile) throws IOException, TelosysToolsException {
		int outputFileSize = 0;
        byte[] buffer = new byte[4096];
        try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
            int nBytes ;
            while ((nBytes = is.read(buffer)) > 0) {
            	outputFileSize += nBytes;
            	if ( outputFileSize > MAX_FILE_SIZE ) {
            		throw new TelosysToolsException("Output file size too large ('zip bomb' risk, max="+ MAX_FILE_SIZE+" bytes)"); 
            	}
                fos.write(buffer, 0, nBytes);
            }
        }
	}
	

	/**
	 * Remove the "root directory" for the given path <br>
	 * ( for example: returns "foo/bar" for "root-dir/foo/bar" or "" for "root-dir" )
	 * @param entryName
	 * @return
	 */
	protected static String cutEntryRootDir(String entryName) {
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
	
}