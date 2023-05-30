package rs.moma.gui;

import rs.moma.entities.ZakazaniTretman;
import rs.moma.entities.Zaposlen;
import rs.moma.managers.Klijenti;
import rs.moma.managers.Tretmani;
import rs.moma.entities.Klijent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class KozmeticarPage extends KalendarForm {
    private JList<String> tretmaniList;
    private JScrollPane   kalendarPane;
    private JTable        kalendarTbl;
    private JTable        tretmaniTbl;
    private JPanel        mainPanel;
    private JLabel        mesecLbl;
    private JButton       rightBtn;
    private       JButton  leftBtn;
    private final Zaposlen kozmeticar;

    public KozmeticarPage(Zaposlen kozmeticar) {
        super(false);
        this.kozmeticar = kozmeticar;

        setSize(1050, 1000);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(750, 800));
        setTitle(kozmeticar.Ime + " " + kozmeticar.Prezime);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        tretmaniList.setSelectionModel(new NoSelectionModel());
        tretmaniList.setListData(kozmeticar.getTretmani().stream().map(tretman -> tretman.Naziv).sorted().toArray(String[]::new));
        super.setup(kalendarPane, tretmaniTbl, 200, kalendarTbl, mesecLbl, rightBtn, leftBtn, false, false);

        setVisible(true);
    }

    protected ArrayList<ZakazaniTretman> getTretmani() {
        return kozmeticar.getZakazaniTretmani();
    }

    protected String[][] tretmanToList(ZakazaniTretman tretman) {
        Klijent klijent = new Klijenti().get(tretman.KlijentID);
        return new String[][]{{}, {"Vreme: ", tretman.Vreme.getHour() + "h"},
                              {"Klijent: ", klijent.getDisplayName()},
                              {"Tretman: ", new Tretmani().get(tretman.TretmanID).Naziv},
                              {"Trajanje: ", tretman.Trajanje + " minuta"}};
    }

    @Override
    protected void updatePage() {}

    private void createUIComponents() {
        kalendarTbl = super.makeKalendarTable();
    }
}
