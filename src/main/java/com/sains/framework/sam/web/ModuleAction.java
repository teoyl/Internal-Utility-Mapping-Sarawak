package com.sains.framework.sam.web;

import java.util.List;
import com.sains.common.util.Validator;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.BaseException;
import com.sains.framework.sam.dao.ModuleDAOImpl;
import com.sains.framework.model.Module;
import java.util.Map;

public class ModuleAction extends BaseActionSupport<Module> implements ModelDriven<Module>{
	private static final long serialVersionUID = -6659925652584240539L;
//	private Module model = new Module();
	//private ModuleDAO moduleDAO = serviceFactory.getModuleService();// new ModuleDAOImpl();
        //private static ModuleDAO moduleDAO = null;
	private List<Options> statusList = null; 
	private List<Options> moduleOptions = null;
	
	private String parent_code = "";
	private String parent_desc = "";
	private String searchPage = "searchStandard";
	

	public ModuleAction(){
            setAction("Module");
            setPageTitle_("Module");
//            baseDAO = new ModuleDAOImpl();
            //moduleDAO = serviceFactory.getModuleService();
            model = new Module();
            // set the required field for common check.
            getRequiredParam().put("module_code", "module.code");
            getRequiredParam().put("module_name", "module.name");
	}

	@Override
	public Module getModel() {
		return model;
	}
	
	public void specificValidation(String validationType){
            //if Module Type = S(Sub Module) the parent code is required.
            if (model.getModule_type().equals("S")){
                if ( (model.getParent_module_id() == null)
//                        || Validator.isEmpty(getParent_code())
                        ){
                    addActionError(getText("field.required.when", new String[]{getText("parent.module"), getText("module.type"), "Sub Module"}));
                }
                //if the Parent's parent module = self, meaning recursive relation occured.
//                if (getActionErrors().size() <= 0 && validationType.equals("update")){
//                    System.out.println("before call getModuleByCode, " + model.getModule_code());
//                    Module parentModule = baseDAO.getModelByCode("module_code", getParent_code(), model);
//                    if (parentModule != null){
//                        while (parentModule != null){
//                            if (parentModule.getParentModule() != null){
//                                if (parentModule.getParentModule().getID().equals(model.getID())){
//                                    addActionError(getText("errors.recursiveRelation"));
//                                    break;
//                                }
//                            }
//                            parentModule = parentModule.getParentModule();
//                        }
//                    }
//
//                }
            }


            if (!Validator.isEmpty(model.getModule_code())){
                //Duplication check. (module_id = "" means it is a new record.
                /*if (module.getModule_id().equals("")){
                    if (moduleDAO.getModuleByCode(module.getModule_code(), module) != null){
                        addActionError(getText("field.duplicated", new String[]{getText("module.code"), module.getModule_code()}));
                    }
                } else { //existing record.
                    if (moduleDAO.getModuleByCode(module.getModule_code(), module) != null &&
                            !moduleDAO.getModuleByCode(module.getModule_code(), module).getModule_id().equals(module.getModule_id())){
                        addActionError(getText("field.duplicated", new String[]{getText("module.code"), module.getModule_code()}));
                    }
                }*/

//                for (String duplicateName : baseDAO.checkDuplicateFields(new String[]{"module_code", "module_name"}, model, false)){
//                    /**
//                     * if the "uniqueByColumns" is true, the return list will be empty if no record found, else, list with 1 object will be returned.
//                     * so can directly put the error message without any other checking.
//                    addActionError(">>" + getText("field.duplicated", new String[]{getText("module.code")+","+getText("module.name"), module.getModule_code()+","+module.getModule_name()}));
//                     */
//                    if (duplicateName.equals("module_code")){
//                        addActionError(getText("field.duplicated", new String[]{getText("module.code"), model.getModule_code()}));
//                    } else if (duplicateName.equals("module_name")){
//                        addActionError(getText("field.duplicated", new String[]{getText("module.name"), model.getModule_name()}));
//                    }
//                }

                // below is the sample to check 1 column for duplication.
                /*if (moduleDAO.isDuplicate("module_code", module)){
                    addActionError(getText("field.duplicated", new String[]{getText("module.code"), module.getModule_code()}));
                }
                // to check multiple columns, have to call same methods repetively.
                if (moduleDAO.isDuplicate("module_name", module)){
                    addActionError(getText("field.duplicated", new String[]{getText("module.name"), module.getModule_name()}));
                }*/
                
                //Parent Code checking
                if (model.getModule_code().equals(getParent_code())){
                    addActionError(getText("field.cannotBeSame", new String[]{getText("module.code"), getText("parent.code")}));
                }
            }
	}

