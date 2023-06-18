import java.io.File;        //import the file class
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException; //import IOException class to handle errors
import java.util.*;

import javax.lang.model.util.ElementScanner14;

public class Part02 {
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
            players.add(new Player(new HashSet<Card>(), 0));

        boolean newGame = false;
        boolean roundEnd = false;

        System.out.println("\nBefore Start, Please be aware of how to play the game.");
        System.out.println("Heres are the rules and commands.");
        promptEnterKey();
        rule();
        System.out.print("\n");
        Guide();

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
        int errors = 0;
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
            case "":
                if (errors<=3){
                    System.out.println("Not a valid input.");
                    errors = errors +1;
                    promptEnterKey();
                    break;
                }

                else if(errors > 3){
                    System.out.println("Seems like you need some help");
                    errors = 0;
                    Guide();
                    break;
                }
                
            
            case "h":
                System.out.println("Loading GuideBook....");
                promptEnterKey();
                Guide();
                break;

            case "g":
                System.out.println("Displaying game rule....");
                promptEnterKey();
                rule();
                promptEnterKey();
                break;


            case "s":
                System.out.println("Are you sure to restart the game? [Y/N]");
                String comfirmation = input.nextLine();
                comfirmation = comfirmation.toUpperCase();
                    if (comfirmation.equals("Y")){
                        newGame = true;
                        promptEnterKey();
                        break;                       
                    }

                    else if(comfirmation.equals("N")){
                        System.out.println("\nContinuing...");
                        promptEnterKey();
                        continue;                    
                    }

                    else{
                        System.out.println("\nInvalid input, continuing...");
                        promptEnterKey();
                        continue;
                    }
                    
                    
            
            //resume previous game
            case "r":
                System.out.println("Please enter the filename to load your game: ");
                String loadFile = input.nextLine();
                String data;
                try {
                    File myObj = new File(loadFile + ".txt");
                    Scanner myReader = new Scanner(myObj);
                    data = myReader.nextLine();                 //load trickNum
                    trickNum = Integer.valueOf(data);           
                    data = myReader.nextLine();
                    String[] player1_array = data.split(",");   //load player 1 hand cards
                    String suit;
                    String rank;
                    HashSet<Card> list1 = new HashSet<>();
                    players.get(0).setHand(list1);
                    for(int i =0;i<player1_array.length ; i++){
                        suit = ""+ player1_array[i].charAt(0);
                        if (player1_array[i].length() > 2){
                            rank = "10";
                        }
                        else{
                            rank = "" + player1_array[i].charAt(1);
                        }
                        players.get(0).addCardToHand(new Card(suit,rank));
                    }
                    data = myReader.nextLine();                 //load player 2 hand cards
                    String[] player2_array = data.split(",");
                    HashSet<Card> list2 = new HashSet<>();
                    players.get(1).setHand(list2);
                    for(int i =0;i<player2_array.length ; i++){
                        suit = ""+ player2_array[i].charAt(0);
                        if (player2_array[i].length() > 2){
                            rank = "10";
                        }
                        else{
                            rank = "" + player2_array[i].charAt(1);
                        }
                        players.get(1).addCardToHand(new Card(suit,rank));
                    }
                    data = myReader.nextLine();                 //load player 3 hand cards
                    String[] player3_array = data.split(","); 
                    HashSet<Card> list3 = new HashSet<>();    
                    players.get(2).setHand(list3);
                    for(int i =0;i<player3_array.length ; i++){
                        suit = ""+ player3_array[i].charAt(0);
                        if (player3_array[i].length() > 2){
                            rank = "10";
                        }
                        else{
                            rank = "" + player3_array[i].charAt(1);
                        }
                        players.get(2).addCardToHand(new Card(suit,rank));
                    }
                    data = myReader.nextLine();                 //load player 4 hand cards
                    String[] player4_array = data.split(","); 
                    HashSet<Card> list4 = new HashSet<>();
                    players.get(3).setHand(list4);
                    for(int i =0;i<player4_array.length ; i++){
                        suit = ""+ player4_array[i].charAt(0);
                        if (player4_array[i].length() > 2){
                            rank = "10";
                        }
                        else{
                            rank = "" + player4_array[i].charAt(1);
                        }
                        players.get(3).addCardToHand(new Card(suit,rank));
                    }
                    data = myReader.nextLine();                 //load center card
                    if (data == ""){
                        center.clear();
                    }
                    else {
                        String[] center_array = null;               
                        center_array = data.split(",");
                        center.clear();
                        for (int i = 0 ; i < center_array.length; i ++){
                            suit = "" + center_array[i].charAt(0);
                            if (center_array[i].length() > 2){
                                rank = "10";
                            }
                            else{
                                rank = "" + center_array[i].charAt(1);
                            }
                            center.add(new Card(suit, rank));
                        }
                    }
                    data = myReader.nextLine();                 //load deck card
                    if (data == ""){
                            deck.clear();
                    }
                    else {
                        String[] deck_array = data.split(",");
                        deck.clear();
                        for (int i = 0 ; i < deck_array.length; i ++){
                            suit = "" + deck_array[i].charAt(0);
                            if (deck_array[i].length() > 2){
                                rank = "10";
                            }
                            else{
                                rank = "" + deck_array[i].charAt(1);
                            }
                            deck.add(new Card(suit, rank));
                        }                      
                    }            
                    for (int j = 0; j < numOfPlayers; j++){     //load players' score
                        data = myReader.nextLine();
                        int score = Integer.valueOf(data);
                        players.get(j).setScore(score);
                    }
                    data = myReader.nextLine();                 //load turn
                    turn = Integer.valueOf(data);
                    System.out.println("\nFile loaded successfully.");
                    myReader.close();
                    } 
                catch (FileNotFoundException e) {
                    System.out.println("\nFile does not exist.");
                    e.printStackTrace();
                    }
                promptEnterKey();
                break;

