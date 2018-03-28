// *****************************************
//
// File:    SixQueensModelListener.java
// Package: ---
// Unit:    Interface SixQueensModelListener
//
// *****************************************

/**
 * This interface specifies the interface for an object that
 * receives reports from the Model in the Six Queens game
 *
 * @author  Ian Naple
 * @version 3/27/2018
 */
public interface SixQueensModelListener {

    /**
     * newGame reports when a new game is started
     */
    public void newGame();

    /**
     * setQueen reports when a queen was placed on the board
     *
     * @param row the row the queen was placed in
     * @param col the column the queen was placed in
     */
    public void setQueen(int row, int col);

    /**
     * waitingForPartner reports when a player is waiting for another player to join a game
     */
    public void waitingForPartner();

    /**
     * yourTurn reports when it is the player's turn
     */
    public void yourTurn();

    /**
     * theirTurn reports when it is the other player's turn
     *
     * @param name the other player's name
     */
    public void theirTurn(String name);

    /**
     * youWin reports when the player wins
     */
    public void youWin();

    /**
     * theyWin reports when the other player wins
     *
     * @param name the other player's name
     */
    public void theyWin(String name);

    /**
     * quit reports when a player quits the game
     */
    public void quit();
}
