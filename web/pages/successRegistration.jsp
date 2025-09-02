<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>eQP - User's Registration Page</title>
        <%--<s:head />--%>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <%--POPUP--%>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" type="text/css" />
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" type="text/css" />
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>

        <%--for auto retrieve--%>
        <sx:head parseContent="true" debug="false" />
        <script type="text/javascript" src="include/inforLoader.js"></script>
        <script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <style type="text/css">
            @import url(styles/style_internal_el.css);
        </style>
    </head>
    <body style="background-color: #fff;">
        <br/>
        <div class="titleFramework">
            <table border="0" cellpadding="0" cellspacing="0" class="tableTop">
                <tr>
                     <%--<td width="40" align="right"><img src="images/elodgement/icons/user_new.png" alt="New User" width="24" height="24"/></td>--%>
                        <td align="left">
                            <span class="titleText"><s:text name="signup" /></span>
                            <span class="titleActionTypeText"><span class="titleSeparator">|</span> Instructions</span><br/>
                        </td>
                </tr>
            </table>
        </div>
        <div class="xbox">
            <form action="printRegistration" method="post">
                <%--<table  border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="white" align="center">--%> <%--main table--%>
                <table class="form" cellspacing="1" cellpadding="1"  width="100%" border="0">
                    <tr align="left">
                        <td style="padding: 5px;">
                            <jsp:include page="/pages/base/actionError.jsp" />
                            <table>
                                <tr>
                                    <td width="70px"><img src="images/elodgement/icons/step2.png" alt="Step 2"/></td>                                    
                                    <%--<td> <span style="font-size: 12px;"><s:text name="user.success.reg.step.2"/></span></td>--%>
                                    <td> <span style="font-size: 12px;"><s:text name="user.success.reg.step.6"/></span></td>
                                </tr>
                                 <tr>
                                    <td width="70px">&nbsp;</td>
                                    <td> <BR>
                                        <span style="font-size: 12px;">
                                             <!--Inner Table-->
                                             <table cellpadding="0" cellspacing="0" style="width: 500px; border: 0px solid #000000;">
                                                    <tr class="tableHeader">
                                                        <%--Sub Header--%>
                                                        <td colspan="3" align="left" class="rounded-topLeftRight2" style="color:#003736; padding: 5px; border-bottom: 1px solid #003736; ">
                                                            <b><s:text name="user.reg.qp.acc"/></b>
                                                            <br>
                                                        </td>
                                                        <td><br/><br/></td>
                                                    </tr>
                                                    <tr style="background-color: #fff;">
                                                        <%--<td>&nbsp;</td>--%>
                                                        <td align="left" width="50%" style="padding: 5px; font-weight: bold;">
                                                                <s:text name="user.full.name"/>
                                                        <td width="1%">:</td>
                                                        <td style="font-weight: bold;">&nbsp;&nbsp;
                                                            <s:property value="model.us_user_name"/>
                                                        </td>
                                                    </tr>
                                                    <tr style="background-color: #fff;">
                                                        <%--<td>&nbsp;</td>--%>
                                                        <td align="left" width="50%" style="padding: 5px; font-weight: bold;"><%--<div align="left" >--%>
                                                                <s:text name="user.ic.no"/><%--<jsp:include page="/pages/base/requiredField.jsp"/>--%><%--</div>--%></td>
                                                        <td width="1%">:</td>
                                                        <td style="font-weight: bold;">&nbsp;&nbsp;
                                                            <s:hidden name="us_id" value="%{model.us_id}"/>
                                                            <s:property value="model.us_id_number"/>
                                                            <%--ahmadni @ 28-Oct-2016--%>
                                                            <%--<s:property value="model.us_user_id"/>
                                                            <s:hidden name="us_user_id" value="%{model.us_user_id}"/>--%>
                                                            <s:hidden name="us_user_type" value="%{model.us_user_type}" />
                                                        </td>
                                                    </tr>
                                                  <tr style="background-color: #fff;"><td colspan="3" style="height: 5px;"></td></tr>
                                                   <tr style="background-color: #fff;">
                                                        <%--<td>&nbsp;</td>--%>
                                                        <td align="left" style="padding: 5px; font-weight: bold;"><s:text name="user.emailAddress"/><%--<jsp:include page="/pages/base/requiredField.jsp"/>--%></td>
                                                        <td>:</td>
                                                        <td style="font-weight: bold;">&nbsp;&nbsp;
                                                            <s:property value="%{model.us_email}" />
                                                        </td>
                                                        <%--<td>&nbsp;</td>--%>
                                                    </tr>
                                                    <%--Added by IvyL--%>
                                                     <%--<tr style="background-color: #fff; height: 40px; ">
                                                         <td colspan="3" align="center">
                                                             <input type="button" name="txtCreateAccount" id="txtCreateAccount" class="defaultButton" value="Print User Account Request Form" onClick="window.location = 'downloadFile?dType=formQP&dCode=<s:property value='%{model.us_id}'/>';" />
                                                         </td>
                                                    </tr>--%>
                                                    <tr><td colspan="3" class="rounded-bottomLeftRight2" style="height: 5px; background-color: #fff;"></td></tr>
                                                </table>
                                     <!--table end-->
                                            
                                        </span></br>
                                    </td>
                                </tr>
                                <%--<tr>
                                    <td width="70px"><img src="images/elodgement/icons/step3.png" alt="Step 3"/></td>
                                    <td> <span style="font-size: 12px;"><s:text name="user.success.reg.step.3"/></span></td>
                                </tr>
                                <tr style="height: 5px;"><td colspan="2"></td></tr>
                                <tr>
                                    <td><img src="images/elodgement/icons/step4.png" alt="Step 4"/></td>
                                    <td> <span style="font-size: 12px;"><s:text name="user.success.reg.step.4"/></span></td>
                                </tr>
                                <tr style="height: 5px;"><td colspan="2"></td></tr>
                                <tr>
                                    <td><img src="images/elodgement/icons/step5.png" alt="Step 5"/></td>
                                    <td> <span style="font-size: 12px;"><s:text name="user.success.reg.step.5"/></span></td>
                                </tr>
                                <tr style="height: 5px;"><td colspan="2"></td></tr>
                                <tr>
                                    <td><img src="images/elodgement/icons/step6.png" alt="Step 6"/></td>
                                    <td> <span style="font-size: 12px;"><s:text name="user.success.reg.step.6"/></span></td>
                                </tr>--%>
                            </table>
                        </td>

                    </tr>
                    <tr align="center">
                        <td>
                            <div align="center">
                                <br>
                                <table>
                                    <tr>
                                        <td> <a href="initLogin" target="_parent" class="plain">Back to login page</a></td><%--ahmadni 22/4/2014 during uat for eLASIS SF--%>
                                    </tr>
                                </table>
                                </div>
                        </td>
                    </tr>
                    <br/>
                </table>
                <br/>
            </form>
        </div>
    </body>
</html>
