package com.sains.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
// import java.io.*;
// import java.util.*;
// import java.util.regex.*;
import java.text.*;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
// import java.math.*;


/**
 * This class provides rounding methods on Double or Float objects
 * 
 * 
 */
public class DoubleUtil {
	private static DecimalFormat numberFormat = new DecimalFormat("###0.00");
	private static DecimalFormat financeNumberFormat = new DecimalFormat("#,##0.00");

	private static FieldPosition f = new FieldPosition(0);
	private final static double epsilon = Math.pow(10, -5);
	
	private final static int defaultMinimumFractionDigits = 2;
	private final static int defaultMaximumFractionDigits = 2;
	
	private static final String[] majorNames = 
	{
	    "",
	    " Thousand",
	    " Million",
	    " Billion",
	    " Trillion",
	    " Quadrillion",
	    " Quintillion"
	};
	
    private static final String[] tensNames = 
    {
	    "",
	    " Ten",
	    " Twenty",
	    " Thirty",
	    " Forty",
	    " Fifty",
	    " Sixty",
	    " Seventy",
	    " Eighty",
	    " Ninety"
	};
	
    private static final String[] numNames = 
    {
	    "",
	    " One",
	    " Two",
	    " Three",
	    " Four",
	    " Five",
	    " Six",
	    " Seven",
	    " Eight",
	    " Nine",
	    " Ten",
	    " Eleven",
	    " Twelve",
	    " Thirteen",
	    " Fourteen",
	    " Fifteen",
	    " Sixteen",
	    " Seventeen",
	    " Eighteen",
	    " Nineteen"
	};
    
    /**
	 * Converts a Java double into a string, with two decimal places.
	 */
  	public static String getFormattedQuantityFromDouble(double value)
	{
		return getFormattedQuantityFromDouble(value, defaultMinimumFractionDigits, defaultMaximumFractionDigits);
	}
	
	/***
	 * Converts a Java double into a string, with two decimal places.
	 ***/
  	public static String getFormattedQuantityFromDouble(double value, int minimumFractionDigits, int maximumFractionDigits)
	{
		return getFormattedQuantityFromDouble(value, minimumFractionDigits, maximumFractionDigits, false);
	}	
	
	/***
	 * Converts a Java double into a string, with two decimal places.
	 ***/
  	public static String getFormattedQuantityFromDouble(double value, 
  			int minimumFractionDigits, int maximumFractionDigits, boolean useFinanceNumberFormat)
	{
    	StringBuffer str = new StringBuffer();
    	if(compareDouble(value, 0) == 0)
    		value = Math.abs(value);
    		
    	/*************************************************************************
    	 * AZLIN'S NOTE
    	 * ============
    	 * 
    	 * JDK 1.4.2 default mode for rounding is half-even rounding 
    	 * i.e. always round towards the nearest neighbour unless both neighbours
    	 * are equidistant, in which case, round towards the even neighbor.
    	 *
    	 * For formatting of finance docs, this is not the most approriate rounding mode.
    	 * Rather, half-up rounding should be used. Since there's no way to change 
    	 * the rounding mode in JDK 1.4.2 and below, an interim solution is to 
    	 * convert the double input to BigDecimal and format it using the setScale 
    	 * method, with scale sets to the maximumFractionDigits.  
    	 *
    	 * Note that in JDK 1.6, get/setRounding methods have been added to 
    	 * NumberFormat. Something to consider when we eventually migrate.
    	 *************************************************************************/
    	BigDecimal bd = new BigDecimal(Double.toString(value));
    	bd = bd.setScale(maximumFractionDigits, BigDecimal.ROUND_HALF_UP);
    	
    	if(useFinanceNumberFormat)
    	{
			financeNumberFormat.setMinimumFractionDigits(minimumFractionDigits);
			financeNumberFormat.setMaximumFractionDigits(maximumFractionDigits);
	    	financeNumberFormat.format(bd.doubleValue(), str, f);
    	}
    	else
    	{
			numberFormat.setMinimumFractionDigits(minimumFractionDigits);
			numberFormat.setMaximumFractionDigits(maximumFractionDigits);
	    	numberFormat.format(bd.doubleValue(), str, f);
    	}
    	
    	return str.toString();
	}	
	
	/**
	 * Compare two double value within the specified tolerance epsilon. 
	 * Returns:
	 *
	 * Less than 0 - If value1 is smaller than value2
	 * Greater than 0 - If value1 is greater than value2.
	 * 0 - If value1 is equal to value 2
	 */
	public static int compareDouble(double value1, double value2)
	{
		double diff = value1 - value2;
		double absDiff = Math.abs(diff);

		if(diff == 0 || (absDiff > 0 && absDiff < epsilon))
			return 0;
		else if(diff < 0)
			return -1;
		else
			return 1;
	}
	
