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
import com.sains.framework.model.ItemCategory;
import com.sains.framework.model.Module;
import com.sains.framework.model.User;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import org.hibernate.Session;
import org.hibernate.annotations.Where;

public class ParentModelService extends ParentModel {
    ParentModel dbModel = null;
    ParentModel webModel = null;
    public ParentModelService(ModelBase dbModel, ModelBase webModel) {
        this.dbModel = (ParentModel)dbModel;
        this.webModel = (ParentModel)webModel;
        getUppyUpload_appCodeSetup().put("parentModel_file", "parent_file_id;uppyParentModel_actualFolder");
    }

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
            if (dbModel.get_operation().equals("processDeleteChild")) {
                deleteSelected(session, dbModel, dbModel.get_arrDelete("childDeleted"), ChildModel.class);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    public void preInsert(Session session) throws Exception {
        System.out.println("parentModelService - preInsert");
        if (webModel.getParent_name().equalsIgnoreCase("zzz")) { //own business logic
            throw new CustomBaseException("someerror.error");
        }
        
//        Debug.printInfo("SAMPLE : doing own looping and forget to assign Parent's PK to Child's FK");
//        System.out.println("webModel.getChildList().size = " + webModel.getChildList().size());
        for (ChildModel child : webModel.getChildList()) {
            if (Validator.isEmpty(child.getID())) {
                child.defaultAddProperties();
            } else {
                child.defaultUpdateProperties();
            }
            if (child.getChild_name().equals("ABC")) {
                throw new CustomBaseException("someerror.error");
            }
            session.save(child);
            for (GrandChildModel gc : child.getGrandChildList()) {
                if (webModel.get_operation().equals("operation1")) {
                    //checking for operation 1
                    //checking for operation 1
                    //checking for operation 1
                    //checking for operation 1
                    //checking for operation 1
                } else if (webModel.get_operation().equals("operation2")) {
                    //checking for operation 2
                    //checking for operation 2
                    //checking for operation 2
                    //checking for operation 2
                    //checking for operation 2
                }
                if (Validator.isEmpty(gc.getID())) {
                    gc.defaultAddProperties();
                } else {
                    gc.defaultUpdateProperties();
                }
                if (gc.getGc_name().equals("jjj")) {
                    throw new CustomBaseException("someerror.error");
                }
                session.save(gc);
            }
        }
//        webModel.postInsert(session);
    }
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
    @Override
    public void preUpdate(Session session, Object updatingModel) throws Exception {
        System.out.println("parentModelService - preUpdate");
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        ParentModel webModel = (ParentModel)updatingModel;
        System.out.println("hibernateModel.getParent_age() = " + dbModel.getParent_age());
        System.out.println("webModel.getParent_age() = " + webModel.getParent_age());
//        if (dbModel.getParent_age() > webModel.getParent_age()) {
//            throw new CustomBaseException("Only can get older");
//        }
        System.out.println("webModel.childList.size = " + webModel.getChildList().size());
//        try {
//            deleteSelected(session, webModel.get_arrDelete("childDeleted"), ChildModel.class); //delete the marked to be deleted child first before insert/udpate new/existing child
//            updateList(session, webModel.getChildList(), "setParent_id");
//        } catch (Exception e) {
//            throw e;
//        }
    }
    
    public void getMyChildList_byOperation() {
        System.out.println("getMyChildList_byOperation ------------- parent model service, operation = " + get_operation());
        if (get_operation() != null) {
            if (get_operation().equals("")) {//empty = default
                setupMyChildList("getChildList", ChildModel.class, "childDeleted", Boolean.FALSE);
                setupMyChildList("getPropertyList", PropertyModel.class, "propertyDeleted", Boolean.FALSE);
            } else if (get_operation().equals("verification")) {
                setupMyChildList("getChildList", ChildModel.class, "childDeleted", Boolean.FALSE);
            } else if (get_operation().equals("approval")) {
                setupMyChildList("getxyzlist", ChildModel.class, "childDeleted", Boolean.FALSE);
            } else if (get_operation().equals("update_property_only")) {
                setupMyChildList("getPropertyList", PropertyModel.class, "propertyDeleted", Boolean.FALSE);
            }
        }
    }
    

}
