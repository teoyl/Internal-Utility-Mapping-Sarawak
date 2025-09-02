<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, mininum-scale=1,
              user-scalable=no, minimal-ui, viewport-fit=cover"/>
        <title>Sample</title>
        <link href="f7/css/framework7.bundle.css" rel="stylesheet"/>
        <link href="f7/css/app.css" rel="stylesheet"/>
        <link rel="stylesheet" href="uppy/v1.15.0/uppy.min.css">
        <style>
            .hidden {
                display: none !important;
            }
            .item-inner-multiFile {
                padding-right: calc(var(--f7-list-item-padding-horizontal) + var(--f7-safe-area-right));
                width: 100%;
            }
        </style>
        <script src="f7/js/framework7.bundle.js" type="text/javascript"></script>
        <script src="f7/bubbleshell-1.1.0.min.js"></script>
        <script src="include/jquery/jquery-3.4.1.min.js"></script>
        <script src="uppy/v1.15.0/uppy.min.js"></script>
        <script type="text/javascript" language="javascript" src="pages/scripts/common.js"></script>
        <script type="text/javascript">
            var $$ = Dom7;
            var bsObj;
            var serverUrl = "<s:property value='serverUrl'/>";
            $(document).ready(function () {
//                alert("landing : document.ready");
//                setInterval()
                var appConfig = {
                    'application_name': 'bubbleshell',
                    'application_version': '1.0.0',
                    'device_type': bsGetMobileOperatingSystem(),
//			'device_type': android / ios,
                    'language': 'en'
                };
                bsObj = new bubbleshell(appConfig);
                appBiometricReaderAuthentication();
//                alert('mltk = ' + localStorage.getItem('__MOONLIGHT_TOKEN__'));
                if (localStorage.getItem('__MOONLIGHT_TOKEN__') === null || localStorage.getItem('__MOONLIGHT_TOKEN__') === "") {
//                    alert('set token');
                    var temp = {
			'sys_module': 'AUTHTOKEN',
			'sys_type': 'set',
			'sys_request_code': 99,
			'sys_value': '<s:property value="#session.__MOONLIGHT_TOKEN__"/>'
                    };
                    bsObj.perform(temp);
                    localStorage.setItem('__MOONLIGHT_TOKEN__', "<s:property value="#session.__MOONLIGHT_TOKEN__"/>");
                    localStorage.setItem('__MOONLIGHT_REFRESH_TOKEN__', "<s:property value="#session.__MOONLIGHT_REFRESH_TOKEN__"/>");
                    
                }
                <s:if test="isActivateFingerPrint">
                    if (localStorage.getItem('fpl') === null) {
                        localStorage.setItem('fpl', 'Y'); <%-- FingerPrintLogin enabled, remove if disable --%>
                        localStorage.setItem('fpl_id', '<s:property value="#session.user_id"/>');
                        localStorage.setItem('fpl_pk', '<s:property value="fplPublicKey"/>');
                        localStorage.setItem('fpl_deviceId', '<s:property value="bsDeviceId"/>');
                        localStorage.setItem('fpEnabled', 'true');
                    }
                </s:if>
                if (localStorage.getItem('fpEnabled') === 'true') {
                    $("#enableFingerPrint").css("display", "none");
                }
            });

            function bsAuthTokenCallback(status, requestCode, result) {	
		//Pop out message to check
                if (result==="") {
                    $("#sampleFormId").submit();
                } else {
                    localStorage.setItem('__MOONLIGHT_TOKEN__', result);
                    $("#__MOONLIGHT_TOKEN__").val(localStorage.getItem('__MOONLIGHT_TOKEN__'));
                }
                $("#sampleFormId").submit();
            }
            
            function doLogout() {
                localStorage.removeItem("__MOONLIGHT_TOKEN__");
                localStorage.removeItem("__MOONLIGHT_REFRESH_TOKEN__");
                document.location = serverUrl + "logoutBS";
            }
            
            function activateFingerPrintLogin() {
                document.location = serverUrl + "activateFingerPrintBS";
            }
            
            function doLogout() {
                localStorage.removeItem("__MOONLIGHT_TOKEN__");
                localStorage.removeItem("__MOONLIGHT_REFRESH_TOKEN__");
                var temp = {
			'sys_module': 'AUTHTOKEN',
			'sys_type': 'set',
			'sys_request_code': 99,
			'sys_value': ''
		};
		bsObj.perform(temp);
                document.location = serverUrl + "logoutBS";
            }

            function appOpenQRCodeReader(scannerReqCode) {
                var temp = {
                    'sys_module': 'QRCODE',
                    'sys_request_code': scannerReqCode,
                };
                bsObj.perform(temp);
            }

            function bsQRCodeReaderCallback(status, requestCode, result) {
                //Pop out message to check
                if (requestCode === 99) {
                    if (result !== "") {
                        $("#scanner99").text(result);
                    } else {
                        $("#scanner99").text("");
                    }
                } else if (requestCode === 98) {
                    if (result !== "") {
                        $("#scanner98").text(result);
                    } else {
                        $("#scanner98").text("");
                    }
                }
            }
            
            function loadPreview(event) {
		var reader = new FileReader();
		reader.onload = function() {
			var output = document.getElementById('preview');
			output.src = reader.result;
		};
		reader.readAsDataURL(event.target.files[0]);
            }
            
            var hasBiometricReader = false;
            function appBiometricReaderAuthentication() {
                var temp = {
                        'sys_module': 'BIOMETRICAUTH',
                        'sys_request_code': 99,
                };
                bsObj.perform(temp);
            }
            function bsBiometricReaderAuthenticationCallback(status, requestCode, result) {	
                //Pop out message to check
                if (status === 1) {
                    hasBiometricReader = true;
                }
            }
            function appOpenBiometricReader() {
		var temp = {
			'sys_module': 'BIOMETRIC',
			'sys_request_code': 99,
		};
		bsObj.perform(temp);
            }
            function bsBiometricReaderCallback(status, requestCode, result) {	
                //Pop out message to check
                alert(
                        'status = ' + status +
                        'requestCode = ' + requestCode +
                        'result = ' + result 
                );		
            }

            function appOpenPage(openUrl) {
                var temp = {
                    'sys_module': 'OPENPAGE',
                    'sys_title': 'This is job listing',
                    'sys_toolbar': true,
                    'sys_toolbar_refresh_button': true,
                    'sys_toolbar_back_button': true,
                    'sys_toolbar_back_button_icon': 'close',
                    'sys_url': serverUrl + "redirectBS?actualUrl=" + openUrl
                    
                };
                bsObj.perform(temp);
            }
            function downloadPage(url) {
                alert(serverUrl + "redirectBS?actualUrl="+serverUrl+url);
                var temp = {
                    'sys_module': 'OPENPAGE',
                    'sys_title': 'Download file',
                    'sys_toolbar': true,
                    'sys_toolbar_refresh_button': false,
                    'sys_toolbar_back_button': true,
                    'sys_toolbar_back_button_icon': 'close',
                    'sys_url': serverUrl + "redirectBS?actualUrl="+serverUrl+url
                    
                };
                bsObj.perform(temp);
            }
            function appOpenDownloadPage() {
                var temp = {
                    'sys_module': 'OPENPAGE',
                    'sys_title': 'Download pdf',
                    'sys_toolbar': true,
                    'sys_toolbar_refresh_button': false,
                    'sys_toolbar_back_button': true,
                    'sys_toolbar_back_button_icon': 'close',
                    'sys_url': serverUrl + "redirectBS?actualUrl="+serverUrl+"loadPDFBS"
                    
                };
                bsObj.perform(temp);
            }
            function appOpenDownload2Page() {
                var temp = {
                    'sys_module': 'OPENPAGE',
                    'sys_title': 'Download pdf 2s',
                    'sys_toolbar': true,
                    'sys_toolbar_refresh_button': false,
                    'sys_toolbar_back_button': true,
                    'sys_toolbar_back_button_icon': 'close',
                    'sys_url': serverUrl + "redirectBS?actualUrl="+serverUrl+"loadPDF2BS"
                    
                };
                bsObj.perform(temp);
            }
        </script>
    </head>
    <body>
        <!--        <div id="app">
                    <div class="view view-main">-->
        <!--<form action="doLoginBS" name="form" id="sampleFormId" class="" method="post">-->
        <div class="view view-main view-init safe-areas">
            <div class="page">
                <div class="page-content">
                    <div class="list">
                        <ul>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <div class="item-title">Welcome, <s:property value="#session.user_name"/></div>
                                    </div>
                                    <div class="item-media"><i class="icon f7-icons">person</i></div>
                                </div>
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <a class="">File name here</a>
                                    </div>
                                    <div class="item-media"><a onclick="alert(123);"><i class="icon f7-icons">trash</i></a></div>
                                </div>
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <s:include value="/base/uppyIncludeSingleFile_bs.jsp">
                                            <s:param name="uploadUrl_">uppyUploadSample</s:param>
                                            <s:param name="uppyFieldName_">file1</s:param>
                                            <s:param name="uppyHiddenName_">uppySample2FileId</s:param>
                                            <s:param name="hideArrow">Y</s:param>
                                            <s:param name="uppyFileList" value="testList"/>
                                            <s:param name="uploadParams">drAppCode_=uppySample2&uploadRecordId_=<s:property value="%{yourModel.ID}"/></s:param>
                                            <s:param name="uploadedFileName"><s:property value="uploadedFile_fileName"/></s:param>
                                            <s:param name="uploadedFileId"><s:property value="uploadedFile_fileId"/></s:param>
                                            <s:param name="theRecordId"><s:property value="yourModel.ID"/></s:param>
                                            <s:param name="allowedFileTypes">'image/*','application/pdf'</s:param>
                                        </s:include>
                                    </div>
                                    <div class="file1im item-media"></div>
                                </div> 
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner-multiFile">
                                        <s:include value="/base/uppyIncludeMultipleFile_bs.jsp">
                                            <s:param name="uploadUrl_">uppyUploadSample</s:param>
                                            <s:param name="uppyFieldName_">multi1</s:param>
                                            <s:param name="uppyHiddenName_">multipleFileDrDocIds</s:param>
                                            <s:param name="hideArrow">Y</s:param>
                                            <s:param name="maxNumberOfFiles">3</s:param>
                                            <s:param name="uploadParams">drAppCode_=uppySample3&uploadRecordId_=<s:property value="%{yourParentModel.ID}"/></s:param>
                                            <s:param name="uppyFileListName">childList</s:param>
                                            <s:param name="uploadedFileName"><s:property value="uploadedFile_fileName"/></s:param>
                                            <s:param name="uploadedFileId"><s:property value="uploadedFile_fileId"/></s:param>
                                            <s:param name="theRecordId"><s:property value="yourParentModel.ID"/></s:param>
                                            <s:param name="allowedFileTypes">'image/*','application/pdf'</s:param>
                                            <s:param name="allowedFileTypesDesc"><s:text name="imagesAndPDF"/></s:param>
                                            <%--<s:param name="delBtnMarginTop">6px</s:param>--%>
                                        </s:include>
                                    </div>
                                    <div class="multi1im item-media"></div>
                                </div> 
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <a onclick="appOpenPage('openPage1BS?testId=123')">
                                            Open Job Listing
                                        </a>
                                    </div> 
                                </div> 
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <a onclick="appOpenDownloadPage()">
                                            Download PDF
                                        </a>
                                    </div> 
                                </div> 
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <a onclick="appOpenDownload2Page()">
                                            Download PDF (2)
                                        </a>
                                    </div> 
                                </div> 
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <a onclick="appOpenQRCodeReader(99)">
                                            Scanner 1
                                        </a>
                                    </div> 
                                </div> 
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <a onclick="appOpenQRCodeReader(98)">
                                            Scanner 2
                                        </a>
                                    </div> 
                                </div> 
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <a onclick="appOpenBiometricReader()">
                                            Finger Print
                                        </a>
                                    </div> 
                                </div> 
                            </li>
                            <li id="enableFingerPrint">
                                <div class="item-content">
                                    <div class="item-inner">
                                        <a onclick="activateFingerPrintLogin()">
                                            Activate Finger Print for Login
                                        </a>
                                    </div> 
                                </div> 
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <input id='imageInput' style='display: none;' accept='image/*' type='file' 
                                        onchange='loadPreview(event)' >
                                        <img id='preview' style='width: 100%; height: 100%; border-radius: 1px;' onclick="$('#imageInput').click()">
                                    </div> 
                                </div> 
                            </li>
                            <li>
                                <div class="item-content">
                                    <a onclick="doLogout()">
                                        Logout
                                    </a>
                                </div> 
                            </li>

                        </ul>
                    </div>
                    <div class="list">
                        <ul>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <div class="item-title">Scanner 1 Result:</div>
                                        <div class="item-after">
                                            <font id='scanner99'></font>
                                        </div>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div class="item-content">
                                    <div class="item-inner">
                                        <div class="item-title">Scanner 2 Result:</div>
                                        <div class="item-after">
                                            <font id='scanner98'></font>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <form name="sampleFormId" id="sampleFormId" method="POST" class="" >
            <input type="hidden" name="__MOONLIGHT_TOKEN__" id="__MOONLIGHT_TOKEN__" value=""/>
        </form>
        <div id="downloadDiv" style="visibility: hidden">
        </div>
        <!--</form>-->
        <!--            </div>
                    </div>-->
    </body>
    <script src="f7/js/app.js" type="text/javascript"></script>
</html>
