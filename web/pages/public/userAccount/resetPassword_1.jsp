<%-- 
    Document   : resetPassword
    Created on : Aug 26, 2010, 11:07:04 AM
    Author     : Ivy Lee
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><s:text name="system.shortname"/></title>

    <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
    <script type="text/javascript">
         function localValidateForm(form, operation) {
             alert("here");
            var errors = new Array();

            validateRequired(form, errors);

            passwordCheck(form.us_password, "Password", errors,12,16);
              <%--ICTU GUIDE: PASSWORD ATLEAST 12 CHARACTERS--%>
            passwordCheck(form.us_password, "Password", errors,12,16);
            if ( trim(form.confirmPassword.value) != trim(form.us_password.value) ){
                 errors[errors.length] = "<s:text name='errors.passwordNotMatch'/>";
            }
            <%--ICTU GUIDE: PASSWORD CANNOT BE SAME AS USER ID--%>
           if ( trim(form.us_user_id.value).toUpperCase() == trim(form.us_password.value).toUpperCase() ) {
                errors[errors.length] = "<s:text name='errors.passwordSameAsUserID'/>";
            }

            if (errors.length > 0) {
                alert(errors.join('\n'));
                setFocus(form);
            }
            return errors.length > 0 ? false : true;
        }
        function required(){
            this.aa = new Array("us_password", "Password");
    </script>

    <%--<s:head />--%>



</head>

<body>
    <center>
    <table  border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="white" align="center"> <%--main table--%>
        <form action="resetPassword" method="post">
        <tr align="left">
             <td>
                  <jsp:include page="/pages/base/actionError.jsp" />
                      <table width="85%" border="0" cellspacing="0" cellpadding="2" align="center">  <%--form table--%>
                         <tr class="tableHeader">
                            <%--Sub Header--%>
                            <td colspan="2">Reset Password</td>
                        </tr>
                        <tr>
                            <td width="60" align='left' height="60" valign="top"><div align="left"><img src="images/user_premium.jpg" width="50" height="50" /></div></td>
                            <td width="100%" align='left' height="60" valign="top">
                                 <p><b><span style='color:maroon'>Note : </span> Select your new password and enter it below.</b></p>
                            </td>
                        </tr>
                        <tr>
                             <td align='center'valign="top" colspan="2">
                                 <table width="90%" border="0">
                                        <tr>
                                            <td height="20px"><div align="left" >
                                               <s:text name="user.id"/></div></td>
                                            <td><div align="left" >
                                                 <b><s:property value="model.us_user_id"/></b>
                                                <s:hidden name="us_id" value="%{model.us_id}"/>
                                                <s:hidden name="us_user_id" value="%{model.us_user_id}"/>
                                                </div>
                                            </td>
                                        </tr>
                                       <%-- <tr>
                                            <td height="20px"><div align="left" >
                                               <s:text name="user.emailAddress"/></div></td>
                                            <td><div align="left" >
                                                 <b><s:property value="model.us_email"/></b>
                                                </div>
                                            </td>
                                        </tr>--%>
                                       <tr>
                                            <td><div align="left"><s:text name="user.newPassword"/><jsp:include page="/pages/base/requiredField.jsp"/></div></td>
                                            <td><div align="left">
                                                <s:password cssClass="field" theme="simple" name="us_password" value="%{model.us_password}" size="50"/>
                                                 <i>&nbsp;<s:text name="notePassword"/> </i>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><div align="left"><s:text name="user.confirmPassword"/><jsp:include page="/pages/base/requiredField.jsp"/></div></td>
                                            <td><div align="left">
                                                <s:password cssClass="field" theme="simple" name="confirmPassword" value="%{confirmPassword}" size="50"/>
                                             </div>
                                            </td>
                                        </tr>
                                 </table>
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
                        <input type="submit" name="resetPassword" id="resetPassword" class="button" value="Reset Password" onclick="return localValidateForm(this.form);" />
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
