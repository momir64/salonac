package rs.moma.gui;

import rs.moma.entities.*;
import rs.moma.gui.helper.NameValue;
import rs.moma.gui.helper.DateKeyAdapter;
import rs.moma.gui.helper.NumericKeyAdapter;
import rs.moma.managers.TipoviTretmana;
import rs.moma.managers.Tretmani;
import rs.moma.managers.ZakazaniTretmani;
import rs.moma.managers.Zaposleni;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static rs.moma.helper.DataTools.*;

public class KlijentZakazivanjeForm extends ZakazivanjeForm {
    private JTextField minTrajanjeTxt;
    private JTextField maxTrajanjeTxt;
    private JComboBox<NameValue> kozmeticarBox;
    private JTextField           minCenaTxt;
    private JTextField           maxCenaTxt;
    private JComboBox<NameValue> tretmanBox;
    private JPanel               mainPanel;
    private JTextField           datumTxt;
    private JComboBox<NameValue> vremeBox;
    private JComboBox<NameValue> tipBox;
    private JButton              addBtn;

    public KlijentZakazivanjeForm(JFrame parent, Klijent klijent, LocalDate date, Runnable update) {
        super(parent, "Zakazivanje tretmana", true);
        setSize(655, 655);
        setContentPane(mainPanel);
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(510, 550));

