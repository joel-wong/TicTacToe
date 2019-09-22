import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/** Simulates 2 player Tic-Tac-Toe using command line input and text-based output
 * The Server functions to create Games and connect players to Games.
 * It will wait for a player to connect. Once a player connects, it will create a game,
 * then send the player a port number that they will use to connect to that game.
 *
 * This program uses a "fat client" model. Each client maintains their own referee, player, and board.
 * The server is simply used for connecting Clients to games and passing messages between Clients
 *
 * @author Joel Wong, Student ID 30018132
 * @version 1.0
 * @since March 15th, 2019
 */

public class Server {
	/** Output to the current Client connected to the Server that is not currently in a game */
	PrintWriter out;
	/** Socket to connect to the Client that is not currently in a game */
	Socket aSocket;
	/** ServerSocket used to allow players to connect to this Server */
	ServerSocket serverSocket;
	/** Used to run the Games */
	ExecutorService threadPool;
	/** The next available port number for a player to connect to a Game */
	int portNumberOfGameForNextPlayer;

	public Server() {
		// Create ServerSocket so that player can be sent a port number corresponding to a Game
		try {
			serverSocket = new ServerSocket(9000);
		} catch (IOException e) {
			System.err.println("Create the new socket error");
			System.exit(-1);
		}
		System.out.println("Server is running");

		// up to 5 games occurring simultaneously.
		threadPool = Executors.newFixedThreadPool(5);

		// the first port that new games will have is 9001. Port numbers increment for the games
		portNumberOfGameForNextPlayer = 9001;
	}

	/** Hosts a "Lobby" for players to connect to. For every two players that connect, a new Game object is created.
	 * Then, the player is sent a port number to connect to that game.
	 *
	 */
	public void hostTicTacToeServer(){
		try {
			// when players first connect, they are first sent a port number for their game.
			while(true) {
				connectToNextPlayer();
				connectPlayerToGame();
				disconnectFromPlayer();

				// next player connects to next port number
				portNumberOfGameForNextPlayer++;

				// repeat listening for players and sending port numbers until stopped
			}
		} catch (IOException e) {
			System.err.println("Received an IOException, exiting...");
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

	/** Wait for the next player to connect, then setup output stream to communicate with them.
	 *
	 * @throws IOException If there are any errors accepting connections
	 */
	public void connectToNextPlayer() throws IOException {

		// wait until a player connects
		aSocket = serverSocket.accept();

		// a player has connected
		System.out.println("A new player has connected!");

		// Need to output port number to player
		out = new PrintWriter((aSocket.getOutputStream()), true);

	}

	/** Create a Game if required, then send the player the port number of the next available game
	 *
	 * @throws IOException If there are any errors communicating with the client over sockets
	 */
	public void connectPlayerToGame() throws IOException {
		// create game with the two input numbers if this is the first player to join the game
		if(portNumberOfGameForNextPlayer % 2 == 1) {
			threadPool.execute(new Game(portNumberOfGameForNextPlayer, portNumberOfGameForNextPlayer+1));
		}

		// send port number to player
		out.println(portNumberOfGameForNextPlayer);

	}

	/** Closes output streams to the current client.
	 *
	 * @throws IOException If unable to close the output stream
	 */
	public void disconnectFromPlayer() throws IOException {
		// close output stream
		out.close();
	}

	/** Creates and runs the TicTacToe Server.
	 *
	 * @param args Ignored
	 */
	public static void main(String[] args) {
		Server myserver = new Server();
		myserver.hostTicTacToeServer();
	}
}