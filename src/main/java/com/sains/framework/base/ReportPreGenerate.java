package com.sains.framework.base;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sains.common.util.DateUtil;
import com.sains.common.util.Formatter;
import com.sains.common.util.Options;
import com.sains.common.util.SystemConstants;
import com.sains.common.util.Validator;
import com.sains.framework.base.web.DynamicRptAction;
import com.sains.workflow.util.RouteUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class ReportPreGenerate{
    public static int count = 0;
    public String productSalesCheck(DynamicRptAction dynamicRptAction, Map param){        
        if (dynamicRptAction.isPrintToExcel() && dynamicRptAction.getConfigMap().get("jasperReportXls") != null){
            dynamicRptAction.setJasperReport(dynamicRptAction.getConfigMap().get("jasperReportXls").toString());
        }
//        if (true) {
//            actionSupport.addActionError("da da da da....");
//        }
        return ActionSupport.SUCCESS;
    }
    
    public String getDomainName(DynamicRptAction dynamicRptAction, Map param){
        param.put("pDomainName", SystemConstants.DOMAIN.domainName);
            
        return ActionSupport.SUCCESS;
    }

    public String preAnggaranBelanjawan(DynamicRptAction dynamicRptAction, Map param){
        Debug.printFrameworkDebug("param = " + param);
        Integer pYear = Integer.parseInt(param.get("pYear")+"");

        param.put("pQueryYear", pYear+"");
        pYear++;
        param.remove("pYear");
        param.put("pYear", pYear+"0101");

        return ActionSupport.SUCCESS;
    }

    public String preAnggaranBelanjawan2(DynamicRptAction dynamicRptAction, Map param){
        Integer pYear = Integer.parseInt(param.get("pYear")+"");

        //eg: if year = 2013, pQueryYear = 2013, pPreviousYear = '20130101', pYear = '20140101'
        //    because report will use created_date < pPreviousYear or pYear
        param.put("pQueryYear", pYear+"");
        param.put("pPreviousQueryYear", (pYear-1)+"");
        param.put("pPreviousYear", pYear+"0101");
        pYear++;
        param.remove("pYear");
        param.put("pYear", pYear+"0101");

        return ActionSupport.SUCCESS;
    }

    //added by Zhafari @19-Feb-2014
    public String preBukuPerkhidmatan(DynamicRptAction dynamicRptAction, Map param){
        try {
            String hisID[] = ((String)param.get("pHisID")).split("_his_");
            String pHisID = "";
            
            int i = 0;
            for (String id : hisID) {
                pHisID += "'" + id + "'";
                i++;
                if (i < hisID.length) pHisID += ",";
            }
            
            param.put("pHisID", pHisID);
            
        } catch (Exception e) {
            param.put("pHisID", null);
        }
        try {
            String page = (String)param.get("pPage");
            int tempPage = 0;
            if (page != null && !page.equals("")) {
                tempPage = Integer.parseInt(page);
            } else {
                tempPage = 1;
            }
            param.remove("pPage");
            param.put("pPage", tempPage);
            java.sql.Timestamp date = null;
            if (param.get("pDateFrom") != null) {
                date = DateUtil.getTimestampFromDate(((Date)param.get("pDateFrom")));
                param.put("pStartDate", Formatter.formatDate(date, "yyyyMMdd"));
            }
            if (param.get("pDateTo") != null) {
                date = DateUtil.getTimestampFromDate(((Date)param.get("pDateTo")));
                param.put("pEndDate", Formatter.formatDate(date, "yyyyMMdd"));
            }
            
            param.remove("pDateFrom");
            param.remove("pDateTo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ActionSupport.SUCCESS;
    }

    //Zhafari @ 24-Apr-2015
    public String preGenerateSenaraiPegawai(DynamicRptAction dynamicRptAction, Map param){
        int i = 1;
        for (String tab : param.get("pTabOption").toString().split("%2C")) {
            param.put("pField" + i, tab);
            i++;
        }
        return ActionSupport.SUCCESS;
    }
    
    //Zhafari @ 24-Apr-2015
    public String preGenerateBilanganPegawai(DynamicRptAction dynamicRptAction, Map param){        
        
        String subRep[] = param.get("pTabOption").toString().split("%2C");        

        for (int i = 0; i < subRep.length; i++) {
            if (subRep[i].contains("jantina")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bil_pegawai_jantina.jasper");
            } else if (subRep[i].contains("umur")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bil_pegawai_umur.jasper");
            } else if (subRep[i].contains("etnik")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bil_pegawai_etnik.jasper");
            } else if (subRep[i].contains("kp")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bil_pegawai_kp.jasper");
            } else if (subRep[i].contains("tl")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bil_pegawai_tl.jasper");
            } else if (subRep[i].contains("gp")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bil_pegawai_gp.jasper");
            } else if (subRep[i].contains("up")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bil_pegawai_up.jasper");
            } else if (subRep[i].contains("bahagian")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bil_pegawai_bahagian.jasper");
            }
        }
        for (int i = subRep.length; i < 8; i++) {
            param.put("pSubRep"+(i+1), null);
        }
        return ActionSupport.SUCCESS;
    }
    
    //Zhafari @ 24-Apr-2015
    public String preGenerateBilanganJawatan(DynamicRptAction dynamicRptAction, Map param){        
        
        String subRep[] = param.get("pTabOption").toString().split("%2C");        

        for (int i = 0; i < subRep.length; i++) {
            if (subRep[i].contains("gj")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bilangan_jawatan_gj.jasper");
            } else if (subRep[i].contains("kp")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bilangan_jawatan_kp.jasper");
            } else if (subRep[i].contains("sj")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bilangan_jawatan_sj.jasper");
            } else if (subRep[i].contains("tj")) {
                param.put("pSubRep"+(i+1), "rekod_statistik_penjawat_awam_bilangan_jawatan_tj.jasper");
            }
        }
        for (int i = subRep.length; i < 4; i++) {
            param.put("pSubRep"+(i+1), null);
        }
        return ActionSupport.SUCCESS;
    }
    
    //Zhafari @ 24-Apr-2015
    public String preGenerateMaklumatPekerja(DynamicRptAction dynamicRptAction, Map param){
        String selectedTab = param.get("pTabOption").toString().replaceAll("%2C", "");
        Debug.printFrameworkDebug("selectedTab " + selectedTab);
        param.put("pSelectedTab", selectedTab);
        return ActionSupport.SUCCESS;
    }
    
    //Zhafari @ 24-Apr-2015
    public String preGenerateKenyataanCuti(DynamicRptAction dynamicRptAction, Map param){
        param.put("pStartYear", (String)(param.get("leaveYearFrom")));
        param.put("pEndYear", (String)(param.get("leaveYearTo")));
//        System.out.println("pStartYear " + param.get("pStartYear"));
        try {
            String page = (String)param.get("pPage");
            int tempPage = 0;
            if (!Validator.isEmpty(page)) {
                tempPage = Integer.parseInt(page);
            } else {
                tempPage = 1;
            }
            param.remove("pPage");
            param.put("pPage", tempPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ActionSupport.SUCCESS;
    }
    
    public String preGenerateDilantikTahun(DynamicRptAction dynamicRptAction, Map param) {
        param.put("pStartYear", param.get("yearAptFrom"));
        param.put("pEndYear", param.get("yearAptTo"));
//        System.out.println("param.get(pStartYear) " + param.get("pStartYear"));
//        System.out.println("param.get(pEndYear) " + param.get("pEndYear"));
        return ActionSupport.SUCCESS;
    }
    
    public String preGenerateStatus(DynamicRptAction dynamicRptAction, Map param){
        String selectedTab = param.get("pStatus").toString().replaceAll("%2C", ",");
        param.put("pStatus", selectedTab);
        Debug.printFrameworkDebug("reportParams in pregenerate: " + param);
        return ActionSupport.SUCCESS;
    }
}

