// ****************************
//
// File:    SixQueensView.java
// Package: ---
// Unit:    Class SixQueensView
//
// ****************************

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class SixQueensView provides the user interface for the Six Queens Game.
 *
 * @author  Alan Kaminsky
 * @version 01-Mar-2018
 */
public class SixQueensView implements SixQueensModelListener
{

// Hidden data members.

    private static final int GAP = 10;

    private JFrame frame;
    private SixQueensJPanel board;
    private JTextField messageField;
    private JButton newGameButton;
    private SixQueensViewListener viewListener;

// Hidden constructors.

    /**
     * Construct a new Six Queens view object.
     *
     * @param  name  Player's name.
     */
    private SixQueensView
    (String name)
    {
        frame = new JFrame ("Six Queens -- " + name);
        JPanel p1 = new JPanel();
        p1.setLayout (new BoxLayout (p1, BoxLayout.Y_AXIS));
        p1.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));
        frame.add (p1);

        board = new SixQueensJPanel();
        board.setFocusable (false);
        board.setAlignmentX (0.5f);
        p1.add (board);
        p1.add (Box.createVerticalStrut (GAP));

        messageField = new JTextField (5);
        messageField.setEditable (false);
        messageField.setFocusable (false);
        messageField.setAlignmentX (0.5f);
        p1.add (messageField);
        p1.add (Box.createVerticalStrut (GAP));

        newGameButton = new JButton ("New Game");
        newGameButton.setFocusable (false);
        newGameButton.setAlignmentX (0.5f);
        p1.add (newGameButton);

        // adds a listener for when a square is clicked by a player
        board.setListener(new SixQueensJPanelListener() {
            @Override
            public void squareClicked(int r, int c) {
                if(viewListener != null)
                    viewListener.squareChosen(SixQueensView.this, r, c);
            }
        });

        // adds a listener for when a player clicks the new game button
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(viewListener != null)
                    viewListener.newGame(SixQueensView.this);
            }
        });

        // adds a listener for when a player closes the window
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(viewListener != null)
                    viewListener.quit(SixQueensView.this);
                System.exit(0);
            }
        });

        frame.pack();
        frame.setVisible (true);
    }

    /**
     * create constructs a new Six Queens view oject
     * @param name the player's name
     * @return an instance of the view object
     */
    public static SixQueensView create(String name) {
        UIRef uiRef = new UIRef();
        onSwingThreadDo(new Runnable() {
            public void run() {
                uiRef.ui = new SixQueensView(name);
            }
        });
        return uiRef.ui;
    }

    /**
     * This helper class provides a view Six Queens view object
     */
    private static class UIRef {
        public SixQueensView ui;
    }

    /**
     * setViewListener sets the view listener
     * @param viewListener the view listener
     */
    public void setViewListener(SixQueensViewListener viewListener) {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                SixQueensView.this.viewListener = viewListener;
            }
        });
    }

    /**
     * newGame reports that a new game was started
     */
    @Override
    public void newGame() {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                board.clear();
            }
        });
    }

    /**
     * setQueen reports that a queen was placed on the board
     * @param row the row the queen was placed in
     * @param col the column the queen was placed in
     */
    @Override
    public void setQueen(int row, int col) {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                board.setQueen(row, col, true);
                hideButtons(row, col);
            }
        });
    }

    /**
     * waitingForPartner reports the player is waiting for another player to join
     */
    @Override
    public void waitingForPartner() {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                messageField.setText("Waiting for partner");
                newGameButton.setEnabled(false);
            }
        });
    }

    /**
     * yourTurn reports that it is this player's turn
     */
    @Override
    public void yourTurn() {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                messageField.setText("Your turn");
                newGameButton.setEnabled(true);
            }
        });
    }

    /**
     * theirTurn reports that it is the other player's turn
     * @param name the other player's name
     */
    @Override
    public void theirTurn(String name) {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                messageField.setText(name + "'s turn");
                newGameButton.setEnabled(true);
            }
        });
    }

    /**
     * youWin reports that this player won the game
     */
    @Override
    public void youWin() {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                messageField.setText("You win!");
                newGameButton.setEnabled(true);
            }
        });
    }

    /**
     * theyWin reports that the other player won the game
     * @param name the other player's name
     */
    @Override
    public void theyWin(String name) {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                messageField.setText(name + " wins!");
                newGameButton.setEnabled(true);
            }
        });
    }

    /**
     * quit reports that a player left the game
     */
    @Override
    public void quit() {
        System.exit(0);
    }

    /**
     * onSwingThreadDo executes the runnable object on the Swing thread
     * @param task the task to be executed
     */
    private static void onSwingThreadDo(Runnable task) {
        try {
            SwingUtilities.invokeAndWait (task);
        } catch (Throwable exc) {
            exc.printStackTrace (System.err);
            System.exit (1);
        }
    }

    /**
     * hideButtons hides the buttons a queen cannot be placed on.
     * This function is called whenever setQueen is called.
     * @param row the row the queen was placed on
     * @param col the column the queen was placed on
     */
    private void hideButtons(int row, int col) {
        // hides buttons below the queen
        for(int i = row + 1; i < SixQueensJPanel.ROWS; i++)
            board.setVisible(i, col, false);

        // hides buttons above the queen
        for(int i = row - 1; i >= 0; i--)
            board.setVisible(i, col, false);

        // hides buttons to the right of the queen
        for(int i = col + 1; i < SixQueensJPanel.COLS; i++)
            board.setVisible(row, i, false);

        // hides buttons to the left of the queen
        for(int i = col - 1; i >= 0; i--)
            board.setVisible(row, i, false);

        // hides buttons diagonally below the queen
        int r = row + 1;
        int colRight = col + 1;
        int colLeft = col - 1;
        while(r < SixQueensJPanel.ROWS && (colRight < SixQueensJPanel.COLS || colLeft >= 0)) {
            if(colRight < SixQueensJPanel.ROWS) {
                board.setVisible(r, colRight, false);
                colRight++;
            }
            if(colLeft >= 0) {
                board.setVisible(r, colLeft, false);
                colLeft--;
            }
            r++;
        }

        // hides buttons diagonally above the queen
        r = row - 1;
        colRight = col + 1;
        colLeft = col - 1;
        while(r >= 0 && (colRight < SixQueensJPanel.COLS || colLeft >= 0)) {
            if(colRight < SixQueensJPanel.ROWS) {
                board.setVisible(r, colRight, false);
                colRight++;
            }
            if(colLeft >= 0) {
                board.setVisible(r, colLeft, false);
                colLeft--;
            }
            r--;
        }
    }
}
