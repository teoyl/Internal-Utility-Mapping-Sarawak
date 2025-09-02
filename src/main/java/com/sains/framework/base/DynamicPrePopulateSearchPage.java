package com.sains.framework.base;

import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.Formatter;
import com.sains.framework.base.web.DynamicAction;
import java.util.Date;

import java.util.Map;

public class DynamicPrePopulateSearchPage{
    public static int count = 0;
    public String prePopulate_Leave_HR_Pending(DynamicAction dynamicAction, Map param) throws Exception{
        dynamicAction.getSearchFieldsDateData().put("search_start_date", Formatter.formatDate(new Date(), dynamicAction.DEFAULT_DATE_POPUP_FORMAT_java));
        return ActionSupport.SUCCESS;
    }
    public String prePopulateTest(DynamicAction dynamicAction, Map param) throws Exception{
        System.out.println("- - - - this is prePopulateTest - - - -");
//        dynamicAction.getSearchFieldsDataMap().put("search_moal_id", "idid");
        return ActionSupport.SUCCESS;
    }
}

