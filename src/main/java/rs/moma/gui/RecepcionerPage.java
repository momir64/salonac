package rs.moma.gui;

import rs.moma.entities.*;
import rs.moma.gui.helper.NameValue;
import rs.moma.gui.helper.NumericKeyAdapter;
import rs.moma.managers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;

import static rs.moma.helper.DataTools.*;

public class RecepcionerPage extends KalendarForm {
    private JScrollPane          kalendarPane;
    private JTable               kalendarTbl;
    private JTable               tretmaniTbl;
    private JTextField           minCenaTxt;
    private JTextField           maxCenaTxt;
    private JComboBox<NameValue> tretmanBox;
    private JPanel               mainPanel;
    private JButton              dodajBtn;
    private JButton              rightBtn;
    private JLabel               mesecLbl;
    private JButton              leftBtn;
    private JComboBox<NameValue> tipBox;

    public RecepcionerPage(Zaposlen recepcioner, WelcomePage homePage) {
        super(true);

        $$$setupUI$$$();
        setSize(1020, 1108);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(950, 801));
        setTitle(recepcioner.Ime + " " + recepcioner.Prezime);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        minCenaTxt.addKeyListener(new NumericKeyAdapter(true));
        maxCenaTxt.addKeyListener(new NumericKeyAdapter(true));
        tipBox.addActionListener(e -> fillTretmaniBox());
        tretmanBox.addActionListener(e -> updatePage());
        tipBox.addActionListener(e -> updatePage());
        addOnChangeDo(minCenaTxt, this::updatePage);
        addOnChangeDo(maxCenaTxt, this::updatePage);

        super.setup(kalendarPane, tretmaniTbl, 230, kalendarTbl, mesecLbl, rightBtn, leftBtn, true, true);
        updatePage();

