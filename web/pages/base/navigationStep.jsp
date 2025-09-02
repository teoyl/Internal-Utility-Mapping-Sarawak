<%-- 
    Document   : navigationStep
    Created on : Nov 20, 2013, 3:02:53 PM
    Author     : Delvene
--%>

<%@taglib uri="/struts-tags" prefix="s"%>

<script language="javascript">
    function showVerifierInfo(index) {
        document.getElementById("verifierTag_"+index).title = document.getElementById("verifierInfo_"+index).value;
    }
    
    function changeBg() {
        document.getElementById
    }
    
</script>
  <!--affix side menu-->
<div class="style-switcher">
    <h3 class="title-xs text-uppercase text-center mt-20 title-color-switcher">Navigation</h3>
    <hr class="hr-divider-xs margin-center">
    <div class="header-switches text-center">
        <s:iterator value="stepNavList" id="stepNav" status="stepNavStatus">
            <a class="btn btn-default btn-block btn-xs text-theme-sm  ${stepNav.stepStatus}" href="#">${stepNavStatus.index+1}. &nbsp;<s:text name="qp.app.navStep.%{#stepNav.stepLabel}"/><br/> ${stepNav.stepVerifierID}</a>
        </s:iterator>
    </div>
</div>
<%--<table width="100%" cellpadding="0" cellspacing="0" style="table-layout: fixed" border="0">
    <tr>
        <s:iterator value="stepNavList" id="stepNav" status="stepNavStatus">
            <s:if test="(#stepNavStatus.index) % stepNavList.size() == 0">
                <td></td>
                <s:if test="(#stepNavStatus.index) % stepNavList.size() == 1">
                <td class="circleBase <s:property value="#stepNav.stepStatus" />_1" id="1" width="52px" height="26px" align="center">${stepNav.stepNo}</td>
                </s:if>
                <s:else>
                    <td class= circleBase <s:property value="#stepNav.stepStatus" />" id="2" width="52px" height="26px" align="center">${stepNav.stepNo}</td>
                </s:else>   
            </s:if>
            <s:else>
                <td class=" circleBase <s:property value="#stepNav.stepStatus" />_bar"></td>
                <td class=" circleBase <s:property value="#stepNav.stepStatus" />_bar"></td>
                <td class=" circleBase <s:property value="#stepNav.stepStatus" />" width="26px" height="26px" align="center">${stepNav.stepNo}</td>

                <s:if test="stepNavList.size() == (#stepNavStatus.index + 1)">
                    <td></td>
                </s:if>
            </s:else>
        </s:iterator>
    </tr>
    <tr>
        <td height="5px"></td>
    </tr>
        <!--<tr valign="middle" class="visible-lg">-->
        <tr valign="top" class="visible-lg">
            <s:iterator value="stepNavList" id="stepNav" status="stepNavStatus">
                <td colspan="3" align="center" style="padding-left:2px; padding-right:2px;" class="<s:property value="#stepNav.stepStatus" />_lbl">
                    <s:text name="qp.app.navStep.%{#stepNav.stepLabel}"/> 
                    <s:if test="#stepNav.stepVerification">
                        <s:hidden theme="simple" name="verifierInfo_%{#stepNavStatus.index}" value="%{#stepNav.stepVerifierInfo}" />
                        <br>
                        <s:if test='!#stepNav.stepVerifierID.equals("")'>
                            (<span id="verifierTag_${stepNavStatus.index}" class="verifier_tag" onmouseover="showVerifierInfo(${stepNavStatus.index})">${stepNav.stepVerifierID}</span>)                            
                        </s:if>
                    </s:if>
                </td>
            </s:iterator>
        </tr>
</table>--%>