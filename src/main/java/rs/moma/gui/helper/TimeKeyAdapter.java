package rs.moma.gui.helper;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TimeKeyAdapter extends KeyAdapter {
    public void keyTyped(KeyEvent e) {
        char       c      = e.getKeyChar();
        JTextField txt    = (JTextField) e.getComponent();
        String     input  = txt.getText().replace("h", "").replaceAll("^0", "");
        int        posOld = txt.getCaretPosition();
        int        pos    = Math.min(posOld, input.length());
        e.consume();
        if (c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
            String tmp = input.substring(0, pos) + c + input.substring(pos);
            if (c >= ('0' + (pos == 0 ? 1 : 0)) && c <= '9' && Integer.parseInt(tmp) > 0 && Integer.parseInt(tmp) < 24) {
                input = tmp;
            } else {
                e.getComponent().getToolkit().beep();
                return;
            }
        }
        txt.setText((input.equals("") ? String.valueOf(0) : input) + "h");
        txt.setCaretPosition(c >= '0' && c <= '9' ? pos + 1 : posOld);
    }
}
