package rs.moma.gui.crud;

import rs.moma.entities.TipTretmana;
import rs.moma.managers.TipoviTretmana;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.showMessageDialog;
import static rs.moma.helper.DataTools.isInputValid;

public class EditTipTretmanaForm extends JDialog {
    private JPanel     mainPanel;
    private JTextField nazivTxt;
    private JButton    saveBtn;

    public EditTipTretmanaForm(JFrame parent, TipTretmana tip, Runnable update) {
        super(parent, tip != null ? "Tip: " + tip.Tip : "Dodavanje tipa tretmana", true);
        setMinimumSize(new Dimension(450, 300));
        setSize(500, 350);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        if (tip != null) fillInput(tip);

        saveBtn.addActionListener(e -> Save(tip, update));

        setVisible(true);
    }

    public void Close(Runnable update) {
        update.run();
        setVisible(false);
        dispose();
    }

    public void Save(TipTretmana tip, Runnable update) {
        if (!isInputValid(nazivTxt)) showMessageDialog(this, "Neispravan unos!");
        else if (tip == null) {
            if (!new TipoviTretmana().add(new TipTretmana(-1, nazivTxt.getText(), true))) showMessageDialog(this, "Greška sa čuvanjem tipa!");
            else Close(update);
        } else {
            if (!new TipoviTretmana().edit(tip, new TipTretmana(-1, nazivTxt.getText()))) showMessageDialog(this, "Greška sa čuvanjem tipa!");
            else Close(update);
        }
    }

    public void fillInput(TipTretmana tip) {
        nazivTxt.setText(tip.Tip);
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        final JPanel       spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 4;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Naziv:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 1;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc       = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 20;
        mainPanel.add(spacer2, gbc);
        nazivTxt    = new JTextField();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 2;
        gbc.gridy   = 1;
        gbc.weightx = 3.0;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(nazivTxt, gbc);
        final JPanel spacer3 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 3;
        gbc.gridy      = 1;
        gbc.gridheight = 3;
        gbc.weightx    = 3.0;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 2;
        gbc.gridwidth = 3;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 20;
        mainPanel.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 3;
        gbc.weightx = 2.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer5, gbc);
        saveBtn = new JButton();
        saveBtn.setMaximumSize(new Dimension(78, 50));
        saveBtn.setMinimumSize(new Dimension(78, 50));
        saveBtn.setPreferredSize(new Dimension(78, 50));
        saveBtn.setText("Sačuvaj");
        gbc       = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        mainPanel.add(saveBtn, gbc);
        final JPanel spacer6 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 4;
        gbc.gridwidth = 4;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer6, gbc);
        label1.setLabelFor(nazivTxt);
    }
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {return mainPanel;}
}