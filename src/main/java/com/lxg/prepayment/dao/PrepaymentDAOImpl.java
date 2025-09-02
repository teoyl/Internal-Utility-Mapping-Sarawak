/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lxg.prepayment.dao;

import com.lxg.prepayment.model.PpBalanceModel;
import com.lxg.prepayment.model.PpStagingModel;
//import com.lxg.prepayment.model.PpTopupModel;
import com.lxg.prepayment.model.PpTranModel;
import com.lxg.common.model.PublicUserModel;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.utimaps.model.PaymentModel;
import java.util.Map;

public class PrepaymentDAOImpl extends BaseDAOImpl<PpStagingModel> implements PrepaymentDAO {

    private BaseDAO retrieverDAO = new BaseDAOImpl();
    private String orderCollPoint = "";
    private String orderRemarks = "";
    private String orderApprovalReason = "";
    private String orderNo = "";
    /**
     *
     * @param transactionType = Staging or EBPP Topup or (Storefront or Elodgement)Payment or Account Maintenance
     * @param commitTransaction = True or False
     * @param balanceModel = PpBalanceModel
     * @param stagingModel = PpStagingModel
     * @param obj = if (transaction = 'EL_PYMNT') is 'ElPaymentModel' else is 'PaymentModel'  // ThoTH @ 2-Nov-2011
     *        OLD:: paymentModel = PaymentModel  // transAmount = Payment Amount to be paid - cancelled
     * @param topupModel = topupModel
     * @param updatedBy = RVS (Staging Transaction) or UsUserId (Storefront Payment) or EBPP (EBPP TopUp)
     * @param topupStatus = A-Approved or R-Rejected or E-Expired
     * @param cartModel = Cart
     * @throws Exception
     */
    private synchronized void processData(String transactionType, Boolean commitTransaction, PpBalanceModel balanceModel, Object obj, String updatedBy) throws Exception {
//    private synchronized void processData(String transactionType, Boolean commitTransaction, PpBalanceModel balanceModel, PpStagingModel stagingModel, PaymentModel paymentModel, PpTopupModel topupModel, String updatedBy, String topupStatus, Cart cartModel) throws Exception {
//    private synchronized void processData(String transactionType, Boolean commitTransaction, PpBalanceModel balanceModel, PpStagingModel stagingModel, String transAmount, PpTopupModel topupModel, String updatedBy, String topupStatus, Cart cartModel) throws Exception {
        String acc = "";
//        Map sessionMap = ActionContext.getContext().getSession();
//        System.out.println("sessionMap user id "+sessionMap.get("p_loginId"));
//        String strYear = "",strOrderId = "", strPoNo = "",strPodId = "",strNewPodId = "";
//        Calendar cal=Calendar.getInstance();
//        strYear = Integer.toString( cal.get(Calendar.YEAR)) ;
//        boolean adsApv = false, policeApv = false, ssApv = false;
//        boolean adsItemApv = false, policeItemApv = false, ssItemApv = false;
//        String itemDesc = "", adsItemDesc = "";
//        PurchaseDetailDAO pdDAO = new PurchaseDetailDAOImpl();

//        Map mailParam = new HashMap();
//        PublicUser publicUser = null;
//        String emailLink =  "";

        System.out.println("begin process data...");
        System.out.println("transactionType = " + transactionType);

        try {
            // ThoTH @ 2-Nov-2011
            PaymentModel lsPaymentModel = null;
            if (transactionType.equals(SystemConstants.PREPAYMENT_CHECK.TRANSACTION.USJ_PYMNT)) { //Joveni @16.2.2022
                lsPaymentModel = (PaymentModel)obj;
            }
            // ThoTH @ 2-Nov-2011 - END


            if (commitTransaction) {
                beginBatchTransaction();
            }
           
            if (balanceModel == null) { // new account
                acc = SystemConstants.PREPAYMENT_CHECK.ACC_NEW;
                balanceModel = new PpBalanceModel();
//                BaseActionSupport.defaultAddProperties(new PpBalanceModel(), updatedBy);
//                BaseActionSupport.defaultAddProperties(balanceModel, updatedBy);
                System.out.println("bal id"+balanceModel.getBal_id());// insert to PP_Balance
            } else { // current account
                acc = SystemConstants.PREPAYMENT_CHECK.ACC_CURRENT;
//                BaseActionSupport.defaultUpdateProperties(balanceModel, updatedBy);  // update PP_Balance
            }
            
            System.out.println("BALANCE MODEL ID : " + balanceModel.getID());

            // insert to PP_Tran
            PpTranModel tranModel = new PpTranModel();
//            BaseActionSupport.defaultAddProperties(tranModel, updatedBy);
            tranModel.setID(com.sains.framework.base.CommonFunction.getId(16));

            if (transactionType.equals(SystemConstants.PREPAYMENT_CHECK.TRANSACTION.USJ_PYMNT)) {  // TOL Payment : joveni @ 16-Feb-2022
                tranModel.setTran_date(lsPaymentModel.getPayment_date());
                tranModel.setTran_amount(Double.parseDouble("-"+lsPaymentModel.getPayment_amount().toString()));
                tranModel.setTran_type(SystemConstants.PREPAYMENT_CHECK.TRANS_TYPE.USJ_PYMNT); // Utimaps Payment
                tranModel.setTran_desc("");
//                tranModel.setTran_payment_id(lsPaymentModel.getPayment_id());// store payment id for posting to RVS purpose
                tranModel.setBal_id(balanceModel.getID()); // set foreign key
                tranModel.setRvs_posted("N"); 
                getSession().save(tranModel);
                
                System.out.println("--END SAVE TRAN MODEL " + tranModel.getID() + "--");
            }

            // PP_Balance
            if (transactionType.equals(SystemConstants.PREPAYMENT_CHECK.TRANSACTION.USJ_PYMNT) ) {  // Joveni @ 16-Mac-2017
                  Double transAmt = Double.parseDouble(lsPaymentModel.getPayment_amount().toString());
                  Double balanceAmt = balanceModel.getBal_amount();
                  Double newBalanceAmt = balanceAmt - transAmt;
                  balanceModel.setBal_amount(newBalanceAmt);
                  getSession().update(balanceModel); // update PP_Balance
                  
                  System.out.println("new balance amount ::::: " + newBalanceAmt);
             }

            System.out.println("commitTransaction = " + commitTransaction);
            if (commitTransaction) {
                commitBatchTransaction();
            }

        } catch (Exception ex) {
            if (commitTransaction) {
                rollbackBatchTransaction();
            }
            throw ex;
        } finally {
            if (commitTransaction) {
                closeSession();
            }
        }
    }

