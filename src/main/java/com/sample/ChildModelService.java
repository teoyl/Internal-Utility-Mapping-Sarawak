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

public class ChildModelService extends ChildModel {
    ChildModel dbModel = null;
    ChildModel webModel = null;
    public ChildModelService(ModelBase dbModel, ModelBase webModel) {
        userDefined_autoValidation(Boolean.TRUE);
//        userDefined_insertNoDuplicate("Child_name;Child Name");
        userDefined_insertNoDuplicateRange("parent_id,child_name");
        userDefined_duplicateFieldsRangeKeyDescription("sample.child.name");
//        setupMyChildList("getGrandChildList", GrandChildModel.class, "gcDeleted", Boolean.TRUE);
        userDefined_insertRequired("Child_name;sample.child.name");
        userDefined_updateRequired("Child_name;sample.child.name");
        //userDefined_validateRecursive(Boolean.TRUE);
        //userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
        this.dbModel = (ChildModel)dbModel;
        this.webModel = (ChildModel)webModel;
        getUppyUpload_appCodeSetup().put("childModel_file", "child_file_id;uppyChildModel_actualFolder");
    }

    @Override
    public void preDelete(Session session, Object deletingObject) throws Exception {
        Debug.printInfo("pre delete of child model service");
        System.out.println("((ModelBase)deletingObject).ID = " + ((ModelBase)deletingObject).getID());
        System.out.println("webModel.getID = " + webModel.getID());
        /** checking to prevent deletion of record if it is referred by child record **/
        checkingDAO.setSession(session);
//        if (checkingDAO.sqlCountRecord("select count(*) from t_grand_child where child_id = '"+ ((ModelBase)deletingObject).getID() +"'", null) > 0) {

        /** checking before deleting a record : START **/
//        Map param = new HashMap();
//        param.put("childID", ((ModelBase)deletingObject).getID());
//        if (checkingDAO.sqlCountRecord("select count(*) from t_grand_child where child_id = :childID", param) > 0) {
//            throw new CustomBaseException("ChildModel is referring by GrandChildModel");
//        }
        /** checking before deleting a record : END **/
        
        /** checking to prevent deletion of record by certain condition MUST use Hibernate Relation method**/
        //System.out.println("child_name = " + child_name);
        //System.out.println("deletingObject.child_name = " + ((ChildModel)deletingObject).getChild_name());
//        ChildModel childModel = (ChildModel)deletingObject;
//        if (childModel.getChild_name().equalsIgnoreCase("Pls Keep Me")) {
//            throw new CustomBaseException("Please don''t delete us, I''m \"" + child_name+ "\" and my parent name is \"" + getParentModel().getParent_name() + "\"");
//        }
    }

//    @Override
//    public void preUpdate(Session session, Object updatingModel) throws Exception {
//        BaseDAOImpl dao = new BaseDAOImpl();
//        dao.setSession(session);
//        ChildModel webModel = (ChildModel)updatingModel;
//        try {
//            deleteSelected(session, webModel.get_arrDelete("gcDeleted"), GrandChildModel.class); //delete the marked to be deleted child first before insert/udpate new/existing child
//            updateList(session, webModel.getGrandChildList(), "setChild_id");
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    @Override
    public void preInsert(Session session) throws Exception {
        Debug.printInfo("pre Insert of CHILD model service"); 
        if (webModel.getChild_name().equals("ZZZ")) {
            throw new CustomBaseException("error.something");
        }
    }
    
    public void getMyChildList_byOperation() {
        System.out.println("getMyChildList_byOperation ------------- child model service, operation = " + get_operation());
        if (get_operation() != null) {
            if (get_operation().equals("")) {//empty = default
                setupMyChildList("getGrandChildList", GrandChildModel.class, "gcDeleted", Boolean.TRUE);
//                dbModel.setupMyChildList("getChildList", ChildModel.class, "childDeleted", Boolean.FALSE);
//                dbModel.setupMyChildList("getPropertyList", PropertyModel.class, "propertyDeleted", Boolean.FALSE);
            } else if (get_operation().equals("update_property_only")) {
                setupMyChildList("getPropertyList", PropertyModel.class, "childDeleted", Boolean.FALSE);
            }
        }
    }
    
    @Override
    public void postUpdate(Session session, Object updatingObject) throws Exception {
        Debug.printInfo("postUpdate of CHILD model service");
    }

    @Override
    public void postInsert(Session session) throws Exception {
        Debug.printInfo("postInsert of CHILD model service");
    }

}
