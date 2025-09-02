<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<script type="text/javascript" src="pages/scripts/confirmation.js"></script>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<s:if test='usePopupCalander.equals("Y")'>
    <jsp:include page="/include/jquery-datepicker/datepick.impian.jsp"></jsp:include>
<!--    <script type="text/javascript" src="include/popcalendar.js"></script>-->
</s:if>
<s:if test='getSearchFieldLookup().size() > 0 || isMixConfig'>  <%-- Edited by Delvene @ 28-Aug-2014 :: To open access to lookup in mixConfig --%>
<script type="text/javascript" src="pages/scripts/lookup.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
<script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
<link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
<link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
</s:if>

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
    function getPageSize(fieldType, form) {
        <s:if test='showPageSize'>
            document.getElementById('searchFormPageSize'+fieldType).value = document.getElementById('mainPageSize').value;
            if(fieldType=="_mc"){
                form.searchFormPageSize_mc.value = document.getElementById('mainPageSize').value;
            }
        </s:if>
        return true;
    }
    <s:if test='isMixConfig'>
    function getSearchCode() {
        document.getElementById('addFormSearchCode').value = document.getElementById('mainSearchCode').value;
        return true;
    }
    function searchCodeChange(fieldData) {
        <s:iterator value="mcf_actionList" var="mcfAction" status="mcfActionStatus">
            document.getElementById('${mcfAction}table').style.visibility='hidden';
            document.getElementById('${mcfAction}table').style.height='0px';
            document.getElementById('${mcfAction}addDelete').style.visibility='hidden';
            document.getElementById('${mcfAction}addDelete').style.height='0px';
        </s:iterator>
        document.getElementById(fieldData.split("::")[0]+"table").style.visibility='inherit';
        document.getElementById(fieldData.split("::")[0]+"table").style.height='auto';
        document.getElementById(fieldData.split("::")[0]+"addDelete").style.visibility='inherit';
        document.getElementById(fieldData.split("::")[0]+"addDelete").style.height='auto';
        document.getElementById(fieldData.split("::")[0]+"searchCode").value = fieldData;
    }
    
    // Added by Delvene @ 28-Aug-2014 :: Overwrite the lookup function in lookup.js to support dynamic form id according to MixConfig action (mcfAction)
    // Added a new parameter :: mcfAction
    function lookup(title, query, lookFor, writeTo, lookupType, displayedColumns, focusOn, onclick, filterBy, mcfAction) {
        var parentFormId = mcfAction + "search2DynamicFormId";
        window.listenerAttached = false;
        var args='query=' + query + "&lookFor=" + lookFor + "&writeTo=" + writeTo;
        var writeTo_Array = writeTo.split(",");
        var firstField = writeTo_Array[0];
        if (filterBy)
        {
          args += '&filterBy=' + filterBy;
        }
        if (lookupType)
        {
          args += '&lookup=' + lookupType;
        } 
        if (displayedColumns)
        {
          args += '&displayedColumns=' + displayedColumns;
        } 
        if (focusOn)
        {
              args += '&focusOn=' + focusOn;
        }
        if (title) 
        {
              args += '&title=' + title;
        }
        else {
              title = "Lookup";
        }
        args = "processLookup?" + args;
        args += "&lookupParentFormId=" + parentFormId;
//        onclick = attachLookupOnclick(query, args, onclick, filterBy, title);
        onclick = attachLookupOnclick(query, args, onclick, filterBy, title, '450','700');// sereneChye@27/10/2014 add Height, Width 
        document.write("<img id=\'lu_"+ firstField + "\' style=\'cursor: pointer; cursor:hand;' src=\'images/document_lookup.gif\' alt=\'" + title +"\' width=\'18' height=\'18' align=\'absmiddle' onclick=\'" + onclick + "\'>");
    }
    // Added by Delvene @ 28-Aug-2014 :: Overwrite the lookup function in lookup.js to support dynamic form id according to MixConfig action (mcfAction) - END
    </s:if>
    <s:if test='usePopupCalander.equals("Y")'>
        $( document ).ready(function() {
            initDatePicker();
            <s:iterator value="searchFields_with_dateFromTo" var="popCalField" status="popCalFieldStatus">
                $('#${popCalField}From').datepick('option', {onSelect: function(dateText, instance){prepareEndDate(dateText, '${popCalField}')}});
            </s:iterator>
        });
        <s:if test="searchFields_with_dateFromTo != null && searchFields_with_dateFromTo.size > 0">
        function prepareEndDate(date, dateTo_id) {
//            $('#'+dateTo_id+'To').datepick('setDate', date);
            $('#'+dateTo_id+'To').datepick('option', {minDate: $('#'+dateTo_id+'From').val()}).focus();
        }
        </s:if>

