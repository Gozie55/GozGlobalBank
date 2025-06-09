<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="navbar.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Transaction History</title>
        <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css"/>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js"></script>
        <style>
            body {
                background-color: #f8f9fa;
            }
            .container {
                margin-top: 50px;
            }
            table {
                background-color: white;
            }
            th {
                background-color: #007bff;
                color: white;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <h2 class="text-center">Your Transactions</h2>

            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Transaction ID</th>
                        <th>Username</th>
                        <th>Type</th>
                        <th>Amount</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Date & Time</th>
                    </tr>
                </thead>
                <tbody id="transactionTable">
                    <!-- ✅ Render Transactions from Backend using JSTL -->
                    <c:choose>
                        <c:when test="${empty transactions}">
                            <tr><td colspan="6" class="text-center">No transactions found</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="transaction" items="${transactions}">
                                <tr>
                                    <td>${transaction.id}</td>
                                    <td>${transaction.username}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${transaction.transactionType == 'SENT'}">
                                                <span style="color: red;">Sent</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: green;">Received</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <fmt:setLocale value="en_NG" />
                                    <fmt:formatNumber value="${transaction.amount}" type="currency" currencySymbol="₦" var="formattedAmount"/>

                                    <td>${formattedAmount}</td>
                                    <td>${transaction.senderAccount}</td>
                                    <td>${transaction.receiverAccount}</td>
                                    <td>${transaction.timestamp}</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>


    </body>
</html>
