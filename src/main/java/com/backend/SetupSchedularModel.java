package com.backend;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
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
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;

@Entity
@Table(name="t_setup_schedular")
public class SetupSchedularModel extends ModelBase implements java.io.Serializable {
    private String job_id = "";
    private String created_by;
    private java.sql.Timestamp created_date;
    private String job_code;
    private String job_repeat;
    private String job_repeat_every;
    private String job_switch_on = "N";
    private String trigger_once;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Job_id","Job_code","Job_repeat","Job_repeat_every","Job_switch_on","Trigger_once"};


    private final String[] columnLength = new String[] {"job_id:20","job_code:30","job_repeat:1","job_repeat_every:10","job_switch_on:1","trigger_once:1"};
    private Map columnLengthMap = null;

    public SetupSchedularModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="job_id")
    public String getJob_id() {
    	return job_id;
    }

    public void setJob_id(String job_id) {
    	this.job_id = job_id;
    }

    @Transient 
    public String getID() { 
        return job_id; 
    } 

    public void setID(String pk_id) {
        this.job_id = pk_id;
    }

    @Column(name="created_by")
    public String getCreated_by() {
    	return created_by;
    }

    public void setCreated_by(String created_by) {
    	this.created_by = created_by;
    }

    @Column(name="created_date")
    public java.sql.Timestamp getCreated_date() {
    	return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
    	this.created_date = created_date;
    }

    @Transient
    public String getCreated_date_str() {
        return Formatter.formatDate(created_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setCreated_date_str(String created_date) {
        try {
            this.created_date = DateUtil.getTimestampFromDate(DateUtil.getDate(created_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="job_code")
    public String getJob_code() {
    	return job_code;
    }

    public void setJob_code(String job_code) {
    	this.job_code = job_code;
    }

    @Column(name="job_repeat")
    public String getJob_repeat() {
    	return job_repeat;
    }

    public void setJob_repeat(String job_repeat) {
    	this.job_repeat = job_repeat;
    }

    @Column(name="job_repeat_every")
    public String getJob_repeat_every() {
    	return job_repeat_every;
    }

    public void setJob_repeat_every(String job_repeat_every) {
    	this.job_repeat_every = job_repeat_every;
    }

    @Column(name="job_switch_on")
    public String getJob_switch_on() {
    	return job_switch_on;
    }

    public void setJob_switch_on(String job_switch_on) {
    	this.job_switch_on = job_switch_on;
    }
    
    @Transient
    public Boolean getJob_switch_on_boo() {
    	return (job_switch_on == null || job_switch_on.equals("Y"));
    }

    public void setJob_switch_on_boo(Boolean job_switch_on_boo) {
    	if (job_switch_on_boo) {
            job_switch_on = "Y";
        } else {
            job_switch_on = "N";
        }
    }

    @Column(name="trigger_once")
    public String getTrigger_once() {
    	return trigger_once;
    }

    public void setTrigger_once(String trigger_once) {
    	this.trigger_once = trigger_once;
    }
    
    @Transient
    public Boolean getTrigger_once_boo() {
    	return (trigger_once == null || trigger_once.equals("Y"));
    }

    public void setTrigger_once_boo(Boolean trigger_once_boo) {
    	if (trigger_once_boo) {
            trigger_once = "Y";
        } else {
            trigger_once = "N";
        }
    }

    @Column(name="updated_by")
    public String getUpdated_by() {
    	return updated_by;
    }

    public void setUpdated_by(String updated_by) {
    	this.updated_by = updated_by;
    }

    @Column(name="updated_date")
    public java.sql.Timestamp getUpdated_date() {
    	return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updated_date) {
    	this.updated_date = updated_date;
    }

    @Transient
    public String getUpdated_date_str() {
        return Formatter.formatDate(updated_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setUpdated_date_str(String updated_date) {
        try {
            this.updated_date = DateUtil.getTimestampFromDate(DateUtil.getDate(updated_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
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

    /* ******** Write your code after this line ****** */

}
