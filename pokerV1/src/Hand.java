import java.util.LinkedList;
import java.util.List;

public class Hand {

    private List<Card> cards;

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void emptyHand() {
        cards.clear();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
