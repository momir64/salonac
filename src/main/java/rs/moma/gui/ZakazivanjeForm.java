package rs.moma.gui;

import rs.moma.entities.Tretman;
import rs.moma.entities.ZakazaniTretman;
import rs.moma.entities.Zaposlen;
import rs.moma.managers.Zaposleni;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static rs.moma.DataTools.getSelectedValue;
import static rs.moma.DataTools.strToDate;

public class ZakazivanjeForm extends JDialog {
    public ZakazivanjeForm(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
    }

    public void fillTretmaniBox(ArrayList<Tretman> tretmani, JComboBox<ComboKeyValue> tretmanBox, JComboBox<ComboKeyValue> kozmeticarBox, JComboBox<ComboKeyValue> vremeBox, LocalDate datum, ZakazaniTretman oldTretman) {
        ArrayList<Zaposlen> zaposleni = new Zaposleni().get();
        tretmani.removeIf(tretman -> zaposleni.stream().noneMatch(zaposlen -> Arrays.stream(zaposlen.ZaduzeniTretmani).anyMatch(tretmanID -> tretmanID == tretman.ID)));
        tretmani.sort(Comparator.comparing(tip -> tip.Naziv));
        tretmanBox.setModel(new DefaultComboBoxModel(tretmani.stream().map(t -> new ComboKeyValue(t.Naziv, t)).toArray()));
        fillKozmeticariBox(tretmanBox, kozmeticarBox, vremeBox, datum, oldTretman);
    }

    public void fillKozmeticariBox(JComboBox<ComboKeyValue> tretmanBox, JComboBox<ComboKeyValue> kozmeticarBox, JComboBox<ComboKeyValue> vremeBox, LocalDate datum, ZakazaniTretman oldTretman) {
        if (tretmanBox.getItemCount() == 0)
            kozmeticarBox.setModel(new DefaultComboBoxModel<>());
        else {
            ComboKeyValue       oldKozmeticar = (ComboKeyValue) kozmeticarBox.getSelectedItem();
            ArrayList<Zaposlen> kozmeticari   = new Zaposleni().getRadi((Tretman) getSelectedValue(tretmanBox));
            kozmeticari.sort(Comparator.comparing(tip -> tip.Ime));
            kozmeticarBox.setModel(new DefaultComboBoxModel(kozmeticari.stream().map(k -> new ComboKeyValue(k.getDisplayName(), k)).toArray()));
            if (oldKozmeticar != null && kozmeticari.stream().anyMatch(kozmeticar -> kozmeticar.ID == ((Zaposlen) oldKozmeticar.getValue()).ID))
                kozmeticarBox.setSelectedItem(oldKozmeticar);
        }
        fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, datum, oldTretman);
    }

    public void fillTerminiBox(JComboBox<ComboKeyValue> kozmeticarBox, JComboBox<ComboKeyValue> tretmanBox, JComboBox<ComboKeyValue> vremeBox, LocalDate datum, ZakazaniTretman oldTretman) {
        if (datum == null || kozmeticarBox.getItemCount() == 0)
            vremeBox.setModel(new DefaultComboBoxModel<>());
        else {
            ComboKeyValue oldTermin = (ComboKeyValue) vremeBox.getSelectedItem();
            ArrayList<Integer> termini = ((Zaposlen) getSelectedValue(kozmeticarBox))
                    .getSlobodniTermini(datum, oldTretman, ((Tretman) getSelectedValue(tretmanBox)).Trajanje);
            vremeBox.setModel(new DefaultComboBoxModel(termini.stream().map(i -> new ComboKeyValue(i + "h", i)).toArray()));
            if (oldTermin != null && termini.contains((Integer) oldTermin.getValue()))
                vremeBox.setSelectedItem(oldTermin);
        }
    }

    public LocalDate getDatum(JTextField datumTxt) {
        return strToDate(datumTxt.getText());
    }

    public LocalDateTime getTermin(JTextField datumTxt, JComboBox<ComboKeyValue> vremeBox) {
        return strToDate(datumTxt.getText()).atTime((Integer) getSelectedValue(vremeBox), 0);
    }
}
