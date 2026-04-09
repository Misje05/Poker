import java.util.List;

public class Hand {

    private List<Card> cards;

    public Hand() {}

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public Card getCard(int index) { return cards.get(index); }

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
