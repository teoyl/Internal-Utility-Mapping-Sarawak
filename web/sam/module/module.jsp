<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration Page</title>
<style type="text/css">
@import url(style.css);
</style>
</head>
<body>
<s:include value="searchModule.jsp"></s:include>

<s:url id="changeLocale" action="changeLocale">
	
</s:url>
<s:a href="%{changeLocale}"><s:text name="change.locale"/></s:a>

<form action="Module">
	<s:submit action="addModule" value="Add" /> 
</form>

<form action="Module">
	<s:submit action="setupModule" value="Get Setup" /> 
</form>

<s:if test="moduleList.size() > 0">
	<div class="content">
	<table class="userTable" cellpadding="5px">
		<tr>
			<th>Module Code</th>
			<th>Name</th>
			<th>Type</th>
			<th>Edit</th>
			<th>Delete</th>
			<th>Parent</th>
		</tr>
		<s:iterator value="moduleList" status="moduleStatus">
			<tr
				class="<s:if test="#moduleStatus.odd == true ">odd</s:if><s:else>even</s:else>">
				<td><s:property value="module_code" /></td>
				<td><s:property value="module_name" /></td>
				<td><s:property value="module_type" /></td>
				<td>
					<s:url id="editURL" action="processEditModule">
						<s:param name="id" value="%{module_id}"></s:param>
					</s:url>
					<s:a href="%{editURL}">Edit</s:a>
				</td>
				<td>
					<s:url id="deleteURL" action="deleteModule">
						<s:param name="id" value="%{module_id}"></s:param>
					</s:url>
					<s:a href="%{deleteURL}">Delete</s:a>
				</td>
				<td>
					<s:if test="parentModule != null" >
						<% int i = 1; %>
						<s:iterator value="parentModule">
							<s:property value="module_name" />
						</s:iterator>
					</s:if>
				</td>
			</tr>
		</s:iterator>
	</table>
	</div>
</s:if>
</body>
</html>