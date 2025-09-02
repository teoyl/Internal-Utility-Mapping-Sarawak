<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sample</title>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="sampleFormId" class="">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>API Call<small>Sample</small></h4>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <label><u>API Call URL</u></label>
                    </div>
                    <div class="row">
                        <label>
                            <s:property value="apiUrl" escapeHtml="true"/>
                        </label>
                    </div><br>
                    <div class="row">
                        <label><u>API Call RETURN:</u></label>
                    </div>
                    <div class="row">
                        <label>
                            <s:property value="apiReturnStr" escapeHtml="true"/>
                        </label>
                    </div>
                </div>
            </div>
        </form>
</body>
</html>