	public static String convertNumberToWords(int number) 
	{
	    /* special case */
	    if (number == 0) 
	    { 
	    	return "Zero"; 
	    }
	
	    String prefix = "";
	
	    if (number < 0) 
	    {
	        number = -number;
	        prefix = "Negative";
	    }
	
	    String soFar = "";
	    int place = 0;
	
	    do 
	    {
	      int n = number % 1000;
	      if (n != 0)
	      {
	         String s = convertLessThanOneThousand(n);
	         soFar = s + majorNames[place] + soFar;
	      }
	      place++;
	      number /= 1000;
	    } 
	    while (number > 0);
	
	    return (prefix + soFar).trim();
	}	
	
	private static String convertLessThanOneThousand(int number) 
	{
	    String soFar;
	
	    if (number % 100 < 20)
	    {
	        soFar = numNames[number % 100];
	        number /= 100;
	    }
	    else 
	    {
	        soFar = numNames[number % 10];
	        number /= 10;
	
	        soFar = tensNames[number % 10] + soFar;
	        number /= 10;
	    }
	    if (number == 0) 
	    	return soFar;
	    return numNames[number] + " Hundred" + soFar;
	}	
	
	
	public static java.lang.String getNumToString(java.lang.Double d) {
		return getNumToString(d.doubleValue());
	}
	
	public static String getNumToString(double d) {
		String formatedString;
		String number;
		String decimal;
		formatedString = getFormattedQuantityFromDouble(d);
		number = formatedString.substring(0, formatedString.indexOf("."));
		number = convertNumberToWords(Integer.parseInt(number));
		decimal = formatedString.substring(formatedString.indexOf(".") + 1);
		if (decimal.equals("00")){
			formatedString = number + " Only";
		} else {
			decimal = convertNumberToWords(Integer.parseInt(decimal));
			formatedString = number + " And Cent " + decimal + " Only";
		}
		
		return formatedString;
	}
	
	/**
	 * To fix the precision of the value
	 * @param value
	 * @return The rounded value in double
	 */
	public static double round(double value) {
		return round(value, 11);
	}
	public static double currency(double value) {
		return round(round(value), 2);
	}
	/**
	 * Round the value to the decimal point defined in the pattern
	 * @param value
	 * @param pattern
	 * @return double
	 */
	public static double round(double value, String pattern) {
		int fraction = pattern.indexOf(".") >= 0 ? 
				pattern.substring(pattern.indexOf(".")+1).length() : 0;
		return round(value, fraction);
	}
	/**
	 * Round the value to the given decimal point
	 * @param value
	 * @param fraction
	 * @return double
	 */
	public static double round(double value, int fraction) {
		return BigDecimal.valueOf(value).setScale(fraction, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static int roundUp(double value) {
		return BigDecimal.valueOf(value).setScale(0, BigDecimal.ROUND_UP).intValue();
	}
	
	/**
	 * Compare two double value within the specified tolerance epsilon. 
	 *
	 * @return 	the value <code>0</code> if <code>value1</code> is 
	 * 			equal to <code>value2</code>; a value less than 
	 * 			<code>0</code> if <code>value1</code> is numerically less 
	 * 			than the <code>value2</code>; and a value greater 
	 * 			than <code>0</code> if <code>value1</code> is numerically greater than <code>value2</code>.
	 *
	 */
	public static int compare(double value1, double value2)
	{
		double diff = value1 - value2;
		double absDiff = Math.abs(diff);

		if(diff == 0 || (absDiff > 0 && absDiff < epsilon))
			return 0;
		else if(diff < 0)
			return -1;
		else
			return 1;
	}
	
	public static String formatNumber(double value, int fraction){
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(fraction);
                nf.setMaximumFractionDigits(fraction);
		return nf.format(value);
	}
        
    //For conversion of 1,000,000 become 1 Million

    private static final String NAMES[] = new String[]{
    "thousand",
    "mil",
    "bil",
    "tri"
       
    };
    private static final BigInteger THOUSAND = BigInteger.valueOf(1000);
    private static final NavigableMap<BigInteger, String> MAP;
    static
    {
        MAP = new TreeMap<BigInteger, String>();
        for (int i=0; i<NAMES.length; i++)
        {
            MAP.put(THOUSAND.pow(i+1), NAMES[i]);
        }
    }   
    
    public static String createString(String value)    
    {
        BigInteger number = new BigInteger(Formatter.formatDecimal(value, Formatter.CURRENCY_PATTERN_PLAIN2));
        Map.Entry<BigInteger, String> entry = MAP.floorEntry(number);
        if (entry == null)
        {
            return "";
        }
        if (number.toString().length() < 7){
            return Formatter.formatDecimal(number.toString(), Formatter.CURRENCY_PATTERN);
        }
        BigInteger key = entry.getKey();
        BigInteger d = key.divide(THOUSAND);
        BigInteger m = number.divide(d);
        float f = m.floatValue() / 1000.0f;
        float rounded = ((int)(f * 100.0))/100.0f;
        String entryValue= entry.getValue();
        if (rounded % 1 == 0)
        {
             if (rounded > 1){
            entryValue = entryValue+".";
        }
            return ((int)rounded) + " "+entryValue;
        }
        if (rounded > 1){
//            entryValue = entryValue+"s";
            entryValue = entryValue+".";
        }
        return f+" "+entryValue;
    }
}
