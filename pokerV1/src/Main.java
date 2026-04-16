import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int number = getIntInput("How many players?", 1, 10);
        if (number == -1) return;
        
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            String[] options = {"Login", "Register", "Cancel"};
            int choice = JOptionPane.showOptionDialog(null, "Player " + (i + 1) + ": Choose an action", "Poker Login",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (choice == 2 || choice == -1) return; // Cancel or closed

            String name = JOptionPane.showInputDialog("Username:");
            String pass = JOptionPane.showInputDialog("Password:");

            if (choice == 1) { // REGISTER
                if (DatabaseManager.registerPlayer(name, pass)) {
                    JOptionPane.showMessageDialog(null, "Registration successful! You can now login.");
                    i--; // Go back to login for this player
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed. Username might be taken.");
                    i--;
                }
                continue;
            }

            // LOGIN
            int chips = DatabaseManager.loginPlayer(name, pass);
            if (chips != -1) {
                players.add(new Player(name, chips));
                JOptionPane.showMessageDialog(null, "Welcome " + name + "! Chips: " + chips);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid login for " + name);
                i--;
            }
        }

        int smallBlind = getIntInput("Write small blind value:", 1, 10000);
        if (smallBlind == -1) return;
        int dealerIndex = 0;

        while (players.size() > 1) {
            GameRound gameRound = new GameRound(players);
            
            for (Player p : players) {
                p.resetForNewRound();
                p.setAmountBet(0);
            }

            // Blinds
            int sbIdx = (dealerIndex + 1) % players.size();
            int bbIdx = (dealerIndex + 2) % players.size();
            players.get(sbIdx).placeBet(smallBlind);
            players.get(sbIdx).setAmountBet(smallBlind);
            players.get(bbIdx).placeBet(smallBlind * 2);
            players.get(bbIdx).setAmountBet(smallBlind * 2);
            gameRound.setPot(smallBlind * 3);

            // Deal cards
            for (Player player : players) {
                ArrayList<Card> cards = gameRound.dealCards();
                player.addCard(cards.get(0));
                player.addCard(cards.get(1));
            }

            int lastBet = smallBlind * 2;
            int startIdx = (dealerIndex + 3) % players.size();

            while (true) {
                lastBet = runBettingStreet(players, gameRound, startIdx, lastBet);
                if (countActivePlayers(players) <= 1) break;

                RoundPhase current = gameRound.getRoundPhase();
                if (current == RoundPhase.PRE_FLOP) gameRound.dealFlop();
                else if (current == RoundPhase.FLOP) gameRound.dealTurn();
                else if (current == RoundPhase.TURN) gameRound.dealRiver();
                else if (current == RoundPhase.RIVER) break;

                gameRound.nextPhase();
                lastBet = 0;
                for (Player p : players) p.setAmountBet(0);
                startIdx = (dealerIndex + 1) % players.size(); 
            }

            // SHOWDOWN / AWARD POT
            Player winner = gameRound.evalWinner();
            if (winner != null) {
                JOptionPane.showMessageDialog(null, "🏆 " + winner.getName() + " wins " + gameRound.getPot() + " chips!");
                winner.winChips(gameRound.getPot());
                
                // SAVE TO DATABASE
                DatabaseManager.updateChips(winner.getName(), winner.getChips());
            }

            // UPDATE LOSERS TOO (so their chips go down in DB)
            for (Player p : players) {
                if (p != winner) {
                    DatabaseManager.updateChips(p.getName(), p.getChips());
                }
            }

            // ELIMINATION
            players.removeIf(p -> p.getChips() <= 0);
            dealerIndex = (dealerIndex + 1) % (players.isEmpty() ? 1 : players.size());

            if (players.size() > 1) {
                int choice = JOptionPane.showConfirmDialog(null, "Start next round?", "P20 Poker", JOptionPane.YES_NO_OPTION);
                if (choice != JOptionPane.YES_OPTION) break;
            }
        }

        if (!players.isEmpty()) {
            Player maks = players.get(0);
            for(Player p : players) {
                if(p.getChips() > maks.getChips()) {
                    maks = p;
                }
            }
            JOptionPane.showMessageDialog(null, "🎊 CHAMPION: " + maks.getName() + " 🎊");
        }
    }

    private static int runBettingStreet(ArrayList<Player> players, GameRound round, int index, int lastBet) {
        int playersProcessed = 0;
        while (playersProcessed < players.size()) {
            Player p = players.get(index % players.size());
            
            // Skip folded/all-in players
            if (p.getStatus() == PlayerStatus.FOLDED || p.getStatus() == PlayerStatus.ALL_IN) {
                playersProcessed++;
                index++;
                continue;
            }

            // Skip if only 1 player left
            if (countActivePlayers(players) <= 1) break;

            String statusInfo = String.format("%s\nPot: %d\nLast Bet: %d\n%s: %d chips (Active bet: %d)",
                    round.getRoundPhase(), round.getPot(), lastBet, p.getName(), p.getChips(), p.getAmountBet());

            Object[] options = (lastBet > p.getAmountBet()) ? new Object[]{"Fold", "Call", "Raise"} : new Object[]{"Fold", "Check", "Raise"};
            int choice = JOptionPane.showOptionDialog(null, statusInfo, "Your Action", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);

            if (choice == JOptionPane.YES_OPTION) { // FOLD
                p.setStatus(PlayerStatus.FOLDED);
            } else if (choice == JOptionPane.NO_OPTION) { // CALL / CHECK
                int toCall = lastBet - p.getAmountBet();
                if (toCall >= p.getChips()) {
                    toCall = p.getChips();
                    p.setStatus(PlayerStatus.ALL_IN);
                }
                p.placeBet(toCall);
                round.addToPot(toCall);
                p.setAmountBet(p.getAmountBet() + toCall);
            } else if (choice == JOptionPane.CANCEL_OPTION) { // RAISE
                String rStr = JOptionPane.showInputDialog("Raise to (Total amount):", lastBet + 20);
                if (rStr == null) rStr = String.valueOf(lastBet);
                int newBet = Integer.parseInt(rStr);
                if (newBet < lastBet) newBet = lastBet;
                
                int toAdd = newBet - p.getAmountBet();
                if (toAdd >= p.getChips()) {
                    toAdd = p.getChips();
                    newBet = p.getAmountBet() + toAdd;
                    p.setStatus(PlayerStatus.ALL_IN);
                }
                
                p.placeBet(toAdd);
                round.addToPot(toAdd);
                p.setAmountBet(newBet);
                
                if (newBet > lastBet) {
                    lastBet = newBet;
                    playersProcessed = 0; // Reset as someone raised
                }
            }

            playersProcessed++;
            index++;
        }
        return lastBet;
    }

    private static int countActivePlayers(List<Player> players) {
        int count = 0;
        for (Player p : players) {
            if (p.getStatus() != PlayerStatus.FOLDED) count++;
        }
        return count;
    }

    private static int getIntInput(String message, int min, int max) {
        while (true) {
            String in = JOptionPane.showInputDialog(message);
            if (in == null) return -1;
            try {
                int val = Integer.parseInt(in);
                if (val >= min && val <= max) return val;
            } catch (Exception ignored) {}
            JOptionPane.showMessageDialog(null, "Please enter a number between " + min + " and " + max);
        }
    }
}
