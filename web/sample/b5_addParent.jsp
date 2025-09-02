<%-- 
    Document   : addParent
    Created on : Feb 11, 2011, 9:32:55 AM
    Author     : lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Parent - Add Page</title>
        <script type="text/javascript">
            $( document ).ready(function() {
                initDatePicker();
//                $('.dp_dob').datepicker({
//                    format: "dd/mm/yyyy",
//                    autoclose: true
//                });
//                $("[data-mask]").inputmask();
            });
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        <form id="addParentForm" action="loadAddPageParent" method="post">
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden name="action"/>
            <div class="card">
                <div class="card-header bg-light">
                    <h5>Parent <small class="fw-normal text-600">[Add]</small></h5>
                </div>
                <div class="card-body">
                    <div class="row mb-1">
                        <label class="col-md-3 col-form-label col-form-label-sm"><s:text name="Sample.parentName"/><font class="asterisk">*</font></label>
                        <div class="col-md-6">
                            <s:textfield theme="simple" name="parent_name" maxlength='%{model.columnLengthMap["parent_name"]}' cssClass="form-control form-control-sm" value="%{model.parent_name}"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-3 col-form-label col-form-label-sm"><s:text name="Sample.parentGender"/><font class="asterisk">*</font></label>
                        <div class="col-md-6">
                            <div class="form-check">
                                <s:radio list="genderList" listKey="keyData" listValue="valueData" theme="simple" name="parent_gender" value="%{model.parent_gender}"/>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-3 col-form-label col-form-label-sm"><s:text name="Sample.age"/><font class="asterisk">*</font></label>
                        <div class="col-md-6">
                            <s:textfield theme="simple" name="parent_age" cssClass="form-control form-control-sm" value="%{model.parent_age}"/>
                        </div>
                    </div>
                    <div class="row mb-1">
                        <label class="col-md-3 col-form-label col-form-label-sm">State<font class="asterisk">*</font></label>
                        <div class="col-md-6">
                            <s:select list="stateList" listKey="keyData" listValue="valueData" name="parent_state" theme="simple" value="%{model.parent_state}" cssClass="form-control form-control-sm sds-dropdown"/>
                        </div>
                    </div>
                    <div class="row mt-3 pt-3">
                        <div class="col-md-12">
                            <h5>Children</h5>
                            <s:if test="has_right('processAddChild')">
                                <button class="btn btn-sm btn-falcon-default" type="submit" name="action:processAddChildParent" id="processAddChildParent" onclick="this.form.submit()"><i class="fa fa-plus"></i> <span class="ms-1">Add Child</span></button>
                            </s:if>
                            <s:if test="has_right('processDeleteChild')">
                                <button class="btn btn-sm btn-falcon-default" type="submit" name="action:processDeleteChildParent" id="processDeleteChildParent" onclick="if (isCheckboxSelected(form.child_selected)) {
                                            return confirmPermanentDelete();
                                        } else {
                                            return false;
                                        }"><i class="fa fa-trash"></i> <span class="ms-1">Delete</span></button>
                            </s:if>
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col-md-12">
                            <div class="table-responsive">
                                <table width="100%" class="table table-sds table-sm fs--1 table-striped table-hover">
                                    <thead class="bg-200 text-900">
                                        <tr>
                                            <th style="width:1%;" class="align-middle white-space-nowrap">
                                                <div class="form-check fs-0 mb-0">
                                                    <s:if test="model.childList.size() > 0">
                                                        <input type="checkbox"  id="child_select" class="selectAll form-check-input" name="child_select" onClick="toggleCheckboxByName(this,'child_selected');">
                                                    </s:if>
                                                    <s:else>
                                                        <input type="checkbox" id="child_select" class="selectAll form-check-input" name="child_select" disabled >
                                                    </s:else>
                                                     <label for="child_select" class="tableFormCheckbox form-check-label"></label>
                                                </div>
                                            </th>
                                            <th width="460px" class="align-middle white-space-nowrap"><s:text name="Sample.name" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                            <th width="310px" class="align-middle white-space-nowrap"><s:text name="Sample.DOB" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                            <th width="140px" class="align-middle white-space-nowrap"><s:text name="Sample.age" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <s:iterator value="model.childList" status="childStatus" var="child">
                                            <tr>
                                                <td class="align-middle white-space-nowrap">
                                                    <div class="form-check fs-0 mb-0">
                                                        <input type="checkbox" name="child_selected" class="checkbox_child form-check-input" id="${childStatus.index}" value="${childStatus.index}" onclick="toggleSelectAll()">
                                                        <label for="${childStatus.index}" class="tableFormCheckbox form-check-label"></label>
                                                    </div>
                                                </td>
                                                <td class="align-middle white-space-nowrap"><s:textfield cssClass="form-control form-control-sm" theme="simple" name="childList[%{#childStatus.index}].child_name" value="%{#child.child_name}"/></td>
                                                <td class="align-middle white-space-nowrap">
                                                    <div class="col-md-12">
                                                        <div class="input-group input-group-sm datepicker">
                                                            <div class="input-group-text input-button" data-toggle>
                                                                <i class="far fa-calendar-alt"></i>
                                                            </div>
                                                            <input type="text" class="form-control form-control-sm" name="childList[${childStatus.index}].child_dob_str" value="${child.child_dob_str}" data-input/>
                                                        </div>
                                                    </div>

                                                </td>
                                                <td class="align-middle white-space-nowrap">
                                                    <%--<s:textfield cssClass="form-control form-control-sm" theme="simple" name="childList[%{#childStatus.index}].child_age" value="%{#child.child_age}" data-inputmask='"mask": "999"' data-mask=""/>--%>
                                                    <s:textfield maxlength="3" cssClass="form-control form-control-sm" theme="simple" name="childList[%{#childStatus.index}].child_age" value="%{#child.child_age}"/>
                                                </td>
                                            </tr>
                                        </s:iterator>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 text-end">
                            <button class="btn btn-sm btn-primary" type="submit" name="action:processInsertParent" id="processInsertApplication" onclick="return validateForm(this.form, 'insert')"><i class="fa fa-save"></i> <span class="ms-1">Save</span></button>        
                            <button class="btn btn-sm btn-falcon-default" type="submit" name="action:cancelParent" id="cancelApplication"><i class="fa fa-times"></i> <span class="ms-1">Cancel</span></button>        
                        </div>
                    </div>                                    
                </div>
            </div>
        </form>
    </body>
</html>
