<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>GozGlobal Bank - Navbar</title>
        <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css"/>
        <script src="/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js" defer></script>
        <style>
            /* Custom styles for the navbar */
            .navbar {
                background-color: #007bff !important; /* Primary blue color */
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* Subtle shadow */
            }
            .navbar-brand {
                font-size: 1.5rem;
                font-weight: bold;
                color: white !important; /* White text for brand */
            }
            .navbar-nav .nav-link {
                color: rgba(255, 255, 255, 0.8) !important; /* Semi-transparent white text */
                font-size: 1rem;
                margin: 0 10px; /* Spacing between nav items */
                transition: color 0.3s ease; /* Smooth hover effect */
            }
            .navbar-nav .nav-link:hover {
                color: white !important; /* Fully white on hover */
            }
            .navbar-nav .nav-link.active {
                color: white !important; /* Fully white for active link */
                font-weight: bold; /* Bold for active link */
            }
            .navbar-toggler {
                border-color: rgba(255, 255, 255, 0.5); /* Light border for toggler */
            }
            .navbar-toggler-icon {
                background-image: url("data:image/svg+xml,%3csvg viewBox='0 0 30 30' xmlns='http://www.w3.org/2000/svg'%3e%3cpath stroke='rgba(255, 255, 255, 0.8)' stroke-width='2' stroke-linecap='round' stroke-miterlimit='10' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
            }
            /* Logout Button Style */
            .logout-btn {
                background: none;
                border: none;
                color: rgba(255, 255, 255, 0.8);
                font-size: 1rem;
                cursor: pointer;
                transition: color 0.3s ease;
                text-decoration: none; /* Remove underline */
            }
            .logout-btn:hover {
                color: white;
            }
        </style>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark sticky-top">
            <div class="container-fluid">
                <!-- Brand/Logo -->
                <a class="navbar-brand" href="index">GozGlobal Bank</a>

                <!-- Toggler for mobile view -->
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <!-- Navbar Links -->
                <div class="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul class="navbar-nav ms-auto"> <!-- Align links to the right -->
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="/api/index">Home</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/api/login">Login</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/api/register">Register</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="about">About</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="services">Services</a>
                        </li>
                        
                        <!-- Logout Button (Now Aligned Properly) -->
                        <li class="nav-item">
                            <form action="/logout" method="POST" class="d-inline">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
                                <button type="submit" class="nav-link logout-btn">Logout</button>
                            </form>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </body>
</html>
