<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
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

            function required() {
                this.aa = new Array("group_code", "<s:text name='group.code' />");
                this.ab = new Array("group_name", "<s:text name='group.name' />");
            }

            function validateItems(form, errors) {
                if (!form.selected) {
                    errors[errors.length] = formatText(messageRequired, "Application");
                    return;
                }
                if (typeof (form.selected.type) == "string") {
                    validateItem(form.selected, 1, errors);
                } else {
                    var isError = false;
                    for (var i = 0; i < form.selected.length; i++) {
                        isError = validateItem(form.selected[i], i + 1, errors);
                        if (isError)
                            break;
                    }
                }
            }

            function validateItem(selected, idx, errors) {
                if (!selected.checked) {
                    //if (selected.value == ""){
                    errors[errors.length] = "Application at row " + idx + " not checked.";
                }
            }
            
            function searchUser() {
                submitForm_bshor("userGroupForm", "processEditUserUserGroup");
            }
        </SCRIPT>
        <%--<s:head />--%>

    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <%--div class="row">
            <div class="col-md-12">
                <h3 class="title-v3"><s:text name="userGroup" /> <small><s:text name="actionType.searchUser" /></small></h3>
            </div>
        </div--%>
        <form action="processUpdateUserUserGroup" name="userGroupForm" id="userGroupForm" method="post">
            <s:hidden name="action" />
            <s:hidden name="searchCondition" />
            <s:hidden name="ug_id" value="%{model.ug_id}" />
            <s:hidden name="id" value="%{model.ug_id}" />
            <s:hidden name="group_code" value="%{model.group_code}"/>
            <s:hidden name="group_name" value="%{model.group_name}"/>
            <s:hidden name="group_type" value="%{model.group_type}"/>
            <s:iterator value="model.groupApplication" status="appStatus" var="userGroupApp">
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
                    <s:iterator value="application.rightsList" status="rightsStatus" var="appRights">
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" value='%{#appRights.gaRights_id}' />
                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRightChecked" value='%{#appRights.gaRightChecked}'/>
                    </s:iterator>
                </s:if>
            </s:iterator>

            <div class="card">
                <div class="card-header bg-light">
                    <h5><s:property value="%{model.group_name}"/> (<s:property value="%{model.group_code}"/>)&nbsp;<small class="fw-normal text-600"><s:text name="button.search"/></small></h5>
                </div>
                <div class="card-body">
                    <div class="row pb-3">
                        <div class="col-md-5">
                            <div class="row">
                                <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.id"/></label>
                                <div class="col-md-8">
                                    <s:textfield name="ug_search_user_id" id="ug_search_user_id" cssClass="form-control form-control-sm" value="%{ug_search_user_id}" />
                                </div>
                            </div>
                        </div>
                        <div class="col-md-5">
                            <div class="row">
                                <label class="col-md-4 col-form-label col-form-label-sm"><s:text name="user.name"/></label>
                                <div class="col-md-8">
                                    <s:textfield name="ug_search_user_name" id="ug_search_user_name" cssClass="form-control form-control-sm" value="%{ug_search_user_name}" />
                                </div>
                            </div>
                        </div>
                        <div class="col-md-2 text-end">
                            <button class="btn btn-sm btn-primary" type="submit" name="searchUserBtn" id="searchUserBtn" onclick="searchUser();"><i class="fa fa-search"></i> <span class="ms-1"><s:text name="button.search"/></span></button>
                        </div>
                    </div>
                    <div class="row mt-3">
                        <div class="col-md-8 form-row-margin">
                            <s:if test="has_right('loadAddUserPage')">
                                <button class="btn btn-sm btn-falcon-default" type="submit" name="action:loadAddUserPageUserGroup" id="loadAddUserPageUserGroup"><i class="fa fa-plus"></i> <span class="ms-1"><s:text name="button.add.user"/></span></button>  
                                <%--<s:submit cssClass="defaultButton" theme="simple" action="loadAddUserPageUserGroup" value="Add User" />--%>
                            </s:if>
                            <s:if test="has_right('processDeleteUser')">
                                <button class="btn btn-sm btn-falcon-default" type="submit" name="action:processDeleteUserUserGroup" id="processDeleteUserUserGroup"><i class="far fa-trash-alt"></i> <span class="ms-1"><s:text name="button.delete.user"/></span></button>
                                <%--<s:submit cssClass="defaultButton" theme="simple" action="processDeleteUserUserGroup" value="Delete User">
                                          onclick="if ( isCheckboxSelected(form.user_selected)) {return confirmPermanentDelete();} else {return false;}"/>--%>
                            </s:if>
                        </div>
                        <div class="col-md-4 text-end">
                            <button class="btn btn-sm btn-falcon-default" type="submit" name="action:goEditPageUserGroup" id="goEditPageUserGroup"><i class="fa fa-arrow-left"></i> <span class="ms-1"><s:text name="button.back"/></span></button>  
                            <%--<s:submit cssClass="defaultButton" theme="simple" action="goEditPageUserGroup" value="Back"/>--%>
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col-md-12">
                            <div class="table-responsive">
                                <table class="table table-sds table-sm fs--1 table-striped table-hover" cellspacing="1" cellpadding="1"  width="100%">
                                    <thead class="bg-200 text-900">
                                        <tr>
                                            <th style="width:1%;" class="align-middle white-space-nowrap">
                                                <div class="form-check fs-0 mb-0">
                                                <s:if test="userList.size() > 0">
                                                    <input type="checkbox" id="user_select" name="user_select" onclick="toggleCheckbox(this, delUser_ids);" class="form-check-input">
                                                </s:if>
                                                <s:else>
                                                    <input type="checkbox" id="user_select" name="user_select" disabled class="form-check-input">
                                                </s:else>
                                                </div>
                                            </th>
                                            <th width="100" class="align-middle white-space-nowrap">
                                                <s:if test='sort_.equals("id")'>
                                                    <s:if test='order_.equals("A")'>
                                                        <a href='<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="sortURL_"/>&action=<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="action"/>&sort_=id&order_=D'><s:text name="user.id"/></a>
                                                        <img src='images/sortasc.gif' border='0'/>
                                                    </s:if><s:else>
                                                        <a href='<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="sortURL_"/>&action=<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="action"/>&sort_=id&order_=A'><s:text name="user.id"/></a>
                                                        <img src='images/sortdes.gif' border='0'/>
                                                    </s:else>
                                                </s:if><s:else>
                                                    <a href='<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="sortURL_"/>&action=<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="action"/>&sort_=id&order_=A'><s:text name="user.id"/></a>
                                                </s:else>
                                            </th>
                                            <th width="100" class="align-middle white-space-nowrap">
                                                <s:if test='sort_.equals("name")'>
                                                    <s:if test='order_.equals("A")'>
                                                        <a href='<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="sortURL_"/>&action=<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="action"/>&sort_=name&order_=D'><s:text name="user.name"/></a>
                                                        <img src='images/sortasc.gif' border='0'/>
                                                    </s:if><s:else>
                                                        <a href='<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="sortURL_"/>&action=<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="action"/>&sort_=name&order_=A'><s:text name="user.name"/></a>
                                                        <img src='images/sortdes.gif' border='0'/>
                                                    </s:else>
                                                </s:if><s:else>
                                                    <a href='<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="sortURL_"/>&action=<s:property escapeHtml="true" escapeJavaScript="true" escapeXml="true" escapeCsv="true" value="action"/>&sort_=name&order_=A'><s:text name="user.name"/></a>
                                                </s:else>
                                            </th>
                                            <th width="100" class="align-middle white-space-nowrap">Primary Contact</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <s:if test="userList != null && userList.size > 0">
                                            <s:iterator value="userList" status="userStatus" var="userGroupUser">
                                                <tr class="<s:if test="#userStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                    <td class="align-middle white-space-nowrap">
                                                        <div class="form-check fs-0 mb-0">
                                                        <s:checkbox theme="simple" name="user_selected" id="delUser_ids" fieldValue="%{#userGroupUser.ID}" onclick="checkToggleCheckbox(user_select, delUser_ids)" class="form-check-input"/>
                                                        </div>
                                                    </td>
                                                    <td width="100" class="align-middle white-space-nowrap"><s:property value="%{groupUser.us_user_id}" /></td>
                                                    <td width="100" class="align-middle white-space-nowrap"><s:property value='%{groupUser.us_user_name}' /></td>
                                                    <td width="100" class="align-middle white-space-nowrap">
                                                        <s:if test='#userGroupUser.ug_is_primary.equals("Y")'>
                                                            <s:text name='ug.primary.%{#userGroupUser.ug_is_primary}'/>
                                                        </s:if>
                                                        <s:else>
                                                            -
                                                        </s:else>
                                                    </td>
                                                </tr>
                                            </s:iterator>
                                            </s:if>
                                            <s:else>
                                                <tr><td colspan="4" class="text-center">--<s:text name="errors.recordNoFound"/>.--</td></tr>
                                            </s:else>
                                    </tbody>
                                </table>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <jsp:include page="/pages/pagination/b5_paging.jsp"></jsp:include>
                                </div>
                            </div>
                        </div>
                    </div> 
                </div>
            </div>   
        </form>
    </body>
</html>