<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%--<s:head />--%>
<style type="text/css">
@import url(style.css);
</style>
</head>
<body>
	<%int tabelCol = 0;%>
	<table border="0" cellpadding="1" cellspacing="1" width="100%" class="list">
          	<!-- display the columns -->
			  <tr class="CLASS_TABLE_HEADER" width="2%">
            	<th>
            		<s:if test="result.size() > 0">
              			<input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, ids);">
              		</s:if>
              		<s:else>
              			<input type="checkbox" id="cbselect" name="cbselect" disabled >
              		</s:else>
            	</th>
            	<s:iterator value="displayFieldsHeader" status="columnStatus" var="column">
     				<th>
     					${column}
      				</th>
				</s:iterator>
			  </tr>
              
			  <!-- display the data -->
			  <s:if test="columns.size() > 0">
			  <s:if test="result.size() > 0">
			  	<%
			  		String[] cssClass = {"value", "valueB"};
			  				String[] cssCenterClass = {"valueCenter", "valueCenterB"};
			  				String pageNo = request.getParameter("pageNo");
			  				if (com.sains.common.util.Validator.isEmpty(pageNo)) {
			  					pageNo = "1";
			  				}
			  				int row = 0, firstIndex = (Integer.parseInt(pageNo)-1)*10+1;
			  	%>
			  	<s:iterator value="result" status="resultStatus" id="r">
			  		<tr><%tabelCol = 0;%>
	       				<td class="<s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" width="1%">
	       					<s:checkbox theme="simple" name="delete" id="ids" fieldValue="%{getResultPrimaryKey(#resultStatus.index)}" onclick="checkToggleCheckbox(cbselect, ids)"/><%firstIndex++;%>
	       				</td>
	       				<s:iterator value="displayFields" id="displayCol">
	       					<s:iterator value="r" id="resultMap">
						  		<s:if test="#resultMap.key.equals(#displayCol)">
						  			<td class="<s:if test="#resultStatus.odd == true ">odd</s:if><s:else>even</s:else>" id="${resultMap.key}" style="word-wrap: break-word">
						  				<s:if test='getCheckEditLink(#displayCol).equals("true")'>
						  					<s:url id="editLink" action="processEdit%{action}">
												<s:param name="id" value="%{getResultPrimaryKey(#resultStatus.index)}"></s:param>
												<s:param name="action" value="%{action}"></s:param>
											</s:url>
						  					<s:a href="%{editLink}">${resultMap.value}</s:a>
						  				</s:if>
						  				<s:else>${resultMap.value}</s:else>
						  				<%tabelCol++;%>
       				    			</td>
						  		</s:if> 
						  	</s:iterator>
					  	</s:iterator>
					  	<s:iterator value="r" id="resultMap">
					  		<s:iterator value="hiddenColumns" id="hiddenCol">
						  		<s:if test="#resultMap.key.equals(#hiddenCol)">
						  			<span id="${hiddenCol}"><input type="hidden"  value="${resultMap.value}"/></span>
					  			</s:if> 
					  		</s:iterator>
					  	</s:iterator>
	                </tr>
              	</s:iterator>
			  </s:if>
            </s:if>
            <%-- if no results --%>
            <s:if test="result.size() <= 0 && searched">
                <tr><td class="remark" colspan="${tabelCol+1}"><s:text name="errors.noResultFound" /></td></tr>
			</s:if>
			<%-- pagination --%>
			<s:if test="result.size() > 0 && searched">
			  <tr><td colspan="<%=tabelCol + 1%>">
				<jsp:include page="../pagination/paging.jsp"></jsp:include>
			  </td></tr>					
			</s:if>
		  </table>
</body>
</html>