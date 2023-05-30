package rs.moma.gui;

import rs.moma.entities.*;
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

import static rs.moma.DataTools.*;

public class KlijentZakazivanjeForm extends ZakazivanjeForm {
    private JTextField               minTrajanjeTxt;
    private JTextField               maxTrajanjeTxt;
    private JComboBox<ComboKeyValue> kozmeticarBox;
    private JTextField               minCenaTxt;
    private JTextField               maxCenaTxt;
    private JComboBox<ComboKeyValue> tretmanBox;
    private JPanel                   mainPanel;
    private JTextField               datumTxt;
    private JComboBox<ComboKeyValue> vremeBox;
    private JComboBox<ComboKeyValue> tipBox;
    private JButton                  addBtn;

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
        tipBox.setModel(new DefaultComboBoxModel(tipovi.stream().map(tip -> new ComboKeyValue(tip.Tip, tip.ID)).toArray()));
        tretmanBox.addActionListener(e -> fillKozmeticariBox(tretmanBox, kozmeticarBox, vremeBox, getDatum(datumTxt), null));
        kozmeticarBox.addActionListener(e -> fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), null));
        addOnChangeDo(datumTxt, () -> fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), null));
        tipBox.addActionListener(e -> fillTretmaniBox());
        if (date == null) date = LocalDate.now();
        datumTxt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        addBtn.addActionListener(e -> zakaziTermin(klijent, update));
        datumTxt.addKeyListener(new DateKeyAdapter(this, datumTxt));
        minTrajanjeTxt.addKeyListener(new NumericKeyAdapter(this));
        maxTrajanjeTxt.addKeyListener(new NumericKeyAdapter(this));
        minCenaTxt.addKeyListener(new NumericKeyAdapter(this));
        maxCenaTxt.addKeyListener(new NumericKeyAdapter(this));
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
        ComboKeyValue tretmanOld = (ComboKeyValue) tretmanBox.getSelectedItem();
        ArrayList<Tretman> tretmani = new Tretmani().filter((int) getSelectedValue(tipBox), null,
                                                            txtToInt(minTrajanjeTxt), txtToInt(maxTrajanjeTxt), txtToInt(minCenaTxt), txtToInt(maxCenaTxt));
        fillTretmaniBox(tretmani, tretmanBox, kozmeticarBox, vremeBox, getDatum(datumTxt), null);
        if (tretmanOld != null && tretmani.stream().anyMatch(tretman -> tretman.ID == ((Tretman) tretmanOld.getValue()).ID))
            tretmanBox.setSelectedItem(tretmanOld);
    }
}
