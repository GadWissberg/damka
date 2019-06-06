package interfaces;

import logic.Board;
import logic.Player;

import java.awt.event.MouseListener;

public interface Controller extends MouseListener {
    void setCellSize(int width, int height);

    void subscribeForOutput(DamkaDisplay subscriber);

    Player getPlayer1();

    Player getPlayer2();

    Player getCurrentTurn();

    Board getBoard();
}
