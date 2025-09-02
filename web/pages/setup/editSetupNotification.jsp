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
            <div class="row">
                <!--left box-->
                <div class="col-md-6">
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.no_type"/></label>
                            <s:textfield theme="simple" name="no_type" value="%{model.no_type}" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.no_desc"/></label>
                            <s:textarea theme="simple" cols="50"  rows="3"  value="%{model.no_desc}" name="no_desc" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="radio-group radio-group-default required">
                            <label><s:text name="notification.no_sms"/></label><br>
                            <div class="radio radio-inline radio-success">
                                <s:radio name="no_sms" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" value="%{model.no_sms}" />
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="radio-group radio-group-default required">
                            <label><s:text name="notification.no_email"/></label><br>
                            <div class="radio radio-inline radio-success">
                                <s:radio name="no_email" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" value="%{model.no_email}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.no_default_sender"/></label>
                            <s:textfield theme="simple" name="no_default_sender" cssClass="form-control" value="%{model.no_default_sender}"/>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.no_template_subject"/></label>
                            <s:textfield theme="simple" name="no_template_subject" cssClass="form-control" value="%{model.no_template_subject}" />
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.smtp"/></label>
                            <s:textfield theme="simple" name="smtp" cssClass="form-control" value="%{model.smtp}"/>
                        </div>
                    </div>
                </div>

                <!--right box-->
                <div class="col-md-6">
                    <div class="col-md-12">
                        <div class="form-group form-group-default required">
                            <label><s:text name="notification.no_template_body"/></label>
                            <s:textarea theme="simple" cols="100"  rows="14" name="no_template_body" cssClass="form-control" value="%{model.no_template_body}" />
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
                    <button class="btn btn-primary" type="submit" name="action:processUpdateSN" id="processUpdateSN" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i>Save</button>
                    <button class="btn btn-default" type="button" onclick="previewTemplate()"><i class="fa fa-eye"></i>Preview</button>
                    <button class="btn btn-default" type="submit" name="action:cancelSN" id="cancelSN"><i class="fa fa-close"></i>Cancel</button>
                    <%--input type="button" id="btnPreview" value="Preview" onclick="previewTemplate()" /--%>
                    <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processUpdateSN" value="Save" onclick="return localValidateForm(this.form, 'update')"/>--%>
                    <%--<s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="cancelSN" value="Cancel"/>--%>
                </div>
                </div>
            </div><br/><br/>


            <!--Preview Modal-->
            <div id="previewModalDiv" class="modal fade" tabindex="-1" data-width="60%" data-height="" style="display: none;"  data-backdrop="static" data-keyboard="false">
                <div class="">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h3 class="title-v2">Preview</h3><br>
                        </div>
                        <div class="modal-body myModalContent"></div>
                        <div class="modal-footer">
                            <button type="button" data-dismiss="modal" class="btn btn-default">Close</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!--end responsive-->

            <%--div class="panel panel-default ">
                <div class="panel-heading ">
                    <h3 class="panel-title"> 
                        <span class="titleText"><s:text name="notificationSetup.title" /></span>
                        <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span>
                    </h3>
                </div>
                <div class="panel-body">
                    <!--            <div class="titleFramework">
                                    <span class="titleText"><s:text name="notificationSetup.title" /></span>
                                    <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span><br>
                                </div>
                                <div class="xbox">-->

                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <td align="right">
                                <s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processUpdateSN" value="Save" onclick="return localValidateForm(this.form, 'update')"/>
                                <s:submit type="button" cssClass="defaultButton btn mrg-lr-5" theme="simple" action="cancelSN" value="Cancel"/>
                            </td>
                        </tr>
                    </table>
                    <table class="table borderless">
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_type"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" name="no_type" value="%{model.no_type}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_desc"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textarea theme="simple" name="no_desc" cols="50"  rows="3" value="%{model.no_desc}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_sms"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:radio name="no_sms" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" value="%{model.no_sms}"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_email"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:radio name="no_email" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" value="%{model.no_email}"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_system"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:radio name="no_system" theme="simple" list="SmsList" listKey="keyData" listValue="valueData" value="%{model.no_system}"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_default_sender"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" size="30" name="no_default_sender" value="%{model.no_default_sender}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left><s:text name="notification.no_template_subject"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td align="left"><s:textfield theme="simple" size="66" id="no_template_subject" name="no_template_subject" value="%{model.no_template_subject}" cssClass="input-md form-control"/></td>
                        </tr>
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="150px" align=left valign="top"><s:text name="notification.no_template_body"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px" valign="top">:</td>
                            <td align="left">
                                <s:textarea cols="100" id="no_template_body"  rows="25" theme="simple" name="no_template_body" value="%{model.no_template_body}" cssClass="input-md form-control"/>
                                <br>
                                <input type="button" id="btnPreview" value="Preview" onclick="previewTemplate()" />
                            </td>
                        </tr>
                    </table>
                </div>
            </div--%>
        </form>

        <%--div id="divPreview" style="display:none;">
            &nbsp;
        </div--%>

        <span id="spanDomainFull" style="display:none;">
            <s:text name="domain.textualFull" />
        </span>
        <span id="spanFeedback" style="display:none;">
            <s:text name="email.feedback" />
        </span>
    </body>
</html>