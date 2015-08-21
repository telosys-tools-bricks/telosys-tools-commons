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
package org.telosys.tools.commons.dbcfg;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.XmlFileUtil;
import org.telosys.tools.commons.XmlUtil;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DbConfigManager {

	public final static String USER     = "user";
	public final static String PASSWORD = "password";

	private final File file ;

	/**
	 * Constructor for a specific 'dbcfg' file
	 * @param file
	 */
	public DbConfigManager(File file) {
		super();
		if ( file != null ) {
			this.file = file;
		}
		else {
			throw new IllegalArgumentException("File is null");
		}
	}

	/**
	 * Constructor for the standard 'dbcfg' file (defined in the given TelosysToolsCfg )
	 * @param telosysToolsCfg
	 */
	public DbConfigManager( TelosysToolsCfg telosysToolsCfg) {
		super();
		if ( telosysToolsCfg != null ) {
			String dbcfgFileName = telosysToolsCfg.getDatabasesDbCfgFileAbsolutePath();
			this.file = new File(dbcfgFileName);
		}
		else {
			throw new IllegalArgumentException("TelosysToolsCfg is null");
		}
	}

	public DatabasesConfigurations load() throws TelosysToolsException {
		
		DatabasesConfigurations databasesConfigurations = new DatabasesConfigurations();
		
		Document document = XmlFileUtil.load( file );
		
        //--- Root element "<databases>"
        Element root = document.getDocumentElement();
        if (root == null)
        {
        	return databasesConfigurations ;
        }
        
        //--- Root element "<databases>" attributes
        databasesConfigurations.setDatabaseDefaultId(
        		XmlUtil.getNodeAttributeAsInt(root, ConstXML.DATABASES_DEFAULT_ID_ATTRIBUTE, 0) );
        
        databasesConfigurations.setDatabaseMaxId(
        		XmlUtil.getNodeAttributeAsInt(root, ConstXML.DATABASES_MAX_ID_ATTRIBUTE, 0) );
        
        //--- List of "<db>" elements
        NodeList dbList = root.getElementsByTagName(ConstXML.DB_ELEMENT);
        if (dbList == null)
        {
        	return databasesConfigurations ;
        }

        
        //--- For each database node in the XML file
        for ( int i = 0 ; i < dbList.getLength() ; i++ )
        {
        	//--- Parse the Database Node
            Node dbNode = dbList.item(i);
            DatabaseConfiguration databaseConfiguration = xmlNodeToDatabaseConfiguration(dbNode) ;
            databasesConfigurations.storeDatabaseConfiguration(databaseConfiguration);
        }
        
        return databasesConfigurations ;
	}

	public void save(DatabasesConfigurations databasesConfigurations) throws TelosysToolsException {

		Document document = XmlUtil.createDomDocument();
		
		//--- XML root element
		Element rootElement = document.createElement(ConstXML.DATABASES_ROOT_ELEMENT);
		rootElement.setAttribute(ConstXML.DATABASES_MAX_ID_ATTRIBUTE,     ""+databasesConfigurations.getDatabaseMaxId() );
		rootElement.setAttribute(ConstXML.DATABASES_DEFAULT_ID_ATTRIBUTE, ""+databasesConfigurations.getDatabaseDefaultId() );
		document.appendChild(rootElement);		
		
		//--- XML "db" elements
		List<DatabaseConfiguration> list = databasesConfigurations.getDatabaseConfigurationsList();
		for ( DatabaseConfiguration databaseConfiguration : list ) {
			
			//--- Create Element
			Element dbElement = document.createElement(ConstXML.DB_ELEMENT);
			//--- Populate Element
			databaseConfigurationToXmlElement(databaseConfiguration, document, dbElement);
			//--- Add Element in root
			rootElement.appendChild(dbElement);
		}
		
		//--- Save in file
		XmlFileUtil.save( document, file );
		
	}
	
	private DatabaseConfiguration xmlNodeToDatabaseConfiguration(Node dbNode) throws TelosysToolsException {
		

        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
        
        databaseConfiguration.setDatabaseId    ( XmlUtil.getNodeAttributeAsInt(dbNode, ConstXML.DB_ID_ATTRIBUTE));
        databaseConfiguration.setDatabaseName  ( XmlUtil.getNodeAttribute(dbNode, ConstXML.DB_NAME_ATTRIBUTE) );
        databaseConfiguration.setJdbcUrl       ( XmlUtil.getNodeAttribute(dbNode, ConstXML.DB_URL_ATTRIBUTE));
        databaseConfiguration.setDriverClass   ( XmlUtil.getNodeAttribute(dbNode, ConstXML.DB_DRIVER_ATTRIBUTE));

        databaseConfiguration.setTypeName      ( XmlUtil.getNodeAttribute(dbNode, ConstXML.DB_TYPE_NAME) ); // Ver 2.1.0
        databaseConfiguration.setDialect       ( XmlUtil.getNodeAttribute(dbNode, ConstXML.DB_DIALECT) ); // Ver 2.1.0
        
        databaseConfiguration.setIsolationLevel( XmlUtil.getNodeAttribute(dbNode, ConstXML.DB_ISOLATION_LEVEL_ATTRIBUTE));
        databaseConfiguration.setPoolSize      ( XmlUtil.getNodeAttributeAsInt(dbNode, ConstXML.DB_POOLSIZE_ATTRIBUTE));
        
        Element dbElem = (Element) dbNode;

        //--- Database properties : n sub-elements "<property name="xxx" value="yyy" />
        NodeList propertyTags = dbElem.getElementsByTagName(ConstXML.DB_PROPERTY_ELEMENT);
        Properties properties = new Properties();
        int iPropCount = propertyTags.getLength();
        for ( int i = 0 ; i < iPropCount ; i++ )
        {
            Node node = propertyTags.item(i);
            if (node != null)
            {
                if (node instanceof Element)
                {
                    Element elemProperty = (Element) node;

                    String name  = elemProperty.getAttribute(ConstXML.DB_PROPERTY_NAME_ATTRIBUTE);
                    String value = elemProperty.getAttribute(ConstXML.DB_PROPERTY_VALUE_ATTRIBUTE);
                    properties.setProperty(name, value);
                    
                    if ( USER.equals(name) ) {
                    	databaseConfiguration.setUser(value);
                    }
                    if ( PASSWORD.equals(name) ) {
                    	databaseConfiguration.setPassword(value);
                    }
                }
            }
        }
        
        //--- Database metadata
        NodeList metadataList = dbElem.getElementsByTagName(ConstXML.DB_METADATA_ELEMENT);
        if (metadataList.getLength() > 0)
        {
            if (metadataList.item(0) instanceof Element)
            {
                Element elemMetadata = (Element) metadataList.item(0);
                databaseConfiguration.setMetadataCatalog(elemMetadata.getAttribute(ConstXML.DB_METADATA_ATTR_CATALOG));
                databaseConfiguration.setMetadataSchema(elemMetadata.getAttribute(ConstXML.DB_METADATA_ATTR_SCHEMA));
                databaseConfiguration.setMetadataTableNamePattern(elemMetadata.getAttribute(ConstXML.DB_METADATA_ATTR_TABLE_NAME_PATTERN));
                databaseConfiguration.setMetadataTableTypes(elemMetadata.getAttribute(ConstXML.DB_METADATA_ATTR_TABLE_TYPES));                
                databaseConfiguration.setMetadataTableNameInclude(elemMetadata.getAttribute(ConstXML.DB_METADATA_ATTR_TABLE_NAME_INCLUDE)); // ver 2.1.1
                databaseConfiguration.setMetadataTableNameExclude(elemMetadata.getAttribute(ConstXML.DB_METADATA_ATTR_TABLE_NAME_EXCLUDE)); // ver 2.1.1
            }
        }
        
        return databaseConfiguration ;
	}

	private void databaseConfigurationToXmlElement(DatabaseConfiguration databaseConfiguration, Document document, Element dbElement) throws TelosysToolsException {
		
		dbElement.setAttribute(ConstXML.DB_ID_ATTRIBUTE,     ""+databaseConfiguration.getDatabaseId());
		dbElement.setAttribute(ConstXML.DB_NAME_ATTRIBUTE,   databaseConfiguration.getDatabaseName());
		dbElement.setAttribute(ConstXML.DB_URL_ATTRIBUTE,    databaseConfiguration.getJdbcUrl());
		dbElement.setAttribute(ConstXML.DB_DRIVER_ATTRIBUTE, databaseConfiguration.getDriverClass() );
        
		dbElement.setAttribute(ConstXML.DB_TYPE_NAME,  databaseConfiguration.getTypeName() ); // Ver 2.1.0
		dbElement.setAttribute(ConstXML.DB_DIALECT,    databaseConfiguration.getDialect() ); // Ver 2.1.0
		
		dbElement.setAttribute(ConstXML.DB_ISOLATION_LEVEL_ATTRIBUTE, databaseConfiguration.getIsolationLevel() );
		dbElement.setAttribute(ConstXML.DB_POOLSIZE_ATTRIBUTE,        ""+databaseConfiguration.getPoolSize() );
        
		//--- <property name="user"      value="xxx" />
		dbElement.appendChild( createPropertyElement(document, USER,     databaseConfiguration.getUser() ) );
		//--- <property name="password"  value="xxx" />
		dbElement.appendChild( createPropertyElement(document, PASSWORD, databaseConfiguration.getPassword() ) );
		
		//--- <metadata catalog="" schema="ROOT" table-name-pattern="%" table-types="TABLE   VIEW  " />
		dbElement.appendChild( createMetaDataElement(document, databaseConfiguration ) );
	}
	
	private Element createPropertyElement(Document document, String name, String value) throws TelosysToolsException {
		Element propertyElement = document.createElement(ConstXML.DB_PROPERTY_ELEMENT);
		propertyElement.setAttribute(ConstXML.DB_PROPERTY_NAME_ATTRIBUTE,  name );
		propertyElement.setAttribute(ConstXML.DB_PROPERTY_VALUE_ATTRIBUTE, value );
		return propertyElement ;
	}

	private Element createMetaDataElement(Document document, DatabaseConfiguration databaseConfiguration) throws TelosysToolsException {
		Element e = document.createElement(ConstXML.DB_METADATA_ELEMENT );
		e.setAttribute(ConstXML.DB_METADATA_ATTR_CATALOG, databaseConfiguration.getMetadataCatalog() );
		e.setAttribute(ConstXML.DB_METADATA_ATTR_SCHEMA, databaseConfiguration.getMetadataSchema() );
		e.setAttribute(ConstXML.DB_METADATA_ATTR_TABLE_NAME_PATTERN, databaseConfiguration.getMetadataTableNamePattern() );
		e.setAttribute(ConstXML.DB_METADATA_ATTR_TABLE_TYPES, databaseConfiguration.getMetadataTableTypes() );
		e.setAttribute(ConstXML.DB_METADATA_ATTR_TABLE_NAME_INCLUDE, databaseConfiguration.getMetadataTableNameInclude() ); // ver 2.1.1
		e.setAttribute(ConstXML.DB_METADATA_ATTR_TABLE_NAME_EXCLUDE, databaseConfiguration.getMetadataTableNameExclude() ); // ver 2.1.1
		return e ;
	}
}
