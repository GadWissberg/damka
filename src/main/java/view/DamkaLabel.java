package view;

import javax.swing.*;
import java.awt.*;

public class DamkaLabel extends JLabel {
    private static final Font font = new Font("Serif", Font.PLAIN, 25);
    private static final int HORIZONTAL_MARGIN = 25;

    public DamkaLabel() {
        this(null);
    }

    public DamkaLabel(String text) {
        super(text);
        setFont(font);
        setBorder(BorderFactory.createEmptyBorder(0, HORIZONTAL_MARGIN, 0, HORIZONTAL_MARGIN));
    }
}
