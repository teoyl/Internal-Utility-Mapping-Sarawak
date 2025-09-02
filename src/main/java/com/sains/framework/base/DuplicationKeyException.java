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
public class DuplicationKeyException extends BaseException{
    public DuplicationKeyException(String... args){
        setMsgCode("errors.duplicationKey");
        args[0] = actionSupport.getText(args[0]);
        setArgs(args);
    }
}