<%--
    Document   : userAccess_step2
    Created on : Feb 19, 2014, 9:32:42 PM
    Author     : Delvene
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<sx:head parseContent="true" debug="false" />

<form method="post" id="prForm" class="prForm" action="processUpdateUserAccess" >
    <s:hidden theme="simple" name="action" />
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

    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
        <tr>
            <td align="right" class="button-container">
                <s:submit type="submit" cssClass="defaultButton buttonPrevStep" theme="simple" action="prevStepUserAccess" value="" title="%{getText('mohon.button.pre')}"/>
                <s:if test="showUpdateBtn_">
                    <s:submit type="submit" cssClass="defaultButton buttonSave" theme="simple" action="processUpdateUserAccess" value="%{getText('button.save')}"/>
                    <s:submit type="submit" cssClass="defaultButton buttonCancel" theme="simple" action="cancelChangesUserAccess" value="%{getText('button.cancel')}"/>
                </s:if>
                <s:else>
                    <s:submit type="submit" cssClass="defaultButton buttonNextStep" theme="simple" action="nextStepUserAccess" value="" title="%{getText('mohon.button.next')}"/>
                </s:else>
                <s:submit type="submit" cssClass="defaultButton buttonBackToList" theme="simple" action="cancelUserAccess" value="" title="%{getText('button.back')}"/>
            </td>
        </tr>
    </table>

    <table cellpadding="0" cellspacing="0" width="100%">
        <tr class="sub_header_bg">
            <td width="20px"></td>
            <td><s:text name="userAccess.userGroup"/></td>
        </tr>
        <tr>
            <td height="10px"><%--some space--%></td>
        </tr>
        <tr>
            <td></td>
            <td>
                <s:submit cssClass="defaultButton buttonAdd" theme="simple" action="loadAddPageUserAccess" value="Tambah" />
                <s:submit cssClass="defaultButton buttonDelete" theme="simple" action="deleteUserAccess" value="Hapus" onclick="if ( isCheckboxSelected(delGroup_ids)) {return confirmPermanentDelete();} else {return false;}"/>
            </td>
        </tr>
        <tr>
            <td height="10px"><%--some space--%></td>
        </tr>
    </table>

    <table class="defaultTable" cellpadding="0" cellspacing="0" width="100%">
        <tr>
            <th width="3px"></th>
            <th>
                <s:if test="model.groupUserList.size() > 0">
                    <input type="checkbox" id="group_select" name="group_select" onclick="toggleCheckbox(this, delGroup_ids);">
                </s:if>
                <s:else>
                    <input type="checkbox" id="group_select" name="group_select" disabled >
                </s:else>
            </th>
            <th><s:text name="userAccess.userGroup.code" /></th>
            <th><s:text name="userAccess.userGroup.name" /></th>
        </tr>
        <s:iterator value="model.groupUserList" status="groupUserStatus" id="groupUser">
            <tr class="<s:if test="#groupUserStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                <s:hidden theme="simple" name="groupUserList[%{#groupUserStatus.index}].ID" value="%{#groupUser.ID}" />
                <s:hidden theme="simple" name="groupUserList[%{#groupUserStatus.index}].ug_id" value="%{#groupUser.ug_id}" />
                <s:hidden theme="simple" name="groupUserList[%{#groupUserStatus.index}].userGroup.ID" value="%{#groupUser.userGroup.ID}" />
                <td></td>
                <td><s:checkbox theme="simple" name="group_selected" id="delGroup_ids" fieldValue="%{#groupUserStatus.index}" onclick="checkToggleCheckbox(group_select, delGroup_ids)"/></td>
                <td>
                    <s:hidden theme="simple" name="groupUserList[%{#groupUserStatus.index}].userGroup.group_code" value="%{#groupUser.userGroup.group_code}" />
                    <s:property value="%{#groupUser.userGroup.group_code}" />
                    <%--<s:textfield theme="simple" name="groupUserList[%{#groupUserStatus.index}].userGroup.group_code" value="%{#groupUser.userGroup.group_code}"/>--%>
                </td>
                <td>
                    <s:hidden theme="simple" name="groupUserList[%{#groupUserStatus.index}].userGroup.group_name" value="%{#groupUser.userGroup.group_name}" />
                    <s:property value="%{#groupUser.userGroup.group_name}" />
                    <%--<s:textfield size="50" theme="simple" name="groupUserList[%{#groupUserStatus.index}].userGroup.group_name" value="%{#groupUser.userGroup.group_name}"/>--%>
                </td>
            </tr>
        </s:iterator>
    </table>
</form>
