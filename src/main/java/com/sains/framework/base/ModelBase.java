/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sains.framework.base;

import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.DateUtil;
import com.sains.common.util.FtpInterface;
import com.sains.common.util.FtpsUtil;
import com.sains.common.util.OBSUtil;
import com.sains.common.util.SFTPBean;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import static com.sains.framework.base.BaseDAOImpl.defaultUpdateProperties;
import com.sains.framework.model.DrDocRepoModel;
import com.sains.framework.model.User;
import com.sample.UppyModel;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.text.WordUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author lenovo
 */
/**
 * Set Duplication Check ON
 *   - userDefined_autoValidation(Boolean.TRUE);
 *   - userDefined_insertNoDuplicate("column1;column1_label_in_package,location_descs;FaLocation.description");
 *   Duplication By Range/Columns setup
 *     - userDefined_insertNoDuplicateRange("Column1, Column2, Column3");
 *     - userDefined_duplicateFieldsRangeKeyDescription("package.properties.message");
 * Set Recursive Check ON
 *   - userDefined_validateRecursive(Boolean.TRUE);
 *   - userDefined_noRecursive("preCheckCondition, getParentLocation, parent_fa_location_id, FaLocation.parentLocation");
 *   eg:
 *     userDefined_noRecursive("module_type:S, getParentModule, parent_module_id, parent.module");
 *     - param1 : only do checking of module_type = "S", if no precheck condition set it as
 *       userDefined_noRecursive(", getParentModule, parent_module_id, parent.module"); //start with comma
 *     - param2 : method to get the parent;
 *     - param3 : the parant's id
 *     - param4 : label of the parent desc.
 * @author lenovo
 */
public abstract class ModelBase {
    public static Boolean TraceModelOperation = Boolean.TRUE;
    public static Integer TraceModelOperation_level = -1;
    public static final String CHILD_KEY = "child_key";
    public static final String PARENT_KEY = "parent_key";
    public static final Boolean GLOBAL_USE_USER_PK_FOR_CREATE_UPDATE_BY = Boolean.FALSE;
    private int primaryKeyLength = 20;
    private Session session_ = null;
    public static final String INSERT = SystemConstants.COMM_OPERATION.INSERT;
    public static final String UPDATE = SystemConstants.COMM_OPERATION.UPDATE;
    private String insertOrUpdate;
    private String insertRequired;
    private String updateRequired;
    private Boolean autoValidate = Boolean.TRUE;
    private Boolean validateRecursive = Boolean.FALSE;
    private String insertNoDuplicate;
    private String updateNoDuplicate;
    private String noRecursive;
    private String insertNoDuplicateRange;
    private String updateNoDuplicateRange;
    private String duplicateFieldsRangeKeyDescription;
    private Map columnLengthMap;
    private boolean isDisabled;
    private boolean isReferred;
    private String _deletedItem;
    private String _deleteOption = "model";
    private String[] _selected;
    private String _operation;
    private String _hideShowMore = "H"; //"H" for Hide: "S" for show
    private String _markedAsDel = "N";
    private Boolean isDataModified_ = Boolean.FALSE;
    private Boolean checkIsDataModified_ = Boolean.FALSE;

    protected BaseDAO checkingDAO = new BaseDAOImpl();

    public enum ReversalOpt {
        INSERT("insert"),
        UPDATE("update"),
        DELETE("delete");
        ReversalOpt(String theOpt) {
            opt = theOpt;
        }
        private String opt;
        public String getOpt() {
            return opt;
        }
        public void setOpt(String opt) {
            this.opt = opt;
        }
    }
    
    public String getID(){return null;}
    public void setID(String id){}

    public Boolean getCheckIsDataModified_() {
        return checkIsDataModified_;
    }

    public void setCheckIsDataModified_(Boolean checkIsDataModified_) {
        this.checkIsDataModified_ = checkIsDataModified_;
    }

