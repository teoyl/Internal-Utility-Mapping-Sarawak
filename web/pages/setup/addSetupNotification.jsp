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
                this.aa = new Array("no_type", "<s:text name='notification.no_type' />");
                this.ab = new Array("no_desc", "<s:text name='notification.no_desc' />");
                this.ac = new Array("no_sms", "<s:text name='notification.no_sms' />");
                this.ad = new Array("no_email", "<s:text name='notification.no_email' />");
                this.af = new Array("no_default_sender", "<s:text name='notification.no_default_sender' />");
                this.ag = new Array("no_template_subject", "<s:text name='notification.no_template_subject' />");
                this.ah = new Array("no_template_body", "<s:text name='notification.no_template_body' />");
            }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="system.name"/> - <s:text name="notificationSetup.title" /> - <s:text name="actionType.edit" /></title>
        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        <form theme="simple" action="processInsertSN" method="post">
            <s:hidden name="action" />
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <div class="row">
                <!--left box-->
                <div class="col-md-6">
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.no_type"/></label>
                            <s:textfield theme="simple" name="no_type" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.no_desc"/></label>
                            <s:textarea theme="simple" cols="50"  rows="3" name="no_desc" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="radio-group radio-group-default required">
                            <label><s:text name="notification.no_sms"/></label><br>
                            <div class="radio radio-inline radio-success">
                                <s:radio name="no_sms" theme="simple" list="SmsList" listKey="keyData" listValue="valueData"  />
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="radio-group radio-group-default required">
                            <label><s:text name="notification.no_email"/></label><br>
                            <div class="radio radio-inline radio-success">
                                <s:radio name="no_email" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" />
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.no_default_sender"/></label>
                            <s:textfield theme="simple" name="no_default_sender" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.no_template_subject"/></label>
                            <s:textfield theme="simple" name="no_template_subject" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.smtp"/></label>
                            <s:textfield theme="simple" name="smtp" cssClass="form-control"/>
                        </div>
                    </div>
                </div>

                <!--right box-->
                <div class="col-md-6">
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.no_template_body"/></label>
                            <s:textarea theme="simple" cols="100"  rows="14" name="no_template_body" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.sms_content"/></label>
                            <s:textarea theme="simple" cols="100"  rows="6" name="sms_content" cssClass="form-control" value="%{model.sms_content}" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                <div class="col-md-12">
                    <button class="btn btn-primary" type="submit" name="action:processInsertSN" id="processInsertSN" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i>Save</button>
                    <button class="btn btn-default" type="submit" name="action:cancelSN" id="cancelSN"><i class="fa fa-close"></i>Cancel</button>
                        <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processInsertSN" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>--%>
                        <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="cancelSN" value="Cancel"/>--%>
                </div>
                </div>
            </div><br/><br/>




            <%--div class="panel panel-default ">
                <div class="panel-heading ">
                    <h3 class="panel-title"> 
                        <span class="titleText"><s:text name="notificationSetup.title" /></span>
                        <span class="titleActionTypeText"> | <s:text name="actionType.add" /></span>
                    </h3>
                </div>
                <div class="panel-body">
                    <!--            <div class="titleFramework">
                                    <span class="titleText"><s:text name="notificationSetup.title" /></span>
                                    <span class="titleActionTypeText"> | <s:text name="actionType.add" /></span><br>
                                </div>
                                <div class="xbox">-->
                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <td align="right">
                                <s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processInsertSN" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                                <s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="cancelSN" value="Cancel"/>
                            </td>
                        </tr>
                    </table>
                    <table class="table borderless">
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_type"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" size="30" name="no_type" cssClass="input-md form-control requiredField"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_desc"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textarea theme="simple" cols="50"  rows="3" name="no_desc" cssClass="input-md form-control requiredField" /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_sms"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:radio name="no_sms" theme="simple" list="SmsList" listKey="keyData" listValue="valueData"  /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_email"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:radio name="no_email" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_system"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:radio name="no_system" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" /></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_default_sender"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" size="30" name="no_default_sender" cssClass="input-md form-control requiredField"/> </td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_template_subject"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" size="66" name="no_template_subject" cssClass="input-md form-control requiredField"/> </td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left valign="top"><s:text name="notification.no_template_body"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px" valign="top">:</td>
                            <td align="left"><s:textarea theme="simple" cols="100"  rows="25" name="no_template_body" cssClass="input-md form-control requiredField"/></td>
                        </tr>
                    </table>
                </div>
            </div--%>
        </form>
    </body>
</html>