import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        int number = Integer.parseInt(JOptionPane.showInputDialog("How many players?"));
        ArrayList<Player> players = new ArrayList<Player>(number);


        for(int i = 0; i < number; i++) {
            String name =  JOptionPane.showInputDialog("Name of the " + i + " player:");
            int chips = Integer.parseInt(JOptionPane.showInputDialog("Amount of chips of the " + i + " player:"));
            Player player = new Player(name,chips);
            players.add(player);
        }


        int smallBlind = Integer.parseInt(JOptionPane.showInputDialog("Write small blind:"));
        int lastBet = smallBlind * 2;
        int sbIndex = 0;
        int index = 2;
        ArrayList<Player> copyPlayers = new ArrayList<>();
        for(Player player : players) {
            copyPlayers.add(player);
        }


        do{
            GameRound gameRound = new GameRound(copyPlayers);


            gameRound.setPot(smallBlind * 3);
            /** Trenger en til doWhile **/
            if (gameRound.getRoundPhase() != RoundPhase.PRE_FLOP) {
                lastBet = 0;
            }


            for(Player player : players){
                ArrayList<Card> cards = new ArrayList<>();
                cards = gameRound.dealCards();
                player.addCard(cards.get(0));
                player.addCard(cards.get(1));
            }


            players.get(sbIndex % players.size()).placeBet(smallBlind);
            players.get(sbIndex + 1 % players.size()).placeBet(smallBlind * 2);
            players.get(sbIndex % players.size()).setAmountBet(smallBlind);
            players.get(sbIndex + 1 % players.size()).setAmountBet(smallBlind * 2);
            sbIndex++;


            int bogusRound = 1;


            while(bogusRound < players.size()) {
                if(players.get(index).getStatus() == PlayerStatus.FOLDED || players.get(index).getStatus() == PlayerStatus.ALL_IN) {
                    bogusRound++;
                    index++;


                }
                else if(players.get(index).getChips() <= lastBet) {
                    Object[] options1 = { "Fold", "All in" };
                    int choice1 = JOptionPane.showOptionDialog(
                            null,
                            "Choose an action " + players.get(index).getName(),
                            "Actions",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options1,
                            null
                    );
                    if(choice1 == JOptionPane.YES_OPTION) {
                        players.get(index).setStatus(PlayerStatus.FOLDED);
                        bogusRound++;
                        index++;
                    }
                    else {
                        players.get(index).setStatus(PlayerStatus.ALL_IN);
                        int remainingChips = players.get(index).getChips();
                        players.get(index).placeBet(remainingChips);
                        gameRound.addToPot(remainingChips);
                        players.get(index).setAmountBet(remainingChips + players.get(index).getAmountBet());
                        bogusRound++;
                        index++;
                    }


                } else {
                    Object[] options2 = { "Fold", "Call/Check", "Raise" };
                    int choice2 = JOptionPane.showOptionDialog(
                            null,
                            "Choose an action " + players.get(index).getName(),
                            "Actions",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options2,
                            null);


                    if(choice2 == JOptionPane.YES_OPTION) {
                        players.get(index).setStatus(PlayerStatus.FOLDED);
                        bogusRound++;
                        index++;
                    }
                    else if(choice2 == JOptionPane.NO_OPTION) {
                        if(lastBet == 0) {
                            bogusRound++;
                            index++;
                        } else {
                            int newBet = lastBet -  players.get(index).getAmountBet();
                            players.get(index).placeBet(newBet);
                            gameRound.addToPot(newBet);
                            players.get(index).setAmountBet(newBet + players.get(index).getAmountBet());
                            bogusRound++;
                            index++;
                        }
                    }
                    else {
                        bogusRound = 1;
                        int newBet = Integer.parseInt(JOptionPane.showInputDialog("Choose amount to bet, Latest bet was: " + lastBet));
                        if(players.get(index).getChips() == newBet) {
                            players.get(index).setStatus(PlayerStatus.ALL_IN);
                        }
                        players.get(index).placeBet(newBet - players.get(index).getAmountBet());
                        gameRound.addToPot  (newBet - players.get(index).getAmountBet());
                        players.get(index).setAmountBet(newBet);
                        lastBet = newBet;
                    }
                }
            }
            gameRound.nextPhase();
        } while(copyPlayers.size() > 1);


        System.out.println("Player " + players.getFirst().getName() + " has won!");
    }
}
