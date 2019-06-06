package logic;

public class BoardPosition {
    private int row, col;

    public BoardPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void set(int row, int column) {
        this.row = row;
        this.col = column;
    }
}
