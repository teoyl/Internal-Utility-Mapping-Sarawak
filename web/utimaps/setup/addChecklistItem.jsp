<%-- 
    Document   : addChecklistItem
    Created on : Dec 10, 2018, 2:28:40 PM
    Author     : User
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Checklist Items</title>
        
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />

        <s:head />
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
                            
        <%-- <form theme="simple" action='${model.ug_id != ""?"addListItemEditChecklistSetup":"addListItemChecklistSetup"}' method="post"> --%>
        <form theme="simple" action='' method="post" id="addListItemChecklistSetup">
            <s:hidden name="action" />
            <div class="row">
                <div class="col-md-5">
                    <div class="form-group form-group-default required">
                        <label><s:text name="checklist.desc"/></label>
                        <s:textfield theme="simple" name="application_code" value="" cssClass="form-control"/>
                    </div>
                    
                    <div class="form-group form-group-default required">
                        <label><s:text name="checklist.dataType"/></label>
                        <s:textfield theme="simple" name="application_code" value="" cssClass="form-control"/>
                    </div>
                    
                </div> 
            </div>
                    
            <div class="row form-row-margin">
                <div class="col-md-12">
                    <%-- Too be modified again --%>
                    <div class="form-group form-group-default">
                        <s:if test='model.ug_id != ""'>
                            <button class="btn btn-default" type="submit" name="action:addListItemEditChecklistSetup" id="addListItemEditChecklistSetup"><i class="fa fa-plus"></i>Add</button>
                        </s:if>
                        <s:else>
                            <button class="btn btn-default" type="submit" name="action:addListItemChecklistSetup" id="addListItemChecklistSetup"><i class="fa fa-plus"></i>Add</button>
                        </s:else>
                    </div>
                </div>
            </div>    
        </form>
    </body>
</html>
