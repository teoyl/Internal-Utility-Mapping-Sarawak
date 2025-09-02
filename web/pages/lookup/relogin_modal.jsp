<%@page import="com.sains.common.util.AesUtil"%>
<%@page import="com.sains.common.util.SystemConstants"%>
<!DOCTYPE html>  
<%@taglib uri="/struts-tags" prefix="s"%>
<head>
    <s:if test="usePubPrivateKey">
    <script language="JavaScript" type="text/javascript" src="include/crypto/jsencrypt.js"></script>
    </s:if>
    <%--<s:head />--%>
    <%
        String strStateCode = (session.getAttribute("sesStateCode") == null ? "" : session.getAttribute("sesStateCode").toString());
        if (strStateCode.equals("")) {
            strStateCode = AesUtil.random(10);
            session.setAttribute("sesStateCode", strStateCode);
        }
    %>
    <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
        <script src="<%=SystemConstants.SarawakID.PLUGIN_URL[SystemConstants.ENV]%>"></script>
    </s:if>
    <script type="text/javascript">
<%--        <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
        document.addEventListener("DOMContentLoaded", function () {
            swkid_sso_init({
                client_id: '<%=SystemConstants.SarawakID.CLIENT_ID[SystemConstants.ENV]%>',
                state: '<%=strStateCode%>',
                response_type: 'code',
                redirect_uri: '<%=SystemConstants.SarawakID.SYSTEM_URL[SystemConstants.ENV]%>/ssoVerifyLogin',
                logout_redirect_uri: '<%=SystemConstants.SarawakID.SYSTEM_URL[SystemConstants.ENV]%>/processlogoutLogout',
                logout_uri: '<%=SystemConstants.SarawakID.SYSTEM_URL[SystemConstants.ENV]%>/processlogoutLogout',
                //                style:'icon-text',
                icon_width: '30',
                //                position:'',
                misc_param: 'test',
                profile_listing_style: 'details',
                //                profile_listing_email:'on',
                //                force_login_btn: 'off'
            });
            //            swkid_login_form_submit();
        });
        </s:if> --%>
        function swkid_callback(returnObj) {
            if (returnObj.swkid_login_status == "after_swkid_login") {
                if (!$("#swkid-profile-picture").hasClass("swkid-profile-picture")) {
                    login_form_submit();
                }
            }
        }
        function doLogin() {
            if ($('#passwd').val()==='') {
                alert("Please enter password");
                return false;
            } else {
            <s:if test="usePubPrivateKey">
                var encrypt = new JSEncrypt();
                encrypt.setPublicKey($('#pubkey').val());
                var encrypted = encrypt.encrypt($('#passwd').val());
                $('#passwd').val(encrypted)
            </s:if>
                divSubmitForm('processloginLogin', 'reloginForm', 'lookupModal');
                return true;
            }
        }
        $(document).ready(function () {
            document.getElementById("userId").focus();
<%--        <s:if test="@com.sains.common.util.SystemConstants$SarawakID@ENABLE_SWKID[@com.sains.common.util.SystemConstants@ENV]">
            if ($("#swkid-profile-picture").hasClass("swkid-profile-picture")) {
                login_form_submit();
            }
        </s:if> --%>
        });
    </script>
</head>
<form theme="simple" action="processloginLogin" method="post" id="reloginForm">
<div class="" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="lookupModalLabel"><s:property value="%{finalLookupDesc}"/></h4>
        </div>
        <div class="modal-body">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4><s:text name="errors.sesionExpired"/></h4>
                </div>
                <div class="panel-body">
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label"><s:text name="common.username"/></label>
                        <div class="col-md-5" style="height: 28px">
                            <s:if test="@com.sains.common.util.SystemConstants@ENV == 0">
                                <input type="text" name="userId" id="userId" value="admin">
                            </s:if><s:else>
                                <input type="text" name="userId" id="userId" value="">
                            </s:else>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label"><s:text name="common.password"/></label>
                        <div class="col-md-5" style="height: 28px">
                            <s:if test="@com.sains.common.util.SystemConstants@ENV == 0">
                                <input type="password" id="passwd" name="passwd" value="password" autocomplete="off" maxlength="20">
                            </s:if><s:else>
                                <input type="password" id="passwd" name="passwd" value="" autocomplete="off" maxlength="20" class="form-control">
                            </s:else>
                        </div>
                    </div>
                    <div class="row form-horizontal form-group">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-5">
                            <s:submit cssClass="defaultButton" theme="simple" value='%{getText("button.login")}' action="processloginLogin" onclick="doLogin(); return false;"/>
<!--                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return validateForm_bshor('sampleFormId');">
                                <i class="fa fa-save"></i>Save-js
                            </button>-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</form>
<s:if test="usePubPrivateKey">
<input type="hidden" id="pubkey" value="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCB8fiA+6c7OL+82UOtc0NA29CuMtVh7Mkgx9GCVMX9GR5A0fAKOP7qWOsCYSK/Q1Kkt33bxfsRSGuTf+KlzU4QCsn77AuDpoPX0As1XZzltXCrl++Kafd93WtMJsmUYsxfYw/nvbpPt/UY6TBXfkhwyUeiEkdzfoeoz/IBWG2lzwIDAQAB">
</s:if>