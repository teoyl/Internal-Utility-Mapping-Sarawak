<%-- 
    Document   : passwordRecovery
    Created on : Aug 24, 2010, 4:39:44 PM
    Author     : Ivy Lee
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><s:text name="system.shortname"/></title>
</head>

<body>
    <center>
    <table  border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="white" align="center"> <%--main table--%>
        <form action="recoverPassword" method="post">
        <tr align="left">
             <td>
                  <jsp:include page="/pages/base/actionError.jsp" />
                      <table width="85%" border="0" cellspacing="0" cellpadding="2" align="center">  <%--form table--%>
                         <tr class="tableHeader">
                            <%--Sub Header--%>
                            <td colspan="2">Forgot your Password ? </td>
                        </tr>
                        <tr>
                            <td width="100" align='center' height="60" valign="top"><div align="center"><img src="images/user_premium.jpg" width="50" height="50" /></div></td>
                            <td width="100%" align='left' height="60" valign="top">
                                 <p><b><span style='color:maroon'>Note : </span> To reset your password, type the full email address you use for your online transaction with us.</b></p>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center" height="100" valign="middle">
                                <s:text name="user.emailAddress"/><jsp:include page="/pages/base/requiredField.jsp"/> &nbsp;
                               <s:textfield cssClass="field" theme="simple" name="us_email" size="50" value="%{model.us_email}"/>
                               <%--<input type="text" name="us_email" id="us_email" class="field" size="50" />--%>
                            </td>
                        </tr>
                    </table> <%--form table--%>
                     <br>
                    <table width="80%" border="0" cellspacing="0" cellpadding="0" align="center"> <%--divider--%>
                        <tr bgcolor="#76868C"><td colspan="3"height="1"></td></tr>
                    </table>  <%--divider--%>
             </td>

        </tr>
        <tr>
            <td colspan="2"><div align="center">
                    <p><br>
                        <input type="submit" name="recoverPassword" id="recoverPassword" class="button" value="Submit" onclick="return localValidateForm(this.form);" />
                    </p>
                </div>
            </td>
        </tr>
         <tr align="left">
               <td><div align="left">
                   <br>
                   <a href="index.jsp" target="_parent" class="plain">Back to home page</a></div>
               </td>
         </tr>
    </form>
    </table> <%--main table--%>
    </center>
</body>
</html>
