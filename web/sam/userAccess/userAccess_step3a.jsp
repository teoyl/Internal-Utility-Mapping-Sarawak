<%-- 
    Document   : userAccess_step3a
    Created on : Feb 20, 2014, 12:04:49 PM
    Author     : Delvene
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<script language="Javascript">
function validateSelect(form){
    if ( isCheckboxSelected(form.WorkflowGroup_selected)) {
        return true;
    } else {
        alert(messageAtLeastOneItem);
        return false;
    }
}
</script>

<table width="100%" class="form">
    <tr>
        <td>
            <form action="processSearchWfGroupUserAccess" method="post" id="wfGroupForm">
                <table cellspacing="0" cellpadding="5" border="0" width="100%">
                    <s:hidden theme="simple" name="id" />
                    <s:hidden theme="simple" name="model.ID" />
                    <s:hidden theme="simple" name="model.emp_id" />
                    <s:hidden theme="simple" name="currStep_" />

                    <s:hidden theme="simple" name="model.us_user_id" />
                    <s:hidden theme="simple" name="model.us_user_name" />
                    <s:hidden theme="simple" name="model.us_email" />
                    <s:hidden theme="simple" name="model.us_status" />
                    <s:hidden theme="simple" name="model.us_admin" />
                    <s:hidden theme="simple" name="model.us_division" />
                    <s:hidden theme="simple" name="model.us_last_login_date" />
                    <s:hidden theme="simple" name="model.us_ldap" />

                    <s:iterator value="model.wfGroupUserList" status="wfGroupUserStatus" id="wfGroupUser">
                        <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].ID' value='%{#wfGroupUser.ID}' />
                        <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].wg_id' value='%{#wfGroupUser.wg_id}' />
                        <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].workflowgroup.ID' value='%{#wfGroupUser.workflowgroup.ID}' />
                        <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].workflowgroup.wg_code' value='%{#wfGroupUser.workflowgroup.wg_code}' />
                        <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].workflowgroup.wg_name' value='%{#wfGroupUser.workflowgroup.wg_name}' />
                    </s:iterator>
                    <%--<tr>
                        <td align="left" class="button-container-list" colspan="2">
                            for line
                        </td>
                    </tr>--%>
                    <tr valign="top">
                        <td align="left">
                            <table>
                                <s:include value="/pages/base/dynamicSearch2.jsp">
                                    <s:param name="searchingParam" value="'WorkflowGroup'" />
                                    <s:param name="functionParam" value="'processSearchWfGroupUserAccess'" />
                                </s:include>
                            </table>
                        </td>
                        <td align="right">
                            <s:submit cssClass="defaultButton" theme="simple" value="%{getText('button.search')}"/>
                            <s:submit theme="simple" cssClass="defaultButton" action="cancelUserAccess" value="%{getText('button.cancel')}"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
    <tr>
        <td>
            <form id="sortForm" action="processSearchWfGroupUserAccess" method="post">
                <s:hidden theme="simple" name="id" />
                <s:hidden theme="simple" name="model.ID" />
                <s:hidden theme="simple" name="model.emp_id" />
                <s:hidden theme="simple" name="currStep_" />

                <s:hidden theme="simple" name="model.us_user_id" />
                <s:hidden theme="simple" name="model.us_user_name" />
                <s:hidden theme="simple" name="model.us_email" />
                <s:hidden theme="simple" name="model.us_status" />
                <s:hidden theme="simple" name="model.us_admin" />
                <s:hidden theme="simple" name="model.us_division" />
                <s:hidden theme="simple" name="model.us_last_login_date" />
                <s:hidden theme="simple" name="model.us_ldap" />

                <s:iterator value="model.wfGroupUserList" status="wfGroupUserStatus" id="wfGroupUser">
                    <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].ID' value='%{#wfGroupUser.ID}' />
                    <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].wg_id' value='%{#wfGroupUser.wg_id}' />
                    <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].workflowgroup.ID' value='%{#wfGroupUser.workflowgroup.ID}' />
                    <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].workflowgroup.wg_code' value='%{#wfGroupUser.workflowgroup.wg_code}' />
                    <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].workflowgroup.wg_name' value='%{#wfGroupUser.workflowgroup.wg_name}' />
                </s:iterator>

                <s:iterator value="getDynamicSearchField(#searchParam)" var="field" status="rowStatus">
                    <s:hidden theme="simple" name="search_%{#field}" value="%{getDynamicSearchData(#searchParam)[#rowStatus.index]}" />
                </s:iterator>
                <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
                <s:hidden theme="simple" name="action"/>
                <s:if test='getDynamicResult("WorkflowGroup").size() > 0'>
                    <s:hidden theme="simple" name="listSize" value="1"/>
                </s:if>
                <s:else>
                    <s:hidden theme="simple" name="listSize" value="0"/>
                </s:else>
            </form>
        </td>
    </tr>
</table>
<hr>
<form action='processAddWfGroupUserAccess' method="post">
    <table class="wwFormTable" cellpadding="3" cellspacing="0" border="0" width="100%">
        <s:hidden theme="simple" name="id" />
        <s:hidden theme="simple" name="model.ID" />
        <s:hidden theme="simple" name="model.emp_id" />
        <s:hidden theme="simple" name="currStep_" />

        <s:hidden theme="simple" name="model.us_user_id" />
        <s:hidden theme="simple" name="model.us_user_name" />
        <s:hidden theme="simple" name="model.us_email" />
        <s:hidden theme="simple" name="model.us_status" />
        <s:hidden theme="simple" name="model.us_admin" />
        <s:hidden theme="simple" name="model.us_division" />
        <s:hidden theme="simple" name="model.us_last_login_date" />
        <s:hidden theme="simple" name="model.us_ldap" />

        <s:iterator value="model.wfGroupUserList" status="wfGroupUserStatus" id="wfGroupUser">
            <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].ID' value='%{#wfGroupUser.ID}' />
            <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].wg_id' value='%{#wfGroupUser.wg_id}' />
            <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].workflowgroup.ID' value='%{#wfGroupUser.workflowgroup.ID}' />
            <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].workflowgroup.wg_code' value='%{#wfGroupUser.workflowgroup.wg_code}' />
            <s:hidden theme="simple" name='wfGroupUserList[%{#wfGroupUserStatus.index}].workflowgroup.wg_name' value='%{#wfGroupUser.workflowgroup.wg_name}' />
        </s:iterator>

        <tr>
            <td>
                <s:submit theme="simple" cssClass="defaultButton" action="processAddWfGroupUserAccess" value="%{getText('button.add.selected')}" onclick="return validateSelect(form)" />
            </td>
        </tr>
        <tr>
            <td>
                <s:include value="/pages/base/dynamicList2.jsp">
                    <s:param name="listingParam" value="'WorkflowGroup'" />
                </s:include>
            </td>
        </tr>
    </table>
</form>