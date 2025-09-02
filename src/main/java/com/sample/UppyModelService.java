package com.sample;


import com.sains.common.util.SFTPBean;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.ModelBase;
import org.hibernate.Session;

public class UppyModelService extends UppyModel{
    UppyModel dbModel = null;
    UppyModel webModel = null;
    public UppyModelService (ModelBase dbModel, ModelBase webModel) {
        this.dbModel = (UppyModel)dbModel;
        this.webModel = (UppyModel)webModel;
        getUppyUpload_appCodeSetup().put("directModel_file", "dr_doc_id;uppyDirectModel_actualFolder");
        getUppyUpload_appCodeSetup().put("uppySample2_file1", "dr_doc_id_2;uppySample2_actualFolder");
    }

//    @Override
//    public void postInsert(Session session) throws Exception {
//        if (!Validator.isEmpty(dbModel.getUppy_parent_id())) {
//            dbModel.set_uppyUploadFile_drDocPath(dbModel.get_uppyUploadFile_drDocPath()+"/"+dbModel.getUppy_parent_id());
//        }
//        if (dbModel.getDrDocRepoModel() != null && !Validator.isEmpty(dbModel.getDrDocRepoModel().getDr_doc_id())) {
//            System.out.println("set to null?!?!");
//            dbModel.setDr_doc_id(dbModel.getDrDocRepoModel().getDr_doc_id());
//        }
//        super.postInsert(session); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public void preDelete(Session session, Object deletingObject) throws Exception {
        System.out.println("UppyModelService preDelete");
        if (dbModel.getDrDocRepoModel() != null) {
            if (!Validator.isEmpty(dbModel.getDrDocRepoModel().getDr_doc_id())) {
                BaseDAOImpl dao = new BaseDAOImpl();
                dao.setSession(session);
                SFTPBean sftp = new SFTPBean();
                sftp.deleteFile(dbModel.getDrDocRepoModel().getDr_doc_path());
                if (dbModel.appendRecordID()) {
                    sftp.deleteFolder(dbModel.getDrDocRepoModel().getDr_doc_path().substring(0, dbModel.getDrDocRepoModel().getDr_doc_path().lastIndexOf("/")));
                }
                System.out.println("dbModel.getDrDocRepoModel().getDr_doc_path() =  " + dbModel.getDrDocRepoModel().getDr_doc_path());
                dao.deleteModel(dbModel.getDrDocRepoModel());
            }
        }
    }
    
    
}