    public final void userDefined_insertRequired(String required) {
        if (this.getClass().getSimpleName().endsWith("Service")) {
            updateCurrentRequestObject(this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-7)+"_ud_insertRequired", required);
        } else {
            updateCurrentRequestObject(this.getClass().getSimpleName()+"_ud_insertRequired", required);
        }
        this.insertRequired = required;
    }

    public String get_deletedItem() {
        return _deletedItem;
    }

    public void set_deletedItem(String _deletedItem) {
        this._deletedItem = _deletedItem;
    }
    public void add_deletedItem(String deletedItem) {
        if (Validator.isEmpty(this._deletedItem)) {
            this._deletedItem = deletedItem;
        } else {
            this._deletedItem += ","+deletedItem;
        }
    }

    public String get_deleteOption() {
        return _deleteOption;
    }

    public void set_deleteOption(String _deleteOption) {
        this._deleteOption = _deleteOption;
    }

    public String[] get_selected() {
        return _selected;
    }

    public void set_selected(String[] _selected) {
        this._selected = _selected;
    }

    public String insertRequired() {
        return (String) currentRequestObject(this.getClass().getSimpleName()+"_ud_insertRequired");
//        return insertRequired;
    }

    public final void userDefined_updateRequired(String required) {
        if (this.getClass().getSimpleName().endsWith("Service")) {
            updateCurrentRequestObject(this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-7)+"_ud_updateRequired", required);
        } else {
            updateCurrentRequestObject(this.getClass().getSimpleName()+"_ud_updateRequired", required);
        }
        this.updateRequired = required;
    }

    public String updateRequired() {
        return (String) currentRequestObject(this.getClass().getSimpleName()+"_ud_updateRequired");
//        return updateRequired;
    }

    public void userDefined_autoValidation(Boolean autoValidation) {
        if (this.getClass().getSimpleName().endsWith("Service")) {
            updateCurrentRequestObject(this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-7)+"_ud_autoValidate", autoValidation);
        } else {
            updateCurrentRequestObject(this.getClass().getSimpleName()+"_ud_autoValidate", autoValidation);
        }
        autoValidate = autoValidation;
    }

    public Boolean autoValidation() {
        autoValidate = (Boolean) currentRequestObject(this.getClass().getSimpleName()+"_ud_autoValidate");
        if (autoValidate == null) {
            autoValidate = Boolean.TRUE; //default auto validation to true if not set
        }
        return autoValidate;
    }

    public void userDefined_validateRecursive(Boolean validateRecursive) {
        if (this.getClass().getSimpleName().endsWith("Service")) {
            updateCurrentRequestObject(this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-7)+"_ud_validateRecursive", validateRecursive);
        } else {
            updateCurrentRequestObject(this.getClass().getSimpleName()+"_ud_validateRecursive", validateRecursive);
        }
        this.validateRecursive = validateRecursive;
    }

    public Boolean validateRecursive() {
        validateRecursive = (Boolean) currentRequestObject(this.getClass().getSimpleName()+"_ud_validateRecursive");
        if (validateRecursive == null) {
            validateRecursive = Boolean.FALSE;
        }
        return validateRecursive;
//        return validateRecursive;
    }

    public void userDefined_insertNoDuplicate(String noDuplicate) {
        if (this.getClass().getSimpleName().endsWith("Service")) {
            updateCurrentRequestObject(this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-7)+"_ud_insertNoDuplicate", noDuplicate);
        } else {
            updateCurrentRequestObject(this.getClass().getSimpleName()+"_ud_insertNoDuplicate", noDuplicate);
        }
        this.insertNoDuplicate = noDuplicate;
    }

    public String insertNoDuplicate() {
        return (String) currentRequestObject(this.getClass().getSimpleName()+"_ud_insertNoDuplicate");
//        return insertNoDuplicate;
    }

    public void userDefined_insertNoDuplicateRange(String noDuplicateRange) {
        if (this.getClass().getSimpleName().endsWith("Service")) {
            updateCurrentRequestObject(this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-7)+"_ud_insertNoDuplicateRange", noDuplicateRange);
        } else {
            updateCurrentRequestObject(this.getClass().getSimpleName()+"_ud_insertNoDuplicateRange", noDuplicateRange);
        }
        this.insertNoDuplicateRange = noDuplicateRange;
    }

    public String insertNoDuplicateRange() {
        return (String) currentRequestObject(this.getClass().getSimpleName()+"_ud_insertNoDuplicateRange");
//        return insertNoDuplicateRange;
    }

    public void userDefined_noRecursive(String noRecursive) {
        if (this.getClass().getSimpleName().endsWith("Service")) {
            updateCurrentRequestObject(this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-7)+"_ud_noRecursive", noRecursive);
        } else {
            updateCurrentRequestObject(this.getClass().getSimpleName()+"_ud_noRecursive", noRecursive);
        }
        this.noRecursive = noRecursive;
    }

    public String noRecursive() {
        return (String) currentRequestObject(this.getClass().getSimpleName()+"_ud_noRecursive");
//        return noRecursive;
    }

    public void userDefined_updateNoDuplicate(String noDuplicate) {
        if (this.getClass().getSimpleName().endsWith("Service")) {
            updateCurrentRequestObject(this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-7)+"_ud_updateNoDuplicate", noDuplicate);
        } else {
            updateCurrentRequestObject(this.getClass().getSimpleName()+"_ud_updateNoDuplicate", noDuplicate);
        }
        this.updateNoDuplicate = noDuplicate;
    }

    public String updateNoDuplicate() {
        return (String) currentRequestObject(this.getClass().getSimpleName()+"_ud_updateNoDuplicate");
//        return updateNoDuplicate;
    }

    public void userDefined_updateNoDuplicateRange(String noDuplicateRange) {
        if (this.getClass().getSimpleName().endsWith("Service")) {
            updateCurrentRequestObject(this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-7)+"_ud_updateNoDuplicateRange", noDuplicateRange);
        } else {
            updateCurrentRequestObject(this.getClass().getSimpleName()+"_ud_updateNoDuplicateRange", noDuplicateRange);
        }
        this.updateNoDuplicateRange = noDuplicateRange;
    }

    public String updateNoDuplicateRange() {
        return (String) currentRequestObject(this.getClass().getSimpleName()+"_ud_updateNoDuplicateRange");
//        return updateNoDuplicateRange;
    }

    public void userDefined_duplicateFieldsRangeKeyDescription(String duplicateFieldsRangeKeyDescription) {
        if (this.getClass().getSimpleName().endsWith("Service")) {
            updateCurrentRequestObject(this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length()-7)+"_ud_duplicateFieldsRangeKeyDescription", duplicateFieldsRangeKeyDescription);
        } else {
            updateCurrentRequestObject(this.getClass().getSimpleName()+"_ud_duplicateFieldsRangeKeyDescription", duplicateFieldsRangeKeyDescription);
        }
        this.duplicateFieldsRangeKeyDescription = duplicateFieldsRangeKeyDescription;
    }

    public String duplicateFieldsRangeKeyDescription() {
        return (String) currentRequestObject(this.getClass().getSimpleName()+"_ud_duplicateFieldsRangeKeyDescription");
//        return duplicateFieldsRangeKeyDescription;
    }

    public String insertOrUpdate() {
        return insertOrUpdate;
    }

    public void insertOrUpdate(String insertOrUpdate) {
        this.insertOrUpdate = insertOrUpdate;
    }

    public Map getColumnLengthMap(String[] columnLength) {
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

    public void resetFields(String fields) {
        if (!Validator.isEmpty(fields)) {
            for (String str : fields.split(";")) {
                //
            }
        }
    }

    private void resetField(Object model) {
        if (((ModelBase) model).insertOrUpdate().equals(((ModelBase) model).INSERT)) {
            try {
                Method m = model.getClass().getMethod("setID", Integer.class);
                m.invoke(model, new Integer(0));
            } catch (Exception e) {
                CommonFunction.exceptionStackTrace(e);
            }

        }
    }

    public boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public boolean isIsReferred() {
        return isReferred;
    }

    public void setIsReferred(boolean isReferred) {
        this.isReferred = isReferred;
    }

    // Check any other record that refer to this record
    public void checkForReferenced(org.hibernate.Session session) throws Exception {
        if (this.getModelService() != null) {
            Boolean noSuchMethod = Boolean.FALSE;
            try {
                Method m = this.getModelService().getClass().getDeclaredMethod("checkForReferenced", Session.class);
            } catch (Exception e) {
                noSuchMethod = Boolean.TRUE;
            }
            if (!noSuchMethod) {
                this.getModelService().checkForReferenced(session);
            }
        }
//        System.out.println("checkForReferenced() not override in model.");
        if (showNotOverrideMethod) new LogFunction().logInfo(this.getClass(), "checkForReferenced() not override in model.");
    }

    public void preInsert(org.hibernate.Session session) throws Exception {
//        System.out.println("preInsert() not override in model.");
//        session.save(this);
//        if (this.getModelService() != null) {
//            Boolean noSuchMethod = Boolean.FALSE;
//            try {
//                Method m = this.getModelService().getClass().getDeclaredMethod("preInsert", Session.class);
//            } catch (Exception e) {
//                noSuchMethod = Boolean.TRUE;
//            }
//            if (!noSuchMethod) {
//                this.getModelService().preInsert(session);
//            }
//        } else {
            if (showNotOverrideMethod) new LogFunction().logInfo(this.getClass(), "preInsert() not override in model.");
//        }
    }

    public void manualOperation(org.hibernate.Session session) throws Exception {
//        if (this.getModelService() != null) {
//            Boolean noSuchMethod = Boolean.FALSE;
//            try {
//                Method m = this.getModelService().getClass().getDeclaredMethod("manualOperation", Session.class);
//            } catch (Exception e) {
//                noSuchMethod = Boolean.TRUE;
//            }
//            if (!noSuchMethod) {
//                this.getModelService().manualOperation(session);
//            }
//        } else {
//        System.out.println("preInsert() not override in model.");
            if (showNotOverrideMethod) new LogFunction().logInfo(this.getClass(), "manualOperation() not override in model.");
//        }
    }

    public void preUpdate(org.hibernate.Session session) throws Exception {
//        System.out.println("preUpdate() not override in model.");
        if (showNotOverrideMethod) new LogFunction().logInfo(this.getClass(), "preUpdate() not override in model.");
    }

    public Boolean calledPreUpdate = Boolean.FALSE;
    public void preUpdate(org.hibernate.Session session, Object dataEntryModel) throws Exception {
//        Debug.printFrameworkDebug("PreUpdate " + dataEntryModel.getClass());
//        if (!calledPreUpdate) {
//            ((ModelBase)dataEntryModel).calledPreUpdate = Boolean.TRUE;
//            System.out.println("111");
//            if (((ModelBase)dataEntryModel).getModelService() != null) {
//                System.out.println("222");
//                Boolean noSuchMethod = Boolean.FALSE;
//                try {
//                    System.out.println("333");
//                    Method m = ((ModelBase)dataEntryModel).getModelService().getClass().getDeclaredMethod("preUpdate", Session.class, Object.class);
//                } catch (Exception e) {
//                    System.out.println("444");
//                    noSuchMethod = Boolean.TRUE;
//                }
//                if (!noSuchMethod) {
//                    System.out.println("555");
//                    ((ModelBase)dataEntryModel).clearMyChildMap();
//                    ((ModelBase)dataEntryModel).getModelService().getMyChildList_byOperation();
//                    ((ModelBase)dataEntryModel).getMyChildMap().putAll(((ModelBase)dataEntryModel).getModelService().getMyChildMap());
//                    ((ModelBase)dataEntryModel).getMyChildMapSetup().putAll(((ModelBase)dataEntryModel).getModelService().getMyChildMapSetup());
//                    ((ModelBase)dataEntryModel).getModelService().preUpdate(session, dataEntryModel);
//                    return;
//                }
//            }
//        }
//        System.out.println("doing modelbase.preUpdate() ID=" + ((ModelBase)dataEntryModel).getID());
        checkingDAO.setSession(session);
        ModelBase webModel = (ModelBase)dataEntryModel;
//        if (TraceModelOperation) {
//            if (TraceModelOperation_level == 0) {
//                traceModelOperation_1stLvl("preUpdate", webModel.getModelService().getClass().getSimpleName());
//            }
//        }
//        try {
//            Method m = this.getClass().getMethod("getCreated_by");
//            webModel.setCreated_by(m.invoke(this).toString());
//        } catch (Exception e) {
//        }
        Set<String> childListKeySet = (Set) currentRequestObject(webModel.getClass().getSimpleName() +"_"+ webModel.get_operation());
        if (childListKeySet == null) {
            if (((ModelBase)dataEntryModel).getModelService() == null) {
                webModel.clearMyChildMap();
                webModel.getMyChildList_byOperation();
            }
            childListKeySet = webModel.getMyChildMap().keySet();
        }
        
        for (String childList_getter : childListKeySet) {
            
            String deletedName = (String)currentRequestObject(webModel.getClass().getSimpleName() +"_"+ webModel.get_operation()+"_deletedName");
            deleteSelected(session, webModel, webModel.get_arrDelete(deletedName==null?(String)webModel.getMyChildMapSetup().get(childList_getter+"_deletedName"):deletedName), (Class)webModel.getMyChildMap().get(childList_getter)); //delete the marked to be deleted child first before insert/udpate new/existing child

            Method m = webModel.getClass().getMethod(childList_getter);
            Object listOrModelBase = m.invoke(webModel);
//            Boolean isList = Boolean.FALSE;
//            try {
//                List list = (List)listOrModelBase;
//                isList = Boolean.TRUE;
//            } catch (Exception e) {
//            }
            if (!(listOrModelBase instanceof ModelBase)) {
                List<ModelBase> childList = (List)listOrModelBase;
                if (childList != null && !childList.isEmpty()) { //only need get FkName and convert setter
                    String set_fkName = getter_FK(webModel.getClass(), childList_getter);
                    String firstChar = set_fkName.substring(0, 1);
                    set_fkName = "set" + firstChar.toUpperCase() + set_fkName.substring(1);
                    System.out.println("calling updatelist" + set_fkName);
                    updateList(session, childList, set_fkName, webModel);
                }
            } else {
                ModelBase updateModel = (ModelBase)listOrModelBase;
                if (updateModel != null) {
                    BaseDAO dao = new BaseDAOImpl();
                    dao.setSession(session);
                    ModelBase updatingDbModel = (ModelBase)dao.getModelById(updateModel.getID(), updateModel.getClass());
                    if (updatingDbModel != null) {
                        session.evict(updatingDbModel);
                        dao.updateWithSession(session, updateModel);
                    } else {
                        dao.insertWithSession(session, updateModel);
                        String keyName = m.getAnnotation(JoinColumn.class).name();
                        m = webModel.getClass().getMethod("get" + keyName.substring(0, 1).toUpperCase() + keyName.substring(1));
                        Class returnClass = m.getReturnType();
                        m = webModel.getClass().getMethod("set" + keyName.substring(0, 1).toUpperCase() + keyName.substring(1), returnClass);
                        if (returnClass == Integer.class) {
                            m.invoke(this, new Integer(updateModel.getID()));
                        } else {
                            m.invoke(this, updateModel.getID());
                        }
                    }
                }
            }
        }
    }

    public void postDelete(org.hibernate.Session session) throws Exception {
        Debug.printDebug("postDelete() not override in model.");
        if (showNotOverrideMethod) new LogFunction().logInfo(this.getClass(), "postDelete() not override in model.");
    }

    public void checkData(org.hibernate.Session session) throws Exception {
//        System.out.println("checkData() not override in model.");
        if (showNotOverrideMethod) new LogFunction().logInfo(this.getClass(), "checkData() not override in model.");
    }

//public abstract class ModelBase {
//    public static final String INSERT = "insert";
//    public static final String UPDATE = "update";
//    private String insertRequired;
//    private String updateRequired;
//    private Boolean autoValidate = Boolean.TRUE;
//    private Boolean validateRecursive = Boolean.FALSE;
//    private String insertNoDuplicate;
//    private String updateNoDuplicate;
//    private String noRecursive;
//    private String insertNoDuplicateRange;
//    private String updateNoDuplicateRange;
//    private String duplicateFieldsRangeKeyDescription;
    public Boolean showNotOverrideMethod = Boolean.FALSE;

    //variable for frameworkInsert & frameworkUpdate
    private String insertControl;
    private String updateControl;
    private String deletedItem;
    private Map myChildMap = null;
    private Map myChildMapSetup = null;
//
//    public void userDefined_insertRequired(String required){
//        this.insertRequired = required;
//    }
//    public String insertRequired(){
//        return insertRequired;
//    }
//
//    public void userDefined_updateRequired(String required){
//        this.updateRequired = required;
//    }
//    public String updateRequired(){
//        return updateRequired;
//    }
//    public void userDefined_autoValidation(Boolean autoValidation){
//        autoValidate = autoValidation;
//    }
//    public Boolean autoValidation(){
//        return autoValidate;
//    }
//    public void userDefined_validateRecursive(Boolean validateRecursive){
//        this.validateRecursive = validateRecursive;
//    }
//    public Boolean validateRecursive(){
//        return validateRecursive;
//    }
//    public void userDefined_insertNoDuplicate(String noDuplicate){
//        this.insertNoDuplicate = noDuplicate;
//    }
//    public String insertNoDuplicate(){
//        return insertNoDuplicate;
//    }
//
//    public void userDefined_insertNoDuplicateRange(String noDuplicateRange){
//        this.insertNoDuplicateRange = noDuplicateRange;
//    }
//    public String insertNoDuplicateRange(){
//        return insertNoDuplicateRange;
//    }
//
//    public void userDefined_noRecursive(String noRecursive){
//        this.noRecursive = noRecursive;
//    }
//    public String noRecursive(){
//        return noRecursive;
//    }
//
//    public void userDefined_updateNoDuplicate(String noDuplicate){
//        this.updateNoDuplicate = noDuplicate;
//    }
//    public String updateNoDuplicate(){
//        return updateNoDuplicate;
//    }
//
//    public void userDefined_updateNoDuplicateRange(String noDuplicateRange){
//        this.updateNoDuplicateRange = noDuplicateRange;
//    }
//    public String updateNoDuplicateRange(){
//        return updateNoDuplicateRange;
//    }
//
//    public void userDefined_duplicateFieldsRangeKeyDescription(String duplicateFieldsRangeKeyDescription){
//        this.duplicateFieldsRangeKeyDescription = duplicateFieldsRangeKeyDescription;
//    }
//
//    public String duplicateFieldsRangeKeyDescription(){
//        return duplicateFieldsRangeKeyDescription;
//    }

    public String insertControl() {
        return insertControl;
    }

    public void userDefined_insertControl(String insertControl) {
        this.insertControl = insertControl;
    }

    public String updateControl() {
        if (Validator.isEmpty(updateControl)) {
            updateControl = insertControl;
        }
        return updateControl;
    }

    public void userDefined_updateControl(String updateControl) {
        this.updateControl = updateControl;
    }

    public String getDeletedItem() {
        return deletedItem;
    }

    public void setDeletedItem(String deletedItem) {
        this.deletedItem = deletedItem;
    }
    
    public void clearMyChildMap() {
        getMyChildMap().clear();
        getMyChildMapSetup().clear();
    }
    public final void setupMyChildList(String listName, Class childClass, String listDeletedName, Boolean deleteUsingSQL) {
        getMyChildMap().put(listName, childClass);
        getMyChildMapSetup().put(listName+"_deletedName", listDeletedName);
        getMyChildMapSetup().put(listName+"_useSQL", deleteUsingSQL);
        
        updateCurrentRequestObject(this.getClass().getSimpleName()+"_"+ get_operation(), getMyChildMap().keySet());
        updateCurrentRequestObject(this.getClass().getSimpleName()+"_"+ get_operation()+"_deletedName", listDeletedName);
        updateCurrentRequestObject(this.getClass().getSimpleName()+"_"+ get_operation()+"_useSQL", deleteUsingSQL);
    }
    
    public void getMyChildList_byOperation() {
        
    }
    public Map myChildMap_byOperation = null;
    public Map<String, Object> getMyChildMap_byOperation() {
        return getMyChildMap();
    }
    public Map getMyChildMapSetup_byOperation() {
        return getMyChildMapSetup();
    }
    
    public final Map<String, Object> getMyChildMap() {
        if (myChildMap == null) myChildMap = new HashMap();
        return myChildMap;
    }

    public Map getMyChildMapSetup() {
        if (myChildMapSetup == null) myChildMapSetup = new HashMap();
        return myChildMapSetup;
    }

    public void setDeletingItemMap(Map myChildMap) {
        this.myChildMap = myChildMap;
    }

    public void insertValidation(Session session, Object insertingObject) throws Exception {
        if (showNotOverrideMethod) Debug.printInfo("insertValidation not override");
    }

    public void postInsert(Session session) throws Exception {
        
        moveToActualPath(session);//try to move temp file to actual folder
        checkingDAO.setSession(session);
        Set<String> childListKeySet = (Set) currentRequestObject(this.getClass().getSimpleName() +"_"+ this.get_operation());
        if (childListKeySet == null) {
            if (((ModelBase)this).getModelService() == null) {
                clearMyChildMap();
                getMyChildList_byOperation();
            }
            childListKeySet = this.getMyChildMap().keySet();
        }
        for (String childList_getter : childListKeySet) {
            Method m = this.getClass().getMethod(childList_getter);
            Object listOrModelBase = m.invoke(this);
            if (!(listOrModelBase instanceof ModelBase)) {
                List<ModelBase> list = (List)m.invoke(this);
                if (list!=null && !list.isEmpty()) {
                    List newList = (List) currentRequestObject("insertUpdateList");
                    if (newList == null) {
                        newList = new ArrayList();
                        updateCurrentRequestObject("insertUpdateList", newList);
                    }
                    newList.add(list);
                }
                if (list != null) {
                    Integer idx = 1;
                    for (ModelBase child : list) {
                        createIncreaseIdxVar(idx++);
                        //do not insert the marked as deleted record
                        if (child.get_markedAsDel()!=null && child.get_markedAsDel().equalsIgnoreCase("Y")) {
                            continue;
                        }
                        String set_fkName = getter_FK(this.getClass(), childList_getter);
                        String firstChar = set_fkName.substring(0, 1);
                        set_fkName = "set" + firstChar.toUpperCase() + set_fkName.substring(1);
                        if (isIntegerPk(this.getClass())) {
                            m = child.getClass().getMethod(set_fkName, Integer.class);
                            m.invoke(child, new Integer(this.getID()));
                        } else {
                            m = child.getClass().getMethod(set_fkName, String.class);
                            m.invoke(child, this.getID());
                        }
                        checkingDAO.insertWithSession(session, child);
                    }
                    reduceIdxVar();
                }
            } else {
                if (listOrModelBase!=null) {
                    List newList = (List) currentRequestObject("insertUpdateList");
                    if (newList == null) {
                        newList = new ArrayList();
                        updateCurrentRequestObject("insertUpdateList", newList);
                    }
                    newList.add(listOrModelBase);
                }
                if (listOrModelBase != null) {
                        //do not insert the marked as deleted record
                    if ( ((ModelBase)listOrModelBase).get_markedAsDel()!=null && ((ModelBase)listOrModelBase).get_markedAsDel().equalsIgnoreCase("Y")) {
                        continue;
                    }
                    String set_fkName = getter_FK_manyToOne(this.getClass(), childList_getter);
                    String firstChar = set_fkName.substring(0, 1);
                    set_fkName = "set" + firstChar.toUpperCase() + set_fkName.substring(1);
                    if (isIntegerPk(this.getClass())) {
                        m = ((ModelBase)listOrModelBase).getClass().getMethod(set_fkName, Integer.class);
                        m.invoke(((ModelBase)listOrModelBase), new Integer(this.getID()));
                    } else {
                        m = ((ModelBase)listOrModelBase).getClass().getMethod(set_fkName, String.class);
                        m.invoke(((ModelBase)listOrModelBase), this.getID());
                    }
                    checkingDAO.insertWithSession(session, ((ModelBase)listOrModelBase));
                }
            }
        }
    }
    
    private Map<String, Object> indexVar() {
        if (currentRequestObject("indexVar") == null) {
            updateCurrentRequestObject("indexVar", new HashMap());
        }
        return (Map)currentRequestObject("indexVar");
    }
//    private String indexLevel(String ID, Integer level) {
//        Map levelMap = indexVar().get(ID+ "_" + level);
//        if (levelMap == null) {
//            
//        }
//    }
    private String currentIdxStr() {
        if (indexVar().get("currentIdxStr") == null) {
            return "";
        }
        return (String)indexVar().get("currentIdxStr");
    }
    private void createIncreaseIdxVar(Integer idx) {
        System.out.println(" in createIncreaseIdxVar idx = " + idx);
        String currentIdx = (String)indexVar().get("currentIdxVar");
        if (idx.equals(1)) {
            if (currentIdx == null) {
                currentIdx = "1";
            } else {
                if (indexVar().get("currentIdxStr") != null) {
                    currentIdx = (String)indexVar().get("currentIdxStr");
                }
                System.out.println("1. currentIdx = " + currentIdx);
                System.out.println("2. currentIdx = " + currentIdx);
                Integer lastDigit = null;
//                if (currentIdx.contains(".")) {
//                    lastDigit = Integer.parseInt(currentIdx.substring(currentIdx.indexOf(".")+1));
//                    currentIdx = currentIdx.substring(0, currentIdx.indexOf("."));
//                    System.out.println("3. currentIdx = " + currentIdx + " lastDigit = " + lastDigit);
//                    currentIdx += "."+lastDigit;
//                    System.out.println("4. currentIdx = " + currentIdx);
//                } else {
//                    lastDigit = Integer.parseInt(currentIdx);
//                    System.out.println("5. currentIdx = " + currentIdx + " lastDigit = " + lastDigit);
//                    currentIdx = lastDigit+"";
//                    System.out.println("6. currentIdx = " + currentIdx);
//                }
            }
        }
        indexVar().put("currentIdxVar", currentIdx);
        indexVar().put("currentIdx", idx);
        indexVar().put("currentIdxStr", currentIdx  + "." + idx);
//        return currentIdx + "." + idx;
    }
    private void reduceIdxVar() {
        String currentIdx = (String)indexVar().get("currentIdxStr");
        if (currentIdx != null) {
            if (currentIdx.contains(".")) {
                System.out.println("b4 currentIdx =  " + currentIdx);
                currentIdx = currentIdx.substring(0, currentIdx.lastIndexOf("."));
                try {
                    Integer.parseInt(currentIdx);
                    currentIdx = (Integer.parseInt(currentIdx)+1) +"";
                } catch (Exception e) {
                }
                indexVar().put("currentIdxStr", currentIdx);
                if (currentIdx.contains(".")) {
                    currentIdx = currentIdx.substring(0, currentIdx.lastIndexOf("."));
                }
                System.out.println("after currentIdx =  " + currentIdx);
            } else {
                currentIdx = (Integer.parseInt(currentIdx) + 1) + "";
            }
        } else {
            currentIdx = "1";
        }
        indexVar().put("currentIdxVar", currentIdx);
    }
    
    public void updateList(Session session, List childList, String fk_setter, ModelBase parentModel) throws Exception {
        
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        if (childList!=null && !childList.isEmpty()) {
            List list = (List) currentRequestObject("insertUpdateList");
            if (list == null) {
                list = new ArrayList();
                updateCurrentRequestObject("insertUpdateList", list);
            }
            list.add(childList);
        }
        Integer idx = 1;
        for (ModelBase child : (List<ModelBase>)childList) {
            createIncreaseIdxVar(idx++);
            //do not insert/update the marked as deleted record
            if (child.get_markedAsDel()!=null && child.get_markedAsDel().equalsIgnoreCase("Y")) {
                deleteModel(session, child);
                continue;
            }
            if (Validator.isEmpty(child.getID())) {
                System.out.println("111. this.getID = " + parentModel.getID());
                if (isIntegerPk(this.getClass())) {
                    Method m = child.getClass().getMethod(fk_setter, Integer.class);
                    m.invoke(child, new Integer(parentModel.getID()));
                } else {
                    Method m = child.getClass().getMethod(fk_setter, String.class);
                    m.invoke(child, parentModel.getID());
                }
                System.out.println(" do insert child +++++++++++++++++");
                try {
                    dao.insertWithSession(session, child);
                } catch (Exception e) {
                    if (TraceModelOperation) {
                        addTraceError(e);
                    }
                    throw e;
                }
            } else {
                System.out.println(" do update child +-+-+-+-+-+-+-+-+");
                dao.updateWithSession(session, child);
            }
        }
        reduceIdxVar();
    }

//    public void preUpdate(Session session, Object updatingObject) throws Exception {
//        if (showNotOverrideMethod) System.out.println("preUpdate not override");
//    }

    public void postUpdate(Session session, Object updatingObject) throws Exception {
//        if (((ModelBase)updatingObject).getModelService() != null) {
//            Boolean noSuchMethod = Boolean.FALSE;
//            try {
//                Method m = ((ModelBase)updatingObject).getModelService().getClass().getDeclaredMethod("postUpdate", Session.class, Object.class);
//            } catch (Exception e) {
//                noSuchMethod = Boolean.TRUE;
//            }
//            if (!noSuchMethod) {
//                ((ModelBase)updatingObject).getModelService().postUpdate(session, updatingObject);
//            }
//        } else {
            if (showNotOverrideMethod) Debug.printInfo("postUpdate not override");
//        }
    }

    public void preDelete(Session session, Object deletingObject) throws Exception {
//        if (((ModelBase)deletingObject).getModelService() != null) {
//            Boolean noSuchMethod = Boolean.FALSE;
//            try {
//                Method m = ((ModelBase)deletingObject).getModelService().getClass().getDeclaredMethod("preDelete", Session.class, Object.class);
//            } catch (Exception e) {
//                noSuchMethod = Boolean.TRUE;
//            }
//            if (!noSuchMethod) {
//                ((ModelBase)deletingObject).getModelService().preDelete(session, deletingObject);
//            }
//        } else {
            if (showNotOverrideMethod) Debug.printInfo("preDelete not override");
//        }
    }

    protected String modelTableName(Class modelClass) {
        Table table = (Table)modelClass.getAnnotation(Table.class);
        return table.name();
    }
    protected String model_PK(Class modelClass) {
        String name = null;
        for(Method method : modelClass.getDeclaredMethods()){
//                String name = method.getName();
            if (!method.getName().startsWith("get")) continue;
            Annotation[] annotations = method.getAnnotations();
            Boolean found = Boolean.FALSE;
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].annotationType().equals(Id.class)) {
                    found = Boolean.TRUE;
                }
                if (annotations[i].annotationType().equals(Column.class)) {
                    name = ((Column)annotations[i]).name();
                }
            }
            if (found) {
                break;
            }
        }
        return name;
    }
    
    protected String getter_FK(Class modelClass, String getter) throws Exception {
        Method method = modelClass.getDeclaredMethod(getter);
        Annotation[] annotations = method.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].annotationType().equals(OneToMany.class)) {
                return ((OneToMany)annotations[i]).mappedBy();
            }
        }
        return null;
    }
    protected String getter_FK_manyToOne(Class modelClass, String getter) throws Exception {
        Method method = modelClass.getDeclaredMethod(getter);
        Annotation[] annotations = method.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].annotationType().equals(JoinColumn.class)) {
                return ((JoinColumn)annotations[i]).referencedColumnName();
            }
        }
        return null;
    }
    public void deleteSelected(Session session, ModelBase webModel, String selectedIds, Class deletingClass ) throws Exception {
        System.out.println("deleteSelected..., selectedIds = " + selectedIds);
        if (!Validator.isEmpty(selectedIds)){
            for (String itemId : selectedIds.split(",")){
                ModelBase deletingModel = (ModelBase)deletingClass.getConstructor().newInstance();
                deletingModel.setID(itemId);
                System.out.println("calling preDelete..");
                traceModelOperation("preDelete", webModel.getClass().getSimpleName(), TraceModelOperation_level);
                deletingModel.preDelete(session, deletingModel);
                deletingModel.deleteModel(session, deletingModel);
                System.out.println("calling postDelete..");
                traceModelOperation("postDelete", webModel.getClass().getSimpleName(), TraceModelOperation_level);
                deletingModel.postDelete(session, deletingModel);
            }
        }
    }
    public void deleteModel(Session session, Object deletingObject) throws Exception{
        checkingDAO.setSession(session);
        Set<String> childListKeySet = (Set) currentRequestObject(deletingObject.getClass().getSimpleName() +"_"+ ((ModelBase)deletingObject).get_operation());
        if (childListKeySet == null) {
            if (((ModelBase)deletingObject).getModelService((ModelBase)deletingObject) == null) {
                ((ModelBase)deletingObject).clearMyChildMap();
                ((ModelBase)deletingObject).getMyChildList_byOperation();
            }
            childListKeySet = ((ModelBase)deletingObject).getMyChildMap().keySet();
        }
        Integer idx = 1;
        for (String childList_getter : childListKeySet) {
            createIncreaseIdxVar(idx++);
            Boolean useSQL = (Boolean)currentRequestObject(((ModelBase)deletingObject).getClass().getSimpleName() +"_"+ ((ModelBase)deletingObject).get_operation()+"_useSQL");
            if (useSQL == null) {
                useSQL = (Boolean)((ModelBase)deletingObject).getMyChildMapSetup().get(childList_getter+"_useSQL");
            }
            if ( !(useSQL) ) { //using hibernate relation to get child
                Method m = deletingObject.getClass().getMethod(childList_getter);
                List<ModelBase> list = (List)m.invoke(deletingObject);
                for (ModelBase deletingChild : list) {
                    if (deletingChild.getModelService(deletingChild) != null) {
                        Boolean noSuchMethod = Boolean.FALSE;
                        try {
                            m = deletingChild.getModelService().getClass().getDeclaredMethod("preDelete", Session.class, Object.class);
                        } catch (Exception e) {
                            noSuchMethod = Boolean.TRUE;
                        }
                        if (!noSuchMethod) {
                            traceModelOperation("preDelete", deletingChild.getClass().getSimpleName()+"Service", TraceModelOperation_level);
                            deletingChild.getModelService().preDelete(session, deletingChild);
                        } else {
                            traceModelOperation("preDelete", "ModelBase", TraceModelOperation_level);
                        }
                        deleteModel(session, deletingChild);
                        noSuchMethod = Boolean.FALSE;
                        try {
                            m = deletingChild.getModelService().getClass().getDeclaredMethod("postDelete", Session.class, Object.class);
                        } catch (Exception e) {
                            noSuchMethod = Boolean.TRUE;
                        }
                        if (!noSuchMethod) {
                            traceModelOperation("postDelete", deletingChild.getClass().getSimpleName()+"Service", TraceModelOperation_level);
                            deletingChild.getModelService().postDelete(session, deletingChild);
                        } else {
                            traceModelOperation("postDelete", "ModelBase", TraceModelOperation_level);
                        }
                    } else {
                        deletingChild.preDelete(session, deletingChild);
                        deleteModel(session, deletingChild);
                    }
                }
            } else {//using sql to get child's id
                Class childClass = (Class)((ModelBase)deletingObject).getMyChildMap().get(childList_getter);
                Map map = new HashMap();
                map.put(PARENT_KEY, ((ModelBase)deletingObject).getID());
                String tableName = modelTableName(childClass);
                String fkName = getter_FK(deletingObject.getClass(), childList_getter);
                List<Map> childKeyList = checkingDAO.getListFromSql("select "+ model_PK(childClass) +" as "+CHILD_KEY + " from " + tableName + " where " + fkName + " = :"+PARENT_KEY, map);
                for (Map childMap : childKeyList) {
                    ModelBase childModel = (ModelBase)childClass.getConstructor().newInstance();
                    childModel.setID(childMap.get(CHILD_KEY)+"");
                    childModel.set_deleteOption("SQL");
                    if (childModel.getModelService() != null) {
                        Boolean noSuchMethod = Boolean.FALSE;
                        try {
                            Method m = childModel.getModelService().getClass().getDeclaredMethod("preDelete", Session.class, Object.class);
                        } catch (Exception e) {
                            noSuchMethod = Boolean.TRUE;
                        }
                        if (!noSuchMethod) {
                            traceModelOperation("preDelete", childModel.getModelService().getClass().getSimpleName(), TraceModelOperation_level);
                            childModel.getModelService().preDelete(session, childModel);
                        } else {
                            traceModelOperation("preDelete", "ModelBase", TraceModelOperation_level);
                        }
                        deleteModel(session, childModel);
                        noSuchMethod = Boolean.FALSE;
                        try {
                            Method m = childModel.getModelService().getClass().getDeclaredMethod("postDelete", Session.class, Object.class);
                        } catch (Exception e) {
                            noSuchMethod = Boolean.TRUE;
                        }
                        if (!noSuchMethod) {
                            traceModelOperation("postDelete", childModel.getModelService().getClass().getSimpleName(), TraceModelOperation_level);
                            childModel.getModelService().postDelete(session, childModel);
                        } else {
                            traceModelOperation("postDelete", "ModelBase", TraceModelOperation_level);
                        }
                    } else {
                        childModel.preDelete(session, childModel);
                        deleteModel(session, childModel);
                        childModel.postDelete(session, childModel);
                    }
                }
                if (!childKeyList.isEmpty()) {
                    if (!((ModelBase)deletingObject).createUpdate_useUserPK()) {
                        session.createNativeQuery("update " + tableName + " set "+ defaultUpdatedBy() +" = '"+getLoginId()+"', "+defaultUpdatedDate()+" = now() where "+fkName+" = '"+ ((ModelBase)deletingObject).getID() +"'").executeUpdate();
                        session.createNativeQuery("delete from " + tableName + " where "+fkName+" = '"+ ((ModelBase)deletingObject).getID() +"'").executeUpdate();
                    } else {
                        session.createNativeQuery("update " + tableName + " set "+ defaultUpdatedBy() +" = '"+getLoginUserId()+"', "+defaultUpdatedDate()+" = now() where "+fkName+" = '"+ ((ModelBase)deletingObject).getID() +"'").executeUpdate();
                        if (isIntegerPk(deletingObject.getClass())) {
                            session.createNativeQuery("delete from " + tableName + " where "+fkName+" = "+ ((ModelBase)deletingObject).getID()).executeUpdate();
                        } else {
                            session.createNativeQuery("delete from " + tableName + " where "+fkName+" = '"+ ((ModelBase)deletingObject).getID() +"'").executeUpdate();
                        }
                    }
                }
            }
        }
        reduceIdxVar();
        if ( !((ModelBase)deletingObject).get_deleteOption().equals("SQL") ) {
            System.out.println("1111111111111111111111111111111111111111");
            Debug.printFrameworkDebug("deletingObject.getClass = " + deletingObject.getClass());
            if (((ModelBase)deletingObject).createUpdate_useUserPK() ) {
                System.out.println("222222222222222222222222222222222222222222");
                checkingDAO.auditDeleteWithSession(session, deletingObject, getLoginUserId(), true);
            } else {
                System.out.println("333333333333333333333333333333333");
                checkingDAO.auditDeleteWithSession(session, deletingObject, getLoginId(), true);
            }
        }
    }

    public void postDelete(Session session, Object deletingObject) throws Exception {
        if (showNotOverrideMethod) Debug.printInfo("postDelete not override");
    }

    public void deleteList(Session session, List deletingList) throws Exception {
        if (deletingList != null && deletingList.size() > 0) {
            for (Object deletingObject : deletingList) {
                checkReferring(session, ((ModelBase)deletingObject));
                ((ModelBase)deletingObject).preDelete(session, deletingObject);
                ((ModelBase)deletingObject).deleteModel(session, deletingObject);
                ((ModelBase)deletingObject).postDelete(session, deletingObject);
            }
        }
    }
    public void checkReferring(Session session, ModelBase deletingObject) throws Exception {
        
    }

    public List<List> myItemsLists() throws Exception {
        return null;
    }

    public String get_operation() {
        String theOperation = (String) currentRequestObject("current_operation");
        if (theOperation == null) {
            return "";
        }
        return theOperation;
//        if (_operation == null) {
//            _operation = "";
//        }
//        return _operation;
    }

    public void set_operation(String _operation) {
        updateCurrentRequestObject("current_operation", _operation);
        this._operation = _operation;
    }

    public String get_hideShowMore() {
        return _hideShowMore;
    }

    public void set_hideShowMore(String _hideShowMore) {
        this._hideShowMore = _hideShowMore;
    }

    public String get_markedAsDel() {
        return _markedAsDel;
    }

    public void set_markedAsDel(String _markedAsDel) {
        this._markedAsDel = _markedAsDel;
    }

    public Boolean isDataModified_() {
        return isDataModified_;
    }

    public void setIsDataModified_(Boolean isDataModified_) {
        this.isDataModified_ = isDataModified_;
    }

    public Session getSession_() {
        return session_;
    }

    public void setSession_(Session session_) {
        this.session_ = session_;
    }

