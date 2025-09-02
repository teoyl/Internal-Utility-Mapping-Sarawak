/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend;

import com.backend.model.RvElTranModel;
import com.sains.common.util.DateUtil;
import com.sains.common.util.DoubleUtil;
import com.sains.common.util.ElasisSeqModel;
import com.sains.common.util.SeqModel;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.LogFunction;
import com.sains.framework.base.SessionFactoryImpl;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joveni.h
 */
public class Post2RVSEngine extends LRBase{
    
    protected String strEbppReceiptNo;
    protected String strEbppVourcherNo;
    protected String strTrnDiv;
    protected String strTrnType;
    protected String strTrnDist;
    protected String strTrnBs;
    protected String strTrnLot;
    protected String strTitVer;
    protected java.sql.Timestamp dtPayDate = null;
    protected Double dbPayAmount;
    protected String strImpFlag;
    protected String strElasisPayId;
    protected String strPayType;
    protected String strTranCode;
    protected String strSubCode;
    protected String strPayMethod;
    protected String strDocNo;
    protected String strDivNo;
    protected String strSource;
    protected String strEcaseRef;
    protected String strJobNo;
    protected String strIdType;
    protected String strIdNum;
    protected String strRemarks;
    protected String strAppName;
    protected String strLAASPayID;

    //extra columns not in RVELTRAN
    protected String lotNumber;
    protected String bsNumber;
    protected String cc1;
    protected String cc2;
    protected String cc3;
    protected String applicationType;
    protected String strLsFeeId;
    protected String strApplicantName;
    protected String strApplicantAddr;
    protected Double dbTotalPayAmount = 0d;

    protected java.sql.Connection conn_eqp = null;
    
    CommonFunction cf = new CommonFunction();
    
    public int post2RVS(List usjPaymentList) throws Exception {
        System.out.println("****** usjPaymentList in post2RVSEngine******" +usjPaymentList.size());
        boolean commitTrans = false;

        Calendar cal = Calendar.getInstance();
        String strYear = Integer.toString(cal.get(Calendar.YEAR));

        //Get max seq
        Long lMaxSeq = getRvsReceiptNo_MaxSeq(strYear);
        String strMaxSeq = Long.toString(lMaxSeq);
        String strPrevMaxSeq = strMaxSeq;
        
        try {


            //******************************************************* EIS Fee PAYMENT *******************************************************
            if (usjPaymentList != null && usjPaymentList.size() > 0) {
                lMaxSeq = lMaxSeq + 1; //get new max seq
                strMaxSeq = processUtimapsFeePayment(usjPaymentList, lMaxSeq);
                lMaxSeq = Long.parseLong(strMaxSeq);
                commitTrans = true;
            }
            
            if (commitTrans) {
                update_RvsReceiptNo_MaxSeq(strYear, strMaxSeq);//IMPORTANT: Must update at the last.
                strPrevMaxSeq = strMaxSeq;

                conn.commit();
                commitTrans = false;
            }
            

        } catch (Exception ex) {
            ex.printStackTrace();
            if (conn != null) {
                conn.rollback();
            }
            
            if (conn_new != null) {
                conn_new.rollback();
            }
            
//            cf.writeFile("Post2RVS", "[ERROR] Post2RVSEngine >>> Failed posting to RVS. :: " +ex);
            new LogFunction().logError(this.getClass(), "EIS Payment : Failed posting to RVS.", ex);
            throw ex;
        } finally {
            closeAll();
            closeAll_new();
            
            if (conn != null) {
                SessionFactoryImpl.closeConnection(conn);
            }
            conn = null;
            
            if (conn_new != null) {
                SessionFactoryImpl.closeConnection(conn_new);
            }
            conn_new = null;
        }

        return 0;
    }
    
