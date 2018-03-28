// ****************************************
//
// File:    SixQueensViewListener.java
// Package: ---
// Unit:    Interface SixQueensViewListener
//
// ****************************************

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class provides the view proxy for the Six Queens game.  It
 * implements the server side of the client-server network communication.
 *
 * @author  Ian Naple
 * @version 3/27/2018
 */
public class ViewProxy implements SixQueensModelListener {
    private Socket socket;
    private SixQueensViewListener viewListener;
    private DataInputStream in;
    private DataOutputStream out;

    /**
     * This constructor creates a new view proxy
     *
     * @param s the socket
     */
    public ViewProxy(Socket s) {
        try {
            this.socket = s;
            socket.setTcpNoDelay(true);
            in = new DataInputStream(this.socket.getInputStream());
            out = new DataOutputStream(this.socket.getOutputStream());
        } catch (SocketException e) {
            System.out.println("");
            e.printStackTrace();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * setListener sets the listener for the view proxy
     *
     * @param viewListener the listener
     */
    public void setListener(SixQueensViewListener viewListener) {
        this.viewListener = viewListener;
        new ReaderThread().start();
    }

    /**
     * newGame reports that a new game was started
     */
    @Override
    public void newGame() {
        try {
            out.writeByte('N');
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * setQueen reports that a queen was placed
     *
     * @param row the row the queen was placed in
     * @param col the column the queen was placed in
     */
    @Override
    public void setQueen(int row, int col) {
        try {
            out.writeByte('Q');
            out.writeByte(row);
            out.writeByte(col);
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * waitingForPartner reports that the player is waiting for a partner
     */
    @Override
    public void waitingForPartner() {
        try {
            out.writeByte('P');
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * yourTurn reports that it is this player's turn
     */
    @Override
    public void yourTurn() {
        try {
            out.writeByte('Y');
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * theirTurn reports that is is the other player's turn
     *
     * @param name the name of the other player
     */
    @Override
    public void theirTurn(String name) {
        try {
            out.writeByte('T');
            out.writeUTF(name);
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * youWin reports that this player won the game
     */
    @Override
    public void youWin() {
        try {
            out.writeByte('W');
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * theyWin reports that the other player won the game
     *
     * @param name the other player's name
     */
    @Override
    public void theyWin(String name) {
        try {
            out.writeByte('L');
            out.writeUTF(name);
            out.flush();
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * quit reports that a player quit
     */
    @Override
    public void quit() {
        try {
            out.writeByte('B');
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
        /**
         * run begins the reader thread
         */
        public void run() {
            int opCode;
            int row, col;
            String name;

            // reads in a character and runs the corresponding function
            try {
                while (true) {
                    opCode = in.readByte();
                    switch (opCode) {
                        // join case
                        case 'J':
                            name = in.readUTF();
                            viewListener.join(ViewProxy.this, name);
                            break;

                        // square chosen case
                        case 'S':
                            row = in.readByte();
                            col = in.readByte();
                            viewListener.squareChosen(ViewProxy.this, row, col);
                            break;

                        // new game case
                        case 'N':
                            viewListener.newGame(ViewProxy.this);
                            break;

                        // quit case
                        case 'Q':
                            viewListener.quit(ViewProxy.this);
                            break;

                        default:
                            System.err.println("Bad Message");
                    }
                }
            } catch (IOException e) {
                error(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    error(e);
                }
            }
        }
    }

    /**
     * error prints out an IO error message and exits the program
     *
     * @param e the IO error
     */
    private static void error(IOException e) {
        System.err.println("ViewProxy: I/O error");
        e.printStackTrace();
        System.exit(1);
    }
}
