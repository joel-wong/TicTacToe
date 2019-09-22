import java.io.*;
import java.util.Scanner;

/** A class used for running a 2 player Tic-Tac-Toe game with a local player, run in the current process,
 * and a "remote" player, which is on a different process
 *
 * @author Joel Wong, Student ID 30018132
 * @version 1.0
 * @since March 15th, 2019
 */

public class Referee implements Constants {
    /** Stores the board that the referee is controlling and connects the referee with the players*/
    private Board board;
    /** Stores the local player in the Tic-Tac-Toe game */
    private Player myPlayer;
    /** Tells the referee whether the local Client is playing X or O */
    private boolean playingX;
    /** Used to take user input from the command line */
    private BufferedReader stdInBufferedReader;

    /** Consructor for referee. Saves playingX to a instance variable but otherwise does not initialize anything.
     *
     * @param playingX Whether the local player is playing X's (as opposed to Os)
     */
    public Referee(boolean playingX) {
        board = null;
        myPlayer = null;
        this.playingX = playingX;
        stdInBufferedReader = null;
    }

    /** Returns the game board that the referee is overseeing.
     *
     * @return The game board that the referee is overseeing.
     */
    public Board getBoard(){
        return board;
    }

    /** Sets the board referee that the referee is overseeing
     * @param board A board without any players currently using it*/
    public void setBoard(Board board) {
        this.board = board;
    }

    /** Returns the value of playingX
     *
     * @return Whether the local player is playing as X (as opposed to O)
     */
    public boolean getPlayingX(){
        return playingX;
    }

    /** Sets up the board, standard input stream, and the player for the game.
     * Maintains one local player (myPlayer) and uses the functions of board to simulate the other player.
     * @throws IOException If there is an input whether reading from standard input
     */
    public void setupGame() throws IOException {
        // create the board
        board = new Board();

        System.out.println("Referee started the game...");

        // Get name of the player
        System.out.print("\nPlease enter your name: \n");
        stdInBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String name = stdInBufferedReader.readLine();
        while (name == null) {
            System.out.print("Please try again: ");
            name = stdInBufferedReader.readLine();
        }

        // Set the appropriate mark for the player
        if(playingX) {
            myPlayer = new Player(name, LETTER_X);
        }
        else {
            myPlayer = new Player(name, LETTER_O);
        }
        // set the board for the player
        myPlayer.setBoard(board);

        // show the board
        board.display();
    }

    /** Determines if the game is finished. There are three possible cases:
     * 1. The local player has won
     * 2. The remote player has won
     * 3. The board is full and no one has won
     *
     * @return true if someone has won the game or the board is full
     */
    public boolean checkGameEnd(){
        // First check if someone has won
        if ((board.xWins() && playingX) || (board.oWins() && !playingX)) {
            System.out.println("THE GAME IS OVER: YOU WIN!!! :)");
            return true;
        }
        else if ((board.xWins() && !playingX) || (board.oWins() && playingX)) {
            System.out.println("THE GAME IS OVER: You lose.");
            return true;
        }
        // Then check if the board is full
        else if (board.isFull()) {
            System.out.println("THE GAME IS OVER: It is a tie!");
            return true;
        }
        return false;
    }

    /** Makes a call to myPlayer to allow the local user to input the desired row and column for their next move
     *
     * @return The row and column of the move as a String, separated by a space
     */
    public String makeMyMove(){
        // Make a move and return the move location as a string
        String moveMade = myPlayer.makeMove();
        // print the current state of the board
        board.display();
        // send coordinates of move to Game
        return moveMade;
    }

    /** Takes the opponent move and adds it to the board. Assumes the opponents move is valid (for this assignment,
     * this is already checked by the other process/remote player)
     *
     * @param opponentMove The row and column of the opponent's move, separated by a space
     */
    public void makeOpponentMove(String opponentMove){
        Scanner sc = new Scanner(opponentMove);
        int row = sc.nextInt();
        int col = sc.nextInt();
        // can assume opponent input is valid.
        if (playingX) {
            // opponent is playing O's
            board.addMark(row, col, LETTER_O);
        }
        else {
            // opponent is playing X's
            board.addMark(row, col, LETTER_X);
        }
        System.out.println("\nThe other player made a move!");
        board.display();
        sc.close();
    }

    /** Returns true if the opponent made the last move. THis assumes O plays first
     *
     * @return True if the opponent made the last move
     */
    public boolean opponentMadeLastMove() {
        if((((board.getMarkCount() % 2) == 0) && !playingX) || (((board.getMarkCount() % 2) == 1) && playingX)) {
            return true;
        }
        return false;
    }

}