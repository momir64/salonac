package rs.moma.gui.helper;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.*;

public class DisabledRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setForeground(new Color(96, 103, 106));
        return c;
    }
}
