<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %> 
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@taglib uri="/struts-tags" prefix="s"%>   
<s:set name="ctx" value="%{pageContext.request.contextPath}"/>
 
<title><s:set id="systemName_"><s:text name="system.name"/></s:set>
<decorator:title default="${systemWelcome_}"/>
</title>     
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>     
<%--<style type="text/css">
	@import url(style.css);
</style> --%>
<decorator:head/>
  <script type="text/javascript" src="pages/scripts/common.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/pages/scripts/validation.js"></script>
  <link href="styles/general.css" rel="stylesheet" type="text/css" />
  <link href="styles/common.css" rel="stylesheet" type="text/css" />
<body style="margin: 0" bgcolor="#fd9602" >
<center>
<table width="990" align="center" bgcolor="white">
<tr>
    <td colspan="2">
        <page:applyDecorator page="/publicmain/iframeHeader.jsp" name="panel1" />
    </td>
</tr>

<tr align = "center" height ="350" bgcolor="white">
    <td colspan="2" valign="top">
        <decorator:body />
    </td>
	
</tr>
</table>
<table width="990" align="center" >
<tr>
	<td colspan="2">
        <page:applyDecorator page="/publicmain/publicFooter.jsp" name="panel1" />
	</td>
</tr>
</table>
</center>
</body>

