package rs.moma.gui.helper;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NumericKeyAdapter extends KeyAdapter {
    private final boolean AllowFloat;

    public NumericKeyAdapter(boolean allowFloat) {
        AllowFloat = allowFloat;
    }

    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!(c >= '0' && c <= '9' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || (AllowFloat && c == '.' && !((JTextField) e.getComponent()).getText().contains(".")))) {
            e.getComponent().getToolkit().beep();
            e.consume();
        }
    }
}
