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
        <script src="f7/js/framework7.bundle.js" type="text/javascript" ></script>
        <script src="f7/bubbleshell-1.1.0.min.js"></script>
        <script src="include/jquery/jquery-3.4.1.min.js"></script>
        <script src="include/crypto/jsencrypt.js"></script>
        <script type="text/javascript">
            var bsObj;
            var serverUrl = "http://10.17.101.219:8080/training3/";
            $(document).ready(function () {
                $("#bsFPL_divice_id").val("");
                $("#bsFPL_secret").val("");
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
//                localStorage.clear();
                if (localStorage.getItem('__MOONLIGHT_TOKEN__') === null) {
                    localStorage.setItem('__MOONLIGHT_TOKEN__', "<s:property value="#session.__MOONLIGHT_TOKEN__"/>");
                    localStorage.setItem('__MOONLIGHT_REFRESH_TOKEN__', "<s:property value="#session.__MOONLIGHT_REFRESH_TOKEN__"/>");localStorage.setItem('__MOONLIGHT_TOKEN__', "<s:property value="#session.__MOONLIGHT_TOKEN__"/>");
                }
                localStorage.setItem('fpl_token', "<s:property value="fpl_token"/>");
            });
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
                    if (localStorage.getItem('fpEnabled') === "true") {
                        $("#fingerPrint").css("display", "block");
                    }
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
                if (status === 1) {
                    var encrypt = new JSEncrypt();
                    encrypt.setPublicKey(localStorage.getItem("fpl_pk"));
                    var encrypted = encrypt.encrypt(localStorage.getItem("fpl_token"));
                    $("#bsUserId").val(localStorage.getItem("fpl_id"));
                    $("#bsFPL_device_id").val(localStorage.getItem("fpl_deviceId"));
                    $("#bsFPL_secret").val(encrypted);
                    $("#sampleFormId").submit();
                }
            }
        </script>
    </head>
    <body>
<!--        <div id="app">
            <div class="view view-main">-->
        <form action="doLoginBS" name="form" id="sampleFormId" class="" method="post">
            <s:if test="actionErrors.size() > 0">
            <div class="list">
                <ul>
                    <s:actionerror theme="simple" escape="true"/>
                </ul>
            </div>
            </s:if>
            <div class="list inline-labels no-hairlines-md">
                <ul>
                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">Name</div>
                            <div class="item-input-wrap">
                                <input type="text" placeholder="Your name" name="bsUserId" id="bsUserId">
                                <span class="input-clear-button"></span>
                            </div>
                        </div>
                    </li>
                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">Password</div>
                            <div class="item-input-wrap">
                                <input type="password" placeholder="Your password" name="bsPswd">
                                <input type="hidden" name="bsFPL_secret" id="bsFPL_secret">
                                <input type="hidden" name="bsFPL_device_id" id="bsFPL_device_id">
                                <span class="input-clear-button"></span>
                            </div>
                        </div>
                    </li>
                    <li>
                        <input type="submit" class="button button-fill" value="Login">
                    </li>
                    <li>
                        <input type="button" class="button button-fill" value="Use Finger Print" id="fingerPrint" style="display:none" onclick="appOpenBiometricReader()">
                    </li>
<!--                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">E-mail</div>
                            <div class="item-input-wrap">
                                <input type="email" placeholder="Your e-mail">
                                <span class="input-clear-button"></span>
                            </div>
                        </div>
                    </li>
                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">URL</div>
                            <div class="item-input-wrap">
                                <input type="url" placeholder="URL">
                                <span class="input-clear-button"></span>
                            </div>
                        </div>
                    </li>
                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">Phone</div>
                            <div class="item-input-wrap">
                                <input type="tel" placeholder="Your phone number">
                                <span class="input-clear-button"></span>
                            </div>
                        </div>
                    </li>
                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">Gender</div>
                            <div class="item-input-wrap input-dropdown-wrap">
                                <select placeholder="Please choose...">
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                </select>
                            </div>
                        </div>
                    </li>
                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">Birthday</div>
                            <div class="item-input-wrap">
                                <input type="date" value="2014-04-30" placeholder="Please choose...">
                            </div>
                        </div>
                    </li>
                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">Date time</div>
                            <div class="item-input-wrap">
                                <input type="datetime-local" placeholder="Please choose...">
                            </div>
                        </div>
                    </li>
                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">Range</div>
                            <div class="item-input-wrap">
                                <div class="range-slider range-slider-init" data-label="true">
                                    <input type="range" value="50" min="0" max="100" step="1">
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">Textarea</div>
                            <div class="item-input-wrap">
                                <textarea placeholder="Bio"></textarea>
                            </div>
                        </div>
                    </li>
                    <li class="item-content item-input">
                        <div class="item-media">
                            <i class="icon demo-list-icon"></i>
                        </div>
                        <div class="item-inner">
                            <div class="item-title item-label">Resizable</div>
                            <div class="item-input-wrap">
                                <textarea class="resizable" placeholder="Bio"></textarea>
                            </div>
                        </div>
                    </li>-->
                </ul>
            </div>
        </form>
<!--            </div>
            </div>-->
    </body>
    <script type="text/javascript" src="f7/js/app.js"></script>
</html>
