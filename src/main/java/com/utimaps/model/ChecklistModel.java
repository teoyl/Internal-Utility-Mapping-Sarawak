/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.User;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;

@Entity
@FilterDefs({
    @FilterDef(name="caseFilter", parameters={
        @ParamDef( name="checkType", type="string")
    }),
})
@Table(name = "US_CHECKLIST")
public class ChecklistModel extends ModelBase implements java.io.Serializable{
    private String check_id;
    private String case_id;
    private String check_type;
    private String check_status;
    private String rec_status;
    private String verify_status;
    private String check_comment_oic;
    private String check_by_oic;
    private java.sql.Timestamp check_date_oic;
    private String verify_by_oic;
    private java.sql.Timestamp verify_date_oic;
    private String comment_ss;
    private String check_by_ss;
    private java.sql.Timestamp check_date_ss;
    private String check_counter;
    private String message_id;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    private String check_by_oic_str;
    private String check_by_ss_str;
    private String comment_verify_oic;
    
    private Map columnLengthMap = null;
//    public String[] updatableColumns = new String[]{"check_id","case_id","check_type","check_status","check_comment_oic","check_by_oic","check_date_oic","comment_ss","check_by_ss","check_date_ss","check_counte","message_id"};
    public String[] updatableColumns = new String[]{"check_id"};
    
    public String[] columnLength = new String[]{"check_id:20","case_id:20","check_type:20","check_status:2","rec_status:2","check_comment_oic:4000","check_by_oic:20","comment_ss:4000","check_by_ss:20","check_counter:4","message_id:20"};

    public static final class OPERATION {
    }
    
    @Id
    @Column(name = "check_id")
    public String getCheck_id() {
        return check_id;
    }

    public void setCheck_id(String check_id) {
        this.check_id = check_id;
    }
    
    @Transient 
    public String getID() { 
        return check_id; 
    } 

    public void setID(String pk_id) {
        this.check_id = pk_id;
    }

    @Column(name = "case_id")
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    @Column(name = "check_type")
    public String getCheck_type() {
        return check_type;
    }

    public void setCheck_type(String check_type) {
        this.check_type = check_type;
    }

    @Column(name = "check_status")
    public String getCheck_status() {
        return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }
 
    @Column(name = "rec_status")
     public String getRec_status() {
        return rec_status;
    }

    public void setRec_status(String rec_status) {
        this.rec_status = rec_status;
    }

    @Column(name = "verify_status")
    public String getVerify_status() {
        return verify_status;
    }

    public void setVerify_status(String verify_status) {
        this.verify_status = verify_status;
    }

    @Column(name = "check_comment_oic")
    public String getCheck_comment_oic() {
        return check_comment_oic;
    }

    public void setCheck_comment_oic(String check_comment_oic) {
        this.check_comment_oic = check_comment_oic;
    }

    @Column(name = "check_by_oic")
    public String getCheck_by_oic() {
        return check_by_oic;
    }

    public void setCheck_by_oic(String check_by_oic) {
        this.check_by_oic = check_by_oic;
    }

    @Column(name = "check_date_oic")
    public Timestamp getCheck_date_oic() {
        return check_date_oic;
    }

    public void setCheck_date_oic(Timestamp check_date_oic) {
        this.check_date_oic = check_date_oic;
    }

    @Column(name = "verify_by_oic")
    public String getVerify_by_oic() {
        return verify_by_oic;
    }

    public void setVerify_by_oic(String verify_by_oic) {
        this.verify_by_oic = verify_by_oic;
    }

    @Column(name = "verify_date_oic")
    public Timestamp getVerify_date_oic() {
        return verify_date_oic;
    }

    public void setVerify_date_oic(Timestamp verify_date_oic) {
        this.verify_date_oic = verify_date_oic;
    }

    @Column(name = "comment_verify_oic")
    public String getComment_verify_oic() {
        return comment_verify_oic;
    }

    public void setComment_verify_oic(String comment_verify_oic) {
        this.comment_verify_oic = comment_verify_oic;
    }
    
    @Column(name = "comment_ss")
    public String getComment_ss() {
        return comment_ss;
    }

    public void setComment_ss(String comment_ss) {
        this.comment_ss = comment_ss;
    }

    @Column(name = "check_by_ss")
    public String getCheck_by_ss() {
        return check_by_ss;
    }

    public void setCheck_by_ss(String check_by_ss) {
        this.check_by_ss = check_by_ss;
    }

    @Column(name = "check_date_ss")
    public Timestamp getCheck_date_ss() {
        return check_date_ss;
    }

    public void setCheck_date_ss(Timestamp check_date_ss) {
        this.check_date_ss = check_date_ss;
    }

    @Column(name = "check_counter")
    public String getCheck_counter() {
        return check_counter;
    }

