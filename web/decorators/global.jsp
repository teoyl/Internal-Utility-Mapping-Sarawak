<%@page import="com.sains.common.util.SystemConstants"%>
<%@page import="com.sains.common.util.AesUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="16kb" autoFlush="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<% String strStateCode = null;%>
<s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV] || @com.SysConf@get('fim.enabled').equalsIgnoreCase('y')">
    <%
        strStateCode = (session.getAttribute("sesStateCode") == null ? "" : session.getAttribute("sesStateCode").toString());
        if (strStateCode.equals("")) {
            strStateCode = AesUtil.random(10);
            session.setAttribute("sesStateCode", strStateCode);
        }
    %>
</s:if>
<s:set var="ctx" value="%{pageContext.request.contextPath}"/>
<html lang="en" class="st-layout ls-top-navbar-large ls-bottom-footer show-sidebar sidebar-l4">
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <s:set var="systemWelcome_"><s:text name="system.welcome"/></s:set>
            <style>
                .title-alert {
                    border-bottom: 1px solid #ddd;
                    border-left: 2px solid red;
                    font-size: 22px;
                    font-weight: 300;
                    margin-bottom: 16px;
                    margin-top: 0;
                    padding-bottom: 5px;
                    padding-left: 15px;
                }
            </style>
            <title><s:text name="system.shortname"/> <s:text name="system.name"/></title>

        <!--<script src="include/jquery/jquery.js"></script>-->
        <script src="include/jquery/jquery-3.5.1.min.js"></script>
        <script src="include/bootstrap/bootstrap-idletimeout/script/bootstrap-session-timeout.js" type="text/javascript"></script>
        <link rel="icon" href="<s:text name="system.icon"/>" type="image/x-icon"/>

        <!--Bootstrap-->
        <link href="include/bootstrap/bootstrap.css" rel="stylesheet"/>
        
        <!--Fonts-->
        <link href="include/fonts/fonts.css" rel="stylesheet"/>
        <link href="include/fonts/font-awesome.css" rel="stylesheet"/>

        <!--Back-to-top-->
        <link href="include/back-to-top/back-to-top.css" rel="stylesheet"/>

        <!--Select2-->
        <link href="include/select2/select2.css" rel="stylesheet"/>

        <!--Modal-->
        <!--<link href="include/modal/bootstrap-modal-bs3patch.css" rel="stylesheet"/>-->
        <link href="include/modal/bootstrap-modal.css" rel="stylesheet"/>

        <!--Date and Daterange picker-->
        <link href="include/datepicker/datepicker3.css" rel="stylesheet"/>
        <link href="include/daterangepicker/daterangepicker.css" rel="stylesheet"/>

        <link href="include/bootstrap/customs.css" rel="stylesheet"/>
        <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
            <link href="https://sarawakid-tnt.sarawak.gov.my/web/web/default/sso_bar.css" rel="stylesheet"/>
        </s:if>
        <!--Date and daterange picker-->
        <script type="text/javascript" language="javascript" src="include/datepicker/bootstrap-datepicker3.js"></script>
        <script type="text/javascript" language="javascript" src="include/daterangepicker/moment.js"></script>
        <script type="text/javascript" language="javascript" src="include/daterangepicker/daterangepicker.js"></script>

        <!--Input Mask-->
        <script type="text/javascript" language="javascript" src="include/input-mask/jquery.inputmask.js"></script>
        <script type="text/javascript" language="javascript" src="include/input-mask/jquery.inputmask.extensions.js"></script>

        <script type="text/javascript" language="javascript" src="pages/scripts/common.js"></script>
        <!--<script type="text/javascript" language="javascript" src="validate_jsp"></script>-->
        <script type="text/javascript" language="javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" language="javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" language="javascript" src="pages/scripts/lookup.js"></script>    

        <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
            <script src="<%=SystemConstants.SarawakID.PLUGIN_URL[SystemConstants.ENV]%>"></script>
        </s:if>
        <script type="text/javascript">
            <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
            document.addEventListener("DOMContentLoaded", function () {
                swkid_sso_init({
                    client_id: '<%=SystemConstants.SarawakID.CLIENT_ID[SystemConstants.ENV]%>',
                    state: '<%=strStateCode%>',
                    response_type: 'code',
                    redirect_uri: '<%=SystemConstants.SarawakID.SYSTEM_URL[SystemConstants.ENV]%>/ssoVerifyLogin',
                    logout_redirect_uri: '<%=SystemConstants.SarawakID.SYSTEM_URL[SystemConstants.ENV]%>/processlogoutLogout',
                    logout_uri: '<%=SystemConstants.SarawakID.SYSTEM_URL[SystemConstants.ENV]%>/processlogoutLogout',
                    //                style:'icon-text',
                    icon_width: '30',
                    //                position:'',
                    misc_param: 'test',
//                    profile_listing_style: 'details',
                    //                profile_listing_email:'on',
                    //                force_login_btn: 'off'
                });
                //            swkid_login_form_submit();
            });
            function swkid_callback(returnObj) {
                console.log(returnObj);
            }
            function profile_onclick() {
                return;
            }
            </s:if>
            $(document).ready(function () {
                <s:if test='#session.enableCountdown && (#session.logined).equalsIgnoreCase("true")'>
                $.sessionTimeout({
                    keepAliveUrl: 'welcome',
                    logoutUrl: 'processlogoutLogout',
                    redirUrl: 'processlogoutLogout',
                    warnAfter: <s:property value="#session.countdown_warnAfter"/>,
                    redirAfter: <s:property value="#session.countdown_redirAfter"/>,
                    countdownBar: true,<s:if test='#session.countdown_stayAfterTimeout'>
                    stayAtPage: true,</s:if>
                    ignoreUserActivity: true
                });
                </s:if>
                $(".taCount" ).each(function( index ) {
                    var oriPB = '';
                    var oriMB = '';
                    var ta = document.getElementById($(this).prop("id"));
                    var width = ta.clientWidth;
                    var height = ta.clientHeight;
                    $( '<span class="label label-default" id="'+$(this).prop("id")+'_count"></span>' ).insertAfter( $( this ) );
                    var _count_id = $(this).prop("id")+"_count";
                    var _id = $(this).prop("id");
                    var newPB = 5;
                    var newMB = 0;
                    $(this).on('blur', function() {
                        $("#"+_id+"_div").css('padding-bottom', oriPB);
                        $("#"+_count_id).html("");
                    });
                    $(this).on("mouseup", function(){
                        if($(this).prop("clientWidth") !== width || $(this).prop("clientHeight") !== height){
                            $(this).focus();
                            $(this).prop("selectionStart", $(this).val().length);
                            $(this).prop("selectionEnd", $(this).val().length);
        //                            ta.selectionStart = $(this).val().length;
        //                            ta.selectionEnd = $(this).val().length;
                        }
                        width = ta.clientWidth;
                        height = ta.clientHeight;
                      });
                    $(this).inputFilter(function(value) {
//                        console.log('id = ' + _id+"_div");
//                        var position = $("#"+_id+"_div").position();
//                        console.log(_id + ":" + position.top);
        //                $("#"+_count_id).css({top: position.top + $("#"+_id).height()+10, left: position.left, position:'absolute'});
                        if (value.length > $("#"+_id).attr("maxlength")) {
                            $("#"+_id).val(value.substring(0, $("#"+_id).attr("maxlength")));
                            return false;
                        } else {
                            if (oriPB === '') {
                                oriPB = $("#"+_id+"_div").css('padding-bottom');
                                if (oriPB === undefined) {
                                    oriPB = '';
                                }
                                newPB = 5 + parseInt(oriPB.replaceAll('px', ''));
                            }
                            if (oriMB === '') {
                                oriMB = $("#"+_id).css('margin-bottom');
                                if (oriMB === undefined) {
                                    oriMB = '';
                                }
                                newMB = 0 + parseInt(oriMB.replaceAll('px', ''));
                            }
                            $("#"+_id+"_div").css('padding-bottom', newPB+"px");
                            $("#"+_id).css('margin-bottom', newMB+"px");
                            $("#"+_count_id).html(value.length + "/" + $("#"+_id).attr("maxlength"));
                            return true;
                        }
        //                        return /^\d*$/.test(value)&&(value === "" || parseFloat(value) <= 100000);
                    });
        //            console.log( index + ": " + $( this ).prop("id") );
                });
                <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
                $("#swkid_plugin_m").html($("#swkid_plugin").html());

                $(".swkid-profile-picture").on("click", function (ev) {
                    if (document.getElementById('sso-bar-lang-ul')) {
                        document.getElementById('sso-bar-lang-ul').style.display = 'none';
                    }
                    $(this).closest("div").find('.swkid-plugin-ul').each(function (i, row) {
                        if ($(this).css("display") !== 'block') {
                            // ul.style.display = 'block';
                            $(this).css("display", "block");
                        } else {
                            $(this).css("display", "none");
                            // ul.style.display = 'none';
                        }
                    });
                });
                </s:if>
            });
            function registerDateRangePicker(drPicker) {
                $('input[name="' + drPicker + '"]').daterangepicker({
                    autoUpdateInput: true,
                    autoApply: true
                });
                $('input[name="' + drPicker + '"]').on('apply.daterangepicker', function (ev, picker) {
                    $(this).val(picker.startDate.format('<s:text name="date_default_date_dateRangePicker"/>') + ' - ' + picker.endDate.format('<s:text name="date_default_date_dateRangePicker"/>'));
                    $("#" + drPicker + "From").val(picker.startDate.format('<s:text name="date_default_date_dateRangePicker"/>'));
                    $("#" + drPicker + "To").val(picker.endDate.format('<s:text name="date_default_date_dateRangePicker"/>'));
                });
                $('#clear' + drPicker).on("click", function (ev) {
                    $('#' + drPicker).val('');
                    $('#' + drPicker + 'From').val('');
                    $('#' + drPicker + 'To').val('');
                });
                $('input[name="' + drPicker + '"]').on("change", function (ev) {
                    if ($(this).val().trim() === '') {
                        $("#" + drPicker + "From").val('');
                        $("#" + drPicker + "To").val('');
                        $(this).val('');
                    }
                });
            }
        </script>

        <decorator:head/>
    </head>

    <body>
        <%-- <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
             <s:if test="#session.sSysUserObjId != null">
         <div class="swkheader-system-wrapper">
             <div class="swkheader-system-container">
                 <div id="swkheader-system" class="swkheader-system swkheader-system-website">
                     <div class="swkheader-system-row">
                         <div class="swkheader-system-left">
                             <div class="swkheader-system-button">
                                 <a class="icon-state-crest" href="https://sarawakid-tnt.sarawak.gov.my/web/home/index/"><img src="https://sarawakid-tnt.sarawak.gov.my/web/web/default/res/icon/sarawak_hornbill.png"></a>
                                 <a class="icon-app-logo" href="https://sarawakid-tnt.sarawak.gov.my/web/home/index/"><img src="https://sarawakid-tnt.sarawak.gov.my/web/web/default/res/icon/image_sarawakid_logo.png"></a>
                             </div>
                         </div>
                         <div class="swkheader-system-center">
                             <a href="welcome">
                                 <div class="swkheader-system-name"><s:text name="system.name"/></div>
                                 <div class="swkheader-system-name-short"><s:text name="system.shortname"/></div>
                             </a>
                         </div>


                        <div class="swkheader-system-right">
                            <div class="swkheader-system-button-div">

                            </div>
                            <div class="swkheader-system-button">
                                <div id="swkid_plugin"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
            </s:if>
        </s:if>--%>
        <!--Fixed navbar-->
        <div class="navbar navbar-size-large navbar-default" role="navigation">
            <!--System name (Desktop view)-->
            <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV] && #session.sSysUserObjId != null">
            <!-- <div class="navbar-brand navbar-brand-primary navbar-brand-logo navbar-nav-padding-left eLogo">  
                <div class="swkheader-system-row" style="padding:0">
                    <div class="swkheader-system-button">
                        <a class="icon-state-crest" href="https://sarawakid-tnt.sarawak.gov.my/web/home/index/"><img src="https://sarawakid-tnt.sarawak.gov.my/web/web/default/res/icon/sarawak_hornbill.png"></a>
                        <a class="icon-app-logo" href="https://sarawakid-tnt.sarawak.gov.my/web/home/index/"><img src="https://sarawakid-tnt.sarawak.gov.my/web/web/default/res/icon/image_sarawakid_logo.png"></a>
                    </div>
                    <a href="welcome">
                        <div class="swkheader-system-name"><s:text name="system.name"/></div>
                        <div class="swkheader-system-name-short"><s:text name="system.shortname"/></div>
                    </a>
                </div>
            </div>-->
                <nav class="desktop-view-menu">
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="padding-right:0;">
                        <s:if test='#session.logined.equals("true")'>
                        <ul class="nav navbar-nav">
                            <li class="active"><button class="navbar-toggle collapse in" data-toggle="collapse" id="menu-toggle-2"> <span class="glyphicon glyphicon-th-large" aria-hidden="true"></span></button></li>
                        </ul>
                        </s:if>
                        <ul class="nav navbar-nav navbar-left">
                            <div class="" style="padding-top:10px;">
                                <h3><s:text name="system.name"/></h3>
                            </div>
                        </ul>
                        <s:if test='#session.logined.equals("true")'>
                        <ul class="nav navbar-nav navbar-right">
                            <li>
                                <div class="swkheader-system-right" style="padding-top:5px;">
                                    <div class="swkheader-system-button-div">

                                    </div>
                                    <div class="swkheader-system-button" style="padding-top:5px;vertical-align: middle;line-height: 30px;">
                                        <s:property value="#session.userName"/>
                                        <div id="swkid_plugin"></div>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div class="btn-group" style="padding:10px">
                                    <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <img src="images/menu_vertical.png" alt="people" class="img-circle " style="width:30px"/>
                                    </a>
                                    <ul class="dropdown-menu" style="width:130px;">
                                        <li><a href="loadPreference"><i class="fa fa-user"></i>&nbsp;&nbsp; My Profile</a></li>
                                    </ul>
                                </div>
                            </li>
                        </ul>
                        </s:if>
                    </div>
                </nav>
            </s:if>
            <s:else>
                <!--<div class="navbar-brand navbar-brand-primary navbar-brand-logo navbar-nav-padding-left eLogo">
                    <a class="sysName" href="welcome"><s:text name="systemInfo.systemShortName"/> <small><s:text name="systemInfo.version2"/></small></a>
                </div>-->
                <nav class="desktop-view-menu">
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <s:if test='#session.logined.equals("true")'>
                        <ul class="nav navbar-nav">
                            <li class="active"><button class="navbar-toggle collapse in" data-toggle="collapse" id="menu-toggle-2"> <span class="glyphicon glyphicon-th-large" aria-hidden="true"></span></button></li>
                            <!--<li><h4 class="sys-fullname"><s:text name="systemInfo.systemName"/> <s:text name="systemInfo.version"/></h4></li>-->
                        </ul>
                        </s:if>
                        <ul class="nav navbar-nav navbar-left">
                            <div class="" style="padding-top:10px;">
                                <h3><a href="welcome"><s:text name="system.name"/></a></h3>
                            </div>
                        </ul>
                            <s:if test='#session.logined.equals("true")'>
                        <ul class="nav navbar-nav navbar-right">
                            <s:if test="#session.multilingualSupport">
                            <li>
                                <div class="btn-group" style="padding:10px">
                                <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <!--<img src="images/current_user.png" alt="people" class="img-circle "/>-->
                                    <img src="<s:property value='#session.defaultLanguage.substring(#session.defaultLanguage.indexOf(";")+1)'/>" alt="<s:property value='#session.defaultLanguage.substring(0, #session.defaultLanguage.indexOf(";"))'/>" class="img-circle" style="width: 30px; height: 30px; border-radius: 50%;"/>
                                </a>
                                    <s:iterator value="#session.multilingualDD" var="theLanguage">
                                    <ul class="dropdown-menu" style="width:130px;">
                                        <li><a href="changeLocale?language=<s:property value='#theLanguage.substring(0, #theLanguage.indexOf(";"))'/>"><img src="<s:property value='#theLanguage.substring(#theLanguage.indexOf(";")+1)'/>" alt="people" class="img-circle" style="width: 30px; height: 30px; border-radius: 50%;"/>&nbsp;&nbsp; <s:property value='#theLanguage.substring(0, #theLanguage.indexOf(";")).toUpperCase()'/></a></li>
                                    </ul>
                                    </s:iterator>
                                </div>
                            </li>
                            </s:if>
                            <li>
                                <s:property value="#session.userName"/>
                                <div class="btn-group" style="padding:10px">
                                <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <!--<img src="images/current_user.png" alt="people" class="img-circle "/>-->
                                    <img src="https://sarawakid-tnt.sarawak.gov.my/web/web/default/res/empty.png" alt="people" class="img-circle" style="width: 30px; height: 30px; border-radius: 50%;"/>
                                </a>
                                    <ul class="dropdown-menu" style="width:130px;">
                                        <li><a href="loadPreference"><i class="fa fa-user"></i>&nbsp;&nbsp; My Profile</a></li>
                                        <li><a href="processlogoutLogout" ><i class="fa fa-sign-out"></i>&nbsp;&nbsp; Logout</a></li>
                                    </ul>
                                </div>
                            </li>
                            <li>

                            </li>
                            <!--<li><h4 class="sys-fullname"><s:text name="systemInfo.systemName"/> <s:text name="systemInfo.version"/></h4></li>-->
                        </ul>
                            </s:if>
                    </div>
                </nav>
            </s:else>
            <!--System name (Tablet/mobile view)-->
            <div class="mobile-view-menu">
                <div style="margin-top:8px; display: inline-flex;">
                    <s:text name="systemInfo.systemShortName"/> <small><s:text name="systemInfo.version2"/></small>
                </div>
                <ul class="nav navbar-nav navbar-right" style="float:right; display: inline-flex;">
                    <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV] && #session.sSysUserObjId != null">
                        <li>      
                            <div class="swkheader-system-right">
                                <div class="swkheader-system-button-div">

                                </div>
                                <div class="swkheader-system-button" style="padding-top:5px;vertical-align: middle;line-height: 30px;">
                                    <div id="swkid_plugin_m"></div>
                                </div>
                            </div>
                        </li>    
                        <li>
                            <div class="btn-group" style="padding-top:5px;">
                                <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <img src="images/menu_vertical.png" alt="people" class="img-circle " style="width:30px;"/>
                                </a>
                                <ul class="dropdown-menu" style="width:130px;">
                                    <li><a href="loadPreference"><i class="fa fa-user"></i>&nbsp;&nbsp; My Profile</a></li>
                                </ul>
                            </div>
                        </li>
                    </s:if>
                    <s:else>
                        <s:if test="#session.multilingualSupport">
                            <li>
                                <div class="btn-group" style="padding:5px;padding-right:15px;">
                                <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <!--<img src="images/current_user.png" alt="people" class="img-circle "/>-->
                                    <img src="<s:property value='#session.defaultLanguage.substring(#session.defaultLanguage.indexOf(";")+1)'/>" alt="<s:property value='#session.defaultLanguage.substring(0, #session.defaultLanguage.indexOf(";"))'/>" class="img-circle" style="width: 30px; height: 30px; border-radius: 50%;"/>
                                </a>
                                    <s:iterator value="#session.multilingualDD" var="theLanguage">
                                    <ul class="dropdown-menu" style="width:130px;">
                                        <li><a href="changeLocale?language=<s:property value='#theLanguage.substring(0, #theLanguage.indexOf(";"))'/>"><img src="<s:property value='#theLanguage.substring(#theLanguage.indexOf(";")+1)'/>" alt="people" class="img-circle" style="width: 30px; height: 30px; border-radius: 50%;"/>&nbsp;&nbsp; <s:property value='#theLanguage.substring(0, #theLanguage.indexOf(";")).toUpperCase()'/></a></li>
                                    </ul>
                                    </s:iterator>
                                </div>
                            </li>
                            </s:if> 
                        <s:if test='#session.logined.equals("true")'>
                        <li>
                            <div class="btn-group" style="padding:5px;padding-right:15px;">
                            <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <!--<img src="images/current_user.png" alt="people" class="img-circle "/>-->
                                <img src="https://sarawakid-tnt.sarawak.gov.my/web/web/default/res/empty.png" alt="people" class="img-circle" style="width: 30px; height: 30px; border-radius: 50%;"/>
                            </a>
                                <ul class="dropdown-menu" style="width:130px;">
                                    <li><a href="loadPreference"><i class="fa fa-user"></i>&nbsp;&nbsp; My Profile</a></li>
                                    <li><a href="processlogoutLogout" ><i class="fa fa-sign-out"></i>&nbsp;&nbsp; Logout</a></li>
                                </ul>
                            </div>
                        </li>
                        </s:if>
                    </s:else>
                    <s:if test='#session.logined.equals("true")'>
                    <li class="">
                        <button type="button" class="navbar-toggle2" data-toggle="collapse" id="menu-toggle">
                            <span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
                        </button>
                    </li>
                    </s:if>
                </ul>
            </div>
        </div>

        <s:if test='#session.logined.equals("true")'>
        <div id="wrapper" <s:if test='#session.msb.equals("H")'>class='toggled-2'</s:if>>
                <!--Sidebar-->
                <div id="sidebar-wrapper">
                    <ul class="sidebar-nav nav-pills nav-stacked" id="menu">
                    <%--<div class="profile" <s:if test='#session.msb.equals("H")'>style='display: none;'</s:if>>
                        <a href="#">
                            <img src="images/empty.jpg" alt="people" class="img-circle width-80"/>
                        </a>
                        <h5 class="welcomeText">Welcome,</h5>
                        <h4 class="text-display-1 margin-none">
                            <a href="loadPreference" style="color: #fff"><s:property value="#session.userName"/></a>
                            <br>
                            <s:if test="#session.sSysUserObjId == null">
                                <button onclick="document.location = 'processlogoutLogout'" class="btn btn-default btn-logout"><i class="fa fa-sign-out"></i>&nbsp;&nbsp;Logout</button>
                            </s:if>
                    <%--<s:else>
                        <button onclick="javascript:swkid_logout('https://sarawakid-tnt.sarawak.gov.my/web/share/ssologout/',{logout_redirect_uri: '<%=SystemConstants.SarawakID.SYSTEM_URL[SystemConstants.ENV]%>/processlogoutLogout',return_info:true});" class="btn btn-default btn-logout"><i class="fa fa-sign-out"></i>&nbsp;&nbsp;Logout</button>
                    </s:else>--%
                </h4>
            </div>--%>
                    <ul class="nav-list"><s:property value="#session.menuList" escapeHtml="false"/></ul>
                </ul>
            </div>
            <!--Page Content-->
            <div id="page-content-wrapper">
                <div class="container-fluid bottomMargin">
                    <!--Breadcrumb-->
                    <page:applyDecorator page="/main/breadcrumb.jsp" name="panel1" />
                    <!--Content-->
                    <decorator:body />
                </div>
            </div>

            <!--Footer-->
            <footer class="footer">
                <div class="desktop_footer">
                    <b><s:text name="systemInfo.systemName"/> &nbsp;|&nbsp; <s:text name="systemInfo.version"/></b>
                    <br>
                        <font style="font-size:11px;"><s:text name="systemInfo.copyRight"/> <s:text name="systemInfo.copyRightYear"/> &nbsp;|&nbsp; <s:text name="systemInfo.bestView"/></font>
                </div>
                <div class="mobile_footer">
                    <b><s:text name="systemInfo.systemName"/> &nbsp;|&nbsp; <s:text name="systemInfo.version"/></b> &nbsp;|&nbsp;
                    <font style="font-size:11px;"><s:text name="systemInfo.copyRight"/> <s:text name="systemInfo.copyRightYear"/></font>
                </div>
            </footer>
        </div>
        </s:if><s:else>
            <div id="wrapper" style="padding-left: 0px">
                <div id="page-content-wrapper">
                    <div class="container-fluid bottomMargin">
                        <!--Breadcrumb-->
                        <page:applyDecorator page="/main/breadcrumb.jsp" name="panel1" />
                        <!--Content-->
                        <decorator:body />
                    </div>
                </div>

                <!--Footer-->
                <footer class="footer">
                    <div class="desktop_footer">
                        <b><s:text name="systemInfo.systemName"/> &nbsp;|&nbsp; <s:text name="systemInfo.version"/></b>
                        <br>
                            <font style="font-size:11px;"><s:text name="systemInfo.copyRight"/> <s:text name="systemInfo.copyRightYear"/> &nbsp;|&nbsp; <s:text name="systemInfo.bestView"/></font>
                    </div>
                    <div class="mobile_footer">
                        <b><s:text name="systemInfo.systemName"/> &nbsp;|&nbsp; <s:text name="systemInfo.version"/></b> &nbsp;|&nbsp;
                        <font style="font-size:11px;"><s:text name="systemInfo.copyRight"/> <s:text name="systemInfo.copyRightYear"/></font>
                    </div>
                </footer>
            </div>
        </s:else>

        <div class="modal fade" id="lookupModal" tabindex="-1" role="dialog" aria-labelledby="lookupModalLabel"></div>
        <div class="modal fade fixMarginLeft" id="moreInfoDiv"></div>  
        <div id='encode' class='hidden'></div>

        <!--Alert Modal-->
        <div id="alertDiv" class="modal fade" tabindex="-1" data-width="" data-height="" style="display: none;" data-keyboard="true">
            <div class="">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close btnCloseAlert" data-dismiss="modal" aria-hidden="true">×</button>
                        <div class="title"></div>
                    </div>
                    <div class="modal-body myModalContent"></div>
                </div>
            </div>
        </div>
        <div id="loadingModal" class="modal fade" tabindex="-1" data-width="" data-height="" style="display: none; padding-right: 0 !important;" data-backdrop="static" data-keyboard="false">
            <div class="modal-content">
                <div class="modal-body myModalContent">
                    <div class="spinner-border"></div><%--Loading...--%>
                </div>
            </div>  
        </div>
        <!--Bootstrap-->
        <script src="include/bootstrap/bootstrap.js"></script>
        <script src="include/bootstrap/sidebar_menu.js"></script>

        <!--Back-to-top-->
        <script src="include/back-to-top/back-to-top.js"></script>

        <!--Select2-->
        <script src="include/select2/select2.js"></script>

        <!--Modal-->
        <script src="include/modal/bootstrap-modalmanager.js"></script>
        <script src="include/modal/bootstrap-modal.js"></script>
        <script src="include/modal/bootstrap-waitingfor.js"></script>
        <script type="text/javascript">
            var fromStart = true;
            var moreInfoDiv_relogin = false;
            $(document).ready(function () {
                $.each($(".accordion-heading"), function (i, theMenu) {
                    var _menu = getCookie("_menu");
                    var openedArr = _menu.split(',');
                    for (var i = 0; i < openedArr.length; i++) {
                        var openMenu = openedArr[i];
                        if (openMenu === $(theMenu).attr("data-target")) {
                            $(theMenu).click();
                            break;
                        }
                    }
                });
                fromStart = false;
//                if (!scrolled) {
//                    window.scrollBy(0, 50);
//                }
//                scrolled = true;
                $('.sds-dropdown').select2();

                $(window).resize(function () {
                    $('.select2').css('width', "100%");
                });
                $('form').find('input[type=text],textarea,select,radio,checkbox').filter(':visible:first').focus();
            <%--<s:if test='#session.menuSideBar.equals("H")'>
                $('#wrapper').addClass('toggled-2');
            </s:if>--%>
            });
            var reloadOnClose = false;
            var offLoading = false;
            var uppyUploadingCount = 0;
            var uppyValidateInprogress=true;
            var uppyShowInprogressMsg=true;
            var uppyHasInprogress=false;
            $('a[href]:not([target="_blank"])').not('[href^="#"]').on('click',function(){
                if (uppyHasInprogress) {
                    offLoading = true;
                }
                if (!offLoading) {
                    $('#loadingModal').modal('show');
                }
                uppyHasInprogress = false;
            });
            $('form:not([target="_blank"])').on('submit',function(){
                if (uppyHasInprogress) {
                    offLoading = true;
                }
                if (!offLoading) {
                    $('#loadingModal').modal('show');
                }
                uppyHasInprogress = false;
            });
        </script>
    </body>
</html>