    /**
     * @param Id = us_id or co_id
     * @param accType = I(Individual) or (C)Company
     * @throws Exception
     */
//    @Override
    private PpBalanceModel checkAccountExist(String Id, String accType) throws Exception {
        PpBalanceModel balanceModel = null;
        if (accType.equals(SystemConstants.PREPAYMENT_CHECK.ACC_TYPE_INDIVIDUAL)) {
//            balanceModel = (PpBalanceModel) retrieverDAO.getModelByCode("us_id", Id, new PpBalanceModel());
            balanceModel = (PpBalanceModel) getObjectByCode("us_id", Id, new PpBalanceModel());
        } else if (accType.equals(SystemConstants.PREPAYMENT_CHECK.ACC_TYPE_COMPANY)) {
//            balanceModel = (PpBalanceModel) retrieverDAO.getModelByCode("co_id", Id, new PpBalanceModel());
            balanceModel = (PpBalanceModel) getObjectByCode("co_id", Id, new PpBalanceModel());
        }

        return balanceModel;
    }

    /**
     *
     * @param transaction = Staging, EBPP Topup, (Storefront or Elodgement)Payment, Account Maintenance
     * @param Id = StagingId (Staging Backend Transaction) or TopUpId (EBPP Topup Backend Transaction) or UsId, CoId (Payment Transaction)
     * @param acc_type = I (Individual Deposit Account) or C (Company Deposit Account)
     * @param obj = if (transaction = 'EL_PYMNT') is 'ElPaymentModel' else is 'PaymentModel'  // ThoTH @ 2-Nov-2011
     * @param Status = A-Approved or R-Rejected or E-Expired or Freezed
     * @param cartModel = Cart  -- Removed by ThoTH @ 25-Oct-2011
     * @param us_user_id  -- Addded by yonglai @ 30-april-2025 --using user id  in records in pp_tran for corp prepayment account.
     * @throws Exception
     */
    @Override
    public synchronized Boolean startTransaction(String transaction, String Id, String acc_type, Object obj, String Status, Boolean commitTransaction, String us_user_id) throws Exception {
//        retrieverDAO.setSession(getSession());
        
        System.out.println("transaction = " + transaction+ " :: id = "+Id);
        Boolean proceed = true;
        try {
            // ThoTH @ 2-Nov-2011
            PaymentModel lsPaymentModel = null;
            if (transaction.equals(SystemConstants.PREPAYMENT_CHECK.TRANSACTION.USJ_PYMNT)) { //Joveni @16.02.2022
                lsPaymentModel = (PaymentModel) obj;
            } 
            // ThoTH @ 2-Nov-2011 - END

//            cartModel.getPurchaseDetailList()
            if (commitTransaction) beginBatchTransaction();

            PublicUserModel publicUserModel = null;
            PpBalanceModel balanceModel = null;
            String keyId = "";
            String accType = "";
//            Boolean proceed = true;

            if (transaction.equals(SystemConstants.PREPAYMENT_CHECK.TRANSACTION.USJ_PYMNT)) { // TOL Payment Transaction : joveni @ 16-Feb-2022
                  keyId = Id;
                  accType = acc_type;

                  System.out.println("keyId = " + keyId);
                  System.out.println("accType = " + acc_type);

//                  CustCompanyModel custCoModel = null;
                  if (!Validator.isEmpty(keyId) && !Validator.isEmpty(accType)){
                      if (accType.equals(SystemConstants.PAYMENT_OPTION.COMPANY_DEPOSIT_ACC)){
//                          custCoModel = checkCompanyExist("co_id",keyId);
//                          if (custCoModel != null ){
                                balanceModel = checkAccountExist(keyId, accType); // check if balance account exist
                                if(balanceModel==null){
                                    proceed = false; System.out.println("Company NOT FOUND.");
                                }
//                           }else {proceed = false; System.out.println("Company NOT FOUND.");}
                      }else if (accType.equals(SystemConstants.PAYMENT_OPTION.INDIVIDUAL_DEPOSIT_ACC)){
                           publicUserModel = checkUserExist("us_id",keyId);
                          if (publicUserModel != null ) { // check if user exist
                              //Prepayment made available for all user - added by ahmadni 9/5/2013
//                              if (publicUserModel.getUs_user_type().equals(SystemConstants.USER_TYPE.PREMIUM)){ // check if PREMIUM user
                                    balanceModel = checkAccountExist(keyId, accType); // check if balance account exist
//                              }else {proceed = false; System.out.println("User IS NOT a PREMIUM user.");}
                          }else{ proceed = false; System.out.println("User NOT FOUND.");}
                      }

                       if (balanceModel != null){  // check if balance exist
                          if (balanceModel.getBal_status().equals( SystemConstants.PREPAYMENT_CHECK.BALANCE_STATUS.ACTIVE)){  // check if balance account active
                              Double balAmt = balanceModel.getBal_amount();
                              Double transAmt = Double.parseDouble(lsPaymentModel.getPayment_amount().toString());
//                              Double newAmt = balAmt - transAmt;
                              if ( balAmt - transAmt >= 0){ // check if balance amount enough
                                  Map sessionMap = ActionContext.getContext().getSession();
                                  if (!Validator.isEmpty(keyId)) {
//                                  if (!Validator.isEmpty(sessionMap.get("p_userId").toString())) {
//                                      processData(transaction, Boolean.FALSE, balanceModel, lsPaymentModel, sessionMap.get("p_loginId").toString());
                                      if (accType.equals(SystemConstants.PAYMENT_OPTION.COMPANY_DEPOSIT_ACC)){
                                        processData(transaction, Boolean.FALSE, balanceModel, lsPaymentModel, us_user_id); //using user id instead of co id
//                                        processData(transaction, Boolean.FALSE, balanceModel, lsPaymentModel, keyId);
                                    }else{
                                        processData(transaction, Boolean.FALSE, balanceModel, lsPaymentModel, publicUserModel.getUs_user_id());
                                    }
                                  }

                              } else {// throw msg if bal amt nt enuf
                                  proceed = false; System.out.println("Balance amount NOT ENOUGH.");
                              }
                          }else {proceed = false; System.out.println("Balance account IS NOT ACTIVE.");}
                       }else{ proceed = false; System.out.println("Balance account NOT EXIST.");}
                  }

             }

//             System.out.println("BEGIN COMMIT. " + proceed);
            if(proceed){
                if (commitTransaction) commitBatchTransaction();
//                System.out.println("END COMMIT.");
            } else {
                // ThoTH: Should use throw Exception as the first place
                //        Don't want to affect other code, so just rollback here, no matter commitTransaction or not.
                rollbackBatchTransaction();
            }

        } catch (Exception ex) {
            if (commitTransaction) rollbackBatchTransaction();
            throw ex;
        } finally {
            if (commitTransaction) {
                closeSession();
            } else {
                setSession(null); // For synchronized(ppDAO) in PurchaseDAOImpl
            }
        }
        return proceed;
    }

