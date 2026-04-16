<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Poker P20 - Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body>
    <div class="card">
        <div style="font-size: 3rem;">♣️</div>
        <h1>SIGN UP</h1>
        <form action="${pageContext.request.contextPath}/register" method="post">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit">Create Account</button>
        </form>
        <p style="margin-top: 20px;"><a href="${pageContext.request.contextPath}/login">Back to Login</a></p>
    </div>
</body>
</html>
