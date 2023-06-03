package rs.moma.gui;

import rs.moma.helper.NazivVrednostVreme;
import rs.moma.helper.BrojVrednost;
import rs.moma.helper.KeyValue;
import rs.moma.managers.ZakazaniTretmani;
import rs.moma.managers.Klijenti;
import rs.moma.managers.Isplate;
import rs.moma.entities.Zaposlen;
import rs.moma.entities.Tretman;
import rs.moma.entities.Klijent;
import rs.moma.entities.Salon;
import rs.moma.gui.helper.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
    private JButton              removeBonusRuleBtn;
    private JTextField           odPeriodPrihodiTxt;
    private JTextField           doPeriodPrihodiTxt;
    private JTable               prihodiRashodiTbl;
    private JLabel               otkazaoKlijentLbl;
    private JButton              saveBonusRuleBtn;
    private JLabel               nijeSePojavioLbl;
    private JButton              editSalonDataBtn;
    private JButton              makeIzvestajiBtn;
    private JButton              removeEntietBtn;
    private JLabel               otkazaoSalonLbl;
    private JTextField           minLojalnostTxt;
    private JTextField           bonusOfWhatTxt;
    private JComboBox<NameValue> bonusOfWhatBox;
    private JButton              makeLojalneBtn;
    private JButton              showPrihodiBtn;
    private JButton              editEntitetBtn;
    private JComboBox<NameValue> entitetTypeBox;
    private JTextField           bonusPeriodTxt;
    private JTextField           pocetakRadaTxt;
    private JComboBox<NameValue> bonusPeriodBox;
    private JTextField           nazivSalonaTxt;
    private JButton              addEntitetBtn;
    private JComboBox<NameValue> bonusRuleBox;
    private JTextField           krajRadaTxt;
    private JTable               entitetiTbl;
    private JLabel               izvrsenoLbl;
    private JList<String>        lojalniLst;
    private JLabel               prihodiLbl;
    private JLabel               rashodiLbl;
    private JTabbedPane          tabbedPane;
    private JPanel               mainPanel;
    private JLabel               saldoLbl;

    public MenadzerPage(Zaposlen menadzer) {
        setSize(1300, 950);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1245, 801));
        setTitle(menadzer.Ime + " " + menadzer.Prezime);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Salon salon = new Salon();
        nazivSalonaTxt.setText(String.valueOf(salon.Naziv));
        pocetakRadaTxt.setText(salon.PocetakRadnogVremena + "h");
        krajRadaTxt.setText(salon.KrajRadnogVremena + "h");
        minLojalnostTxt.setText(String.valueOf(salon.MinIznosLojalnosti));

        bonusPeriodBox.setModel(new DefaultComboBoxModel<>(new NameValue[]{new NameValue("Dana", "d"), new NameValue("Nedelja", "w"), new NameValue("Meseci", "m")}));
        bonusOfWhatBox.setModel(new DefaultComboBoxModel<>(new NameValue[]{new NameValue("Broja", "c"), new NameValue("Vrednosti", "v")}));
        fillBonusRules(salon.Bonus.split("\\+"));

        zaposleniIzvestajTbl.setModel(new IzvestajTableModel(new Object[][]{}, new String[]{"Kozmetičar", "Izvršio", "Prihodovao"}));
        tretmaniIzvestajTbl.setModel(new IzvestajTableModel(new Object[][]{}, new String[]{"Tretman", "Izvršeno", "Vrednost"}));
        prihodiRashodiTbl.setModel(new PrihodiTableModel(new Object[][]{}, new String[]{"Vreme", "Naziv", "Iznos"}));
        setIzvestajiWidth();
        setPrihodiWidth();

        zaposleniIzvestajTbl.getTableHeader().setReorderingAllowed(false);
        tretmaniIzvestajTbl.getTableHeader().setReorderingAllowed(false);
        prihodiRashodiTbl.getTableHeader().setReorderingAllowed(false);
        zaposleniIzvestajTbl.setSelectionModel(new NoSelectionModel());
        tretmaniIzvestajTbl.setSelectionModel(new NoSelectionModel());
        prihodiRashodiTbl.setSelectionModel(new NoSelectionModel());
        lojalniLst.setSelectionModel(new NoSelectionModel());
        zaposleniIzvestajTbl.setAutoCreateRowSorter(true);
        tretmaniIzvestajTbl.setAutoCreateRowSorter(true);
        prihodiRashodiTbl.setAutoCreateRowSorter(true);

        odPeriodIzvestajiTxt.setText(new ZakazaniTretmani().getOldestDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        odPeriodPrihodiTxt.setText(new Salon().getOldestPrihodRashod().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        doPeriodIzvestajiTxt.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        doPeriodPrihodiTxt.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));

        minLojalnostTxt.addKeyListener(new NumericKeyAdapter(true));
        bonusPeriodTxt.addKeyListener(new NumericKeyAdapter(false));
        bonusOfWhatTxt.addKeyListener(new NumericKeyAdapter(true));
        odPeriodIzvestajiTxt.addKeyListener(new DateKeyAdapter());
        doPeriodIzvestajiTxt.addKeyListener(new DateKeyAdapter());
        odPeriodPrihodiTxt.addKeyListener(new DateKeyAdapter());
        doPeriodPrihodiTxt.addKeyListener(new DateKeyAdapter());
        pocetakRadaTxt.addKeyListener(new TimeKeyAdapter());
        krajRadaTxt.addKeyListener(new TimeKeyAdapter());

        isplatiZaposleneBtn.addActionListener(e -> isplariZaposlene());
        removeBonusRuleBtn.addActionListener(e -> removeBonusRule());
        saveBonusRuleBtn.addActionListener(e -> saveBonusRule());
        makeIzvestajiBtn.addActionListener(e -> makeIzvestaji());
        editSalonDataBtn.addActionListener(e -> editSalon());
        makeLojalneBtn.addActionListener(e -> makeLojalne());
        showPrihodiBtn.addActionListener(e -> showPrihodi());
        bonusRuleBox.addActionListener(e -> fillBonusRule());

        setVisible(true);
        fillBonusRule();
        makeIzvestaji();
        showPrihodi();
        makeLojalne();
    }

    public void showPrihodi() {
        LocalDateTime to   = getDatum(doPeriodPrihodiTxt) != null ? getDatum(doPeriodPrihodiTxt).atTime(LocalTime.MAX) : null;
        LocalDateTime from = getDatum(odPeriodPrihodiTxt) != null ? getDatum(odPeriodPrihodiTxt).atStartOfDay() : null;

        ArrayList<NazivVrednostVreme> finansije = new Salon().getFinansije(from, to);

        if (from != null && to != null && from.isBefore(to)) {
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

    public void isplariZaposlene() {
        new Isplate().isplati();
    }

    public String getBonusRuleData() {
        if (bonusPeriodBox.getSelectedItem() == null || bonusOfWhatBox.getSelectedItem() == null) return null;
        return ((NameValue) bonusPeriodBox.getSelectedItem()).Value + "-" + bonusPeriodTxt.getText() + "-" +
               ((NameValue) bonusOfWhatBox.getSelectedItem()).Value + "-" + bonusOfWhatTxt.getText();
    }

    public void saveBonusRule() {
        bonusPeriodTxt.setText(simplifyFloatToString(Float.parseFloat(bonusPeriodTxt.getText())));
        bonusOfWhatTxt.setText(simplifyFloatToString(Float.parseFloat(bonusOfWhatTxt.getText())));
        ArrayList<String> rules = new ArrayList<>();
        for (int i = 0; i < bonusRuleBox.getItemCount(); i++)
            if (i == bonusRuleBox.getSelectedIndex())
                rules.add(getBonusRuleData());
            else if (bonusRuleBox.getItemAt(i).Value != null)
                rules.add((String) bonusRuleBox.getItemAt(i).Value);
        new Salon().edit(null, -1, -1, -1, String.join("+", rules));
        int oldPos = bonusRuleBox.getSelectedIndex();
        fillBonusRules(rules.toArray(new String[0]));
        bonusRuleBox.setSelectedIndex(oldPos);
    }

    public void removeBonusRule() {
        if (bonusRuleBox.getSelectedIndex() == bonusRuleBox.getItemCount() - 1) return;
        ArrayList<String> rules = new ArrayList<>();
        for (int i = 0; i < bonusRuleBox.getItemCount() - 1; i++)
            if (i != bonusRuleBox.getSelectedIndex() && bonusRuleBox.getItemAt(i).Value != null)
                rules.add((String) bonusRuleBox.getItemAt(i).Value);
        new Salon().edit(null, -1, -1, -1, String.join("+", rules));
        fillBonusRules(rules.toArray(new String[0]));
    }

    public void fillBonusRules(String[] rules) {
        bonusRuleBox.removeAllItems();
        for (int i = 0; i < rules.length; i++) bonusRuleBox.addItem(new NameValue("Bonus pravilo " + (i + 1), rules[i]));
        bonusRuleBox.addItem(new NameValue("Novo pravilo", null));
    }

    public void fillBonusRule() {
        if (bonusRuleBox.getSelectedItem() == null) return;
        String rule = (String) ((NameValue) bonusRuleBox.getSelectedItem()).Value;
        if (rule == null) {
            bonusPeriodBox.setSelectedIndex(0);
            bonusOfWhatBox.setSelectedIndex(0);
            bonusPeriodTxt.setText("0");
            bonusOfWhatTxt.setText("0");
        } else {
            String[] parts = rule.split("-");
            bonusPeriodBox.setSelectedItem(new NameValue("", parts[0]));
            bonusPeriodTxt.setText(parts[1]);
            bonusOfWhatBox.setSelectedItem(new NameValue("", parts[2]));
            bonusOfWhatTxt.setText(parts[3]);
        }
    }

    public void setPrihodiWidth() {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.LEFT);
        int WIDTH1 = 190;
        int WIDTH2 = 150;
        prihodiRashodiTbl.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        prihodiRashodiTbl.getColumnModel().getColumn(2).setCellRenderer(new FloatRenderer());
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
        zaposleniIzvestajTbl.getColumnModel().getColumn(2).setCellRenderer(new FloatRenderer());
        tretmaniIzvestajTbl.getColumnModel().getColumn(2).setCellRenderer(new FloatRenderer());
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

        if (from != null && to != null && from.isBefore(to)) {
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

    public int txtTimeToInt(JTextField txt) {
        return Integer.parseInt(txt.getText().replace("h", ""));
    }

    public void editSalon() {
        Salon salon = new Salon();
        if (txtTimeToInt(pocetakRadaTxt) < txtTimeToInt(krajRadaTxt)) {
            salon.edit(nazivSalonaTxt.getText(), txtTimeToInt(pocetakRadaTxt), txtTimeToInt(krajRadaTxt), -1, null);
            showMessageDialog(this, "Uspešno čuvanje podataka!");
        } else {
            showMessageDialog(this, "Neispravno radno vreme!");
            pocetakRadaTxt.setText(salon.PocetakRadnogVremena + "h");
            krajRadaTxt.setText(salon.KrajRadnogVremena + "h");
        }
    }

    public void makeLojalne() {
        new Salon().edit(null, -1, -1, txtToFloat(minLojalnostTxt), null);
        ArrayList<String> output = new ArrayList<>(Arrays.asList(padRight("Klijent", 30) + "Potrošio", ""));
        output.addAll(toArrayList(new Klijenti().getLojalne().stream().sorted(Comparator.comparing(Klijent::getUkupnoPlatio).reversed())
                                                .map(klijent -> padRight(klijent.getDisplayName(), 30) + klijent.getUkupnoPlatio())));
        lojalniLst.setListData(output.toArray(new String[0]));
    }

    private void createUIComponents() {
        prihodiRashodiTbl = new StripedTable();
    }
}
