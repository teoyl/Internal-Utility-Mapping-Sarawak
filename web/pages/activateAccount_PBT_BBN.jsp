<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@taglib uri="/struts-tags" prefix="s"%>
        <title>Perlu Bantuan</title>
        <script type="text/javascript" src="include/jquery_11.js"></script>
        <script language="javascript">
//            $(document).ready(function() {
//                $(".content_info").hide();
//
//                $(".common_showHideBtn").click(function() {
//                    var selectedIndex = $('.common_showHideBtn').index(this);
//
//                    $(".content_info").eq(selectedIndex).slideToggle(function() {
//                        $(".common_showHideBtn").eq(selectedIndex).text($(".common_showHideBtn").eq(selectedIndex).text() == '[Tutup]' ? '[Papar]' : '[Tutup]');
//                    });
//                });
//            });
        </script>
    </head>
    <body >
        <div class="main-box-bg">
        <table >
            <tr>
                <td> 
                    <table cellpadding="3" cellspacing="0" width="100%" >
                        <tr class="supportTitle">
                            <td width="600px">Permohonan Pengaktifan Akaun </td>
                            <td id='papar' >
                                <!--<span class="common_showHideBtn">[Papar]</span>-->
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="content_info xbox">
                <td>
                    <div  class="login-text">
                    <jsp:include page="/pages/base/actionError.jsp"></jsp:include>    
                    <form id="activateForm" action="processActivateLoginESS">    
                    <table>
                        <tr><td >Sila aktifkan akaun anda untuk log masuk ke sistem SCS-HRA</td></tr>
                        <tr><td height="54"><input type="text" id="userId" name="userId" placeholder="No. Kad Pengenalan/ No. Pasport (bukan warganegara): " value='<s:property value="%{userId}"/>' ></td></tr>
                    <tr><td ><img border="0" class="captcha" src="<s:text name="domain.textualFull"/>/generateCaptchaLoginESS"></td></tr>
                        <tr><td >Sila taip huruf yang ditunjuk di atas</td></tr>
                        <tr><td height="54"><input type="text" size="10px" name="strCaptcha_" placeholder="" ></td></tr>
                        <tr><td>   
                            <s:submit name="loginBtn" id="loginBtn" type="submit" theme="simple" value="%{getText('button.submit')}" src='%{#loginBtn}' action="processCheckActivateLoginESS"/>
                            <!--<button id="loginBtn" class="align_right" type="reset" value="<s:text name="button.reset"/><s:text name="button.reset"/>"></button>-->
                            </td>
                        </tr>
                    </table>
                    </form>        
                    </div>
                </td>
            </tr>
            
        </table>
            </div>
    </body>  
</html>
