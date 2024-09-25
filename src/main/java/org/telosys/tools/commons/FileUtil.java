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
package org.telosys.tools.commons ;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.telosys.tools.commons.exception.TelosysRuntimeException;

/**
 * Utility class for FILE operations (static methods)
 * 
 * @author Laurent GUERIN 
 * 
 */
public class FileUtil {
	
    private static final int     BUFFER_SIZE = 4*1024 ; // 4 kb  
    
    /**
     * Private constructor
     */
    private FileUtil() {
    }
    
    private static FileInputStream createInputStream(File inputFile) {
    	try {
			return new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			throw new TelosysRuntimeException("Cannot open input file " + inputFile.getName() 
				+ "(file not found)", e);
		}
    }

    private static FileOutputStream createOutputStream(File outputFile) {
	    try {
	    	return new FileOutputStream(outputFile);
	    } 
	    catch (FileNotFoundException e) {
			throw new TelosysRuntimeException("Cannot open output file " + outputFile.getName() 
				+ "(file not found)", e);
	    }
    }
    //----------------------------------------------------------------------------------------------------
    /**
     * Build a file path with the given directory path and the given file name.<br>
     * The starting/ending separators "/" and "\" are managed in order to buil a correct path.
     *  
     * @param dir  the directory path( eg "x:/aaa/bb" or "x:/aa/bb/" )
     * @param file the file name ( eg "foo.txt" or "/foo.txt" ) 
     * @return
     */
    public static String buildFilePath(String dir, String file) {
    	String s1 = dir ;
    	if ( dir.endsWith("/") || dir.endsWith("\\") ) {
    		s1 = dir.substring(0, dir.length() - 1 );
    	}
    	
    	String s2 = file ;
    	if ( file.startsWith("/") || file.startsWith("\\") ) {
    		s2 = file.substring(1);
    	}
    	
		return s1 + "/" + s2 ;
	}
    
    //----------------------------------------------------------------------------------------------------
    /**
     * Copy the given input data in a destination file
     * @param inputData
     * @param outputFile
     * @param createFolder
     */
    public static long copyDataToFile(String inputData, File outputFile, boolean createFolder) {
    	//--- The input content is the string
		byte[] bytes = inputData.getBytes(StandardCharsets.UTF_8);
    	InputStream inputStream = new ByteArrayInputStream(bytes);
    	
    	return copyInputStreamToFile(inputStream, outputFile, createFolder) ;
    }

    /**
     * Copy a file into another one 
     * @param inputFileName
     * @param outputFileName
     * @param createFolder
     */
    public static long copyFileToFile(String inputFileName, String outputFileName, boolean createFolder) {
    	File inputFile = new File(inputFileName);
    	File outputFile = new File(outputFileName);
    	return copyInputStreamToFile(createInputStream(inputFile), outputFile, createFolder) ;
    }

    //----------------------------------------------------------------------------------------------------
    /**
     * Copy a file into another one 
     * @param inputFile
     * @param outputFile
     * @param createFolder if true creates the destination folder if necessary
     * @throws Exception
     */
    public static long copyFileToFile(File inputFile, File outputFile, boolean createFolder) {
    	return copyInputStreamToFile(createInputStream(inputFile), outputFile, createFolder) ;
    }

    /**
     * Copy input URL to destination file 
     * @param  inputFileURL
     * @param  outputFileName
     * @param  createFolder if true creates the destination folder if necessary
     * @throws TelosysToolsException
     */
    public static long copyFileToFile(URL inputFileURL, String outputFileName, boolean createFolder) throws TelosysToolsException {
    	InputStream is = null ;
		try {
			is = inputFileURL.openStream();
		} catch (IOException e) {
            throw new TelosysToolsException("copy : cannot open input URL " + inputFileURL.toString(), e);
		}
        return copyInputStreamToFile(is, new File(outputFileName), createFolder);
    }
    
    
    private static long copyInputStreamToFile(InputStream is, File outputFile, boolean createFolder) {
        //--- Create output file folder is non existent 
        if ( createFolder ) {
        	createParentFolderIfNecessary(outputFile);
        }
        //--- Open output file
		FileOutputStream fos = createOutputStream(outputFile);
        //--- Copy and close
        return copyAndClose( is, fos);
    }
    
    //----------------------------------------------------------------------------------------------------
    /**
     * Copy a file in the given directory 
     * @param inputFile
     * @param directory
     * @param createFolder
     * @return the output file in directory
     */
    public static File copyFileInDirectory(File inputFile, File directory, boolean createFolder) {
    	if ( ! directory.isDirectory() ) {
   			throw new IllegalArgumentException(directory + " is not a directory");
    	}
    	String outputFileFullPath = FileUtil.buildFilePath(directory.getAbsolutePath(), inputFile.getName());
    	File outputFile = new File(outputFileFullPath);
    	
        //--- Open input file
		InputStream  fis  = createInputStream(inputFile);
        copyInputStreamToFile(fis, outputFile, createFolder);
        return outputFile;
    }

    
    /**
     * Same as 'copyFileFromMetaInf' but only if the destination file doesn't exist
     * @param filePathInMetaInf  origin file path in "META-INF"
     * @param destFullPath  destination full path
     * @param createFolder  if true creates the destination folder if necessary
     * @return
     * @throws TelosysToolsException
     */
    public static boolean copyFileFromMetaInfIfNotExist(String filePathInMetaInf, String destFullPath, boolean createFolder) throws TelosysToolsException {
		File destFile = new File (destFullPath) ;
		if ( destFile.exists() ) {
			return false ; // Not copied
		}
		else {
			copyFileFromMetaInf(filePathInMetaInf, destFullPath, createFolder) ;
			return true ; // Copied
		}
	}
	
