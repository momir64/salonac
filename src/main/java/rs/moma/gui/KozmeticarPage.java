package rs.moma.gui;

import rs.moma.entities.Klijent;
import rs.moma.entities.Salon;
import rs.moma.entities.ZakazaniTretman;
import rs.moma.entities.Zaposlen;
import rs.moma.managers.Klijenti;
import rs.moma.managers.Tretmani;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class KozmeticarPage extends JFrame {
    private       JButton                    leftBtn;
    private       JButton                    rightBtn;
    private       JTable                     kalendarTbl;
    private       JList<String>              tretmaniList;
    private       JTable                     rasporedTbl;
    private       JLabel                     mesecLbl;
    private       JPanel                     kozmeticarPanel;
    private       JScrollPane                kalendarPane;
    private final ArrayList<ZakazaniTretman> tretmani;
    private final String[]                   meseci         = {"Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar"};
    private final DefaultTableModel          kalendarModel  = new DefaultTableModel(null, new String[]{"Pon", "Uto", "Sre", "Čet", "Pet", "Sub", "Ned"});
    private final DefaultTableModel          rasporedModel  = new DefaultTableModel(null, new String[]{""});
    private final Calendar                   kalendar       = new GregorianCalendar();
    private       int                        selectedRow    = -1;
    private       int                        selectedColumn = -1;

    public KozmeticarPage(Salon salon) {
        tretmani = ((Zaposlen) salon.korisnik).getZakazaniTretmani();

        setContentPane(kozmeticarPanel);
        setMinimumSize(new Dimension(750, 800));
        setTitle(salon.korisnik.Ime + " " + salon.korisnik.Prezime);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1050, 1000);
        setLocationRelativeTo(null);

        tretmaniList.setListData(((Zaposlen) salon.korisnik).getTretmani().stream().sorted().toArray(String[]::new));
        tretmaniList.setSelectionModel(new NoSelectionModel());

        kalendarPane.setViewportView(kalendarTbl);
        kalendarTbl.setRowHeight(50);
        kalendarTbl.setModel(kalendarModel);
        kalendarTbl.getTableHeader().setPreferredSize(new Dimension(0, 50));
        kalendarTbl.getTableHeader().setBackground(new Color(81, 84, 86));
        kalendarTbl.getTableHeader().setReorderingAllowed(false);
        kalendarTbl.getTableHeader().setResizingAllowed(false);

        rasporedTbl.setRowHeight(150);
        rasporedTbl.setModel(rasporedModel);
        rasporedTbl.setTableHeader(null);
        rasporedTbl.setSelectionModel(new NoSelectionModel());
        rasporedTbl.getColumnModel().getColumn(0).setCellRenderer(new MultilineCellRenderer());

        updateMonth();

        leftBtn.addActionListener(e -> {
            kalendar.add(Calendar.MONTH, -1);
            updateMonth();
        });

        rightBtn.addActionListener(e -> {
            kalendar.add(Calendar.MONTH, 1);
            updateMonth();
        });

        setVisible(true);
    }

    private LocalDate cell2date(int row, int column) {
        try {
            return LocalDate.of(kalendar.get(Calendar.YEAR), kalendar.get(Calendar.MONTH) + 1, 2 - kalendar.get(Calendar.DAY_OF_WEEK) + row * 7 + column);
        } catch (DateTimeException e) {
            return null;
        }
    }

    private boolean isCellValid(int row, int column) {
        for (ZakazaniTretman tretman : tretmani) {
            if (tretman.Vreme.toLocalDate().equals(cell2date(row, column)))
                return true;
        }
        return false;
    }

    private void updateMonth() {
        kalendarModel.setRowCount(0);
        kalendarModel.setRowCount(6);
        int i = kalendar.get(Calendar.DAY_OF_WEEK) - 1;
        for (int day = 1; day <= kalendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
             kalendarModel.setValueAt(day, i / 7, i++ % 7);
        mesecLbl.setText(meseci[kalendar.get(Calendar.MONTH)] + " " + kalendar.get(Calendar.YEAR));
    }

    private String[][] tretman2list(ZakazaniTretman tretman) {
        Klijent klijent = new Klijenti().get(tretman.KlijentID);
        return new String[][]{{}, {"Vreme: ", tretman.Vreme.getHour() + "h"},
                              {"Klijent: ", klijent.Ime + " " + klijent.Prezime},
                              {"Tretman: ", new Tretmani().get(tretman.TretmanID).Naziv},
                              {"Trajanje: ", tretman.Trajanje + " minuta"}};
    }

    private void fillRaspored(LocalDate date) {
        rasporedModel.setRowCount(0);
        for (ZakazaniTretman tretman : tretmani)
            if (tretman.Vreme.toLocalDate().equals(date))
                rasporedModel.addRow(new Object[]{tretman2list(tretman)});
    }

    private void createUIComponents() {
        kalendarTbl = new JTable() {
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
            @Override
            public boolean isCellSelected(int row, int column) {
                boolean selected = getSelectedColumnCount() == 1 && getSelectedRowCount() == 1 && getValueAt(row, column) != null && isCellValid(row, column) && super.isCellSelected(row, column);
                if (selected && (selectedColumn != column || selectedRow != row)) {
                    selectedColumn = column;
                    selectedRow    = row;
                    fillRaspored(cell2date(row, column));
                }
                return selected;
            }
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (getValueAt(row, column) == null || !isCellValid(row, column))
                    return new DisabledRenderer();
                return super.getCellRenderer(row, column);
            }
        };
    }
}
