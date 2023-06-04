package rs.moma.gui.helper;

import javax.swing.table.DefaultTableCellRenderer;

public class NumberRenderer extends DefaultTableCellRenderer {
    public NumberRenderer() {
        super();
        setHorizontalAlignment(RIGHT);
    }

    public NumberRenderer(boolean isRight) {
        super();
        if (isRight) setHorizontalAlignment(RIGHT);
    }

    public void setValue(Object value) {
        if (value instanceof Float) super.setValue(String.format("%,.2f", value));
        if (value instanceof Integer) super.setValue(String.format("%d", value));
    }
}