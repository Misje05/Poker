package no.hvl.dat110.poker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PokerTable {
    private String id;
    private List<Player> players = new CopyOnWriteArrayList<>();
    private GameRound currentRound;
    private int dealerIndex = 0;
    private int turnIndex = 0;
    private int currentBet = 0;
    private int smallBlind = 10;
    private int bigBlind = 20;
    private boolean gameStarted = false;

    public PokerTable(String id) {
        this.id = id;
    }

    public synchronized void addPlayer(Player player) {
        boolean exists = false;
        for (Player p : players) {
            if (p.getName().equals(player.getName())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            players.add(player);
        }
    }

    public synchronized void startNewRound() {
        if (players.size() < 2) return;
        
        currentRound = new GameRound(new ArrayList<>(players));
        gameStarted = true;
        
        // Reset players and blinds
        for (Player p : players) p.resetForNewRound();
        
        // Take Blinds
        Player sb = players.get(dealerIndex % players.size());
        Player bb = players.get((dealerIndex + 1) % players.size());
        sb.bet(smallBlind);
        bb.bet(bigBlind);
        currentRound.addToPot(smallBlind + bigBlind);
        currentBet = bigBlind;
        
        // Deal initial cards
        for (Player p : players) {
            ArrayList<Card> cards = currentRound.dealCards();
            p.addCard(cards.get(0));
            p.addCard(cards.get(1));
        }
        
        turnIndex = (dealerIndex + 2) % players.size();
    }

    public synchronized void handleAction(String playerName, String action, int amount) {
        Player p = getCurrentPlayer();
        if (p == null || !p.getName().equals(playerName)) return;

        switch (action.toLowerCase()) {
            case "fold":
                p.setStatus(PlayerStatus.FOLDED);
                break;
            case "call":
                int toCall = currentBet - p.getBet();
                p.bet(toCall);
                currentRound.addToPot(toCall);
                break;
            case "raise":
                int totalToBet = (currentBet - p.getBet()) + amount;
                p.bet(totalToBet);
                currentRound.addToPot(totalToBet);
                currentBet = p.getBet();
                break;
        }

        checkRoundStatus();
    }

    private synchronized void checkRoundStatus() {
        boolean allFinished = true;
        List<Player> active = new ArrayList<>();
        for (Player p : players) {
            if (p.getStatus() != PlayerStatus.FOLDED) {
                active.add(p);
                if (p.getBet() < currentBet) allFinished = false;
            }
        }

        if (active.size() == 1) {
            endRound(active.get(0));
            return;
        }

        if (allFinished) {
            nextPhase();
        } else {
            nextTurn();
        }
    }

    private void nextPhase() {
        for (Player p : players) p.setBet(0);
        currentBet = 0;
        
        RoundPhase phase = currentRound.getRoundPhase();
        if (phase == RoundPhase.PRE_FLOP) {
            currentRound.dealFlop();
            currentRound.nextPhase();
        } else if (phase == RoundPhase.FLOP) {
            currentRound.dealTurn();
            currentRound.nextPhase();
        } else if (phase == RoundPhase.TURN) {
            currentRound.dealRiver();
            currentRound.nextPhase();
        } else {
            endRound(currentRound.evalWinner());
            return;
        }
        turnIndex = (dealerIndex + 1) % players.size();
    }

    private void endRound(Player winner) {
        if (winner != null) winner.addChips(currentRound.getPot());
        gameStarted = false;
        dealerIndex++;
    }

    public synchronized void nextTurn() {
        turnIndex = (turnIndex + 1) % players.size();
        while (players.get(turnIndex).getStatus() == PlayerStatus.FOLDED) {
            turnIndex = (turnIndex + 1) % players.size();
        }
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(turnIndex % players.size());
    }

    public String getId() { return id; }
    public List<Player> getPlayers() { return players; }
    public GameRound getCurrentRound() { return currentRound; }
    public boolean isGameStarted() { return gameStarted; }
    public int getTurnIndex() { return turnIndex; }
}
