<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<SCRIPT language="Javascript">
function required(){
    this.aa = new Array("group_code", "<s:text name='group.code' />");
    this.ab = new Array("group_name", "<s:text name='group.name' />");
}
</SCRIPT>
<%--<s:head />--%>
</head>
<body>
	<jsp:include page="/pages/base/actionError.jsp"></jsp:include>
	<form theme="simple" action="processCloneUserGroup" name="userGroupForm" method="post">
            <s:hidden name="action" />
            <s:hidden theme="simple" name="model.ID" value="%{model.ID}"/>
            <s:hidden theme="simple" name="id" value="%{model.ID}"/>
                            
            
            <div class="row">
                <div class="col-md-12 text-right">
                    <button class="btn btn-primary" type="submit" name="action:processCloneUserGroup" id="processCloneUserGroup"  onclick="return validateForm(this.form, 'insert')"><i class="fa fa-save"></i>Save</button>        
                    <button class="btn btn-default buttonCancel" type="submit" name="action:loadEditPageUserGroup" id="loadEditPageUserGroup"><i class="fa fa-close"></i>Cancel</button>    
                    <%--s:submit theme="simple" cssClass="defaultButton buttonSave" action="processCloneUserGroup" value="%{getText('button.save')}" onclick="return validateForm(this.form, 'insert')"/>
                    <s:submit theme="simple" cssClass="defaultButton buttonCancel" action="loadEditPageUserGroup" value="%{getText('button.back')}"/--%>
                </div>
            </div>
            <h3 class="title-v2"><s:text name="group.copyFrom"/></h3>    
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group form-group-default viewOnly">
                        <label><s:text name="group.code"/></label>
                        <s:property value="%{model.group_code}"/>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group form-group-default viewOnly">
                        <label><s:text name="group.name"/></label>
                        <s:property value="%{model.group_name}"/>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group form-group-default viewOnly">
                        <label><s:text name="group.type"/></label>
                        <s:text name="group.type.%{model.group_type}"/>
                    </div>
                </div>
            </div><br><br>
            <h3 class="title-v2"><s:text name="group.newGroupInformation"/></h3>
            
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group form-group-default required">
                        <label><s:text name="group.code"/></label>
                        <s:textfield theme="simple" name="tempSetupGroup.group_code" value="%{tempSetupGroup.group_code}" size="40" cssClass="form-control"/>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group form-group-default required">
                        <label><s:text name="group.name"/></label>
                        <s:textfield theme="simple" name="tempSetupGroup.group_name" value="%{tempSetupGroup.group_name}" size="100" cssClass="form-control"/>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group form-group-default form-group-default-select2">
                        <label><s:text name="group.type"/></label>
                        <s:select cssClass="full-width"  data-init-plugin="select2" list="groupTypeOption" listKey="keyData" listValue="valueData" theme="simple" name="tempSetupGroup.group_type" value="%{tempSetupGroup.group_type}"/>
                    </div>
                </div>
            </div>
            
            
            <%--div class="titleFramework">
                <span class="titleText"><s:text name="userGroup" /></span>
                <span class="titleActionTypeText"> | <s:text name="actionType.copy" /></span><br>
            </div>
            
            <div class="xbox" >
                <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                    <tr>
                        <td align="right">
                            <s:submit theme="simple" cssClass="defaultButton buttonSave" action="processCloneUserGroup" value="%{getText('button.save')}" onclick="return validateForm(this.form, 'insert')"/>
                            <s:submit theme="simple" cssClass="defaultButton buttonCancel" action="loadEditPageUserGroup" value="%{getText('button.back')}"/>
                        </td>
                    </tr>
                </table>

                <table>
                    <tr>
                        <td colspan="3"/>
                        <td align="left"><u><b><s:text name="common.copyFrom"/></b></u>
                           
                        </td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="50px" align=left><s:text name="group.code"/></td>
                        <td width="3px">:</td>
                        <td align="left">${model.group_code}</td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="50px" align=left><s:text name="group.name"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                        <td>:</td>
                        <td>${model.group_name}</td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="50px" align=left><s:text name="group.type"/></td>
                        <td>:</td>
                        <td><s:text name="group.type.%{model.group_type}"/></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="50px" align=left><s:text name="group.dept"/></td>
                        <td>:</td>
                        <td>${model.dept_id}</td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="50px" align=left><s:text name="group.system"/></td>
                        <td>:</td>
                        <td>${model.system_id}</td>
                    </tr>
                </table>
                <hr>
                <table>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="50px" align=left><s:text name="group.code"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                        <td width="3px">:</td>
                        <td align="left"><s:textfield id="group_code" theme="simple" name="tempSetupGroup.group_code" value="%{tempSetupGroup.group_code}" size="40"/></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="50px" align=left><s:text name="group.name"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                        <td>:</td>
                        <td><s:textfield theme="simple" id="group_name" name="tempSetupGroup.group_name" value="%{tempSetupGroup.group_name}" size="100"/></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="50px" align=left><s:text name="group.type"/></td>
                        <td>:</td>
                        <td><s:select list="groupTypeOption" listKey="keyData" listValue="valueData" theme="simple" name="tempSetupGroup.group_type" value="%{tempSetupGroup.group_type}" /></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="50px" align=left><s:text name="group.dept"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                        <td>:</td>
                        <td><s:textfield theme="simple" id="group_dept" name="tempSetupGroup.dept_id" value="%{tempSetupGroup.dept_id}"/></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="50px" align=left><s:text name="group.system"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                        <td>:</td>
                        <td><s:textfield theme="simple" id="group_system" name="tempSetupGroup.system_id" value="%{tempSetupGroup.system_id}"/></td>
                    </tr>
                </table>
            </div--%>
	</form>
</body>
</html>