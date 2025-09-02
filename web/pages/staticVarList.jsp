<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript">
            function updateChanges() {
                if (validateForm_bshor('staticVarFormId')) {
                    submitForm('staticVarFormId', 'processUpdateStaticVar');
                } else {
                    alert("<s:text name="field.redRequired"/>");
                }
                return false;
            }
        </script>
    </head>
    <body>
        <form name="schedularForm" id="staticVarFormId" method="post" action="processUpdateSysSchedular">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4><s:text name="StaticVar.appName"/></h4>
                </div>
                <div class="panel-body">
                    <table width="100%" class="table table-sds table-condensed table-striped table-hover">
                        <thead>
                            <tr>
                                <th><s:text name="StaticVar.variableName"/></th>
                                <th><s:text name="StaticVar.variableValue"/></th>
                            </tr>
                        </thead>
                        <tbody>
                    <s:iterator value="staticVarMap.keySet" var="key" status="keyStatus">
                        <tr>
                            <td>
                                <s:property value="%{#key}"/>
                            </td>
                            <td>
                                <s:textfield name="%{#key}" cssClass="form-control mb0" value='%{staticVarMap.get(#key)}' required="required"/>
                            </td>
                        </tr>
                    </s:iterator>
                        </tbody>
                    </table>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-5">
                            <button type="submit" class="btn btn-primary" name="" id="actionName" onclick="return updateChanges();">
                                <i class="fa fa-save"></i><s:text name="schedular.update"/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
                            <div id="submitFormDiv" class="hidden"></div>
    </body>
</html>