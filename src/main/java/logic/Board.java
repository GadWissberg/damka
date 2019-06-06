package logic;

import logic.pawn.Pawn;

import java.lang.reflect.Array;

public class Board {
    public static final int CELLS_IN_ROW = 8;
    private Pawn[][] board = new Pawn[CELLS_IN_ROW][CELLS_IN_ROW];
    private int numOfBluePawns = 12;
    private int numOfRedPawns = 12;
    private Pawn selectedPawn;

    public void fillBoard(Player p1, Player p2) {
        int evenRow = 0;
        for (int i = 0; i < 3; i++) {
            evenRow ^= 1;
            for (int j = 0; j < CELLS_IN_ROW - evenRow; j += 2) {
                board[i][j + evenRow] = new Pawn(p1, i, j + evenRow);
                board[7 - i][j + (evenRow ^ 1)] = new Pawn(p2, 7 - i, j + (evenRow ^ 1));
            }
        }
    }

    // This method will be unnecessary once the game board completes
    public void printBoard() {
        for (int i = 0; i < CELLS_IN_ROW; i++) {
            for (int j = 0; j < CELLS_IN_ROW; j++) {
                if (board[i][j] == null)
                    System.out.print(" " + " ");
                else
                    System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int getNumOfBluePawns() {
        return numOfBluePawns;
    }

    public int getNumOfRedPawns() {
        return numOfRedPawns;
    }

    public boolean spotOnBoardIsFree(int row, int column) {
        return this.board[row][column] == null;
    }

    public Pawn getPawnAtPosition(int row, int column) {
        return board[row][column];
    }

    public void setSelectedPawn(Pawn pawn) {
        selectedPawn = pawn;
    }
}
