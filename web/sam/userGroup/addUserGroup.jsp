<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
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
                if($('.dept').val() == "GOV-SD-114"){ //if dept=L&S, mandatory for division 03-10-2018
                    this.ac = new Array("group_division", "<s:text name='common.division'/>");
                }
//                this.ad = new Array("dept_id", "<s:text name="group.dept"/>");
            }
            
            function div(){
                if($('.dept').val() == "GOV-SD-114"){
                    $('.division').addClass("required");
                }else{
                    $('.division').removeClass("required");
                    $('.group_division').val('').trigger('change');
                }
            }
            
            $(document).ready(function() {
                //onload
                div();
                
                //onchange
                $(".dept").change(function() {
                    div();
                });
            });
        </SCRIPT>
        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <%--div class="row">
            <div class="col-md-4">
                <h3 class="title-v3"><s:text name="userGroup" /> <small><s:text name="actionType.add" /></small></h3>
            </div>
        </div--%>

        <form theme="simple" action="processInsertUserGroup" name="userGroupForm" method="post">
            <s:hidden name="action" />
            <div class="row form-row-margin">
                <div class="col-md-12 text-right">
                    <button class="btn btn-primary" type="submit" name="action:processInsertUserGroup" id="processInsertUserGroup"  onclick="return validateForm(this.form, 'insert')"><i class="fa fa-save"></i><s:text name="button.save"/></button>        
                    <button class="btn btn-default" type="submit" name="action:cancelUserGroup" id="cancelUserGroup"><i class="fa fa-close"></i><s:text name="button.cancel"/></button>        
                    <%--s:submit theme="simple" cssClass="defaultButton buttonSave" action="processInsertUserGroup" value="Save" onclick="return validateForm(this.form, 'insert')"/>
                    <s:submit theme="simple" cssClass="defaultButton buttonCancel" action="cancelUserGroup" value="Cancel"/--%>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group">
                        <label><s:text name="group.code"/></label>
                        <s:textfield theme="simple" name="group_code" value="%{model.group_code}" size="40" cssClass="form-control"/>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group">
                        <label><s:text name="group.name"/></label>
                        <s:textfield theme="simple" name="group_name" value="%{model.group_name}" size="100" cssClass="form-control"/>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group form-group-select">
                        <label><s:text name="group.type"/></label>
                        <s:select cssClass="form-control sds-dropdown" list="groupTypeOption" listKey="keyData" listValue="valueData" theme="simple" name="group_type" value="%{model.group_type}"/>
                    </div>
                </div>
            </div>
           

            <div class="row">
                <div class="col-md-12">
                    <h3 class="title-v3"><s:text name="userGroup.assignedApplication" /></h3>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 form-row-margin">
                    <s:if test="has_right('loadAddApplicationPage')">
                        <button class="btn btn-default" type="submit" name="action:loadAddApplicationPageUserGroup" id="loadAddApplicationPageUserGroup"><i class="fa fa-plus"></i><s:text name="button.addApplication"/></button>
                    </s:if>
                    <s:if test="has_right('processDeleteApplication')">
                        <button class="btn btn-default" type="submit" name="action:processDeleteApplicationUserGroup" id="processDeleteApplicationUserGroup" onclick="if (isCheckboxSelected(delApp_ids)) {
                                    return confirmDelete();
                                } else {
                                    return false;
                                }"><i class="fa fa-trash"></i><s:text name="button.deleteApplication"/></button>
                    </s:if>
                    <%--s:if test="has_right('loadAddApplicationPage')">
                        <s:submit cssClass="defaultButton" theme="simple" action="loadAddApplicationPageUserGroup" value="Add Application" />
                    </s:if>
                    <s:if test="has_right('processDeleteApplication')">
                        <s:submit cssClass="defaultButton" theme="simple" action="processDeleteApplicationUserGroup" value="Delete Application"
                                  onclick="if ( isCheckboxSelected(delApp_ids)) {return confirmDelete();} else {return false;}" />
                    </s:if--%> 
                </div>
            </div>    

            <s:if test="groupApplication.size() > 0">        
                <div class="row">
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <table class="table table-sds table-condensed table-striped table-hover" cellspacing="1" cellpadding="1"  width="100%">
                                <tr>
                                    <th width="1%" align="center">
                                        <div class="checkbox check-success tableListingCheckbox">
                                            <s:if test="groupApplication.size() > 0">
                                                <input type="checkbox"  id="cbselect" class="selectAll" name="cbselect" onClick="toggleCheckboxByName(this,'selected');">
                                                <!--<input type="checkbox" id="rights_select" name="rights_select" onclick="toggleCheckbox(this, delRights_ids);">-->
                                            </s:if>
                                            <s:else>
                                                <input type="checkbox" id="cbselect" class="selectAll" name="cbselect" disabled >
                                            </s:else>
                                            <label for="cbselect" class="tableListingCheckbox"></label>
                                        </div>
                                        <%--<s:if test="groupApplication.size() > 0">
                                            <input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, delApp_ids);">
                                        </s:if>
                                        <s:else>
                                            <input type="checkbox" id="cbselect" name="cbselect" disabled >
                                        </s:else>--%>
                                    </th>
                                    <th width="100">Application Code</th>
                                    <th width="200">Application Name</th>
                                    <th width="60" class="text-center"><s:text name="right.create" /></th>
                                    <th width="60" class="text-center"><s:text name="right.retrieve" /></th>
                                    <th width="60" class="text-center"><s:text name="right.update" /></th>
                                    <th width="60" class="text-center"><s:text name="right.delete" /></th>
                                    <th width="60" class="text-center"><s:text name="right.print" /></th>
                                </tr>
                                <s:iterator value="model.groupApplication" status="appStatus" var="userGroupApp">
                                    <tr class="<s:if test="#appStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                        <td align="center">
                                            <div class="checkbox check-success tableListingCheckbox">
                                                <input type="checkbox" name="selected" class="checkbox_child" id="${appStatus.index}" value="${appStatus.index}" onclick="toggleSelectAll()">
                                                <label for="${appStatus.index}" class="tableListingCheckbox"></label>
                                            </div>
                                            <%--<s:checkbox theme="simple" name="selected" id="delApp_ids" fieldValue="%{#appStatus.index}" onclick="checkToggleCheckbox(cbselect, delApp_ids)"/>--%>
                                        </td>
                                        <td width="100">
                                            <s:hidden name='groupApplication[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                                            <s:hidden name='groupApplication[%{#appStatus.index}].application.ID' value='%{application.ID}' />
                                            <s:hidden name='groupApplication[%{#appStatus.index}].application.application_code' value='%{application.application_code}' />
                                            <s:property value="%{application.application_code}" />
                                        </td>
                                        <td width="200">
                                            <s:hidden name='groupApplication[%{#appStatus.index}].application.application_name' value='%{application.application_name}' />
                                            <s:property value='%{application.application_name}' />
                                        </td>
                                        <%int index = 0;%>
                                        <td align="center">
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.create_right" value="%{application.create_right}"/>
                                          <%--  <s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].create_right" id="create_ids" value='%{create_right.equals("Y")?"True":"false"}'
                                                        fieldValue="Y" disabled="%{application.create_right.equalsIgnoreCase('Y')?'false':'true'}" />--%>
                                            <div class="checkbox check-success tableListingCheckbox">
                                                <input type="checkbox" value="Y" name="groupApplication[<s:property value='%{#appStatus.index}'/>].create_right" class="checkbox_child" id="${appStatus.index}create_ids" <s:if test='#userGroupApp.create_right.equals("Y")'>checked</s:if> <s:if test='!application.create_right.equalsIgnoreCase("Y")'>disabled</s:if>>
                                                <label for="${appStatus.index}create_ids" class="tableListingCheckbox"></label>
                                            </div>
                                        </td>
                                        <td align="center">
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.retrieve_right" value="%{application.retrieve_right}"/>
                                           <%-- <s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{retrieve_right.equals("Y")?"True":"false"}'
                                                        fieldValue="Y" disabled="%{application.retrieve_right.equalsIgnoreCase('Y')?'false':'true'}" />--%>
                                            <div class="checkbox check-success tableListingCheckbox">
                                                <input type="checkbox"  value="Y"  name="groupApplication[<s:property value='%{#appStatus.index}'/>].retrieve_right" class="checkbox_child" id="${appStatus.index}retrieve_ids" <s:if test='#userGroupApp.retrieve_right.equals("Y")'>checked</s:if> <s:if test='!application.retrieve_right.equalsIgnoreCase("Y")'>disabled</s:if>>
                                                <label for="${appStatus.index}retrieve_ids" class="tableListingCheckbox"></label>
                                            </div>
                                        </td>
                                        <td align="center">
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.update_right" value="%{application.update_right}"/>
                                            <%--<s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].update_right" id="update_ids" value='%{update_right.equals("Y")?"True":"false"}'
                                                        fieldValue="Y" disabled="%{application.update_right.equalsIgnoreCase('Y')?'false':'true'}" />--%>
                                            <div class="checkbox check-success tableListingCheckbox">
                                                <input type="checkbox"  value="Y"  name="groupApplication[<s:property value='%{#appStatus.index}'/>].update_right" class="checkbox_child" id="${appStatus.index}update_ids" <s:if test='#userGroupApp.update_right.equals("Y")'>checked</s:if> <s:if test='!application.update_right.equalsIgnoreCase("Y")'>disabled</s:if>>
                                                <label for="${appStatus.index}update_ids" class="tableListingCheckbox"></label>
                                            </div>
                                        </td>
                                        <td align="center">
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.delete_right" value="%{application.delete_right}"/>
                                            <%--<s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].delete_right" id="delete_ids" value='%{delete_right.equals("Y")?"True":"false"}'
                                                        fieldValue="Y" disabled="%{application.delete_right.equalsIgnoreCase('Y')?'false':'true'}" />--%>
                                            <div class="checkbox check-success tableListingCheckbox">
                                                <input type="checkbox"  value="Y"  name="groupApplication[<s:property value='%{#appStatus.index}'/>].delete_right" class="checkbox_child" id="${appStatus.index}delete_ids" <s:if test='#userGroupApp.delete_right.equals("Y")'>checked</s:if> <s:if test='!application.delete_right.equalsIgnoreCase("Y")'>disabled</s:if>>
                                                <label for="${appStatus.index}delete_ids" class="tableListingCheckbox"></label>
                                            </div>
                                        </td>
                                        <td align="center">
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.print_right" value="%{application.print_right}"/>
                                            <%--<s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].print_right" id="print_ids" value='%{print_right.equals("Y")?"True":"false"}'
                                                        fieldValue="Y" disabled="%{application.print_right.equalsIgnoreCase('Y')?'false':'true'}" />--%>
                                            <div class="checkbox check-success tableListingCheckbox">
                                                <input type="checkbox"  value="Y"  name="groupApplication[<s:property value='%{#appStatus.index}'/>].print_right" class="checkbox_child" id="${appStatus.index}print_ids" <s:if test='#userGroupApp.print_right.equals("Y")'>checked</s:if> <s:if test='!application.print_right.equalsIgnoreCase("Y")'>disabled</s:if>>
                                                <label for="${appStatus.index}print_ids" class="tableListingCheckbox"></label>
                                            </div>
                                        </td>
                                    </tr>
                                    <s:if test="application.rightsList.size > 0">
                                        <s:iterator value="application.rightsList" status="rightsStatus" var="appRights">
                                            <tr class="<s:if test="#appStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                <s:set var="rightsName">${application.application_code}_${appRights.app_rights_code}</s:set>
                                                <s:if test="#rightsStatus.index == 0">
                                                    <td colspan="3" align="right">
                                                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                                        <label title="${appRights.app_rights_code}"><s:property value="%{#appRights.app_rights_description}" /></label>:</td>
                                                    </s:if>
                                                    <s:else>
                                                    <td colspan="3" align="right">
                                                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                                        <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                                        <label title="${appRights.app_rights_code}"><s:property value="%{#appRights.app_rights_description}" /></label>:</td>
                                                    </s:else>
                                                <td align="center">
                                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                                    <s:hidden theme="simple" name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" value='%{#appRights.gaRights_id}' />
                                                    <%--<s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRightChecked" id="rights_ids" value='%{#appRights.gaRightChecked.equals("Y")?"True":"false"}' fieldValue="Y" />--%>
                                                    <div class="checkbox check-success tableListingCheckbox">
                                                        <input type="checkbox"  value="Y"  name="groupApplication[<s:property value='%{#appStatus.index}'/>].application.rightsList[<s:property value='%{#rightsStatus.index}'/>].gaRightChecked" class="checkbox_child" id="rights_ids${appStatus.index}_${rightsStatus.index}" <s:if test='#appRights.gaRightChecked.equals("Y")'>checked</s:if>>
                                                        <label for="rights_ids${appStatus.index}_${rightsStatus.index}" class="tableListingCheckbox"></label>
                                                    </div>
                                                </td>
                                                <td colspan="4"></td>
                                            </tr>
                                        </s:iterator>
                                    </s:if>
                                </s:iterator>
                            </table>
                        </div>
                    </div>
                </div>
            </s:if>



            <%--div class="panel panel-default ">  
                <div class="panel-heading">
                    <h3 class="panel-title"> 
                        <span class="titleText"><s:text name="userGroup" /></span>
                        <span class="titleActionTypeText"> | <s:text name="actionType.add" /></span></h3>
                </div>
                <div class="panel-body">
                    <div class="dynamic-pull">
                        <table cellspacing="0" cellpadding="2" border="0" class="form table borderless">
                            <tr>
                                <td >
                                    <s:submit theme="simple" cssClass="defaultButton buttonSave" action="processInsertUserGroup" value="Save" onclick="return validateForm(this.form, 'insert')"/>
                                    <s:submit theme="simple" cssClass="defaultButton buttonCancel" action="cancelUserGroup" value="Cancel"/>
                                </td>
                            </tr>
                        </table>
                    </div>           
                    <table class="form table borderless">
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="100px" align=left><s:text name="group.code"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" name="group_code" value="%{model.group_code}" size="40" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td  align=left><s:text name="group.name"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td>:</td>
                            <td><s:textfield theme="simple" name="group_name" value="%{model.group_name}" size="100" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td  align=left><s:text name="group.type"/></td>
                            <td>:</td>
                            <td><s:select list="groupTypeOption" listKey="keyData" listValue="valueData" theme="simple" name="group_type" value="%{model.group_type}" cssClass="input-md form-control" /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td  align=left><s:text name="group.dept"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td>:</td>
                            <td><s:textfield theme="simple" name="dept_id" value="%{model.dept_id}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td  align=left><s:text name="group.system"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td>:</td>
                            <td><s:textfield theme="simple" name="system_id" value="%{model.system_id}" cssClass="input-md form-control"/></td>
                        </tr>
                    </table>
                    <hr>
                    <table width="100%" class="form">
                        <tr class="CLASS_TABLE_HEADER" >
                            <td class="header_2"><span class="imgArrowRight" /><span class="header_2Text"><s:text name="userGroup.assignedApplication" /></span></td>
                        </tr>
                    </table>
                    <table  class="form table borderless">
                        <tr>
                            <td colspan="2">
                                <s:if test="has_right('loadAddApplicationPage')">
                                    <s:submit cssClass="defaultButton" theme="simple" action="loadAddApplicationPageUserGroup" value="Add Application" />
                                </s:if>
                                <s:if test="has_right('processDeleteApplication')">
                                    <s:submit cssClass="defaultButton" theme="simple" action="processDeleteApplicationUserGroup" value="Delete Application"
                                              onclick="if ( isCheckboxSelected(delApp_ids)) {return confirmDelete();} else {return false;}" />
                                </s:if>
                            </td>
                        </tr>
                    </table>
                    <table class="defaultTable table borderless" cellspacing="1" cellpadding="1"  width="100%">
                        <tr>
                            <th width="1%" align="center">
                                <s:if test="groupApplication.size() > 0">
                                    <input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, delApp_ids);">
                                </s:if>
                                <s:else>
                                    <input type="checkbox" id="cbselect" name="cbselect" disabled >
                                </s:else>
                            </th>
                            <th width="100">Application Code</th>
                            <th width="200">Application Name</th>
                            <th width="60"><s:text name="right.create" /></th>
                            <th width="60"><s:text name="right.retrieve" /></th>
                            <th width="60"><s:text name="right.update" /></th>
                            <th width="60"><s:text name="right.delete" /></th>
                            <th width="60"><s:text name="right.print" /></th>
                        </tr>
                        <s:iterator value="model.groupApplication" status="appStatus" id="userGroupApp">
                            <tr class="<s:if test="#appStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                <td align="center"><s:checkbox theme="simple" name="selected" id="delApp_ids" fieldValue="%{#appStatus.index}" onclick="checkToggleCheckbox(cbselect, delApp_ids)"/></td>
                                <td width="100">
                                    <s:hidden name='groupApplication[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.ID' value='%{application.ID}' />
                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.application_code' value='%{application.application_code}' />
                                    <s:property value="%{application.application_code}" />
                                </td>
                                <td width="200">
                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.application_name' value='%{application.application_name}' />
                                    <s:property value='%{application.application_name}' />
                                </td>
                                <%int index = 0;%>
                                <td align="center">
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.create_right" value="%{application.create_right}"/>
                                    <s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].create_right" id="create_ids" value='%{create_right.equals("Y")?"True":"false"}'
                                                fieldValue="Y" disabled="%{application.create_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                </td>
                                <td align="center">
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.retrieve_right" value="%{application.retrieve_right}"/>
                                    <s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].retrieve_right" id="retrieve_ids" value='%{retrieve_right.equals("Y")?"True":"false"}'
                                                fieldValue="Y" disabled="%{application.retrieve_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                </td>
                                <td align="center">
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.update_right" value="%{application.update_right}"/>
                                    <s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].update_right" id="update_ids" value='%{update_right.equals("Y")?"True":"false"}'
                                                fieldValue="Y" disabled="%{application.update_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                </td>
                                <td align="center">
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.delete_right" value="%{application.delete_right}"/>
                                    <s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].delete_right" id="delete_ids" value='%{delete_right.equals("Y")?"True":"false"}'
                                                fieldValue="Y" disabled="%{application.delete_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                </td>
                                <td align="center">
                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.print_right" value="%{application.print_right}"/>
                                    <s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].print_right" id="print_ids" value='%{print_right.equals("Y")?"True":"false"}'
                                                fieldValue="Y" disabled="%{application.print_right.equalsIgnoreCase('Y')?'false':'true'}" />
                                </td>
                            </tr>
                            <s:if test="application.rightsList.size > 0">
                                <s:iterator value="application.rightsList" status="rightsStatus" id="appRights">
                                    <tr class="<s:if test="#appStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                        <s:set var="rightsName">${application.application_code}_${appRights.app_rights_code}</s:set>
                                        <s:if test="#rightsStatus.index == 0">
                                            <td colspan="3" align="right">
                                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                                <label title="${appRights.app_rights_code}"><s:property value="%{#appRights.app_rights_description}" /></label>:</td>
                                            </s:if>
                                            <s:else>
                                            <td colspan="3" align="right">
                                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                                <label title="${appRights.app_rights_code}"><s:property value="%{#appRights.app_rights_description}" /></label>:</td>
                                            </s:else>
                                        <td align="center">
                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                            <s:hidden theme="simple" name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" value='%{#appRights.gaRights_id}' />
                                            <s:checkbox theme="simple" name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRightChecked" id="rights_ids" value='%{#appRights.gaRightChecked.equals("Y")?"True":"false"}' fieldValue="Y" />
                                        </td>
                                        <td colspan="4"></td>
                                    </tr>
                                </s:iterator>
                            </s:if>
                        </s:iterator>
                    </table>
                </div--%>
        </form>
        <br><br>
    </body>
</html>