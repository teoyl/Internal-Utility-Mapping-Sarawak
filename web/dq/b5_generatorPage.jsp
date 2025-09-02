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
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form target="_blank" id="dqRptGenFormId" method="post" action="generateDqRptDqRptGen">
            <s:hidden name="model.ID" value="%{model.ID}"/>
            <s:hidden name="model.tmpl_name" value="%{model.tmpl_name}"/>
            <div class="card">
                <div class="card-header bg-light">
                    <h5><s:property value="%{model.tmpl_name}"/> <small class="fw-normal text-600"><s:text name="actionType.generator"/></small></h5>
                </div>
                <div class="card-body">
                    <s:iterator value="searchFieldList" var="theField" status="fieldStatus">
                        <div class="row mb-1">
                            <label class="col-md-4 col-form-label col-form-label-sm text-md-end"><s:property value="%{#theField.fld_lbl}"/></label>
                            <div class="col-md-5">
                                <s:hidden name="searchFieldList[%{#fieldStatus.index}].ID" value="%{#theField.ID}"/>
                                <s:hidden name="searchFieldList[%{#fieldStatus.index}].fld_lbl" value="%{#theField.fld_lbl}"/>
                                <s:hidden name="searchFieldList[%{#fieldStatus.index}].fld_helperText" value="%{#theField.fld_helperText}"/>
                                <input type="text" class="form-control form-control-sm" name="searchFieldList[${fieldStatus.index}].fld_col" value="<s:property value="%{#theField.fld_col}"/>" placeholder="<s:property value="%{#theField.fld_helperText}"/>"/>
                            </div>
                        </div>
                    </s:iterator>
                    <div class="row mb-1">
                        <label class="col-md-4 col-form-label col-form-label-sm text-md-end"></label>
                        <div class="col-md-5">
                            <button type="submit" class="btn btn-sm btn-primary" name="action:generateDqRptDqRptGen" id="actionName" onclick="">
                                <i class="fas fa-cogs"></i> <span class="ms-1"><s:text name="button.generate"/></span>
                            </button>
                            <button type="button" class="btn btn-sm btn-falcon-default" name="reset" id="actionName" onclick="resetFields(this.form);">
                                <i class="fa fa-undo"></i> <span class="ms-1"><s:text name="button.reset"/></span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
