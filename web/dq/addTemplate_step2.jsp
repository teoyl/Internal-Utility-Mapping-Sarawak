<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="DqTemplate.appName"/></title>
        <script type="text/javascript" language="javascript" src="include/jquery/jquery-1.11.0.js"></script>
        <script type="text/javascript" language="javascript" src="include/jquery-ui-1.11.4/jquery-ui.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/print/jquery.dataTables.min.js"></script>
        <script type="text/javascript" language="javascript" src="include/datatable/jquery.dataTables.rowReordering.js"></script>
        <link href="include/datatable/dataTables.bootstrap.min.css" rel="stylesheet"/>
        <link href="include/datatable/jquery.dataTables.min.css" rel="stylesheet"/>
        <script type="text/javascript">
            function submitTemplateForm(formAction) {
                $('.templateFieldRow').each(function (i, row) {
                    var $row = $(row);
                    $("#order_"+$row.attr("id")).val((i+1)*10);
                });
                submitForm('dqTemplateForm', formAction);
            }

            $(document).ready(function () {
                var table = $('#templateFieldTable').DataTable({
                    dom: 't',
                    "paging": false
                });
                table.rowReordering();  
                toggleSelectAll('cbShowMe');
            });
        </script>
        <style>
            table.dataTable tbody th, table.dataTable tbody td {
                padding: 0px 3px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form id="dqTemplateForm" method="post" action="processInsertDqTemplate">
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <s:hidden name="tmpl_ds_id" value="%{model.tmpl_ds_id}"/>
            <s:hidden name="tmpl_name" value="%{model.tmpl_name}" />
            <s:hidden name="tmpl_desc" value="%{model.tmpl_desc}"/>
            <s:textarea name="tmpl_cond" rows="5" cssClass="hidden" theme="simple"/>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4><s:text name="DqTemplate.appName"/> <small><s:text name="actionType.add"/></small></h4>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="table-responsive">
                            <table id="templateFieldTable" class="table table-sds table-condensed table-striped table-hover" width="100%">
                            <thead>
                                <tr>
                                    <th><s:text name="common.no"/></th>
                                    <th><s:text name="DqTemplate.showMe"/><br>
                                        <div class="checkbox check-success">
                                            <input type="checkbox"  id="cbselect" class="selectAll" name="cbselect" onClick="toggleCheckboxByClassName(this,'cbShowMe');">
                                            <label for="cbselect"></label>
                                        </div>
                                    </th>
                                    <th><s:text name="DqTemplate.fieldName"/></th>
                                    <th><s:text name="DqTemplate.fieldLabel"/></th>
                                    <th><s:text name="DqTemplate.fieldHelperText"/></th>
                                    <!--<th><s:text name="DqTemplate.fieldSetup"/></th>--> 
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator value="model.templateFieldList" var="field" status="fieldStatus">
                                    <tr id="row-${fieldStatus.index+1}" class="templateFieldRow">
                                        <td>${fieldStatus.index+1}</td>
                                        <td>
                                            <div class="checkbox check-success">
                                                <input class="cbShowMe" type="checkbox" <s:if test="#field.fld_showMe_boo">checked</s:if> value="Y" name="model.templateFieldList[${fieldStatus.index}].fld_showMe" id="_${fieldStatus.index}" onclick="toggleSelectAll('cbShowMe')"/>
                                                <label for="_${fieldStatus.index}"></label>
                                            </div>
                                        <%--<s:checkbox theme="simple" name="selected" cssClass="checkbox_child" id="%{#resultStatus.index}" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="toggleSelectAll();"/>--%>
                                    </div>
                                        </td>
                                        <td><s:textfield readonly="true" value="%{#field.fld_col}" name="model.templateFieldList[%{#fieldStatus.index}].fld_col" cssClass="form-control"/></td>
                                        <td>
                                            <s:hidden id="order_row-%{#fieldStatus.index+1}" value="%{#field.fld_order}" name="model.templateFieldList[%{#fieldStatus.index}].fld_order" cssClass="form-control"/>
                                            <s:textfield value="%{#field.fld_lbl}" name="model.templateFieldList[%{#fieldStatus.index}].fld_lbl" cssClass="form-control"/>
                                        </td>
                                        <td><s:textfield value="%{#field.fld_helperText}" name="model.templateFieldList[%{#fieldStatus.index}].fld_helperText" cssClass="form-control"/></td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                            </table>
                        </div> 
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-5">
                            <button type="button" class="btn btn-primary" name="action:loadAddPageDqTemplate" id="actionName" onclick="submitTemplateForm('loadAddPageDqTemplate')">
                                <i class="fa fa-toggle-left"></i><s:text name="DqTemplate.previous"/>
                            </button>
                            <button type="button" class="btn btn-primary" name="action:processInsertDqTemplate" id="actionName" onclick="submitTemplateForm('processInsertDqTemplate')">
                                <i class="fa fa-save"></i><s:text name="button.save"/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    <div class="modal fade" id="columnsDiv" tabindex="-1" role="dialog" aria-labelledby="columnsDivLabel"></div>
    </body>
</html>
