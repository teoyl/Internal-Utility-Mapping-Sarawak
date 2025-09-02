/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author lenovo
 */
public class PropertyGetter {
    private final ResourceBundle bundle = ResourceBundle.getBundle("package", new Locale("", ""));
    public String getPropertyText(String param){
        try {
            return bundle.getString(param);
        } catch (Exception e) {
        }
        return param;
    }
    
    private static ResourceBundle package_bundle = null;
    public static String getPropertyText_package(String param){
        if (package_bundle == null) {
            package_bundle = ResourceBundle.getBundle("package", new Locale("", ""));
        }
        try {
            return package_bundle.getString(param);
        } catch (Exception e) {
        }
        return param;
    }
}
