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
    private boolean gameStarted = false;

    public PokerTable(String id) {
        this.id = id;
    }

    public synchronized void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public synchronized void startNewRound() {
        if (players.size() < 2) return;
        
        currentRound = new GameRound(new ArrayList<>(players));
        gameStarted = true;
        
        // Reset players
        for (Player p : players) {
            p.resetForNewRound();
        }
        
        // Deal initial cards
        for (Player p : players) {
            ArrayList<Card> cards = currentRound.dealCards();
            p.addCard(cards.get(0));
            p.addCard(cards.get(1));
        }
        
        turnIndex = (dealerIndex + 3) % players.size(); // Start after blinds
    }

    // Getters and helper methods for the web view
    public String getId() { return id; }
    public List<Player> getPlayers() { return players; }
    public GameRound getCurrentRound() { return currentRound; }
    public boolean isGameStarted() { return gameStarted; }
    public int getTurnIndex() { return turnIndex; }
    
    public Player getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(turnIndex % players.size());
    }

    public synchronized void nextTurn() {
        turnIndex = (turnIndex + 1) % players.size();
        // Skip players who folded
        while (players.get(turnIndex).getStatus() == PlayerStatus.FOLDED) {
            turnIndex = (turnIndex + 1) % players.size();
        }
    }
}
