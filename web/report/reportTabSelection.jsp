<%-- 
    Document   : reportTabSelection
    Created on : Aug 2, 2013, 10:08:29 AM
    Author     : Delvene
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
    <title>Report Tab Selection</title>
    <%--<s:head />--%>

    <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
    <link rel="stylesheet" type="text/css" href="styles/msen_style_internal.css" />
    <script language="javascript">
        function generateReport() {
            var selectedTab = "";
            var cbTab = document.getElementsByName("selectTab");

            if (!checkSelectedTab(cbTab)) {
                alert("Sila pilih laporan.");
                return false;
            }

            for(var i=0; i<cbTab.length; i++) {
                if(cbTab[i].checked) {
                    selectedTab += cbTab[i].value + ",";
                }
            }

            parent.openReport(selectedTab);
        }

        function checkSelectedTab(checkbox) {
            var is_checked = false;

            for(var i=0; i<checkbox.length; i++) {
                if(checkbox[i].checked) {
                    is_checked = true;
                    break;
                }
            }
            return is_checked;
        }
    </script>

    </head>
<body>
<table cellpadding="4" cellspacing="0">
    <tr>
        <td style="font-weight: bold;" colspan="3"><s:text name="PR.report.chooseTabMsg"/></td>
    </tr>
    <s:iterator value="menuButtonList" id="menuTab" status="menuTabStatus">
        <s:if test="(#menuTabStatus.index) % 3 == 0"><tr></s:if>
        <td><s:checkbox theme="simple" name="selectTab" id="ids" fieldValue="%{#menuTab.btnCode}" />&nbsp;<s:property value="%{#menuTab.btnName}" /></td>
        <s:if test="(#menuButtonStatus.index) % 3 == 2"></tr></s:if>
    </s:iterator>
    <tr>
        <td align="right" colspan="3"><s:submit theme="simple" cssClass="defaultButton" onclick="return generateReport();" value="%{getText('button.generate')}" /></td>
    </tr>
</table>
</body>
</html>
