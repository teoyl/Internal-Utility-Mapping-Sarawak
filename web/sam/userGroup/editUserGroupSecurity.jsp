<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<script type="text/javascript" src="pages/scripts/lookup.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
<SCRIPT language="Javascript">
	function localValidateForm(form, operation) {
		var errors = new Array();
		
		validateRequired(form, errors);
		// sample to validate items.
		//validateItems(form, errors);		
		if (errors.length > 0) {
			alert(errors.join('\n'));
			setFocus(form);
		}
		return errors.length > 0 ? false : true;
	}

	function required(){
		this.aa = new Array("group_code", "<s:text name='group.code' />");
		this.ab = new Array("group_name", "<s:text name='group.name' />");
	}

	function validateItems(form, errors) {
		if (!form.selected) {
			errors[errors.length] = formatText(messageRequired, "Application");
			return;
		}
		if (typeof(form.selected.type) == "string") {
			validateItem(form.selected, 1, errors);
		} else {
			var isError = false;
			for (var i=0; i < form.selected.length; i++) {
				isError = validateItem(form.selected[i],i+1,errors);
				if (isError) break;
			}
		}
	}

	function validateItem(selected, idx, errors) {
		if (!selected.checked){
		//if (selected.value == ""){
			errors[errors.length] = "Application at row " + idx + " not checked.";
		}
	}
        
        function clearUnit(rowNum) {
            $("#est_id_bu_"+rowNum).val("");
            $("#lb_est_description_bu_"+rowNum).text("");
            $("#est_description_bu_"+rowNum).val("");
        }    
        
        function checkDept(rowNum) {
            var deptId = $("#dept_id_"+rowNum).val();
            if (deptId === "") {
                alert("Jabatan perlu diisi");
                return false;
            }
            return true;
        }
</SCRIPT>
<%--<s:head />--%>

