<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>

<%@taglib uri="/struts-tags" prefix="s"%>
<s:set var="ctx" value="%{pageContext.request.contextPath}"/>

<html lang="en" class="st-layout ls-top-navbar-large ls-bottom-footer show-sidebar sidebar-l4">
    <head>
        <meta charset="UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <s:set var="systemWelcome_"><s:text name="system.welcome"/></s:set>
        <title>SDS Framework Development</title>
        <!--Jquery-->
        <script src="include/jquery/jquery.js"></script>
        <!--<script src="include/jquery/jquery-3.4.1.min.js"></script>-->
        
        <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico?v=2" type="image/x-icon" />

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
        <link href="include/modal/bootstrap-modal-bs3patch.css" rel="stylesheet"/>
        <link href="include/modal/bootstrap-modal.css" rel="stylesheet"/>
        
        <!--Date and Daterange picker-->
        <link href="include/datepicker/datepicker3.css" rel="stylesheet"/>
        <link href="include/daterangepicker/daterangepicker.css" rel="stylesheet"/>
        
        <link href="include/bootstrap/customs.css" rel="stylesheet"/>
        <!--Date and daterange picker-->
        <script type="text/javascript" language="javascript" src="include/datepicker/bootstrap-datepicker3.js"></script>
        <script type="text/javascript" language="javascript" src="include/daterangepicker/moment.js"></script>
        <script type="text/javascript" language="javascript" src="include/daterangepicker/daterangepicker.js"></script>
        
        <!--Input Mask-->
        <script type="text/javascript" language="javascript" src="include/input-mask/jquery.inputmask.js"></script>
        <script type="text/javascript" language="javascript" src="include/input-mask/jquery.inputmask.extensions.js"></script>
        
        <script type="text/javascript" language="javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" language="javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" language="javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" language="javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript">
            function registerDateRangePicker(drPicker) {
                $('input[name="'+drPicker+'"]').daterangepicker({
                                          autoUpdateInput: true,
                                          autoApply: true
                });
                $('input[name="'+drPicker+'"]').on('apply.daterangepicker', function(ev, picker) {
                    $(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
                  $("#"+drPicker+"From").val(picker.startDate.format('DD/MM/YYYY'));
                  $("#"+drPicker+"To").val(picker.endDate.format('DD/MM/YYYY'));
                });

                $('#clear'+drPicker).on("click", function(ev) {
                    $('#'+drPicker).val('');
                    $('#'+drPicker+'From').val('');
                    $('#'+drPicker+'To').val('');
                });
                $('input[name="'+drPicker+'"]').on("change", function(ev) {
                    if ($(this).val().trim() === '') {
                        $("#"+drPicker+"From").val('');
                        $("#"+drPicker+"To").val('');
                        $(this).val('');
                    }
                });
            }
        </script>
        <decorator:head />
    </head>

    <body>
        <!-- Fixed navbar -->
        <div class="navbar navbar-size-large navbar-default" role="navigation">
            <!--System name (Desktop view)-->
            <div class="navbar-brand navbar-brand-primary navbar-brand-logo navbar-nav-padding-left eLogo">
                <a class="sysName" href="welcome">SDS <small>v1.0</small></a>
            </div>
            <nav class="desktop-view-menu">
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li class="active"><button class="navbar-toggle collapse in" data-toggle="collapse" id="menu-toggle-2"> <span class="glyphicon glyphicon-th-large" aria-hidden="true"></span></button></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li><h4 class="sys-fullname">SDS Framework Development System v1.0</h4></li>
                    </ul>
                </div>
            </nav>

            <!--System name (Tablet/mobile view)-->
            <div class="mobile-view-menu">
                <a class="sysName2" href="#">SDS <small> v1.0</small></a>
                <ul class="nav navbar-nav navbar-right" style="float:right;">
                    <li class="">
                        <button type="button" class="navbar-toggle2" data-toggle="collapse" id="menu-toggle">
                            <span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
                        </button>
                    </li>
                </ul>
            </div>
        </div><!-- /Fixed navbar -->
        <div id="wrapper" <s:if test='#session.msb.equals("H")'>class='toggled-2'</s:if>>
            <!-- Sidebar -->
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav nav-pills nav-stacked" id="menu">
                    <div class="profile" <s:if test='#session.msb.equals("H")'>style='display: none;'</s:if>>
                        <a href="#">
                            <img src="images/empty.jpg" alt="people" class="img-circle width-80"/>
                        </a>
                        <h5 class="welcomeText">Welcome,</h5>
                        <h4 class="text-display-1 margin-none">
                        <a href="loadPreference" style="color: #fff"><s:property value="#session.userName"/></a>
                        <br><button onclick="document.location='processlogoutLogout'" class="btn btn-default btn-logout"><i class="fa fa-sign-out"></i>&nbsp;&nbsp;Logout</button></h4>

                    </div>
                    <ul class="nav-list">
                        <s:property value="#session.menuList" escapeHtml="false"/>
                    </ul>
<!--                    <ul class="nav-list">
                        <li>
                        <a class="accordion-heading" data-toggle="collapse" data-target="#submenu1">
                            <span class="nav-header-primary">Menu Link <span class="pull-right"><b class="caret"></b></span></span>
                        </a>
                            <ul class="nav-list collapse" id="submenu1">
                                    <li>
                                        <a class="accordion-heading" data-toggle="collapse" data-target="#submenu2">Sub Menu Link <span class="pull-right"><b class="caret"></b></span></a>
                                        <ul class="nav-list collapse" id="submenu2">
                                                <li><a href="#" title="Title">Sub Sub Menu Link</a></li>
                                                <li><a href="#" title="Title">Sub Sub Menu Linksjdfi sdjifjsdo</a></li>
                                        </ul>
                                    </li>
                            </ul>
                        </li>
                    </ul>-->
<!--                    <li class="dropdown active">
                        <a href="javascript:;">
                            <span class="fa-stack fa-lg pull-left">
                                <i class="fa fa-square-o fa-stack-1x menu-icon"></i>
                            </span>
                            <div class="menu-label">Menu 1</div>
                        </a>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <span class="fa-stack fa-lg pull-left">
                                <i class="fa fa-square-o fa-stack-1x menu-icon"></i>
                            </span>
                            <div class="menu-label">Menu 2 <b class="caret"></b></div>
                        </a>
                        <ul class="dropdown-menu"> 
                            <li><a href="javascript:;">Sub Menu 1</a></li>
                            <li><a href="javascript:;">Sub Menu 2</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <span class="fa-stack fa-lg pull-left">
                                <i class="fa fa-square-o fa-stack-1x menu-icon"></i>
                            </span>
                            <div class="menu-label">Menu 3 <b class="caret"></b></div>
                        </a>
                        <ul class="dropdown-menu"> 
                            <li><a href="javascript:;">Sub Menu 1</a></li>
                            <li><a href="javascript:;">Sub Menu 2</a></li>
                            <li><a href="javascript:;">Sub Menu 3</a></li>
                            <li><a href="javascript:;">Sub Menu 4</a></li>
                        </ul>
                    </li>-->

                </ul>
            </div><!-- /#sidebar-wrapper -->



            <!-- Page Content -->
            <div id="page-content-wrapper">
                <div class="container-fluid bottomMargin">
                    <!--Breadcrumb-->
                    <page:applyDecorator page="/main/breadcrumb.jsp" name="panel1" />

                    <!--Content-->
                    <decorator:body />

                </div><!--/container-fluid-->
            </div><!-- /#page-content-wrapper -->

            <!-- FOOTER  -->
            <footer class="footer">
                <div class="desktop_footer">
                    <b>SDS Framework Development System | version 1.0</b> 
                    <br><font style="font-size:11px;">Copyright &copy; SAINS 2019 &nbsp;|&nbsp; Best viewed in 1024 x 768 resolutions (or higher) using Mozilla Firefox 3.0+</font>
                </div>
                <div class="mobile_footer">
                    <b>SDS version 1.0</b>  | <font style="font-size:11px;">Copyright &copy; SAINS 2019</font>
                </div>
            </footer>
            <!-- /. FOOTER  -->
        </div><!-- /#wrapper -->
        <%--        <div class="modal fade" id="lookupModal" tabindex="-1" role="dialog" aria-labelledby="lookupModalLabel"></div> --%>
<div class="modal fade" id="lookupModal" tabindex="-1" role="dialog" aria-labelledby="lookupModalLabel">
</div><div id='encode' class='hidden'></div>
<!--Alert Modal-->
<div id="alertDiv" class="modal fade" tabindex="-1" data-width="" data-height="" style="display: none;" data-keyboard="true">
    <div class="">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close btnCloseAlert" data-dismiss="modal" aria-hidden="true">×</button>
                <div class="title"></div>
            </div>
            <div class="modal-body myModalContent"></div>
<%--            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-default btnCloseAlert">Close</button>
            </div>--%>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!--end responsive--> 
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
            var scrolled = false;
            $(document).ready(function () {
                alert(!scrolled);
                if (!scrolled) {
                    window.scrollBy(0, 50);
                }
                scrolled = true;
                $('.sds-dropdown').select2();

                $(window).resize(function () {
                    $('.select2').css('width', "100%");
                });
                $('form').find('input[type=text],textarea,select,radio,checkbox').filter(':visible:first').focus();
                <%--<s:if test='#session.menuSideBar.equals("H")'>
                    $('#wrapper').addClass('toggled-2');
                </s:if>--%>
            });
            function sideMenuPostEvent() {
                $('.select2').css('width', "100%");
                $('.sds-dropdown').select2();
            }
        </script>
    </body>
</html>

