<%@taglib uri="/struts-tags" prefix="s"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html data-bs-theme="light" lang="en-US" dir="ltr">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">


        <!-- ===============================================-->
        <!--    Document Title-->
        <!-- ===============================================-->
        <title><s:text name="errors.403"/></title>


        <!-- ===============================================-->
        <!--    Favicons-->
        <!-- ===============================================-->
        <link rel="shortcut icon" type="image/x-icon" href="<s:text name="system.icon"/>">
        <meta name="theme-color" content="#ffffff">
        <script src="falcon-v3.16.0/public/assets/js/config.js"></script>


        <!-- ===============================================-->
        <!--    Stylesheets-->
        <!-- ===============================================-->
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,500,600,700%7cPoppins:300,400,500,600,700,800,900&amp;display=swap" rel="stylesheet">
        <!--<link href="../vendors/simplebar/simplebar.min.css" rel="stylesheet">-->
        <link href="falcon-v3.16.0/public/assets/css/theme.min.css" rel="stylesheet" id="style-default">
        <link href="falcon-v3.16.0/public/assets/css/user.css" rel="stylesheet" id="user-style-default">
    </head>
    <body>

        <!-- ===============================================-->
        <!--    Main Content-->
        <!-- ===============================================-->
        <main class="main" id="top">
            <div class="container" data-layout="container">
                <script>
                    var isFluid = JSON.parse(localStorage.getItem('isFluid'));
                    if (isFluid) {
                        var container = document.querySelector('[data-layout]');
                        container.classList.remove('container');
                        container.classList.add('container-fluid');
                    }
                </script>

                <div class="row flex-center min-vh-100 py-6 text-center">
                    <div class="col-sm-10 col-md-8 col-lg-6 col-xxl-5"><a class="d-flex flex-center mb-4" href="<s:url value="/"/>"><img class="me-2" src="<s:text name="system.icon"/>" alt="" width="58" /><span class="font-sans-serif fw-bolder fs-5 d-inline-block"><s:text name="system.name"/></span></a>
                        <div class="card">
                            <div class="card-body p-4 p-sm-5">
                                <div class="fw-black lh-1 text-300 fs-error">403</div>
                                <p class="lead mt-4 text-800 font-sans-serif fw-semi-bold w-md-75 w-xl-100 mx-auto"><s:text name="errors.403"/></p>
                                <hr />
                                <p><s:text name="errors.notAllowToView"/></p>
                                <p><s:text name="errors.404.msg2"/></p>
                                <a class="btn btn-primary btn-sm mt-3" href="<s:url value="/"/>"><span class="fas fa-home me-2"></span><s:text name="system.home"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <!-- ===============================================-->
        <!--    End of Main Content-->
        <!-- ===============================================-->

        <!-- ===============================================-->
        <!--    JavaScripts-->
        <!-- ===============================================-->
        <script src="falcon-v3.16.0/public/vendors/fontawesome/all.min.js"></script>
        
    </body>
</html>