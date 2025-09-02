package com.lxg.common.model;

import com.sains.common.util.DateUtil;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseDAOImpl;
import com.sains.framework.base.CommonFunction;
import com.sains.framework.base.CustomBaseException;
import com.sains.framework.base.ModelBase;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.SQLQuery;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
@Entity
@FilterDefs({
    @FilterDef(name="filterSubStatus")
})
@Table(name = "T_CUST_COMPANY")
public class CustCompanyModel extends ModelBase implements java.io.Serializable {

    private String co_id;
    private String co_name;
    private String co_reg_num;
    private String co_registered_address1;
    private String co_registered_address2;
    private String co_registered_address3;
    private String co_registered_address4;
    private String co_registered_postcode;
    private String co_registered_city;
    private String co_registered_state = SystemConstants.COMM_State.SARAWAK_SETUP_CODE;
    private String co_email;
    private String adv_code;
    private String co_status = "Y";
    private String co_branch;
    private String co_div;
    private String ps_id;

    private String co_registered_country;
    private String co_business_nature;
    private String co_business_others;

    private java.sql.Timestamp created_date;
    private String created_by;
    private java.sql.Timestamp updated_date;
    private String updated_by;
    private String swkid_corp = "";

     public String[] updatableColumns = new String[]{"Co_id", "Co_name", "Adv_code", "Co_status", "Co_reg_num", "Co_registered_address1",
        "Co_registered_address2", "Co_registered_address3", "Co_registered_address4", "Co_registered_postcode", "Co_registered_city", "Co_registered_state",
        "Co_branch", "Co_div", "Ps_id", "Updated_by", "Updated_date", "Swkid_corp", "Co_email", "Co_registered_country", "Co_business_nature", "Co_business_others"};
    private final String[] columnLength = new String[]{"co_name:100","adv_code:10","co_status:1","co_reg_num:20","co_registered_address1:50","co_registered_address2:50",
    "co_registered_address3:30","co_registered_address4:30","co_registered_postcode:5","co_registered_city:20","adv_code:10", "co_branch:200", "co_div:3", "ps_id:5", "swkid_corp:100", "co_email:100", "co_registered_country:2", "co_business_nature:500", "co_business_others:100"};
    private Map columnLengthMap = null;

    public static final class OPERATION {

        public static final String UPDATE = "update";
        public static final String UPDATE_PROFILE = "update_profile";
        public static final String DO_DELETE_CHILD = "do_delete_child";
        public static final String ATTACHMENT_TEMP_ADD = "add_temp_attachment";
        public static final String COMPLETE = "complete"; //sereneChye @ 29/8/2016
        public static final String UPDATE_EX_COR_STATUS = "update_ex_cor_status"; //sereneChye @ 5/9/2016
        public static final String ACTIVE_COR_STATUS = "active_ex_cor"; //sereneChye @ 27/10/2016
        public static final String UPDATE_COR_APP_RENEW = "update_cor_app_renew"; //sereneChye @ 31/10/2016
        public static final String UPDATE_COR_APP = "update_cor_app"; //sereneChye @ 31/10/2016
        public static final String UPDATE_APP_NEW = "update_app_new"; //sereneChye @ 31/10/2016
        public static final String UPDATE_APP_RENEW = "update_app_renew"; //sereneChye @ 31/10/2016
        public static final String INSERT_NEW_SAME_COR = "insert_new_same_cor"; //sereneChye @ 31/10/2016
        public static final String INSERT_NEW_DIFFER_COR = "insert_new_different_cor"; //sereneChye @ 31/10/2016
        public static final String FROM_DC = "from_dc"; //thensw @ 25/10/2017
        public static final String FROM_SP = "from_sp"; //thensw @ 25/10/2017
    }

    public CustCompanyModel() {
        userDefined_autoValidation(Boolean.TRUE);
//        userDefined_insertNoDuplicate("co_reg_num;user.coRegNum");
//        userDefined_updateNoDuplicate("co_reg_num;user.coRegNum");

//        userDefined_insertNoDuplicate("co_name;user.coName,co_reg_num;user.coRegNum");
//        userDefined_updateNoDuplicate("co_name;user.coName,co_reg_num;user.coRegNum");
//        userDefined_insertNoDuplicateRange("co_name,co_reg_num,co_branch");
//        userDefined_duplicateFieldsRangeKeyDescription("reg.search.coProfile");
        userDefined_manualDuplicationCheck(Boolean.TRUE);
    }

    @Id
    @Column(name = "co_id")
    public String getCo_id() {
        return co_id;
    }

