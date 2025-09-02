<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript" src="pages/scripts/validation.jsp"></script>
        <script type="text/javascript" src="pages/scripts/confirmation.js"></script>
        <script type="text/javascript" src="pages/scripts/lookup.js"></script>
        <script type="text/javascript" src="pages/scripts/controls.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/windowfiles/dhtmlwindow.js"></script>
        <script type="text/javascript" src="include/dhtmlwindow/modalfiles/modal.js"></script>
        <script type="text/javascript" src="pages/scripts/combo_AJAX.js"></script>
        <script type="text/javascript" src="include/inforLoader.js"></script>
        <link rel="stylesheet" href="include/dhtmlwindow/windowfiles/dhtmlwindow.css" />
        <link rel="stylesheet" href="include/dhtmlwindow/modalfiles/modal.css" />
        <style type="text/css">
            .captcha { background: url("images/login/Captcha1.jpg") no-repeat; } 
        </style>
        <!--sereneChye@ 16/11/2016-->
        <jsp:include page="/include/jquery-datepicker/datepick.impian.jsp"></jsp:include>
        <script language="javascript">
            $(document).ready(function() {
                initDatePicker();
                Captcha();
            });
        </script>
        
        <!--<script type="text/javascript" src="include/popcalendar.js"></script>-->