    public void setCheck_counter(String check_counter) {
        this.check_counter = check_counter;
    }

    @Column(name = "message_id")
    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public void setColumnLength(String[] columnLength) {
        this.columnLength = columnLength;
    }
    
    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "created_date")
    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Column(name = "updated_date")
    public Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Timestamp updated_date) {
        this.updated_date = updated_date;
    }
      
    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }
    
    @Transient
    public Map getColumnLengthMap() {
        if (columnLengthMap == null) {
            columnLengthMap = new HashMap();
            String[] strA = null;
            
            for (String str : columnLength) {
                strA = str.split(":");
                columnLengthMap.put(strA[0], strA[1]);
            }
        }
        
        return columnLengthMap;
    }
    
    private User user;    
    @OneToOne(targetEntity = User.class, optional = true, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "US_ID", name = "CHECK_BY_OIC", insertable = false, updatable = false, nullable = true)
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    private User checkUser;
    @OneToOne(targetEntity = User.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "US_ID", name = "CHECK_BY_OIC", insertable = false, updatable = false, nullable = true)
    public User getCheckUser() {
        return checkUser;
    }
    
    public void setCheckUser(User checkUser) {
        this.checkUser = checkUser;
    }
    
    private User verifyUser;
    @OneToOne(targetEntity = User.class, optional = true, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "US_ID", name = "VERIFY_BY_OIC", insertable = false, updatable = false, nullable = true)
    public User getVerifyUser() {
        return verifyUser;
    }
    
    public void setVerifyUser(User verifyUser) {
        this.verifyUser = verifyUser;
    }
    
    private User checkSSUser;
    @OneToOne(targetEntity = User.class, optional = true, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "US_ID", name = "CHECK_BY_SS", insertable = false, updatable = false, nullable = true)
    public User getCheckSSUser() {
        return checkSSUser;
    }
    
    public void setCheckSSUser(User checkSSUser) {
        this.checkSSUser = checkSSUser;
    }
    
    @Transient
    public String getCheck_date_oic_str() {
        if(check_date_oic == null) {
            return "-";
        } else {
            return Formatter.formatDate(check_date_oic, SystemConstants.DATE.dataEntryFormat3);
        }
    }
    
    public void setCheck_date_oic_str(String check_date_oic) {
        try {
            this.check_date_oic = DateUtil.getTimestampFromDate(DateUtil.getDate(check_date_oic, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistModel", "ChecklistModel", "setCheck_date_oic_str");
        }
    }
    
    @Transient
    public String getVerify_date_oic_str() {
        if(verify_date_oic == null) {
            return "-";
        } else {
            return Formatter.formatDate(verify_date_oic, SystemConstants.DATE.dataEntryFormat3);
        }
    }
    
    public void setVerify_date_oic_str(String verify_date_oic) {
        try {
            this.verify_date_oic = DateUtil.getTimestampFromDate(DateUtil.getDate(verify_date_oic, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistModel", "ChecklistModel", "setCheck_date_oic_str");
        }
    }
    
    @Transient
    public String getCheck_date_ss_str() {
        if(check_date_ss == null) {
            return "-";
        } else {
            return Formatter.formatDate(check_date_ss, SystemConstants.DATE.dataEntryFormat3);
        }
    }
    
    public void setCheck_date_ss_str(String check_date_ss) {
        try {
            this.check_date_ss = DateUtil.getTimestampFromDate(DateUtil.getDate(check_date_ss, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistModel", "ChecklistModel", "setCheck_date_oic_str");
        }
    }
    
    @Transient
    public String getCheck_by_oic_str() {
//        if(this.user !=null){
//            check_by_oic_str = this.user.getUs_user_name();
//        }
        return check_by_oic_str;
    }

    public void setCheck_by_oic_str(String check_by_oic_str) {
        this.check_by_oic_str = check_by_oic_str;
    }
    
    @Transient
    public String getCheck_by_ss_str() {
        return check_by_ss_str;
    }

    public void setCheck_by_ss_str(String check_by_ss_str) {
        this.check_by_ss_str = check_by_ss_str;
    }
    
    
    
    @Transient
    public String getCheck_date_oic_str2() {
        if(check_date_oic == null) {
            return "-";
        } else {
            return Formatter.formatDate(check_date_oic, SystemConstants.DATE.dataTimeAmPmEntryFormat);
        }
    }
    
    public void setCheck_date_oic_str2(String check_date_oic) {
        try {
            this.check_date_oic = DateUtil.getTimestampFromDate(DateUtil.getDate(check_date_oic, SystemConstants.DATE.dataTimeAmPmEntryFormat));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistModel", "ChecklistModel", "setCheck_date_oic_str");
        }
    }
}
