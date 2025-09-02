<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Report Viewer</title>
<script language="javascript">
    function printRpt(printTo_) {
        document.getElementById('searchForm')['printTo_'].value = printTo_;
    }
</script>
</head>
<body>
<jsp:include page="/pages/base/actionError.jsp"></jsp:include>

<form id="searchForm" name="searchForm" action="generateDynamicRpt?${reportParams}" method="POST">
<s:hidden name="rptCode"/>
<s:hidden name="printTo_"/>
<table height="98%" cellspacing="1" cellpadding="3" border="0" width="100%">
    <s:if test="(has_right('exportToExcel') || has_right('exportToWord'))">
    <tr height="2%">
        <td>
            <%--<s:submit theme="simple" name="btnExportExcel" cssClass="defaultButton" action="exportToExcel" value="Export to SpreadSheet"  />    commented by Delvene @ 30-Jul-2013--%>
            <s:if test="has_right('exportToExcel')">
                <s:submit theme="simple" name="btnExportExcel" cssClass="defaultButton" action="generateDynamicRpt" onclick="printRpt('excel'); return true;" value="%{getText('button.exportToExcel')}"  />
            </s:if>
            <s:if test="has_right('exportToWord')">
                <s:submit theme="simple" name="btnExportWord" cssClass="defaultButton" action="generateDynamicRpt" onclick="printRpt('word'); return true;" value="%{getText('button.exportToWord')}"  />
            </s:if>
        </td>
    </tr></s:if>
    <tr height="100%">
        <td>
            <IFRAME id="pdfviewer" src="generateDynamicRpt?rptCode=<s:property value='rptCode'/>&<s:property value='reportParams'/>"
                    WIDTH=100% HEIGHT="800px" style="z-index: -999;">
            </IFRAME>
        </td>
    </tr>
</table>
</form>

</body>
</html>
