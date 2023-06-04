package rs.moma.gui;

import rs.moma.gui.helper.NameValue;
import rs.moma.entities.Klijent;
import rs.moma.managers.Klijenti;

import javax.swing.*;

import java.awt.*;

import static javax.swing.JOptionPane.showMessageDialog;
import static rs.moma.helper.DataTools.*;

public class RegisterForm extends JDialog {
    private JButton              registracijaBtn;
    private JPanel               registerPanel;
    private JTextField           usernameTxt;
    private JPasswordField       passwordTxt;
    private JTextField           prezimeTxt;
    private JTextField           telefonTxt;
    private JTextField           adresaTxt;
    private JTextField           imeTxt;
    private JComboBox<NameValue> polBox;

    public RegisterForm(JFrame parent) {
        super(parent, "Registracija", true);
        setMinimumSize(new Dimension(560, 560));
        setContentPane(registerPanel);
        setSize(600, 600);
        setLocationRelativeTo(parent);
        fillPolBox(polBox);
        registracijaBtn.addActionListener(e -> {
            Klijenti klijenti = new Klijenti();
            if (!isInputValid(imeTxt, prezimeTxt, telefonTxt, adresaTxt, usernameTxt, passwordTxt))
                showMessageDialog(this, "Neispravan unos!");
            else if (!klijenti.isUsernameFree(usernameTxt.getText()))
                showMessageDialog(this, "Uneto korisničko ime je zauzeto!");
            else if (isInputFilled(imeTxt, prezimeTxt, telefonTxt, adresaTxt, usernameTxt, passwordTxt)) {
                klijenti.add(new Klijent(imeTxt.getText(),
                                         prezimeTxt.getText(),
                                         (EPol) getSelectedValue(polBox),
                                         telefonTxt.getText(),
                                         adresaTxt.getText(),
                                         usernameTxt.getText(),
                                         passwordTxt.getText()));
                setVisible(false);
                dispose();
            } else
                showMessageDialog(this, "Popunite sva polja!");
        });
        setVisible(true);
    }
}