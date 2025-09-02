<%-- NO MORE USING --%>

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
            @import url(styles/impian_style_internal.css);
        </style>
    </head>
    <body style=" background-image: url(images/loginBg.png)">
        <form theme="simple" action="processloginLogin" method="post">
            <table width="100%" border="0">
                <tr>
                    <td>
                        <%--**Please translate to Malay!<br>
                        Acting by <u><s:property value="#session.actingUserName"/></u> from <b><s:property value="#session.act_start_date"/></b> to <b><s:property value="#session.act_end_date"/></b>.
                        <br>
                        Cancel acting to proceed with your normal login. <a href="withdrawActingLogin">Cancel Acting</a>
                        <br>
                        <a href="processlogoutLogout">Logout</a>
                        <br/>--%>
                        <p style="font-size: 14px">
                        Jawatan dan tugasan anda sedang dimangku/ ditanggung oleh <u><s:property value="#session.actingUserName"/></u> mulai <b><s:property value="#session.act_start_date"/></b> sehingga <b><s:property value="#session.act_end_date"/></b>.
                        <br>
                        Jika anda ingin meneruskan tugasan anda dengan sistem GEMS, sila klik <a href="withdrawActingLogin">Balik ke Menu Anda</a> untuk memulihkan akses pengguna anda.
                        </p>
                        <%--Memangku tugas <u><s:property value="#session.actingUserName"/></u> mulai <b><s:property value="#session.act_start_date"/></b> sehingga <b><s:property value="#session.act_end_date"/></b>.
                        <br>
                        Sila Batal Pemangkuan untuk teruskan dengan login biasa. <a href="withdrawActingLogin">Batal Pemangkuan</a>
                        <br>
                        <a href="processlogoutLogout">Log Keluar</a>--%>
                        <br/>
                    </td>
                </tr>
                <tr id="errorDetail_">
                    <td align="left">
                        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
                    </td>
                </tr>

            </table>
        </form>
    </body>
</html>