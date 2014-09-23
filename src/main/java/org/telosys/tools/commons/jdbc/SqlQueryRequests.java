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
