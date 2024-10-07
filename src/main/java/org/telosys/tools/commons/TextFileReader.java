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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Generic text file reader <br>
 * 
 * @author L. Guerin
 *
 */
public abstract class TextFileReader {
	
    private static final int BUFFER_SIZE = 2048;
	
    private final String filePath ;
    
    /**
     * Constructor 
	 * @param filePath 
	 */
	protected TextFileReader(String filePath) {
		super();
		if ( filePath == null ) {
			throw new IllegalArgumentException("File path is null");
		}
		this.filePath = filePath;
	}

	/**
	 * Specific line parser
	 * @param line the line to be parsed
	 * @param lines the list where to add the line if selected by the parsing
	 * @return
	 */
	protected abstract void parseLine(String line, List<String> lines) ;


	/**
	 * Check file existence
	 * @return true if the file exists, else false
	 */
	public boolean exists() {
		File file = new File(filePath);
		return file.exists() ;
	}
	
	public String getPath() {
		return filePath ;
	}
	
	/**
	 * Loads the lines selected by the abstract implementation
	 * @return
	 * @throws GeneratorException
	 */
	public List<String> loadLines() {
    	List<String> list = null ;
        FileReader fr = getFileReader();
        BufferedReader br = new BufferedReader(fr, BUFFER_SIZE);
		try {
			list = parse(br);
		} catch (IOException e) {
			close(br);
			throw new RuntimeException("IO exception (file '" + filePath + "') ", e );
		}
		close(br);
        close(fr);
        return list ;
    }
    
    private List<String> parse(BufferedReader br) throws IOException {
    	List<String> lines = new LinkedList<String>();
        String line;
        while ((line = br.readLine()) != null) {
        	parseLine(line, lines);
        }
        return lines ;
    }
    
    /**
     * Return a FileReader for the current file name or null if the file doesn't exist
     * @return
     */
    private FileReader getFileReader() {
    	FileReader fr = null ;
        try {
			fr = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found : " + filePath );
		}
    	return fr; 
    }
    
    private void close(FileReader fr) {
    	if ( fr != null ) {
        	try {
    			fr.close();
    		} catch (IOException e) {
    			// Nothing todo
    		}
    	}
    }
    
    private void close(BufferedReader br) {
    	if ( br != null ) {
        	try {
    			br.close();
    		} catch (IOException e) {
    			// Nothing todo
    		}
    	}
    }
}
