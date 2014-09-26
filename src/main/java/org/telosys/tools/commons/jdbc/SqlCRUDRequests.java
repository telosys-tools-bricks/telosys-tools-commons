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

public class SqlCRUDRequests {
	

    private String  _table           = null;

    private String  _keyColumns[]    = null;

    private String  _dataColumns[]   = null;

    private String  _sAutoIncColumn  = null;

    //--- SQL requests
    private String  _sqlSelect       = null;

    private String  _sqlExists       = null;

    private String  _sqlInsert       = null;

    private String  _sqlUpdate       = null;

    private String  _sqlDelete       = null;

    //--- Pre-built clauses
    private String  _allColumnsList  = null;

    private String  _whereCriteria   = null;

//    //--- Join management
//    private boolean _bSelectWithJoin = false;
//
//    private String  _sJoinTables     = null;
//
//    private String  _sJoinColumns    = null;
//
//    private String  _sJoinCriteria   = null;

    //============================================================================
    //  CONSTRUCTORS
    //============================================================================
    private void init(String table, String keyColumns[], String dataColumns[], String sAutoInc) // v 0.9.9
    {
        if (table == null || keyColumns == null || dataColumns == null)
        {
            return;
        }

        //--- Init the input parameters
        _table = table.trim();
        if ( sAutoInc != null )
        {
            _sAutoIncColumn = sAutoInc.trim() ;
        }
        else
        {
            _sAutoIncColumn = null ;
        }

        _keyColumns = (String[]) keyColumns.clone(); // clone to avoid exposing mutable object
        _dataColumns = (String[]) dataColumns.clone(); // clone to avoid exposing mutable object

        //--- Build the 4 SQL Request
        _sqlSelect = buildSqlSelect();
        _sqlExists = buildSqlSelectCount();
        _sqlInsert = buildSqlInsert();
        _sqlUpdate = buildSqlUpdate();
        _sqlDelete = buildSqlDelete();

        //--- Pre-built clauses parts
        _allColumnsList = columnsAll(true);
        _whereCriteria = whereCriteria(true);
    }
    
    /**
     * Constructor
     * @param table the table
     * @param keyColumns the columns of the Primary Key
     * @param dataColumns the other columns of the table ( not in the Primary Key ) 
     */
    public SqlCRUDRequests(String table, String keyColumns[], String dataColumns[])
    {
        init(table, keyColumns, dataColumns, null); // v 0.9.9
    }
    
    /**
     * Constructor for tables with an "auto incremented" column 
     * @param table the table
     * @param keyColumns the columns of the Primary Key
     * @param dataColumns the other columns of the table ( not in the Primary Key ) 
     * @param sAutoInc the name the "auto incremented" column 
     */
    public SqlCRUDRequests(String table, String keyColumns[], String dataColumns[], String sAutoInc ) // v 0.9.9
    {
        init(table, keyColumns, dataColumns, sAutoInc);
    }
    
    //============================================================================
    //  PRIVATE METHODS
    //============================================================================
    /**
     * Determines if a column is auto-incremented
     * @param sCol
     * @return true if the given column is the auto-incremented column of the table
     */
    private boolean isAutoInc ( String sCol ) // v 0.9.9
    {
        if ( _sAutoIncColumn != null )
        {
            return _sAutoIncColumn.equals( sCol ) ; 
        }
        return false ;
    }
    
    /**
     * Build a list of coma separated colums with ALL the columns <br>
     * (like standard columnsAll ) except for the AUTO_INC column <br> 
     * ie : "tab.col1, tab.col2, tab.col3"
     * @param bPrefix
     * @return
     */
    private String columnsWithoutAutoInc(boolean bPrefix) // v 0.9.9
    {
        StringBuffer sb = new StringBuffer(200);
        int iNbCol = 0;
        //--- Data Columns
        for ( int c = 0 ; c < _dataColumns.length ; c++ )
        {
            if ( ! isAutoInc ( _dataColumns[c] ) ) // Check if the column is auto inc
            {        
	            if (iNbCol > 0)
	            {
	                sb.append(", ");
	            }
	            if (bPrefix)
	            {
	                sb.append(_table + ".");
	            }
	            sb.append(_dataColumns[c]);
	            iNbCol++;
            }
        }
        //--- Key Columns
        for ( int c = 0 ; c < _keyColumns.length ; c++ )
        {
            if ( ! isAutoInc ( _keyColumns[c] ) ) // Check if the column is auto inc
            {        
	            if (iNbCol > 0)
	            {
	                sb.append(", ");
	            }
	            if (bPrefix)
	            {
	                sb.append(_table + ".");
	            }
	            sb.append(_keyColumns[c]);
	            iNbCol++;	            
            }
        }
        return sb.toString();
    }
    
