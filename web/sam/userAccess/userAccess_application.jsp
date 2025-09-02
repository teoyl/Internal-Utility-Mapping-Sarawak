
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style type="text/css">
            @import url(styles/impian_style_internal.css);
        </style>
    </head>
    <body>
        <!--<div  style="height:390px; width: 580px; margin-left: 5px; ">-->
            <table class="userAccessTable xbox" cellpadding="5px;" style="border-collapse: collapse;"  width="580px" >   
           
            <tr class="userAccessTitle"> 
                <td class="userAccessDetailHeader" width="290px"><s:text name="module.type.M"/></td>
                <td class="userAccessDetailHeader" width="290px"><s:text name="module.type.S"/></td>
            </tr>
            
             ${moduleNameTiles_} 
            <%--<s:iterator value="groupAppList_" status="applicationListListStatus" id="applicationList">--%>
                <!--<tr>-->
                    <%--<td class="userAccessDetail" width="10px"> ${applicationList.application.attachedModule.module_name}</td>--%>
                   <%--<td class="userAccessDetail" width="10px">${moduleNameTiles_}</td>--%> 
                   <%--<td class="userAccessDetail" >${applicationList.application_id} --${applicationList.application.application_name}</td>--%>
                <!--</tr>-->
            <%--</s:iterator>--%>
                
              
               
         <!--</div>-->
        </table>
    </body>
</html>
