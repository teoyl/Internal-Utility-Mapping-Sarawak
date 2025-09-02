    //serene @ 14.09.2021

package com.sains.framework.model;

import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.workflow.model.SetupSubSystemModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "t_setup_system")
public class SetupSystemModel extends ModelBase implements java.io.Serializable {

    private static final long serialVersionUID = -665992565258121239L;
   private String route_workflow; 
   private String system_api_url; 
    private String system_id; 
    private String system_code; 
    private String system_name; 
    private java.sql.Timestamp created_date; 
    private String created_by; 
    private java.sql.Timestamp updated_date; 
    private String updated_by; 

    //private Group parentGroup;
    private SetupGroup setupGroup;
    private SetupSubSystemModel setupSubSystem;
    
    //private List<Group> attachedApplication;
    private final String[] updatableColumns = new String[]{"Route_Workflow", "System_Id", "System_Code", "System_Name", "Created_Date", "Created_By", "Updated_Date", "Updated_By","System_api_url"};



    @Transient
    public String[] getUpdatableColumns() {
        return updatableColumns;
    }

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
        return getSystem_id();
    }

    public void setID(String system_id) {
        setSystem_id(system_id);
    }
    
     @Column(name = "route_workflow")
    public String getRoute_workflow() {
        return route_workflow;
    }

    public void setRoute_workflow(String route_workflow) {
        this.route_workflow = route_workflow;
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

     @Column(name = "system_api_url")
    public String getSystem_api_url() {
        return system_api_url;
    }

    public void setSystem_api_url(String system_api_url) {
        this.system_api_url = system_api_url;
    }


    
    /*@ManyToOne(targetEntity=Group.class, optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="parent_group_id", insertable=false, updatable=false, nullable = true)
	public Group getParentGroup() {
		return parentGroup;
	}
	public void setParentGroup(Group parentGroup) {
		this.parentGroup = parentGroup;
	}
	
	@OneToMany(targetEntity=Group.class, fetch=FetchType.LAZY, mappedBy="parent_group_id")
	public List<Group> getChildGroup() {
		return childGroup;
	}
	public void setChildGroup(List<Group> childGroup) {
		this.childGroup = childGroup;
	}
	
	@OneToMany(targetEntity=Application.class, fetch=FetchType.LAZY, mappedBy="attached_group_id")
	public List<Group> getAttachedApplication() {
		return attachedApplication;
	}
	public void setAttachedApplication(List<Group> attachedApplication) {
		this.attachedApplication = attachedApplication;
	}*/
    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

  
    public void setCreated_by(String createdBy) {
        created_by = createdBy;
    }

    @Column(name = "created_date")
    public java.sql.Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(java.sql.Timestamp createdDate) {
        created_date = createdDate;
    }

    @Column(name = "updated_date")
    public java.sql.Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updatedDate) {
        updated_date = updatedDate;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updatedBy) {
        updated_by = updatedBy;
    }
    

    @OneToOne(targetEntity=SetupGroup.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="system_id", insertable=false, updatable=false, nullable = true)
    public SetupGroup getSetupGroup() {
        return setupGroup;
    }

    public void setSetupGroup(SetupGroup setupGroup) {
        this.setupGroup = setupGroup;
    }
	
    private List<SetupGroup> setupGroupList;
    @OneToMany(targetEntity = SetupGroup.class, fetch = FetchType.LAZY, mappedBy = "system_id")
    @OrderBy("group_type, group_name")
    public List<SetupGroup> getSetupGroupList() {
        return setupGroupList;
    }

    public void setSetupGroupList(List<SetupGroup> setupGroupList) {
        this.setupGroupList = setupGroupList;
    }
    
    @OneToOne(targetEntity=SetupSubSystemModel.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="system_id", insertable=false, updatable=false, nullable = true)
    public SetupSubSystemModel getSetupSubSystem() {
        return setupSubSystem;
    }

    public void setSetupSubSystem(SetupSubSystemModel setupSubSystem) {
        this.setupSubSystem = setupSubSystem;
    }
    
//    private List<SetupGroup> setupGroupWFList = new ArrayList();
//    @OneToMany(targetEntity = SetupGroup.class, fetch = FetchType.LAZY, mappedBy = "system_id")
//    @Filter(name="SetupSystemModel_groupTypeW", condition="group_type = 'W' and (dept_id = :deptid or dept_id = 'COMMON') and group_level < :maxGroupLevel and group_level >= :minGroupLevel -1")
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
//    @OneToMany(targetEntity = SetupGroup.class, fetch = FetchType.LAZY, mappedBy = "system_id")
//    @Filter(name="SetupSystemModel_groupTypeA", condition="group_type = 'A' and (dept_id = :deptid or dept_id = 'COMMON') and group_level < :maxGroupLevel and group_level >= :minGroupLevel -1")
////  @Filter(name="SetupSystemModel_groupTypeA", condition="group_type = 'A' and (dept_id = :deptid or dept_id = 'COMMON') and (group_code <> 'HRM_Central' and group_code <> 'DEP_Admin')") //sereneChye
//    @OrderBy("group_name")
//    public List<SetupGroup> getSetupGroupAppList() {
//        return setupGroupAppList;
//    }
//
//    public void setSetupGroupAppList(List<SetupGroup> setupGroupAppList) {
//        this.setupGroupAppList = setupGroupAppList;
//    }
}