    private PublicUserModel checkUserExist(String name, String value) throws Exception {
        PublicUserModel publicUserModel = null;
        publicUserModel = (PublicUserModel) getObjectByCode(name, value, new PublicUserModel());

        if (publicUserModel == null) {
            return null;
        }

        return publicUserModel;
    }

//    private CustCompanyModel checkCompanyExist(String name, String value) throws Exception {
////    private CustCompanyModel checkCompanyExist(String str_co_reg_num) throws Exception {
//        CustCompanyModel custCoModel = null;
//        custCoModel = (CustCompanyModel) getObjectByCode(name, value, new CustCompanyModel());
//
//        if (custCoModel == null) {
//            return null;
//        }
//        return custCoModel;
//    }

//    public String checkCompanyAccountRights(String usId, String method) throws Exception{ // check whether user has the rights to access company's prepayment account
//        boolean recordFound=false;
//        List<UserCompanyModel> userCoList = new ArrayList();
//        // ThoTH @ 4-Nov-2011 : Avoid to use Instance retrievalDAO in this case,
//        // since this method may close by others
////        retrieverDAO.setSession(getSession());   // commented by ThoTH @ 4-Nov-2011 : Not need to share in this case
//        BaseDAO localDAO = new BaseDAOImpl();
//        String coId="";
//
//        try {
//            Map param = new HashMap();
//            param.put("us_id", usId);
//            userCoList =localDAO.list(param, UserCompanyModel.class);
//
////            System.out.println("userCoList size = " + userCoList.size());
//
//            List<PublicSetupGroupModel> userGroupList = new ArrayList();
//    //        List<PublicGroupApplicationModel> groupAppList = new ArrayList();
//    //        List<PublicGroupApplicationRightsModel> groupAppRightsList = new ArrayList();
//    //        List<PublicGroupUserModel> groupUserList = new ArrayList();
//    //        PublicApplicationRightsModel appRightsModel = null;
//
//              for (UserCompanyModel userCo : userCoList){
//                    // get user groups belongs to company
//                    Map paramUG = new HashMap();
//                    paramUG.put("co_id", userCo.getCo_id());
//                    userGroupList = localDAO.list(paramUG, PublicSetupGroupModel.class);
////                    System.out.println("co name = "+userCo.getCustCompany().getCo_name());
//        //            System.out.println("userGroupList = " + userGroupList.size());
//
//                    for (PublicSetupGroupModel ug : userGroupList){
//                       // get group user                     
//                       for(PublicGroupUserModel gu : ug.getGroupUserList()){
////                       groupUserList = ug.getGroupUserList();
////                       for(PublicGroupUserModel gu : groupUserList){
//                           
//                           if(gu.getUs_id().equals(usId)){  // check if user was assigned in the user group
//
//                               // get group applications
//                                for (PublicGroupApplicationModel groupApp : ug.getGroupApplicationList()){
////                                groupAppList = ug.getGroupApplicationList();
////                                for (PublicGroupApplicationModel groupApp : groupAppList){
//
//                                   // get group application rights
//                                   for(PublicGroupApplicationRightsModel groupAppRights : groupApp.getGroupAppRights()){
////                                   groupAppRightsList = groupApp.getGroupAppRights();
////                                   for(PublicGroupApplicationRightsModel groupAppRights : groupAppRightsList){
//
////                                       appRightsModel = new PublicApplicationRightsModel();
////                                       appRightsModel=groupAppRights.getPublicAppRights();
//                                        if (!Validator.isEmpty(groupAppRights.getPublicAppRights().getApp_rights_methods())) { //AiMin@24.5.2021. Remark: Added if checking. Avoid null value causing payment option page cannot load for eLodgement.
//                                           StringTokenizer st = new StringTokenizer(groupAppRights.getPublicAppRights().getApp_rights_methods(), ",");
//                                           while (st.hasMoreTokens()) {
//                                               String temp = st.nextToken().trim();
//                                               if (method.equals(temp)) {
////                                                 System.out.println("temp : " + temp);
//                                                   if (groupAppRights.getHasRight().equals("Y")) {
//                                                       recordFound = true;
//                                                   }
//                                               }
//                                           }
//                                           if (recordFound == true) break;
//                                       }
//                                   }
//
//                                   if(recordFound==true) break;
//                                }
//                           }
//                           if(recordFound==true) break;
//                       }
//                        if(recordFound==true) break;
//                    }
//
//                    if(recordFound==true){
//                        if(Validator.isEmpty(coId)) {
//                            coId=userCo.getCo_id();
//                        } else{
//                            coId += "," +userCo.getCo_id();
//                        }
//                    }
//
//                    recordFound=false;
//        //            if(Validator.isEmpty(coId)) coId=userCoList.get(i).getCo_id(); coId += "," + userCoList.get(i).getCo_id();
//                }
//
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            localDAO.closeSession();
//        }
//      
//        return coId;
//    }

