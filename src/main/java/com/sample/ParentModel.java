/* ----------------------------------------------
NAME   : ParentModel.java 
CREATED BY  : Java Model Generator                   
CREATED Date:                    
UPDATED BY  :                                
UPDATED Date:                    
------------------------------------------------*/
package com.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.Debug;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.DrDocRepoModel;
import com.sains.framework.model.ItemCategory;
import com.sains.framework.model.Module;
import com.sains.framework.model.User;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.Session;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "t_parent")
public class ParentModel extends ModelBase implements java.io.Serializable {

    private String created_by;
    private java.sql.Timestamp created_date;
    private String parent_address;
    private Integer parent_age;
    private java.sql.Timestamp parent_dob;
    private String parent_gender;
    private String parent_id;
    private String parent_name;
    private String parent_own_car;
    private String parent_state;
    private String updated_by;
    private java.sql.Timestamp updated_date;
    public String[] updatableColumns = new String[]{"Parent_id", "Parent_address", "Parent_age", "Parent_dob", "Parent_gender", "Parent_name", "Parent_own_car", "Parent_state", "parent_file_id"};
    private final String[] columnLength = new String[]{"parent_gender:1", "parent_id:16", "parent_name:100", "parent_address:100", "parent_own_car:1", "parent_state:3"};
    private Map columnLengthMap = null;

