import controller.Json;
import controller.Player;
import controller.Session;
import view.GameWindow;

import javax.swing.*;
import java.awt.*;

public class Damka {
    public static void main(String... args) {
        Session session = new Session();
        GameWindow display = new GameWindow();
        Player p1 = new Player("Trump", Color.RED, Player.Direction.DOWN);
        Player p2 = new Player("Bibi", Color.BLUE, Player.Direction.UP);
        session.initialize(p1, p2);
        display.initialize(session, session);
        display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        display.setResizable(false);

        // json test
        Json j = new Json();
        j.getAllSessionData(session);
    }
}