    // Check if has right to use the Selected Company PP A/C : ThoTH @ 2-Nov-2011
//    public void validateCompanyAccountRights(String coId, String transaction) throws Exception {
//        String coIdList = checkCompanyAccountRights((String) ActionContext.getContext().getSession().get("p_userId"), SystemConstants.STOREFRONT_PAYMENT.APP_METHOD.USE_PREPAYMENT_ACC);
//        if (coIdList.indexOf(coId) < 0) {
//            throw new CustomBaseException("errors.invalidAccount");
//        }
//
//        if (transaction.equals(SystemConstants.PREPAYMENT_CHECK.TRANSACTION.EL_PYMNT)) {
//            // Only can use the Login Company PP Account
//            if (!ActionContext.getContext().getSession().get("p_co_id").toString().equals(coId)) {
//                throw new CustomBaseException("errors.invalidAccount");
//            }
//        }
//    }

    //ahmadni
//    public synchronized String insertTopup (PpTopupModel topupInfo) throws Exception{
//        
//        PublicUser publicUser = null;
//        CustCompanyModel company = null;
//        retrieverDAO.setSession(getSession());
//
//        try {
//        beginBatchTransaction();
//        String strToNo = "";
//        String strYear = "";
//        Calendar cal = Calendar.getInstance();
//        strYear = Integer.toString(cal.get(Calendar.YEAR));
//
//        BaseActionSupport.defaultAddProperties_p(topupInfo);
//        strToNo = getRunningSeq2("TO", strYear, "TP_" + strYear.substring(2, 4), "", true, "000000");
//        topupInfo.setTopup_ref_no(strToNo);
//
//        getSession().save(topupInfo);
//
//        //insert activity log - ahmadni 23/6/2014
//        PublicUserDAO puserDAO =  new PublicUserDAOImpl();
//        puserDAO.setSession(getSession());
//        publicUser = (PublicUser) retrieverDAO.getObjectByCode("us_user_id", ActionContext.getContext().getSession().get("p_loginId").toString(), new PublicUser());
//            try {
//                if (topupInfo.getTopup_acc_type().equals("C")) {
//                    company = (CustCompanyModel) retrieverDAO.getObjectById(topupInfo.getTopup_remark(), CustCompanyModel.class);
//                    //puserDAO.logActivity(ActionContext.getContext().getSession().get("p_userName").toString() + " had made top up request of RM"+Formatter.formatCurrency(topupInfo.getTopup_amount())+ " on "+company.getCo_name(),"1");
//                    puserDAO.logActivity(new ActionSupport().getText("log.desc.topup", new String[]{ActionContext.getContext().getSession().get("p_userName").toString(), Formatter.formatCurrency(topupInfo.getTopup_amount()), company.getCo_name()}), "1");
//                } else {
//                    //publicUser = (PublicUser) retrieverDAO.getObjectByCode("us_user_id",ActionContext.getContext().getSession().get("p_loginId").toString(),new PublicUser());
//                    // puserDAO.logActivity(ActionContext.getContext().getSession().get("p_userName").toString() + " had made top up request of RM"+Formatter.formatCurrency(topupInfo.getTopup_amount())+ " on "+publicUser.getUs_user_name(),"1");
//                    puserDAO.logActivity(new ActionSupport().getText("log.desc.topup", new String[]{ActionContext.getContext().getSession().get("p_userName").toString(), Formatter.formatCurrency(topupInfo.getTopup_amount()), publicUser.getUs_user_name()}), "1");
//                }
//            } catch (Exception e) {
//                throw e;
//            }
//
//        commitBatchTransaction();
//        }
//        catch (Exception e){
//            rollbackBatchTransaction();//see if can fix invalid hash total - ahmadni @ 02-Jul-2019
//            throw e;
//        } finally {
//            closeSession();
//        }
//        return "";
//    }

