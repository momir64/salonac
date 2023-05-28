package rs.moma.gui;

import rs.moma.entities.ZakazaniTretman;
import rs.moma.managers.Zaposleni;
import rs.moma.entities.Zaposlen;
import rs.moma.managers.Tretmani;
import rs.moma.entities.Klijent;
import rs.moma.entities.Salon;

import javax.swing.*;
import java.awt.*;

public class KlijentPage extends KalendarForm {
    private JScrollPane kalendarPane;
    private JTable      tretmaniTbl;
    private JTable      kalendarTbl;
    private JPanel      mainPanel;
    private JLabel      mesecLbl;
    private JButton     rightBtn;
    private JButton     leftBtn;
    private JLabel      karticaLbl;
    private JLabel      ukupnoLbl;
    private JButton     dodajBtn;

    public KlijentPage(Klijent klijent) {
        super(klijent.getSviZakazaniTretmani());

        setSize(1000, 988);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(750, 800));
        setTitle(klijent.Ime + " " + klijent.Prezime);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ukupnoLbl.setText(klijent.getUkupnoPlatio() + " RSD");
        karticaLbl.setText((klijent.getKarticaLojalnosti(new Salon().MinIznosLojalnosti) ? "I" : "Ne ") + "spunjavate uslov za karticu lojalnosti.");
        super.setup(kalendarPane, tretmaniTbl, kalendarTbl, mesecLbl, rightBtn, leftBtn);

        setVisible(true);
    }

    protected String[][] tretman2list(ZakazaniTretman tretman) {
        Zaposlen kozmeticar = new Zaposleni().get(tretman.KozmeticarID);
        return new String[][]{{}, {"Vreme: ", tretman.Vreme.getHour() + "h"},
                              {"Stanje: ", tretman.Stanje.toString()},
                              {"Kozmetičar: ", kozmeticar.Ime + " " + kozmeticar.Prezime},
                              {"Tretman: ", new Tretmani().get(tretman.TretmanID).Naziv},
                              {"Trajanje: ", tretman.Trajanje + " minuta"},
                              {"Cena: ", tretman.getPlaceniIznos() + " RSD"}};
    }

    private void createUIComponents() {
        kalendarTbl = super.makeKalendarTable();
    }
}