            //exit the game
            case "x":
                System.out.println("Do you want to save your current game? (Y/N)");
                String save = input.next();
                save = save.toUpperCase();
                switch (save) {
                //save the game
                case "Y":
                    boolean fileExists = false;
                    System.out.println("Please enter the filename to save your game: ");
                    String filename = input.next();  
                    input.nextLine();     
                    //open new file with user input filename
                    try {
                        File myFile = new File(filename + ".txt");
                        if (myFile.createNewFile()) {
                            System.out.println("File created: " + myFile.getName());
                        } 
                        else {
                            fileExists = true;
                            System.out.println("File already exists.");
                        }

                        if (fileExists) {
                            System.out.println("Overwrite the file? [Y/N]");
                            String overwriteConfirm = input.next();
                            input.nextLine();
                            overwriteConfirm = overwriteConfirm.toUpperCase();

                            if (overwriteConfirm.equals("N"))
                                break;
                            else if (!overwriteConfirm.equals("Y")) {
                                System.out.println("Not a valid input.");
                                break;
                            }
                        }
                    } 
                    catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                    }
                    //save data into the file
                    try {
                        //open the file in FileWriter
                        FileWriter myWriter = new FileWriter(filename+".txt");  
                        myWriter.write(trickNum + "\n");                        //save trick number
                        
                        int cardsInHand;
                        for (int i = 0; i < numOfPlayers; i++){                 //save player handcards
                            //for (int j = 0; j < players.get(i).hand.size(); j++) 
                            for (Card card : players.get(i).hand) {
                                cardsInHand = 0;
                                if (cardsInHand < players.get(i).hand.size() - 1){
                                    myWriter.write(card + ",");
                                    cardsInHand++;
                                }
                                else {
                                    myWriter.write(card + "");
                                    cardsInHand++;
                                }
                            }
                            myWriter.write("\n");
                        }
                        if (center.size() != 0){
                            for (int i = 0;i < center.size();i++){                  //save center card
                            if (i < center.size()-1){
                                myWriter.write(center.get(i) + ",");                          
                            }
                            else{
                               myWriter.write(center.get(i) + "\n");                         
                            }
                            }
                        }
                        else if (center.size() == 0){
                            myWriter.write("\n");
                        }
                        if (deck.size() != 0){
                            for (int i = 0; i < deck.size(); i++){                   //save deck
                            if (i < deck.size()-1){
                                myWriter.write(deck.get(i) + ",");                          
                            }
                            else{
                               myWriter.write(deck.get(i) + "\n");                         
                            }
                            }                        
                        }
                        else if (deck.size() == 0){
                            myWriter.write("\n");
                        }
                        
                        for (int i = 0; i < numOfPlayers; i++)
                            myWriter.write(players.get(i).getScore() + "\n");   //save players' scores
                        myWriter.write(turn + "\n");                            //save turn
                        //close the file
                        myWriter.close();   
                        System.out.println("Successfully wrote to the file.");

                        System.out.println("Do you want to quit the game? [Y/N]");
                        String quitting = input.nextLine();
                        quitting = quitting.toLowerCase();

                        if (quitting.equals("y")){
                            System.out.println("\nQuitting game....");
                            System.out.println("See you again, Bye Bye!");
                            System.exit(0);
                        }
                        else if (quitting.equals("n")){
                            System.out.println("Game will continue...");
                            promptEnterKey();
                            continue;
                        }
                        else {
                            System.out.println("Invalid input. Game continuing....");
                            promptEnterKey();
                            continue;
                        }
                        } 
                    catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                    break;
                
