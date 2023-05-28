package rs.moma.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MultilineCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JTable cell = new JTable();
        cell.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(88, 90, 92)));
        cell.setModel(new DefaultTableModel((String[][]) value, new String[]{"", ""}));
        DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
        renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
        cell.getColumnModel().getColumn(0).setCellRenderer(renderRight);
        cell.getColumnModel().getColumn(0).setMinWidth(100);
        cell.getColumnModel().getColumn(0).setMaxWidth(100);
        return cell;
    }
}
