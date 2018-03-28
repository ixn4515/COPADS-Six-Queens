// ************************
//
// File:    SixQueens.java
// Package: ---
// Unit:    Class SixQueens
//
// ************************

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * This class is the client main program for the Six Queens game.
 *
 * @author  Ian Naple
 * @version 3/27/2018
 */
public class SixQueens {
    public static void main(String[] args) {
        // check to make sure there are 3 command line arguments
        if (args.length != 3) {
            System.err.println("Usage: java SixQueens <host> <port> <playername>");
            System.exit(1);
        }
        String host = args[0];
        String playerName = args[2];

        // checks that the port only contains numbers
        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException n) {
            System.err.println("The port may only contain numbers");
            System.exit(1);
        }

        // connects the player to the server
        try {
            // create socket connection to server
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));

            SixQueensView view = SixQueensView.create(playerName);
            ModelProxy modelProxy = new ModelProxy(socket);
            view.setViewListener(modelProxy);
            modelProxy.setListener(view);
            modelProxy.join(view, playerName);
        } catch (IOException e) {
            error(e);
        }
    }

    /**
     * error prints out an IO error message and exits the program
     *
     * @param e the IO error
     */
    private static void error(IOException e) {
        System.err.println("SixQueens: I/O error");
        e.printStackTrace();
        System.exit(1);
    }
}
