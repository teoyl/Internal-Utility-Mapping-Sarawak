<%@page import="com.sains.common.util.AesUtil"%>
<%@page import="com.sains.common.util.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <link rel="icon" href="<s:text name="system.icon"/>" type="image/x-icon"/>
        <title><s:text name="system.name" /></title>
        <!--<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon" />-->
        <%--<link rel="stylesheet" type="text/css" href="styles/loginstyle.css"/>--%>
        <script src="include/jquery/jquery-3.4.1.min.js"></script>
        <script type="text/javascript" src="<s:url value="/pages/menu/simpletreemenu.js"/>"></script>
        <s:if test="usePubPrivateKey">
        <script language="JavaScript" type="text/javascript" src="include/crypto/jsencrypt.js"></script>
        </s:if>
        <link rel="stylesheet" type="text/css" href="<s:url value="/pages/menu/menutree.css"/>" />
        <link rel="stylesheet" type="text/css" href="<s:url value="/styles/style_internal.css"/>" />
        <%--<s:head />--%>
        <style type="text/css">
            @import url(styles/style_internal.css);
            <%--@import url(styles/msen_style_internal.css);--%>
        </style>
        <script type="text/javascript">
            function swkid_callback(returnObj) {
		console.log(returnObj);
            }
            function doLogin() {
                if ($('#passwd').val()==='') {
                    alert("Please enter password");
                    return false;
                }
                <s:if test="usePubPrivateKey">
                    var encrypt = new JSEncrypt();
                    encrypt.setPublicKey($('#pubkey').val());
                    var encrypted = encrypt.encrypt($('#passwd').val());
                    $('#passwd').val(encrypted)
                </s:if>
                return true;
            }
        </script>
    </head>
    <body>
        <form theme="simple" action="processMapSSOLogin" method="post">
            <table id="loginTable" cellspacing="0" border="0" align="center">
                <tr><td height="15px" colspan="3"></td></tr>
                <tr><td height="145px" align="center" colspan="3"><font style="font-size: 48px"><s:text name="system.name"/></font></td></tr>
                <tr><td height="30px" colspan="3"></td></tr>
                <tr><td height="34px" align="center" colspan="3"><font style="font-size: 20px"><s:text name="system.name1"/><br><s:text name="system.name2"/></font></td></tr>
                <tr><td height="32px" colspan="3"></td></tr>
                <tr>
                    <td></td>
                    <td align="center" id="login_frame" width="435px" rowspan="2" valign="middle">
                        <table cellspacing="0" cellpadding="0">
                            <tr><td height="50px" colspan="2"><jsp:include page="/pages/base/actionError.jsp"></jsp:include></td></tr>
                            <tr align="right">
                                <td>
                                    <%--<img height="15" width="114" src="<s:url value="/images/ess_login/username_text.png"/>" border="0">--%>
                                    <s:text name="common.username"/>&nbsp;:&nbsp;
                                </td>
                                <td id="login_username"><input type="text" name="userId" value="admin"></td>
                            </tr>
                            <tr><td colspan="2" height="5px"></td></tr>
                            <tr align="right">
                                <td>
                                    <%--<img height="15" width="114" src="<s:url value="/images/ess_login/password_text.png"/>" border="0"/>--%>
                                    <s:text name="common.password"/>&nbsp;:&nbsp;
                                </td>
                                <td id="login_password"><input type="password" id="passwd" name="passwd" value="password" autocomplete="off" maxlength="20"></td>
                            </tr>
                            <tr><td height="10px"></td></tr>
                            <tr>
                                <%--<s:set name="loginBtn"><s:url value="/images/login/login_button.png"/></s:set>--%>
                                <td colspan="2" align="right">

                                    <s:hidden name="org.apache.catalina.filters.CSRF_NONCE" value="%{#session.org.apache.catalina.filters.CSRF_NONCE}"/>
                                    <s:submit cssClass="defaultButton" theme="simple" value='%{getText("button.login")}' action="processMapSSOLogin" onclick="return doLogin();"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td></td>
                </tr>
                <tr id="table_bottom">
                    <td></td>
                    <td></td>
                </tr>
                <tr><td colspan="3" id="table_bottom" height="125px"></td></tr>
            </table>
        </form>
        <s:if test="usePubPrivateKey">
        <input type="hidden" id="pubkey" value="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCB8fiA+6c7OL+82UOtc0NA29CuMtVh7Mkgx9GCVMX9GR5A0fAKOP7qWOsCYSK/Q1Kkt33bxfsRSGuTf+KlzU4QCsn77AuDpoPX0As1XZzltXCrl++Kafd93WtMJsmUYsxfYw/nvbpPt/UY6TBXfkhwyUeiEkdzfoeoz/IBWG2lzwIDAQAB">
        </s:if>
        <script type="text/javascript" language="javascript">document.forms[0].userId.focus()</script>
    </body>
</html>