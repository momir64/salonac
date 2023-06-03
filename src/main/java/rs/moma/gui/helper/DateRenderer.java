package rs.moma.gui.helper;

import javax.swing.table.DefaultTableCellRenderer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateRenderer extends DefaultTableCellRenderer {
    public void setValue(Object value) {
        if (value instanceof LocalDateTime) super.setValue(((LocalDateTime) value).format(DateTimeFormatter.ofPattern("HH:mm  dd.MM.yyyy.")));
        else if (value instanceof LocalDate) super.setValue("00:00  " + ((LocalDate) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
    }
}