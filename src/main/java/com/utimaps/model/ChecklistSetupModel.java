/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "T_SETUP_CHECKLIST")
public class ChecklistSetupModel extends ModelBase implements java.io.Serializable{
    private String checklist_id;
    private String checklist_name;
    private String checklist_status;
    private String process_type;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    private Map columnLengthMap = null;
    public String[] updatableColumns = new String[]{"checklist_id","checklist_name","checklist_status","process_type"};
    
    public String[] columnLength = new String[]{};

    public static final class OPERATION {
        public static final String PROCESS_DELETE_LIST = "processDeleteList";
        public static final String PROCESS_UPDATE = "processUpdate";
        public static final String UPDATE_STATUS = "processUpdateStatus";
        public static final String SAVE_CHECKLIST = "saveChecklist";
        public static final String UPLOAD_SUPPORTING_FILE = "uploadSupportingFile";
        public static final String UPDATE_UPLOAD_SUPPORTING_FILE = "updateUploadSupportingFile";
    }
    
    @Id
    @Column(name = "checklist_id")
    public String getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
    }
    
    @Transient
    public String getID() { 
            return checklist_id; 
    } 

    public void setID(String pk_id) {
            this.checklist_id = pk_id;
    }

    @Column(name = "checklist_name")
    public String getChecklist_name() {
        return checklist_name;
    }

    public void setChecklist_name(String checklist_name) {
        this.checklist_name = checklist_name;
    }

    @Column(name = "checklist_status")
    public String getChecklist_status() {
        return checklist_status;
    }

    public void setChecklist_status(String checklist_status) {
        this.checklist_status = checklist_status;
    }

    @Column(name = "process_type")
    public String getProcess_type() {
        return process_type;
    }

    public void setProcess_type(String process_type) {
        this.process_type = process_type;
    }
    
    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "created_date")
    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }
    
    @Transient
    public String getCreated_date_only_str() {
        return Formatter.formatDate(created_date, SystemConstants.DATE.dataEntryFormat3);
    }

    public void setCreated_date_only_str(String created_date) {
        try {
            this.created_date = DateUtil.getTimestampFromDate(DateUtil.getDate(created_date, SystemConstants.DATE.dataEntryFormat));
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistSetupModel", "ChecklistSetupModel", "setCreated_date_only_str");
        }
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    @Column(name = "updated_date")
    public Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Timestamp updated_date) {
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
    
    private List<ChecklistItemSetupModel> checklistItemList = new ArrayList();
    @OneToMany(targetEntity = ChecklistItemSetupModel.class, fetch = FetchType.LAZY, mappedBy = "checklist_id")
    @Filter(name = "statusFilter", condition = "ci_status = 'Y' ")
    @OrderBy("ci_sequence ASC")
    public List<ChecklistItemSetupModel> getChecklistItemList() {
        return checklistItemList;
    }

    public void setChecklistItemList(List<ChecklistItemSetupModel> checklistItemList) {
        this.checklistItemList = checklistItemList;
    }
    
    @Override
    public void preInsert(org.hibernate.Session session) throws Exception {
        checkingDAO.setSession(session);
        System.out.println("Inside preInsert");
        
        if(this.getChecklistItemList() != null ) {
            System.out.println("Checklist Detail: " + this.getChecklistItemList().size());
            for(ChecklistItemSetupModel checklistItem : this.getChecklistItemList()) {
                if(Validator.isEmpty(checklistItem.getCi_id())) {
                    checklistItem.defaultAddProperties();
                    checklistItem.setChecklist_id(this.getChecklist_id());
                    checklistItem.setCi_status(checklistItem.getCi_status());
                    session.save(checklistItem);
                }
            }
        }
        
    }
    
    @Override
    public void preUpdate(org.hibernate.Session session, Object dataEntryModel) throws Exception {
        System.out.println("Inside preUpdate");
        setSession_(session);
        checkingDAO.setSession(session);
        ChecklistSetupModel updatingModel = (ChecklistSetupModel) dataEntryModel;

        if(updatingModel.get_operation().equals(ChecklistSetupModel.OPERATION.PROCESS_UPDATE)) {
            for(ChecklistItemSetupModel checklistItem : updatingModel.getChecklistItemList()) {
                if(Validator.isEmpty(checklistItem.getCi_id())) {
                    checklistItem.defaultAddProperties();
                    checklistItem.setChecklist_id(updatingModel.getChecklist_id());
                    checklistItem.setCi_status(checklistItem.getCi_status());
                    session.save(checklistItem);
                } else {
                    ChecklistItemSetupModel checklistItemsModel = (ChecklistItemSetupModel)checkingDAO.getModelById(checklistItem.getCi_id(), ChecklistItemSetupModel.class);
                    checklistItemsModel.defaultUpdateProperties();
                    checklistItemsModel.setChecklist_id(updatingModel.getChecklist_id());
                    checklistItemsModel.setCi_status(checklistItem.getCi_status());
                    checklistItemsModel.setCi_sequence(checklistItem.getCi_sequence());
                    checklistItemsModel.setCi_desc(checklistItem.getCi_desc());
                    checklistItemsModel.setCi_datatype(checklistItem.getCi_datatype());
                    checklistItemsModel.setCi_notes(checklistItem.getCi_notes());
                    session.update(checklistItemsModel);
                }
            }
        } 
    }
    
    @Override
    public void manualOperation(org.hibernate.Session session) throws Exception {
        BaseDAOImpl dao = new BaseDAOImpl();
        dao.setSession(session);
        try {
            System.out.println("operation " + this.get_operation());
            if (this.get_operation().equals(OPERATION.PROCESS_DELETE_LIST)) {                
                ChecklistItemSetupModel checklistDetail = null;
                String[] strArr = get_deletedItem().split(",");
                for (String id: strArr) {
                    System.out.println("ID: " + id);
                    checklistDetail = (ChecklistItemSetupModel) dao.getObjectById(id, ChecklistItemSetupModel.class);
                    session.delete(checklistDetail);
                }
            }
        } catch (Exception e) {
            CommonFunction.writeLogFile(e.getStackTrace(), "ChecklistSetupModel", "ChecklistSetupModel", "manualOperation");
        }
    }
}
