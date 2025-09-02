package com;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author lenovo
 */
public class SysConf {
    private static ResourceBundle bundle = null;
    public static void list(){
        for (String key : bundle.keySet()) {
            System.out.println(key + "= " + bundle.getString(key));
        }
    }
    public static String get(String param){
        if (bundle == null) {
            try {
                ResourceBundle systemProp = ResourceBundle.getBundle("env", new Locale("", ""));
                bundle = ResourceBundle.getBundle(systemProp.getString("prop.name"), new Locale("", ""));
            } catch (Exception e) {
                bundle = ResourceBundle.getBundle("system", new Locale("", ""));
            }
        }
        try {
            return bundle.getString(param);
        } catch (Exception e) {
        }
        return param;
    }
}
