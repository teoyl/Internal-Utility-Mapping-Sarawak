package com.sains.common.util;

import com.sains.framework.base.Debug;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 * 
 */
public class DateUtil {
	private static final Log log = LogFactory.getLog(DateUtil.class);

	private static final String datePattern = "dd/MM/yyyy";

	private static final String timePattern = datePattern + " HH:mm a";

	private static String defaultDatePattern = "EEE MMM dd HH:mm z yyyy";

	/**
	 * Return default datePattern (MM/dd/yyyy)
	 * 
	 * @return a string representing the date pattern on the UI
	 * @deprecated
	 */
	public static String getDatePattern() {
		return datePattern;
	}

	/**
	 * This method attempts to convert an Oracle-formatted date in the form
	 * dd-MMM-yyyy to mm/dd/yyyy.
	 * 
	 * @param aDate date from database as a string
	 * @return formatted string for the ui
	 * @deprecated
	 */
	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(datePattern);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date/time in the
	 * format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param strDate
	 *            a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 * @deprecated
	 */
	public static final Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '"
					+ aMask + "'");
		}

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			Debug.printError("Exception : " + pe.getMessage());
			if (log.isDebugEnabled()) {
				log.debug("conversion failed, trying default date format");
			}

			df = new SimpleDateFormat(defaultDatePattern);

			try {
				date = df.parse(strDate);
				//
			} catch (ParseException pe2) {
				throw new ParseException(pe2.getMessage(), pe2.getErrorOffset());
			}

			if (log.isDebugEnabled()) {
				log.debug("formatted date successfully!");
			}
		}

		return (date);
	}

	/**
	 * This method returns the current date time in the format: MM/dd/yyyy HH:MM a
	 * 
	 * @param theTime the current time
	 * @return the current date/time
	 * @deprecated
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(timePattern, theTime);
	}

	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException
	 * @deprecated
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(datePattern);

		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}

	/**
	 * This method generates a string representation of a date's date/time in
	 * the format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param aDate
	 *            a date object
	 * @return a formatted string representation of the date
	 * 
	 * @see java.text.SimpleDateFormat
	 * @deprecated
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date based on the
	 * System Property 'dateFormat' in the format you specify on input
	 * 
	 * @param aDate A date to convert
	 * @return a string representation of the date
	 * @deprecated
	 */
	public static final String convertDateToString(Date aDate) {
		return getDateTime(datePattern, aDate);
	}

	/**
	 * This method converts a String to a date using the datePattern
	 * 
	 * @param strDate the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * 
	 * @throws ParseException
	 * @deprecated
	 */
	public static Date convertStringToDate(String strDate)
			throws ParseException {
		Date aDate = null;

		try {
			if (log.isDebugEnabled()) {
				log.debug("converting date with pattern: " + datePattern);
			}

			aDate = convertStringToDate(datePattern, strDate);
		} catch (ParseException pe) {
			log.error("Could not convert '" + strDate
					+ "' to a date, throwing exception");
			pe.printStackTrace();
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return aDate;
	}

	/**
	 * compare two dates
	 * The format of date1 and date2 must be in dd/MM/yyyy
	 * @param date1 String
	 * @param date2 String
	 * @return <code>true</code> if date2 is after date1, <code>false</code> otherwise
	 */
	public static boolean compareDate(String date1, String date2) throws ParseException {

		if (DateUtil.getDate(date2).before(DateUtil.getDate(date1))) {
			return true;
		}
		return false;
	}

	/**
	 * @return Returns the defaultDatePattern.
	 * @deprecated
	 */
	public static String getDefaultDatePattern() {
		return defaultDatePattern;
	}

	/**
	 * @param defaultDatePattern The defaultDatePattern to set.
	 * @deprecated
	 */
	public static void setDefaultDatePattern(String defaultDatePattern) {
		DateUtil.defaultDatePattern = defaultDatePattern;
	}

/*	*//**
	 * @return Returns the log.
	 *//*
	public static Log getLog() {
		return log;
	}

	*//**
	 * @param log
	 *            The log to set.
	 *//*
	public static void setLog(Log log) {
		DateUtil.log = log;
	}
*/
	/**
	 * @return Returns the timePattern.
	 */
/*	public static String getTimePattern() {
		return timePattern;
	}

	*//**
	 * @param timePattern
	 *            The timePattern to set.
	 *//*
	public static void setTimePattern(String timePattern) {
		DateUtil.timePattern = timePattern;
	}
*/
	/**
	 * @param datePattern
	 *            The datePattern to set.
	 */
/*	public static void setDatePattern(String datePattern) {
		DateUtil.datePattern = datePattern;
	}
*/
	/**
	 * Get current Timestamp object.
	 * 
	 * @return java.sql.Timestamp
	 */
	public static java.sql.Timestamp getCurrentTimestamp(){		
		return new java.sql.Timestamp(System.currentTimeMillis());
	}	
        
        // ThoTH @ 5-Sept-2014 :: Copied from Internet
        public static java.sql.Timestamp getCurrentTimestamp_nano(){		
            long timeInMillis = System.currentTimeMillis();
            long timeInNanos = System.nanoTime();

            java.sql.Timestamp timestamp = new java.sql.Timestamp(timeInMillis);
            timestamp.setNanos((int) (timeInNanos % 1000000000));
            
            return timestamp;
	}
	
	/**
	 * Get current date in Timestamp object.
	 * 
	 * @return java.sql.Timestamp
	 */	
//	public static java.sql.Timestamp getCurrentDate(){
//		return getDate(getCurrentTimestamp());
//	}		

	/**
	 * Get current date in Date object.
	 * 
	 * @return java.util.Date
	 */
	public static java.util.Date getCurrentDate() {
		return new java.util.Date(getCurrentTimestamp().getTime());
	}

	/**
	 * Convert the given Timestamp to date format.
	 * 
	 * @param timestamp java.sql.Timestamp
	 * @return java.sql.Timestamp
	 */
	public static java.sql.Timestamp getDate(java.sql.Timestamp timestamp) {
		if (timestamp == null) {
			return null;	
		}
		Calendar cal = getCalendar(timestamp);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);

		return new java.sql.Timestamp(cal.getTime().getTime());
	}

	/**
	 * Get current Calendar object.
	 * 
	 * @return java.util.Calendar
	 */
	public static java.util.Calendar getCalendar() {
		Calendar cal = Calendar.getInstance();
		return cal;
	}

	/**
	 * Convert the given Timestamp object to Calendar object.
	 * 
	 * @param timestamp java.sql.Timestamp
	 * @return java.util.Calendar
	 */
	public static java.util.Calendar getCalendar(java.sql.Timestamp timestamp) {
		Calendar cal = getCalendar();
		cal.setTime(new java.util.Date(timestamp.getTime()));
		return cal;
	}
	
	/**
	 * Convert the given Timestamp object to Calendar object.
	 * 
	 * @param timestamp java.sql.Timestamp
	 * @return java.util.Calendar
	 */
	public static java.util.Calendar getCalendar(java.util.Date date) {
		Calendar cal = getCalendar();
		cal.setTime(date);
		return cal;
	}
	
	/**
	 * Convert the given date from String to Date.
	 * 
	 * @param sDate The date in string format
	 * @return java.util.Date
	 */
	public static Date getDate(String sDate) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(Formatter.DATE_PATTERN);
		return df.parse(sDate);
	}
	
	/**
	 * Convert the given date from String to Date.
	 * 
	 * @param sDate The date in string format
	 * @param the pattern of the date
	 * @return java.util.Date
	 */
	public static Date getDate(String sDate, String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.parse(sDate);
	}

	public static Date getDate(String sDate, boolean isLenient)
			throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(Formatter.DATE_PATTERN);
		df.setLenient(isLenient);
		return df.parse(sDate);
	}
	
	public static Calendar rollCalendar(Calendar cal, int field, int value){
		return null;
	}

        public static java.sql.Timestamp getTimestampFromDate(Date aDate){
            return new java.sql.Timestamp(aDate.getTime());
        }
        
        // ThoTH @ 6-Feb-2015
        public static String getMonth_malay(Integer intM) {
            String[] monthNames = {"Januari","Februari","Mac","April","Mei","Jun",
		"Julai","Ogos","September","Oktober","November","Disember"};
                
            return monthNames[intM -1];
        }
        
        // ThoTH @ 6-Feb-2015
        public static String getWeekday_malay(Integer intM) {
            String[] monthNames = {"I","S","R","K","J","S","A"};
                
            return monthNames[intM -1];
        }

    // TeoYL @ 28-Aug-2024     
    public static String getCurrentDatePlus7Days() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        return Formatter.formatDate(calendar.getTime(), SystemConstants.DATE.dataEntryFormat3);
//        return calendar.getTime();
    }
}
