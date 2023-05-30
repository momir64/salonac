package rs.moma.gui;

import rs.moma.entities.*;
import rs.moma.managers.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

import static rs.moma.DataTools.*;

public class RecepcionerPage extends KalendarForm {
    private JScrollPane              kalendarPane;
    private JTable                   kalendarTbl;
    private JTable                   tretmaniTbl;
    private JTextField               minCenaTxt;
    private JTextField               maxCenaTxt;
    private JComboBox<ComboKeyValue> tretmanBox;
    private JPanel                   mainPanel;
    private JButton                  dodajBtn;
    private JButton                  rightBtn;
    private JLabel                   mesecLbl;
    private JButton                  leftBtn;
    private JComboBox<ComboKeyValue> tipBox;

    public RecepcionerPage(Zaposlen recepcioner) {
        super(true);

        setSize(1020, 1108);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(950, 801));
        setTitle(recepcioner.Ime + " " + recepcioner.Prezime);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        minCenaTxt.addKeyListener(new NumericKeyAdapter(this));
        maxCenaTxt.addKeyListener(new NumericKeyAdapter(this));
        tipBox.addActionListener(e -> fillTretmaniBox());
        tretmanBox.addActionListener(e -> updatePage());
        tipBox.addActionListener(e -> updatePage());
        addOnChangeDo(minCenaTxt, this::updatePage);
        addOnChangeDo(maxCenaTxt, this::updatePage);

        super.setup(kalendarPane, tretmaniTbl, 230, kalendarTbl, mesecLbl, rightBtn, leftBtn, true, true);
        updatePage();

        dodajBtn.addActionListener(e -> new RecepcionerZakazivanjeForm(this, null, cellToDate(selectedRow, selectedColumn), this::updatePage));

        setVisible(true);
    }

    public ArrayList<ZakazaniTretman> getTretmani() {
        return new ZakazaniTretmani().filter((int) getSelectedValue(tipBox),
                                             (int) getSelectedValue(tretmanBox),
                                             txtToInt(minCenaTxt), txtToInt(maxCenaTxt));
    }

    public void updatePage() {
        kalendarTbl.repaint();
        fillTipoviBox();
        fillTretmaniBox();
        fillTretmani();
    }

    public void fillTipoviBox() {
        ComboKeyValue              tipOld   = (ComboKeyValue) tipBox.getSelectedItem();
        ArrayList<TipTretmana>     tipovi   = new TipoviTretmana().get();
        ArrayList<ZakazaniTretman> tretmani = new ZakazaniTretmani().get();
        tipovi.removeIf(tip -> tretmani.stream().noneMatch(tr -> new Tretmani().get(tr.TretmanID).TipID == tip.ID));
        tipovi.add(new TipTretmana(-1, ""));
        tipovi.sort(Comparator.comparing(tip -> tip.Tip));
        tipBox.setModel(new DefaultComboBoxModel(tipovi.stream().map(tip -> new ComboKeyValue(tip.Tip, tip.ID)).toArray()));
        if (tipOld != null && tipovi.stream().anyMatch(tip -> tip.ID == (int) tipOld.getValue()))
            tipBox.setSelectedItem(tipOld);
    }

    public void fillTretmaniBox() {
        ComboKeyValue              tretmanOld       = (ComboKeyValue) tretmanBox.getSelectedItem();
        ArrayList<ZakazaniTretman> zakazaniTretmeni = new ZakazaniTretmani().get();
        ArrayList<Tretman>         tretmani         = new Tretmani().filter((int) getSelectedValue(tipBox), null, -1, -1, -1, -1);
        tretmani.removeIf(tretman -> zakazaniTretmeni.stream().noneMatch(tr -> tr.TretmanID == tretman.ID));
        tretmani.add(new Tretman(-1, -1, "", -1, -1));
        tretmani.sort(Comparator.comparing(tip -> tip.Naziv));
        tretmanBox.setModel(new DefaultComboBoxModel(tretmani.stream().map(t -> new ComboKeyValue(t.Naziv, t.ID)).toArray()));
        if (tretmanOld != null && tretmani.stream().anyMatch(tretman -> tretman.ID == (int) tretmanOld.getValue()))
            tretmanBox.setSelectedItem(tretmanOld);
    }

    protected String[][] tretmanToList(ZakazaniTretman tretman) {
        Zaposlen kozmeticar = new Zaposleni().get(tretman.KozmeticarID);
        Klijent  klijent    = new Klijenti().get(tretman.KlijentID);
        return new String[][]{{}, {"Vreme: ", tretman.Vreme.getHour() + "h"},
                              {"Stanje: ", tretman.Stanje.toString().replace("_", " ")},
                              {"Klijent: ", klijent.getDisplayName()},
                              {"Kozmetičar: ", kozmeticar.getDisplayName()},
                              {"Tretman: ", new Tretmani().get(tretman.TretmanID).Naziv},
                              {"Trajanje: ", tretman.Trajanje + " minuta"},
                              {"Plaćeno: ", tretman.getPlaceniIznos() + " RSD"}};
    }

    private void createUIComponents() {
        kalendarTbl = super.makeKalendarTable();
    }
}
