package logic.pawn;

import logic.BoardPosition;
import logic.Player;


public  interface PawnInterface {

    int getId();

     Player getPlayer();

    BoardPosition getPosition();
}
