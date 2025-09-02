<%-- 
    Document   : dynamicRptControl
    Created on : Dec 9, 2013, 12:31:07 PM
    Author     : Delvene
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<title>${searchDescription} - <s:text name="actionType.search" /></title>
<%--<s:head />--%>

</head>
<body>
<jsp:include page="actionError.jsp"></jsp:include>

<div class="panel panel-default">
<div class="panel-heading">
<div class="panel-title">
<!--<div class="titleFramework">-->
    <span class="titleText">${searchDescription}</span>
    <span class="titleActionTypeText"> | <s:text name="actionType.search" /></span><br>
</div>
</div>
<div class="panel-body">
<!--<div class="xbox">-->
    <jsp:include page="${searchPage}.jsp"></jsp:include>
</div>
</div>
</body>
</html>
