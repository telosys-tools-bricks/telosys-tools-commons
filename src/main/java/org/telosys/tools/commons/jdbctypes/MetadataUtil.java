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
package org.telosys.tools.commons.jdbctypes;

import java.sql.DatabaseMetaData;

public class MetadataUtil {

	public static String getForeignKeyDeferrability(int code)
	{
		switch (code)
		{
		case DatabaseMetaData.importedKeyInitiallyDeferred : 
			return "Initially deferred";
		case DatabaseMetaData.importedKeyInitiallyImmediate : 
			return "Initially immediate";
		case DatabaseMetaData.importedKeyNotDeferrable : 
			return "Not deferrable";
		}
		return "???" ;
	}

	public static String getForeignKeyUpdateRule(int code)
	{
		return getDeleteUpdateRule(code);
	}
	public static String getForeignKeyDeleteRule(int code)
	{
		return getDeleteUpdateRule(code);
	}
	private static String getDeleteUpdateRule(int code)
	{
		switch (code)
		{
		case DatabaseMetaData.importedKeyNoAction : 
			return "No action";
		case DatabaseMetaData.importedKeyCascade : 
			return "Cascade";
		case DatabaseMetaData.importedKeySetNull : 
			return "Set null";
		case DatabaseMetaData.importedKeySetDefault : 
			return "Set default";
		case DatabaseMetaData.importedKeyRestrict : 
			return "Restrict";
		}
		return "???" ;
	}
}
