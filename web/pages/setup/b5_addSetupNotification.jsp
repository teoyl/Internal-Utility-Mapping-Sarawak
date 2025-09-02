<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
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
            <div class="row g-0">
                <!--left box-->
                <div class="col-md-6 pe-md-2">
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.no_type"/></label>
                                    <s:textfield theme="simple" name="no_type" cssClass="form-control form-control-sm"/>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="co requiredl">
                                    <label class="form-label"><s:text name="notification.no_desc"/></label>
                                    <s:textarea theme="simple" cols="50"  rows="3" name="no_desc" cssClass="form-control form-control-sm" />
                                </div>
                            </div>
                            <div class="row mb-1">
                                <div class="col-md-6 required">
                                    <label class="form-label"><s:text name="notification.no_sms"/></label><br>
                                    <div class="form-check">
                                        <s:radio name="no_sms" theme="simple" list="SmsList" listKey="keyData" listValue="valueData"  />
                                    </div>
                                </div>
                                <div class="col-md-6 required">
                                    <label class="form-label"><s:text name="notification.no_email"/></label><br>
                                    <div class="form-check">
                                        <s:radio name="no_email" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" />
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.no_default_sender"/></label>
                                    <s:textfield theme="simple" name="no_default_sender" cssClass="form-control form-control-sm"/>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.no_template_subject"/></label>
                                    <s:textfield theme="simple" name="no_template_subject" cssClass="form-control form-control-sm"/>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.smtp"/></label>
                                    <s:textfield theme="simple" name="smtp" cssClass="form-control form-control-sm"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!--right box-->
                <div class="col-md-6 ps-md-2">
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.no_template_body"/></label>
                                    <s:textarea theme="simple" cols="100"  rows="14" name="no_template_body" cssClass="form-control form-control-sm"/>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.sms_content"/></label>
                                    <s:textarea theme="simple" cols="100"  rows="6" name="sms_content" cssClass="form-control form-control-sm" value="%{model.sms_content}" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="card mb-3">
                <div class="card-body">
                    <button class="btn btn-sm btn-primary" type="submit" name="action:processInsertSN" id="processInsertSN" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i> <span class="ms-1">Save</span></button>
                    <button class="btn btn-sm btn-falcon-default" type="submit" name="action:cancelSN" id="cancelSN"><i class="fa fa-times"></i> <span class="ms-1">Cancel</span></button>
                            <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processInsertSN" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>--%>
                            <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="cancelSN" value="Cancel"/>--%>
                </div>
            </div>
        </form>
    </body>
</html>