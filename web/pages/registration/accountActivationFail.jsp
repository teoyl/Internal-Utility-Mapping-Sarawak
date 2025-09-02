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
                <font face="Arial,Helvetica,Geneva,Swiss,SunSans-Regular" color="#595959" style="font-size: 11pt;">
                   <%--// [Start] @20Mar2014. If user simply take any activation url for activation, then this checking applied.--%>
                    <s:if test="#session.userId == null">
                        No matching record is found.
                    </s:if>
                   <%--// [End] @20Mar2014. If user simply take any activation url for activation, then this checking applied.--%>
                    <s:else>
                        <p>Dear
                            <s:if test="#session.userName != null">
                                <s:property value="#session.userName.toUpperCase()"/>,
                            </s:if>
                            <s:else>
                                <s:property value="#session.loginId.toUpperCase()"/>,
                            </s:else>
                            <br>
                        </p>
                    </s:else>
                    
                   <%-- <p>Welcome to <b>eLASIS</b>.--%>
                     Account activation failed. Please contact System Administrator to verify your account details.
                    <p>&nbsp;</p>
                    <p><span style="font-size: 12pt;">Thank You.</span></p></font>
                    <%--<img height="129" width="129" src="images/lns_logo.jpg" border="0" alt="L&S Logo">--%>
            </td>
        </tr>
    </table>
    </center>
</body>
</html>