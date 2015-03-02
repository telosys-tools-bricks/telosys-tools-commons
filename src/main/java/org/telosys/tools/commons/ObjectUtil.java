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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ObjectUtil {
	
	/**
	 * Private constructor to avoid instance creation
	 */
	private ObjectUtil(){}
	
    private static void close(Closeable stream) {
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
     * Copies a file into another one 
     * @param inputFileName
     * @param outputFileName
     * @param createFolder if true creates the destination folder if necessary
     * @throws Exception
     */
    public static <T> T deepCopy(T originalObject) 
    {
    	Object clone = null ;
    	ObjectOutputStream oos = null;
        ObjectInputStream  ois = null;
        try {
        	//--- 1) Serialize the object in memory 
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			oos = new ObjectOutputStream(baos);
			// serialize and pass the object
			oos.writeObject(originalObject); // recursively traverses the object's graph
			oos.flush();
        	//--- 2) Deserialize the object from memory 
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray()); 
			ois = new ObjectInputStream(bais);
			// return the new object
			//@SuppressWarnings("unchecked")
			//T clone = (T) ois.readObject(); 
			clone = ois.readObject(); 
			//return clone ;
		} catch (IOException e1) {
			throw new RuntimeException("IOException in deepCopy", e1);
		} catch (ClassNotFoundException e2) {
			throw new RuntimeException("ClassNotFoundException in deepCopy", e2);
		}
        finally {
        	close(oos);
        	close(ois);
        }
        
        @SuppressWarnings("unchecked")
        T typedClone = (T) clone ;
        
        return typedClone ;
    }
}
