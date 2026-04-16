package no.hvl.dat110.poker;
public class Card {
    private final Suit suit;
    private final Rank rank;

    /**
     * Oppretter et nytt kort med spesifisert farge og verdi.
     * @param suit Kortets farge (Enum)
     * @param rank Kortets verdi (Enum)
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }


    public String getCardImage() {
        return this.rank.name() + "_" + this.suit.name() + ".svg";
    }

    /** @return Kortets farge */
    public Suit getSuit() {
        return suit;
    }

    /** @return Kortets verdi */
    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

