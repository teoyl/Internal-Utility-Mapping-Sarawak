<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <jsp:include page="/include/crypto/crypto.jsp"></jsp:include> <%--ThoTH @ 3-Apr-2014--%>
        <title><s:text name="system.shortname"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, shrink-to-fit=no"/>
        <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico?v=2" type="image/x-icon" />

        <link href="include/login/pace-theme-flash.css" rel="stylesheet" type="text/css"/>
        <link href="include/bootstrap/bootstrap.css" rel="stylesheet"/>
        <link href="include/bootstrap/font-awesome.css" rel="stylesheet"/>
        <link href="include/switch/bootstrap-switch.css" rel="stylesheet"/>
        <link href="include/scrollbar/jquery.scrollbar.css" rel="stylesheet" />
        <link href="include/theme/select2.css" rel="stylesheet" type="text/css" media="screen"/>
        <link href="include/login/switchery.css" rel="stylesheet" type="text/css" media="screen"/>
        <link href="include/login/pages-icons.css" rel="stylesheet" type="text/css"/>
        <link href="include/login/modern.css" class="main-stylesheet" rel="stylesheet" type="text/css"/>
        <link href="include/login/login.css" rel="stylesheet"/>
        <script> 
        var $buoop = {required:{e:0,f:0,o:0,s:0,c:0},insecure:true,api:2018.08 }; 
        function $buo_f(){ 
         var e = document.createElement("script"); 
         e.src = "//browser-update.org/update.min.js"; 
         document.body.appendChild(e);
        };
        try {document.addEventListener("DOMContentLoaded", $buo_f,false)}
        catch(e){window.attachEvent("onload", $buo_f)}
        </script>
        <script type="text/javascript">
            window.onload = function()
            {
                // fix for windows 8
                if (navigator.appVersion.indexOf("Windows NT 6.2") != -1)
                    document.head.innerHTML += '<link rel="stylesheet" type="text/css" href="pages/css/windows.chrome.fix.css" />'
            }
        </script>
    </head>
    <body class="fixed-header">
        <div class="login-wrapper ">

            <!--left box content-->
            <div class="bg-pic">
                <img src="images/wallpaper.jpg" data-src="images/wallpaper.jpg" data-src-retina="images/wallpaper.jpg" alt class="lazy">
                <div class="bg-caption pull-bottom sm-pull-bottom text-white p-l-20 m-b-20">
                    <h2 class="semi-bold text-white"><s:text name="system.name"/></h2>
                        <p class="small"><s:text name="system.shortname"/> <s:text name="systemInfo.version"/> | <s:text name="systemInfo.copyRight"/></p>
                </div>
            </div>

            <!--right box content-->
            <div class="login-container bg-white">
                <div class="m-l-30 m-r-30 p-t-35 m-t-30 sm-p-l-15 sm-p-r-15 sm-p-t-40">    
                    <img src="images/Sarawak.svg.png" width="80" height="80"/><br/><br/>
                    <span class="system-name"><font class="text-green">e</font><font>SPA</font></span>
                    <span><s:text name="systemInfo.version"/></span>
                    <h2 class="semi-bold visible-xs"><s:text name="system.name"/></h2>
                    <p class="p-t-35"><s:text name="login.signIn"/></p>
                    <%--form id="form-login" class="p-t-15" role="form" action="home.html"--%>
                    <form id="loginForm" class="p-t-15" role="form" action="processloginLogin" method="post" onSubmit="return false;"> 
                        <s:if test="actionErrors.size() > 0 || actionMessages.size() > 0 || fieldErrors.size() > 0">
                            <div class="form-group">
                                <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
                             </div>
                        </s:if>
                        <div class="form-group form-group-default">
                            <label>User ID</label>
                            <div class="controls">
                                <input type="text" id="userId" name="userId" placeholder="User ID" class="form-control email">
                            </div>
                        </div>
                        <div class="form-group form-group-default">
                            <label>Password</label>
                            <div class="controls">
                                <input type="password" class="form-control" name="passwd" name="passwd" placeholder="Credentials" required>
                            </div>
                        </div><br>
                        <div class="row">
                            <div class="col-md-12">
                                <button class="btn btn-primary btn-cons m-t-10" name="loginBtn" id="loginBtn" src='%{#loginBtn}' type="submit">Sign in</button>
                                <button class="btn btn-default btn-cons m-t-10" name="loginBtn" id="loginBtn" type="reset"><s:text name="button.reset"/></button>
                                <%--<s:submit name="loginBtn" id="loginBtn"  class="btn btn-primary btn-cons m-t-10" type="submit" theme="simple" value="Sign In" src='%{#loginBtn}' action="" cssClass=""/>--%>
                            </div>
                        </div>
                            
                        <div class="row" style="margin-top:50px;">
                            <div class="m-b-20 p-r-80 sm-m-t-20 sm-p-r-15 sm-p-b-20 clearfix hidden-xs">
                                <a href="aboutRegistration">About <s:text name="system.shortname"/></a> | <a href="tncRegistration">Terms and Conditions</a>
                                <span class="hidden-xs" style="font-size:12px;"><br><i><s:text name="system.best.view"/></i></span>
                            </div>
                        </div>    
                    </form>
                            
                    <%--br/>Click <a href="templateLogin">here</a> for latest sample template.--%>         
                    <p class="small text-center visible-xs"><s:text name="system.shortname"/> <s:text name="systemInfo.version"/> | <s:text name="systemInfo.copyRight"/></p>
                </div>
            </div>
        </div>



        <script src="include/login/pace.js" type="text/javascript"></script><!--loading bar-->
        <script src="include/jquery/jquery.js"></script>
        <script src="include/login/modernizr.js" type="text/javascript"></script>
        <script src="include/login/jquery-ui.js" type="text/javascript"></script>
        <script src="include/login/tether.js" type="text/javascript"></script>
        <script src="include/bootstrap/bootstrap.js"></script>
        <script src="include/login/jquery-easy.js" type="text/javascript"></script>
        <script src="include/login/jquery.unveil.min.js" type="text/javascript"></script>
        <script src="include/login/jquery.ioslist.min.js" type="text/javascript"></script>
        <script src="include/login/jquery.actual.min.js"></script>
        <script src="include/scrollbar/jquery.scrollbar.min.js"></script>
        <script src="include/theme/select2.full.min.js" type="text/javascript"></script>
        <script  src="include/login/classie.js" type="text/javascript"></script>
        <script src="include/login/switchery.js" type="text/javascript"></script>
        <script src="include/theme/jquery.validate.min.js" type="text/javascript"></script><!--form validation-->
        <script src="include/theme/pages.js"></script><!--grey when click form-->
        <script src="include/switch/bootstrap-switch.js"></script>	   
    </body>
    <script type="text/javascript" language="javascript">document.forms[0].userId.focus()</script>
</html>