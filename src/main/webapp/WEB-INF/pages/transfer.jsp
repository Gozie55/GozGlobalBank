<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="./navbar.jsp" %>

<%@ page import="java.text.NumberFormat, java.util.Locale" %>


<%
    NumberFormat nairaFormat = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));
    Object balanceObj = session.getAttribute("balance");
    String formattedBalance = (balanceObj != null) ? nairaFormat.format(balanceObj) : "₦0.00";
%>

<!DOCTYPE html>
<html>
    <head>
        <title>GozGlobal Bank - Transfer</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css"/>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            $(document).ready(function () {
                // ✅ Auto-fetch customer details when account number is entered
                $("#accnum").on("input", function () {
                    var accountNumber = $(this).val();
                    if (accountNumber.length === 10) {
                        $.ajax({
                            url: "/api/getCustomerDetails",
                            type: "GET",
                            data: {accountNumber: accountNumber},
                            success: function (response) {
                                $("#customerName").text(response.firstname + " " + response.lastname)
                                        .addClass("text-success").removeClass("text-danger");
                            },
                            error: function () {
                                $("#customerName").text("Account not found")
                                        .addClass("text-danger").removeClass("text-success");
                            }
                        });
                    } else {
                        $("#customerName").text("").removeClass("text-danger text-success");
                    }
                });

                // ✅ Check if the user has sufficient balance
                $("#amount").on("input", function () {
                    var amount = parseFloat($(this).val());
                    var accountNumber = $("#accnum").val();

                    if (amount > 0 && accountNumber.length === 10) {
                        $.ajax({
                            url: "/api/checkCustomerBalance",
                            type: "GET",
                            data: {amount: amount},
                            success: function (response) {
                                var balance = parseFloat(response);
                                if (isNaN(balance)) {
                                    $("#balanceInfo").text("Error retrieving balance")
                                            .addClass("text-danger").removeClass("text-success");
                                } else if (amount > balance) {
                                    $("#balanceInfo").text("Insufficient balance")
                                            .addClass("text-danger").removeClass("text-success");
                                } else {
                                    $("#balanceInfo").text("Available Balance: ₦" + balance.toLocaleString())
                                            .addClass("text-success").removeClass("text-danger");
                                }
                            },
                            error: function (xhr) {
                                if (xhr.status === 404) {
                                    $("#balanceInfo").text("Account not found")
                                            .addClass("text-danger").removeClass("text-success");
                                } else {
                                    $("#balanceInfo").text("Error retrieving balance")
                                            .addClass("text-danger").removeClass("text-success");
                                }
                            }
                        });
                    } else {
                        $("#balanceInfo").text("").removeClass("text-danger text-success");
                    }
                });

                // ✅ AJAX Submission for Money Transfer
                $("#transferForm").submit(function (event) {
                    event.preventDefault();

                    var accountNumber = $("#accnum").val();
                    var amount = $("#amount").val();
                    var pin = $("#pin").val();

                    if (accountNumber.length !== 10) {
                        alert("Error: Enter a valid 10-digit account number.");
                        return;
                    }
                    if (amount <= 0) {
                        alert("Error: Amount must be greater than zero.");
                        return;
                    }
                    if (pin.length < 4) {
                        alert("Error: Enter a valid 4-digit PIN.");
                        return;
                    }

                    $.ajax({
                        url: "/api/TransferMoney",
                        type: "POST",
                        contentType: "application/x-www-form-urlencoded",
                        data: {accountNumber: accountNumber, amount: amount, pin: pin},
                        success: function (response) {
                            alert("✅ Transfer successful!");

                            // ✅ Fetch updated balance after transfer
                            $.ajax({
                                url: "/api/balance",
                                type: "GET",
                                success: function (newBalance) {
                                    $("#balanceDisplay").text("Your Balance: ₦" + newBalance.toLocaleString());
                                }
                            });

                            $("#transferForm")[0].reset();
                            $("#customerName").text("").removeClass("text-success text-danger");
                            $("#balanceInfo").text("").removeClass("text-success text-danger");
                        },
                        error: function (xhr) {
                            if (xhr.status === 403) {
                                alert("❌ Incorrect PIN. Please try again.");
                            } else if (xhr.status === 400) {
                                alert("❌ Insufficient balance.");
                            } else {
                                alert("❌ Error: " + xhr.responseText);
                            }
                        }
                    });
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
                            <h3>Transfer to a GozGlobal Account</h3>
                        </div>
                        <div class="card-body">
                            <p id="balanceDisplay" class="text-center text-primary fs-5">
                                <strong>Your Balance:</strong> <%= formattedBalance %>
                            </p>
                            <form id="transferForm" method="post">
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="accnum" class="form-label">Account Number</label>
                                        <input type="text" class="form-control" id="accnum" name="accountNumber" 
                                               placeholder="Enter 10-digit account number" required>
                                        <p id="customerName" class="mt-2"></p>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="bankname" class="form-label">Bank</label>
                                        <input type="text" class="form-control" id="bankname" name="bank" 
                                               placeholder="Enter bank name" required>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="amount" class="form-label">Amount</label>
                                    <input type="number" class="form-control" id="amount" name="amount" 
                                           placeholder="Enter amount" required>
                                    <p id="balanceInfo" class="mt-2"></p>
                                </div>
                                <div class="mb-3">
                                    <label for="pin" class="form-label">Pin</label>
                                    <input type="password" class="form-control" id="pin" name="pin" 
                                           placeholder="Enter PIN" autocomplete="off" required>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary btn-lg">Transfer</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
