<!DOCTYPE html>  
<%@taglib uri="/struts-tags" prefix="s"%>

<html>
	<head>
		<script type="text/javascript" language="javascript" src="pages/scripts/common.js"></script>
		<script type="text/javascript" language="javascript" src="pages/scripts/validation.js"></script>
		<script type="text/javascript" language="javascript" src="pages/scripts/lookup.js"></script>
                <s:if test='usePopupCalander.equals("Y")'>
                    <script type="text/javascript" language="javascript" src="include/jquery_11.js"></script>
                    <jsp:include page="/include/jquery-datepicker/datepick.impian.jsp"></jsp:include>
                </s:if>
		<script language="javascript">
		<!--
                    document.onkeyup = Navi;
                    var navigating_ = true;
                    var escCnt= 0;
                    var currentRow = 0;
                    var previousRow = -1;
                    var previousClass;
                        parent.rowIdx = "<%= request.getParameter("rowIdx")%>";
			function updateOpener(value)
			{
				var lookFor = "<%= request.getParameter("lookFor")%>";
				var writeTo = "<%= request.getParameter("writeTo")%>";
				var focusOn = "<%= request.getParameter("focusOn")%>";
				var filter	= "<%= request.getParameter("filter")%>";
				var query	= "<%= request.getParameter("query")%>";
                                var parentFormId= "<%= request.getParameter("lookupParentFormId")%>";
				var lookForArray = lookFor.split(",");
				var writeToArray = writeTo.split(",");
				var filterArray = filter.split(",");


				for (var i = 0; i < writeToArray.length; i++)
				{
				    var lookupValue = lookForArray[i].trim();
					var retVal = "";
                                        var node = value.getElementsByTagName("td")[lookupValue];
                                        if (document.all) {
                                            for (tdIdx = 0; tdIdx < value.getElementsByTagName("td").length; tdIdx++) {
                                                if (value.getElementsByTagName("td").item(tdIdx).id == lookupValue) {
                                                    node = value.getElementsByTagName("td").item(tdIdx);
                                                    break;
                                                }
                                            }
                                        } else {
                                            node = value.getElementsByTagName("td")[lookupValue];
                                        }

					if (node)
					{
						if (document.all) {
							//node = node.childNodes[0];
                                                        node = node.getElementsByTagName("span")[0].innerHTML;
						} else {
                                                    node = node.getElementsByTagName("span")[0].innerHTML;
							<%--node = node.innerHTML;
							node = node.replace("<span>", "");
							node = node.replace("</span>", "");--%>
						}
					}
					else {
						//node = value.getElementsByTagName("LABEL")[0];
						if (value.getElementsByTagName("LABEL")[0]){
							node = value.getElementsByTagName("LABEL")[0];
						} else {
							if (document.all) {
								//node = value.getElementsByTagName("SPAN")[lookupValue];
                                                                for (tdIdx = 0; tdIdx < value.getElementsByTagName("INPUT").length; tdIdx++) {
                                                                    if (value.getElementsByTagName("INPUT").item(tdIdx).id == lookupValue) {
                                                                        node = value.getElementsByTagName("INPUT").item(tdIdx);
                                                                        break;
                                                                    }
                                                                }
							} else {
								node = value.getElementsByTagName("INPUT")[lookupValue];
							}
							if (node){
								if (document.all) {
									//node = node.childNodes[0];
                                                                        node = node.value;
								} else {
									node = node.value;
								}
							}
						}
					}
                                        if (node.value) {
                                            node.value = node.value.replace("&amp;", "&").replace("%26", "&");
                                            retVal = node.value
                                        } else if (node) {
                                            node = node.replace("&amp;", "&").replace("%26", "&");
                                            retVal = node;
                                        }
					//retVal = (document.all)? (node.value? node.value : node.innerText) : node;
					//retVal = (document.all)? (node.value? node.value : node.innerHtml) : node.nextSibling.value;
					// Not supported
					/*
					if (window.showModalDialog)
					{
						dialogArguments.document.forms[0]["<%= request.getParameter("writeTo")%>"].value = retVal;
					}
					else
					*/
					{
					  var writeToValue = writeToArray[i].trim();
					  <%--if (window.parent.document.forms[0][writeToValue])
					  {
					  	window.parent.document.forms[0][writeToValue].value = retVal.trim();
					  }--%>
                                          if (window.parent.document.getElementById(parentFormId)[writeToValue])
					  {
					  	window.parent.document.getElementById(parentFormId)[writeToValue].value = retVal.trim();
					  }
					  else
					  {
					  	var labels = window.parent.document.getElementsByTagName("label");

				        for (var j = 0; j < labels.length; j++)
        				{
				          if (labels[j].htmlFor == writeToValue)
				          {
					          if (document.all) {
						          labels[j].innerText = retVal;
					          } else {
				          		  labels[j].innerHTML = retVal;
					          }
				          	break;
				          }
        				}
					  }
		  			}
		  		}

				if (focusOn != "null" && focusOn != "")
				{
					<%--if (window.parent.document.forms[0][focusOn]){
						window.parent.document.forms[0][focusOn].focus();--%>
                                        if (window.parent.document.getElementById(parentFormId)[focusOn]){
                                            window.parent.document.getElementById(parentFormId)[focusOn].focus();
					} else {
						var textFields = window.parent.document.getElementsByTagName("text");
				        for (var j = 0; j < textFields.length; j++) {
					        if (textFields[j].htmlFor == writeToValue) {
					        	textFields[j].focus();
					          	break;
					        }
	      				}
					}
				}

	  			//window.close();
	  			parent.lookup_dtmlwindow.hide();
				//window.parent.close("<%= request.getParameter("query")%>");
			}

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

                        function focusResult(code){
                            if (!navigating_) {
                                return;
                            }
                            var maxRow = 0;
                            <s:if test="pageSize == null">
                                for (trRowIdx = 9; trRowIdx >=0; trRowIdx--) {
                            </s:if><s:else>
                                for (trRowIdx = ${pageSize}; trRowIdx >=0; trRowIdx--) {
                            </s:else>
                                if (document.getElementById("resultTR_"+trRowIdx)) {
                                    maxRow = trRowIdx;
                                    break;
                                }
                            }
                            if (code == 0) {
                                document.getElementById("resultTR_"+previousRow).className = previousClass;
                                document.getElementById("<s:property value="firstSearchFieldName"/>").focus();
                                return;
                            }
                            if (previousRow == -1) {
                                previousRow = 0;
                            } else if (code == 38) {//up
                                if (previousRow <= 0) {
                                    currentRow = maxRow;
                                } else {
                                    currentRow = previousRow - 1;
                                }
                            } else if (code == 40) {
                                if (previousRow >= maxRow) {
                                    currentRow = 0;
                                } else {
                                    currentRow = previousRow + 1;
                                }
                            }
                            var trClassName = document.getElementById("resultTR_"+currentRow).className;
                            if (previousRow != currentRow) {
                                document.getElementById("resultTR_"+previousRow).className = previousClass;
                                previousRow = currentRow;
                            }
                            previousClass = trClassName;
                            document.getElementById("resultTR_"+currentRow).className = document.getElementById("resultTR_"+currentRow).className + "Selected";
                        }

                        function clearNavi(){
//                            escCnt = 0;
                            focusResult(0);
                            navigating_ = false;
                        }

                        function Navi(e){
                            var evtobj=window.event? event : e; //distinguish between IE's explicit event object (window.event) and Firefox's implicit.
                            var unicode=evtobj.charCode? evtobj.charCode : evtobj.keyCode;
                            if (unicode == 13 && navigating_) {
                                updateOpener(document.getElementById("resultTR_"+currentRow));
                            } else if (unicode == 27) {
                                if (++escCnt > 1) {
                                    parent.lookup_dtmlwindow.hide();
                                }
                                if (escCnt == 1) {
                                    clearNavi();
                                }
                            } else if (unicode == 38 || unicode == 40) {
                                escCnt = 0;
                                if (!navigating_) {
                                    focusResult(0);
                                } else {
                                    focusResult(unicode);
                                }
                            } else if (unicode == 33 || unicode == 34 || unicode == 35 || unicode == 36) {
                                escCnt = 0;
                                if (navigating_) {
                                    var navHref;
                                    if (unicode == 33) {
                                        navHref = document.getElementById("ppId");
                                    } else if (unicode == 34) {
                                        navHref = document.getElementById("npId");
                                    } else if (unicode == 35) {
                                        navHref = document.getElementById("lpId");
                                    } else if (unicode == 36) {
                                        navHref = document.getElementById("fpId");
                                    }
                                    if (navHref) {
                                        navHref.click();
                                    }
                                }
                            } else {
                                escCnt = 0;
                            }
                        }
			//-->
                        <s:if test='usePopupCalander.equals("Y")'>
                            $( document ).ready(function() {
                                initDatePicker();
                                <s:iterator value="searchFields_with_dateFromTo" var="popCalField" status="popCalFieldStatus">
                                    $('#${popCalField}From').datepick('option', {onSelect: function(dateText, instance){prepareEndDate(dateText, '${popCalField}')}});
                                </s:iterator>
                            });
                            <s:if test="searchFields_with_dateFromTo != null && searchFields_with_dateFromTo.size > 0">
                            function prepareEndDate(date, dateTo_id) {
                                $('#'+dateTo_id+'To').datepick('setDate', date);
                                $('#'+dateTo_id+'To').datepick('option', {minDate: $('#'+dateTo_id+'From').val()}).focus();
                            }
                            </s:if>

                    //        InitCalendar2("images/",false);
                        </s:if>
		</script>
	</head>

        <body bgcolor="white">
	<%
		int col = 0;
		int tabelCol = 0;
		String result = request.getQueryString();
		if ((result != null) && (result != "")) {
			String[] params = result.split("\\&");
			result = "";
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					// ignore pageNo, criteria
					if (!(params[i].indexOf("pageNo")>=0
						|| params[i].toUpperCase().indexOf("CRITERIAS")>=0)) {
						result+=(params[i]+"&");
					}
				}
			}
  		}
	%>

      <table class="form" border="0" cellpadding="1" cellspacing="1" width="100%">
          <form action="searchLookup" method="post">
          <s:hidden name="rowIdx" />
	  <s:hidden name="lookup" />
	  <s:hidden name="query" />
	  <s:hidden name="lookFor" />
	  <s:hidden name="writeTo" />
          <s:hidden name="filterBy"/>
          <s:hidden name="filter" />
	  <s:hidden name="displayedColumns" />
	  <s:hidden name="focusOn" />
	  <s:hidden name="retrieveOnLoad" />
          <s:hidden name="lookupParentFormId" />
          <s:hidden name="teaC_" />
          <s:hidden name="LAN_" />
          <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
          <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
        <!-- if lookup type is not specified, do not show the search criteria and the buttons -->
            <s:if test="!hideSearchScreen">
 		<% if (request.getParameter("lookup") != null && request.getParameter("lookup").indexOf("useSetup_") < 0) {%>
                <tr><td height="1">
	        	<jsp:include page='lookup_${lookup}.jsp'/>
	        </td></tr>
                <tr><td height="1">
                  <%@include file="button.jsp"%>
                </td></tr>
                <tr><td><hr/></td></tr>
                <% } else {%>
                <tr><td height="1">
	        	<jsp:include page='lookup_dynamic.jsp'/>
	        </td></tr>
                <tr><td height="1">
                  <%@include file="button.jsp"%>
                </td></tr>
                <tr><td height="1"><hr/></td></tr>
		<% } %>
            </s:if>
                        <tr><td height="1">
          <table class="defaultTable" border="0" cellpadding="1" cellspacing="1" width="100%" class="list">

          	<!-- display the columns -->
			  <tr class="CLASS_TABLE_HEADER" width="2%">
			  	<s:if test="result.size() > 0">

            	</s:if>
            	<th>
              		No.
            	</th>
            	<s:iterator value="goodDisplayingColumns" status="columnStatus" var="column">
                    <s:set var="disp" value="displayingColumns[#columnStatus.index]"/>
     				<th style="<s:property value='%{getFieldStyleFormat(displayingColumns[columnStatus.index])}'/>"><%tabelCol++;%>
                                    <s:if test="isSortingField(#disp) == true"><a class="thLink" href="javascript:sortField('${disp}')"><s:property value="%{#column}"/>
                                            <s:if test="(dynamicSortBy == #disp) && (result.size() > 0)">
                                                <s:if test='dynamicSortOrder == "D"'>
                                                    <img alt="sort_decending" src='images/sortdes.gif' border='0'/>
                                                </s:if>
                                                <s:else>
                                                    <img alt="sort_ascending" src='images/sortasc.gif' border='0'/>
                                                </s:else>
                                            </s:if>
                                        </a></s:if>
                                    <s:else><s:property value="%{#column}"/></s:else>
      				</th>
				</s:iterator>
			  </tr>

			  <!-- display the data -->
			  <%--<s:if test="columns.size() > 0">--%>
			  <s:if test="result.size() > 0">
			  	<s:set var="rowIndex">${(pageNo-1)*pageSize+1}</s:set>
			  	<s:iterator value="result" status="resultStatus" var="r">
                                    <tr style="cursor: pointer" class="<s:if test="#resultStatus.odd == true ">lk_odd</s:if><s:else>lk_even</s:else>" id="resultTR_${resultStatus.index}" onclick="updateOpener(this)">
	       				<td width="1%">${rowIndex + resultStatus.index}</td>
	       				<s:iterator value="displayingColumns" var="displayCol">
                                            <%--<s:if test="#r.containsKey(#displayCol)">
                                                <td id="${displayCol}" width="250">
                                                    <span>
                                                        <s:if test='getCheckFormatField(#displayCol).equals("true")'>
                                                        <s:property value="%{getFormattedField(#displayCol, #resultStatus.index)}"/>
                                                        </s:if><s:else>
                                                        <s:property value="%{#r.get(#displayCol)}"/></s:else>
                                                    </span>
                                                    <s:iterator value="hiddenColumns" var="hiddenCol">
                                                        <s:if test='getCheckFormatField(#displayCol).equals("true")'>
                                                            <input type="hidden" name="${hiddenCol}" id="${hiddenCol}" value="<s:property value="%{getFormattedField(#displayCol, #resultStatus.index)}"/>"/>
                                                        </s:if><s:else>
                                                            <input type="hidden" name="${hiddenCol}" id="${hiddenCol}" value="<s:property value="%{getHiddenValue(#resultStatus.index, #hiddenCol)}"/>"/>
                                                        </s:else>
                                                    </s:iterator>
                                                </td>
                                            </s:if>--%>
                                            <s:iterator value="r" var="resultMap">
                                                <s:if test="#resultMap.key.equals(#displayCol)">
                                                <td id="${resultMap.key}" width="250">
                                                    <span>
                                                        <s:if test='getCheckFormatField(#displayCol).equals("true")'>
                                                        <s:property value="%{getFormattedField(#displayCol, #resultStatus.index)}"/>
                                                        </s:if><s:else>
                                                        <s:property value="%{#r.get(#displayCol)}"/></s:else>
                                                    </span>
                                                    <s:iterator value="hiddenColumns" var="hiddenCol">
                                                        <s:if test='getCheckFormatField(#displayCol).equals("true")'>
                                                            <input type="hidden" name="${hiddenCol}" id="${hiddenCol}" value="<s:property value="%{getFormattedField(#displayCol, #resultStatus.index)}"/>"/>
                                                        </s:if><s:else>
                                                            <input type="hidden" name="${hiddenCol}" id="${hiddenCol}" value="<s:property value="%{getHiddenValue(#resultStatus.index, #hiddenCol)}"/>"/>
                                                        </s:else>
                                                    </s:iterator>
                                                </td>
                                                </s:if>
                                            </s:iterator>
                                        </s:iterator> 

	                </tr>
              	</s:iterator>
			  </s:if>
            <%--</s:if>--%>
            <%-- if no results --%>
            <s:if test="result.size() <= 0 && searched">
            	<tr><td class="remark" colspan="<%=tabelCol + 1%>"><s:text name="common.noRecordFound" /></td></tr>
			</s:if>
			<%-- pagination --%>
			<s:if test="result.size() > 0 && searched">
			  <tr><td colspan="<%=tabelCol + 1%>">
				<jsp:include page="../pagination/paging.jsp"></jsp:include>
			  </td></tr>
			</s:if>
		  </table>
        </td></tr>
                        </form>
	</table>

        <form action="searchLookup" id="sortForm" name="sortForm" method="post">
            <s:hidden theme="simple" name="dynamicSortBy" value="%{dynamicSortBy}" />
            <s:hidden theme="simple" name="dynamicSortOrder" value="%{dynamicSortOrder}" />
            <s:hidden theme="simple" name="pageSize" value="%{pageSize}" />
            <s:hidden theme="simple" name="action" />

            <s:hidden name="rowIdx" />
            <s:hidden name="lookup" />
            <s:hidden name="query" />
            <s:hidden name="lookFor" />
            <s:hidden name="writeTo" />
            <s:hidden name="filterBy"/>
            <s:hidden name="filter" />
            <s:hidden name="displayedColumns" />
            <s:hidden name="focusOn" />
            <s:hidden name="retrieveOnLoad" />
            <s:hidden name="lookupParentFormId" />

            <s:if test="result.size() > 0">
                <s:hidden theme="simple" name="listSize" value="1"/>
            </s:if>
            <s:else>
                <s:hidden theme="simple" name="listSize" value="0"/>
            </s:else>
            <s:iterator value="searchFields" var="field" status="rowStatus">
                <s:hidden theme="simple" name="search_%{#field}" value="%{searchFieldsData[#rowStatus.index]}"/>
            </s:iterator>
        </form>
            <s:if test="result.size() > 0">
                <script language="javascript">
                    focusResult();
                </script>
            </s:if><s:else>
                <script language="javascript">
                    document.getElementById("<s:property value="firstSearchFieldName"/>").focus();
                </script>
            </s:else>
	</body>
</html>