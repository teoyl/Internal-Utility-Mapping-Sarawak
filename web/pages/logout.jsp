<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <jsp:include page="/include/crypto/crypto.jsp"></jsp:include> <%--ThoTH @ 3-Apr-2014--%>
        <title><s:text name="logout"/></title>
        <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico?v=2" type="image/x-icon" />
        <%--<s:head />--%>
        <%--style type="text/css">
            @import url(styles/impian_style_login_ess.css);
        </style--%>
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
        <script language="javascript">

        </script>
    </head>
    <body>
        <s:set name="countsys1" value="0"/>
        <s:set name="countsys2" value="0"/>
        <h1 class="error-number"><img src="images/logout.png"/></h1>
        <h2 class="semi-bold"><s:text name="login.logout"/></h2>
        <p class="p-b-10"><s:text name="login.thankYou"/>  <s:text name="system.name"/> [<s:text name="system.shortname"/>]</p>
        <p>
            <s:text name="login.loginOn"/> <b>${timeLoggedIn}</b>
            <br><s:text name="login.loginOut"/> <b>${timeLoggedOut}</b>
            <br><s:text name="login.loginDuration"/> <b>${logonDuration}</b>
        </p><br>
        
        <s:if test="activityLogList.size > 0">
            <table cellpadding="0" cellspacing="0" width="100%" style="margin: 10px; font-family: Arial,Helvetica,Geneva,Swiss,SunSans-Regular; font-size: 12px;">
                <s:iterator value="activityLogList" status="logStatus" id="log">
                    <s:if test="#countsys1 ==0">
                        <s:if test="#log.system_id == 3">
                            <s:set name="countsys1" value="1"/>
                            <tr><td>&nbsp;</td></tr>
                            <tr><td><b><s:text name="system.shortname"/> <s:text name="login.activitySummary"/></b></td></tr>
                        </s:if>
                    </s:if>
                    <s:if test="#countsys2 ==0">
                        <s:if test="#log.system_id == 6">
                            <tr><td>&nbsp;</td></tr>
                            <tr><td><b><s:text name="system.shortname"/> <s:text name="login.activitySummary"/></b></td></tr>
                        </s:if>
                    </s:if>
                    <tr><td><s:text name="date_default_datetime"><s:param value="#log.created_date"/></s:text> <s:property value="#log.log_desc"/></td></tr>
                </s:iterator>
            </table>
        </s:if>

        <p><s:text name="logout_clear_cache"/><br><b><s:text name="login.loginAgain" /> </b><a href="internal"><s:text name="reg.here"/></a>.</p>
        <p>
            <a href="internal"><s:text name="login"/> <s:text name="system.shortname"/> <s:text name="login.internalPortal" /> </a>
            <br>
            <a href="initLogin"><s:text name="login"/> <s:text name="system.shortname"/> <s:text name="login.publicPortal" /> </a>
        </p>





        <%--div class="login-background">
            <div class="center" >
                <div class="login-inner-box " style="margin-left: auto;margin-right: auto;" >
                    <form id="loginForm" action="processloginLogin" method="post" onsubmit="return false;"><!--//Change to EQP 29-Aug-2016-->
                        <s:hidden name="refererUrl_" />
                        <br/>
                        <table border="0" cellpadding="0" cellspacing="0" width="98%" style="margin: 10px; padding: 5px; font-family: Arial,Helvetica,Geneva,Swiss,SunSans-Regular; font-size: 12px; " class="login-box-text">
                            <tr><td align="center" style="font-size:25px"><img height="50" width="50" align="absmiddle" onclick="" alt="Exit Icon" src="images/exit.jpg" style="cursor: pointer;" >&nbsp;<b>You are now logged out!</b> </td></tr>
                            <tr><td align="center"><b>Thank you for using  <s:text name="system.name"/> [<s:text name="system.shortname"/>]</b> </td></tr>
                            <tr><td><br/></td></tr>
                            <tr><td align="center">You logged in on <b>${timeLoggedIn}</b></td></tr>
                            <tr><td align="center">You logged out on <b>${timeLoggedOut}</b></td></tr>
                            <tr><td align="center">Your logon duration is <b>${logonDuration}</b></td></tr>
                            <tr><td><br/></td></tr>
                                    <!--<s:iterator value="systemList" status="systemStatus" id="system">-->
                                    <s:set name="countsys1" value="0"/>
                                    <s:set name="countsys2" value="0"/>
                                    <s:if test="activityLogList.size > 0">
                                <tr>
                                    <td style="border: 1px solid black;">
                                        <table cellpadding="0" cellspacing="0" width="100%" style="margin: 10px; font-family: Arial,Helvetica,Geneva,Swiss,SunSans-Regular; font-size: 12px;">
                                            <s:iterator value="activityLogList" status="logStatus" id="log">
                                                <s:if test="#countsys1 ==0">
                                                    <s:if test="#log.system_id == 3">
                                                        <s:set name="countsys1" value="1"/>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td><b><s:text name="lbl.elodg"/> System Activity Summary</b></td></tr>
                                                    </s:if>
                                                </s:if>
                                                <s:if test="#countsys2 ==0">
                                                    <s:if test="#log.system_id == 6">
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td><b><s:text name="lbl.esub"/> System Activity Summary</b></td></tr>
                                                    </s:if>
                                                </s:if>
                                                <!--<s:iterator value="activityLogList" status="logStatus" id="log">-->
                                                <tr><td><s:text name="date_default_datetime"><s:param value="#log.created_date"/></s:text> <s:property value="#log.log_desc"/></td></tr>
                                            </s:iterator>
                                            <!--<s:else><tr><td>No activity is logged.</td></tr></s:else>-->
                                        </table>
                                    </td>
                                </tr>
                            </s:if>
                            <!--</s:iterator>-->
                            <tr><td><br/></td></tr>
                            <tr><td align="center"><s:text name="logout_clear_cache"/></td></tr>
                            <tr><td align="center"><b>To login again, click <a href="initLoginEQP">here</a>.</b></td></tr>
                            <tr><td><br/></td></tr>
                            <tr><td align="center"><b><a href="internal"><s:text name="login"/> <s:text name="system.shortname"/> Internal Portal </a></b></td></tr>
                            <tr><td align="center"><b> <a href="initLoginEQP"><s:text name="login"/> <s:text name="system.shortname"/> Public Portal </a></b></td></tr>
                            <tr><td style="height: 5px;"></td></tr>
                                <!--<tr><td align="center"><a href="initLoginEQP"><img src="images/logo.jpg" alt="LASIS Logo"></a></td></tr>-->
                        </table>   
                    </form>
                </div>
            </div> 
        </div--%>
    </body>  
</html>