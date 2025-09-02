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
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Where;

/**
 *
 * @author yonglai
 */

@Entity
@FilterDefs({
    @FilterDef(name="statusFilter")
})
@Table(name = "T_MISC_PLAN")
public class MiscellaneousPlanModel extends ModelBase implements java.io.Serializable{
    private String misc_plan_id;
    private String plan_no;
    private String survey_job_no;
    private String plan_title;
    private String file_ref;
    private String field_book;
    private String sheet_ref;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    
    private String created_div;
    private String usj_div;
    private String usj_seq;
    private String usj_year;
    
    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{"misc_plan_id","plan_no","usj_div","usj_seq","usj_year","survey_job_no","plan_title","file_ref","field_book","sheet_ref","created_div"};

    public String[] columnLength = new String[]{"misc_plan_id:20", "plan_no:20", "survey_job_no:20", "title:1000", "file_ref:100", "field_book:100", "sheet_ref:20"};

    @Id
    @Column(name = "misc_plan_id")
    public String getMisc_plan_id() {
        return misc_plan_id;
    }

    public void setMisc_plan_id(String misc_plan_id) {
        this.misc_plan_id = misc_plan_id;
    }
    
    @Transient
    public String getID() { 
        return misc_plan_id; 
    } 

    public void setID(String pk_id) {
        this.misc_plan_id = pk_id;
    }

    @Column(name = "plan_no")
    public String getPlan_no() {
        return plan_no;
    }

    public void setPlan_no(String plan_no) {
        this.plan_no = plan_no;
    }
    
    @Column(name = "usj_div")
    public String getUsj_div() {
        return usj_div;
    }

    public void setUsj_div(String usj_div) {
        this.usj_div = usj_div;
    }

    @Column(name = "usj_seq")
    public String getUsj_seq() {
        return usj_seq;
    }

    public void setUsj_seq(String usj_seq) {
        this.usj_seq = usj_seq;
    }

    @Column(name = "usj_year")
    public String getUsj_year() {
        return usj_year;
    }

    public void setUsj_year(String usj_year) {
        this.usj_year = usj_year;
    }

    @Column(name = "survey_job_no")
    public String getSurvey_job_no() {
        return survey_job_no;
    }

    public void setSurvey_job_no(String survey_job_no) {
        this.survey_job_no = survey_job_no;
//        this.survey_job_no = "USJ/" + usj_div + "/" + usj_seq + "/" + usj_year;
    }

    @Column(name = "plan_title")
    public String getPlan_title() {
        return plan_title;
    }

    public void setPlan_title(String plan_title) {
        this.plan_title = plan_title;
    }

    @Column(name = "file_ref")
    public String getFile_ref() {
        return file_ref;
    }

    public void setFile_ref(String file_ref) {
        this.file_ref = file_ref;
    }

    @Column(name = "field_book")
    public String getField_book() {
        return field_book;
    }

    public void setField_book(String field_book) {
        this.field_book = field_book;
    }

    @Column(name = "sheet_ref")
    public String getSheet_ref() {
        return sheet_ref;
    }

