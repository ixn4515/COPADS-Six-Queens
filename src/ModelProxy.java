// *************************
//
// File:    ModelProxy.java
// Package: ---
// Unit:    Class ModelProxy
//
// *************************

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class provides the model proxy for the Six Queens game.
 * It implements the client side of the client-server network communication.
 *
 * @author  Ian Naple
 * @version 3/27/2018
 */
public class ModelProxy implements SixQueensViewListener {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private SixQueensModelListener modelListener;

    /**
     * This constructor creates a new model proxy
     *
     * @param s
     */
    public ModelProxy(Socket s) {
        try {
            this.socket = s;
            socket.setTcpNoDelay(true);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * setListener sets the model listener
     *
     * @param modelListener the model listener
     */
    public void setListener(SixQueensModelListener modelListener) {
        this.modelListener = modelListener;
        new ReaderThread().start();
    }

    /**
     * join reports when a player joins the game
     *
     * @param view the view object reporting the player joining
     * @param name the name of the player
     */
    @Override
    public void join(SixQueensModelListener view, String name) {
        try {
            out.writeByte('J');
            out.writeUTF(name);
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * squareChosen reports when a queen was placed on the board
     *
     * @param view the view reporting the placement
     * @param row  the row the queen was placed in
     * @param col  the column the queen was placed in
     */
    @Override
    public void squareChosen(SixQueensModelListener view, int row, int col) {
        try {
            out.writeByte('S');
            out.writeByte(row);
            out.writeByte(col);
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * newGame reports when a new game is started
     *
     * @param view the view reporting the new game
     */
    @Override
    public void newGame(SixQueensModelListener view) {
        try {
            out.writeByte('N');
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * quit reports when a player quits the game
     *
     * @param view the view reporting the player leaving the game
     */
    @Override
    public void quit(SixQueensModelListener view) {
        try {
            out.writeByte('Q');
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * This class receives messages from the network, interprets them,
     * and executes the proper functions to process them
     */
    private class ReaderThread extends Thread {
        public void run() {
            int opCode;
            int row, col;
            String name;

            // reads in a character and runs the corresponding function
            try {
                while (true) {
                    opCode = in.readByte();
                    switch (opCode) {
                        // new game case
                        case 'N':
                            modelListener.newGame();
                            break;

                        // queen placed case
                        case 'Q':
                            row = in.readByte();
                            col = in.readByte();
                            modelListener.setQueen(row, col);

                            // waiting for partner case
                        case 'P':
                            modelListener.waitingForPartner();
                            break;

                        // your turn case
                        case 'Y':
                            modelListener.yourTurn();
                            break;

                        // their turn case
                        case 'T':
                            name = in.readUTF();
                            modelListener.theirTurn(name);
                            break;

                        // you win case
                        case 'W':
                            modelListener.youWin();
                            break;

                        // they win case
                        case 'L':
                            name = in.readUTF();
                            modelListener.theyWin(name);
                            break;

                        // quit case
                        case 'B':
                            modelListener.quit();
                            break;

                        default:
                            System.err.println("Bad Message");
                            break;
                    }
                }
            } catch (IOException e) {
                error(e);
            }
        }
    }

    /**
     * error prints out an IO error message and exits the program
     *
     * @param e the IO error
     */
    private static void error(IOException e) {
        System.err.println("ModelProxy: I/O error");
        e.printStackTrace();
        System.exit(1);
    }
}
