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
public class DuplicatedFieldException extends BaseException{
    public DuplicatedFieldException(String... args){
        setMsgCode("field.duplicated");
        args[0] = actionSupport.getText(args[0]);
        setArgs(args);
    }
}