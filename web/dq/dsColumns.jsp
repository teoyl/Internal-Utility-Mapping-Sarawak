<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:text name="DqTemplate.appName"/></title>
        <script type="text/javascript">
        </script>
    </head>
    <body>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4><s:text name="DqTemplate.appName"/> <small><s:text name="DqTemplate.columns"/></small></h4>
            </div>
            <div class="panel-body">
                    <div class="row">
                <s:iterator value="columnList" var="theColumn" status="colStatus">
                        <div class="col-lg-4">${colStatus.index+1}.&nbsp;<s:property value="%{#theColumn.keyData}"/></div>
                </s:iterator>
                    </div>
            </div>
        </div>
    </body>
</html>