//        InitCalendar2("images/",false);
    </s:if>
</script>
<table id="mainTable" cellspacing="0" cellpadding="5" border="0" width="100%" >
    <tr class="bgSearch" ><%--sereneC @ 28/8/2014 :: added style--%>
        <td style="border-bottom: 1px solid #aaaaaa;vertical-align:bottom;">
    <s:if test='isMixConfig'>
        <table class="wwFormTable" width="100%">
            <tr valign="center">
                <td class="tdLabelleft" width="205px">Sila Pilih</td>
                <td>
                    <%--Edited by Delvene @ 28-Aug-2014 :: To support dropdown list in other MixedConfig--%>
                    <%--<s:select onchange="searchCodeChange(this.value)" theme="simple" cssStyle="width: 322px" id="mainSearchCode" name="searchCode" list='getSearchDDList("searchCode_dd")' listKey="keyData" listValue="valueData" value='%{searchCode}' />--%>
                    <s:select onchange="searchCodeChange(this.value)" theme="simple" cssStyle="width: 322px" id="mainSearchCode" name="searchCode" list='mixedConfig_map.get("mainSearchCodeDD").get("searchCode_dd")' listKey="keyData" listValue="valueData" value='%{searchCode}' />
                    <%--Edited by Delvene @ 28-Aug-2014 :: To support dropdown list in other MixedConfig - END--%>
                </td>
            </tr>
        </table>
        <s:iterator value="mcf_actionList" var="mcfAction" status="mcfActionStatus">
            <%--<form style="visibility: hidden; height: 0" action="${mcfAction}search2Dynamic" id="${mcfAction}search2DynamicFormId" method="POST">--%>
            <div id="${mcfAction}table">
        <table class="wwFormTable" width="100%">
        <tr>
            <form action="search2Dynamic" name="${mcfAction}search2DynamicFormId" id="${mcfAction}search2DynamicFormId" method="POST">
                <td>
                <table>
                    <s:iterator value="hiddenFields" var="hiddenField" status="hiddenRowStatus">
                    <input type='hidden' <s:property escapeHtml="true" value="%{#hiddenField}"/> />
                    </s:iterator>
                    <s:hidden theme="simple" name="pageSize" id="searchFormPageSize_mc"/>
                    <s:hidden theme="simple" name="action" />
                    <s:hidden theme="simple" name="dynamicSortBy" value='%{mixedConfig_map.get(#mcfAction+"dynamicSortBy")}' />
                    <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
                    <s:hidden theme="simple" id="%{mcfAction}searchCode" name="searchCode" value="%{searchCode}" />
                    <s:iterator value='%{mixedConfig_map.get(#mcfAction+"searchFields")}' var="field" status="rowStatus">
                    <tr valign="center">
