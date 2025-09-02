/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lxg.prepayment.dao;

import com.lxg.prepayment.model.PpStagingModel;
import com.lxg.prepayment.model.PpTranModel;
import com.sains.framework.base.BaseDAO;
import java.util.List;

/**
 *
 * @author joveni.h
 */
public interface PrepaymentDAO extends BaseDAO<PpStagingModel> {

//     public void processData(PpStagingModel model, PpBalanceModel ppBalanceModel, String Id, String accType, String acc,  Boolean commitTransaction) throws Exception;
//     public void insert(PpStagingModel model, String Id, String Identifier) throws Exception;
//     public void update(PpStagingModel ppStagingModel, PpBalanceModel ppBalanceModel, String Id, String Identifier) throws Exception;
//     public boolean checkAccountExist(String Id) throws Exception;
//     public void checkBalanceExist(PpStagingModel ppStagingModel, String Id, String accType) throws Exception;
//     public void updateCase(PpStagingModel ppStagingModel) throws Exception;
     public Boolean startTransaction(String transaction, String Id, String acc_type, Object obj, String Status, Boolean commitTransaction, String us_user_id) throws Exception;
//     public String checkCompanyAccountRights(String usId, String method) throws Exception;
//     public void validateCompanyAccountRights(String coId, String transaction) throws Exception;
//     public String insertTopup (PpTopupModel topupInfo) throws Exception;
//     public int post2RVS(List<PpTranModel> tranList) throws Exception; //ahmadni 21/9/2011
//     public Boolean startMultipleTransaction(String transaction, String Id, String acc_type, List<ElPaymentModel> paymentModelList, String Status, Boolean commitTransaction) throws Exception;//ahmadni 24/1/2014


}
