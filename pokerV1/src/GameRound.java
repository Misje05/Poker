import java.util.ArrayList;

public class GameRound {

        ArrayList<Player> players = new ArrayList<Player>();
        Deck deck = new Deck();
        int pot = 0;
        int currentBet = 0;
        RoundPhase roundphase = RoundPhase.PRE_FLOP;
        ArrayList<Card> table = new ArrayList<>();

       public GameRound(ArrayList<Player> players) {
           this.players = players;
       }

       public ArrayList<Card> DealCards() {
           Card card1 = deck.dealCard();
           Card card2 = deck.dealCard();
           ArrayList<Card> cards = new ArrayList<>();
           cards.add(card1);
           cards.add(card2);
           return cards;
       }

       public void nextPhase() {
           if(roundphase == RoundPhase.PRE_FLOP) {
               roundphase = RoundPhase.FLOP;
           }
           else if(roundphase == RoundPhase.FLOP) {
               roundphase = RoundPhase.TURN;
           }
           else {
               roundphase = RoundPhase.RIVER;
           }
       }

       public Player evalWinner() {
           return Evaluator.evaluateHandRankings();
       }

}
