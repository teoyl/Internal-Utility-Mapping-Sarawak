package com.sains.framework.sam.web;

import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.model.GitemModel;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.sains.common.util.Validator;
import com.sains.common.util.DateUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.framework.base.BaseActionSupport;

import com.sains.framework.lookup.DataRetriever;
import com.sains.framework.base.CommonFunction;
import org.apache.struts2.components.ActionError;


public class GitemAction extends BaseActionSupport<GitemModel> implements ModelDriven<GitemModel>{
	private static final long serialVersionUID = -6659925652584240539L;
	private GitemModel model = new GitemModel();
	private BaseDAO modelDAO = new BaseDAOImpl();
	private List<Options> statusList = null; 
	private List<Options> moduleOptions = null;
	
	private String category_code = "";
	private String category_desc = "";
	private String searchPage = "searchStandard";
	

	public GitemAction(){
		// set the required field for common check.
		//getRequiredParam().put("module_code", "module.code");
		//getRequiredParam().put("module_name", "module.name");
	}

	@Override
	public GitemModel getModel() {
		return model;
	}
	
	public void specificValidation(String validationType){

	}

	public String processInsert(){
            try{
		validateRequired();
		specificValidation("insert");
		if (getActionErrors().size() > 0){
                    return SystemConstants.ACTION_Status.INSERT_FAIL;
		}

		return super.insert(getModel());
            } catch (Exception e) {
                return SystemConstants.ACTION_Status.INSERT_FAIL;
            } finally {
                baseDAO.closeSession();
                modelDAO.closeSession();
            }
	}

	public String loadAddPage(){
		return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
	}

	//retrieve data for editing.
	public String loadEditPage(){
            try {
		model = super.processEdit(getModel());
		if (model.getItemCategory() != null){
			setCategory_code(model.getItemCategory().getItem_category_code());
			setCategory_desc(model.getItemCategory().getItem_category_name());
		}
            } catch (Exception e){

            } finally {
                baseDAO.closeSession();
            }
            return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
	}

    public String processUpdate(){
        String rtnStr = SUCCESS;
        try {


        } catch (Exception e) {

        } finally {
            closeSession();
        }
        return rtnStr;
    }

	public String delete(){
		
		return SUCCESS;
	}

	public List<Options> getModuleOptions() {
		if (moduleOptions == null){
			moduleOptions = new ArrayList();
			moduleOptions.add(new Options("M", "Module"));
			moduleOptions.add(new Options("S", "Sub Module"));
		}
		return moduleOptions;
	}

	public void setModuleOptions(List<Options> moduleOptions) {
		this.moduleOptions = moduleOptions;
	}

	public String getCategory_code() {
		return category_code;
	}

	public void setCategory_code(String categoryCode) {
		category_code = categoryCode;
	}

	public String getCategory_desc() {
		return category_desc;
	}

	public void setCategory_desc(String categoryDesc) {
		category_desc = categoryDesc;
	}

}
