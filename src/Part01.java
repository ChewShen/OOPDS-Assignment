import java.util.*;

public class Part01 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Random random = new Random();

        ArrayList<Card> deck = new ArrayList<>();

        // 4 suits - clubs, spades, diamonds, hearts
        String[] cardSuit = {"c","s","d","h"};
        // 13 cards per suit = 52 cards in a deck
        String[] cardRank = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        
        ArrayList<Card> center = new ArrayList<>();

        //creates 4 players and gives them each an empty hand and score of 0
        ArrayList<Player> players = new ArrayList<Player>();
        int numOfPlayers = 4;
        for (int i = 0; i < numOfPlayers; i++)
            players.add(new Player(new ArrayList<Card>(), 0));

        boolean newGame = false;
        boolean roundEnd = false;

        while (!newGame) {
        roundEnd = false;

        // adds 52 cards to the deck
        deck.clear();
        for (int i = 0; i < cardSuit.length; i++) {
            for (int j = 0; j < cardRank.length; j++) {
                deck.add(new Card(cardSuit[i], cardRank[j]));
            }
        }

        //shuffles the deck so the 52 cards are randomized
        Collections.shuffle(deck);
        
        //adds the first card in the deck to the center and removes it from the deck
        center.clear();
        center.add(deck.get(0));
        deck.remove(0);

        int previousTurn = 0;
        //turn + 1 = which player's turn it is
        int turn = 0;

        switch (center.get(0).getRank()) {
        case "A":
        case "5":
        case "9":
        case "K":
            turn = 0;
            break;

        case "2":
        case "6":
        case "10":
            turn = 1;
            break;

        case "3":
        case "7":
        case "J":
            turn = 2;
            break;

        case "4":
        case "8":
        case "Q":
            turn = 3;
            break;
        }

        //randomly distributes 7 cards to each player
        for (int i = 0; i < numOfPlayers; i++) {
            players.get(i).hand.clear();
            for (int j = 0; j < 7; j++) {
                int randomIndex = random.nextInt(deck.size());
                players.get(i).addCardToHand(deck.get(randomIndex));
                deck.remove(randomIndex);
            }
        }

        //records the cards played to see who played the highest value
        ArrayList< Map<Card, Player> > sameSuit = new ArrayList<>();

        int trickNum = 1;
        //each trick begins here
        while (!roundEnd) {
        int numOfPlays = 0;

        while (numOfPlays < 4) {
            //displays trick number
            System.out.println("\nTrick #" + trickNum);

            //displays each player's deck
            for (int i = 0; i < numOfPlayers; i++)
                System.out.println("Player" + (i + 1) + ": " + players.get(i).getHand());
            
            //displays the center deck
            System.out.println("Center : " + center);
            //displays the main deck
            System.out.println("Deck   : " + deck);

            //displays score for each player
            System.out.print("Score: ");
            for (int i = 0; i < numOfPlayers; i++)
                System.out.print("Player" + (i + 1) + ": " + players.get(i).getScore() + " | ");
            System.out.print("\n");

            //displays which player's turn it is
            System.out.print("Turn : Player" + (turn + 1) + "\n");

            System.out.print("> ");

            String command = input.nextLine();
            command = command.toLowerCase(); //

            switch (command) {
            // start a new game
            case "s":
                newGame = true;
                break;

            //exit the game
            case "x":
                System.exit(0);
                break;

            //draw cards into player's deck until playable card obtained
            case "d":
                boolean validCard = false;

                while (!validCard) {
                    //checks if there are any cards in deck
                    if (deck.size() > 0) {
                        int randomIndex = random.nextInt(deck.size());
                        players.get(turn).addCardToHand(deck.get(randomIndex));
                        System.out.println("Drew card " + deck.get(randomIndex) + ".");

                        if (deck.get(randomIndex).getRank().equals(center.get(0).getRank())
                        || deck.get(randomIndex).getSuit().equals(center.get(0).getSuit())) {
                            validCard = true;
                        }
                        deck.remove(randomIndex);
                    }
                    //if no cards, skips player's turn
                    else {
                        System.out.println("No cards available to be drawn.");
                        System.out.println("Player" + (players.indexOf(players.get(turn)) + 1) + "\'s turn will be skipped.");
                        
                        numOfPlays += 1;
                        switch(turn) {
                            case 0:
                                turn = 1;
                                break;
                            case 1:
                                turn = 2;
                                break;
                            case 2:
                                turn = 3;
                                break;
                            case 3:
                                turn = 0;
                                break;
                            }

                        break;
                    }
                }

                break;

            case "":
                System.out.println("Not a valid input.");
                break;

            default:

            //depending on which player's turn it is, moves card to the center
            Card cardToMove;
            Map<Card, Player> cardPlayed = new HashMap<>();
            boolean cardMatchCenter = false;
            boolean cardInHand = false;
            while (!cardMatchCenter && !cardInHand) {
                String chosenCard = command;
                String chosenCardSuit = Character.toString(chosenCard.charAt(0));
                chosenCardSuit = chosenCardSuit.toLowerCase();
                //substring instead of charAt because card rank may be equal to 10
                String chosenCardRank = chosenCard.substring(1);
                chosenCardRank = chosenCardRank.toUpperCase();

                cardToMove = new Card(chosenCardSuit, chosenCardRank);
            
                //checks if the card exist in player's hand
                //if yes, moves the card to center deck
                //if no, throws exception
                //limit of for loop = number of cards in player's hand
                for (int i = 0; i < players.get(turn).hand.size(); i++) {
                    if (players.get(turn).hand.get(i).isEqual(cardToMove)) {
                        cardInHand = true;

                        if (center.size() == 0) {
                            center.add(cardToMove);
                            players.get(turn).removeCardFromHand(players.get(turn).hand.get(i));
                            
                            cardPlayed.clear();
                            cardPlayed.put(cardToMove, players.get(turn));
                            sameSuit.add(cardPlayed);

                            cardMatchCenter = true;
                            numOfPlays += 1;
                            break;
                        }

                        cardToMove = players.get(turn).hand.get(i);
                        break;
                    }
                    else {
                        cardInHand = false;
                    }
                }
                if (!cardInHand) {
                    System.out.println("Card does not exist in the player's deck.");
                    break;
                }
                
                //checks if the card matches the rank/suit of the lead card
                //only makes the check if there is at least 1 card in center
                //if yes, allows the move
                //if no, throws exception
                if (!cardMatchCenter) {
                    if (cardToMove.getSuit().equals(center.get(0).getSuit())) {
                        cardMatchCenter = true;
                        players.get(turn).removeCardFromHand(cardToMove);
                        center.add(cardToMove);
                        
                        cardPlayed.clear();
                        cardPlayed.put(cardToMove, players.get(turn));
                        sameSuit.add(cardPlayed);

                        numOfPlays += 1;
                    }

                    else if (cardToMove.getRank().equals(center.get(0).getRank())) {
                        cardMatchCenter = true;
                        players.get(turn).removeCardFromHand(cardToMove);
                        center.add(cardToMove);
                        
                        cardPlayed.clear();
                        cardPlayed.put(cardToMove, players.get(turn));
                        
                        numOfPlays += 1;
                    }

                    else {
                        System.out.println("Card does not have a matching rank/suit.");
                        cardMatchCenter = false;
                        break;
                    }
                }// end of moving card
            }

            previousTurn = turn;
            
            if (cardMatchCenter && cardInHand) {
                switch(turn) {
                    case 0:
                        turn = 1;
                        break;
                    case 1:
                        turn = 2;
                        break;
                    case 2:
                        turn = 3;
                        break;
                    case 3:
                        turn = 0;
                        break;
                    }
            }

            } // end of command switch

            if (players.get(previousTurn).hand.isEmpty()) {
                System.out.println("* * * Player" + (players.indexOf(players.get(previousTurn)) + 1) + " wins the round * * *");
                roundEnd = true;
                break;
            }

            if (newGame == true)
                break;

        } // end of player's turn

        if (newGame == true)
            break;

        //checks the cardsToCompare to see who played the card with the highest rank
        //has to be card with same suit as lead card
        //if 2 have same rank, player who came first wins

        Card highestRank = new Card();
        int winnerIndex = 0;

        //temporarily assigns highest ranked card to
        //the first entry in sameSuit
        for (var entry: sameSuit.get(0).entrySet())
            highestRank = entry.getKey();

        //compares all other entries in sameSuit to find highest valued card
        for (int i = 1; i < sameSuit.size(); i++) {
            for (var entry: sameSuit.get(i).entrySet())
                if (entry.getKey().getValue() > highestRank.getValue()) {
                    highestRank = entry.getKey();
                    winnerIndex = i;
                }
        }

        if (!roundEnd)
            System.out.println("* * * Player" + (players.indexOf(sameSuit.get(winnerIndex).get(highestRank)) + 1) + " wins Trick #" + trickNum + " * * *");

        //assigns turn to winning player
        turn = players.indexOf(sameSuit.get(winnerIndex).get(highestRank));
        trickNum += 1;

        //clears the sameSuit and center decks to start a new trick
        sameSuit.clear();
        center.clear();
        } // end of trick

        int totalScore = 0;
        //records each player's score at the end of a round
        if (roundEnd == true) {
            for (int i = 0; i < numOfPlayers; i++) {
                totalScore = 0;
                for (int j = 0; j < players.get(i).hand.size(); j++) {
                    totalScore += players.get(i).hand.get(j).getValue();
                }
                players.get(i).setScore(totalScore);
            }
        }

        newGame = false;
        } // end of round
    }
}