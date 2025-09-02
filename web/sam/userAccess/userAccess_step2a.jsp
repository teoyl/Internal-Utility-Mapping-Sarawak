<%--
    Document   : userAccess_step2a
    Created on : Feb 19, 2014, 10:42:13 PM
    Author     : Delvene
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<script language="Javascript">
function validateSelect(form){
    if ( isCheckboxSelected(form.UserGroup_selected)) {
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
            <form action="processSearchUserGroupUserAccess" method="post" id="userGroupForm">
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

                    <s:iterator value="model.groupUserList" status="groupUserStatus" id="groupUser">
                        <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].ID' value='%{#groupUser.ID}' />
                        <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].ug_id' value='%{#groupUser.ug_id}' />
                        <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].userGroup.ID' value='%{#groupUser.userGroup.ID}' />
                        <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].userGroup.group_code' value='%{#groupUser.userGroup.group_code}' />
                        <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].userGroup.group_name' value='%{#groupUser.userGroup.group_name}' />
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
                                    <s:param name="searchingParam" value="'UserGroup'" />
                                    <s:param name="functionParam" value="'processSearchUserGroupUserAccess'" />
                                </s:include>
                            </table>
                        </td>
                        <td align="right">
                            <s:submit cssClass="defaultButton" theme="simple" value="%{getText('button.search')}"/>
                            <s:submit theme="simple" cssClass="defaultButton " action="cancelUserAccess" value="%{getText('button.cancel')}"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
    <tr>
        <td>
            <form id="sortForm" action="processSearchUserGroupUserAccess" method="post">
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

                <s:iterator value="model.groupUserList" status="groupUserStatus" id="groupUser">
                    <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].ID' value='%{#groupUser.ID}' />
                    <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].ug_id' value='%{#groupUser.ug_id}' />
                    <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].userGroup.ID' value='%{#groupUser.userGroup.ID}' />
                    <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].userGroup.group_code' value='%{#groupUser.userGroup.group_code}' />
                    <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].userGroup.group_name' value='%{#groupUser.userGroup.group_name}' />
                </s:iterator>

                <s:iterator value="getDynamicSearchField(#searchParam)" var="field" status="rowStatus">
                    <s:hidden theme="simple" name="search_%{#field}" value="%{getDynamicSearchData(#searchParam)[#rowStatus.index]}" />
                </s:iterator>
                <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
                <s:hidden theme="simple" name="action"/>
                <s:if test='getDynamicResult("UserGroup").size() > 0'>
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
<form action='processAddUserGroupUserAccess' method="post">
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

        <s:iterator value="model.groupUserList" status="groupUserStatus" id="groupUser">
            <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].ID' value='%{#groupUser.ID}' />
            <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].ug_id' value='%{#groupUser.ug_id}' />
            <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].userGroup.ID' value='%{#groupUser.userGroup.ID}' />
            <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].userGroup.group_code' value='%{#groupUser.userGroup.group_code}' />
            <s:hidden theme="simple" name='groupUserList[%{#groupUserStatus.index}].userGroup.group_name' value='%{#groupUser.userGroup.group_name}' />
        </s:iterator>
        
        <tr>
            <td>
                <s:submit theme="simple" cssClass="defaultButton" action="processAddUserGroupUserAccess" value="%{getText('button.add.selected')}" onclick="return validateSelect(form)" />
            </td>
        </tr>
        <tr>
            <td>
                <s:include value="/pages/base/dynamicList2.jsp">
                    <s:param name="listingParam" value="'UserGroup'" />
                </s:include>
            </td>
        </tr>
    </table>
</form>