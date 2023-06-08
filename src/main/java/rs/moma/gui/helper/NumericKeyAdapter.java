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
        char       c   = e.getKeyChar();
        JTextField txt = (JTextField) e.getComponent();
        if (!(c >= '0' && c <= '9' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || (AllowFloat && c == '.' && txt.getText().length() > 0 && !txt.getText().contains(".")))) {
            e.getComponent().getToolkit().beep();
            e.consume();
        }
        if (txt.getText().equals("."))
            txt.setText("0");
    }
}
