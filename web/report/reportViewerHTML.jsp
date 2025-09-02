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
<s:set name="reportContent" value="strBuffer" />
<table  cellspacing="1" cellpadding="3" border="0" width="100%" >
    <tr><td width="100%">Buttons Here</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td><s:property escape="false" value="strBuffer"/></td></tr>
</table>

<%--<form id="myForm" action="processInsertGrabJob">
<table border="1">
	<tr>
		<td>Workflow</td>
		<td>Number of Job Pending</td>
		<td>&nbsp;</td>
	</tr>
	<s:iterator value="listJobtobeGrab" status="jobPoolStatus" id="iteratorJobPool">
	<tr>
		<td><s:property value="%{#iteratorJobPool.get('wfname')}"/></td>
		<td align="center"><s:property value="%{#iteratorJobPool.get('longcount')}"/></td>
		<td>
			<s:a href="processInsertGrabJob?en_id=%{#iteratorJobPool.get('enid')}">Grab Job</s:a>
		</td>
	</tr>

	</s:iterator>
</table>
</form>--%>
</body>
</html>