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
<s:include value="searchApplication.jsp"></s:include>

<form action="applicationAction">
	<s:submit action="addApplication" value="Add" /> 
</form>

<s:if test="applicationList.size() > 0">
	<div class="content">
	<table class="userTable" cellpadding="5px">
		<tr class="even">
			<th>Application Code</th>
			<th>Name</th>
			<th>Type</th>
			<th>Edit</th>
			<th>Delete</th>
		</tr>
		<s:iterator value="applicationList" status="applicationStatus">
			<tr
				class="<s:if test="#applicationStatus.odd == true ">odd</s:if><s:else>even</s:else>">
				<td><s:property value="application_code" /></td>
				<td><s:property value="application_name" /></td>
				<td><s:property value="application_type" /></td>
				<td>
					<s:url id="editURL" action="processEditApplication">
						<s:param name="id" value="%{application_id}"></s:param>
					</s:url>
					<s:a href="%{editURL}">Edit</s:a>
				</td>
				<td>
					<s:url id="deleteURL" action="deleteApplication">
						<s:param name="id" value="%{application_id}"></s:param>
					</s:url>
					<s:a href="%{deleteURL}">Delete</s:a>
				</td>
			</tr>
		</s:iterator>
	</table>
	</div>
</s:if>
</body>
</html>