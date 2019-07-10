package interfaces;

import java.util.*;
import model.*;

public interface DamkaDisplay {
    void setSelectionImage(double x, double y);

    void refreshDisplay();

    void setSelectionImageVisibility(boolean b);

    void displayMessage(String text);

    void setAvailableMovesLocations(ArrayList<BoardPixelLocation> canMoveto);
}
