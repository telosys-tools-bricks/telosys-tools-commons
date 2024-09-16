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
import java.io.Closeable;
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
    
    /**
     * Close the given stream without throwing exception
     * @param stream
     */
    public static void close(Closeable stream) {
    	if ( stream != null ) {
        	try {
    			stream.close();
    		} catch (IOException e) {
    			// No action 
    		}
    	}
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
     * Copy a file into another one 
     * @param inputFileName
     * @param outputFileName
     * @param createFolder if true creates the destination folder if necessary
     * @throws Exception
     */
    public static void copy(String inputFileName, String outputFileName, boolean createFolder) //throws Exception
    {
        //--- Open input file
    	File inputFile = new File(inputFileName);
    	File outputFile = new File(outputFileName);
    	
        FileInputStream fis = createInputStream(inputFile);
        
        //--- Create output file folder is non existent 
        if ( createFolder ) {
        	createParentFolderIfNecessary(outputFile);        	
        }
    	
        //--- Open output file
        FileOutputStream fos = createOutputStream(outputFile);
        
        //--- Copy and close
        copyAndClose(fis, fos);
    }

    //----------------------------------------------------------------------------------------------------
    /**
     * Copy a file into another one 
     * @param inputFile
     * @param outputFile
     * @param createFolder if true creates the destination folder if necessary
     * @throws Exception
     */
    public static void copy(File inputFile, File outputFile, boolean createFolder) //throws Exception
    {
        //--- Open input file
		FileInputStream fileInputStream = createInputStream(inputFile);
        
    	copyInputStreamToFile(fileInputStream, outputFile, createFolder) ;
    }

    public static void copy(String inputContent, File outputFile, boolean createFolder) //throws Exception
    {
    	//--- The input content is the string
		byte[] bytes = inputContent.getBytes(StandardCharsets.UTF_8);
    	InputStream inputStream = new ByteArrayInputStream(bytes);
    	
    	copyInputStreamToFile(inputStream, outputFile, createFolder) ;
    }
    
    private static void copyInputStreamToFile(InputStream is, File outputFile, boolean createFolder) //throws Exception
    {
        //--- Create output file folder is non existent 
        if ( createFolder ) {
        	createParentFolderIfNecessary(outputFile);
        }
        //--- Open output file
		FileOutputStream fos = createOutputStream(outputFile);
        //--- Copy and close
        copyAndClose( is, fos);
    }
    
    //----------------------------------------------------------------------------------------------------
    /**
     * Copy a file into a directory 
     * @param inputFile 
     * @param directory 
     * @param createFolder if true creates the destination folder if necessary
     * @throws Exception
     */
    public static void copyToDirectory(File inputFile, File directory, boolean createFolder) //throws Exception
    {
    	if ( directory.exists() && ! directory.isDirectory() ) {
   			throw new IllegalArgumentException(directory + " is not a directory");
    	}
    	String outputFileFullPath = FileUtil.buildFilePath(directory.getAbsolutePath(), inputFile.getName());
    	File outputFile = new File(outputFileFullPath);
    	
        //--- Open input file
		InputStream  fis  = createInputStream(inputFile);
		
        //--- Create output file folder is non existent 
        if ( createFolder ) {
        	createParentFolderIfNecessary(outputFile);
        }
    	
        //--- Open output file
		FileOutputStream fos = createOutputStream(outputFile);
        
        //--- Copy and close
        copyAndClose(fis,fos);
    }

    //----------------------------------------------------------------------------------------------------
    /**
     * Copy input URL to destination file 
     * @param  inputFileURL
     * @param  outputFileName
     * @param  createFolder if true creates the destination folder if necessary
     * @throws Exception
     */
    public static void copy(URL inputFileURL, String outputFileName, boolean createFolder) throws Exception
    {
        //--- Open input stream
    	InputStream is = null ;
		try {
			is = inputFileURL.openStream();
		} catch (IOException e) {
            throw new Exception("copy : cannot open input URL " + inputFileURL.toString(), e);
		}
        
        //--- Create output file folder is non existent 
        if ( createFolder ) {
        	createParentFolderIfNecessary(new File(outputFileName));        	
        }		

    	//--- Open output stream
        FileOutputStream fos = createOutputStream(new File(outputFileName));
        //--- Copy and close
        copyAndClose( is, fos);
    }
    
    /**
     * Same as 'copyFileFromMetaInf' but only if the destination file doesn't exist
     * @param filePathInMetaInf  origin file path in "META-INF"
     * @param destFullPath  destination full path
     * @param createFolder  if true creates the destination folder if necessary
     * @return
     * @throws Exception
     */
    public static boolean copyFileFromMetaInfIfNotExist(String filePathInMetaInf, String destFullPath, boolean createFolder) throws Exception {
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
	private static String buildMetaInfPath(String filePathInMetaInf) throws Exception {
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
	public static void copyFileFromMetaInf(String filePathInMetaInf, String destFullPath, boolean createFolder) throws Exception {
		//--- Build the full file name ( e.g. "META-INF/mydir/myfile" )
		String fullFileNameInMetaInf = buildMetaInfPath(filePathInMetaInf) ;

		//--- Get input stream (file in JAR)
		InputStream is = FileUtil.class.getResourceAsStream(fullFileNameInMetaInf);
		if ( is == null ) {
			throw new Exception("File '" + filePathInMetaInf + "' not found in 'META-INF' \n");
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
    public static void createParentFolderIfNecessary( File file) {
    	File parent = file.getParentFile() ;
    	if ( parent != null ) {
    		if ( parent.exists() == false ) {
    			// Creates the directory, including any necessary but nonexistent parent directories.
    			DirUtil.createDirectory( parent ); // v 3.0.0
    		}
    	}
    }
    //----------------------------------------------------------------------------------------------------
    /**
     * Copy the content of the given InputStream to the given OutputStream, then close all streams.
     * @param is
     * @param os
     */
    private static void copyAndClose(InputStream is, OutputStream os)
    {
        //--- Copy and close
        if ( is != null && os != null ) {
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			try {
                while ((len = is.read(buffer)) > 0)
                {
                    os.write(buffer, 0, len);
                }
                is.close();
                os.close();
            } 
			catch (IOException e) {
                throw new TelosysRuntimeException("Cannot copy : IO error", e);
            }
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
	 * @throws Exception
	 * @since 2.0.7
	 */
	public static void copyFolder( File source, File destination, boolean overwrite ) throws Exception {
	 	if ( source.isDirectory() ) {
    		//--- If the destination directory doesn't exist create it
    		if ( ! destination.exists() ) {
    			destination.mkdir();
    		}
    		//--- Get all the directory content
    		for (String file : source.list() ) {
    		   File srcFile = new File(source, file);
    		   File destFile = new File(destination, file);
    		   //--- recursive copy
    		   copyFolder(srcFile,destFile, overwrite);
    		}
 
    	} else {
    		//--- Source is a file
    		if ( destination.exists() && ( overwrite == false ) ) {
    			//--- Do not overwrite !
    			return ;
    		}
    		else {
    			//--- Copy file to file
        		InputStream  inputStream  = createInputStream(source);
				OutputStream outputStream = createOutputStream(destination);
				copyAndClose(inputStream, outputStream);
    		}
    	}
	}
    //----------------------------------------------------------------------------------------------------
	/**
	 * Reads the file content and loads it in a byte array 
	 * @param file
	 * @return
	 * @since 3.0.0
	 */
	public static byte[] read(File file) {
		
		if ( file == null ) {
			throw new IllegalArgumentException("File argument is null");
		}
		
		FileInputStream fileInputStream = createInputStream(file);
		
		byte[] fileContent = new byte[(int) file.length()];
		try {
			fileInputStream.read(fileContent);
			fileInputStream.close();
		} catch (IOException e) {
			throw new TelosysRuntimeException("Cannot read or close file '" + file.getName() + "'", e);
		}
		return fileContent;
	}
}
