<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>

<%@taglib uri="/struts-tags" prefix="s"%>
<s:set var="ctx" value="%{pageContext.request.contextPath}"/>

<html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <s:set var="systemWelcome_"><s:text name="system.welcome"/></s:set>
        <title><s:text name="system.shortname"/>:
            <decorator:title default='${systemWelcome_} !'/>
        </title>
        <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico?v=2" type="image/x-icon" />
        <!-- Google Fonts -->
        <link href="plugins/google-fonts/css.css" rel="stylesheet" type="text/css">
        <link href="plugins/google-fonts/icon.css" rel="stylesheet" type="text/css">

        <!-- Bootstrap Core Css -->
        <link href="include/theme/bootstrap-theme.css" rel="stylesheet" type="text/css"/>
        <link href="plugins/bootstrap/css/bootstrap.css" rel="stylesheet">

        <!-- Waves Effect Css -->
        <link href="plugins/node-waves/waves.css" rel="stylesheet" />

        <!-- Animation Css -->
        <link href="plugins/animate-css/animate.css" rel="stylesheet" />

        <!-- Sweet Alert Css -->
        <link href="plugins/sweetalert/sweetalert.css" rel="stylesheet" />

        <!-- Custom Css -->
        <link href="include/theme/customs.css" rel="stylesheet" type="text/css"/><!--Place your customs css here-->

        <!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
        <!--<link href="../themes/all-themes.css" rel="stylesheet" />-->
        <!-- Custom Js -->
        <link href="plugins/css/style.css" rel="stylesheet" type="text/css"/>
        <link href="plugins/bootstrap/css/bootstrap-theme.css" rel="stylesheet" type="text/css"/>
        <link href="plugins/bootstrap-select/css/bootstrap-select.css" rel="stylesheet" />
        <!--<link href="include/theme/forms.css" rel="stylesheet" type="text/css"/>-->
        <link href="include/theme/breadcrumb.css" rel="stylesheet" type="text/css"/>
        <link href="include/theme/customs.css" rel="stylesheet" type="text/css"/><!--Place your customs css here-->
        <link href="include/theme/drawing.css" rel="stylesheet" type="text/css"/>
        <!-- Jquery Core Js -->
        <script src="plugins/jquery/jquery.min.js"></script>

        <!-- Bootstrap Core Js -->
        <script src="plugins/bootstrap/js/bootstrap.js"></script>

        <!-- Select Plugin Js -->
        <script src="plugins/bootstrap-select/js/bootstrap-select.js"></script>

        <!-- Slimscroll Plugin Js -->
        <script src="plugins/jquery-slimscroll/jquery.slimscroll.js"></script>

        <!-- Waves Effect Plugin Js -->
        <script src="plugins/node-waves/waves.js"></script>
        
        <script src="plugins/js/admin.js"></script>
        <script src="plugins/js/pages/forms/form-validation.js"></script>
        <script src="plugins/jquery-validation/jquery.validate.js"></script>

        <script src="pages/scripts/common.js"></script>
        <script src="pages/scripts/validation.jsp"></script>
        <script src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" src="pages/scripts/focusColumn.js"></script>
        
        <!--Currency Formatting-->
