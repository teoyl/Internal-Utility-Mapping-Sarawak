<%-- 
    Document   : editBackend
    Created on : Oct 6, 2010, 9:54:57 AM
    Author     : thoth
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<%--<s:head />--%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Administrator: Backend Console</title>

        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>

        <script type="text/javascript">
            function disableButton(btn) {
                if (btn.value.toString().substring(0, 5) == "Start") {
                    document.getElementById("form0").action = "_LBAstartSelectedBackend";
                } else if (btn.value.toString().substring(0, 4) == "Stop") {
                    document.getElementById("form0").action = "_LBAstopSelectedBackend";
                } else {
                    document.getElementById("form0").action = "_LBAshowThreadBackend";
                }

                document.getElementById("btnStartAll").disabled = true;
                document.getElementById("btnStopAll").disabled = true;
                document.getElementById("btnShowThread").disabled = true;

                btn.form.submit();
            }
        </script>
    </head>
    <body>
        <div class="titleFramework">
            <span class="titleText">Backend Console</span>
            <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span><br>
        </div>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        <div class="xbox" style="margin-left: 5pt; margin-top: 2pt;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
        <tr>
            <td width="20px">&nbsp;</td>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <form id="form0" name="form0" action="_LBAloadEditPageBackend">
                        <table class="defaultTable" cellspacing="0" cellpadding="0" border="0" width="100%">
                            <tr class="tableHeader">
                                <th align="center" width="5%">&nbsp;</th>
                                <th class="blue_td" align="center" width="20%">Name</th>
                                <th class="blue_td" align="center" width="10%">State</th>
                                <th class="blue_td" align="center" width="40%">Type</th>
                                <%--<td class="blue_td" align="center" width="25%">&nbsp;</td>--%>
                            </tr>
                            <!-- ALL -->
                            <tr class = "even">
                                <td class="blue_td" style="padding: 3px">
                                    <s:if test='isEmailReminderOn.equals("Stopping")'>
                                        <input type="checkbox" disabled id="select_all" name="select_all" onclick="toggleCheckbox(this, selected_backend);" />
                                    </s:if><s:else>
                                        <input type="checkbox" id="select_all" name="select_all" onclick="toggleCheckbox(this, selected_backend);" />
                                    </s:else>
                                    
                                </td>
                                <td colspan="3" class="blue_td" style="padding: 3px">
                                        <%--<s:submit type="submit" id="btnStartAll" theme="simple" action="_LBAstartSelectedBackend" value="Start Selected" onclick="disableButton(this);" />
                                        <s:submit type="submit" id="btnStopAll" theme="simple" action="_LBAstopSelectedBackend" value="Stop Selected" onclick="disableButton(this);" />
                                        <s:submit type="submit" id="btnShowThread" theme="simple" action="_LBAshowThreadBackend" value="Show Thread Count" onclick="disableButton(this);" />--%>
                                        <s:submit cssClass="defaultButton"  type="submit" id="btnStartAll" theme="simple" action="startSelectedBackend" value="Start Selected" onclick="disableButton(this);" />
                                        <s:submit cssClass="defaultButton"  type="submit" id="btnStopAll" theme="simple" action="stopSelectedBackend" value="Stop Selected" onclick="disableButton(this);" />
                                        <s:submit cssClass="defaultButton"  type="submit" id="btnShowThread" theme="simple" action="showThreadBackend" value="Show Thread Count" onclick="disableButton(this);" />
                                </td>
                            </tr>

                            <!-- Call Workflow -->
                            <tr class = "odd">
                                <td class="blue_td" style="padding: 3px">
                                    <s:if test='isEmailReminderOn.equals("Stopping")'>
                                        <s:checkbox disabled="true" theme="simple" name="selected_backend" id="selected_backend" fieldValue="1" onclick="checkToggleCheckbox(select_all, selected_backend);"/>
                                    </s:if><s:else>
                                        <s:checkbox theme="simple" name="selected_backend" id="selected_backend" fieldValue="1" onclick="checkToggleCheckbox(select_all, selected_backend);"/>
                                    </s:else>
                                </td>
                                <td class="blue_td" style="padding: 3px"><s:text name="msen.backend.emailReminder" /></td>
                                <td class="blue_td" align="center"><s:property value="isEmailReminderOn" /></td>
                                <td class="blue_td" style="padding: 3px"><s:text name="msen.backend.emailReminderDesc" /></td>
                            </tr>
                            <%--<tr class = "even">
                                <td class="blue_td" style="padding: 3px">
                                    <s:if test='isEmailReminderOn.equals("Stopping")'>
                                        <s:checkbox disabled="true" theme="simple" name="selected_backend" id="selected_backend" fieldValue="2" onclick="checkToggleCheckbox(select_all, selected_backend);"/>
                                    </s:if><s:else>
                                        <s:checkbox theme="simple" name="selected_backend" id="selected_backend" fieldValue="2" onclick="checkToggleCheckbox(select_all, selected_backend);"/>
                                    </s:else>
                                </td>
                                <td class="blue_td" style="padding: 3px">Call Job Expired Notification</td>
                                <td class="blue_td" align="center"><s:property value="isEmailReminderOn" /></td>
                                <td class="blue_td" style="padding: 3px">Trigger Email Reminder for Expired Active Job</td>
                            </tr>--%>

                        </table>
                        </form>

                    </td>
                </tr>
                </table>
            
            </td>
            <td width="10%">&nbsp;</td>
        </tr>
        </table></div>
    </body>
</html>
