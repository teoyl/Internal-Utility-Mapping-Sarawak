<%--ThoTH @ 1-Apr-2014--%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="include/jquery.js"></script>
<script type="text/javascript" src="include/crypto/aes.js"></script>
<script type="text/javascript" src="include/crypto/pbkdf2.js"></script>
<script type="text/javascript" src="include/crypto/AesUtil.js"></script>
<script language="javascript">

    $(function(){
        $("#loginBtn").click(function(){
            <s:set name="myUrl" value=""/>
            <s:if test='systemType_.equals(@com.sains.common.util.SystemConstants$SYSTEM_TYPE@ESS)'>
                <s:set name="myUrl">ESS</s:set>
            </s:if>
            <%--$.getJSON('<s:url action="obtainKeyDataLogin%{#myUrl}" />',function(data) {
                var passPhrase = data.passPhrase;
                var iv = data.iv
                var salt = data.salt;
                var keySize = data.keySize;
                var iterationCount = data.iterationCount;
                var epwd = $('#passwd').val();
                var aesUtil = new AesUtil(keySize, iterationCount)
                var encrypted = aesUtil.encrypt(salt, iv, passPhrase, epwd);

                document.getElementById("passwd").value = encrypted;//commented by ahmadni due to error login in LIVE - 15-Mar-2017

//                $("#loginForm").submit();
                document.forms["loginForm"].submit();
            });--%>
                document.forms["loginForm"].submit();
        });
    });
    
    //added by ChangMH
    $(function(){
        $("#activeBtn").click(function(){
            <s:set name="myUrl" value=""/>
            <s:if test='systemType_.equals(@com.sains.common.util.SystemConstants$SYSTEM_TYPE@ESS)'>
                <s:set name="myUrl">ESS</s:set>
            </s:if>   
            if(localValidateForm(document.forms["loginForm"])){    
                $.getJSON('<s:url action="obtainKeyDataLogin%{#myUrl}" />',function(data) {
                    var passPhrase = data.passPhrase;
                    var iv = data.iv
                    var salt = data.salt;
                    var keySize = data.keySize;
                    var iterationCount = data.iterationCount;
                    var epwd = $('#passwd').val();
                    var epwd2 = $('#confirm_passwd_').val();
                    var aesUtil = new AesUtil(keySize, iterationCount)
                    var encrypted = aesUtil.encrypt(salt, iv, passPhrase, epwd);
                    var encrypted2 = aesUtil.encrypt(salt, iv, passPhrase, epwd2);

                    document.getElementById("passwd").value = encrypted;
                    document.getElementById("confirm_passwd_").value = encrypted2;

//                $("#loginForm").submit();
                    document.forms["loginForm"].submit();
                });
            } 
        });
    });
</script>