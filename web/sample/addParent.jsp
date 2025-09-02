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
                $('.dp_dob').datepicker({
                    format: "dd/mm/yyyy",
                    autoclose: true
                });
                $("[data-mask]").inputmask();
            });
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        <form id="addParentForm" action="loadAddPageParent" method="post">
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden name="action"/>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Parent <small>[Add]</small></h4>
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
                            <s:radio list="genderList" listKey="keyData" listValue="valueData" theme="simple" name="parent_gender" value="%{model.parent_gender}"/>
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
                        <button class="btn btn-default" type="submit" name="action:processAddChildParent" id="processAddChildParent" onclick="this.form.submit()"><i class="fa fa-plus"></i>Add Child</button>
                    </s:if>
                    <s:if test="has_right('processDeleteChild')">
                        <button class="btn btn-default" type="submit" name="action:processDeleteChildParent" id="processDeleteChildParent" onclick="if (isCheckboxSelected(form.child_selected)) {
                                    return confirmPermanentDelete();
                                } else {
                                    return false;
                                }"><i class="fa fa-trash"></i>Delete</button>
                    </s:if>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="table-responsive">
                        <table width="100%" class="table table-sds table-condensed table-striped table-hover">
                            <thead>
                                <tr>
                                    <th width="1%">
                                        <div class="checkbox check-success tableCheckbox">
                                            <s:if test="model.childList.size() > 0">
                                                <input type="checkbox"  id="child_select" class="selectAll" name="child_select" onClick="toggleCheckboxByName(this,'child_selected');">
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
                                                <input type="checkbox" name="child_selected" class="checkbox_child" id="${childStatus.index}" value="${childStatus.index}" onclick="toggleSelectAll()">
                                                <label for="${childStatus.index}" class="tableFormCheckbox"></label>
                                            </div>
                                        </td>
                                        <td><s:textfield cssClass="form-control" theme="simple" name="childList[%{#childStatus.index}].child_name" value="%{#child.child_name}"/></td>
                                        <td>
                                            <div class="col-md-12">
                                                <div class="input-group date">
                                                    <div class="input-group-addon">
                                                        <i class="fa fa-calendar"></i>
                                                    </div>
                                                    <s:textfield cssClass="form-control dp_dob" theme="simple" name="childList[%{#childStatus.index}].child_dob_str" value="%{#child.child_dob_str}"/>
                                                </div>
                                            </div>
                                            
                                        </td>
                                        <td>
                                            <%--<s:textfield cssClass="form-control" theme="simple" name="childList[%{#childStatus.index}].child_age" value="%{#child.child_age}" data-inputmask='"mask": "999"' data-mask=""/>--%>
                                            <s:textfield maxlength="3" cssClass="form-control" theme="simple" name="childList[%{#childStatus.index}].child_age" value="%{#child.child_age}"/>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 text-right">
                    <button class="btn btn-primary" type="submit" name="action:processInsertParent" id="processInsertApplication" onclick="return validateForm(this.form, 'insert')"><i class="fa fa-save"></i>Save</button>        
                    <button class="btn btn-default" type="submit" name="action:cancelParent" id="cancelApplication"><i class="fa fa-close"></i>Cancel</button>        
                </div>
            </div>
        </form>
    </body>
</html>
