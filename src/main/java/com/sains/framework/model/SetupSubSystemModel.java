/* ----------------------------------------------
   NAME   : SetupSystemModel.java 
   CREATED BY  : Java Model Generator                   
   CREATED Date: 20-JULY     -2010                   
   UPDATED BY  :                                
   UPDATED Date:                    
 ------------------------------------------------*/
package com.sains.workflow.model;

import com.sains.framework.model.SetupGroup;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;

// ThoTH @ 30-Oct-2014:: FilterDef with Param, then name CANNOT have DOT, e.g. SetupSystemModel.groupTypeA
@Entity
@Table(name = "t_setup_subsystem")
public class SetupSubSystemModel implements java.io.Serializable {

    private static final long serialVersionUID = -1180851691791611580L;
    private String system_id = "";
    private String system_code; //used to store sub_system_code
    private String system_name;
    private java.sql.Timestamp created_date;
    private String created_by;
    private java.sql.Timestamp updated_date;
    private String updated_by;

    private final String[] updatableColumns = new String[]{"System_id", "Main_syscode", "System_code", "System_name"};

    @Id
    @Column(name = "system_id")
    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    @Transient
    public String getID() {
        return system_id;
    }

    public void setID(String pk_id) {
        this.system_id = pk_id;
    }
    
    @Column(name = "system_code")
    public String getSystem_code() {
        return system_code;
    }

    public void setSystem_code(String system_code) {
        this.system_code = system_code;
    }

    @Column(name = "system_name")
    public String getSystem_name() {
        return system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    @Column(name = "created_date")
    public java.sql.Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "updated_date")
    public java.sql.Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updated_date) {
        this.updated_date = updated_date;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

    /* ******** Write your code after this line ****** */
    private List<SetupGroup> setupGroupList;

    @OneToMany(targetEntity = SetupGroup.class, fetch = FetchType.LAZY, mappedBy = "system_id")
    public List<SetupGroup> getSetupGroupList() {
        return setupGroupList;
    }

    public void setSetupGroupList(List<SetupGroup> setupGroupList) {
        this.setupGroupList = setupGroupList;
    }
//    private List<SetupGroup> setupGroupWFList = new ArrayList();
//
//    @OneToMany(targetEntity = SetupGroup.class, fetch = FetchType.LAZY, mappedBy = "system_id")
//    @Filter(name = "SetupSystemModel_groupTypeW", condition = "group_type = 'W' and (dept_id = :deptid or dept_id = 'COMMON') and group_level < :maxGroupLevel and group_level >= :minGroupLevel -1")
//    @OrderBy("group_name")
//    public List<SetupGroup> getSetupGroupWFList() {
//        return setupGroupWFList;
//    }
//
//    public void setSetupGroupWFList(List<SetupGroup> setupGroupWFList) {
//        this.setupGroupWFList = setupGroupWFList;
//    }
//
//    private List<SetupGroup> setupGroupAppList = new ArrayList();
//
//    @OneToMany(targetEntity = SetupGroup.class, fetch = FetchType.LAZY, mappedBy = "system_id")
//    @Filter(name = "SetupSystemModel_groupTypeA", condition = "group_type = 'A' and (dept_id = :deptid or dept_id = 'COMMON') and group_level < :maxGroupLevel and group_level >= :minGroupLevel -1")
////  @Filter(name="SetupSystemModel_groupTypeA", condition="group_type = 'A' and (dept_id = :deptid or dept_id = 'COMMON') and (group_code <> 'HRM_Central' and group_code <> 'DEP_Admin')") //sereneChye
//    @OrderBy("group_name")
//    public List<SetupGroup> getSetupGroupAppList() {
//        return setupGroupAppList;
//    }
//
//    public void setSetupGroupAppList(List<SetupGroup> setupGroupAppList) {
//        this.setupGroupAppList = setupGroupAppList;
//    }

    //sereneChye @ 7/11/2014
//    private List<SetupGroup> setupGroupAppHRM_CentralList = new ArrayList();
//    @OneToMany(targetEntity = SetupGroup.class, fetch = FetchType.LAZY, mappedBy = "system_id")
////    @Filter(name="SetupSystemModel_groupTypeA", condition="group_type = 'A' and (dept_id = :deptid or dept_id = 'COMMON')")
//    @Filter(name="SetupSystemModel_groupTypeA_HRM_Central", condition="group_type = 'A' and (dept_id = :deptid or dept_id = 'COMMON')") //sereneChye
//    @OrderBy("group_code")
//    public List<SetupGroup> getSetupGroupAppHRM_CentralList() {
//        return setupGroupAppHRM_CentralList;
//    }
//
//    public void setSetupGroupAppHRM_CentralList(List<SetupGroup> setupGroupAppHRM_CentralList) {
//        this.setupGroupAppHRM_CentralList = setupGroupAppHRM_CentralList;
//    }
//    private List<SetupGroup> setupGroupAppDEP_AdminList = new ArrayList();
//    @OneToMany(targetEntity = SetupGroup.class, fetch = FetchType.LAZY, mappedBy = "system_id")
////    @Filter(name="SetupSystemModel_groupTypeA", condition="group_type = 'A' and (dept_id = :deptid or dept_id = 'COMMON')")
//    @Filter(name="SetupSystemModel_groupTypeA_DEP_Admin", condition="group_type = 'A' and (dept_id = :deptid or dept_id = 'COMMON')and (group_code <> 'HRM_Central')") //sereneChye
//    @OrderBy("group_code")
//    public List<SetupGroup> getSetupGroupAppDEP_AdminList() {
//        return setupGroupAppDEP_AdminList;
//    }
//
//    public void setSetupGroupAppDEP_AdminList(List<SetupGroup> setupGroupAppDEP_AdminList) {
//        this.setupGroupAppDEP_AdminList = setupGroupAppDEP_AdminList;
//    }
//    private List<SetupGroup> setupGroupWFList = new ArrayList();
//    @OneToMany(targetEntity = SetupGroup.class, fetch = FetchType.LAZY, mappedBy = "system_id")
//    @Filter(name="SetupSystemModel.groupTypeW", condition="group_type = 'W' and (dept_id = :deptid or dept_id = 'COMMON')")
//    @OrderBy("group_name")
//    public List<SetupGroup> getSetupGroupWFList() {
//        return setupGroupWFList;
//    }
//    public void setSetupGroupWFList(List<SetupGroup> setupGroupWFList) {
//        this.setupGroupWFList = setupGroupWFList;
//    }
//    
    private String sysHasAssign = "N";

    @Transient
    public String getSysHasAssign() {
        return sysHasAssign;
    }

    public void setSysHasAssign(String sysHasAssign) {
        this.sysHasAssign = sysHasAssign;
    }

}
