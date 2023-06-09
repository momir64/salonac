package rs.moma.gui;

import rs.moma.entities.*;
import rs.moma.gui.helper.NameValue;
import rs.moma.gui.helper.DateKeyAdapter;
import rs.moma.entities.helper.ClassWithID;
import rs.moma.managers.Klijenti;
import rs.moma.managers.Tretmani;
import rs.moma.managers.ZakazaniTretmani;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static rs.moma.helper.DataTools.*;
import static rs.moma.helper.DataTools.EStanjeTermina.*;

public class RecepcionerZakazivanjeForm extends ZakazivanjeForm {
    private       JComboBox<NameValue> kozmeticarBox;
    private       JComboBox<NameValue> klijentBox;
    private       JComboBox<NameValue> tretmanBox;
    private       JComboBox<NameValue> stanjeBox;
    private       JComboBox<NameValue> vremeBox;
    private       JLabel               stanjeLbl;
    private       JPanel               mainPanel;
    private       JTextField           datumTxt;
    private final ZakazaniTretman      tretman;
    private       JButton              addBtn;

    public RecepcionerZakazivanjeForm(JFrame parent, ZakazaniTretman tretman, LocalDate date, Runnable update) {
        super(parent, "Zakazivanje tretmana", true);
        this.tretman = tretman;

        setSize(655, 655);
        setContentPane(mainPanel);
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(510, 550));

        if (tretman == null) {
            remove(stanjeBox);
            remove(stanjeLbl);
        } else {
            addBtn.setText("Izmeni");
            ArrayList<EStanjeTermina> stanja = new ArrayList<>(Arrays.asList(OTKAZAO_KLIJENT, OTKAZAO_SALON));
            if (tretman.Vreme.isAfter(LocalDateTime.now()))
                stanja.add(ZAKAZAN);
//            ZAKOMETARISANO KAKO BI SE MOGLI IZVRŠITI TERMINI IZ BUDUĆNOSTI
//            || || || || || || || || || || || || || || || || || || || || ||
//            \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/
//            else  // <= POTREBNO SAMO OTKOMENTARISATI ELSE ZA REALNU UPOTREBU
            stanja.addAll(Arrays.asList(IZVRSEN, NIJE_SE_POJAVIO));

            stanja.sort(Comparator.comparing(Enum::toString));
            stanjeBox.setModel(new DefaultComboBoxModel<>(stanja.stream().map(stanje -> new NameValue(getStanjeName(stanje), stanje)).toArray(NameValue[]::new)));
            stanjeBox.setSelectedItem(new NameValue("", tretman.Stanje));
        }