	public String processInsert(){
            try{
                setPageSubTitle_("Add");
		validateRequired();
		//specificValidation("insert");
		if (getActionErrors().size() > 0){
                    return SystemConstants.ACTION_Status.INSERT_FAIL;
		}
		serviceFactory.getModuleService().insert(getModel());
                addActionMessage(getText("createSuccess"));
                return SUCCESS;
            } catch (BaseException be) {
                addActionMessage(be.getMessage());
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            } catch (Exception e) {
                addActionMessage(getText("createFail"));
                return SystemConstants.ACTION_Status.INSERT_FAIL;
//            } finally {
//                baseDAO.closeSession();
                //moduleDAO.closeSession();
            }
	}

	//retrieve data for editing.
	public String loadEditPage(){
            try {
                setPageSubTitle_("Edit");
		model = super.processEdit(getModel());
		if (model.getParentModule() != null){
			setParent_code(model.getParentModule().getModule_code());
			setParent_desc(model.getParentModule().getModule_name());
		}
            } catch (Exception e){

            } finally {
                baseDAO.closeSession();
            }
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
	}

    private String moreInfoUpdate = null;
    public String getMoreInfoUpdate() {
        return moreInfoUpdate;
    }
    public void setMoreInfoUpdate(String moreInfoUpdate) {
        this.moreInfoUpdate = moreInfoUpdate;
    }
    
    public String processUpdate(){
        if (moreInfoUpdate!=null && moreInfoUpdate.equalsIgnoreCase("Y")) {
            moreInfoUpdate = null;
            Map map = getDynamicActionSetup("Module");
            noDeco_include = (String)map.get("moreInfoJsp");
            return processUpdate()+"_moreInfo";
        }
        String rtnStr = SUCCESS;
        try {
            setPageSubTitle_("Edit");
            validateRequired();
            specificValidation("update");
            if (getActionErrors().size() > 0 || getActionMessages().size() > 0){
                return SystemConstants.ACTION_Status.UPDATE_FAIL;
            }
//            Module dbModule = moduleDAO.getModelById(model.getModule_id(), model);
//
//            dbModule.setModule_code(model.getModule_code());
//            dbModule.setModule_name(model.getModule_name());
//            dbModule.setModule_type(model.getModule_type());
//            dbModule.setParent_module_id(model.getParent_module_id());
            serviceFactory.getModuleService().update(model);
            addActionMessage(getText("updateSuccess"));
//            rtnStr = super.processUpdate(model);

        } catch (BaseException be) {
            addActionMessage(be.getMessage());
            return SystemConstants.ACTION_Status.UPDATE_FAIL;
        } catch (Exception e) {
            addActionMessage(getText("updateFail"));
            return SystemConstants.ACTION_Status.UPDATE_FAIL;
        } finally {
//            closeSession();
        }
        return rtnStr;
    }

	public String delete(){
		if (getSelected() == null){
			setRedirectMessage(getText("deleteFail"));
			return SystemConstants.ACTION_Status.DELETE_FAIL;
		}
		Module dbModule;
		for (String deleteId : getSelected()){
			if (deleteId.equalsIgnoreCase("false")){
				setRedirectMessage(getText("deleteFail"));
				return SystemConstants.ACTION_Status.DELETE_FAIL;
			}
			dbModule = baseDAO.getModelById(deleteId, getModel().getClass());
			//if the module is referred by other sub module.
			if (dbModule.getChildModule().size() > 0){
				setRedirectMessage(getText("module.deleteFail.referedByModule", new String[]{model.getModule_code()}));
			} else if (dbModule.getAttachedApplication().size() > 0){
				setRedirectMessage(getText("module.deleteFail.referedByApp", new String[]{model.getModule_code()}));
			}
			
		}
		if (getActionErrors().size() > 0){
			return SystemConstants.ACTION_Status.DELETE_FAIL;
		}
		
		//delete one go!
		for (String deleteId : getSelected()){
			super.delete(deleteId, getModel());
		}
		return SUCCESS;
	}

	public List<Options> getModuleOptions() {
		return getCommList().getModuleTypeOption();
	}

	public void setModuleOptions(List<Options> moduleOptions) {
		this.moduleOptions = moduleOptions;
	}

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parentCode) {
		parent_code = parentCode;
	}

	public String getParent_desc() {
		return parent_desc;
	}

	public void setParent_desc(String parentDesc) {
		parent_desc = parentDesc;
	}

}
