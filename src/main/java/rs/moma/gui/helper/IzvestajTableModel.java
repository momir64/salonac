package rs.moma.gui.helper;

import javax.swing.table.DefaultTableModel;

public class IzvestajTableModel extends DefaultTableModel {
    public IzvestajTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 1) return Integer.class;
        if (column == 2) return Float.class;
        return super.getColumnClass(column);
    }
}
