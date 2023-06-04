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
}
