<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Poker P20 - Game Table</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700;800&display=swap" rel="stylesheet">
    <!-- Auto-refresh every 3 seconds to sync players -->
    <meta http-equiv="refresh" content="3">
    <style>
        .poker-table {
            background: #076324;
            border: 10px solid #5d3a1a;
            border-radius: 100px;
            width: 100%;
            height: 300px;
            margin: 20px 0;
            position: relative;
            display: flex;
            justify-content: center;
            align-items: center;
            box-shadow: inset 0 0 50px rgba(0,0,0,0.5);
        }
        .community-cards { display: flex; gap: 10px; }
        .card-item {
            background: white; color: black; padding: 10px; border-radius: 5px;
            width: 40px; height: 60px; font-weight: bold; border: 1px solid #ccc;
        }
        .players-container {
            display: flex; justify-content: space-around; width: 100%; margin-top: 20px;
        }
        .player-box {
            padding: 10px; background: rgba(255,255,255,0.1); border-radius: 10px;
            min-width: 100px; text-align: center;
        }
        .active-turn { border: 2px solid #ffd700; background: rgba(255, 215, 0, 0.1); }
        .my-hand { margin-top: 30px; padding: 20px; background: rgba(255,255,255,0.05); border-radius: 15px; }
    </style>
</head>
<body>
    <div class="card" style="width: 800px;">
        <div style="display: flex; justify-content: space-between;">
            <h1>Table: ${table.id}</h1>
            <a href="${pageContext.request.contextPath}/" style="color: #ccc;">Back to Lobby</a>
        </div>

        <div class="poker-table">
            <c:choose>
                <c:when test="${not table.gameStarted}">
                    <div style="text-align: center;">
                        <p>Waiting for players...</p>
                        <c:if test="${table.players.size() >= 2}">
                            <form action="${pageContext.request.contextPath}/game/${table.id}/start" method="post">
                                <button type="submit" style="width: auto; padding: 10px 30px;">Start Game</button>
                            </form>
                        </c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="community-cards">
                        <c:forEach var="card" items="${table.currentRound.tableCards}">
                            <div class="card-item">${card}</div>
                        </c:forEach>
                        <c:if test="${empty table.currentRound.tableCards}">
                            <div style="color: rgba(255,255,255,0.3);">PRE-FLOP</div>
                        </c:if>
                    </div>
                    <div style="position: absolute; bottom: 20px; font-weight: bold;">
                        POT: ${table.currentRound.pot} 💰
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="players-container">
            <c:forEach var="p" items="${table.players}">
                <div class="player-box ${table.currentPlayer.name == p.name ? 'active-turn' : ''}">
                    <strong>${p.name}</strong><br>
                    <small>${p.chips} 💰</small><br>
                    <c:if test="${p.status == 'FOLDED'}"><small style="color: #ff4d4d;">FOLDED</small></c:if>
                </div>
            </c:forEach>
        </div>

        <c:if test="${table.gameStarted}">
            <div class="my-hand">
                <h3>Your Hand</h3>
                <div style="display: flex; gap: 10px; justify-content: center;">
                    <c:forEach var="p" items="${table.players}">
                        <c:if test="${p.name == currentUser}">
                            <div class="card-item">${p.hand.getCard(0)}</div>
                            <div class="card-item">${p.hand.getCard(1)}</div>
                        </c:if>
                    </c:forEach>
                </div>
                
                <c:if test="${table.currentPlayer.name == currentUser}">
                    <div style="margin-top: 20px; display: flex; gap: 10px; justify-content: center;">
                        <button style="width: 100px; background: #666;">Fold</button>
                        <button style="width: 100px; background: #28a745;">Call</button>
                        <button style="width: 100px; background: #e94560;">Raise</button>
                    </div>
                </c:if>
            </div>
        </c:if>
    </div>
</body>
</html>
