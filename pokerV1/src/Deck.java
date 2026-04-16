import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a deck of 52 playing cards containing all combinations of suits and ranks.
 */
public class Deck {
    private ArrayList<Card> cards = new ArrayList<Card>();

    /**
     * Creates a new deck and fills it with 52 unique cards.
     */
    public Deck () {
        for(Suit suit : Suit.values()) {
            for(Rank rank : Rank.values()) {
                Card card = new Card(suit, rank);
                cards.add(card);
            }
        }
    }

    /**
     * Returns the list of cards currently in the deck.
     *
     * @return an ArrayList containing all remaining cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Deals (and removes) the top card from the deck.
     *
     * @return the top card from the deck
     */
    public Card dealCard() {
        Card card = cards.getFirst();
        cards.remove(card);
        return card;
    }

    /**
     * Removes all cards from the deck.
     */
    public void reset() {
        cards.clear();
    }
}