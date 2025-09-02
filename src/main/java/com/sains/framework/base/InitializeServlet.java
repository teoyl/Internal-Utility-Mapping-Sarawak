/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sains.framework.base;

import javax.servlet.http.HttpServlet;

/**
 *
 * @author lenovo
 */
public class InitializeServlet extends HttpServlet {
    //private PaymentCheck paymentCheck = new PaymentCheck();
    public InitializeServlet(){
        //Disble the checking thread
        /*boolean found = false;
        for (Thread t : Thread.getAllStackTraces().keySet()){
            if (t.getName().equals("PaymentCheck")){
                found = true;
                break;
            }
        }
        if (!found) {
            paymentCheck.start();
        }*/
    }

/*    @Override
    protected void finalize() throws Throwable {
//        PaymentCheck.keepChecking = false;
//        paymentCheck = null;
        super.finalize();
    }*/
}