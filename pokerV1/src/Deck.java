import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<Card>();

    public Deck (ArrayList<Card> cards) {
        this.cards = cards;
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
