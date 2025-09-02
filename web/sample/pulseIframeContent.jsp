<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sample</title>
        <script language="JavaScript" type="text/javascript" src="include/crypto/jsencrypt.js"></script>
        <script type="text/javascript">
            function sendPulse() {
//                var posting = $.post("https://127.0.0.1:8113/forNewProject/pulse", $("#pulseForm").serialize());
//                posting.done(function(data) {
//                    if (data.startsWith("error:")) {
//                        alert(data.substring(6));
//                    } else {
//                    }
//                });
//                $.ajax({
//                    type: "POST",
//                    url: "https://127.0.0.1:8113/forNewProject/pulse",
//                    data: "data",
//                    success: "success"
//                  });
                  
                $("#pulseSender").load("http://127.0.0.1:8080/forNewProject/pulse",
                    function (message) {
                        if (message === "Expired") {
                            document.location = "initLogin";
                        } else {
                                alert(message);
                        }
                    });
            }
            function doEncrypt() {
                var encrypt = new JSEncrypt();
                encrypt.setPublicKey($('#publicKey').val());
                var encrypted = encrypt.encrypt($('#inputData').val());
                $('#outputData').val(encrypted)
            }
            
            function doDecrypt() {
                var decrypt = new JSEncrypt();
                decrypt.setPrivateKey($('#privateKey').val());
                var decrypted = decrypt.decrypt($('#inputData').val());
                $('#outputData').val(decrypted)
            }
            $(document).ready(function () {
//            $('#checkSession').on('submit', function(e) {
//                $('#checkSession').attr("action", "http://10.17.101.219:8080/forNewProject/itemChangeLoader");
////                    e.preventDefault();
//                    $.ajax({
//                        url : $(this).attr('action')//,
////                        xhrFields: { withCredentials: true }//,
//                    });
//                });
//            });
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form id="checkSession" action="itemChangeLoader">
            <input type="text" name="field1"/>
            <input type="hidden" name="itemValue" value="1"/>
            <input type="hidden" name="itemCate" value="DivDis"/>
            <input type="submit" name="Submit"/>
        </form>
</body>
</html>
