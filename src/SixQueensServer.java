// ******************************
//
// File:    SixQueensServer.java
// Package: ---
// Unit:    Class SixQueensServer
//
// ******************************

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class creates the server that clients connect to in order to play a Six Queens game
 *
 * @author  Ian Naple
 * @version 3/27/2018
 */
public class SixQueensServer {
    public static void main(String[] args) {
        // check to make sure there are 2 command line arguments
        if (args.length != 2) {
            System.err.println("Usage: java SixQueensServer <host> <port>");
            System.exit(1);
        }
        String host = args[0];

        // checks that the port only contains numbers
        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("The port may only contain numbers");
            System.exit(1);
        }

        // creates the socket and listens for connections from users
        try {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(host, port));

            SixQueensModel model = null;
            while (true) {
                Socket socket = server.accept();
                ViewProxy viewProxy = new ViewProxy(socket);
                if (model == null || model.isFinished()) {
                    model = new SixQueensModel();
                    viewProxy.setListener(model);
                } else {
                    viewProxy.setListener(model);
                    model = null;
                }
            }
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
        System.err.println("SixQueensServer: I/O error");
        e.printStackTrace();
        System.exit(1);
    }
}
