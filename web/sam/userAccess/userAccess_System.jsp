<%-- 
    Document   : userAccess_System
    Created on : Feb 18, 2014, 3:53:01 PM
    Author     : user
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<sx:head parseContent="true" debug="false" />
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>


<form method="post" id="prForm" class="prForm" action="nextStepUserAccess" >
    <s:hidden theme="simple" name="action" />
    <s:hidden theme="simple" name="id" />
    <s:hidden theme="simple" name="model.ID" />
    <s:hidden theme="simple" name="model.emp_id" />
    <s:hidden theme="simple" name="currStep_" />

    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
        <tr>
            <td class="button-container"> 
                <div class="step">
                    <span class="header1">${currentStep_}</span>
                </div>
            </td>
            <td align="right" class="button-container">
                <%--<s:submit type="submit" cssClass="defaultButton" theme="simple" action="nextStepUserAccess" value="%{getText('userAccess.view')}" title=""/>--%>
                <%--<s:submit type="submit" cssClass="defaultButton" theme="simple"  value="%{getText('userAccess.view')}" onclick="selectMultipleSystem();"/>--%>
                <s:submit type="submit" cssClass="defaultButton" theme="simple" action="prevStepUserAccess" value="%{getText('button.back')}"/>
                <s:submit type="submit" cssClass="defaultButton buttonGo" theme="simple"  value="%{getText('userAccess.view')}" onclick="if ( isCheckboxSelected(form.selected)) {return true;} else {return false};"/>
                <%--<s:submit type="submit" cssClass="defaultButton" theme="simple"  value="%{getText('userAccess.view')}" onclick="if (checkCheckBoxes(this)) {return true;} else {return false; }"/>--%>
                <s:submit type="submit" cssClass="defaultButton buttonBackToList" theme="simple" action="cancelUserAccess" value="" title="%{getText('button.back')}"/>
                
            </td>
        </tr>
    </table>

    <table cellpadding="0" cellspacing="0" width="100%" style="margin: 10px;">
        <s:hidden theme="simple" name="selectSysId_" value=""/>
        <s:iterator value="systemList_" id="system" status="systemStatus">
            <tr valign="top">
                <td width="15px"><!--some space--> </td>
                <td width="30px"><s:checkbox theme="simple" name="selected" id="ids" fieldValue="%{#system.system_id}" value='%{#system.sysHasAssign.equals("Y")?"True":"false"}' /></td>
                <td class="imp_label">${system.system_name}</td>
            </tr>
        </s:iterator>
    </table>
</form>
