<%--
    Document   : editBackend
    Created on : Oct 6, 2010, 9:54:57 AM
    Author     : thoth
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>

<html>
    <head>
        <title>Administrator: Hibernate Information</title>
        <script language="JavaScript">
            function isInternetExplorer() {
                if (navigator.appVersion.match('MSIE 7')){
                    return true;
                } else {
                    return false;
                }
            }
            function confirmReset() {
                var answer = confirm("Confirm to reset?");
                var isIE = false;

                isIE = isInternetExplorer();
                return (isIE? event.returnValue = answer : answer);
            }
        </script>
    </head>
    <body bgcolor="white">
        <form action="loadInforHibernate" method="post">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                    <td width="50%"><b>Session Hashcode not closed properly:</b>
                        <s:submit theme="simple" value="Reset Count" action="resetSessionCountHibernate" onclick="return confirmReset();"/>
                        <s:submit theme="simple" value="Display/Hide Not Closed" action="displayNotCloseHibernate" />
                        <s:submit theme="simple" value="Display/Hide Active User" action="displayActiveUserHibernate" />
                        <s:hidden theme="simple" name="displayFlag" id="displayFlag" />
                        <s:hidden theme="simple" name="showActiveSession" id="showActiveSession" />
                    </td>
                    <td width="50%">
                        <!-- 1vne1t2v1w8v1s3g1s3m1w8v1t3b1vnoIN -->
                        <b>Connection Hashcode not closed properly:</b>
                        <s:submit theme="simple" value="Reset Count" action="resetConnectionCountHibernate" onclick="return confirmReset();"/>
                        <s:submit theme="simple" value="Reset Test" action="processSetSessionHibernate" />
                        <s:hidden theme="simple" name="us_user_id" id="us_user_id"/>
                        <s:hidden theme="simple" name="us_security" id="us_security"/>
                    </td>
                </tr>
                <tr>
                    <td width="50%">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                        <s:iterator value="notCloseSessionHashCode_key" var="notClosedHC" status="inforLabelStatus">
                            <tr><td width="100%">${notClosedHC}</td></tr>
                        </s:iterator>
                        </table>
                    </td>
                    <td width="50%">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                        <s:iterator value="notCloseConnectionHashCode_key" var="notClosedHC" status="inforLabelStatus">
                            <tr><td width="100%">${notClosedHC}</td></tr>
                        </s:iterator>
                        </table>
                    </td>
                </tr>
            </table>
            <br>
            <br>
            <b>Hibernate Configuration Information:${hostIP}</b><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr><td width="50%"><u>Session Connection Pool</u></td><td width="50%"><u>Connection Pool</u></td></tr>
                <tr>
                    <td valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                            <s:iterator value="sessionPropertyInforLabel" var="sessionInforLabel" status="inforLabelStatus">
                                <tr>
                                    <s:if test='closeSessionLess.equals("Y")'>
                                        <tr><td width="200px">${sessionInforLabel}</td><td>:</td>
                                            <td>
                                                <font <s:if test='#sessionInforLabel.equals("Closed Count")'>color='red'</s:if>><s:property value="sessionPropertyInfor[#inforLabelStatus.index]"/></font>
                                            </td>
                                        </tr>
                                    </s:if>
                                    <s:else>
                                        <tr><td width="200px">${sessionInforLabel}</td><td>:</td><td><s:property value="sessionPropertyInfor[#inforLabelStatus.index]"/></td></tr>
                                    </s:else>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                    <td valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                            <s:iterator value="propertyInforLabel" var="inforLabel" status="inforLabelStatus">
                                <tr>
                                    <s:if test='closeConnectionLess.equals("Y")'>
                                    <tr><td width="200px">${inforLabel}</td><td>:</td>
                                        <td>
                                            <font <s:if test='#inforLabel.equals("Closed Count")'>color='red'</s:if>><s:property value="propertyInfor[#inforLabelStatus.index]"/></font>
                                            </td>
                                        </tr>
                                </s:if><s:else>
                                    <tr><td width="200px">${inforLabel}</td><td>:</td><td><s:property value="propertyInfor[#inforLabelStatus.index]"/></td></tr>
                                        </s:else>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>
            </table>
            <s:if test='%{getDisplayFlag().equals("Y")}' >
                <b>Not Close Session:</b><br>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                    <tr><td width="50%"><u>Session Connection Pool</u></td><td width="50%"><u>Connection Pool</u></td></tr>
                    <tr>
                        <td valign="top">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                                <s:iterator value="sessionPropertyInforLabel" var="sessionInforLabel" status="inforLabelStatus">
                                <tr>
                                    <s:if test='closeSessionLess.equals("Y")'>
                                        <tr>
                                            <td width="200px">${inforLabel}</td><td>:</td>
                                            <td>
                                                <font <s:if test='#sessionInforLabel.equals("Closed Count")'>color='red'</s:if>><s:property value="sessionPropertyInfor[#inforLabelStatus.index]"/></font>
                                            </td>
                                        </tr>
                                    </s:if>
                                    <s:else>
                                        <tr><td width="200px">${sessionInforLabel}</td><td>:</td><td><s:property value="sessionPropertyInfor[#inforLabelStatus.index]"/></td></tr>
                                    </s:else>
                                </tr>
                                </s:iterator>
                            </table>
                        </td>
                        <td valign="top">
                        </td>
                    </tr>
                </table>
            </s:if>
            <s:if test="showActiveSession">
                <br>
                <br>
                <hr>
                <b>Active Session</b><br>
                                [<s:property value = "activeSessionSet.keySet()" />]
                <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                    <tr>
                        <td valign="top">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                                <s:iterator value="activeSessionSet" var="theSession" status="rStatus">
                                    <tr><td align="left" width="1px" >${rStatus.index + 1}.</td>
                                        <td align="left" width="20px" >Login ID</td>
                                        <td align="left" width="10px">:</td>
                                        <td align="left" width="200px"><s:property value='%{#theSession.getAttribute("p_loginId")}'/><s:property value='%{#theSession.getAttribute("loginId")}'/>@<s:property value='%{#theSession.getAttribute("user_ip")}'/></td>
                <!--                            <td align="right">System Type</td>
                                            <td width="10px">:</td>
                                            <td><s:property value='%{#theSession.getAttribute("loginSystemType_")}'/></td>
                                            <td align="right">Login Datetime</td>
                                            <td width="10px">:</td>
                                            <td><s:property value='%{#theSession.getAttribute("loginTime")}'/></td>-->
                                    </tr>
                                </s:iterator>
                            </table>
                        </td>
                    </tr>
                </table>
            </s:if>    
            
            <br><br>
            <textarea rows="5" style="width: 100%"><s:property value="hibernateStatistic"/></textarea>
        </form>
    </body>
</html>
