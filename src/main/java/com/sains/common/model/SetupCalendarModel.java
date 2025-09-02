package com.sains.common.model;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity
@Table(name="t_setup_calendar")
public class SetupCalendarModel extends ModelBase implements java.io.Serializable {
    private String cal_id = "";
    private java.sql.Timestamp cal_date;
    private String cal_desc;
    private String is_holiday = "N";
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private final String[] updatableColumns = new String[] {"Cal_id","Cal_date","Cal_desc","Is_holiday"};


    private final String[] columnLength = new String[] {"cal_id:20","cal_desc:100","is_holiday:1"};
    private Map columnLengthMap = null;
    
    public static final class OPERATION {
        public static final String AutoInsertPublicHoliday = "AutoInsertPublicHoliday";
    }

    public SetupCalendarModel() {
        //userDefined_autoValidation(Boolean.TRUE);
        //userDefined_insertNoDuplicate("module_code;module.code");
        //userDefined_validateRecursive(Boolean.TRUE);~r~n'
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }


    @Id
    @Column(name="cal_id")
    public String getCal_id() {
    	return cal_id;
    }

    public void setCal_id(String cal_id) {
    	this.cal_id = cal_id;
    }

    @Transient 
    public String getID() { 
        return cal_id; 
    } 

    public void setID(String pk_id) {
        this.cal_id = pk_id;
    }

    @Column(name="cal_date")
    public java.sql.Timestamp getCal_date() {
    	return cal_date;
    }

    public void setCal_date(java.sql.Timestamp cal_date) {
    	this.cal_date = cal_date;
    }

    @Transient
    public String getCal_date_str() {
        return Formatter.formatDate(cal_date, SystemConstants.DATE.dataEntryFormat);    }

    public void setCal_date_str(String cal_date) {
        try {
            this.cal_date = DateUtil.getTimestampFromDate(DateUtil.getDate(cal_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e){}
    }

    @Column(name="cal_desc")
    public String getCal_desc() {
    	return cal_desc;
    }

    public void setCal_desc(String cal_desc) {
    	this.cal_desc = cal_desc;
    }

    @Column(name="is_holiday")
    public String getIs_holiday() {
    	return is_holiday;
    }

    public void setIs_holiday(String is_holiday) {
    	this.is_holiday = is_holiday;
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
    /* ******** Write your DAO code after this line ****** */
    private String _date1 = "";
    private String _date2 = "";

    public void set_date1(String _date1) {
        this._date1 = _date1;
    }

    public void set_date2(String _date2) {
        this._date2 = _date2;
    }
    
    
    @Override
    public void manualOperation(org.hibernate.Session session) throws Exception {
        if (get_operation().equals(OPERATION.AutoInsertPublicHoliday)) {
            SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.DATE.dataEntryFormat);

            SetupCalendarModel cal = null;

            Calendar calStart = Calendar.getInstance();
            calStart.setTime(sdf.parse(_date1));

            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(sdf.parse(_date2));

            while (calStart.before(calEnd)) {
                int dayOfWeek = calStart.get(Calendar.DAY_OF_WEEK);
                cal = new SetupCalendarModel();
                cal.setCal_date(new Timestamp(calStart.getTimeInMillis()));
                
                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    if (dayOfWeek == Calendar.SATURDAY) {
                        cal.setCal_desc("SATURDAY");
                    } else {
                        cal.setCal_desc("SUNDAY");
                    }
                    cal.setIs_holiday("Y");
                }
                cal.defaultAddProperties();
                session.save(cal);

                calStart.add(Calendar.DAY_OF_YEAR, 1);

            }
        }
    }
}
