<%@taglib uri="/struts-tags" prefix="s"%>
    <s:iterator value="searchFields" var="field" status="rowStatus">
        <s:if test="(#rowStatus.index % 2) == 0">
            <div class="row form-row-margin">
        </s:if>
            <div class="col-md-2 fieldLabel"><s:property value="%{searchFieldsLabel[#rowStatus.index]}"/></div>
            <div class="col-md-3">
                <s:set var="searchfield_dd_key">${field}_dd_key</s:set><s:set var="searchfield_lookupSearch">${field}_lookupSearch</s:set><s:set var="searchfield_dd">${field}_dd</s:set>
                <s:set var="searchfield_dd_list">${field}_dd_list</s:set>  <%--Added by Delvene @ 29-Aug-2013--%>
                <s:set var="searchfield_dd_type">${field}_dd_type</s:set>  <%--Added by Delvene @ 21-Apr-2014--%>
                <s:if test='#field.startsWith("_date_")'>
                    <s:if test='#field.endsWith("_fromTo")'>
                        <div class="form-group">
                            <div class="input-daterange input-group" id="datepicker-range">
                                <s:textfield theme="simple" cssClass="input-sm form-control bootstrapDatePicker" id="%{#field.substring(6, (#field.length() - 7))}From" name="search_%{#field.substring(6, (#field.length() - 7))}From" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"From")}' type="text"/>
                                <div class="input-group-addon">to</div>
                                <s:textfield theme="simple" cssClass="input-sm form-control bootstrapDatePicker" id="%{#field.substring(6, (#field.length() - 7))}To" name="search_%{#field.substring(6, (#field.length() - 7))}To" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field)+"To")}' type="text"/>
                            </div>
                        </div>
                    </s:if>
                    <s:else>
                        <div class="has-feedback">
                            <s:textfield cssClass="form-control bootstrapDatePicker" theme="simple" id="search_%{#field.substring(6)}" name="search_%{#field.substring(6)}" value='%{searchFieldsDateData.get("search_"+extractSearchFieldForDate(#field))}'/>
                            <i class="fa fa-calendar form-control-feedback"></i>    
                        </div>
                    </s:else>
                </s:if>
                <s:elseif test='getSearchFieldDD().get(#searchfield_dd_key) != null'>
                    <s:if test='getSearchFieldDD().get(#searchfield_dd_type) != null'>  <%--Added by Delvene @ 21-Apr-2014--%>
                        <div class="checkbox checkbox-inline check-success">
                            <s:checkboxlist theme="simple" id="search_%{#field}" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{getData(#field)}' />
                        </div>
                    </s:if>
                    <s:else>
                        <s:if test="getSearchFieldDD().get(#searchfield_dd_list).equalsIgnoreCase('setupcode')">    <%--Added by Delvene @ 29-Aug-2013--%>
                            <s:select cssClass="full-width form-control" theme="simple" id="search_%{#field}" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="code_id" listValue="code_desc" value='%{getData(#field)}' />
                        </s:if>
                        <s:else>
                            <s:select cssClass="full-width form-control" theme="simple" id="search_%{#field}" name="search_%{#field}" list="getSearchDDList(#searchfield_dd)" listKey="keyData" listValue="valueData" value='%{getData(#field)}' />
                    </s:else>
                    </s:else>
                </s:elseif>
                <s:else>
                    <s:textfield cssClass="form-control" theme="simple" id="search_%{#field}" name="search_%{#field}" value="%{getData(#field)}"/>
                </s:else>
            </div>
            <div class="col-md-1"/>
        <s:if test="(#rowStatus.index % 2) == 1">
            </div>
        </s:if><s:else>
            <s:if test="searchFields.size == #rowStatus.index+1 && searchFields.size % 2 == 0">
                </div>
            </s:if>
        </s:else>
    </s:iterator>
    <s:if test='showPageSize'>
        <s:if test="searchFields.size % 2 == 0">
            <div class="row form-row-margin">
        </s:if>
            <div class="col-md-2 fieldLabel"><s:text name="paging.recordPerPage"/></div>
            <div class="col-md-3">
                <s:select cssClass="full-width form-control" theme="simple" id="pageSize" name="pageSize" list="pageSizeOption" listKey="keyData" listValue="valueData" value="%{pageSize}"/>
            </div>
            <div class="col-md-1"/>
        </div>
    </s:if>
    <jsp:include page="/pages/base/actionError.jsp"></jsp:include>