package com.sains.framework.base;

//import com.elasis.cart.model.SeqModel;
import com.sains.common.util.SeqModel;//serene @ 10/3/2017

import java.text.DecimalFormat;
import org.hibernate.Session;

public class RunningSeqFunction {

    //added by wongkk4 8/7/2010
    /* Moved by ThoTH from CommonFunction.java @ 26-Sept-2011
     * Because "static synchronized" will lock the whole Java file,
     *      when someone call the static Synchronized method
     * -- TESTED --
     */
    @Deprecated
    public static synchronized String getRunningSeq(String strType, String strYear, String prefix, String postfix, Boolean appendZero) throws Exception {
        BaseDAO<SeqModel> seqDAO = new BaseDAOImpl();
        DecimalFormat sixDigits = new DecimalFormat("000000");
        //String strNewSeq = "";
//        Integer liSeq = 0;
        long liSeq = 0;//serene@ 10/3/2017
        try {
            SeqModel seq = seqDAO.getModelById(strType + strYear, SeqModel.class);

//            for (int i = 1; i< 100; i++) {
//                for (int a = 1; a< 50000; a ++) {
//                }
//                System.out.println("************* " + seq.getSeq_last_no());
//            }

            if (seq == null) {
                //System.out.println("NewSeq");
                liSeq = 1;
                seq = new SeqModel();
                seq.setSeq_id(strType + strYear);
//                seq.setSeq_last_no(liSeq);
                seq.setSeq_last_no(liSeq);

                seqDAO.insert(seq);
            } else {
                //System.out.println("Before seq = " + seq.getSeq_last_no() );
                liSeq = seq.getSeq_last_no() + 1;
                seq.setSeq_last_no(liSeq);
                //try{
                //seqDAO.update(seq);
                seqDAO.directUpdate(seq);
                //}catch(Exception ex){
                //}
            }
            // System.out.println("After seq = " + llSeq );
            //aeDAO.update(ae);
        } catch (Exception e) {
            throw e;
        } finally {
            seqDAO.closeSession();
        }
        if (appendZero) {
//            System.out.println("Append" + prefix + sixDigits.format(llSeq) + postfix);
            return prefix + sixDigits.format(liSeq) + postfix;
        } else {
//            System.out.println("NO Append" + prefix + llSeq + postfix);
            return prefix + liSeq + postfix;
        }
    }

    public static synchronized String getRunningSeq(String strType, String strYear, String prefix, String postfix, Boolean appendZero, Session externalSession) throws Exception {
        System.out.println("getRunningSeq");
        BaseDAO<SeqModel> seqDAO = new BaseDAOImpl();
        seqDAO.setSession(externalSession);
        DecimalFormat sixDigits = new DecimalFormat("000000");
        //String strNewSeq = "";
        //Integer liSeq = 0;
        long liSeq = 0;//sereneChye @ 10/3/2017 
        try {
            SeqModel seq = seqDAO.getModelById(strType + strYear, SeqModel.class);

//            for (int i = 1; i< 100; i++) {
//                for (int a = 1; a< 50000; a ++) {
//                }
//                System.out.println("************* " + seq.getSeq_last_no());
//            }

            if (seq == null) {
                //System.out.println("NewSeq");
                liSeq = 1;
                seq = new SeqModel();
                seq.setSeq_id(strType + strYear);
                seq.setSeq_last_no(liSeq);
                System.out.println("strType + strYear: " +strType + strYear);
                externalSession.save(seq);
//                seqDAO.insert(seq);
            } else {
                //System.out.println("Before seq = " + seq.getSeq_last_no() );
                liSeq = seq.getSeq_last_no() + 1;
                seq.setSeq_last_no(liSeq);
                //try{
                externalSession.update(seq);
//                seqDAO.directUpdate(seq);
                //}catch(Exception ex){
                //}
            }
            // System.out.println("After seq = " + llSeq );
            //aeDAO.update(ae);
        } catch (Exception e) {
            throw e;
        } finally {
//            seqDAO.closeSession();
        }
        if (appendZero) {
//            System.out.println("Append" + prefix + sixDigits.format(llSeq) + postfix);
            return prefix + sixDigits.format(liSeq) + postfix;
        } else {
//            System.out.println("NO Append" + prefix + llSeq + postfix);
            return prefix + liSeq + postfix;
        }
    }

}

