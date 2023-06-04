package rs.moma.gui;

import rs.moma.entities.*;
import rs.moma.gui.crud.*;
import rs.moma.helper.NazivVrednostVreme;
import rs.moma.helper.BrojVrednost;
import rs.moma.helper.KeyValue;
import rs.moma.managers.*;
import rs.moma.gui.helper.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static javax.swing.JOptionPane.showMessageDialog;
import static rs.moma.helper.DataTools.EStanjeTermina.*;
import static rs.moma.helper.DataTools.*;

public class MenadzerPage extends JFrame {
    private JTextField           odPeriodIzvestajiTxt;
    private JTextField           doPeriodIzvestajiTxt;
    private JTable               zaposleniIzvestajTbl;
    private JTable               tretmaniIzvestajTbl;
    private JButton              isplatiZaposleneBtn;
    private JTextField           odPeriodPrihodiTxt;
    private JTextField           doPeriodPrihodiTxt;
    private JTable               prihodiRashodiTbl;
    private JLabel               otkazaoKlijentLbl;
    private JLabel               nijeSePojavioLbl;
    private JButton              editSalonDataBtn;
    private JButton              makeIzvestajiBtn;
    private JButton              removeEntitetBtn;
    private JLabel               otkazaoSalonLbl;
    private JTextField           minLojalnostTxt;
    private JButton              makeLojalneBtn;
    private JButton              showPrihodiBtn;
    private JButton              editEntitetBtn;
    private JComboBox<NameValue> entitetTypeBox;
    private JTextField           pocetakRadaTxt;
    private JTextField           nazivSalonaTxt;
    private JButton              addEntitetBtn;
    private JTextField           krajRadaTxt;
    private JTable               entitetiTbl;
    private JLabel               izvrsenoLbl;
    private JList<String>        lojalniLst;
    private JLabel               prihodiLbl;
    private JLabel               rashodiLbl;
    private JPanel               mainPanel;
    private JLabel               saldoLbl;