<!--                        <td class="tdLabel" width="117px">-->
                            <td class="tdLabelleft" width="200px">
                            <s:property value='%{mixedConfig_map.get(#mcfAction+"searchFieldsLabel")[#rowStatus.index]}'/>
                        </td>
                        <td ><s:set name="searchfield_dd_type">${field}_dd_type</s:set><s:set name="searchfield_dd_key">${field}_dd_key</s:set><s:set name="searchfield_lookupSearch">${field}_lookupSearch</s:set><s:set name="searchfield_dd">${field}_dd</s:set>
                            <s:set name="searchfield_dd_list">${field}_dd_list</s:set>  <%--Added by Delvene @ 29-Aug-2013--%>
                            <%--Edited by Delvene @ 27-Aug-2014 :: Change new date selection--%>
                            <s:if test='#field.startsWith("_date_")'>
                                <s:if test='#field.endsWith("_fromTo")'>
                                    <s:textfield cssClass="datepick-impian embed input-md" theme="simple" cssStyle="width: 130px; padding: 1px 3px 1px 3px;" id="%{#mcfAction}_%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/><s:text name="report_to"/>&nbsp;&nbsp;&nbsp;
                                    <s:textfield cssClass="datepick-impian embed input-md" theme="simple" cssStyle="width: 130px; padding: 1px 3px 1px 3px;" id="%{#mcfAction}_%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>
                                </s:if>
                                <s:else>
                                    <s:textfield cssClass="datepick-impian embed input-md" theme="simple" cssStyle="width: 322px" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                </s:else>
                            </s:if>
                            <%--Edited by Delvene @ 27-Aug-2014 :: Change new date selection - END--%>
                            <%--Edited by Delvene @ 27-Aug-2014 :: To support dropdown list in other MixedConfig--%>
                            <s:elseif test='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd_key) != null'>
                                <s:if test='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd_type) != null'>
                                    <s:checkboxlist theme="simple" name="search_%{#field}" list='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd)' listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_"+#field)}' />
                                </s:if>
                                <s:else>
                                    <s:if test='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd_list).equalsIgnoreCase("setupcode")'>    <%--Added by Delvene @ 29-Aug-2013--%>
                                        <s:select theme="simple" cssStyle="width: 322px" name="search_%{#field}" list='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd)' listKey="code_id" listValue="code_desc" value='%{searchFieldsDataMap.get("search_"+#field)}' />
                                    </s:if>
                                    <s:else>
                                        <s:select theme="simple" cssStyle="width: 322px" name="search_%{#field}" list='mixedConfig_map.get(#mcfAction+"searchFieldDD").get(#searchfield_dd)' listKey="keyData" listValue="valueData" value='%{searchFieldsDataMap.get("search_"+#field)}' />
                                    </s:else>
                                </s:else>
                            </s:elseif>
                            <%--Edited by Delvene @ 28-Aug-2014 :: To support dropdown list in other MixedConfig--%>
                            <s:elseif test='mixedConfig_map.get(#mcfAction+"searchFieldLookup").get(#searchfield_lookupSearch) != null'>
                                <s:textfield theme="simple" name="search_%{#field}" value='%{searchFieldsDataMap.get("search_"+#field)}' size="30" cssStyle="width: 292px; padding: 1px 3px 1px 3px;"/>
                                <script language="javascript">
                                    <s:property escape="false" value='%{mixedConfig_map.get(#mcfAction+"searchFieldLookup").get(#searchfield_lookupSearch)}'/>
                                </script>
                            </s:elseif>
                            <s:else>
                                <s:textfield theme="simple" name="search_%{#field}" value='%{searchFieldsDataMap.get("search_"+#field)}' size="30" cssStyle="width: 314px; padding: 1px 3px 1px 3px;"/>
                            </s:else>
                            ${searchFieldsHelperText[field]}
                        </td>
                    </tr>
                </s:iterator>
                </table>
            </td>
            <td valign="top" align="right">
                <table>
                    <tr valign="bottom">
                        <td>
                            <span style="padding-right: 1px;">
                                <input type="submit" onclick="return getPageSize('_mc',this.form);" value="<s:text name="button.search"/>" class="defaultButton"/>
                            </span>
                            <input type="button" value="<s:text name="button.reset"/>" class="defaultButton" onclick="resetFields(this.form)"/>
                        </td>
                    </tr>
                </table>
            </td>
        </form>
        </tr>
        </table>
                </div>
    </s:iterator>
                    <%--<tr valign="center">
                        <td width="20px">&nbsp;</td>
                        <td class="tdLabel">Please Select</td>
                        <td ><s:select theme="simple" cssStyle="width: 322px" name="searchCode" list='getSearchDDList("searchCode_dd")' listKey="keyData" listValue="valueData" value='' /></td>
                    </tr>--%>
    </s:if><s:else>
    <form action="search2Dynamic" id="search2DynamicFormId" method="POST">
        <table class="wwFormTable" width="100%">
        <tr>
            <td>
                <table>
                <s:iterator value="hiddenFields" var="hiddenField" status="hiddenRowStatus">
                <input type='hidden' <s:property escapeHtml="true" value="%{#hiddenField}"/> />
                </s:iterator>
                <s:hidden theme="simple" name="pageSize" id="searchFormPageSize"/>
                <s:hidden theme="simple" name="action" />
                <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
                <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
                <s:iterator value="searchFields" var="field" status="rowStatus">
                    <tr valign="center">
                        <td width="20px">&nbsp;</td>
                        <td class="tdLabelleft" width="200px;">${searchFieldsLabel[rowStatus.index]}</td>
                        <td ><s:set name="searchfield_dd_type">${field}_dd_type</s:set><s:set name="searchfield_dd_key">${field}_dd_key</s:set><s:set name="searchfield_lookupSearch">${field}_lookupSearch</s:set><s:set name="searchfield_dd">${field}_dd</s:set>
                            <s:set name="searchfield_dd_list">${field}_dd_list</s:set>  <%--Added by Delvene @ 29-Aug-2013--%>
                            <s:if test='#field.startsWith("_date_")'>
                                <s:if test='#field.endsWith("_fromTo")'>
                                    <s:textfield cssClass="datepick-impian embed input-md" theme="simple" cssStyle="width: 134px" id="%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}'/><s:text name="report_to"/>&nbsp;&nbsp;&nbsp;
                                    <s:textfield cssClass="datepick-impian embed input-md" theme="simple" cssStyle="width: 134px" id="%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}'/>
                                </s:if>
                                <s:else>
                                    <s:textfield cssClass="datepick-impian embed input-md" theme="simple" cssStyle="width: 322px" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                                </s:else>
                            </s:if>
                            <s:elseif test='getSearchFieldDD().get(#searchfield_dd_key) != null'>
                                <s:if test='getSearchFieldDD().get(#searchfield_dd_type) != null'>
                                    <s:checkboxlist theme="simple" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" />
                                </s:if>
                                <s:else>
                                    <s:if test="getSearchFieldDD().get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">    <%--Added by Delvene @ 29-Aug-2013--%>
                                        <s:select theme="simple" cssStyle="width: 322px" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="code_id" listValue="code_desc" value="%{searchFieldsData[#rowStatus.index]}" />
                                    </s:if>
                                    <s:else>
                                        <s:select theme="simple" cssStyle="width: 322px" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value="%{searchFieldsData[#rowStatus.index]}" />
                                    </s:else>
                                </s:else>
                            </s:elseif>
                            <s:elseif test='getSearchFieldLookup().get(#searchfield_lookupSearch) != null'>
                                <s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssStyle="width: 292px; padding: 1px 3px 1px 3px;"/>
                                <script language="javascript">
                                    <s:property escape="false" value="%{getSearchFieldLookup().get(#searchfield_lookupSearch)}"/>
                                </script>
                            </s:elseif>
                            <s:else>
                                <s:textfield theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}" size="30" cssStyle="width: 314px; padding: 1px 3px 1px 3px; %{getFieldStyleFormat(#field, 'searchField', '', '')}"/>
                            </s:else>
                            ${searchFieldsHelperText[field]}
                        </td>
                    </tr>
                </s:iterator>
                </table>
            </td>
            <td valign="top" align="right">
                <table>
                    <tr valign="bottom">
                        <td>
                            <span style="padding-right: 1px;"><input type="submit" onclick="return getPageSize('',this.form);" value="<s:text name="button.search"/>" class="defaultButton"/>
                            </span>
                            <input type="button" value="<s:text name="button.reset"/>" class="defaultButton" onclick="resetFields(this.form)"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        </table>
    </form>
    </s:else>
        <jsp:include page="actionError.jsp"></jsp:include>
        <form theme="simple" action="search2Dynamic" id="sortForm" method="POST">
           <s:hidden theme="simple" name="pageSize" value="%{pageSize}" />
           <s:hidden theme="simple" name="searchCode" value="%{searchCode}" />
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
    </td></tr>
<%--    <tr class="bgSearch"><td><jsp:include page="actionError.jsp"></jsp:include></td></tr>--%> <%--added by Delvene @ 09-Jul-2013--%>
    <%-- <tr class="bgSearch"><td >
    </td>
</tr>--%>
<!--  <tr style='height:10px' ><td>  some space</td></tr>-->
<!--<tr class="bgSearch" ><td><hr/></td></tr>-->
<!--<tr class="bgSearch" height="10px;"><td style="border-bottom: 1px solid #aaaaaa;vertical-align:bottom;"></td></tr>-->
     <s:if test='isMixConfig'>
        <form action="dynamicAction" method="POST">
        <tr >
            <td style="padding-top:20px;">
                <table id="mainTable" cellspacing="0" cellpadding="0" border="0" width="100%" >
                <tr>
                    <td id="vfasd">
                <s:hidden theme="simple" id="addFormSearchCode" name="searchCode" />
                <s:hidden theme="simple" name="action" />
            <s:iterator value="mcf_actionList" var="mcfAction" status="mcfActionStatus">
            <div id="${mcfAction}addDelete" >
                <s:if test='!("Y".equals(mixedConfig_map.get(#mcfAction+"hideAddButton"))) || !("Y".equals(mixedConfig_map.get(#mcfAction+"hideDeleteButton")))'>
                <s:if test='!"Y".equals(mixedConfig_map.get(#mcfAction+"hideAddButton"))'>
                    <s:if test="has_right2('DynamicAction','loadAddPage',#mcfAction)">
                        <s:submit theme="simple" id="addButton" action='%{mixedConfig_map.get(#mcfAction+"addPageURL")}' value="%{getText('button.add')}" cssClass="defaultButton" onclick="return getSearchCode();"/>
                    </s:if>
                </s:if>
                <s:if test='!"Y".equals(mixedConfig_map.get(#mcfAction+"hideDeleteButton"))'>
                    <s:if test="has_right2('DynamicAction','delete',#mcfAction)">
                        <s:submit theme="simple" id="deleteButton" action='%{mixedConfig_map.get(#mcfAction+"deleteURL")}' value="%{getText('button.delete')}" cssClass="defaultButton"
                          onclick="if ( isCheckboxSelected(document.getElementsByName('selected'))) {return confirmDelete();} else {return false};"/>
                    </s:if>
                </s:if>
                <%--<s:if test='hideDeleteButton.equals("N")'>
                    <s:if test="has_right('delete')">
                        <s:submit theme="simple" action="%{deleteURL}" value="%{getText('button.delete')}" cssClass="defaultButton"
                          onclick="if ( isCheckboxSelected(form.selected)) {return confirmDelete();} else {return false};"/>
                    </s:if>
                </s:if>--%>

                </s:if>
                </div>
        </s:iterator>
                    </td>
                    <td align="right">
                        <s:if test='showPageSize'>
                            <s:text name="paging.recordPerPage"/>:<s:select theme="simple" id="mainPageSize" name="mainPageSize" list="pageSizeOption" listKey="keyData" listValue="valueData" value="%{pageSize}"/>
                        </s:if>
                    </td>
                </tr>
                </table>
            </td>
        </tr>
        <tr align="left" >
            <td>
                <jsp:include page="${listPage}.jsp"></jsp:include>
            </td>
        </tr>
        </form>
    </s:if><s:else>
    <form action="dynamicAction" method="POST">
        <!--comment by sereneChye @ 2/10/2014:: to show showPageSize even though dun hv button tambah-->
        <%--<s:if test='hideAddButton.equals("N") || hideDeleteButton.equals("N")'>--%>
    <tr>
        <td style="padding-top:10px;">
            <table id="mainTable" cellspacing="0" cellpadding="0" border="0" width="100%" >
                <tr>
                    <td id="vfasd">
            <s:hidden theme="simple" name="action" />
            <s:if test='hideAddButton.equals("N")'>
                <s:if test="has_right('loadAddPage')">
                    <s:submit theme="simple" action="%{addPageURL}" value="%{getText('setupcode.button.add')}" cssClass="defaultButton" />
                </s:if>
            </s:if>
            <s:if test='hideDeleteButton.equals("N")'>
                <s:if test="has_right('delete')">
                    <s:submit theme="simple" action="%{deleteURL}" value="%{getText('button.delete')}" cssClass="defaultButton"
                      onclick="if ( isCheckboxSelected(form.selected)) {return confirmDelete();} else {return false};"/>
                </s:if>
            </s:if>
            </td>
            <td align="right">
                <s:if test='showPageSize'>
                    <s:text name="paging.recordPerPage"/>:<s:select theme="simple" id="mainPageSize" name="mainPageSize" list="pageSizeOption" listKey="keyData" listValue="valueData" value="%{pageSize}"/>
                </s:if>
            </td>
            </tr></table>
        </td>
    </tr>
    <%--</s:if>--%>
    <tr align="left" >
        <td>
            <jsp:include page="${listPage}.jsp"></jsp:include>
        </td>
    </tr>
    </form>
    </s:else>
    <%--<tr align="left" >
        <td>
            <jsp:include page="${listPage}.jsp"></jsp:include>
        </td>
    </tr>--%>
<s:if test='isMixConfig'>
    <script language="javascript">
        searchCodeChange(document.getElementById("mainSearchCode").value);
    </script>
</s:if>
</table>

