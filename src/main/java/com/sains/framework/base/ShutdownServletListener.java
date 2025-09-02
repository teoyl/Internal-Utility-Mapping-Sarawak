/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sains.framework.base;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author lenovo
 */
public class ShutdownServletListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {

    }

    public void contextDestroyed(ServletContextEvent sce) {
        //PaymentCheck.setKeepChecking(false);
    }

}
