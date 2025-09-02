<%-- 
    Document   : map_viewer
    Created on : Sep 23, 2024, 9:39:22 AM
    Author     : yonglai
--%>

<%@taglib uri="/struts-tags" prefix="s"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="utimaps.mapviewer.title" /></title>
    </head>
    <body>
        <div>
            <iframe src="https://utilitysurvey-tnt.sarawak.gov.my/utimaps/esubi_map?parameters=<s:property value='%{mapParamStr}'/>" width="100%" height="600"></iframe>
        </div>
    </body>
</html>