    /**
     * Build a list of coma separated colums (for ALL the columns) ie : "tab.col1, tab.col2, tab.col3"
     * 
     * @return
     */
    private String columnsAll(boolean bPrefix)
    {
        StringBuffer sb = new StringBuffer(200);
        int iNbCol = 0;

        //--- Data Columns
        for ( int c = 0 ; c < _dataColumns.length ; c++ )
        {
            if (iNbCol > 0)
            {
                sb.append(", ");
            }
            if (bPrefix)
            {
                sb.append(_table + ".");
            }
            sb.append(_dataColumns[c]);
            iNbCol++;
        }

        //--- Key Columns
        for ( int c = 0 ; c < _keyColumns.length ; c++ )
        {
            if (iNbCol > 0)
            {
                sb.append(", ");
            }
            if (bPrefix)
            {
                sb.append(_table + ".");
            }
            sb.append(_keyColumns[c]);
            iNbCol++;
        }
        return sb.toString();
    }

    /**
     * Build a list of commas separated "?" ( for ALL the columns ) ie : "?, ?, ?"
     * 
     * @return
     */
    private String values()
    {
//        StringBuffer sb = new StringBuffer(200);
//        int iNbCol = 0;
//
//        //--- Data Columns
//        for ( int c = 0 ; c < _dataColumns.length ; c++ )
//        {
//            if (iNbCol > 0)
//            {
//                sb.append(", ");
//            }
//            sb.append("?");
//            iNbCol++;
//        }
//
//        //--- Key Columns
//        for ( int c = 0 ; c < _keyColumns.length ; c++ )
//        {
//            if (iNbCol > 0)
//            {
//                sb.append(", ");
//            }
//            sb.append("?");
//            iNbCol++;
//        }
//        return sb.toString();
    	
        return buildValues( _dataColumns.length + _keyColumns.length );
    }

    private String valuesWithoutAutoInc() // v 0.9.9
    {
        return buildValues(_dataColumns.length + _keyColumns.length - 1);
    }
    
    private String buildValues(int numberOfValues) // v 0.9.9
    {
    	if ( numberOfValues <= 0 ) {
    		return "" ;
    	}
    	
        StringBuffer sb = new StringBuffer();
        //--- Columns
        for ( int c = 0 ; c < numberOfValues ; c++ )
        {
            if (c > 0)
            {
                sb.append(", ");
            }
            sb.append("?");
        }
        return sb.toString();
    }

    /**
     * Build the where criteria ( for KEY columns only) <br>
     * ie : "tab.col1 = ? and tab.col2 = ?"
     * 
     * @return
     */
    private String whereCriteria(boolean bPrefix)
    {
        StringBuffer sb = new StringBuffer(200);
        //--- Key Columns
        for ( int c = 0 ; c < _keyColumns.length ; c++ )
        {
            if (c > 0)
            {
                sb.append(" and ");
            }
            if (bPrefix)
            {
                sb.append(_table + ".");
            }
            sb.append(_keyColumns[c] + " = ?");
        }
        return sb.toString();
    }

    /**
     * Build the set column value clause (for DATA columns only) ie : "tab.col1 = ?, tab.col2 = ?"
     * 
     * @return
     */
    private String setDataValues(boolean bPrefix)
    {
        StringBuffer sb = new StringBuffer(200);
        //--- Data Columns
        for ( int c = 0 ; c < _dataColumns.length ; c++ )
        {
            if (c > 0)
            {
                sb.append(", ");
            }
            if (bPrefix)
            {
                sb.append(_table + ".");
            }
            sb.append(_dataColumns[c] + " = ?");
        }
        return sb.toString();
    }

