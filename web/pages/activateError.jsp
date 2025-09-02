<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript">
            /**
             * Comment
             */
        </script>
    </head>
    <body style=" background-image: url(images/loginBg.png)">
        <div class="main-box-bg">
            <table >
                <tr>
                    <td> 
                        <table cellpadding="3" cellspacing="0" width="100%" >
                            <tr class="supportTitle">
                                <td width="600px"><s:text name="account.operation.%{strTitle_}" /></td>
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
                            <table>
                                    <!--<tr><td >ID Pengguna <b><s:property value="%{userId}" /></b> telah wujud dalam SarawakNet, sila <a onclick="closePopup();">log masuk</a> dengan menggunakan ID Pengguna dan Kata Laluan(akuan emel) Sarawaknet anda.</td></tr>-->
                                <tr>
                                    <td>
                                        <s:submit name="loginBtn" id="loginBtn" type="submit" theme="simple" onClick="parent.winPodH.hide()" value="%{getText('button.ok')}" src='%{#loginBtn}'/>
                                        <!--<input type=button onClick="parent.winPodH.hide()" value="Close this window">-->
                                    </td>
                                </tr>
                            </table>      
                        </div>
                    </td>
                </tr>

            </table>
        </div>
    </body>
</html>