        ArrayList<Zaposlen>    zaposleni = new Zaposleni().get();
        ArrayList<TipTretmana> tipovi    = new TipoviTretmana().get();
        tipovi.removeIf(tip -> zaposleni.stream().noneMatch(zaposlen -> Arrays.stream(zaposlen.ZaduzeniTretmani).anyMatch(tretmanID -> tretmanID != -1 && new Tretmani().get(tretmanID).TipID == tip.ID)));
        tipovi.add(new TipTretmana(-1, ""));
        tipovi.sort(Comparator.comparing(tip -> tip.Tip));
        tipBox.setModel(new DefaultComboBoxModel(tipovi.stream().map(tip -> new NameValue(tip.Tip, tip.ID)).toArray()));
        tretmanBox.addActionListener(e -> fillKozmeticariBox(tretmanBox, kozmeticarBox, vremeBox, getDatum(datumTxt), null));
        kozmeticarBox.addActionListener(e -> fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), null));
        addOnChangeDo(datumTxt, () -> fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), null));
        tipBox.addActionListener(e -> fillTretmaniBox());
        if (date == null) date = LocalDate.now();
        datumTxt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        addBtn.addActionListener(e -> zakaziTermin(klijent, update));
        datumTxt.addKeyListener(new DateKeyAdapter());
        minTrajanjeTxt.addKeyListener(new NumericKeyAdapter(false));
        maxTrajanjeTxt.addKeyListener(new NumericKeyAdapter(false));
        minCenaTxt.addKeyListener(new NumericKeyAdapter(true));
        maxCenaTxt.addKeyListener(new NumericKeyAdapter(true));
        addOnChangeDo(minTrajanjeTxt, this::fillTretmaniBox);
        addOnChangeDo(maxTrajanjeTxt, this::fillTretmaniBox);
        addOnChangeDo(minCenaTxt, this::fillTretmaniBox);
        addOnChangeDo(maxCenaTxt, this::fillTretmaniBox);
        fillTretmaniBox();

        setVisible(true);
    }

    public void zakaziTermin(Klijent klijent, Runnable update) {
        if (vremeBox.getItemCount() == 0) return;
        new ZakazaniTretmani().add(new ZakazaniTretman((Tretman) getSelectedValue(tretmanBox),
                                                       getTermin(datumTxt, vremeBox), klijent, (Zaposlen) getSelectedValue(kozmeticarBox)));
        fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), null);
        update.run();
    }

    public void fillTretmaniBox() {
        NameValue tretmanOld = (NameValue) tretmanBox.getSelectedItem();
        ArrayList<Tretman> tretmani = new Tretmani().filter((int) getSelectedValue(tipBox), null,
                                                            txtToInt(minTrajanjeTxt), txtToInt(maxTrajanjeTxt), txtToFloat(minCenaTxt), txtToFloat(maxCenaTxt));
        fillTretmaniBox(tretmani, tretmanBox, kozmeticarBox, vremeBox, getDatum(datumTxt), null);
        if (tretmanOld != null && tretmani.stream().anyMatch(tretman -> tretman.ID == ((Tretman) tretmanOld.getValue()).ID))
            tretmanBox.setSelectedItem(tretmanOld);
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
        final JLabel label1 = new JLabel();
        label1.setText("Tip:");
        GridBagConstraints gbc;
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 1;
        gbc.gridy      = 1;
        gbc.gridheight = 7;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 20;
        mainPanel.add(spacer1, gbc);
        tipBox        = new JComboBox();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 1;
        gbc.gridwidth = 5;
        gbc.weightx   = 1.0;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(tipBox, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Trajanje:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label2, gbc);
        minTrajanjeTxt = new JTextField();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 3;
        gbc.gridy      = 2;
        gbc.weightx    = 1.0;
        gbc.anchor     = GridBagConstraints.WEST;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        mainPanel.add(minTrajanjeTxt, gbc);
        final JLabel label3 = new JLabel();
        label3.setText(" do ");
        gbc       = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill  = GridBagConstraints.BOTH;
        mainPanel.add(label3, gbc);
        maxTrajanjeTxt = new JTextField();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 5;
        gbc.gridy      = 2;
        gbc.weightx    = 1.0;
        gbc.anchor     = GridBagConstraints.WEST;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        mainPanel.add(maxTrajanjeTxt, gbc);
        final JLabel label4 = new JLabel();
        label4.setText(" minuta");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 6;
        gbc.gridy  = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label4, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Cena:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label5, gbc);
        minCenaTxt  = new JTextField();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 3;
        gbc.gridy   = 3;
        gbc.weightx = 1.0;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(minCenaTxt, gbc);
        final JLabel label6 = new JLabel();
        label6.setText(" do ");
        gbc       = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.fill  = GridBagConstraints.BOTH;
        mainPanel.add(label6, gbc);
        maxCenaTxt  = new JTextField();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 5;
        gbc.gridy   = 3;
        gbc.weightx = 1.0;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(maxCenaTxt, gbc);
        final JLabel label7 = new JLabel();
        label7.setText(" RSD");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 6;
        gbc.gridy  = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label7, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Tretman:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label8, gbc);
        tretmanBox    = new JComboBox();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 4;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(tretmanBox, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Kozmetičar:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label9, gbc);
        kozmeticarBox = new JComboBox();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 5;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(kozmeticarBox, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("Datum:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label10, gbc);
        datumTxt = new JTextField();
        datumTxt.setText("__.__.____.");
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 6;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(datumTxt, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Vreme:");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 7;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label11, gbc);
        final JLabel label12 = new JLabel();
        label12.setText("od ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label12, gbc);
        final JLabel label13 = new JLabel();
        label13.setText("od ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label13, gbc);
        final JPanel spacer2 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 7;
        gbc.gridy      = 1;
        gbc.gridheight = 9;
        gbc.weightx    = 3.0;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 10;
        gbc.gridwidth = 8;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 8;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer4, gbc);
        addBtn = new JButton();
        addBtn.setMaximumSize(new Dimension(78, 50));
        addBtn.setMinimumSize(new Dimension(78, 50));
        addBtn.setPreferredSize(new Dimension(78, 50));
        addBtn.setText("Zakaži");
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 9;
        gbc.gridwidth = 5;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(addBtn, gbc);
        final JPanel spacer5 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 8;
        gbc.gridwidth = 7;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 20;
        mainPanel.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 9;
        gbc.weightx = 2.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer6, gbc);
        vremeBox      = new JComboBox();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 7;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(vremeBox, gbc);
        label1.setLabelFor(tipBox);
        label3.setLabelFor(maxTrajanjeTxt);
        label6.setLabelFor(maxCenaTxt);
        label8.setLabelFor(tretmanBox);
        label9.setLabelFor(kozmeticarBox);
        label10.setLabelFor(datumTxt);
        label11.setLabelFor(vremeBox);
        label12.setLabelFor(minTrajanjeTxt);
        label13.setLabelFor(minCenaTxt);
    }
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {return mainPanel;}
}
