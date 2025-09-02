<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration Page</title>
<%--<s:head />--%>
<style type="text/css">
@import url(style.css);
</style>
</head>
<body>
	<jsp:include page="/pages/base/actionError.jsp"></jsp:include>
	<form action="processUpdateMenu" name="menuForm">
		<s:hidden name="action" />
		<s:hidden name="menu_id" value="%{model.menu_id}"/>
		<table>
			<tr>
				<td align="right">
					<s:submit theme="simple" value="Create Menu" />
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td align="right"><s:text name="menu.code"/>:</td><td><s:textfield theme="simple" name="menu_code" value="%{model.menu_code}"/></td>
			</tr>
			<tr>
				<td align="right"><s:text name="menu.name"/>:</td><td><s:textfield theme="simple" name="menu_name" value="%{model.menu_name}"/></td>
			</tr>
			<tr>
				<td colspan="2">
					<s:submit theme="simple" action="processAddApplicationMenu" value="Add Application" />
					<s:submit theme="simple" action="processDeleteApplicationMenu" value="Delete Application" />
				</td>
			</tr>
			<tr>
				<table>
					<tr>
						<th width="10">
		            		<s:if test="applicationList.size() > 0">
		              			<input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, delApp_ids);">
		              		</s:if>
		              		<s:else>
		              			<input type="checkbox" id="cbselect" name="cbselect" disabled >
		              		</s:else>
		            	</th>
						<th width="100">Application Code</th>
						<th width="200">Application Name</th>
						<th width="400"colSpan="5">Access Right</th>
					</tr>
					<s:iterator value="applicationList.values" status="appStatus" id="menuApp">
					<tr class="<s:if test="#appStatus.odd == true ">odd</s:if><s:else>even</s:else>">
						<td><s:checkbox theme="simple" name="selected" id="delApp_ids" fieldValue="%{menuApplication.application_id}" onclick="checkToggleCheckbox(cbselect, delApp_ids)"/></td>
						<td width="100"><s:property value="%{menuApplication.application_code}" />
						</td>
						<td width="200"><s:property value='%{menuApplication.application_name}' /></td>
						<%int index=0; %>
						<td width="80">
							<s:checkbox theme="simple" name="accessRight_create_%{#appStatus.index}" id="create_ids" value='%{create_right.equals("Y")?"True":"false"}'
							fieldValue="Y" disabled="%{menuApplication.create_right.equalsIgnoreCase('Y')?'false':'true'}" />
							<s:text name="right.create" />
						</td>
						<td width="80">
							<s:checkbox theme="simple" name="accessRight_retrieve_%{#appStatus.index}" id="retrieve_ids" value='%{retrieve_right.equals("Y")?"True":"false"}' 
							fieldValue="Y" disabled="%{menuApplication.retrieve_right.equalsIgnoreCase('Y')?'false':'true'}" />
							<s:text name="right.retrieve" />
						</td>
						<td width="80">
							<s:checkbox theme="simple" name="accessRight_update_%{#appStatus.index}" id="update_ids" value='%{update_right.equals("Y")?"True":"false"}' 
							fieldValue="Y" disabled="%{menuApplication.update_right.equalsIgnoreCase('Y')?'false':'true'}" />
							<s:text name="right.update" />
						</td>
						<td width="80">
							<s:checkbox theme="simple" name="accessRight_delete_%{#appStatus.index}" id="delete_ids" value='%{delete_right.equals("Y")?"True":"false"}'
							fieldValue="Y" disabled="%{menuApplication.retrieve_right.equalsIgnoreCase('Y')?'false':'true'}" />
							<s:text name="right.delete" />
						</td>
						<td width="80">
							<s:checkbox theme="simple" name="accessRight_print_%{#appStatus.index}" id="print_ids" value='%{print_right.equals("Y")?"True":"false"}'
							fieldValue="Y" disabled="%{menuApplication.print_right.equalsIgnoreCase('Y')?'false':'true'}" />
							<s:text name="right.print" />
						</td>
					</tr>
					</s:iterator>				
				</table>
			</tr>
		</table>
	</form>
</body>
</html>