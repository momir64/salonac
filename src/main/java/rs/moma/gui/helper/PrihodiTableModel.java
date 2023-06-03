package rs.moma.gui.helper;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;

public class PrihodiTableModel extends DefaultTableModel {
    public PrihodiTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 0) return LocalDateTime.class;
        if (column == 2) return Float.class;
        return super.getColumnClass(column);
    }
}
