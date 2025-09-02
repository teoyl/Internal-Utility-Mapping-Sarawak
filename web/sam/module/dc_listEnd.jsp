<%@taglib uri="/struts-tags" prefix="s"%>
<td style="text-align: center; width: 1%">
    <s:property escapeHtml="true" value="%{#r.get('parent_code')}"/>
</td>
<td style="text-align: center; width: 1%">
    <a href="#" onclick="tryCall('<s:property escapeHtml="true" value="%{#r.get('parent_code')}"/>'); return false;">BBB</a>
</td>