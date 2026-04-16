<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Poker P20 - Game Table</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
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
            width: 70px;
            height: 100px;
            margin: 0 5px;
        }
        .card-item img {
            width: 100%;
            height: 100%;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.5);
        }
        .players-container {
            display: flex; justify-content: space-around; width: 100%; margin-top: 20px;
        }
        .player-box {
            padding: 15px; background: rgba(255,255,255,0.05); border-radius: 12px;
            min-width: 120px; text-align: center; border: 1px solid rgba(255,255,255,0.1);
            transition: 0.3s;
        }
        .active-turn { border: 2px solid #ffd700; background: rgba(255, 215, 0, 0.1); }
        .my-hand { margin-top: 30px; padding: 20px; background: rgba(255,255,255,0.05); border-radius: 15px; }
    </style>
</head>
<body>
    <div class="card" style="width: 900px;">
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
                            <div class="card-item">
                                <img src="${pageContext.request.contextPath}/images/${card.suit}_${card.rank}.png" alt="${card}">
                            </div>
                        </c:forEach>
                        <c:if test="${empty table.currentRound.tableCards}">
                            <div style="color: rgba(255,255,255,0.3); font-size: 1.5rem; letter-spacing: 5px;">PRE-FLOP</div>
                        </c:if>
                    </div>
                    <div style="position: absolute; bottom: 20px; font-weight: bold; font-size: 1.2rem;">
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
                            <div class="card-item">
                                <img src="${pageContext.request.contextPath}/images/${p.hand.getCard(0).suit}_${p.hand.getCard(0).rank}.png" alt="Card 1">
                            </div>
                            <div class="card-item">
                                <img src="${pageContext.request.contextPath}/images/${p.hand.getCard(1).suit}_${p.hand.getCard(1).rank}.png" alt="Card 2">
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
                
                <c:if test="${table.currentPlayer.name == currentUser}">
                    <div style="margin-top: 25px; display: flex; gap: 15px; justify-content: center;">
                        <form action="${pageContext.request.contextPath}/game/${table.id}/action" method="post">
                            <input type="hidden" name="action" value="fold">
                            <button type="submit" style="width: 120px; background: #666; margin-top: 0;">Fold</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/game/${table.id}/action" method="post">
                            <input type="hidden" name="action" value="call">
                            <button type="submit" style="width: 120px; background: #28a745; margin-top: 0;">Call</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/game/${table.id}/action" method="post" style="display: flex; gap: 5px;">
                            <input type="hidden" name="action" value="raise">
                            <input type="number" name="amount" value="50" style="width: 60px; margin: 0; padding: 5px;">
                            <button type="submit" style="width: 100px; background: #e94560; margin-top: 0;">Raise</button>
                        </form>
                    </div>
                </c:if>
            </div>
        </c:if>
    </div>
</body>
</html>
