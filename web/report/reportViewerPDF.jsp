<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Report Viewer</title>

</head>
<body>
<jsp:include page="/pages/base/actionError.jsp"></jsp:include>
<div class="titleFramework">
    <span class="titleText">Report Viewer</span>
</div>
<div class="xbox">
<form id="searchForm" name="searchForm" action="doOpenXlsReport" method="POST">
<table height="90%" cellspacing="1" cellpadding="3" border="0" width="100%">
    <tr height="2%">
        <td>
            <s:submit theme="simple" name="btnExportExcel" cssClass="defaultButton" action="doOpenXlsReport" value="Export to SpreadSheet"  />
        </td>
    </tr>
    <tr>
        <td>
            <IFRAME id="pdfviewer" src="doOpenPdfReport?pName=<s:property value='getpName()'/>&param1=<s:property value='getParam1()'/>"
                    WIDTH=100% HEIGHT=100% style="z-index: -999;">
            </IFRAME>
        </td>
    </tr>
    <s:hidden theme="simple" name="pName" value="%{pName}" />
    <s:hidden theme="simple" name="param1" value="%{param1}" />
</table>
</form>
</div>

</body>
</html>