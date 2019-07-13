package model;

import controller.Player;

import java.awt.*;

public class Pawn {
    private final Player player;
    private final BoardPosition position;
    private boolean queen;

    public Pawn(Player player, int row, int column) {
        this.player = player;
        this.position = new BoardPosition(row, column);
    }

    public String toString() {
        return player.getColor() == Color.RED ? "R" : "B";
    }

    public Player getPlayer() {
        return player;
    }

    public BoardPosition getPosition() {
        return position;
    }

    public void setPosition(int row, int column) {
        position.set(row, column);
    }

    public void setQueen(boolean b) {
        queen = b;
    }

    public boolean isQueen() {
        return queen;
    }
}
