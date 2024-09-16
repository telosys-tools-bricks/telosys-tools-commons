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
package org.telosys.tools.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.telosys.tools.commons.exception.TelosysRuntimeException;


/**
 * Utility class for DATE operations ( set of static methods )
 * 
 * @author Laurent GUERIN
 *  
 */
public final class DateUtil
{

    private static final String DATE_ISO_FORMAT      = "yyyy-MM-dd" ;
    private static final String DATE_TIME_ISO_FORMAT = "yyyy-MM-dd HH:mm:ss" ;
    private static final String TIME_ISO_FORMAT      = "HH:mm:ss" ;
    
    /**
     * Private constructor to avoid instance creation
     */
    private DateUtil() {
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Returns the current date as java.sql.Date
     * @return the current date
     */
    public static java.sql.Date todaySqlDate()
    {
        //--- Date du jour
        return new java.sql.Date(Calendar.getInstance().getTimeInMillis());
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Returns the current date as java.util.Date
     * @return the current date
     */
    public static java.util.Date todayUtilDate()
    {
        //--- Date du jour
        return new java.util.Date(Calendar.getInstance().getTimeInMillis());
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Returns the current date as a String formated with the given format
     * @param sFormat the format to apply
     * @return the current date
     */
    public static String todayString(final String sFormat)
    {
        //--- Date du jour
        java.util.Date date = new java.util.Date(Calendar.getInstance().getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(sFormat);
        return dateFormat.format(date);
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Format the given day, month and year 
     * @param iDay the day ( 1 to 31 )
     * @param iMonth the month  ( 1 to 12 )
     * @param iYear the year
     * @param sFormat the format string usable to build a SimpleDateFormat
     * @return the formated date ( using the given format )
     */
    public static String format(final int iDay, final int iMonth, final int iYear, final String sFormat)
    {
        java.util.Date date = getUtilDate(iDay, iMonth, iYear);
        SimpleDateFormat dateFormat = new SimpleDateFormat(sFormat);
        return dateFormat.format(date);
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Format the given day, month and year ( in ISO format ) 
     * @param iDay the day ( 1 to 31 )
     * @param iMonth ( 1 to 12 )
     * @param iYear the year
     * @return the formated date ( using the default ISO format )
     */
    public static String format(final int iDay, final int iMonth, final int iYear)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_ISO_FORMAT);
        return dateFormat.format( getUtilDate(iDay, iMonth, iYear) );
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Format the given date using the given format
     * @param date
     * @param sFormat
     * @return the formated date
     * @since 1.0.3
     */
    public static String format(final java.util.Date date, final String sFormat)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(sFormat);
        return dateFormat.format(date);
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Returns a java.sql.Date instance for the given day, month and year
     * @param iDay the day ( 1 to 31 )
     * @param iMonth the month ( 1 to 12 )
     * @param iYear the year
     * @return the resulting date
     */
    public static java.sql.Date getSqlDate(final int iDay, final int iMonth, final int iYear)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(iYear, iMonth - 1, iDay);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Returns a standard java.util.Date instance for the given day, month and year
     * @param iDay the day ( 1 to 31 )
     * @param iMonth the month ( 1 to 12 )
     * @param iYear the year
     * @return the resulting date
     */
    public static java.util.Date getUtilDate(final int iDay, final int iMonth, final int iYear)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(iYear, iMonth - 1, iDay);
        return new java.util.Date(cal.getTimeInMillis());
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Returns the day of the month for the given date 
     * @param date 
     * @return the day ( from 1 to 31 ) 
     */
    public static int getDay(final java.sql.Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Returns the month of the given date
     * @param date :
     * @return the month ( from 1 to 12 )
     */
    public static int getMonth(final java.sql.Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Returns the year of the given date 
     * @param date :
     * @return the year
     */
    public static int getYear(final java.sql.Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Returns the given date as an ISO date string ( "YYYY-MM-DD" )
     * @param date
     * @return the date in ISO string format ( or "" if null )
     */
    public static String dateISO(final java.util.Date date)
    {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_ISO_FORMAT);
            return dateFormat.format( date );    
        }
        else {
            return "";
        }
    }
    
    //----------------------------------------------------------------------------------------------
    /**
     * Returns the given date as an ISO time string ( "HH:MM:SS" )
     * @param date
     * @return the time in ISO string format ( or "" if null )
     */
    public static String timeISO(final java.util.Date date)
    {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_ISO_FORMAT);
            return dateFormat.format( date );    
        }
        else {
            return "";
        }
    }
    
    //----------------------------------------------------------------------------------------------
    /**
     * Returns the given date as an ISO date and time string ( "YYYY-MM-DD HH:MM:SS" )
     * @param date
     * @return the date and time in ISO string format ( or "" if null )
     */
    public static String dateTimeISO(final java.util.Date date)
    {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_ISO_FORMAT);
            return dateFormat.format( date );    

        }
        else {
            return "";
        }
    }

    //-----------------------------------------------------------------------------------------
    private static void throwParseDateException(String sDate, String sMsg) {
        throw new TelosysRuntimeException("Cannot parse date '" + sDate + "' : " + sMsg );
    }
    private static void throwParseTimeException(String sTime, String sMsg) {
        throw new TelosysRuntimeException("Cannot parse time '" + sTime + "' : " + sMsg );
    }
    private static void throwParseDateTimeException(String sDateTime, String sMsg) {
        throw new TelosysRuntimeException("Cannot parse date & time '" + sDateTime + "' : " + sMsg );
    }
    
    private static final String INVALID_TIME_FORMAT     = "invalid format 'HH:MM:SS' expected" ;
    
    //-----------------------------------------------------------------------------------------
    /**
     * Parse the given date ( supposed to be in ISO format : "YYYY-MM-DD" )
     * @param sDate
     * @return date or null if the given string is null or void
     * @throws TelosysRuntimeException if the date is invalid
     * @since 1.0.2
     */
    public static java.util.Date parseDate( String sDate )
    {
        if ( sDate == null ) return null ;
        if ( sDate.length() == 0 ) return null ;
        
        java.util.Date ret = null ;
        try {
            //--- Try to parse the input date ( with non lenient parsing => check validity )
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_ISO_FORMAT);
            dateFormat.setLenient(false); // non lenient parsing ( exception if invalid date )
            ret = dateFormat.parse(sDate);
        } 
        catch (ParseException e) {
            throwParseDateException(sDate, "invalid date");
        }
        return ret ;
    }
    
    //----------------------------------------------------------------------------------------------
    /**
     * Parse the given time ( supposed to be in ISO format : "HH:MM:SS" )
     * @param sTime
     * @return time or null if the given string is null or void
     * @throws TelosysRuntimeException if the time is invalid
     * @since 1.0.2
     */
    public static java.util.Date parseTime( String sTime )
    {
        if ( sTime == null ) return null ;
        if ( sTime.length() == 0 ) return null ;
        
        char c = 0 ;
        for ( int i = 0 ; i < 8 ; i++ ) // the length is 8 "HH:MM:SS"
        {
            c = sTime.charAt(i);
            if ( ( c < '0' || c > '9') && ( c != ':' ) )  
            {
                throwParseTimeException(sTime, INVALID_TIME_FORMAT );
            }
            if ( c == ':' && ( i != 2 && i != 5 ) )
            {
                throwParseTimeException(sTime, INVALID_TIME_FORMAT );
            }
        }

        java.util.Date ret = null ;
        try {
            //--- Try to parse the input date ( with non lenient parsing => check validity )
            SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_ISO_FORMAT);
            dateFormat.setLenient(false); // non lenient parsing ( exception if invalid date )
            ret = dateFormat.parse(sTime);
            
        } 
        catch (ParseException e) {
            throwParseTimeException(sTime, "invalid time" );
        }
        return ret ;
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Parse the given date & time ( supposed to be in ISO format : "YYYY-MM-DD HH:MM:SS" )
     * @param sDateTime
     * @return date with time or null if the given string is null or void
     * @throws TelosysRuntimeException if the time is invalid
     * @since 1.0.2
     */
    public static java.util.Date parseDateTime( String sDateTime )
    {
        if ( sDateTime == null ) return null ;
        if ( sDateTime.length() == 0 ) return null ;

        java.util.Date ret = null ;
        try {
            //--- Try to parse the input datetime ( with non lenient parsing => check validity )
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_ISO_FORMAT);
            dateFormat.setLenient(false); // non lenient parsing ( exception if invalid date )
            ret = dateFormat.parse(sDateTime);
        } 
        catch (ParseException e) {
            throwParseDateTimeException(sDateTime, "invalid date or time" );
        }
        return ret ;
    }
    
}