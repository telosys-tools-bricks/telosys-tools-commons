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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XmlUtil {

    private static final int HIGHEST_SPECIAL = '>';
    
    private static char[][] xmlRepresentation = new char[HIGHEST_SPECIAL + 1][];
    static {
        xmlRepresentation['&']  = "&amp;".toCharArray();
        xmlRepresentation['<']  = "&lt;".toCharArray();
        xmlRepresentation['>']  = "&gt;".toCharArray();
        xmlRepresentation['"']  = "&#034;".toCharArray();
        xmlRepresentation['\''] = "&#039;".toCharArray();
    }
    
    //-----------------------------------------------------------------------------------
    /** 
     * Private constructor (no instantiation) 
     */
    private XmlUtil()
    {
    }
    
    //-----------------------------------------------------------------------------------	
    /**
     * Converts the given original string to an XML string.<br>
     * Replaces "&", "<", ">", "double quote" and "single quote" 
     * @param originalString
     * @return 
     */
    public static String escapeXml(String originalString) {
    	
    	if ( null == originalString ) {
    		return "" ;
    	}
    	
        int start = 0;
        int length = originalString.length();
        char[] arrayBuffer = originalString.toCharArray();
        StringBuffer escapedStringBuffer = null;

        for (int i = 0; i < length; i++) {
            char c = arrayBuffer[i];
            if (c <= HIGHEST_SPECIAL) {
                char[] escaped = xmlRepresentation[c];
                if (escaped != null) {
                    // new StringBuffer to build the xml string
                    if (start == 0) {
                        escapedStringBuffer = new StringBuffer(length + 5);
                    }
                    // add unescaped portion
                    if (start < i) {
                        escapedStringBuffer.append(arrayBuffer,start,i-start);
                    }
                    start = i + 1;
                    // add escaped xml
                    escapedStringBuffer.append(escaped);
                }
            }
        }
        // escaping not required
        if (start == 0) {
            return originalString;
        }
        // add rest of unescaped portion
        if (start < length) {
            escapedStringBuffer.append(arrayBuffer,start,length-start);
        }
        return escapedStringBuffer.toString();
    }
    
    /**
     * Creates an empty DOM document
     * @return
     */
    public static Document createDomDocument() 
    {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
        	DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            document = documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Cannot create DOM document", e);
        }
        return document;
    }

    //---------------------------------------------------------------------------------------------------
    private static int convertToInt(String sAttributeName, String s) {
    	int i = 0 ;
    	try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
    		throw new RuntimeException("XML error : attribute '" + sAttributeName + "' is not an integer", e);
		}
		return i ;
    }
    //---------------------------------------------------------------------------------------------------
    /**
     * @param node
     * @param attributeName
     * @return
     */
    public static String getNodeAttribute( Node node, String attributeName )
    {
        String sRetValue = null ;
        NamedNodeMap attributes = node.getAttributes();
        if ( attributes != null )
        {
            //--- Find the attribute 
            Node attrib = attributes.getNamedItem(attributeName);
            if ( attrib != null )
            {
                //--- Get the attribute value
                sRetValue = attrib.getNodeValue() ;
            }
        }
        return sRetValue ;
    }
    //---------------------------------------------------------------------------------------------------
    /**
     * @param node
     * @param attributeName
     * @return
     * @throws TelosysToolsException
     */
    public static int getNodeAttributeAsInt( Node node, String attributeName ) 
    {
    	String s = getNodeAttribute( node, attributeName );
    	if ( null == s ) {
    		throw new RuntimeException("XML error : int attribute '" + attributeName + "' not found" );
    	}
    	else {
    		return convertToInt(attributeName, s);
    	}
    }
    //---------------------------------------------------------------------------------------------------
    /**
     * @param node
     * @param attributeName
     * @param defaultValue
     * @return
     * @throws TelosysToolsException
     */
    public static int getNodeAttributeAsInt( Node node, String attributeName, int defaultValue )
    {
    	String s = getNodeAttribute( node, attributeName );
    	if ( null == s ) {
    		return defaultValue;
    	}
    	else {
    		return convertToInt(attributeName, s);
    	}
    }
    //---------------------------------------------------------------------------------------------------
	
}
