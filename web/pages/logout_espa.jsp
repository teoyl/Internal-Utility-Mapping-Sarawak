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
        <style type="text/css">
            /*@import url(styles/impian_style_login_ess.css);*/
        </style>
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

        <!--span class="col-xs-12 header-text hidden-xs">
            <img src="images/logo_mrpe.png" > ONLINE MANAGEMENT OF SPA QUALIFIED PERSONS
        </span-->

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

         <p><s:text name="logout_clear_cache"/><br><b><s:text name="login.loginAgain" /> </b><a href="initLogin"><s:text name="reg.here"/></a>.</p>
        <p>
<!--            <a href="initLoginSPA"><s:text name="login"/> <s:text name="system.shortname"/> </a>
            <br>-->
            <a href="initLogin"><s:text name="login"/> <s:text name="system.shortname"/> <s:text name="login.publicPortal" /> </a>
        </p>
    </body>  
</html>