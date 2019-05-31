package logic;

import logic.pawn.PawnInterface;
import logic.pawn.PawnTempImpl;

public class Board {
    private PawnInterface[][] board = new PawnInterface[8][8];
    private int numOfBluePawns = 12;
    private int numOfRedPawns = 12;

    public void fillBoard(Player p1, Player p2) {
        int evenRow = 0;
        for (int i = 0; i < 3; i++) {
            evenRow ^= 1;
            for (int j = 0; j < 8 - evenRow; j += 2) {
                board[i][j + evenRow] = new PawnTempImpl(p1, i, j + evenRow);
                board[7 - i][j + (evenRow ^ 1)] = new PawnTempImpl(p2, 7 - i, j + (evenRow ^ 1));
            }
        }
    }

    // This method will be unnecessary once the game board completes
    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
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

    public PawnInterface getPawnAtPosition(int row, int column) {
        return board[row][column];
    }
}
