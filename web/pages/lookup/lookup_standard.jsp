<%@taglib uri="/struts-tags" prefix="s"%>
<table class="form" border="0" cellpadding="1" cellspacing="1" width="100%">
  <tr>
    <td class="label">Code<td>
    <td class="label2">:</td>
    <td class="value"><input type="text" name="criterias(module_code)" value='<%=request.getParameter("criterias(module_code)")==null? "" : request.getParameter("criterias(module_code)")%>' size="30"/><td>
  </tr>
  <tr>
    <td class="label">Description<td>
    <td class="label2">:</td>
    <td class="value"><input type="text" name='criterias(module_name)' value='<%=request.getParameter("criterias(module_name)")==null? "" : request.getParameter("criterias(module_name)")%>' size="30" /><td>
  </tr>
</table>