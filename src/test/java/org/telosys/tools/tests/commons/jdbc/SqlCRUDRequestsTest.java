package org.telosys.tools.tests.commons.jdbc;

import junit.framework.TestCase;

import org.junit.Assert;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.jdbc.SqlCRUDRequests;

public class SqlCRUDRequestsTest extends TestCase {

	public void testTablePerson() throws TelosysToolsException {
		SqlCRUDRequests requests = new SqlCRUDRequests("PERSON", new String[]{"ID"}, new String[] {"FIRST_NAME", "LAST_NAME"} ) ;
		System.out.println("Requests : " + requests );
		System.out.println(requests.hasAutoIncColumn());
		
		Assert.assertFalse(requests.hasAutoIncColumn());
		Assert.assertEquals("PERSON", requests.getTableName());
		
		Assert.assertTrue(requests.getSqlSelect().endsWith("ID = ?")  );
		Assert.assertTrue(requests.getSqlUpdate().endsWith("ID = ?")  );
		Assert.assertTrue(requests.getSqlDelete().endsWith("ID = ?")  );
		Assert.assertTrue(requests.getSqlInsert().endsWith("( ?, ?, ? )")  );
	}

	public void testTablePersonAutoIncr() throws TelosysToolsException {
		SqlCRUDRequests requests = new SqlCRUDRequests("PERSON", new String[]{"ID"}, new String[] {"FIRST_NAME", "LAST_NAME"}, "ID" ) ;
		System.out.println("Requests : " + requests );
		System.out.println(requests.hasAutoIncColumn());
		
		Assert.assertTrue(requests.hasAutoIncColumn());
		Assert.assertEquals("PERSON", requests.getTableName());
		
		Assert.assertTrue(requests.getSqlSelect().endsWith("ID = ?")  );
		Assert.assertTrue(requests.getSqlUpdate().endsWith("ID = ?")  );
		Assert.assertTrue(requests.getSqlDelete().endsWith("ID = ?")  );
		Assert.assertFalse( requests.getSqlInsert().contains("ID"));
		Assert.assertTrue(requests.getSqlInsert().endsWith("( ?, ? )")  ); // Nb col - 1 
	}

	public void testTablePersonAutoIncr2() throws TelosysToolsException {
		SqlCRUDRequests requests = new SqlCRUDRequests("PERSON", 
									new String[]{"ID"}, 
									new String[] {"FIRST_NAME", "LAST_NAME", "COUNT"}, 
									"COUNT" ) ;
		
		System.out.println("Requests : " + requests );
		System.out.println(requests.hasAutoIncColumn());
		
		Assert.assertTrue(requests.hasAutoIncColumn());
		Assert.assertEquals("PERSON", requests.getTableName());
		
		Assert.assertTrue(requests.getSqlSelect().endsWith("ID = ?")  );
		Assert.assertTrue(requests.getSqlUpdate().endsWith("ID = ?")  );
		Assert.assertTrue(requests.getSqlDelete().endsWith("ID = ?")  );
		Assert.assertFalse( requests.getSqlInsert().contains("COUNT"));
		Assert.assertTrue(requests.getSqlInsert().endsWith("( ?, ?, ? )")  ); // Nb col - 1 
	}

}
