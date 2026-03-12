import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<Card>();

    public Deck () {
        for(Suit suit : Suit.values()) {
            for(Rank rank : Rank.values()) {
                Card card = new Card(suit, rank);
                cards.add(card);
            }
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        Card card = cards.getFirst();
        cards.remove(card);
        return card;
    }

    public void reset() {
        cards.clear();
    }
}
