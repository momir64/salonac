package rs.moma.gui;

import rs.moma.entities.ZakazaniTretman;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.function.Supplier;

public abstract class KalendarForm extends JFrame {
    protected final String[]          meseci         = {"Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar"};
    protected final DefaultTableModel kalendarModel  = new DefaultTableModel(null, new String[]{"Pon", "Uto", "Sre", "Čet", "Pet", "Sub", "Ned"});
    protected final Calendar          kalendar       = new GregorianCalendar();
    protected       int               selectedRow    = -1;
    protected       int               selectedColumn = -1;
    protected       boolean           EmptySelectable;
    private         JTable            tretmaniTbl;
    private         boolean           showDelete;
    private         boolean           showEdit;

    protected Supplier<ArrayList<ZakazaniTretman>> tretmani;
    protected KalendarForm(Supplier<ArrayList<ZakazaniTretman>> tretmani, boolean emptySelectable) {
        EmptySelectable = emptySelectable;
        this.tretmani   = tretmani;
    }

    protected void setup(JScrollPane kalendarPane, JTable tretmaniTbl, JTable kalendarTbl, JLabel mesecLbl, JButton rightBtn, JButton leftBtn, boolean showDelete, boolean showEdit) {
        this.tretmaniTbl = tretmaniTbl;
        this.showDelete  = showDelete;
        this.showEdit    = showEdit;

        kalendarPane.setViewportView(kalendarTbl);
        kalendarTbl.setRowHeight(50);
        kalendarTbl.setModel(kalendarModel);
        kalendarTbl.getTableHeader().setPreferredSize(new Dimension(0, 50));
        kalendarTbl.getTableHeader().setBackground(new Color(81, 84, 86));
        kalendarTbl.getTableHeader().setReorderingAllowed(false);
        kalendarTbl.getTableHeader().setResizingAllowed(false);

        tretmaniTbl.setRowHeight(200);
        tretmaniTbl.setTableHeader(null);
        tretmaniTbl.setSelectionModel(new NoSelectionModel());
        setTretmaniModel(new DefaultTableModel(null, new String[]{""}));

        leftBtn.addActionListener(e -> {
            kalendar.add(Calendar.MONTH, -1);
            updateMonth(mesecLbl);
        });

        rightBtn.addActionListener(e -> {
            kalendar.add(Calendar.MONTH, 1);
            updateMonth(mesecLbl);
        });

        updateMonth(mesecLbl);
    }

    protected void setTretmaniModel(TableModel model) {
        tretmaniTbl.setModel(model);
        tretmaniTbl.getColumnModel().getColumn(0).setCellRenderer(new MultilineCellRenderer(showDelete, showEdit, null, null));
        tretmaniTbl.getColumnModel().getColumn(0).setCellEditor(new MultilineCellRenderer(showDelete, showEdit, this::removeTretman, null));
    }

    protected LocalDate cell2date(int row, int column) {
        try {
            return LocalDate.of(kalendar.get(Calendar.YEAR), kalendar.get(Calendar.MONTH) + 1, 2 - kalendar.get(Calendar.DAY_OF_WEEK) + row * 7 + column);
        } catch (DateTimeException e) {
            return null;
        }
    }

    protected boolean isCellValid(int row, int column) {
        for (ZakazaniTretman tretman : tretmani.get()) {
            if (tretman.Vreme.toLocalDate().equals(cell2date(row, column)))
                return true;
        }
        return false;
    }

    protected void updateMonth(JLabel mesecLbl) {
        kalendarModel.setRowCount(0);
        kalendarModel.setRowCount(6);
        selectedColumn = -1;
        selectedRow    = -1;
        int i = kalendar.get(Calendar.DAY_OF_WEEK) - 1;
        for (int day = 1; day <= kalendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
             kalendarModel.setValueAt(day, i / 7, i++ % 7);
        mesecLbl.setText(meseci[kalendar.get(Calendar.MONTH)] + " " + kalendar.get(Calendar.YEAR));
    }

    protected abstract String[][] tretman2list(ZakazaniTretman tretman);

    protected void fillTretmani() {
        fillTretmani(cell2date(selectedRow, selectedColumn));
    }

    protected void fillTretmani(LocalDate date) {
        DefaultTableModel tretmaniModel = new DefaultTableModel(null, new String[]{""});
        for (ZakazaniTretman tretman : tretmani.get())
            if (tretman.Vreme.toLocalDate().equals(date))
                tretmaniModel.addRow(new Object[]{new KeyValue(tretman2list(tretman), tretman)});
        setTretmaniModel(tretmaniModel);
    }

    protected abstract void Update();

    public void removeTretman(ZakazaniTretman tretman) {
        fillTretmani();
        Update();
    }

    protected JTable makeKalendarTable() {
        return new JTable() {
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }

            @Override
            public boolean isCellSelected(int row, int column) {
                boolean selected = getSelectedColumnCount() == 1 && getSelectedRowCount() == 1 && getValueAt(row, column) != null && (EmptySelectable || isCellValid(row, column)) && super.isCellSelected(row, column);
                if (selected && (selectedColumn != column || selectedRow != row)) {
                    selectedColumn = column;
                    selectedRow    = row;
                    if (isCellValid(row, column))
                        fillTretmani(cell2date(row, column));
                    else
                        setTretmaniModel(new DefaultTableModel(null, new String[]{""}));
                }
                return selected;
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (getValueAt(row, column) == null || !isCellValid(row, column))
                    return new DisabledDateRenderer();
                return super.getCellRenderer(row, column);
            }
        };
    }
}