    private long getRvsReceiptNo_MaxSeq(String strYear) throws Exception {
//        long lSeq = 300001; //IMPORTANT: RVS Receipt No start from 300,000
//        long lSeq = 700001; //IMPORTANT: RVS Receipt No start from 700,000
        long lSeq =0;
        String strSeqId = ElasisSeqModel.SEQ_ID_PREFIX.RVS_RECEIPT + strYear;
//        SeqModel seqModel = (SeqModel) retrieverDAO.getObjectById(SeqModel.SEQ_ID_PREFIX.RVS_RECEIPT + strYear, SeqModel.class);
        String strSeq = cf.getSingleValueWithSession(baseDAO.getSession(), "t_setup_seq_elasis", "seq_last_no", " seq_id= '"+strSeqId+"'");
        CommonFunction.writeFile("Post2RVS", "Post2RVS Running seq :: " +strSeq);
//        if (seqModel == null) {
        if (strSeq == null || Validator.isEmpty(strSeq)) {
            lSeq= 700001;
            insert_RvsReceiptNoSeq(strSeqId, lSeq); //insert if not exist yet
        } else {
//            lSeq = seqModel.getSeq_last_no() + 1;
//            lSeq = seqModel.getSeq_last_no();
             lSeq=Long.parseLong(strSeq); 
        }

        System.out.println("return max seq >>>>>>>>>>>>>>>>>>>>>> " + lSeq);
        return lSeq;
    }
    
    private void insert_RvsReceiptNoSeq(String strSeqId, long lMaxSeq) throws Exception {
        sqlStmt_.setLength(0);
        param_.clear();

//        sqlStmt_.append("INSERT INTO T_SETUP_SEQ (" + SeqModel.getInsertColumns() + ") ");
        sqlStmt_.append("INSERT INTO T_SETUP_SEQ_ELASIS (" + ElasisSeqModel.getInsertColumns() + ") ");
        sqlStmt_.append("VALUES (?,?) ");

        param_.add(strSeqId);
        param_.add(lMaxSeq);

        sqlUpdate(param_, sqlStmt_.toString());
    }
    
    private void update_RvsReceiptNo_MaxSeq(String strYear, String strMaxSeq) throws Exception {
        sqlStmt_.setLength(0);
        param_.clear();

//        sqlStmt_.append("UPDATE T_SETUP_SEQ SET ");
        sqlStmt_.append("UPDATE T_SETUP_SEQ_ELASIS SET ");

        sqlStmt_.append(" SEQ_LAST_NO = ? ");
//        param_.add(Long.parseLong(strMaxSeq)); //cannot use this!!! no nid to convert.
        param_.add(strMaxSeq);

        sqlStmt_.append(" WHERE SEQ_ID = ? ");
        param_.add(ElasisSeqModel.SEQ_ID_PREFIX.RVS_RECEIPT + strYear);

        sqlUpdate(param_, sqlStmt_.toString());
    }
    
    private String processUtimapsFeePayment(List usjPaymentList, long pMaxSeq) throws Exception {
        String strMaxSeq = Long.toString(pMaxSeq);

        long lMaxSeq = pMaxSeq;
        int iCount = 0;
        for (Map map : (List<Map>) usjPaymentList) { //EIS Fees List
            iCount++;

            //****** Columns under RVELTRAN01 [Start]***************************
            strMaxSeq = Long.toString(lMaxSeq);
            System.out.println("USJ FEE PAYMENT FEE RVS SEQ #" + iCount + ": " + strMaxSeq);

            strEbppReceiptNo = strMaxSeq;
            strEbppVourcherNo = (String) map.get("BILLREFNO"); //ls_payment.bill_ref_no
            strTrnDiv = (String) map.get("TRN_DIV"); //ls_afclots.trn_div
            strTrnType = (String) map.get("TRN_TYPE"); //ls_afclots.trn_type
            strTrnDist = (String) map.get("TRN_DIST"); //ls_afclots.trn_dist
            strTrnBs = (String) map.get("TRN_BS"); //ls_afclots.trn_bs
            strTrnLot = (String) map.get("TRN_LOT"); //ls_afclots.trn_lot
            strTitVer = (String) map.get("TIT_VER"); //ls_afclots.tit_ver
            
            if(map.get("STATUSDATE") != null){
                dtPayDate = DateUtil.getTimestampFromDate(formatPaymentDate(map.get("STATUSDATE").toString())); //Remark: Replaced PAYMENTDATE @2.7.2020.
            } else{
                dtPayDate = null;
            }

            dbPayAmount = DoubleUtil.round(Double.parseDouble(map.get("PODPRICE").toString()), 2); //ls_payment_item.item_amount
            strImpFlag = "Y";
            strElasisPayId = (String) map.get("ITEMID"); //ls_payment_item.item_id //
//            strElasisPayId = ""; //ls_payment_item.item_id //
//            strPayType = (String) map.get("PAYTYPE"); //
            strPayType = ""; //
            strTranCode = (String) map.get("RVRCODE"); //ls_payment_item.rvr_code
            strSubCode = (String) map.get("SUBCODE"); //ls_payment_item.subcode
//            strSubCode = ""; //ls_payment_item.subcode
            strPayMethod = (String) map.get("PAYMENTMETHOD"); //ls_payment.payment_method  
            
            
            strDocNo = "";
            /*
            strDivNo = SystemConstants.POST2RVS.KCH_DIV;
            */
            /*  Remarks: @10.1.2019: Ivy confirmed with Vyonne to post to divisional.
                         @4.7.2018. Ivy said default trn div.
                         @6.7.2018. Wait Edris to give signal to deploy this changes. */
            strDivNo = (String) map.get("CASE_DIV"); 
	    
            strSource = RvElTranModel.SOURCE.LXG_COMMON; 
            strEcaseRef = (String) map.get("ECASEREF"); //ls_r_appcase.shortkey1
            strJobNo = (String) map.get("JOBNO"); //ls_r_appcase.shortkey1
            strIdType = "";
            strIdNum = "";
            strAppName = "";
            strLAASPayID = (String) map.get("LAASPAYID");
            strRemarks = "";
            //****** Columns under RVELTRAN01 [End]***************************

            insert_RvElTran01();
            update_ElPaymentItem();

            lMaxSeq = lMaxSeq + 1;
        }

        return strMaxSeq;
    }
    
