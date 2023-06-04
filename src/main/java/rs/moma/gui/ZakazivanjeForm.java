package rs.moma.gui;

import rs.moma.entities.Tretman;
import rs.moma.entities.ZakazaniTretman;
import rs.moma.entities.Zaposlen;
import rs.moma.gui.helper.NameValue;
import rs.moma.managers.Zaposleni;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static rs.moma.helper.DataTools.*;

public class ZakazivanjeForm extends JDialog {
    public ZakazivanjeForm(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
    }

    public void fillTretmaniBox(ArrayList<Tretman> tretmani, JComboBox<NameValue> tretmanBox, JComboBox<NameValue> kozmeticarBox, JComboBox<NameValue> vremeBox, LocalDate datum, ZakazaniTretman oldTretman) {
        ArrayList<Zaposlen> zaposleni = new Zaposleni().get();
        tretmani.removeIf(tretman -> zaposleni.stream().noneMatch(zaposlen -> Arrays.stream(zaposlen.ZaduzeniTipoviTretmana).anyMatch(tipID -> tipID == tretman.TipID)));
        tretmani.sort(Comparator.comparing(tip -> tip.Naziv));
        tretmanBox.setModel(new DefaultComboBoxModel<>(tretmani.stream().map(t -> new NameValue(t.Naziv, t)).toArray(NameValue[]::new)));
        fillKozmeticariBox(tretmanBox, kozmeticarBox, vremeBox, datum, oldTretman);
    }

    public void fillKozmeticariBox(JComboBox<NameValue> tretmanBox, JComboBox<NameValue> kozmeticarBox, JComboBox<NameValue> vremeBox, LocalDate datum, ZakazaniTretman oldTretman) {
        if (tretmanBox.getItemCount() == 0)
            kozmeticarBox.setModel(new DefaultComboBoxModel<>());
        else {
            NameValue           oldKozmeticar = (NameValue) kozmeticarBox.getSelectedItem();
            ArrayList<Zaposlen> kozmeticari   = new Zaposleni().getRadi((Tretman) getSelectedValue(tretmanBox));
            kozmeticari.sort(Comparator.comparing(tip -> tip.Ime));
            kozmeticarBox.setModel(new DefaultComboBoxModel<>(kozmeticari.stream().map(k -> new NameValue(k.getDisplayName(), k)).toArray(NameValue[]::new)));
            if (oldKozmeticar != null && kozmeticari.stream().anyMatch(kozmeticar -> kozmeticar.ID == ((Zaposlen) oldKozmeticar.getValue()).ID))
                kozmeticarBox.setSelectedItem(oldKozmeticar);
        }
        fillTerminiBox(kozmeticarBox, tretmanBox, vremeBox, datum, oldTretman);
    }

    public void fillTerminiBox(JComboBox<NameValue> kozmeticarBox, JComboBox<NameValue> tretmanBox, JComboBox<NameValue> vremeBox, LocalDate datum, ZakazaniTretman oldTretman) {
        if (datum == null || kozmeticarBox.getItemCount() == 0)
            vremeBox.setModel(new DefaultComboBoxModel<>());
        else {
            NameValue oldTermin = (NameValue) vremeBox.getSelectedItem();
            ArrayList<Integer> termini = ((Zaposlen) getSelectedValue(kozmeticarBox))
                    .getSlobodniTermini(datum, oldTretman, ((Tretman) getSelectedValue(tretmanBox)).Trajanje);
            vremeBox.setModel(new DefaultComboBoxModel<>(termini.stream().map(i -> new NameValue(i + "h", i)).toArray(NameValue[]::new)));
            if (oldTermin != null && termini.contains((Integer) oldTermin.getValue()))
                vremeBox.setSelectedItem(oldTermin);
        }
    }
}
