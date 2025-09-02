<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Password Recovery Page</title>
        <%--<s:head />--%>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
     
        <style type="text/css">
            @import url(styles/style_internal_el.css);
        </style>
    </head>
    <body style="background-color: #fff;">
        <br/>
         <!--<div class="xbox">-->
          <div class="panel panel-default ">
          <div class="panel-heading ">
            <h3 class="panel-title"> 
                <span class="titleText"><s:text name="qp.recoverPassword" /></span>
                <span class="titleActionTypeText"> | <s:text name="emailSent" /></span>
            </h3>
          </div>
           <div class="panel-body">
<!--        <div class="titleFramework">
            <table border="0" cellpadding="0" cellspacing="0" class="tableTop">
                <tr>                    
                    <td><span class="titleText"><s:text name="qp.recoverPassword"/></span><br/></td>
                </tr>
            </table>
        </div>-->
       
        
            <form action="recoverPasswordELLoginEL" method="post">
                <center>
                    <table cellspacing="0" cellpadding="0" border="0" style="border-collapse: collapse; width: 100%; font-family: Arial,Helvetica,Geneva,Swiss,SunSans-Regular;">
                        <tr>
                            <td align="left" style="padding: 5px;">
                                <font face="Arial,Helvetica,Geneva,Swiss,SunSans-Regular" color="#595959">
                                    <p>To reset your password, follow the instructions sent to your email address : <span style="color:blue"><b><s:property value="%{model.us_email}"/></b></span>.
                                    <p><span>Thank You.</span></p></font>
                                <!--<img height="129" width="129" src="images/eqp_logo.jpg" border="0" alt="eQP Logo">-->
                            </td>
                        </tr>
                        <tr align="center">
                        <td><div align="center">
                                <br><br>
                                <a class="plain" href="initLogin" target="_parent" >Back to login page</a></div>
                        </td>
                    </tr>
                    <br>
                    </table>
                </center>
            </form>
        </div></div>
    </body>
</html>
