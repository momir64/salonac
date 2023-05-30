package rs.moma.gui;

import rs.moma.entities.ZakazaniTretman;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class MultilineCellRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    static        ImageIcon deleteIcon;
    static        ImageIcon editIcon;
    private final CellPanel cell;

    static {
        try {
            deleteIcon = new ImageIcon(ImageIO.read(ClassLoader.getSystemResource("delete.png")));
            editIcon   = new ImageIcon(ImageIO.read(ClassLoader.getSystemResource("edit.png")));
        } catch (IOException e) {
            System.err.println("Greška prilikom učitavanja ikona!");
            throw new RuntimeException(e);
        }
    }

    public MultilineCellRenderer(boolean showDelete, boolean showEdit, Consumer<ZakazaniTretman> delete, Consumer<ZakazaniTretman> edit) {
        cell = new CellPanel(showDelete, showEdit, delete, edit);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        cell.setData((KeyValue) value);
        return cell;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        cell.setData((KeyValue) value);
        return cell;
    }

    @Override
    public Object getCellEditorValue() {
        return cell.getData();
    }

    static class CellPanel extends JPanel {
        JButton  deleteBtn = new JButton(deleteIcon);
        JButton  editBtn   = new JButton(editIcon);
        JTable   table     = new JTable();
        KeyValue data;

        CellPanel(boolean showDelete, boolean showEdit, Consumer<ZakazaniTretman> delete, Consumer<ZakazaniTretman> edit) {
            super(new BorderLayout());
            JPanel p2 = new JPanel(new GridBagLayout());
            p2.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(88, 90, 92)));
            p2.setBackground(new Color(70, 73, 75));

            table.setSelectionModel(new NoSelectionModel());
            deleteBtn.addActionListener(e -> delete.accept((ZakazaniTretman) (data.getValue())));
            editBtn.addActionListener(e -> edit.accept((ZakazaniTretman) (data.getValue())));
            deleteBtn.setFocusPainted(false);
            editBtn.setFocusPainted(false);

            if (showDelete)
                p2.add(deleteBtn, new GridBagConstraints(1, 0, 1, 1, 0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(showEdit ? 20 : 0, 0, 0, 30), 10, 10));
            if (showEdit)
                p2.add(editBtn, new GridBagConstraints(1, 1, 1, 1, 0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 20, 30), 10, 10));
            p2.add(table, new GridBagConstraints(0, 0, 1, showEdit ? 2 : 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 20, 0, 0), 0, 0));

            JPanel p1 = new JPanel(new BorderLayout());
            p1.add(p2);
            add(p1);
        }

        public void setData(KeyValue data) {
            table.setModel(new DefaultTableModel((String[][]) data.getKey(), new String[]{"", ""}));
            DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
            renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
            table.getColumnModel().getColumn(0).setCellRenderer(renderRight);
            table.getColumnModel().getColumn(0).setMinWidth(100);
            table.getColumnModel().getColumn(0).setMaxWidth(100);
            this.data = data;
        }

        public KeyValue getData() {
            return data;
        }
    }
}
