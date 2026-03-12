import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestHand {

    Card diamondKing = new Card(Suit.DIAMONDS, Rank.KING);
    Card spadeNine = new Card(Suit.SPADES, Rank.NINE);


    @Test
    public void testAddCard(){
        List<Card> cards = new ArrayList<>();
        cards.add(diamondKing);
        cards.add(spadeNine);
        Hand h = new Hand(cards);

        assertFalse(h.isEmpty());
        assertTrue(cards.contains(diamondKing));
    }

    @Test
    public void testEmptyHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(diamondKing);
        cards.add(spadeNine);
        Hand h = new Hand(cards);
        h.emptyHand();

        assertTrue(h.isEmpty());
        assertFalse(cards.contains(spadeNine));
    }

}
