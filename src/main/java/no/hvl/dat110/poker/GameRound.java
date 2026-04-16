package no.hvl.dat110.poker;
import java.util.ArrayList;

public class GameRound {

    ArrayList<Player> players = new ArrayList<Player>();
    Deck deck = new Deck();
    int pot = 0;
    int currentBet = 0;
    RoundPhase roundphase = RoundPhase.PRE_FLOP;
    ArrayList<Card> table = new ArrayList<>();

    public GameRound(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Card> dealCards() {
        Card card1 = deck.dealCard();
        Card card2 = deck.dealCard();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        return cards;
    }

    public void dealFlop() {
        table.add(deck.dealCard());
        table.add(deck.dealCard());
        table.add(deck.dealCard());
    }

    public void dealTurn() {
        table.add(deck.dealCard());
    }

    public void dealRiver() {
        table.add(deck.dealCard());
    }

    public void nextPhase() {
        if (roundphase == RoundPhase.PRE_FLOP) {
            roundphase = RoundPhase.FLOP;
        }
        else if(roundphase == RoundPhase.FLOP) {
            roundphase = RoundPhase.TURN;
        }
        else {
            roundphase = RoundPhase.RIVER;
        }
    }

    public RoundPhase getRoundPhase() {
        return roundphase;
    }


    public void setPot(int pot) {
        this.pot = pot;
    }


    public int getPot() {
        return pot;
    }


    public void addToPot(int i) {
        setPot(getPot() + i);
    }


    public Player evalWinner() {
        Player winner = null;

        for (Player player : players) {
            if (player.getStatus() == PlayerStatus.FOLDED) {
                continue;
            }

            if (winner == null) {
                winner = player;
                continue;
            }

            int result = Evaluator.compareHandRankings(
                    player.getHand(),
                    winner.getHand(),
                    table
            );

            if (result > 0) {
                winner = player;
            }
        }

        return winner;
    }
}
