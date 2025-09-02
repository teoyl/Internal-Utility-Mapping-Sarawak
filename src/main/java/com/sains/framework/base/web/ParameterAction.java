package com.sains.framework.base.web;

import com.opensymphony.xwork2.ModelDriven;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseActionSupport;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.BaseException;
import com.sains.framework.base.LogFunction;
import com.sains.framework.model.ParameterModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterAction extends BaseActionSupport<ParameterModel> implements ModelDriven<ParameterModel> {

    private static final long serialVersionUID = -6659925652584240539L;
    private List<ParameterModel> modelList = null;
    private List modelGroupList = new ArrayList();
    private String pageRequired_ = null;

    public ParameterAction() {
        model = new ParameterModel();
    }
    
    public ParameterAction(org.hibernate.Session session) {
        super(session);
        model = new ParameterModel();
    }

    @Override // change the "String" and return value to the
    public ParameterModel getModel() {
        return model;
    }

    public List<ParameterModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<ParameterModel> modelList) {
        this.modelList = modelList;
    }

    public void specificValidation(String validationType) {
    }

    public String processInsert() {
        String rtnStr = SUCCESS;
        try {
//            if (modelList != null) {
//                for (ParameterModel param : (List<ParameterModel>)modelList) {
//                    if (param == null){
//                        System.out.println("param is null");
//                    } else {
//                        System.out.println("param.id = " + param.getID());
//                    }
//                }
//            }
            modelList.get(0).setSystem_code(systemCode_);
            modelList.get(0).setSystem_descs(systemDesc_);
            serviceFactory.getParameterService().insert(modelList);
            setId(modelList.get(0).getSystem_code());
            addActionMessage(getText("createSuccess"));
        } catch (BaseException be) {
            addActionError(be.getMessage());
            setId(modelList.get(0).getSystem_code());
            rtnStr = SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
        } catch (Exception e) {
            if (e.getCause() != null) {
                addActionError(e.getCause().getMessage());
            } else {
                addActionError(e.getMessage());
            }
            new LogFunction().logError(this.getClass(), "", e);
            setId(modelList.get(0).getSystem_code());
            return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
        }
        return loadEditPage();
    }

    private String systemCode_;
    private String systemDesc_;

    public String getSystemCode_() {
        return systemCode_;
    }

    public void setSystemCode_(String systemCode_) {
        this.systemCode_ = systemCode_;
    }

    public String getSystemDesc_() {
        return systemDesc_;
    }

    public void setSystemDesc_(String systemDesc_) {
        this.systemDesc_ = systemDesc_;
    }
    
    public String loadAddPage() {
        setId(modelList.get(0).getSystem_code());
        loadEditPage();
        return SystemConstants.ACTION_Status.LOAD_ADD_PAGE;
    }

    //retrieve data for editing.
    @Override
    public String loadEditPage() {
        try {
            BaseDAO retrievingDAO = new BaseDAOImpl();
            retrievingDAO.setSession(baseDAO.getSession());
            Map param = new HashMap();
            param.put("system_code", getId().toString());
//            BaseActionSupport.defaultEntIdToParam(param);
            modelList = (ArrayList) retrievingDAO.list_order(param, ParameterModel.class, false, "order by group_code, group_item_order");
            String tempGroupCode = "null";
            List tempList = null;
            int idx = 0;
            String tempRequired = null;
            for (ParameterModel paramModel : modelList) {
                if (paramModel.getField_indicator().equalsIgnoreCase("input")){
                    if (paramModel.getReq_field().equalsIgnoreCase("Y")) {
                        if (!paramModel.getParameter_setup().startsWith("radio;")) {
                            if (tempRequired == null) {
//                                tempRequired = "modelList"+idx+"_parameter_value;"+paramModel.getParameter_descs();
                                tempRequired = "modelList"+idx+"_parameter_value;"+paramModel.getValue_explaination();
                            } else {
//                                tempRequired += ",modelList"+idx+"_parameter_value;"+paramModel.getParameter_descs();
                                tempRequired += ",modelList"+idx+"_parameter_value;"+paramModel.getValue_explaination();
                            }
                        }
                    }
                    //to populate select with lookup
                    if (paramModel.getParameter_setup().startsWith("select;") && !Validator.isEmpty(paramModel.getNumericJavascript()) ) {
                        paramModel.setLookUp("modelList"+idx+"_parameter_value");
                    }
                }
                idx++;
                if (paramModel.getGroup_code().equals(tempGroupCode)) {
                    tempList.add(paramModel);
                } else {
                    tempGroupCode = paramModel.getGroup_code();
                    tempList = new ArrayList();
                    tempList.add(paramModel);
                    modelGroupList.add(tempList);
                }
            }
            if (tempRequired != null) {
                pageRequired_ = "";
                int count = 1;
                if (tempRequired != null) {
                    for (String req : tempRequired.split(",")) {
                        String[] reqArr = req.split(";");
                        pageRequired_ += "this.a" + count++ + " = new Array('" + reqArr[0].trim() + "', '" + getText(reqArr[1]) + "'); " + "\r\n";
                    }
                }
            }
        } catch (Exception e) {
            new LogFunction().logError(this.getClass(), "", e);
            return SUCCESS;
        }
        return SystemConstants.ACTION_Status.LOAD_EDIT_PAGE;
    }

    @Override
    public String processUpdate() {
        String rtnStr = SUCCESS;
        try {
//            if (modelList != null) {
//                for (ParameterModel param : (List<ParameterModel>)modelList) {
//                    if (param == null){
//                        System.out.println("param is null");
//                    } else {
//                        System.out.println("param.id = " + param.getID());
//                    }
//                }
//            }
            setId(modelList.get(0).getSystem_code());
            serviceFactory.getParameterService().update(modelList);
            addActionMessage(getText("updateSuccess"));
        } catch (BaseException be) {
            addActionError(be.getMessage());
            rtnStr = SystemConstants.ACTION_Status.UPDATE_FAIL;
        } catch (Exception e) {
            if (e.getCause() != null) {
                addActionError(e.getCause().getMessage());
            } else {
                addActionError(e.getMessage());
            }
            new LogFunction().logError(this.getClass(), "", e);
            return SystemConstants.ACTION_Status.UPDATE_FAIL;
        }
        return loadEditPage();
    }

    @Override
    public String delete() {
        return SUCCESS;
    }

    public List getModelGroupList() {
        return modelGroupList;
    }

    public void setModelGroupList(List modelGroupList) {
        this.modelGroupList = modelGroupList;
    }

    public String getPageRequired_() {
        return pageRequired_;
    }
    public void setPageRequired_(String pageRequired_) {
        this.pageRequired_ = pageRequired_;
    }
}

