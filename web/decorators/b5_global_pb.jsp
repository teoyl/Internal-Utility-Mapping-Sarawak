<%-- 
    Document   : b5_global_pb
    Created on : Nov 3, 2023, 3:13:46 PM
    Author     : yonglai
--%>

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
<html data-bs-theme="light" lang="en-US" dir="ltr">
    <head>        
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">


        <!-- ===============================================-->
        <!--    Document Title-->
        <!-- ===============================================-->
        <title><s:text name="system.name" /> - <s:property value="%{pageTitle_}" escapeHtml="false" /></title>
        <s:set var="systemWelcome_"><s:text name="system.welcome"/></s:set>


        <!-- ===============================================-->
        <!--    Favicons-->
        <!-- ===============================================-->
        <link rel="shortcut icon" type="image/x-icon" href="<s:text name="system.icon"/>">
        <meta name="theme-color" content="#ffffff">
        <script src="falcon-v3.16.0/public/assets/js/config.js"></script>
        <script src="falcon-v3.16.0/public/vendors/simplebar/simplebar.min.js"></script>
        <script src="include/jquery/jquery-3.4.1.min.js"></script>


        <!-- ===============================================-->
        <!--    Stylesheets-->
        <!-- ===============================================-->
        <link href="falcon-v3.16.0/public/vendors/select2/select2.min.css" rel="stylesheet">
        <link href="falcon-v3.16.0/public/vendors/select2-bootstrap-5-theme/select2-bootstrap-5-theme.min.css" rel="stylesheet">
        <link href="falcon-v3.16.0/public/vendors/flatpickr/flatpickr.min.css" rel="stylesheet" />
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,500,600,700%7cPoppins:300,400,500,600,700,800,900&amp;display=swap" rel="stylesheet">
        <link href="falcon-v3.16.0/public/vendors/simplebar/simplebar.min.css" rel="stylesheet">
        <link href="falcon-v3.16.0/public/assets/css/theme.min.css" rel="stylesheet" id="style-default">
        <link href="falcon-v3.16.0/public/assets/css/user.css" rel="stylesheet" id="user-style-default">
            
        <!--Back-to-top-->
        <link href="include/back-to-top/back-to-top.css" rel="stylesheet"/>
               
        
        <!-- ===============================================-->
        <!--    SarawakID Integration -->
        <!-- ===============================================-->
        <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
            <link href="https://sarawakid-tnt.sarawak.gov.my/web/web/default/sso_bar.css" rel="stylesheet"/>
        </s:if>

        <link href="include/main_styles.css" rel="stylesheet"/>
        <decorator:head/>
        
        <style>
            main .container-fluid {
                padding: 0;
                background: white;
            }
            main .container {
                width: 85%;
            }
            nav {
                border-bottom: 2px solid rgba(0, 0, 0, 0.1);
                background: #fbfbfd;
            }
            .navbar-brand {
                color: black;
            }
            .navbar-utimaps .nav-link {
                font-weight: 500;
                color: black;
            }
            .navbar-utimaps .nav-item {
                margin: 0 10px;
            }
            .fa-bell {
                font-size:24px;
                color: rgb(52, 120, 246);
            }
            .fa-user-circle {
                font-size:30px; 
                color: rgb(52, 120, 246);
            }
            .pageTitle {
                margin-top: 0.5rem;
            }
            .pageTitle_hr_ {
                margin: 0.5rem 0;
                border: 0;
                border-top: 2px solid;
            }
            nav .container {
                padding: 0;
            }
        </style>
    </head>

    <body>

        <!-- ===============================================-->
        <!--    Main Content-->
        <!-- ===============================================-->
        <main class="main" id="top">
            <div class="container" data-layout="container">
                <script>
                    var isFluid = JSON.parse(localStorage.getItem('isFluid'));
                    if (isFluid) {
                        var container = document.querySelector('[data-layout]');
                        container.classList.remove('container');
                        container.classList.add('container-fluid');
                    }
                </script>
                
                <!--testing-->
                <s:set var="#session.logined">true</s:set>
                <s:if test='#session.logined.equals("true")'>
                    <nav class="navbar navbar-expand-lg navbar-light navbar-utimaps">
                        <div class="container">
                            <a class="navbar-brand" href="<s:url value="/"/>">eSubmission</a>
                            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mynavbar">
                                <span class="navbar-toggler-icon"></span>
                            </button>
                            <div class="collapse navbar-collapse" id="mynavbar">
                                <ul class="navbar-nav me-auto">
                                    <li class="nav-item">
                                        <a class="nav-link" href="javascript:void(0)">Home</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="javascript:void(0)">New Utility Submission</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="javascript:void(0)">My Submissions</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="javascript:void(0)">Payments</a>
                                    </li>
                                </ul>
                                <ul class="navbar-nav navbar-nav-icons ms-auto flex-row align-items-center">
                                    <li class="nav-item">
                                        <div class="theme-control-toggle fa-icon-wait">
                                            <input class="form-check-input ms-0 theme-control-toggle-input" id="themeControlToggle" type="checkbox" data-theme-control="theme" value="dark" />
                                            <label class="mb-0 theme-control-toggle-label theme-control-toggle-light" for="themeControlToggle" data-bs-toggle="tooltip" data-bs-placement="left" title="Switch to light theme"><span class="fas fa-sun fs-0"></span></label>
                                            <label class="mb-0 theme-control-toggle-label theme-control-toggle-dark" for="themeControlToggle" data-bs-toggle="tooltip" data-bs-placement="left" title="Switch to dark theme"><span class="fas fa-moon fs-0"></span></label>
                                        </div>
                                    </li>
                                    <li class="nav-item px-2">
                                        <a class="" href="/utimaps/loadListPageMessage">
                                            <span class="far fa-bell"></span>
                                        </a>
                                    </li>
                                    <%--
                                    <s:if test='#session.multilingualSupport'>
                                    <li class="nav-item dropdown">
                                        <a class="nav-link pe-0 ps-2" id="navbarMultilingual" role="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <div class="avatar avatar-l">
                                                <img class="rounded-circle" src="<s:property value='#session.defaultLanguage.substring(#session.defaultLanguage.indexOf(";")+1)'/>" alt="<s:property value='#session.defaultLanguage.substring(0, #session.defaultLanguage.indexOf(";"))'/>" />
                                            </div>
                                        </a>
                                        <div class="dropdown-menu dropdown-caret dropdown-menu-end py-0" aria-labelledby="navbarMultilingual">
                                            <div class="bg-white dark__bg-1000 rounded-2 py-2">
                                                <s:iterator value="#session.multilingualDD" var="theLanguage">
                                                    <a class="dropdown-item d-flex align-items-center" href="changeLocale?language=<s:property value='#theLanguage.substring(0, #theLanguage.indexOf(";"))'/>">
                                                        <div class="avatar avatar-l me-2">
                                                            <img class="rounded-circle" src="<s:property value='#theLanguage.substring(#theLanguage.indexOf(";")+1)'/>" alt="<s:property value='#theLanguage.substring(0, #theLanguage.indexOf(";")).toUpperCase()'/>" />
                                                        </div>
                                                        <s:property value='#theLanguage.substring(0, #theLanguage.indexOf(";")).toUpperCase()'/>
                                                    </a>
                                                </s:iterator>
                                            </div>
                                        </div>
                                    </li>
                                    </s:if> --%>
                                    <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV] && #session.sSysUserObjId != null">
                                        <li class="nav-item">
                                            <div id="swkid_plugin"></div>
                                        </li>
                                    </s:if>
                                    <s:else>
                                    <li class="nav-item dropdown">
                                        <a class="nav-link pe-0 ps-2" id="navbarDropdownUser" role="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <div class="avatar avatar-xl">
                                                <span class="fas fa-user-circle"></span>
                                            </div>
                                        </a>
                                        <div class="dropdown-menu dropdown-caret dropdown-menu-end py-0" aria-labelledby="navbarDropdownUser">
                                            <div class="bg-white dark__bg-1000 rounded-2 py-2">
                                                <a class="dropdown-item">
                                                    <p class="d-none d-md-block mb-0"><s:property value="#session.userName"/></p>
                                                </a>
                                                <div class="dropdown-divider"></div>
                                                <a class="dropdown-item" href="loadPreference">My Profile</a>
                                                <div class="dropdown-divider"></div>
                                                <a class="dropdown-item" href="processlogoutLogout">Logout</a>
                                            </div>
                                        </div>
                                    </li>
                                    </s:else>
                                </ul>
                            </div>
                        </div>
                    </nav>
                    <div class="content">
                        
                        <page:applyDecorator page="/main/breadcrumb.jsp" name="panel1" />

                        <div class="container">
                            
                            <h3 class="pageTitle"><s:property value="%{pageTitle_}" escapeHtml="false" /></h3>
                            <hr class="pageTitle_hr_" />
                            
                            <decorator:body />
                            
                        </div>

                        <footer class="footer">
                            <hr />
                            <div class="container">
                                <div class="row g-0 justify-content-between fs--1 mt-2 mb-3">
                                    <div class="col-12 col-sm-auto text-center">
                                        <p class="mb-0 text-600"><s:text name="systemInfo.copyRight"/> <s:text name="systemInfo.copyRightYear"/>
                                            <span class="d-none d-lg-inline-block"> &nbsp;<s:text name="systemInfo.gov"/>. &nbsp;<s:text name="systemInfo.allRights"/>.</span>
                                            <!--<span class="d-none d-lg-inline-block"> &nbsp;|&nbsp; <s:text name="systemInfo.bestView"/></span>-->
                                        </p>
                                    </div>
                                    <div class="col-12 col-sm-auto text-center">
                                        <p class="mb-0 text-600"><s:text name="systemInfo.version"/></p>
                                    </div>
                                </div>
                            </div>
                        </footer>
                    </div>
            
                </s:if>
                <s:else>
                    
                    <decorator:body />
                    
                </s:else>
                
                <div class="modal fade" id="lookupModal" tabindex="-1" role="dialog" aria-labelledby="lookupModalLabel"></div>
                <div class="modal fade fixMarginLeft" id="moreInfoDiv"></div>  
                <div id='encode' class='d-none'></div>

                <!-- Alert Modal-->
                <div id="alertDiv" class="modal fade" tabindex="-1" data-bs-keyboard="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title title"></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body myModalContent"></div>
                        </div>
                    </div>
                </div>
                
                <!-- Loading Modal-->
                <div id="loadingModal" class="modal fade" tabindex="-1" data-bs-backdrop="static" data-bs-keyboard="false">
                    <div class="modal-dialog modal-dialog-centered" style="width:fit-content;">
                        <div class="modal-content">
                            <div class="modal-body myModalContent">
                                <div class="spinner-border text-primary">
                                    <span class="visually-hidden">Loading...</span>
                                </div><%--Loading...--%>
                            </div>
                        </div>  
                    </div>
                </div>
                
            </div>
        </main>
        <!-- ===============================================-->
        <!--    End of Main Content-->
        <!-- ===============================================-->


        <!-- ===============================================-->
        <!--    JavaScripts-->
        <!-- ===============================================-->
        <script src="falcon-v3.16.0/public/vendors/popper/popper.min.js"></script>
        <script src="falcon-v3.16.0/public/vendors/bootstrap/bootstrap.min.js"></script>
        <script src="falcon-v3.16.0/public/vendors/anchorjs/anchor.min.js"></script>
        <script src="falcon-v3.16.0/public/vendors/is/is.min.js"></script>
        <script src="falcon-v3.16.0/public/vendors/select2/select2.min.js"> </script>
        <script src="falcon-v3.16.0/public/vendors/select2/select2.full.min.js"> </script>
        <script src="falcon-v3.16.0/public/assets/js/flatpickr.js"></script>
        <script src="falcon-v3.16.0/public/vendors/fontawesome/all.min.js"></script>
        <script src="falcon-v3.16.0/public/vendors/lodash/lodash.min.js"></script>
        <script src="https://polyfill.io/v3/polyfill.min.js?features=window.scroll"></script>
        <script src="falcon-v3.16.0/public/vendors/list.js/list.min.js"></script>
        <script src="falcon-v3.16.0/public/vendors/inputmask/inputmask.min.js"></script>
        <script src="falcon-v3.16.0/public/assets/js/theme.js"></script>
                
        <script src="include/bootstrap/bootstrap-idletimeout/script/bootstrap-session-timeout.js" type="text/javascript"></script>
        <script src="include/bootstrap/b5_sidebar_menu.js"></script>
        
        <!--Back-to-top-->
        <script src="include/back-to-top/back-to-top.js"></script>
        
        <!--Date and daterange picker-->
        <!--<script type="text/javascript" language="javascript" src="include/datepicker/bootstrap-datepicker3.js"></script>-->
        <!--<script type="text/javascript" language="javascript" src="include/daterangepicker/moment.js"></script>-->
        <!--<script type="text/javascript" language="javascript" src="include/daterangepicker/daterangepicker.js"></script>-->
        
        <script type="text/javascript" language="javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" language="javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" language="javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" language="javascript" src="pages/scripts/lookup.js"></script>  


        <!-- ===============================================-->
        <!--    SarawakID Integration -->
        <!-- ===============================================-->
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
//                    style:'icon-text',
                    icon_width: '30',
