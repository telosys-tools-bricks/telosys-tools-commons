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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PropUtil {
	
	/**
	 * Private constructor to avoid instance creation
	 */
	private PropUtil() {
	}
	
    private static final int  BUFFER_SIZE  = 2048;
	
	/**
	 * Update the properties file by setting the new value 
	 * @param sFileName
	 * @param sKey
	 * @param sValue
	 * @return
	 */
	public static int update(String sFileName, String sKey, String sValue) 
    {
//		Date now = new Date();
//		SimpleDateFormat ISOformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String sNow = ISOformat.format(now);
//        String sComment = "Property updated by Telosys plugin ( " + sNow + " )" ;
//
//		int iRet = update ( sFileName, sKey, sValue, sComment ) ;
		
		int iRet = update ( sFileName, sKey, sValue, null ) ;
		
//		System.out.println("ret = " + iRet );
		return iRet ;
    }

	/**
	 * Update the properties file by setting the new value 
	 * @param sFileName
	 * @param sKey
	 * @param sNewValue
	 * @param sComment
	 * @return
	 */
	public static int update(String sFileName, String sKey, String sNewValue, String sComment ) 
    {
//		System.out.println("update(" + sFileName + ")");
		if ( sFileName == null )
		{
			throw new IllegalArgumentException("FileName argument is null");
		}
		if ( sKey == null )
		{
			throw new IllegalArgumentException("Key argument is null");
		}
		if ( sNewValue == null )
		{
			throw new IllegalArgumentException("Value argument is null");
		}

		int iChanges = 0 ;
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024*16);
//		byte[] content = null ;

		FileReader fr = getFileReader(sFileName);
//        if ( fr != null )
//        {
//        	//--- 1) Read properties file and update it in memory
//            BufferedReader br = new BufferedReader(fr, BUFFER_SIZE);
//            
//            OutputStreamWriter osw = new OutputStreamWriter(output);
//            BufferedWriter bw = new BufferedWriter(osw);
//            try
//            {
//            	iRet = update(br, sKey, sNewValue, sComment, bw);
//                bw.flush();
//            } catch (IOException e)
//            {
//				MsgBox.error("File '" + sFileName + "' I.O. Exception");
//            } finally
//            {
//                close(br);
//            }
//            close(fr);
//        }
//
        //--- 1) Update the file (the result is in "content"
        try {
			iChanges = update(fr, sKey, sNewValue, sComment, output);
            close(fr);
		} catch (RuntimeException e) {
            close(fr);
            throw e ;
		}
        
        //--- 2) Write the new properties file (if changes)
        if ( iChanges > 0 )
        {
            //content = output.toString();
        	byte[] content = output.toByteArray();
            if ( content.length > 0 )
            {
                writeFileContent(sFileName, content );
            }
            else
            {
                throw new RuntimeException("new content to write is void");
            }
        }
        
//		System.out.println("----------" );
//		System.out.println(content);
//		System.out.println("----------" );
        return iChanges ;
    }
    
	public static String currentDateTime() 
    {
		Date now = new Date();
		SimpleDateFormat ISOformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sNow = ISOformat.format(now);
		return sNow ;
    }
	
	private static void writeFileContent(String sFileName, byte[] content )
	{
		if ( sFileName == null )
		{
			throw new IllegalArgumentException("FileName argument is null");
		}
		if ( content == null )
		{
			throw new IllegalArgumentException("Content argument is null");
		}
        FileOutputStream fos = null ;
        try
        {
            fos = new FileOutputStream(sFileName);
        } catch (FileNotFoundException ex)
        {
            throw new RuntimeException("writeFile : cannot open file.", ex);
        }
        if ( fos != null )
        {
            try
            {
                fos.write(content);
                fos.flush();
                fos.close();
            } catch (IOException ioex)
            {
                throw new RuntimeException("writeFile : write error.", ioex);
            }
        }	
	}
	
    private static int update(FileReader fr, String sKey, String sNewValue, String sComment, ByteArrayOutputStream output) 
    {
		if ( fr == null )
		{
			throw new IllegalArgumentException("FileReader argument is null");
		}

		int iRet = 0 ;
		//--- 1) Read properties file and update it in memory
        BufferedReader br = new BufferedReader(fr, BUFFER_SIZE);
        
        OutputStreamWriter osw = new OutputStreamWriter(output);
        BufferedWriter bw = new BufferedWriter(osw);
        try
        {
        	iRet = update(br, sKey, sNewValue, sComment, bw);
            bw.flush();
	        close(br);
        } catch (IOException e)
        {
	        close(br);
            throw new RuntimeException("IOException", e);
	    }
        return iRet ;
    }
    
    private static int update(BufferedReader br, String sKey, String sNewValue, String sComment, BufferedWriter writer) throws IOException
    {
    	
//		System.out.println("update(BufferedReader)");
    	int iRet = 0 ;
        String sLine;
        String[] parts = null;
        while ((sLine = br.readLine()) != null)
        {
        	sLine = sLine.trim() ;
            if (sLine.length() > 0)
            {
                if ( ( ! isVoid(sLine) ) && ( ! isComment(sLine) ) )
                {
                	parts = StrUtil.split(sLine, '=');
                	if ( parts.length >= 2 )
                	{
                		//System.out.println(" . " + parts[0] + " = " + parts[1] );
                		if ( sKey.equals( parts[0].trim() ) )
                		{
                			//--- KEY found
                			if ( ! sNewValue.trim().equals( parts[1].trim() ) )
                			{
	                			//--- Change the file content
	                			if ( sComment != null )
	                			{
	                                writer.write( "# " + sComment );
	                                writer.newLine();
	                			}
	                            writer.write( "# Update " + currentDateTime() + " ( original property : " + sLine + " )");
	                            writer.newLine();
	                			// property found => change its value
	                    		//System.out.println("  *** REPLACE BY '" + sNewValue+"'" );
	                    		sLine = sKey + " = " + sNewValue ;
	                    		iRet++ ;
                			}
                		}
                	}
                }
            }
            writer.write(sLine);
            writer.newLine();
        }
        return iRet ;
    }
    
    private static boolean isVoid(String sLine)
    {
    	if ( sLine != null )
    	{
            if ( sLine.trim().length() > 0 )
            {
                return false ;
            }
    	}
        return true ;
    }

    private static boolean isComment(String sLine)
    {
    	if ( sLine != null )
    	{
            if (sLine.trim().startsWith("#"))
            {
                return true;
            }
    	}
        return false;
    }

    /**
     * Return a FileReader for the current file name or null if the file doesn't exist
     * @return
     */
    private static FileReader getFileReader( String sFileName )
    {
    	FileReader fr = null ;
    	if ( sFileName != null )
    	{
            try {
				fr = new FileReader(sFileName);
			} catch (FileNotFoundException e) {
				// Not an error // MsgBox.error("File '" + sFileName + "' not found");
				fr = null ;
			}
    	}
    	return fr; 
    }
    
    private static void close(FileReader fr)
    {
    	if ( fr != null )
    	{
        	try {
    			fr.close();
    		} catch (IOException e) {
    			// Nothing todo
    		}
    	}
    }
    
    private static void close(BufferedReader br)
    {
    	if ( br != null )
    	{
        	try {
    			br.close();
    		} catch (IOException e) {
    			// Nothing todo
    		}
    	}
    }

//    public static void main(String args[])
//    {
//    	update("D:/DevPluginEclipse/TelosysTools/workspace/TelosysTools/test/telosys.properties", 
//    			"mapperclass", 
//    			"new.vo.xml." + ConfigDefaults.DEFAULT_XML_MAPPER_CLASS_NAME );
//    }
}

