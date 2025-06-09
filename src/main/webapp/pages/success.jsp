<%-- 
    Document   : success.jsp
    Created on : Nov 8, 2024, 10:42:19 AM
    Author     : CHIGOZIE IWUJI
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="./navbar.jsp" %>


<!DOCTYPE html>
<html>
    <head>
        <title>Success - GozGlobal Bank</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!--<link rel="stylesheet" href="style.css"/>-->
        <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css"/>
        <script src="/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js" defer></script>
    </head>
    <body>
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card border-success">
                        <div class="card-body">
                            <h2 class="text-success text-center">Details Entered Successfully!</h2>
                            <p class="text-center">Thank you for providing the required information. Our team will review your details and get back to you soon.</p>
                            <div class="text-center">
                                <a href="/" class="btn btn-success">Return to Home Page</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>