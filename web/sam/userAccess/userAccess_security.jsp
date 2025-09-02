
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Akses Pengguna</title>
        <s:set name="Sarawak" value="@com.sains.common.util.SystemConstants$ACCESS_TYPE@Sarawak"></s:set>
        <s:set name="COMMON" value="@com.sains.common.util.SystemConstants$DEPT_CODE@COMMON"></s:set>
        
    </head>
    <body><div class="xbox" style="height:370px; width: 570px; margin-left: 3px; ">
         <table class="userAccessTable" cellpadding="5px;" style="border-collapse: collapse;"  width="100%">   
            <tr class="userAccessTitle">     
                <td width="30%" valign="top"><s:text name="userAccess.secuGroup.level" /></td>
                <td class="userAccessDetailHeader" width="30%" valign="top"><s:text name="userAccess.secuGroup.dept" /></td>
                <td valign="top"><s:text name="userAccess.secuGroup.unit" /></td>
            </tr>
            <s:iterator value="groupSecurityList_" status="groupSecurityStatus" id="groupSecurity">
                <tr>  
                    <td valign="top"><s:text name="userSecurity.%{#groupSecurity.access_level}" /></td>
                    <td valign="top">
                        <s:if test="#groupSecurity.access_level == #Sarawak">
                            <s:text name="-"/>
                        </s:if>
                        <s:elseif test='dept_id.equals(#COMMON)'>
                            <s:text name="userAccess.common"/>
                        </s:elseif>
                        <s:else>
                            ${groupSecurity.department.dept_name}
                        </s:else>
                    </td>
                  
                    <td valign="top">
                        ${groupSecurity.establishment_bu.noAndDesc}
                      
                    </td>
                </tr>
            </s:iterator>
        </table>
    </body></div>
</html>
