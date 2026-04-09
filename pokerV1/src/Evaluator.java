import java.util.ArrayList;
import java.util.List;

public class Evaluator {

    private static int handRankings;

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

    public static void checkOnePair(List<Card> tableHand) {
        for (int i = 0; i < tableHand.size(); i++) {
            for (int j = i + 1; j < tableHand.size(); j++) {
                if (tableHand.get(i).getRank().equals(tableHand.get(j).getRank())) {
                    handRankings = 2;
                    return;
                }
            }
        }
    }


    /*
     * Checks if a pair exsists in the tableHand. If it does, the rank is added to
     * pairedRanks where a for loop later counts the number of pairs, which if it's
     * equalt or greather than 2, sets the value of two pairs to the handRankings
     * variable.
     */
    public static void checkTwoPairs(List<Card> tableHand) {
        int pairCount = 0;
        List<Rank> pairedRanks = new ArrayList<>();

        for (int i = 0; i < tableHand.size(); i++) {
            if (pairedRanks.contains(tableHand.get(i).getRank())) {
                continue;
            }
            int counter = 0;

            for (int j = i + 1; j < tableHand.size(); j++) {
                if (tableHand.get(i).getRank().equals(tableHand.get(j).getRank())) {
                    counter++;
                }
            }
            if (counter >= 1) {
                pairCount++;
                pairedRanks.add(tableHand.get(i).getRank());
            }
        }
        if (pairCount >= 2) {
            handRankings = 3;
        }
    }

    public static void checkThreeOfAKind(List<Card> tableHand) {
        int counter = 0;

        for (int i = 0; i < tableHand.size(); i++) {
            counter = 0;

            for (int j = i + 1; j < tableHand.size(); j++) {
                if (tableHand.get(i).getRank().equals(tableHand.get(j).getRank())) {
                    counter++;
                }
            }
            if (counter >= 3) {
                handRankings = 4;
                return;
            }
        }
    }

    // Se gjennom
    public static void checkStraight(List<Card> tableHand) {
        // Extract unique ranks and sort them
        List<Integer> uniqueRanks = new ArrayList<>();
        for (Card card : tableHand) {
            int value = card.getRank().getValue();
            if (!uniqueRanks.contains(value)) {
                uniqueRanks.add(value);
            }
        }
        uniqueRanks.sort((a, b) -> Integer.compare(b, a));

        // Check for 5 consecutive cards
        for (int i = 0; i < uniqueRanks.size() - 4; i++) {
            if (uniqueRanks.get(i) - uniqueRanks.get(i + 4) == 4) {
                handRankings = 5;
                return;
            }
        }

        // Check for Ace-low straight (A-2-3-4-5)
        if (uniqueRanks.contains(14) && uniqueRanks.contains(2) && uniqueRanks.contains(3)
                && uniqueRanks.contains(4) && uniqueRanks.contains(5)) {
            handRankings = 5;
        }
    }

    public static void checkFlush(List<Card> tableHand) {
        int counter = 0;

        for (int i = 0; i < tableHand.size(); i++) {
            counter = 0;

            for (int j = i + 1; j < tableHand.size(); j++) {
                if (tableHand.get(i).getSuit().equals(tableHand.get(j).getSuit())) {
                    counter++;
                }
            }
            if (counter >= 5) {
                handRankings = 6;
                return;
            }
        }
    }

    // Se gjennom
    public static void checkFullHouse(List<Card> tableHand) {
        boolean hasThreeOfAKind = false;
        boolean hasPair = false;
        List<Rank> countedRanks = new ArrayList<>();

        // Check for three of a kind and pairs
        for (int i = 0; i < tableHand.size(); i++) {
            if (countedRanks.contains(tableHand.get(i).getRank())) {
                continue;
            }
            int counter = 0;

            for (int j = i + 1; j < tableHand.size(); j++) {
                if (tableHand.get(i).getRank().equals(tableHand.get(j).getRank())) {
                    counter++;
                }
            }
            countedRanks.add(tableHand.get(i).getRank());
            
            if (counter >= 2) {
                hasThreeOfAKind = true;
            } else if (counter >= 1) {
                hasPair = true;
            }
        }

        if (hasThreeOfAKind && hasPair) {
            handRankings = 7;
        }
    }

    public static void checkFourOfAKind(List<Card> tableHand) {
        int counter = 0;

        for (int i = 0; i < tableHand.size(); i++) {
            counter = 0;

            for (int j = i + 1; j < tableHand.size(); j++) {
                if (tableHand.get(i).getRank().equals(tableHand.get(j).getRank())) {
                    counter++;
                }
            }
            if (counter == 4) {
                handRankings = 8;
                return;
            }
        }
    }

    // Se gjennom
    public static void checkStraightFlush(List<Card> tableHand) {
        // For each suit, extract cards and check for straight
        for (Suit suit : Suit.values()) {
            List<Integer> suitRanks = new ArrayList<>();
            
            for (Card card : tableHand) {
                if (card.getSuit() == suit) {
                    int value = card.getRank().getValue();
                    if (!suitRanks.contains(value)) {
                        suitRanks.add(value);
                    }
                }
            }
            
            suitRanks.sort((a, b) -> Integer.compare(b, a));
            
            // Check for 5 consecutive cards in same suit
            if (suitRanks.size() >= 5) {
                for (int i = 0; i <= suitRanks.size() - 5; i++) {
                    if (suitRanks.get(i) - suitRanks.get(i + 4) == 4) {
                        handRankings = 9;
                        return;
                    }
                }
                
                // Check for Ace-low straight flush (A-2-3-4-5)
                if (suitRanks.contains(14) && suitRanks.contains(2) && suitRanks.contains(3)
                        && suitRanks.contains(4) && suitRanks.contains(5)) {
                    handRankings = 9;
                    return;
                }
            }
        }
    }

    // Se gjennom
    public static void checkRoyalFlush(List<Card> tableHand) {
        // For each suit, check if it contains A-K-Q-J-10
        for (Suit suit : Suit.values()) {
            List<Integer> suitRanks = new ArrayList<>();
            
            for (Card card : tableHand) {
                if (card.getSuit() == suit) {
                    suitRanks.add(card.getRank().getValue());
                }
            }
            
            // Check for royal flush (A-K-Q-J-10)
            if (suitRanks.contains(14) && suitRanks.contains(13) && suitRanks.contains(12)
                    && suitRanks.contains(11) && suitRanks.contains(10)) {
                handRankings = 10;
                return;
            }
        }
    }



    private static List<Card> combineAndSort(Hand hand, List<Card> table) {
        List<Card> allCards = hand.getCards();
        allCards.addAll(table);

        allCards.sort((a,b) -> b.getRank().compareTo(a.getRank()));
        return allCards;
    }

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
