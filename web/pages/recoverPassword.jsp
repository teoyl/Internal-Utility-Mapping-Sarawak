<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, shrink-to-fit=no"/>
<html>
    <head>
        <jsp:include page="/include/crypto/crypto.jsp"></jsp:include> <%--ThoTH @ 3-Apr-2014--%>
            <title>Password Recovery Page</title>
        <%--<s:head />--%>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" src="pages/scripts/common.js"></script>

        <link href="include/login/pace-theme-flash.css" rel="stylesheet" type="text/css"/>
        <link href="include/bootstrap/bootstrap.css" rel="stylesheet">
        <link href="include/bootstrap/font-awesome.css" rel="stylesheet">
        <link href="include/switch/bootstrap-switch.css" rel="stylesheet">
        <link href="include/scrollbar/jquery.scrollbar.css" rel="stylesheet" >
        <link href="include/theme/select2.css" rel="stylesheet" type="text/css" media="screen"/>
        <link href="include/login/switchery.css" rel="stylesheet" type="text/css" media="screen"/>
        <link href="include/login/pages-icons.css" rel="stylesheet" type="text/css">
        <link href="include/login/modern.css" class="main-stylesheet" rel="stylesheet" type="text/css"/>
        <link href="include/login/login.css" rel="stylesheet">

        <script type="text/javascript">
            window.onload = function()
            {
                // fix for windows 8
                if (navigator.appVersion.indexOf("Windows NT 6.2") != -1)
                    document.head.innerHTML += '<link rel="stylesheet" type="text/css" href="include/login/windows.chrome.fix.css" />'
            }
            
            function checkNewIC() {
                var strNewIC = document.getElementById("us_id_number").value;
                if (IsValidNewIC(strNewIC, "ID Number (NRIC No.)")) {
                    //document.getElementById("us_id_number").value = FormatNewIC(strNewIC);
                }
                else {
                    document.getElementById("us_id_number").value = "";
                    return false;
                }
            }

            function localValidateForm(form) {
                var strID = document.getElementById("us_id_number").value;
                var strEmail = document.getElementById("us_email").value;
                if (document.getElementById("us_id_number").value === "" && document.getElementById("us_email").value === "") {
                    alert("Fill in the detail");
                    return false;
                } else {
                    if (document.getElementById("us_id_number").value !== "") {
                        if (strID.indexOf("-") > 2) {
                            document.getElementById("us_id_number").value = removeDashIC(strID);
                        }
//                        if (isNaN(strID)) {                        
//                            alert("Invalid ID number");
//                            return false;
//                        }else{
//                            checkNewIC();
//                        }
                        return true;
                    } else if (document.getElementById("us_email").value !== "") {
                        return true;
                    }
                }

            }
            function chkHyphen(fld, pos,e) {
                var keynum;
                var keychar;
                var numcheck;

                if(window.e) // IE
                {
                    keynum = e.keyCode;
                }
                else if(e.which) // Netscape/Firefox/Opera
                {
                    keynum = e.which;
                }
                
                for (var i = 0; i < pos.length; i++) {
                    if (pos[i] === fld.value.length)
                        if(keynum!==8){
                        fld.value += '-';
                        }
                }
            }

            //trim non-numeric values, allow numbers and dashes only
            function FilterChar3(field,e) {
                var keynum;
                var keychar;
                var numcheck;

                if(window.e) // IE
                {
                    keynum = e.keyCode;
                }
                else if(e.which) // Netscape/Firefox/Opera
                {
                    keynum = e.which;
                }
//                alert("keynum " + keynum);
                if (!(keynum >=48 && keynum <=57) && !(keynum==8)&& !(keynum==45) && !(keynum==undefined) ){
                    return false;
                }else{
                    
                    var vResult = new String();
                    var vNum = "0123456789-";
                    var vChar = field.value.split(""); // create array
                    for (i = 0; i < vChar.length; i++) {
                        if (vNum.indexOf(vChar[i]) != -1)
                            vResult += vChar[i];
                    }
                    if (field.value != vResult)
                        field.value = vResult;
                    
                    return true;
                }
            }
        </script>
    </head>


    <body class="fixed-header">
        <div class="login-wrapper">

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
                <div class="p-l-50 m-l-20 p-r-50 m-r-20 p-t-35 m-t-10 sm-p-l-15 sm-p-r-15 sm-p-t-40">
                    <br><img src="images/Sarawak.svg.png" width="80" height="80"/><br/><br/>
                    <span class="system-name"><font class="text-green">e</font><font>SPA</font></span>
                    <span><s:text name="systemInfo.version"/></span>
                    <h2 class="semi-bold visible-xs"><s:text name="system.name"/></h2>
                    <br><br><h4>Password Recovery</h4>
                    <p class="text-green">To reset your password, type the full email address or identity card number you use for your online transaction with us.</p>
                    <form action="recoverPasswordRegistration" class="p-t-15" role="form" method="post" class="form-horizontal form-inline">
                        <s:if test="actionErrors.size() > 0 || actionMessages.size() > 0 || fieldErrors.size() > 0">
                            <div class="form-group">
                                <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
                                </div>
                        </s:if>
                        <div class="form-group form-group-default">
                            <label><s:text name="user.emailAddress"/></label>
                            <div class="controls">
                                <input type="text" id="userId" name="us_email" placeholder="Email Address" class="form-control" value="${model.us_email}">
                            </div>
                        </div>
                        <div class="form-group text-center">
                            OR
                        </div>
                        <div class="form-group form-group-default">
                            <label><s:text name="user.ic.no"/></label>
                            <div class="controls">
                                <input type="text" name="userIcNo" id="us_id_number" placeholder="000000-00-0000" class="form-control" value="${model.us_id_number}"  onchange="checkNewIC();" maxlength="14" onkeyup= "chkHyphen(this, [6, 9],event);" onkeypress="return FilterChar3(this,event);">
                                <%--<s:textfield cssClass="field input-md form-control" theme="simple" name="us_id_number" size="50" value="%{model.us_id_number}" onchange="checkNewIC();"/>--%>
                            </div>
                        </div>
                                <i><small>Note: If Malaysian, to provide NRIC. eg.800130-13-1111<small></i><br><br><br>
                        <div class="row">
                            <div class="col-md-12">
                                <button class="btn btn-primary btn-cons m-t-10" name="action:recoverPasswordRegistration" id="recoverPasswordRegistration" type="submit" onclick="return localValidateForm(this.form);"><s:text name="button.submit"/></button>
                                <button class="btn btn-default btn-cons m-t-10" name="action:initLogin" id="initLogin" type="submit"><s:text name="button.cancel"/></button>
                            </div>
                        </div>
                    </form>

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
</html>
