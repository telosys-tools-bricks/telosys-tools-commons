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
package org.telosys.tools.commons ;

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


public class FileUtil {
	
    private static final int BUFFER_SIZE = 4*1024 ; // 4 kb   
    
    public static void close(Closeable stream) {
    	if ( stream != null ) {
        	try {
    			stream.close();
    		} catch (IOException e) {
    			// No action 
    		}
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
    	if ( dir.endsWith("/") || dir.endsWith("\\") )
    	{
    		s1 = dir.substring(0, dir.length() - 1 );
    	}
    	
    	String s2 = file ;
    	if ( file.startsWith("/") || file.startsWith("\\") )
    	{
    		s2 = file.substring(1);
    	}
    	
		return s1 + "/" + s2 ;
	}
    
    //----------------------------------------------------------------------------------------------------
    /**
     * Copies a file into another one 
     * @param inputFileName
     * @param outputFileName
     * @param createFolder if true creates the destination folder if necessary
     * @throws Exception
     */
    public static void copy(String inputFileName, String outputFileName, boolean createFolder) throws Exception
    {
        //--- Open input file
		FileInputStream fis = null;
        try {
            fis = new FileInputStream(inputFileName);
        } 
        catch (FileNotFoundException ex) {
            throw new Exception("copy : cannot open input file.", ex);
        }
        
        //--- Create output file folder is non existent 
        if ( createFolder ) {
        	//createFolderIfNecessary(outputFileName);
        	createParentFolderIfNecessary(new File(outputFileName));        	
        }
    	
        //--- Open output file
		FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputFileName);
        } 
        catch (FileNotFoundException ex) {
        	close(fis);
            throw new Exception("copy : cannot open output file.", ex);
        }
        
        //--- Copy and close
        copyAndClose( fis, fos);
    }

    //----------------------------------------------------------------------------------------------------
    /**
     * Copies a file into another one 
     * @param inputFile
     * @param outputFile
     * @param createFolder if true creates the destination folder if necessary
     * @throws Exception
     */
    public static void copy(File inputFile, File outputFile, boolean createFolder) throws Exception
    {
        //--- Open input file
		FileInputStream fis = new FileInputStream(inputFile);
        
        //--- Create output file folder is non existent 
        if ( createFolder ) {
        	createParentFolderIfNecessary(outputFile);
        }
    	
        //--- Open output file
		FileOutputStream fos = new FileOutputStream(outputFile);
        
        //--- Copy and close
        copyAndClose( fis, fos);
    }

    //----------------------------------------------------------------------------------------------------
    /**
     * Copies a file into a directory 
     * @param inputFile 
     * @param directory 
     * @param createFolder if true creates the destination folder if necessary
     * @throws Exception
     */
    public static void copyToDirectory(File inputFile, File directory, boolean createFolder) throws Exception
    {
    	if ( directory.exists() ) {
    		if ( ! directory.isDirectory() ) {
    			throw new Exception(directory + " is not a directory");
    		}
    	}
    	String outputFileFullPath = FileUtil.buildFilePath(directory.getAbsolutePath(), inputFile.getName());
    	File outputFile = new File(outputFileFullPath);
    	
        //--- Open input file
		FileInputStream fis = new FileInputStream(inputFile);
        
        //--- Create output file folder is non existent 
        if ( createFolder ) {
        	createParentFolderIfNecessary(outputFile);
        }
    	
        //--- Open output file
		FileOutputStream fos = new FileOutputStream(outputFile);
        
        //--- Copy and close
        copyAndClose( fis, fos);
    }

    //----------------------------------------------------------------------------------------------------
    /**
     * Copies input URL to destination file 
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
        	//createFolderIfNecessary(outputFileName);
        	createParentFolderIfNecessary(new File(outputFileName));        	
        }		

    	//--- Open output stream
		FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(outputFileName);
        } catch (FileNotFoundException ex)
        {
            throw new Exception("copy : cannot open output file " + outputFileName, ex);
        }
        
        //--- Copy and close
        copyAndClose( is, fos);
    }
    
    //----------------------------------------------------------------------------------------------------
    /**
     * Creates the parent folder for the given file if it doesn't exist
     * @param file 
     */
    private static void createParentFolderIfNecessary( File file) {
    	//File file = new File(fullFileName) ;
    	File parent = file.getParentFile() ;
    	if ( parent != null ) {
    		if ( parent.exists() == false ) {
    			// Creates the directory, including any necessary but nonexistent parent directories.
    			// parent.mkdirs() ; 
    			DirUtil.createDirectory( parent ); // v 3.0.0
    		}
    	}
    }
    //----------------------------------------------------------------------------------------------------
    /**
     * Copy the content of the given InputStream to the given OutputStream, then close all streams.
     * @param is
     * @param os
     * @throws Exception
     */
    private static void copyAndClose(InputStream is, OutputStream os) throws Exception
    {
        //--- Copy and close
        if ( is != null && os != null )
        {
			byte buffer[] = new byte[BUFFER_SIZE];
			int len = 0;
			
			try
            {
                while ((len = is.read(buffer)) > 0)
                {
                    os.write(buffer, 0, len);
                }
                is.close();
                os.close();
            } catch (IOException ioex)
            {
                throw new Exception("copy : IO error.", ioex);
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
				throw new RuntimeException("Cannot convert URL to URI (file '" + fileName + "')");
			}
			return new File(uri);
		}
		else {
			throw new RuntimeException("File '" + fileName + "' not found");
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
		//System.out.println("Copy " + source + " --> " + destination);
	 	if ( source.isDirectory() ) {
	 		 
    		//--- If the destination directory doesn't exist create it
    		if ( ! destination.exists() ) {
    			//System.out.println(" - MkDir " + destination + " ...");
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
    			//System.out.println(" - Copy to file " + destination + " ...");
        		try {
    				InputStream  inputStream  = new FileInputStream(source);
    				OutputStream outputStream = new FileOutputStream(destination);
    				copyAndClose(inputStream, outputStream);
    			} catch (FileNotFoundException e) {
    				throw new Exception("File not found", e);
    			}
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
	public static byte[] read( File file ) throws Exception {
		
		if ( file == null ) {
			throw new Exception ("File argument is null");
		}
		
		FileInputStream fileInputStream ;
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new Exception("File '" + file.getName() + "' not found", e);
		}
		
		
		byte[] fileContent = new byte[(int) file.length()];
		try {
			fileInputStream.read(fileContent);
			fileInputStream.close();
		} catch (IOException e) {
			throw new Exception("IOException : cannot read or close file  '" + file.getName() + "' ", e);
		}
		return fileContent;
	}
}
