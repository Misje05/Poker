<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Poker P20 - Lobby</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body>
    <div class="card" style="width: 500px;">
        <div style="display: flex; justify-content: space-between; margin-bottom: 2rem;">
            <div style="text-align: left;">
                <h2>Welcome, ${username}!</h2>
                <p style="color: #ffd700; font-weight: bold;">Chips: ${chips} 💰</p>
            </div>
            <a href="${pageContext.request.contextPath}/logout" style="color: #666; font-size: 0.8rem;">Logout</a>
        </div>
        <h3 style="text-align: left;">Active Tables</h3>
        <c:forEach var="t" items="${tables}">
            <div style="background: rgba(255, 255, 255, 0.05); padding: 15px; border-radius: 12px; margin-top: 10px; display: flex; justify-content: space-between; align-items: center;">
                <div><strong>${t.id == 'high-stakes' ? '💎 High Stakes' : '🍺 Casual Play'}</strong><br><small>${t.players.size()} Players</small></div>
                <a href="${pageContext.request.contextPath}/game/${t.id}" style="width: auto; padding: 5px 15px; background: #28a745; color: white; border-radius: 6px; text-decoration: none; font-size: 0.9rem;">Join</a>
            </div>
        </c:forEach>
        <button style="background: var(--secondary); margin-top: 20px;">Create New Table</button>
    </div>
</body>
</html>