        tretmanBox.addActionListener(e -> fillKozmeticariBox(tretmanBox, kozmeticarBox, vremeBox, getDatum(datumTxt), tretman));
        kozmeticarBox.addActionListener(e -> fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), tretman));
        date = tretman != null ? tretman.Vreme.toLocalDate() : date == null ? LocalDate.now() : date;
        datumTxt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        addBtn.addActionListener(e -> {
            if (tretman == null) zakaziTermin(update);
            else editTermin(tretman, update);
        });
        datumTxt.addKeyListener(new DateKeyAdapter());
        addOnChangeDo(datumTxt, () -> fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), tretman));
        fillKlijentiBox();
        fillTretmaniBox();
        selectOptionsIfNotNull();

        setVisible(true);
    }

    public void zakaziTermin(Runnable update) {
        if (vremeBox.getItemCount() == 0) return;
        new ZakazaniTretmani().add(new ZakazaniTretman((Tretman) getSelectedValue(tretmanBox),
                                                       getTermin(datumTxt, vremeBox), (Klijent) getSelectedValue(klijentBox),
                                                       (Zaposlen) getSelectedValue(kozmeticarBox)));
        fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), tretman);
        update.run();
    }

    public void editTermin(ZakazaniTretman oldTretman, Runnable update) {
        if (vremeBox.getItemCount() == 0) return;
        new ZakazaniTretmani().edit(oldTretman, new ZakazaniTretman((Tretman) getSelectedValue(tretmanBox),
                                                                    getTermin(datumTxt, vremeBox), (Klijent) getSelectedValue(klijentBox),
                                                                    (Zaposlen) getSelectedValue(kozmeticarBox), (EStanjeTermina) getSelectedValue(stanjeBox),
                                                                    oldTretman.KarticaLojalnosti));
        fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), tretman);
        update.run();
    }

    public void selectOptionsIfNotNull() {
        if (tretman == null) return;
        selectBoxOption(tretmanBox, tretman.TretmanID);
        selectBoxOption(klijentBox, tretman.KlijentID);
        selectBoxOption(kozmeticarBox, tretman.KozmeticarID);
        vremeBox.setSelectedItem(new NameValue("", tretman.Vreme.getHour()));
    }

    public void selectBoxOption(JComboBox<NameValue> box, int id) {
        for (int i = 0; i < box.getItemCount(); i++) {
            Object value = box.getItemAt(i).getValue();
            if (value instanceof ClassWithID && ((ClassWithID) value).getID() == id) {
                box.setSelectedIndex(i);
                break;
            }
        }
    }

    public void fillKlijentiBox() {
        ArrayList<Klijent> klijenti = toArrayList(new Klijenti().get().stream().filter(klijent -> klijent.Aktivan));
        klijenti.sort(Comparator.comparing(tip -> tip.Ime));
        klijentBox.setModel(new DefaultComboBoxModel<>(klijenti.stream().map(k -> new NameValue(k.getDisplayName(), k)).toArray(NameValue[]::new)));
    }

    public void fillTretmaniBox() {
        fillTretmaniBox(new Tretmani().get(), tretmanBox, kozmeticarBox, vremeBox, getDatum(datumTxt), tretman);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        final JPanel       spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 4;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 3;
        gbc.gridy      = 1;
        gbc.gridheight = 8;
        gbc.weightx    = 3.0;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer2, gbc);
        stanjeLbl = new JLabel();
        stanjeLbl.setText("Stanje:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(stanjeLbl, gbc);
        stanjeBox   = new JComboBox();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 2;
        gbc.gridy   = 1;
        gbc.weightx = 3.0;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(stanjeBox, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Klijent:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label1, gbc);
        klijentBox = new JComboBox();
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        mainPanel.add(klijentBox, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Datum:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label2, gbc);
        datumTxt = new JTextField();
        datumTxt.setText("__.__.____.");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        mainPanel.add(datumTxt, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Vreme:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label3, gbc);
        vremeBox   = new JComboBox();
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        mainPanel.add(vremeBox, gbc);
        final JPanel spacer3 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 7;
        gbc.gridwidth = 3;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 20;
        mainPanel.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 8;
        gbc.weightx = 2.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer4, gbc);
        addBtn = new JButton();
        addBtn.setMaximumSize(new Dimension(78, 50));
        addBtn.setMinimumSize(new Dimension(78, 50));
        addBtn.setPreferredSize(new Dimension(78, 50));
        addBtn.setText("Zakaži");
        gbc       = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        mainPanel.add(addBtn, gbc);
        final JPanel spacer5 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 9;
        gbc.gridwidth = 4;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer5, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Kozmetičar:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label4, gbc);
        kozmeticarBox = new JComboBox();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 4;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(kozmeticarBox, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Tretman:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label5, gbc);
        tretmanBox  = new JComboBox();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 2;
        gbc.gridy   = 2;
        gbc.weightx = 3.0;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(tretmanBox, gbc);
        final JPanel spacer6 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 1;
        gbc.gridy      = 1;
        gbc.gridheight = 6;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 20;
        mainPanel.add(spacer6, gbc);
        stanjeLbl.setLabelFor(stanjeBox);
        label1.setLabelFor(klijentBox);
        label2.setLabelFor(datumTxt);
        label3.setLabelFor(vremeBox);
        label4.setLabelFor(kozmeticarBox);
        label5.setLabelFor(stanjeBox);
    }
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {return mainPanel;}
}
