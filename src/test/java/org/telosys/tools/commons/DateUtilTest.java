package org.telosys.tools.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.telosys.tools.commons.exception.TelosysRuntimeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateUtilTest {

	@Test
	public void test1() {		
		Calendar c = Calendar.getInstance();
		c.set(2018, 10, 28, 18, 59, 45); // 10 month from Zero = 11 (nov)
		Date date = c.getTime();
		assertEquals("2018", DateUtil.format(date, "YYYY"));
		assertEquals("11", DateUtil.format(date, "MM"));
		assertEquals("28", DateUtil.format(date, "dd"));
		assertEquals("18", DateUtil.format(date, "HH"));
		assertEquals("59", DateUtil.format(date, "mm"));
		assertEquals("45", DateUtil.format(date, "ss"));
		assertEquals("2018-11-28 (18:59:45)", DateUtil.format(date, "YYYY-MM-dd (HH:mm:ss)"));
	}
	
	@Test
	public void test2() {		
		Calendar c = Calendar.getInstance();
		c.set(2018, 10, 28, 18, 59, 45); // 10 month from Zero = 11 (nov)
		Date date = c.getTime();
		assertEquals("2018-11-28", DateUtil.dateISO(date));
		assertEquals("2018-11-28 18:59:45", DateUtil.dateTimeISO(date));
	}

	@Test
	public void test01() throws ParseException {		
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false); // non lenient parsing ( exception if invalid date )
	    dateFormat.parse("2018-11-28");
	}
	
	@Test( expected=ParseException.class )
	public void test02() throws ParseException {		
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false); // non lenient parsing ( exception if invalid date )
	    dateFormat.parse("2018+11=28"); // unparsable date
	}
	
	@Test
	public void testParse1() {		
		Date date = DateUtil.parseDate("2018-11-28");
		assertEquals(2018, date.getYear() + 1900);
		assertEquals(11, date.getMonth() + 1);
		assertEquals(28, date.getDate()); 
	}

	@Test
	public void testParse2() {		
		Date date = DateUtil.parseDateTime("2018-11-28 13:45:02");
		assertNotNull(date);
	}

	@Test( expected=TelosysRuntimeException.class )
	public void testParseErr1() {		
		DateUtil.parseDate("2018:11,28");
	}

	@Test( expected=TelosysRuntimeException.class )
	public void testParseErr2() {		
		DateUtil.parseDateTime("2018 11 28 13:45:02");
	}
	
}
