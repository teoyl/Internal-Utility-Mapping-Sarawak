package com.sample.web;

import com.sains.common.util.Options;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.LogFunction;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sample.ParentModel;
import com.sample.ChildModel;
import com.sample.GrandChildModel;
import com.sample.PropertyModel;


public class ParentChildGcGEAction extends BaseActionSupport<ParentModel> implements ModelDriven<ParentModel> {
    private String parent_address_ = null;
    public String getParent_address_() {
        return parent_address_;
    }
    public void setParent_address_(String parent_address_) {
        this.parent_address_ = parent_address_;
    }
    
    public ParentChildGcGEAction() {
        setDaoService_(serviceFactory.getSampleService());
        setAction("ParentChildGcGE"); //Application_code
        model = new ParentModel();
        setCheckCanView_(Boolean.FALSE);
        setInsertUpdateSuccessCode(SystemConstants.ACTION_Status.LOAD_EDIT_PAGE);
    }
    
    private String activeTab = "1";
    public String getActiveTab() {
        return activeTab;
    }
    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }
    
    public String getActiveTabLi(String currentTab) {
        if (currentTab.equals(activeTab)) {
            return "active";
        }
        return "";
    }
    
    public String getActiveTabDiv(String currentTab) {
        if (currentTab.equals(activeTab)) {
            return "in active";
        }
        return "";
    }
    
    private Map<String, String> moreHiddenMap = new HashMap();
    public String moreHiddenValue(String key) {
        return moreHiddenMap.get(key);
    }
    
    private String entryParent;
    public String getEntryParent() {
        return entryParent;
    }
    public void setEntryParent(String entryParent) {
        this.entryParent = entryParent;
    }

    private String entryParentLvl;
    public String getEntryParentLvl() {
        return entryParentLvl;
    }
    public void setEntryParentLvl(String entryParentLvl) {
        this.entryParentLvl = entryParentLvl;
    }

    @Override
    public ParentModel getModel() {
        return model;
    }

    public String loadAddPage() {
        try {
            setAction("ParentChildGcGE"); //Application_code
            // Your Code Here
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);

        }

        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    //retrieve data for editing.
    public String loadEditPage() {
        try {
            setAction("ParentChildGcGE"); //Application_code
            model = super.processEdit(getModel()); //use getId() to retrieve model
            // Your Code Here
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    @Override
    public void specificValidation(String validationType) throws Exception {
        
    }
    
    public String processAddChildList() {
        ChildModel newItem = new ChildModel();
        newItem.set_hideShowMore("S");
        model.getChildList().add(newItem);
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processAddGrandChildList() {
        GrandChildModel newItem = new GrandChildModel();
        newItem.set_hideShowMore("S");
        model.getChildList().get(Integer.parseInt(entryParentLvl)).getGrandChildList().add(newItem);
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    public String processAddPropertyList() {
        PropertyModel newItem = new PropertyModel();
        newItem.set_hideShowMore("S");
        model.getPropertyList().add(newItem);
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }


        public List getDivisionList() {
        return getListFromCommonList("getDivisionList", "pleaseSelect");
    }


}
