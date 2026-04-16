import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's hand of cards in a poker game.
 */
public class Hand {

    private List<Card> cards;

    /**
     * Creates an empty hand.
     */
    public Hand() { this.cards = new ArrayList<Card>(); }

    /**
     * Creates a hand pre-populated with the given list of cards.
     *
     * @param cards the initial list of cards for this hand
     */
    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Returns the card at the given index, or null if the index is out of bounds.
     *
     * @param index the position of the card to retrieve
     * @return the card at the specified index, or null if invalid
     */
    public Card getCard(int index) {
        if (index < 0 || index >= cards.size()) return null;
        return cards.get(index);
    }

    /**
     * Returns all cards in the hand.
     *
     * @return the list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Adds a card to the hand.
     *
     * @param card the card to add
     */
    public void addCard(Card card) { cards.add(card); }

    /**
     * Removes all cards from the hand.
     */
    public void emptyHand() {
        cards.clear();
    }

    /**
     * Returns the number of cards in the hand.
     *
     * @return the size of the hand
     */
    public int size() { return cards.size(); }

    /**
     * Returns true if the hand contains no cards.
     *
     * @return true if the hand is empty, false otherwise
     */
    public boolean isEmpty() { return cards.isEmpty(); }
}