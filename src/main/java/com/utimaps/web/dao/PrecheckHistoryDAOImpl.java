/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utimaps.web.dao;

import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.Debug;
import com.utimaps.model.JobDetailModel;
import com.utimaps.model.PrecheckHistoryModel;

/**
 *
 * @author Aiman
 */
public class PrecheckHistoryDAOImpl extends BaseDAOImpl<PrecheckHistoryModel> implements PrecheckHistoryDAO {

    public void histInsert(PrecheckHistoryModel model) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        try {
            Debug.printDebug("runnning insert history daoimpl~~~~~");
            dao.setSession(getSession());
            beginBatchTransaction();
            getSession().save(model);
            commitBatchTransaction();
        } catch (Exception e) {
            rollbackBatchTransaction();
            e.printStackTrace();
            CommonFunction.writeLogFile(e.getStackTrace(), "SubmissionAction", "SubmissionJobDAOImpl", "updateApplication");
        } finally {
            dao.closeAllSession();
            closeSession();
        }
    }
}
