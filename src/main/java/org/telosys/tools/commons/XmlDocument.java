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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

@Deprecated
public class XmlDocument
{	
	private TelosysToolsLogger _logger = null ;
	
    public XmlDocument() {
		super();
		_logger = null ;
	}
    
    public XmlDocument(TelosysToolsLogger logger ) {
		super();
		_logger = logger ;
	}

    private void log(String s) 
    {
    	if ( _logger != null ) {
    		_logger.log(s);
    	}
    }
    
	private DocumentBuilder getDocumentBuilder() throws TelosysToolsException
    {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null ;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new TelosysToolsException("XML error : Cannot get DocumentBuilder",e);
		}
		return builder ;
    }
    
    public Document load(String sFileName) throws TelosysToolsException
    {
    	if ( sFileName != null )
    	{
        	return load(new File(sFileName));
    	}
    	else
    	{
    		throw new TelosysToolsException("XML error : load(null) ");
    	}
    }

    public Document load(File file) throws TelosysToolsException
    {
    	if ( file != null )
    	{
			DocumentBuilder builder = getDocumentBuilder();		
			Document doc = null;		
			try {
				doc = builder.parse(file);
			} catch (SAXException e) {
				throw new TelosysToolsException("XML error : Cannot parse : SAXException",e);
			} catch (IOException e) {
				throw new TelosysToolsException("XML error : Cannot parse : IOException",e);
			}
			return doc ;
    	}
    	else
    	{
    		throw new TelosysToolsException("XML error : load(null) ");
    	}
    }
    
    public Document load(InputStream is) throws TelosysToolsException
    {
    	if ( is != null )
    	{
			DocumentBuilder builder = getDocumentBuilder();		
			Document doc = null;		
			try {
				doc = builder.parse(is);
			} catch (SAXException e) {
				throw new TelosysToolsException("XML error : Cannot parse : SAXException",e);
			} catch (IOException e) {
				throw new TelosysToolsException("XML error : Cannot parse : IOException",e);
			}
			return doc ;
    	}
    	else
    	{
    		throw new TelosysToolsException("XML error : load(null) ");
    	}
    }
    
    public void save(Document doc, String fileName) throws TelosysToolsException
    {
		log("Xml.save(Document, String)... file name = " + fileName );
    	save ( doc, new StreamResult(fileName));
    }
    
    public void save(Document doc, File file) throws TelosysToolsException
    {
		log("Xml.save(Document, File)... File = " + file );
    	save ( doc, new StreamResult(file));
    }
    
    private void save(Document doc, Result result) throws TelosysToolsException
    {
		log("Xml.save(Document, Result)... ");
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
    		throw new TelosysToolsException("XML error : Cannot save : TransformerException", e);
        }
        catch (TransformerFactoryConfigurationError e) {
    		throw new TelosysToolsException("XML error : Cannot save : TransformerFactoryConfigurationError", e);
        }
    }
    
    //---------------------------------------------------------------------------------------------------
    /**
     * @param node
     * @param attributeName
     * @return
     */
    public String getNodeAttribute( Node node, String attributeName )
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
    public int getNodeAttributeAsInt( Node node, String attributeName ) throws TelosysToolsException
    {
    	String s = getNodeAttribute( node, attributeName );
    	if ( null == s ) {
    		throw new TelosysToolsException("XML error : int attribute '" + attributeName + "' not found" );
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
    public int getNodeAttributeAsInt( Node node, String attributeName, int defaultValue ) throws TelosysToolsException
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
    private int convertToInt(String sAttributeName, String s) throws TelosysToolsException {
    	int i = 0 ;
    	try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
    		throw new TelosysToolsException("XML error : attribute '" + sAttributeName + "' is not an integer", e);
		}
		return i ;
    }
    //---------------------------------------------------------------------------------------------------
    
    /**
     * Creates an empty DOM document
     * @return
     */
    public Document createDomDocument() throws TelosysToolsException
    {
        
        Document doc = null;
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b;
        try {
            b = f.newDocumentBuilder();
            doc = b.newDocument();
        } catch (ParserConfigurationException e) {
            throw new TelosysToolsException("Cannot create DOM document", e);
        }
        
        return doc;
    }
    
    //---------------------------------------------------------------------------------------------------
    /**
     * Creates a DOM document populated with the given XML file
     * @param file
     * @return
     */
    public Document createDomDocument(File file) throws TelosysToolsException
    {
 
        String error = "Cannot parse file " + file.getName() + " !";
        
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        Document doc = createDomDocument();
        try {
            doc = f.newDocumentBuilder().parse(file);
        } catch (SAXException e) {
            throw new TelosysToolsException(error, e);
        } catch (IOException e) {
            throw new TelosysToolsException(error, e);
        } catch (ParserConfigurationException e) {
            throw new TelosysToolsException(error, e);
        }
        return doc;
    }

}
