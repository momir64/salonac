package rs.moma.gui;

import rs.moma.entities.Zaposlen;

import javax.swing.*;
import java.awt.*;

public class MenadzerPage extends JFrame {
    private JPanel      mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel      bazaPanel;
    private JPanel      finansijePanel;
    private JPanel      karticaLojalnostiPanel;
    private JPanel      izvestajiPanel;
    private JPanel      dijagramiPanel;
    private JTable      table1;
    private JComboBox   comboBox1;
    private JButton     sacuvajPraviloButton;
    private JButton     obrisiPraviloButton;
    private JTextField  textField1;
    private JComboBox   comboBox2;
    private JTextField  textField2;
    private JTextField  textField3;
    private JButton     prikaziButton;
    private JList       list1;
    private JTextField  textField4;
    private JButton     generisiListuKlijenataSaButton;
    private JTable      table2;
    private JComboBox   comboBox3;
    private JButton     dodajButton;
    private JButton     izmeniButton;
    private JButton     obrisiButton;
    private JTable      table3;

    public MenadzerPage(Zaposlen menadzer) {
        setSize(1300, 950);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(950, 801));
        setTitle(menadzer.Ime + " " + menadzer.Prezime);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//        minCenaTxt.addKeyListener(new NumericKeyAdapter(this));
//        maxCenaTxt.addKeyListener(new NumericKeyAdapter(this));
//        tipBox.addActionListener(e -> fillTretmaniBox());
//        tretmanBox.addActionListener(e -> updatePage());
//        tipBox.addActionListener(e -> updatePage());
//        addOnChangeDo(minCenaTxt, this::updatePage);
//        addOnChangeDo(maxCenaTxt, this::updatePage);
//
//        super.setup(kalendarPane, tretmaniTbl, 230, kalendarTbl, mesecLbl, rightBtn, leftBtn, true, true);
//        updatePage();
//
//        dodajBtn.addActionListener(e -> new RecepcionerZakazivanjeForm(this, null, cellToDate(selectedRow, selectedColumn), this::updatePage));

        setVisible(true);
    }
}
