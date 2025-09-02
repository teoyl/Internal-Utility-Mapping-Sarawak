
<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="pages/scripts/common.js"></script>
<script type="text/javascript" src="pages/scripts/validation.jsp"></script>
<%--<link href="styles/general.css" rel="stylesheet" type="text/css" />--%>
<link href="styles/common.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" >
 
    function setValue(pValue){
        var columnName = document.getElementById('codeType').value;
        var selectedValue = document.getElementById('selectedColumn'+pValue).value;
        if(columnName=='DIV'){
            columnName = 'trnDiv';
        }else if(columnName=='DIS'){
            columnName = 'trnDist';
        }else if(columnName=='LCT,PLT'){
            columnName = 'trnType';
        }
        else if(columnName=='COU'){
            columnName = 'countryCode';
        }else if(columnName=='COU_P'){ //sereneChye @ 22/9/2016
            columnName = 'countryCode_p';
        }else if(columnName=='COU_F'){ //ahmadni @ 10-Nov-2016
            columnName = 'countryCode_f';
        }
        if (parent.document.getElementById(columnName)){
            parent.document.getElementById(columnName).value = selectedValue;
        }else{//cater for rent and search premium - ahmadni 19/12/2014
            columnName = 'searchDistrict_';
            parent.document.getElementById(columnName).value = selectedValue;
        }
        
        parent.winInst.hide();
    }
    <%--window.onload = function(){
        setCartSummary();
    }--%>

  </script>

<jsp:include page="/pages/base/actionError.jsp"></jsp:include>

<table width="100%" class="tableData">
    <tr class="table_header">
        <td width="10%"  class="center">Code</td>
        <td width="90%"  class="left">Code Description</td>
     </tr>

     <s:if test="getIntRowCount() > 0">
     <s:iterator value="getPublicCodeList()" status="st" id="rs" >
        <s:if test="#st.Odd">
            <tr class = "oddrow" onclick ="setValue('<s:property value="#st.index"/>')" >
        </s:if>
        <s:else>
            <tr class = "evenrow" onclick ="setValue('<s:property value="#st.index"/>')" >
        </s:else>

         <td class="left">
             <%--<s:property value='getCodeType()'/>--%>
             <s:if test="getCodeType()=='DIV'">
             <s:property value='#rs.code_1'/>
             <input type="hidden" name="selectedColumn<s:property value="#st.index"/>" id="selectedColumn<s:property value="#st.index"/>" value="<s:property value='#rs.code_1'/>" />
             </s:if>
             <s:elseif test="getCodeType()=='DIS'">
                 <s:property value='#rs.code_2'/>
                 <input type="hidden" name="selectedColumn<s:property value="#st.index"/>" id="selectedColumn<s:property value="#st.index"/>" value="<s:property value='#rs.code_2'/>" />
             </s:elseif>
             <s:elseif test="getCodeType()=='LCT,PLT'">
                 <s:property value='#rs.code_acr'/>
                 <input type="hidden" name="selectedColumn<s:property value="#st.index"/>" id="selectedColumn<s:property value="#st.index"/>" value="<s:property value='#rs.code_acr'/>" />
             </s:elseif>
             <s:elseif test="getCodeType()=='COU' || getCodeType()=='COU_P' || getCodeType()=='COU_F'">
                 <s:property value='#rs.code_3'/>
                 <input type="hidden" name="selectedColumn<s:property value="#st.index"/>" id="selectedColumn<s:property value="#st.index"/>" value="<s:property value='#rs.code_3'/>" />
             </s:elseif>
             <%--<s:elseif test="getCodeType()=='COU_P'"><!--serene @ 22/9/2016 :: tumbang for user profile loop phone country code -->
                 <s:property value='#rs.code_3'/>
                 <input type="hidden" name="selectedColumn<s:property value="#st.index"/>" id="selectedColumn<s:property value="#st.index"/>" value="<s:property value='#rs.code_3'/>" />
             </s:elseif>--%>
         </td>
         <td class="left"><s:property value='#rs.code_desc'/></td>
 
     </tr>
     </s:iterator>
     <tr><td colspan="4">
     <table width="100%">
      <tr align="right">
          <td align="center" width="25%"></td>
      <td align="center" width="25%"></td>
      <td align="center" width="25%"></td>
      <td align="center">
        <%--<img src="images/add_cart.gif" alt="Add to Cart" onclick="checkSubmit()"  style="cursor:pointer" >--%>
        <input type="hidden" name="codeType" id="codeType" value="<s:property value='codeType'/>" />
        <%--<input type="hidden" name="source" id="source" value="popup" />
        <input type="hidden" name="trnDiv" id="trnDiv"  value="<s:property value='trnDiv'/>"  />
        <input type="hidden" name="instSeq" id="instSeq" value="<s:property value='instSeq'/>"  />
        <input type="hidden" name="instYear" id="instYear"  value="<s:property value='instYear'/>"  />
        <input type="hidden" name="trn" id="trn" value="<s:property value='trn'/>"  />
        <input type="hidden" name="titVer" id="titVer"  value="<s:property value='titVer'/>"  />
        <input type="hidden" name="storey" id="storey" value="<s:property value='storey'/>"  />
        <input type="hidden" name="parcel" id="parcel"  value="<s:property value='parcel'/>"  />
        <s:set var="cartSummary" value='getCartSumm()'/>
        <input type="hidden" name="cartSumm" id="cartSumm" value="<s:property value="#cartSummary"/>"  />--%>

      </td>
     </tr>
     </table>
      
         </td>
    </tr>
     </s:if>
    <s:else>
        <tr class = "evenrow">
            <td align="center" colspan="2">
                No Record found
            </td>
        </tr>
    </s:else>
</table>



