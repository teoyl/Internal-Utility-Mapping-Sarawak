<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script type="text/javascript" language="javascript">
        function updateFields() {
            multipleRecs_ = false;
            <s:if test="result.size != 1">
                <s:if test="!(noErrorMsg)">
                    <s:if test='invokedMsg_!=null && invokedMsg_.length() > 0'>
                        alert('<s:property value="invokedMsg_"/>');
                        if (document.getElementById('<s:property value="focusOn"/>')) {
                            document.getElementById('<s:property value="focusOn"/>').focus();
                        } else {
                            document.getElementById('<s:property value="lookupSearchFieldId_"/>').focus();
                        }
                    </s:if><s:else>
                        alert('<s:text name="errors.searchDataNotFound"><s:param value="%{lookupSearchFieldData_}" /></s:text>');
                        if (document.getElementById('<s:property value="focusOn"/>')) {
                            document.getElementById('<s:property value="focusOn"/>').focus();
                        } else {
                            document.getElementById('<s:property value="lookupSearchFieldId_"/>').focus();
                        }
                    </s:else>
                </s:if><s:elseif test='invokedMsg_!=null && invokedMsg_.length() > 0'>
                    multipleRecs_ = true;
                </s:elseif>
            </s:if><s:else>
                <s:if test='invokedMsg_ != null && invokedMsg_.length() > 0'>
                alert('<s:property value="invokedMsg_"/>');
                if (document.getElementById('<s:property value="focusOn"/>')) {
                    document.getElementById('<s:property value="focusOn"/>').focus();
                } else {
                    document.getElementById('<s:property value="lookupSearchFieldId_"/>').focus();
                }
                </s:if>
            </s:else>
            <s:if test="writeToList.size > 0">
                <s:iterator value="writeToList" var="writeTo" status="writeToStatus">
                    <s:if test='#writeTo.startsWith("lb")'>
                        if (document.getElementById('<s:property value="#writeTo"/>')) {
                            document.getElementById('<s:property value="#writeTo"/>').innerHTML = " <s:property escapeHtml="false" value="writeToDataList[#writeToStatus.index]"/>";
                        }else{
                            alert("Fail to get [<s:property value="#writeTo"/>] by id, probably no id given.");
                        }
                    </s:if>
                    <s:else>
                        if (document.getElementById('<s:property value="#writeTo"/>')) {
                            document.getElementById('<s:property value="#writeTo"/>').value = "<s:property escapeHtml="false" value="writeToDataList[#writeToStatus.index]"/>";
                        }else{
                            alert('<s:property value="lookupParentFormId"/>');
                            alert("Fail to get [<s:property value="#writeTo"/>] by id, probably no id given.");
                        }
                    </s:else>
                </s:iterator>
            </s:if>
            <%--<s:if test="result.size != 1 ||(!noDuplicate || (invokedMsg_ != null && invokedMsg_.length() > 0))">
                document.getElementById('<s:property value="lookupSearchFieldId_"/>').focus();
            </s:if>--%>
            if (postEvent_) {
                if (postEvent_ != "") {
                    parent[postEvent_].apply(this, Array.prototype.slice.call(arguments, 1));
                }
            }
        }
    </script>
</head>
<body>
    <script type="text/javascript" language="javascript">
        updateFields();
    </script>
</body>
</html>