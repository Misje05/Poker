import java.util.ArrayList;
import java.util.List;

public class Hand {

    private List<Card> cards;

    public Hand() { this.cards = new ArrayList<Card>();}

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public Card getCard(int index) {
        if (index < 0 || index >= cards.size()) return null;
        return cards.get(index);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) { cards.add(card); }

    public void emptyHand() {
        cards.clear();
    }

    public int size() { return cards.size(); }

    public boolean isEmpty() { return cards.isEmpty(); }
}
