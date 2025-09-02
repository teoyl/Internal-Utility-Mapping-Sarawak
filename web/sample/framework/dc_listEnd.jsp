<%@taglib uri="/struts-tags" prefix="s"%>
<td style="text-align: center; width: 1%">
    <button title="Edit this record" type="button" class="btn btn-sm btn-icon-only btn-warning" name="action:actionName" id="actionName" onclick="document.location='loadEditPageFrameworkSample?id=<s:property escapeHtml="true" value="%{#r.get('sample_table_id')}"/>'; return false;">
        <i class="fa fa-edit"></i>
    </button>
</td>
<td style="text-align: center; width: 1%">
    <button title="Say Hi" type="button" class="btn btn-sm btn-icon-only btn-warning" name="action:actionName" id="actionName" onclick="sayHello('<s:property escapeHtml="true" value="%{#r.get('string_column')}"/>'); return false;">
        <i class="far fa-hand-paper"></i>
    </button>
</td>