    /**
     * Build the SQL SELECT request
     * 
     * @return
     */
    private String buildSqlSelect()
    {
        //return "select " + allColumns(true) + " from " + _table + " where " + whereCriteria(true);
        return "select " + columnsAll(false) + " from " + _table + " where " + whereCriteria(false);
    }

    /**
     * Build the SQL COUNT request
     * 
     * @return
     */
    private String buildSqlSelectCount()
    {
        return "select count(*) from " + _table + " where " + whereCriteria(false);
    }

    /**
     * Build the SQL INSERT request
     * 
     * @return
     */
//  v 0.9.9
//    private String buildSqlInsert() 
//    {
//        return "insert into " + _table + " ( " + columnsAll(false) + " ) values ( " + values() + " )";
//    }

    private String buildSqlInsert() // v 0.9.9
    {
        if ( _sAutoIncColumn != null )
        {
            //--- Special INSERT request without the AUTO-INC column
            return "insert into " + _table + " ( " + columnsWithoutAutoInc(false) + " ) values ( " + valuesWithoutAutoInc() + " )";
        }
        else
        {
            //--- Standard INSERT request with all the columns
            return "insert into " + _table + " ( " + columnsAll(false) + " ) values ( " + values() + " )";
        }
    }

    /**
     * Build the SQL UPDATE request
     * 
     * @return
     */
    private String buildSqlUpdate()
    {
        return "update " + _table + " set " + setDataValues(false) + " where " + whereCriteria(false);
    }

    /**
     * Build the SQL DELETE request
     * 
     * @return
     */
    private String buildSqlDelete()
    {
        return "delete from " + _table + " where " + whereCriteria(false);
    }

    //============================================================================
    //============================================================================
    /**
     * Creates a ListQuery 
     * @param iKeySize number of parameters for the query ( the number of '?' in the string : 0 to N )
     * @param sQueryCriteria the SQL criteria for the WHERE clause
     * @param sQueryEndOfSelect the additional SQL clause ( e.g. : "order by ..." )
     * @return
     */
    public SqlQueryRequests createQuery(int iKeySize, String sQueryCriteria, String sQueryEndOfSelect) //throws TelosysException
    {
//        if (_bSelectWithJoin)
//        {
//            //--- The SELECT request has a JOIN => build the query with the JOIN
//            //return createQueryWithJoin(iKeySize, sQueryCriteria, sQueryEndOfSelect);
//            return createQueryWithJoin(sQueryCriteria, sQueryEndOfSelect);
//        }
//        else
//        {
//            //--- No JOIN in the SELECT => build a normal query
//            //return createQueryWithoutJoin(iKeySize, sQueryCriteria, sQueryEndOfSelect);
//            return createQueryWithoutJoin(sQueryCriteria, sQueryEndOfSelect);
//        }
        return createQueryWithoutJoin(sQueryCriteria, sQueryEndOfSelect);
    }

    //private SqlQueryRequests createQueryWithoutJoin(int iKeySize, String sQueryCriteria, String sQueryEndOfSelect)
    private SqlQueryRequests createQueryWithoutJoin( String sQueryCriteria, String sQueryEndOfSelect)
    {
        String sWhere = SqlBuilder.buildSqlWhere(sQueryCriteria);
        String sEnd = SqlBuilder.buildSqlComplement(sQueryEndOfSelect);

        String sSelect = "select " + columnsAll(true) + " from " + _table + sWhere + sEnd;
        String sCount  = "select count(*) from " + _table + sWhere;
        String sDelete = "delete from " + _table + sWhere;
        //return new StandardQuery(iKeySize, sSelect, sCount, sDelete);
        return new SqlQueryRequests(sSelect, sCount, sDelete);
    }

//    //private SqlQueryRequests createQueryWithJoin(int iKeySize, String sQueryCriteria, String sQueryEndOfSelect)
//    private SqlQueryRequests createQueryWithJoin( String sQueryCriteria, String sQueryEndOfSelect)
//    {
//        //--- Build the SQL fragments
//        String sColumns = SqlBuilder.buildSqlColumns(columnsAll(true), _sJoinColumns);
//        String sTables = SqlBuilder.buildSqlTables(_table, _sJoinTables);
//        String sWhere = SqlBuilder.buildSqlWhere(_sJoinCriteria, sQueryCriteria);
//        String sEnd = SqlBuilder.buildSqlComplement(sQueryEndOfSelect);
//
//        String sSelect = "select " + sColumns + " from " + sTables + sWhere + sEnd;
//        String sCount  = "select count(*) from " + sTables + sWhere;
//        String sDelete = "delete from " + _table + SqlBuilder.buildSqlWhere(sQueryCriteria);
//        //return new StandardQuery(iKeySize, sSelect, sCount, sDelete);
//        return new SqlQueryRequests(sSelect, sCount, sDelete);
//        
//    }

