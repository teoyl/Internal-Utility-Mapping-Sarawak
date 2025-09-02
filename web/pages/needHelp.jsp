<%-- 
    Document   : needHelp
    Created on : Sep 25, 2014, 12:00:57 PM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Perlu Bantuan</title>
        <script type="text/javascript" src="include/jquery_11.js"></script>
        <script language="javascript">
            $(document).ready(function() {
                $(".content_info").hide();

                $(".common_showHideBtn").click(function() {
                    var selectedIndex = $('.common_showHideBtn').index(this);

                    $(".content_info").eq(selectedIndex).slideToggle(function() {
        //                $(".allowance_showHideBtn").eq(selectedIndex).text('Papar');
                        $(".common_showHideBtn").eq(selectedIndex).text($(".common_showHideBtn").eq(selectedIndex).text() == '[Tutup]' ? '[Papar]' : '[Tutup]');
                    });
                });
            });
        </script>
    </head>
    <body >
        <div class="main-box-bg">
        <table >
            <tr>
                <td> 
                    <table cellpadding="3" cellspacing="0" width="100%" >
                        <tr class="supportTitle">
                            <td width="600px">1.0 Log Masuk</td>
                            <td id='papar' >
                                <span class="common_showHideBtn">[Papar]</span>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="content_info">
                <td>
                   <table class="xbox">
                        <tr><td class="supportContent"> Pengguna SarawakNet/ Pegawai Perkhidmatan Awam Negeri Sarawak (PANS).</td></tr>
                        <tr><td class="supportContentAns"> Sila log masuk dengan menggunakan ID Pengguna dan Kata Laluan (akaun emel) SarawakNet anda.</td></tr>
                        <tr><td height="15px"><!--SomeSpace--></td></tr>
                        <!--Commented by ChangMH @ 17-Nov-2014 :: Not applicable as all user will using ldap account-->
                        <!--<tr><td class="supportContent"> Pengguna Non-SarawakNet/ Pegawai Pihak Berkuasa Tempatan dan Badan Berkanun Negeri (PBT/BBN).</td></tr>
                        <tr><td class="supportContentAns"><u>Log Masuk Kali Pertama</u></td></tr>
                        <tr><td class="supportContentAns">Sila klik <a href="loadActivationPageLoginESS" target="_self">Permohonan Pengaktifan Akaun</a> untuk mengaktifkan ID Pengguna anda.</td></tr>
                        <tr><td class="supportContentAns"><u>Log Masuk</u></td></tr>
                        <tr><td class="supportContentAns">Sila log masuk dengan menggunakan No. Kad Pengenalan (tanpa sengkang) dan Kata Laluan anda.</td></tr>
                        <tr><td height="15px">SomeSpace</td></tr>-->
                    </table>
                </td>
            </tr>
            <!--Commented by ChangMH @ 17-Nov-2014 :: Not applicable as all user will using ldap account-->
            <!--<tr>
                <td> 
                    <table cellpadding="3" cellspacing="0" width="100%">
                        <tr class="supportTitle">
                            <td>2.0 Permohonan Pengaktifan Akaun</td>
                            <td align="right" id='papar'>
                                <span class="common_showHideBtn">[Papar]</span>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>-->
            <!--<tr class="content_info">
                <td>
                   <table class="xbox">
                        <tr><td class="supportContent"> Pengguna Non-SarawakNet</td></tr>
                        <tr><td class="supportContentAns">Akaun anda perlu disahkan sebelum anda dapat log masuk ke sistem SCS-HRA. 
                                Sila klik <a href="loadActivationPageLoginESS" target="_self">sini </a>untuk memasukkan No. Kad Pengenalan/ No. Pasport (untuk bukan warganegara) anda 
                                dan selepas sistem mengesahkan pewujudan profil peribadi anda, satu <i>Activation Link</i> akan dihantar ke emel pejabat anda. </td></tr>
                        <tr><td class="supportContentAns">Klik pautan <i>Activation Link</i> di emel pejabat anda untuk akses ke skrin penukaran Kata Laluan. </td></tr>
                        <tr><td height="15px">SomeSpace</td></tr>
                    </table>
                </td>
            </tr>-->
            <tr>
                <td> 
                    <table cellpadding="3" cellspacing="0" width="100%">
                        <tr class="supportTitle">
                            <td>2.0 Lupa Kata Laluan?</td>
                            <td align="right" id='papar'>
                                <span class="common_showHideBtn">[Papar]</span>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
             <tr class="content_info">
                <td>
                   <table class="xbox">
                        <tr><td class="supportContent">Pengguna SarawakNet</td></tr>
                        <tr><td class="supportContentAns">Sila akses ke sistem <a href="https://fim.sarawak.gov.my/" target="_blank"> FIMS</a> (https://fim.sarawak.gov.my/) untuk set semula Kata Laluan (akaun emel) SarawakNet anda.</td></tr>
                        <tr><td height="15px"><!--SomeSpace--></td></tr>
                        <!--Commented by ChangMH @ 17-Nov-2014 :: Not applicable as all user will using ldap account-->
                        <!--<tr><td class="supportContent">Pengguna Non-SarawakNet</td></tr>
                        <tr><td class="supportContentAns">Sila klik <a href="loadForgotPasswordLoginESS" target="_self">Lupa Kata Laluan?</a> untuk set semula Kata Laluan anda.</td></tr>
                        <tr><td height="15px">SomeSpace</td></tr>-->
                    </table>
                </td>
            </tr>
            <tr>
                <td> 
                    <table cellpadding="3" cellspacing="0" width="100%">
                        <tr class="supportTitle">
                            <td>3.0 Akaun Terkunci</td>
                            <td align="right" id='papar'>
                                <span class="common_showHideBtn">[Papar]</span>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
             <tr class="content_info">
                <td>
                   <table class="xbox">
                        <tr><td class="supportContentAns">
                                Sila hubungi Pentadbir Sistem di pejabat anda untuk mengaktifkan ID Pengguna yang telah dikunci.
                                <!--Akaun anda telah dikunci mungkin kerana:--> 
                                <u/l>
<!--                                    <li> Melebihi bilangan percubaan yang dibenarkan untuk log masuk (lebih daripada 3 percubaan), atau</li>
                                    <li> Kata laluan baru telah tidak disimpan dengan betul semasa proses pertukaran kata laluan.</li>-->
                                </u/l>
                            </td>
                        </tr>
                        <!--Commented by ChangMH @ 17-Nov-2014 :: Not applicable as all user will using ldap account-->
                        <!--<tr><td class="supportContentAns">Sila klik <a href="loadActivationPageLoginESS" target="_self">Permohonan Pengaktifan Akaun</a> untuk mengaktifkan ID Pengguna yang telah dikunci.</td></tr>-->
                        <tr><td height="15px"><!--SomeSpace--></td></tr>
                    </table>
                </td>
            </tr>
             <tr>
                <td> 
                    <table cellpadding="3" cellspacing="0" width="100%">
                        <tr class="supportTitle">
                            <td>4.0 Aduan</td>
                            <td align="right" id='papar'>
                                <span class="common_showHideBtn">[Papar]</span>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
             <tr class="content_info">
                <td>
                   <table class="xbox">
                        <tr><td class="supportContentAns">Jika anda menghadapi sebarang masalah semasa log masuk, sila hubungi <br/>
                                <ul><li>SAINS Call Centre: 1-300-88-7246 </li>
                                    <li>emel kepada <a href="mailto:callcentre@sains.com.my" target="_blank">callcentre@sains.com.my</a></li>
                                    <li>Laman web: <a href="http://callcentre.sains.com.my/" target="_blank">http://callcentre.sains.com.my/</a></li>
                                    
                                </ul>
                            </td>
                        </tr>
                        
                    </table>
                </td>
            </tr>
        </table>
            </div>
    </body>  
</html>
