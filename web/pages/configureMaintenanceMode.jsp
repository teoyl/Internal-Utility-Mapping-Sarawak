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
        <form name="maintenanceModeForm" id="maintenanceModeFormId" method="post" action="configureMaintenanceModeLogin">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Maintenance Mode : Configure</h4>
                </div>
                <div class="panel-body">
                    <s:if test="maintenanceMode">
                        <div class="form-horizontal form-group row">
                            <label class="col-md-3 control-label">Start</label>
                            <label style="text-align:left;" class="col-md-9 control-label"><s:property value="#session.maintenanceStart_time"/></label>
                        </div>
                        <div class="form-horizontal form-group row">
                            <label class="col-md-3 control-label">End</label>
                            <label style="text-align:left;" class="col-md-9 control-label"><s:property value="#session.maintenanceEnd_time"/></label>
                        </div>
                        <s:if test="#session.maintenanceRemark != null">
                            <div class="form-horizontal form-group row">
                                <label class="col-md-3 control-label">Remark:</label>
                                <div class="col-md-9">
                                <s:iterator value="#session.maintenanceRemark" var="theRemark" status="remarkIdx">
                                    <label style="text-align:left;" class="control-label"><s:property escapeHtml="true" value="theRemark"/></label>
                                    <s:if test="#session.maintenanceRemark.size() != #remarkIdx.index+1"><br></s:if>
                                </s:iterator>
                                </div>
                            </div>
                        </s:if>

                        <s:if test="allowIpList_AI != null">
                            <div class="form-horizontal form-group row">
                                <label class="col-md-3 control-label">Allowed IP:</label>
                                <div class="col-md-9" >
                                <s:iterator value="allowIpList_AI" var="theIp" status="ipIdx">
                                    <label style="text-align:left;" class="control-label"><s:property escapeHtml="true" value="theIp"/></label>
                                    <s:if test="allowIpList_AI.size() != #ipIdx.index+1"><br></s:if>
                                </s:iterator>
                                </div>
                            </div>
                        </s:if>
                        <div class="row"><hr></div>
                    </s:if>
                    <div class="form-horizontal form-group row">
                        <label class="col-md-3 control-label">Start Datetime <font class="asterisk">*</font></label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="maintenanceStartTime" value="" required/>
                        </div>
                        <label style="text-align:left;" class="col-md-5 control-label text-left">( ddMMyyyyHHmm eg: 201908081400 for 08 Aug 2019 2:00pm)</label>
                    </div>
                    <div class="form-horizontal form-group row">
                        <label class="col-md-3 control-label">End Datetime <font class="asterisk">*</font></label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" name="maintenanceEndTime" value="" required/>
                        </div>
                        <label style="text-align:left;" class="col-md-5 control-label text-left">( ddMMyyyyHHmm eg: 201908081400 for 08 Aug 2019 2:00pm)</label>
                    </div>
                    <div class="form-horizontal form-group row">
                        <label class="col-md-3 control-label">Allowed IP <font class="asterisk">*</font></label>
                        <div class="col-md-4">
                            <s:textfield cssClass="form-control" name="allowedIp" value="%{allowedIp_AI}" required="required"/>
                        </div>
                        <label style="text-align:left;" class="col-md-5 control-label text-left">( <s:property value="myCurrentIp" escapeHtml="true"/> is your current IP<br>;; seperated eg: 10.10.10.10;;10.10.10.11)</label>
                    </div>
                    <div class="form-horizontal form-group row">
                        <label class="col-md-3 control-label">Remark</label>
                        <div class="col-md-4">
                            <s:textfield cssClass="form-control" name="maintenanceRemark" value="%{maintenanceRemark_AI}"/>
                        </div>
                        <label style="text-align:left;" class="col-md-5 control-label text-left">( ;; seperated eg: remark 1;;remark 2)</label>
                    </div>
                    <div class="form-horizontal form-group row">
                        <label class="col-md-3 control-label"></label>
                        <div class="col-md-9">
                            <s:if test="maintenanceMode">
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="submitForm('maintenanceModeFormId', 'stopMaintenanceModeLogin');">
                                <i class="fa fa-times"></i>Stop Maintenance Mode
                            </button>
                            </s:if>
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="submitForm_bshor('maintenanceModeFormId', 'startMaintenanceModeLogin');">
                                <i class="fa fa-check"></i>Update Maintenance Mode
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>