    public void setSheet_ref(String sheet_ref) {
        this.sheet_ref = sheet_ref;
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

    @Column(name = "created_div")
    public String getCreated_div() {
        return created_div;
    }

    public void setCreated_div(String created_div) {
        this.created_div = created_div;
    }
    
    @Transient
    public String[] getColumnLength() {
        return columnLength;
    }
    
    public void setColumnLength(String[] columnLength) {
        this.columnLength = columnLength;
    }
    
    private String _selectedDivision;
    @Transient
    public String getSelectedDivision() {
        return _selectedDivision;
    }

    public void setSelectedDivision(String _selectedDivision) {
        this._selectedDivision = _selectedDivision;
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
    
    private String created_date_str;
    @Transient
    public String getCreated_date_str() {
        return Formatter.formatDate(created_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setCreated_date_str(String created_date_str) {
        try {
            this.created_date = DateUtil.getTimestampFromDate(DateUtil.getDate(created_date_str, SystemConstants.DATE.dataEntryFormat3));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "FileModel", "MiscellaneousPlanModel", "setCreated_date_str");
        }
    }
    
    @Transient
    public String getUpdated_date_str() {
        return Formatter.formatDate(updated_date, SystemConstants.DATE.dataEntryFormat);
    }

    public void setUpdated_date_str(String updated_date_str) {
        try {
            this.updated_date = DateUtil.getTimestampFromDate(DateUtil.getDate(updated_date_str, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){
            CommonFunction.writeLogFile(e.getStackTrace(), "MiscellaneousPlanModel", "ChecklistItemModel", "setUpdated_date_str");
        }
    }
    
    @Override
    public void preInsert(org.hibernate.Session session) throws Exception {
        checkingDAO.setSession(session);
        System.out.println("Inside preInsert");
        
        try {
            String formatted_seq = "";
            String formatted_div = "";
            if(!Validator.isEmpty(this.getUsj_seq())) {
                formatted_seq = prefixZeros(this.getUsj_seq(),4,false,"");
                this.setUsj_seq(formatted_seq);
            }
            if(!Validator.isEmpty(this.getUsj_div())) {
                formatted_div = prefixZeros(this.getUsj_div(),2,false,"");
                this.setUsj_div(formatted_div);
            }
            
            if(!Validator.isEmpty(this.getUsj_div()) && !Validator.isEmpty(this.getUsj_div())) {
                this.setSurvey_job_no("USJ/"+this.getUsj_div()+"/"+this.getUsj_seq()+"/"+this.getUsj_year());
            }

            // Get Year
            Calendar cal = Calendar.getInstance();
            String strYear = Integer.toString(cal.get(Calendar.YEAR));

            String strSeq2 = checkingDAO.getRunningSeq("MISC_SEQ", strYear, "", "", true, "000000");

            String formatted_mpno = "";
            String trimmed_div = this.getSelectedDivision().replaceFirst("^0+", "");
            formatted_mpno = "MP " + trimmed_div + "/130-" + strSeq2;
            this.setPlan_no(formatted_mpno);
            this.setCreated_div(this.getSelectedDivision());
            System.out.println("this.getSelectedDivision() " + this.getSelectedDivision());
            
        } catch(Exception e) {
            e.printStackTrace();
        }       
        
    }
    
    @Override
    public void preUpdate(org.hibernate.Session session, Object dataEntryModel) throws Exception {
        System.out.println("Inside preUpdate");
        setSession_(session);
        checkingDAO.setSession(session);

        try {
            MiscellaneousPlanModel updatingModel = (MiscellaneousPlanModel) dataEntryModel;
            String formatted_seq = "";
            String formatted_div = "";
            if(!Validator.isEmpty(updatingModel.getUsj_seq())) {
                formatted_seq = prefixZeros(updatingModel.getUsj_seq(),4,false,"");
                updatingModel.setUsj_seq(formatted_seq);
            }

            if(!Validator.isEmpty(updatingModel.getUsj_div())) {
                formatted_div = prefixZeros(updatingModel.getUsj_div(),2,false,"");
                updatingModel.setUsj_div(formatted_div);
            }
            
            if(!Validator.isEmpty(updatingModel.getUsj_seq()) && !Validator.isEmpty(updatingModel.getUsj_div())) {
                updatingModel.setSurvey_job_no("USJ/"+updatingModel.getUsj_div()+"/"+formatted_seq+"/"+updatingModel.getUsj_year());
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void manualOperation(org.hibernate.Session session) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        try {
            System.out.println("operation " + this.get_operation());
           
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "MiscellaneousPlanModel", "MiscellaneousPlanModel", "manualOperation");
        }
    }
    
    public String prefixZeros(String value, int len, boolean prefixAlphabet, String prefixAl) {
        char[] t = new char[len];
        int l = value.length();
        int k = len - l;
        for (int i = 0; i < k; i++) {
            t[i] = '0';
        }
        value.getChars(0, l, t, k);
        
        if(prefixAlphabet){
            return prefixAl + new String(t);
        }else{
            return new String(t);
        }        
    }
    
    private List<FileModel> attachmentList;

    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "misc_plan_id", name = "case_id", insertable = false, updatable = false, nullable = true)
    @Filter(name="statusFilter", condition="file_status = 'Y'")
    public List<FileModel> getAttachmentList() {
        return attachmentList;
    }
    
    public void setAttachmentList(List<FileModel> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
