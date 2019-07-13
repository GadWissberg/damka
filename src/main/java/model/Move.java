package model;

public class Move {
    private final BoardPosition destination;
    MoveType type = MoveType.REGULAR;

    public enum MoveType {REGULAR, EAT}

    public Move(BoardPosition destination) {
        this.destination = destination;
    }

    public BoardPosition getDestination() {
        return destination;
    }

    public MoveType getType() {
        return type;
    }
}