<!--        <script src="include/jquery-number-master/jquery.number.js"></script>
        <script src="include/jquery-number-master/jquery.number.min.js"></script>-->
        <decorator:head />
    </head>
    <body class="body-nav-fixed-menu-top">
        <div class="wrapper-body">
            <!-- NAVBAR -->
            <!--===============================================================-->
            <div id="header">
                <nav id="nav" class="navbar navbar-default navbar-fixed-top">
                    <div class="menu-top menu-top-inverse">
                        <div class="container">
                            <div class="row visible-xs">
                                <div class="col-xs-12" style="color:white;font-weight:bold;margin-top:5px;line-height:15px;text-align:center;">
                                    Welcome, <s:property value="#session.userName" escapeHtml="false"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="hidden-sm hidden-md col-lg-3">
                                    <a class="title-menu-top display-inline-block hidden-xs"><s:property value="#session.todaysDateTime" /></a>
                                </div>

                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-9 hidden-xs">
                                    <div class="pull-right">
                                        <div class="dropdown dropdown-login pull-left">
                                            <button class="btn-menu-top hidden-xs" id="dLabel" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" style="border-left:none;">
                                                Welcome, <s:property value="#session.userName" escapeHtml="false"/>
                                            </button>
                                            <div class="list-group dropdown-menu dropdown-menu-right stop-prop">
                                                <!--internal not need profile-->
                                                <!--<a href="loadEditPageProfile" class="list-group-item">Profile<span class="pull-right"><i class="fa fa-user" style="margin-top:5px;"></i></span></a>-->
                                                <a href="processlogoutLogout" class="list-group-item">Logout <span class="pull-right"><i class="fa fa-power-off" style="margin-top:5px;"></i></span></a>
                                            </div>
                                        </div>
                                        <div class="list-inline social-icons-menu-top pull-left">
                                            <!--<a class="social-hover-v1 a-home" href="internal" title="Home"></a>-->
                                            <!--<a class="social-hover-v1 a-help hidden-xs" href="#" title="Help"></a>-->
                                            <a class="social-hover-v1 a-help hidden-xs" href="loadListingPageGuide" title="Guide"></a>
                                            <a class="social-hover-v1 a-user visible-xs" href="loadEditPageProfile" title="<s:property value="#session.userName" escapeHtml="false"/>"></a>
                                            <a class="social-hover-v1 a-logout visible-xs" href="processlogoutLogout" title="Logout"></a>
                                        </div>
                                    </div>
                                </div>


                                <div class="col-xs-12 visible-xs">
                                    <div class="pull-right">
                                        <div class="list-inline social-icons-menu-top pull-left">
                                            <!--<a class="social-hover-v1 a-home" href="internal" title="Home"></a>-->
                                            <!--<a class="social-hover-v1 a-help hidden-xs" href="#" title="Help"></a>-->
                                            <a class="social-hover-v1 a-help hidden-xs" href="laodListingPageGuide" title="Guide"></a>
                                            <a class="social-hover-v1 a-user visible-xs" href="loadEditPageProfile" title="Profile"></a>
                                            <a class="social-hover-v1 a-logout visible-xs" href="processlogoutLogout" title="Logout"></a>
                                        </div>
                                        <s:if test="#session.logined != null">
                                            <s:if test='#session.loginSystemType_ == "ESPA"'>
                                                <div class="dropdown dropdown-cart pull-left">
                                                    <button class="btn-menu-top" id="myMessage" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fa fa-envelope"></i> (<s:property value="#session.inbox" />)</button>
                                                    <div class="dropdown-menu dropdown-menu-right dropdown-menu-cart stop-prop" role="menu" aria-labelledby="myMessage">
                                                        <div class="panel-shopping-cart">
                                                            <s:if test="#session.inboxList.size() > 0">
                                                                <table class="table">
                                                                    <thead>
                                                                        <tr>
                                                                            <th class="text-center" width="5%">#</th>
                                                                            <th width="25%">Case Reference</th>
                                                                            <th width="47%">Subject</th>
                                                                            <th width="23%">Date</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        <s:iterator value="#session.inboxList" status="inboxStatus" var="inboxModel">
                                                                            <s:hidden name="inboxList[%{#inboxStatus.index}].message_id" value="%{#inboxModel.message_id}" />
                                                                            <tr>
                                                                                <td><span class="title text-center">${inboxStatus.index+1}</span></td>
                                                                                <td><span class="title"><a href="loadEditPageSysMessage?id=${inboxModel.message_id}&pageFrom_=inbox"><span class="caseNo" data-appid="${inboxModel.app_id}"></span><s:property value="%{#inboxModel.strRefNo"/></a></span></td>
                                                                                <td><span class="title"><a href="loadEditPageSysMessage?id=${inboxModel.message_id}&pageFrom_=inbox"><s:property value="%{#inboxModel.message_subject}"/></a></span></td>
                                                                                <td><span class="title">${inboxModel.created_datetime}</span></td>
                                                                            </tr>
                                                                        </s:iterator>
                                                                    </tbody>
                                                                </table>
                                                            </s:if>
                                                            <s:else>
                                                                <br/><font class="text-green"><center>There is no new message.</center></font>
                                                                    </s:else>
                                                            <div class="panel-footer">
                                                                <div class="row">
                                                                    <div class="col-sm-12 text-right">
                                                                        <a href="loadListPageSysMessage?moduleCode=Message" class="btn btn-primary btn-sm">View My Inbox</a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div><!--inbox-->
                                            </s:if>
                                        </s:if>
                                    </div>
                                </div>


                            </div>
                        </div>
                    </div>

                    <div class="container">
                        <div class="navbar-header">
                            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                                <span class="sr-only">Toggle navigation</span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                            </button>
                            <a class="navbar-brand" href='initLogin'>
                                <span class="system-name"><font class="text-green"><s:text name="system.shortname"/></font></span>
                                <span><s:text name="systemInfo.version"/></span>
                            </a>
                        </div>
                        <div id="navbar" class="navbar-collapse collapse">
                            <ul class="nav navbar-nav navbar-right">
                                <li class="dropdown"><a href='initLogin'>Home</a></li>
                                <!--<li class="dropdown"><a href="#" data-step='3' data-intro='View dashboard here...'>Dashboard</a></li>-->
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"  data-step='4' data-intro='See the sample forms provided and file uploading page here..'>Features</a>
                                    <ul class="dropdown-menu dropdown-menu-left" role="menu">
                                        <s:if test="#session.logined != null">
                                            <s:if test='#session.loginSystemType_ == "ESPA"'>
                                                <s:property value="#session.menuList_ESS" escapeHtml="false"/>
                                            </s:if>
                                            <s:else>
                                                <s:property value="#session.menuListEspa" escapeHtml="false"/>
                                            </s:else>
                                        </s:if>
                                    </ul>
                                </li>
                            </ul>
                        </div><!--/.nav-collapse -->
                    </div>
                </nav>
            </div>
            <!-- NAVBAR END -->
            <!--PAGE TITLE and BREADCRUMB-->
            <div class="section-heading-page">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-6 dropdown">
                            <s:if test="pageTitleNum_ != null">
                                <center><span class="page-title-numbering-xs visible-xs">${pageTitleNum_}</span></center>
                                </s:if>
                            <h4 class="heading-page  text-center-xs"><s:if test="pageTitleNum_ != null"><span class="page-title-numbering hidden-xs">${pageTitleNum_}</span>&nbsp;</s:if>${pageTitle_} <small>${pageSubTitle_}</small></h4>
                            </div>
                            <div class="col-sm-6"><!--page breadcrumb-->
                            <page:applyDecorator page="/main/breadcrumb.jsp" name="panel1" />
                        </div>
                    </div>
                </div>
            </div>


            <!-- CONTENT -->
            <!--===============================================================-->
            <div class="section section-xs section-both minHeight">
                <div class="container">
                    <br/><decorator:body /><br/>
                </div>
            </div>


            <!--SECTION FOOTER BOTTOM -->
            <!--===============================================================-->
            <div class="section footer-bottom">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-12 text-center col-footer-bottom">
                            <!--<a id="scroll-top" href="#"><i class="fa fa-angle-up fa-2x"></i></a>-->
                            <p class="copyright"><s:text name="system.shortname"/> <s:text name="systemInfo.version"/> | <s:text name="systemInfo.copyRight"/></p>
                        </div>
                    </div>
                </div>
            </div>
            <a href="#" id="scroll" style="display: none;"><span></span></a>      
        </div>

        <!--Indeterminate progress bar-->
        <script src="include/login/pace.js" type="text/javascript"></script>    
        <!--Theme-->
        <script src="include/theme/page.navbar-fixed-shrinked.js"></script><!--for shrinking the header when scroll down-->
        <script src="include/theme/bootstrap3-wysihtml5.all.min.js"></script>
        <script src="include/theme/autoNumeric.js" type="text/javascript"></script>
        <script src="include/theme/dropzone.min.js" type="text/javascript"></script>
        <!--<script src="include/theme/bootstrap-tagsinput.min.js" type="text/javascript"></script>-->