                //don't save
                case "N":
                    System.out.println("\nQuitting game...");
                    System.out.println("See you again, Bye Bye.");
                    System.exit(0);
                    break;
                }
                break;

            //draw cards into player's deck until playable card obtained
            case "d":
                boolean validCard = false;

                while (!validCard) {
                    if (center.size() == 0) {
                        System.out.println("There are no cards in the center.");
                        System.out.println("Unable to draw card.");
                        break;
                    }
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
                for (Card card : players.get(turn).hand) {
                    if (card.isEqual(cardToMove)) {
                        cardInHand = true;

                        if (center.size() == 0) {
                            center.add(cardToMove);
                            players.get(turn).removeCardFromHand(card);
                            
                            cardPlayed.clear();
                            cardPlayed.put(cardToMove, players.get(turn));
                            sameSuit.add(cardPlayed);

                            cardMatchCenter = true;
                            numOfPlays += 1;
                            break;
                        }

                        cardToMove = card;
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

        System.out.println("Continue for another round...");
        promptEnterKey();
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
                for (Card card : players.get(i).hand) {
                    totalScore += card.getValue();
                }
                players.get(i).setScore(totalScore);
            }
        }

        newGame = false;
        } // end of round
    }

    public static void promptEnterKey(){
        System.out.print("\n");
        System.out.println("Press \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

    }

    public static void Guide(){
        System.out.println("+----------------------------------------------------------------------------------+");
        System.out.println("|                                   Command Guidebook                              |");
        System.out.println("+---+------------------------------------------------------------------------------+");
        System.out.println("| s | To reset the current game. (No progress will be saved)                       |");
        System.out.println("| r | To load the game from your save file.                                        |");
        System.out.println("| x | To save and exit the game.                                                   |");
        System.out.println("| d | To draw a card until you obtain specific condition same as the center card.  |");
        System.out.println("| h | To View the Command Guide.                                                   |");
        System.out.println("| g | To view the game rule.                                                       |");
        System.out.println("+---+------------------------------------------------------------------------------+");
        promptEnterKey();
    }
    
    public static void rule(){
        System.out.print("\n");
        System.out.println("+----------------------------------------------------------------------------------+");
        System.out.println("|                                     Objective                                    |");
        System.out.println("+----------------------------------------------------------------------------------+");
        System.out.println("| Be the first player to get rid of your hand.                                     |");
        System.out.println("+----------------------------------------------------------------------------------+");
        System.out.print("\n\n");
        System.out.println("+--------------------------------------------------------------------------------------+");
        System.out.println("|                                         Rule                                         |");
        System.out.println("+--------------------------------------------------------------------------------------+");
        System.out.println("| The goal is to be the first player to get rid of the cards in your hand.             |");
        System.out.println("|                                                                                      |");
        System.out.println("| The relative player deals a card with same ranking or Suit as the FIRST center       |"); 
        System.out.println("| card from their hand. If a player cannot lay down a card, they must draw a card from |");
        System.out.println("| the draw pile. If they still cannot play, they continue drawing until they draw a    |");
        System.out.println("| playerable card.                                                                     |");
        System.out.println("|                                                                                      |");
        System.out.println("| After everyone has played one card, the player who played the highest card of the    |"); 
        System.out.println("| initial suit wins \"the trick\".                                                       |");
        System.out.println("| The winner of a trick leads the next card for the next round.                        |");
        System.out.println("|                                                                                      |");
        System.out.println("| Continue in this manner until one player has emptied their hand. The first player to |");
        System.out.println("| do so is the winner.                                                                 |");
        System.out.println("|                                                                                      |");
        System.out.println("| Final score will be calculated after one player has emptied their hand, the player   |");
        System.out.println("| with the lowest score is the winner.                                                 |");
        System.out.println("+--------------------------------------------------------------------------------------+");
    }

}