    public void setCo_id(String co_id) {
        this.co_id = co_id;
    }

    @Transient
    public String getID() {
        return co_id;
    }

    public void setID(String pk_id) {
        this.co_id = pk_id;
    }

    @Column(name = "co_name")
    public String getCo_name() {
        return co_name;
    }

    public void setCo_name(String co_name) {
        this.co_name = co_name;
    }
    
    @Column(name = "adv_code")
    public String getAdv_code() {
        return adv_code;
    }

    public void setAdv_code(String adv_code) {
        this.adv_code = adv_code;
    }
    
    @Column(name = "co_div")
    public String getCo_div() {
        return co_div;
    }

    public void setCo_div(String co_div) {
        this.co_div = co_div;
    }
    
    @Transient
    public String getCo_name_branch() {
        if (Validator.isEmpty(co_branch)) {
            return co_name;
        }
        return co_name + " ("+ co_branch.trim() +")";
    }
    
    //[Start] @7Mar2014 aimin. [Remarks: For eLasis Shared Registration.]
    @Column(name = "ps_id")
    public String getPs_id() {
        return ps_id;
    }

    public void setPs_id(String ps_id) {
        this.ps_id = ps_id;
    }

    @Column(name = "co_reg_num")
    public String getCo_reg_num() {
        return co_reg_num;
    }

    public void setCo_reg_num(String co_reg_num) {
        this.co_reg_num = co_reg_num;
    }
    @Column(name = "co_branch")
    public String getCo_branch() {
        return co_branch;
    }

    public void setCo_branch(String co_branch) {
        this.co_branch = co_branch;
    }

    @Column(name = "co_registered_address1")
    public String getCo_registered_address1() {
        return co_registered_address1;
    }

    public void setCo_registered_address1(String co_registered_address1) {
        this.co_registered_address1 = co_registered_address1;
    }

    @Column(name = "co_registered_address2")
    public String getCo_registered_address2() {
        return co_registered_address2;
    }

    public void setCo_registered_address2(String co_registered_address2) {
        this.co_registered_address2 = co_registered_address2;
    }

    @Column(name = "co_registered_address3")
    public String getCo_registered_address3() {
        return co_registered_address3;
    }

    public void setCo_registered_address3(String co_registered_address3) {
        this.co_registered_address3 = co_registered_address3;
    }

    @Column(name = "co_registered_address4")
    public String getCo_registered_address4() {
        return co_registered_address4;
    }

    public void setCo_registered_address4(String co_registered_address4) {
        this.co_registered_address4 = co_registered_address4;
    }

    @Column(name = "co_registered_postcode")
    public String getCo_registered_postcode() {
        return co_registered_postcode;
    }

    public void setCo_registered_postcode(String co_registered_postcode) {
        this.co_registered_postcode = co_registered_postcode;
    }

    @Column(name = "co_registered_city")
    public String getCo_registered_city() {
        return co_registered_city;
    }

    public void setCo_registered_city(String co_registered_city) {
        this.co_registered_city = co_registered_city;
    }

    @Column(name = "co_registered_state")
    public String getCo_registered_state() {
        return co_registered_state;
    }

    public void setCo_registered_state(String co_registered_state) {
        this.co_registered_state = co_registered_state;
    }

    @Column(name = "co_email")
    public String getCo_email() {
        return co_email;
    }

    public void setCo_email(String co_email) {
        this.co_email = co_email;
    }
    
    // added @30.9.2011 ai min
    public String getCo_status() {
        return co_status;
    }

    public void setCo_status(String co_status) {
        this.co_status = co_status;
    }

    @Column(name = "swkid_corp")
    public String getSwkid_corp() {
        return swkid_corp;
    }

    public void setSwkid_corp(String swkid_corp) {
        this.swkid_corp = swkid_corp;
    }
    
    @Column(name = "co_registered_country")
    public String getCo_registered_country() {
        return co_registered_country;
    }

    public void setCo_registered_country(String co_registered_country) {
        this.co_registered_country = co_registered_country;
    }

    @Column(name = "co_business_nature")
    public String getCo_business_nature() {
        return co_business_nature;
    }

    public void setCo_business_nature(String co_business_nature) {
        this.co_business_nature = co_business_nature;
    }
    
    @Column(name = "co_business_others")
    public String getCo_business_others() {
        return co_business_others;
    }

    public void setCo_business_others(String co_business_others) {
        this.co_business_others = co_business_others;
    }
    
    @Column(name = "created_date")
    public java.sql.Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(java.sql.Timestamp created_date) {
        this.created_date = created_date;
    }

