package com.sains.framework.base;

import com.sains.common.util.DateUtil;

public class Debug {
    //level 1, only error
    //level 2, print error & Debug
    //level 3, print all
    // 1, 2 = 4
    // 1, 3 = 5
    // 2, 3 = 6
    public static Boolean printFrameworkInfo = Boolean.TRUE;
    public static Boolean printFrameworkDebug = Boolean.TRUE;
    public static Boolean logToFile = Boolean.FALSE;
    public static int debug_level = 3;
    public static void printFrameworkInfo(String msg) {
        if(printFrameworkInfo) {
            System.out.println(DateUtil.getCurrentTimestamp() + " - " + msg);
        }
    }
    public static void printInfo(String msg) {
        if ((debug_level > 2 && debug_level <= 3) || (debug_level == 5 || debug_level == 6)) {
            System.out.println(msg);
        }
    }   
    public static void printDebug(String msg) {
        if ((debug_level > 1 && debug_level <= 3) || (debug_level == 6)) {
            System.out.println(DateUtil.getCurrentTimestamp() + " - " + msg);
        }
    }
    public static void printFrameworkDebug(String msg) {
        if(printFrameworkDebug) {
            System.out.println(DateUtil.getCurrentTimestamp() + " - " + msg);
        }
    }
    public static void printError(String msg) {
        if ((debug_level > 0 && debug_level <= 3) || (debug_level == 5)) {
            System.out.println(msg);
        }
    }
}