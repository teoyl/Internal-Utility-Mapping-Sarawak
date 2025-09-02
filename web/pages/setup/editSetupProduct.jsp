<%-- 
    Document   : editSetupProduct
    Created on : Jul 5, 2010, 3:27:14 PM
    Author     : iveon
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<script type="text/javascript" src="pages/scripts/lookup.js"></script>
<script type="text/javascript" src="pages/scripts/controls.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
<script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
<script language="javascript">

function localValidateForm(form, operation) {
    var errors = new Array();
	validateRequired(form, errors);
    if (!isInteger(form.product_response_time.value))  {
        errors[errors.length] = formatText( "<s:text name='errors.digit' />", "<s:text name='product.responseTime' />");
   }
    if (!isDecimal(form.product_unit_cost.value))  {
        errors[errors.length] = formatText( "<s:text name='errors.digit' />", "<s:text name='product.unitCost' />");
   }

	if (errors.length > 0) {
		alert(errors.join('\n'));
		setFocus(form);
	}
	return errors.length > 0 ? false : true;
}
 function required(){
            this.aa = new Array("product_code", "<s:text name='product.code' />");
            this.ab = new Array("product_family_code", "<s:text name='product.familyCode' />");
            this.ac = new Array("product_name", "<s:text name='product.name' />");
        }
InitCalendar2("images/",false);

