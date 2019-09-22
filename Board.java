import java.io.*;

/** A class used for keeping track of the state of an Tic-Tac-Toe game
 *
 * @author Joel Wong, Student ID 30018132
 * @version 1.0
 * @since January 23rd, 2019
 */

public class Board implements Constants {
    /** character array used to record where players have placed their marks (X's and O's)*/
	private char theBoard[][];
	/** records the number of marks that have been made */
	private int markCount;

    /** Constructor for a new Tic-Tac-Toe board, which creates a cleared board and sets the number of moves made to 0 */
	public Board() {
		markCount = 0;
		theBoard = new char[3][];
		for (int i = 0; i < 3; i++) {
			theBoard[i] = new char[3];
			for (int j = 0; j < 3; j++)
				theBoard[i][j] = SPACE_CHAR;
		}
	}

    /** Returns the character (X, O, or space) at the input row and column.
     *
     * @param row The row of the mark to be returned
     * @param col The column of the mark to be returned
     * @return The mark (or lack thereof) at the input row and column
     */
	public char getMark(int row, int col) {
		return theBoard[row][col];
	}

	public int getMarkCount() {
		return markCount;
	}

	/** Returns true if the entire board is covered with a combination of X's and O's, returns false otherwise.
     *
     * @return Whether the entire board is covered with a combination of X's and O's
     */
	public boolean isFull() {
		return markCount == 9;
	}

    /** Checks whether X has won the game, according to the rules of Tic-Tac-Toe.
     *
     * @return true if there are three X's in at least one row, one column, or one diagonal. Otherwise returns false.
     */
	public boolean xWins() {
		if (checkWinner(LETTER_X) == 1)
			return true;
		else
			return false;
	}

    /** Checks whether O has won the game, according to the rules of Tic-Tac-Toe.
     *
     * @return true if there are three O's in at least one row, one column, or one diagonal. Otherwise returns false.
     */
	public boolean oWins() {
		if (checkWinner(LETTER_O) == 1)
			return true;
		else
			return false;
	}

    /** Prints the current state of the Tic-Tac-Toe game on screen.
     * This shows the marks that have been placed by players in previous turns.
     */
	public void display() {
		displayColumnHeaders();
		addHyphens();
		for (int row = 0; row < 3; row++) {
			addSpaces();
			System.out.print("    row " + row + ' ');
			for (int col = 0; col < 3; col++)
				System.out.print("|  " + getMark(row, col) + "  ");
			System.out.println("|");
			addSpaces();
			addHyphens();
		}
	}

    /** After a player inputs a mark, this is used to input it onto the board.
	 * Also saves the last mark that was added to the board
     *
     * @param row The row of the mark that was input
     * @param col The column of the mark that was input
     * @param mark The character corresponding the the mark that was input
     */
	public void addMark(int row, int col, char mark) {
		theBoard[row][col] = mark;
		markCount++;
	}

    /** Removes all X's and O's from the board and resets the markCount so that another game can be played */
	public void clear() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				theBoard[i][j] = SPACE_CHAR;
		markCount = 0;
	}

    /** For a given input character, checks whether there are at least 3 in any row, column, or diagonal.
     *
     * @param mark The character that is checked for a winning condition.
     * @return true if there are three in a row of the input character, return false otherwise.
     */
	int checkWinner(char mark) {
		int row, col;
		int result = 0;

		for (row = 0; result == 0 && row < 3; row++) {
			int row_result = 1;
			for (col = 0; row_result == 1 && col < 3; col++)
				if (theBoard[row][col] != mark)
					row_result = 0;
			if (row_result != 0)
				result = 1;
		}

		for (col = 0; result == 0 && col < 3; col++) {
			int col_result = 1;
			for (row = 0; col_result != 0 && row < 3; row++)
				if (theBoard[row][col] != mark)
					col_result = 0;
			if (col_result != 0)
				result = 1;
		}

		if (result == 0) {
			int diag1Result = 1;
			for (row = 0; diag1Result != 0 && row < 3; row++)
				if (theBoard[row][row] != mark)
					diag1Result = 0;
			if (diag1Result != 0)
				result = 1;
		}
		if (result == 0) {
			int diag2Result = 1;
			for (row = 0; diag2Result != 0 && row < 3; row++)
				if (theBoard[row][3 - 1 - row] != mark)
					diag2Result = 0;
			if (diag2Result != 0)
				result = 1;
		}
		return result;
	}

    /** Prints labels for the columns (prior to displaying the data in the rows.) */
	void displayColumnHeaders() {
		System.out.print("          ");
		for (int j = 0; j < 3; j++)
			System.out.print("|col " + j);
		System.out.println();
	}

    /** Prints dividers between the rows. */
	void addHyphens() {
		System.out.print("          ");
		for (int j = 0; j < 3; j++)
			System.out.print("+-----");
		System.out.println("+");
	}

    /** Prints spaces (for before and after the X's and O's) so that the marks are easier to see. */
	void addSpaces() {
		System.out.print("          ");
		for (int j = 0; j < 3; j++)
			System.out.print("|     ");
		System.out.println("|");
	}
}
