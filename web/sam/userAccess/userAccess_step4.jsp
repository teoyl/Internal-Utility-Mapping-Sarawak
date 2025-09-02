<%-- 
    Document   : userAccess_step4
    Created on : Feb 20, 2014, 3:27:48 PM
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
    <s:hidden theme="simple" name="showDeptList_" />
    <s:hidden theme="simple" name="strUserDeptId_" />

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
                </s:if>
                <s:submit type="submit" cssClass="defaultButton buttonBackToList" theme="simple" action="cancelUserAccess" value="" title="%{getText('button.back')}"/>
            </td>
        </tr>
    </table>

    <table cellpadding="0" cellspacing="0" width="100%">
        <tr class="sub_header_bg">
            <td width="20px"></td>
            <td><s:text name="userAccess.secuGroup"/></td>
        </tr>
        <tr>
            <td height="10px"><%--some space--%></td>
        </tr>
        <tr>
            <td></td>
            <td>
                <s:if test="showAddSecuBtn_">
                    <s:submit cssClass="defaultButton buttonAdd" theme="simple" action="loadAddPageUserAccess" value="Tambah" />
                </s:if>
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
                <s:if test="model.userSecurityList.size() > 1">
                    <input type="checkbox" id="group_select" name="group_select" onclick="toggleCheckbox(this, delGroup_ids);">
                </s:if>
                <s:else>
                    <input type="checkbox" id="group_select" name="group_select" disabled >
                </s:else>
            </th>
            <th><s:text name="userAccess.secuGroup.dept" /></th>
            <th><s:text name="userAccess.secuGroup.level" /></th>
            <th><s:text name="userAccess.secuGroup.unit" /></th>
        </tr>
        <s:iterator value="model.userSecurityList" status="userSecuStatus" id="userSecu">
            <tr class="<s:if test="#userSecuStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                <s:hidden theme="simple" name="userSecurityList[%{#userSecuStatus.index}].ID" value="%{#userSecu.ID}" />
                <s:hidden theme="simple" name="userSecurityList[%{#userSecuStatus.index}].se_default" value="%{#userSecu.se_default}" />
                <td></td>
                <td>
                    <s:if test='!#userSecu.se_default.equals("Y")'>
                        <s:checkbox theme="simple" name="group_selected" id="delGroup_ids" fieldValue="%{#userSecuStatus.index}" onclick="checkToggleCheckbox(group_select, delGroup_ids)"/>
                    </s:if>
                </td>
                <td>
                    <a href="loadEditPageUserAccess?id=${id}&currStep_=4b&itemId_=${userSecu.ID}">
                        <s:if test="#userSecu.department.dept_kepala != null && #userSecu.department.dept_kepala != ''">
                            <s:property value="%{#userSecu.department.dept_kepala}" /> -
                        </s:if>
                         <s:property value="%{#userSecu.department.dept_name}" />
                    </a>
                </td>
                <td><s:property value="%{#userSecu.access_level_str}" /></td>
                <td><s:property value="%{#userSecu.establishment_bu.est_no}" /> <s:property value="%{#userSecu.establishment_bu.est_description}" /></td>
            </tr>
        </s:iterator>
    </table>
</form>