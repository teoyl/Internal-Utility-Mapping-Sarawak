package com.sains.common.util;

import com.sains.framework.base.LogFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.regexp.RE;


/**
 * This class provides validation methods on common objects such as string
 * 
 * @version $Revision: 1.8 $
 */
public class Validator {
	private static final Log log = LogFactory.getLog(Validator.class);

	/**
	 * Validate the given argument for null or empty value. 
	 * Return <code>true</code> if null or empty value; <code>false</code> otherwise.
	 * 
	 * @param arg String
	 * @return boolean
	 */
	public static boolean isEmpty(String arg) {
		return (arg == null || arg.trim().length() == 0);
	}
        public static boolean notEmpty(String arg) {
            return !isEmpty(arg);
	}
        
        public static boolean isEqualsAndNotNull(String arg1, String arg2) throws Exception {
            if (arg1==null || arg2==null) {
                return false;
            } else {
                return arg1.equals(arg2);
            }
	}

	/**
	 * Validate the given argument for valid email address. 
	 * Return <code>true</code> if valid; <code>false</code> otherwise.
	 * 
	 * @param arg String
	 * @return boolean
	 */
	public static boolean isEmail(String arg) {
		if (!isEmpty(arg)) {
			RE re1 = new RE(".+\\@.+\\..+"); // email format
			RE re2 = new RE("^[a-zA-Z0-9_@.\\-]+$"); // invalid characters
			RE re3 = new RE("^[a-zA-Z]"); // start with non-alphabetic character
			RE re4 = new RE("[a-zA-Z]$"); // end with non-alphabetic character

			return (re1.match(arg) && re2.match(arg) && re3.match(arg) && re4.match(arg));
		}
		return true;
	}

	/**
	 * Validate given argument for valid integer or decimal value.
	 * @param arg String
	 * @return boolean true if parameter is number, false if parameter contains word characters.
	 */
	public static boolean isNumber(String arg) {
		if (!isEmpty(arg)) {
			//RE re = new RE("^[\\-_0-9\\.^\\-]+$");
			RE re = new RE("^[\\-_0-9\\.]+$");
			if (re.match(arg)) {
				//check for multiple points
				int idx = arg.indexOf(".");
				if (idx > -1) {
					if ((arg.indexOf(".", idx + 1) > -1)) {
						return false;
					}
					if (idx == arg.length()-1) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean isDate(String arg) {
		try {
			DateUtil.getDate(arg);
		} catch (java.text.ParseException e) {
			return false;
		}
		return true;
	}
	
	public static boolean isInteger(String arg) {
		if (!isEmpty(arg)) {
			RE re = new RE("^[\\-_0-9]\\d*$");
			if (re.match(arg)) {
				return true;
			}
			return false;
		}
		return false;
	}


    public static boolean isMatching(String arg1, String arg2) {
         if ( arg1.trim().equalsIgnoreCase(arg2.trim()) ){
                return true;
             }
         return false;
	}

    public static boolean isMatchingCase(String arg1, String arg2) {
         if ( arg1.trim().equals(arg2.trim()) ){
                return true;
             }
         return false;
	}

    public static boolean isNewICValid(String strNewIC){
        String delimiter = "-";
        String[] vArrayNewIC = new String[3];
        
        if(strNewIC.contains(delimiter)) {
            vArrayNewIC = strNewIC.split(delimiter);
        } else if(strNewIC.length() == 12) {
            vArrayNewIC[0] = strNewIC.substring(0,6);
            vArrayNewIC[1] = strNewIC.substring(6,8);
            vArrayNewIC[2] = strNewIC.substring(8,12);
        }
        
        //Split NewIC into Array
//        for(int i =0; i < vArrayNewIC.length ; i++) {
//            System.out.println(vArrayNewIC[i]);
//        }

        if (vArrayNewIC.length != 3) {
            return false;
        }

        if (!isNumber(vArrayNewIC[0])) {
            return false;
        }
        if (!isNumber(vArrayNewIC[1])) {
            return false;
        }
        if (!isNumber(vArrayNewIC[2])) {
            return false;
        }

        if (vArrayNewIC[0].length() != 6) {
            return false;
        }
        if (vArrayNewIC[1].length() != 2) {
            return false;
        }
        if (vArrayNewIC[2].length() != 4) {
            return false;
        }

        int vDay = Integer.parseInt(vArrayNewIC[0].substring(4,6), 10);
        int vMonth = Integer.parseInt(vArrayNewIC[0].substring(2,4), 10);
        // int v2DigitsYear = Integer.parseInt(vArrayNewIC[0].substring(0,2), 10);

        // v2DigitsYear = (v2DigitsYear == 0) ? "00" : v2DigitsYear;
        // var vFullYear = (parseInt(v2DigitsYear) < 10) ? ("20" + v2DigitsYear) : ("19" + v2DigitsYear);

        if (vDay <= 0) {
            return false;
        }
        if (vDay > 31) {
            return false;
        }
        if (vMonth <= 0) {
            return false;
        }
        if (vMonth > 12) {
            return false;
        }
        if ((vMonth == 4 || vMonth == 6 || vMonth == 9 || vMonth == 11) && vDay > 30 ) {
            return false;
        }
        // if (vMonth == 2) {
        // if (!IsLeapYear(vFullYear) && (vDay > 28)) {
        // //alert(strFieldLabel + " - Tarikh hari tidak sah.");
        // return false;
        // }
        // if (IsLeapYear(vFullYear) && (vDay > 29)) {
        // //alert(strFieldLabel + " - Tarikh hari tidak sah.");
        // return false;
        // }
        return true;
    }
    
    // ThoTH @ 20-Jun-2015
    public static boolean isBetween(java.sql.Timestamp tsMain, java.sql.Timestamp tsStart, java.sql.Timestamp tsEnd){
        if (tsMain.compareTo(tsStart) >= 0 && tsMain.compareTo(tsEnd) <= 0)    return Boolean.TRUE;
        return Boolean.FALSE;
    }
    
    public static boolean isPhoneNumberValid(String phoneNumber){
        boolean isValid = false;
//             ^         # Assert position at the beginning of the string.
//            \+        # Match a literal "+" character.
//            (?:       # Group but don't capture...
//              [0-9]   #   Match a digit.
//              \x20    #   Match a space character...
//                ?     #     Between zero and one time.
//            )         # End the noncapturing group.
//              {6,14}  #   Repeat the preceding group between 6 and 14 times.
//            [0-9]     # Match a digit.
//            $         # Assert position at the end of the string.
//         Format : Number only 6-14 digits, no empty space

        //Initialize reg ex for phone number.
        String expression = "^(?:[0-9]){6,14}[0-9]$";
//      "^(?:[0-9] ?){6,14}[0-9]$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    
    public static void validateDynamicSortOrder(String sortOrder) throws Exception {
        switch (sortOrder.trim().toLowerCase()) {
            case "a":
            case "d":
            case "asc":
            case "desc":
                break;
            default:
                throw new IllegalArgumentException("Illegal argument of dynamicSortOrder:" + sortOrder);
        }
    }

}