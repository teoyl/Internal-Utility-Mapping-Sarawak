<%-- 
    Document   : post_report_list
    Created on : Dec 12, 2013, 9:50:51 AM
    Author     : Delvene
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
<script language="javascript">
    var winPodH;
    var reportLink;

    function openSelectTab() {
        winPodH=dhtmlmodal.open("popup", "iframe", "generateYearPRHR", "Pilih Laporan Untuk Dijana", "width=400px,height=200px,center=1,resize=1,scrolling=1", "");
        }
</script>
<form method="post" id="postReportFormID" action="processUpdatePost">
    <div class="titleFramework">
        <span class="titleText"><s:text name="report" /></span>
        <span class="titleActionTypeText"> | <s:text name="PR.actionType.list" /></span><br>
    </div>
    <div class="xbox">
        <table width="100%" cellpadding="0" cellspacing="0" border="0" class="pr-list">
            <colgroup>
                <col width="5%">
                <col width="2%">
                <col>
            </colgroup>
            <thead>
                <tr align="left">
                    <th align="center"><s:text name="common.no"/></th>
                    <th><%--some space--%></th>
                    <th><s:text name="POST.REPORT.name"/></th>
                </tr>
            </thead>
            <tbody>
                <s:iterator value="auditReportList" var="auditReports" status="auditReportStatus">
                    <tr class="<s:if test="#auditReportStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                        <td align="center">${auditReportStatus.index + 1}</td>
                        <td><%--some space--%></td>
                        <td>
                            <s:if test="(#auditReports.reportAccessRight)">
                                <a href="dynamicRptAction?rptCode=${auditReports.reportCode}">${auditReports.reportName}</a>
                            </s:if>
                            <s:else>
                                ${auditReports.reportName}
                            </s:else>
                        </td>
                    </tr>
                </s:iterator>
            </tbody>
        </table>
    </div>
</form>