package rs.moma.gui.crud.helper;

import javax.swing.*;
import java.awt.*;

public class CheckBoxListRenderer extends JCheckBox implements ListCellRenderer<CheckListTip> {
    public Component getListCellRendererComponent(JList<? extends CheckListTip> comp, CheckListTip value, int index, boolean isSelected, boolean hasFocus) {
        setSelected(comp.isEnabled() && value.Selected);
        setBackground(comp.getBackground());
        setForeground(comp.getForeground());
        setEnabled(comp.isEnabled());
        setText(value.toString());
        setFont(comp.getFont());
        return this;
    }
}