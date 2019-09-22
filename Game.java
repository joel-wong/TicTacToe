
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/** Simulates 2 player Tic-Tac-Toe using command line input and text-based output
 * The Game functions to connect two players to each other and simply pass messages between them.
 *
 * @author Joel Wong, Student ID 30018132
 * @version 1.0
 * @since March 15th, 2019
 */


public class Game implements Runnable {

	/** Port number that the player 1 will connect to */
	private int player1PortNumber;
	/** Socket that connects with player 1 */
	private Socket player1Socket;
	/** ServerSocket that player 1 will connect to */
	private ServerSocket player1ServerSocket;
	/** Used for sending output to player 1 */
	private PrintWriter outputToPlayer1;
	/** Used to receive input from player 1 */
	private BufferedReader inputFromPlayer1;

	/** Port number that the player 2 will connect to */
	private int player2PortNumber;
	/** Socket that connects with player 2 */
	private Socket player2Socket;
	/** ServerSocket that player 2 will connect to */
	private ServerSocket player2ServerSocket;
	/** Used for sending output to player 2 */
	private PrintWriter outputToPlayer2;
	/** Used to receive input from player 2 */
	private BufferedReader inputFromPlayer2;

	/** Creates a new game with the input port numbers. The Game will be used to communicate between players.
	 *
	 * @param player1PortNumber Port number that the player 1 will connect to
	 * @param player2PortNumber Port number that the player 2 will connect to
	 */
    public Game(int player1PortNumber, int player2PortNumber) {
		this.player1PortNumber = player1PortNumber;
		this.player2PortNumber = player2PortNumber;
	}

	/** Accepts connections on the input ports that were passed to the constructor and sets up IO streams
	 *
	 * @throws IOException If there was an error connecting to one or more players
	 */
	public void connectToPlayers() throws IOException {
		// first connect to player 1
		player1ServerSocket = new ServerSocket(player1PortNumber);
		player1Socket = player1ServerSocket.accept();
		outputToPlayer1 = new PrintWriter((player1Socket.getOutputStream()), true);
		inputFromPlayer1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
		outputToPlayer1.println("Connected");
		System.out.println("The new player is conected to a game on port " + player1PortNumber);


		player2ServerSocket = new ServerSocket(player2PortNumber);
		player2Socket = player2ServerSocket.accept();
		outputToPlayer2 = new PrintWriter((player2Socket.getOutputStream()), true);
		inputFromPlayer2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
		outputToPlayer2.println("Connected");
		System.out.println("The new player is conected to a game on port " + player2PortNumber);

		// let p1 know that p2 is connected
		outputToPlayer1.println("P2connected");

	}

	/** Passes messages between player 1 and player 2. The messages must come in strict alternation.
	 *
	 * @throws IOException If there are any communication errors with the Client
	 */
	public void runTheGame() throws IOException{
		while(true) {
			// player 2 plays first
			String player2Input = inputFromPlayer2.readLine();
			// if the game is done, exit
			if(player2Input.equals("QUIT")) {
				break;
			}
			// otherwise simply pass player 2s input to player 1
			outputToPlayer1.println(player2Input);

			// player 1 players second
			String player1Input = inputFromPlayer1.readLine();
			// if the game is done, exit
			if (player1Input.equals("QUIT")) {
				break;
			}
			// otherwise simply pass player 1s input to player 2
			outputToPlayer2.println(player1Input);
		}
	}

	/** Closes the IO streams to player 1 and player 2
	 *
	 * @throws IOException If the IOStreams were unable to be closed
	 */
	public void closeIOStreams() throws IOException{
		// Close IO streams
		outputToPlayer1.close();
		outputToPlayer2.close();
		inputFromPlayer1.close();
		inputFromPlayer2.close();
	}

	/** Connects to players, runs the game, and then performs cleanup.
	 */
	@Override
	public void run() {
    	try {
			connectToPlayers();
			runTheGame();
			closeIOStreams();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	

}
