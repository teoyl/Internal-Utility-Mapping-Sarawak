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

    //function setDivision(divCode){
    //    var a = divCode;
   //     alert(a);
   //     document.getElementById('search_div_').value = a;
    //}

    function pad(number, length, control) {
        var str = '' + number;
        while (str.length < length) {
            str = '0' + str;
        }
        control.value = str;
        <%--return str;--%>
    }
    <s:if test='usePopupCalander.equals("Y")'>
    InitCalendar2("images/",false);
    </s:if>

    // added by etys @25-04-2012
    <%-- commented by thensw: don't use the method as this will always change the year field back to current even if user already change the criteria to other year.
      -- Please use "dynamicDefaultSearchValueMethod" at dynamic-config_o there to set your dynamic default search value.
        window.onload = function() {
        document.getElementById("search_deal_year").value = new Date().getFullYear();
    }--%>
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
                    <%--from dynamicSearch--%>
                    <tr><td width="100%">
                            <table align="left" border="0">
                                <tr id="id1" valign="center">
                                    <td width="20px">&nbsp;</td>
                                    <td class="tdLabel"><s:text name="els.lbl.ecase.year"/></td>
                                    <td><s:textfield theme="simple" maxLength="4" name="search_deal_year" value="%{searchFieldsData[0]}" size="3"/>&nbsp;/&nbsp;
                                        <s:textfield theme="simple" name="search_deal_no" value="%{searchFieldsData[1]}"/></td>
                                </tr>
                                <%--<tr id="id1" valign="center">
                                    <td width="20px">&nbsp;</td>
                                    <td class="tdLabel">eCase Year</td>
                                    <td ></td>
                                </tr>--%>
                                <%--added by ahmadni 9/11/2012 - for internal instrument--%>
                                <s:if test="action != 'internalInst'">
                                <tr id="id1" valign="center">
                                    <td width="20px">&nbsp;</td>
                                    <td class="tdLabel"><s:text name="lbl.client.reference"/></td>
                                    <td ><s:textfield theme="simple" name="search_deal_client_ref" value="%{searchFieldsData[2]}"/></td>
                                </tr>
                                </s:if>
                                <%--added by ahmadni 9/11/2012 - for internal instrument--%>
                                <s:if test="action != 'internalInst'">
                                <tr id="id1" valign="center">
                                    <td width="20px">&nbsp;</td>
                                    <td class="tdLabel"><s:text name="els.lbl.ecase.division"/></td>
                                    <td >
                                       <s:select theme="simple" name="search_deal_division" list='getSearchDDList("deal_division_dd")' listKey="keyData" listValue="valueData" value="%{searchFieldsData[3]}"/>
                                    <%--<s:select theme="simple" name="search_deal_division" list='getSearchDDList("deal_division_dd")' listKey="keyData" listValue="valueData" value="%{searchFieldsData[3]}" onchange="setDivision(this.value)" />--%></td>
                                </tr>
                                </s:if>
                                <tr id="id1" valign="center">
                                    <td width="20px">&nbsp;</td>
                                    <td class="tdLabel"><s:text name="els.lbl.affected.trn"/></td>
                                    <td >
                                        <table align="left" border="0">
                                            <tr align="center">
                                                <td><s:text name="lbl.division"/></td><td>-</td>
                                                <td><s:text name="els.lbl.trn.type"/></td><td>-</td>
                                                <td><s:text name="els.lbl.trn.district"/></td><td>-</td>
                                                <td><s:text name="els.lbl.trn.block"/></td><td>-</td>
                                                <td><s:text name="els.lbl.trn.lot"/></td><td>(</td>
                                                <td><s:text name="els.lbl.trn.storey"/></td><td>-</td>
                                                <td><s:text name="els.lbl.trn.parcel"/></td><td>)</td>
                                            </tr>
                                            <tr align="center">
                                                <td><s:textfield maxLength="2" style="text-align : center" theme="simple" name="search_div_" value='%{searchFieldsDateData.get("search_div_")}' size="1"/></td><td>-</td>
                                                <td><s:textfield theme="simple" id="search_type_" name="search_type_" value='%{searchFieldsDateData.get("search_type_")}' size="6" maxLength="5" onchange="loadDescs('useSetup_TrnType', 'code_acr', this, 'code_acr', 'type_', '2'); return false;"/>
                                                    <%--<s:textfield onfocus="defaultButton(this,'processInsert%{strutsAction}')" onblur="defaultButton(this,'processUpdate%{strutsAction}')" theme="simple" id="type_" name="search_type_" value='%{searchFieldsDateData.get("search_type_")}' size="6" maxLength="5" onchange="loadDescs('useSetup_TrnType', 'code_acr', this, 'code_acr', 'type_', '2'); return false;"/>--%>
                                                    <script language="javascript">
                                                        lookup("Search Title Type", "TrnType", "code_acr", "search_type_",
                                                        "useSetup_TrnType", "code_acr,code_desc");
                                                    </script>
                                                </td><td>-</td>
                                                <td><s:textfield onkeypress="return (checkNumberDec(this,event,3,0));" theme="simple" name="search_dist_" value='%{searchFieldsDateData.get("search_dist_")}' size="4" maxLength="3" />
                                                    <%--<s:textfield onfocus="defaultButton(this,'processInsert%{strutsAction}')" onblur="defaultButton(this,'processUpdate%{strutsAction}')" onkeypress="return (checkNumberDec(this,event,3,0));" theme="simple" name="search_dist_" value='%{searchFieldsDateData.get("search_dist_")}' size="4" maxLength="3" />--%>
                                                    <script language="javascript">
                                                         lookup("Search District", "District", "code_2", "search_dist_",
                                                        "useSetup_District", "code_2,code_desc", "", "true", "search_div__as_div");
                                                    </script>
                                                </td><td>-</td>
                                                <td><s:textfield onkeypress="return (checkNumberDec(this,event,3,0));" theme="simple" name="search_bs_" onchange="pad(this.value, 3, this)" value='%{searchFieldsDateData.get("search_bs_")}' size="4" maxLength="3" /></td><td>-</td>
                                                <td><s:textfield onkeypress="return (checkNumberDec(this,event,5,0));" theme="simple" name="search_lot_" onchange="pad(this.value, 5, this)" value='%{searchFieldsDateData.get("search_lot_")}' size="4" maxLength="5" /></td><td>(</td>
                                                <td><s:textfield onkeypress="return (checkNumberDec(this,event,2,0));" theme="simple" name="search_storey_" value='%{searchFieldsDateData.get("search_storey_")}' size="4" maxLength="5" /></td><td>-</td>
                                                <td><s:textfield onkeypress="return (checkNumberDec(this,event,2,0));" theme="simple" name="search_parcel_" value='%{searchFieldsDateData.get("search_parcel_")}' size="4" maxLength="5" /></td><td>)</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td></tr>
                   
                </table>            </td>
            <td valign="bottom">
                <table>
                    <tr valign="bottom">
                        <td><s:submit cssClass="elButton" theme="simple" id="btnSearch" value="%{getText('button.search')}" />
                            &nbsp<s:submit cssClass="elButton" theme="simple" id="btnReset" value="%{getText('button.reset')}" onclick="resetFields(this.form)" />
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

