package interfaces;

import java.awt.event.MouseListener;

public interface InputConsumer extends MouseListener {
    void setCellSize(int width, int height);

    void subscribeForOutput(OutputSubscriber subscriber);

}
