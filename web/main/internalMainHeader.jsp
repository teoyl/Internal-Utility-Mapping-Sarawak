<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>

<%--<html>
<head>
</head>
<body>
    <table border="0" cellpadding="0" cellspacing="0" width="990" bgcolor="#f2f2f2">
    <tr>
        <td><img height="95" width="990" src="images/banner_internal.jpg" border="0" alt="banner"></td>
    </tr>
</table>

</body>
</html>--%>


<%--<html>
<head>
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <a href="initLogin" title="Go to Home">
            <img height="95" width="990" src="images/banner_internal.jpg" border="0" alt="banner">
        </a>
        </td>
    </tr>
</table>

</body>
</html>--%>

<table align="center" border="0" cellpadding="0" cellspacing="0" width="1000px">
    <tr>
        <td>
            <a href="welcome" title="Go to Home">
                <div id="imp_banner_text">
                    <table align="center" border="0" cellpadding="0" cellspacing="0" width="1000px" >
                        <tr>
                            <%--<td width="50px"></td>--%>
                            <td height="100px" valign="middle" align="center" class="titleText">
                                <B><i><font style="font-size: 24px;"  ><s:text name="system.name" /><BR><s:text name="system.name1" /></font></i></B>
                                            <%--<B><i><font size="18px" ><s:text name="system.name" /></font></i></B>--%>
                            </td>
                        </tr>
                    </table>
                </div>
            </a>

        </td>
    </tr>
</table>