//   public void checkForReferenced(org.hibernate.Session session) throws Exception {
////        System.out.println("checkForReferenced() not override in model.");
//        new LogFunction().logInfo(this.getClass(), "checkForReferenced() not override in model.");
//    }

//    public void setCreated_by(String createdBy){
//        Debug.printFrameworkDebug("ModelBase.setCreatedBy(String) is called");
//    };
    public abstract void setCreated_date(java.sql.Timestamp createdDate);
//    public void setUpdated_by(String updatedBy){
//        Debug.printFrameworkDebug("ModelBase.setUpdatedBy(String) is called");
//    };
    public abstract void setUpdated_date(java.sql.Timestamp updatedDate);
//    public void setCreated_by(Integer createdBy){
//        Debug.printFrameworkDebug("ModelBase.setCreatedBy(Integer) is called");
//    };
//    public void setUpdated_by(Integer updatedBy){
//        Debug.printFrameworkDebug("ModelBase.setUpdatedBy(Integer) is called");
//    };

    public int get_primaryKeyLength() {
        return primaryKeyLength;
    }

    public void set_primaryKeyLength(int primaryKeyLength) {
        this.primaryKeyLength = primaryKeyLength;
    }

    private static Boolean _useNanoSecond = Boolean.FALSE;
    public static Boolean get_useNanoSecond() {
        return _useNanoSecond;
    }

    public static void set_useNanoSecond(Boolean _useNanoSecond) {
        ModelBase._useNanoSecond = _useNanoSecond;
    }
    
    // ThoTH @ 29-Oct-2015 :: For ACNL
    // Shall scan your own code, to replace "ActionContext.getContext().getSession().get("loginId")" to this 
    public String getLoginId() {
        String str = "";
        try {
            if (ActionContext.getContext().getSession().get("acting_login_id") != null) {
                str = (String) ActionContext.getContext().getSession().get("acting_login_id");
            } else {
                str = (String) ActionContext.getContext().getSession().get("loginId");
            }
        } catch (Exception e) {
            str = SystemConstants.BACKEND.DEFAULT_ID;
        }
        return str;
    }
    
    public Object getLoginUserId() {
        Object userID = null;
        try {
            userID = ActionContext.getContext().getSession().get("userId");
            if (userID == null) {
                userID = SystemConstants.BACKEND.DEFAULT_ID;
            }
            return userID;
        } catch (Exception e) {
        }
        return SystemConstants.BACKEND.DEFAULT_ID;
    }

    private Boolean doNotAssignID__ = Boolean.FALSE;
    public Boolean getDoNotAssignID__() {
        return doNotAssignID__;
    }
    public void setDoNotAssignID__(Boolean doNotAssignID__) {
        this.doNotAssignID__ = doNotAssignID__;
    }
    
    public void defaultAddProperties() {
        try {
            setIsNewModel(Boolean.TRUE);
            try {
                newlyAddedModel().add(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (GLOBAL_USE_USER_PK_FOR_CREATE_UPDATE_BY) {
                if(Validator.isEmpty(getLoginUserId().toString())) {
                    callSetter_method("created_by", "BACKEND");
                } else {
                    callSetter_method("created_by", getLoginUserId().toString());
                }
//                if (updateByIsInteger()) {
//                    setCreated_by(Integer.parseInt(getLoginUserId().toString()));
//                } else {
//                    setCreated_by(getLoginUserId().toString());
//                }
            } else {
                if (createUpdate_useUserPK()) {
                    if(Validator.isEmpty(getLoginUserId().toString())) {
                        callSetter_method("created_by", "BACKEND");
                    } else {
                        callSetter_method("created_by", getLoginUserId().toString());
                    }
//                    if (updateByIsInteger()) {
//                        setCreated_by(Integer.parseInt(getLoginUserId().toString()));
//                    } else {
//                        setCreated_by(getLoginUserId().toString());
//                    }
                } else {
                    if(Validator.isEmpty(getLoginId())) {
                        callSetter_method("created_by", "BACKEND");
                    } else {
                        callSetter_method("created_by", getLoginId());
                    }
                }
                
            }
//            try {
//                if (ActionContext.getContext().getSession().get("acting_login_id") != null) {
//                    setCreated_by((String) ActionContext.getContext().getSession().get("acting_login_id"));
//                } else {
//                    setCreated_by((String) ActionContext.getContext().getSession().get("loginId"));
//                }
//            } catch (Exception e) {
//                setCreated_by(SystemConstants.BACKEND.DEFAULT_ID);
//            }
            if (get_useNanoSecond()) {
                setCreated_date(DateUtil.getCurrentTimestamp_nano());
            } else {
                setCreated_date(DateUtil.getCurrentTimestamp());
            }
            if (!isAutoNumberPk(this.getClass())) {
                if (isIntegerPk(this.getClass())) {
                    setID(com.sains.framework.base.CommonFunction.getIntId_milisec_plus_4_runningDigit().toString());
                } else {
                    setID(com.sains.framework.base.CommonFunction.getId(this.primaryKeyLength));
                }
            }
            defaultUpdateProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void defaultAddProperties(String strCreatedBy) {
        try {
            callSetter_method("created_by", strCreatedBy);
//            setCreated_by(strCreatedBy);
//            setCreated_date(DateUtil.getCurrentTimestamp());
            if (get_useNanoSecond()) {
                setCreated_date(DateUtil.getCurrentTimestamp_nano());
            } else {
                setCreated_date(DateUtil.getCurrentTimestamp());
            }
            if (!isAutoNumberPk(this.getClass())) {
                if (isIntegerPk(this.getClass())) {
                    setID(com.sains.framework.base.CommonFunction.getIntId_milisec_plus_4_runningDigit().toString());
                } else {
                    setID(com.sains.framework.base.CommonFunction.getId(this.primaryKeyLength));
                }
            }
            defaultUpdateProperties(strCreatedBy);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void defaultAddPropertiesOnly(String myId) { //added by amywyp @ 20-11-2017 --to use primary key from EMS (mtg_id)
        try {
            setIsNewModel(Boolean.TRUE);
            callSetter_method("created_by", getLoginId());
//            setCreated_by(getLoginId());
            if (get_useNanoSecond()) {
                setCreated_date(DateUtil.getCurrentTimestamp_nano());
            } else {
                setCreated_date(DateUtil.getCurrentTimestamp());
            }
            setID(myId);
            defaultUpdateProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void defaultUpdateProperties() {
        try {
            if (GLOBAL_USE_USER_PK_FOR_CREATE_UPDATE_BY) {
                if (Validator.notEmpty(defaultUpdatedBy())) {
                    callSetter_method("updated_by", getLoginUserId().toString());
                }
//                if (updateByIsInteger()) {
//                    setUpdated_by(Integer.parseInt(getLoginUserId().toString()));
//                } else {
//                    setUpdated_by(getLoginUserId().toString());
//                }
            } else {
                if (createUpdate_useUserPK()) {
                    if (Validator.notEmpty(defaultUpdatedBy())) {
                        callSetter_method("updated_by", getLoginUserId().toString());
                    }
//                    if (updateByIsInteger()) {
//                        setUpdated_by(Integer.parseInt(getLoginUserId().toString()));
//                    } else {
//                        setUpdated_by(getLoginUserId().toString());
//                    }
                } else {
                    if (Validator.notEmpty(defaultUpdatedBy())) {
                        callSetter_method("updated_by", getLoginId());
                    }
//                    setUpdated_by(getLoginId());
                }
            }
            try {
                updatedModel().add(this);
            } catch (Exception e) {
//                e.printStackTrace();
            }
//            try {
//                if (ActionContext.getContext().getSession().get("acting_login_id") != null) {
//                    setUpdated_by((String) ActionContext.getContext().getSession().get("acting_login_id"));
//                } else {
//                    setUpdated_by((String) ActionContext.getContext().getSession().get("loginId"));
//                }
//            } catch (Exception e) {
//                setUpdated_by(SystemConstants.BACKEND.DEFAULT_ID);
//            }
            if (get_useNanoSecond()) {
                setUpdated_date(DateUtil.getCurrentTimestamp_nano());
            } else {
                setUpdated_date(DateUtil.getCurrentTimestamp());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void defaultUpdateProperties(String strUpdatedBy) throws Exception {
        if (Validator.notEmpty(defaultUpdatedBy())) {
            callSetter_method("updated_by", strUpdatedBy);
        }
//        setUpdated_by(strUpdatedBy);
        if (get_useNanoSecond()) {
            setUpdated_date(DateUtil.getCurrentTimestamp_nano());
        } else {
            //System.out.println("DateUtil.getCurrentTimestamp()"+DateUtil.getCurrentTimestamp());
            setUpdated_date(DateUtil.getCurrentTimestamp());
        }
    }
    
    public Boolean get_idIsEmpty() {
        return getID() == null || getID().trim().equals("");
    }
    
    private String imSelected;
    public String getImSelected() {
        return imSelected;
    }
    public void setImSelected(String imSelected) {
        this.imSelected = imSelected;
    }
    
    //FTP Setting   
    public void closeFTP() {
        try {            
            if (System.getProperty("os.name").startsWith("Windows")) {
                ftps.disconnect();
            } else {
//                new Sftp2Util().disconnectSftp();
                ftps.disconnect();
            }
        } catch (Exception e) {
        }
    }  
    
    public FtpInterface ftps = getFtps();

    public FtpInterface getFtps() {
        if (System.getProperty("os.name").startsWith("Windows")) {
//            ftps = new SFTPBean();
//            ftps = new FtpsUtil();            
            ftps = new OBSUtil();            
        }else{
//            ftps = new Sftp2Util();
//            ftps = new SFTPBean();
            ftps = new OBSUtil();
        }
        return ftps;
    }

    public void setFtps(FtpInterface ftps) {
        this.ftps = ftps;
    }
    
    public static String get_updatedBy(){
        if (ActionContext.getContext().getSession().get("acting_login_id") != null) {
            return (String) ActionContext.getContext().getSession().get("acting_login_id");
        } else {
            return (String) ActionContext.getContext().getSession().get("loginId");
        }
    }
    
    private Boolean isNewModel = Boolean.FALSE;
    public Boolean getIsNewModel() {
        return isNewModel;
    }
    public void setIsNewModel(Boolean isNewModel) {
        this.isNewModel = isNewModel;
    }
    
    private Boolean manualDuplicationCheck = Boolean.FALSE;
    public Boolean getManualDuplicationCheck() {
        return manualDuplicationCheck;
    }
    public void userDefined_manualDuplicationCheck(Boolean manualDuplicationCheck) {
        this.manualDuplicationCheck = manualDuplicationCheck;
    }
    
    public void manualDuplicationCheck(org.hibernate.Session session, Object dataEntryModel) throws Exception {
//        System.out.println("preUpdate() not override in model.");
        if (showNotOverrideMethod) new LogFunction().logInfo(this.getClass(), "manualDuplicationCheck() not override in model.");
    }
    
    public static Map threadVar = new HashMap();
    public static Object currentRequestObject(String objectId) {
        try {
            HttpServletRequest currentRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            return currentRequest.getAttribute(objectId);
        } catch (Exception e) {
            Thread currThread = Thread.currentThread();
            Map threadMap = (Map)threadVar.get(currThread.getId());
            if (threadMap == null) return null;
            return threadMap.get(objectId);
        }
    }
    public static void updateCurrentRequestObject(String objectId, Object value) {
        try {
            HttpServletRequest currentRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            currentRequest.setAttribute(objectId, value);
        } catch (Exception e) {
            Thread currThread = Thread.currentThread();
            Map threadMap = (Map)threadVar.get(currThread.getId());
            if (threadMap == null) {
                threadMap = new HashMap();
                threadVar.put(currThread.getId(), threadMap);
            }
            threadMap.put(objectId, value);
        }
    }
    
    public Set<String> currentMsgsSet() {
        HttpServletRequest currentRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Set<String> currentMsgList = null;
        if (currentRequest.getAttribute("updateMsgs") != null) {
            return (Set)currentRequest.getAttribute("updateMsgs");
        } else {
            currentMsgList = new LinkedHashSet();
            currentRequest.setAttribute("updateMsgs", currentMsgList);
        }
        return currentMsgList;
    }
    
    public List<ModelBase> newlyAddedModel() {
        List<ModelBase> newlyAddedModel = null;
        try {
            HttpServletRequest currentRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            if (currentRequest.getAttribute("newlyAddedModel") != null) {
                return (List)currentRequest.getAttribute("newlyAddedModel");
            } else {
                newlyAddedModel = new ArrayList();
                currentRequest.setAttribute("newlyAddedModel", newlyAddedModel);
            }
        } catch (Exception e) {
            Thread currThread = Thread.currentThread();
            Map threadMap = (Map)threadVar.get(currThread.getId());
            if (threadMap == null) {
                threadMap = new HashMap();
                newlyAddedModel = new ArrayList();
                threadMap.put("newlyAddedModel", newlyAddedModel);
                threadVar.put(currThread.getId(), threadMap);
            } else {
                newlyAddedModel = (List<ModelBase>)threadMap.get("newlyAddedModel");
                if (newlyAddedModel == null) {
                    newlyAddedModel = new ArrayList();
                    threadMap.put("newlyAddedModel", newlyAddedModel);
                }
            }
        }
        return newlyAddedModel;
    }
    
    public List<ModelBase> updatedModel() {
        
        HttpServletRequest currentRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        List<ModelBase> updatedModel = null;
        if (currentRequest.getAttribute("updatedModel") != null) {
            return (List)currentRequest.getAttribute("updatedModel");
        } else {
            updatedModel = new ArrayList();
            currentRequest.setAttribute("updatedModel", updatedModel);
        }
        return updatedModel;
    }
    
    private Integer _currentIndex = 0;
    public Integer get_currentIndex() {
        return _currentIndex;
    }
    public void set_currentIndex(Integer _currentIndex) {
        this._currentIndex = _currentIndex;
    }
    
    private Boolean _hasError = Boolean.FALSE;
    public Boolean get_hasError() {
        return _hasError;
    }
    public void _hasError() {
        this._hasError = Boolean.TRUE;
    }
    
    public void clearNewModelID() {
        for (ModelBase newModel : newlyAddedModel()) {
            newModel.setID("");
        }
    }
    
    private Boolean _ignoreUpdate = Boolean.FALSE;
    public Boolean get_ignoreUpdate() {
        return _ignoreUpdate;
    }
    public void set_ignoreUpdate(Boolean _ignoreUpdate) {
        this._ignoreUpdate = _ignoreUpdate;
    }
    
    public void ignoreUpdate(Session session) {
        session.evict(this);
        _ignoreUpdate = Boolean.TRUE;
    }
    
//    public class ChildObj {
//        private String childFK_name;
//        private Class childClass;
//        public ChildObj(String childFK_name, Class childClass) {
//            this.childFK_name = childFK_name;
//            this.childClass = childClass;
//        }
//        public Class getChildClass() {
//            return childClass;
//        }
//        public void setChildClass(Class childClass) {
//            this.childClass = childClass;
//        }
//
//        public String getChildFK_name() {
//            return childFK_name;
//        }
//        public void setChildFK_name(String childFK_name) {
//            this.childFK_name = childFK_name;
//        }
//        
//    }
    
    Map<String, String[]> arrSelect = null;
    public Map<String, String[]> getArrSelect() {
        return arrSelect;
    }
    public String[] getArrSelect(String selected) {
        if (arrSelect == null) return new String[]{};
        if (!arrSelect.containsKey(selected)) {
            return new String[]{};
        }
        return arrSelect.get(selected);
    }
    public void setArrSelect(Map arrSelect) {
        this.arrSelect = arrSelect;
    }
    
    Map<String, String> arrDelete = null;
    public Map<String, String> getArrDelete() {
        return arrDelete;
    }
    public void setArrDelete(Map arrDelete) {
        this.arrDelete = arrDelete;
    }
    public void add_arrDelete(String deletedItemName, String deletedId) {
        if (arrDelete == null) arrDelete = new HashMap();
        if (Validator.isEmpty(arrDelete.get(deletedItemName))) {
            arrDelete.put(deletedItemName, deletedId);
        } else {
            arrDelete.put(deletedItemName, arrDelete.get(deletedItemName)+","+deletedId);
        }
    }
    public String get_arrDelete(String deletedItemName) {
        if (arrDelete == null) return "";
        if (!arrDelete.containsKey(deletedItemName)) {
            return "";
        }
        return arrDelete.get(deletedItemName);
    }
//    public String[] getArrSelect() {
//        if (arrSelect == null) arrSelect = new HashMap();
//        if (arrSelect.get(selectedName) == null) {
//            arrSelect.put(selectedName, new String[]{});
//        }
//        return arrSelect.get(selectedName);
//    }
    public static Boolean isIntegerPk(Class cls) {
        for(Method method : cls.getDeclaredMethods()){
            Boolean found = Boolean.FALSE;
            Boolean found_integerReturn = Boolean.FALSE;
            Annotation[] annotations = method.getAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].annotationType().equals(Id.class)) {
                    found = Boolean.TRUE;
                }
                if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                    found_integerReturn = Boolean.TRUE;
                }
                if (found && found_integerReturn) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    
    public static Boolean isAutoNumberPk(Class cls) {
        for(Method method : cls.getDeclaredMethods()){
            Boolean found = Boolean.FALSE;
            Boolean found_generatedValue = Boolean.FALSE;
            Annotation[] annotations = method.getAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].annotationType().equals(Id.class)) {
                    found = Boolean.TRUE;
                }
                if (annotations[i].annotationType().equals(javax.persistence.GeneratedValue.class)) {
                    found_generatedValue = Boolean.TRUE;
                }
                if (found && found_generatedValue) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    
    public Boolean createUpdate_useUserPK() { //return false = use loginId/user_name, true = use user table's PK
        return Boolean.FALSE;
    }
    
    public String defaultUpdatedBy() {
        return "updated_by";
    }
    public String defaultUpdatedDate() {
        return "updated_date";
    }
    
    public Boolean updateByIsInteger() throws Exception {
        Method m = getClass().getMethod("getUpdated_by");
        if (m.getGenericReturnType().toString().equals("class java.lang.Integer")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    protected void callSetter_method(String method, String value) throws Exception {
        Method m = getClass().getMethod("get" + method.substring(0, 1).toUpperCase() + method.substring(1));
        if (m.getGenericReturnType().toString().equals("class java.lang.Integer")) {
            m = getClass().getMethod("set" + method.substring(0, 1).toUpperCase() + method.substring(1), Integer.class);
            m.invoke(this, new Integer(value));
        } else {
            m = getClass().getMethod("set" + method.substring(0, 1).toUpperCase() + method.substring(1), String.class);
            m.invoke(this, value);
        }
    }
    
    //** Uppy File Upload : Start **//
    private String _uppyUploadFile_drDocId = null;
    public String get_uppyUploadFile_drDocId() {
        return _uppyUploadFile_drDocId;
    }
    public void set_uppyUploadFile_drDocId(String _uppyUploadFile_drDocId) {
        this._uppyUploadFile_drDocId = _uppyUploadFile_drDocId;
    }
    
    private String _uppyUploadFile_drDocPath = null;
    public String get_uppyUploadFile_drDocPath() {
        return _uppyUploadFile_drDocPath;
    }
    public void set_uppyUploadFile_drDocPath(String _uppyUploadFile_drDocPath) {
        this._uppyUploadFile_drDocPath = _uppyUploadFile_drDocPath;
    }
    
    private Boolean appendRecordID = Boolean.TRUE;
    public void setAppendRecordID(Boolean appendId) {
        appendRecordID = appendId;
    }
    public Boolean appendRecordID() {
        return appendRecordID;
    }
    
    private Map<String, String> uppyUpload_fk_name_map = null;
    public final Map<String, String> getUppyUpload_appCodeSetup() {
        if (uppyUpload_fk_name_map == null) {
            uppyUpload_fk_name_map = new HashMap();
        }
        return uppyUpload_fk_name_map;
    }
    
    private Map<String, String> moveToActualPathMap = null;
    public Map<String, String> getMoveToActualPathMap(){
        if (moveToActualPathMap == null) {
            if (getUppyUpload_appCodeSetup().isEmpty()) {
                ModelBase modelService = getModelService();
                if (modelService!=null) {
                    uppyUpload_fk_name_map = modelService.getUppyUpload_appCodeSetup();
                }
            }
            if (uppyUpload_fk_name_map != null) {
                moveToActualPathMap = new HashMap();
                for (String appCode : uppyUpload_fk_name_map.keySet()) {
                    try {
                        String[] setup = uppyUpload_fk_name_map.get(appCode).split(";");
                        Method m = this.getClass().getMethod("get" + setup[0].substring(0, 1).toUpperCase() + setup[0].substring(1));
                        Object obj = m.invoke(this);
                        if (obj != null) {
                            moveToActualPathMap.put(setup[0], setup[1]);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
//        System.out.println("moveToActualPathMap = " + moveToActualPathMap);
//        System.out.println("uppyUpload_fk_name_map = " + uppyUpload_fk_name_map);
        return moveToActualPathMap;
    }
    
    private void moveToActualPath(Session session) throws Exception {
        
        if (getMoveToActualPathMap() != null) {

//            SFTPBean sftp = new SFTPBean();
            OBSUtil sftp = new OBSUtil();
            for (String key : moveToActualPathMap.keySet()) {
                Method m = this.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
                _uppyUploadFile_drDocId = (String) m.invoke(this);
                if (Validator.isEmpty(_uppyUploadFile_drDocId)) continue;
                DrDocRepoModel docRepo = (DrDocRepoModel) session.getNamedQuery("DocRepo.findBy_dr_doc_id").setParameter("dr_doc_id", _uppyUploadFile_drDocId).uniqueResult();

                System.out.println("docRepo.getDr_doc_application() = " + docRepo.getDr_doc_application());
                _uppyUploadFile_drDocPath = uppyUpload_fk_name_map.get(docRepo.getDr_doc_application()).split(";")[1];
//                _uppyUploadFile_drDocPath = moveToActualPathMap.get(key);
                System.out.println("_uppyUploadFile_drDocPath = " + _uppyUploadFile_drDocPath);
                if (appendRecordID) {
                    sftp.createDirIfNotExists(_uppyUploadFile_drDocPath, Boolean.FALSE);
                    sftp.createDirIfNotExists(_uppyUploadFile_drDocPath+"/"+getID(), Boolean.FALSE);
    //                System.out.println("222 " + _uppyUploadFile_drDocPath);
    //                System.out.println("333 " + _uppyUploadFile_drDocPath+"/"+getID()+"/"+_uppyUploadFile_drDocId);
//                    if (_uppyUploadFile_drDocId.contains(",")) {
//                        for (String idItem : _uppyUploadFile_drDocId.split(",")) {
//                            sftp.moveTempFile(_uppyUploadFile_drDocPath+"/"+getID()+"/"+idItem, idItem, session);
//                        }
//                    } else {
                    sftp.moveTempFile(_uppyUploadFile_drDocPath+"/"+getID()+"/"+_uppyUploadFile_drDocId, _uppyUploadFile_drDocId, session);
//                    }
                } else {
                    sftp.createDirIfNotExists(_uppyUploadFile_drDocPath, Boolean.FALSE);
    //                System.out.println("222 " + _uppyUploadFile_drDocPath);
    //                System.out.println("333 " + _uppyUploadFile_drDocPath+"/"+_uppyUploadFile_drDocId);
//                    if (_uppyUploadFile_drDocId.contains(",")) {
//                        for (String idItem : _uppyUploadFile_drDocId.split(",")) {
//                            sftp.moveTempFile(_uppyUploadFile_drDocPath+"/"+idItem, idItem, session);
//                        }
//                    } else {
                    sftp.moveTempFile(_uppyUploadFile_drDocPath+"/"+_uppyUploadFile_drDocId, _uppyUploadFile_drDocId, session);
//                    }
                }
            }
        }
    }
//    private void moveToActualPath(Session session) throws Exception {
//        if (_uppyUploadFile_drDocId != null) {
//            SFTPBean sftp = new SFTPBean();
//            sftp.createDirIfNotExists(_uppyUploadFile_drDocPath, Boolean.FALSE);
//            if (appendRecordID) {
//                sftp.createDirIfNotExists(_uppyUploadFile_drDocPath+"/"+getID(), Boolean.FALSE);
////                System.out.println("222 " + _uppyUploadFile_drDocPath);
////                System.out.println("333 " + _uppyUploadFile_drDocPath+"/"+getID()+"/"+_uppyUploadFile_drDocId);
//                if (_uppyUploadFile_drDocId.contains(",")) {
//                    for (String idItem : _uppyUploadFile_drDocId.split(",")) {
//                        sftp.moveTempFile(_uppyUploadFile_drDocPath+"/"+getID()+"/"+idItem, idItem, session);
//                    }
//                } else {
//                    sftp.moveTempFile(_uppyUploadFile_drDocPath+"/"+getID()+"/"+_uppyUploadFile_drDocId, _uppyUploadFile_drDocId, session);
//                }
//            } else {
//                sftp.createDirIfNotExists(_uppyUploadFile_drDocPath, Boolean.FALSE);
////                System.out.println("222 " + _uppyUploadFile_drDocPath);
////                System.out.println("333 " + _uppyUploadFile_drDocPath+"/"+_uppyUploadFile_drDocId);
//                if (_uppyUploadFile_drDocId.contains(",")) {
//                    for (String idItem : _uppyUploadFile_drDocId.split(",")) {
//                        sftp.moveTempFile(_uppyUploadFile_drDocPath+"/"+idItem, idItem, session);
//                    }
//                } else {
//                    sftp.moveTempFile(_uppyUploadFile_drDocPath+"/"+_uppyUploadFile_drDocId, _uppyUploadFile_drDocId, session);
//                }
//            }
//        }
//    }
    public void sqlUpdateModel_preparedStmt(String[] updatableColumns, BaseDAOImpl dao, Boolean beginCommit) {
        try {
            if (beginCommit) dao.beginBatchTransaction();
            String modelPK = model_PK(this.getClass());
//            String modelPK = "audit_id";
            String currentOperation = get_operation();
//            if (currentRequestObject(this.getClass()+"__operation") != null) {
//                currentOperation = (String)currentRequestObject(this.getClass()+"__operation");
//            }
            PreparedStatement pStmt = (PreparedStatement) currentRequestObject(this.getClass().getSimpleName()+"__prepareStmt__"+currentOperation);
            if (pStmt == null) {
                CommonFunction.getMethodObjectFromObject(this, modelPK);
//                NativeQuery query = dao.getSession().createNativeQuery("update "+modelTableName(this.getClass())+" " + populateSetColumns(updatableColumns) + " where "+modelPK+" = :keyParam ");
                pStmt = dao.getpStmt("update "+modelTableName(this.getClass())+" " + populateSetColumns_ps(updatableColumns) + " where "+modelPK+" = ? ");
                updateCurrentRequestObject(this.getClass().getSimpleName()+"__prepareStmt__"+currentOperation, pStmt);
                List list = (List)currentRequestObject("__prepareStmt__list");
                if (list == null) {
                    list = new ArrayList();
                }
                list.add(pStmt);
                updateCurrentRequestObject("__prepareStmt__list", list);
            }
//            NativeQuery query = dao.getSession().createSQLQuery("update "+modelTableName(this.getClass())+" " + populateSetColumns(updatableColumns) + " where "+modelPK+" = ? ");
//            System.out.println("query.string = " + query.getQueryString());
//            System.out.println("modelPK = " + modelPK);
//            System.out.println("CommonFunction.getMethodObjectFromObject(this, modelPK) = " + CommonFunction.getMethodObjectFromObject(this, modelPK));

            setParamValue_ps(updatableColumns, pStmt, CommonFunction.getMethodObjectFromObject(this, modelPK), this);
            if (beginCommit) {
                pStmt.executeUpdate();
                dao.commitBatchTransaction();
            } else {
                Integer batchFlushCount = (Integer) currentRequestObject(this.getClass().getSimpleName()+"__prepareStmt__flushCount");
                if (batchFlushCount == null) {
                    batchFlushCount = 0;
                }
                if (batchFlushCount++ >= 1000) {
                    batchFlushCount = 0;
                    updateCurrentRequestObject(this.getClass().getSimpleName()+"__prepareStmt__flushCount", batchFlushCount);
                    dao.getSession().flush();
                }
                pStmt.addBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (beginCommit) dao.rollbackBatchTransaction();
        } finally {
            if (beginCommit) dao.closeSession();
        }
    }
    //** Uppy File Upload : End **//
    public void sqlUpdateModel(String[] updatableColumns, BaseDAO dao, Boolean beginCommit) {
        try {
            if (beginCommit) dao.beginBatchTransaction();
            String modelPK = model_PK(this.getClass());
//            String modelPK = "audit_id";

            CommonFunction.getMethodObjectFromObject(this, modelPK);
            NativeQuery query = dao.getSession().createNativeQuery("update "+modelTableName(this.getClass())+" " + populateSetColumns(updatableColumns) + " where "+modelPK+" = :keyParam ");
//            NativeQuery query = dao.getSession().createSQLQuery("update "+modelTableName(this.getClass())+" " + populateSetColumns(updatableColumns) + " where "+modelPK+" = ? ");
//            System.out.println("query.string = " + query.getQueryString());
//            System.out.println("modelPK = " + modelPK);
//            System.out.println("CommonFunction.getMethodObjectFromObject(this, modelPK) = " + CommonFunction.getMethodObjectFromObject(this, modelPK));
            setParamValue(updatableColumns, query, CommonFunction.getMethodObjectFromObject(this, modelPK), this);
            query.executeUpdate();
            if (beginCommit) dao.commitBatchTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            if (beginCommit) dao.rollbackBatchTransaction();
        } finally {
            if (beginCommit) dao.closeSession();
        }
    }
    public String populateSetColumns(String[] columnsArr) {
        StringBuilder condition = new StringBuilder();
        for (String col : columnsArr) {
            condition.append(", ").append(col).append(" = :").append(col);
//            condition.append(", ").append(col).append(" = ?");
        }
        if (Validator.notEmpty(defaultUpdatedBy())) {
            condition.append(", ").append(defaultUpdatedBy()).append(" = :updatedBy, ").append(defaultUpdatedDate()).append(" = :updatedDate");
        }
        return "set " + condition.substring(1);
    }
    public void setParamValue(String[] columnsArr, NativeQuery query, Object ef_id, Object updateModel) throws Exception {
        int idx = 1;
        for (String col : columnsArr) {
            Object obj = CommonFunction.getMethodObjectFromObject(updateModel, col);
            query.setParameter(col, CommonFunction.getMethodObjectFromObject(updateModel, col));
//            query.setParameter(idx++, CommonFunction.getMethodObjectFromObject(updateModel, col));
        }
        query.setParameter("keyParam", ef_id);
//        query.setParameter(idx, ef_id);
        if (Validator.notEmpty(defaultUpdatedBy())) {
            query.setParameter("updatedBy", CommonFunction.getMethodObjectFromObject(updateModel, defaultUpdatedBy()));
            query.setParameter("updatedDate", CommonFunction.getMethodObjectFromObject(updateModel, defaultUpdatedDate()));
        }
    }
    
    public String populateSetColumns_ps(String[] columnsArr) {
        StringBuilder condition = new StringBuilder();
        for (String col : columnsArr) {
            condition.append(", ").append(col).append(" = ?");
//            condition.append(", ").append(col).append(" = ?");
        }
        if (Validator.notEmpty(defaultUpdatedBy())) {
            condition.append(", ").append(defaultUpdatedBy()).append(" = ?, ").append(defaultUpdatedDate()).append(" = ?");
        }
        return "set " + condition.substring(1);
    }
    public void setParamValue_ps(String[] columnsArr, PreparedStatement pStatement, Object pkName, Object updateModel) throws Exception {
        int idx = 1;
        for (String col : columnsArr) {
            Object obj = CommonFunction.getMethodObjectFromObject(updateModel, col);
            Method method = this.getClass().getMethod("get" + col.substring(0, 1).toUpperCase() + col.substring(1));
            if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                pStatement.setInt(idx++, (Integer)obj);
            } else if (method.getGenericReturnType().toString().equals("class java.lang.String")) {
                pStatement.setString(idx++, (String)obj);
            } else if (method.getGenericReturnType().toString().equals("class java.lang.Double")) {
                pStatement.setDouble(idx++, (Double)obj);
            } else if (method.getGenericReturnType().toString().equals("class java.lang.Character")) {
                pStatement.setString(idx++, ((Character)obj).toString() );
            } else if (method.getGenericReturnType().toString().equals("class java.sql.Timestamp")) {
                pStatement.setTimestamp(idx++, (java.sql.Timestamp)obj);
            } else if (method.getGenericReturnType().toString().equals("class java.sql.Date")) {
                pStatement.setDate(idx++, (java.sql.Date)obj);
            }
//            query.setParameter(idx++, CommonFunction.getMethodObjectFromObject(updateModel, col));
        }
//        query.setParameter(idx, ef_id);
        if (Validator.notEmpty(defaultUpdatedBy())) {
            Method method = this.getClass().getMethod("get" + defaultUpdatedBy().substring(0, 1).toUpperCase() + defaultUpdatedBy().substring(1));
            if (method.getGenericReturnType().toString().equals("class java.lang.Integer")) {
                pStatement.setInt(idx++, (Integer)CommonFunction.getMethodObjectFromObject(updateModel, defaultUpdatedBy()));
            } else {
                pStatement.setString(idx++, (String)CommonFunction.getMethodObjectFromObject(updateModel, defaultUpdatedBy()));
            }
            pStatement.setTimestamp(idx++, (java.sql.Timestamp) CommonFunction.getMethodObjectFromObject(updateModel, defaultUpdatedDate()));
        }
        if (pkName instanceof Integer) {
            pStatement.setInt(idx, (Integer)pkName);
        } else {
            pStatement.setString(idx, (String)pkName);
        }
    }
    
    private Integer baseSortingField = null;
    public Integer getBaseSortingField() {
        return baseSortingField;
    }
    public void setBaseSortingField(Integer baseSortingField) {
        this.baseSortingField = baseSortingField;
    }
    
    private Boolean useSQLUpdate = Boolean.FALSE;
    public Boolean isUseSQLUpdate() {
        return useSQLUpdate;
    }
    public void useSQLUpdate(Boolean useSQLUpdate) {
        this.useSQLUpdate = useSQLUpdate;
    }
    
    public abstract String[] getUpdatableColumns();
    public Map<String, String[]> updatableByOperationMap = null;
    public String[] getUpdatableColumnsByOperation(String operation) {
        if (updatableByOperationMap != null && updatableByOperationMap.containsKey(operation)) {
            return updatableByOperationMap.get(operation);
        }
        return getUpdatableColumns();
    }
    public String[] getUpdatableColumnsByOperation() {
        if (currentRequestObject("current_operation") != null) {
            if (updatableByOperationMap != null && updatableByOperationMap.containsKey((String)currentRequestObject("current_operation"))) {
                return updatableByOperationMap.get((String)currentRequestObject("current_operation"));
            }
        } 
        return getUpdatableColumnsByOperation(get_operation());
    }
    
    ModelBase modelService = null;
    Boolean modelService_notFound = Boolean.FALSE;
    public ModelBase getModelService() {
        if (modelService_notFound) return null;
        if (modelService == null) {
            try {
                Class c = Class.forName(this.getClass().getPackage().getName()+"."+this.getClass().getSimpleName()+"Service");
                modelService = (ModelBase) c.getDeclaredConstructor(ModelBase.class, ModelBase.class).newInstance(this, null);
                
                this.clearMyChildMap();
                modelService.getMyChildList_byOperation();
                this.getMyChildMap().putAll(modelService.getMyChildMap());
                this.getMyChildMapSetup().putAll(modelService.getMyChildMapSetup());
            } catch (Exception e) {
                modelService_notFound = Boolean.TRUE;
            }
        } else {
            return modelService;
        }
        return modelService;
    }
    public ModelBase getModelService(ModelBase updatingModel) {
        if (modelService_notFound) return null;
        if (modelService == null) {
            try {
                Class c = Class.forName(this.getClass().getPackage().getName()+"."+this.getClass().getSimpleName()+"Service");
                modelService = (ModelBase) c.getDeclaredConstructor(ModelBase.class, ModelBase.class).newInstance(this, updatingModel);
                
                this.clearMyChildMap();
                modelService.getMyChildList_byOperation();
                this.getMyChildMap().putAll(modelService.getMyChildMap());
                this.getMyChildMapSetup().putAll(modelService.getMyChildMapSetup());
            } catch (Exception e) {
                modelService_notFound = Boolean.TRUE;
            }
        } else {
            return modelService;
        }
        return modelService;
    }
    
//    private String operationDesc(String operation) {
//        if (operation.equals("preInsert")) {
//            
//        } else if (operation.equals("postInsert")) {
//            
//        }
//    }
//    public List<String> addedOperationList = null;
//    public Map<String, Integer> addedOperation = null;
    private Map<String, Integer> addedOperationVar() {
        if (currentRequestObject("addedOperation") == null) {
            updateCurrentRequestObject("addedOperation", new HashMap());
            updateCurrentRequestObject("addedOperationList", new ArrayList());
        }
        return (Map)currentRequestObject("addedOperation");
    }
    private List<String> addedOperationListVar() {
        return (List)currentRequestObject("addedOperationList");
    }
    public void traceModelOperation(String operation, String model, Integer addMinus_level) {
//        System.out.println("in traceModelOperation.....");
        if (TraceModelOperation) {
//            if (addedOperation == null) {
//                addedOperation = new HashMap();
//                addedOperationList = new ArrayList();
//            }
            TraceModelOperation_level += addMinus_level;
            String key = spacing()+ operation + " " + model + " " + whoAmI();
            key = currentIdxStr() + key;
            if (addedOperationVar().containsKey(key)) {
                addedOperationVar().put(key, addedOperationVar().get(key)+1);
            } else {
                addedOperationVar().put(key, 0);
                addedOperationListVar().add(key);
            }
        }
    }
    public void traceModelOperation_1stLvl(String operation, String model) {
        System.out.println("in traceModelOperation_1stLevel.....");
        if (TraceModelOperation) {
//            if (addedOperation == null) {
//                addedOperation = new HashMap();
//                addedOperationList = new ArrayList();
//            }
            String key = spacing()+ operation + model + " " + whoAmI();
            key = currentIdxStr() + key;
            if (addedOperationVar().containsKey(key)) {
                addedOperationVar().put(key, addedOperationVar().get(key)+1);
            } else {
                addedOperationVar().put(key, 0);
                addedOperationListVar().add(key);
            }
        }
    }
    
    public void addTraceError(Exception e) {
        String key = "** ERROR ** " + e.getMessage();
        addedOperationListVar().add(key);
    }
    
    private static String spacing() {
        String temp = "";
        for (int count = 0; count < TraceModelOperation_level; count++) {
            temp += "  ";
        }
//        return temp + (TraceModelOperation_level+1) + ". ";
        return temp;
    }
    
    public void printOperationList() {
        if (TraceModelOperation && addedOperationListVar()!=null) {
//            System.out.println("---------------------- Framework trigger flow ----------------------");
//            System.out.println("====================== ====================== ======================");
//            System.out.println("addedOperationList.size = " + addedOperationListVar().size());
            for (String op : addedOperationListVar()) {
//                System.out.println(op);
            }
//            System.out.println("====================== ====================== ======================");
        }
        addedOperationListVar().clear();
        addedOperationVar().clear();
        indexVar().clear();
        TraceModelOperation_level = -1;
    }
    
    public Object[] getAllColumns() {
        Class modelClass = this.getClass();
        List<String> colList = new ArrayList();
        for(Method method : modelClass.getDeclaredMethods()){
//                String name = method.getName();
            if (!method.getName().startsWith("get")) continue;
            Annotation[] annotations = method.getAnnotations();
            Boolean found = Boolean.FALSE;
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].annotationType().equals(Id.class)) {
                    found = Boolean.TRUE;
                }
                if (annotations[i].annotationType().equals(Column.class)) {
                    if (found) {
                        colList.add(0, ((Column)annotations[i]).name());
                        found = Boolean.FALSE;
                    } else {
                        colList.add(((Column)annotations[i]).name());
                    }
                }
            }
        }
        return colList.toArray();
    }
    
    //if byUpdatableColumn == TRUE, then it will generate using those columns, else all columns will be used.
    
    public void genAndKeepReverseSql(Boolean byUpdatableColumn, ReversalOpt reversalOpt, Session session) throws Exception{
//        System.out.println("in genAndKeepReverseSql");
        if (currentRequestObject("genKeepReversalSQL") == null || !(Boolean)currentRequestObject("genKeepReversalSQL")) return;
        System.out.println("in genAndKeepReverseSql : start");
        Object[] updatableCols = null;
        if (!reversalOpt.getOpt().equals(ReversalOpt.INSERT)) {
            if (byUpdatableColumn) {
                updatableCols = getUpdatableColumnsByOperation();
            } else {
                updatableCols = getAllColumns();
            }
        }
        Map sqlMap = new HashMap();
        if (reversalOpt.equals(ReversalOpt.INSERT)) {
            sqlMap.put("sql", "delete from " + modelTableName(this.getClass()) + " where "+ model_PK(this.getClass()) +" = ? ");
            List paramList = new ArrayList();
            paramList.add(getID());
            sqlMap.put("params", paramList);
        } else {
            StringBuilder strBuilder = new StringBuilder();
            StringBuilder strBuilder2 = new StringBuilder();
            String PK = model_PK(this.getClass());
            Method m = null;
            List paramList = new ArrayList();
            if (reversalOpt.equals(ReversalOpt.DELETE)) {
                ModelBase delModel = (ModelBase)session.get(this.getClass(), this.getID());
                if (delModel == null) return;
//                this.genAndKeepReverseSql(Boolean.FALSE, ModelBase.ReversalOpt.DELETE);
                strBuilder.append("insert into ").append(modelTableName(this.getClass())).append(" (");
                strBuilder2.append(" values (");
                for (Object colName : updatableCols) {
                    m = delModel.getClass().getMethod("get" + colName.toString().substring(0, 1).toUpperCase() + colName.toString().substring(1));
                    Object obj = m.invoke(delModel);
                    if (obj != null) {
                        strBuilder.append(colName).append(",");
                        strBuilder2.append("?,");
                        paramList.add(obj);
                    }
                }
                strBuilder.delete(strBuilder.length()-1, strBuilder.length());
                strBuilder2.delete(strBuilder2.length()-1, strBuilder2.length());
                strBuilder.append(")").append(strBuilder2).append(")");
            } else {
                strBuilder.append("update ").append(modelTableName(this.getClass())).append(" set ");
                for (Object colName : updatableCols) {
                    if (PK.equalsIgnoreCase(colName.toString())) continue;
                    m = this.getClass().getMethod("get" + colName.toString().substring(0, 1).toUpperCase() + colName.toString().substring(1));
                    Object obj = m.invoke(this);
                    if (obj != null) {
                        strBuilder.append(colName).append(" = ?, ");
                        paramList.add(obj);
                    } else {
                        strBuilder.append(colName).append(" = null, ");
                    }
                }
                strBuilder.delete(strBuilder.length()-2, strBuilder.length());
                strBuilder.append(" where ").append(PK).append(" = ?");
                paramList.add(getID());
            }
            sqlMap.put("sql", strBuilder.toString());
            sqlMap.put("params", paramList);
        }
        getReversalList().add(sqlMap);
    }
    protected List<Map> getReversalList() {
        List reversal_list = (List)currentRequestObject("reversal_SQL");
        if (reversal_list == null) {
            reversal_list = new ArrayList();
            updateCurrentRequestObject("reversal_SQL", reversal_list);
        }
        return reversal_list;
    }
    
    public String whoAmI() {
        return "PK: " + getID();
    }
    
}
