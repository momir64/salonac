package rs.moma.gui;

import rs.moma.entities.Salon;

import javax.swing.*;

public class WelcomePage extends JFrame {
    private JPanel  welcomePanel;
    private JButton loginBtn;
    private JButton registerBtn;
    private JLabel  radnoVremeLabel;
    public  Salon   Salon;

    public WelcomePage(Salon salon) {
        this.Salon = salon;
        setContentPane(welcomePanel);
        setTitle(salon.Naziv);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        radnoVremeLabel.setText(String.format("%02dh - %02dh", salon.PocetakRadnogVremena, salon.KrajRadnogVremena));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        loginBtn.addActionListener(e -> new LoginForm(this));
        registerBtn.addActionListener(e -> new RegisterForm(this));
    }
}
