/**
 * Represents a single playing card with a suit and a rank.
 */
public class Card {
    private final Suit suit;
    private final Rank rank;

    /**
     * Creates a new card with the specified suit and rank.
     *
     * @param suit the suit of the card
     * @param rank the rank of the card
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Returns the filename of the card's image in SVG format.
     *
     * @return a string representing the image filename, e.g. "ACE_SPADES.svg"
     */
    public String getCardImage() {
        return this.rank.name() + "_" + this.suit.name() + ".svg";
    }

    /**
     * Returns the suit of the card.
     *
     * @return the card's suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the rank of the card.
     *
     * @return the card's rank
     */
    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}