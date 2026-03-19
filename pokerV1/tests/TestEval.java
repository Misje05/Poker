import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestEval {

    List<Card> table = new ArrayList<>(List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.FIVE),
            new Card(Suit.DIAMONDS, Rank.SEVEN),
            new Card(Suit.SPADES, Rank.NINE),
            new Card(Suit.HEARTS, Rank.JACK)
    ));

    @Test
    public void testEvaluateHandRankings() {
        // TODO
    }

    @Test
    public void testCompareHandRankings_tie(){
        Hand h1 = new Hand(new ArrayList<>(List.of(
                new Card(Suit.SPADES, Rank.ACE),
                new Card(Suit.CLUBS, Rank.KING)
        )));
        Hand h2 = new Hand(new ArrayList<>(List.of(
                new Card(Suit.DIAMONDS, Rank.ACE),
                new Card(Suit.HEARTS, Rank.KING)
        )));
        int res = Evaluator.compareHandRankings(h1, h2, table);

        assertEquals(0, res);
    }

    @Test
    public void testCompareHandRankings_h1Wins(){
        Hand h1 = new Hand(new ArrayList<>(List.of(
                new Card(Suit.SPADES, Rank.JACK),
                new Card(Suit.DIAMONDS, Rank.JACK)
        )));
        Hand h2 = new Hand(new ArrayList<>(List.of(
                new Card(Suit.HEARTS, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.NINE)
        )));
        int res = Evaluator.compareHandRankings(h1, h2, table);

        assertEquals(1, res);
    }

    @Test
    public void testCompareHandRankings_h2Wins() {
        Hand h1 = new Hand(new ArrayList<>(List.of(
                new Card(Suit.DIAMONDS, Rank.FIVE),
                new Card(Suit.HEARTS, Rank.SEVEN)
        )));
        Hand h2 = new Hand(new ArrayList<>(List.of(
                new Card(Suit.DIAMONDS, Rank.NINE),
                new Card(Suit.SPADES, Rank.JACK)
        )));
        int res = Evaluator.compareHandRankings(h1, h2, table);

        assertEquals(-1, res);
    }

}
