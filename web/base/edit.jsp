<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />

        <SCRIPT language="javascript">
            function localValidateForm(form, operation) {
                var errors = new Array();
	
                validateRequired(form, errors);
                if (errors.length > 0) {
                    alert(errors.join('\n'));
                    setFocus(form);
                }
                form.userAction.value = operation;
                return errors.length > 0 ? false : true;
            }

            function required(){
                this.aa = new Array("fb_type", "<s:text name='feedback.type' />");
            }
        </SCRIPT>
        <s:head />
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        <form method="post" id="editDBAFormId" class="prHrForm" action="loadEditPageDBA">
            <div class="titleFramework">
                <span class="titleText">My-MSSQL</span>
                <span class="titleActionTypeText"> | <s:text name="actionType.manage" /></span><br>
            </div>
            <div class="xbox">
                <s:hidden theme="simple" name="action" value="DBA"/>
                <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                    <tr>
                        <td align="left" class="button-container" width="50">
                            <s:hidden theme="simple" name="userAction" id='userAction'/>
                            <s:submit type="submit" cssClass="defaultButton" theme="simple" action="processUpdateDBA" value='Execute' onclick="return localValidateForm(this.form, 'insert')"/>
                        </td>
                        <td align="left" class="button-container">
                            Paging Size: <input type="text" name="pageSize" size="5" value="${pageSize}"/>
                        </td>
                        <td align="left" class="button-container">
                            Connection Status: ${connectionStatus}
                        </td>
                        <td align="right" class="button-container" width="50">
                            <!-- <s:submit type="submit" cssClass="defaultButton" theme="simple" action="processUpdateDBA" value='Commit' onclick="return localValidateForm(this.form, 'commit')"/>
                            <s:submit type="submit" cssClass="defaultButton" theme="simple" action="processUpdateDBA" value='Rollback' onclick="return localValidateForm(this.form, 'rollback')"/>
                            <s:submit type="submit" cssClass="defaultButton" theme="simple" action="processUpdateDBA" value='Close Connection' onclick="return localValidateForm(this.form, 'closeConnection')"/> -->
                        </td>
                    </tr>
                </table>
                <table cellspacing="0" cellpadding="2" border="0" width="100%" class="form">
                    <tr>
                        <td><s:textarea theme="simple" cssStyle="width:60%; height: 150px; font-size:130%" name="sqlstmt" value='%{sqlstmt.replaceAll("ZXZ", "%")}' rows="10"/></td>
                    </tr>
                    <tr>
                        <td></td>
                    </tr>
                </table>
                <s:if test="result != null && result.size() > 0">
                    <div style="width: 100%; height:350px; overflow: scroll">
                    <table cellspacing="0" cellpadding="2" border="0" class="form" >
                    <s:iterator value="result" var="rMap" status="rStatus">
                        <s:if test="#rStatus.index == 0">
                            <tr>
                                <th>No.</th>
                            <s:iterator value="#rMap.keySet()" var="rMapKey" status="rKeyStatus">
                                <s:if test='!#rMapKey.equals("Total_")'>
                                <th>
                                    ${rMapKey}
                                </th>
                                </s:if>
                            </s:iterator>
                            </tr>
                        </s:if>
                        <tr class="<s:if test="#rStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                            <td>${rStatus.index + 1 + (pageSize*(pageNo-1))}</td>
                            <s:iterator value="#rMap.keySet()" var="rMapKey" status="rKeyStatus">
                                <s:if test='!#rMapKey.equals("Total_")'>
                                <td>
                                    <s:property value="%{#rMap.get(#rMapKey)}"/>
                                </td>
                                </s:if>
                            </s:iterator>
                        </tr>
                    </s:iterator>
                    </table>
                    </div>
                   
                   <jsp:include page="/base/paging_pnf.jsp"></jsp:include>
                </s:if>
            </div>
        </form>
    </body>
</html>