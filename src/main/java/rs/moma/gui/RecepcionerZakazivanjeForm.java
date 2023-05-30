package rs.moma.gui;

import rs.moma.entities.*;
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

import static rs.moma.DataTools.*;
import static rs.moma.DataTools.EStanjeTermina.*;

public class RecepcionerZakazivanjeForm extends ZakazivanjeForm {
    private JComboBox<ComboKeyValue> kozmeticarBox;
    private JComboBox<ComboKeyValue> klijentBox;
    private JComboBox<ComboKeyValue> tretmanBox;
    private JComboBox<ComboKeyValue> stanjeBox;
    private JComboBox<ComboKeyValue> vremeBox;
    private JPanel                   mainPanel;
    private JTextField               datumTxt;
    private JButton                  addBtn;
    private       JLabel          stanjeLbl;
    private final ZakazaniTretman tretman;

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
            stanjeBox.setModel(new DefaultComboBoxModel(stanja.stream().map(stanje -> new ComboKeyValue(stanje.toString().replace("_", " "), stanje)).toArray()));
            stanjeBox.setSelectedItem(new ComboKeyValue("", tretman.Stanje));
        }

        tretmanBox.addActionListener(e -> fillKozmeticariBox(tretmanBox, kozmeticarBox, vremeBox, getDatum(datumTxt), tretman));
        kozmeticarBox.addActionListener(e -> fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, getDatum(datumTxt), tretman));
        date = tretman != null ? tretman.Vreme.toLocalDate() : date == null ? LocalDate.now() : date;
        datumTxt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        addBtn.addActionListener(e -> {
            if (tretman == null) zakaziTermin(update);
            else editTermin(tretman, update);
        });
        datumTxt.addKeyListener(new DateKeyAdapter(this, datumTxt));
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
        vremeBox.setSelectedItem(new ComboKeyValue("", tretman.Vreme.getHour()));
    }

    public <T extends ClassWithID> void selectBoxOption(JComboBox<ComboKeyValue> box, int id) {
        for (int i = 0; i < box.getItemCount(); i++)
            if (((T) box.getItemAt(i).getValue()).getID() == id) {
                box.setSelectedIndex(i);
                break;
            }
    }

    public void fillKlijentiBox() {
        ArrayList<Klijent> klijenti = new Klijenti().get();
        klijenti.sort(Comparator.comparing(tip -> tip.Ime));
        klijentBox.setModel(new DefaultComboBoxModel(klijenti.stream().map(k -> new ComboKeyValue(k.getDisplayName(), k)).toArray()));
    }

    public void fillTretmaniBox() {
        fillTretmaniBox(new Tretmani().get(), tretmanBox, kozmeticarBox, vremeBox, getDatum(datumTxt), tretman);
    }
}
