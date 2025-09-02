<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <s:set var="systemWelcome_"><s:text name="system.welcome"/></s:set>

        <title><s:text name="system.shortname"/> <s:text name="system.name"/></title>

        <!--<script src="include/jquery/jquery.js"></script>-->
        <script src="include/jquery/jquery-3.4.1.min.js"></script>

        <!--<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico?v=2" type="image/x-icon"/>-->

        <!--Bootstrap-->
        <link href="include/bootstrap/bootstrap.css" rel="stylesheet"/>

        <!--Fonts-->
        <link href="include/fonts/fonts.css" rel="stylesheet"/>
        <link href="include/fonts/font-awesome.css" rel="stylesheet"/>
    </head>
    <body>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4>System Under Maintenance</h4>
            </div>
            <div class="panel-body">
                <div class="row">
                    <label class="col-md-3 control-label">Start</label>
                    <label class="col-md-9 control-label"><s:property value="#session.maintenanceStart_time"/></label>
                </div>
                <div class="row">
                    <label class="col-md-3 control-label">End</label>
                    <label class="col-md-9 control-label"><s:property value="#session.maintenanceEnd_time"/></label>
                </div>
                <s:if test="#session.maintenanceRemark != null">
                    <div class="row">
                        <label class="col-md-3 control-label">Remark:</label>
                    </div>
                    <div class="row">
                        <ul>
                    <s:iterator value="#session.maintenanceRemark" var="theRemark">
                            <li><s:property escapeHtml="true" value="theRemark"/></li>
                    </s:iterator>
                        </ul>
                    </div>
                </s:if>
            </div>
        </div>
    </body>
</html>