import view.GameView;

import javax.swing.*;

public class Damka {
    public static void main(String... args) {
        GameView display = new GameView();
        display.initialize();
        display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
