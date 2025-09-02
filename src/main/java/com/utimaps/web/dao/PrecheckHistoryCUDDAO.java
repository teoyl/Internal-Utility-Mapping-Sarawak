/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.web.dao;

import com.sains.framework.base.BaseCUDDAO;
import com.utimaps.model.ApplicationPModel;
//import com.utimaps.model.JobDetailModel;
import com.utimaps.model.PrecheckHistoryModel;
import java.util.Map;

/**
 *
 * @author yonglai
 */
public interface PrecheckHistoryCUDDAO extends BaseCUDDAO<PrecheckHistoryModel>{
    public void histInsert(PrecheckHistoryModel model) throws Exception;
}
