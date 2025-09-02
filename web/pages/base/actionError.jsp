<%@taglib uri="/struts-tags" prefix="s"%>
<div id="actionErrMsg_">
<!--<div id="actionErrMsg_" class="form-row-margin-top">-->
    <s:if test="actionErrors.size() > 0">
       <%-- <table class="label label-warning"><tr>
                <!--<td><img src="images/action_icon/warning.png" width="25px" height="25px"/></td>-->
                <td><font><s:actionerror theme="simple"/></font></td>
        </tr></table>--%>
        <div class="alert alert-danger" role="alert"><s:actionerror theme="simple" escape="true"/></div>
    </s:if>
    <s:if test="actionMessages.size() > 0">
        <%--<table class="label label-info"><tr>
                <!--<td><img src="images/action_icon/info.png" width="25px" height="25px"/></td>-->
                <td><s:actionmessage theme="simple" escape="false" /></td>
        </tr></table>--%>
         <div class="alert alert-success" role="alert"><s:actionmessage theme="simple" escape="true" /></div>
    </s:if>
    <s:if test="fieldErrors.size() > 0">
        <div class="alert alert-warning" role="alert"><s:fielderror theme="simple"/></div>
    </s:if>
    <s:if test="actionInfo != null">
        <!--<table class="label label-success " >-->
            <%--<s:iterator value="actionInfo" status="actionInfoStatus" id="theActionInfo" var="theActionInfo">--%>
                <!--<tr>-->
                    <%--<td style="width: 50px"><img src="${theActionInfo.keyData}" width="25px" height="25px"/></td>--%>
                    <%--<td><li>${theActionInfo.valueData}</li></td>--%>
                <!--</tr>-->
            <%--</s:iterator>--%>
            <!--<tr>-->
                <!--<td height="20px" colspan="2"></td>-->
            <!--</tr>-->
        <!--</table>-->
        <div class="alert alert-info" role="alert"><s:actionmessage theme="simple" escape="true" />
            <s:iterator value="actionInfo" status="actionInfoStatus" var="theActionInfo">
                <span><s:property value="theActionInfo.valueData" escapeJavaScript="true"/></span>
            </s:iterator>
        </div>
    </s:if>
</div>