    /**
     * Changes the SQL SELECT by adding a JOIN
     * 
     * @param sJoinTables
     * @param sJoinColumns
     * @param sJoinCriteria
     */
    public String getSqlSelectWithJoin(String sJoinTables, String sJoinColumns, String sJoinCriteria)
    {
        //--- Build the SQL fragments
        String sColumns = SqlBuilder.buildSqlColumns(columnsAll(true), sJoinColumns);
        String sTables = SqlBuilder.buildSqlTables(_table, sJoinTables);
        String sWhere = SqlBuilder.buildSqlWhere(sJoinCriteria, whereCriteria(true));

        //--- Set the new SELECT request
        String sqlSelectWithJoin = "select " + sColumns + " from " + sTables + sWhere;

//        //--- Keep the Join characteristics
//        _bSelectWithJoin = true;
//        _sJoinTables = sJoinTables;
//        _sJoinColumns = sJoinColumns;
//        _sJoinCriteria = sJoinCriteria;
        return sqlSelectWithJoin ;
    }

    //============================================================================
    //  GETTERS
    //============================================================================

    /**
     * Returns the SQL SELECT request
     * 
     * @return the SQL Select clause
     */
    public String getSqlSelect()
    {
        return _sqlSelect;
    }

    /**
     * Returns the SQL SELECT request adding the optional clause at the end
     * 
     * @param sOptionalClause
     * @return the SQL Select clause
     */
    public String getSqlSelect(String sOptionalClause)
    {
        if (sOptionalClause != null)
        {
            return _sqlSelect + " " + sOptionalClause;
        }
        else
        {
            return _sqlSelect;
        }
    }

    /**
     * Returns the SQL SELECT COUNT request
     * 
     * @return
     */
    public String getSqlExists()
    {
        return _sqlExists;
    }

    /**
     * Returns the SQL INSERT request
     * 
     * @return
     */
    public String getSqlInsert()
    {
        return _sqlInsert;
    }

    /**
     * Returns the SQL UPDATE request
     * 
     * @return
     */
    public String getSqlUpdate()
    {
        return _sqlUpdate;
    }

    /**
     * Returns the SQL DELETE request
     * 
     * @return
     */
    public String getSqlDelete()
    {
        return _sqlDelete;
    }

    /**
     * Returns the TABLE NAME
     * 
     * @return
     */
    public String getTableName()
    {
        return _table;
    }

    /**
     * Returns the 'Auto-Inc' column if any (or NULL)
     * @return
     */
    public String getAutoIncColumn()
    {
        return _sAutoIncColumn;
    }

    /**
     * Returns true if the table has an 'Auto-Inc' column
     * @return
     */
    public boolean hasAutoIncColumn()
    {
        return _sAutoIncColumn != null ;
    }

    /**
     * Returns the list of all the columns (coma separated)
     * 
     * @return
     */
    public String getAllColumnsList()
    {
        return _allColumnsList;
    }

    /**
     * Returns the where criteria of the requests
     * 
     * @return
     */
    public String getWhereCriteria()
    {
        return _whereCriteria;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "'"+ _table + "' requests :\n"
        + ". " + _sqlSelect + "\n"
        + ". " + _sqlInsert + "\n"
        + ". " + _sqlUpdate + "\n"
        + ". " + _sqlDelete + "\n"
        + ". " + _sqlExists + "\n"
        ;
    }	
}
