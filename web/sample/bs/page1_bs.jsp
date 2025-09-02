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
        <script src="include/jquery/jquery-3.4.1.min.js"></script>
        <script src="f7/bubbleshell-1.0.0.min.js"></script>
        <script type="text/javascript">
            var bsObj;
            $(document).ready(function () {
                var appConfig = {
			'application_name': 'bubbleshell',
			'application_version': '1.0.0',
			'device_type': 'android',
//			'device_type': android / ios,
			'language': 'en' 
		};
                bsObj = new bubbleshell(appConfig);
                localStorage.setItem('__MOONLIGHT_TOKEN__', "<s:property value="#session.__MOONLIGHT_TOKEN__"/>");
                localStorage.setItem('__MOONLIGHT_REFRESH_TOKEN__', "<s:property value="#session.__MOONLIGHT_REFRESH_TOKEN__"/>");
            });
            
            function appOpenPage(openUrl) {
		var temp = {
			'sys_module': 'OPENPAGE',
			'sys_title': 'Job Listing',
			'sys_toolbar': true,
			'sys_toolbar_refresh_button': true,
			'sys_toolbar_back_button': true,
			'sys_toolbar_back_button_icon': 'close',
			'sys_url': openUrl
		};
		bsObj.perform(temp);
	}
        </script>
    </head>
    <body>
<!--        <div id="app">
            <div class="view view-main">-->
        <form action="doLoginBS" name="form" id="sampleFormId" class="" method="post">
            <div class="list simple-list">
                <ul>
                    <li>
                        Hello, <s:property value="#session.user_name"/>. This is your job listing
                    </li>
                </ul>
            </div>
            
            <div class="list">
                <ul>
                    <li>
                        <div class="item-content">
                            <div class="item-media"><i class="icon f7-icons">list</i></div>
                            <div class="item-inner">
                                <div class="item-title">Application 1 Checklist</div>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="item-content">
                            <div class="item-media"><i class="icon f7-icons">document_check</i></div>
                            <div class="item-inner">
                                <div class="item-title">Leave approval (Mr ABC)</div>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="item-content">
                            <div class="item-media"><i class="icon f7-icons">document_check</i></div>
                            <div class="item-inner">
                                <div class="item-title">Leave approval (Mr XYZ)</div>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="item-content">
                            <div class="item-media"><i class="icon f7-icons">compose</i></div>
                            <div class="item-inner">
                                <div class="item-title">Application 3 Verification</div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            
        </form>
<!--            </div>
            </div>-->
    </body>
    <script type="text/javascript" src="f7/js/app.js"></script>
</html>
