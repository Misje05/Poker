<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Poker P20 - Game Table</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700;800&display=swap" rel="stylesheet">
    <!-- Auto-refresh has been replaced with smart AJAX polling -->
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
        .folded-style { opacity: 0.4; filter: grayscale(100%); }
        .my-hand { margin-top: 30px; padding: 20px; background: rgba(255,255,255,0.05); border-radius: 15px; }
        
        /* Hide arrows from number input */
        input[type=number]::-webkit-inner-spin-button, 
        input[type=number]::-webkit-outer-spin-button { 
            -webkit-appearance: none; 
            margin: 0; 
        }
        input[type=number] {
            -moz-appearance: textfield;
        }
    </style>
</head>
<body>
<div id="game-root" data-version="${table.stateVersion}">
    <div class="card" style="width: 900px;">
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
                <h1 style="margin: 0;">Table: ${table.id}</h1>
                <p style="margin: 0; font-size: 0.9rem; color: #ffd700;">Blinds: ${table.smallBlind} / ${table.bigBlind}</p>
            </div>
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
                <c:set var="isCurrentPlayer" value="${table.gameStarted and not empty table.currentPlayer and table.currentPlayer.name == p.name}"/>
                <div class="player-box ${isCurrentPlayer ? 'active-turn' : ''} ${p.status == 'FOLDED' ? 'folded-style' : ''}">
                    <strong>${p.name}</strong><br>
                    <small>${p.chips} 💰</small><br>
                    <c:if test="${p.status == 'FOLDED'}"><small style="color: #ff4d4d; font-weight: bold;">FOLDED</small></c:if>
                    <c:if test="${p.bet > 0}"><br><small style="color: #ffd700;">Bet: ${p.bet}</small></c:if>
                </div>
            </c:forEach>
        </div>

        <c:if test="${table.gameStarted}">
            <div class="my-hand">
                <c:set var="myPlayer" value="${null}"/>
                <c:forEach var="p" items="${table.players}">
                    <c:if test="${p.name == currentUser}"><c:set var="myPlayer" value="${p}"/></c:if>
                </c:forEach>

                <h3>Your Hand (${currentUser})</h3>
                <c:if test="${not empty myPlayer}">
                    <div style="display: flex; gap: 10px; justify-content: center; ${myPlayer.status == 'FOLDED' ? 'opacity: 0.3;' : ''}">
                        <c:if test="${not empty myPlayer.hand.getCard(0)}">
                            <div class="card-item">
                                <img src="${pageContext.request.contextPath}/images/${myPlayer.hand.getCard(0).suit}_${myPlayer.hand.getCard(0).rank}.png" alt="Card 1">
                            </div>
                            <div class="card-item">
                                <img src="${pageContext.request.contextPath}/images/${myPlayer.hand.getCard(1).suit}_${myPlayer.hand.getCard(1).rank}.png" alt="Card 2">
                            </div>
                        </c:if>
                    </div>

                    <c:if test="${not empty table.currentPlayer and table.currentPlayer.name == currentUser and myPlayer.status != 'FOLDED'}">
                        <div style="margin-top: 25px; display: flex; gap: 15px; justify-content: center;">
                            <form action="${pageContext.request.contextPath}/game/${table.id}/action" method="post">
                                <input type="hidden" name="action" value="fold">
                                <button type="submit" style="width: 120px; background: #666; margin-top: 0;">Fold</button>
                            </form>

                            <c:choose>
                                <c:when test="${myPlayer.bet == table.currentBet}">
                                    <form action="${pageContext.request.contextPath}/game/${table.id}/action" method="post">
                                        <input type="hidden" name="action" value="check">
                                        <button type="submit" style="width: 120px; background: #007bff; margin-top: 0;">Check</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/game/${table.id}/action" method="post">
                                        <input type="hidden" name="action" value="call">
                                        <button type="submit" style="width: 120px; background: #28a745; margin-top: 0;">Call</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>

                            <form action="${pageContext.request.contextPath}/game/${table.id}/action" method="post" style="display: flex; gap: 5px;">
                                <input type="hidden" name="action" value="raise">
                                <input type="number" id="raiseAmount" name="amount" value="${table.bigBlind}" min="${table.bigBlind}" max="${myPlayer.chips}" 
                                       style="width: 80px; margin: 0; padding: 5px;">
                                <button type="submit" style="width: 100px; background: #e94560; margin-top: 0;">Raise</button>
                            </form>
                        </div>
                    </c:if>

                    <c:if test="${myPlayer.status == 'FOLDED'}">
                        <p style="color: #ff4d4d; margin-top: 15px;">You have folded. Please wait for the next round.</p>
                    </c:if>
                </c:if>
            </div>
        </c:if>
    </div>

    <!-- Winner Notification Overlay -->
    <c:if test="${not empty table.lastWinnerName}">
        <div id="winnerOverlay" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.85); display: flex; justify-content: center; align-items: center; z-index: 1000; backdrop-filter: blur(5px);">
            <div class="card" style="width: 450px; padding: 40px; border: 3px solid #ffd700; text-align: center; background: #1a1a2e; box-shadow: 0 0 50px rgba(0,0,0,0.5);">
                <h2 style="color: #ffd700; margin: 0; font-size: 1.8rem;">🍗 Winner Winner Chicken Dinner!</h2>
                <h1 style="color: white; font-size: 3.5rem; margin: 15px 0;">${table.lastWinnerName}</h1>
                <p style="margin-bottom: 30px; font-size: 1.1rem; color: #ccc;">Won the entire pot!</p>
                
                <form action="${pageContext.request.contextPath}/game/${table.id}/clear-winner" method="post">
                    <c:choose>
                        <c:when test="${table.lastWinnerName == currentUser}">
                            <button type="submit" style="width: 200px; background: #28a745; font-weight: bold; font-size: 1.1rem;">Awesome!</button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit" style="width: 200px; background: #6c757d; font-weight: bold; font-size: 1.1rem;">ouf, i suck!</button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
        </div>
    </c:if>

</div> <!-- End of game-root -->

<!-- AJAX Polling Script -->
<script>
    setInterval(function() {
        var currentVersion = document.getElementById('game-root').getAttribute('data-version');
        
        fetch(window.location.href)
            .then(response => response.text())
            .then(html => {
                var parser = new DOMParser();
                var doc = parser.parseFromString(html, 'text/html');
                var newRoot = doc.getElementById('game-root');
                
                if (newRoot) {
                    var newVersion = newRoot.getAttribute('data-version');
                    if (newVersion !== currentVersion) {
                        // State has changed on the server, we must update the UI.
                        
                        // We preserve the raise input value if the user was typing.
                        var oldInput = document.getElementById('raiseAmount');
                        var oldVal = oldInput ? oldInput.value : null;

                        document.getElementById('game-root').innerHTML = newRoot.innerHTML;
                        document.getElementById('game-root').setAttribute('data-version', newVersion);

                        // If the input is still there in the new HTML, copy the value back
                        var newInput = document.getElementById('raiseAmount');
                        if (oldVal && newInput) {
                            newInput.value = oldVal;
                        }
                    }
                }
            })
            .catch(error => console.error("Polling error:", error));
    }, 1500); // Poll every 1.5 seconds
</script>

</body>
</html>
