package rs.moma.gui.helper;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class StripedTable extends JTable {
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        c.setBackground(row % 2 == 0 ? getBackground() : new Color(66, 68, 69));
        return c;
    }
}
