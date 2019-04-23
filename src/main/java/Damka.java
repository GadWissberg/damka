import logic.Player;
import logic.Session;
import view.GameView;

import javax.swing.*;
import java.awt.*;

public class Damka {
    public static void main(String... args) {
        Session session = new Session();
        GameView display = new GameView();
        display.initialize(session);
        display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Player p1 = new Player("Trump", Color.RED);
        Player p2 = new Player("Bibi", Color.BLUE);
        session.begin(p1, p2);
    }
}
