<%-- 
    Document   : index.jsp
    Created on : Nov 8, 2024, 10:42:19 AM
    Author     : CHIGOZIE IWUJI
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="./navbar.jsp" %>


<!DOCTYPE html>
<html>
    <head>
        <title>GozGlobal Bank OTP</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!--<link rel="stylesheet" href="style.css"/>-->
        <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css"/>
        <script src="/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js" defer></script>
    </head>
    <body>
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-8 col-lg-6">
                    <div class="card">
                        <div class="card-header text-center">
                            <h3>GozGlobal Bank Registration</h3>
                            <p class="mb-0">Create your account to get started</p>
                        </div>
                        <div class="card-body">
                            <form action="validate" method="post">
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="firstname" class="form-label">Enter OTP</label>
                                        <input type="number" class="form-control" id="otp" name="otp" placeholder="enter otp here" required>
                                    </div>
                                </div>
                                <div class="d-grid mb-3">
                                    <button type="submit" class="btn btn-primary btn-lg">Submit</button>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-secondary btn-lg">Resend OTP</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
