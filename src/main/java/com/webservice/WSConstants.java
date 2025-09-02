/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webservice;

/**
 *
 * @author Aiman
 */
public class WSConstants {

    public static final String ClientId = "ClientId";

    public static final class Path {
//        public static final String Base = "http://10.17.101.21:8080/eMINDS_V6";
//        tnt
//        public static final String Base = "https://eminds.tnt.sarawak.gov.my/eMINDS";
//        live

//        public static final String Base = "https://eminds.sarawak.gov.my/eMINDS";
//        public static final String eMindsWS = "/emindService";
        public static final String utimapsWS = "/emindService";
        public static final String AuthService = "/auth";
//        public static final class CodeService {  // ThoTH @ Dummy Testing
//            public static final String Main = "/workspace";
//            public static final String Test = "/access";
//            public static final String Get = "/get";
//            public static final String Update = "/update";
//        }

        public static final class CodeService {  // ThoTH @ Dummy Testing

            public static final String Main = "/code";
            public static final String Test = "/test";
            public static final String Get = "/get";
            public static final String Coa = "/productCategory";
            public static final String Division = "/division";
            public static final String Update = "/update";
            public static final String Kpi = "/kpi";
        }

        public static final class PaymentService {  // Kuehlk @ 18-Oct-2019

            public static final String Main = "/payment";
            public static final String Test = "/test";
            public static final String Get = "/getPayment";
            public static final String Update = "/updatePayment";
            public static final String Cancel = "/cancelBill";

        }

        public static final class WorkspaceService {  // Kuehlk @ 18-Oct-2019

            public static final String Main = "/scsWorkspace";
            public static final String Access = "/access";
            public static final String Overview = "/pendingTaskOverview";
            public static final String Wireman = "/pendingTaskWireman";
            public static final String Chargeman = "/pendingTaskChargeman";
            public static final String Task = "/pendingTask";
            //added by KLK @ 13-Aug-2021 for SCS Mobile
            public static final String ScsTask = "/getScsTask";
            public static final String ScsTaskList = "/getScsTaskList";
            public static final String Check = "/check";  // KuehLK @ 20-Feb-2021
        }

        public static final class ProductService {  // Kuehlk @ 5-Oct-2020

            public static final String Main = "/product";
            public static final String Test = "/test";
            public static final String Get = "/searchProduct";

        }

        public static final class CertificateService {  // Kuehlk @ 5-Oct-2020

            public static final String Main = "/certificate";
            public static final String Test = "/test";
            public static final String Get = "/searchCertificate";

        }

        public static final class MutPortalService {  // Kuehlk @ 21-Nov-2022

            public static final String Main = "/mutPortal";
            public static final String Test = "/test";
            public static final String Get = "/loadApplication";
            public static final String MoumisUserAcct = "/moumisUserAccount";

        }
    }

    public static final class Key {

        public static final String Scope = "SCOPE";
        public static final String Login = "LOGIN";
        public static final String Password = "PWD";
        public static final String Query = "QUERY";
        public static final String Option = "OPTION";
        public static final String Filter = "FILTER";
        public static final String SearchDate = "SEARCH_DATE";
        public static final String SearchEmp = "SEARCH_EMP";
        public static final String SearchUserId = "SearchUserId";
        public static final String SearchId = "SearchId";  // ThoTH @ 20-Jul-2019 : Common ID
        public static final String Results = "RESULTS";
        public static final String Status = "STATUS";
        public static final String ErrorMsg = "ERRMSG";
        public static final String SearchAppRefNo = "SearchAppRefNo";
        public static final String SearchReceiptNo = "SearchReceiptNo";
        public static final String SearchReceiptDate = "SearchReceiptDate";
        public static final String SearchReceiptAmt = "SearchReceiptAmt";
        // KuehLK @ 03-NOV-2022 :
        public static final String SearchBillNo = "SearchBillNo";
        public static final String SearchColCode = "SearchColCode";
        public static final String SearchColName = "SearchColName";

        // KuehLK @ 06-OCT-2020 :
        public static final String SearchModel = "SearchModel";
        public static final String SearchBrand = "SearchBrand";
        public static final String SearchCategory = "SearchCategory";

        public static final String SearchHolderName = "SearchHolderName";
        public static final String SearchCertType = "SearchCertType";
        public static final String SearchDivision = "SearchDivision";

        // Update : // ThoTH @ 21-Jul-2019 
        public static final String UpdateValue1 = "VALUE_1";
        public static final String UpdateValue2 = "VALUE_2";
        public static final String UpdateValue3 = "VALUE_3";
        public static final String UpdateValue4 = "VALUE_4";

        public static final String ApplicationList = "ApplicationList";
        public static final String CertificateList = "CertificateList";

        // KuehLK @ 13-DEC-2022 :
        public static final String client_id = "client_id";
        public static final String token = "token";
        public static final String user_id = "user_id";
        public static final String user_full_name = "user_full_name";
        public static final String user_email = "user_email";
        public static final String user_status = "user_status";
        public static final String user_ldap = "user_ldap";

        // KuehLK @ 10-Jul-2024 :
        public static final String SearchSpayNo = "SearchSpayNo";
    }

    public static final class Error {

        public static final String Unauthorized = "UNAUTHORIZED";
        public static final String InvalidFormat = "Invalid Format";
        public static final String InsufficientData = "Insufficient Data";
        public static final String InvalidBillNo = "Bill No. not available for cancellation.";
        public static final String InvalidClientId = "Invalid Client ID";
        public static final String NotSupported = "Request Not Supported";
        public static final String RecordNotFound = "Record Not Found";
        public static final String PaymentStatusUpdated = "Bill status has already been updated.";
        public static final String UpdateFailed = "Fail to update/insert.";
        public static final String InvalidWorkspaceToken = "Invalid Token (Workspace)";
    }

    public static final class YesNo {

        public static final String Yes = "Y";
        public static final String No = "N";
    }

    public static final class Status {

        public static final String Success = "SUCCESS";
        public static final String Failed = "FAILED";
        public static final String Created = "SUCCESS_CREATED";  // ThoTH @ 21-Jul-2019 
        public static final String Updated = "SUCCESS_UPDATED";  // ThoTH @ 21-Jul-2019 
    }
}
