package interfaces;

import controller.*;
import java.awt.event.*;

public interface Controller extends MouseListener {
    void setCellSize(int width, int height);

    void subscribeForOutput(DamkaDisplay subscriber);

    Player getPlayer1();

    Player getPlayer2();

    Player getCurrentTurn();

    Board getBoard();
}
