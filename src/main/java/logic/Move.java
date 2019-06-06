package logic;

public class Move {
    private final BoardPosition destination;
    private final MoveType type;

    public enum MoveType {REGULAR, EAT}

    public Move(BoardPosition destination, MoveType type) {
        this.destination = destination;
        this.type = type;
    }

    public BoardPosition getDestination() {
        return destination;
    }

    public MoveType getType() {
        return type;
    }
}
