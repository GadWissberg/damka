package logic.pawn;

import logic.BoardPosition;
import logic.Player;

import java.awt.*;

public class Pawn {
    private final Player player;
    private final int row;
    private final int column;

    public Pawn(Player player, int row, int column) {
        this.player = player;
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return player.getColor() == Color.RED ? "R" : "B";
    }

    public int getId() {
        return 0;
    }

    public Player getPlayer() {
        return player;
    }

    public BoardPosition getPosition() {
        return null;
    }
    //TODO: Add setters and getters for row and col. king is bonus for now. remove id.
}
