package rs.moma.gui;

import rs.moma.DataTools.EPol;
import rs.moma.entities.Klijent;
import rs.moma.managers.Klijenti;

import javax.swing.*;

import static javax.swing.JOptionPane.showMessageDialog;
import static rs.moma.DataTools.*;

public class RegisterForm extends JDialog {
    final   Klijenti                 klijenti = new Klijenti();
    private JButton                  registracijaBtn;
    private JPanel                   registerPanel;
    private JTextField               usernameTxt;
    private JPasswordField           passwordTxt;
    private JTextField               prezimeTxt;
    private JTextField               telefonTxt;
    private JTextField               adresaTxt;
    private JTextField               imeTxt;
    private JComboBox<ComboKeyValue> polBox;

    public RegisterForm(JFrame parent) {
        super(parent, "Registracija", true);
        setContentPane(registerPanel);
        setSize(600, 600);
        setLocationRelativeTo(parent);
        polBox.setModel(new DefaultComboBoxModel(new Object[]{new ComboKeyValue("Muško", EPol.MALE), new ComboKeyValue("Žensko", EPol.FEMALE), new ComboKeyValue("Ostalo", EPol.OTHER)}));
        registracijaBtn.addActionListener(e -> {
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