<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <%--text editor js--%>
        <script language="Javascript" src="pages/scripts/jquery-1.3.2.min.js" type="text/javascript"></script>
        <script language="Javascript" src="pages/scripts/htmlbox.colors.js" type="text/javascript"></script>
        <script language="Javascript" src="pages/scripts/htmlbox.styles.js" type="text/javascript"></script>
        <script language="Javascript" src="pages/scripts/htmlbox.syntax.js" type="text/javascript"></script>
        <script language="Javascript" src="pages/scripts/xhtml.js" type="text/javascript"></script>

        <script language="Javascript" src="pages/scripts/htmlbox.full.js" type="text/javascript"></script>
        <%--POPUP--%>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" type="text/css" />
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" type="text/css" />
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>

        <script language="javascript">
            function localValidateForm(form, operation) {
                var errors = new Array();
                validateRequired(form, errors);
                if (errors.length > 0) {
                    alert(errors.join('\n'));
                    setFocus(form);
                }
                return errors.length > 0 ? false : true;
            }
            function required() {
                this.aa = new Array("code_desc", "<s:text name='kpi.desc' />");
                this.ab = new Array("code_2", "<s:text name='kpi.day' />");

            }


        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="system.name"/> - <s:text name="kpi" /> - <s:text name="actionType.edit" /></title>
        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
            <form action="processInsertKPI" id="tcId" name="kpiForm" method="post" enctype="multipart/form-data">
            <s:hidden name="action" />             
            <s:hidden name="code_id" value="%{model.code_id}"/>

            <div class="row">
                <div class="col-md-6">
                    <div class="form-group form-group-default required">
                        <label><s:text name="kpi.desc"/></label>
                        <s:textfield theme="simple" name="code_desc" maxLength='%{model.columnLengthMap["code_desc"]}' value="%{model.code_desc}" cssClass="form-control"/>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group form-group-default required">
                        <label><s:text name="kpi.day"/></label>
                        <s:textfield theme="simple"  name="code_2" maxLength='%{model.columnLengthMap["code_2"]}' value="%{model.code_2}" cssClass="form-control"/>
                    </div>
                </div>
            </div><br/>
            <div class="row">
                <div class="col-md-12 text-right">
                    <s:if test='model.ID == null || model.ID.equals("")'>
                        <button class="btn btn-primary" type="submit" name="action:processInsertKPI" id="processInsertKPI" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i>Save</button>
                            <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processInsertKPI" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>--%>
                        </s:if>
                        <s:else>
                        <button class="btn btn-primary" type="submit" name="action:processUpdateKPI" id="processUpdateKPI" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i>Save</button>
                            <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processUpdateKPI" value="Update" onclick="return localValidateForm(this.form, 'update')"/>--%>
                        </s:else>
                        <button class="btn btn-default" type="submit" name="action:cancelKPI" id="cancelKPI"><i class="fa fa-close"></i>Cancel</button>
                        <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="cancelKPI" value="Cancel"/>--%>
                </div>
            </div>




            <!--            <div class="titleFramework">
                            <span class="titleText"><s:text name="kpi" /></span>
                            <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span><br>
                        </div>
                        <div class="xbox">-->
            <%--div class="panel panel-default ">
                <div class="panel-heading ">
                    <h3 class="panel-title"> 
                        <span class="titleText"><s:text name="kpi" /></span>
                        <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span>
                    </h3>
                </div>
                <div class="panel-body">
                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <td align="right">
                                <s:if test='model.ID == null || model.ID.equals("")'>
                                    <s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processInsertKPI" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                                </s:if><s:else>
                                    <s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processUpdateKPI" value="Update" onclick="return localValidateForm(this.form, 'update')"/>
                                </s:else>
                                <s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="cancelKPI" value="Cancel"/>
                            </td>
                        </tr>
                    </table>
                    <table class="table borderless">


                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="200px" align=left><s:text name="kpi.desc"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td><s:textfield theme="simple" size="50" name="code_desc" maxLength='%{model.columnLengthMap["code_desc"]}' value="%{model.code_desc}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td align=left><s:text name="kpi.day"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" size="15" name="code_2" maxLength='%{model.columnLengthMap["code_2"]}' value="%{model.code_2}" cssClass="input-md form-control"/></td>
                        </tr>

                    </table>
                </div>
            </div--%>
        </form>                    
    </body>
</html>