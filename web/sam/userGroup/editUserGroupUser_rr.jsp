<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
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
            
        </SCRIPT>
        <s:head />

    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <%--div class="row">
            <div class="col-md-12">
                <h3 class="title-v3"><s:text name="userGroup" /> <small><s:text name="actionType.searchUser" /></small></h3>
            </div>
        </div--%>
        <div class="x_panel">
            <form action="processUpdateRRUserGroup" name="userGroupForm" method="post">
                <s:hidden name="action" />
                <s:hidden name="searchCondition" />
                <s:hidden name="ug_id" value="%{model.ug_id}" />
                <s:hidden name="id" value="%{model.ug_id}" />
                <s:hidden name="group_code" value="%{model.group_code}"/>
                <s:hidden name="group_name" value="%{model.group_name}"/>
                <s:hidden name="group_type" value="%{model.group_type}"/>
                <s:hidden name="spa_panel" value="%{model.spa_panel}"/><!--added by amywyp @ 06-02-2018-->
                <s:hidden name="dept_id" value="%{model.dept_id}"/>
                <s:hidden name="system_id" value="%{model.system_id}"/>
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


                <div class="row">
                    <div class="col-md-8 form-row-margin">
                        <s:if test="has_right('loadAddUserPage')">
                            <button class="btn btn-default" type="submit" name="action:loadAddUserPageUserGroup" id="loadAddUserPageUserGroup"><i class="fa fa-plus"></i>Add User</button>  
                            <%--<s:submit cssClass="defaultButton" theme="simple" action="loadAddUserPageUserGroup" value="Add User" />--%>
                        </s:if>
                        <s:if test="has_right('processDeleteUser')">
                            <button class="btn btn-default" type="submit" name="action:processDeleteUserUserGroup" id="processDeleteUserUserGroup"><i class="fa fa-trash-o"></i>Delete User</button>
                            <%--<s:submit cssClass="defaultButton" theme="simple" action="processDeleteUserUserGroup" value="Delete User">
                                      onclick="if ( isCheckboxSelected(form.user_selected)) {return confirmPermanentDelete();} else {return false;}"/>--%>
                        </s:if>
                    </div>
                    <div class="col-md-4 text-right">
                        <button class="btn btn-primary" type="submit" name="action:processUpdateRRUserGroup" id="processUpdateRRUserGroup" onclick="return validateForm(this.form, 'update')"><i class="fa fa-save"></i>Save</button> 
                        <button class="btn btn-default" type="submit" name="action:goEditPageUserGroup" id="goEditPageUserGroup"><i class="fa fa-arrow-left"></i>Back</button>  
                        <%--<s:submit cssClass="defaultButton" theme="simple" action="goEditPageUserGroup" value="Back"/>--%>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <table class="table table-espa table-condensed table-striped table-hover" cellspacing="1" cellpadding="1"  width="100%">
                                <thead>
                                    <tr>
                                        <th width="1%">
                                            <s:if test="userList.size() > 0">
                                                <input type="checkbox" id="user_select" name="user_select" onclick="toggleCheckbox(this, delUser_ids);">
                                            </s:if>
                                            <s:else>
                                                <input type="checkbox" id="user_select" name="user_select" disabled >
                                            </s:else>
                                        </th>
                                        <th width="100">
                                            <s:if test='sort_.equals("id")'>
                                                <s:if test='order_.equals("A")'>
                                                    <a href='${sortURL_}&action=${action}&sort_=id&order_=D'><s:text name="user.id"/></a>
                                                    <img src='images/sortasc.gif' border='0'/>
                                                </s:if><s:else>
                                                    <a href='${sortURL_}&action=${action}&sort_=id&order_=A'><s:text name="user.id"/></a>
                                                    <img src='images/sortdes.gif' border='0'/>
                                                </s:else>
                                            </s:if><s:else>
                                                <a href='${sortURL_}&action=${action}&sort_=id&order_=A'><s:text name="user.id"/></a>
                                            </s:else>
                                        </th>
                                        <th width="100">
                                            <s:if test='!sort_.equals("id")'>
                                                <s:if test='order_.equals("A")'>
                                                    <a href='${sortURL_}&action=${action}&sort_=name&order_=D'><s:text name="user.name"/></a>
                                                    <img src='images/sortasc.gif' border='0'/>
                                                </s:if><s:else>
                                                    <a href='${sortURL_}&action=${action}&sort_=name&order_=A'><s:text name="user.name"/></a>
                                                    <img src='images/sortdes.gif' border='0'/>
                                                </s:else>
                                            </s:if><s:else>
                                                <a href='${sortURL_}&action=${action}&sort_=name&order_=A'><s:text name="user.name"/></a>
                                            </s:else>
                                        </th>
                                        <th width="100">Next Action Officer</th>
                                        <th width="100">Seq</th>
                                        <th width="100">Availability</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:if test="model.groupUser != null && model.groupUser.size > 0">
                                        <s:iterator value="model.groupUser" status="userStatus" var="userGroupUser">
                                            <s:hidden name="model.groupUser[%{#userStatus.index}].ug_user_id" value='%{#userGroupUser.ug_user_id}'/>
                                            <s:hidden name="model.groupUser[%{#userStatus.index}].ug_id" value='%{#userGroupUser.ug_id}'/>
                                            <s:hidden name="model.groupUser[%{#userStatus.index}].us_id" value='%{#userGroupUser.us_id}'/>
                                            <tr class="<s:if test="#userStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                <td><s:checkbox theme="simple" name="user_selected" id="delUser_ids" fieldValue="%{#userGroupUser.ID}" onclick="checkToggleCheckbox(user_select, delUser_ids)"/></td>
                                                <td width="100"><s:property value="%{#userGroupUser.userModel.us_user_id}" /></td>
                                                <td width="200"><s:property value='%{#userGroupUser.userModel.us_user_name}' /></td>
                                                <td width="100">
                                                    <%--<s:radio id="next_action_officer" list="yesNoOptions" listKey="keyData" listValue="valueData" theme="simple" name="model.groupUser[%{#userStatus.index}.next_action_officer" value="%{#userGroupUser.next_action_officer}" ></s:radio>--%>
                                                    <input type="radio"  value="<s:property value="%{#userGroupUser.us_id}" />" id="next_action_officer_" name="next_action_officer_" <s:if test='#userGroupUser.next_action_officer.equals("Y")'>checked</s:if> >
                                                </td>
                                                <td width="100"><s:textfield theme="simple" onkeyup="this.value=this.value.replace(/[^\d]/,'')" name="model.groupUser[%{#userStatus.index}].seq" value="%{#userGroupUser.seq}" size="40" cssClass="form-control"/></td>
                                                <td width="50">
                                                    <label class="switch">
                                                        <input type="checkbox" id="on_duty[<s:property value='%{#userStatus.index}'/>]" name="model.groupUser[<s:property value='%{#userStatus.index}'/>].on_duty" <s:if test='#userGroupUser.on_duty.equals("Y")'>checked</s:if> >
                                                        <span class="slider round"></span>
                                                    </label>
                                                </td>
                                            </tr>
                                        </s:iterator>
                                        </s:if>
                                        <s:else>
                                            <tr><td colspan="6" class="text-center">--No record found.--</td></tr>
                                        </s:else>
                                </tbody>
                            </table>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <jsp:include page="/pages/pagination/paging.jsp"></jsp:include>
                            </div>
                        </div>
                    </div>
                </div>    





                <%--div class="panel panel-default ">  
                    <div class="panel-heading">
                        <h3 class="panel-title"> 
                            <span class="titleText"><s:text name="userGroup" /></span>
                            <span class="titleActionTypeText"> | <s:text name="actionType.searchUser" /></span></h3>
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
                    <!--<div class="xbox">-->
                    <div class="panel-body">
                        <table width="100%" class="form">
                            <tr>
                                <td align="left">
                                    <s:if test="has_right('loadAddUserPage')">
                                        <s:submit cssClass="defaultButton" theme="simple" action="loadAddUserPageUserGroup" value="Add User" />
                                    </s:if>
                                    <s:if test="has_right('processDeleteUser')">
                                        <s:submit cssClass="defaultButton" theme="simple" action="processDeleteUserUserGroup" value="Delete User"
                                                  onclick="if ( isCheckboxSelected(form.user_selected)) {return confirmPermanentDelete();} else {return false;}"/>
                                    </s:if>
                                </td>
                                <td align="right">
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
                                <th width="100">
                                    <s:if test='sort_.equals("id")'>
                                        <s:if test='order_.equals("A")'>
                                            <a href='${sortURL_}&action=${action}&sort_=id&order_=D'><s:text name="user.id"/></a>
                                            <img src='images/sortasc.gif' border='0'/>
                                        </s:if><s:else>
                                            <a href='${sortURL_}&action=${action}&sort_=id&order_=A'><s:text name="user.id"/></a>
                                            <img src='images/sortdes.gif' border='0'/>
                                        </s:else>
                                    </s:if><s:else>
                                        <a href='${sortURL_}&action=${action}&sort_=id&order_=A'><s:text name="user.id"/></a>
                                    </s:else>
                                </th>
                                <th width="100">
                                    <s:if test='!sort_.equals("id")'>
                                        <s:if test='order_.equals("A")'>
                                            <a href='${sortURL_}&action=${action}&sort_=name&order_=D'><s:text name="user.name"/></a>
                                            <img src='images/sortasc.gif' border='0'/>
                                        </s:if><s:else>
                                            <a href='${sortURL_}&action=${action}&sort_=name&order_=A'><s:text name="user.name"/></a>
                                            <img src='images/sortdes.gif' border='0'/>
                                        </s:else>
                                    </s:if><s:else>
                                        <a href='${sortURL_}&action=${action}&sort_=name&order_=A'><s:text name="user.name"/></a>
                                    </s:else>
                                </th>
                            </tr>
                            <s:if test="userList != null && userList.size > 0">
                                <s:iterator value="userList" status="userStatus" id="userGroupUser">
                                    <tr class="<s:if test="#userStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                        <td><s:checkbox theme="simple" name="user_selected" id="delUser_ids" fieldValue="%{#userGroupUser.ID}" onclick="checkToggleCheckbox(user_select, delUser_ids)"/></td>
                                        <td width="100"><s:property value="%{groupUser.us_user_id}" />
                                        </td>
                                        <td width="200"><s:property value='%{groupUser.us_user_name}' /></td>
                                    </tr>
                                </s:iterator>
                                <tr><td colspan="3">
                                        <jsp:include page="/pages/pagination/paging.jsp"></jsp:include>
                                        </td></tr>
                                </s:if>
                        </table>
                    </div>
                </div--%>
            </form>
        </div>
    </body>
</html>