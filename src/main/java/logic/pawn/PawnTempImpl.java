package logic.pawn;

import logic.BoardPosition;
import logic.Player;

import java.awt.*;

public class PawnTempImpl implements PawnInterface {
    private final Player player;
    private final int row;
    private final int column;

    public PawnTempImpl(Player player, int row, int column) {
        this.player = player;
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return player.getColor() == Color.RED ? "R" : "B";
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public BoardPosition getPosition() {
        return null;
    }
    //TODO: Add setters and getters for row and col. king is bonus for now. remove id.
}
