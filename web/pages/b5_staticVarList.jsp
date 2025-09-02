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
            <div class="card">
                <div class="card-header bg-light">
                    <h5><s:text name="StaticVar.appName"/></h5>
                </div>
                <div class="card-body">
                    <table width="100%" class="table table-sds table-sm fs--1 table-striped table-hover">
                        <thead class="bg-200 text-900">
                            <tr>
                                <th style="width:20%;" class="align-middle white-space-nowrap"><s:text name="StaticVar.variableName"/></th>
                                <th class="align-middle white-space-nowrap"><s:text name="StaticVar.variableValue"/></th>
                            </tr>
                        </thead>
                        <tbody>
                    <s:iterator value="staticVarMap.keySet" var="key" status="keyStatus">
                        <tr>
                            <td class="align-middle white-space-nowrap">
                                <s:property value="%{#key}"/>
                            </td>
                            <td class="align-middle white-space-nowrap">
                                <s:textfield name="%{#key}" cssClass="form-control form-control-sm mb0" value='%{staticVarMap.get(#key)}' required="required"/>
                            </td>
                        </tr>
                    </s:iterator>
                        </tbody>
                    </table>
                    <div class="row">
                        <div class="col text-center">
                            <button type="submit" class="btn btn-sm btn-primary" name="" id="actionName" onclick="return updateChanges();">
                                <i class="fa fa-save"></i> <span class="ms-1"><s:text name="schedular.update"/></span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <div id="submitFormDiv" class="hidden"></div>
    </body>
</html>