</head>
<body>
	<jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        <form action="processUpdateUserUserGroup" name="userGroupForm" method="post">
            <div class="titleFramework">
                <span class="titleText"><s:text name="group.groupSecurity" /></span>
                <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span>
            </div>
            <s:hidden name="action" />
            <s:hidden name="searchCondition" />
            <s:hidden name="ug_id" value="%{model.ug_id}" />
            <s:hidden name="id" value="%{model.ug_id}" />
            <s:hidden name="group_code" value="%{model.group_code}"/>
            <s:hidden name="group_name" value="%{model.group_name}"/>
            <s:hidden name="group_type" value="%{model.group_type}"/>
            <s:hidden name="dept_id" value="%{model.dept_id}"/>
            <s:hidden name="system_id" value="%{model.system_id}"/>
            <s:iterator value="model.groupApplication" status="appStatus" id="userGroupApp">
                <s:hidden name='groupApplication[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                <s:hidden name='groupApplication[%{#appStatus.index}].application.ID' value='%{application.ID}' />
                <s:hidden name='groupApplication[%{#appStatus.index}].application.application_code' value='%{application.application_code}' />
                <s:hidden name='groupApplication[%{#appStatus.index}].application.application_name' value='%{application.application_name}' />
                <s:hidden name="groupApplication[%{#appStatus.index}].application.create_right" value="%{application.create_right}"/>
                <s:hidden name="groupApplication[%{#appStatus.index}].create_right" id="create_ids" value='%{create_right}'/>
                <s:hidden name="groupApplication[%{#appStatus.index}].application.retrieve_right" value="%{application.retrieve_right}"/>
                <s:hidden name="groupApplication[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{retrieve_right}'/>
                <s:hidden name="groupApplication[%{#appStatus.index}].application.update_right" value="%{application.update_right}"/>
                <s:hidden name="groupApplication[%{#appStatus.index}].update_right" id="update_ids" value='%{update_right}'/>
                <s:hidden name="groupApplication[%{#appStatus.index}].application.delete_right" value="%{application.delete_right}"/>
                <s:hidden name="groupApplication[%{#appStatus.index}].delete_right" id="delete_ids" value='%{delete_right}'/>
                <s:hidden name="groupApplication[%{#appStatus.index}].application.print_right" value="%{application.print_right}"/>
                <s:hidden name="groupApplication[%{#appStatus.index}].print_right" id="print_ids" value='%{print_right}'/>
                <s:if test="application.rightsList.size > 0">
                    <s:iterator value="application.rightsList" status="rightsStatus" id="appRights">
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" value='%{#appRights.gaRights_id}' />
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRightChecked" value='%{#appRights.gaRightChecked}'/>
                    </s:iterator>
                </s:if>
            </s:iterator>
            <div class="xbox">
                <table width="100%" class="form">
                    <tr>
                        <td align="left">
                            <s:if test="has_right('loadAddSecurity')">
                            <s:submit cssClass="defaultButton" theme="simple" action="loadAddSecurityUserGroup" value="Add Access" />
                            </s:if>
                            <s:if test="has_right('processDeleteSecurity')">
                            <s:submit cssClass="defaultButton" theme="simple" action="processDeleteSecurityUserGroup" value="Delete Access"
                                      onclick="if ( isCheckboxSelected(form.user_selected)) {return confirmPermanentDelete();} else {return false;}"/>
                            </s:if>
                        </td>
                        <td align="right">
                            <s:submit cssClass="defaultButton buttonSave" theme="simple" action="processUpdateSecurityUserGroup" value="Save" />
                            <s:submit cssClass="defaultButton" theme="simple" action="goEditPageUserGroup" value="Back"/>
                        </td>
                    </tr>
                </table>
                <table class="defaultTable" cellspacing="1" cellpadding="1"  width="100%">
                    <tr>
                        <th width="1%">
                            <s:if test="userList.size() > 0">
                                <input type="checkbox" id="user_select" name="user_select" onclick="toggleCheckbox(this, delUser_ids);">
                            </s:if>
                            <s:else>
                                <input type="checkbox" id="user_select" name="user_select" disabled >
                            </s:else>
                        </th>
                        <th width="300px"><s:text name="userAccess.secuGroup.dept" /></th>
                        <th width="150px"><s:text name="userAccess.secuGroup.level" /></th>
                        <th><s:text name="userAccess.secuGroup.unit" /></th>
                    </tr>
                    <input type="hidden" name="est_type" value="u" id="est_type"/>
                    <s:if test="model.groupSecurity != null && model.groupSecurity.size > 0">
                        <s:iterator value="model.groupSecurity" status="localStatus" id="localList">
                            <tr class="<s:if test="#localStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                <td><s:checkbox theme="simple" name="user_selected" id="delUser_ids" fieldValue="%{#localList.ID}" onclick="checkToggleCheckbox(user_select, delUser_ids)" /></td>
                                <td><s:select list="departmentOption" id="dept_id_%{#localStatus.index}" listKey="keyData" listValue="valueData" theme="simple" cssClass="requiredField" name="model.groupSecurity[%{#localStatus.index}].dept_id" value="%{#localList.dept_id}" style="width:300px" onchange='clearUnit("%{#localStatus.index}")' />
                                    <%--<s:textfield theme="simple" name="model.groupSecurity[%{#localStatus.index}].dept_id" value="%{#localList.dept_id}" />--%></td>
                                <td><s:select list="accessTypeOption" id="defSecuLvlDesc_%{#localStatus.index}" listKey="keyData" listValue="valueData" theme="simple" cssClass="requiredField" name="model.groupSecurity[%{#localStatus.index}].access_level" value="%{#localList.access_level}" style="width:150px"/></td>
                                <td>
                                    <s:hidden name="model.groupSecurity[%{#localStatus.index}].est_id" id="est_id_bu_%{#localStatus.index}" value="%{#localList.est_id}" />
                                    <s:hidden name="model.groupSecurity[%{#localStatus.index}].establishment_bu.noAndDesc" id="est_description_bu_%{#localStatus.index}" value="%{#localList.establishment_bu.noAndDesc}"/>
                                    <script language="javascript">
                                        lookupH("<s:text name="actionType.search"/> <s:text name="postOper.sectionUnit"/>", "PtEstablishment", "est_id,noAndDesc,noAndDesc", "est_id_bu_<s:property value="%{#localStatus.index}" />,lb_est_description_bu_<s:property value="%{#localStatus.index}" />,est_description_bu_<s:property value="%{#localStatus.index}" />",
                                        "useSetup_PtEstablishment", "est_code, est_description", "", "checkDept(<s:property value="%{#localStatus.index}" />)", "dept_id_<s:property value="%{#localStatus.index}" />_as_dept_id,est_type");
                                    </script>
                                    <label id='lb_est_description_bu_<s:property value="%{#localStatus.index}" />'>${localList.establishment_bu.noAndDesc}</label>
                                    <%--<s:textfield theme="simple" name="model.groupSecurity[%{#localStatus.index}].est_id" value="%{#localList.est_id}" /> --%>
                                </td>
                            </tr>
                            <s:hidden name="model.groupSecurity[%{#localStatus.index}].se_id" />
                        </s:iterator>
                    </s:if>
                </table>
            </div>
	</form>
</body>
</html>