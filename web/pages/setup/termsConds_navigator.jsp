<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>

<%--<%@taglib uri="/struts-dojo-tags" prefix="sx"%>--%>
<html>
    <head>        
        <%--<jsp:include page="/include/jquery-datepicker/datepick.impian.jsp"></jsp:include>--%>
        <jsp:include page="/include/jquery.autocomplete/ac.impian.jsp"></jsp:include>
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        
        </head>
        
        <body>
            <jsp:include page="/include/jquery-datepicker/datepick.impian.jsp"></jsp:include>
            <script language="javascript">
            $(document).ready(function() {
                initDatePicker();
            });
            </script>        
        <jsp:include page="/pages/setup/${currentPage_}.jsp"></jsp:include>
    </body>
</html>