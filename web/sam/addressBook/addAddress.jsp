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

        <script language="javascript">
            function localValidateForm(form, operation) {
                var errors = new Array();
                validateRequired(form, errors);
//                validateItems(form, errors);
                if (errors.length > 0) {
                    alert(errors.join('\n'));
                    setFocus(form);
                }
                return errors.length > 0 ? false : true;
            }


//            function validateItems(form, errors) {
//                if (form.app_rights_code) {
//                    if (typeof (form.app_rights_code.type) == "string") {
//                        validateItem(form.app_rights_code, form.app_rights_description, form.app_rights_methods, 1, errors);
//                    } else {
//                        var isError = false;
//                        for (var i = 0; i < form.app_rights_code.length; i++) {
//                            isError = validateItem(form.app_rights_code[i], form.app_rights_description[i], form.app_rights_methods[i], i + 1, errors);
//                            if (isError)
//                                break;
//                        }
//                    }
//                }
//            }

//            function validateItem(rights_code, rights_desc, rights_methods, idx, errors) {
//                //if (!selected.checked){
//                if (rights_code.value == "") {
//                    errors[errors.length] = formatText(messageItemized, idx,
//                            formatText(messageRequired, "<s:text name='application.rightsCode' />"));
//                    isError = true;
//                }
//                if (rights_desc.value == "") {
//                    errors[errors.length] = formatText(messageItemized, idx,
//                            formatText(messageRequired, "<s:text name='application.rightDesc' />"));
//                    isError = true;
//                }
//                if (rights_methods.value == "") {
//                    errors[errors.length] = formatText(messageItemized, idx,
//                            formatText(messageRequired, "<s:text name='application.methodInvoked' />"));
//                    isError = true;
//                }
//            }

            function required() {
                this.aa = new Array("list_name", "<s:text name='list.name' />");
                this.ab = new Array("list_desc", "<s:text name='list.desc' />");
            }
        </script>

        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>

        <form method="post" name="form" id="form" action="processInsertSetupAddress">
            <s:hidden theme="simple" name="action" />
            <s:hidden theme="simple" name="list_id" value="%{model.list_id}"/>

            <div class="row">
                <div class="col-md-4">
                    <div class="form-group form-group-default required">
                        <label><s:text name="list.name"/></label>
                        <s:textfield theme="simple" name="list_name" value="%{model.list_name}" cssClass="form-control"/>
                    </div>
                </div>
                <div class="col-md-8">
                    <div class="form-group form-group-default required">
                        <label><s:text name="list.desc"/></label>
                        <s:textfield theme="simple" name="list_desc" value="%{model.list_desc}" cssClass="form-control"/>
                    </div>
                </div>
            </div>

            <br><br>

            <div class="row">
                <div class="col-md-12">
                    <h3 class="title-v3"><s:text name="email.address"/></h3>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 form-row-margin">
                    <%--<s:if test="has_right('processAddEmail')">--%>
                    <button class="btn btn-default" type="submit" name="action:processAddEmailSetupAddress" id="processAddEmailSetupAddress"><i class="fa fa-plus"></i>Add Email</button>
                    <%--</s:if>--%>
                    <%--<s:if test="has_right('processDeleteEmail')">--%>
                    <button class="btn btn-default" type="submit" name="action:processDeleteEmailSetupAddress" id="processDeleteEmailSetupAddress" onclick="if (isCheckboxSelected(form.rights_selected)) {
                                return confirmPermanentDelete();
                            } else {
                                return false;
                            }"><i class="fa fa-trash"></i>Delete Email</button>
                    <%--</s:if>--%>
                </div>
            </div>


            <s:if test="model.emailList.size() > 0">
                <div class="row">
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <table class="table table-epa table-condensed table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>
                                            <s:if test="model.emailList.size() > 0">
                                                <input type="checkbox" id="rights_select" name="rights_select" onclick="toggleCheckbox(this, delRights_ids);">
                                            </s:if>
                                            <s:else>
                                                <input type="checkbox" id="rights_select" name="rights_select" disabled>
                                            </s:else>
                                        </th>
                                        <th><s:text name="email.seq" /></th>
                                        <th><s:text name="email.address" /><jsp:include page="/pages/base/requiredField.jsp"/></th>
                                        <th><s:text name="email.designation" /></th>
                                        <th><s:text name="email.name" /></th>
                                    </tr>
                                </thead>
                                <tbody>

                                    <s:iterator value="model.emailList" status="emailStatus" id="emailItem">
                                        <tr class="<s:if test="#emailStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                            <s:hidden theme="simple" name="emailList[%{#emailStatus.index}].email_id" value="%{#emailItem.email_id}" />
                                            <td><s:checkbox theme="simple" name="rights_selected" id="delRights_ids" fieldValue="%{#emailStatus.index}" onclick="checkToggleCheckbox(rights_select, delRights_ids)" /></td>
                                            <td>
                                                <s:property value="%{#emailStatus.index+1}"/>
                                                <s:hidden theme="simple" name="emailList[%{#emailStatus.index}].email_seq" value="%{#emailStatus.index+1}"/>
                                            </td>
                                            <td><s:textfield  maxLength="50" theme="simple" name="emailList[%{#emailStatus.index}].email" value="%{#emailItem.email}" cssClass="input-sm form-control"/></td>
                                            <td><s:textfield maxLength="100" size="60" theme="simple" name="emailList[%{#emailStatus.index}].email_designation" value="%{#emailItem.email_designation}" cssClass="input-sm form-control"/></td>
                                            <td><s:textfield maxLength="100" size="50"  theme="simple" name="emailList[%{#emailStatus.index}].email_name" value="%{#emailItem.email_name}" cssClass="input-sm form-control"/></td>
                                        </tr>
                                    </s:iterator>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </s:if>
            <br/><br/>

            <div class="row">
                <div class="col-md-12 text-right">
                    <button class="btn btn-primary" type="submit" name="action:processInsertSetupAddress" id="processInsertSetupAddress" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i>Save</button>        
                    <button class="btn btn-default" type="submit" name="action:cancelSetupAddress" id="cancelSetupAddress"><i class="fa fa-close"></i>Cancel</button>        
                </div>
            </div>
        </form>
        <br><br><br>
    </body>
</html>