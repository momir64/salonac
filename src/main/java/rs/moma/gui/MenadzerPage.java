package rs.moma.gui;

import rs.moma.entities.Zaposlen;

import javax.swing.*;
import java.awt.*;

public class MenadzerPage extends JFrame {
    private JPanel      mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel      bazaPanel;
    private JPanel finansijePanel;
    private JPanel karticaLojalnostiPanel;
    private JPanel izvestajiPanel;
    private JPanel dijagramiPanel;

    public MenadzerPage(Zaposlen menadzer) {
        setSize(1020, 1108);
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
