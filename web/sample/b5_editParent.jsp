<%-- 
    Document   : editParent
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
        <title>Parent - Edit Page</title>
        <script type="text/javascript">
            $( document ).ready(function() {
                initDatePicker();
//                $('.dp_dob').datepicker({
//                    format: "dd/mm/yyyy",
//                    autoclose: true
//                });
//                $("[data-mask]").inputmask();
            });
            function addGc(childIndex) { console.log('addGc');
                $("#childIndex").val(childIndex);
                console.log('1');
                submitForm2("processAddGrandChildParent");
                console.log('2');
            }
            $('.addChildBtn').on('click', function(e) {
                submitForm2("processAddChildParent");
            });
            
            function submitForm2(action) { console.log('submitForm2');
                var $form = $("#editParentForm");
                $form.attr("action", action);
                $form.submit();
                $('#confirmDiv2').modal('hide');
            }
            function localValidateForm(form, operation) {
                var errors = new Array();

//                if (!validateForm_bshor("editParentForm")) {
//                    errors[errors.length] = "Fields marked as red are required.";
//                }

                if (errors.length > 0) {
                        alert(errors.join('\n'));
                        setFocus(form);
                } else {
                    submitForm2("processUpdateParent");
                }
            }

        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form id="editParentForm" action="loadEditPageParent" method="post">
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden name="action"/>
            <s:hidden name="childIndex" id="childIndex"/>
            <s:hidden name="model.ID"/>
            <s:hidden name="arrDelete.childDeleted"/>
            <div class="card">
                <div class="card-header bg-light">
                    <h5>Parent <small class="fw-normal text-600">[Edit]</small></h5>
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
                        <div class="col-md-6 radio radio-inline radio-success">
                            <s:radio list="commList.genderOptions" listKey="keyData" listValue="valueData" theme="simple" name="parent_gender" value="%{model.parent_gender}"/>
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
                                <button class="btn btn-sm btn-falcon-default addChildBtn" type="submit" name="action:processAddChildParent" id="processAddChildParent"><i class="fa fa-plus"></i> <span class="ms-1">Add Child</span></button>
                            </s:if>
                            <s:if test="has_right('processDeleteChild')">
                                <button class="btn btn-sm btn-falcon-default" type="button" name="action:processDeleteChildParent" id="processDeleteChildParent" onclick="if (isCheckboxSelected_byClass('allcb')) {
                                            if (confirmPermanentDelete()) {submitForm2('processDeleteChildParent')}
                                        } else {
                                            return false;
                                        }"><i class="fa fa-trash"></i> <span class="ms-1">Delete</span></button>
                            </s:if>
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col-md-12">
                            <div class="table-responsive">
                                <table class="table table-sds table-sm fs--1 table-hover">
                                    <thead class="bg-200 text-900">
                                        <tr>
                                            <th style="width:1%" colspan="2" class="align-middle white-space-nowrap">
                                                <div class="form-check fs-0 mb-0">
                                                    <s:if test="model.childList.size() > 0">
                                                        <input type="checkbox"  id="child_select" class="selectAll form-check-input" name="child_select" onClick="toggleCheckboxByName(this,'arrSelect.childSelected');">
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
                                                        <s:hidden theme="simple" name="childList[%{#childStatus.index}].ID" value="%{#child.ID}" />
                                                        <s:hidden theme="simple" name="childList[%{#childStatus.index}].parent_id" value="%{#child.parent_id}" />
                                                        <s:hidden theme="simple" name="childList[%{#childStatus.index}].arrDelete.gcDeleted" value="%{#child.arrDelete.gcDeleted}" />
                                                        <input type="checkbox" name="arrSelect.childSelected" class="checkbox_child allcb form-check-input" id="${childStatus.index}" value="${childStatus.index}" onclick="toggleSelectAll()">
                                                        <label for="${childStatus.index}" class="tableFormCheckbox form-check-label"></label>
                                                    </div>
                                                </td>
                                                <td class="align-middle white-space-nowrap">
                                                    <button type="button" class="btn btn-sm btn-icon-only btn-falcon-default addGrandChildBtn" name="action:processAddGrandChildParent" onclick="addGc(${childStatus.index}); return true;">
                                                        <span class="fa fa-list-ul"></span>
                                                    </button>
                                                </td>
                                                <td class="align-middle white-space-nowrap"><s:textfield cssClass="form-control form-control-sm" theme="simple" name="childList[%{#childStatus.index}].child_name" value="%{#child.child_name}" required=""/></td>
                                                <td class="align-middle white-space-nowrap">
                                                    <div class="col-md-12">
                                                        <div class="input-group input-group-sm datepicker">
                                                            <div class="input-group-text input-button" data-toggle>
                                                                <i class="fa fa-calendar"></i>
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
                                            <s:if test="#child.grandChildList.size() > 0">
                                                <tr>
                                                <td class="align-middle white-space-nowrap"></td>
                                                <td colspan="4" class="align-middle white-space-nowrap">
                                                    <table width="100%" class="table table-sds table-sm table-hover">
                                                    <%--<tr>
                                                            <td colspan="2">
                                                                <s:if test="has_right('processAddGrandChild')">
                                                                    <button class="btn btn-sm btn-falcon-default" type="submit" name="action:processAddGrandChildParent" id="processAddGrandChildParent"><i class="fa fa-plus"></i>Add</button>
                                                                </s:if>
                                                                <s:if test="has_right('processDeleteGrandChild')">
                                                                    <button class="btn btn-sm btn-falcon-default" type="submit" name="action:processDeleteGrandChildParent" id="processDeleteGrandChildParent" onclick="if (isCheckboxSelected(form['child.arrSelect.gcSelected'])) {
                                                                                return confirmPermanentDelete();
                                                                            } else {
                                                                                return false;
                                                                            }"><i class="fa fa-trash"></i>Delete</button>
                                                                </s:if>
                                                            </td>
                                                        </tr>    --%>
                                                        <tr class="bg-light">
                                                            <th style="width:1%;" style="border-top: 0;" class="align-middle white-space-nowrap">
                                                                <div class="form-check fs-0 mb-0">
                                                                    <s:if test="#child.grandChildList.size() > 0">
                                                                        <input type="checkbox" id="child${childStatus.index}_select" class="all_child_${childStatus.index} form-check-input" name="gc_select" onClick="toggleCheckboxByClassName(this,'child_${childStatus.index}');">
                                                                    </s:if>
                                                                    <s:else>
                                                                        <input type="checkbox" id="child${childStatus.index}_select" class="all_child_${childStatus.index} form-check-input" name="gc_select" disabled >
                                                                    </s:else>
                                                                     <label for="child${childStatus.index}_select" class="tableFormCheckbox form-check-label"></label>
                                                                </div>
                                                            </th>
                                                            <th style="border-top: 0;" class="align-middle white-space-nowrap">
                                                                Grand Child Name
                                                            </th>
                                                        </tr>
                                                    <s:iterator value="#child.grandChildList" status="gcStatus" var="grandChild">
                                                        <tr>
                                                            <td class="align-middle white-space-nowrap">
                                                                <div class="form-check fs-0 mb-0">
                                                                    <s:hidden theme="simple" name="model.childList[%{#childStatus.index}].grandChildList[%{#gcStatus.index}].ID" />
                                                                    <s:hidden theme="simple" name="model.childList[%{#childStatus.index}].grandChildList[%{#gcStatus.index}].child_id" value="%{#grandChild.child_id}" />
                                                                    <input type="checkbox" name="model.childList[${childStatus.index}].arrSelect.gcSelected" class="child_${childStatus.index} allcb form-check-input" id="child${childStatus.index}gc${gcStatus.index}" value="${gcStatus.index}" onclick="toggleSelectAll_ml('all_child_${childStatus.index}', 'child_${childStatus.index}')">
                                                                    <label for="child${childStatus.index}gc${gcStatus.index}" class="tableFormCheckbox form-check-label"></label>
                                                                </div>
                                                            </td>
                                                            <td class="align-middle white-space-nowrap">
                                                                <s:textfield cssClass="form-control form-control-sm" theme="simple" name="model.childList[%{#childStatus.index}].grandChildList[%{#gcStatus.index}].gc_name" value="%{#grandChild.gc_name}" required=""/>
                                                            </td>
                                                        </tr>
                                                    </s:iterator>
                                                    </table>
                                                </td>
                                            </tr>
                                            </s:if>
                                        </s:iterator>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 text-end">
                            <button class="btn btn-sm btn-primary" type="button" name="action:processUpdateParent" id="processUpdateApplication" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i> <span class="ms-1">Save</span></button>        
                            <button class="btn btn-sm btn-falcon-default" type="button" name="action:cancelParent" id="cancelApplication" onclick='submitForm2("cancelParent");'><i class="fa fa-times"></i> <span class="ms-1">Cancel</span></button>        
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
