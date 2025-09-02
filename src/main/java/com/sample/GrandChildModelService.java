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

public class GrandChildModelService extends ChildModel {
    GrandChildModel dbModel = null;
    GrandChildModel webModel = null;
    public GrandChildModelService(ModelBase dbModel, ModelBase webModel) {
        this.dbModel = (GrandChildModel)dbModel;
        this.webModel = (GrandChildModel)webModel;
    }

    @Override
    public void preDelete(Session session, Object deletingObject) throws Exception {
        Debug.printInfo("pre delete of GRAND child model service");
//        if (1==1) {
//            throw new CustomBaseException("Error throw when pre-delete GrandChildModel");
//        }
    }

    @Override
    public void postUpdate(Session session, Object updatingObject) throws Exception {
        Debug.printInfo("postUpdate of GRAND child model service");
    }

    @Override
    public void postInsert(Session session) throws Exception {
        Debug.printInfo("postInsert of GRAND child model service");
    }

    @Override
    public void preInsert(Session session) throws Exception {
        Debug.printInfo("pre Insert of GRAND child model service"); 
    }

    @Override
    public void preUpdate(Session session, Object updatingModel) throws Exception {
        GrandChildModel webModel = (GrandChildModel)updatingModel;
        if (webModel.getGc_name().equals("Error Name")) {
            throw new CustomBaseException("Error with Grand Child's Name");
        }
        System.out.println("pre update of GRAND Child Model Service ");
//        BaseDAOImpl dao = new BaseDAOImpl();
//        dao.setSession(session);
//        ChildModel webModel = (ChildModel)updatingModel;
//        try {
//            deleteSelected(session, webModel.get_arrDelete("gcDeleted"), GrandChildModel.class); //delete the marked to be deleted child first before insert/udpate new/existing child
//            updateList(session, webModel.getGrandChildList(), "setChild_id");
//        } catch (Exception e) {
//            throw e;
//        }
    }

    public String whoAmI() {
        return super.whoAmI() + " GC_Name="+webModel.getGc_name()+ " ChildID=" + webModel.getChild_id();
    }
    

}
