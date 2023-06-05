package rs.moma.gui;

import rs.moma.entities.Klijent;
import rs.moma.entities.ZakazaniTretman;
import rs.moma.entities.Zaposlen;
import rs.moma.managers.Tretmani;
import rs.moma.managers.Zaposleni;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static rs.moma.helper.DataTools.getStanjeName;

public class KlijentPage extends KalendarForm {
    private       JScrollPane kalendarPane;
    private       JTable      tretmaniTbl;
    private       JTable      kalendarTbl;
    private       JPanel      mainPanel;
    private       JLabel      mesecLbl;
    private       JButton     rightBtn;
    private       JButton     leftBtn;
    private       JLabel      karticaLbl;
    private       JLabel      ukupnoLbl;
    private       JButton     dodajBtn;
    private final Klijent     klijent;

    public KlijentPage(Klijent klijent, WelcomePage homePage) {
        super(true);
        this.klijent = klijent;

        $$$setupUI$$$();
        setSize(1000, 988);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(750, 800));
        setTitle(klijent.Ime + " " + klijent.Prezime);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        super.setup(kalendarPane, tretmaniTbl, 200, kalendarTbl, mesecLbl, rightBtn, leftBtn, true, false);
        updatePage();

        dodajBtn.addActionListener(e -> new KlijentZakazivanjeForm(this, klijent, cellToDate(selectedRow, selectedColumn), this::updatePage));

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                homePage.setVisible(true);
            }
        });

        setVisible(true);
    }

    public void updatePage() {
        karticaLbl.setText((klijent.getKarticaLojalnosti() ? "I" : "Ne i") + "spunjavate uslov za karticu lojalnosti.");
        ukupnoLbl.setText(klijent.getUkupnoPlatio() + " RSD");
        fillTretmani();
    }

    protected ArrayList<ZakazaniTretman> getTretmani() {
        return klijent.getSviZakazaniTretmani();
    }

    protected String[][] tretmanToList(ZakazaniTretman tretman) {
        Zaposlen kozmeticar = new Zaposleni().get(tretman.KozmeticarID);
        return new String[][]{{}, {"Vreme: ", tretman.Vreme.getHour() + "h"},
                              {"Stanje: ", getStanjeName(tretman.Stanje)},
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
        gbc.gridwidth = 6;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 30;
        mainPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 0;
        gbc.gridy      = 1;
        gbc.gridheight = 4;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 30;
        mainPanel.add(spacer2, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setMinimumSize(new Dimension(300, 474));
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 1;
        gbc.gridwidth = 2;
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
        dodajBtn = new JButton();
        dodajBtn.setMaximumSize(new Dimension(156, 50));
        dodajBtn.setMinimumSize(new Dimension(156, 50));
        dodajBtn.setPreferredSize(new Dimension(156, 50));
        dodajBtn.setText("Zakaži novi tretman");
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 4;
        gbc.gridwidth = 3;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        panel1.add(dodajBtn, gbc);
        final JPanel spacer4 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 3;
        gbc.gridwidth = 3;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 5;
        panel1.add(spacer4, gbc);
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
        final JPanel spacer5 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 3;
        gbc.gridy      = 1;
        gbc.gridheight = 4;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 30;
        mainPanel.add(spacer5, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel2.setMinimumSize(new Dimension(300, 57));
        gbc            = new GridBagConstraints();
        gbc.gridx      = 4;
        gbc.gridy      = 1;
        gbc.gridheight = 4;
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
        final JPanel spacer6 = new JPanel();
        gbc        = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.ipady  = 5;
        panel2.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 5;
        gbc.gridy      = 1;
        gbc.gridheight = 4;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 30;
        mainPanel.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 5;
        gbc.gridwidth = 6;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 30;
        mainPanel.add(spacer8, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Ukupan trošak:  ");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label2, gbc);
        karticaLbl = new JLabel();
        karticaLbl.setText("Ispunjavate uslov za karticu lojalnosti.");
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 4;
        gbc.gridwidth = 2;
        gbc.weightx   = 1.0;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(karticaLbl, gbc);
        ukupnoLbl = new JLabel();
        ukupnoLbl.setText("0 RSD");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(ukupnoLbl, gbc);
        final JPanel spacer9 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 2;
        gbc.gridwidth = 2;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer9, gbc);
        mesecLbl.setLabelFor(kalendarPane);
        label1.setLabelFor(scrollPane1);
    }
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {return mainPanel;}
}
