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
	<s:hidden name="action" />
	<s:include value="/pages/base/dynamicSearch2.jsp">
		<s:param name="searchingParam" value="'Application'" />
	</s:include>
	
	<form>
		<table width="100%">
			<s:hidden name="action" />
			<s:submit theme="simple" action="addApplicationMenu" value="Add Selected" />
		</table>
		<s:include value="/pages/base/dynamicList2.jsp">
			<s:param name="listingParam" value="'Application'" />
		</s:include>
	</form>
</body>
</html>