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
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />

        <SCRIPT language="javascript">
            function localValidateForm(form, operation) {
                var errors = new Array();

                validateRequired(form, errors);
                // sample to validate items.
                validateItems(form, errors);
                if (errors.length > 0) {
                    alert(errors.join('\n'));
                    setFocus(form);
                }
                return errors.length > 0 ? false : true;
            }


            function required() {
            }


        </SCRIPT>

        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
            <form method="post" theme="simple" action="processUpdateApplication">
            <s:hidden theme="simple" name="action" />
            <s:hidden theme="simple" name="id" value="%{model.application_id}"/>
            <s:hidden theme="simple" name="application_id" value="%{model.application_id}"/>
            <s:hidden name='model.application_code' value='%{model.application_code}' />
            <s:hidden name='model.application_name' value='%{model.application_name}' />
            <s:hidden name="model.create_right" value="%{model.create_right}"/>
            <s:hidden name="model.retrieve_right" value="%{model.retrieve_right}"/>
            <s:hidden name="model.update_right" value="%{model.update_right}"/>
            <s:hidden name="model.delete_right" value="%{model.delete_right}"/>
            <s:hidden name="model.print_right" value="%{model.print_right}"/>
            <s:iterator value="model.rightsList" status="rightsStatus" var="appRights">
                <s:hidden name="model.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                <s:hidden name="model.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                <s:hidden name="model.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
            </s:iterator>
            
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group form-group-default viewOnly">
                        <label><s:text name="application.code"/></label>
                        ${model.application_code}
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group form-group-default viewOnly">
                        <label><s:text name="application.name"/></label>
                        ${model.application_name}
                    </div>
                </div>
            </div><br/>
            <div class="row">
                <div class="col-md-12 form-row-margin">
                    <%--s:submit cssClass="btn btn-default" theme="simple" action="loadAddGroupApplication" value="Add Group" />
                    <s:submit cssClass="btn btn-default" theme="simple" action="processDeleteGroupApplication" value="Delete Group"
                              onclick="if ( isCheckboxSelected(delGroup_ids)) {return confirmDelete();} else {return false;}" /--%>
                    <button class="btn btn-default" type="submit" name="action:loadAddGroupApplication" id="processAddRightsApplication"><i class="fa fa-plus"></i>Add Group</button>
                    <button class="btn btn-default" type="submit" name="action:processDeleteGroupApplication" id="processDeleteGroupApplication" onclick="if ( isCheckboxSelected(delGroup_ids)) {return confirmDelete();} else {return false;}"><i class="fa far fa-trash-alt"></i>Delete Group</button>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="table-responsive">
                        <table cellspacing="0" cellpadding="2" border="0" width="100%" class="table table-sds table-condensed table-striped table-hover">
                            <tr>
                                <th width="1%" align="center">
                                    <s:if test="model.groupAppList.size() > 0">
                                        <input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, delGroup_ids);">
                                    </s:if>
                                    <s:else>
                                        <input type="checkbox" id="cbselect" name="cbselect" disabled >
                                    </s:else>
                                </th>
                                <th width="100">Group Code</th>
                                <th width="200">Group Name</th>
                                <th width="60" class="text-center"><s:text name="right.create" /></th>
                                <th width="60" class="text-center"><s:text name="right.retrieve" /></th>
                                <th width="60" class="text-center"><s:text name="right.update" /></th>
                                <th width="60" class="text-center"><s:text name="right.delete" /></th>
                                <th width="60" class="text-center"><s:text name="right.print" /></th>
                            </tr>
                            <s:iterator value="model.groupAppList" status="groupAppStatus" var="groupAppItem">
                                <tr class="<s:if test="#groupAppStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                    <td align="center"><s:checkbox theme="simple" name="selected" id="delGroup_ids" fieldValue="%{#groupAppItem.ID}" onclick="checkToggleCheckbox(cbselect, delGroup_ids)"/></td>
                                    <%--<td align="center"><s:checkbox theme="simple" name="selected" id="delGroup_ids" fieldValue="%{#groupAppStatus.index}" onclick="checkToggleCheckbox(cbselect, delGroup_ids)"/></td>--%>
                                    <td width="100">
                                        <s:hidden name='model.groupAppList[%{#groupAppStatus.index}].ID' value='%{#groupAppItem.ID}' />
                                        <s:hidden name='model.groupAppList[%{#groupAppStatus.index}].group.ID' value='%{#groupAppItem.group.ID}' />
                                        <s:hidden name='model.groupAppList[%{#groupAppStatus.index}].group.group_code' value='%{#groupAppItem.group.group_code}' />
                                        <s:property value="%{#groupAppItem.group.group_code}" />
                                    </td>
                                    <td width="200">
                                        <s:hidden name='model.groupAppList[%{#groupAppStatus.index}].group.group_name' value='%{#groupAppItem.group.group_name}' />
                                        <s:property value='%{#groupAppItem.group.group_name}' />
                                    </td>
                                    <%int index = 0;%>
                                    <td align="center">
                                        <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].create_right" id="create_ids" value='%{#groupAppItem.create_right.equals("Y")?"True":"false"}'
                                                    fieldValue="Y" disabled="%{model.create_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                    </td>
                                    <td align="center">
                                        <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].retrieve_right" id="retrieve_ids" value='%{#groupAppItem.retrieve_right.equals("Y")?"True":"false"}'
                                                    fieldValue="Y" disabled="%{model.retrieve_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                    </td>
                                    <td align="center">
                                        <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].update_right" id="update_ids" value='%{#groupAppItem.update_right.equals("Y")?"True":"false"}'
                                                    fieldValue="Y" disabled="%{model.update_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                    </td>
                                    <td align="center">
                                        <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].delete_right" id="delete_ids" value='%{#groupAppItem.delete_right.equals("Y")?"True":"false"}'
                                                    fieldValue="Y" disabled="%{model.delete_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                    </td>
                                    <td align="center">
                                        <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].print_right" id="print_ids" value='%{#groupAppItem.print_right.equals("Y")?"True":"false"}'
                                                    fieldValue="Y" disabled="%{model.print_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                    </td>
                                </tr>
                                <s:iterator value="%{#groupAppItem.groupAppRights}" status="rightsStatus" var="appRights">
                                    <tr class="<s:if test="#groupAppStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                        <td colspan="3" align="right">
                                            <s:hidden name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_code" value="%{#appRights.applicationRights.app_rights_code}" />
                                            <s:hidden name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_description" value="%{#appRights.applicationRights.app_rights_description}" />
                                            <label title="${appRights.applicationRights.app_rights_code}"><s:property value="%{#appRights.applicationRights.app_rights_description}" /></label>:</td>
                                        <td align="center">
                                            <s:hidden name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                            <s:hidden theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].app_rights_id" value='%{#appRights.app_rights_id}' />
                                            <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].hasRight" id="rights_ids" value='%{#appRights.hasRight.equals("Y")?"True":"false"}' fieldValue="Y" />
                                        </td>
                                        <td colspan="4"></td>
                                    </tr>
                                </s:iterator>
                            </s:iterator>
                        </table>
                    </div>
                </div>
            </div><br/><br/>
            <div class="row">
                <%--<s:submit type="submit" cssClass="btn btn-primary" theme="simple" action="processUpdateGroupApplication" value="Save" onclick="return localValidateForm(this.form, 'update')"/>--%>
                <%--<s:submit type="submit" cssClass="btn btn-default" theme="simple" action="cancelAddToGroupApplication" value="Back"/>--%>
                <button class="btn btn-primary" type="submit" name="action:processUpdateGroupApplication" id="processUpdateGroupApplication" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i>Save</button>
                <button class="btn btn-default" type="submit" name="action:cancelAddToGroupApplication" id="cancelAddToGroupApplication"><i class="fa fa-arrow-left"></i>Back</button>
            </div>                

            <!--            <div class="titleFramework">
                            <span class="titleText"><s:text name="application" /></span>
                            <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span><br>
                        </div>
                        <div class="xbox">-->
            <%--div class="panel panel-default ">  
                <!--div class="panel-heading">
                   <h3 class="panel-title"> 
                       <span class="titleText"><s:text name="application" /></span>
                   <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span></h3>
                 </div-->
                <div class="panel-body">

                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <td align="right">
                                <s:submit type="submit" cssClass="defaultButton dynamic-pull mrg-lr-5" theme="simple" action="processUpdateGroupApplication" value="Save" onclick="return localValidateForm(this.form, 'update')"/>
                                <s:submit type="submit" cssClass="defaultButton dynamic-pull mrg-lr-5" theme="simple" action="cancelAddToGroupApplication" value="Back"/>
                            </td>
                        </tr>
                    </table>
                    <table class="form">

                        <s:iterator value="model.rightsList" status="rightsStatus" id="appRights">
                            <s:hidden name="model.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                            <s:hidden name="model.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                            <s:hidden name="model.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                        </s:iterator>
                        <tr align="left">
                            <td width="20px">&nbsp;</td>
                            <td><s:text name="application.code"/></td>
                            <td>:</td>
                            <td align="left">${model.application_code}</td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td><s:text name="application.name"/></td>
                            <td>:</td>
                            <td>${model.application_name}</td>
                        </tr>
                    </table>
                    <hr>
                    <table class="form table borderless">
                        <tr>
                            <td colspan="2">
                                <s:submit cssClass="defaultButton" theme="simple" action="loadAddGroupApplication" value="Add Group" />
                                <s:submit cssClass="defaultButton" theme="simple" action="processDeleteGroupApplication" value="Delete Group"
                                          onclick="if ( isCheckboxSelected(delGroup_ids)) {return confirmDelete();} else {return false;}" />
                            </td>
                        </tr>
                    </table>
                    <hr>
                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <th width="1%" align="center">
                                <s:if test="model.groupAppList.size() > 0">
                                    <input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, delGroup_ids);">
                                </s:if>
                                <s:else>
                                    <input type="checkbox" id="cbselect" name="cbselect" disabled >
                                </s:else>
                            </th>
                            <th width="100">Group Code</th>
                            <th width="200">Group Name</th>
                            <th width="60"><s:text name="right.create" /></th>
                            <th width="60"><s:text name="right.retrieve" /></th>
                            <th width="60"><s:text name="right.update" /></th>
                            <th width="60"><s:text name="right.delete" /></th>
                            <th width="60"><s:text name="right.print" /></th>
                        </tr>
                        <s:iterator value="model.groupAppList" status="groupAppStatus" id="groupAppItem">
                            <tr class="<s:if test="#groupAppStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                <td align="center"><s:checkbox theme="simple" name="selected" id="delGroup_ids" fieldValue="%{#groupAppItem.ID}" onclick="checkToggleCheckbox(cbselect, delGroup_ids)"/></td>
                                <!--<td align="center"><s:checkbox theme="simple" name="selected" id="delGroup_ids" fieldValue="%{#groupAppStatus.index}" onclick="checkToggleCheckbox(cbselect, delGroup_ids)"/></td>-->
                                <td width="100">
                                    <s:hidden name='model.groupAppList[%{#groupAppStatus.index}].ID' value='%{#groupAppItem.ID}' />
                                    <s:hidden name='model.groupAppList[%{#groupAppStatus.index}].group.ID' value='%{#groupAppItem.group.ID}' />
                                    <s:hidden name='model.groupAppList[%{#groupAppStatus.index}].group.group_code' value='%{#groupAppItem.group.group_code}' />
                                    <s:property value="%{#groupAppItem.group.group_code}" />
                                </td>
                                <td width="200">
                                    <s:hidden name='model.groupAppList[%{#groupAppStatus.index}].group.group_name' value='%{#groupAppItem.group.group_name}' />
                                    <s:property value='%{#groupAppItem.group.group_name}' />
                                </td>
                                <%int index = 0;%>
                                <td align="center">
                                    <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].create_right" id="create_ids" value='%{#groupAppItem.create_right.equals("Y")?"True":"false"}'
                                                fieldValue="Y" disabled="%{model.create_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                </td>
                                <td align="center">
                                    <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].retrieve_right" id="retrieve_ids" value='%{#groupAppItem.retrieve_right.equals("Y")?"True":"false"}'
                                                fieldValue="Y" disabled="%{model.retrieve_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                </td>
                                <td align="center">
                                    <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].update_right" id="update_ids" value='%{#groupAppItem.update_right.equals("Y")?"True":"false"}'
                                                fieldValue="Y" disabled="%{model.update_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                </td>
                                <td align="center">
                                    <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].delete_right" id="delete_ids" value='%{#groupAppItem.delete_right.equals("Y")?"True":"false"}'
                                                fieldValue="Y" disabled="%{model.delete_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                </td>
                                <td align="center">
                                    <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].print_right" id="print_ids" value='%{#groupAppItem.print_right.equals("Y")?"True":"false"}'
                                                fieldValue="Y" disabled="%{model.print_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                </td>
                            </tr>
                            <!--<s:if test="model.rightsList.size > 0">-->
                            <s:iterator value="%{#groupAppItem.groupAppRights}" status="rightsStatus" id="appRights">
                                <tr class="<s:if test="#groupAppStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                    <!--<s:set name="rightsName">${model.application_code}_${appRights.app_rights_code}</s:set>-->
                                    <!--<s:if test="#rightsStatus.index == 0">
                                        <td colspan="3" align="right">
                                            <s:hidden name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_code" value="%{#appRights.app_rights_code}" />
                                            <s:hidden name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_description" value="%{#appRights.app_rights_description}" />
                                            <label title="${appRights.app_rights_code}"><s:property value="%{#appRights.app_rights_description}" /></label>:</td>
                                    </s:if>
                                    <s:else>-->
                                    <td colspan="3" align="right">
                                        <s:hidden name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_code" value="%{#appRights.applicationRights.app_rights_code}" />
                                        <s:hidden name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].applicationRights.app_rights_description" value="%{#appRights.applicationRights.app_rights_description}" />
                                        <label title="${appRights.applicationRights.app_rights_code}"><s:property value="%{#appRights.applicationRights.app_rights_description}" /></label>:</td>
                                        <!--</s:else>-->
                                    <td align="center">
                                        <s:hidden name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                        <s:hidden theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].app_rights_id" value='%{#appRights.app_rights_id}' />
                                        <s:checkbox theme="simple" name="model.groupAppList[%{#groupAppStatus.index}].groupAppRights[%{#rightsStatus.index}].hasRight" id="rights_ids" value='%{#appRights.hasRight.equals("Y")?"True":"false"}' fieldValue="Y" />
                                    </td>
                                    <td colspan="4"></td>
                                </tr>
                            </s:iterator>
                            <!--</s:if>-->
                        </s:iterator>
                    </table>
                </div--%>
        </form>
    </body>
</html>