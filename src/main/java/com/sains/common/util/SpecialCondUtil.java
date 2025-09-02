package com.sains.common.util;

// import java.math.*;
import java.util.StringTokenizer;

public class SpecialCondUtil {

    public static String mergeSpecialCond(String text, String param) {
        text = text.replace("\t", " ");
        text = text.replace("$", "<<temp>>");
        StringTokenizer st = new StringTokenizer(param, "$");
        while (st.hasMoreTokens()) {
            text = text.replaceFirst("<<temp>>", st.nextToken().trim());
        }
        text = text.replace("<<temp>>", "");
        return text;
    }

    public static String convertRomanNumbering(String str) {
        String strNew = "";
        if (!str.equals("")) {
            if (str.equals("1")) {
                return strNew = "i";
            } else if (str.equals("2")) {
                return strNew = "ii";
            } else if (str.equals("3")) {
                return strNew = "iii";
            } else if (str.equals("4")) {
                return strNew = "iv";
            } else if (str.equals("5")) {
                return strNew = "v";
            } else if (str.equals("6")) {
                return strNew = "vi";
            } else if (str.equals("7")) {
                return strNew = "vii";
            } else if (str.equals("8")) {
                return strNew = "viii";
            } else if (str.equals("9")) {
                return strNew = "ix";
            } else if (str.equals("10")) {
                return strNew = "x";
            } else if (str.equals("11")) {
                return strNew = "xi";
            } else if (str.equals("12")) {
                return strNew = "xii";
            } else if (str.equals("13")) {
                return strNew = "xiii";
            } else if (str.equals("14")) {
                return strNew = "xiv";
            } else if (str.equals("15")) {
                return strNew = "xv";
            } else if (str.equals("16")) {
                return strNew = "xvi";
            } else if (str.equals("17")) {
                return strNew = "xvii";
            } else if (str.equals("18")) {
                return strNew = "xviii";
            } else if (str.equals("19")) {
                return strNew = "xix";
            } else if (str.equals("20")) {
                return strNew = "xx";
            } else if (str.equals("21")) {
                return strNew = "xxi";
            } else if (str.equals("22")) {
                return strNew = "xxii";
            } else if (str.equals("23")) {
                return strNew = "xxiii";
            } else if (str.equals("24")) {
                return strNew = "xxiv";
            } else if (str.equals("25")) {
                return strNew = "xxv";
            } else if (str.equals("26")) {
                return strNew = "xxvi";
            } else if (str.equals("27")) {
                return strNew = "xxvii";
            } else if (str.equals("28")) {
                return strNew = "xxviii";
            } else if (str.equals("29")) {
                return strNew = "xxix";
            } else if (str.equals("30")) {
                return strNew = "xxx";
            } else {
                strNew = "";
                return strNew;
            }
        } else {
            return strNew;
        }
    }

    public static String convertAlphabet(String str) {
        String strNew = "";
        if (!str.equals("")) {
            if (str.equals("1")) {
                return strNew = "a";
            } else if (str.equals("2")) {
                return strNew = "b";
            } else if (str.equals("3")) {
                return strNew = "c";
            } else if (str.equals("4")) {
                return strNew = "d";
            } else if (str.equals("5")) {
                return strNew = "e";
            } else if (str.equals("6")) {
                return strNew = "f";
            } else if (str.equals("7")) {
                return strNew = "g";
            } else if (str.equals("8")) {
                return strNew = "h";
            } else if (str.equals("9")) {
                return strNew = "i";
            } else if (str.equals("10")) {
                return strNew = "j";
            } else if (str.equals("11")) {
                return strNew = "k";
            } else if (str.equals("12")) {
                return strNew = "l";
            } else if (str.equals("13")) {
                return strNew = "m";
            } else if (str.equals("14")) {
                return strNew = "n";
            } else if (str.equals("15")) {
                return strNew = "o";
            } else if (str.equals("16")) {
                return strNew = "p";
            } else if (str.equals("17")) {
                return strNew = "q";
            } else if (str.equals("18")) {
                return strNew = "r";
            } else if (str.equals("19")) {
                return strNew = "s";
            } else if (str.equals("20")) {
                return strNew = "t";
            } else if (str.equals("21")) {
                return strNew = "u";
            } else if (str.equals("22")) {
                return strNew = "v";
            } else if (str.equals("23")) {
                return strNew = "w";
            } else if (str.equals("24")) {
                return strNew = "x";
            } else if (str.equals("25")) {
                return strNew = "y";
            } else if (str.equals("26")) {
                return strNew = "z";
            } else {
                strNew = "";
                return strNew;
            }
        } else {
            return strNew;
        }
    }
}
