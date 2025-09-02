<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <%--<s:head />--%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body >
        <form action="updateSchedularLogin" method="post">
        <s:submit cssClass="btn btn-primary" theme="simple" value="Update Schedular"/><br><br>
        <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
            <tr>
                <th>Job Description</th>
                <th>On/Off</th>
                <th>Repeat</th>
                <th>Next Trigger</th>
                <th>Always trigger once</th>
                <th>Job Setup</th>
            </tr>
        <s:iterator value="timerJobMap" var="key" status="keyStatus">
            <tr>
                <td width='30%'>${key}</td>
                <td width='10%'><s:checkbox value="%{timerJobMap[key].jobSwitch_on}" name="%{key}_onOff"/></td>
                <td width='20%'>
                    ${timerJobMap[key].jobRepeat_disp}
                </td>
                <td width='20%'>${timerJobMap[key].nextTrigger}</td>
                <td width='20%'><s:checkbox value="%{timerJobMap[key].alwaysTriggerOnce}" name="%{key}_alwaysTriggerOnce"/></td>
                <td width='20%'><s:textfield value="%{timerJobMap[key].jobRunEvery}" name="%{key}_jobRunEvery"/></td>
            </tr>
        </s:iterator>
        </table>
    </body>
    </form>
</html>

