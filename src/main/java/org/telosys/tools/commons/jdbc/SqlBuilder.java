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
package org.telosys.tools.commons.jdbc;

public class SqlBuilder {
	
    /**
     * No public constructor
     */
    private SqlBuilder()
    {
    }

    
    /**
     * Build a string containing all the columns needs for a SQL SELECT this
     * string can be used to build a SQL resquest Each column is prefixed by the
     * table name
     * 
     * @param sMainColumns
     *            main table columns (ie: "TABLE.COL1, TABLE.COL2, TABLE.COL3")
     * @param sJoinColumns
     *            join columns can be null if there is no join in the request
     *            (ie: "T2.A, T2.B")
     * @return
     */
    public static String buildSqlColumns(String sMainColumns, String sJoinColumns) //throws TelosysException
    {
        StringBuffer sb = new StringBuffer(200);

        //--- Main columns
        //sb.append(buildSqlMainColumns(sMainColumns));
        if (sMainColumns != null)
        {
            sb.append(sMainColumns);
        }
        //--- Join columns
        if (sJoinColumns != null)
        {
            String sTrimJoinColumns = sJoinColumns.trim();
            if (sTrimJoinColumns.length() > 0)
            {
                if (sTrimJoinColumns.startsWith(","))
                {
                    if (sTrimJoinColumns.length() > 1)
                    {
                        sb.append(sTrimJoinColumns);
                    }
                }
                else
                {
                    sb.append(", " + sTrimJoinColumns);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Build a string containing all the tables needs for a SQL SELECT, the
     * string returned can be used to build a SQL resquest
     * 
     * @param sMainTable
     * @param sJoinTables
     * @return
     */
    public static String buildSqlTables(String sMainTable, String sJoinTables) //throws TelosysException
    {
        StringBuffer sb = new StringBuffer(100);
        //--- Main table
        //sb.append(buildSqlMainTable(sMainTable));
        if (sMainTable != null)
        {
            sb.append(sMainTable);
        }
        else
        {
            sb.append("Error_No_Main_Table");            
        }
        //--- Join tables
        if (sJoinTables != null)
        {
            String sTrimJoinTables = sJoinTables.trim();
            if (sTrimJoinTables.length() > 0)
            {
                if (sTrimJoinTables.startsWith(","))
                {
                    if (sTrimJoinTables.length() > 1)
                    {
                        sb.append(sTrimJoinTables);
                    }
                }
                else
                {
                    sb.append(", " + sTrimJoinTables);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Build a SQL WHERE with a single criteria
     * 
     * @param sQueryCriteria
     *            the query criteria ( ie : "T1.C > ?" ), can be null
     * @return the SQL WHERE CLAUSE ( ie: " where T1.C > ? " ), or " " (one
     *         space) if no criteria
     */
    public static String buildSqlWhere(String sQueryCriteria)
    {
        if (sQueryCriteria != null)
        {
            String sTrimQueryCriteria = sQueryCriteria.trim();
            if (sTrimQueryCriteria.length() > 0)
            {
                String sCriteria = sTrimQueryCriteria;
                // Remove starting "and" if it exists
                if (sTrimQueryCriteria.toLowerCase().startsWith("and "))
                {
                    sCriteria = sTrimQueryCriteria.substring(3);
                }
                //--- Build the WHERE CRITERIA
                return " where " + sCriteria;
            }
        }
        //--- No criteria => no WHERE CLAUSE
        return " ";
    }

    /**
     * Build a SQL WHERE with 2 criterias ( Join Criteria & Query Criteria )
     * 
     * @param sJoinCriteria
     *            the join criteria ( ie : "T1.A = T2.B" ), can be null
     * @param sQueryCriteria
     *            the query criteria ( ie : "T1.C > ?" ), can be null
     * @return the SQL WHERE CLAUSE ( ie: " where T1.A = T2.B and T1.C > ? " )
     *         or " " (one space) if no criteria
     */
    public static String buildSqlWhere(String sJoinCriteria, String sQueryCriteria)
    {
        StringBuffer sbSql = new StringBuffer(200);

        //--- Where criterias
        boolean bFirstCriteriaSet = false;
        if (sJoinCriteria != null)
        {
            String sTrimJoinCriteria = sJoinCriteria.trim();
            if (sTrimJoinCriteria.length() > 0)
            {
                String sCriteria = sTrimJoinCriteria;
                // Remove starting "and" if it exists
                if (sTrimJoinCriteria.toLowerCase().startsWith("and "))
                {
                    sCriteria = sTrimJoinCriteria.substring(3);
                }
                //--- Set the first WHERE CRITERIA
                sbSql.append(" where " + sCriteria);
                bFirstCriteriaSet = true;
            }
        }

        if (sQueryCriteria != null)
        {
            String sTrimQueryCriteria = sQueryCriteria.trim();
            if (sTrimQueryCriteria.length() > 0)
            {
                String sCriteria = sTrimQueryCriteria;
                // Remove starting "and" if it exists
                if (sTrimQueryCriteria.toLowerCase().startsWith("and "))
                {
                    //sbSql.append(sTrimQueryCriteria.substring(3));
                    sCriteria = sTrimQueryCriteria.substring(3);
                }
                if (bFirstCriteriaSet)
                {
                    //--- Set a new WHERE CRITERIA
                    sbSql.append(" and " + sCriteria);
                }
                else
                {
                    //--- Set the first WHERE CRITERIA
                    sbSql.append(" where " + sCriteria);
                }
            }
        }
        if (sbSql.length() > 0)
        {
            return sbSql.toString();
        }
        else
        {
            return " ";
        }
    }

    /**
     * Build the complement of a SQL SELECT request ( a string that can be added
     * at the end of the request )
     * 
     * @param sQueryEndOfSelect
     *            the complement (ie: "order by .." )
     * @return
     */
    public static String buildSqlComplement(String sQueryEndOfSelect)
    {
        if (sQueryEndOfSelect != null)
        {
            String sTrimQueryEndOfSelect = sQueryEndOfSelect.trim();
            if (sTrimQueryEndOfSelect.length() > 0)
            {
                return " " + sTrimQueryEndOfSelect;
            }
        }
        return " ";
    }

}
