import java.util.*;

public class Player {
    ArrayList<Card> hand;
    int score;

    Player() {}

    Player(ArrayList<Card> hand, int score) {
        this.hand = hand;
        this.score = score;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getScore() {
        return score;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }
}
