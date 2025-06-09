<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ include file="navbar.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Fund GozGlobal Account</title>
        <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css"/>
        <script src="https://js.stripe.com/v3/"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js"></script>
        <style>
            body {
                background-color: #f8f9fa;
            }
            .container {
                max-width: 500px;
                margin-top: 50px;
            }
            .card {
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0px 0px 10px rgba(0,0,0,0.1);
            }
        </style>
    </head>
    <body>

        <div class="container">
            <h2 class="text-center">Fund Your GozGlobal Account</h2>
            <div class="card">
                <form id="payment-form">
                    <div class="form-group mb-3">
                        <label>Enter Amount (NGN)</label>
                        <input type="number" id="amount" class="form-control" required>
                    </div>
                    <div class="form-group mb-3">
                        <label>Card Details</label>
                        <div id="card-element" class="form-control" 
                             style="padding: 10px; min-height: 45px; border: 1px solid #ced4da;">
                        </div>
                    </div>


                    <button type="submit" class="btn btn-primary w-100">Fund Now</button>
                </form>
                <div id="payment-message" class="mt-3 text-center"></div>
            </div>
        </div>

        <script>
            $(document).ready(function () {
                let stripe, card;

                // ✅ Fetch Stripe Public Key
                $.get("/api/payment/stripe-key", function (data) {
                    stripe = Stripe(data.publishableKey);
                    var elements = stripe.elements();

                    // ✅ Create a single Card Element (Includes Card Number, Expiry & CVV)
                    card = elements.create("card", {
                        hidePostalCode: true, // Optional: Hide postal code if not needed
                        style: {
                            base: {
                                fontSize: "16px",
                                color: "#32325d",
                                padding: "10px",
                                "::placeholder": {color: "#aab7c4"}
                            }
                        }
                    });

                    // ✅ Mount the card element to the div
                    card.mount("#card-element");

                    console.log("Stripe and card element initialized!"); // Debugging
                });

                // ✅ Handle Payment Submission
                $("#payment-form").submit(function (event) {
                    event.preventDefault();

                    let amount = $("#amount").val();
                    if (!amount || amount <= 0) {
                        alert("Please enter a valid amount.");
                        return;
                    }

                    $("#payment-message").text("Processing payment...");

                    stripe.createToken(card).then(function (result) {
                        if (result.error) {
                            $("#payment-message").text(result.error.message);
                        } else {
                            $.ajax({
                                url: "/api/payment/charge",
                                type: "POST",
                                data: {token: result.token.id, amount: amount},
                                success: function (response) {
                                    if (response.status === "success") {
                                        $("#payment-message").html(`<span class="text-success">Payment successful! Transaction ID: ${response.transactionId}</span>`);
                                    } else {
                                        $("#payment-message").html(`<span class="text-danger">Payment failed: ${response.message}</span>`);
                                    }
                                },
                                error: function (xhr) {
                                    $("#payment-message").html(`<span class="text-danger">Error processing payment.</span>`);
                                }
                            });
                        }
                    });
                });
            });
        </script>

    </body>
</html>