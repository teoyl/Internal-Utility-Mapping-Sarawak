<%-- 
    Document   : recoveryEmailSent
    Created on : Aug 26, 2010, 10:40:55 AM
    Author     : Ivy Lee
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><s:text name="system.name"/></title>
</head>

<body>
    <center>
    <table  border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="white" align="center"> <%--main table--%>
        <tr>
             <td align="left" >
                <font face="Arial,Helvetica,Geneva,Swiss,SunSans-Regular" color="#595959">
                    <p> <b>Recovery Email Sent</b><BR></p>
                    <p>To reset your password, follow the instructions sent to your email address : <span style="color:blue"><b><s:property value="%{model.us_email}"/></b></span>.
                    <p>&nbsp;</p>
                    <p><span style="font-size: 12pt;">Thank You.</span></p></font>
                    <img height="129" width="129" src="images/lns_logo.jpg" border="0" alt="logo">
            </td>
        </tr>
    </table>
    </center>
</body>
</html>