<!--        <script src="include/theme/jquery.inputmask.min.js" type="text/javascript" ></script>
        <script src="include/theme/jquery.validate.min.js" type="text/javascript"></script>-->
        <!--<script src="include/theme/summernote.min.js" type="text/javascript"></script>-->
        <!--<script src="include/theme/bootstrap-timepicker.min.js"></script>-->
        <!--<script src="include/theme/typeahead.bundle.min.js"></script>-->
        <!--<script src="include/theme/typeahead.jquery.min.js"></script>-->
        <!--<script src="include/theme/handlebars-v4.0.5.js"></script>-->
        <!--<script src="include/theme/pages.js"></script>-->
<!--        <script src="include/theme/form_elements.js" type="text/javascript"></script>-->
        <!--<script src="include/theme/scripts.js" type="text/javascript"></script>-->
        <!--Switch-->
        <!--<script src="include/switch/bootstrap-switch.js"></script>-->
        <!--Page Guide-->
        <!--<script src="include/page_guide/intro.js"></script>-->	
        <!--Scrollbar-->
        <script src="include/scrollbar/jquery.scrollbar.min.js"></script>
        <!--Dashboard-->
        <script src="include/dashboard/modernizr-2.6.2-respond-1.1.0.min.js"></script>
        <script src="include/dashboard/jquery.isotope.js"></script>
        <script src="include/dashboard/main.js"></script>
        <!--Modal-->
        <script src="include/modal/bootstrap-modalmanager.js"></script>
        <script src="include/modal/bootstrap-modal.js"></script>
        <script src="include/modal/bootstrap-waitingfor.js"></script>
        <!--Bootstrap Date Range Picker-->
