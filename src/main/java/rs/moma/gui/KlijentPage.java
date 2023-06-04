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
        karticaLbl.setText((klijent.getKarticaLojalnosti() ? "I" : "Ne ") + "spunjavate uslov za karticu lojalnosti.");
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
}