    //ahmadni 22/9/2011 - EBPP TOPUP
//    public synchronized int post2RVS(List<PpTranModel> tranList) throws Exception{
//        retrieverDAO.setSession(getSession());
//
//        String EbpReceiptNo = "";
//
//        try {
//             beginBatchTransaction(); 
//
//            // Prepare List to be updated
//            Map<String, String> map = null;
//            RvElTranModel rvebpptrantmp = null;
//            PpTranModel ppTran = null;            
//            
//            for (PpTranModel tran : tranList){
//
//                // generate running Receipt No
//                Calendar cal=Calendar.getInstance();
//                String strYear = Integer.toString( cal.get(Calendar.YEAR)) ;
//                EbpReceiptNo = getRunningSeq2("EBPReceiptNo", strYear, "", "", true, "000000");
//
//                System.out.println("EbpReceiptNo = " + EbpReceiptNo);
//
//                //set RVELTRAN
//                rvebpptrantmp = new RvElTranModel();
//                rvebpptrantmp.setEbpp_receipt_no(EbpReceiptNo);
//                rvebpptrantmp.setPay_date(tran.getTran_date());
//                rvebpptrantmp.setPay_amount(tran.getTran_amount());
//                rvebpptrantmp.setCreate_date(DateUtil.getCurrentTimestamp());
//                rvebpptrantmp.setElasis_payid("");
//                rvebpptrantmp.setPay_type(SystemConstants.POST2RVS.PAYMENT_TYPE); //cash transaction
//                rvebpptrantmp.setTrancode(SystemConstants.POST2RVS.TRANSACTION_CODE); //transaction code for prepayment
//                rvebpptrantmp.setSubcode("");
//                rvebpptrantmp.setPay_method(SystemConstants.PAYMENT_METHOD.EBP); //prepayment payment code = JVP //* JVP change to JVD 4/10/2011
//                rvebpptrantmp.setDoc_no("");
//                rvebpptrantmp.setDiv_no(SystemConstants.POST2RVS.KCH_DIV); //kuching division
//                getSession().save(rvebpptrantmp);
//
//                ppTran = (PpTranModel) retrieverDAO.getObjectById(tran.getTran_id(), PpTranModel.class);
//                BaseActionSupport.defaultUpdateProperties(ppTran, SystemConstants.BACKEND.DEFAULT_ID);
////                ppTran.updatableColumns = new String[]{"Tran_id", "Rvs_posted"};
//                ppTran.setRvs_receipt_no(Integer.parseInt(EbpReceiptNo)); // modified @6.10.2011 ai min. Reason : parse EbpReceiptNo to Integer
//                ppTran.setRvs_posted("Y"); //default to y
//                getSession().update(ppTran);
//            }
//
//            commitBatchTransaction();
//        } catch (Exception ex){
//            rollbackBatchTransaction();
//            throw ex;
//
//        } finally {
//            closeSession();
//        }
//
//        return 0;
//    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

