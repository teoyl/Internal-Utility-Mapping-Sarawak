<%@taglib uri="/struts-tags" prefix="s"%>
<div id="actionErrMsg_" class="form-row-margin-top">
    <s:if test="actionErrors.size() > 0">
       <%-- <table class="label label-warning"><tr>
                <!--<td><img src="images/action_icon/warning.png" width="25px" height="25px"/></td>-->
                <td><font><s:actionerror theme="simple"/></font></td>
        </tr></table>--%>
        <div class="row">
            <div class="col-xs-12 myalert">
                <div class="alert alert-danger alert-dismissable"> 
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"> × </button> 
                        <s:actionerror theme="simple" escape="false"/>
                </div>
            </div>
        </div>
        <br>
    </s:if>
    <s:if test="actionMessages.size() > 0">
        <%--<table class="label label-info"><tr>
                <!--<td><img src="images/action_icon/info.png" width="25px" height="25px"/></td>-->
                <td><s:actionmessage theme="simple" escape="false" /></td>
        </tr></table>--%>
        <div class="row">
            <div class="col-xs-12 myalert">
                <div class="alert alert-success alert-dismissable"> 
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"> × </button> 
                        <s:actionmessage theme="simple" escape="false" />
                </div>
            </div>
        </div>
        <br>
    </s:if>
    <s:if test="fieldErrors.size() > 0">
        <div class="row">
            <div class="col-xs-12 myalert">
                <div class="alert alert-info alert-dismissable"> 
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"> × </button> 
                    <s:fielderror theme="simple"/>
                </div>
            </div>
        </div>
        <br>
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
        <div class="alert alert-info" role="alert"><s:actionmessage theme="simple" escape="false" />
            <s:iterator value="actionInfo" status="actionInfoStatus" id="theActionInfo" var="theActionInfo">
                <span><s:property value="%{#theActionInfo.valueData}"/></span>
            </s:iterator>
        </div>
        <br>
    </s:if>
</div>