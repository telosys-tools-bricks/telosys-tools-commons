package org.telosys.tools.tests.commons;

import java.io.File;

import junit.framework.TestCase;

import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.XmlFileUtil;
import org.telosys.tools.commons.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XmlFileUtilTest extends TestCase {

	public void print(Element element) {
		System.out.println("<" + element.getNodeName()+">");
	}
	
	public void testLoad() {
		System.out.println("--- testLoad");
		File file = FileUtil.getFileByClassPath("/dbcfg/databases-test1.dbcfg");
		Document document = XmlFileUtil.load(file);
		
		
		Element root = document.getDocumentElement();
		print(root);
		
        NamedNodeMap attributes = root.getAttributes();
        assertNotNull(attributes);
        //--- Find the attribute 
        Node attrib = attributes.getNamedItem("maxId");
        assertNotNull(attrib);
        System.out.println( ". " + attrib.getNodeName() + " = " + attrib.getNodeValue() ) ;

	}

	public void testSave() {
		System.out.println("--- testSave");
		
//        Document document = null;
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        try {
//        	DocumentBuilder documentBuilder = factory.newDocumentBuilder();
//            document = documentBuilder.newDocument();
//        } catch (ParserConfigurationException e) {
//            throw new RuntimeException("Cannot create DOM document", e);
//        }
        Document document = XmlUtil.createDomDocument();

        Element rootElement = document.createElement("root");
        document.appendChild(rootElement);
        
		Element sub = document.createElement("sub");
		rootElement.appendChild(sub);        
        
		String s = XmlFileUtil.saveInString(document);
		System.out.println("RESULT : ");
		System.out.println(s);

		File file = FileUtil.getFileByClassPath("/dbcfg/databases-test1-out.dbcfg");
		System.out.println("File : " + file.toString());
		if ( file.exists() ) {
			System.out.println("File exists " );
			file.delete();
		}
		else {
			System.out.println("File doesn't exist" );
		}
		XmlFileUtil.save(document, file);
		
	}

}
