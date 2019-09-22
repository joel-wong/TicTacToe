import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/** A class used for playing a Tic-Tac-Toe game via sockets. This is one player, and will contact a server on
 * localhost:9000 to receive the port number of a TicTacToe Game.
 *
 * This program uses a "fat client" model. Each client maintains their own referee, player, and board.
 * The server is simply used for connecting Clients to games and passing messages between Clients
 *
 * @author Joel Wong, Student ID 30018132
 * @version 1.0
 * @since March 15th, 2019
 */

public class Client {
    /** Output to the server or game */
    private PrintWriter socketOut;
    /** Socket, used for communicating with the server or game */
    private Socket socket;
    /** Used for input from the server/game */
    private BufferedReader socketIn;
    /** Name of the server that this is running on */
    private String serverName;
    /** Port number that this is connected to */
    private int gamePort;
    /** Controls/runs the Game. All input/output is passed to/comes from the Referee */
    private Referee theRef;

    /** Constructor for Client. Opens a connection to the Server and listens for the port number of an open game.
     *
     * @param serverName The server name for the lobby and game
     * @param portNumber The port number of the server (which will provide another port number for the game)
     */
    public Client(String serverName, int portNumber) {
        this.serverName = serverName;
        try {
            // when first connecting, wait until server fetches a port number
            socket = new Socket(serverName, portNumber);
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // don't need output yet
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
        }
    }

    /** Connects to the Server on localhost:9000 and receives the port number for an open TicTacToe game
     *
     * @throws IOException If we were unable to communicate with the server
     */
    public void connectToTicTacToeLobby() throws IOException {

        String response = "";

        try {
            response = socketIn.readLine();
            gamePort = Integer.parseInt(response);
        }
        catch(NumberFormatException e) {
            System.err.println(response + " is not a valid integer. Exiting...");
            System.exit(-1);
        }

        System.out.println("Game port: " + gamePort);
    }

    /** Close all IO stream used to connect to the Server
     *
     * @throws IOException
     */
    public void disconnectFromTicTacToeLobby() throws IOException {
        // close the original connection
        socketIn.close();
    }

    /** Sets up the IO streams in order to communicate with the Game/other player
     *
     * @throws IOException If there were issues connecting to the Game
     */
    public void connectToGame() throws IOException {
        // create a new socket that will be used to connect to the other player/board
        System.out.println("Attempting to connect to port " + gamePort);
        socket = new Socket(serverName, gamePort);
        socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socketOut = new PrintWriter((socket.getOutputStream()), true);
        if (socketIn.readLine().equals("Connected")) {
            System.out.println("Connection established!");
        }
        else {
            throw new IOException("Did not receive expected connection message");
        }
    }

    /** Initiatilizes all variable and objects required to play the game.
     * First sets up the board and gets the players' names,
     * then connects the players to the board/game and appoints a referee
     * who starts the game
     *
     * @throws IOException If the connection to the socket fails
     */
    public void setupReferee() throws IOException{
        boolean playingX;
        if((gamePort % 2) == 1 ) {
            playingX = true;
        }
        else {
            playingX = false;
        }

        theRef = new Referee(playingX);
        theRef.setupGame();

        if (theRef.getPlayingX()) {
            System.out.println("Waiting for another player to join...");
            System.out.println("You will be playing X\n");
            String player2connected = socketIn.readLine();
            if(player2connected.equals("P2connected")) {
                System.out.println("The other player has connected!");
            }
        }
        // The O player, which is the second to connect, will go first
        else {
            System.out.println("You are playing Os and move first.");
        }
    }

    /** Receives a row and column as a String, separated by a space from the socket that it is connected to for the game
     *
     * @return A row and column as a String, separated by a space. The row and column are assumed to be a valid
     * TicTacToe move in the current game
     * @throws IOException If reading from the socket fails
     */
    public String receiveMove() throws IOException {
        System.out.println("The other player is making a move...");
        return socketIn.readLine();
    }

    /** Plays the TicTacToe game after everything has been set up. We already know the other player
     *  and have the board and referee set up.
     * Performs a series of moves in strict alternation with the other player,
     * communicating with the other player over sockets.
     *
     * @throws IOException If there are connection issues with the sockets
     */
    public void playGame() throws IOException {

        // if playing O, we play first
        if(!(theRef.getPlayingX())) {
            String refereeResponse = theRef.makeMyMove();
            socketOut.println(refereeResponse);
        }
        while (true) {
            // receive opponent move
            String opponentMove = receiveMove();
            theRef.makeOpponentMove(opponentMove);
            if(theRef.checkGameEnd()) {
                break;
            }
            // get the local player's move
            String refereeResponse = theRef.makeMyMove();
            socketOut.println(refereeResponse);
            if(theRef.checkGameEnd()) {
                break;
            }
        }

        if(theRef.opponentMadeLastMove()) {
            // the server will be waiting for the losing  player
            // to send the QUIT message
            socketOut.println("QUIT");
        }
    }

    /** Closes all IO streams created by the client
     *
     * @throws IOException If there was an error when closing the IO stream.
     */
    public void disconnectFromGame() throws IOException{
        socketIn.close();
        socketOut.close();
    }

    /** Connects to the TicTacToe server, receives the port number of a game, connects to that game, and allows the
     * user to play TicTacToe with another user using sockets
     */
    public void playTicTacToe(){
        try {
            connectToTicTacToeLobby();
            disconnectFromTicTacToeLobby();
            connectToGame();
            setupReferee();
            playGame();
            disconnectFromGame();
        }
        catch(IOException e) {
            System.out.println("Error occurred while playing game.");
            System.err.println(e.getStackTrace());
            System.err.println(e.getMessage());
            System.out.println("Exiting...");
            System.exit(-1);
        }
    }

    /** Creates and runs the client, which will play TicTacToe
     *
     * @param args Ignored
     */
    public static void main(String[] args) {
        Client aClient = new Client("localhost", 9000);
        aClient.playTicTacToe();
    }
}