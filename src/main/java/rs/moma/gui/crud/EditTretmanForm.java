package rs.moma.gui.crud;

import rs.moma.entities.Tretman;
import rs.moma.gui.helper.NameValue;
import rs.moma.gui.helper.NumericKeyAdapter;
import rs.moma.managers.TipoviTretmana;
import rs.moma.managers.Tretmani;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.showMessageDialog;
import static rs.moma.helper.DataTools.*;

public class EditTretmanForm extends JDialog {
    private JTextField           trajanjeTxt;
    private JPanel               mainPanel;
    private JTextField           nazivTxt;
    private JTextField           cenaTxt;
    private JButton              saveBtn;
    private JComboBox<NameValue> tipBox;

    public EditTretmanForm(JFrame parent, Tretman tretman, Runnable update) {
        super(parent, tretman != null ? "Tretman: " + tretman.Naziv : "Dodavanje tretmana", true);
        setMinimumSize(new Dimension(480, 450));
        setSize(700, 580);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        tipBox.setModel(new DefaultComboBoxModel<>(new TipoviTretmana().get().stream().map(tip -> new NameValue(tip.Tip, tip.ID)).toArray(NameValue[]::new)));
        trajanjeTxt.addKeyListener(new NumericKeyAdapter(false));
        cenaTxt.addKeyListener(new NumericKeyAdapter(true));
        if (tretman != null) fillInput(tretman);

        saveBtn.addActionListener(e -> Save(tretman, update));

        setVisible(true);
    }

    public Tretman getData() {
        return new Tretman((int) getSelectedValue(tipBox), nazivTxt.getText(), txtToFloat(cenaTxt), txtToInt(trajanjeTxt));
    }

    public void Close(Runnable update) {
        update.run();
        setVisible(false);
        dispose();
    }

    public void Save(Tretman tretman, Runnable update) {
        if (!isInputValid(nazivTxt, tipBox, cenaTxt, trajanjeTxt)) showMessageDialog(this, "Neispravan unos!");
        else if (tretman == null) {
            if (!new Tretmani().add(getData())) showMessageDialog(this, "Greška sa čuvanjem tipa!");
            else Close(update);
        } else {
            if (!new Tretmani().edit(tretman, getData())) showMessageDialog(this, "Greška sa čuvanjem tipa!");
            else Close(update);
        }
    }

    public void fillInput(Tretman tretman) {
        nazivTxt.setText(tretman.Naziv);
        tipBox.setSelectedItem(new NameValue("", tretman.TipID));
        cenaTxt.setText(String.valueOf(tretman.Cena));
        trajanjeTxt.setText(String.valueOf(tretman.Trajanje));
    }
}