<!--        <script src="include/daterangepicker/moment.js"></script>
        <script src="include/daterangepicker/daterangepicker.js"></script>-->
        <!--File Upload-->
        <!--<script src="include/upload/jquery.uploadfile.min.js"></script>-->
        <!--Side Menu Switch-->
        <script src="include/left_sidebar/jquery.slimscroll.js"></script>
        <script type="text/javascript" src="include/left_sidebar/sidebar.js"></script>
        <script type="text/javascript" language="javascript">
            $(document).ready(function () {
                $(window).scroll(function () {
                    if ($(this).scrollTop() > 100) {
                        $('#scroll').fadeIn();
                    } else {
                        $('#scroll').fadeOut();
                    }
                });
                $('#scroll').click(function () {
                    $("html, body").animate({scrollTop: 0}, 600);
                    return false;
                });

                $(".allownumericwithoutdecimal").on("keypress keyup blur", function (event) {
                    $(this).val($(this).val().replace(/[^\d].+/, ""));
                    if (event.which != 8 && event.which != 0 && (event.which < 48 || event.which > 57)) {
                        event.preventDefault();
                    }
                });

                $(".allownumericwithdecimal").on("keypress keyup blur", function (event) {
                    //this.value = this.value.replace(/[^0-9\.]/g,'');
                    $(this).val($(this).val().replace(/[^0-9\.]/g, ''));
                    if ((event.which != 46 || $(this).val().indexOf('.') != -1) && (event.which < 48 || event.which > 57)) {
                        event.preventDefault();
                    }
                });
            });
        </script>
        <!--Alert Modal-->
        <div id="alertDiv" class="modal fade" tabindex="-1" data-width="" data-height="" style="display: none;"  data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close btnCloseAlert" data-dismiss="modal" aria-hidden="true">×</button>
                        <div class="title"></div>
                    </div>
                    <div class="modal-body myModalContent"></div>
                    <div class="modal-footer">
                        <button type="button" data-dismiss="modal" class="btn btn-default btnCloseAlert">Close</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!--end responsive--> 

        <!--Success Modal-->
        <div id="successDiv" class="modal fade" tabindex="-1" data-width="" data-height="" style="display: none;"  data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close btnClose" data-dismiss="modal" aria-hidden="true">×</button>
                        <div class="title"></div>
                    </div>
                    <div class="modal-body myModalContent"></div>
                    <div class="modal-footer">
                        <button type="button" data-dismiss="modal" class="btn btn-default btnClose">Close</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!--end responsive--> 

        <!--Confirmation Modal-->
        <div id="confirmDiv" class="modal fade" tabindex="-1" data-width="" data-height="" style="display: none;"  data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h3 class="title-v2">Confirmation</h3>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-12 myModalContent"></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary btnYes">Yes</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">No</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>

        <!--Confirmation Modal (use confirmDiv2 if same page is using confirmDiv)-->
        <div id="confirmDiv2" class="modal fade" tabindex="-1" data-width="" data-height="" style="display: none;"  data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h3 class="title-v2">Confirmation</h3>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-12 myModalContent"></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary btnYes2">Yes</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">No</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>

        <!--Wide Modal-->
        <div id="wideModal" class="modal fade" tabindex="-1" style="display: none;" data-keyboard="false" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header"></div>
                    <div class="modal-body"><button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

