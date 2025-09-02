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
        <script src="f7/js/framework7.bundle.js" type="text/javascript"></script>
        <script src="f7/bubbleshell-1.1.0.min.js"></script>
        <script src="include/jquery/jquery-3.4.1.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                var appConfig = {
                    'application_name': 'bubbleshell',
                    'application_version': '1.0.0',
                    'device_type': bsGetMobileOperatingSystem(),
//			'device_type': android / ios,
                    'language': 'en'
                };
                var bsObj = new bubbleshell(appConfig);
                var temp = {
			'sys_module': 'AUTHTOKEN',
			'sys_type': 'get',
			'sys_request_code': 99,
		};
		bsObj.perform(temp);
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
        </script>
    </head>
    <body>
<!--        <div id="app">
            <div class="view view-main">-->
        <form action="<s:property value='serverUrl'/>landingBS" name="form" id="sampleFormId" class="" method="post">
            <s:hidden name="__MOONLIGHT_TOKEN__" id="__MOONLIGHT_TOKEN__"/>
        </form>
<!--            </div>
            </div>-->
    </body>
    <script type="text/javascript" src="f7/f7/app.js"></script>
</html>
