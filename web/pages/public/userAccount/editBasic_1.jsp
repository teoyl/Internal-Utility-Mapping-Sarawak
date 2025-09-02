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
            var errors = new Array();

            validateRequired(form, errors);
            if (form.us_preferred_contact.value == "S" || form.us_preferred_contact.value == "B"){
                if (form.us_hp_number.value == ""){
                    errors[errors.length] = formatText(messageRequired, "Handphone No.");
                }
            }
            emailCheck(form.us_email, "Email Address", errors);
         
            if (errors.length > 0) {
                alert(errors.join('\n'));
                setFocus(form);
            }
            return errors.length > 0 ? false : true;
        }
        function required(){
            this.aa = new Array("us_user_id", "User ID");
            this.ac = new Array("us_preferred_contact", "Preferred Contact");
            this.ad = new Array("us_email", "Email Address");        }
    </script>
    <%--<s:head />--%>

</head>

<body>
    <center>
    <table  border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="white" align="center"> <%--main table--%>
        <tr align="left">
             <td>
                 
                 <jsp:include page="/pages/base/actionError.jsp" />
                 <form action="processUpdateProfile" method="post">
                      <table width="85%" border="0" cellspacing="0" cellpadding="2" align="center">  <%--form table--%>
                         <tr class="tableHeader">
                            <%--Sub Header--%>
                            <td colspan="2">Basic Package - Edit User Profile </td>
                        </tr>
                        <tr>
                            <td colspan="2"><jsp:include page="/pages/base/denoteRequired.jsp" /></td>
                        </tr>
                        <%-- <tr>
                            <td width="25%"><div align="left" >
                               <s:text name="user.userType"/></div></td>
                            <td width="75%">
                                <b><s:property value="%{model.us_user_type}" /></b>
                            </td>
                        </tr>--%>
                        <tr >
                            <td height="20px" width="25%"><div align="left" >
                               <s:text name="user.id"/><jsp:include page="/pages/base/requiredField.jsp"/></div></td>
                            <td width="75%">
                                 <b><s:property value="model.us_user_id"/></b>
                                <s:hidden name="us_id" value="%{model.us_id}"/>
                                <s:hidden name="us_user_id" value="%{model.us_user_id}"/>
                                <s:hidden name="us_user_type" value="%{model.us_user_type}" />
                                <s:hidden name="co_id" value="%{model.co_id}"/>
                            </td>
                        </tr>

                        <tr>
                            <td><div align="left"><s:text name="user.preferedContact"/><jsp:include page="/pages/base/requiredField.jsp"/></div></td>
                            <td>
                                <s:select cssClass="field" theme="simple" name="us_preferred_contact" list="preferredContactOption" listKey="keyData" listValue="valueData"  value="%{model.us_preferred_contact}" />
                            </td>
                        </tr>
                        <tr>
                            <td><div align="left"><s:text name="user.emailAddress"/><jsp:include page="/pages/base/requiredField.jsp"/></div></td>
                            <td><s:textfield cssClass="field" theme="simple" name="us_email" value="%{model.us_email}" size="50"/>
                                 <s:hidden name="us_original_email" value="%{model.us_email}" />
                            </td>
                        </tr>
                        <tr>
                            <td><div align="left"><s:text name="user.hpNo"/></div></td>
                            <td><s:textfield cssClass="field" theme="simple" name="us_hp_number" value="%{model.us_hp_number}" size="50"/></td>
                        </tr>

                         <tr>
                            <td><div align="left"><s:text name="user.fullName"/><%--<jsp:include page="/pages/base/requiredField.jsp"/>--%></div></td>
                            <td><s:textfield cssClass="field" theme="simple" name="us_user_name" value="%{model.us_user_name}" size="50"/>
                                <s:hidden name="us_original_user_name" value="%{model.us_user_name}" />
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2"><br></td>
                        </tr>
                         <tr>
                            <td><div align="left">
                                <%--<a href="index.jsp" target="_parent" class="plain">Back to home page</a>--%>
                                </div>
                            </td>

                            <td><div align="left">
                                <input type="submit" class="button" name="txtCreateAccount" id="txtCreateAccount" value="<s:text name="saveChanges"/>" onclick="return localValidateForm(this.form);" />
                                </div></td>
                         </tr>
                         <tr>
                            <td colspan="2">
                                 <%--<br>--%>
                                 <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center"> <%--divider--%>
                                    <tr><td colspan="3"height="10"></td></tr>
                                    <tr bgcolor="#76868C"><td colspan="3"height="1"></td></tr>
                                    <tr><td colspan="3"height="10"></td></tr>
                                </table>  <%--divider--%>
                            </td>
                        </tr>
                         <tr>
                             <td><div align="left"> <s:text name="security"/></div></td>
                            <td>
                                <a href="changePassword" class=""><span style="font-size:11px"> <s:text name="changePassword"/></span> </a>
                            </td>
                        </tr>
                         <tr>
                            <td colspan="2"><br></td>
                        </tr>
                    </table> <%--form table--%>

             </td>
        </tr>
      </form>
    </table> <%--main table--%>
    </center>
</body>
</html>



