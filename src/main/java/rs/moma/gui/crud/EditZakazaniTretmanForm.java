package rs.moma.gui.crud;

import rs.moma.entities.ZakazaniTretman;
import rs.moma.gui.helper.DateKeyAdapter;
import rs.moma.gui.helper.NameValue;
import rs.moma.gui.helper.NumericKeyAdapter;
import rs.moma.gui.helper.TimeKeyAdapter;
import rs.moma.managers.Klijenti;
import rs.moma.managers.Tretmani;
import rs.moma.managers.ZakazaniTretmani;
import rs.moma.managers.Zaposleni;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static javax.swing.JOptionPane.showMessageDialog;
import static rs.moma.helper.DataTools.*;
import static rs.moma.helper.DataTools.EStanjeTermina.*;

public class EditZakazaniTretmanForm extends JDialog {
    private JCheckBox            karticaLojalnostiCB;
    private JComboBox<NameValue> kozmeticarBox;
    private JTextField           trajanjeTxt;
    private JComboBox<NameValue> tretmanBox;
    private JComboBox<NameValue> klijentBox;
    private JPanel               mainPanel;
    private JComboBox<NameValue> stanjeBox;
    private JTextField           datumTxt;
    private JTextField           vremeTxt;
    private JButton              saveBtn;
    private JTextField           cenaTxt;

    public EditZakazaniTretmanForm(JFrame parent, ZakazaniTretman tretman, Runnable update) {
        super(parent, tretman != null ? "Zakazani tretman: " + new Tretmani().get(tretman.TretmanID).Naziv : "Dodavanje zakazanog tretmana", true);
        setMinimumSize(new Dimension(550, 670));
        setSize(900, 750);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        tretmanBox.setModel(new DefaultComboBoxModel<>(new Tretmani().get().stream().map(tr -> new NameValue(tr.Naziv, tr.ID)).toArray(NameValue[]::new)));
        stanjeBox.setModel(new DefaultComboBoxModel<>(Arrays.stream(values()).map(stanje -> new NameValue(getStanjeName(stanje), stanje)).toArray(NameValue[]::new)));
        klijentBox.setModel(new DefaultComboBoxModel<>(new Klijenti().get().stream().map(klijent -> new NameValue(klijent.Ime, klijent.ID)).toArray(NameValue[]::new)));
        kozmeticarBox.setModel(new DefaultComboBoxModel<>(new Zaposleni().getKozmeticari().stream().map(kozmeticar -> new NameValue(kozmeticar.Ime, kozmeticar.ID)).toArray(NameValue[]::new)));
        trajanjeTxt.addKeyListener(new NumericKeyAdapter(false));
        cenaTxt.addKeyListener(new NumericKeyAdapter(true));
        datumTxt.addKeyListener(new DateKeyAdapter());
        vremeTxt.addKeyListener(new TimeKeyAdapter());
        if (tretman != null) fillInput(tretman);

        saveBtn.addActionListener(e -> Save(tretman, update));

        setVisible(true);
    }

    public ZakazaniTretman getData() {
        return new ZakazaniTretman((int) getSelectedValue(tretmanBox), txtToFloat(cenaTxt), txtToInt(trajanjeTxt), getTermin(datumTxt, vremeTxt), (EStanjeTermina) getSelectedValue(stanjeBox),
                                   (int) getSelectedValue(klijentBox), (int) getSelectedValue(kozmeticarBox), karticaLojalnostiCB.isSelected());
    }

    public void Close(Runnable update) {
        update.run();
        setVisible(false);
        dispose();
    }

    public void Save(ZakazaniTretman tretman, Runnable update) {
        if (!isInputValid(tretmanBox, klijentBox, kozmeticarBox, stanjeBox, trajanjeTxt, cenaTxt, datumTxt, vremeTxt)) showMessageDialog(this, "Neispravan unos!");
        else if (tretman == null) {
            if (!new ZakazaniTretmani().add(getData())) showMessageDialog(this, "Greška sa čuvanjem tipa!");
            else Close(update);
        } else {
            if (!new ZakazaniTretmani().edit(tretman, getData())) showMessageDialog(this, "Greška sa čuvanjem tipa!");
            else Close(update);
        }
    }

    public void fillInput(ZakazaniTretman tretman) {
        tretmanBox.setSelectedItem(new NameValue("", tretman.TretmanID));
        klijentBox.setSelectedItem(new NameValue("", tretman.KlijentID));
        kozmeticarBox.setSelectedItem(new NameValue("", tretman.KozmeticarID));
        stanjeBox.setSelectedItem(new NameValue("", tretman.Stanje));
        trajanjeTxt.setText(String.valueOf(tretman.Trajanje));
        cenaTxt.setText(String.valueOf(tretman.Cena));
        datumTxt.setText(tretman.Vreme.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        vremeTxt.setText(tretman.Vreme.format(DateTimeFormatter.ofPattern("HH")) + "h");
        karticaLojalnostiCB.setSelected(tretman.KarticaLojalnosti);
    }
}
