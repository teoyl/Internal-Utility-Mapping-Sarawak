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

            function previewTemplate() {
                // Subject
                var lsSubject = document.getElementById("no_template_subject").value;
                lsSubject = lsSubject.replace(/param>/g, 'b>');
                lsSubject = "<p><b>SUBJECT: </b>" + lsSubject + "</p>";

                // Body
                var lsData = document.getElementById("no_template_body").value;
                lsData = lsData.replace(/param>/g, 'b>');

                var lsTemp = document.getElementById("spanDomainFull").innerHTML;
                lsData = lsData.replace(/<domain.textualFull>/g, lsTemp);

                var lsTemp = document.getElementById("spanFeedback").innerHTML;
                lsData = lsData.replace(/<email.feedback>/g, lsTemp);

                lsData = lsSubject + "<p><b>BODY</b>" + lsData + "</p>";

                //alert(lsData);
//                document.getElementById("divPreview").innerHTML = lsData;

//                divwin = dhtmlwindow.open('divbox', 'div', 'divPreview', 'Preview', 'width=500px,height=450px,left=100px,top=30px,resize=1,scrolling=1');
                $("#previewModalDiv").find('.myModalContent').html(lsData);
                $('#previewModalDiv').modal('show');
                return false;
            }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="system.name"/> - <s:text name="notificationSetup.title" /> - <s:text name="actionType.edit" /></title>
        <%--<s:head />--%>
    </head>
    <body>
        <form theme="simple" action="processUpdateSN" method="post">
            <s:hidden name="action" />
            <s:hidden name="no_id" value="%{model.no_id}"/>
            <s:hidden name="antiCsrf" value="%{#session.antiCsrf}"/>
            <div class="row g-0">
                <!--left box-->
                <div class="col-md-6 pe-md-2">
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.no_type"/></label>
                                    <s:textfield theme="simple" name="no_type" value="%{model.no_type}" cssClass="form-control form-control-sm"/>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.no_desc"/></label>
                                    <s:textarea theme="simple" cols="50"  rows="3"  value="%{model.no_desc}" name="no_desc" cssClass="form-control form-control-sm" />
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6 required">
                                    <label class="form-label"><s:text name="notification.no_sms"/></label><br>
                                    <div class="form-check">
                                        <s:radio name="no_sms" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" value="%{model.no_sms}" />
                                    </div>
                                </div>
                                <div class="col-md-6 required">
                                    <label class="form-label"><s:text name="notification.no_email"/></label><br>
                                    <div class="form-check">
                                        <s:radio name="no_email" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" value="%{model.no_email}"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.no_default_sender"/></label>
                                    <s:textfield theme="simple" name="no_default_sender" cssClass="form-control form-control-sm" value="%{model.no_default_sender}"/>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.no_template_subject"/></label>
                                    <s:textfield theme="simple" name="no_template_subject" cssClass="form-control form-control-sm" value="%{model.no_template_subject}" />
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col required">
                                    <label class="form-label"><s:text name="notification.smtp"/></label>
                                    <s:textfield theme="simple" name="smtp" cssClass="form-control form-control-sm" value="%{model.smtp}"/>
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
                                    <s:textarea theme="simple" cols="100"  rows="14" name="no_template_body" cssClass="form-control form-control-sm" value="%{model.no_template_body}" />
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
                    <button class="btn btn-sm btn-primary" type="submit" name="action:processUpdateSN" id="processUpdateSN" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i> <span class="ms-1">Save</span></button>
                    <button class="btn btn-sm btn-falcon-default" type="button" onclick="previewTemplate()"><i class="fa fa-eye"></i> <span class="ms-1">Preview</span></button>
                    <button class="btn btn-sm btn-falcon-default" type="submit" name="action:cancelSN" id="cancelSN"><i class="fa fa-times"></i> <span class="ms-1">Cancel</span></button>
                    <%--input type="button" id="btnPreview" value="Preview" onclick="previewTemplate()" /--%>
                    <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processUpdateSN" value="Save" onclick="return localValidateForm(this.form, 'update')"/>--%>
                    <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="cancelSN" value="Cancel"/>--%>
                </div>
            </div>

            <!--Preview Modal-->
            <div id="previewModalDiv" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Preview</h5><br>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-hidden="true"></button>
                        </div>
                        <div class="modal-body myModalContent"></div>
                        <div class="modal-footer">
                            <button type="button" data-bs-dismiss="modal" class="btn btn-sm btn-falcon-default">Close</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!--end responsive-->

        </form>

        <span id="spanDomainFull" style="display:none;">
            <s:text name="domain.textualFull" />
        </span>
        <span id="spanFeedback" style="display:none;">
            <s:text name="email.feedback" />
        </span>
    </body>
</html>