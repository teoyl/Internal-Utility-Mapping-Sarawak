<%@taglib uri="/struts-tags" prefix="s"%>

<%int tableCol = 0;%>
<div class="table-responsive scrollbar">
    <table <s:if test="isGenerateDynamicRpt">id="dynamicListTable"</s:if><s:elseif test="useDataTable">id="dataTableId"</s:elseif> class="table table-sds table-sm fs--1 table-striped table-hover overflow-hidden <s:if test="useDataTable">data-table</s:if>" width="100%">
        <thead class="text-900">
            <!-- display the columns -->
            <tr class="bottom-green-2">
                <s:if test="showNumbering">
                    <th width="1%" class="align-middle white-space-nowrap">
                        No.
                    </th>
                </s:if>
                <%tableCol = 0;%>
                <s:iterator value="displayFieldsHeader" status="columnStatus" var="column">
                    <%tableCol++;%>
                    <s:set var="disp" value="displayFields[#columnStatus.index]"/>
                    <th class="align-middle white-space-nowrap <s:if test="useDataTable && !(isSortingField(#disp))">no-sort</s:if><s:else>sort</s:else>" style="<s:property value='%{getFieldStyleFormat(#disp, "header", "", #column)}' escapeHtml="true"/>" >
                        <s:if test="isSortingField(#disp)">
                            <s:if test="!(useDataTable)">
                                <a class="thLink text-black" href="javascript:sortField('${disp}')"><s:property value="%{#column}" escapeHtml="true"/></a>
                                <s:if test="(dynamicSortBy == #disp) && (result.size() > 0)">
                                    <s:if test='dynamicSortOrder == "D"'>
                                        <Img src='images/sortdes.gif' border='0'/>
                                    </s:if>
                                    <s:else>
                                        <Img src='images/sortasc.gif' border='0'/>
                                    </s:else>
                                </s:if><s:else>
                                    &nbsp;<Img alt="sorting" src='images/sortingIcon.gif' border='0'/>
                                </s:else>
                            </s:if><s:else>
                                <s:property value="%{#column}" escapeHtml="true"/>&nbsp;
                            </s:else>
                        </s:if>
                        <s:else>
                            <%--<s:property value="%{#column}"/>--%>
                            <%--to enable sorting for custom sorting field at dynamic config - ahmadni  @ 14-Feb-2017 --%>
                            <s:if test="!(useDataTable)">
                                <a class="thLink" href="javascript:sortField('${disp}')"><s:property value="%{#column}" escapeHtml="true"/></a>
                                <s:if test="(dynamicSortBy == #disp) && (result.size() > 0)">
                                    <s:if test='dynamicSortOrder == "D"'>
                                        <Img src='images/sortdes.gif' border='0'/>
                                    </s:if>
                                    <s:else>
                                        <Img src='images/sortasc.gif' border='0'/>
                                    </s:else>
                                </s:if><s:else>
                                    &nbsp;<Img alt="sorting" src='images/sortingIcon.gif' border='0'/>
                                </s:else>
                            </s:if><s:else>
                                <s:property value="%{#column}" escapeHtml="true"/>
                           </s:else>
                        </s:else>
                    </th>
                </s:iterator>
                <s:if test="moreInfoType!=null">
                    <%tableCol++;%>
                    <th style="text-align: center; width: 1%">
                        <span>Job History</span>
                    </th>
                </s:if>
                <s:if test="customHeaderEnd != null">
                    <jsp:include page="${customHeaderEnd}"/>
                    <% tableCol = tableCol + (Integer)request.getAttribute("thCol");%>
                </s:if>
            </tr>
        </thead>

        <tbody>
            <!-- display the data -->
            <%--<s:if test="columns.size() > 0">--%>
            <s:if test="result.size() > 0">
                <%
                    String[] cssClass = {"value", "valueB"};
                    String[] cssCenterClass = {"valueCenter", "valueCenterB"};
                    String pageNo = request.getParameter("pageNo");
                    if (com.sains.common.util.Validator.isEmpty(pageNo)) {
                        pageNo = "1";
                    }
                    int row = 0, firstIndex = (Integer.parseInt(pageNo) - 1) * 10 + 1;
                %>
                <s:iterator value="result" status="resultStatus" var="r">
                    <tr class="bottom-green-1">
                        <input type="hidden" name="dcRecordIds" id="recordIds_${resultStatus.index}" value="<s:property value="%{getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>">
                        <s:if test="showNumbering">
                            <td class="white-space-nowrap text-middle align-middle <s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" width="1%">
                                ${ ((pageNo-1)*pageSize) + resultStatus.index+1}.
                            </td>
                        </s:if>
                        <s:iterator value="displayFields" var="displayCol">
                            <td class="align-middle" id="${displayCol}" style="word-wrap: break-word; <s:property value='%{getFieldStyleFormat(#displayCol, "detail", #r.get(#displayCol), #r)}' escapeHtml="true"/>">
                                
                                <s:if test='getCheckEditLink(#displayCol).equals("true")'>
                                    <s:set var="theTarget" value="%{getOpenLinkAtNewPage(#displayCol)}"/>
                                    <s:if test='isMixConfig'>
                                        <s:url var="editLink" action="%{editPageURL}">
                                            <s:param name="id" value="%{getResultPrimaryKey(#resultStatus.index)}"></s:param>
                                            <s:param name="action" value="%{action}"></s:param>
                                            <s:param name="searchCode" value="%{searchCode}"></s:param>
                                        </s:url>
                                    </s:if>
                                    <s:else>
                                        <s:url var="editLink" action="%{editPageURL}">
                                            <s:param name="id" value="%{getResultPrimaryKey(#resultStatus.index)}"></s:param>
                                            <s:param name="action" value="%{action}"></s:param>
                                        </s:url>
                                    </s:else>
                                    <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                        <s:a href="%{editLink}" cssStyle="%{editLinkStyle}" target="%{theTarget}">
                                            <div style="width: 100%"><s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text></div>
                                        </s:a>
                                    </s:if>
                                    <s:else>
                                        <s:a href="%{editLink}" cssStyle="%{editLinkStyle}" target="%{theTarget}"><div style="width: 100%"><s:property value="%{#r.get(#displayCol)}"/></div></s:a>
                                    </s:else>
                                </s:if>
                                <s:elseif test='getCheckEditLink(#displayCol+"_otherLink").equals("true")'>
                                    <s:set var="theTarget" value="%{getOpenLinkAtNewPage(#displayCol)}"/>
                                    <s:url var="displayFieldLink" action='%{getDisplayFieldLink(#displayCol+"_otherLink", #r)}'></s:url>
                                    <s:set var="displayFieldLink_btn" value='%{getDisplayFieldLink(#displayCol+"_otherLink", #r)}'/>
                                    <s:set var="lDisplayStyle" value='%{getDisplayFieldLink(#displayCol+"_style", #r)}'/>
                                    <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                        <s:a href="%{displayFieldLink}" cssStyle="%{lDisplayStyle}" target="%{theTarget}">
                                            <div style="width: 100%">
                                                <s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                                </div>
                                        </s:a>
                                    </s:if>
                                    <s:elseif test='#displayFieldLink_btn.contains("@@")'>
                                        <s:if test='#displayFieldLink_btn.split("@@")[1].equalsIgnoreCase("button")'>
                                            <button class="btn btn-sm btn-falcon-default" onclick="submitDcForm('<s:property value="%{getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>', '<s:property value='#displayFieldLink_btn.split("@@")[0]'/>'); return false;"><s:text name="%{#r.get(#displayCol)}"/></button>
                                        </s:if><s:elseif test='#displayFieldLink_btn.split("@@")[1].equalsIgnoreCase("input")'>
                                            <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                                <s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                            </s:if><s:elseif test="#r.get(#displayCol) instanceof java.util.Date">
                                                <input type="text" class="form-control Date_Picker" name="${displayCol}<s:property value="%{getResultPrimaryKey(#resultStatus.index)}"/>" value="<s:text name="date_default_date"><s:param value="%{#r.get(#displayCol)}" /></s:text>">
                                            </s:elseif><s:else>
                                                <input type="text" class="form-control" name="${displayCol}<s:property value="%{getResultPrimaryKey(#resultStatus.index)}"/>" escapeHtml="true"/>" value="<s:property value="%{#r.get(#displayCol)}" escapeHtml="true"/>">
                                            </s:else>
                                        </s:elseif><s:elseif test='#displayFieldLink_btn.split("@@")[1].equalsIgnoreCase("textarea")'>
                                            <div style="max-height: 100%" id="${displayCol}<s:property value="%{getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>_div">
                                                <textarea style="min-height:100px;" class="taCount form-control form-control-sm" maxlength="<s:property value='#displayFieldLink_btn.split("@@")[2]'/>" name="${displayCol}<s:property value="%{getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>" id="${displayCol}<s:property value="%{getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>"><s:property value="%{#r.get(#displayCol)}" escapeHtml="true"/></textarea>
                                            </div>
                                        </s:elseif><s:elseif test='#displayFieldLink_btn.split("@@")[1].equalsIgnoreCase("checkbox")'>
                                            <div class="col-md-5 checkbox right check-success">
                                                <input type="checkbox" value="Y" name="${displayCol}<s:property value="%{getResultPrimaryKey(#resultStatus.index)}"/>" id="checkbox${resultStatus.index}" <s:if test='%{#r.get(#displayCol).equals("Y") || #r.get(#displayCol).equals("A")}'>checked</s:if>>
                                                <label for="checkbox${resultStatus.index}"></label>
                                            </div>
                                        </s:elseif><s:elseif test='#displayFieldLink_btn.split("@@")[1].equalsIgnoreCase("select")'>
                                            <s:property value="%{populateDD(#displayFieldLink_btn)}"/>
                                            <s:select theme="simple" cssClass="dsrfdd rowText form-control sds-dropdown mySelectBox input-sm" name="%{#displayCol+getResultPrimaryKey(#resultStatus.index)}" id="%{#displayCol+getResultPrimaryKey(#resultStatus.index)}" list="getSearchDDList(#displayCol)" listKey="keyData" listValue="valueData" value='%{#r.get(#displayCol)}' />
                                        </s:elseif><s:elseif test='#displayFieldLink_btn.split("@@")[1].equalsIgnoreCase("radio")'>
                                            <s:property value="%{populateDD(#displayFieldLink_btn)}"/>
                                            <div class="radio radio-inline radio-success">
                                                <s:radio theme="simple" list="getSearchDDList(#displayCol)" listValue="valueData" name="%{#displayCol+getResultPrimaryKey(#resultStatus.index)}" label="" value='%{#r.get(#displayCol)}'/>
                                            </div>
                                            <%--<s:radio theme="simple" cssClass="dsrfdd rowText form-control sds-dropdown mySelectBox input-sm" name="%{#displayCol+getResultPrimaryKey(#resultStatus.index)}" id="%{#displayCol+getResultPrimaryKey(#resultStatus.index)}" list="getSearchDDList(#displayCol)" listKey="keyData" listValue="valueData" value='%{#r.get(#displayCol)}' />--%>
                                        </s:elseif>
                                    </s:elseif><s:else>
                                        <s:if test="!(#r.get(#displayCol) == null)">
                                            <s:if test='(#r.containsKey("errId"))'>
                                                <s:if test='!(#r.get("errId") == null || #r.get("errId").equals(""))'>
                                                    <s:a href="%{displayFieldLink}" cssStyle="%{lDisplayStyle}" target="%{theTarget}"><div style="width: 100%"><s:text name="%{#r.get(#displayCol)}" escapeHtml="true"/></div></s:a>
                                                </s:if><s:else>
                                                    <s:property escapeHtml="true" value='%{#r.get("errId")}'/>
                                                    <s:property escapeHtml="true" value="%{#r.get(#displayCol)}"/>
                                                </s:else>
                                            </s:if><s:else>
                                                <s:a href="%{displayFieldLink}" cssStyle="%{lDisplayStyle}" target="%{theTarget}"><div style="width: 100%"><s:text name="%{#r.get(#displayCol)}" escapeHtml="true"/></div></s:a>
                                            </s:else>
                                        </s:if>
                                    </s:else>
                                </s:elseif>
                                <s:else>
                                    <s:if test='getCheckFormatField(#displayCol).equals("true")'>
                                        <s:property escapeHtml="true" value="%{getFormattedField(#displayCol, #resultStatus.index)}"/>
                                    </s:if>
                                    <s:else>
                                        <s:if test="#r.get(#displayCol) instanceof java.sql.Timestamp">
                                            <s:text name="date_default_datetime"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                        </s:if><s:elseif test="#r.get(#displayCol) instanceof java.util.Date">
                                            <s:text name="date_default_date"><s:param value="%{#r.get(#displayCol)}" /></s:text>
                                        </s:elseif>
                                        <s:else>
                                            <s:if test='#displayCol.equals("err_msg")'>
                                                <div style="white-space:pre; max-width:500px; max-height:200px; overflow:auto"><s:property escapeHtml="true" value="%{#r.get(#displayCol)}"/></div>
                                            </s:if>
                                            <s:else>
                                                <s:property escapeHtml="false" value="%{#r.get(#displayCol)}"/>
                                            </s:else>
                                        </s:else>
                                    </s:else>
                                </s:else>
                            </td>
                        </s:iterator>
                        <s:iterator value="hiddenColumns" var="hiddenCol">
                            <span id="${hiddenCol}"><input type="hidden"  value="%{#r.get(#hiddenCol)}" /></span>
                        </s:iterator>
                        <s:if test="moreInfoType!=null">
                            <td class="text-middle align-middle ">
                                <!--<button title="<s:text name="button.viewMoreInfo"/>" class="btn btn-sm btn-falcon-default btn-icon-only" onclick="openMoreInfo('<s:property value="action"/>', '<s:property value="%{getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>'); return false;">-->
                                <button title="<s:text name="button.viewMoreInfo"/>" class="no-bg-btn word-green" onclick="openMoreInfo('Submission', '<s:property value="%{getResultPrimaryKey(#resultStatus.index)}" escapeHtml="true"/>'); return false;">
                                
                                    <i class="far fa-clock word-green"></i>
                                </button>
                            </td>
                        </s:if>
                        <s:if test="customListEnd != null">
                            <jsp:include page="${customListEnd}"/>
                        </s:if>
                    </tr>
                </s:iterator>
            </s:if>
            <s:else>
                <tr><td class="remark" colspan="<%= tableCol + 1%>"><center><s:text name="common.noRecordFound" /></center></td></tr>
            </s:else>
        </tbody>
    </table>
</div>

<%-- pagination --%>
<s:if test="paging && result.size() > 0 && searched && !(useDataTable)">
    <jsp:include page="../pagination/b5_paging.jsp"></jsp:include>
</s:if>
