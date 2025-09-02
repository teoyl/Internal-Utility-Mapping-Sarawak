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
        <form theme="simple" action="processInsertUserGroup" name="userGroupForm" method="post">
            <s:hidden name="action" />
            <div class="card">
                <div class="card-header bg-light">              
                    <div class="row flex-between-center">                            
                        <div class="col-6 col-sm-auto d-flex align-items-center pe-0">
                            <h5><s:text name="userGroup"/>&nbsp;<small class="fw-normal text-600"><s:text name="button.add"/></small></h5>
                        </div>
                        <div class="col-6 col-sm-auto ms-auto text-end ps-0">                            
                            <button class="btn btn-sm btn-sm btn-primary" type="submit" name="action:processInsertUserGroup" id="processInsertUserGroup"  onclick="return validateForm(this.form, 'insert')"><i class="fa fa-save"></i> <span class="ms-1"><s:text name="button.save"/></span></button>        
                            <button class="btn btn-sm btn-sm btn-falcon-default" type="submit" name="action:cancelUserGroup" id="cancelUserGroup"><i class="fa fa-times"></i> <span class="ms-1"><s:text name="button.cancel"/></span></button>      
                        </div>
                    </div>
                </div>
                <div class="card-body">                    
                    <div class="row">
                        <div class="col-md-4">
                            <div class="mb-1">
                                <label class="form-label"><s:text name="group.code"/></label>
                                <s:textfield theme="simple" name="group_code" value="%{model.group_code}" size="40" cssClass="form-control form-control-sm"/>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="mb-1">
                                <label class="form-label"><s:text name="group.name"/></label>
                                <s:textfield theme="simple" name="group_name" value="%{model.group_name}" size="100" cssClass="form-control form-control-sm"/>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="mb-1">
                                <label class="form-label"><s:text name="group.type"/></label>
                                <s:select cssClass="form-control form-control-sm sds-dropdown" list="groupTypeOption" listKey="keyData" listValue="valueData" theme="simple" name="group_type" value="%{model.group_type}"/>
                            </div>
                        </div>
                    </div>

                    <div class="row mt-3">
                        <div class="col-md-12">
                            <h5><s:text name="userGroup.assignedApplication" /></h5>
                        </div>
                    </div>
                    <div class="row mt-1">
                        <div class="col-md-12 form-row-margin">
                            <s:if test="has_right('loadAddApplicationPage')">
                                <button class="btn btn-sm btn-falcon-default" type="submit" name="action:loadAddApplicationPageUserGroup" id="loadAddApplicationPageUserGroup"><i class="fa fa-plus"></i> <span class="ms-1"><s:text name="button.addApplication"/></span></button>
                            </s:if>
                            <s:if test="has_right('processDeleteApplication')">
                                <button class="btn btn-sm btn-falcon-default" type="submit" name="action:processDeleteApplicationUserGroup" id="processDeleteApplicationUserGroup" onclick="if (isCheckboxSelected(delApp_ids)) {
                                            return confirmDelete();
                                        } else {
                                            return false;
                                        }"><i class="fa fa-trash"></i> <span class="ms-1"><s:text name="button.deleteApplication"/></span></button>
                            </s:if>
                        </div>
                    </div>    

                    <s:if test="groupApplication.size() > 0">        
                        <div class="row mt-2">
                            <div class="col-md-12">
                                <div class="table-responsive">
                                    <table class="table table-sds table-sm fs--1 table-striped table-hover" cellspacing="1" cellpadding="1"  width="100%">
                                        <thead class="bg-200 text-900">
                                            <tr>
                                                <th style="width:1%;" class="white-space-nowrap">
                                                    <div class="form-check fs-0 d-flex align-items-center">
                                                        <s:if test="groupApplication.size() > 0">
                                                            <input type="checkbox"  id="cbselect" class="selectAll form-check-input" name="cbselect" onClick="toggleCheckboxByName(this,'selected');">
                                                        </s:if>
                                                        <s:else>
                                                            <input type="checkbox" id="cbselect" class="selectAll form-check-input" name="cbselect" disabled >
                                                        </s:else>
                                                        <label for="cbselect" class="tableListingCheckbox form-check-label"></label>
                                                    </div>
                                                </th>
                                                <th width="100" class="align-middle white-space-nowrap">Application Code</th>
                                                <th width="200" class="align-middle white-space-nowrap">Application Name</th>
                                                <th width="60" class="align-middle white-space-nowrap text-center"><s:text name="right.create" /></th>
                                                <th width="60" class="align-middle white-space-nowrap text-center"><s:text name="right.retrieve" /></th>
                                                <th width="60" class="align-middle white-space-nowrap text-center"><s:text name="right.update" /></th>
                                                <th width="60" class="align-middle white-space-nowrap text-center"><s:text name="right.delete" /></th>
                                                <th width="60" class="align-middle white-space-nowrap text-center"><s:text name="right.print" /></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <s:iterator value="model.groupApplication" status="appStatus" var="userGroupApp">
                                            <tr class="<s:if test="#appStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                <td align="center" class="align-middle">
                                                    <div class="form-check fs-0 mb-0">
                                                        <input type="checkbox" name="selected" class="checkbox_child form-check-input" id="${appStatus.index}" value="${appStatus.index}" onclick="toggleSelectAll()">
                                                        <label for="${appStatus.index}" class="tableListingCheckbox form-check-label"></label>
                                                    </div>
                                                </td>
                                                <td width="100" class="align-middle white-space-nowrap">
                                                    <s:hidden name='groupApplication[%{#appStatus.index}].ID' value='%{#userGroupApp.ID}' />
                                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.ID' value='%{application.ID}' />
                                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.application_code' value='%{application.application_code}' />
                                                    <s:property value="%{application.application_code}" />
                                                </td>
                                                <td width="200" class="align-middle white-space-nowrap">
                                                    <s:hidden name='groupApplication[%{#appStatus.index}].application.application_name' value='%{application.application_name}' />
                                                    <s:property value='%{application.application_name}' />
                                                </td>
                                                <%int index = 0;%>
                                                <td class="align-middle white-space-nowrap text-center">
                                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.create_right" value="%{application.create_right}"/>
                                                    <div class="form-check fs-0 mb-0">
                                                        <input type="checkbox" value="Y" name="groupApplication[<s:property value='%{#appStatus.index}'/>].create_right" class="checkbox_child form-check-input float-none" id="${appStatus.index}create_ids" <s:if test='#userGroupApp.create_right.equals("Y")'>checked</s:if> <s:if test='!application.create_right.equalsIgnoreCase("Y")'>disabled</s:if>>
                                                        <label for="${appStatus.index}create_ids" class="tableListingCheckbox form-check-label"></label>
                                                    </div>
                                                </td>
                                                <td class="align-middle white-space-nowrap text-center">
                                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.retrieve_right" value="%{application.retrieve_right}"/>
                                                    <div class="form-check fs-0 mb-0">
                                                        <input type="checkbox"  value="Y"  name="groupApplication[<s:property value='%{#appStatus.index}'/>].retrieve_right" class="checkbox_child form-check-input float-none" id="${appStatus.index}retrieve_ids" <s:if test='#userGroupApp.retrieve_right.equals("Y")'>checked</s:if> <s:if test='!application.retrieve_right.equalsIgnoreCase("Y")'>disabled</s:if>>
                                                        <label for="${appStatus.index}retrieve_ids" class="tableListingCheckbox form-check-label"></label>
                                                    </div>
                                                </td>
                                                <td class="align-middle white-space-nowrap text-center">
                                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.update_right" value="%{application.update_right}"/>
                                                    <div class="form-check fs-0 mb-0">
                                                        <input type="checkbox"  value="Y"  name="groupApplication[<s:property value='%{#appStatus.index}'/>].update_right" class="checkbox_child form-check-input float-none" id="${appStatus.index}update_ids" <s:if test='#userGroupApp.update_right.equals("Y")'>checked</s:if> <s:if test='!application.update_right.equalsIgnoreCase("Y")'>disabled</s:if>>
                                                        <label for="${appStatus.index}update_ids" class="tableListingCheckbox form-check-label"></label>
                                                    </div>
                                                </td>
                                                <td class="align-middle white-space-nowrap text-center">
                                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.delete_right" value="%{application.delete_right}"/>
                                                    <div class="form-check fs-0 mb-0">
                                                        <input type="checkbox"  value="Y"  name="groupApplication[<s:property value='%{#appStatus.index}'/>].delete_right" class="checkbox_child form-check-input float-none" id="${appStatus.index}delete_ids" <s:if test='#userGroupApp.delete_right.equals("Y")'>checked</s:if> <s:if test='!application.delete_right.equalsIgnoreCase("Y")'>disabled</s:if>>
                                                        <label for="${appStatus.index}delete_ids" class="tableListingCheckbox form-check-label"></label>
                                                    </div>
                                                </td>
                                                <td class="align-middle white-space-nowrap text-center">
                                                    <s:hidden name="groupApplication[%{#appStatus.index}].application.print_right" value="%{application.print_right}"/>
                                                    <div class="form-check fs-0 mb-0">
                                                        <input type="checkbox"  value="Y"  name="groupApplication[<s:property value='%{#appStatus.index}'/>].print_right" class="checkbox_child form-check-input float-none" id="${appStatus.index}print_ids" <s:if test='#userGroupApp.print_right.equals("Y")'>checked</s:if> <s:if test='!application.print_right.equalsIgnoreCase("Y")'>disabled</s:if>>
                                                        <label for="${appStatus.index}print_ids" class="tableListingCheckbox form-check-label"></label>
                                                    </div>
                                                </td>
                                            </tr>
                                            <s:if test="application.rightsList.size > 0">
                                                <s:iterator value="application.rightsList" status="rightsStatus" var="appRights">
                                                    <tr class="<s:if test="#appStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                                        <s:set var="rightsName">${application.application_code}_${appRights.app_rights_code}</s:set>
                                                        <s:if test="#rightsStatus.index == 0">
                                                            <td colspan="3" class="align-middle white-space-nowrap text-end">
                                                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                                                <label title="${appRights.app_rights_code}" class="mb-0"><s:property value="%{#appRights.app_rights_description}" /></label>:</td>
                                                            </s:if>
                                                            <s:else>
                                                            <td colspan="3" class="align-middle white-space-nowrap text-end">
                                                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_code" value="%{#appRights.app_rights_code}" />
                                                                <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].app_rights_description" value="%{#appRights.app_rights_description}" />
                                                                <label title="${appRights.app_rights_code}" class="mb-0"><s:property value="%{#appRights.app_rights_description}" /></label>:</td>
                                                            </s:else>
                                                        <td class="align-middle white-space-nowrap text-center">
                                                            <s:hidden name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].ID" value="%{#appRights.ID}" />
                                                            <s:hidden theme="simple" name="groupApplication[%{#appStatus.index}].application.rightsList[%{#rightsStatus.index}].gaRights_id" value='%{#appRights.gaRights_id}' />
                                                            <div class="form-check fs-0 mb-0">
                                                                <input type="checkbox"  value="Y"  name="groupApplication[<s:property value='%{#appStatus.index}'/>].application.rightsList[<s:property value='%{#rightsStatus.index}'/>].gaRightChecked" class="checkbox_child form-check-input float-none" id="rights_ids${appStatus.index}_${rightsStatus.index}" <s:if test='#appRights.gaRightChecked.equals("Y")'>checked</s:if>>
                                                                <label for="rights_ids${appStatus.index}_${rightsStatus.index}" class="tableListingCheckbox form-check-label"></label>
                                                            </div>
                                                        </td>
                                                        <td colspan="4" class="align-middle white-space-nowrap text-center"></td>
                                                    </tr>
                                                </s:iterator>
                                            </s:if>
                                        </s:iterator>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </s:if>
                </div>
            </div>
        </form>
        <br><br>
    </body>
</html>