/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.dao;

import com.sains.framework.base.BaseCUDDAO;
import com.utimaps.model.ApplicationPModel;

/**
 *
 * @author Aiman
 */
public interface UtilAppCUDDAO extends BaseCUDDAO<ApplicationPModel> {
    public void manualInsert(ApplicationPModel model, String pbUserId) throws Exception;
    public void updateForWorkflow(ApplicationPModel model, String subType) throws Exception;
    public void updateForPayment(ApplicationPModel model) throws Exception;
}
