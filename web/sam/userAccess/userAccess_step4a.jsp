<%-- 
    Document   : userAccess_step4a
    Created on : Feb 21, 2014, 2:43:36 PM
    Author     : Delvene
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<script language="javascript">
function localValidateForm(form) {
    var errors = new Array();
    validateRequired(form, errors);

    if(document.getElementById("defSecuLvlDesc").value === "") {
        errors[errors.length++] = formatText(messageRequired, "<s:text name='userAccess.secuGroup.level' />");
        
    } else if(document.getElementById("defSecuLvlDesc").value === "${secuLevel_Unit}" && 
              document.getElementById("est_id_bu").value === "") {
        errors[errors.length++] = formatText(messageRequired, "<s:text name='userAccess.secuGroup.unit' />");
    }
    

    if (errors.length > 0) {
        alert(errors.join('\n'));
        setFocus(form);
    }
    return errors.length > 0 ? false : true;
}

function required(){
    this.aa = new Array("model.userSecurityList[0].dept_id", "<s:text name='userAccess.secuGroup.dept' />");
}

function setDefaultAccessLevel(deptID) {
    var userDeptID = document.getElementById("strUserDeptId_").value;
    if(deptID === userDeptID) {
        document.getElementById("defSecuLvl").disabled = true;
        document.getElementById("defSecuLvlDesc").disabled = false;
        document.getElementById("defSecuLvlDesc").value = "";
        document.getElementById("showUnitSeach").style.display = "inline";
        document.getElementById("est_description_bu").disabled = false;
    } else {
        document.getElementById("defSecuLvl").disabled = false;
        document.getElementById("defSecuLvl").value = ${secuLevel_Department};
        document.getElementById("defSecuLvlDesc").disabled = true;
        document.getElementById("defSecuLvlDesc").value = ${secuLevel_Department};
        document.getElementById("showUnitSeach").style.display = "none";
        document.getElementById("est_description_bu").disabled = true;
    }
}

function setDefaultUnit(accessLevel) {
    if(accessLevel === "${secuLevel_Unit}) {
        document.getElementById("showUnitSeach").style.display = "inline";
        document.getElementById("showUnitCompulsory").style.display = "inline";
        document.getElementById("est_description_bu").disabled = false;
        document.getElementById("est_description_bu").style.border = "1px solid #FF0000";
    } else {
        document.getElementById("showUnitSeach").style.display = "none";
        document.getElementById("showUnitCompulsory").style.display = "none";
        document.getElementById("est_description_bu").disabled = true;
        document.getElementById("est_description_bu").value = "";
        document.getElementById("est_description_bu").style.border = "1px solid #DDD";
        document.getElementById("est_id_bu").value = "";
    }
}
</script>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<sx:head parseContent="true" debug="false" />

