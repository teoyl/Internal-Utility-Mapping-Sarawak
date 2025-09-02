<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="DqRptGen.appName"/></title>
        <script type="text/javascript">
            $(document).ready(function () {
            });
            function resetFields(form) {
                var noOfElements = form.elements.length;
                for (var i = 0; i < noOfElements; i++) {
                    if (!(form.elements[i].type == "hidden"
                            || form.elements[i].type == "submit"
                            || form.elements[i].type == "button")) {
                        clearValue(form.elements[i]);
                    }
                    if (form.elements[i].id.indexOf("_hidden_") != -1) {
            <%--Delvene @ 19-May-2015 :: Reset searchable hidden field--%>
                        clearValue(form.elements[i]);
                    }
                }
                $(".dsrfdd").val("");
                $(".dsrfdd").select2();
                $(".dsrf").val("");
                setFocus(form);
            }
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/b4_actionError.jsp"/>
        <form target="_blank" id="dqRptGenFormId" method="post" action="generateDqRptDqRptGen">
            <s:hidden name="model.ID" value="%{model.ID}"/>
            <s:hidden name="model.tmpl_name" value="%{model.tmpl_name}"/>
            <div class="kt-portlet">
                <div class="kt-portlet__head">
                    <div class="kt-portlet__head-label">
                        <h3 class="kt-portlet__head-title">
                            <s:property value="%{model.tmpl_name}"/> <small><s:text name="actionType.generator"/></small>
                        </h3>
                    </div>
                </div>
                <div class="kt-portlet__body">
                    <s:iterator value="searchFieldList" var="theField" status="fieldStatus">
                        <div class="form-group row">
                            <label class="col-3 col-form-label"><s:property value="%{#theField.fld_lbl}"/></label>
                            <div class="col-md-5">
                                <s:hidden name="searchFieldList[%{#fieldStatus.index}].ID" value="%{#theField.ID}"/>
                                <s:hidden name="searchFieldList[%{#fieldStatus.index}].fld_lbl" value="%{#theField.fld_lbl}"/>
                                <s:hidden name="searchFieldList[%{#fieldStatus.index}].fld_helperText" value="%{#theField.fld_helperText}"/>
                                <input type="text" class="form-control" name="searchFieldList[${fieldStatus.index}].fld_col" value="<s:property value="%{#theField.fld_col}"/>" placeholder="<s:property value="%{#theField.fld_helperText}"/>"/>
                            </div>
                        </div>
                    </s:iterator>
                </div>
                <div class="kt-portlet__foot">
                    <div class="kt-form__actions">
                        <div class="form-group row">
                            <div class="col-md-5">
                                <button type="submit" class="btn btn-brand" name="action:generateDqRptDqRptGen" id="actionName" onclick="">
                                    <i class="fa fa-gears"></i><s:text name="button.generate"/>
                                </button>
                                <button type="button" class="btn btn-outline-brand" name="reset" id="actionName" onclick="resetFields(this.form);">
                                    <i class="fa fa-undo"></i><s:text name="button.reset"/>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
