package rs.moma.gui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NumericKeyAdapter extends KeyAdapter {
    private final Window Parent;

    NumericKeyAdapter(Window parent) {
        Parent = parent;
    }

    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!(c >= '0' && c <= '9' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            Parent.getToolkit().beep();
            e.consume();
        }
    }
}
