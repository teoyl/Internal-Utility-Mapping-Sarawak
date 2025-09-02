<%@page import="com.sains.common.util.SystemConstants"%>
<%@page import="com.sains.common.util.AesUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<% String strStateCode = null;%>
<s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
    <%
        strStateCode = (session.getAttribute("sesStateCode") == null ? "" : session.getAttribute("sesStateCode").toString());
        if (strStateCode.equals("")) {
            strStateCode = AesUtil.random(10);
            session.setAttribute("sesStateCode", strStateCode);
        }
    %>
</s:if>
<s:set var="ctx" value="%{pageContext.request.contextPath}"/>
<%boolean stickyFooter=true;%>
<s:set var="stickyFooter"><%=stickyFooter%></s:set>
<html lang="en">
    <head>
        <base href="">
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <s:set var="systemWelcome_"><s:text name="system.welcome"/></s:set>
        <style>
            .title-alert {
                /*border-bottom: 1px solid #ddd;*/
                border-left: 2px solid red;
                font-size: 22px;
                font-weight: 300;
                /*margin-bottom: 16px;*/
                /*margin-top: 0;*/
                /*padding-bottom: 5px;*/
                padding-left: 15px;
            }
        </style>
        <title><s:text name="system.shortname"/> <s:text name="system.name"/></title>

        <link rel="icon" href="<s:text name="system.icon"/>" type="image/x-icon"/>

        <!--Fonts-->
        <link href="include/fonts/fonts.css" rel="stylesheet"/>
        <link href="include/fonts/font-awesome.css" rel="stylesheet"/>
        <link href="include/assets/css/font/font.css" rel="stylesheet">
            
        <link href="include/assets/css/customs.css" rel="stylesheet"/>
        <link href="include/assets/plugins/global/plugins.bundle.css" rel="stylesheet" type="text/css" />
        <link href="include/assets/css/style.bundle.css" rel="stylesheet" type="text/css" />
        
        <!--begin::Layout Skins(used by all pages) -->
        <link href="include/assets/css/skins/header/base/light.css" rel="stylesheet" type="text/css" />
        <link href="include/assets/css/skins/header/menu/light.css" rel="stylesheet" type="text/css" />
        <link href="include/assets/css/skins/brand/dark.css" rel="stylesheet" type="text/css" />
        <link href="include/assets/css/skins/aside/dark.css" rel="stylesheet" type="text/css" />
        
        <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
            <link href="https://sarawakid-tnt.sarawak.gov.my/web/web/default/sso_bar.css" rel="stylesheet"/>
        </s:if>

        <script src="include/jquery/jquery-3.4.1.min.js"></script>
        
        <!--Input Mask-->
        <script type="text/javascript" language="javascript" src="include/input-mask/jquery.inputmask.js"></script>
        <script type="text/javascript" language="javascript" src="include/input-mask/jquery.inputmask.extensions.js"></script>

        <script type="text/javascript" language="javascript" src="pages/scripts/common.js"></script>
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
                    profile_listing_style: 'details',
                    //                profile_listing_email:'on',
                    //                force_login_btn: 'off'
                });
                //            swkid_login_form_submit();
            });
            function swkid_callback(returnObj) {
                console.log(returnObj);
            }
            </s:if>
            function profile_onclick() {
                return;
            }
            $(document).ready(function () {
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
            });
            function registerDateRangePicker(drPicker) {
                $('input[name="' + drPicker + '"]').daterangepicker({
                    autoUpdateInput: true,
                    autoApply: true
                });
                $('input[name="' + drPicker + '"]').on('apply.daterangepicker', function (ev, picker) {
                    $(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
                    $("#" + drPicker + "From").val(picker.startDate.format('DD/MM/YYYY'));
                    $("#" + drPicker + "To").val(picker.endDate.format('DD/MM/YYYY'));
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

    <body class="kt-quick-panel--right kt-demo-panel--right kt-offcanvas-panel--right kt-header--fixed kt-header-mobile--fixed kt-subheader--enabled kt-subheader--fixed kt-subheader--solid kt-aside--enabled kt-aside--fixed kt-page--loading <s:if test="#stickyFooter == 'true'">kt-footer--fixed</s:if>">
       
        <!-- begin:: Header Mobile -->
        <div id="kt_header_mobile" class="kt-header-mobile  kt-header-mobile--fixed ">
            <div class="kt-header-mobile__logo">
                <a href="welcome">
                    <!--<img alt="Logo" src="include/assets/media/logos/logo-light.png" />-->
                    <span style="font-size:1.2rem;font-family: Poppins;color:white">FDS (MySQL) <small>v1.0</small></span>
                </a>
            </div>
            <div class="kt-header-mobile__toolbar">
                <button class="kt-header-mobile__toggler kt-header-mobile__toggler--left" id="kt_aside_mobile_toggler"><span></span></button>
                <!--<button class="kt-header-mobile__toggler" id="kt_header_mobile_toggler"><span></span></button>-->
                <button class="kt-header-mobile__topbar-toggler" id="kt_header_mobile_topbar_toggler"><i class="flaticon-more"></i></button>
            </div>
        </div>
        <!-- end:: Header Mobile -->

        <div class="kt-grid kt-grid--hor kt-grid--root">
            <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--ver kt-page">
                <div class="kt-aside  kt-aside--fixed  kt-grid__item kt-grid kt-grid--desktop kt-grid--hor-desktop" id="kt_aside">

                    <!-- begin:: Aside -->
                    <div class="kt-aside__brand kt-grid__item " id="kt_aside_brand">
                        <div class="kt-aside__brand-logo">
                            <a href="welcome">
                                <!--<img alt="Logo" src="include/assets/media/logos/logo-light.png" />-->
                                <span style="font-size:1.2rem;font-family: Poppins;color:white">FDS (MySQL) <small>v1.0</small></span>
                            </a>
                        </div>
                        <div class="kt-aside__brand-tools">
                            <button class="kt-aside__brand-aside-toggler" id="kt_aside_toggler">
                                <span>
                                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px" viewBox="0 0 24 24" version="1.1" class="kt-svg-icon">
                                        <g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                                            <polygon points="0 0 24 0 24 24 0 24" />
                                            <path d="M5.29288961,6.70710318 C4.90236532,6.31657888 4.90236532,5.68341391 5.29288961,5.29288961 C5.68341391,4.90236532 6.31657888,4.90236532 6.70710318,5.29288961 L12.7071032,11.2928896 C13.0856821,11.6714686 13.0989277,12.281055 12.7371505,12.675721 L7.23715054,18.675721 C6.86395813,19.08284 6.23139076,19.1103429 5.82427177,18.7371505 C5.41715278,18.3639581 5.38964985,17.7313908 5.76284226,17.3242718 L10.6158586,12.0300721 L5.29288961,6.70710318 Z" fill="#000000" fill-rule="nonzero" transform="translate(8.999997, 11.999999) scale(-1, 1) translate(-8.999997, -11.999999) " />
                                            <path d="M10.7071009,15.7071068 C10.3165766,16.0976311 9.68341162,16.0976311 9.29288733,15.7071068 C8.90236304,15.3165825 8.90236304,14.6834175 9.29288733,14.2928932 L15.2928873,8.29289322 C15.6714663,7.91431428 16.2810527,7.90106866 16.6757187,8.26284586 L22.6757187,13.7628459 C23.0828377,14.1360383 23.1103407,14.7686056 22.7371482,15.1757246 C22.3639558,15.5828436 21.7313885,15.6103465 21.3242695,15.2371541 L16.0300699,10.3841378 L10.7071009,15.7071068 Z" fill="#000000" fill-rule="nonzero" opacity="0.3" transform="translate(15.999997, 11.999999) scale(-1, 1) rotate(-270.000000) translate(-15.999997, -11.999999) " />
                                        </g>
                                    </svg>
                                </span>
                                <span>
                                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px" viewBox="0 0 24 24" version="1.1" class="kt-svg-icon">
                                        <g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                                            <polygon points="0 0 24 0 24 24 0 24" />
                                            <path d="M12.2928955,6.70710318 C11.9023712,6.31657888 11.9023712,5.68341391 12.2928955,5.29288961 C12.6834198,4.90236532 13.3165848,4.90236532 13.7071091,5.29288961 L19.7071091,11.2928896 C20.085688,11.6714686 20.0989336,12.281055 19.7371564,12.675721 L14.2371564,18.675721 C13.863964,19.08284 13.2313966,19.1103429 12.8242777,18.7371505 C12.4171587,18.3639581 12.3896557,17.7313908 12.7628481,17.3242718 L17.6158645,12.0300721 L12.2928955,6.70710318 Z" fill="#000000" fill-rule="nonzero" />
                                            <path d="M3.70710678,15.7071068 C3.31658249,16.0976311 2.68341751,16.0976311 2.29289322,15.7071068 C1.90236893,15.3165825 1.90236893,14.6834175 2.29289322,14.2928932 L8.29289322,8.29289322 C8.67147216,7.91431428 9.28105859,7.90106866 9.67572463,8.26284586 L15.6757246,13.7628459 C16.0828436,14.1360383 16.1103465,14.7686056 15.7371541,15.1757246 C15.3639617,15.5828436 14.7313944,15.6103465 14.3242754,15.2371541 L9.03007575,10.3841378 L3.70710678,15.7071068 Z" fill="#000000" fill-rule="nonzero" opacity="0.3" transform="translate(9.000003, 11.999999) rotate(-270.000000) translate(-9.000003, -11.999999) " />
                                        </g>
                                    </svg>
                                </span>
                            </button>
                            <!--<button class="kt-aside__brand-aside-toggler kt-aside__brand-aside-toggler--left" id="kt_aside_toggler"><span></span></button>-->
                        </div>
                    </div>
                    <!-- end:: Aside -->

                    <!-- begin:: Aside Menu -->
                    <div class="kt-aside-menu-wrapper kt-grid__item kt-grid__item--fluid" id="kt_aside_menu_wrapper">
                        <div id="kt_aside_menu" class="kt-aside-menu ps--active-y" data-ktmenu-vertical="1" data-ktmenu-scroll="1" data-ktmenu-dropdown-timeout="500">
                            <ul class="kt-menu__nav ">
                                <s:property value="#session.menuList" escapeHtml="false"/>
                            </ul>
                        </div>
                    </div>
                    <!-- end:: Aside Menu -->
                </div>
                <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor kt-wrapper" id="kt_wrapper">
                    
                    <!-- begin:: Header -->
                    <div id="kt_header" class="kt-header kt-grid__item  kt-header--fixed ">
                        <div id="kt_header_menu" class="kt-header-menu kt-header-menu-mobile  kt-header-menu--layout-default " style="font-size:1.2rem;font-family: Poppins;">
                            <ul class="kt-menu__nav ">
                                <li class="kt-menu__item">
                                    Framework Development System (MySQL) version 1.0
                                </li>		
                            </ul>
                        </div>

                        <!-- begin:: Header Topbar -->
                        <div class="kt-header__topbar">
                            <!--begin: Language bar -->
                            <s:if test="#session.multilingualSupport">
                                <div class="kt-header__topbar-item kt-header__topbar-item--langs">
                                    <div class="kt-header__topbar-wrapper" data-toggle="dropdown" data-offset="10px,0px">
                                        <span class="kt-header__topbar-icon">
                                            <img class="" src="<s:property value='#session.defaultLanguage.substring(#session.defaultLanguage.indexOf(";")+1)'/>" alt="<s:property value='#session.defaultLanguage.substring(0, #session.defaultLanguage.indexOf(";"))'/>" />
                                        </span>
                                    </div>
                                    <div class="dropdown-menu dropdown-menu-fit dropdown-menu-right dropdown-menu-anim dropdown-menu-top-unround">
                                        <ul class="kt-nav kt-margin-t-10 kt-margin-b-10">
                                            <s:iterator value="#session.multilingualDD" var="theLanguage">
                                            <li class="kt-nav__item">
                                                <a href="changeLocale?language=<s:property value='#theLanguage.substring(0, #theLanguage.indexOf(";"))'/>" class="kt-nav__link">
                                                    <span class="kt-nav__link-icon"><img src="<s:property value='#theLanguage.substring(#theLanguage.indexOf(";")+1)'/>" alt="<s:property value='#theLanguage.substring(0, #theLanguage.indexOf(";")).toUpperCase()'/>" /></span>
                                                    <span class="kt-nav__link-text"><s:property value='#theLanguage.substring(0, #theLanguage.indexOf(";")).toUpperCase()'/></span>
                                                </a>
                                            </li>
                                            </s:iterator>
                                        </ul>
                                    </div> 
                                </div>
                            </s:if>
                            <!--end: Language bar -->
                            
                            <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV] && #session.sSysUserObjId != null">
                                <!--begin::sarawakid-->     
                                <div class="swkheader-system-right" style="align-self:center">
                                    <div class="swkheader-system-button-div"></div>
                                    <div class="swkheader-system-button" style="vertical-align: middle;line-height: 30px;">
                                        <s:property value="#session.userName"/>
                                        <div id="swkid_plugin"></div>
                                    </div>
                                </div>
                                <!--end::sarawakid--> 
                                &nbsp;
                                <!--begin::more button--> 
                                <div class="dropdown dropdown-inline" style="align-self:center">
                                    <button type="button" class="btn btn-hover-brand btn-elevate-hover btn-icon btn-sm btn-icon-md" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <i class="flaticon-more"></i>
                                    </button>
                                    <div class="dropdown-menu dropdown-menu-right" style="">
                                        <a class="dropdown-item" href="loadPreference"><i class="fa fa-user" style="align-self:center"></i> My Profile</a>
                                    </div>
                                </div>
                                <!--end::more button--> 
                            </s:if>
                            <s:else>
                                <!--begin: User Bar -->
                                <div class="kt-header__topbar-item kt-header__topbar-item--user">
                                    <div class="kt-header__topbar-wrapper" data-toggle="dropdown" data-offset="0px,0px">
                                        <div class="kt-header__topbar-user">
                                            <span class="kt-header__topbar-welcome kt-hidden-mobile">Hi,</span>
                                            <span class="kt-header__topbar-username kt-hidden-mobile"><s:property value="#session.userName"/></span>
                                            <img class="kt-hidden" alt="Pic" src="include/assets/media/users/300_25.jpg" />

                                            <!--use below badge element instead the user avatar to display username's first letter(remove kt-hidden class to display it) -->
                                            <span class="kt-badge kt-badge--username kt-badge--unified-success kt-badge--lg kt-badge--rounded kt-badge--bold"><s:property value="#session.userName.charAt(0)"/></span>
                                        </div>
                                    </div>
                                    <div class="dropdown-menu dropdown-menu-fit dropdown-menu-right dropdown-menu-anim dropdown-menu-top-unround dropdown-menu-xl">

                                        <!--begin: Head -->
                                        <div class="kt-user-card kt-user-card--skin-dark kt-notification-item-padding-x" style="background-image: url(include/assets/media/misc/bg-1.jpg)">
                                            <div class="kt-user-card__avatar">
                                                <img class="kt-hidden" alt="Pic" src="include/assets/media/users/300_25.jpg" />

                                                <!--use below badge element instead the user avatar to display username's first letter(remove kt-hidden class to display it) -->
                                                <span class="kt-badge kt-badge--lg kt-badge--rounded kt-badge--bold kt-font-success"><s:property value="#session.userName.charAt(0)"/></span>
                                            </div>
                                            <div class="kt-user-card__name">
                                                <s:property value="#session.userName"/>
                                            </div>
                                        </div>
                                        <!--end: Head -->

                                        <!--begin: Navigation -->
                                        <div class="kt-notification">
                                            <a href="loadPreference" class="kt-notification__item">
                                                <div class="kt-notification__item-icon">
                                                    <i class="flaticon2-calendar-3 kt-font-success"></i>
                                                </div>
                                                <div class="kt-notification__item-details">
                                                    <div class="kt-notification__item-title kt-font-bold">
                                                        My Profile
                                                    </div>
                                                    <div class="kt-notification__item-time">
                                                        Account settings and more
                                                    </div>
                                                </div>
                                            </a>
                                            <div class="kt-notification__custom kt-space-between">
                                                <s:if test="#session.sSysUserObjId == null">
                                                <a href="processlogoutLogout" class="btn btn-label btn-label-brand btn-sm btn-bold">Sign Out</a>
                                                <!--<a href="custom/user/login-v2.html" target="_blank" class="btn btn-clean btn-sm btn-bold">Upgrade Plan</a>-->
                                                </s:if>
                                            </div>
                                        </div>

                                        <!--end: Navigation -->
                                    </div>
                                </div>
                                <!--end: User Bar -->
                            </s:else>
                        </div> 
                        <!-- end:: Header Topbar -->
                    </div>
                    <!-- end:: Header -->
                    
                    <div class="kt-content  kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor" id="kt_content">
                        <!-- begin:: Subheader -->
                        <div class="kt-subheader   kt-grid__item" id="kt_subheader">
                            <div class="kt-container  kt-container--fluid ">
                                <div class="kt-subheader__main">
                                    <div class="kt-subheader__breadcrumbs">
                                        <page:applyDecorator page="/main/breadcrumb.jsp" name="panel1" />  
                                    </div>
                                </div>
                                <div class="kt-subheader__toolbar">
                                    <div class="kt-subheader__wrapper">
                                        <!--<a href="#" class="btn kt-subheader__btn-daterange" id="kt_dashboard_daterangepicker" data-toggle="kt-tooltip" title="Select dashboard daterange" data-placement="left">
                                            <span class="kt-subheader__btn-daterange-title" id="kt_dashboard_daterangepicker_title">Today</span>&nbsp;
                                            <span class="kt-subheader__btn-daterange-date" id="kt_dashboard_daterangepicker_date">Aug 16</span>

                                            <i class="flaticon2-calendar-1"></i>
                                            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px" viewBox="0 0 24 24" version="1.1" class="kt-svg-icon kt-svg-icon--sm">
                                                <g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                                                    <rect x="0" y="0" width="24" height="24" />
                                                    <path d="M4.875,20.75 C4.63541667,20.75 4.39583333,20.6541667 4.20416667,20.4625 L2.2875,18.5458333 C1.90416667,18.1625 1.90416667,17.5875 2.2875,17.2041667 C2.67083333,16.8208333 3.29375,16.8208333 3.62916667,17.2041667 L4.875,18.45 L8.0375,15.2875 C8.42083333,14.9041667 8.99583333,14.9041667 9.37916667,15.2875 C9.7625,15.6708333 9.7625,16.2458333 9.37916667,16.6291667 L5.54583333,20.4625 C5.35416667,20.6541667 5.11458333,20.75 4.875,20.75 Z" fill="#000000" fill-rule="nonzero" opacity="0.3" />
                                                    <path d="M2,11.8650466 L2,6 C2,4.34314575 3.34314575,3 5,3 L19,3 C20.6568542,3 22,4.34314575 22,6 L22,15 C22,15.0032706 21.9999948,15.0065399 21.9999843,15.009808 L22.0249378,15 L22.0249378,19.5857864 C22.0249378,20.1380712 21.5772226,20.5857864 21.0249378,20.5857864 C20.7597213,20.5857864 20.5053674,20.4804296 20.317831,20.2928932 L18.0249378,18 L12.9835977,18 C12.7263047,14.0909841 9.47412135,11 5.5,11 C4.23590829,11 3.04485894,11.3127315 2,11.8650466 Z M6,7 C5.44771525,7 5,7.44771525 5,8 C5,8.55228475 5.44771525,9 6,9 L15,9 C15.5522847,9 16,8.55228475 16,8 C16,7.44771525 15.5522847,7 15,7 L6,7 Z" fill="#000000" />
                                                </g>
                                            </svg> 
                                        </a>-->
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- end:: Subheader -->

                        <!-- begin:: Content -->
                        <div class="kt-container  kt-container--fluid  kt-grid__item kt-grid__item--fluid">
                            <decorator:body />
                        </div>
                        <!-- end:: Content -->
                    </div>

                    <!-- begin:: Footer -->
                    <div class="kt-footer  kt-grid__item kt-grid kt-grid--desktop kt-grid--ver-desktop" id="kt_footer" style="z-index:90;">
                        <div class="kt-container  kt-container--fluid ">
                            <div class="kt-footer__copyright">
                                <s:text name="systemInfo.copyRight"/> <s:text name="systemInfo.copyRightYear"/> &nbsp;|&nbsp; <s:text name="systemInfo.bestView"/>
                                <!--2019&nbsp;&copy;&nbsp;<a href="http://keenthemes.com/metronic" target="_blank" class="kt-link">Keenthemes</a>-->
                            </div>
                            <div class="kt-footer__menu">
                                <b><s:text name="systemInfo.systemName"/> &nbsp;|&nbsp; <s:text name="systemInfo.version"/></b>
                                <!--<a href="http://keenthemes.com/metronic" target="_blank" class="kt-footer__menu-link kt-link">About</a>
                                <a href="http://keenthemes.com/metronic" target="_blank" class="kt-footer__menu-link kt-link">Team</a>
                                <a href="http://keenthemes.com/metronic" target="_blank" class="kt-footer__menu-link kt-link">Contact</a>-->
                            </div>
                        </div>
                    </div>
                    <!-- end:: Footer -->
                </div>
            </div>
        </div>

        <!-- begin::Scrolltop -->
        <div id="kt_scrolltop" class="kt-scrolltop">
            <i class="fa fa-arrow-up"></i>
        </div>
        <!-- end::Scrolltop -->
        
        <div class="modal fade" id="lookupModal" tabindex="-1" role="dialog" aria-labelledby="lookupModalLabel"></div>
        <div id='encode' class='hidden'></div>

        <!--begin::Alert Modal-->
        <div id="alertDiv" class="modal fade" tabindex="-1" data-width="" data-height="" style="display: none;" data-keyboard="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <div class="title"></div>
                        <button type="button" class="close btnCloseAlert" data-dismiss="modal" aria-hidden="true"></button>
                    </div>
                    <div class="modal-body myModalContent"></div>
                </div>
            </div>
        </div>
        <!--end::Alert Modal-->
        <div id="loadingModal" class="modal fade" tabindex="-1" data-width="" data-height="" style="display: none; padding-right: 0 !important;" data-backdrop="static" data-keyboard="false">
            <div class="modal-contentmodal-lg" role="document">
                <div class="modal-body myModalContent">
                    <div class="spinner-border"></div><%--Loading...--%>
                </div>
            </div>  
        </div>
        <script>
            var KTAppOptions = {
                "colors": {
                    "state": {
                        "brand": "#5d78ff",
                        "dark": "#282a3c",
                        "light": "#ffffff",
                        "primary": "#5867dd",
                        "success": "#34bfa3",
                        "info": "#36a3f7",
                        "warning": "#ffb822",
                        "danger": "#fd3995"
                    },
                    "base": {
                        "label": [
                            "#c5cbe3",
                            "#a1a8c3",
                            "#3d4465",
                            "#3e4466"
                        ],
                        "shape": [
                            "#f0f3ff",
                            "#d9dffa",
                            "#afb4d4",
                            "#646c9a"
                        ]
                    }
                }
            };
        </script>

        <!--begin::Global Theme Bundle(used by all pages) -->
        <script src="include/assets/plugins/global/plugins.bundle.js" type="text/javascript"></script>
        <script src="include/assets/js/scripts.bundle.js" type="text/javascript"></script>
        <!--end::Global Theme Bundle -->
        
        <!--begin::datatable  -->
        <script src="include/assets/plugins/custom/datatables/datatables.bundle.js" type="text/javascript"></script>

        <!--end::datatable -->
        
        <script src="include/assets/js/pages/crud/forms/widgets/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="include/assets/js/pages/crud/forms/widgets/bootstrap-daterangepicker.js" type="text/javascript"></script>
        
        <script src="include/assets/js/pages/crud/forms/widgets/select2.js" type="text/javascript"></script>
        <script src="include/bootstrap/sidebar_menu.js"></script>

        <script type="text/javascript">
            var fromStart = true;
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