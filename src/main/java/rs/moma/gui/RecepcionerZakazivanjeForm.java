package rs.moma.gui;

import rs.moma.entities.*;
import rs.moma.gui.helper.NameValue;
import rs.moma.gui.helper.DateKeyAdapter;
import rs.moma.helper.ClassWithID;
import rs.moma.managers.Klijenti;
import rs.moma.managers.Tretmani;
import rs.moma.managers.ZakazaniTretmani;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static rs.moma.helper.DataTools.*;
import static rs.moma.helper.DataTools.EStanjeTermina.*;

public class RecepcionerZakazivanjeForm extends ZakazivanjeForm {
    private       JComboBox<NameValue> kozmeticarBox;
    private       JComboBox<NameValue> klijentBox;
    private       JComboBox<NameValue> tretmanBox;
    private       JComboBox<NameValue> stanjeBox;
    private       JComboBox<NameValue> vremeBox;
    private       JLabel               stanjeLbl;
    private       JPanel               mainPanel;
    private       JTextField           datumTxt;
    private final ZakazaniTretman      tretman;
    private       JButton              addBtn;

    public RecepcionerZakazivanjeForm(JFrame parent, ZakazaniTretman tretman, LocalDate date, Runnable update) {
        super(parent, "Zakazivanje tretmana", true);
        this.tretman = tretman;

        setSize(655, 655);
        setContentPane(mainPanel);
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(510, 550));

        if (tretman == null) {
            remove(stanjeBox);
            remove(stanjeLbl);
        } else {
            addBtn.setText("Izmeni");
            ArrayList<EStanjeTermina> stanja = new ArrayList<>(Arrays.asList(OTKAZAO_KLIJENT, OTKAZAO_SALON));
            if (tretman.Vreme.isAfter(LocalDateTime.now()))
                stanja.add(ZAKAZAN);
            else
                stanja.addAll(Arrays.asList(IZVRSEN, NIJE_SE_POJAVIO));
            stanja.sort(Comparator.comparing(Enum::toString));
            stanjeBox.setModel(new DefaultComboBoxModel<>(stanja.stream().map(stanje -> new NameValue(getStanjeName(stanje), stanje)).toArray(NameValue[]::new)));
            stanjeBox.setSelectedItem(new NameValue("", tretman.Stanje));
        }

        tretmanBox.addActionListener(e -> fillKozmeticariBox(tretmanBox, kozmeticarBox, vremeBox, getDatum(datumTxt), tretman));
        kozmeticarBox.addActionListener(e -> fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), tretman));
        date = tretman != null ? tretman.Vreme.toLocalDate() : date == null ? LocalDate.now() : date;
        datumTxt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        addBtn.addActionListener(e -> {
            if (tretman == null) zakaziTermin(update);
            else editTermin(tretman, update);
        });
        datumTxt.addKeyListener(new DateKeyAdapter());
        addOnChangeDo(datumTxt, () -> fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), tretman));
        fillKlijentiBox();
        fillTretmaniBox();
        selectOptionsIfNotNull();

        setVisible(true);
    }

    public void zakaziTermin(Runnable update) {
        if (vremeBox.getItemCount() == 0) return;
        new ZakazaniTretmani().add(new ZakazaniTretman((Tretman) getSelectedValue(tretmanBox),
                                                       getTermin(datumTxt, vremeBox), (Klijent) getSelectedValue(klijentBox),
                                                       (Zaposlen) getSelectedValue(kozmeticarBox)));
        fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), tretman);
        update.run();
    }

    public void editTermin(ZakazaniTretman oldTretman, Runnable update) {
        if (vremeBox.getItemCount() == 0) return;
        new ZakazaniTretmani().edit(oldTretman, new ZakazaniTretman((Tretman) getSelectedValue(tretmanBox),
                                                                    getTermin(datumTxt, vremeBox), (Klijent) getSelectedValue(klijentBox),
                                                                    (Zaposlen) getSelectedValue(kozmeticarBox)));
        fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), tretman);
        update.run();
    }

    public void selectOptionsIfNotNull() {
        if (tretman == null) return;
        selectBoxOption(tretmanBox, tretman.TretmanID);
        selectBoxOption(klijentBox, tretman.KlijentID);
        selectBoxOption(kozmeticarBox, tretman.KozmeticarID);
        vremeBox.setSelectedItem(new NameValue("", tretman.Vreme.getHour()));
    }

    public void selectBoxOption(JComboBox<NameValue> box, int id) {
        for (int i = 0; i < box.getItemCount(); i++) {
            Object value = box.getItemAt(i).getValue();
            if (value instanceof ClassWithID && ((ClassWithID) value).getID() == id) {
                box.setSelectedIndex(i);
                break;
            }
        }
    }

    public void fillKlijentiBox() {
        ArrayList<Klijent> klijenti = new Klijenti().get();
        klijenti.sort(Comparator.comparing(tip -> tip.Ime));
        klijentBox.setModel(new DefaultComboBoxModel<>(klijenti.stream().map(k -> new NameValue(k.getDisplayName(), k)).toArray(NameValue[]::new)));
    }

    public void fillTretmaniBox() {
        fillTretmaniBox(new Tretmani().get(), tretmanBox, kozmeticarBox, vremeBox, getDatum(datumTxt), tretman);
    }

}
