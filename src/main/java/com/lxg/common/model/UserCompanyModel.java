package com.lxg.common.model;

//import com.mrpe.qp.application.model.QPApplicationModel;
import com.sains.framework.base.ModelBase;
import java.util.Map;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 *
 * @author ahmadni 19/05/2011
 */
@Entity
@Table(name = "t_user_company")
public class UserCompanyModel extends ModelBase implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String usco_id = "";
    private String us_id = "";
    private String co_id = "";
    private String usco_status= "I"; // default to 'I' (inactive) // modified @13.2.2012 ai min
    private java.sql.Timestamp usco_status_date;//modified data type from String to Timestamp 24.5.2011 ai min
    private String usco_status_by = "";
    private String app_id=""; //ahmadni @ 27-Oct-2016

    public String[] updatableColumns = new String[]{"Usco_id", "Us_id", "Co_id", "Usco_status", "Usco_status_date", "Usco_status_by","App_id"};
    private final String[] columnLength = new String[]{"usco_id:16", "us_id:16", "co_id:16", "usco_status:1", "usco_status_date", "usco_status_by:16"};
    private Map columnLengthMap = null;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    @Id
    @Column(name = "usco_id")
    public String getUsco_id() {
        return usco_id;
    }

    public void setUsco_id(String coId) {
        this.usco_id = coId;
    }

    @Transient
    public String getID() {
        return getUsco_id();
    }

    public void setID(String uscoId) {
        setUsco_id(uscoId);
    }

    @Column(name = "co_id")
    public String getCo_id() {
        return co_id;
    }

    public void setCo_id(String co_id) {
        this.co_id = co_id;
    }

    @Column(name = "us_id")
    public String getUs_id() {
        return us_id;
    }

    public void setUs_id(String us_id) {
        this.us_id = us_id;
    }

    @Column(name = "usco_status")
    public String getUsco_status() {
        return usco_status;
    }

    public void setUsco_status(String usco_status) {
        this.usco_status = usco_status;
    }

    @Column(name = "usco_status_by")
    public String getUsco_status_by() {
        return usco_status_by;
    }

    public void setUsco_status_by(String usco_status_by) {
        this.usco_status_by = usco_status_by;
    }

    //modified data type on 24.5.2011
    @Column(name = "usco_status_date")
    public java.sql.Timestamp getUsco_status_date() {
        return usco_status_date;
    }

    public void setUsco_status_date(java.sql.Timestamp usco_status_date) {
        this.usco_status_date = usco_status_date;
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String createdBy) {
        created_by = createdBy;
    }

    @Column(name = "created_date")
    public java.util.Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(java.sql.Timestamp createdDate) {
        created_date = createdDate;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updatedBy) {
        updated_by = updatedBy;
    }

    @Column(name = "updated_date")
    public java.util.Date getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updatedDate) {
        updated_date = updatedDate;
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

    private CustCompanyModel custCompany;

    @ManyToOne(targetEntity = CustCompanyModel.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName="co_id", name="co_id", insertable = false, updatable = false, nullable = true)
    public CustCompanyModel getCustCompany() {
        return custCompany;
    }

    public void setCustCompany(CustCompanyModel custCompany) {
        this.custCompany = custCompany;
    }

    @Override
    public void preUpdate(org.hibernate.Session session, Object dataEntryModel) throws Exception {
         
    }

   
}
