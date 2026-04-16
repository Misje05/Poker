<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Poker P20 - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body>
    <div class="card">
        <div style="font-size: 3rem;">♠️</div>
        <h1>POKER P20</h1>
        <c:if test="${not empty error}"><div style="color: #ff4d4d; margin-bottom: 10px;">${error}</div></c:if>
        <c:if test="${not empty message}"><div style="color: #4dff4d; margin-bottom: 10px;">${message}</div></c:if>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit">Login</button>
        </form>
        <p style="margin-top: 20px;">No account? <a href="${pageContext.request.contextPath}/register">Register</a></p>
    </div>
</body>
</html>
