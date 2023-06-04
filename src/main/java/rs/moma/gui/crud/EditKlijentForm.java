package rs.moma.gui.crud;

import rs.moma.entities.Klijent;
import rs.moma.gui.helper.NameValue;
import rs.moma.managers.Klijenti;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.showMessageDialog;
import static rs.moma.gui.crud.helper.EditKorisnikForm.fillInputKorisnik;
import static rs.moma.helper.DataTools.*;

public class EditKlijentForm extends JDialog {
    private JTextField           usernameTxt;
    private JTextField           lozinkaTxt;
    private JTextField           prezimeTxt;
    private JTextField           telefonTxt;
    private JTextField           adresaTxt;
    private JPanel               mainPanel;
    private JButton              saveBtn;
    private JTextField           imeTxt;
    private JComboBox<NameValue> polBox;

    public EditKlijentForm(JFrame parent, Klijent klijent, Runnable update) {
        super(parent, klijent != null ? "Klijent: " + klijent.getDisplayName() : "Dodavanje klijenta", true);
        setMinimumSize(new Dimension(480, 570));
        setSize(860, 670);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        fillPolBox(polBox);
        if (klijent != null) fillInputKorisnik(klijent, imeTxt, prezimeTxt, polBox, telefonTxt, adresaTxt, usernameTxt, lozinkaTxt);

        saveBtn.addActionListener(e -> Save(klijent, update));

        setVisible(true);
    }

    public Klijent getData() {
        return new Klijent(imeTxt.getText(), prezimeTxt.getText(), (EPol) getSelectedValue(polBox), telefonTxt.getText(), adresaTxt.getText(), usernameTxt.getText(), lozinkaTxt.getText());
    }

    public void Close(Runnable update) {
        update.run();
        setVisible(false);
        dispose();
    }

    public void Save(Klijent klijent, Runnable update) {
        if (!isInputValid(imeTxt, prezimeTxt, polBox, telefonTxt, adresaTxt, usernameTxt, lozinkaTxt)) showMessageDialog(this, "Neispravan unos!");
        else if (klijent == null) {
            if (!new Klijenti().add(getData())) showMessageDialog(this, "Greška sa čuvanjem tipa!");
            else Close(update);
        } else {
            if (!new Klijenti().edit(klijent, getData())) showMessageDialog(this, "Greška sa čuvanjem tipa!");
            else Close(update);
        }
    }
}