    private Date formatPaymentDate(String paymentDate) throws Exception {
        Date newDate = DateUtil.getDate(paymentDate, "yyyy-MM-dd HH:mm:ss");
        return newDate;
    }
    
    private void insert_RvElTran01() throws Exception {
        sqlStmt_.setLength(0);
        param_.clear();

        sqlStmt_.append("INSERT INTO RVELTRAN01 (" + RvElTranModel.getInsertColumns() + ") ");
        sqlStmt_.append("VALUES (?,?,?,?,?,?,?,?,?,?,  sysdate,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?) ");

        param_.add(strEbppVourcherNo);
        param_.add(strEbppReceiptNo);
        param_.add(strTrnDiv);
        param_.add(strTrnType);
        param_.add(strTrnDist);
        param_.add(strTrnBs);
        param_.add(strTrnLot);
        param_.add(strTitVer);
        param_.add(dtPayDate);
        param_.add(dbPayAmount);

        param_.add(strImpFlag);
        param_.add(strElasisPayId);
        param_.add(strPayType);
        param_.add(strTranCode);
        param_.add(strSubCode);
        param_.add(strPayMethod);
        param_.add(strDocNo);
        param_.add(strDivNo);
        param_.add(strSource);

        param_.add(strEcaseRef);
        param_.add(strJobNo);
        param_.add(strIdType);
        param_.add(strIdNum);
        param_.add(strRemarks);
        param_.add(strAppName);
        param_.add(strLAASPayID);
//        if(strSource.equals(RvElTranModel.SOURCE.EQP_PAYMENT)){
//            sqlUpdate_new(param_, sqlStmt_.toString()); //different DB so use different connection.
//        }else{
            sqlUpdate(param_, sqlStmt_.toString());
//        }
        
        System.out.println("inserted rveltran.");
    }
    
    private void update_ElPaymentItem() throws Exception {
        sqlStmt_.setLength(0);
        param_.clear();

        sqlStmt_.append("UPDATE US_PAYMENT_ITEM SET RVS_POSTED = 'Y', UPDATED_DATE = sysdate, ");

        sqlStmt_.append(" UPDATED_BY = ?, ");
        param_.add(SystemConstants.BACKEND.DEFAULT_ID);

        sqlStmt_.append(" RVS_RECEIPTNO = ? ");
        param_.add(strEbppReceiptNo);
        System.out.println("us_payment_item.rvs_receipt_no : " + strEbppReceiptNo);

        sqlStmt_.append(" WHERE PAYITEM_ID = ? ");
        param_.add(strElasisPayId);
        System.out.println("eis_payment_item.item_id : " + strElasisPayId);

        sqlUpdate(param_, sqlStmt_.toString());
        System.out.println("updated el_payment_item.");
    }
    
    
}
