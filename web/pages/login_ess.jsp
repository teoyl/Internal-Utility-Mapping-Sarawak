<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript" src="pages/menu/simpletreemenu.js"></script>
        <link rel="stylesheet" type="text/css" href="pages/menu/menutree.css" />
        <%--<s:head />--%>
        <style type="text/css">
            @import url(styles/msen_style_internal_ess.css);
        </style>
    </head>
    <body<%-- id="loginBg"--%>>
        <%--<div style="height: 100px"></div>--%>
        <%--<table id="impianLoginESS" cellspacing="0" cellpadding="0" border="0" align="center">
            <tr>
                <td height="270px"></td>
            </tr>
            <tr>
                <td height="150px">
                    <form theme="simple" action="processloginLogin" method="post">
                        <table cellspacing="0" border="0" align="center" width="300px">
                            <tr>
                                <td class="label">ID Pengguna</td>
                            </tr>
                            <tr>
                                <td><input type="text" name="userId" value="leeafacd97"></td>
                            </tr>
                            <tr>
                                <td height="10px"></td>
                            </tr>
                            <tr>
                                <td class="label">Kata Laluan</td>
                            </tr>
                            <tr>
                                <td><input type="password" name="passwd" value="password"></td>
                            </tr>
                            <tr>
                                <td align="right"><s:submit cssClass="submitLogin" theme="simple" value="Login" action="processloginLoginESS"/></td>
                            </tr>
                        </table>
                    </form>
                </td>
            </tr>
            <tr>
                <td></td>
            </tr>
        </table>--%>
        <table id="loginTable" cellspacing="0" border="0" align="center">
            <form theme="simple" action="processloginLoginESS" method="post">
                <tr><td height="15px" colspan="3"></td></tr>
                <tr><td align="center" colspan="3"><img height="142" width="440" src="images/ess_login/impian_ess_logo.png" border="0"></td></tr>
                <tr><td height="30px" colspan="3"></td></tr>
                <tr><td align="center" colspan="3"><img height="55" width="451" src="images/ess_login/impian_ess_title.png" border="0"></td></tr>
                <tr><td height="32px" colspan="3"></td></tr>
                <tr>
                    <td></td>
                    <td align="center" id="login_frame" width="435px" rowspan="2" valign="middle">
                        <table cellspacing="0" cellpadding="0">
                            <tr><td height="50px" colspan="2"><jsp:include page="/pages/base/actionError.jsp"></jsp:include></td></tr>
                            <tr align="center">
                                <td><img height="15" width="114" src="images/ess_login/username_text.png" border="0"></td>
                                <td id="login_username"><input type="text" name="userId" value="leeafacd97"></td>
                            </tr>
                            <tr><td colspan="2" height="5px"></td></tr>
                            <tr align="center">
                                <td><img height="15" width="114" src="images/ess_login/password_text.png" border="0"></td>
                                <td id="login_password"><input type="password" name="passwd" value="password"></td>
                            </tr>
                            <tr><td height="10px"></td></tr>
                            <tr>
                                <td colspan="2" align="right">
                                    <s:submit type="image" width="86" theme="simple" value="Login" src="images/ess_login/login_button.png" action="processloginLoginESS"/>
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
                <tr><td colspan="3" id="table_bottom" height="103px"></td></tr>
            </form>
        </table>
    </body>
    <script type="text/javascript" language="javascript">document.forms[0].userId.focus()</script>
</html>