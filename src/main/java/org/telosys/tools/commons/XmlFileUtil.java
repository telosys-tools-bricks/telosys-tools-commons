/**
 *  Copyright (C) 2008-2014  Telosys project org. ( http://www.telosys.org/ )
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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlFileUtil
{	
	private static DocumentBuilder getDocumentBuilder() 
    {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null ;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("XML error : Cannot get DocumentBuilder",e);
		}
		return builder ;
    }
    
    public static Document load(String sFileName) 
    {
    	if ( sFileName != null )
    	{
        	return load(new File(sFileName));
    	}
    	else
    	{
    		throw new RuntimeException("XML error : load(null) ");
    	}
    }

    public static Document load(File file) 
    {
    	if ( file != null )
    	{
			DocumentBuilder builder = getDocumentBuilder();		
			Document doc = null;		
			try {
				doc = builder.parse(file);
			} catch (SAXException e) {
				throw new RuntimeException("XML error : Cannot parse : SAXException",e);
			} catch (IOException e) {
				throw new RuntimeException("XML error : Cannot parse : IOException",e);
			}
			return doc ;
    	}
    	else
    	{
    		throw new IllegalArgumentException("XML error : load(null) ");
    	}
    }
    
    public static Document load(InputStream is) 
    {
    	if ( is != null )
    	{
			DocumentBuilder builder = getDocumentBuilder();		
			Document doc = null;		
			try {
				doc = builder.parse(is);
			} catch (SAXException e) {
				throw new RuntimeException("XML error : Cannot parse : SAXException",e);
			} catch (IOException e) {
				throw new RuntimeException("XML error : Cannot parse : IOException",e);
			}
			return doc ;
    	}
    	else
    	{
    		throw new IllegalArgumentException("XML error : load(null) ");
    	}
    }
    
    public static void save(Document doc, String fileName) 
    {
    	save ( doc, new StreamResult(fileName));
    }
    
    public static void save(Document doc, File file) 
    {
    	save ( doc, new StreamResult(file));
    }
    
    public static String saveInString(Document doc) 
    {
    	StreamResult streamResult = new StreamResult( new StringWriter() ) ;
    	save ( doc, streamResult );
    	return streamResult.getWriter().toString();
    }
    
    
    private static void save(Document doc, Result result) 
    {
        //--- Write the XML document in XML file
        try {
            Source source = new DOMSource(doc);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            //--- Transform the DOM document into XML file 
            transformer.transform(source, result);
        }
        catch (TransformerException e) {
    		throw new RuntimeException("XML error : Cannot save : TransformerException", e);
        }
        catch (TransformerFactoryConfigurationError e) {
    		throw new RuntimeException("XML error : Cannot save : TransformerFactoryConfigurationError", e);
        }
    }
    
}
