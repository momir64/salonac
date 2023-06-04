package rs.moma.gui;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import rs.moma.entities.*;
import rs.moma.gui.crud.*;
import rs.moma.helper.NazivVrednostVreme;
import rs.moma.helper.BrojVrednost;
import rs.moma.helper.KeyValue;
import rs.moma.managers.*;
import rs.moma.gui.helper.*;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private JPanel               prihodiTipoviPanel;
    private JPanel               realizovaniPoKozmeticarimaPanel;
    private JPanel               procenatStatusaPanel;

    public MenadzerPage(Zaposlen menadzer, WelcomePage homePage) {
        $$$setupUI$$$();
        setSize(1500, 950);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1300, 801));
        setTitle(menadzer.Ime + " " + menadzer.Prezime);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

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

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                homePage.setVisible(true);
            }
        });

        setVisible(true);
        showDijagrami();
        makeIzvestaji();
        showEntiteti();
        showPrihodi();
        makeLojalne();
    }

    public void showDijagrami() {
        if (!new ZakazaniTretmani().get().isEmpty()) {
            prihodiTipoviPanel.add(new XChartPanel<>(makePrihodiTipoviChart()));
            procenatStatusaPanel.add(new XChartPanel<>(makePieChart("Status kozmetičkih tretmana u poslednjih 30 dana", new ZakazaniTretmani().getStatusiDijagramData())));
            realizovaniPoKozmeticarimaPanel.add(new XChartPanel<>(makePieChart("Opterećenje kozmetičara u poslednjih 30 dana", new ZakazaniTretmani().getKozmeticariDijagramData())));
        }
    }

    public PieChart makePieChart(String title, ArrayList<NameValue> data) {
        PieChart chart = new PieChartBuilder().theme(Styler.ChartTheme.GGPlot2).width(getMinimumSize().width / 3 - 10).height(getMinimumSize().height - 100).title(title).build();
        chart.getStyler().setLabelsVisible(false);
        chart.getStyler().setChartBackgroundColor(new Color(60, 63, 65));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        for (NameValue nv : data) if ((int) nv.Value > 0) chart.addSeries(nv.Name, (int) nv.Value);
        return chart;
    }

    public XYChart makePrihodiTipoviChart() {
        XYChart chart = new XYChartBuilder().theme(Styler.ChartTheme.GGPlot2).width(getMinimumSize().width / 3 - 10).height(getMinimumSize().height - 100).title("Prihodi po tipu kozmetičkog tretmana").build();
        chart.getStyler().setChartBackgroundColor(new Color(60, 63, 65));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);

        for (NameValue nv : new ZakazaniTretmani().getPrihodiTipDijagramData()) {
            ArrayList<Integer> xData = new ArrayList<>();
            ArrayList<Double>  yData = new ArrayList<>();
            for (KeyValue mesecIznos : (ArrayList<KeyValue>) nv.Value)
                if ((Double) mesecIznos.Value > 0) {
                    xData.add((int) mesecIznos.Key);
                    yData.add((Double) mesecIznos.Value);
                }
            if (!xData.isEmpty()) chart.addSeries(nv.Name, xData, yData);
        }

        chart.getStyler().setxAxisTickLabelsFormattingFunction(x -> getMonthName((int) x.longValue()));

        return chart;
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
            TipoviTretmana     tipoviTretmana = new TipoviTretmana();
            ArrayList<Tretman> tretmani       = new Tretmani().get();
            for (Tretman tretman : tretmani)
                values.add(new Object[]{tretman, tretman.Naziv, tipoviTretmana.get(tretman.TipID).Tip, tretman.Cena, padLeft(String.valueOf(tretman.Trajanje), 3) + " min"});
            entitetiTbl.setModel(new DefaultTableModel(values.toArray(new Object[][]{}), new String[]{"", "Naziv", "Tip", "Cena", "Trajanje"}));
            columnModel.removeColumn(columnModel.getColumn(0));
            columnModel.getColumn(2).setCellRenderer(new NumberRenderer(false));

        } else if (((NameValue) entitetTypeBox.getSelectedItem()).Value == ZakazaniTretman.class) {
            ArrayList<ZakazaniTretman> zakazaniTretmani = new ZakazaniTretmani().get();
            Zaposleni                  zaposleni        = new Zaposleni();
            Klijenti                   klijenti         = new Klijenti();
            Tretmani                   tretmani         = new Tretmani();
            for (ZakazaniTretman tretman : zakazaniTretmani)
                values.add(new Object[]{tretman, tretmani.get(tretman.TretmanID).Naziv, tretman.Vreme.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy.")),
                                        getStanjeName(tretman.Stanje), klijenti.get(tretman.KlijentID).getDisplayName(),
                                        zaposleni.get(tretman.KozmeticarID).getDisplayName(),
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
    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        final JTabbedPane  tabbedPane1 = new JTabbedPane();
        GridBagConstraints gbc;
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        mainPanel.add(tabbedPane1, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Salon", panel1);
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(4);
        label1.setHorizontalTextPosition(4);
        label1.setText("Naziv salona: ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 1;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 15, 10);
        panel1.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        label2.setHorizontalTextPosition(4);
        label2.setText("Pečetak radnog vremena: ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 2;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 15, 10);
        panel1.add(label2, gbc);
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(4);
        label3.setHorizontalTextPosition(4);
        label3.setText("Kraj radnog vremena: ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 3;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 15, 10);
        panel1.add(label3, gbc);
        final JPanel spacer1 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 0;
        gbc.gridy      = 1;
        gbc.gridheight = 4;
        gbc.weightx    = 1.0;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 3;
        gbc.gridy      = 1;
        gbc.gridheight = 4;
        gbc.weightx    = 1.0;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 4;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 5;
        gbc.gridwidth = 4;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        pocetakRadaTxt = new JTextField();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 2;
        gbc.gridy      = 2;
        gbc.weightx    = 1.0;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.ipady      = 10;
        gbc.insets     = new Insets(0, 0, 15, 100);
        panel1.add(pocetakRadaTxt, gbc);
        krajRadaTxt = new JTextField();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 2;
        gbc.gridy   = 3;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipady   = 10;
        gbc.insets  = new Insets(0, 0, 15, 100);
        panel1.add(krajRadaTxt, gbc);
        editSalonDataBtn = new JButton();
        editSalonDataBtn.setText("Sačuvaj");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 2;
        gbc.gridy   = 4;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipady   = 10;
        gbc.insets  = new Insets(0, 0, 0, 100);
        panel1.add(editSalonDataBtn, gbc);
        nazivSalonaTxt = new JTextField();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 2;
        gbc.gridy      = 1;
        gbc.weightx    = 1.0;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.ipady      = 10;
        gbc.insets     = new Insets(0, 0, 15, 100);
        panel1.add(nazivSalonaTxt, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Finansije", panel2);
        final JPanel spacer5 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 0;
        gbc.gridy      = 1;
        gbc.gridheight = 5;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 60;
        panel2.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 6;
        gbc.gridwidth = 10;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 60;
        panel2.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 9;
        gbc.gridy      = 1;
        gbc.gridheight = 5;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 60;
        panel2.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 10;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 60;
        panel2.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 3;
        gbc.gridy      = 3;
        gbc.gridheight = 3;
        gbc.weightx    = 1.0;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 2;
        gbc.gridwidth = 8;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 20;
        panel2.add(spacer10, gbc);
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(4);
        label4.setHorizontalTextPosition(4);
        label4.setRequestFocusEnabled(false);
        label4.setText("Od: ");
        gbc       = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.fill  = GridBagConstraints.BOTH;
        gbc.ipady = 5;
        panel2.add(label4, gbc);
        odPeriodPrihodiTxt = new JTextField();
        odPeriodPrihodiTxt.setText("__.__.____.");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 6;
        gbc.gridy   = 3;
        gbc.weightx = 0.3;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipadx   = 100;
        gbc.ipady   = 3;
        gbc.insets  = new Insets(0, 0, 3, 0);
        panel2.add(odPeriodPrihodiTxt, gbc);
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(4);
        label5.setHorizontalTextPosition(4);
        label5.setText("Do: ");
        gbc       = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.fill  = GridBagConstraints.BOTH;
        gbc.ipady = 5;
        panel2.add(label5, gbc);
        doPeriodPrihodiTxt = new JTextField();
        doPeriodPrihodiTxt.setText("__.__.____.");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 6;
        gbc.gridy   = 4;
        gbc.weightx = 0.3;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipadx   = 100;
        gbc.ipady   = 3;
        gbc.insets  = new Insets(0, 0, 3, 0);
        panel2.add(doPeriodPrihodiTxt, gbc);
        showPrihodiBtn = new JButton();
        showPrihodiBtn.setText("Prikaži");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 6;
        gbc.gridy   = 5;
        gbc.weightx = 0.3;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipadx   = 40;
        gbc.ipady   = 3;
        gbc.insets  = new Insets(0, 0, 3, 0);
        panel2.add(showPrihodiBtn, gbc);
        final JPanel spacer11 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 5;
        gbc.gridy      = 3;
        gbc.gridheight = 3;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 5;
        panel2.add(spacer11, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 1;
        gbc.gridwidth = 8;
        gbc.weightx   = 1.0;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.BOTH;
        panel2.add(scrollPane1, gbc);
        Font prihodiRashodiTblFont = this.$$$getFont$$$("Consolas", -1, 15, prihodiRashodiTbl.getFont());
        if (prihodiRashodiTblFont != null) prihodiRashodiTbl.setFont(prihodiRashodiTblFont);
        scrollPane1.setViewportView(prihodiRashodiTbl);
        isplatiZaposleneBtn = new JButton();
        isplatiZaposleneBtn.setHorizontalTextPosition(0);
        isplatiZaposleneBtn.setText("<html><div style=\"text-align: center;\">Isplati<br>zaposlene</div></html>");
        gbc            = new GridBagConstraints();
        gbc.gridx      = 8;
        gbc.gridy      = 3;
        gbc.gridheight = 3;
        gbc.weightx    = 0.1;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.ipady      = 3;
        panel2.add(isplatiZaposleneBtn, gbc);
        final JPanel spacer12 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 7;
        gbc.gridy      = 3;
        gbc.gridheight = 3;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 5;
        panel2.add(spacer12, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Ukupni prihodi: ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 3;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.ipadx  = 15;
        gbc.ipady  = 5;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel2.add(label6, gbc);
        prihodiLbl = new JLabel();
        prihodiLbl.setHorizontalAlignment(4);
        prihodiLbl.setHorizontalTextPosition(4);
        prihodiLbl.setMinimumSize(new Dimension(150, 17));
        prihodiLbl.setText("0 RSD");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 3;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 15, 15, 0);
        panel2.add(prihodiLbl, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Ukupni rashodi: ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 4;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.ipady  = 5;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel2.add(label7, gbc);
        rashodiLbl = new JLabel();
        rashodiLbl.setHorizontalAlignment(4);
        rashodiLbl.setHorizontalTextPosition(4);
        rashodiLbl.setMinimumSize(new Dimension(150, 17));
        rashodiLbl.setText("0 RSD");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 4;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 15, 15, 0);
        panel2.add(rashodiLbl, gbc);
        saldoLbl = new JLabel();
        saldoLbl.setHorizontalAlignment(4);
        saldoLbl.setHorizontalTextPosition(4);
        saldoLbl.setMinimumSize(new Dimension(150, 17));
        saldoLbl.setText("0 RSD");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 5;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 15, 15, 0);
        panel2.add(saldoLbl, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Saldo: ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 5;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.ipady  = 5;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel2.add(label8, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Kartica lojalnosti", panel3);
        final JPanel spacer13 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 0;
        gbc.gridy      = 1;
        gbc.gridheight = 3;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 60;
        panel3.add(spacer13, gbc);
        final JPanel spacer14 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 6;
        gbc.gridy      = 1;
        gbc.gridheight = 3;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 60;
        panel3.add(spacer14, gbc);
        final JPanel spacer15 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 7;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 60;
        panel3.add(spacer15, gbc);
        final JPanel spacer16 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 4;
        gbc.gridwidth = 7;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 60;
        panel3.add(spacer16, gbc);
        minLojalnostTxt = new JTextField();
        gbc             = new GridBagConstraints();
        gbc.gridx       = 3;
        gbc.gridy       = 1;
        gbc.weightx     = 1.0;
        gbc.anchor      = GridBagConstraints.WEST;
        gbc.fill        = GridBagConstraints.BOTH;
        gbc.ipady       = 8;
        panel3.add(minLojalnostTxt, gbc);
        makeLojalneBtn = new JButton();
        makeLojalneBtn.setText("Generiši listu klijenata sa karticom lojalnosti");
        gbc       = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.fill  = GridBagConstraints.BOTH;
        gbc.ipady = 8;
        panel3.add(makeLojalneBtn, gbc);
        final JPanel spacer17 = new JPanel();
        gbc       = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 3;
        gbc.ipady = 8;
        panel3.add(spacer17, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Minimalni potrošeni iznos:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 8;
        panel3.add(label9, gbc);
        final JPanel spacer18 = new JPanel();
        gbc       = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 3;
        gbc.ipady = 8;
        panel3.add(spacer18, gbc);
        final JPanel spacer19 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 2;
        gbc.gridwidth = 5;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 20;
        panel3.add(spacer19, gbc);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 3;
        gbc.gridwidth = 5;
        gbc.weightx   = 1.0;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.BOTH;
        panel3.add(scrollPane2, gbc);
        lojalniLst = new JList();
        Font lojalniLstFont = this.$$$getFont$$$("Consolas", -1, 15, lojalniLst.getFont());
        if (lojalniLstFont != null) lojalniLst.setFont(lojalniLstFont);
        scrollPane2.setViewportView(lojalniLst);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Baza podataka", panel4);
        final JPanel spacer20 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 0;
        gbc.gridy      = 1;
        gbc.gridheight = 5;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 60;
        panel4.add(spacer20, gbc);
        final JPanel spacer21 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 6;
        gbc.gridy      = 1;
        gbc.gridheight = 5;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 60;
        panel4.add(spacer21, gbc);
        final JPanel spacer22 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 7;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 60;
        panel4.add(spacer22, gbc);
        final JPanel spacer23 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 6;
        gbc.gridwidth = 7;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 60;
        panel4.add(spacer23, gbc);
        entitetTypeBox = new JComboBox();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 1;
        gbc.gridy      = 1;
        gbc.gridwidth  = 5;
        gbc.anchor     = GridBagConstraints.WEST;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipady      = 8;
        panel4.add(entitetTypeBox, gbc);
        final JPanel spacer24 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 4;
        gbc.gridwidth = 5;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 20;
        panel4.add(spacer24, gbc);
        addEntitetBtn = new JButton();
        addEntitetBtn.setText("Dodaj");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 1;
        gbc.gridy   = 5;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.ipady   = 15;
        panel4.add(addEntitetBtn, gbc);
        editEntitetBtn = new JButton();
        editEntitetBtn.setText("Izmeni");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 3;
        gbc.gridy   = 5;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.ipady   = 15;
        panel4.add(editEntitetBtn, gbc);
        removeEntitetBtn = new JButton();
        removeEntitetBtn.setText("Obriši");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 5;
        gbc.gridy   = 5;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.ipady   = 15;
        panel4.add(removeEntitetBtn, gbc);
        final JPanel spacer25 = new JPanel();
        gbc       = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        gbc.ipady = 15;
        panel4.add(spacer25, gbc);
        final JPanel spacer26 = new JPanel();
        gbc       = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 5;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        gbc.ipady = 15;
        panel4.add(spacer26, gbc);
        final JPanel spacer27 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 2;
        gbc.gridwidth = 5;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 20;
        panel4.add(spacer27, gbc);
        final JScrollPane scrollPane3 = new JScrollPane();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 3;
        gbc.gridwidth = 5;
        gbc.weightx   = 1.0;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.BOTH;
        panel4.add(scrollPane3, gbc);
        Font entitetiTblFont = this.$$$getFont$$$("Consolas", -1, 15, entitetiTbl.getFont());
        if (entitetiTblFont != null) entitetiTbl.setFont(entitetiTblFont);
        scrollPane3.setViewportView(entitetiTbl);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Izveštaji", panel5);
        final JPanel spacer28 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 0;
        gbc.gridy      = 1;
        gbc.gridheight = 8;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 60;
        panel5.add(spacer28, gbc);
        final JPanel spacer29 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 10;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 60;
        panel5.add(spacer29, gbc);
        final JPanel spacer30 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 9;
        gbc.gridwidth = 10;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 60;
        panel5.add(spacer30, gbc);
        odPeriodIzvestajiTxt = new JTextField();
        odPeriodIzvestajiTxt.setText("__.__.____.");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 3;
        gbc.gridy   = 6;
        gbc.weightx = 1.0;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipady   = 10;
        gbc.insets  = new Insets(0, 0, 4, 0);
        panel5.add(odPeriodIzvestajiTxt, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("Od:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 10;
        panel5.add(label10, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Do:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 10;
        panel5.add(label11, gbc);
        doPeriodIzvestajiTxt = new JTextField();
        doPeriodIzvestajiTxt.setText("__.__.____.");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 3;
        gbc.gridy   = 7;
        gbc.weightx = 1.0;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipady   = 10;
        gbc.insets  = new Insets(0, 0, 4, 0);
        panel5.add(doPeriodIzvestajiTxt, gbc);
        makeIzvestajiBtn = new JButton();
        makeIzvestajiBtn.setLabel("Prikaži");
        makeIzvestajiBtn.setText("Prikaži");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 3;
        gbc.gridy  = 8;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.ipady  = 10;
        panel5.add(makeIzvestajiBtn, gbc);
        final JPanel spacer31 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 5;
        gbc.gridwidth = 3;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 30;
        panel5.add(spacer31, gbc);
        final JLabel label12 = new JLabel();
        label12.setHorizontalAlignment(4);
        label12.setText("Izvršeno: ");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 5;
        gbc.gridy   = 1;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.ipady   = 15;
        panel5.add(label12, gbc);
        izvrsenoLbl = new JLabel();
        izvrsenoLbl.setText("0");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 6;
        gbc.gridy   = 1;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipady   = 15;
        gbc.insets  = new Insets(0, 10, 0, 0);
        panel5.add(izvrsenoLbl, gbc);
        final JPanel spacer32 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 5;
        gbc.gridy     = 3;
        gbc.gridwidth = 4;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 20;
        panel5.add(spacer32, gbc);
        final JLabel label13 = new JLabel();
        label13.setHorizontalAlignment(4);
        label13.setText("Otkazao klijent: ");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 5;
        gbc.gridy   = 2;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.ipady   = 15;
        panel5.add(label13, gbc);
        otkazaoKlijentLbl = new JLabel();
        otkazaoKlijentLbl.setText("0");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 6;
        gbc.gridy   = 2;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipady   = 15;
        gbc.insets  = new Insets(0, 10, 0, 0);
        panel5.add(otkazaoKlijentLbl, gbc);
        final JPanel spacer33 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 2;
        gbc.gridy      = 6;
        gbc.gridheight = 3;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.ipadx      = 3;
        gbc.ipady      = 10;
        panel5.add(spacer33, gbc);
        final JPanel spacer34 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 4;
        gbc.gridy      = 1;
        gbc.gridheight = 8;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 30;
        panel5.add(spacer34, gbc);
        final JPanel spacer35 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 9;
        gbc.gridy      = 1;
        gbc.gridheight = 8;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 60;
        panel5.add(spacer35, gbc);
        final JLabel label14 = new JLabel();
        label14.setHorizontalAlignment(4);
        label14.setText("Nije se pojavio: ");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 7;
        gbc.gridy   = 1;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.ipady   = 15;
        panel5.add(label14, gbc);
        nijeSePojavioLbl = new JLabel();
        nijeSePojavioLbl.setText("0");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 8;
        gbc.gridy   = 1;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipady   = 15;
        gbc.insets  = new Insets(0, 10, 0, 0);
        panel5.add(nijeSePojavioLbl, gbc);
        otkazaoSalonLbl = new JLabel();
        otkazaoSalonLbl.setText("0");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 8;
        gbc.gridy   = 2;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.ipady   = 15;
        gbc.insets  = new Insets(0, 10, 0, 0);
        panel5.add(otkazaoSalonLbl, gbc);
        final JLabel label15 = new JLabel();
        label15.setHorizontalAlignment(4);
        label15.setText("Otkazao salon: ");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 7;
        gbc.gridy   = 2;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.ipady   = 15;
        panel5.add(label15, gbc);
        final JScrollPane scrollPane4     = new JScrollPane();
        Font              scrollPane4Font = this.$$$getFont$$$("Consolas", -1, 15, scrollPane4.getFont());
        if (scrollPane4Font != null) scrollPane4.setFont(scrollPane4Font);
        gbc            = new GridBagConstraints();
        gbc.gridx      = 1;
        gbc.gridy      = 1;
        gbc.gridwidth  = 3;
        gbc.gridheight = 4;
        gbc.weightx    = 6.0;
        gbc.weighty    = 1.0;
        gbc.fill       = GridBagConstraints.BOTH;
        panel5.add(scrollPane4, gbc);
        Font tretmaniIzvestajTblFont = this.$$$getFont$$$("Consolas", -1, 15, tretmaniIzvestajTbl.getFont());
        if (tretmaniIzvestajTblFont != null) tretmaniIzvestajTbl.setFont(tretmaniIzvestajTblFont);
        scrollPane4.setViewportView(tretmaniIzvestajTbl);
        final JScrollPane scrollPane5     = new JScrollPane();
        Font              scrollPane5Font = this.$$$getFont$$$("Consolas", -1, 15, scrollPane5.getFont());
        if (scrollPane5Font != null) scrollPane5.setFont(scrollPane5Font);
        gbc            = new GridBagConstraints();
        gbc.gridx      = 5;
        gbc.gridy      = 4;
        gbc.gridwidth  = 4;
        gbc.gridheight = 5;
        gbc.weightx    = 1.0;
        gbc.fill       = GridBagConstraints.BOTH;
        panel5.add(scrollPane5, gbc);
        Font zaposleniIzvestajTblFont = this.$$$getFont$$$("Consolas", -1, 15, zaposleniIzvestajTbl.getFont());
        if (zaposleniIzvestajTblFont != null) zaposleniIzvestajTbl.setFont(zaposleniIzvestajTblFont);
        scrollPane5.setViewportView(zaposleniIzvestajTbl);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Dijagrami", panel6);
        prihodiTipoviPanel = new JPanel();
        prihodiTipoviPanel.setLayout(new GridBagLayout());
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        panel6.add(prihodiTipoviPanel, gbc);
        realizovaniPoKozmeticarimaPanel = new JPanel();
        realizovaniPoKozmeticarimaPanel.setLayout(new GridBagLayout());
        gbc         = new GridBagConstraints();
        gbc.gridx   = 1;
        gbc.gridy   = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        panel6.add(realizovaniPoKozmeticarimaPanel, gbc);
        procenatStatusaPanel = new JPanel();
        procenatStatusaPanel.setLayout(new GridBagLayout());
        gbc         = new GridBagConstraints();
        gbc.gridx   = 2;
        gbc.gridy   = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        panel6.add(procenatStatusaPanel, gbc);
    }
    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {resultName = currentFont.getName();} else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {resultName = fontName;} else {
                resultName = currentFont.getName();
            }
        }
        Font    font             = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac            = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font    fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {return mainPanel;}
}
