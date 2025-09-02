package com.sains.common.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.*;
import com.sains.common.util.SystemConstants;


/**
 * This class provides formatting methods on common objects such as string, date, numeric into text.
 * 
 * @version $Revision: 1.15 $
 */

public class Formatter {

	//private static final Logger logger = Logger.getLogger(Formatter.class);

	public static final String DATE_PATTERN = "dd MMM yyyy";
	public static final String TIME_PATTERN = "hh:mm a";
	public static final String TIMESTAMP_PATTERN = DATE_PATTERN + ' ' + TIME_PATTERN;
    public static final String REPORT_TIMESTAMP_PATTERN = "E, dd MMM yyyy" + ' ' + TIME_PATTERN +  ' ' + "z";
	public static final String CURRENCY_PATTERN = "#,##0.00";
	public static final String DECIMAL_PATTERN = "#,##0.0000";
	public static final String NUMBER_PATTERN = "#,##0";
	public static final String CURRENCY_PATTERN_PLAIN = "##0.00";
	public static final String DECIMAL_PATTERN_PLAIN = "##0.0000";
	public static final String CURRENCY_PATTERN_PLAIN2 = "##0";

	/**
	 * Add leading or trailing zero to the given value.
	 * 
	 * @param value String
	 * @param length int
	 * @param trail <code>true</code> if trailing zero; <code>false</code> otherwise
	 * @return String
	 */	
	public static String appendZero(String value, int length, boolean trail) {
		if (value == null) {
			return "";
		}
		if (trail) { // if trailing zero
			for (int j = value.length(); j < length; j++) {
				value += '0';
			}
		} else {
			for (int j = value.length(); j < length; j++) {
				value = '0' + value;
			}
		}
		return value;
	}	

	/**
	 * Add leading or trailing blank space.
	 * @param value String
	 * @param length int 
	 * @param align <code>true</code> if left-aligned; <code>false</code> if right-aligned
	 * @return String
	 */		
	public static String alignText(String value, int length, boolean align) {
		/*if (value.length() > length) {
			return value.substring(0, length);
		}*/
		for (int j = value.length(); j < length; j++) {
			if (align) {
				value += ' ';
			} else {
				value = ' ' + value;
			}
		}
		return value;
	}	

  	/**
	 * Wrap the given argument for proper alignment on html. Replace the
	 * carriage return (\r) to <code><br</code>>.
	 * 
	 * @param arg String
	 * @return String
	 */
	public static String wrapText(String value) {
		if (!Validator.isEmpty(value)) {
			return value.replaceAll(SystemConstants.NEW_LINE, "<br>");
		}
		return "";
	}
	
	/**
	 * Wrap the given argument for proper alignment on html with specified length. 
	 * Replace the carriage return (\r) to <code><br</code>>.
	 * 
	 * @param value
	 * @param length
	 * @return
	 */
	public static String wrapText(String value, int length) {
		if (!Validator.isEmpty(value)) {
			String[] strarr = value.split(SystemConstants.NEW_LINE);
			String newvalue = "";
			for(int i=0; i<strarr.length; i++) {
				if(strarr[i].length()>length) {
					String[] substr = strarr[i].split(" ");
					String str = "";
					for( int j=0, count=length; j< substr.length; j++) {
						if(count >= substr[j].length()) {
							str = str.concat(substr[j]);
							str = str.concat(" ");
							count=count-(substr[j].length()+1);
						} else {
							str = str.concat("<br>");
							str = str.concat(substr[j]);
							str = str.concat(" ");
							count=length;
							count=count-(substr[j].length()+1);
						}
					}
					newvalue=newvalue.concat(str);
				} else {
					newvalue=newvalue.concat(strarr[i]);
				}
				newvalue=newvalue.concat("<br>");
			}
			newvalue=newvalue.substring(0, newvalue.lastIndexOf("<br>"));
			return newvalue;
		}
		return "";
	}

	/**
	 * Replace the pattern in the given key with the new value.
	 * 
	 * @param key String. the original text.
	 * @param pattern String. text to be replaced.
	 * @param value String. new text to replace the pattern.
	 * @return String
	 */
	public static String replaceParameter(String key, String pattern, String newValue) {
		pattern = "\\{" + pattern + "\\}";
		org.apache.regexp.RE re = new org.apache.regexp.RE(pattern);
		return re.subst(key, (newValue) != null ? newValue : "");
	}
	
