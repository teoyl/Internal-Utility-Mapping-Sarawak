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
                $('.dp_dob').datepicker({
                    format: "dd/mm/yyyy",
                    autoclose: true
                });
                $("[data-mask]").inputmask();
            });
            function addGc(childIndex) {
                $("#childIndex").val(childIndex);
                submitForm("processAddGrandChildParent");
            }
            $('.addChildBtn').on('click', function(e) {
                submitForm("processAddChildParent");
            });
            
            function submitForm(action) {
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
                    submitForm("processUpdateParent");
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
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Parent <small>[Edit]</small></h4>
                </div>
                <div class="panel-body">
                    <div class="row form-group">
                        <label class="col-md-3 control-label"><s:text name="Sample.parentName"/><font class="asterisk">*</font></label>
                        <div class="col-md-6">
                            <s:textfield theme="simple" name="parent_name" maxlength='%{model.columnLengthMap["parent_name"]}' cssClass="form-control" value="%{model.parent_name}"/>
                        </div>
                    </div>
                    <div class="row form-group">
                        <label class="col-md-3 radio-label"><s:text name="Sample.parentGender"/><font class="asterisk">*</font></label>
                        <div class="col-md-6 radio radio-inline radio-success">
                            <s:radio list="commList.genderOptions" listKey="keyData" listValue="valueData" theme="simple" name="parent_gender" value="%{model.parent_gender}"/>
                        </div>
                    </div>
                    <div class="row form-group">
                        <label class="col-md-3 control-label"><s:text name="Sample.age"/><font class="asterisk">*</font></label>
                        <div class="col-md-6">
                            <s:textfield theme="simple" name="parent_age" cssClass="form-control" value="%{model.parent_age}"/>
                        </div>
                    </div>
                    <div class="row form-group">
                        <label class="col-md-3 control-label">State<font class="asterisk">*</font></label>
                        <div class="col-md-6">
                            <s:select list="stateList" listKey="keyData" listValue="valueData" name="parent_state" theme="simple" value="%{model.parent_state}" cssClass="form-control sds-dropdown"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <h4 class="title-v3">Children</h4>
                    <s:if test="has_right('processAddChild')">
                        <button class="btn btn-default addChildBtn" type="submit" name="action:processAddChildParent" id="processAddChildParent"><i class="fa fa-plus"></i>Add Child</button>
                    </s:if>
                    <s:if test="has_right('processDeleteChild')">
                        <button class="btn btn-default" type="button" name="action:processDeleteChildParent" id="processDeleteChildParent" onclick="if (isCheckboxSelected_byClass('allcb')) {
                                    if (confirmPermanentDelete()) {submitForm('processDeleteChildParent')}
                                } else {
                                    return false;
                                }"><i class="fa fa-trash"></i>Delete</button>
                    </s:if>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="table-responsive">
                        <table class="table table-sds table-condensed table-hover">
                            <thead>
                                <tr>
                                    <th width="1%" colspan="2">
                                        <div class="checkbox check-success tableCheckbox">
                                            <s:if test="model.childList.size() > 0">
                                                <input type="checkbox"  id="child_select" class="selectAll" name="child_select" onClick="toggleCheckboxByName(this,'arrSelect.childSelected');">
                                            </s:if>
                                            <s:else>
                                                <input type="checkbox" id="child_select" class="selectAll" name="child_select" disabled >
                                            </s:else>
                                             <label for="child_select" class="tableFormCheckbox"></label>
                                        </div>
                                    </th>
                                    <th width="460px"><s:text name="Sample.name" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                    <th width="310px"><s:text name="Sample.DOB" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                    <th width="140px"><s:text name="Sample.age" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator value="model.childList" status="childStatus" var="child">
                                    <tr>
                                        <td>
                                            <div class="checkbox check-success tableFormCheckbox">
                                                <s:hidden theme="simple" name="childList[%{#childStatus.index}].ID" value="%{#child.ID}" />
                                                <s:hidden theme="simple" name="childList[%{#childStatus.index}].parent_id" value="%{#child.parent_id}" />
                                                <s:hidden theme="simple" name="childList[%{#childStatus.index}].arrDelete.gcDeleted" value="%{#child.arrDelete.gcDeleted}" />
                                                <input type="checkbox" name="arrSelect.childSelected" class="checkbox_child allcb" id="${childStatus.index}" value="${childStatus.index}" onclick="toggleSelectAll()">
                                                <label for="${childStatus.index}" class="tableFormCheckbox"></label>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="btn-group addDelBtn">
                                                <button type="button" class="btn btn-icon-only btn-default addGrandChildBtn" name="action:processAddGrandChildParent" onclick="addGc(${childStatus.index}); return true;">
                                                    <span class="fa fa-list-ul"></span>
                                                </button>
                                            </div>
                                        </td>
                                        <td ><s:textfield cssClass="form-control" theme="simple" name="childList[%{#childStatus.index}].child_name" value="%{#child.child_name}" required=""/></td>
                                        <td >
                                            <div class="col-md-12">
                                                <div class="input-group date">
                                                    <div class="input-group-addon">
                                                        <i class="fa fa-calendar"></i>
                                                    </div>
                                                    <s:textfield cssClass="form-control dp_dob" theme="simple" name="childList[%{#childStatus.index}].child_dob_str" value="%{#child.child_dob_str}"/>
                                                </div>
                                            </div>
                                            
                                        </td>
                                        <td >
                                            <%--<s:textfield cssClass="form-control" theme="simple" name="childList[%{#childStatus.index}].child_age" value="%{#child.child_age}" data-inputmask='"mask": "999"' data-mask=""/>--%>
                                            <s:textfield maxlength="3" cssClass="form-control" theme="simple" name="childList[%{#childStatus.index}].child_age" value="%{#child.child_age}"/>
                                        </td>
                                    </tr>
                                    <s:if test="#child.grandChildList.size() > 0">
                                        <tr>
                                        <td></td>
                                        <td colspan="4">
                                            <table width="100%" class="table table-sds table-hover">
                                            <%--<tr>
                                                    <td colspan="2">
                                                        <s:if test="has_right('processAddGrandChild')">
                                                            <button class="btn btn-default" type="submit" name="action:processAddGrandChildParent" id="processAddGrandChildParent"><i class="fa fa-plus"></i>Add</button>
                                                        </s:if>
                                                        <s:if test="has_right('processDeleteGrandChild')">
                                                            <button class="btn btn-default" type="submit" name="action:processDeleteGrandChildParent" id="processDeleteGrandChildParent" onclick="if (isCheckboxSelected(form['child.arrSelect.gcSelected'])) {
                                                                        return confirmPermanentDelete();
                                                                    } else {
                                                                        return false;
                                                                    }"><i class="fa fa-trash"></i>Delete</button>
                                                        </s:if>
                                                    </td>
                                                </tr>    --%>
                                                <tr>
                                                    <th width="1%" style="border-top: 0;">
                                                        <div class="checkbox check-success tableCheckbox">
                                                            <s:if test="#child.grandChildList.size() > 0">
                                                                <input type="checkbox" id="child${childStatus.index}_select" class="all_child_${childStatus.index}" name="gc_select" onClick="toggleCheckboxByClassName(this,'child_${childStatus.index}');">
                                                            </s:if>
                                                            <s:else>
                                                                <input type="checkbox" id="child${childStatus.index}_select" class="all_child_${childStatus.index}" name="gc_select" disabled >
                                                            </s:else>
                                                             <label for="child${childStatus.index}_select" class="tableFormCheckbox"></label>
                                                        </div>
                                                    </th>
                                                    <th style="border-top: 0;">
                                                        Grand Child Name
                                                    </th>
                                                </tr>
                                            <s:iterator value="#child.grandChildList" status="gcStatus" var="grandChild">
                                                <tr>
                                                    <td>
                                                        <div class="checkbox check-success tableFormCheckbox">
                                                            <s:hidden theme="simple" name="model.childList[%{#childStatus.index}].grandChildList[%{#gcStatus.index}].ID" />
                                                            <s:hidden theme="simple" name="model.childList[%{#childStatus.index}].grandChildList[%{#gcStatus.index}].child_id" value="%{#grandChild.child_id}" />
                                                            <input type="checkbox" name="model.childList[${childStatus.index}].arrSelect.gcSelected" class="child_${childStatus.index} allcb" id="child${childStatus.index}gc${gcStatus.index}" value="${gcStatus.index}" onclick="toggleSelectAll_ml('all_child_${childStatus.index}', 'child_${childStatus.index}')">
                                                            <label for="child${childStatus.index}gc${gcStatus.index}" class="tableFormCheckbox"></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <s:textfield cssClass="form-control" theme="simple" name="model.childList[%{#childStatus.index}].grandChildList[%{#gcStatus.index}].gc_name" value="%{#grandChild.gc_name}" required=""/>
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
                <div class="col-md-12 text-right">
                    <button class="btn btn-primary" type="button" name="action:processUpdateParent" id="processUpdateApplication" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i>Save</button>        
                    <button class="btn btn-default" type="button" name="action:cancelParent" id="cancelApplication" onclick='submitForm("cancelParent");'><i class="fa fa-close"></i>Cancel</button>        
                </div>
            </div>
        </form>
    </body>
</html>
