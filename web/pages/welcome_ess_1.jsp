<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<%--<s:head />--%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
    <script type="text/javascript" language="javascript">
        <%--ThoTH @ 20-May-2013--%>
        function changeImage(obj){
           <%--obj.src="images/menu/"+obj.id+"_hover.png";--%>
           obj.style.backgroundImage="url(images/menu/"+obj.id+"_main_hover.png)";  //edited by Delvene @ 13-Feb-2014
        }
        function changeImageBack(obj){
           <%--obj.src="images/menu/"+obj.id+".png";--%>
           obj.style.backgroundImage="url(images/menu/"+obj.id+"_main.png)";    //edited by Delvene @ 13-Feb-2014
        }
        function loginToSifbas() {
            var tempHtml = document.getElementById("sifbasSpan").innerHTML;
            document.getElementById("sifbasSpan").innerHTML = "<s:text name="rightPanel.login.SIFBAS.connecting"/>";
            $.getJSON('sifbasLogin',function(data) {
                if(data.status === "success") {
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
        // ThoTH @ 20-May-2013
        //String titleList_ESS = (String) session.getAttribute("menuTitle_ESS");
        String titleList_ESS = (String) session.getAttribute("moduleTile_ESS"); //edited by Delvene @ 13-Feb-2014
        if (titleList_ESS == null) titleList_ESS = "";
    %>
<body>
    
    <%--Edited by Delvene @ 13-Feb-2014--%>
    <s:set name="systemTypeValue"><%=com.sains.common.util.SystemConstants.SYSTEM_TYPE.EQP%></s:set>
    <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
    <div id="menu-tile-container">
            <%--Added by Delvene @ 13-Feb-2014 :: To display application tiles in main screen--%>
            <s:if test="appTile != null && appTile != ''">
                <table id="menu-tile-table" border="0" cellpadding="5" cellspacing="5" width="100%">
                    ${appTile}
                </table>
            </s:if>
            <s:else>
               <%-- <table id="menu-tile-table" align="center" border="0" cellpadding="0" cellspacing="5" >--%>
                 <!--<div class="row">-->
                     <%--Added by sereneChye @ 25/8/2014 :: To display title of main layout--%>
                    <%=titleList_ESS%>
                    <%-- </table>--%>
                <!--</div>-->
            </s:else>
            <%--Added by Delvene @ 10-Dec-2013 :: To display application tiles in main screen - END--%>
    </div>
   
    <s:include value="recentVisit.jsp"/>
</body>
</html>

