<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script language="javascript">
            function localValidateForm(form, operation) {
                var errors = new Array();
                validateRequired(form, errors);
                if (errors.length > 0) {
                    alert(errors.join('\n'));
                    setFocus(form);
                }
                return errors.length > 0 ? false : true;
            }
            function required() {
                this.aa = new Array("model.userModel.us_user_name", "<s:text name='qp.signature.name' />");

            }

            function setLatestVer() {
                if (confirmLatestVersion() == true) {
                    document.getElementById("latestVerFlag").innerHTML = 'Yes';
                    document.getElementById("latestVerFlag_").value = "Y";
                }
            }
            function confirmLatestVersion() {
                var answer = confirm("You are about to set this Signature as the latest version. Do you want to proceed?");
                return answer;
            }

        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--<title><s:text name="system.name"/> - <s:text name="qp.card.signature" /> - <s:text name="actionType.add" /></title>--%>
        <%--<s:head />--%>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
            <form action="processInsertSetupSignature" id="signatureId" name="signatureForm" method="post" enctype="multipart/form-data">
            <s:hidden name="action" />             
            <s:hidden name="ss_id" value="%{model.ss_id}"/>
            <s:hidden name="us_id" value="%{model.us_id}" />

            <div class="row">
                <div class="col-md-6">
                    <div class="form-group form-group-default required disabled">
                        <label><s:text name="qp.signature.name"/></label>
                        <div class="input-group">
                            <s:if test='!signerName_.equals("")'>
                                <s:textfield cssClass="form-control" id="us_user_name" name="model.userModel.us_user_name" value='%{signerName_}'/>                                
                            </s:if><s:else>
                                <s:textfield cssClass="form-control" id="us_user_name" name="model.userModel.us_user_name" value='%{model.userModel.us_user_name}'/>                                
                            </s:else>
                            <div class="input-group-btn">
                                <script language="javascript">
                                    lookup2("Search Internal User", "InternalUser", "us_user_name,us_id", "us_user_name,us_id",
                                            "useSetup_InternalUser", "us_user_name", "us_user_name", "", "");
                                </script> 
                            </div>
                        </div>
                    </div>   
                </div>
                <div class="col-md-6">
                    <div class="radio-group radio-group-default">
                        <label><s:text name="qp.card.acting"/></label><br/>
                        <div class="checkbox checkbox-inline check-success" style="margin-top:0px;margin-bottom:4px;">
                            <input type="checkbox" name="sign_acting" value='%{model.sign_acting.equals("Y")?"True":"false"}' id="sign_acting">
                            <label for="sign_acting"></label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <s:if test="model.sign_attach != ''">
                    <div class="col-md-6">
                        <div class="form-group form-group-default required">
                            <label><s:text name="common.attachment"/></label>
                            <s:hidden name="sign_attach" value="%{model.sign_attach}"/>
                            <s:url action="downloadSetupSignatureFile" id="fileDownload" namespace="/"></s:url>
                            <s:a href="%{fileDownload}?fileName=%{model.sign_attach}"><s:property value="%{model.sign_attach}"/></s:a>
                            </div>
                        </div>
                </s:if>
                <s:else>
                    <div class="col-md-6">
                        <div class="form-group form-group-default required">
                            <label><s:text name="common.attachment"/></label>
                            <s:file theme="simple" name="fileUpload"/>
                        </div>
                    </div> 
                </s:else>
                <div class="col-md-6">
                    <div class="form-group form-group-default">
                        <label><s:text name="tc.latest"/></label>
                        <s:hidden name="latestVerFlag_"/>
                        <s:if test="latestVerFlag_ != ''">
                            <span id="latestVerFlag">
                                <s:if test='latestVerFlag_.equals("Y")'><s:text name="tc.%{latestVerFlag_}"/></s:if> 
                                <s:if test='latestVerFlag_.equals("N")'><input type="button" value="Set as latest version" onclick="setLatestVer()" /></s:if>
                                </span>
                        </s:if><s:else>
                            <span id="latestVerFlag"><input type="button" value="Set as latest version" onclick="setLatestVer()" /></span>
                            </s:else>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 text-right">
                    <s:if test='model.ID == null || model.ID.equals("")'>
                        <button class="btn btn-primary" type="submit" name="action:processInsertSetupSignature" id="processInsertSetupSignature" onclick="return localValidateForm(this.form, 'insert')"><i class="fa fa-save"></i>Save</button>
                        <%--<s:submit type="button" cssClass="defaultButton  dynamic-pull mrg-lr-5" theme="simple" action="processInsertSetupSignature" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>--%>
                    </s:if>
                    <s:else>
                        <button class="btn btn-primary" type="submit" name="action:processUpdateSetupSignature" id="processUpdateSetupSignature" onclick="return localValidateForm(this.form, 'update')"><i class="fa fa-save"></i>Save</button>
                        <%--<s:submit type="button" cssClass="defaultButton  dynamic-pull mrg-lr-5" theme="simple" action="processUpdateSetupSignature" value="Update" onclick="return localValidateForm(this.form, 'update')"/>--%>
                    </s:else>
                    <button class="btn btn-default" type="submit" name="action:cancelSetupSignature" id="cancelSetupSignature"><i class="fa fa-close"></i>Cancel</button>
                    <%--<s:submit type="button" cssClass="defaultButton  dynamic-pull mrg-lr-5" theme="simple" action="cancelSetupSignature" value="Cancel"/>--%>
                </div>
            </div>

            <%--<div class="titleFramework">
                <span class="titleText"><s:text name="qp.card.signature" /></span>
                <span class="titleActionTypeText"> | <s:text name="actionType.edit" /></span><br>
            </div>
            <div class="xbox">--%>
            <%--div class="panel panel-default ">  
                <div class="panel-heading">
                    <h3 class="panel-title"> 
                        <span class="titleText"><s:text name="qp.card.signature" /></span>
                        <span class="titleActionTypeText"> | 
                            <s:if test='model.ID == null || model.ID.equals("")'>
                                <s:text name="actionType.add" />                        
                            </s:if>
                            <s:else>
                                <s:text name="actionType.edit" />
                            </s:else>
                        </span></h3>
                </div>
                <div class="panel-body">
                    <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form table borderless">
                        <tr>
                            <td align="right">
                                <s:if test='model.ID == null || model.ID.equals("")'>
                                    <s:submit type="button" cssClass="defaultButton  dynamic-pull mrg-lr-5" theme="simple" action="processInsertSetupSignature" value="Save" onclick="return localValidateForm(this.form, 'insert')"/>
                                </s:if><s:else>
                                    <s:submit type="button" cssClass="defaultButton  dynamic-pull mrg-lr-5" theme="simple" action="processUpdateSetupSignature" value="Update" onclick="return localValidateForm(this.form, 'update')"/>
                                </s:else>
                                <s:submit type="button" cssClass="defaultButton  dynamic-pull mrg-lr-5" theme="simple" action="cancelSetupSignature" value="Cancel"/>
                            </td>
                        </tr>
                    </table>
                    <table class="form table borderless">
                        <s:hidden name="action" />             
                        <s:hidden name="ss_id" value="%{model.ss_id}"/>
                        <s:hidden name="us_id" value="%{model.us_id}" />

                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td width="200px" align=left><s:text name="qp.signature.name"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                            <td width="3px">:</td>
                            <td>                           
                                <s:if test='!signerName_.equals("")'>
                                    <s:textfield theme="simple" size="50" id="us_user_name" name="model.userModel.us_user_name" value='%{signerName_}' disabled="true"/>                                
                                </s:if><s:else>
                                    <s:textfield theme="simple" size="50" id="us_user_name" name="model.userModel.us_user_name" value='%{model.userModel.us_user_name}' disabled="true"/>                                
                                </s:else>
                                <script language="javascript">
                                    <!--teaC_ = "<s:property value='%{getHc(actionClassName_,"User")}'/>";-->
                                    lookup("Search Internal User", "InternalUser", "us_user_name,us_id", "us_user_name,us_id",
                                            "useSetup_InternalUser", "us_user_name", "us_user_name", "", "");
                                </script> 
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td align=left><s:text name="qp.card.acting"/></td>
                            <td>:</td>
                            <td align="left">
                                <s:checkbox theme="simple" name="sign_acting" value='%{model.sign_acting.equals("Y")?"True":"false"}'/>
                            </td>
                        </tr>
                        <s:if test="model.sign_attach != ''">
                            <tr>
                                <td>&nbsp;</td>
                                <td align=left><s:text name="common.attachment"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                                <td>:</td>
                                <td align="left">
                                    <s:hidden name="sign_attach" value="%{model.sign_attach}"/>
                                    <s:url action="downloadSetupSignatureFile" id="fileDownload" namespace="/"></s:url>
                                    <s:a href="%{fileDownload}?fileName=%{model.sign_attach}"><s:property value="%{model.sign_attach}"/></s:a>
                                    </td>
                                </tr>
                        </s:if>
                        <s:else>
                            <tr>
                                <td>&nbsp;</td>
                                <td align=left><s:text name="common.attachment"/><jsp:include page="/pages/base/requiredField.jsp"/></td>
                                <td>:</td>
                                <td align="left">
                                    <s:file theme="simple" name="fileUpload"/>
                                </td>
                            </tr>
                        </s:else>
                        <tr>
                            <td>&nbsp;</td>
                            <td align=left><s:text name="tc.latest"/></td>
                            <td>:</td>
                            <td>
                                <s:hidden name="latestVerFlag_"/>
                                <s:if test="latestVerFlag_ != ''">
                                    <span id="latestVerFlag">
                                        <s:if test='latestVerFlag_.equals("Y")'><s:text name="tc.%{latestVerFlag_}"/></s:if> 
                                        <s:if test='latestVerFlag_.equals("N")'><input type="button" value="Set as latest version" onclick="setLatestVer()" /></s:if>
                                        </span>
                                </s:if><s:else>
                                    <span id="latestVerFlag"><input type="button" value="Set as latest version" onclick="setLatestVer()" /></span>
                                    </s:else>
                            </td>
                        </tr>
                    </table>
                </div>
            </div--%>
        </form>                    
    </body>
</html>