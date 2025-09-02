/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sains.framework.base;

import com.sains.framework.base.BaseException;

/**
 *
 * @author thensw
 */
public class DynamicPreSearchException extends BaseException{
    public DynamicPreSearchException(String msgCode, String... args){
        setMsgCode(msgCode);
        if (args != null){
            int i = 0;
            for (String arg : args){
                args[i] = actionSupport.getText(args[i++]);
            }
        }
        setArgs(args);
    }
}