<!--        <script language="javascript">
                InitCalendar2("images/", false);
        </script>-->
        <SCRIPT language="Javascript">
            function localValidateForm(form, operation) {
                
                var errors = new Array();
                validateRequired(form, errors);                
                emailCheck(document.getElementById("us_email"), "<s:text name='user.emailAddress' />", errors);
                if ( document.getElementById("us_password").value !== ""){
                    <%--ICTU GUIDE: PASSWORD ATLEAST 12 CHARACTERS--%>
                        passwordCheck(form.us_password, "Password", errors, 8, 16);
                    if (trim(document.getElementById("confirm_password").value) !== trim(form.us_password.value)) {
                            errors[errors.length] = formatText(messageValueNotMatch, "<s:text name='user.newPassword'/>", "<s:text name='user.confirmPassword'/>");
                     }
                    <%--ICTU GUIDE: PASSWORD CANNOT BE SAME AS USER ID--%>
                    if (trim(form.us_id_number.value).toUpperCase() == trim(form.us_password.value).toUpperCase()) {
                        errors[errors.length] = "<s:text name='errors.passwordSameAsUserID'/>";
                    }
                }

                if (errors.length > 0) {
                    alert(errors.join('\n'));
                    setFocus(form);
                }
                return errors.length > 0 ? false : true;
            }

            function required() {
//                this.aa = new Array("us_user_id", "<s:text name='user.id' />");
                this.aa = new Array("model.us_user_name", "<s:text name='user.full.name' />");
                this.ab = new Array("model.us_nationality", "<s:text name='user.citizenship' />");
                this.ac = new Array("model.us_id_number", "<s:text name='user.ic.no' />")
                this.al = new Array("model.us_dob_str", "<s:text name='user.dob' />")
                this.ad = new Array("model.us_preferred_contact", "<s:text name='user.prefered.contact' />");
                this.ae = new Array("model.us_email", "<s:text name='user.emailAddress' />");
                this.af = new Array("model.us_hp_country_code", "<s:text name='country.tel.code' />");
                this.ag = new Array("model.us_hp_number", "<s:text name='user.hpNo' />");
                this.ah = new Array("us_password", "<s:text name='user.password' />");
                this.ai = new Array("_strNew_us_password", "<s:text name='user.confirmPassword' />");
                if (document.getElementById("co_reg_num").value !== "" || document.getElementById("co_name").value !== ""){
                    this.aj = new Array("custCompanyModel.co_reg_num", "<s:text name='co.reg.num' />");
                    this.ak = new Array("custCompanyModel.co_name", "<s:text name='co.name' />");
                }
            }

            function validateItems(form, errors) {
                if (typeof (form.co_reg_num.type) == "string") {
                    validateItem(form.co_reg_num, form.co_name, 1, errors);
                } else {
                    var isError = false;
                    for (var i = 0; i < form.co_reg_num.length; i++) {
                        isError = validateItem(form.co_reg_num[i], form.co_name[i], i + 1, errors);
                        if (isError)
                            break;
                    }
                }
            }

            function validateItem(selected, idx, errors) {
                if (co_reg_num.value == "") {
                    errors[errors.length] = formatText(messageItemized, idx,
                            formatText(messageRequired, "<s:text name='user.coRegNum' />"));
                    isError = true;
                }
                if (co_name.value == "") {
                    errors[errors.length] = formatText(messageItemized, idx,
                            formatText(messageRequired, "<s:text name='user.coName' />"));
                    isError = true;
                }
            }

            function upperCase(x)
            {
                var y = document.getElementById(x).value;
                document.getElementById(x).value = y.toUpperCase();
            }

            function checkNewIC() {
                var strNewIC = document.getElementById("us_id_number").value;
                if (document.getElementById("us_nationality").value === 'MY' && strNewIC !== '') {
                    if (strNewIC.length === 12) {
                        if (!isNaN(strNewIC)) {
                            if (IsValidNewIC(strNewIC, "<s:text name='lbl.registration.number'/>")) {
                             //   document.getElementById("us_id_number").value = FormatNewIC(strNewIC);
                            } else {
                                return false;
                            }
                        }//else alert("not number");
                    } else
                    if (strNewIC.length === 14) {
                        if (strNewIC.indexOf("-") > 2) {
                            if (IsValidNewIC(strNewIC, "<s:text name='lbl.registration.number'/>")) {
                           //     document.getElementById("us_id_number").value = FormatNewIC(strNewIC);
                            } else {
                                return false;
                            }
                        }//else{alert("no dash")};

                    }
                }
            }

            <%--added by ahmadni 13/12/11--%>
            function formatIcNo() {
                strNewIC = document.getElementById("us_id_number").value;
                //document.getElementById("us_id_number").value = FormatNewIC(strNewIC);

            }

            function alphanumeric1(inputtxt)
            {
                var letters = /^[0-9a-zA-Z]+$/;
                if (inputtxt.value.match(letters))
                {
                    return true;
                }
                else
                {
                    alert('<s:text name="lbl.alphanumeric.only"/>');
                    return false;
                }
            }
            
            function loadCodeDesc(pType, pCode1Filter) {
                    var varTitle = "Country Code Listing";                    

                    winInst=dhtmlmodal.open("popup1", "iframe", "populatePublicCodeRegistration?codeType="+pType+"&code1Filter="+pCode1Filter, varTitle, "width=700px,height=350px,resize=1,scrolling=1,center=1", "");
                    winInst.onclose=function(){                        
                        return true;
                    }

                }                
                
              function populateDOB() {
                var strNewIC = document.getElementById("us_id_number").value;
                if (document.getElementById("us_nationality").value === 'COU-MYS' && strNewIC !== '') {
                    if (strNewIC.length === 12) {
                        if (!isNaN(strNewIC)) {
                            if (IsValidNewIC(strNewIC, "<s:text name='lbl.registration.number'/>")) {
                                var year = strNewIC.substring(0,2);
                                var month = strNewIC.substring(2,4);
                                var day = strNewIC.substring(4,6);                                
                                var dob = day+"/"+month+"/19"+year;
                                document.getElementById("us_dob_str").value = dob;
                            } else {
                                return false;
                            }
                        }//else alert("not number");
                    } else
                    if (strNewIC.length === 14) {
                        if (strNewIC.indexOf("-") > 2) {
                            if (IsValidNewIC(strNewIC, "<s:text name='lbl.registration.number'/>")) {
                               var year = strNewIC.substring(0,2);
                                var month = strNewIC.substring(2,4);
                                var day = strNewIC.substring(4,6);                                
                                var dob = day+"/"+month+"/19"+year;
                                document.getElementById("us_dob_str").value = dob;
                            } else {
                                return false;
                            }
                        }//else{alert("no dash")};

                    }
                }

            }
             
            function populateList(method, inputValue, outputName, writeTo) {
                var url = method + "?searchItem" + "=" + inputValue + "&outputName=" + outputName;
                initiateAJAX(url, writeTo, "", "");
            }
            
            function initiateAJAX(url, writeTo, action){
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
            
             function loadInputValue(method, inputValue, writeTo) {
                var url = method + "?searchItem" + "=" + inputValue;
                initiateAJAX(url, writeTo, "loadInputValue");
            }
         function isCheckedDeclare(){  
            
               var isValid = false;

               alert(document.getElementById('declaration').checked);
               if(document.getElementById('declaration').checked){
                    isValid = true;
               }else{
                    alert("<s:text name='qp.app.declaration.error' />");
               }

              return isValid;
          }
        //serene @ 28-Feb-2017 : reset form
        function resetFields(form) {
            var noOfElements = form.elements.length;
            for (var i = 0; i < noOfElements; i++) {
                clearValue(form.elements[i]);
            }
        }
        function Captcha(){
//            var alpha = new Array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');
            var alpha = new Array('0','1','2','3','4','5','6','7','8','9');
            var i;
            var code = '';
            for (i=0;i<6;i++){
//              var a = alpha[Math.floor(Math.random() * alpha.length)];
//              var b = alpha[Math.floor(Math.random() * alpha.length)];
//              var c = alpha[Math.floor(Math.random() * alpha.length)];
//              var d = alpha[Math.floor(Math.random() * alpha.length)];
//              var e = alpha[Math.floor(Math.random() * alpha.length)];
//              var f = alpha[Math.floor(Math.random() * alpha.length)];
//              var g = alpha[Math.floor(Math.random() * alpha.length)];
              code = code + alpha[Math.floor(Math.random() * alpha.length)] + ' ';
             }
           //var code = a + ' ' + b + ' ' + ' ' + c + ' ' + d + ' ' + e + ' '+ f + ' ' + g;
           document.getElementById("mainCaptcha").value = code;
           document.getElementById('mainCaptchaLabel').innerHTML = code;
           var imageNo = Math.floor(Math.random() * 4);
           document.getElementById('captchaTable').style.backgroundImage = "url('images/login/Captcha"+imageNo+".jpg')";
           
           
         }
        function ValidCaptcha(){
            var string1 = removeSpaces(document.getElementById('mainCaptcha').value);
            var string2 = removeSpaces(document.getElementById('txtInput').value);
            if (string1 == string2){
              return true;
            }
            else{        
              return false;
            }
        }
        function removeSpaces(string){
          return string.split(' ').join('');
        }
        </SCRIPT>
        <%--<s:head />--%>

    </head>
    <%@taglib uri="/struts-dojo-tags" prefix="sx"%>
    <sx:head parseContent="true" debug="false" />
    <body onload="Captcha();">
        <jsp:include page="/pages/base/actionError.jsp"></jsp:include>
        <jsp:include page="/include/jquery-datepicker/datepick.impian.jsp"></jsp:include>
            <form action="" name="userGroupForm" method="post" class="prForm">
                
          <!--Added by IvyL 10th Nov-->
         <div class="panel panel-default ">
          <div class="panel-heading ">
            <h3 class="panel-title"> 
                <span class="titleText"><s:text name="signup" /></span>
                <span class="titleActionTypeText">
                    |Fill in the form
                </span>
            </h3>
          </div>
          
          <div class="panel-body">
            <s:hidden name="action" />
            <s:hidden name="id" value="%{model.us_id}" />
            <s:hidden theme="simple" name="strTc_version"/>

            <!--Added by Ivy 10th Nov 2016-->
            <!--Button-->
            <table border="0" width="99%" cellpadding="0" class="datatable" style="margin:5px;">		
                <tr>
                    <td align="right" colspan="7">
                        <%--<s:reset cssClass="defaultButton dynamic-pull btn mrg-lr-5" type="reset" theme="simple" value="%{getText('button.resetForm')}"/>--%>
                        <s:submit cssClass="defaultButton" theme="simple" type="submit" value='%{getText("button.reset")}' onclick="resetFields(this.form)"/>
                        <s:reset cssClass="defaultButton dynamic-pull btn mrg-lr-5" type="reset" theme="simple" value="%{getText('button.resetForm')}"/>
                        <s:submit cssClass="defaultButton dynamic-pull btn mrg-lr-5" type="button"  theme="simple" action="initLogin" value="%{getText('button.cancel')}" /> 
                        <s:submit cssClass="defaultButton dynamic-pull btn mrg-lr-5" type="button"  theme="simple" action="processInsertRegistration" value="%{getText('qp.button.nxt')}" onclick="return checkNewIC(),isCheckedDeclare(), localValidateForm(this.form)"/>
                    </td>
                </tr>
            </table>
            
            
               <div class="sub_header_bg " ><span class="imgArrowRight"/><span class="header_2Text"><s:text name="qp.personal.info"/></span></div>
                <table class="table borderless form even" style="padding: 10px;border-collapse: initial;">
                    <tr><td> 
                        <label class="col-lg-2 control-label"><s:text name="user.title"/></label>
                        <s:select list="titleList" listKey="code_id" listValue="code_desc" theme="simple" id="us_title" name="model.us_title" value="%{model.us_title}"  disabled="%{editMode_}" cssClass="input-md form-control" /></td>
                    </td></tr>                  
                    <%--<tr><td> 
                        <label class="col-lg-2 control-label"><s:text name="user.profession"/></label>
                        <s:select list="regQualificationList" listKey="code_id" listValue="code_desc" theme="simple" id="us_profession" name="model.us_profession" value="%{model.us_profession}"  disabled="%{editMode_}" cssClass="input-md form-control" /></td>
                    </td></tr>--%>
                    <tr><td> 
                        <label class="col-lg-2 control-label"><s:text name="user.full.name"/><jsp:include page="/pages/base/requiredField.jsp"/></label>
                        <s:textfield theme="simple" name="model.us_user_name" value="%{model.us_user_name}" cssClass="requiredField input-md form-control" disabled="%{editMode_}" onKeyPress="return checkEnterKey(event);"/>
                    </td></tr>
                    <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.citizenship"/><jsp:include page="/pages/base/requiredField.jsp"/> </label>
                         <s:select list="countryList" listKey="code_id" listValue="code_desc" theme="simple" id="us_nationality" name="model.us_nationality" value="%{model.us_nationality}" cssClass="requiredField input-md form-control" disabled="%{editMode_}" />
                    </td></tr>
                     <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.ic.no"/><jsp:include page="/pages/base/requiredField.jsp"/> </label>
                         <s:textfield theme="simple" id="us_id_number" name="model.us_id_number" value="%{model.us_id_number}" onchange="populateDOB()" cssClass="requiredField input-md form-control" disabled="%{editMode_}"/>
                     </td></tr>
                     <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.dob"/><jsp:include page="/pages/base/requiredField.jsp"/> </label>
                        <div class="col-lg-3" style="padding-left: 0px;">
                        <s:textfield theme="simple" name="model.us_dob_str" id="us_dob_str" value='%{model.us_dob_str}' cssClass="datepick-impian embed_2 requiredField input-md form-control"  disabled="%{editMode_}" />
                        </div>
                        <!--                        <img theme="simple" alt="" src="images/calendar.gif" id="imgCalTo2" style=""
                             onclick="popUpCalendar(this, document.getElementById('us_dob_str'), '<s:text name="date_default_date_popup" />')"   
                             title="Calendar" align="absmiddle" height="18" width="18"/>-->
                     </td></tr>
                     <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.pob"/></label>
                        <s:textfield theme="simple" id="us_birth_place" name="model.us_birth_place" value="%{model.us_birth_place}"  disabled="%{editMode_}" cssClass="input-md form-control"/>
                     </td></tr>
                      <%--//Preferred Contact--%>
                      <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.prefered.contact"/><jsp:include page="/pages/base/requiredField.jsp"/> </label>
                       <s:select theme="simple" name="model.us_preferred_contact" list="preferredContactOption" listKey="keyData" listValue="valueData" value="%{model.us_preferred_contact}"  cssClass="requiredField input-md form-control" disabled="%{editMode_}"/>
                     </td></tr>
                      <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.emailAddress"/><jsp:include page="/pages/base/requiredField.jsp"/> </label>
                        <s:textfield theme="simple" id="us_email" name="model.us_email" value="%{model.us_email}"  cssClass="requiredField input-md form-control" disabled="%{editMode_}"/>
                      </td></tr>
                      <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.hpNo"/><jsp:include page="/pages/base/requiredField.jsp"/> </label>
                        <div class="input-group input-group-prForm"><!--sereneChye@ 16/11/2016-->
                            <span class="input-group-addon"><input style="width: 30px" type="text" size="5" name="model.us_hp_country_code" id="countryCode" maxlength="5"  value="<s:property value='%{model.us_hp_country_code}'/>"  readonly="true"  cssClass="requiredField input-md form-control"/></span>
                            <span class="input-group-addon"><img height="18" width="18" align="absmiddle" onclick="loadCodeDesc('COU', '!in 000')" alt="List Country Code" src="images/document_lookup.gif" style="cursor: pointer;" ></span>
                            <div class="input-group-btn input-group-addon"><!--sereneChye@ 16/11/2016-->
                                <s:textfield style="width: 220px" theme="simple" name="model.us_hp_number" value="%{model.us_hp_number}" onKeyPress="return checkEnterKey(event);" cssClass="requiredField input-md form-control"/>
                            </div>
                        </div>
                      </td></tr>
                      <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.phoneNo"/></label>
                         <div class="input-group input-group-prForm"><!--sereneChye@ 16/11/2016-->
                            <span class="input-group-addon"><input style="width: 30px" type="text" size="5" name="model.us_phone_country_code" id="countryCode_p" maxlength="5"  value="<s:property value='%{model.us_phone_country_code}'/>" readonly="true" cssClass="input-md form-control"/></span>
                            <span class="input-group-addon"><img height="18" width="18" align="absmiddle" onclick="loadCodeDesc('COU_P', '!in 000')" alt="List Country Code" src="images/document_lookup.gif" style="cursor: pointer;" ></span>
                            <div class="input-group-btn input-group-addon"><!--sereneChye@ 16/11/2016-->
                                <s:textfield style="width: 220px" theme="simple" name="model.us_phone_number" value="%{model.us_phone_number}" onKeyPress="return checkEnterKey(event);"  disabled="%{editMode_}" cssClass="input-md form-control"/>
                            </div>
                        </div>
                       </td></tr>
                     <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.faxNo"/></label>
                        <div class="input-group input-group-prForm"><!--sereneChye@ 16/11/2016-->
                            <span class="input-group-addon"><input style="width: 30px" type="text" size="5" name="model.us_fax_country_code" id="countryCode_f" maxlength="5"  value="<s:property value='%{model.us_fax_country_code}'/>" readonly="true" cssClass="input-md form-control"/></span>
                            <span class="input-group-addon"><img height="18" width="18" align="absmiddle" onclick="loadCodeDesc('COU_F', '!in 000')" alt="List Country Code" src="images/document_lookup.gif" style="cursor: pointer;" ></span>
                            <div class="input-group-btn input-group-addon"> <!--sereneChye@ 16/11/2016-->
                                <s:textfield style="width: 220px" theme="simple" name="model.us_fax_number" value="%{model.us_fax_number}" onKeyPress="return checkEnterKey(event);"  disabled="%{editMode_}" cssClass="input-md form-control"/>
                            </div>
                        </div>
                       </td></tr>
                     <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.password"/><jsp:include page="/pages/base/requiredField.jsp"/> </label>
                         <s:password theme="simple" name="us_password" value="%{model.us_password}" onKeyPress="return checkEnterKey(event);" onchange="vldtPassword(this.value, '%{getText('user.password')}', this, '8', '16', 'Y','Y')" disabled="%{editMode_}" cssClass="requiredField input-md form-control"/>
                         </td></tr>
                      <tr><td> 
                        <label class="col-lg-2 control-label"> <s:text name="user.confirmPassword"/><jsp:include page="/pages/base/requiredField.jsp"/> </label>
                        <s:password theme="simple" id="confirm_password" name="_strNew_us_password" value="%{model._strNew_us_password}" onKeyPress="return checkEnterKey(event);" disabled="%{editMode_}" cssClass="requiredField input-md form-control"/>
                         </td></tr>
                 </table>
                   
                    <!--Added by Ivy 10th Nov 2016-->
               <div class="sub_header_bg " ><span class="imgArrowRight"/><span class="header_2Text"><s:text name="co.title"/></span>
               </div>
               <table class="table borderless form even" style="padding: 10px;border-collapse: initial;">
                   <tr><td> 
                        <label class="col-lg-2 control-label"><s:text name="co.reg.num"/></label>
                        <s:textfield theme="simple" id="co_reg_num" name="custCompanyModel.co_reg_num" value="%{custCompanyModel.co_reg_num}"  cssClass="input-md form-control"/>
                    </td></tr>
                   <tr><td> 
                        <label class="col-lg-2 control-label"><s:text name="co.name"/></label>
                        <s:textfield theme="simple" id="co_name" name="custCompanyModel.co_name" value="%{custCompanyModel.co_name}" cssClass="input-md form-control"/>
                    </td></tr>
                   <tr><td> 
                        <label class="col-lg-2 control-label"><s:text name="co.addr"/></label>
                        <s:textfield maxLength="50" theme="simple" name="custCompanyModel.co_registered_address1" value="%{custCompanyModel.co_registered_address1}" disabled="%{editMode_}" cssClass="input-md form-control"/>
                    </td></tr>
                    <tr><td> 
                        <label class="col-lg-2 control-label">&nbsp;</label>
                        <s:textfield maxLength="50" theme="simple" name="custCompanyModel.co_registered_address2" value="%{custCompanyModel.co_registered_address2}"  disabled="%{editMode_}" cssClass="input-md form-control"/>
                    </td></tr>
                    <tr><td> 
                        <label class="col-lg-2 control-label">&nbsp;</label>
                        <s:textfield maxLength="50" theme="simple" name="custCompanyModel.co_registered_address3" value="%{custCompanyModel.co_registered_address3}" disabled="%{editMode_}" cssClass="input-md form-control"/>
                    </td></tr>
                     <tr><td> 
                        <label class="col-lg-2 control-label">&nbsp;</label>
                        <s:textfield maxLength="50" theme="simple" name="custCompanyModel.co_registered_address4" value="%{custCompanyModel.co_registered_address4}"  disabled="%{editMode_}" cssClass="input-md form-control"/>
                    </td></tr>
                    <tr><td> 
                        <label class="col-lg-2 control-label"><s:text name="user.postcode"/></label>
                        <s:textfield theme="simple" name="custCompanyModel.co_registered_postcode" value="%{custCompanyModel.co_registered_postcode}"  disabled="%{editMode_}" cssClass="input-md form-control" maxLength="5"/>
                    </td></tr>
                    <tr><td> 
                        <label class="col-lg-2 control-label"><s:text name="user.city"/></label>
                        <%--<s:textfield theme="simple" name="custCompanyModel.co_registered_city" value="%{custCompanyModel.co_registered_city}"  disabled="%{editMode_}" cssClass="input-md form-control"/>--%>
                        <div id="twnByStt"><s:select  cssClass=" input-md form-control" id="co_registered_city" list="twnBySttList_" listKey="code_id" listValue="code_desc" theme="simple" name="custCompanyModel.co_registered_city" value="custCompanyModel.co_registered_city" onchange="populateList('loadSttByTwnRegistration', this.value+'&required=&outputID=co_registered_state', 'custCompanyModel.co_registered_state', 'stateByTown');"/></div>
                    </td></tr>
                      <tr id="stateByTown"><td> 
                        <label class="col-lg-2 control-label"><s:text name="user.state"/></label>
                        <s:select cssClass=" input-md form-control" id="co_registered_state" list="stateList" listKey="code_id" listValue="code_desc" theme="simple" name="custCompanyModel.co_registered_state" value="custCompanyModel.co_registered_state" onchange="populateList('loadTwnBySttRegistration', this.value+'&required=&outputID=co_registered_city', 'custCompanyModel.co_registered_city', 'twnByStt'); "/>
                          <%--<s:select list="stateList" listKey="code_id" listValue="code_desc" theme="simple" id="custCompanyModel.co_registered_state" name="custCompanyModel.co_registered_state" value="%{custCompanyModel.co_registered_state}"   disabled="%{editMode_}" cssClass="input-md form-control" />--%>
                    </td></tr>
               </table>
          <table>
          <tr>
              <td colspan="2">
                 Text Captcha<br />
           </td>
          </tr>
          <tr >
           <td style="width:100px;">
               <!--<table class="captcha"  style="width:100px;height:50px;">-->
               <table id="captchaTable"  style="width:100px;height:50px;">    
                <tr>
                    <td align="center">
                        <label id="mainCaptchaLabel" style="color:white" />
                        <!--<input type="hidden" id="mainCaptcha"/>-->
                        <%--<s:hidden id="mainCaptcha" name="mainCaptcha" value="" />--%>
                        <!--<input type="text" id="mainCaptcha"/>-->
                        <!--<input type="button" id="refresh" value="Refresh" onclick="Captcha();" />-->
                    </td>
                </tr>
                </table>
                      
           </td>
           <td>
               <img src="images/login/refresh.png" style="width:25px;height:25px;" onclick="Captcha();" alt="Refresh"/>
             <!--<input type="button" id="refresh" value="Refresh" onclick="Captcha();" />-->
           </td>
          </tr>
          <tr>
           <td colspan="2">
               <s:hidden id="mainCaptcha" name="mainCaptcha" value="" />
            <input type="text" id="txtInput"/>    
          </td>
         </tr>
         <tr>
          <td colspan="2">
            <input id="Button1" type="button" value="Check" onclick="alert(ValidCaptcha());"/>
          </td>
        </tr>
      </table>
              <%-- <div class="sub_header_bg " ><span class="imgArrowRight"/><span class="header_2Text"><s:text name="qp.account.declaration"/></span></div>
               <table class="table borderless form even" style="padding: 10px;border-collapse: initial;">
                   <tr id="stateByTown">
                       <td width="5px"> 
                           <s:checkbox theme="simple" id="declaration" name="selected"  fieldValue="" value="" cssClass="" /></td>
                           <td><s:text name="qp.account.acknowledge.1"/><br/>
                           <s:text name="qp.account.acknowledge.2"/>
                       </td></tr>
               </table>--%>


<!--<table>
                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td align="left"></td>
                        <td></td>
                        <td>
                            <s:submit cssClass="defaultButton" type="button"  theme="simple" action="processInsertRegistration" value="%{getText('button.submit')}" onclick="return checkNewIC(), localValidateForm(this.form)"/>
                            <s:reset cssClass="defaultButton" type="reset" theme="simple" value="%{getText('button.reset')}"/>
                            <s:submit cssClass="defaultButton" type="button"  theme="simple" action="initLogin" value="%{getText('button.cancel')}" />      
                        </td>                            
                    </tr>
                     <tr><td colspan="4">&nbsp;</td></tr>
                </table>-->

            </div>
                        </div>
        </form>
    </body>
</html>