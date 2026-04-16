import java.util.ArrayList;

/**
 * Represents a single round of poker, managing the deck, table cards, pot, and round phase.
 */
public class GameRound {

    ArrayList<Player> players = new ArrayList<Player>();
    Deck deck = new Deck();
    int pot = 0;
    int currentBet = 0;
    RoundPhase roundphase = RoundPhase.PRE_FLOP;
    ArrayList<Card> table = new ArrayList<>();

    /**
     * Creates a new GameRound with the given list of players.
     *
     * @param players the list of players participating in this round
     */
    public GameRound(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Deals two cards from the deck and returns them as a hand.
     *
     * @return an ArrayList containing two dealt cards
     */
    public ArrayList<Card> dealCards() {
        Card card1 = deck.dealCard();
        Card card2 = deck.dealCard();
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        return cards;
    }

    /**
     * Deals three cards from the deck onto the table (the flop).
     */
    public void dealFlop() {
        table.add(deck.dealCard());
        table.add(deck.dealCard());
        table.add(deck.dealCard());
    }

    /**
     * Deals one card from the deck onto the table (the turn).
     */
    public void dealTurn() {
        table.add(deck.dealCard());
    }

    /**
     * Deals one card from the deck onto the table (the river).
     */
    public void dealRiver() {
        table.add(deck.dealCard());
    }

    /**
     * Advances the round to the next phase in order: PRE_FLOP -> FLOP -> TURN -> RIVER.
     */
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

    /**
     * Returns the current phase of the round.
     *
     * @return the current {@link RoundPhase}
     */
    public RoundPhase getRoundPhase() {
        return roundphase;
    }

    /**
     * Sets the pot to the given amount.
     *
     * @param pot the new pot value
     */
    public void setPot(int pot) {
        this.pot = pot;
    }

    /**
     * Returns the current pot amount.
     *
     * @return the current pot value
     */
    public int getPot() {
        return pot;
    }

    /**
     * Adds the given amount to the current pot.
     *
     * @param i the amount to add to the pot
     */
    public void addToPot(int i) {
        setPot(getPot() + i);
    }

    /**
     * Evaluates all active players' hands and returns the winner.
     * Folded players are skipped. In case of equal rankings, the player
     * with higher individual card values wins.
     *
     * @return the {@link Player} with the best hand, or null if no active players remain
     */
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