        dodajBtn.addActionListener(e -> new RecepcionerZakazivanjeForm(this, null, cellToDate(selectedRow, selectedColumn), this::updatePage));

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                homePage.setVisible(true);
            }
        });

        setVisible(true);
    }

    public ArrayList<ZakazaniTretman> getTretmani() {
        return new ZakazaniTretmani().filter((int) getSelectedValue(tipBox),
                                             (int) getSelectedValue(tretmanBox),
                                             txtToFloat(minCenaTxt), txtToFloat(maxCenaTxt));
    }

    public void updatePage() {
        kalendarTbl.repaint();
        fillTipoviBox();
        fillTretmaniBox();
        fillTretmani();
    }

    public void fillTipoviBox() {
        NameValue                  tipOld           = (NameValue) tipBox.getSelectedItem();
        ArrayList<ZakazaniTretman> zakazaniTretmani = new ZakazaniTretmani().get();
        ArrayList<TipTretmana>     tipovi           = new TipoviTretmana().get();
        Tretmani                   tretmani         = new Tretmani();

        tipovi.removeIf(tip -> zakazaniTretmani.stream().noneMatch(tr -> tretmani.get(tr.TretmanID).TipID == tip.ID));
        tipovi.add(new TipTretmana(-1, ""));
        tipovi.sort(Comparator.comparing(tip -> tip.Tip));
        tipBox.setModel(new DefaultComboBoxModel<>(tipovi.stream().map(tip -> new NameValue(tip.Tip, tip.ID)).toArray(NameValue[]::new)));
        if (tipOld != null && tipovi.stream().anyMatch(tip -> tip.ID == (int) tipOld.getValue()))
            tipBox.setSelectedItem(tipOld);
    }

    public void fillTretmaniBox() {
        NameValue                  tretmanOld       = (NameValue) tretmanBox.getSelectedItem();
        ArrayList<ZakazaniTretman> zakazaniTretmeni = new ZakazaniTretmani().get();
        ArrayList<Tretman>         tretmani         = new Tretmani().filter((int) getSelectedValue(tipBox), null, -1, -1, -1, -1);
        tretmani.removeIf(tretman -> zakazaniTretmeni.stream().noneMatch(tr -> tr.TretmanID == tretman.ID));
        tretmani.add(new Tretman(-1, -1, "", -1, -1));
        tretmani.sort(Comparator.comparing(tip -> tip.Naziv));
        tretmanBox.setModel(new DefaultComboBoxModel<>(tretmani.stream().map(t -> new NameValue(t.Naziv, t.ID)).toArray(NameValue[]::new)));
        if (tretmanOld != null && tretmani.stream().anyMatch(tretman -> tretman.ID == (int) tretmanOld.getValue()))
            tretmanBox.setSelectedItem(tretmanOld);
    }

    protected String[][] tretmanToList(ZakazaniTretman tretman) {
        Zaposlen kozmeticar = new Zaposleni().get(tretman.KozmeticarID);
        Klijent  klijent    = new Klijenti().get(tretman.KlijentID);
        return new String[][]{{}, {"Vreme: ", tretman.Vreme.getHour() + "h"},
                              {"Stanje: ", getStanjeName(tretman.Stanje)},
                              {"Klijent: ", klijent.getDisplayName()},
                              {"Kozmetičar: ", kozmeticar.getDisplayName()},
                              {"Tretman: ", new Tretmani().get(tretman.TretmanID).Naziv},
                              {"Trajanje: ", tretman.Trajanje + " minuta"},
                              {"Plaćeno: ", tretman.getPlaceniIznos() + " RSD"}};
    }

    private void createUIComponents() {
        kalendarTbl = super.makeKalendarTable();
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
        final JPanel       spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 11;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 30;
        mainPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 0;
        gbc.gridy      = 1;
        gbc.gridheight = 5;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 30;
        mainPanel.add(spacer2, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setMinimumSize(new Dimension(300, 474));
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 1;
        gbc.gridwidth = 7;
        gbc.weightx   = 1.0;
        gbc.anchor    = GridBagConstraints.NORTH;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(panel1, gbc);
        kalendarPane = new JScrollPane();
        kalendarPane.setMinimumSize(new Dimension(16, 358));
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 2;
        gbc.gridwidth = 3;
        gbc.anchor    = GridBagConstraints.NORTH;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        panel1.add(kalendarPane, gbc);
        kalendarTbl.setCellSelectionEnabled(true);
        kalendarTbl.setColumnSelectionAllowed(true);
        kalendarTbl.setDragEnabled(false);
        kalendarTbl.setFocusable(false);
        kalendarTbl.setPreferredScrollableViewportSize(new Dimension(300, 300));
        kalendarTbl.setSelectionBackground(new Color(-11833681));
        kalendarTbl.setShowHorizontalLines(true);
        kalendarTbl.setShowVerticalLines(true);
        kalendarPane.setViewportView(kalendarTbl);
        final JPanel spacer3 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 1;
        gbc.gridwidth = 3;
        gbc.anchor    = GridBagConstraints.NORTH;
        gbc.ipady     = 5;
        panel1.add(spacer3, gbc);
        rightBtn = new JButton();
        rightBtn.setFocusPainted(false);
        rightBtn.setIcon(new ImageIcon(getClass().getResource("/right.png")));
        rightBtn.setMinimumSize(new Dimension(38, 38));
        rightBtn.setPreferredSize(new Dimension(38, 38));
        rightBtn.setText("");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        panel1.add(rightBtn, gbc);
        mesecLbl = new JLabel();
        mesecLbl.setHorizontalAlignment(0);
        mesecLbl.setHorizontalTextPosition(0);
        mesecLbl.setMinimumSize(new Dimension(0, 38));
        mesecLbl.setPreferredSize(new Dimension(0, 38));
        mesecLbl.setText("Januar");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 1;
        gbc.gridy   = 0;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        panel1.add(mesecLbl, gbc);
        leftBtn = new JButton();
        leftBtn.setFocusPainted(false);
        leftBtn.setIcon(new ImageIcon(getClass().getResource("/left.png")));
        leftBtn.setMinimumSize(new Dimension(38, 38));
        leftBtn.setPreferredSize(new Dimension(38, 38));
        leftBtn.setText("");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(leftBtn, gbc);
        final JPanel spacer4 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 8;
        gbc.gridy      = 1;
        gbc.gridheight = 5;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 30;
        mainPanel.add(spacer4, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel2.setMinimumSize(new Dimension(300, 57));
        gbc            = new GridBagConstraints();
        gbc.gridx      = 9;
        gbc.gridy      = 1;
        gbc.gridheight = 8;
        gbc.weightx    = 2.0;
        gbc.weighty    = 1.0;
        gbc.fill       = GridBagConstraints.BOTH;
        mainPanel.add(panel2, gbc);
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setMinimumSize(new Dimension(58, 38));
        label1.setPreferredSize(new Dimension(58, 38));
        label1.setText("Tretmani");
        gbc       = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        panel2.add(label1, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        panel2.add(scrollPane1, gbc);
        tretmaniTbl = new JTable();
        tretmaniTbl.setFocusable(false);
        tretmaniTbl.setShowHorizontalLines(true);
        tretmaniTbl.setShowVerticalLines(true);
        scrollPane1.setViewportView(tretmaniTbl);
        final JPanel spacer5 = new JPanel();
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.ipady  = 5;
        panel2.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 10;
        gbc.gridy      = 1;
        gbc.gridheight = 5;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 30;
        mainPanel.add(spacer6, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Tip:  ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label2, gbc);
        final JPanel spacer7 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 9;
        gbc.gridwidth = 11;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 30;
        mainPanel.add(spacer7, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Tretman:  ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label3, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Plaćeno:  ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill   = GridBagConstraints.VERTICAL;
        gbc.ipady  = 30;
        mainPanel.add(label4, gbc);
        minCenaTxt  = new JTextField();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 4;
        gbc.gridy   = 5;
        gbc.weightx = 1.0;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(minCenaTxt, gbc);
        final JLabel label5 = new JLabel();
        label5.setText(" do ");
        gbc       = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 5;
        gbc.fill  = GridBagConstraints.BOTH;
        mainPanel.add(label5, gbc);
        maxCenaTxt  = new JTextField();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 6;
        gbc.gridy   = 5;
        gbc.weightx = 1.0;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(maxCenaTxt, gbc);
        final JLabel label6 = new JLabel();
        label6.setText(" RSD");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 7;
        gbc.gridy  = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label6, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("od ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 3;
        gbc.gridy  = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label7, gbc);
        tretmanBox    = new JComboBox();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 3;
        gbc.gridy     = 4;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(tretmanBox, gbc);
        tipBox        = new JComboBox();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 3;
        gbc.gridy     = 3;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(tipBox, gbc);
        final JPanel spacer8 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 2;
        gbc.gridy      = 3;
        gbc.gridheight = 3;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 10;
        mainPanel.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 8;
        gbc.gridwidth = 7;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer9, gbc);
        dodajBtn = new JButton();
        dodajBtn.setMaximumSize(new Dimension(156, 50));
        dodajBtn.setMinimumSize(new Dimension(156, 50));
        dodajBtn.setPreferredSize(new Dimension(156, 50));
        dodajBtn.setText("Zakaži novi tretman");
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 7;
        gbc.gridwidth = 7;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(dodajBtn, gbc);
        final JPanel spacer10 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 6;
        gbc.gridwidth = 7;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 15;
        mainPanel.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 2;
        gbc.gridwidth = 7;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 15;
        mainPanel.add(spacer11, gbc);
        label2.setLabelFor(tipBox);
        label3.setLabelFor(tretmanBox);
        label5.setLabelFor(maxCenaTxt);
        label7.setLabelFor(minCenaTxt);
    }
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {return mainPanel;}
}
