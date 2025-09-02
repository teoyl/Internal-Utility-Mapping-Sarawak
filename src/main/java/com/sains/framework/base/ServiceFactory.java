/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sains.framework.base;

import com.backend.CounterPaymentEngine;
import com.backend.Post2RVSEngine;
import com.lxg.prepayment.dao.PrepaymentDAO;
import com.lxg.prepayment.dao.PrepaymentDAOImpl;
import com.sains.common.util.SFTPBeanConnectionPool;
import com.sains.framework.model.Module;
import com.sains.framework.model.SetupGroup;
import com.sains.framework.model.User;
import com.sains.framework.sam.dao.ApplicationCUDDAO;
import com.sains.framework.sam.dao.ApplicationDAOImpl;
import com.sains.framework.sam.dao.AutoEmailCUDDAO;
import com.sains.framework.sam.dao.AutoEmailDAOImpl;
import com.sains.framework.sam.dao.ParameterDAOImpl;
import com.sains.framework.sam.dao.UserCUDDAO;
import com.sains.framework.sam.dao.UserDAOImpl;
import com.sains.framework.sam.dao.UserGroupCUDDAO;
import com.sains.framework.sam.dao.UserGroupDAOImpl;
import com.utimaps.dao.UtilAppCUDDAO;
import com.utimaps.web.dao.PrecheckHistoryCUDDAO;
import com.utimaps.web.dao.PrecheckHistoryDAOImpl;
import com.utimaps.dao.SurveyFirmPCUDDAO;
import com.utimaps.dao.UtilAppDAOImpl;
import com.utimaps.dao.SurveyFirmPDAOImpl;
import com.sample.ParentModel;
import com.utimaps.model.AppLocalityModel;
import com.utimaps.model.PrecheckHistoryModel;
import com.utimaps.model.ApplicationPModel;
import com.utimaps.model.ChecklistSetupModel;
import com.utimaps.model.PaymentModel;
import com.utimaps.web.dao.IssuanceJobCUDDAO;
import com.utimaps.web.dao.IssuanceJobDAOImpl;
import com.utimaps.web.dao.SubmissionJobCUDDAO;
import com.utimaps.web.dao.SubmissionJobDAOImpl;
import com.utimaps.web.dao.SurveyApplicationCUDDAO;
import com.utimaps.web.dao.SurveyApplicationDAOImpl;
import com.utimaps.model.FileModel;
import com.utimaps.model.MiscellaneousPlanModel;

/**
 *
 * @author lenovo
 */
public class ServiceFactory {

    private static ServiceFactory instance;
    private static ServiceLocator locator;

    static {
        if (instance == null) {
            instance = new ServiceFactory();
        }
    }

    public ServiceFactory() {
        locator = ServiceLocator.getInstance();
    }

