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
        <script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <%--POPUP--%>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" type="text/css" />
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" type="text/css" />
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        </head>
        
        <body>
            <jsp:include page="/include/jquery-datepicker/datepick.impian.jsp"></jsp:include>
            <script language="javascript">
            $(document).ready(function() {
                initDatePicker();
            });
            </script>        
        <jsp:include page="/sam/publicUser/${currentPage_}.jsp"></jsp:include>
    </body>
</html>