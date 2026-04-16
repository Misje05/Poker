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
    private int lastRaiseSize = 0;
    private int smallBlind = 50;
    private int bigBlind = 100;
    private boolean gameStarted = false;
    private boolean bbHasOption = true;
    private int playersActedThisPhase = 0;
    private String lastWinnerName = null;
    private int stateVersion = 0;

    public PokerTable(String id) {
        this.id = id;
    }

    public synchronized void addPlayer(Player player) {
        boolean exists = false;
        for (Player p : players) {
            if (p.getName().equals(player.getName())) { exists = true; break; }
        }
        if (!exists) {
            players.add(player);
            stateVersion++;
        }
    }

    public synchronized void startNewRound() {
        if (players.size() < 2) return;
        
        currentRound = new GameRound(new ArrayList<>(players));
        gameStarted = true;
        currentBet = bigBlind;
        lastRaiseSize = bigBlind;
        bbHasOption = true;
        playersActedThisPhase = 0;
        lastWinnerName = null;
        stateVersion++;
        
        for (Player p : players) {
            p.resetForNewRound();
            p.setBet(0);
        }
        
        Player sb = players.get(dealerIndex % players.size());
        Player bb = players.get((dealerIndex + 1) % players.size());
        sb.bet(smallBlind);
        bb.bet(bigBlind);
        currentRound.addToPot(smallBlind + bigBlind);
        
        for (Player p : players) {
            ArrayList<Card> cards = currentRound.dealCards();
            p.addCard(cards.get(0));
            p.addCard(cards.get(1));
        }
        
        turnIndex = (dealerIndex + 2) % players.size();
        ensureActiveTurn();
    }

    public synchronized void handleAction(String playerName, String action, int amount) {
        Player p = getCurrentPlayer();
        if (p == null || !p.getName().equals(playerName) || p.getStatus() == PlayerStatus.FOLDED) return;

        switch (action.toLowerCase()) {
            case "fold":
                p.setStatus(PlayerStatus.FOLDED);
                break;
            case "call":
                int toCall = currentBet - p.getBet();
                p.bet(toCall);
                currentRound.addToPot(toCall);
                break;
            case "check":
                if (p.getBet() != currentBet) return;
                break;
            case "raise":
                if (amount < lastRaiseSize) amount = lastRaiseSize;
                int addedInBet = (currentBet - p.getBet()) + amount;
                p.bet(addedInBet);
                currentRound.addToPot(addedInBet);
                lastRaiseSize = amount;
                currentBet = p.getBet();
                bbHasOption = false;
                playersActedThisPhase = 0; // Reset as others must now respond to raise
                break;
        }

        playersActedThisPhase++;
        
        if (currentRound.getRoundPhase() == RoundPhase.PRE_FLOP && p.getBet() == bigBlind && bbHasOption) {
            Player bb = players.get((dealerIndex + 1) % players.size());
            if (p.getName().equals(bb.getName())) {
                bbHasOption = false;
            }
        }

        stateVersion++;
        checkRoundStatus();
    }

    private synchronized void checkRoundStatus() {
        List<Player> active = new ArrayList<>();
        for (Player p : players) if (p.getStatus() != PlayerStatus.FOLDED) active.add(p);

        if (active.size() == 1) {
            endRound(active.get(0));
            return;
        }

        boolean phaseFinished = true;
        
        // BUG FIX: Must have acted at least once AND bets must match
        if (playersActedThisPhase < active.size()) {
            phaseFinished = false;
        }

        if (currentRound.getRoundPhase() == RoundPhase.PRE_FLOP && bbHasOption) {
            phaseFinished = false;
        }

        for (Player p : active) {
            if (p.getBet() < currentBet) {
                phaseFinished = false;
                break;
            }
        }

        if (phaseFinished) nextPhase();
        else {
            turnIndex = (turnIndex + 1) % players.size();
            ensureActiveTurn();
        }
    }

    private void nextPhase() {
        for (Player p : players) p.setBet(0);
        currentBet = 0;
        lastRaiseSize = bigBlind;
        playersActedThisPhase = 0;
        
        RoundPhase phase = currentRound.getRoundPhase();
        if (phase == RoundPhase.PRE_FLOP) { currentRound.dealFlop(); currentRound.nextPhase(); }
        else if (phase == RoundPhase.FLOP) { currentRound.dealTurn(); currentRound.nextPhase(); }
        else if (phase == RoundPhase.TURN) { currentRound.dealRiver(); currentRound.nextPhase(); }
        else { endRound(currentRound.evalWinner()); return; }
        
        turnIndex = (dealerIndex + 0) % players.size(); 
        ensureActiveTurn();
    }

    private void endRound(Player winner) {
        if (winner != null) {
            winner.addChips(currentRound.getPot());
            lastWinnerName = winner.getName();
        }
        
        // PERSISTENCE: Save new chip balances to database for ALL players
        for (Player p : players) {
            DatabaseManager.updateChips(p.getName(), p.getChips());
        }
        
        gameStarted = false;
        dealerIndex = (dealerIndex + 1) % players.size();
    }

    public synchronized void clearLastWinner() {
        this.lastWinnerName = null;
        stateVersion++;
    }

    private void ensureActiveTurn() {
        int count = 0;
        while (players.get(turnIndex % players.size()).getStatus() == PlayerStatus.FOLDED && count < players.size()) {
            turnIndex = (turnIndex + 1) % players.size();
            count++;
        }
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(turnIndex % players.size());
    }

    // Getters for JSP
    public String getId() { return id; }
    public List<Player> getPlayers() { return players; }
    public GameRound getCurrentRound() { return currentRound; }
    public boolean isGameStarted() { return gameStarted; }
    public int getSmallBlind() { return smallBlind; }
    public int getBigBlind() { return bigBlind; }
    public int getCurrentBet() { return currentBet; }
    public String getLastWinnerName() { return lastWinnerName; }
    public int getStateVersion() { return stateVersion; }
}
