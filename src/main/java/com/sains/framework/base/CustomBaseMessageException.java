/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sains.framework.base;

import com.sains.framework.base.BaseException;

/**
 * Support package.properties, and multiple parameters
 * Only display ONE line in JSP
 * 
 * e.g. 
 *    throw new CustomBaseException("cart.payableItemNotFound");
 *    throw new CustomBaseException("errors.invalidPOD_paymentStatus", "PodStatus_Type." + pd.getPod_status());
 *
 * if no matching found in package.properties, it will display the words pass in
 *
 * e.g.
 *    throw new CustomBaseException("I am TTH");
 *
 *
 * @author thensw
 */
public class CustomBaseMessageException extends BaseException{
    public CustomBaseMessageException(String msgCode, String... args){
        setMsgCode(msgCode);
        if (args.length > 0) {
            int idx = 0;
            for (String strArg : args) {
                args[idx++] = actionSupport.getText(strArg);
            }
        }
        setArgs(args);
    }
}
