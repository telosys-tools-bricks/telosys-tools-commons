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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Zip utility 
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

		//--- Check output directory existence
		File folder = new File(outputFolder);
		if (!folder.exists()) {
			if (createFolder) {
				// Create all parent directories 
				DirUtil.createDirectory( folder );
			} else {
				throw new TelosysToolsException("Cannot UnZip : destination dir '" + outputFolder + "' doesn't exist");
			}
		}

		try ( ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile)) ) {
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {

				final String zipEntryName = zipEntry.getName();

				// cut the root folder (e.g.  for "basic-templates-master/aaa/bbb" return "aaa/bbb" ) 
				String entryDestination = cutEntryName(zipEntryName) ;
				if ( entryDestination.length() > 0 ) {
					//--- Install this entry
					// build the destination file name
					File destinationFile = new File(outputFolder + File.separator + entryDestination );
					// unzip ( file or directory )
					if ( zipEntry.isDirectory() ) {
						DirUtil.createDirectory(destinationFile); // v 3.0.0
					}
					else {
						unzipEntry(zis, destinationFile); // extract to file
					}
				}
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();

		} catch (IOException ex) {
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

	/**
	 * Remove the "root directory" for the given path <br>
	 * ( for example: returns "foo/bar" for "root-dir/foo/bar" or "" for "root-dir" )
	 * @param entryName
	 * @return
	 */
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
	
}