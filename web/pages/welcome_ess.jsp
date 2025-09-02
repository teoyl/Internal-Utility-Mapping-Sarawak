<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<%--<s:head />--%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>
    <script type="text/javascript" language="javascript">
        <%--ThoTH @ 20-May-2013--%>
        function changeImage(obj){
           obj.src="images/menu/"+obj.id+"_hover.png";
        }
        function changeImageBack(obj){
           obj.src="images/menu/"+obj.id+".png";
        }
    </script>
</head>
    <%
        // ThoTH @ 20-May-2013
        String titleList_ESS = (String) session.getAttribute("menuTitle_ESS");
        if (titleList_ESS == null) titleList_ESS = "";
    %>
<body>
    <s:set name="systemTypeValue"><%=com.sains.common.util.SystemConstants.SYSTEM_TYPE.DEFAULT%></s:set>
    <div id="menu-tile-container">
        <table id="menu-tile-table" align="center" border="0" cellpadding="0" cellspacing="3" >
            <%=titleList_ESS%>
        </table>
    </div>
    <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
    <s:include value="recentVisit.jsp"/>
</body>
</html>

