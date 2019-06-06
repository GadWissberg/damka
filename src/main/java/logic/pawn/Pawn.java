package logic.pawn;

import logic.BoardPosition;
import logic.Player;

import java.awt.*;

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

    public int getId() {
        return 0;
    }

    public Player getPlayer() {
        return player;
    }

    public BoardPosition getPosition() {
        return position;
    }
    //TODO: Add setters and getters for row and col. king is bonus for now. remove id.
}
