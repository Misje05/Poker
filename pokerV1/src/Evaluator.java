import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Evaluator {

    private static int handRankings;


    // MAIN METHODS

    /*
     * Method which sets and integer for the value of what rank the player has acchieved with the
     */
    public static int evaluateHandRankings(Hand hand, List<Card> table) {

        handRankings = 0;

        List<Card> allCards = hand.getCards();
        allCards.addAll(table);

        checkHighCard();                // 1
        checkOnePair(allCards);         // 2
        checkTwoPairs(allCards);        // 3
        checkThreeOfAKind(allCards);    // 4
        checkStraight(allCards);        // 5
        checkFlush(allCards);           // 6
        checkFullHouse(allCards);       // 7
        checkFourOfAKind(allCards);     // 8
        checkStraightFlush(allCards);   // 9
        checkRoyalFlush(allCards);      // 10
        return handRankings;
    }


    // SUPPORT METHODS

    public static void checkHighCard() {
        handRankings = 1;
    }

    public static void checkOnePair(List<Card> table) {

        for (int i = 0; i < table.size(); i++) {

        }
        handRankings = 2;
    }

    public static void checkTwoPairs(List<Card> table) {
        handRankings = 3;
    }

    public static void checkThreeOfAKind(List<Card> table) {
        handRankings = 4;
    }

    public static void checkStraight(List<Card> table) {
        handRankings = 5;
    }

    public static void checkFlush(List<Card> table) {
        handRankings = 6;
    }

    public static void checkFullHouse(List<Card> table) {
        handRankings = 7;
    }

    public static void checkFourOfAKind(List<Card> table) {
        handRankings = 8;
    }

    public static void checkStraightFlush(List<Card> table) {

        handRankings = 9;
    }

    public static void checkRoyalFlush(List<Card> table) {

        handRankings = 10;
    }

    private static List<Card> combineAndSort(Hand hand, List<Card> table) {
        List<Card> allCards = hand.getCards();
        allCards.addAll(table);

        allCards.sort((a,b) -> b.getRank().compareTo(a.getRank()));
        return allCards;
    }


    // EGOR

    public static int compareHandRankings(Hand h1, Hand h2, List<Card> table){
        int h1Rank = evaluateHandRankings(h1, table);
        int h2Rank = evaluateHandRankings(h2, table);

        if (h1Rank != h2Rank){
            return Integer.compare(h1Rank, h2Rank);
        }

        List<Card> cardsH1 = combineAndSort(h1, table);
        List<Card> cardsH2 = combineAndSort(h2, table);

        for (int i = 0; i < Math.min(cardsH1.size(), cardsH2.size()); i++){
            if (cardsH1.get(i).getRank().getValue() > cardsH2.get(i).getRank().getValue()){
                return 1;
            }
            else if (cardsH1.get(i).getRank().getValue() < cardsH2.get(i).getRank().getValue()){
                return -1;
            }
        }
        return 0;
    }
}
