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
        <!--<link rel="stylesheet" type="text/css" href="<s:url value="/pages/menu/menutree.css"/>" />-->
        <!--<link rel="stylesheet" type="text/css" href="<s:url value="/styles/style_internal.css"/>" />-->
        <%--<s:head />--%>
        <link href="include/bootstrap/bootstrap.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" href="<s:url value="styles/lxg_login_style.css"/>" />
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
        <script src="include/bootstrap/bootstrap.js"></script>

        <%
            String strStateCode = (session.getAttribute("sesStateCode") == null ? "" : session.getAttribute("sesStateCode").toString());
            if (strStateCode.equals("")) {
                strStateCode = AesUtil.random(10);
                session.setAttribute("sesStateCode", strStateCode);
            }
        %>
        <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
            <script src="<%=SystemConstants.SarawakID.PLUGIN_URL[SystemConstants.ENV]%>"></script>
        </s:if>
        <script type="text/javascript" nonce="r4DjhKbfO5ry">
            $(document).ready(function () {
                $('.fimLoginBtn').on('click', function (e) {
                    fimLogin();
                });
            });
            <s:if test="@com.SysConf@get('fim.enabled').equalsIgnoreCase('y')">
            function fimLogin() {
                window.location = "<s:property value="@com.SysConf@get('fim.AUTHORIZE_URL')"/>?response_type=code&client_id=<s:property value="@com.SysConf@get('fim.CLIENT_ID')"/>&redirect_uri=<s:property value="@com.SysConf@get('fim.REDIRECT_URL')"/>&scope=<s:property value="@com.SysConf@get('fim.SCOPE')"/>&state=<%=strStateCode%>";
                    }
            </s:if>
            <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
                    document.addEventListener("DOMContentLoaded", function () {
                        swkid_sso_init({
                            client_id: '<%=SystemConstants.SarawakID.CLIENT_ID[SystemConstants.ENV]%>',
                            state: '<%=strStateCode%>',
                            response_type: 'code',
                            redirect_uri: '<%=SystemConstants.SarawakID.SYSTEM_URL[SystemConstants.ENV]%>/ssoVerifyLogin',
                            logout_redirect_uri: '<%=SystemConstants.SarawakID.SYSTEM_URL[SystemConstants.ENV]%>/processlogoutLogout',
                            logout_uri: '<%=SystemConstants.SarawakID.SYSTEM_URL[SystemConstants.ENV]%>/processlogoutLogout',
                            //                style:'icon-text',
                            icon_width: '30',
                            //                position:'',
                            misc_param: 'test',
                            profile_listing_style: 'details',
                            //                profile_listing_email:'on',
                            //                force_login_btn: 'off'
                        });
                        //            swkid_login_form_submit();
                    });
            </s:if>
                    function swkid_callback(returnObj) {
                        if (returnObj.swkid_login_status == "after_swkid_login") {
                            if (!$("#swkid-profile-picture").hasClass("swkid-profile-picture")) {
                                login_form_submit();
                            }
                        }
                    }
                    function doLogin() {
                        if ($('#passwd').val() === '') {
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
                    $(document).ready(function () {
            <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
                        if ($("#swkid-profile-picture").hasClass("swkid-profile-picture")) {
                            login_form_submit();
                        }
            </s:if>
                    });
        </script>
        <style nonce="EuTVqS192VKl">
            .fimlogin{
                font: 11px ;
            }
            
        </style>
    </head>
    <!--<body class="hold-transition login-page login-bg">-->
    <body >
        <form theme="simple" action="processloginLogin" method="post" >
            <div class="wrapper fadeInDown">
                <!-- Tabs Titles -->
                <!--    <h2> Sign In </h2>-->
                <div id="formContent">
                    <div id="formHeader">
                        <!-- Icon -->
                        <div class="fadeIn first">
                            <div class="col-xs-12">
                                <div class="row">
                                    <div class="col-xs-3"><img src="images/lns_logo.png" width="115px"  height="115px"/></div>
                                    <div class="col-xs-9"><h3><span><s:text name="system.name"/></span></h3></div>
                                </div>  
                            </div>
                        </div>
                    </div>
                    <!-- Login Form -->

                    <div id="formBody">
                        <!--<div class="card-body login-card-body">-->
                        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
                        <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
                            <div id="swkid_plugin"></div>
                        </s:if>

                        <div class="form-group">
                            <!--<div class="input-group mb-3">-->
                            <label for="username" class="col-xs-12 label-login text-18"><s:text name="common.username"/></label>
                            <s:if test="@com.sains.common.util.SystemConstants@ENV == 0">
                                <input type="text" name="userId" value="sainsadmin" class="fadeIn second" class="form-control" id="username" >
                            </s:if>
                            <s:else>
                                <input type="text" name="userId" value="" class="fadeIn second" class="form-control" id="username" />
                            </s:else>
                            <div class="input-group-append">
                                <div class="input-group-text">
                                    <span class="fas fa-users"></span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="password" class="label-login col-xs-12 text-18 "><s:text name="common.password"/></label>
                            <s:if test="@com.sains.common.util.SystemConstants@ENV == 0"> 
                                <input class="fadeIn third" type="password" id="passwd" name="passwd" value="password"  autocomplete="off" maxlength="20"/>
                            </s:if><s:else>
                                <input class="fadeIn third" type="password" id="passwd" name="passwd" value="" autocomplete="off" maxlength="20"/>
                            </s:else>
                        </div>
                            <div class="form-group">
                                <div class="col-xs-12">
                                <!--<div class="input-group-append">-->
                                    <div class="input-group-text">
                                        <p class="label-login text-12"><i><s:text name="common.footer"/></i></p>
                                        <p class="label-login text-12"><i><a href="#" ><s:text name="common.fim"/></p>
                                        <span class="fas fa-lock"></span>
                                    </div>
                                <!--</div>-->
                                </div>
                            </div>
                    </div>
                    <%-----------------------------button-----------------------------------------------------------------------%>
                    <div id="formFooter">
                        <s:if test="@com.SysConf@get('fim.enabled').equalsIgnoreCase('y')">
                            <p><a href="#" class="fimLoginBtn"><s:text name="fim.useFimToLogin"/></a></p>
                            </s:if>
                        <input type="submit" class="fadeIn fourth col-xs-12" value="Login">
                        <!--<button class="btn btn-primary btn-block" name="loginBtn" id="loginBtn" src='%{#loginBtn}' type="submit">Sign in</button>-->
                        <!--<input type="reset" class="fadeIn fifth" value="Reset">-->

                    </div>
                </div>
            </div>

            <!--        <form theme="simple" action="processloginLogin" method="post">
                        <table id="loginTable" cellspacing="0" border="0" align="center">
                            <tr><td height="15px" colspan="3"></td></tr>
                            <tr><td height="145px" align="center" colspan="3"><font style="font-size: 48px"><s:text name="system.name"/>xx</font></td></tr>
                            <tr><td height="30px" colspan="3"></td></tr>
                            <tr><td height="34px" align="center" colspan="3"><font style="font-size: 20px"><s:text name="system.name1"/>yy<br><s:text name="system.name2"/></font></td></tr>
                            <tr><td height="32px" colspan="3"></td></tr>
                            <tr>
                                <td></td>
                                <td align="center" id="login_frame" width="435px" rowspan="2" valign="middle">
            <%--<jsp:include page="/pages/base/actionError.jsp"></jsp:include>--%>
            <table cellspacing="0" cellpadding="0">
            <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
            <tr><td height="50px" colspan="3"><div id="swkid_plugin"/></td></tr>
            </s:if>
            <tr align="right">
                <td>
            <%--<img height="15" width="114" src="<s:url value="/images/ess_login/username_text.png"/>" border="0">--%>
        </td>
        <td id="login_username" colspan="2">
            <s:if test="@com.sains.common.util.SystemConstants@ENV == 0">
                <input type="text" name="userId" value="sainsadmin">
            </s:if><s:else>
                <input type="text" name="userId" value="">
            </s:else>
        </td>
        </tr>
        <tr><td colspan="3" height="5px"></td></tr>
        <tr align="right">
        <td>
            <%--<img height="15" width="114" src="<s:url value="/images/ess_login/password_text.png"/>" border="0"/>--%>
            <s:text name="common.password"/>&nbsp;:&nbsp;
        </td>
        <td id="login_password" colspan="2">
            <s:if test="@com.sains.common.util.SystemConstants@ENV == 0">
                <input class="form-control" type="password" id="passwd" name="passwd" value="password" autocomplete="off" maxlength="20">
            </s:if><s:else>
                <input type="password" id="passwd" name="passwd" value="" autocomplete="off" maxlength="20">
            </s:else>
            
        </td>
        </tr>
        <tr><td height="10px"></td></tr>
        <tr>
            <%--<s:set name="loginBtn"><s:url value="/images/login/login_button.png"/></s:set>--%>
            <td/>
            <td align="left">
                <a href="forgotPassword"><s:text name="forgotPassword"/></a>
            </td>
            <td align="right">
        
            <s:hidden name="org.apache.catalina.filters.CSRF_NONCE" value="%{#session.org.apache.catalina.filters.CSRF_NONCE}"/>
            <s:submit cssClass="defaultButton" theme="simple" value='%{getText("button.login")}' action="processloginLogin" onclick="return doLogin();"/>
        </td>
        </tr>
            <s:if test="@com.SysConf@get('fim.enabled').equalsIgnoreCase('y')">
                <tr><td align="right" height="32px" colspan="3"><a href="#" onclick="fimLogin();"><s:text name="fim.useFimToLogin"/></a></td></tr>
            </s:if>
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
        </form>-->
            <s:if test="usePubPrivateKey">
                <input type="hidden" id="pubkey" value="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCB8fiA+6c7OL+82UOtc0NA29CuMtVh7Mkgx9GCVMX9GR5A0fAKOP7qWOsCYSK/Q1Kkt33bxfsRSGuTf+KlzU4QCsn77AuDpoPX0As1XZzltXCrl++Kafd93WtMJsmUYsxfYw/nvbpPt/UY6TBXfkhwyUeiEkdzfoeoz/IBWG2lzwIDAQAB">
            </s:if>
        </form>
        <script type="text/javascript" language="javascript" nonce="r4DjhKbfO5ry">document.forms[0].userId.focus()</script>
    </body>
</html>