// ****************************************
//
// File:    SixQueensViewListener.java
// Package: ---
// Unit:    Interface SixQueensViewListener
//
// ****************************************

/**
 * This interface provides the interface for an object that
 * receives reports from the View in the Six Queens game.
 *
 * @author  Ian Naple
 * @version 3/27/2018
 */
public interface SixQueensViewListener {

    /**
     * join reports that a player joined the game
     *
     * @param view the view object reporting the player joining
     * @param name the name of the player
     */
    public void join(SixQueensModelListener view, String name);

    /**
     * squareChosen reports when a queen is placed on a square
     *
     * @param view the view reporting the placement
     * @param row  the row the queen was placed in
     * @param col  the column the queen was placed in
     */
    public void squareChosen(SixQueensModelListener view, int row, int col);

    /**
     * newGame reports when a new game is started
     *
     * @param view the view reporting the new game
     */
    public void newGame(SixQueensModelListener view);

    /**
     * quit reports when a player quits the game
     *
     * @param view the view reporting the player leaving the game
     */
    public void quit(SixQueensModelListener view);
}
