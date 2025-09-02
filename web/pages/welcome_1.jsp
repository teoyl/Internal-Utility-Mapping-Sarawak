<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <%--<s:head />--%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome</title>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" language="javascript">
            <%--ThoTH @ 20-May-2013--%>
            function changeImage(obj) {
                //obj.src="images/menu/"+obj.id+"_hover.png";
                obj.style.backgroundImage = "url(images/menu/" + obj.id + "_main_hover.png)";  //edited by Delvene @ 11-Dec-2013
            }
            function changeImageBack(obj) {
                //obj.src="images/menu/"+obj.id+".png";
                obj.style.backgroundImage = "url(images/menu/" + obj.id + "_main.png)";    //edited by Delvene @ 11-Dec-2013
            }
            function loginToSifbas() {
                var tempHtml = document.getElementById("sifbasSpan").innerHTML;
                document.getElementById("sifbasSpan").innerHTML = "<s:text name="rightPanel.login.SIFBAS.connecting"/>";
                $.getJSON('sifbasLogin', function(data) {
                    alert('data.url : ' + data.url);
                    if (data.status === "success") {
                        document.getElementById("sifbasSpan").innerHTML = tempHtml;
                        window.open(data.url, '_blank');
                    } else {
                        document.getElementById("sifbasSpan").innerHTML = tempHtml;
                        alert(data.status);
                    }
                });
            }

            // ThoTH @ 5-Jun-2015
            function submitForm(form, action, operation) {
                form.action = action;
                form.submit();
            }
        </script>
    </head>
    <%
        // Zhafari @ 16-Apr-2013 - tile menu
        //String titleList = (String) session.getAttribute("menuTitle");
        String titleList = (String) session.getAttribute("moduleTile"); //edited by Delvene @ 11-Dec-2013
        if (titleList == null) {
            titleList = "";
        }
        // Zhafari @ 16-Apr-2013 - END
    %>
    <body >
        <s:if test="actionErrors.size() > 0 || actionMessages.size() > 0">
            <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        </s:if>
        <s:set name="systemTypeValue"><%=com.sains.common.util.SystemConstants.SYSTEM_TYPE.DEFAULT%></s:set>
            <div id="menu-tile-container" >
            <%--Added by Delvene @ 10-Dec-2013 :: To display application tiles in main screen--%>
            <s:if test="appTile != null && appTile != ''">
                ${appTile}
            </s:if>
            <s:else>
                <div class="row">
                    <%--left box--%>
                    <div class="col-md-8">
                        <!--<div class="row">-->
                            <%=titleList%>
                        <!--</div>-->
                    </div>
                    <%--right box--%>
                    <div class="col-md-4">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="shop-box bordered">
                                    <table width="100%">
                                        <tr>
                                            <td>
                                                <h2 class="title-v3 text-capitalize"><s:text name="rightPanel.SystemNotice" />
                                            </td>
                                            <td>
                                                <i class="fa fa-bell-o fa-3x text-green pull-right"></i></h2>
                                            </td>
                                        </tr>
                                    </table>  
                                    <div class="row">
                                         <div class="col-md-12"><h4><a href="loadEditPageRouteJobMain" style="color:black;"><s:text name="rightPanel.TaskPane" /></a></h4></div>
                                        <!--23.10.2018 replaced by Ivy to eSPA Job Listing Page--> 
                                        <!--<div class="col-md-12"><h4><a href="loadEditPageJobMain" style="color:black;"><s:text name="rightPanel.Task" /></a></h4></div>-->
                                    </div><br>        
                                    <div class="row">
                                        <div class="col-xs-6 col-md-10"><s:text name="rightPanel.TaskInPool" /></div>
                                        <div class="col-xs-6 col-md-2">
                                            <a href="loadEditPageRouteJobMain?page=welcome" class="btn btn-primary pull-right">${jobMap_.get('longcount')}</a>
                                            <%--button class="btn btn-primary pull-right" type="button">${jobMap_.get('longcount')}</button--%></div>
                                    </div>
                                    <div class="row sideBox">
                                        <hr>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-6 col-md-10"><s:text name="rightPanel.TaskInHand" /></div>
                                        <div class="col-xs-6 col-md-2">
                                            <a href="loadEditPageRouteJobMain" class="btn btn-primary pull-right">${jobMap_.get('ipcount')}</a>
                                            <%--button class="btn btn-primary pull-right" type="button">${jobMap_.get('ipcount')}</button--%></div>
                                    </div>        
                                   <%-- <div class="row sideBox">
                                        <hr>
                                    </div><br>
                                    <div class="row">
                                        <div class="col-md-12"><h4><s:text name="qp.reference.guide" /></h4></div>
                                    </div><br> 
                                    <div class="row">
                                        <div class="col-md-12"><a href="downloadFile?dType=userManualInternal"><s:text name="qp.user.guide" /></a></div>
                                    </div>--%>
                                </div>
                            </div>
                        </div>
                    </div><br><br>
                    <%--div class="menu-tile-sidebar " style="float: left; padding-left: 15px; ">
                        <table>
                           <tr><td class="sidebar_header"><span style="padding-left: 20px;"><s:text name="rightPanel.SystemNotice" /></span>
                                <span class="notice-img pull-right" ><img src="images/sidebar/icon_notify.png"></span></td></tr>
                           <tr><td class="sidebar_title">
                                 <div class='sidebar_subtitle' style="border-bottom: solid #eeeeee 2px;">
                                     <a href="loadEditPageJobMain"><s:text name="rightPanel.Task" /></a>
                                 </div>
                                     <table width="100%" align="left" border="0" style="font-weight: normal; " >
                                         <tr style="border-bottom: solid #eeeeee 2px; height: 30px;">	
                                            <td width="15px">&nbsp;</td>	
                                            <td width="150px"><s:text name="rightPanel.TaskInPool" /></td>
                                            <td class="grey-bg"><s:property value="%{jobMap_.get('longcount')}"/></td>
                                         </tr>
                                         <tr height="30px">
                                             <td></td>
                                             <td><s:text name="rightPanel.TaskInHand" /></td>
                                             <td class="grey-bg" ><s:property value="%{jobMap_.get('ipcount')}"/></td>
                                         </tr>
                                     </table>  
                                 </td>
                             </tr>
                             <tr><td class="sidebar_title">
                                 <div class='sidebar_subtitle' style="border-bottom: solid #eeeeee 2px;">
                                     <s:text name="qp.reference.guide" />
                                 </div>
                                     <table width="100%" align="left" border="0" style="font-weight: normal; " >
                                         <tr style="border-bottom: solid #eeeeee 2px; height: 30px;">	
                                            <td width="15px">&nbsp;</td>	
                                            <td width="150px"><a href="downloadFile?dType=userManualInternal"><s:text name="qp.user.guide" /></a></td>
                                            
                                         </tr>                                         
                                     </table>  
                                 </td>
                             </tr>
                        </table>
                    </div--%>
                </div>
            </s:else>
        </div>
        <s:include value="recentVisit.jsp"/>
    </body>
</html>

