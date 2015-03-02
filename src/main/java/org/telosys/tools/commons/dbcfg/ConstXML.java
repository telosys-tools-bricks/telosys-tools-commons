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

/**
 * XML constants for  "dbconfig.xml" or "databases.dbcfg" file
 * 
 * @author L.Guerin
 *
 */
class ConstXML {

	 public final static String DATABASES_ROOT_ELEMENT = "databases";
	 public final static String DATABASES_MAX_ID_ATTRIBUTE = "maxId";
	 public final static String DATABASES_DEFAULT_ID_ATTRIBUTE = "defaultId";

	 //--------------------------------------------------------------------------
	 
	 public final static String DB_ELEMENT = "db";
	 public final static String DB_ID_ATTRIBUTE = "id";
	 public final static String DB_NAME_ATTRIBUTE = "name";
	 public final static String DB_DRIVER_ATTRIBUTE = "driver";
	 public final static String DB_URL_ATTRIBUTE = "url";

	 public final static String DB_TYPE_NAME  = "typeName"; // ver 2.1.0
	 public final static String DB_DIALECT    = "dialect";  // ver 2.1.0
	 
	 public final static String DB_ISOLATION_LEVEL_ATTRIBUTE = "isolationLevel";
	 public final static String DB_POOLSIZE_ATTRIBUTE = "poolSize";
	 
	 public final static String DB_PROPERTY_ELEMENT = "property";
	 public final static String DB_PROPERTY_NAME_ATTRIBUTE = "name";
	 public final static String DB_PROPERTY_VALUE_ATTRIBUTE = "value";
	 
	 public final static String DB_METADATA_ELEMENT = "metadata";
	 public final static String DB_METADATA_ATTR_CATALOG = "catalog" ;
	 public final static String DB_METADATA_ATTR_SCHEMA = "schema" ;
	 public final static String DB_METADATA_ATTR_TABLE_NAME_PATTERN = "table-name-pattern";
	 public final static String DB_METADATA_ATTR_TABLE_TYPES = "table-types";
	 
	 public final static String DB_METADATA_ATTR_TABLE_NAME_INCLUDE = "table-name-include"; // ver 2.1.1
	 public final static String DB_METADATA_ATTR_TABLE_NAME_EXCLUDE = "table-name-exclude"; // ver 2.1.1
	 
	 //---------------------------------------------------------------------------
}
