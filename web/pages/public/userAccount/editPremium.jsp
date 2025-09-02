<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>e-Lasis</title>

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
            this.ab = new Array("us_preferred_contact", "Preferred Contact");
            this.ac = new Array("us_email", "Email Address");        }
            this.ad = new Array("us_user_name", "<s:text name='user.fullName' />");
            this.ae = new Array("us_nationality", "<s:text name='user.nationality' />");
            this.af = new Array("us_id_number", "<s:text name='user.idNo' />");
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
                            <td colspan="2">Premium Package - Edit User Profile </td>
                        </tr>
                        <tr>
                            <td colspan="2"><jsp:include page="/pages/base/denoteRequired.jsp" /></td>
                        </tr>
                        <tr>
                            <td width="25%"><div align="left" >
                               <s:text name="user.id"/><jsp:include page="/pages/base/requiredField.jsp"/></div></td>
                            <td width="75%" height="20px">
                                <s:hidden name="us_id" value="%{model.us_id}"/>
                                <b><s:property value="model.us_user_id"/></b>
                                <s:hidden name="us_user_id" value="%{model.us_user_id}"/>
                                <s:hidden name="us_user_type" value="%{model.us_user_type}" />
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
                            <td><div align="left"><s:text name="user.fullName"/><jsp:include page="/pages/base/requiredField.jsp"/></div></td>
                            <td height="20px">
                                 <%--<b><s:property value="model.us_user_name"/></b>--%>
                                 <%--<s:hidden name="us_user_name" value="%{model.us_user_name}"/>--%>
                                 <s:textfield cssClass="field" theme="simple" name="us_user_name" value="%{model.us_user_name}" size="50" disabled="true"/>
                                 <s:hidden name="us_original_user_name" value="%{model.us_user_name}" />
                            </td>
                        </tr>
                        <tr>
                            <td><div align="left"><s:text name="user.nationality"/><jsp:include page="/pages/base/requiredField.jsp"/></div></td>
                            <td>
                                <s:select cssClass="field" theme="simple" name="us_nationality" list="nationalityList" listKey="code_1" listValue="code_desc" value="%{model.us_nationality}" disabled="true"/>
                            </td>
                        </tr>
                          <tr>
                            <td><div align="left"><s:text name="user.idNo"/><jsp:include page="/pages/base/requiredField.jsp"/></div></td>
                            <td>
                                <s:textfield cssClass="field "theme="simple" name="us_id_number" value="%{model.us_id_number}" disabled="true"/>
                                <i>&nbsp;Note: If Malaysian, to provide IC Number </i>
                            </td>
                        </tr>
                        <tr>
                            <td><div align="left"><s:text name="user.profession"/></div></td>
                            <td><s:textfield cssClass="field" theme="simple" name="us_profession" value="%{model.us_profession}" size="50"/></td>
                        </tr>
                         <tr>
                             <td colspan="2"><BR></td>
                        </tr>

                   <%--Company Information--%>
                   <tr bgcolor="#76868C"><td colspan="3"height="1"></td></tr>
                   <tr>
                        <td colspan="3"><div align="left"><b><i><s:text name="user.company"/></i></b></div><BR></td>
                   </tr>
                   <tr>
                        <td><div align="left"><s:text name="user.coName"/></div></td>
                        <td><s:textfield cssClass="field" theme="simple" name="co_name" value="%{model.userCompany.co_name}" size="100" disabled="true"/>
                            <s:hidden name="co_id" value="%{model.co_id}"/>
                        </td>
                    </tr>
                     <tr>
                        <td><div align="left"><s:text name="user.coRegNum"/></div></td>
                        <td>
                            <s:textfield cssClass="field" theme="simple" name="co_reg_num" value="%{model.userCompany.co_reg_num}" size="20" disabled="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td><div align="left"><s:text name="user.coAddress"/></div></td>
                        <td><s:textfield cssClass="field" theme="simple" name="co_registered_address1" value="%{model.getUserCompany().getCo_registered_address1()}" size="50"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><s:textfield cssClass="field" theme="simple" name="co_registered_address2" value="%{model.getUserCompany().getCo_registered_address2()}" size="50"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><s:textfield cssClass="field "theme="simple" name="co_registered_address3" value="%{model.getUserCompany().getCo_registered_address3()}" size="50"/></td>
                    </tr>
                    <tr>
                        <td><div align="left"><s:text name="user.coPostcode"/></div></td>
                        <td><s:textfield cssClass="field "theme="simple" name="co_registered_postcode" value="%{model.getUserCompany().getCo_registered_postcode()}" size="20"/></td>
                    </tr>
                    <tr>
                        <td><div align="left"><s:text name="user.coCity"/></div></td>
                        <td><s:textfield cssClass="field "theme="simple" name="co_registered_city" value="%{model.getUserCompany().getCo_registered_city()}" size="50"/></td>
                    </tr>
                    <tr>
                        <td><div align="left"><s:text name="user.coState"/></div></td>
                        <td>
                            <s:select cssClass="field" theme="simple" name="co_registered_state" list="stateList" listKey="code_1" listValue="code_desc" value="%{model.getUserCompany().getCo_registered_state()}"/>
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
                        <tr>
                             <td colspan="2"><div align="center"><P>&nbsp;</P><b><u>Note:</u></b> <s:text name="user.fullName"/>, <s:text name="user.nationality"/>, <s:text name="user.idNo"/>, <s:text name="user.coName"/> and <s:text name="user.coRegNum"/> are not editable fields once the application has been approved.
                             Contact Land and Survey Department Sarawak if you wish to update these information. </div></td>
                        </tr>
                       <%--
                         <tr>
                            <td colspan="2"><br></td>
                        </tr>
                        <tr>
                            <td><div align="left">
                                <a href="index.jsp" target="_parent" class="plain">Back to home page</a></div>
                            </td>
                            <td><div align="left">
                                <input type="submit" class="button" name="txtCreateAccount" id="txtCreateAccount" value="<s:text name="saveChanges"/>" onclick="return localValidateForm(this.form);" />
                                </div></td>
                        
                    </tr>--%>
                    </table> <%--form table--%>

             </td>
        </tr>
      </form>
    </table> <%--main table--%>
    </center>
</body>
</html>



