<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration Page</title>
<%--<s:head />--%>
<style type="text/css">
@import url(style.css);
</style>
</head>
<body>
	<form action="searchDynamic">
		<table class="wwFormTable">
		<s:iterator value="searchFields" var="field" status="rowStatus">
		<tr>
    		<td class="tdLabel"><label class="label">${searchFieldsLabel[rowStatus.index]}:</label></td>
			<td>
				<input type="text" name="search_${field}" value='<%=request.getParameter("${field}")==null? "" : request.getParameter("${field}")%>' size="30"/>
			</td>
		</tr>
		</s:iterator>
		<tr>
			<td colspan=2 align="right">
				<div align="right"><input type="submit" value="Search"/></div>
			</td>
		</tr>
		</table>
	</form>
</body>
</html>