package rs.moma.gui.crud.helper;

import rs.moma.gui.helper.NameValue;
import rs.moma.entities.helper.Korisnik;

import javax.swing.*;

public class EditKorisnikForm {
    public static void fillInputKorisnik(Korisnik korisnik, JTextField imeTxt, JTextField prezimeTxt, JComboBox<NameValue> polBox, JTextField telefonTxt, JTextField adresaTxt, JTextField usernameTxt, JTextField lozinkaTxt, JCheckBox aktivanCB) {
        imeTxt.setText(korisnik.Ime);
        prezimeTxt.setText(korisnik.Prezime);
        polBox.setSelectedItem(new NameValue("", korisnik.Pol));
        telefonTxt.setText(korisnik.Telefon);
        adresaTxt.setText(korisnik.Adresa);
        usernameTxt.setText(korisnik.Username);
        lozinkaTxt.setText(korisnik.Lozinka);
        aktivanCB.setSelected(korisnik.Aktivan);
    }
}
