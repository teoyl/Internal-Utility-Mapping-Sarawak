<%-- 
    Document   : reportTabSelection
    Created on : September 20, 2013, 10:08:29 AM
    Author     : Edmund Chee
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <title>Report Tab Selection</title>
        <%--<s:head />--%>

        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>

        <link rel="stylesheet" type="text/css" href="styles/msen_style_internal.css" />


        <script language="javascript">
            function genReport() {
                var sYear=document.getElementById("prStartYear").value;
                var eYear=document.getElementById("prEndYear").value;

                if(sYear > eYear){
                    alert("Sila Masukkan Tarikh Yang Sah");
                    return false;
                }else{
                    window.open("generateYearReportPRHR.action?prStartYear="+ document.getElementById("prStartYear").value + "&prEndYear=" + document.getElementById("prEndYear").value);                    
                }
            }           
        </script>

    </head>
    <body>
        <form id="aaa" action="EmployeeActionPRHR" method="post" name="aaa" enctype="multipart/form-data">
            <table cellpadding="4" cellspacing="0">
                <tr>
                    <td style="font-weight: bold;" colspan="3"><s:text name="msgRpTabSelection2"/></td>
                </tr>
                <tr>
                    <td>
                        <s:text name="startYear"/>
                    </td>
                    <td>
                        :
                    </td>
                    <td>
                        <s:select theme="simple" list="generatedYearList" name="prStartYear" value="%{prStartYear}" listKey="keyData" listValue="valueData"/>

                    </td>
                </tr>
                <tr>
                    <td>
                        <s:text name="endYear"/>
                    </td>
                    <td>
                        :
                    </td>
                    <td>
                        <s:select theme="simple" list="generatedYearList" name="prEndYear" value="%{prEndYear}" listKey="keyData" listValue="valueData"/>
                    </td>
                </tr>
                <tr>
                    <%--<td align="right" colspan="3"><s:submit theme="simple" cssClass="defaultButton" onclick="window.location.href='generateYearReportPRHR.action?'" value="%{getText('button.generate')}" /></td>--%>
                    <td align="right" colspan="3"><button cssClass="defaultButton" onclick="if ( genReport()) {return true();} else {return false;}"><s:text name="button.generate"/></button></td>
                </tr>
            </table>
        </form>
    </body>
</html>
