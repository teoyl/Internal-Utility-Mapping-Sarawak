package com.sample;

import com.sains.common.util.Options;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.LogFunction;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.ModelBase;
import com.sains.framework.model.ApplicationRights;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ParentAction extends BaseActionSupport<ParentModel> implements ModelDriven<ParentModel> {

    private static final long serialVersionUID = -6659925652584240539L;
    private String useServiceFactory_ = "N";

    public String getUseServiceFactory_() {
        return useServiceFactory_;
    }

    public void setUseServiceFactory_(String useServiceFactory_) {
        this.useServiceFactory_ = useServiceFactory_;
    }


    public ParentAction() {
        setDaoService_(serviceFactory.getSampleService());
        setAction("Parent"); //Application_code
        model = new ParentModel();
        setCheckCanView_(Boolean.TRUE);
        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);
        // set the required field for common check.
        //getRequiredParam().put("column_name1", "column.name1");
        //getRequiredParam().put("column_name2", "column.name2");


    }

    @Override // change the "String" and return value to the
    public ParentModel getModel() {
        return model;
    }

    public String loadAddPage() {
        try {
            BaseDAO dao = baseDAO;
            dao.getModelById("", ParentModel.class);
            // You Code Here
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);

        }

        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    public String processUpdate() throws Exception {
//        ModelBase.updateCurrentRequestObject("ParentModel_", new HashSet());
//        ModelBase.updateCurrentRequestObject("ParentModel_uc", new String[]{"Parent_id", "Parent_dob", "Parent_gender", "Parent_name", "Parent_own_car", "Parent_state"});
        return super.processUpdate();
    }
    
    
    //retrieve data for editing.
    public String loadEditPage() {
        try {
            model = super.processEdit(getModel());
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String delete() {
        try {
            if (getSelected() != null) {
                getDaoService_().frameworkDelete(getSelected(), ParentModel.class);
            }
            addActionMessage(getText("deleteSuccess"));
        } catch (BaseException be) {
            setRedirectMessage(be.getMessage());
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            setRedirectMessage(getText("updateFail"));
            return SystemConstants.ACTION_Status.INSERT_FAIL;
        }
        return SUCCESS;
    }

    public List getGenderList(){
        return getCommList().getGenderOptions();
    }

    public List getStateList(){
        List list = new ArrayList();
        list.add(new Options("", getText("pleaseSelect")));
        list.add(new Options("1", "State 1"));
        list.add(new Options("2", "State 2"));
        list.add(new Options("3", "State 3"));
        list.add(new Options("4", "State 4"));
        list.add(new Options("5", "State 5"));
        return list;
    }

    public List getYesNoList(){
        List list = new ArrayList();
        list.add(new Options("Y", "Yes"));
        list.add(new Options("N", "No"));
        return list;
    }

    public String processAddChild(){
        if (model.getChildList() == null){
            model.setChildList(new ArrayList());
        }
        model.getChildList().add(new ChildModel());
        return Validator.isEmpty(model.getID())?SystemConstants.ACTION_Status.LOAD_ADD_PAGE:SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    
    public Integer childIndex = null;
    public Integer getChildIndex() {
        return childIndex;
    }
    public void setChildIndex(Integer childIndex) {
        this.childIndex = childIndex;
    }
    
    public String processAddGrandChild(){
        model.getChildList().get(childIndex).getGrandChildList().add(new GrandChildModel());
        return Validator.isEmpty(model.getID())?SystemConstants.ACTION_Status.LOAD_ADD_PAGE:SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    
    public String processDeleteChild() {
        for (int i = model.getArrSelect("childSelected").length; i>0; i--) {
            ChildModel item = model.getChildList().remove(Integer.parseInt(model.getArrSelect("childSelected")[i-1]));
            if (item.getID() != null) {
                model.add_arrDelete("childDeleted", item.getID());
            }
        }
        for (ChildModel child : model.getChildList()) {
            for (int i = child.getArrSelect("gcSelected").length; i>0; i--) {
                GrandChildModel item = child.getGrandChildList().remove(Integer.parseInt(child.getArrSelect("gcSelected")[i-1]));
                if (item.getID() != null) {
                    child.add_arrDelete("gcDeleted", item.getID());
                }
            }
        }
        try {
        /** if direct delete the child from DB : START **/
//            if (!Validator.isEmpty(model.get_arrDelete("child_delected"))){
//                getModel().set_operation("processDeleteChild");
//                serviceFactory.getSampleService().manualUpdate(getModel());
//            }
        /** if direct delete the child from DB : END **/
        
        
//        } catch (BaseException be) {
//            addActionMessage(be.getMessage());
        } catch (Exception e) {
            addActionError(e.getMessage());
        }

        if (Validator.isEmpty(getModel().getID())) {
            setPageSubTitle_("Add");
            return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
        }
        setPageSubTitle_("Edit");
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }
    
//    public String loadInfor() {
//        return loadEditPage();
//    }
//    
//    public String loadFullInfor() {
//        return loadEditPage();
//    }
    public void specificValidation(String validationType) throws Exception {
        if (getModel().getParent_age() <= 10) {
            addActionError("Cannot be lah, parent to young");
        }
    }
}