	/**
	 * Builds the path with the "META-INF" prefix
	 * @param filePathInMetaInf
	 * @return
	 * @throws Exception
	 */
	private static String buildMetaInfPath(String filePathInMetaInf) {
		return FileUtil.buildFilePath("/META-INF/", filePathInMetaInf) ;
	}
	
	/**
	 * Copy a file from a resource accessible via the ClassPath <br>
	 * The file is supposed to be located in the standard resource folder "META-INF" <br>
	 *  
	 * @param filePathInMetaInf origin file path in "META-INF"
	 * @param destFullPath destination full path
     * @param createFolder if true creates the destination folder if necessary
	 * @throws Exception
	 */
	public static void copyFileFromMetaInf(String filePathInMetaInf, String destFullPath, boolean createFolder) throws TelosysToolsException {
		//--- Build the full file name ( e.g. "META-INF/mydir/myfile" )
		String fullFileNameInMetaInf = buildMetaInfPath(filePathInMetaInf) ;

		//--- Get input stream (file in JAR)
		InputStream is = FileUtil.class.getResourceAsStream(fullFileNameInMetaInf);
		if ( is == null ) {
			throw new TelosysToolsException("File '" + filePathInMetaInf + "' not found in 'META-INF' \n");
		}
		
        //--- Create output file folder is non existent 
        if ( createFolder ) {
        	createParentFolderIfNecessary(new File(destFullPath));        	
        }
    			
		//--- Open output stream
        FileOutputStream fos = createOutputStream(new File(destFullPath));
        //--- Copy 
		copyAndClose(is, fos);
	}
    
    //----------------------------------------------------------------------------------------------------
    /**
     * Creates the parent folder for the given file if it doesn't exist
     * @param file 
     */
    public static boolean createParentFolderIfNecessary(File file) {
	    if ( file == null ) {
	    	throw new IllegalArgumentException("File argument is null");
	    }
    	File parent = file.getParentFile() ;
    	if ( parent != null  &&  ! parent.exists() ) {
			// Creates the directory, including any necessary but nonexistent parent directories.
			DirUtil.createDirectory( parent ); 
			return true;
    	}
    	else {
        	return false;
    	}
    }
    //----------------------------------------------------------------------------------------------------
    /**
     * Copy the content of the given InputStream to the given OutputStream, then close all streams.
     * @param is
     * @param os
     */
    private static long copyAndClose(InputStream is, OutputStream os) {
        //--- Copy and close
        if ( is != null && os != null ) {
        	long size = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			try {
                while ((len = is.read(buffer)) > 0)
                {
                    os.write(buffer, 0, len);
                    size = size + len;
                }
                is.close();
                os.close();
                return size;
            } 
			catch (IOException e) {
                throw new TelosysRuntimeException("Cannot copy : IO error", e);
            }
		}
        else {
            throw new TelosysRuntimeException("Cannot copy : InputStream or OutputStream is null");
        }
    }
    
    //----------------------------------------------------------------------------------------------------
	/**
	 * Search the given file (or folder) using the ClassPath
	 * @param fileName file name or folder name
	 * @return
	 */
	public static File getFileByClassPath(String fileName) {
		URL url = FileUtil.class.getResource(fileName);
		if ( url != null ) {
			URI uri = null ;
			try {
				uri = url.toURI();
			} catch (URISyntaxException e) {
				throw new TelosysRuntimeException("Cannot convert URL to URI (file '" + fileName + "')");
			}
			return new File(uri);
		}
		else {
			throw new TelosysRuntimeException("File '" + fileName + "' not found");
		}
	}
    
    //----------------------------------------------------------------------------------------------------
	/**
	 * Copy the source folder to the destination folder with all its content (recursively)
	 * @param source
	 * @param destination
	 * @param overwrite
	 * @since 2.0.7
	 */
	public static int copyFolderToFolder(File sourceFolder, File destinationFolder, boolean overwriteFiles ) {
    	if ( ! sourceFolder.isDirectory() ) {
   			throw new IllegalArgumentException(sourceFolder + " is not a directory");
    	}
    	if ( ! destinationFolder.exists() ) {
    		destinationFolder.mkdir();
    	}
       	if ( ! destinationFolder.isDirectory() ) {
   			throw new IllegalArgumentException(destinationFolder + " is not a directory");
    	}
       	return recursiveCopy(sourceFolder, destinationFolder, overwriteFiles);
	}
	private static int recursiveCopy( File source, File destination, boolean overwrite ) {
	 	if ( source.isDirectory() ) { // Source is a directory
    		//--- If the destination directory doesn't exist create it
    		if ( ! destination.exists() ) {
    			destination.mkdir();
    		}
    		if ( ! destination.isDirectory() ) {
    			throw new TelosysRuntimeException("copyFolder: destination '" + destination.getName() + "' is not a directory");
    		}
    		//--- Get all the directory content
    		int count = 0 ;
    		for (String file : source.list() ) {
    		   File srcFile = new File(source, file);
    		   File destFile = new File(destination, file);
    		   //--- recursive copy => add count for each sub level
    		   count = count + recursiveCopy(srcFile, destFile, overwrite);
    		}
    		return count;
    	} else { 
    		// Source is just a file => just copy it 
    		if ( !destination.exists() || ( destination.exists() && overwrite ) ) {
    			//--- Copy file to file with folder creation if necessary
    			copyFileToFile(source, destination, true); 
        		return 1; // 1 file copied
    		}
    		else {
    			return 0; // no file copied
    		}
    	}
	}

}
