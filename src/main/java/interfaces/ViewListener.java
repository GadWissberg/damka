package interfaces;

import controller.Board;
import controller.Player;

import java.awt.event.MouseListener;

public interface ViewListener extends MouseListener {
    void setCellSize(int width, int height);

    void subscribeForOutput(DamkaDisplay subscriber);

    Player getPlayer1();

    Player getPlayer2();

    Player getCurrentTurn();

    Board getBoard();
}
