package com.sample;



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
import com.sains.common.util.Validator;
import com.sains.framework.base.ModelBase;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name="t_master")
public class MasterModel extends ModelBase implements java.io.Serializable {
    private Integer master_id = null;
    private Integer created_by;
    private java.sql.Timestamp created_date;
    private String master_code;
    private String master_name;
    private String master_status;
    private Double master_total;
    private Integer updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Master_id","Master_code","Master_name","Master_status","Master_total"};


    private final String[] columnLength = new String[] {"master_id:20","master_code:20","master_name:50","master_status:5"};
    private Map columnLengthMap = null;

    public MasterModel() {
        userDefined_autoValidation(Boolean.TRUE);
//        setupMyChildList("getDetailList", DetailModel.class, "detailDeleted", Boolean.FALSE);
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="master_id")
    public Integer getMaster_id() {
    	return master_id;
    }

    public void setMaster_id(Integer master_id) {
    	this.master_id = master_id;
    }

    @Transient 
    public String getID() { 
        if (master_id == null) {
            return null;
        }
        return master_id.toString(); 
    } 

    public void setID(String pk_id) {
        if (Validator.isEmpty(pk_id)) {
            master_id = null;
        } else {
            this.master_id = Integer.parseInt(pk_id);
        }
    }

    @Column(name="created_by")
    public Integer getCreated_by() {
    	return created_by;
    }

    public void setCreated_by(Integer created_by) {
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

    @Column(name="master_code")
    public String getMaster_code() {
    	return master_code;
    }

    public void setMaster_code(String master_code) {
    	this.master_code = master_code;
    }

    @Column(name="master_name")
    public String getMaster_name() {
    	return master_name;
    }

    public void setMaster_name(String master_name) {
    	this.master_name = master_name;
    }

    @Column(name="master_status")
    public String getMaster_status() {
    	return master_status;
    }

    public void setMaster_status(String master_status) {
    	this.master_status = master_status;
    }

    @Column(name="master_total")
    public Double getMaster_total() {
    	return master_total;
    }

    public void setMaster_total(Double master_total) {
    	this.master_total = master_total;
    }

    @Transient
    public String getMaster_total_str() {
        return master_total==null?"":Formatter.formatDecimal(master_total.toString(), Formatter.CURRENCY_PATTERN_PLAIN);
    }

    public void setMaster_total_str(String master_total) {
        try {
            this.master_total = Double.parseDouble(master_total.replaceAll(",", ""));
        } catch (Exception e){}
    }

    @Column(name="updated_by")
    public Integer getUpdated_by() {
    	return updated_by;
    }

    public void setUpdated_by(Integer updated_by) {
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
    private List detailList = new ArrayList();
    @OneToMany(targetEntity = DetailModel.class, fetch = FetchType.LAZY, mappedBy = "master_id")
    public List<DetailModel> getDetailList() {
        return detailList;
    }
    public void setDetailList(List<DetailModel> detailList) {
        this.detailList = detailList;
    }
    
    @Override
    public Boolean createUpdate_useUserPK() {
        return Boolean.TRUE;
    }
}
