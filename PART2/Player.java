import java.util.*;

public class Player {
    HashSet<Card> hand;
    int score;

    Player() {}

    Player(HashSet<Card> hand, int score) {
        this.hand = hand;
        this.score = score;
    }

    public HashSet<Card> getHand() {
        return hand;
    }

    public int getScore() {
        return score;
    }

    public void setHand(HashSet<Card> hand) {
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
