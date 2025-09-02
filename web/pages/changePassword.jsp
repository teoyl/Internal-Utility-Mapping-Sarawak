<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<html>
    <head>
        <jsp:include page="/include/crypto/crypto.jsp"></jsp:include> <%--ThoTH @ 3-Apr-2014--%>
            <style type="text/css">
                @import url(styles/impian_style_internal_annoucement.css);
                @import url(styles/impian_style_login_ess.css);
            </style>
            <script type="text/javascript" src="pages/scripts/common.js"></script>
            <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
            <script type="text/javascript" src="pages/scripts/lookup.js"></script>
            <script type="text/javascript" src="pages/scripts/controls.js"></script>
            <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
            <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
            <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
            <script language="javascript">
                function localValidateForm(form) {
                    var errors = new Array();
                    validateRequired(form, errors);
                    
                    <%--ICTU GUIDE: PASSWORD ATLEAST 12 CHARACTERS--%>
//                    alert(document.getElementById("passwd").value);        
                    passwordCheck(document.getElementById("passwd"), "Kata Laluan Baru", errors, 12, 16);
                    if (trim(document.getElementById("passwd").value) != trim(document.getElementById("confirm_passwd_").value)) {
                        errors[errors.length] = "<s:text name='accountActivate.error.comfirmPasswordX'/>";
                    }
                     <%--ICTU GUIDE: PASSWORD CANNOT BE SAME AS USER ID--%>
                    if (trim(document.getElementById("userId").value).toUpperCase() == trim(document.getElementById("passwd").value).toUpperCase()) {
                        errors[errors.length] = "<s:text name='accountActivate.errors.passwordSameAsUserID'/>";
                    }
                    
                    if (errors.length > 0) {
                        alert(errors.join('\n'));
                        setFocus(form);
                    }
                    return errors.length > 0 ? false : true;
//                    return false;

                }
                function required() {
                    this.ad = new Array("passwd", "<s:text name='user.newPassword' />");
                    this.ae = new Array("confirm_passwd_", "<s:text name='accountActivate.confirmNewPassword' />");
                }
                
                function loadLoginPage(){
                    document.forms["loginForm"].action="initLogin";
//                    document.forms["loginForm"].action="initLoginESS";//Change to EQP 29-Aug-2016
                    document.forms["loginForm"].submit();
                }
        </script>
    </head>

    <body style=" background-image: url(images/loginBg.png)">
        <table style="height: 100%" width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
            <!--<form id="loginForm" theme="simple" action="cancelLogin" method="post" enctype="multipart/form-data">-->
            <td align="center" height="90px" id="banner_imp" >
                <s:if test='systemType_.equals("ESS")'>
                    <page:applyDecorator page="/main/internalMainHeader_1.jsp" name="panel1" />
                </s:if>
                <s:else>
                    <page:applyDecorator page="/main/internalMainHeader.jsp" name="panel1" />
                </s:else>
            </td>
            <tr>
                <td style="border-top: 3px solid #A2C4C9;">&nbsp;</td>
            </tr>
            <tr>
                <td height="15px"><%--some space--%></td>
            </tr>
            <tr>
                <td align="center" valign="top" height="585px">
                    <div class="main-box-bg">
                        <table >
                            <tr>
                                <td> 
                                    <table cellpadding="3" cellspacing="0" width="100%" >
                                        <tr class="supportTitle">
                                            <td width="600px">Pengaktifan Akaun : Sila set Kata Laluan anda </td>
                                            <td id='papar' >
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr class="content_info xbox">
                                <td>
                                    <div  class="login-text">
                                        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>    
                                            <form id="loginForm" action="processActivateLoginESS" method="post" onSubmit="return false;">    
                                                <table>
                                                    <tr><td >ID Pengguna</td></tr>
                                                    <tr><td height="54">
                                                        <s:hidden theme="simple" name="userId" />
                                                        <s:hidden theme="simple" name="strActivationCode_" />
                                                        <input disabled="true" type="text" id="userId" name="userId" placeholder="No. Kad Pengenalan/ No. Pasport (bukan warganegara): " value='<s:property value="%{userId}"/>' >
                                                    </td></tr>
                                                <tr><td ></td></tr>
                                                <tr><td ><s:text name="user.newPassword" /></td></tr>
                                                <!--<tr><td height="54"><input type="password" size="10px" id="passwd" name="passwd" onchange="vldtPassword(this.value, '%{getText('user.newPassword')}', this, '12', '16', 'Y','Y')"></td></tr>-->
                                                <tr><td height="54"><s:password cssClass="requiredField" theme="simple" id="passwd" name="passwd" onchange="vldtPassword(this.value, '%{getText('user.newPassword')}', this, '12', '16', 'Y','Y')" /></td></tr>
                                                <tr><td ><s:text name="accountActivate.confirmNewPassword" /></td></tr>
                                                <tr><td height="54"><input type="password" size="10px" id="confirm_passwd_" name="confirm_passwd_" ></td></tr>
                                                <tr><td>   
                                                        <s:submit name="activeBtn" id="activeBtn" type="submit" theme="simple" value="%{getText('button.submit')}" src='%{#loginBtn}' action="processActivateLoginESS" />
                                                        <button id="loginBtn" onclick="loadLoginPage()" class="align_right" type="reset" value="<s:text name="button.cancel"/>"><s:text name="button.cancel"/></button>
                                                    </td>
                                                </tr>
                                            </table>
                                        </form>        
                                    </div>
                                </td>
                            </tr>

                        </table>
                    </div>         
                </td>
            </tr>
            <tr>
                <td height="auto">&nbsp;</td>
            </tr>
            <tr>
                <td align="center" id="footer_imp"><jsp:include page="/main/internalMainFooter_1.jsp" /></td>
            </tr>
        </form>
    </table>
</body>
</html>