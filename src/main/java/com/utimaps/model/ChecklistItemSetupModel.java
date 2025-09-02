/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utimaps.model;

import com.sains.common.util.SeqModel;
import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.ModelBase;
import com.sains.framework.base.ServiceFactory;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Session;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;

@Entity
@FilterDefs({
    @FilterDef(name="caseFilter", 
        parameters=@ParamDef( name="caseNo", type="string")
    ),
    @FilterDef(name="statusFilter")
})

@Table(name = "T_SETUP_CHECKLIST_ITEM")
public class ChecklistItemSetupModel extends ModelBase implements java.io.Serializable {
    
    private String ci_id;
    private String checklist_id;
    private String ci_desc;
    private String ci_notes;
    private String ci_status;
    private String ci_datatype;	
    private Integer ci_sequence;
    private String created_by;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    public String[] updatableColumns = new String[] {"Ci_id", "Checklist_id", "Ci_desc", "Ci_status", "ci_sequence", "Ci_datatype", "Ci_notes"};
    private final String[] columnLength = new String[] {"ci_desc:600"};
    private Map columnLengthMap = null;

    @Id
    @Column(name = "ci_id")
    public String getCi_id() {
        return ci_id;
    }

    public void setCi_id(String ci_id) {
        this.ci_id = ci_id;
    }
    
    @Transient
    public String getID() { 
        return ci_id; 
    } 

    public void setID(String ci_id) {
        this.ci_id = ci_id;
    }

    @Column(name = "checklist_id")
    public String getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
    }

    @Column(name = "ci_desc")
    public String getCi_desc() {
        return ci_desc;
    }

    public void setCi_desc(String ci_desc) {
        this.ci_desc = ci_desc;
    }

    @Column(name = "ci_notes")
    public String getCi_notes() {
        return ci_notes;
    }

    public void setCi_notes(String ci_notes) {
        this.ci_notes = ci_notes;
    }

    @Column(name = "ci_status")
    public String getCi_status() {
        return ci_status;
    }

    public void setCi_status(String ci_status) {
        this.ci_status = ci_status;
    }

    @Column(name = "ci_datatype")
    public String getCi_datatype() {
        return ci_datatype;
    }

    public void setCi_datatype(String ci_datatype) {
        this.ci_datatype = ci_datatype;
    }

    @Column(name = "ci_sequence")
    public Integer getCi_sequence() {
        return ci_sequence;
    }

    public void setCi_sequence(Integer ci_sequence) {
        this.ci_sequence = ci_sequence;
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
    
    private List<ChecklistItemModel> checklistResultList = new ArrayList();
    @OneToMany(targetEntity = ChecklistItemModel.class, fetch = FetchType.LAZY, mappedBy = "ci_id")
    @Filter(name="caseFilter", condition="case_id = :caseNo")
    @OrderBy("created_date DESC")
    public List<ChecklistItemModel> getChecklistResultList() {
        return checklistResultList;
    }

    public void setChecklistResultList(List<ChecklistItemModel> checklistResultList) {
        this.checklistResultList = checklistResultList;
    }
    
    private List<FileModel> checklistFileModelList = new ArrayList();
    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "ci_id", name = "ci_id", insertable = false, updatable = false, nullable = true)
    @Filter(name="caseFilter", condition="case_id = :caseNo")
    @Filter(name="typeFilter", condition="file_type <> :fileType")
    @Filter(name="statusFilter", condition="file_status <> 'D'")
    @OrderBy("created_date DESC")
    public List<FileModel> getChecklistFileModelList() {
        return checklistFileModelList;
    }

    public void setChecklistFileModelList(List<FileModel> checklistFileModelList) {
        this.checklistFileModelList = checklistFileModelList;
    }
    
    private List<FileModel> checklistFileModelList2 = new ArrayList();
    @OneToMany(targetEntity = FileModel.class, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "ci_datatype", name = "file_type", insertable = false, updatable = false, nullable = true)
    @Filter(name="caseFilter", condition="case_id = :caseNo")
    @Filter(name="typeFilter", condition="file_type <> :fileType")
    @Filter(name="statusFilter", condition="file_status <> 'D'")
    @OrderBy("created_date DESC")
    public List<FileModel> getChecklistFileModelList2() {
        return checklistFileModelList2;
    }

    public void setChecklistFileModelList2(List<FileModel> checklistFileModelList2) {
        this.checklistFileModelList2 = checklistFileModelList2;
    }
    
//    private ChecklistItemModel checklistItemModel;
//    @OneToOne(targetEntity = ChecklistItemModel.class, optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(referencedColumnName = "CI_ID", name = "CI_ID", insertable = false, updatable = false, nullable = true)
//    @Filter(name="caseFilter", condition="case_id = '1712545348354fMUx7W0'")
//    public ChecklistItemModel getChecklistItemModel() {
//        System.out.println("checklistItemModel " + checklistItemModel.getCl_id());
//        System.out.println("checklistItemModel " + checklistItemModel.getCase_id());
//        return checklistItemModel;
//    }
//    
//    public void setChecklistItemModel(ChecklistItemModel checklistItemModel) {
//        this.checklistItemModel = checklistItemModel;
//    }
//    
//    private ChecklistItemModel checklistItemModel;
//    public ChecklistItemModel getChecklistItemModel(String case_id, Session externalSession) {
//        BaseDAO<ChecklistItemModel> seqDAO = new BaseDAOImpl();
//        seqDAO.setSession(externalSession);
//        ChecklistItemModel cim = (ChecklistItemModel) seqDAO.getModelByCode("case_id,ci_id", case_id+","+ci_id, new ChecklistItemModel());
//        System.out.println("ci_id " + ci_id);
//        System.out.println("cim " + cim);
////        System.out.println("cim " + cim.getCl_id());
//        checklistItemModel = cim;
//        return checklistItemModel;
//    }
//    
//    public void setChecklistItemModel(ChecklistItemModel checklistItemModel) {
//        this.checklistItemModel = checklistItemModel;
//    }
    
    
}
