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
<s:include value="searchUser.jsp"></s:include>
<form action="User">
	<s:submit action="addUser" value="Add" /> 
</form>

<s:if test="userList.size() > 0">
	<div class="content">
	<table class="userTable" cellpadding="5px">
		<tr class="even">
			<th>User ID</th>
			<th>Name</th>
			<th>Email</th>
			<th>Status</th>
			<th>Admin?</th>
			<th>Edit</th>
			<th>Delete</th>
		</tr>
		<s:iterator value="userList" status="userStatus">
			<tr
				class="<s:if test="#userStatus.odd == true ">odd</s:if><s:else>even</s:else>">
				<td><s:property value="us_user_id" /></td>
				<td><s:property value="us_user_name" /></td>
				<td><s:property value="us_email" /></td>
				<td><s:property value="us_status" /></td>
				<td><s:property value="us_admin" /></td>
				<td>
					<s:url id="editURL" action="processEditUser">
						<s:param name="id" value="%{us_id}"></s:param>
					</s:url>
					<s:a href="%{editURL}">Edit</s:a>
				</td>
				<td>
					<s:url id="deleteURL" action="deleteUser">
						<s:param name="id" value="%{us_id}"></s:param>
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