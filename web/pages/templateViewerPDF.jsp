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
    <br/>
<jsp:include page="/pages/base/actionError.jsp"></jsp:include>
<div class="titleFramework">
    <table border="0" cellpadding="0" cellspacing="0" class="tableTop">
        <tr>
            <td width="40" align="right"><img src="images/pdf.png" alt="" width="24" height="24"/></td>
             <td><span class="titleText">Template Viewer</span><br></td>
        </tr>
    </table>
</div>


    <div class="xbox" style="margin: 0px 15px 0 10px;">
<form id="searchForm" name="searchForm" action="doOpenXlsReport" method="POST">
    
    <%--<table style="height: 100%" cellspacing="1" cellpadding="3" border="0" width="100%">--%>
    <table style="height: 100%" cellspacing="0" cellpadding="0" border="0" width="100%">
    <tr height="2%">
        <td>
            <%--<s:submit theme="simple" name="btnExportExcel" cssClass="defaultButton" action="doOpenXlsReport" value="Export to SpreadSheet"  />--%>
        </td>
    </tr>
    <tr >
        <td height="88%">
            <IFRAME id="pdfviewer" src="viewPdf<s:property value='getAction()'/>?id=<s:property value='getId()'/>&formType=<s:property value='getFormType()'/>"
                    WIDTH=100% HEIGHT="1000px" style="z-index: -999;">
            </IFRAME>
        </td>
    </tr>
    <%--<s:hidden theme="simple" name="pName" value="%{pName}" />
    <s:hidden theme="simple" name="param1" value="%{param1}" />--%>
</table>
</form>
</div>

</body>
</html>