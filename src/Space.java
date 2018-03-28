// ********************
//
// File:    Space.java
// Package: ---
// Unit:    Class Space
//
// ********************

/**
 * This class represents a space on the Six Queens game board.
 * The space can either be empty, contain a queen, or be an
 * invalid space to play a queen.
 *
 * @author  Ian Naple
 * @version 3/27/2018
 */
public class Space {
    private enum Piece {
        /**
         * The space is a valid spot to place a queen
         */
        EMPTY,

        /**
         * The space contains a queen
         */
        QUEEN,

        /**
         * The space is empty but cannot have a queen placed on it
         */
        INVALID
    }

    private Piece currentStatus;

    /**
     * This constructor sets the Space instance to empty
     */
    public Space() {
        currentStatus = Piece.EMPTY;
    }

    /**
     * isEmpty checks if the space is empty
     *
     * @return true if the space is empty, false if it is not
     */
    public boolean isEmpty() {
        return currentStatus == Piece.EMPTY;
    }

    /**
     * placeQueen places a Queen on the space
     */
    public void placeQueen() {
        currentStatus = Piece.QUEEN;
    }

    /**
     * clearSpace clears the queen from a space
     */
    public void clearSpace() {
        currentStatus = Piece.EMPTY;
    }

    /**
     * invalidateSpace sets a space to invalid when a queen cannot be placed on it
     */
    public void invalidateSpace() {
        currentStatus = Piece.INVALID;
    }
}
