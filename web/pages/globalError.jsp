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
            /*@import url(styles/style_internal.css);*/
        </style>
        <script type="text/javascript">
            /**
             * Comment
             */
            function showHideError(button) {
                if (button.value == "<s:text name="errors.showError"/>") {
                    button.value = "<s:text name="errors.hideError"/>"
                    document.getElementById("errorDetail_").style.display = "table-row";
                } else {
                    button.value = "<s:text name="errors.showError"/>"
                    document.getElementById("errorDetail_").style.display = "none";
                }
            }
        </script>
    </head>
    <body style=" background-image: url(images/loginBg.png)">
        <form theme="simple" action="processloginLogin" method="post">
            <table width="100%" border="0">
                <tr>
                    <td>
                        <font size="5"><s:text name="errors.unexpectedError"/></font>
                        <br/>
                    </td>
                </tr>
                <tr>                    
                    <td align="left">
                        <input type="button" onclick="showHideError(this)" value="<s:text name="errors.showError"/>"/>
                    </td>
                </tr>
                <tr id="errorDetail_">
                    <td align="left">
                        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
                    </td>
                </tr>

            </table>
        </form>
        <script type="text/javascript" language="javascript">document.getElementById("errorDetail_").style.display = "none";</script>
    </body>
</html>