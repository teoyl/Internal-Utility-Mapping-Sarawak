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
            
            function newKeyPair() {
                divSubmitForm("newKeyPairSample", "sampleFormId", "newKeyPair");
            }
        </script>
    </head>
    <body>
        <jsp:include page="/pages/base/actionError.jsp"/>
        <form name="form" id="sampleFormId" class="" method="post">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4>Encryption <small>sample</small></h4>
                </div>
                <div class="panel-body">
                    
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label">Public Key</label>
                        <div class="col-md-5">
                            <input type="text" id='publicKey' class="form-control" name="publicKey" value="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJ9x0GLrsUnH36yorGMl0BQfuowPR9iXdrY5SXq/NKTTdzcb/4szCYEiSvelPR3Ia6zCMn6Txdv2GhlYbZfjIA8CAwEAAQ=="/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label">Private Key</label>
                        <div class="col-md-5">
                            <input type="text" id='privateKey' class="form-control" name="privateKey" value="MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAn3HQYuuxScffrKisYyXQFB+6jA9H2Jd2tjlJer80pNN3Nxv/izMJgSJK96U9HchrrMIyfpPF2/YaGVhtl+MgDwIDAQABAkAib2g8FP8jGiG6nmsstpvr3LZjTWGM65ld3Z16/xSVjCZHDhPczrEbNVmeOPs0cBeH0RwJApigBV3LcuapkntxAiEA5CvOJgkgrMkna/l/xGuPcV9YHd3OIo73BSg0UPHC/tsCIQCy5Cht/ACsxcD6fguLoBzDUf8wxNNasHlHf29jEfln3QIgRJnHCTiFUm3IJHaHK48LOZLo81HUwB8rtak40X1emzECIDTkUrPaK5Tpz1oAxIce6wIxD0OrHlaVEPa7zgI7wUMdAiAU9gDl5l+ZIv+ykZBZL5k0t+CMF7/7TaQeH62LS/y0nA=="/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label">Input Data</label>
                        <div class="col-md-5">
                            <input type="text" id='inputData' class="form-control" name="inputData" value="fMQP0jc2sh5DbWul3gEKrv5lt6bkFzagyEH10F5bFyyXJU/m0QKC8+kP3n+0/oEsDO18qqvGMFoGubbWTEuGkQ=="/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label">Output Data</label>
                        <div class="col-md-5">
                            <input type="text" id='outputData' class="form-control" name="outputData" value=""/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label">No. of Bit</label>
                        <div class="col-md-5">
                            <input type="text" id='noOfBit' class="form-control" name="noOfBit" value=""/>
                        </div>
                    </div>
                    <div class="form-horizontal form-group">
                        <label class="col-md-4 control-label">Action</label>
                        <div class="col-md-5 control-label">
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return doDecrypt();">
                                <i class="fa fa-plus"></i>Decrypt
                            </button>
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="return doEncrypt();">
                                <i class="fa fa-plus"></i>Encrypt
                            </button>
                            <button type="button" class="btn btn-primary" name="" id="actionName" onclick="newKeyPair();">
                                <i class="fa fa-plus"></i>Generate new Key Pair
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="newKeyPair" class="hidden"></div>
        </form>
</body>
</html>
