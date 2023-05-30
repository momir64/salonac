package rs.moma.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;

public class DateKeyAdapter extends KeyAdapter {
    private final Window     Parent;
    private final JTextField Txt;

    DateKeyAdapter(Window parent, JTextField txt) {
        Parent = parent;
        Txt    = txt;
    }

    public void keyTyped(KeyEvent e) {
        char   c      = e.getKeyChar();
        String input  = Txt.getText().replace("_", "").replace(".", "");
        int    posOld = Txt.getCaretPosition();
        int    pos    = posOld - (posOld > 10 ? 3 : posOld > 5 ? 2 : posOld > 2 ? 1 : 0);
        if (c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE)
            input = input.substring(0, pos) + c + input.substring(pos);
        else if (c == KeyEvent.VK_DELETE && (posOld == 2 || posOld == 5))
            input = input.substring(0, pos) + input.substring(pos + 1);
        if (((c < '0' || c > '9') && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) || input.length() > 8)
            Parent.getToolkit().beep();
        else {
            input = input + String.join("", Collections.nCopies(8 - input.length(), "_"));
            Txt.setText(input.substring(0, 2) + "." + input.substring(2, 4) + "." + input.substring(4, 8) + ".");
            Txt.setCaretPosition(c >= '0' && c <= '9' ? pos + 1 + (pos > 3 ? 2 : pos > 1 ? 1 : 0) : posOld);
        }
        e.consume();
    }
}
