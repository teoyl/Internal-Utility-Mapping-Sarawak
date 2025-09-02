<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<script type="text/javascript" src="pages/menu/simpletreemenu.js"></script>
<link rel="stylesheet" type="text/css" href="pages/menu/menutree.css" />


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head><body>

<%
	String treeList = (String) session.getAttribute("menuList");
	if (treeList == null) treeList = "";
%>
<table bgcolor = "#207B24" height="100%" width="100%" border="0" cellspacing="0" cellpadding="0" style="border-collapse:collapse">
<tr valign="top"><td width="100%">
<ul id='navigationTree' class='treeview2' >
<%=treeList%>
</ul>
</td></tr></table>

</body>
<script type="text/javascript">
var navigationTree = new treeMenu("navigationTree", true, 1, "", "pages/menu/images/menu_arrow_close.gif", "pages/menu/images/menu_arrow_open.gif");
//navigationTree.createTree("navigationTree", true, 0, "", "images/menu_arrow_close.gif", "images/menu_arrow_open.gif")
</script>
</html>