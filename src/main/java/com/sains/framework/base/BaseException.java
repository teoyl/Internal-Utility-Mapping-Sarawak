/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sains.framework.base;

import com.opensymphony.xwork2.ActionSupport;

/**
 *
 * @author lenovo
 */
public class BaseException extends Exception {
    private String msgCode = "";
    private String[] args = null;
    public static ActionSupport actionSupport = new ActionSupport();

    public static void setActionSupport(ActionSupport actionSupport) {
        BaseException.actionSupport = actionSupport;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    @Override
    public String getMessage(){
        try {
            return actionSupport.getText(msgCode, args);
        } catch (Exception e) {
        }
        return msgCode;
    }
}