//                    position:'',
                    misc_param: 'test',
//                    profile_listing_style: 'details',
//                    profile_listing_email:'on',
//                    force_login_btn: 'off'
                });
//                swkid_login_form_submit();
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
                    $( '<span class="badge badge-subtle-secondary mt-1" id="'+$(this).prop("id")+'_count"></span>' ).insertAfter( $( this ) );
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
                $('#' + drPicker + '_DateRange').flatpickr({
                    mode: 'range',
                    dateFormat: newDateRangePicker_defaultFormat,
                    wrap: true,
                    onChange: function(selectedDates, dateStr, instance) {
                        var sdt = '';
                        var edt = '';
                        if (selectedDates[0]) {
                            sdt = instance.formatDate(selectedDates[0], newDateRangePicker_defaultFormat);
                        }
                        if (selectedDates[1]) {
                            edt = instance.formatDate(selectedDates[1], newDateRangePicker_defaultFormat);
                        }
                        
                        $("#" + drPicker + "From").val(sdt);
                        $("#" + drPicker + "To").val(edt);
                        $("#" + drPicker).val(sdt + " - " + edt);
                    }
                });
            }
        </script>
                
        <script type="text/javascript">
            $(document).ready(function () {                    
                $.each($('.sds-dropdown'), function () {
                    var opt = {
                        theme: 'bootstrap-5'
                    };
                    if ($(this).hasClass('form-control-sm')) {
                        opt.selectionCssClass = 'select2--small';
                        opt.dropdownCssClass = 'select2--small';
                    }
                    var options = $.extend(opt, $(this).data('options'));
                    $(this).select2(options);
                });        

                $('form').find('input[type=text],textarea,select,radio,checkbox').filter(':visible:first').focus();
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