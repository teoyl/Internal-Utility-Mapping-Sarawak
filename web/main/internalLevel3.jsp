<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<style type="text/css">

.header {
    font-family: Arial,Helvetica,Geneva,Swiss,SunSans-Regular;
    color: #c9c9c9;
    font-size: 12px;
}
</style>


<html>
<head>
</head>
<body>
    <div id="mainLogin" style=" margin: 0; padding-top: -20pt; padding-left: 10pt; float: left" class="header">
    <s:property value="#session.userName"/>
    <a class="headerLink" title="Logout" href="processlogoutLogout">( Logout )</a>
</div> 

</body>
</html>