     private String orderId = "";

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

//    /**
//     * For Multiple Sign and Payment - ahmadni 29/2/2014
//     * @param transaction = Staging, EBPP Topup, (Storefront or Elodgement)Payment, Account Maintenance
//     * @param Id = StagingId (Staging Backend Transaction) or TopUpId (EBPP Topup Backend Transaction) or UsId, CoId (Payment Transaction)
//     * @param acc_type = I (Individual Deposit Account) or C (Company Deposit Account)
//     * @param obj = if (transaction = 'EL_PYMNT') is 'ElPaymentModel' else is 'PaymentModel'  // ThoTH @ 2-Nov-2011
//     * @param Status = A-Approved or R-Rejected or E-Expired or Freezed
//     * @param cartModel = Cart  -- Removed by ThoTH @ 25-Oct-2011
//     * @throws Exception
//     */
//    @Override
//    public synchronized Boolean startMultipleTransaction(String transaction, String Id, String acc_type, List<ElPaymentModel> paymentModelList, String Status, Boolean commitTransaction) throws Exception {
////        retrieverDAO.setSession(getSession());
//
//        PaymentDAO paymentDAO = new PaymentDAOImpl();
//        paymentDAO.setSession(getSession());
//        Boolean proceed = true;
//        Map sessionMap = ActionContext.getContext().getSession();
//        try {
//
////            cartModel.getPurchaseDetailList()
//            if (commitTransaction) beginBatchTransaction();
//
//            PublicUser publicUserModel = null;
//            PpBalanceModel balanceModel = null;
//            String keyId = "";
//            String accType = "";
////            Boolean proceed = true;
//
//        for (ElPaymentModel elPaymentModel:paymentModelList){
//
//                  keyId = Id;
//                  accType = acc_type;
//
//                  CustCompanyModel custCoModel = null;
//                  if (!Validator.isEmpty(keyId) && !Validator.isEmpty(accType)){
//                      if (accType.equals(SystemConstants.PAYMENT_OPTION.COMPANY_DEPOSIT_ACC)){
//                          custCoModel = checkCompanyExist("co_id",keyId);
//                          if (custCoModel != null ){
//                                balanceModel = checkAccountExist(keyId, accType); // check if balance account exist
//                           }else {proceed = false; System.out.println("Company NOT FOUND.");}
//                      }else if (accType.equals(SystemConstants.PAYMENT_OPTION.INDIVIDUAL_DEPOSIT_ACC)){
//                           publicUserModel = checkUserExist("us_id",keyId);
//                          if (publicUserModel != null ) { // check if user exist
//                              if (publicUserModel.getUs_user_type().equals(SystemConstants.USER_TYPE.PREMIUM)){ // check if PREMIUM user
//                                    balanceModel = checkAccountExist(keyId, accType); // check if balance account exist
//                              }else {proceed = false; System.out.println("User IS NOT a PREMIUM user.");}
//                          }else{ proceed = false; System.out.println("User NOT FOUND.");}
//                      }
//
//                       if (balanceModel != null){  // check if balance exist
//                          if (balanceModel.getBal_status().equals( SystemConstants.PREPAYMENT_CHECK.BALANCE_STATUS.ACTIVE)){  // check if balance account active
//                              Double balAmt = balanceModel.getBal_amount();
//                              Double transAmt = Double.parseDouble(elPaymentModel.getPayment_amount().toString());//minus total amount to be paid
////                              Double newAmt = balAmt - transAmt;
//                              if ( balAmt - transAmt >= 0){ // check if balance amount enough
//
//                                  if (!Validator.isEmpty(sessionMap.get("p_userId").toString())) {
//                                      proceed = true;
//                                      //processData(transaction, Boolean.FALSE, balanceModel, null, paymentModelList, null, sessionMap.get("p_loginId").toString(), "");
//                                  }
//
//                              } else {// throw msg if bal amt nt enuf
//                                  proceed = false; System.out.println("Balance amount NOT ENOUGH.");
//                              }
//                          }else {proceed = false; System.out.println("Balance account IS NOT ACTIVE.");}
//                       }else{ proceed = false; System.out.println("Balance account NOT EXIST.");}
//                  }
//
//        }
//            //End of Storefront Topup Transaction
//
////             System.out.println("BEGIN COMMIT. " + proceed);
//            if(proceed){
//                processMultipleData(transaction, Boolean.FALSE, balanceModel, null, paymentModelList, null, sessionMap.get("p_loginId").toString(), "");
//                if (commitTransaction) commitBatchTransaction();
////                System.out.println("END COMMIT.");
//            } else {
//                // ThoTH: Should use throw Exception as the first place
//                //        Don't want to affect other code, so just rollback here, no matter commitTransaction or not.
//                rollbackBatchTransaction();
//            }
//
//        } catch (Exception ex) {
//            if (commitTransaction) rollbackBatchTransaction();
//            throw ex;
//        } finally {
//            if (commitTransaction) {
//                closeSession();
//            } else {
//                setSession(null); // For synchronized(ppDAO) in PurchaseDAOImpl
//            }
//        }
//        return proceed;
//    }
//
//    /**
//     * For Multiple Sign and Payment - ahmadni 29/2/2014
//     * @param transactionType = Staging or EBPP Topup or (Storefront or Elodgement)Payment or Account Maintenance
//     * @param commitTransaction = True or False
//     * @param balanceModel = PpBalanceModel
//     * @param stagingModel = PpStagingModel
//     * @param obj = if (transaction = 'EL_PYMNT') is 'ElPaymentModel' else is 'PaymentModel'  // ThoTH @ 2-Nov-2011
//     *        OLD:: paymentModel = PaymentModel  // transAmount = Payment Amount to be paid - cancelled
//     * @param topupModel = topupModel
//     * @param updatedBy = RVS (Staging Transaction) or UsUserId (Storefront Payment) or EBPP (EBPP TopUp)
//     * @param topupStatus = A-Approved or R-Rejected or E-Expired
//     * @param cartModel = Cart
//     * @throws Exception
//     */
//    private synchronized void processMultipleData(String transactionType, Boolean commitTransaction, PpBalanceModel balanceModel, PpStagingModel stagingModel, List<ElPaymentModel> paymentModelList, PpTopupModel topupModel, String updatedBy, String topupStatus) throws Exception {
//        String acc = "";
//
//        try {
//            if (commitTransaction) {
//                beginBatchTransaction();
//            }
//
//            if (balanceModel == null) { // new account
//                acc = SystemConstants.PREPAYMENT_CHECK.ACC_NEW;
//                balanceModel = new PpBalanceModel();
////                BaseActionSupport.defaultAddProperties(new PpBalanceModel(), updatedBy);
//                BaseActionSupport.defaultAddProperties(balanceModel, updatedBy);
//               // System.out.println("bal id"+balanceModel.getBal_id());// insert to PP_Balance
//            } else { // current account
//                acc = SystemConstants.PREPAYMENT_CHECK.ACC_CURRENT;
//                BaseActionSupport.defaultUpdateProperties(balanceModel, updatedBy);  // update PP_Balance
//            }
//
//            // insert to PP_Tran
//            PpTranModel tranModel = new PpTranModel();
//            BaseActionSupport.defaultAddProperties(tranModel, updatedBy);
//            for (ElPaymentModel elPaymentModel : paymentModelList) {
//
//                if (transactionType.equals(SystemConstants.PREPAYMENT_CHECK.TRANSACTION.EL_PYMNT)) {  // eLodgement Payment : ThoTH @ 2-Nov-2011
//                    tranModel.setTran_date(elPaymentModel.getPayment_date());
//                    tranModel.setTran_amount(Double.parseDouble("-" + elPaymentModel.getPayment_amount().toString()));
//                    tranModel.setTran_type(SystemConstants.PREPAYMENT_CHECK.TRANS_TYPE.ELODGEMENT_PYMNT); // eLodgement Payment
//                    tranModel.setTran_desc("");
//                    tranModel.setTran_payment_id(elPaymentModel.getPayment_id());// store payment id for posting to RVS purpose
//                    tranModel.setBal_id(balanceModel.getID()); // set foreign key
//                    tranModel.setRvs_posted("N");
//                    getSession().save(tranModel);
//
//                }
//
//                // PP_Balance
//                if (transactionType.equals(SystemConstants.PREPAYMENT_CHECK.TRANSACTION.EL_PYMNT)) {  // ThoTH @ 2-Nov-2011
//                    Double transAmt = Double.parseDouble(elPaymentModel.getPayment_amount().toString());
//                    Double balanceAmt = balanceModel.getBal_amount();
//                    Double newBalanceAmt = balanceAmt - transAmt;
//                    balanceModel.setBal_amount(newBalanceAmt);
//                    getSession().update(balanceModel); // update PP_Balance
//
//                }
//            }
//            if (commitTransaction) {
//                commitBatchTransaction();
//            }
//
//        } catch (Exception ex) {
//            if (commitTransaction) {
//                rollbackBatchTransaction();
//            }
//            throw ex;
//        } finally {
//            if (commitTransaction) {
//                closeSession();
//            }
//        }
//    }    

}

