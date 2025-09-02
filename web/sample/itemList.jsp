<%@taglib prefix="s" uri="/struts-tags" %>
<s:if test="itemList.isEmpty">
    <label></label>
</s:if><s:else>
<s:iterator value="itemList" var="theItem" status="itemStatus">
    <div class="">
        <%--<s:hidden name="itemList[%{#itemStatus.index}].keyData" value="%{#theItem.keyData}"/>--%>
        <s:select id="divId%{#itemStatus.index}" list="divisionList" onchange="" listKey="keyData" listValue="valueData" name="itemList[%{#itemStatus.index}].keyData" cssClass="form-control sds-dropdown newItems" value="%{#theItem.keyData}"/>
    </div>
</s:iterator>
</s:else>