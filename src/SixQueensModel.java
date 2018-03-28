// *****************************
//
// File:    SixQueensModel.java
// Package: ---
// Unit:    Class SixQueensModel
//
// *****************************

/**
 * This class provides the application logic for the Six Queens game.
 *
 * @author  Ian Naple
 * @version 3/27/2018
 */
public class SixQueensModel implements SixQueensViewListener {
    private String name1;
    private String name2;
    private SixQueensModelListener view1;
    private SixQueensModelListener view2;
    private SixQueensModelListener turn;
    private boolean isFinished;
    private BoardState board;

    /**
     * This constructor initializes a new Six Queens game
     */
    public SixQueensModel() {
        name1 = null;
        name2 = null;
        board = new BoardState();
        isFinished = false;
    }

    /**
     * join reports when a player joins the game
     *
     * @param view the view object reporting the player joining
     * @param name the name of the player
     */
    @Override
    public synchronized void join(SixQueensModelListener view, String name) {
        if (name1 == null) {
            name1 = name;
            view1 = view;
            view1.waitingForPartner();
        } else {
            name2 = name;
            view2 = view;
            startNewGame();
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
    public synchronized void squareChosen(SixQueensModelListener view, int row, int col) {
        if (view != turn || !board.isEmpty(row, col))
            return;
        else if (view == view1)
            setQueen(view1, row, col);
        else
            setQueen(view2, row, col);
    }

    /**
     * newGame reports when a new game is started
     *
     * @param view the view reporting the new game
     */
    @Override
    public synchronized void newGame(SixQueensModelListener view) {
        if (name2 != null)
            startNewGame();
    }

    /**
     * quit reports when a player quits the game
     *
     * @param view the view reporting the player leaving the game
     */
    @Override
    public synchronized void quit(SixQueensModelListener view) {
        if (view1 != null)
            view1.quit();
        if (view2 != null)
            view2.quit();
        turn = null;
        isFinished = true;
    }

    /**
     * isFinished returns whether or not the game is over
     *
     * @return true when the game is over, false otherwise
     */
    public synchronized boolean isFinished() {
        return isFinished;
    }

    /**
     * startNewGame starts a game between two players
     */
    private void startNewGame() {
        board.clear();
        view1.newGame();
        view2.newGame();

        turn = view1;
        view1.yourTurn();
        view2.theirTurn(name1);
    }

    /**
     * setQueen places a queen on the board then checks if the player won the game
     *
     * @param current the current player
     * @param row     the row the queen was placed in
     * @param col     the column the queen was placed in
     */
    private void setQueen(SixQueensModelListener current, int row, int col) {
        board.setQueen(row, col);
        view1.setQueen(row, col);
        view2.setQueen(row, col);

        // current player won
        if (board.checkWin()) {
            turn = null;
            if (current == view1) {
                view1.youWin();
                view2.theyWin(name1);
            } else {
                view1.theyWin(name2);
                view2.youWin();
            }
        }

        // nobody won yet
        else {
            if (current == view1) {
                turn = view2;
                view1.theirTurn(name2);
                view2.yourTurn();
            } else {
                turn = view1;
                view1.yourTurn();
                view2.theirTurn(name1);
            }
        }
    }
}