<form method="post" id="userAccForm" class="postForm" action="processUpdateUserAccess" >
    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
        <tr>
            <td align="left" class="button-container">
               <jsp:include page="/pages/base/requiredField.jsp"/> denotes mandatory field.
            </td>
            <td align="right" class="button-container">
                <s:if test='currentViewModel_.ID == null || currentViewModel_.ID.equals("")'>
                    <s:submit type="submit" cssClass="defaultButton buttonSave" theme="simple" action="processInsertUserAccess" value="%{getText('button.save')}" onclick="return localValidateForm(this.form)"/>
                </s:if>
                <s:else>
                    <s:submit type="submit" cssClass="defaultButton buttonSave" theme="simple" action="processUpdateUserAccess" value="%{getText('button.save')}" onclick="return localValidateForm(this.form)"/>
                </s:else>
                <s:submit type="submit" cssClass="defaultButton buttonCancel" theme="simple" action="cancelUserAccess" value="%{getText('button.cancel')}"/>
            </td>
        </tr>
    </table>

    <table cellpadding="2" cellspacing="0" width="100%">
        <s:hidden theme="simple" name="model.userSecurityList[0].ID" value="%{currentViewModel_.ID}" />
        <s:hidden theme="simple" name="model.userSecurityList[0].us_id" value="%{currentViewModel_.us_id}" />
        <s:hidden theme="simple" name="model.userSecurityList[0].se_default" value="%{currentViewModel_.se_default}" />
        <%--<tr>
            <td height="5px">some space</td>
        </tr>
        <tr>
            <td></td>
            <td colspan="3">
                <img alt="" src="images/info.png" title="Maklumat Am" align="absmiddle" height="17" width="17">&nbsp;
                <s:text name="userAccses.deptInfo" />
            </td>
        </tr>--%>
        <tr>
            <td height="5px"><%--some space--%></td>
        </tr>
        <tr>
            <td width="20px"></td>
            <td width="150px" class="label_imp"><s:text name="userAccess.secuGroup.dept" /><jsp:include page="/pages/base/requiredField.jsp"/></td>
            <td width="10px" class="label_imp">:</td>
            <td>
                <s:if test="showDeptList_">
                    <s:select list="departmentOption" id="dept_id" listKey="keyData" listValue="valueData" theme="simple" cssClass="requiredField" name="model.userSecurityList[0].dept_id" value="%{currentViewModel_.dept_id}" onchange="setDefaultAccessLevel(this.value)"/>
                </s:if>
                <s:else>
                    <s:hidden theme="simple" name="model.userSecurityList[0].dept_id" id="dept_id" value="%{currentViewModel_.dept_id}" />
                    <s:select list="departmentOption" listKey="keyData" listValue="valueData" disabled="true" theme="simple" cssClass="requiredField" name="model.userSecurityList[0].dept_id" value="%{currentViewModel_.dept_id}"/>
                </s:else>
            </td>
        </tr>
        <tr>
            <td></td>
            <td class="label_imp"><s:text name="userAccess.secuGroup.level" /><jsp:include page="/pages/base/requiredField.jsp"/></td>
            <td class="label_imp">:</td>
            <td>
                <s:if test="showAccessList_">
                    <s:hidden theme="simple" id="defSecuLvl" disabled="true" name="model.userSecurityList[0].access_level" />
                    <s:if test='currentViewModel_.se_default.equals("N")'>
                        <s:select list="userSecurityOption" id="defSecuLvlDesc" listKey="keyData" listValue="valueData" theme="simple" cssClass="requiredField" name="model.userSecurityList[0].access_level" value="%{currentViewModel_.access_level}" onchange="setDefaultUnit(this.value)"/>
                    </s:if>
                    <s:else>
                        <s:select list="userSecurityOption" id="defSecuLvlDesc" listKey="keyData" listValue="valueData" theme="simple" cssClass="requiredField" name="model.userSecurityList[0].access_level" value="%{currentViewModel_.access_level}"/>
                    </s:else>
                </s:if>
                <s:else>
                    <s:hidden theme="simple" id="defSecuLvl" name="model.userSecurityList[0].access_level" value="%{currentViewModel_.access_level}" />
                    <s:select list="userSecurityOption" id="defSecuLvlDesc" listKey="keyData" listValue="valueData" disabled="true" cssClass="requiredField" theme="simple" name="model.userSecurityList[0].access_level" value="%{#secuLevel_Department}"/>
                </s:else>
            </td>
        </tr>
        <tr>
            <td></td>
            <td class="label_imp">
                <s:text name="userAccess.secuGroup.unit" />
                <div id="showUnitCompulsory" style="display: none">
                    <jsp:include page="/pages/base/requiredField.jsp"/>
                </div>
            </td>
            <td class="label_imp">:</td>
            <td>
                <input type="hidden" name="est_type" value="u" id="est_type"/>
                <s:hidden name="model.userSecurityList[0].est_id" id="est_id_bu" value="%{currentViewModel_.est_id}" />
                <s:if test="showUnitSearch_">
                    <%--<s:textfield theme="simple" size="10" name="model.userSecurityList[0].establishment_bu.est_description" readonly="true" id="est_description_bu" value="%{currentViewModel_.establishment_bu.est_description}"/>--%>
                    <s:textarea theme="simple" name="model.userSecurityList[0].establishment_bu.est_description" readonly="true" cols="40" rows="1" id="est_description_bu" cssClass="textarea_lookup" value="%{currentViewModel_.establishment_bu.est_description}"/>
                    <div id="showUnitSeach" style="display: inline">
                        <script language="javascript">
                            lookupH("<s:text name="actionType.search"/> <s:text name="postOper.sectionUnit"/>", "PtEstablishment", "est_id,noAndDesc", "est_id_bu,est_description_bu",
                            "useSetup_PtEstablishment", "est_code, est_description", "", "true", "dept_id,est_type");
                        </script>
                    </div>
                </s:if>
                <s:else>
                    <%--<s:textfield theme="simple" size="10" name="model.userSecurityList[0].establishment_bu.est_description" disabled="true" readonly="true" id="est_description_bu" value="%{currentViewModel_.establishment_bu.est_description}"/>--%>
                    <s:textarea theme="simple" name="model.userSecurityList[0].establishment_bu.est_description" disabled="true" readonly="true" cols="40" rows="1" id="est_description_bu" cssClass="textarea_lookup" value="%{currentViewModel_.establishment_bu.est_description}"/>
                    <s:if test='currentViewModel_.se_default.equals("N")'>
                        <div id="showUnitSeach" style="display: none">
                            <script language="javascript">
                                lookupH("<s:text name="actionType.search"/> <s:text name="postOper.sectionUnit"/>", "PtEstablishment", "est_id,noAndDesc", "est_id_bu,est_description_bu",
                                "useSetup_PtEstablishment", "est_code, est_description", "", "true", "dept_id,est_type");
                            </script>
                        </div>
                    </s:if>
                </s:else>
            </td>
        </tr>
        <tr>
            <td height="3px"><%--some space--%></td>
        </tr>
    </table>

    <s:hidden theme="simple" name="action" />
    <s:hidden theme="simple" name="id" />
    <s:hidden theme="simple" name="model.ID" />
    <s:hidden theme="simple" name="model.emp_id" />
    <s:hidden theme="simple" name="currStep_" />
    <s:hidden theme="simple" name="showDeptList_" />
    <s:hidden theme="simple" name="strUserDeptId_" />
    <s:hidden theme="simple" name="itemId_" />

    <s:hidden theme="simple" name="model.us_user_id" />
    <s:hidden theme="simple" name="model.us_user_name" />
    <s:hidden theme="simple" name="model.us_email" />
    <s:hidden theme="simple" name="model.us_status" />
    <s:hidden theme="simple" name="model.us_admin" />
    <s:hidden theme="simple" name="model.us_division" />
    <s:hidden theme="simple" name="model.us_last_login_date" />
    <s:hidden theme="simple" name="model.us_ldap" />
</form>