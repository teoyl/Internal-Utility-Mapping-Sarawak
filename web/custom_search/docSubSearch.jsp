<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<s:if test='usePopupCalander.equals("Y")'>
<script type="text/javascript" src="include/popcalendar.js"></script>
</s:if>
<%--<s:if test='getSearchFieldLookup().size() > 0'>--%>
<script type="text/javascript" src="pages/scripts/lookup.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
<%--</s:if>--%>

<script language="javascript">
    function resetFields(form) {
        var noOfElements = form.elements.length;
        for (var i = 0; i < noOfElements; i++) {
            if (!(form.elements[i].type == "hidden"
                || form.elements[i].type == "submit"
                || form.elements[i].type == "button")) {
                clearValue(form.elements[i]);
            }
        }
        setFocus(form);
    }

    <s:if test='usePopupCalander.equals("Y")'>
    InitCalendar2("images/",false);
    </s:if>
</script>
<table cellspacing="0" cellpadding="5" border="0" width="100%" >
    <tr>
        <form action="search2Dynamic" name="id5" id="search2DynamicFormId">
            <table class="wwFormTable" id="id4"><tbody>
        <tr>
            <td id="id3">
                <s:hidden theme="simple" name="action" />
                <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
                <table id="id2">
                    <tr>
                        <td width="100%">
                            <table align="left" border="0">
                                <tr id="id1" valign="center">
                                    <td width="20px">&nbsp;</td>
                                    <td class="tdLabel"><s:text name="documentSetup.nature"/></td>
                                    <td>
                                        <s:select theme="simple" name="search_sdoc_nature" list='getSearchDDList("sdoc_nature_dd")' listKey="code_1" listValue="code_desc" value="%{searchFieldsData[0]}"/>
                                    </td>
                                </tr>
                                <tr id="id1" valign="center">
                                    <td width="20px">&nbsp;</td>
                                    <td class="tdLabel"><s:text name="documentSetup.subNature"/></td>
                                    <td>
                                        <s:textfield theme="simple" name="search_sdoc_sub_nature" value='%{searchFieldsData[1]}' size="50"/>
                                        <script language="javascript">
                                            lookup("Search Sub Nature", "SubNature", "code_2", "search_sdoc_sub_nature",
                                            "useSetup_SubNature", "code_2,code_desc", "", "true", "search_sdoc_nature_as_nature");
                                        </script>
                                    </td>
                                </tr>
                                <tr id="id1" valign="center">
                                    <td width="20px">&nbsp;</td>
                                    <td class="tdLabel"><s:text name="documentSetup.type"/></td>
                                    <td ><s:textfield theme="simple" name="search_sdoc_type" value="%{searchFieldsData[2]}"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td valign="bottom">
                <table>
                    <tr valign="bottom">
                        <td>
                            <input type="submit" value="Search" class="defaultButton"/><input type="button" value="Reset" class="defaultButton" onclick="resetFields(this.form)"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        </tbody></table>
    </form>
    </tr>
<tr>
    <form action="search2Dynamic" id="sortForm">
        <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
        <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
        <s:hidden theme="simple" name="action" />
        <s:if test="result.size() > 0">
            <s:hidden theme="simple" name="listSize" value="1"/>
        </s:if>
        <s:else>
            <s:hidden theme="simple" name="listSize" value="0"/>
        </s:else>
        <s:iterator value="searchFields" var="field" status="rowStatus">
            <s:if test='#field.startsWith("_date_")'>
                <s:if test='#field.endsWith("_fromTo")'>
                    <s:hidden theme="simple" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/>
                    <s:hidden name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>
                </s:if>
                <s:else>
                    <s:hidden name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                </s:else>
            </s:if><s:else>
                <s:hidden theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}"/>
            </s:else>
        </s:iterator>
    </form>
</tr>
<tr><td><hr/></td></tr>
        <form action="dynamicAction">
        <s:if test='hideAddButton.equals("N") || hideDeleteButton.equals("N")'>
    <tr class="buttonMenu_2">
        <td class="buttonMenu_2">
            <s:hidden theme="simple" name="action" />
            <s:if test='hideAddButton.equals("N")'>
                <s:if test="has_right('loadAddPage')">
                    <s:submit theme="simple" action="%{addPageURL}" value="Add" cssClass="defaultButton" />
                </s:if>
            </s:if>
            <s:if test='hideDeleteButton.equals("N")'>
                <s:if test="has_right('delete')">
                    <s:submit theme="simple" action="%{deleteURL}" value="Delete" cssClass="defaultButton"
                      onclick="if ( isCheckboxSelected(form.selected)) {return confirmDelete();} else {return false};"/>
                </s:if>
            </s:if>
        </td>
    </tr>
        </s:if>
    <tr align="left" >
        <td>
            <jsp:include page="${listPage}.jsp"></jsp:include>
        </td>
    </tr>
</form>
</table>

