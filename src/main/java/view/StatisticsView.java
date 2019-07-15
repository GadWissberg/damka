package view;

import interfaces.ViewListener;

import javax.swing.*;
import java.awt.*;

public class StatisticsView extends JFrame {

    JFrame statFrame = new JFrame();

    public StatisticsView(ViewListener viewListener) {
        super("Games statistics");
        this.setSize(new Dimension(300, 160));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel gamesWonOne = new JLabel("Player 1:  " +viewListener.getGamesWonOne());
        JLabel gamesWonTwo = new JLabel("Player 2:  " +viewListener.getGamesWonTwo());
        gamesWonOne.setFont (gamesWonOne.getFont().deriveFont(34.0f));
        gamesWonTwo.setFont (gamesWonTwo.getFont().deriveFont(34.0f));
        panel.add(gamesWonOne);
        panel.add(gamesWonTwo);
        add(panel);
    }

}
