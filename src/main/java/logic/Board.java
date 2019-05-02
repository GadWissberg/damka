package logic;

public class Board {
    private String[][] board = new String[8][8];
    private int numOfBluePawns = 12;
    private int numOfRedPawns = 12;

    public void fillBoard() {
        int evenRow = 0;
        for (int i = 0; i < 3; i++) {
            evenRow ^= 1;
            for (int j = 0; j < 8-evenRow; j += 2) {
                board[i][j+evenRow] = "R";
                board[7-i][j+(evenRow^1)]= "B";
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

    public int getNumOfBluePawns() { return  numOfBluePawns; }
    public int getNumOfRedPawns() { return  numOfRedPawns; }

    public boolean spotOnBoardIsFree(int row, int column){
        return this.board[row][column] == null;
    }

}
