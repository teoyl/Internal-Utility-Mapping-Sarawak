<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
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
            <div class="card">
                <div class="card-body">
            
                    <div class="row">
                        <div class="col-md-12 text-end">
                            <button class="btn btn-sm btn-primary" type="submit" name="action:processCloneUserGroup" id="processCloneUserGroup"  onclick="return validateForm(this.form, 'insert')"><i class="fa fa-save"></i> <span class="ms-1">Save</span></button>        
                            <button class="btn btn-sm btn-falcon-default buttonCancel" type="submit" name="action:loadEditPageUserGroup" id="loadEditPageUserGroup"><i class="fa fa-times"></i> <span class="ms-1">Cancel</span></button>    
                            <%--s:submit theme="simple" cssClass="defaultButton buttonSave" action="processCloneUserGroup" value="%{getText('button.save')}" onclick="return validateForm(this.form, 'insert')"/>
                            <s:submit theme="simple" cssClass="defaultButton buttonCancel" action="loadEditPageUserGroup" value="%{getText('button.back')}"/--%>
                        </div>
                    </div>
                    <h5><s:text name="group.copyFrom"/></h5>    
                    <div class="row">
                        <div class="col-md-4 mb-1">
                            <label class="form-label"><s:text name="group.code"/></label>
                            <s:property value="%{model.group_code}"/>
                        </div>
                        <div class="col-md-4 mb-1">
                            <label class="form-label"><s:text name="group.name"/></label>
                            <s:property value="%{model.group_name}"/>
                        </div>
                        <div class="col-md-4 mb-1">
                            <label class="form-label"><s:text name="group.type"/></label>
                            <s:text name="group.type.%{model.group_type}"/>
                        </div>
                    </div>
                        
                    <h5 class="mt-3"><s:text name="group.newGroupInformation"/></h5>

                    <div class="row">
                        <div class="col-md-4">
                            <div class="mb-1 required">
                                <label><s:text name="group.code"/></label>
                                <s:textfield theme="simple" name="tempSetupGroup.group_code" value="%{tempSetupGroup.group_code}" size="40" cssClass="form-control form-control-sm"/>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="mb-1 required">
                                <label class="form-label"><s:text name="group.name"/></label>
                                <s:textfield theme="simple" name="tempSetupGroup.group_name" value="%{tempSetupGroup.group_name}" size="100" cssClass="form-control form-control-sm"/>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="mb-1">
                                <label class="form-label"><s:text name="group.type"/></label>
                                <s:select cssClass="form-control form-control-sm sds-dropdown" list="groupTypeOption" listKey="keyData" listValue="valueData" theme="simple" name="tempSetupGroup.group_type" value="%{tempSetupGroup.group_type}"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
	</form>
</body>
</html>