    public ParentModel() {
        showNotOverrideMethod = Boolean.TRUE;
        userDefined_autoValidation(Boolean.TRUE);
        userDefined_insertNoDuplicate("parent_name;Sample.parentName");
//        setupMyChildList("getChildList", ChildModel.class, "childDeleted", Boolean.FALSE);
//        setupMyChildList("getUser", User.class, "userDeleted", Boolean.FALSE);
        userDefined_insertRequired("parent_name;Sample.parentName");
        userDefined_updateRequired("parent_name;Sample.parentName");
//        userDefined_insertControl("<L1>listName;parentModel;childModel's parentId name(FK);deletedItem's instance(</L1>");
//        userDefined_insertControl("<L1>childList;parentModel;parent_id;deletedItem</L1>");
        //userDefined_validateRecursive(Boolean.TRUE);
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "created_date")
    public java.sql.Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "parent_address")
    public String getParent_address() {
        return parent_address;
    }

    public void setParent_address(String parent_address) {
        this.parent_address = parent_address;
    }

    @Column(name = "parent_age")
    public Integer getParent_age() {
        return parent_age;
    }

    public void setParent_age(Integer parent_age) {
        this.parent_age = parent_age;
    }
    
    @Transient
    public String getParent_age_str() {
        return parent_age.toString();
    }

    public void setParent_age_str(String parent_age) {
        try {
            this.parent_age = Integer.parseInt(parent_age);
        } catch (Exception e) {
            this.parent_age = null;
        }
    }

    @Column(name = "parent_dob")
    public java.sql.Timestamp getParent_dob() {
        return parent_dob;
    }

    public void setParent_dob(java.sql.Timestamp parent_dob) {
        this.parent_dob = parent_dob;
    }

    @Transient
    public String getParent_dob_str() {
        return Formatter.formatDate(parent_dob, SystemConstants.DATE.dataEntryFormat);
    }

    public void setParent_dob_str(String parent_dob) {
        try {
            this.parent_dob = DateUtil.getTimestampFromDate(DateUtil.getDate(parent_dob, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e) {
        }
    }

    @Column(name = "parent_gender")
    public String getParent_gender() {
        return parent_gender;
    }

    public void setParent_gender(String parent_gender) {
        this.parent_gender = parent_gender;
    }

    @Id
    @Column(name = "parent_id")
    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    @Transient
    public String getID() {
        return parent_id;
    }

    public void setID(String id) {
        this.parent_id = id;
    }

    @Column(name = "parent_name")
    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    @Column(name = "parent_own_car")
    public String getParent_own_car() {
        return parent_own_car;
    }

    public void setParent_own_car(String parent_own_car) {
        this.parent_own_car = parent_own_car;
    }

    @Column(name = "parent_state")
    public String getParent_state() {
        return parent_state;
    }

    public void setParent_state(String parent_state) {
        this.parent_state = parent_state;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Column(name = "updated_date")
    public java.sql.Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updated_date) {
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

    /* ******** Write your code after this line ****** */
    private List childList = new ArrayList();
    @OneToMany(targetEntity = ChildModel.class, fetch = FetchType.LAZY, mappedBy = "parent_id")
    public List<ChildModel> getChildList() {
        return childList;
    }
    public void setChildList(List<ChildModel> childList) {
        this.childList = childList;
    }
    
    private List propertyList = new ArrayList();
    @OneToMany(targetEntity = PropertyModel.class, fetch = FetchType.LAZY, mappedBy = "parent_id")
    public List<PropertyModel> getPropertyList() {
        return propertyList;
    }
    public void setPropertyList(List<PropertyModel> propertyList) {
        this.propertyList = propertyList;
    }
    
//    private List childGreater20List = new ArrayList();
//    @OneToMany(targetEntity = ChildModel.class, fetch = FetchType.LAZY, mappedBy = "parent_id")
//    @Where(clause="child_age >= 20")
//    public List<ChildModel> getChildGreater20List() {
//        return childGreater20List;
//    }
//    public void setChildGreater20List(List childGreater20List) {
//        this.childGreater20List = childGreater20List;
//    }
//    
//    
//    //for testing only
//    @Transient
//    @OneToMany(targetEntity = ChildModel.class, fetch = FetchType.LAZY, mappedBy = "parent_id")
//    public List<ChildModel> getDeletedChildList() {
//        return getChildList();
//    }
//    public void setDeletedChildList(List<ChildModel> childList) {
//        this.childList = childList;
//    }

//    @Override
//    public void deleteModel(Session session, Object deletingObject) throws Exception {
//        
//    }

    @Override
    public void preDelete(Session session, Object deletingObject) throws Exception {
//        checkingDAO.setSession(session);
//        if (checkingDAO.sqlCountRecord("select count(*) from t_child where parent_id = '"+ ((ModelBase)deletingObject).getID() +"'") > 0) {
//            throw new CustomBaseException("ParentModel is referring by ChildModel ");
//        }
    }
    
    @Override
    public void manualOperation(org.hibernate.Session session) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        try {
            if (this.get_operation().equals("processDeleteChild")) {
                deleteSelected(session, this, this.get_arrDelete("childDeleted"), ChildModel.class);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
//    @Override
//    public void preInsert(Session session) throws Exception {
//        if (getParent_name().equalsIgnoreCase("zzz")) {
//            throw new CustomBaseException("someerror.error");
//        }
//        Debug.printInfo("SAMPLE : doing own looping and forget to assign Parent's PK to Child's FK");
//        for (ChildModel child : getChildList()) {
//            if (Validator.isEmpty(child.getID())) {
//                child.defaultAddProperties();
//            } else {
//                child.defaultUpdateProperties();
//            }
//            if (child.getChild_name().equals("ABC")) {
//                throw new CustomBaseException("someerror.error");
//            }
//            session.save(child);
//            for (GrandChildModel gc : child.getGrandChildList()) {
//                if (Validator.isEmpty(gc.getID())) {
//                    gc.defaultAddProperties();
//                } else {
//                    gc.defaultUpdateProperties();
//                }
//                if (gc.getGc_name().equals("jjj")) {
//                    throw new CustomBaseException("someerror.error");
//                }
//                session.save(gc);
//            }
//        }
//    }
//    @Override
//    public void preInsert(Session session) throws Exception {
//        if (getParent_name().equals("111")) {
//            throw new CustomBaseException("someerror.error");
//        }
//        for (ChildModel child : getChildList()) {
//            if (Validator.isEmpty(child.getID())) {
//                child.defaultAddProperties();
//            } else {
//                child.defaultUpdateProperties();
//            }
//            if (child.getChild_name().equals("ABC")) {
//                throw new CustomBaseException("someerror.error");
//            }
//            session.save(child);
//            for (GrandChildModel gc : child.getGrandChildList()) {
//                if (Validator.isEmpty(gc.getID())) {
//                    gc.defaultAddProperties();
//                } else {
//                    gc.defaultUpdateProperties();
//                }
//                if (gc.getGc_name().equals("jjj")) {
//                    throw new CustomBaseException("someerror.error");
//                }
//                session.save(gc);
//            }
//        }
//    }
/*    @Override
    public void preUpdate(Session session, Object updatingModel) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        ParentModel webModel = (ParentModel)updatingModel;
        if (this.getParent_age() > webModel.getParent_age()) {
            throw new CustomBaseException("Only can get older");
        }
        super.preUpdate(session, updatingModel);
//        try {
//            deleteSelected(session, webModel.get_arrDelete("childDeleted"), ChildModel.class); //delete the marked to be deleted child first before insert/udpate new/existing child
//            updateList(session, webModel.getChildList(), "setParent_id");
//        } catch (Exception e) {
//            throw e;
//        }
    }
*/
    
    private User user;
    @ManyToOne(targetEntity=User.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="created_by", insertable=false, updatable=false, nullable = true)
    public User getUser() {
            return user;
    }
    public void setUser(User user) {
            this.user = user;
    }
    
    @Transient
    public String[] getUpdatableColumnsByOperation() {
        if (updatableByOperationMap == null) {
            updatableByOperationMap = new HashMap();
            updatableByOperationMap.put("update_parent_name", new String[] {"Parent_name"});
            updatableByOperationMap.put("update_parent_age", new String[] {"Parent_age"});
        }
        return super.getUpdatableColumnsByOperation();
    }
    
    private String parent_file_id;
    @Column(name = "parent_file_id")
    public String getParent_file_id() {
        return parent_file_id;
    }
    public void setParent_file_id(String parent_file_id) {
        this.parent_file_id = parent_file_id;
    }
    
    DrDocRepoModel parentDrDocModel = null;
    @OneToOne(targetEntity=DrDocRepoModel.class, optional=true, fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName="dr_doc_id", name="parent_file_id", insertable=false, updatable=false, nullable = true)
    public DrDocRepoModel getParentDrDocModel() {
            return parentDrDocModel;
    }
    public void setParentDrDocModel(DrDocRepoModel parentDrDocModel) {
            this.parentDrDocModel = parentDrDocModel;
    }
}