	public static String replaceParameter(String key, String[] params) {
		for (int i = 0; i < params.length; i++) {
			key = Formatter.replaceParameter(key, String.valueOf(i), params[i]);
		}
		return key;
	}
	/**
	 * Format the given Timestamp object into Date format with the default pattern.
	 * 
	 * @param timestamp java.sql.Timestamp
	 * @return String
	 */
	public static String formatDate(java.sql.Timestamp timestamp) {
		return formatDate(timestamp, DATE_PATTERN);
	}
	
	public static String formatDate(java.sql.Timestamp timestamp, String pattern) {
            //System.out.println("pattern "+pattern);
		if (timestamp != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(new Date(timestamp.getTime()));
		}
		return "";
	}
        
    // ThoTH @ 23-Jun-2014
    public static Timestamp formatDate(String pDate, String pattern) throws Exception {
        if (pDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date parsedDate = sdf.parse(pDate);
            return new java.sql.Timestamp(parsedDate.getTime());
        }
        return null;
    }

    public static String formatReportDate(java.sql.Timestamp timestamp) {
		return formatDate(timestamp, REPORT_TIMESTAMP_PATTERN);
	}

	/**
	 * Format the given Date object into Date format with the default pattern.
	 * 
	 * @param date java.util.Date
	 * @return String
	 */
	public static String formatDate(java.util.Date date) {
		return formatDate(date, DATE_PATTERN);
	}

	public static String formatDate(java.util.Date date, String pattern) {
		if (date != null) {
			return formatDate(new java.sql.Timestamp(date.getTime()), pattern);
		}
		return "";
	}

   public static String formatReportDate(java.util.Date date) {
		return formatDate(date, REPORT_TIMESTAMP_PATTERN);
	}

	/**
	 * Format the given Timestamp object with the default pattern. 
	 * 
	 * @param timestamp java.sql.Timestamp
	 * @return String
	 */
	public static String formatTimestamp(java.sql.Timestamp timestamp) {
		return formatTimestamp(timestamp, TIMESTAMP_PATTERN);
	}

   public static String formatTimestamp(java.sql.Timestamp timestamp, String pattern) {
        if (timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(new Timestamp(timestamp.getTime()));
        }
        return "";
    }

	/**
	 * Format the given string object into currency format with the default pattern.
	 * 
	 * @param decimal String
	 * @return String
	 */
	public static String formatCurrency(String decimal) {
            return formatDecimal(decimal.replaceAll(",", ""), CURRENCY_PATTERN);
	}

	/**
	 * Format the given decimal object into the currency format with the default pattern.
	 * 
	 * @param decimal java.math.BigDecimal
	 * @return String
	 */
	public static String formatCurrency(Double decimal) {
            if (decimal == null) {
                return "";
            }
            return formatDecimal(decimal.toString(), CURRENCY_PATTERN);
	}

	/**
	 * Format the given string object with the given pattern.
	 * 
	 * @param decimal java.math.BigDecimal
	 * @param pattern String
	 * @return String
	 */
	public static String formatDecimal(String decimal, String pattern) {
            try {
		decimal = String.valueOf(DoubleUtil.round(Double.parseDouble(decimal), pattern));
		if (!Validator.isEmpty(decimal)) {
			NumberFormat nf = new DecimalFormat(pattern);
			return nf.format(Double.parseDouble(decimal));
		}
            } catch (Exception e) {
            }
            return "";
	}	

	/**
	 * Format the given string object with the given pattern.
	 * 
	 * @param decimal java.math.BigDecimal
	 * @param pattern String
	 * @return String
	 */
	public static String formatDecimal(String decimal) {
		return formatDecimal(decimal, DECIMAL_PATTERN);
	}
	
	public static String formatInteger(String integer) {
		return formatDecimal(integer, NUMBER_PATTERN);
	}
	
	public static String firstUpper(String s) {
		return Character.toUpperCase(s.charAt(0))+s.substring(1).toLowerCase();
	}
	public static String lastUpper(String s) {
		int n = s.length();
		return s.substring(0,n-1)+Character.toUpperCase(s.charAt(n-1));
	}  
	  
	public static String initcap(String input) {    
		Pattern p = Pattern.compile ("("+
				"(\\w?')|"+
				"(\\w|')+"+
		")");
		Matcher m = p.matcher (input);
		StringBuffer sb = new StringBuffer ();
		while (m.find ()) {
			m.appendReplacement (sb, firstUpper(m.group()));
		}
		m.appendTail (sb);

		p = Pattern.compile ("([Mm][Aa]?[Cc](\\w))");
		m = p.matcher (sb.toString());
		sb = new StringBuffer ();
		while (m.find ()) {
			m.appendReplacement (sb, lastUpper(m.group()));
		}
		m.appendTail (sb);
		return sb.toString();
	}
}