    public synchronized static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    //public BaseDAO getModuleService(){
    public BaseCUDDAO getModuleService() {
        try {
            return (BaseCUDDAO) locator.locateBaseDAO("ModuleDAOImpl", Module.class);
            //return (ModuleDAO)locator.locate(ModuleDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UtilAppCUDDAO getUtilAppService() {
        try {
            return (UtilAppCUDDAO) locator.locate(UtilAppDAOImpl.class);
            //return (ModuleDAO)locator.locate(ModuleDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public SurveyFirmPCUDDAO getSurveyFirmService() {
        try {
            return (SurveyFirmPCUDDAO) locator.locate(SurveyFirmPDAOImpl.class);
            //return (ModuleDAO)locator.locate(ModuleDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseCUDDAO getAppLocalityService() {
        try {
//            return (AppLocalityCUDDAO) locator.locate(AppLocalityDAOImpl.class);
            return (BaseCUDDAO) locator.locateBaseDAO("AppLocalityDAOImpl", AppLocalityModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrecheckHistoryCUDDAO getPrecheckHistoryService() {
        try {
//            return (AppLocalityCUDDAO) locator.locate(AppLocalityDAOImpl.class);
            return (PrecheckHistoryCUDDAO) locator.locate(PrecheckHistoryDAOImpl.class);
//            return (BaseCUDDAO) locator.locateBaseDAO("PrecheckHistoryDAOImpl", PrecheckHistoryModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseCUDDAO getFileModelService() {
        try {
            return (BaseCUDDAO) locator.locateBaseDAO("FileDAOImpl", FileModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ThoTH @ 23-Apr-2015 : replace to use ApplicationCUDDAO
    public ApplicationCUDDAO getApplicationService() {
        try {
            return (ApplicationCUDDAO) locator.locate(ApplicationDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AutoEmailCUDDAO getAutoEmailService() {
        try {
            return (AutoEmailCUDDAO) locator.locate(AutoEmailDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserCUDDAO getInternalUserService() {
        try {
            return (UserCUDDAO) locator.locate(UserDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserGroupCUDDAO getInternalUserGroupService() {
        try {
            return (UserGroupCUDDAO) locator.locate(UserGroupDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseCUDDAO getPublicUserService() {
        try {
            return (BaseCUDDAO) locator.locateBaseDAO("PublicUserDAOImpl", User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseCUDDAO getUserService() {
        try {
            return (BaseCUDDAO) locator.locateBaseDAO("UserDAOImpl", User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseCUDDAO getPrEmployeeService() {
        try {
            return (BaseCUDDAO) locator.locateBaseDAO("PrEmployeeDAOImpl", User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //     ahmadni @ 5-Jul-2016
    public BaseCUDDAO getPublicUserGroupService() {
        try {
            return (BaseCUDDAO) locator.locateBaseDAO("PublicUserGroupDAOImpl", SetupGroup.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseCUDDAO getParameterService() {
        try {
            return (BaseCUDDAO) locator.locate(ParameterDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseCUDDAO getSampleService() {
        try {
            return (BaseCUDDAO) locator.locateBaseDAO("SampleDAOImpl", ParentModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public SFTPBeanConnectionPool getSFTPBeanConnectionPool() {
        try {
            return (SFTPBeanConnectionPool) locator.locate(SFTPBeanConnectionPool.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseCUDDAO getChecklistSetupService() {
        try {
            return (BaseCUDDAO) locator.locateBaseDAO("ChecklistSetupDAOImpl", ChecklistSetupModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public SurveyApplicationCUDDAO getSurveyApplicationService() {
        try {
            return (SurveyApplicationCUDDAO) locator.locate(SurveyApplicationDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

//    public BaseCUDDAO getChecklistSurveyJobService() {
//        try {
//            return (BaseCUDDAO) locator.locate(ChecklistSurveyJobDAOImpl.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//        return null;
//    }
    public BaseCUDDAO getPaymentService() {
        try {
            return (BaseCUDDAO) locator.locateBaseDAO("PaymentDAOImpl", PaymentModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //ai min - 14/7/2011
    public PrepaymentDAO getPrepaymentService() {
        try {
            return (PrepaymentDAO) locator.locate(PrepaymentDAOImpl.class);
        } catch (Exception e) {
//            e.printStackTrace();
            new LogFunction().logError(this.getClass(), "", e);
        }
        return null;
    }

    public Post2RVSEngine getPost2RVSService() {
        try {
            return (Post2RVSEngine) locator.locate(Post2RVSEngine.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CounterPaymentEngine getCounterPaymentService() {
        try {
            return (CounterPaymentEngine) locator.locate(CounterPaymentEngine.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public IssuanceJobCUDDAO getIssuanceJobService() {
        try {
            return (IssuanceJobCUDDAO) locator.locate(IssuanceJobDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public SubmissionJobCUDDAO getSubmissionJobService() {
        try {
            return (SubmissionJobCUDDAO) locator.locate(SubmissionJobDAOImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public BaseCUDDAO getMiscPlanService() {
        try {
            return (BaseCUDDAO) locator.locateBaseDAO("MiscPlanDAOImpl", MiscellaneousPlanModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