    @Column(name = "created_by")
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "updated_date")
    public java.sql.Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(java.sql.Timestamp updated_date) {
        this.updated_date = updated_date;
    }

    @Column(name = "updated_by")
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
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

    private String deletedUserItem = "";

    @Transient
    public String getDeletedUserItem() {
        return deletedUserItem;
    }

    public void setDeletedUserItem(String deletedUserItem) {
        this.deletedUserItem = deletedUserItem;
    }
    
    private String tempAppId = "";//serene @ 29/8/2016
    @Transient
    public String getTempAppId() {
        return tempAppId;
    }

    public void setTempAppId(String tempAppId) {
        this.tempAppId = tempAppId;
    }
    
    private String usId = "";//serene @ 29/8/2016
    @Transient
    public String getUsId() {
        return usId;
    }

    public void setUsId(String usId) {
        this.usId = usId;
    }

    // bernard @05-JUL-2016 : Shareholder List
//    private List<QPShareHolderModel> ShareHolderList = new ArrayList();
//
//    @OneToMany(targetEntity = QPShareHolderModel.class, fetch = FetchType.LAZY, mappedBy = "co_id")
//    @Filters({
//        @Filter(name="filterSubStatus", condition="share_sub_status != '12'"),
//    })
//    @OrderBy("created_date")
//
//    public List<QPShareHolderModel> getShareHolderList() {
//        return ShareHolderList;
//    }
//
//    public void setShareHolderList(List<QPShareHolderModel> ShareHolderList) {
//        this.ShareHolderList = ShareHolderList;
//    }
//
//    // bernard @12-JUL-2016 : Director List
//    private List<QPDirectorModel> DirectorList = new ArrayList();
//
//    @OneToMany(targetEntity = QPDirectorModel.class, fetch = FetchType.LAZY, mappedBy = "co_id")
//    @Filters({
//        @Filter(name="filterSubStatus", condition="dir_sub_status != '12'"),
//    })
//    @OrderBy("created_date")
//
//    public List<QPDirectorModel> getDirectorList() {
//        return DirectorList;
//    }
//
//    public void setDirectorList(List<QPDirectorModel> DirectorList) {
//        this.DirectorList = DirectorList;
//    }

    @Override
    public void preInsert(org.hibernate.Session session) throws Exception {
        if (get_operation() != null && get_operation().startsWith(OPERATION.FROM_DC)) {
            System.out.println("in preInsert...");
            this.defaultAddProperties();
//            if (this.get_operation().endsWith(DcApplicationAction.SubMenu.PA.getCode())) {
//                this.getCustCompany().defaultAddProperties();
//                this.getCustCompany().setInd_id(null);
//                this.getCustCompany().setCo_id(this.getID());
//                session.save(this.getCustCompany());
//            } else {
//                this.getCustCompany().defaultAddProperties();
//                this.getCustCompany().setInd_id(null);
//                this.getCustCompany().setCo_id(this.getID());
//                session.save(this.getCustCompany());
//            }
//            if (Validator.isEmpty(this.getCustCompany().getID())) {
//                this.getCustCompany().defaultAddProperties();
//                this.getCustCompany().setInd_id(null);
//                this.getCustCompany().setCo_id(this.getID());
//                session.save(this.getCustCompany());
//            } else {
//                this.getCustCompany().defaultUpdateProperties();
//                session.update(this.getCustCompany());
//            }
            
            updateShareholder(session, this, OPERATION.FROM_DC);
            updateDirector(session, this, OPERATION.FROM_DC);
        }else if (get_operation() != null && get_operation().startsWith(OPERATION.FROM_SP)) {
            System.out.println("sp in preInsert...");
            this.defaultAddProperties();
//            if (this.get_operation().endsWith(DcApplicationAction.SubMenu.PA.getCode())) {
//                this.getCustCompany().defaultAddProperties();
//                this.getCustCompany().setInd_id(null);
//                this.getCustCompany().setCo_id(this.getID());
//                session.save(this.getCustCompany());
//            } else {
//                this.getCustCompany().defaultAddProperties();
//                this.getCustCompany().setInd_id(null);
//                this.getCustCompany().setCo_id(this.getID());
//                session.save(this.getCustCompany());
//            }
//            if (Validator.isEmpty(this.getCustCompany().getID())) {
//                this.getCustCompany().defaultAddProperties();
//                this.getCustCompany().setCo_id(this.getID());
//                session.save(this.getCustCompany());
//            } else {
//                this.getCustCompany().defaultUpdateProperties();
//                session.update(this.getCustCompany());
//            }
            
            updateShareholder(session, this, OPERATION.FROM_DC);
            updateDirector(session, this, OPERATION.FROM_DC);
        } else {
            System.out.println("Insert new company" + this.getCo_id());
            UserCompanyModel userCo = new UserCompanyModel();
            userCo.setUsco_status(SystemConstants.USER_ACC_STATUS.ACTIVE);
            userCo.setUs_id(this.getUs_id());
            userCo.setCo_id(this.getCo_id());

            userCo.defaultAddProperties();
            session.save(userCo);

            updateShareholder(session, this, OPERATION.UPDATE_COR_APP);
            updateDirector(session, this, OPERATION.UPDATE_COR_APP);
            updateAttachment(session, this, OPERATION.UPDATE_COR_APP);
        }
    }

    @Override
    public void preUpdate(org.hibernate.Session session, Object dataEntryModel) throws Exception {
        checkingDAO.setSession(session);
        setSession_(session);
        CustCompanyModel updatingModel = (CustCompanyModel) dataEntryModel;
        // String strUs_id = (String) ActionContext.getContext().getSession().get("userId");
        System.out.println("updatingModel.get_operation()" + updatingModel.get_operation());
        //need to make sure all operation with value
        if(Validator.isEmpty(updatingModel.get_operation())){
            //do nothing
        }
        else if (updatingModel.get_operation().startsWith(OPERATION.FROM_DC)) {
            System.out.println("in preUpdate... from_dc custCompany");

            updateShareholder(session, updatingModel, OPERATION.FROM_DC);
            updateDirector(session, updatingModel, OPERATION.FROM_DC);
        }else if (updatingModel.get_operation().startsWith(OPERATION.FROM_SP)) {//for Siting - ahmadni @ 14-Dec-2017
            updateShareholder(session, updatingModel, OPERATION.FROM_DC);
            updateDirector(session, updatingModel, OPERATION.FROM_DC);
        }
        else if (updatingModel.get_operation().equals(OPERATION.UPDATE)) {
          
        }else if(updatingModel.get_operation().equals(OPERATION.UPDATE_COR_APP)){
            updateShareholder(session, updatingModel,OPERATION.UPDATE_COR_APP);
            updateDirector(session, updatingModel, OPERATION.UPDATE_COR_APP);
            updateAttachment(session, updatingModel, OPERATION.UPDATE_COR_APP);
                
        }else if(updatingModel.get_operation().equals(OPERATION.COMPLETE)){
            updatingModel.updatableColumns = new String[]{"Co_sub_status"};
            
        }else if(updatingModel.get_operation().equals(OPERATION.UPDATE_EX_COR_STATUS)){
            updatingModel.updatableColumns = new String[]{""};
            
            UserCompanyModel usComModel = (UserCompanyModel)checkingDAO.getObjectByCode("app_id,usco_status", updatingModel.getTempAppId()+","+"Y", new UserCompanyModel());
            if(usComModel != null){
                usComModel.updatableColumns = new String[]{"Usco_id","Usco_status", "Usco_status_date","Us_id"};
                usComModel.setUsco_status(SystemConstants.USER_ACC_STATUS.INACTIVE);
                usComModel.setUs_id(updatingModel.getUs_id());
                usComModel.setUsco_status_date(DateUtil.getCurrentTimestamp());
                usComModel.defaultUpdateProperties();
                session.update(usComModel);
            }
        }else if(updatingModel.get_operation().equals(OPERATION.INSERT_NEW_SAME_COR)){
            UserCompanyModel userCo = new UserCompanyModel();
            userCo.setUsco_status(SystemConstants.USER_ACC_STATUS.ACTIVE);
            userCo.setUs_id(updatingModel.getUs_id());
            userCo.setCo_id(updatingModel.getCo_id());
            userCo.defaultAddProperties();
            session.save(userCo);

            updatingModel.updatableColumns = new String[]{"Co_id", "Co_name", "Co_reg_num", "Co_registered_address1", "Co_registered_address2", "Co_registered_address3", "Co_registered_address4",
            "Co_registered_postcode", "Co_registered_city", "Co_registered_state", "Co_outside_address1", "Co_outside_address2", "Co_outside_address3", "Co_outside_address4",
            "Co_outside_postcode", "Co_outside_city", "Co_outside_state", "Co_outside_country", "Co_reg_date", "Co_permit_date", "Co_phone_number", "Updated_by", "Updated_date"};
            updateShareholder(session, this, OPERATION.UPDATE_COR_APP);
            updateDirector(session, this, OPERATION.UPDATE_COR_APP);
            updateAttachment(session, this, OPERATION.UPDATE_APP_NEW);
        }
    }

    private void updateShareholder(org.hibernate.Session session, CustCompanyModel updatingModel, String AppType) throws Exception {

    }

    private void updateDirector(org.hibernate.Session session, CustCompanyModel updatingModel,  String AppType) throws Exception {
        
    }

    private void updateAttachment(org.hibernate.Session session, CustCompanyModel updatingModel,  String AppType) throws Exception {
        //**Attachment
        CommonFunction cf = new CommonFunction();
        try {
            
        } catch (Exception e) {
        }finally{
            closeFTP();
        }
    }

    @Override
    public void manualOperation(org.hibernate.Session session) throws Exception {
        CommonFunction cf = new CommonFunction();
        try {

            if (this.get_operation().equals("delete")) {
                BaseDAOImpl dao = new BaseDAOImpl();
                dao.setSession(session);
                dao.auditDelete(this);
            } else if (this.get_operation().equals(OPERATION.ATTACHMENT_TEMP_ADD)) {
                
            }
        } catch (Exception e) {
            throw e;
        } finally{
            closeFTP();
        }
    }
    
    private List editColumnList = new ArrayList();
    public void setEditColumnList(List editColumnList) {
        this.editColumnList = editColumnList;
    }
    
    public String us_id;
    @Transient

    public String getUs_id() {
        return us_id;
    }

    public void setUs_id(String us_id) {
        this.us_id = us_id;
    }
    
    @Override
    public void manualDuplicationCheck(org.hibernate.Session session, Object dataEntryModel) throws Exception {
        SQLQuery query = null;
        try {
            String extraStmt = "";
            CustCompanyModel updatingModel = (CustCompanyModel) dataEntryModel;
            if (!Validator.isEmpty(updatingModel.getID())) { //update
                extraStmt = " and t_cust_company.co_id != :co_id ";
            }
            if (updatingModel.get_operation() != null && updatingModel.get_operation().startsWith(OPERATION.FROM_DC)) {
                if (Validator.isEmpty(updatingModel.getCo_branch())) {
                    query = session.createSQLQuery("select count(*) from t_cust_company "
                            + "inner join spa_applicant on spa_applicant.co_id = t_cust_company.co_id "
                            + "where ltrim(rtrim(co_name)) = :co_name "
                            + "and ltrim(rtrim(co_reg_num)) = :co_reg_num and (co_branch is null or co_branch = '') "
                            + "and spa_applicant.applicant_role = :applicant_role "
                            + "and app_id = :app_id " + extraStmt);

                } else {
                    query = session.createSQLQuery("select count(*) from t_cust_company "
                            + "inner join spa_applicant on spa_applicant.co_id = t_cust_company.co_id "
                            + "where ltrim(rtrim(co_name)) = :co_name "
                            + "and ltrim(rtrim(co_reg_num)) = :co_reg_num "
                            + "and spa_applicant.applicant_role = :applicant_role "
                            + "and ltrim(rtrim(co_branch)) = :co_branch " 
                            + "and app_id = :app_id " + extraStmt);
                    query.setString("co_branch", updatingModel.getCo_branch());
                }
                query.setString("co_name", updatingModel.getCo_name());
                query.setString("co_reg_num", updatingModel.getCo_reg_num());
            } else {
                if (Validator.isEmpty(updatingModel.getCo_branch())) {
                    query = session.createSQLQuery("select count(*) from t_cust_company where ltrim(rtrim(co_name)) = :co_name "
                            + "and ltrim(rtrim(co_reg_num)) = :co_reg_num and (co_branch is null or co_branch = '') "
                            + "and co_id not in (select co_id from spa_applicant) " + extraStmt);
                } else {
                    query = session.createSQLQuery("select count(*) from t_cust_company where ltrim(rtrim(co_name)) = :co_name "
                            + "and ltrim(rtrim(co_reg_num)) = :co_reg_num "
                            + "and ltrim(rtrim(co_branch)) = :co_branch "
                            + "and co_id not in (select co_id from spa_applicant) " + extraStmt);
                    query.setString("co_branch", updatingModel.getCo_branch());
                }
                query.setString("co_name", updatingModel.getCo_name());
                query.setString("co_reg_num", updatingModel.getCo_reg_num());
            }
            if (!Validator.isEmpty(updatingModel.getID())) { //update
                query.setString("co_id", updatingModel.getID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object from = query.uniqueResult();
        Method m = from.getClass().getMethod("longValue");
        Long value = (Long)m.invoke(from);
        
        if (value > 0) {
            throw new CustomBaseException("This Company Profile already exists.");
        }
    }
}
