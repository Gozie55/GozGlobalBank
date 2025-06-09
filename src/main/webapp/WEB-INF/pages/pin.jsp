<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="./navbar.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <title>GozGlobal Bank - Set PIN</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css"/>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            $(document).ready(function () {
                function validatePin() {
                    var pin = $("#pin").val();
                    var cpin = $("#cpin").val();

                    // Ensure only digits (0-9) are entered
                    $("#pin, #cpin").val(function (_, value) {
                        return value.replace(/\D/g, "").substring(0, 4); // Only digits, max 4
                    });

                    // Check if PIN and Confirm PIN match
                    if (pin.length === 4 && cpin.length === 4) {
                        if (pin === cpin) {
                            $("#confirmPinInfo").text("PINs match! ✅").css("color", "green");
                        } else {
                            $("#confirmPinInfo").text("PINs do not match ❌").css("color", "red");
                        }
                    } else {
                        $("#confirmPinInfo").text("");
                    }
                }

                // Trigger validation on input for both fields
                $("#pin, #cpin").on("input", validatePin);

                // Prevent form submission if PINs do not match
                $("#confirmPinForm").on("submit", function (e) {
                    var pin = $("#pin").val();
                    var cpin = $("#cpin").val();
                    if (pin !== cpin) {
                        e.preventDefault();
                        alert("PINs do not match! Please correct before submitting.");
                    }
                });
            });
        </script>
    </head>
    <body>
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-8 col-lg-6">
                    <div class="card">
                        <div class="card-header text-center">
                            <h3>SET PIN</h3>
                        </div>
                        <div class="card-body">
                            <form id="confirmPinForm" action="confirmPin" method="post">
                                <div class="mb-3">
                                    <label for="pin" class="form-label">PIN</label>
                                    <input type="password" class="form-control" id="pin" name="pin" 
                                           placeholder="Enter 4-digit PIN" maxlength="4" required>
                                    <p id="pininfo" class="mt-2"></p>
                                </div>
                                <div class="mb-3">
                                    <label for="cpin" class="form-label">Confirm PIN</label>
                                    <input type="password" class="form-control" id="cpin" name="cpin" 
                                           placeholder="Confirm 4-digit PIN" maxlength="4" autocomplete="off" required>
                                    <p id="confirmPinInfo" class="mt-2"></p>
                                </div>

                                <div class="d-grid">
                                    <input type="submit" class="btn btn-primary btn-lg" value="Submit"/>
                                </div>
                            </form>
                        </div>
                        <div class="card-footer text-center">
                            <p class="mb-0">Already have an account? <a href="login" class="text-primary">Login here</a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
