/*
 * Created on Jun 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sains.common.util;

import com.sains.framework.base.Debug;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author IS006
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataTypeConverter {


	public static double convertStringToDouble(String value) throws NumberFormatException{
		return (Double.valueOf(value).doubleValue());	
	}
	
	public static String convertDoubleToString(Double value) throws NumberFormatException{
		return (String.valueOf(value));
	}
	
	public static int convertStringToInt(String value) throws NumberFormatException{
		return (Integer.parseInt(value));	
	}
	
	public static String convertIntToString(int value) throws NumberFormatException{
		return (String.valueOf(value));
	}
	
	public static Timestamp convertStringToTimestamp(String value){	
		return Timestamp.valueOf(value);
	}
	public static String convertTimestampToString(Timestamp value){
		return String.valueOf(value);
	}
	
	//public static
	public static Date convertStringToDate(String value)throws ParseException{
		//convert string to date 
		Date date = new Date();
		try{
		DateFormat formatter = new SimpleDateFormat("yyyy:MM:dd");
			return formatter.parse(value);
		}catch(ParseException pe){
        	Debug.printError("ParseException: " + pe.getMessage());
        	return null;
        }	
	}
}
