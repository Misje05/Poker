import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeck {

    @Test
    public void testShuffle(){
        Deck d1 = new Deck();
        Deck d2 = new Deck();
        d1.shuffle();
        d2.shuffle();

        assertNotEquals(d1.getCards(), d2.getCards());
    }

    @Test
    public void testDealCard() {
        Deck d = new Deck();
        int initialSize = d.getCards().size();
        Card dealt = d.dealCard();

        assertNotNull(dealt);
        assertEquals(initialSize - 1, d.getCards().size());
    }

    @Test
    public void testReset(){
        Deck d = new Deck();
        d.dealCard();
        d.dealCard();
        d.reset();

        assertEquals(0, d.getCards().size());
    }

}
