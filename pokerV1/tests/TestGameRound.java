import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameRound {

    @Test
    public void testDealCard() {
        ArrayList<Player> players = new ArrayList<>();
        GameRound gr = new GameRound(players);

        ArrayList<Card> cards = gr.dealCards();

        assertNotNull(cards);
        assertEquals(2, cards.size());
        assertNotNull(cards.get(0));
        assertNotNull(cards.get(1));
    }

    @Test
    public void testNextPhase() {
        ArrayList<Player> players = new ArrayList<>();
        GameRound gr = new GameRound(players);

        assertEquals(RoundPhase.PRE_FLOP, gr.roundphase);
        gr.nextPhase();
        assertEquals(RoundPhase.FLOP, gr.roundphase);
        gr.nextPhase();
        assertEquals(RoundPhase.TURN, gr.roundphase);
        gr.nextPhase();
        assertEquals(RoundPhase.RIVER, gr.roundphase);
    }
}
