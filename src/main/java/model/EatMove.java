package model;

import controller.pawn.Pawn;

public class EatMove extends Move {
    private final Pawn pawnToEat;

    public EatMove(BoardPosition destination, Pawn pawnToEat) {
        super(destination);
        this.pawnToEat = pawnToEat;
        this.type = MoveType.EAT;
    }

    public Pawn getPawnToEat() {
        return pawnToEat;
    }
}
