<%@taglib uri="/struts-tags" prefix="s"%>

<SCRIPT language="Javascript">
    function localValidateForm(form, operation) {
        var errors = new Array();

        validateRequired(form, errors);
        // sample to validate items.
        //validateItems(form, errors);		
        if (errors.length > 0) {
            alert(errors.join('\n'));
            setFocus(form);
        }
        return errors.length > 0 ? false : true;
    }

    function required() {
        this.aa = new Array("model.us_user_name", "<s:text name='user.full.name' />");
        this.ab = new Array("model.us_nationality", "<s:text name='user.citizenship' />");
        this.ac = new Array("model.us_id_number", "<s:text name='user.ic.no' />");
        this.ad = new Array("model.us_dob_str", "<s:text name='user.dob' />");
        this.ae = new Array("model.us_email", "<s:text name='user.emailAddress' />");
        this.af = new Array("model.us_hp_country_code", "<s:text name='country.tel.code' />");
        this.ag = new Array("model.us_hp_number", "<s:text name='user.hpNo' />");
        // Disabled by Ivy for Adminitration Screen
//        this.ah = new Array("model.us_mailing_postcode", "<s:text name='user.postcode' />");
//        this.ai = new Array("model.us_mailing_state", "<s:text name='user.state' />");
//        this.aj = new Array("model.us_mailing_city", "<s:text name='user.city' />");
    }

    function validateItems(form, errors) {
        if (!form.selected) {
            errors[errors.length] = formatText(messageRequired, "Application");
            return;
        }
        if (typeof (form.selected.type) == "string") {
            validateItem(form.selected, 1, errors);
        } else {
            var isError = false;
            for (var i = 0; i < form.selected.length; i++) {
                isError = validateItem(form.selected[i], i + 1, errors);
                if (isError)
                    break;
            }
        }
    }

    function validateItem(selected, idx, errors) {
        if (!selected.checked) {
            //if (selected.value == ""){
            errors[errors.length] = "Application at row " + idx + " not checked.";
        }
    }
    function setRowIndx(a) {
        document.getElementById("subsRowIndx_").value = a;
    }

    function loadCodeDesc(pType, pCode1Filter) {
        var varTitle = "";
        if (pType === 'COU' || pType === 'COU_P' || pType === 'COU_F') {
            varTitle = "Country Code Listing";
        }

        winInst = dhtmlmodal.open("popup1", "iframe", "populatePublicCodeRegistration?codeType=" + pType + "&code1Filter=" + pCode1Filter, varTitle, "width=700px,height=350px,resize=1,scrolling=1,center=1", "");
        winInst.onclose = function() {
            return true;
        }

    }

    function populateList(method, inputValue, outputName, writeTo) {
        var url = method + "?searchItem" + "=" + inputValue + "&outputName=" + outputName;
        initiateAJAX(url, writeTo, "", "");
    }
    function initiateAJAX(url, writeTo, action)
    {
        var xmlHttpRequest = getXMLHttpRequest();

        xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState == 4) {
                if (xmlHttpRequest.status == 200) {

                    document.getElementById(writeTo).innerHTML = xmlHttpRequest.responseText;

                } else {
                    alert("HTTP error " + xmlHttpRequest.status + ": " + xmlHttpRequest.statusText);
                }
            }
        }

        xmlHttpRequest.open("POST", url, true);
        xmlHttpRequest.setRequestHeader("Content-Type",
                "application/x-www-form-urlencoded");
        xmlHttpRequest.send(null);
    }

    function checkCountry(method, country, stateID, townID) {
        var url = method + "?" + "searchItem=" + country;
        var xmlHttpRequest = getXMLHttpRequest();
    <%--alert(url);--%>

        xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState == 4) {
                if (xmlHttpRequest.status == 200) {
                    document.getElementById(stateID).value = "";
                    document.getElementById(townID).value = "";
                } else {
                    alert("HTTP error " + xmlHttpRequest.status + ": " + xmlHttpRequest.statusText);
                }
            }
        }
        xmlHttpRequest.open("POST", url, true);
        xmlHttpRequest.setRequestHeader("Content-Type",
                "application/x-www-form-urlencoded");
        xmlHttpRequest.send(null);
    }



    function getXMLHttpRequest() {
        var xmlHttpReq = false;
        // to create XMLHttpRequest object in non-Microsoft browsers
        if (window.XMLHttpRequest) {
            xmlHttpReq = new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            try {
                // to create XMLHttpRequest object in later versions
                // of Internet Explorer
                xmlHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (exp1) {
                try {
                    // to create XMLHttpRequest object in older versions
                    // of Internet Explorer
                    xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (exp2) {
                    xmlHttpReq = false;
                }
            }
        }
        return xmlHttpReq;
    }

    <s:set name="countryMalaysia"><%=com.sains.common.util.SystemConstants.SETUP_CODE_HARDCODE.COU_Malaysia%></s:set>
    <s:set name="stateOversea"><%=com.sains.common.util.SystemConstants.SETUP_CODE_HARDCODE.STT_Luar_Negara%></s:set>
    <s:set name="stateOther"><%=com.sains.common.util.SystemConstants.SETUP_CODE_HARDCODE.STT_Lain_lain%></s:set>
    <s:set name="stateNoInfo"><%=com.sains.common.util.SystemConstants.SETUP_CODE_HARDCODE.STT_No_Info%></s:set>
    <s:set name="townOther"><%=com.sains.common.util.SystemConstants.SETUP_CODE_HARDCODE.TWN_Other%></s:set>
    <%--if user select state, auto-select country--%>
    function selectCountry(countryListID, stateList) {
        var state = stateList.options[stateList.selectedIndex].value;
        var countryList = document.getElementById(countryListID);
        countryList.value = '${countryMalaysia}';
        if (state == '${stateNoInfo}' || state == '${stateOversea}') {
            countryList.value = '';
        } else {
            countryList.value = '${countryMalaysia}';
        }
    }


    <%--if user select country other than malaysia, auto-select luar negeri for state--%>
    function selectStateTown(stateListID, townListID, countryList) {
        var country = countryList.options[countryList.selectedIndex].value;
        var stateList = document.getElementById(stateListID);
        var townList = document.getElementById(townListID);

        if (country != '${countryMalaysia}') {
            stateList.value = '${stateOversea}';
            townList.value = '${townOther}';
        } else {
            stateList.value = '';
            townList.value = '';
        }
    }

    function selectStateTown_Freetext(townFieldID, townListID, country) {
        if (country === 'course_country') {
            var cou = document.getElementById(country).value;
            country = cou;
        }
        if (country === '${countryMalaysia}') {
            document.getElementById(townFieldID).style.display = "none";
            document.getElementById(townListID).style.display = "inline";
        } else {
            document.getElementById(townFieldID).style.display = "inline";
            document.getElementById(townListID).style.display = "none";
        }
    }

    function selectStateByCountry(countryList, stateListID) {
        var stateList = document.getElementById(stateListID);
        if (countryList.value != '${countryMalaysia}') {
            stateList.value = '${stateOversea}';
        } else {
            if (stateList.value == '${stateOversea}') {
                stateList.value = '';
            }
        }
    }

    function selectPassportCountry(passMysOption, passTypeListID, countryListID, stateListID) {
        var passTypeList = document.getElementById(passTypeListID);
        var countryList = document.getElementById(countryListID);
        var stateList = document.getElementById(stateListID);

        if (passMysOption.value == 'Y') {
            countryList.value = '${countryMalaysia}';
            if (stateList.value == '' || stateList.value == '${stateOversea}') {
                stateList.value = '';
            }
        } else {
            if (countryList.value == '${countryMalaysia}') {
                countryList.value = '';
            }
            stateList.value = '${stateOversea}';
            passTypeList.value = '${passportTypeInternational}';
        }
    }

    function loadCodeDesc(pType, pCode1Filter) {
        var varTitle = "Country Code Listing";

        winInst = dhtmlmodal.open("popup1", "iframe", "populatePublicCodeRegistration?codeType=" + pType + "&code1Filter=" + pCode1Filter, varTitle, "width=700px,height=350px,resize=1,scrolling=1,center=1", "");
        winInst.onclose = function() {
            return true;
        }

    }
    function populateDOB() {
        var strNewIC = document.getElementById("model_us_id_number").value;
        if (document.getElementById("us_dob_str").value === '') {
            if (document.getElementById("model.us_nationality").value === 'COU-MYS' && strNewIC !== '') {
                if (strNewIC.length === 12) {
                    if (!isNaN(strNewIC)) {
                        if (IsValidNewIC(strNewIC, "<s:text name='lbl.registration.number'/>")) {
                            var year = strNewIC.substring(0, 2);
                            var month = strNewIC.substring(2, 4);
                            var day = strNewIC.substring(4, 6);
                            var dob = day + "/" + month + "/19" + year;
                            document.getElementById("us_dob_str").value = dob;
                        } else {
                            return false;
                        }
                    }//else alert("not number");
                } else
                if (strNewIC.length === 14) {
                    if (strNewIC.indexOf("-") > 2) {
                        if (IsValidNewIC(strNewIC, "<s:text name='lbl.registration.number'/>")) {
                            var year = strNewIC.substring(0, 2);
                            var month = strNewIC.substring(2, 4);
                            var day = strNewIC.substring(4, 6);
                            var dob = day + "/" + month + "/19" + year;
                            document.getElementById("us_dob_str").value = dob;
                        } else {
                            return false;
                        }
                    }//else{alert("no dash")};

                }
            }
        }
    }

</SCRIPT>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<sx:head parseContent="true" debug="false" />
<jsp:include page="/pages/base/actionError.jsp"/>


<form action="processUpdatePublic" name="publicUserForm" method="post" enctype="multipart/form-data" class="prForm">
    <s:hidden name="action" />   
    <s:hidden name="searchCondition" />
    <s:hidden name="us_id" value="%{model.us_id}" />
    <%--<s:hidden name="us_user_id" value="%{model.us_user_id}" />--%>
    <s:hidden name="custCompanyModel.co_id" value="%{custCompanyModel.co_id}" />
    <s:set name="subsPending"><%=com.sains.common.util.SystemConstants.USERSUBSCRIPTION.STATUS.PENDING%></s:set>
    <s:hidden theme="simple" name="subsRowIndx_"/>

    <div class="row">
        <div class="col-md-12 text-right">
            <button class="btn btn-primary" type="submit" name="action:processUpdatePublicUser_" id="processUpdatePublicUser_" onclick="return localValidateForm(this.form);"><i class="fa fa-save"></i>Save</button>     
            <button class="btn btn-default" type="submit" name="action:cancelPublicUser_" id="cancelPublicUser_"><i class="fa fa-close"></i>Cancel</button>     
            <%--<s:submit cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processUpdatePublicUser_"  value="Update" onclick="return localValidateForm(this.form);"/>
            <s:submit cssClass="defaultButton buttonCancel btn mrg-lr-5" theme="simple" action="cancelPublicUser_" value="Cancel"/>--%>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12"><h3 class="title-v3"><s:text name="qp.personal.info"/></h3></div>
    </div>
    <div class="row">
        <div class="col-md-4">
            <div class="form-group form-group-default form-group-default-select2">
                <label><s:text name="user.title"/></label>
                <s:select cssClass="full-width"  data-init-plugin="select2" list="titleList" listKey="code_id" listValue="code_desc" theme="simple" id="us_title" name="model.us_title" value="%{model.us_title}" />
            </div>
        </div>
        <div class="col-md-4">
            <div class="form-group form-group-default required">
                <label><s:text name="user.full.name"/></label>
                <s:textfield theme="simple" size="47" name="model.us_user_name" cssClass="form-control" value="%{model.us_user_name}"/>
            </div>
        </div>
        <div class="col-md-4">
            <div class="form-group form-group-default form-group-default-select2 required">
                <label><s:text name="user.full.name"/><s:text name="user.citizenship"/></label>
                <s:select cssClass="full-width"  data-init-plugin="select2" list="countryList" listKey="code_id" listValue="code_desc" theme="simple" id="model.us_nationality" name="model.us_nationality" value="%{model.us_nationality}" />
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-4">
            <div class="form-group form-group-default required">
                <label><s:text name="user.ic.no"/></label>
                <s:textfield theme="simple" name="model.us_id_number" cssClass="form-control" value="%{model.us_id_number}"/>
            </div>
        </div>
        <div class="col-md-4">
            <div class="form-group form-group-default required">
                <label><s:text name="user.dob"/></label>
                <s:textfield name="model.us_dob_str" id="us_dob_str" value="%{model.us_dob_str}"  cssClass="form-control bootstrapDatePickerDown" onfocus="populateDOB()"/>
            </div>
        </div>
        <div class="col-md-4">
            <div class="form-group form-group-default ">
                <label><s:text name="user.pob"/></label>
                <s:textfield theme="simple" size="47" maxlength='%{model.columnLengthMap.get("us_birth_place")}' name="model.us_birth_place" value="%{model.us_birth_place}" cssClass="form-control"/>
            </div>
        </div>
    </div><br/><br/>




    <div class="row">
        <div class="col-md-12"><h3 class="title-v3"><s:text name="user.contactInfo"/></h3></div>
    </div>
    <div class="row">
        <!--left-->
        <div class="col-md-4">
            <div class="form-group form-group-default required">
                <label><s:text name="user.emailAddress"/></label>
                <s:textfield theme="simple" size="47" name="model.us_email" cssClass="form-control" value="%{model.us_email}"/>
            </div>
            <div class="form-group form-group-default required">
                <label><s:text name="user.hpNo"/></label>
                <div class="input-group input-group-prForm">
                    <span class="input-group-addon"><s:textfield theme="simple" style="width: 30px" cssClass="requiredField input-md form-control" size="5" name="model.us_hp_country_code" id="countryCode" maxlength="5"  value="%{model.us_hp_country_code}"  readonly="true"/><%--model.us_hp_country_code--%></span>
                    <span class="input-group-addon"><img height="18" width="18" align="absmiddle" onclick="loadCodeDesc('COU', '!in 000')" alt="List Country Code" src="images/document_lookup.gif" style="cursor: pointer;" ></span>
                    <span class="input-group-btn input-group-addon"><s:textfield style="width: 220px"  theme="simple" name="model.us_hp_number" cssClass="requiredField input-md form-control" value="%{model.us_hp_number}"/></span>
                </div>
            </div>
            <div class="form-group form-group-default">
                <label><s:text name="user.phoneNo"/></label>
                <div class="input-group input-group-prForm">
                    <span class="input-group-addon"><s:textfield theme="simple"  style="width: 30px" size="5" name="model.us_phone_country_code" id="countryCode_p" maxlength="5"  value="%{model.us_phone_country_code}"  readonly="true" cssClass="input-md form-control"/><%--model.us_phone_country_code--%></span>
                    <span class="input-group-addon"><img height="18" width="18" align="absmiddle" onclick="loadCodeDesc('COU_P', '!in 000')" alt="List Country Code" src="images/document_lookup.gif" style="cursor: pointer;" > </span>
                    <span class="input-group-addon"><s:textfield style="width: 220px" theme="simple" name="model.us_phone_number" value="%{model.us_phone_number}" cssClass="input-md form-control"/></span>
                </div>
            </div>
            <div class="form-group form-group-default">
                <label><s:text name="user.faxNo"/></label>
                <div class="input-group input-group-prForm">
                    <span class="input-group-addon"><s:textfield theme="simple"  style="width: 30px" size="5" name="model.us_fax_country_code" id="countryCode_f" maxlength="5"  value="%{model.us_fax_country_code}"  readonly="true" cssClass="input-md form-control "/><%--model.us_fax_country_code--%></span>
                    <span class="input-group-addon"><img height="18" width="18" align="absmiddle" onclick="loadCodeDesc('COU_F', '!in 000')" alt="List Country Code" src="images/document_lookup.gif" style="cursor: pointer;" ></span>
                    <span class="input-group-addon"><s:textfield style="width: 220px" theme="simple" name="model.us_fax_number" value="%{model.us_fax_number}" cssClass="input-md form-control"/></span>
                </div>
            </div>
        </div>
        <!--middle-->
        <div class="col-md-4">
            <div class="form-group form-group-default">
                <label><s:text name="user.address"/> <font class="text-green"><i>(line 1)</i></font></label>
                    <s:textfield disabled="%{editMode_}" theme="simple" size="47"  name="model.us_mailing_address1" maxlength='%{model.columnLengthMap.get("us_mailing_address1")}' value="%{model.us_mailing_address1}" cssClass="form-control"/>
            </div>
            <div class="form-group form-group-default">
                <label><s:text name="user.address"/> <font class="text-green"><i>(line 2)</i></font></label>
                    <s:textfield disabled="%{editMode_}" theme="simple" size="47"  name="model.us_mailing_address2" maxlength='%{model.columnLengthMap.get("us_mailing_address2")}' value="%{model.us_mailing_address2}" cssClass="form-control"/>
            </div>
            <div class="form-group form-group-default">
                <label><s:text name="user.address"/> <font class="text-green"><i>(line 3)</i></font></label>
                    <s:textfield disabled="%{editMode_}" theme="simple" size="47"  name="model.us_mailing_address3" maxlength='%{model.columnLengthMap.get("us_mailing_address3")}' value="%{model.us_mailing_address3}" cssClass="form-control"/>
            </div>
            <div class="form-group form-group-default">
                <label><s:text name="user.address"/> <font class="text-green"><i>(line 4)</i></font></label>
                    <s:textfield disabled="%{editMode_}" theme="simple" size="47"  name="model.us_mailing_address4" maxlength='%{model.columnLengthMap.get("us_mailing_address4")}' value="%{model.us_mailing_address4}" cssClass="form-control"/>
            </div>
        </div>
        <!--right-->
        <div class="col-md-4">
            <div class="form-group form-group-default">
                <label><s:text name="user.postcode"/></label>
                <s:textfield disabled="%{editMode_}" theme="simple" size="47" cssClass="form-control" maxlength='%{model.columnLengthMap.get("us_mailing_postcode")}' name="model.us_mailing_postcode" value="%{model.us_mailing_postcode}"/>
            </div>
            <div class="form-group form-group-default form-group-default-select2">
                <label><s:text name="user.state"/></label>
                <s:select cssClass="full-width"  data-init-plugin="select2" disabled="%{editMode_}"  id="stateByTown" list="stateList" listKey="code_id" listValue="code_desc" theme="simple" name="model.us_mailing_state" value="%{model.us_mailing_state}" onchange="populateList('loadTwnBySttProfile', this.value+'&outputID=us_mailing_city&currentMenuButton_=PI', 'model.us_mailing_city', 'twnByStt'); "/>
            </div>
            <div class="form-group form-group-default form-group-default-select2">
                <label><s:text name="user.city"/></label>
                <s:select cssClass="full-width"  data-init-plugin="select2" disabled="%{editMode_}"  id="twnByStt" list="twnBySttList_" listKey="code_id" listValue="code_desc" theme="simple" name="model.us_mailing_city" value="model.us_mailing_city" onchange="populateList('loadSttByTwnProfile', this.value+'&outputID=us_mailing_state&currentMenuButton_=PI', 'model.us_mailing_state', 'stateByTown'); " />
            </div>
        </div>
    </div><br/><br/>

    <div class="row">
        <div class="col-md-12"><h3 class="title-v3"><s:text name="user.login.detail"/></div>
    </div>
    <div class="row">
        <div class="col-md-3">
            <div class="form-group form-group-default form-group-default-select2">
                <label><s:text name="user.status"/></label>
                <s:select cssClass="full-width"  data-init-plugin="select2" name="model.us_status" theme="simple" list="statusList" listKey="keyData" listValue="valueData" value="%{model.us_status}" />
            </div>
        </div>
        <div class="col-md-3">
            <div class="form-group form-group-default viewOnly">
                <label><s:text name="user.lastLogin"/></label>
                <s:if test='model.us_last_login_date != null || model.us_last_login_date.equals("")'>                        
                    <s:text name="date_default_datetime"><s:param value="%{model.us_last_login_date}"/></s:text></td>
                </s:if>
                <else>
                    <br>
                </else>
            </div>
        </div>
        <div class="col-md-3">
            <div class="form-group form-group-default viewOnly">
                <label><s:text name="user.lastFailLogin"/></label>
                <s:if test='model.us_fail_login_date != null || model.us_fail_login_date.equals("")'>
                    <s:text name="date_default_datetime"><s:param value="%{model.us_fail_login_date}"/></s:text>
                </s:if>
                <else>
                    <br>
                </else>
            </div>
        </div>
        <div class="col-md-3">
            <div class="form-group form-group-default viewOnly">
                <label><s:text name="user.failAttempt"/></label>
                <s:property value="%{model.us_fail_attempt_count}"/>
            </div>
        </div>
    </div><br/><br/>


    <div class="row">
        <div class="col-md-12">
            <h3 class="title-v3"><s:text name="user.subscription" /></h3>
        </div>
    </div>
    <div class="row form-row-margin">
        <div class="col-md-12">
            <s:if test="has_right('loadAddApplicationPage')">
                <button class="btn btn-default" type="submit" name="action:loadAddSubscriptionPublicUser_" id="loadAddSubscriptionPublicUser_"><i class="fa fa-plus"></i><s:text name="button.add"/></button>     
                    <%--<s:submit cssClass="defaultButton" theme="simple" action="loadAddSubscriptionPublicUser_" value="%{getText('button.add')}" />--%>
                </s:if>
                <s:if test="has_right('processDeleteApplication')">
                <button class="btn btn-default" type="submit" name="action:processDeleteSubscriptionUserGroup" id="processDeleteSubscriptionUserGroup" onclick="if (isCheckboxSelected(delApp_ids)) {
                            return confirmDelete();
                        } else {
                            return false;
                        }" ><i class="fa fa-trash-o"></i><s:text name="button.delete"/></button>     
                    <%--s:submit cssClass="defaultButton" theme="simple" action="processDeleteSubscriptionUserGroup" value="%{getText('button.delete')}"
                                  onclick="if ( isCheckboxSelected(delApp_ids)) {return confirmDelete();} else {return false;}" /--%>
                </s:if>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">  
            <div class="table-responsive">
                <table class="table table-epsa table-condensed table-striped table-hover" cellspacing="1" cellpadding="1"  width="100%">
                    <thead>
                        <tr>
                            <s:if test="has_right('processDeleteApplication')">
                            <th width="1%" align="center">
                                <div class="checkbox check-success tableFormCheckbox">
                                    <s:if test="model.subscriptionList.size() > 0">
                                        <input type="checkbox"  id="cbselect" class="selectAll" name="cbselect" onClick="toggleCheckboxByName(this,'subscription_selected');">
                                        <!--<input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, delApp_ids);" >-->
                                    </s:if>
                                    <s:else>
                                        <input type="checkbox" id="cbselect" class="selectAll" name="cbselect" disabled >
                                    </s:else>
                                    <label for="cbselect" class="tableFormCheckbox"></label>
                                </div>
                            </th>
                            </s:if>
                            <th width="15%"><s:text name="lbl.date.received"/><jsp:include page="/pages/base/requiredField.jsp"/></th>
                            <th width="10%"><s:text name="lbl.subscription.type"/></th>
                            <th width="15%"><s:text name="lbl.verified.by"/></th>
                            <th width="15%"><s:text name="lbl.verified.date"/><jsp:include page="/pages/base/requiredField.jsp"/></th>
                            <th width="16%"><s:text name="lbl.verify.status"/></th>
                            <th width="15%"><s:text name="lbl.remarks"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator value="model.subscriptionList" status="subscriptionStatus" id="userSubs" var="subscription">
                            <tr class="<s:if test="#subStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                <%--Checkbox--%>      
                                <s:if test="has_right('processDeleteApplication')">
                                <td align="center">
                                    <%--<s:checkbox theme="simple" name="selected" id="delApp_ids" fieldValue="%{#subscriptionStatus.index}" onclick="checkToggleCheckbox(cbselect, delApp_ids)"/>--%>
                                    <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                        <div class="checkbox check-success tableFormCheckbox">
                                            <input type="checkbox" name="subscription_selected" class="checkbox_child" id="${subscriptionStatus.index}" value="${subscriptionStatus.index}" onclick="toggleSelectAll()">
                                            <label for="${subscriptionStatus.index}" class="tableFormCheckbox"></label>
                                        </div>
                                        <%--<s:checkbox theme="simple" name="subscription_selected" id="delSubcribe_ids"  fieldValue="%{#subscriptionStatus.index}"
                                                    onclick="checkToggleCheckbox(subscription_select, delSubcribe_ids)" />--%>
                                    </s:if>
                                    <s:else>
                                        <div class="checkbox check-success tableFormCheckbox">
                                            <input type="checkbox" name="subscription_selected" class="checkbox_child" id="${subscriptionStatus.index}"  disabled="true" value="${subscriptionStatus.index}" onclick="toggleSelectAll()">
                                            <label for="${subscriptionStatus.index}" class="tableFormCheckbox"></label>
                                        </div>
                                        <%--<s:checkbox theme="simple" name="subscription_selected" id="delSubcribe_ids"  fieldValue="%{#subscriptionStatus.index}"
                                                    onclick="checkToggleCheckbox(subscription_select, delSubcribe_ids)" disabled="true"/>--%>
                                    </s:else>
                                    <s:hidden name="model.subscriptionList[%{#subscriptionStatus.index}].cs_id" value="%{#subscription.cs_id}"/>
                                </td>
                                </s:if>
                                <%--Subscription Date Received--%>
                                <td>
                                    <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                        <s:textfield name="model.subscriptionList[%{#subscriptionStatus.index}].cs_date_received_str"  theme="simple"
                                                     size="15"  id="strCs_date_received_%{#subscriptionStatus.index}" value='%{#subscription.cs_date_received_str}' cssClass="datepick-impian embed requiredField input-md "/>
                                    </s:if>
                                    <s:else>
                                        <s:textfield name="model.subscriptionList[%{#subscriptionStatus.index}].cs_date_received_str"  theme="simple"
                                                     size="15"  id="strCs_date_received_%{#subscriptionStatus.index}" value='%{#subscription.cs_date_received_str}' readonly="true" cssClass="input-md form-control"/>
                                    </s:else>
                                </td>
                                <%--Subscription Type--%>
                                <td>
                                    <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                        <s:select style="width: 140px;" cssClass="field input-md form-control" theme="simple" id="cs_type_%{#subscriptionStatus.index}"
                                                  name="model.subscriptionList[%{#subscriptionStatus.index}].cs_type" list="csTypeList"
                                                  listKey="keyData" listValue="valueData" value="%{#subscription.cs_type}"/>
                                    </s:if>
                                    <s:else>
                                        <s:select style="width: 140px;" cssClass="field input-md form-control" theme="simple" id="cs_type_%{#subscriptionStatus.index}"
                                                  name="model.subscriptionList[%{#subscriptionStatus.index}].cs_type" list="csTypeList"
                                                  listKey="keyData" listValue="valueData" value="%{#subscription.cs_type}" disabled="true"/>
                                        <s:hidden theme="simple" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_type" value="%{#subscription.cs_type}"/>
                                    </s:else>
                                </td>
                                <%--Subscription Verified By--%>
                                <td>
                                    <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                        <s:textfield id="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_by" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_by"  theme="simple"
                                                     size="15" value='%{#subscription.cs_verified_by}' cssClass="input-md form-control" />                            
                                        <script language="javascript">
                                            <%--teaC_ = "<s:property value='%{getHc(actionClassName_,"User")}'/>";--%>
                                            lookup("Search Internal User", "InternalUser", "us_user_name,us_user_id", "model.subscriptionList[${subscriptionStatus.index}].us_user_name,model.subscriptionList[${subscriptionStatus.index}].cs_verified_by",
                                                    "useSetup_InternalUser", "us_user_id,us_user_name", "us_user_name", "", "");
                                        </script>
                                        <%--<s:hidden name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_by" value="%{#subscription.cs_verified_by}" />--%>
                                    </s:if>
                                    <s:else>
                                        <s:property value='%{#subscription.cs_verified_by}'/>
                                        <s:hidden theme="simple" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_by" value="%{#subscription.cs_verified_by}"/>
                                    </s:else>
                                </td>
                                <%--Subscription Verified Date--%>
                                <td align="center">
                                    <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                        <s:textfield name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_date_str" value="%{#subscription.cs_verified_date_str}" theme="simple" cssClass="datepick-impian embed requiredField input-md" size="15" />
                                    </s:if>
                                    <s:else>
                                        <s:textfield name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_date_str" value="%{#subscription.cs_verified_date_str}" theme="simple" size="15" readonly="true" cssClass="input-md form-control"/>                                    

                                    </s:else>
                                </td>
                                <%--Subscription Status--%>
                                <td>
                                    <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                        <s:submit cssClass="defaultButton" theme="simple" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_type" onclick='setRowIndx(%{#subscriptionStatus.index})' action="approveSubscriptionPublicUser_" value="%{getText('button.approve')}" />
                                        <s:submit cssClass="defaultButton" theme="simple" onclick='setRowIndx(%{#subscriptionStatus.index})' action="rejectSubscriptionPublicUser_" value="%{getText('button.reject')}" />                                    
                                    </s:if>
                                    <s:else>
                                        <s:text name="user.subscription.%{#subscription.cs_verified_status}"/>
                                        <s:hidden theme="simple" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_status" value="%{#subscription.cs_verified_status}"/>
                                    </s:else>
                                </td>
                                <%--Subscription Remarks--%>
                                <td align="center">
                                    <s:textfield id="model.subscriptionList[%{#subscriptionStatus.index}].cs_remarks" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_remarks"  theme="simple"
                                                 size="20"           value='%{#subscription.cs_remarks}' cssClass="input-md form-control" /><br/>
                                    <%--<s:a theme="simple" href="resendAccountRequestPublicUser_?id=%{model.us_id}&cs=%{#subscription.cs_type}&idx=%{#subscriptionStatus.index}"><s:text name="resend.account.request.form"/></s:a><br/>--%>
                                    <s:if test='#subscription.cs_verified_status != subsPending && !#subscription.cs_verified_status.equals(#subsPending)' >
                                        <s:a theme="simple" href="resendActivationLinkPublicUser_?id=%{model.us_id}&cs=%{#subscription.cs_type}&idx=%{#subscriptionStatus.index}" ><s:text name="resend.account.activation.link"/></s:a>
                                    </s:if>
                                </td>                            
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>  
            </div>
        </div>
    </div><br><br>

    <div class="row">
        <div class="col-md-12">
            <h3 class="title-v3"><s:text name="user.userGroup" /></h3>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 form-row-margin">
            <s:if test="has_right('loadAddPublicUG')">
                <button class="btn btn-default" type="submit" name="action:loadAddPublicUGPublicUser_" id="loadAddPublicUGPublicUser_"><i class="fa fa-plus"></i><s:text name="button.add.user.group"/></button>     
                    <%--<s:submit id="addPublicUserGroupBtn" cssClass="defaultButton" theme="simple" action="loadAddPublicUGPublicUser_" value="%{getText('button.add.user.group')}" />--%>
                </s:if>
                <s:if test="has_right('processDeletePublicUG')">
                <button class="btn btn-default" type="submit" name="action:processDeletePublicUGPublicUser_" id="processDeletePublicUGPublicUser_" onclick="if (isCheckboxSelected(form.publicUG_selected)) {
                            return confirmDelete();
                        } else {
                            return false;
                        }"><i class="fa fa-trash-o"></i><s:text name="button.delete.user.group"/></button>     
                    <%--s:submit cssClass="defaultButton" theme="simple" action="processDeletePublicUGPublicUser_" value="%{getText('button.delete.user.group')}"
                              onclick="if ( isCheckboxSelected(form.publicUG_selected)) {return confirmDelete();} else {return false;}"/--%>
                </s:if>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="table-responsive">
                <table class="table table-condensed table-striped table-hover" cellspacing="1" cellpadding="1"  width="100%">
                    <thead>
                        <tr>
                            <th width="1%" align="center">
                                <s:hidden name="deletedUGItem" value="%{model.deletedUGItem}"/>    
                                <div class="checkbox check-success tableListingCheckbox">
                                    <s:if test="model.groupUserList.size() > 0">
                                        <input type="checkbox"  id="publicUG_select" class="selectAll" name="publicUG_select" onClick="toggleCheckboxByName(this,'publicUG_selected');">
                                        <!--<input type="checkbox" id="publicUG_select" name="publicUG_select" onclick="toggleCheckbox(this, delGp_ids);">-->
                                    </s:if>
                                    <s:else>
                                        <input type="checkbox" id="publicUG_select" class="selectAll" name="publicUG_select" disabled >
                                    </s:else>
                                    <label for="publicUG_select" class="tableFormCheckbox"></label>
                                </div>
                            </th>
                            <th width="40%"><s:text name="group.code"/></th>
                            <th width="59%"><s:text name="group.name"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator value="model.groupUserList" status="groupUserStatus" id="iteratorGroupUser" >
                            <tr class="<s:if test="#groupUserStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                                <s:hidden theme="simple" name="model.groupUserList[%{#groupUserStatus.index}].ug_user_id" value="%{#iteratorGroupUser.ug_user_id}" />
                                <s:hidden theme="simple" name="model.groupUserList[%{#groupUserStatus.index}].ug_id" value="%{#iteratorGroupUser.ug_id}" />
                                <td>
                                    <div class="checkbox check-success tableListingCheckbox">
                                        <input type="checkbox" name="publicUG_selected" class="checkbox_child" id="${groupUserStatus.index}" value="${groupUserStatus.index}" onclick="toggleSelectAll()">
                                        <label for="${groupUserStatus.index}" class="tableFormCheckbox"></label>
                                    </div>
                                    <%--<s:checkbox theme="simple" name="publicUG_selected" id="delGp_ids" fieldValue="%{#groupUserStatus.index}" onclick="checkToggleCheckbox(publicUG_select, delGp_ids)"/>--%>
                                </td>
                                <td><s:property value="%{#iteratorGroupUser.userGroup.group_code}" />
                                    <s:hidden theme="simple" name="model.groupUserList[%{#groupUserStatus.index}].userGroup.group_code" value="%{#iteratorGroupUser.userGroup.group_code}"/></td>
                                <td><s:property value="%{#iteratorGroupUser.userGroup.group_name}" />
                                    <s:hidden theme="simple" name="model.groupUserList[%{#groupUserStatus.index}].userGroup.group_name" value="%{#iteratorGroupUser.userGroup.group_name}"/></td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </div>
        </div>
    </div><br><br>


    <%--div class="panel panel-default ">
        <div class="panel-heading ">
            <h3 class="panel-title"> 
                <span class="titleText"><s:text name="Public User" /></span>
                <span class="titleActionTypeText">
                    | <s:text name="actionType.edit" />
                </span>
            </h3>
        </div>
        <div class="panel-body">    
            <s:set name="subsPending"><%=com.sains.common.util.SystemConstants.USERSUBSCRIPTION.STATUS.PENDING%></s:set>
            <s:hidden theme="simple" name="subsRowIndx_"/>
            <table class="table borderless form " cellspacing="1" cellpadding="1"  width="100%">
                <tr>
                    <td width="20px">&nbsp;</td>
                    <!--<td width="50px" align="left"></td>-->
                    <!--<td></td>-->
                    <td align="right">                    
                        <s:submit cssClass="defaultButton btn mrg-lr-5" theme="simple" action="processUpdatePublicUser_"  value="Update" onclick="return localValidateForm(this.form);"/>
                        <s:submit cssClass="defaultButton buttonCancel btn mrg-lr-5" theme="simple" action="cancelPublicUser_" value="Cancel"/>
                        <!--<s:submit type="submit" cssClass="defaultButton" theme="simple" action="activateUserPublicUser_" value="%{getText('button.activate.public.user')}" />-->
                    </td>
                </tr>
                <!-- <tr>
                     <td width="20px">&nbsp;</td>
                     <td width="150px" align="left"><s:text name="user.id"/></td>
                     <td>:</td>
                     <td><s:property value="%{us_user_id}"/></td>
                 </tr>-->
                <tr valign="center" height="30px">
                    <td width="20px">&nbsp;</td>
                    <td width="150px" colspan="1" class="label_imp">
                <u> <s:text name="qp.personal.info"/></u>
                </td>
                </tr>
                <tr>
                    <td >&nbsp;</td>
                    <!--<td  align="left" class="label_imp"><s:text name="user.title"/><jsp:include page="/pages/base/requiredField.jsp"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.title"/></label>  
                        <div class="col-sm-4"><s:select list="titleList" listKey="code_id" listValue="code_desc" theme="simple" id="us_title" name="model.us_title" value="%{model.us_title}" cssClass=" input-md form-control"/></div>
                    </td>
                </tr>
                <tr>
                    <td >&nbsp;</td>
                    <!--<td  align="left" class="label_imp"><s:text name="user.full.name"/><jsp:include page="/pages/base/requiredField.jsp"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.full.name"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                        <div class="col-sm-4"><s:textfield theme="simple" size="47" name="model.us_user_name" cssClass="requiredField input-md form-control" value="%{model.us_user_name}"/></DIV>
                    </td>
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <!--<td width="50px" align="left" class="label_imp"><s:text name="user.citizenship"/><jsp:include page="/pages/base/requiredField.jsp"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.citizenship"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                        <div class="col-sm-4"><s:select list="countryList" listKey="code_id" cssClass="requiredField input-md form-control" listValue="code_desc" theme="simple" id="model.us_nationality" name="model.us_nationality" value="%{model.us_nationality}" /></div>
                    </td>
                    <!--<s:textfield theme="simple" name="user_citizenship" value="%{model.us_nationality}"/></td>-->
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <!--<td width="50px" align="left" class="label_imp"><s:text name="user.ic.no"/><jsp:include page="/pages/base/requiredField.jsp"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.ic.no"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                        <div class="col-sm-4"><s:textfield theme="simple" name="model.us_id_number" cssClass="requiredField input-md form-control" value="%{model.us_id_number}"/></DIV>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <!--<td align="left" class="label_imp"><s:text name="user.dob"/><jsp:include page="/pages/base/requiredField.jsp"/></td>-->
                    <!--<td>:</td>-->                
                    <td><label class="col-lg-2 control-label"><s:text name="user.dob"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                        <div class="col-sm-4"><s:textfield name="model.us_dob_str" id="us_dob_str" value="%{model.us_dob_str}" theme="simple" cssClass="datepick-impian embed requiredField input-md" onfocus="populateDOB()"/></DIV>
                    </td> 
                </tr>
                <tr>
                    <td width="20px">&nbsp;</td>
                    <!--<td  class="label_imp" width="170px" align="left"><s:text name="user.pob"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.pob"/></label>
                        <div class="col-sm-4"><s:textfield theme="simple" size="47" maxlength='%{model.columnLengthMap.get("us_birth_place")}' name="model.us_birth_place" value="%{model.us_birth_place}" cssClass="input-md form-control"/></DIV>
                    </td>
                </tr>           
                <tr valign="center" height="30px">
                    <td >&nbsp;</td>
                    <td colspan="1" class="label_imp">
                <u> <s:text name="user.contactInfo"/></u>
                </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <!--<td class="label_imp" align="left"><s:text name="user.emailAddress"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.emailAddress"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                        <div class="col-sm-4"><s:textfield theme="simple" size="47" name="model.us_email" cssClass="requiredField input-md form-control" value="%{model.us_email}"/></DIV>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <!--<td class="label_imp" align="left"><s:text name="user.hpNo"/><jsp:include page="/pages/base/requiredField.jsp"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.hpNo"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                        <div class="col-sm-4">
                            <div class="input-group input-group-prForm">
                                <span class="input-group-addon"><s:textfield theme="simple" style="width: 30px" cssClass="requiredField input-md form-control" size="5" name="model.us_hp_country_code" id="countryCode" maxlength="5"  value="%{model.us_hp_country_code}"  readonly="true"/><!--model.us_hp_country_code--%=></SPAN>
                                <span class="input-group-addon"><img height="18" width="18" align="absmiddle" onclick="loadCodeDesc('COU', '!in 000')" alt="List Country Code" src="images/document_lookup.gif" style="cursor: pointer;" ></span>
                                <span class="input-group-btn input-group-addon"><s:textfield style="width: 220px"  theme="simple" name="model.us_hp_number" cssClass="requiredField input-md form-control" value="%{model.us_hp_number}"/></SPAN>
                            </div></div>
                    </td>
                </tr> 

                <tr>
                    <td>&nbsp;</td>
                    <!--<td class="label_imp" align="left"><s:text name="user.phoneNo"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.phoneNo"/></label>
                        <div class="col-sm-4">
                            <div class="input-group input-group-prForm">
                                <span class="input-group-addon"><s:textfield theme="simple"  style="width: 30px" size="5" name="model.us_phone_country_code" id="countryCode_p" maxlength="5"  value="%{model.us_phone_country_code}"  readonly="true" cssClass="input-md form-control"/><!--model.us_phone_country_code--></SPAN>
                                <span class="input-group-addon"><img height="18" width="18" align="absmiddle" onclick="loadCodeDesc('COU_P', '!in 000')" alt="List Country Code" src="images/document_lookup.gif" style="cursor: pointer;" > </SPAN>
                                <span class="input-group-addon"><s:textfield style="width: 220px" theme="simple" name="model.us_phone_number" value="%{model.us_phone_number}" cssClass="input-md form-control"/></SPAN>
                            </div></div>
                    </td>
                </tr> 
                <tr>
                    <td>&nbsp;</td>
                    <!--<td class="label_imp" align="left"><s:text name="user.faxNo"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.faxNo"/></label>
                        <div class="col-sm-4 ">
                            <div class="input-group input-group-prForm">
                                <span class="input-group-addon"><s:textfield theme="simple"  style="width: 30px" size="5" name="model.us_fax_country_code" id="countryCode_f" maxlength="5"  value="%{model.us_fax_country_code}"  readonly="true" cssClass="input-md form-control "/><!--model.us_fax_country_code--></SPAN>
                                <span class="input-group-addon"><img height="18" width="18" align="absmiddle" onclick="loadCodeDesc('COU_F', '!in 000')" alt="List Country Code" src="images/document_lookup.gif" style="cursor: pointer;" ></SPAN>
                                <span class="input-group-addon"><s:textfield style="width: 220px" theme="simple" name="model.us_fax_number" value="%{model.us_fax_number}" cssClass="input-md form-control"/></SPAN>
                            </div></div>
                    </td>
                </tr> 
                <tr valign="top">
                    <td></td>
                    <!--<td class="label_imp" align="left"><s:text name="user.address"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.address"/></label>
                        <div class="col-sm-4 ">
                            <s:textfield disabled="%{editMode_}" theme="simple" size="47"  name="model.us_mailing_address1" maxlength='%{model.columnLengthMap.get("us_mailing_address1")}' value="%{model.us_mailing_address1}" cssClass="input-md form-control"/>
                        </div>
                    </td>
                </tr>
                <tr valign="top">
                    <td></td>
                    <!--<td></td>-->
                    <!--<td></td>-->
                    <td><label class="col-lg-2 control-label">&nbsp;</label>
                        <div class="col-sm-4 ">
                            <s:textfield disabled="%{editMode_}" theme="simple" size="47"  name="model.us_mailing_address2" maxlength='%{model.columnLengthMap.get("us_mailing_address2")}' value="%{model.us_mailing_address2}" cssClass="input-md form-control"/>
                        </DIV>
                    </td>
                </tr>
                <tr valign="top">
                    <td></td>
                    <!--<td></td>-->
                    <!--<td></td>-->
                    <td><label class="col-lg-2 control-label">&nbsp;</label>
                        <div class="col-sm-4 ">
                            <s:textfield disabled="%{editMode_}" theme="simple" size="47"  name="model.us_mailing_address3" maxlength='%{model.columnLengthMap.get("us_mailing_address3")}' value="%{model.us_mailing_address3}" cssClass="input-md form-control"/>
                        </div>
                    </td>
                </tr>
                <tr valign="top">
                    <td></td>
                    <!--<td></td>-->
                    <!--<td></td>-->
                    <td><label class="col-lg-2 control-label">&nbsp;</label>
                        <div class="col-sm-4 ">
                            <s:textfield disabled="%{editMode_}" theme="simple" size="47"  name="model.us_mailing_address4" maxlength='%{model.columnLengthMap.get("us_mailing_address4")}' value="%{model.us_mailing_address4}" cssClass="input-md form-control"/>
                        </div>
                    </td>
                </tr>

                <tr valign="top">
                    <td>&nbsp;</td>
                    <!--<td class="label_imp" align=left><s:text name="user.postcode"/><jsp:include page="/pages/base/requiredField.jsp"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.postcode"/></label>
                        <div class="col-sm-4 ">
                            <s:textfield disabled="%{editMode_}" theme="simple" size="47" cssClass="input-md form-control" maxlength='%{model.columnLengthMap.get("us_mailing_postcode")}' name="model.us_mailing_postcode" value="%{model.us_mailing_postcode}"/>
                        </div>
                    </td>
                </tr> 

                <tr valign="top">
                    <td>&nbsp;</td>
                    <td id="stateByTown">
                        <label class="col-lg-2 control-label"><s:text name="user.state"/></label>
                        <div class="col-sm-4 ">
                            <!--<s:select disabled="%{editMode_}"  cssClass="requiredField" id="us_mailing_state" list="stateList" listKey="code_id" listValue="code_desc" theme="simple" name="model.us_mailing_state" value="%{model.us_mailing_state}" onchange="populateList('loadTwnBySttProfile', this.value+'&required=1&outputID=us_mailing_city&currentMenuButton_=PI', 'model.us_mailing_city', 'twnByStt');selectCountry('us_nationality', this); "/>-->
                            <s:select disabled="%{editMode_}"  cssClass="input-md form-control" id="us_mailing_state" list="stateList" listKey="code_id" listValue="code_desc" theme="simple" name="model.us_mailing_state" value="%{model.us_mailing_state}" onchange="populateList('loadTwnBySttProfile', this.value+'&outputID=us_mailing_city&currentMenuButton_=PI', 'model.us_mailing_city', 'twnByStt'); "/>
                        </div>
                    </td>
                </tr> 
                <tr valign="top">
                    <td>&nbsp;</td>
                    <td>
                        <label class="col-lg-2 control-label"><s:text name="user.city"/></label>
                        <!--<div id="twnByStt"><s:select disabled="%{editMode_}"  id="us_mailing_city" list="twnBySttList_" listKey="code_id" listValue="code_desc" theme="simple" name="model.us_mailing_city" value="model.us_mailing_city" onchange="populateList('loadSttByTwnProfile', this.value+'&required=1&outputID=us_mailing_state&currentMenuButton_=PI', 'model.us_mailing_state', 'stateByTown'); selectCountry('us_nationality', this);"/>-->
                        <div id="twnByStt">
                            <div class="col-sm-4 "><s:select disabled="%{editMode_}"  id="us_mailing_city" list="twnBySttList_" listKey="code_id" listValue="code_desc" theme="simple" name="model.us_mailing_city" value="model.us_mailing_city" onchange="populateList('loadSttByTwnProfile', this.value+'&outputID=us_mailing_state&currentMenuButton_=PI', 'model.us_mailing_state', 'stateByTown'); " cssClass="input-md form-control"/></div>
                        </DIV>
                    </td>

                </tr> 
                <tr>
                    <td>&nbsp;</td>
                    <!--<td align="left"></td>-->
                    <!--<td></td>-->
                    <td></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <!--<td></td>-->
                    <!--<td></td>-->
                    <td class="label_imp" align="left"><u><s:text name="user.login.detail"/></u></td>
                </tr> 
                <tr>
                    <td>&nbsp;</td>
                    <!--<td class="label_imp" align="left"><s:text name="user.status"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.status"/></label>
                        <div class="col-sm-4 ">
                            <s:select name="model.us_status" theme="simple" list="statusList" listKey="keyData" listValue="valueData" value="%{model.us_status}" cssClass="input-md form-control"/>
                            <!--<s:select name="us_status" theme="simple" list="statusList" listKey="keyData" listValue="valueData" value="%{model.UserModel.us_status}" cssClass="input-md form-control"/>-->
                            <!--<s:textfield theme="simple" name="model.us_status" value="%{model.us_status}"/>-->
                        </div>
                    </td>
                </tr> 
                <tr>
                    <td>&nbsp;</td>
                    <!--<td class="label_imp" align="left"><s:text name="user.lastLogin"/></td>-->
                    <!--<td>:</td>-->
                    <td>
                        <label class="col-lg-2 control-label"><s:text name="user.lastLogin"/></label>
                        <div class="col-sm-4 ">
                            <s:if test='model.us_last_login_date != null || model.us_last_login_date.equals("")'>                        
                                <s:text name="date_default_datetime"><s:param value="%{model.us_last_login_date}"/></s:text></td>
                        </s:if>
                    </div>
                </tr> 
                <tr>
                    <td>&nbsp;</td>
                    <!--<td class="label_imp" align="left"><s:text name="user.lastFailLogin"/></td>-->
                    <!--<td>:</td>-->
                    <td><label class="col-lg-2 control-label"><s:text name="user.lastFailLogin"/></label>
                        <div class="col-sm-4 ">
                            <s:if test='model.us_fail_login_date != null || model.us_fail_login_date.equals("")'>
                                <s:text name="date_default_datetime"><s:param value="%{model.us_fail_login_date}"/></s:text>
                            </s:if>
                        </DIV>
                    </td>
                </tr> 
                <tr>
                    <td>&nbsp;</td>
                    <!--<td class="label_imp" align="left"><s:text name="user.failAttempt"/></td>-->
                    <!--<td>:</td>-->
                    <td>
                        <label class="col-lg-2 control-label"><s:text name="user.failAttempt"/></label>
                        <div class="col-sm-4 ">
                            <s:property value="%{model.us_fail_attempt_count}"/>
                        </DIV>
                    </td>
                </tr> 
                <tr><td>&nbsp;</td></tr>
            </table>
            <br/>
            <table width="100%" class="form">
                <tr class="CLASS_TABLE_HEADER" >
                    <td class="header_2"><span class="imgArrowRight" /><span class="header_2Text"><s:text name="user.subscription" /></span></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td colspan="2">
                        <s:if test="has_right('loadAddApplicationPage')">
                            <s:submit cssClass="defaultButton" theme="simple" action="loadAddSubscriptionPublicUser_" value="%{getText('button.add')}" />
                        </s:if>
                        <s:if test="has_right('processDeleteApplication')">
                            <s:submit cssClass="defaultButton" theme="simple" action="processDeleteSubscriptionUserGroup" value="%{getText('button.delete')}"
                                      onclick="if ( isCheckboxSelected(delApp_ids)) {return confirmDelete();} else {return false;}" />
                        </s:if>
                    </td>
                </tr>
            </table>   
            <table class="defaultTable table borderless" cellspacing="1" cellpadding="1"  width="100%">
                <tr>
                    <th width="1%" align="center">
                        <s:if test="model.subscriptionList.size() > 0">
                            <input type="checkbox" id="cbselect" name="cbselect" onclick="toggleCheckbox(this, delApp_ids);" >
                        </s:if>
                        <s:else>
                            <input type="checkbox" id="cbselect" name="cbselect" disabled >
                        </s:else>
                    </th>
                    <th width="15%"><s:text name="lbl.date.received"/><jsp:include page="/pages/base/requiredField.jsp"/></th>
                    <th width="10%"><s:text name="lbl.subscription.type"/></th>
                    <th width="15%"><s:text name="lbl.verified.by"/></th>
                    <th width="15%"><s:text name="lbl.verified.date"/><jsp:include page="/pages/base/requiredField.jsp"/></th>
                    <th width="16%"><s:text name="lbl.verify.status"/></th>
                    <th width="15%"><s:text name="lbl.remarks"/></th>

                </tr>
                <s:iterator value="model.subscriptionList" status="subscriptionStatus" id="userSubs" var="subscription">
                    <tr class="<s:if test="#subStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                        <!--Checkbox-->                            
                        <td align="center">
                            <!--<s:checkbox theme="simple" name="selected" id="delApp_ids" fieldValue="%{#subscriptionStatus.index}" onclick="checkToggleCheckbox(cbselect, delApp_ids)"/>-->
                            <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                <s:checkbox theme="simple" name="subscription_selected" id="delSubcribe_ids"  fieldValue="%{#subscriptionStatus.index}"
                                            onclick="checkToggleCheckbox(subscription_select, delSubcribe_ids)" />
                            </s:if>
                            <s:else>
                                <s:checkbox theme="simple" name="subscription_selected" id="delSubcribe_ids"  fieldValue="%{#subscriptionStatus.index}"
                                            onclick="checkToggleCheckbox(subscription_select, delSubcribe_ids)" disabled="true"/>
                            </s:else>
                            <s:hidden name="model.subscriptionList[%{#subscriptionStatus.index}].cs_id" value="%{#subscription.cs_id}"/>
                        </td>
                        <!--Subscription Date Received-->
                        <td>
                            <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                <s:textfield name="model.subscriptionList[%{#subscriptionStatus.index}].cs_date_received_str"  theme="simple"
                                             size="15"  id="strCs_date_received_%{#subscriptionStatus.index}" value='%{#subscription.cs_date_received_str}' cssClass="datepick-impian embed requiredField input-md "/>
                            </s:if>
                            <s:else>
                                <s:textfield name="model.subscriptionList[%{#subscriptionStatus.index}].cs_date_received_str"  theme="simple"
                                             size="15"  id="strCs_date_received_%{#subscriptionStatus.index}" value='%{#subscription.cs_date_received_str}' readonly="true" cssClass="input-md form-control"/>
                            </s:else>
                        </td>
                        <!--Subscription Type-->
                        <td>
                            <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                <s:select style="width: 140px;" cssClass="field input-md form-control" theme="simple" id="cs_type_%{#subscriptionStatus.index}"
                                          name="model.subscriptionList[%{#subscriptionStatus.index}].cs_type" list="csTypeList"
                                          listKey="keyData" listValue="valueData" value="%{#subscription.cs_type}"/>
                            </s:if>
                            <s:else>
                                <s:select style="width: 140px;" cssClass="field input-md form-control" theme="simple" id="cs_type_%{#subscriptionStatus.index}"
                                          name="model.subscriptionList[%{#subscriptionStatus.index}].cs_type" list="csTypeList"
                                          listKey="keyData" listValue="valueData" value="%{#subscription.cs_type}" disabled="true"/>
                                <s:hidden theme="simple" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_type" value="%{#subscription.cs_type}"/>
                            </s:else>
                        </td>
                        <!--Subscription Verified By-->
                        <td align="center">
                            <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                <s:textfield id="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_by" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_by"  theme="simple"
                                             size="15" value='%{#subscription.cs_verified_by}' cssClass="input-md form-control" />                            
                                <script language="javascript">
                                    <!--teaC_ = "<s:property value='%{getHc(actionClassName_,"User")}'/>";-->
                                    lookup("Search Internal User", "InternalUser", "us_user_name,us_user_id", "model.subscriptionList[${subscriptionStatus.index}].us_user_name,model.subscriptionList[${subscriptionStatus.index}].cs_verified_by",
                                            "useSetup_InternalUser", "us_user_id,us_user_name", "us_user_name", "", "");
                                </script>
                                <!--<s:hidden name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_by" value="%{#subscription.cs_verified_by}" />-->
                            </s:if>
                            <s:else>
                                <s:property value='%{#subscription.cs_verified_by}'/>
                                <s:hidden theme="simple" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_by" value="%{#subscription.cs_verified_by}"/>
                            </s:else>
                        </td>
                        <!--Subscription Verified Date-->
                        <td align="center">
                            <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                <s:textfield name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_date_str" value="%{#subscription.cs_verified_date_str}" theme="simple" cssClass="datepick-impian embed requiredField input-md" size="15" />
                            </s:if>
                            <s:else>
                                <s:textfield name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_date_str" value="%{#subscription.cs_verified_date_str}" theme="simple" size="15" readonly="true" cssClass="input-md form-control"/>                                    

                            </s:else>
                        </td>
                        <!--Subscription Status-->
                        <td align="center">
                            <s:if test='#subscription.cs_verified_status == subsPending || #subscription.cs_verified_status.equals(#subsPending)' >
                                <s:submit cssClass="defaultButton" theme="simple" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_type" onclick='setRowIndx(%{#subscriptionStatus.index})' action="approveSubscriptionPublicUser_" value="%{getText('button.approve')}" />
                                <s:submit cssClass="defaultButton" theme="simple" onclick='setRowIndx(%{#subscriptionStatus.index})' action="rejectSubscriptionPublicUser_" value="%{getText('button.reject')}" />                                    
                            </s:if>
                            <s:else>
                                <s:text name="user.subscription.%{#subscription.cs_verified_status}"/>
                                <s:hidden theme="simple" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_verified_status" value="%{#subscription.cs_verified_status}"/>
                            </s:else>
                        </td>
                        <!--Subscription Remarks-->
                        <td align="center">
                            <s:textfield id="model.subscriptionList[%{#subscriptionStatus.index}].cs_remarks" name="model.subscriptionList[%{#subscriptionStatus.index}].cs_remarks"  theme="simple"
                                         size="20"           value='%{#subscription.cs_remarks}' cssClass="input-md form-control" /><br/>
                            <!--<s:a theme="simple" href="resendAccountRequestPublicUser_?id=%{model.us_id}&cs=%{#subscription.cs_type}&idx=%{#subscriptionStatus.index}"><s:text name="resend.account.request.form"/></s:a><br/>-->
                            <s:if test='#subscription.cs_verified_status != subsPending && !#subscription.cs_verified_status.equals(#subsPending)' >
                                <s:a theme="simple" href="resendActivationLinkPublicUser_?id=%{model.us_id}&cs=%{#subscription.cs_type}&idx=%{#subscriptionStatus.index}" ><s:text name="resend.account.activation.link"/></s:a>
                            </s:if>
                        </td>                            
                    </tr>

                </s:iterator>
            </table>    
            <br/>
            <!-- USER GROUP SECTION -->   
            <table width="100%" class="form">
                <tr class="CLASS_TABLE_HEADER" >
                    <td class="header_2"><span class="imgArrowRight" /><span class="header_2Text"><s:text name="user.userGroup" /></span></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td colspan="2">
                        <s:if test="has_right('loadAddApplicationPage')">
                            <s:submit cssClass="defaultButton" theme="simple" action="loadAddSubscriptionPublicUser_" value="%{getText('button.add')}" />
                        </s:if>
                        <s:if test="has_right('processDeleteApplication')">
                            <s:submit cssClass="defaultButton" theme="simple" action="processDeleteSubscriptionUserGroup" value="%{getText('button.delete')}"
                                      onclick="if ( isCheckboxSelected(delApp_ids)) {return confirmDelete();} else {return false;}" />
                        </s:if>
                    </td>
                </tr>
            </table>   
            <table width="100%" class="table borderless">
                <!--//Add Public User Group, Delete Public User Group [Button]-->
                <tr><td><s:if test="has_right('loadAddPublicUG')">
                            <s:submit id="addPublicUserGroupBtn" cssClass="defaultButton" theme="simple" action="loadAddPublicUGPublicUser_" value="%{getText('button.add.user.group')}" />
                        </s:if>
                        <s:if test="has_right('processDeletePublicUG')">
                            <s:submit cssClass="defaultButton" theme="simple" action="processDeletePublicUGPublicUser_" value="%{getText('button.delete.user.group')}"
                                      onclick="if ( isCheckboxSelected(form.publicUG_selected)) {return confirmDelete();} else {return false;}"/>
                        </s:if></td></tr>
            </table>
            <table class="defaultTable table borderless" cellspacing="1" cellpadding="1"  width="100%">
                <tr><th width="1%" align="center">
                        <s:hidden name="deletedUGItem" value="%{model.deletedUGItem}"/>                    
                        <s:if test="model.groupUserList.size() > 0">
                            <input type="checkbox" id="publicUG_select" name="publicUG_select" onclick="toggleCheckbox(this, delGp_ids);">
                        </s:if>
                        <s:else>
                            <input type="checkbox" id="publicUG_select" name="publicUG_select" disabled >
                        </s:else></th>
                    <th width="40%"><s:text name="group.code"/></th>
                    <th width="59%"><s:text name="group.name"/></th></tr>
                        <s:iterator value="model.groupUserList" status="groupUserStatus" id="iteratorGroupUser" >
                    <tr class="<s:if test="#groupUserStatus.odd == true ">odd</s:if><s:else>even</s:else>">
                        <s:hidden theme="simple" name="model.groupUserList[%{#groupUserStatus.index}].ug_user_id" value="%{#iteratorGroupUser.ug_user_id}" />
                        <s:hidden theme="simple" name="model.groupUserList[%{#groupUserStatus.index}].ug_id" value="%{#iteratorGroupUser.ug_id}" />
                        <td><s:checkbox theme="simple" name="publicUG_selected" id="delGp_ids" fieldValue="%{#groupUserStatus.index}" onclick="checkToggleCheckbox(publicUG_select, delGp_ids)"/></td>
                        <td><s:property value="%{#iteratorGroupUser.userGroup.group_code}" />
                            <s:hidden theme="simple" name="model.groupUserList[%{#groupUserStatus.index}].userGroup.group_code" value="%{#iteratorGroupUser.userGroup.group_code}"/></td>
                        <td><s:property value="%{#iteratorGroupUser.userGroup.group_name}" />
                            <s:hidden theme="simple" name="model.groupUserList[%{#groupUserStatus.index}].userGroup.group_name" value="%{#iteratorGroupUser.userGroup.group_name}"/></td>
                    </tr>
                </s:iterator>
            </table>


    <%--<table>
    <tr><td></td><td colspan="3">Company Profile</td></tr>
    <tr><td>&nbsp;</td></tr>

<tr>
    <td></td>
    <td align="left"><s:text name="co.reg.num"/></td>
    <td>:</td>
    <td><s:textfield theme="simple" name="custCompanyModel.co_reg_num" value="%{custCompanyModel.co_reg_num}"/></td>
</tr>
<tr>
    <td></td>
    <td align="left"><s:text name="co.name"/></td>
    <td>:</td>
    <td><s:textfield theme="simple" name="custCompanyModel.co_name" value="%{custCompanyModel.co_name}"/></td>                       
</tr>
<tr>
    <td></td>
    <td align="left"><s:text name="co.addr"/></td>
    <td>:</td>
    <td><s:textfield theme="simple" name="custCompanyModel.co_registered_address1" value="%{custCompanyModel.co_registered_address1}"/></td>
</tr>
<tr>
    <td></td>
    <td align="left"></td>
    <td>:</td>
    <td><s:textfield theme="simple" name="custCompanyModel.co_registered_address2" value="%{custCompanyModel.co_registered_address2}"/></td>
</tr>
<tr>
    <td></td>
    <td align="left"></td>
    <td>:</td>
    <td><s:textfield theme="simple" name="custCompanyModel.co_registered_address3" value="%{custCompanyModel.co_registered_address3}"/></td>
</tr>
<tr>
    <td></td>
    <td align="left"></td>
    <td>:</td>
    <td><s:textfield theme="simple" name="custCompanyModel.co_registered_address4" value="%{custCompanyModel.co_registered_address4}"/></td>
</tr>
<tr>
    <td></td>
    <td align="left"><s:text name="user.postcode"/></td>
    <td>:</td>
    <td><s:textfield theme="simple" name="custCompanyModel.co_registered_postcode" value="%{custCompanyModel.co_registered_postcode}"/></td>
</tr>
<tr>
    <td></td>
    <td align="left"><s:text name="user.city"/></td>
    <td>:</td>
    <td><s:textfield theme="simple" name="custCompanyModel.co_registered_city" value="%{custCompanyModel.co_registered_city}"/></td>
</tr>
<tr>
    <td width="20px">&nbsp;</td>
    <td width="50px" align="left"><s:text name="user.state"/></td>
    <td>:</td>
    <td>
        <s:select list="stateList" listKey="code_id" listValue="code_desc" theme="simple" id="custCompanyModel.co_registered_state" name="custCompanyModel.co_registered_state" value="%{custCompanyModel.co_registered_state}" /></td>
</tr>
<tr>
    <td width="20px">&nbsp;</td>
    <td width="50px" align="left"></td>
    <td></td>
    <td>
         <s:submit type="submit"  theme="simple" action="processUpdatePublic" value="%{getText('button.update')}" />
         <s:submit type="submit"  theme="simple" action="activateUserPublic" value="%{getText('button.activate.public.user')}" />
    </td>
</tr>
</table>-->
</div>
</div--%>
</form>
<script language="Javascript">
    populateDOB();
</script>