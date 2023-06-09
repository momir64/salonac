package rs.moma.gui.helper;

import javax.swing.table.DefaultTableCellRenderer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateRenderer extends DefaultTableCellRenderer {
    final boolean DoubleSpace;

    public DateRenderer() {
        DoubleSpace = true;
    }

    public DateRenderer(boolean doubleSpace) {
        DoubleSpace = doubleSpace;
    }

    public void setValue(Object value) {
        if (value instanceof LocalDateTime) super.setValue(((LocalDateTime) value).format(DateTimeFormatter.ofPattern("HH:mm " + (DoubleSpace ? " " : "") + "dd.MM.yyyy.")));
        else if (value instanceof LocalDate) super.setValue("00:00 " + (DoubleSpace ? " " : "") + ((LocalDate) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
    }
}