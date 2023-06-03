package rs.moma.gui;

import rs.moma.entities.Salon;

import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {
    private JPanel  welcomePanel;
    private JButton loginBtn;
    private JButton registerBtn;
    private JLabel  radnoVremeLabel;

    public WelcomePage() {
        Salon salon = new Salon();
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
    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new GridBagLayout());
        welcomePanel.setPreferredSize(new Dimension(400, 300));
        final JLabel label1 = new JLabel();
        label1.setAlignmentY(0.0f);
        label1.setText("Radno vreme:");
        GridBagConstraints gbc;
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor  = GridBagConstraints.SOUTHEAST;
        gbc.insets  = new Insets(0, 0, 10, 5);
        welcomePanel.add(label1, gbc);
        radnoVremeLabel = new JLabel();
        radnoVremeLabel.setAlignmentY(0.0f);
        radnoVremeLabel.setText("08h - 20h");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 1;
        gbc.gridy   = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor  = GridBagConstraints.SOUTHWEST;
        gbc.insets  = new Insets(0, 5, 10, 0);
        welcomePanel.add(radnoVremeLabel, gbc);
        registerBtn = new JButton();
        registerBtn.setFocusPainted(false);
        registerBtn.setPreferredSize(new Dimension(200, 50));
        registerBtn.setText("Registruj se");
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 2;
        gbc.gridwidth = 2;
        gbc.weightx   = 1.0;
        gbc.weighty   = 1.0;
        gbc.anchor    = GridBagConstraints.NORTH;
        gbc.insets    = new Insets(10, 0, 0, 0);
        welcomePanel.add(registerBtn, gbc);
        loginBtn = new JButton();
        loginBtn.setFocusPainted(false);
        loginBtn.setMargin(new Insets(0, 0, 0, 0));
        loginBtn.setPreferredSize(new Dimension(200, 50));
        loginBtn.setText("Prijavi se");
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 1;
        gbc.gridwidth = 2;
        gbc.weightx   = 1.0;
        gbc.weighty   = 1.0;
        gbc.anchor    = GridBagConstraints.SOUTH;
        gbc.insets    = new Insets(0, 0, 10, 0);
        welcomePanel.add(loginBtn, gbc);
        final JPanel spacer1 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 2;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        welcomePanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 4;
        gbc.gridwidth = 2;
        gbc.weighty   = 0.1;
        gbc.fill      = GridBagConstraints.VERTICAL;
        welcomePanel.add(spacer2, gbc);
    }
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {return welcomePanel;}
}
