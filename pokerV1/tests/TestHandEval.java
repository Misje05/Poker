import org.junit.jupiter.api.Test;

import java.util.List;

public class TestHandEval {

    Hand hand = new Hand(List.of(
            new Card(Suit.CLUBS, Rank.TWO),
            new Card(Suit.HEARTS, Rank.FIVE),
            new Card(Suit.SPADES, Rank.NINE),
            new Card(Suit.DIAMONDS, Rank.JACK),
            new Card(Suit.CLUBS, Rank.KING)
    ));

    @Test
    public void testIsHighCard() {
        assertTrue(Rank.KING.isHighCard());

    }

}
