<%--Description : For eLasis Internal, eCase Management--%>

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
                        <table id="id2">
                            <input type="hidden" name="action" value="OnlineDealing" id="action"/>
                            <input type="hidden" name="dynamicSortBy" value="ecaseid" id="dynamicSortBy"/>
                            <input type="hidden" name="dynamicSortOrder" value="D" id="dynamicSortOrder"/>

                            <%--<tr id="id1" valign="center">

                                <td width="20px">&nbsp;</td>
                                <td class="tdLabel">eCase No</td>
                                <td >



                                    <input type="text" name="search_deal.deal_no" size="30" value="" id="search_deal_deal_no"/>


                                </td>
                            </tr>

                            <tr id="id1" valign="center">
                                <td width="20px">&nbsp;</td>

                                <td class="tdLabel">eCase Year</td>
                                <td >



                                    <input type="text" name="search_deal.deal_year" size="30" value="" id="search_deal_deal_year"/>


                                </td>
                            </tr>--%>

                             <%--eCase Year/No.--%>
                            <tr id="id1" valign="center">
                                <td width="20px">&nbsp;</td>
                                <td class="tdLabel"><s:text name="els.lbl.ecase.year"/></td>
                                <td><s:textfield theme="simple" maxLength="4" name="search_deal.deal_year" value="%{searchFieldsData[0]}" size="3"/>&nbsp;/&nbsp;
                                    <s:textfield theme="simple" name="search_deal.deal_no" value="%{searchFieldsData[1]}"/></td>
                            </tr>
                            <%--Client Reference--%>
                            <tr id="id1" valign="center">
                                <td width="20px">&nbsp;</td>
                                <td class="tdLabel"><s:text name="lbl.client.reference"/></td>
                                <td >
                                    <s:textfield theme="simple" maxLength="4" name="search_deal.deal_client_ref" value="%{searchFieldsData[2]}" size="30"/>
                                    <%--<input type="text" name="search_deal.deal_client_ref" size="30" value="" id="search_deal_deal_client_ref"/>--%>
                                </td>
                            </tr>
                            <%--eCase Division--%>
                            <tr id="id1" valign="center">
                                <td width="20px">&nbsp;</td>
                                <td class="tdLabel"><s:text name="els.lbl.ecase.division"/></td>
                                <td >
                                    <s:select theme="simple" name="search_deal.deal_division" list='getSearchDDList("deal.deal_division_dd")' listKey="keyData" listValue="valueData" value="%{searchFieldsData[3]}"/>
                                    <%--<select name="search_deal.deal_division" id="search_deal_deal_division">
                                        <option value="" selected="selected">ALL</option>
                                        <option value="01">KUCHING</option>
                                        <option value="02">SRI AMAN</option>
                                        <option value="03">SIBU</option>
                                        <option value="04">MIRI</option>

                                        <option value="05">LIMBANG</option>
                                        <option value="06">SARIKEI</option>
                                        <option value="07">KAPIT</option>
                                        <option value="08">SAMARAHAN</option>
                                        <option value="09">BINTULU</option>
                                        <option value="10">MUKAH</option>

                                        <option value="11">BETONG</option>
                                    </select>--%>
                                </td>
                            </tr>
                            <%--eCase Status--%>
                             <tr id="id1" valign="center">
                                <td width="20px">&nbsp;</td>
                                <td class="tdLabel"><s:text name="els.lbl.ecase.status"/></td>
                                <td>
                                    <s:select theme="simple" name="search_deal.deal_status" list='getSearchDDList("deal.deal_status_dd")' listKey="keyData" listValue="valueData" value="%{searchFieldsData[4]}"/>
                                </td>
                            </tr>
                            <%--Status Date--%>
                            <tr id="id1" valign="center">
                                <td width="20px">&nbsp;</td>
                                <td class="tdLabel"><s:text name="lbl.status.date"/></td>
                                <td>
                                    <s:textfield theme="simple" name="search_deal_updated_dateFrom" value='%{searchFieldsDateData.get("search_deal_updated_dateFrom")}' size="26" readonly="true"/>
                                    <%--<input type="text" name="search_pl_assign_dateFrom" size="26" value="%{searchFieldsData[7]}" readonly="readonly" id="search_pl_assign_dateFrom"/>--%>
                                    <img alt="" src="images/calendar.gif" id="imgCalTo1" style="" onclick="popUpCalendar(this, document.getElementById('search_deal_updated_dateFrom'), 'dd mmmm yyyy')"   title="Calendar" align="absmiddle" height="18" width="18">&nbsp;<s:text name="lbl.to"/>
                                    <s:textfield theme="simple" name="search_deal_updated_dateTo" value='%{searchFieldsDateData.get("search_deal_updated_dateTo")}' size="26" readonly="true"/>
                                    <%--<input type="text" name="search_pl_assign_dateTo" size="26" value="%{searchFieldsData[8]}" readonly="readonly" id="search_pl_assign_dateTo"/>--%>
                                    <img alt="" src="images/calendar.gif" id="imgCalTo1" style="" onclick="popUpCalendar(this, document.getElementById('search_deal_updated_dateTo'), 'dd mmmm yyyy')"   title="Calendar" align="absmiddle" height="18" width="18">
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

