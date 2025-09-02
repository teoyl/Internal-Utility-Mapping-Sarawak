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
	<form action="searchUser">
		<s:textfield name="us_id" label="ID" value=""/>
		<s:textfield name="us_user_id" label="User ID"/>
		<s:textfield name="us_user_name" label="User Name" />
		<s:select name="us_admin" label="Admin" list="searchAdminList" listKey="keyData" listValue="valueData" />
		<s:select name="us_status" label="Status" list="searchStatusList" listKey="keyData" listValue="valueData" />
		<s:textfield name="us_email" label="Email Address" />
		<s:submit value="Search"/>
	</form>
</body>
</html>