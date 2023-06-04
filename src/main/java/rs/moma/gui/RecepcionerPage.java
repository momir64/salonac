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
}
