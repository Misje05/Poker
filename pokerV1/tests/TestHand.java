import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestHand {

    Hand h = new Hand(new ArrayList<>());
    Card c1 = new Card(Suit.DIAMONDS, Rank.KING);
    Card c2 = new Card(Suit.SPADES, Rank.NINE);

    @Test
    public void testAddCard(){
        h.addCard(c1);
        h.addCard(c2);

        assertFalse(h.isEmpty());
        assertTrue(h.getCards().contains(c1));
        assertTrue(h.getCards().contains(c2));
    }

    @Test
    public void testEmptyHand() {
        h.addCard(c1);
        h.addCard(c2);
        h.emptyHand();

        assertTrue(h.isEmpty());
        assertFalse(h.getCards().contains(c2));
    }

}
