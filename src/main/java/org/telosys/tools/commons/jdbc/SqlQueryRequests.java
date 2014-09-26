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
package org.telosys.tools.commons.jdbc;

public class SqlQueryRequests {

	private final String sqlSelect ; //  "select " + sColumns + " from " + sTables + sWhere + sEnd;
	private final String sqlCount ; // = "select count(*) from " + sTables + sWhere;
	private final String sqlDelete ; // = "delete from " + _table + SqlBuilder.buildSqlWhere(sQueryCriteria);
	
	
    public SqlQueryRequests(String sSelect, String sCount, String sDelete) {
		super();
		this.sqlSelect = sSelect;
		this.sqlCount  = sCount;
		this.sqlDelete = sDelete;
	}

	/**
     * Return the SQL request "select ... from ..."
     * @return
     */
    public String getSqlSelect() {
    	return sqlSelect ;
    }

    /**
     * Return the SQL request "delete from ..."
     * @return
     */
    public String getSqlDelete() {
    	return sqlDelete ;
    }

    /**
     * Return the SQL request "select count(*) from ..."
     * @return
     */
    public String getSqlCount() {
    	return sqlCount ;
    }


}
