<%-- 
    Document   : home.jsp
    Created on : Nov 8, 2024, 10:42:19 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="./navbar.jsp" %>

<%@ page import="java.text.NumberFormat, java.util.Locale" %>
<%
    Object balanceObj = session.getAttribute("balance"); // Retrieve balance from session

    double balance = 0.0;
    if (balanceObj != null) {
        try {
            balance = Double.parseDouble(balanceObj.toString()); // Convert to double
        } catch (Exception e) {
            balance = 0.0; // Handle invalid value
        }
    }

    NumberFormat nairaFormat = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));
    String formattedBalance = nairaFormat.format(balance); // Convert balance to currency format

    request.setAttribute("formattedBalance", formattedBalance); // Pass formatted balance to JSP
%>

<!DOCTYPE html>
<html>
    <head>
        <title>GozGlobal</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="style.css"/>
        <script src="/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js" defer></script>
        <style>
            body {
                background-color: #f8f9fa;
            }
            .hero-section {
                background: linear-gradient(to right, #007bff, #00bfff);
                color: white;
                padding: 100px 0;
                text-align: center;
            }
            .hero-section h1 {
                font-size: 3.5rem;
                font-weight: bold;
            }
            .hero-section p {
                font-size: 1.25rem;
                margin-top: 20px;
            }
            .cta-buttons {
                margin-top: 30px;
            }
            .cta-buttons .btn {
                margin: 10px;
                padding: 10px 30px;
                font-size: 1.1rem;
            }
            .features-section {
                padding: 80px 0;
                text-align: center;
            }
            .features-section h2 {
                font-size: 2.5rem;
                margin-bottom: 40px;
            }
            .feature-card {
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                background-color: white;
                margin-bottom: 20px;
            }
            .feature-card h3 {
                font-size: 1.5rem;
                margin-bottom: 15px;
            }
            .feature-card p {
                font-size: 1rem;
                color: #666;
            }
            .btn-transaction {
                display: inline-block;
                padding: 12px 30px;
                font-size: 1.1rem;
                font-weight: bold;
                text-transform: uppercase;
                color: white;
                background: linear-gradient(135deg, #ff7e5f, #feb47b);
                border: none;
                border-radius: 30px;
                transition: all 0.3s ease-in-out;
                text-decoration: none;
            }

            .btn-transaction:hover {
                background: linear-gradient(135deg, #feb47b, #ff7e5f);
                transform: translateY(-2px);
                box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.2);
            }

        </style>
    </head>
    <body>
        <script>

        </script>

        <!-- Hero Section -->
        <div class="hero-section">
            <div class="container">
                <h1>Welcome to GozGlobal Bank</h1>
                <h1>${user==null ? "" : user}</h1>

                <p>Your trusted partner in banking. Secure, fast, and reliable banking services for everyone.</p>
                <div class="cta-buttons">
                    <!-- Display balance -->

                    <h3 id="balance">${user == null ? "" : 'Balance: '.concat(formattedBalance)}</h3>

                    ${user==null ? 
                      '<a href="/api/register" class="btn btn-light btn-lg">Register</a>
                      <a href="/api/login" class="btn btn-outline-light btn-lg">Login</a>' 
                      : 
                      '<a href="/api/fund" class="btn btn-light btn-lg">Fund Account</a>
                      <a href="/api/transfer" class="btn btn-outline-light btn-lg">Transfer</a>
                      <a href="/api/transactions" class="btn btn-transaction">Transaction History</a>'
                    }
                </div>

            </div>
        </div>
        <div class="container p-5 text-center">
            <div class="row justify-content-center">
                <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                    <div class="feature-card">
                        <img src="pages/images/phone.png" class="img-fluid" alt=""/>
                        <h3>Buy Airtime</h3>
                        <h3>*</h3>
                    </div>
                </div>
                <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                    <div class="feature-card">
                        <img src="pages/images/wifi.png" class="img-fluid" alt=""/>
                        <h3>Buy Data</h3>
                        <h3>*</h3>                    
                    </div>
                </div>
                <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                    <div class="feature-card">
                        <img src="pages/images/phone.png" class="img-fluid" alt=""/>
                        <h3>Recharge Cable</h3>
                        <h3>*</h3>
                    </div>
                </div>
            </div>
        </div>



        <!-- Features Section -->
        <div class="features-section">
            <div class="container">
                <h2>Why Choose GozGlobal Bank?</h2>
                <div class="row">
                    <div class="col-md-4">
                        <div class="feature-card">
                            <h3>Secure Banking</h3>
                            <p>We use the latest encryption technologies to keep your data safe and secure.</p>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="feature-card">
                            <h3>24/7 Support</h3>
                            <p>Our customer support team is available round the clock to assist you.</p>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="feature-card">
                            <h3>Easy Transactions</h3>
                            <p>Transfer money, pay bills, and manage your accounts with ease.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>