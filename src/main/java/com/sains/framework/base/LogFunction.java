package com.sains.framework.base;

import org.apache.logging.log4j.LogManager;

public class LogFunction {
    public static void logDebug(Class logClass, String msg, Exception e){
        if (e == null){
            LogManager.getLogger(logClass).debug(msg);
        } else {
            LogManager.getLogger(logClass).debug(msg, e);
        }
    }

    public static void logError(Class logClass, String msg, Exception e){
        if (e == null){
            LogManager.getLogger(logClass).error(msg);
        } else {
            LogManager.getLogger(logClass).error(msg, e);
        }
    }

    public static void logInfo(Class logClass, String msg){
        LogManager.getLogger(logClass).info(msg);
    }
    
    public static int print_level = 3;
    public static void printInfo(String msg) {
        if (print_level > 0) {
            System.out.println(msg);
        }
    }   
    public static void printDebug(String msg) {
        if (print_level > 1) {
            System.out.println(msg);
        }
    }
    public static void printError(String msg) {
        if (print_level > 2) {
            System.out.println(msg);
        }
    }
}