    public MenadzerPage(Zaposlen menadzer) {
        setSize(1500, 950);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1300, 801));
        setTitle(menadzer.Ime + " " + menadzer.Prezime);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Salon salon = new Salon();
        nazivSalonaTxt.setText(String.valueOf(salon.Naziv));
        pocetakRadaTxt.setText(salon.PocetakRadnogVremena + "h");
        krajRadaTxt.setText(salon.KrajRadnogVremena + "h");
        minLojalnostTxt.setText(String.valueOf(salon.MinIznosLojalnosti));

        entitetTypeBox.setModel(new DefaultComboBoxModel<>(new NameValue[]{
                new NameValue("Klijenti", Klijent.class), new NameValue("Zaposleni", Zaposlen.class), new NameValue("Tipovi tretmana", TipTretmana.class),
                new NameValue("Tretmani", Tretman.class), new NameValue("Zakazani tretmani", ZakazaniTretman.class)}));

        zaposleniIzvestajTbl.setModel(new IzvestajTableModel(new Object[][]{}, new String[]{"Kozmetičar", "Izvršio", "Prihodovao"}));
        tretmaniIzvestajTbl.setModel(new IzvestajTableModel(new Object[][]{}, new String[]{"Tretman", "Izvršeno", "Vrednost"}));
        prihodiRashodiTbl.setModel(new PrihodiTableModel(new Object[][]{}, new String[]{"Vreme", "Naziv", "Iznos"}));
        setIzvestajiWidth();
        setPrihodiWidth();

        entitetiTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        zaposleniIzvestajTbl.getTableHeader().setReorderingAllowed(false);
        tretmaniIzvestajTbl.getTableHeader().setReorderingAllowed(false);
        prihodiRashodiTbl.getTableHeader().setReorderingAllowed(false);
        entitetiTbl.getTableHeader().setReorderingAllowed(false);
        zaposleniIzvestajTbl.setSelectionModel(new NoSelectionModel());
        tretmaniIzvestajTbl.setSelectionModel(new NoSelectionModel());
        prihodiRashodiTbl.setSelectionModel(new NoSelectionModel());
        lojalniLst.setSelectionModel(new NoSelectionModel());
        zaposleniIzvestajTbl.setAutoCreateRowSorter(true);
        tretmaniIzvestajTbl.setAutoCreateRowSorter(true);
        prihodiRashodiTbl.setAutoCreateRowSorter(true);
        entitetiTbl.setAutoCreateRowSorter(true);

        odPeriodIzvestajiTxt.setText(new ZakazaniTretmani().getOldestDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        odPeriodPrihodiTxt.setText(new Salon().getOldestPrihodRashod().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        doPeriodIzvestajiTxt.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        doPeriodPrihodiTxt.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));

        minLojalnostTxt.addKeyListener(new NumericKeyAdapter(true));
        odPeriodIzvestajiTxt.addKeyListener(new DateKeyAdapter());
        doPeriodIzvestajiTxt.addKeyListener(new DateKeyAdapter());
        odPeriodPrihodiTxt.addKeyListener(new DateKeyAdapter());
        doPeriodPrihodiTxt.addKeyListener(new DateKeyAdapter());
        pocetakRadaTxt.addKeyListener(new TimeKeyAdapter());
        krajRadaTxt.addKeyListener(new TimeKeyAdapter());

        isplatiZaposleneBtn.addActionListener(e -> isplatiZaposlene());
        makeIzvestajiBtn.addActionListener(e -> makeIzvestaji());
        removeEntitetBtn.addActionListener(e -> removeEntitet());
        entitetTypeBox.addActionListener(e -> showEntiteti());
        editSalonDataBtn.addActionListener(e -> editSalon());
        makeLojalneBtn.addActionListener(e -> makeLojalne());
        showPrihodiBtn.addActionListener(e -> showPrihodi());
        editEntitetBtn.addActionListener(e -> editEntiet());
        addEntitetBtn.addActionListener(e -> addEntitet());

        setVisible(true);
        makeIzvestaji();
        showEntiteti();
        showPrihodi();
        makeLojalne();
    }

    public void addEntitet() {
        if (entitetTypeBox.getSelectedItem() == null) return;

        if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Klijent.class)
            new EditKlijentForm(this, null, this::showEntiteti);
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Zaposlen.class)
            new EditZaposlenForm(this, null, this::showEntiteti);
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == TipTretmana.class)
            new EditTipTretmanaForm(this, null, this::showEntiteti);
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Tretman.class)
            new EditTretmanForm(this, null, this::showEntiteti);
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == ZakazaniTretman.class)
            new EditZakazaniTretmanForm(this, null, this::showEntiteti);
    }

    public void editEntiet() {
        if (entitetTypeBox.getSelectedItem() == null || entitetiTbl.getSelectedRow() == -1) return;

        if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Klijent.class)
            new EditKlijentForm(this, (Klijent) entitetiTbl.getModel().getValueAt(entitetiTbl.convertRowIndexToModel(entitetiTbl.getSelectedRow()), 0), this::showEntiteti);
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Zaposlen.class)
            new EditZaposlenForm(this, (Zaposlen) entitetiTbl.getModel().getValueAt(entitetiTbl.convertRowIndexToModel(entitetiTbl.getSelectedRow()), 0), this::showEntiteti);
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == TipTretmana.class)
            new EditTipTretmanaForm(this, (TipTretmana) entitetiTbl.getModel().getValueAt(entitetiTbl.convertRowIndexToModel(entitetiTbl.getSelectedRow()), 0), this::showEntiteti);
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Tretman.class)
            new EditTretmanForm(this, (Tretman) entitetiTbl.getModel().getValueAt(entitetiTbl.convertRowIndexToModel(entitetiTbl.getSelectedRow()), 0), this::showEntiteti);
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == ZakazaniTretman.class)
            new EditZakazaniTretmanForm(this, (ZakazaniTretman) entitetiTbl.getModel().getValueAt(entitetiTbl.convertRowIndexToModel(entitetiTbl.getSelectedRow()), 0), this::showEntiteti);
    }

    public void removeEntitet() {
        if (entitetTypeBox.getSelectedItem() == null || entitetiTbl.getSelectedRow() == -1) return;

        if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Klijent.class)
            new Klijenti().remove((Klijent) entitetiTbl.getModel().getValueAt(entitetiTbl.convertRowIndexToModel(entitetiTbl.getSelectedRow()), 0));
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Zaposlen.class)
            new Zaposleni().remove((Zaposlen) entitetiTbl.getModel().getValueAt(entitetiTbl.convertRowIndexToModel(entitetiTbl.getSelectedRow()), 0));
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == TipTretmana.class)
            new TipoviTretmana().remove((TipTretmana) entitetiTbl.getModel().getValueAt(entitetiTbl.convertRowIndexToModel(entitetiTbl.getSelectedRow()), 0));
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Tretman.class)
            new Tretmani().remove((Tretman) entitetiTbl.getModel().getValueAt(entitetiTbl.convertRowIndexToModel(entitetiTbl.getSelectedRow()), 0));
        else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == ZakazaniTretman.class)
            new ZakazaniTretmani().remove((ZakazaniTretman) entitetiTbl.getModel().getValueAt(entitetiTbl.convertRowIndexToModel(entitetiTbl.getSelectedRow()), 0));

        showEntiteti();
    }

    public void showEntiteti() {
        if (entitetTypeBox.getSelectedItem() == null) return;
        TableColumnModel    columnModel = entitetiTbl.getColumnModel();
        ArrayList<Object[]> values      = new ArrayList<>();

        if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Klijent.class) {
            ArrayList<Klijent> klijenti = new Klijenti().get();
            for (Klijent klijent : klijenti)
                values.add(new Object[]{klijent, klijent.Ime, klijent.Prezime, getPolName(klijent.Pol), klijent.Telefon, klijent.Adresa, klijent.Username, klijent.Lozinka});
            entitetiTbl.setModel(new DefaultTableModel(values.toArray(new Object[][]{}), new String[]{"", "Ime", "Prezime", "Pol", "Telefon", "Adresa", "Username", "Lozinka"}));
            columnModel.removeColumn(columnModel.getColumn(0));
            columnModel.getColumn(4).setMinWidth(150);

        } else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Zaposlen.class) {
            ArrayList<Zaposlen> zaposleni = new Zaposleni().get();
            for (Zaposlen zaposlen : zaposleni)
                values.add(new Object[]{zaposlen, zaposlen.Ime, zaposlen.Prezime, getPolName(zaposlen.Pol), zaposlen.Telefon, zaposlen.Adresa, zaposlen.Username, zaposlen.Lozinka,
                                        getTipZaposlenogName(zaposlen.TipZaposlenog), getSpremaName(zaposlen.Sprema), zaposlen.GodineStaza, zaposlen.PlataOsnova});
            entitetiTbl.setModel(new DefaultTableModel(values.toArray(new Object[][]{}), new String[]{"", "Ime", "Prezime", "Pol", "Telefon", "Adresa", "Username",
                                                                                                      "Lozinka", "Posao", "Sprema", "Staž", "Osnovica"}));
            columnModel.removeColumn(columnModel.getColumn(0));
            columnModel.getColumn(3).setMinWidth(90);
            columnModel.getColumn(4).setMinWidth(185);
            columnModel.getColumn(5).setMinWidth(100);
            columnModel.getColumn(9).setCellRenderer(new NumberRenderer());
            columnModel.getColumn(10).setCellRenderer(new NumberRenderer());

        } else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == TipTretmana.class) {
            ArrayList<TipTretmana> tipovi = new TipoviTretmana().get();
            for (TipTretmana tip : tipovi)
                values.add(new Object[]{tip, tip.Tip});
            entitetiTbl.setModel(new DefaultTableModel(values.toArray(new Object[][]{}), new String[]{"", "Tip"}));
            columnModel.removeColumn(columnModel.getColumn(0));

        } else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == Tretman.class) {
            ArrayList<Tretman> tretmani = new Tretmani().get();
            for (Tretman tretman : tretmani)
                values.add(new Object[]{tretman, tretman.Naziv, new TipoviTretmana().get(tretman.TipID).Tip, tretman.Cena, padLeft(String.valueOf(tretman.Trajanje), 3) + " min"});
            entitetiTbl.setModel(new DefaultTableModel(values.toArray(new Object[][]{}), new String[]{"", "Naziv", "Tip", "Cena", "Trajanje"}));
            columnModel.removeColumn(columnModel.getColumn(0));
            columnModel.getColumn(2).setCellRenderer(new NumberRenderer(false));

        } else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == ZakazaniTretman.class) {
            ArrayList<ZakazaniTretman> tretmani = new ZakazaniTretmani().get();
            for (ZakazaniTretman tretman : tretmani)
                values.add(new Object[]{tretman, new Tretmani().get(tretman.TretmanID).Naziv, tretman.Vreme.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy.")),
                                        getStanjeName(tretman.Stanje), new Klijenti().get(tretman.KlijentID).getDisplayName(),
                                        new Zaposleni().get(tretman.KozmeticarID).getDisplayName(),
                                        tretman.getPlaceniIznos(), padLeft(String.valueOf(tretman.Trajanje), 3) + " min"});
            entitetiTbl.setModel(new DefaultTableModel(values.toArray(new Object[][]{}), new String[]{"", "Tretman", "Vreme", "Stanje", "Klijent",
                                                                                                      "Kozmetičar", "Cena", "Trajanje"}));
            columnModel.removeColumn(columnModel.getColumn(0));
            columnModel.getColumn(5).setCellRenderer(new NumberRenderer(false));
            columnModel.getColumn(0).setMinWidth(180);
            columnModel.getColumn(3).setMinWidth(110);
        }
    }

    public void showPrihodi() {
        LocalDateTime to   = getDatum(doPeriodPrihodiTxt) != null ? getDatum(doPeriodPrihodiTxt).atTime(LocalTime.MAX) : null;
        LocalDateTime from = getDatum(odPeriodPrihodiTxt) != null ? getDatum(odPeriodPrihodiTxt).atStartOfDay() : null;

        ArrayList<NazivVrednostVreme> finansije = new Salon().getFinansije(from, to);

        if (from != null && to != null && (from.isBefore(to) || from.isEqual(to))) {
            ArrayList<Object[]> values = new ArrayList<>();
            for (NazivVrednostVreme nvv : finansije)
                values.add(new Object[]{nvv.Vreme, nvv.Naziv, nvv.Vrednost});
            prihodiRashodiTbl.setModel(new PrihodiTableModel(values.toArray(new Object[][]{}), new String[]{"Vreme", "Naziv", "Iznos"}));
            prihodiLbl.setText(String.format("%,.2f", new ZakazaniTretmani().getPrihodiVrednost(from, to)) + " RSD");
            rashodiLbl.setText(String.format("%,.2f", new Isplate().getRashodiVrednost(from, to)) + " RSD");
            saldoLbl.setText(String.format("%,.2f", finansije.stream().mapToDouble(nvv -> nvv.Vrednost).sum()) + " RSD");
            setPrihodiWidth();
        } else {
            showMessageDialog(this, "Neispravan period!");
            if (!finansije.isEmpty())
                odPeriodPrihodiTxt.setText(new Salon().getOldestPrihodRashod().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
            doPeriodPrihodiTxt.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        }
    }

    public void isplatiZaposlene() {
        new Isplate().isplati();
        showPrihodi();
    }

    public void setPrihodiWidth() {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.LEFT);
        int WIDTH1 = 190;
        int WIDTH2 = 150;
        prihodiRashodiTbl.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        prihodiRashodiTbl.getColumnModel().getColumn(2).setCellRenderer(new NumberRenderer());
        prihodiRashodiTbl.getColumnModel().getColumn(0).setCellRenderer(new DateRenderer());
        prihodiRashodiTbl.getColumnModel().getColumn(2).setPreferredWidth(WIDTH2);
        prihodiRashodiTbl.getColumnModel().getColumn(0).setPreferredWidth(WIDTH1);
        prihodiRashodiTbl.getColumnModel().getColumn(2).setMaxWidth(WIDTH2);
        prihodiRashodiTbl.getColumnModel().getColumn(0).setMaxWidth(WIDTH1);
        prihodiRashodiTbl.getColumnModel().getColumn(0).setMinWidth(1);
        prihodiRashodiTbl.getColumnModel().getColumn(2).setMinWidth(1);
    }

    public void setIzvestajiWidth() {
        int WIDTH1 = 100;
        int WIDTH2 = 150;
        zaposleniIzvestajTbl.getColumnModel().getColumn(2).setCellRenderer(new NumberRenderer());
        tretmaniIzvestajTbl.getColumnModel().getColumn(2).setCellRenderer(new NumberRenderer());
        zaposleniIzvestajTbl.getColumnModel().getColumn(2).setPreferredWidth(WIDTH2);
        zaposleniIzvestajTbl.getColumnModel().getColumn(1).setPreferredWidth(WIDTH1);
        tretmaniIzvestajTbl.getColumnModel().getColumn(2).setPreferredWidth(WIDTH2);
        tretmaniIzvestajTbl.getColumnModel().getColumn(1).setPreferredWidth(WIDTH1);
        zaposleniIzvestajTbl.getColumnModel().getColumn(2).setMaxWidth(WIDTH2);
        zaposleniIzvestajTbl.getColumnModel().getColumn(1).setMaxWidth(WIDTH1);
        tretmaniIzvestajTbl.getColumnModel().getColumn(2).setMaxWidth(WIDTH2);
        tretmaniIzvestajTbl.getColumnModel().getColumn(1).setMaxWidth(WIDTH1);
        zaposleniIzvestajTbl.getColumnModel().getColumn(1).setMinWidth(1);
        zaposleniIzvestajTbl.getColumnModel().getColumn(2).setMinWidth(1);
        tretmaniIzvestajTbl.getColumnModel().getColumn(2).setMinWidth(1);
        tretmaniIzvestajTbl.getColumnModel().getColumn(1).setMinWidth(1);
    }

    public void makeIzvestaji() {
        ZakazaniTretmani tretmani = new ZakazaniTretmani();
        LocalDateTime    from     = getDatum(odPeriodIzvestajiTxt) != null ? getDatum(odPeriodIzvestajiTxt).atStartOfDay() : null;
        LocalDateTime    to       = getDatum(doPeriodIzvestajiTxt) != null ? getDatum(doPeriodIzvestajiTxt).atTime(LocalTime.MAX) : null;

        if (from != null && to != null && (from.isBefore(to) || from.isEqual(to))) {
            izvrsenoLbl.setText(String.valueOf(tretmani.countOfStatus(IZVRSEN, from, to)));
            nijeSePojavioLbl.setText(String.valueOf(tretmani.countOfStatus(NIJE_SE_POJAVIO, from, to)));
            otkazaoKlijentLbl.setText(String.valueOf(tretmani.countOfStatus(OTKAZAO_KLIJENT, from, to)));
            otkazaoSalonLbl.setText(String.valueOf(tretmani.countOfStatus(OTKAZAO_SALON, from, to)));

            ArrayList<Object[]> values = new ArrayList<>();
            for (KeyValue kv : tretmani.getTretmaniIzvestaj(from, to))
                values.add(new Object[]{((Tretman) kv.Key).Naziv, String.valueOf(((BrojVrednost) kv.Value).Broj), ((BrojVrednost) kv.Value).Vrednost});
            tretmaniIzvestajTbl.setModel(new IzvestajTableModel(values.toArray(new Object[][]{}), new String[]{"Tretman", "Izvršeno", "Vrednost"}));

            values.clear();
            for (KeyValue kv : tretmani.getKozmeticariIzvestaj(from, to))
                values.add(new Object[]{((Zaposlen) kv.Key).getDisplayName(), ((BrojVrednost) kv.Value).Broj, ((BrojVrednost) kv.Value).Vrednost});
            zaposleniIzvestajTbl.setModel(new IzvestajTableModel(values.toArray(new Object[][]{}), new String[]{"Kozmetičar", "Izvršio", "Prihodovao"}));

            setIzvestajiWidth();
        } else {
            showMessageDialog(this, "Neispravan period!");
            odPeriodIzvestajiTxt.setText(new ZakazaniTretmani().getOldestDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
            doPeriodIzvestajiTxt.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        }
    }

    public void editSalon() {
        Salon salon = new Salon();
        if (getTime(pocetakRadaTxt) < getTime(krajRadaTxt)) {
            salon.edit(nazivSalonaTxt.getText(), getTime(pocetakRadaTxt), getTime(krajRadaTxt), -1);
            showMessageDialog(this, "Uspešno čuvanje podataka!");
        } else {
            showMessageDialog(this, "Neispravno radno vreme!");
            pocetakRadaTxt.setText(salon.PocetakRadnogVremena + "h");
            krajRadaTxt.setText(salon.KrajRadnogVremena + "h");
        }
    }

    public void makeLojalne() {
        new Salon().edit(null, -1, -1, txtToFloat(minLojalnostTxt));
        ArrayList<String> output = new ArrayList<>(Arrays.asList(padRight("Klijent", 30) + "Potrošio", ""));
        output.addAll(toArrayList(new Klijenti().getLojalne().stream().sorted(Comparator.comparing(Klijent::getUkupnoPlatio).reversed())
                                                .map(klijent -> padRight(klijent.getDisplayName(), 30) + klijent.getUkupnoPlatio())));
        lojalniLst.setListData(output.toArray(new String[0]));
    }

    private void createUIComponents() {
        entitetiTbl          = new StripedTable();
        prihodiRashodiTbl    = new StripedTable();
        tretmaniIzvestajTbl  = new StripedTable();
        zaposleniIzvestajTbl = new StripedTable();
    }
}
