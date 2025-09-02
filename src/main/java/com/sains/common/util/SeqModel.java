/* ----------------------------------------------
   NAME   : SeqModel.java
   CREATED BY  : Java Model Generator
   CREATED Date: 08-JULY     -2010
   UPDATED BY  :
   UPDATED Date:
 ------------------------------------------------*/



package com.sains.common.util;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="T_SETUP_SEQ")
public class SeqModel implements java.io.Serializable {
    private String seq_id = "";
    private Long seq_last_no;

    private final String[] updatableColumns = new String[] {"Seq_id","Seq_last_no"};

    private static String insertColumns = "Seq_id,Seq_last_no";
    
    public static final class SEQ_ID_PREFIX {
         public static final String RVS_RECEIPT="RVSReceiptNo";
    }

    @Id
    @Column(name="seq_id")
    public String getSeq_id() {
    	return seq_id;
    }

    public void setSeq_id(String seq_id) {
    	this.seq_id = seq_id;
    }

    @Transient
    public String getID() {
        return seq_id;
    }

    public void setID(String pk_id) {
        this.seq_id = pk_id;
    }

    @Column(name="seq_last_no")
    public Long getSeq_last_no() {
    	return seq_last_no;
    }

    public void setSeq_last_no(Long seq_last_no) {
    	this.seq_last_no = seq_last_no;
    }



    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

    /* ******** Write your code after this line ****** */
    @Transient
    public static String getInsertColumns() {
        return insertColumns;
    }

}
