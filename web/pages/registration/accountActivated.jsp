<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%--<title><s:text name='systemInfo.title' /></title>--%>
</head>

<body>
    <center>
    <table  border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="white" align="center"> <%--main table--%>
        <tr>
             <td align="left" >
                <font face="Arial,Helvetica,Geneva,Swiss,SunSans-Regular" color="#595959">
                    <!--Edited by IvyL@ 26 April 2018-->
                    <p>Hi
                       <span style=" font-style: italic; font-weight: bold">
                        <s:if test="#session.userName != null">
                            <s:property value="#session.userName.toUpperCase()"/>,
                        </s:if>
                        <s:else>
                            <s:property value="#session.loginId.toUpperCase()"/>,
                        </s:else>
                       </span>
                        <br>
                    </p><br>
                    <p>Welcome to the <b><i> <s:text name="system.name"/> (<s:text name="system.shortname"/> )</i></b> online services provided by the <s:text name="systemInfo.fullsystemName"/>.</p><br>
                    <p>Your user account has been successfully activated. You are required to provide login ID and password to access the system. <br>
                    <ul style="margin-left:10px;">
                     <li>For Malaysian, your login ID will be your New Identity Card Number.</li>
                     <li>For non-Malaysian, your login ID will be the registered email address.</li>
                    </ul><br>
                    Click <a href="initLogin" class="text-green">&nbsp;here&nbsp;</a> to proceed to eSPA's login screen.
                   </p>
                     <%--Please click--%><%--<a href="" onClick="window.location='welcomeRegistration'">&nbsp;here&nbsp;</a>--%>
                    <p>&nbsp;</p>
                    <p>Sincerely,<br>
                    Planning Division Team, <br>
                    Ministry of Urban Development and Natural Resources.</p>
                    <!--<p><span style="font-size: 12pt;">Thank You.</span></p></font>-->
                    <%--<img height="129" width="129" src="images/lns_logo.jpg" border="0" alt="L&S Logo">--%>
            </td>
        </tr>
    </table>
    </center>
</body>
</html>