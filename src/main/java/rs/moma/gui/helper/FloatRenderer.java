package rs.moma.gui.helper;

import javax.swing.table.DefaultTableCellRenderer;

public class FloatRenderer extends DefaultTableCellRenderer {
    public FloatRenderer() {
        super();
        setHorizontalAlignment(RIGHT);
    }

    public void setValue(Object value) {
        if (value instanceof Float) super.setValue(String.format("%,.2f", value));
    }
}