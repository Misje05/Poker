import java.util.ArrayList;
import java.util.Collections;
/**
 * Representerer en kortstokk bestående av 52 spillkort.
 * Kortstokken inneholder alle kombinasjoner av {@code Suit} og {@code Rank}.
 */
public class Deck {
    private ArrayList<Card> cards = new ArrayList<Card>();
    /**
     * Oppretter en ny kortstokk og fyller den med 52 unike kort.
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
     * Returnerer listen med kort i kortstokken.
     *
     * @return en {@code ArrayList} som inneholder alle kortene
    */
    public ArrayList<Card> getCards() {
        return cards;
    }
    //Trekker (og fjerner) det øverste kortet fra kortstokken.
    public Card dealCard() {
        Card card = cards.getFirst();
        cards.remove(card);
        return card;
    }
    /**
     * Tømmer kortstokken for alle kort.
     * Etter kall på denne metoden vil kortstokken være tom.
     */
    public void reset() {
        cards.clear();
    }
}
