// *************************
//
// File:    BoardState.java
// Package: ---
// Unit:    Class BoardState
//
// *************************

/**
 * This class holds the board data for the Six Queens game.
 * The board is represented as a 2D array of Space objects.
 *
 * @author  Ian Naple
 * @version 3/27/2018
 */
public class BoardState {
    public static final int ROW_LENGTH = 6;
    public static final int COL_LENGTH = 6;

    private Space board[][];

    /**
     * This constructor initializes the board
     */
    public BoardState () {
        board = new Space[ROW_LENGTH][COL_LENGTH];
        for (int row = 0; row < ROW_LENGTH; row++) {
            for (int col = 0; col < COL_LENGTH; col++)
                board[row][col] = new Space();
        }
    }

    /**
     * clear removes every queen from the board
     */
    public void clear() {
        for(int row = 0; row < ROW_LENGTH; row++) {
            for(int col = 0; col < COL_LENGTH; col++)
                board[row][col].clearSpace();
        }
    }

    /**
     * setQueen places a queen on the board
     * @param row the row the queen was placed in
     * @param col the column the queen was placed in
     */
    public void setQueen(int row, int col) {
        board[row][col].placeQueen();
        invalidateSpaces(row, col);
    }

    /**
     * invalidateSpaces sets the spaces invalidated by placing a queen
     * @param row the row the queen was placed in
     * @param col the column the queen was placed in
     */
    private void invalidateSpaces(int row, int col) {
        // invalidates all spaces below the queen
        for(int i = row + 1; i < ROW_LENGTH; i++) {
            if(board[i][col].isEmpty())
                board[i][col].invalidateSpace();
        }
        // invalidates all spaces above the queen
        for(int i = row - 1; i >= 0; i--) {
            if(board[i][col].isEmpty())
                board[i][col].invalidateSpace();
        }

        // invalidates all spaces to the right of the queen
        for(int i = col + 1; i < COL_LENGTH; i++) {
            if(board[row][i].isEmpty())
                board[row][i].invalidateSpace();
        }

        // invalidates all spaces to the left of the queen
        for(int i = col - 1; i >= 0; i--) {
            if (board[row][i].isEmpty())
                board[row][i].invalidateSpace();
        }

        // invalidates all spaces diagonally below the queen
        int r = row + 1;
        int colRight = col + 1;
        int colLeft = col - 1;
        while(r < ROW_LENGTH && (colRight < COL_LENGTH || colLeft >= 0))  {
            // checks if there are still more spaces below and to the right of the queen
            if(colRight < COL_LENGTH) {
                if(board[r][colRight].isEmpty())
                    board[r][colRight].invalidateSpace();
                colRight++;
            }
            // checks if there are still more spaces below and to the left of the queen
            if(colLeft >= 0) {
                if(board[r][colLeft].isEmpty())
                    board[r][colLeft].invalidateSpace();
                colLeft--;
            }
            r++;
        }

        // invalidates all spaces diagonally above the queen
        r = row - 1;
        colRight = col + 1;
        colLeft = col - 1;
        while(r >= 0 && (colRight < COL_LENGTH || colLeft >= 0)) {
            // checks if there are still more spaces above and to the right of the queen
            if(colRight < COL_LENGTH) {
                if(board[r][colRight].isEmpty())
                    board[r][colRight].invalidateSpace();
                colRight++;
            }
            // checks if there are still more queens above and to the left of the queen
            if(colLeft >= 0) {
                if(board[r][colLeft].isEmpty())
                    board[r][colLeft].invalidateSpace();
                colLeft--;
            }
            r--;
        }

    }
    /**
     * isEmpty checks if a given space is empty
     * @param row the row of the space
     * @param col the column of the space
     * @return true if the space is empty, false if it isn't
     */
    public boolean isEmpty(int row, int col) {
        return board[row][col].isEmpty();
    }

    /**
     * checkWin checks if the game was won
     * @return true if the game is won, false otherwise
     */
    public boolean checkWin() {
        // checks the board for any empty spaces
        for(int row = 0; row < ROW_LENGTH; row++) {
            for(int col = 0; col < COL_LENGTH; col++) {
                if(board[row][col].isEmpty())
                    return false;
            }
        }
        return true;
    }
}
