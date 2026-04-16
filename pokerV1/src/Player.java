import java.util.ArrayList;
import java.util.List;

/**
 * Represents a participant in a Texas Hold'em game,
 * managing their identity, chip balance, hand, and in-round actions.
 */
public class Player {

    /** The player's name or username. */
    private final String name;

    /** The player's remaining chip balance. */
    private int chips;

    /** The player's private hole cards. */
    private Hand hand;

    /** The player's current status in the round. */
    private PlayerStatus status;

    /** The player's current amount bet this street. */
    private int amountBet;

    /**
     * Creates a new player with the given name and starting chip balance.
     *
     * @param name         the name of the player
     * @param initialChips the starting chip balance for the player
     */
    public Player(String name, int initialChips) {
        this.name = name;
        this.chips = initialChips;
        this.hand = new Hand();
        this.status = PlayerStatus.ACTIVE;
    }

    /**
     * Internal helper that validates and performs a chip deduction.
     *
     * @param amount the amount to deduct from the balance
     * @return true if the deduction was successful, false if insufficient chips
     */
    private boolean deductChips(int amount) {
        if (amount > chips) {
            return false;
        }
        chips -= amount;
        return true;
    }

    /**
     * Places a bet by deducting the given amount from the player's chip balance.
     *
     * @param amount the amount to bet
     * @return true if the bet was placed successfully, false otherwise
     */
    public boolean placeBet(int amount) {
        return deductChips(amount);
    }

    /**
     * Raises by deducting the given amount from the player's chip balance.
     *
     * @param amount the amount to raise by
     * @return true if the raise was successful, false otherwise
     */
    public boolean raise(int amount) {
        return deductChips(amount);
    }

    /**
     * Calls the current bet by matching the given amount.
     *
     * @param amountToMatch the amount needed to call
     * @return true if the player had enough chips to call, false otherwise
     */
    public boolean call(int amountToMatch) {
        return placeBet(amountToMatch);
    }

    /**
     * Marks the player as folded by updating their status.
     */
    public void fold() {
        this.status = PlayerStatus.FOLDED;
    }

    /**
     * Adds the given amount of chips to the player's balance.
     *
     * @param amount the number of chips won
     */
    public void winChips(int amount) {
        this.chips += amount;
    }

    /**
     * Adds a card to the player's hand, up to a maximum of two hole cards.
     *
     * @param card the card to deal to the player
     */
    public void addCard(Card card) {
        if (hand.getCard(0) == null || hand.getCard(1) == null) {
            hand.addCard(card);
        } else {
            System.err.println("Attempted to add too many cards to " + name);
        }
    }

    /**
     * Removes all cards from the player's hand.
     */
    public void clearHand() {
        hand.emptyHand();
    }

    /**
     * Resets the player's status to ACTIVE and clears their hand before a new round.
     */
    public void resetForNewRound() {
        this.status = PlayerStatus.ACTIVE;
        clearHand();
    }

    /**
     * Returns whether the player has folded in the current round.
     *
     * @return true if the player's status is FOLDED, false otherwise
     */
    public boolean isFolded() {
        return status == PlayerStatus.FOLDED;
    }

    /**
     * Returns the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player's current hand.
     *
     * @return the player's hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Returns the player's current chip balance.
     *
     * @return the player's chip balance
     */
    public int getChips() {
        return chips;
    }

    /**
     * Returns the player's current status.
     *
     * @return the player's status
     */
    public PlayerStatus getStatus() {
        return status;
    }

    /**
     * Updates the player's status (e.g. to ALL_IN).
     *
     * @param status the new status to set
     */
    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    /**
     * Sets the player's current amount bet this street.
     *
     * @param amountBet the amount bet to set
     */
    public void setAmountBet(int amountBet) {
        this.amountBet = amountBet;
    }

    /**
     * Returns the player's current amount bet this street.
     *
     * @return the amount bet
     */
    public int getAmountBet() {
        return amountBet;
    }

    /**
     * Returns a formatted string representation of the player's current state.
     *
     * @return a string containing the player's name, chip balance, status, and hand
     */
    @Override
    public String toString() {
        return String.format("%s [%d chips], Status: %s, Hand: %s", name, chips, status, hand);
    }
}