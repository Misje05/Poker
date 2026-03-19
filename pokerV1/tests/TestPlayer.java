import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestPlayer {

    Player player;

    @BeforeEach
    public void setUp(){
        player = new Player("BigPlaya", 1000);
    }

    @Test
    public void testPlaceBet(){
        boolean res = player.placeBet(200);

        assertTrue(res);
        assertEquals(800, player.getChips());
    }

    @Test
    public void testPlaceBet_insufficientChips(){
        boolean res = player.placeBet(1200);

        assertFalse(res);
        assertEquals(1000, player.getChips());
    }

    @Test
    public void testRaise(){
        player.placeBet(200);
        boolean res = player.raise(300);

        assertTrue(res);
        assertEquals(500, player.getChips());
    }

    @Test
    public void testCall(){
        player.placeBet(200);
        boolean res = player.call(200);

        assertTrue(res);
        assertEquals(600, player.getChips());
    }

    @Test
    public void testFold(){
        assertFalse(player.isFolded());
        player.fold();
        assertTrue(player.isFolded());
    }

    @Test
    public void testAddCard(){
        Card c1 = new Card(Suit.HEARTS, Rank.ACE);
        Card c2 = new Card(Suit.CLUBS, Rank.KING);
        Card c3 = new Card(Suit.DIAMONDS, Rank.QUEEN);

        player.addCard(c1);
        assertEquals(1, player.getHand().size());

        player.addCard(c2);
        assertEquals(2, player.getHand().size());

        // maks two cards
        player.addCard(c3);
        assertEquals(2, player.getHand().size());
    }

    @Test
    public void testClearHand(){
        Card c1 = new Card(Suit.HEARTS, Rank.ACE);
        Card c2 = new Card(Suit.CLUBS, Rank.KING);

        player.addCard(c1);
        player.addCard(c2);
        assertEquals(2, player.getHand().size());

        player.clearHand();
        assertEquals(0, player.getHand().size());
    }

    @Test
    public void testResetForNewRound(){
        Card c1 = new Card(Suit.HEARTS, Rank.ACE);
        Card c2 = new Card(Suit.CLUBS, Rank.KING);

        player.addCard(c1);
        player.addCard(c2);
        player.setStatus(PlayerStatus.FOLDED);
        player.fold();

        player.resetForNewRound();

        assertEquals(1000, player.getChips());
        assertFalse(player.isFolded());
        assertEquals(0, player.getHand().size());
    }
}
