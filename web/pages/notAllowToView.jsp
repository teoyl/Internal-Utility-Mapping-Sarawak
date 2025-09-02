<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <!--<script type="text/javascript" src="pages/menu/simpletreemenu.js"></script>-->
        <!--<link rel="stylesheet" type="text/css" href="pages/menu/menutree.css" />-->
        <%--<s:head />--%>
        <style type="text/css">
            /*@import url(styles/impian_style_internal.css);*/
        </style>
    </head>
    <!--<body style=" background-image: url(images/loginBg.png)">-->
    <body>
        <form theme="simple" action="processloginLogin" method="post">
            <table width="100%" border="0">
                <tr>
                    <td>
                        <s:text name="errors.notAllowToView"/>
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