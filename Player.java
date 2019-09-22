import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.*;

/** A class used for input into the Tic-Tac-Toe game.
 * Represents a player in a Tic-Tac-Toe game/
 *
 * @author Joel Wong, Student ID 30018132
 * @version 1.0
 * @since January 23rd, 2019
 */

public class Player implements Constants {
    /** Store's the player's name, input at the start of the game. */
    private String name;
    /** Stores the player's board and allows them to play against others*/
    private Board board;
    /** Store's the Tic-Tac-Toe opponent of this player */
    private Player opponent;
    /** Stores the player's marking character (X or O) */
    private char mark;
    /** Used for received player input when determining where to place their next mark */
    private Scanner sc;

    /** Creates a new player to play TicTacToe with a given name and mark.
     *
     * @param name The name of the player
     * @param mark The mark (X's or O's) of the player
     */

    Player(String name, char mark) {
        this.name = name;
        this.mark = mark;
        this.sc = new Scanner(System.in);
    }

    /** Returns the name of the player.
     *
     * @return The name of the player.
     */
    String getName(){
        return name;
    }

    /** Returns the player's marking character (X or O).
     *
     * @return The player's marking character (X or O).
     */
    char getMark(){
        return mark;
    }

    // No need for setname or setmark, it's not going to change.

    /** Sets the player's board, which allows them to play against others
     * @param board The board that the players are playing on. This board can only have two players using it at a time. */
    void setBoard(Board board){
        this.board = board;
    }

    /** Sets the player's opponent, which allows them to pass the turn after they make a mark
     * @param opponent The opponent of this player. This player will pass the turn to the opponent after they have made a move.*/
    void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    /** Provides an interface to receives the desired location of the X or O from the command line,
     * then adds that mark to the board. Will repeatedly ask for input if user inputs incorrect format.
     *
     * @return The row and column of the move as a String, separated by a space.
     */
    public String makeMove() {
        // row of input
        int row;
        // column of input
        int col;

        // Attempt to get user input. If user input is invalid, tell the user and try to get input again.
        while (true) {
            // Get row number, but ensure it's an integer between 0 and 2
            while (true) {
                System.out.println("\n" + name + ", what row should your next " + mark + " be placed in? ");
                try {
                    // Input row number
                    row = sc.nextInt();
                    // check if it is in the desired range
                    if (row < 0 || row > 2) {
                        System.out.println("That is not a valid input, row must be between 0 and 2");
                        continue;
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    // Input was not an integer
                    System.out.println("That is not an integer. Please enter an integer between 0 and 2 (inclusive).");
                    // move to next open space in system input (similar to clearing the buffer)
                    sc.next();
                    // restart loop
                    continue;
                }
            }

            // Get column number, but ensure it's an integer between 0 and 2
            while (true) {
                System.out.println("\n" + name + ", what column should your next " + mark + " be placed in? ");
                try {
                    // Input column number
                    col = sc.nextInt();
                    // check if it is in the desired range
                    if (col < 0 || col > 2) {
                        System.out.println("That is not a valid input, column must be between 0 and 2");
                        continue;
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    // Input was not an integer
                    System.out.println("That is not an integer. Please enter an integer between 0 and 2 (inclusive).");
                    // move to next open space in system input (similar to clearing the buffer)
                    sc.next();
                    // restart loop
                    continue;
                }
            }

            // Check if row and column is already filled. If so, reset
            if (board.getMark(row, col) != SPACE_CHAR) {
                System.out.println("That space is already filled. Please enter the row and column again.");
                // move to next open space in system input (similar to clearing the buffer)
                // restart loop
                continue;
            }

            // Since the input is valid, add the mark to the board.
            board.addMark(row, col, mark);
            return new String(row + " " + col);
        }
    }

    public void play(){}

}