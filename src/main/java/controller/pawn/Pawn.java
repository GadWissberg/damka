package controller.pawn;

import controller.*;
import java.awt.*;
import model.*;

public class Pawn {
    private final Player player;
    private final BoardPosition position;

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
        position.set(row,column);
    }
}