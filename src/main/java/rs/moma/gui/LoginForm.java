package rs.moma.gui;

import rs.moma.entities.Klijent;
import rs.moma.entities.Zaposlen;
import rs.moma.managers.Klijenti;
import rs.moma.managers.Zaposleni;

import javax.swing.*;

import static javax.swing.JOptionPane.showMessageDialog;
import static rs.moma.DataTools.*;

public class LoginForm extends JDialog {
    private JPanel         loginPanel;
    private JTextField     usernameTxt;
    private JPasswordField passwordTxt;
    private JButton        prijavaBtn;

    public LoginForm(WelcomePage parent) {
        super(parent, "Prijava", true);
        setContentPane(loginPanel);
        setSize(500, 350);
        setLocationRelativeTo(parent);
        prijavaBtn.addActionListener(e -> {
            if (!isInputFilled(usernameTxt, passwordTxt))
                showMessageDialog(this, "Popunite sva polja!");
            else if (!isInputValid(usernameTxt, passwordTxt))
                showMessageDialog(this, "Neispravan unos!");
            else {
                Klijent  klijent  = new Klijenti().prijava(usernameTxt.getText(), passwordTxt.getText());
                Zaposlen zaposlen = new Zaposleni().prijava(usernameTxt.getText(), passwordTxt.getText());
                if (klijent == null && zaposlen == null)
                    showMessageDialog(this, "Neispravno ime ili šifra!");
                else {
                    parent.Salon.korisnik  = klijent == null ? zaposlen : klijent;
                    parent.Salon.isKlijent = klijent != null;
                    parent.setVisible(false);
                    parent.dispose();

                    // USPEŠAN LOGIN
                    // OTVORITI FORMU

                    setVisible(false);
                    dispose();
                }
            }
        });
        setVisible(true);
    }
}