</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="system.name"/> - <s:text name="productSetup.title" /> - <s:text name="actionType.edit" /></title>
<%--<s:head />--%>
</head>
<body>
    <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
    <form theme="simple" action="processUpdateSetupProduct">
        <div class="titleFramework">
            <span class="titleText"><s:text name="productSetup.title" /></span>
            <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span><br>
        </div>
        <div class="xbox">
            <s:hidden name="action" />
            <s:hidden name="product_id" value="%{model.product_id}"/>
            <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                <tr>
                    <td align="right">
                        <s:submit type="button" cssClass="defaultButton" theme="simple" action="processUpdateSetupProduct" value="Save" onclick="return localValidateForm(this.form, 'update')"/>
                        <s:submit type="button" cssClass="defaultButton" theme="simple" action="cancelSetupProduct" value="Cancel"/>
                    </td>
                </tr>
            </table>
            <table>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.code"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:textfield theme="simple" name="product_code" value="%{model.product_code}"/> (e.g. MAP001) </td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.familyCode"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                    <td width="3px">:</td>
                    <td align="left">  <s:select name="product_family_code" list="familyCodeList" listKey="code_1" listValue="code_desc" value="%{model.product_family_code}"/></td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.name"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:textfield theme="simple"  size="100%" name="product_name" value="%{model.product_name}" /></td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.shortName"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:textfield theme="simple"  size="100%" name="product_short_name" value="%{model.product_short_name}" />
                    </td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.desc"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:textarea cssClass="textArea" theme="simple" cols="50"  rows="3" name="product_desc"  value="%{model.product_desc}"/></td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.responseTime"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:textfield theme="simple" name="product_response_time" value="%{model.product_response_time}" /></td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.remark"/></td>
                    <td width="3px">:</td>
                    <td align="left" ><s:textarea cssClass="textArea" theme="simple" cols="50"  rows="3"  name="product_remark" value="%{model.product_remark}" /></td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.format"/></td>
                    <td width="3px">:</td>
                    <td align="left">
                        <s:select name="product_format" list="productFormatList" listKey="code_1" listValue="code_desc" value="%{model.product_format}"/>
                    </td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.rvrcode"/></td>
                    <td width="3px">:</td>
                    <td align="left">
                        <s:textfield theme="simple" name="product_rvrcode" value="%{model.product_rvrcode}" size="3"/> /
                        <s:textfield theme="simple" name="product_subcode" value="%{model.product_subcode}" size="3" />
                        <%--<s:textfield theme="simple" name="product_rvrcode" value="%{model.product_rvrcode}" readonly="true"/> -
                        <s:textfield theme="simple" name="product_subcode" value="%{model.product_subcode}" readonly="true"/>
                        <script language="javascript">
                            lookup("Search Revenue Code", "Rvrcode", "trancode,trancode,description,description", "product_rvrcode,product_subcode,description,lbdescription",
                            "useSetup_Rvrcode", "trancode,description", "", "");
                        </script>
                        <input type="text" name="trancode" value="${trancode}" readonly />
                        <script language="javascript">
                            lookup("Search Revenue Code", "Rvrcode", "trancode,trancode,description,description", "trancode,product_rvrcode,description,lbdescription",
                            "useSetup_Rvrcode", "trancode,description", "", "");
                        </script> 
                        <s:hidden theme="simple" name="description" value="%{description}"/>
                        <label class="label" id="lbdescription" for="lbdescription">${description}</label>--%>
                    </td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.feeType"/></td>
                    <td width="3px">:</td>
                    <td align="left">  <s:select name="product_fee_type" list="feeTypeList" listKey="code_1" listValue="code_desc" value="%{model.product_fee_type}"/></td>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.unitCost"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:textfield theme="simple" name="product_unit_cost" value="%{model.product_unit_cost}" /></td>
                </tr>
                 <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.scale"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:textfield theme="simple" size="100%" name="product_scale" value="%{model.product_scale}" /></td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.download"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:checkbox theme="simple" name="product_download" fieldValue="Y" value='%{model.product_download.equals("Y")?"True":"false"}' /></td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.collection"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:checkbox name="product_collection" theme="simple" fieldValue="Y"  value='%{model.product_collection.equals("Y")?"True":"false"}'  /></td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.fixedCollection"/></td>
                    <td width="3px">:</td>
                    <td align="left">  <s:select name="product_fixed_collection" list="divisionList" listKey="code_1" listValue="code_desc" value="%{model.product_fixed_collection}"/></td>
                </tr>
                 <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.requestApproval"/></td>
                    <td width="3px">:</td>
                    <td align="left">
                       <%--<s:text name="product.approvalAuthority"/> &nbsp;--%>
                       <s:hidden name="product_request_approval"  value='%{model.product_request_approval}' />
                       <s:checkbox theme="simple" name="product_approval_lns" fieldValue="Y" value='%{model.product_approval_lns.equals("Y")?"True":"false"}'/>
                       <s:text name="product.approvalLns"/>&nbsp;&nbsp;&nbsp;&nbsp;
                       <s:checkbox theme="simple" name="product_approval_police" fieldValue="Y" value='%{model.product_approval_police.equals("Y")?"True":"false"}' />
                       <s:text name="product.approvalPolice"/>&nbsp;&nbsp;&nbsp;&nbsp;
                       <s:checkbox theme="simple" name="product_approval_ss" fieldValue="Y" value='%{model.product_approval_ss.equals("Y")?"True":"false"}' />
                       <s:text name="product.approvalSs"/>
                    </td>
                </tr>
               <%-- <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.requestApproval"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:checkbox theme="simple" name="product_request_approval" fieldValue="Y" value='%{model.product_request_approval.equals("Y")?"True":"false"}' />
                    </td>
                </tr>
                   <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.approvalAuthority"/></td>
                    <td width="3px">:</td>
                    <td align="left" valign="middle">
                       <s:checkbox theme="simple" name="product_approval_lns" fieldValue="Y" value='%{model.product_approval_lns.equals("Y")?"True":"false"}'/>
                       <s:text name="product.approvalLns"/>&nbsp;&nbsp;&nbsp;&nbsp;
                       <s:checkbox theme="simple" name="product_approval_police" fieldValue="Y" value='%{model.product_approval_police.equals("Y")?"True":"false"}' />
                       <s:text name="product.approvalPolice"/>&nbsp;&nbsp;&nbsp;&nbsp;
                       <s:checkbox theme="simple" name="product_approval_ss" fieldValue="Y" value='%{model.product_approval_ss.equals("Y")?"True":"false"}' />
                       <s:text name="product.approvalSs"/>
                    </td>
                </tr>--%>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.fileId"/></td>
                    <td width="3px">:</td>
                    <td align="left">
                         <s:hidden theme="simple" name="file_id" value="%{model.file_id}"/>
                         <input type="text" name="file_path" value="${filePath}" size="100%" readonly/>
                        <script language="javascript">
                            lookup("Search File Path", "SetupFile", "file_id,file_path", "file_id,file_path",
                            "useSetup_SetupFile", "file_path,file_desc", "", "");
                        </script>
                    </td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.autoPDF"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:checkbox theme="simple" name="product_auto_pdf" fieldValue="Y" value='%{model.product_auto_pdf.equals("Y")?"True":"false"}' /></td>
                </tr>
                 <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.multipleJob"/></td>
                    <td width="3px">:</td>
                    <td align="left"><s:checkbox theme="simple" name="multiple_job" fieldValue="Y" value='%{model.multiple_job.equals("Y")?"True":"false"}' /></td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <td width="150px" align=left><s:text name="product.parentId"/></td>
                    <td width="3px">:</td>
                    <td align="left">
                        <s:hidden theme="simple" name="product_parent_id" value="%{model.product_parent_id}"/>
                        <input type="text" name="parent_product_code" value="${parentProductCode}" size="6" readonly/>-
                        <input type="text" name="parent_product_name" value="${parentProductName}" size="100%" readonly/>
                        <script language="javascript">
                            <%--function lookup(title, query, lookFor, writeTo, lookupType, displayedColumns, focusOn, onclick, filterBy)--%>
                            lookup("Search Product", "SetupProduct", "product_id,product_code,product_name", "product_parent_id,parent_product_code,parent_product_name",
                            "useSetup_UserGroup",  "product_code,product_name", "", "");
                        </script>
                    </td>
                </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="product.refLink"/></td>
                        <td width="3px">:</td>
                        <td align="left"><s:textfield theme="simple" size="100%" name="product_ref_link" value="%{model.product_ref_link}" /></td>
                    </tr>
                    <tr>
                        <td width="20px">&nbsp;</td>
                        <td width="150px" align=left><s:text name="product.sampleLink"/></td>
                        <td width="3px">:</td>
                        <td align="left"><s:textfield theme="simple" size="100%" name="product_sample_link" value="%{model.product_sample_link}" /></td>
                    </tr>
            </table>
        </div>
    </form>
</body>
</html>
