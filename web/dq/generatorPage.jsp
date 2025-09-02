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
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4><s:property value="%{model.tmpl_name}"/> <small><s:text name="actionType.generator"/></small></h4>
                </div>
                <div class="panel-body">
                    <s:iterator value="searchFieldList" var="theField" status="fieldStatus">
                        <div class="form-horizontal form-group">
                            <label class="col-md-4 control-label"><s:property value="%{#theField.fld_lbl}"/></label>
                            <div class="col-md-5">
                                <s:hidden name="searchFieldList[%{#fieldStatus.index}].ID" value="%{#theField.ID}"/>
                                <s:hidden name="searchFieldList[%{#fieldStatus.index}].fld_lbl" value="%{#theField.fld_lbl}"/>
                                <s:hidden name="searchFieldList[%{#fieldStatus.index}].fld_helperText" value="%{#theField.fld_helperText}"/>
                                <input type="text" class="form-control" name="searchFieldList[${fieldStatus.index}].fld_col" value="<s:property value="%{#theField.fld_col}"/>" placeholder="<s:property value="%{#theField.fld_helperText}"/>"/>
                            </div>
                        </div>
                    </s:iterator>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-5">
                            <button type="submit" class="btn btn-primary" name="action:generateDqRptDqRptGen" id="actionName" onclick="">
                                <i class="fa fa-gears"></i><s:text name="button.generate"/>
                            </button>
                            <button type="button" class="btn btn-default" name="reset" id="actionName" onclick="resetFields(this.form);">
                                <i class="fa fa-undo"></i><s:text name="button.reset"/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
