<%@taglib uri="/struts-tags" prefix="s"%>

<table class="" border="0" cellpadding="1" cellspacing="1" width="100%">
  <tr class="">
    <td class="" align="center">
      <s:submit value='%{getText("button.search")}' cssClass="defaultButton" type="button" theme="simple"/>
      <s:submit value="Reset" cssClass="defaultButton" type="button" onclick="resetFields(this.form); return false;" theme="simple"/>
    </td>
  </tr>
</table>
