package com.sains.framework.model;

import com.sains.common.util.Validator;
import com.sains.framework.base.ModelBase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name="t_setup_module")
@NamedQueries({
    @NamedQuery(name = "Module.findByModuleCode",query = "from Module where module_code = :module_code")
})
public class Module extends ModelBase implements java.io.Serializable{
        private static final long serialVersionUID = 10002L;
	private String module_id = "";
	private String module_code = "";
	private String module_name = "";
	private String module_name_code = "";
	private String module_icon = "";
	private String module_type = "";
        private Integer module_order = 0;
	
	private Module parentModule;
	private List<Module> childModule;
	private List<Module> attachedApplication;
	private String parent_module_id;
	private String created_by;
	private java.sql.Timestamp created_date;
	private String updated_by;
	private java.sql.Timestamp updated_date;
	
	private final String[] updatableColumns = new String[] {"Module_id", "Module_code", "Module_name", "Module_name_code", "Module_icon","Module_type",
			"Module_order", "Parent_module_id", "Updated_by", "Updated_date"};
        private final String[] columnLength = new String[] {"module_code:20","module_name:100"};
        private Map columnLengthMap = null;

        public Module() {
            userDefined_autoValidation(Boolean.TRUE);
            userDefined_insertNoDuplicate("module_code;module.code");
            userDefined_validateRecursive(Boolean.TRUE);
            userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
            //userDefined_updateNoDuplicate("module_code;module.code"); //not necessary if same as insertNoDuplicate
        }

	@Id
	@Column(name="module_id")
	public String getModule_id() {
		return module_id;
	}
	public void setModule_id(String moduleId) {
		module_id = moduleId;
	}
	
	@Transient
	public String getID() {
		return getModule_id();
	}

	public void setID(String moduleId) {
		setModule_id(moduleId);
	}
	
	
	@Column(name="module_code")
	public String getModule_code() {
		return module_code;
	}
	public void setModule_code(String moduleCode) {
		module_code = moduleCode;
	}
	
	@Column(name="module_name")
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String moduleName) {
		module_name = moduleName;
	}

        @Column(name="module_name_code")
        public String getModule_name_code() {
            return module_name_code;
        }

        public void setModule_name_code(String module_name_code) {
            this.module_name_code = module_name_code;
        }
        
	@Column(name="module_icon")
	public String getModule_icon() {
		return module_icon;
	}
	public void setModule_icon(String moduleIcon) {
		module_icon = moduleIcon;
	}

	@Column(name="module_type")
	public String getModule_type() {
            return module_type;
	}
	public void setModule_type(String moduleType) {
            module_type = moduleType;
            if (module_type.equalsIgnoreCase("M")){ //if module_type = Module, parent_module_id should be empty
                parent_module_id = null;
            }
	}

    @Column(name="module_order")
    public Integer getModule_order() {
        return module_order;
    }

    public void setModule_order(Integer module_order) {
        this.module_order = module_order;
    }
	
	@Column(name="parent_module_id")
	public String getParent_module_id() {
		return parent_module_id;
	}
	public void setParent_module_id(String parentModuleId) {
            if (Validator.isEmpty(parentModuleId)){
                parentModuleId = null;
            }
            if (module_type.equalsIgnoreCase("M")){ //if module_type = Module, parent_module_id should be empty
                parentModuleId = null;
            }
            parent_module_id = parentModuleId;
	}
	
	@ManyToOne(targetEntity=Module.class, optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="parent_module_id", insertable=false, updatable=false, nullable = true)
	public Module getParentModule() {
		return parentModule;
	}
	public void setParentModule(Module parentModule) {
		this.parentModule = parentModule;
	}
	
	@OneToMany(targetEntity=Module.class, fetch=FetchType.LAZY, mappedBy="parent_module_id")
	public List<Module> getChildModule() {
		return childModule;
	}
	public void setChildModule(List<Module> childModule) {
		this.childModule = childModule;
	}
	
	@OneToMany(targetEntity=Application.class, fetch=FetchType.LAZY, mappedBy="attached_module_id")
	public List<Module> getAttachedApplication() {
		return attachedApplication;
	}
	public void setAttachedApplication(List<Module> attachedApplication) {
		this.attachedApplication = attachedApplication;
	}
	
	@Column(name="created_by")
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String createdBy) {
		created_by = createdBy;
	}
	
	@Column(name="created_date")
	public java.sql.Timestamp getCreated_date() {
		return created_date;
	}
	public void setCreated_date(java.sql.Timestamp createdDate) {
		created_date = createdDate;
	}
	
	@Column(name="updated_by")
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updatedBy) {
		updated_by = updatedBy;
	}
	
	@Column(name="updated_date")
	public java.sql.Timestamp getUpdated_date() {
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
                    columnLengthMap.put(strA[0].toLowerCase(), strA[1]);
                }
            }

            return columnLengthMap;
        }
}