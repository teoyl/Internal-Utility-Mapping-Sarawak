<%@taglib uri="/struts-tags" prefix="s"%>
<s:if test="#session.historyList != null">
<table class="defaultTable" align="left" width="400">
    <tr><th>History</th></tr>
<s:iterator value="#session.historyList" var="history">
    <tr><td>
    <s:a href="%{history}" >${session.historyMap[history]}</s:a>
    </td></tr>
</s:iterator>
</table>
</s:if>