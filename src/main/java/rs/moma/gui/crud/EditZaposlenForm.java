package rs.moma.gui.crud;

import rs.moma.entities.Klijent;
import rs.moma.entities.TipTretmana;
import rs.moma.entities.Tretman;
import rs.moma.entities.Zaposlen;
import rs.moma.gui.crud.helper.CheckBoxListRenderer;
import rs.moma.gui.crud.helper.CheckListTip;
import rs.moma.gui.helper.NameValue;
import rs.moma.gui.helper.NumericKeyAdapter;
import rs.moma.managers.Klijenti;
import rs.moma.managers.TipoviTretmana;
import rs.moma.managers.Zaposleni;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import static javax.swing.JOptionPane.showMessageDialog;
import static rs.moma.gui.crud.helper.EditKorisnikForm.fillInputKorisnik;
import static rs.moma.helper.DataTools.*;
import static rs.moma.helper.DataTools.ETipZaposlenog.*;

public class EditZaposlenForm extends JDialog {
    private JButton              removeBonusRuleBtn;
    private JList<CheckListTip>  tipoviTretmanaLst;
    private JButton              saveBonusRuleBtn;
    private JTextField           bonusOfWhatTxt;
    private JTextField           bonusPeriodTxt;
    private JComboBox<NameValue> bonusOfWhatBox;
    private JComboBox<NameValue> bonusPeriodBox;
    private JComboBox<NameValue> bonusRuleBox;
    private JTextField           usernameTxt;
    private JTextField           osnovicaTxt;
    private JTextField           prezimeTxt;
    private JTextField           telefonTxt;
    private JTextField           lozinkaTxt;
    private JTextField           kSpremaTxt;
    private JComboBox<NameValue> spremaBox;
    private JTextField           adresaTxt;
    private JPanel               mainPanel;
    private JTextField           kStazTxt;
    private JButton              saveBtn;
    private JTextField           stazTxt;
    private JTextField           imeTxt;
    private JComboBox<NameValue> tipBox;
    private JComboBox<NameValue> polBox;
    private JPanel               kozmeticarPanel;

    public EditZaposlenForm(JFrame parent, Zaposlen zaposlen, Runnable update) {
        super(parent, zaposlen != null ? "Zaposleni: " + zaposlen.getDisplayName() : "Dodavanje zaposlenog", true);
        setMinimumSize(new Dimension(1000, 800));
        setSize(1100, 900);
        setLocationRelativeTo(parent);
        setContentPane(mainPanel);

        fillPolBox(polBox);
        bonusOfWhatBox.setModel(new DefaultComboBoxModel<>(new NameValue[]{new NameValue("Tretmana", "c"), new NameValue("Vrednosti tretmana", "v")}));
        bonusPeriodBox.setModel(new DefaultComboBoxModel<>(new NameValue[]{new NameValue("Dana", "d"), new NameValue("Nedelja", "w"), new NameValue("Meseci", "m")}));
        tipBox.setModel(new DefaultComboBoxModel<>(Arrays.stream(ETipZaposlenog.values()).map(tip -> new NameValue(getTipZaposlenogName(tip), tip)).toArray(NameValue[]::new)));
        spremaBox.setModel(new DefaultComboBoxModel<>(Arrays.stream(ENivoSpreme.values()).map(sprema -> new NameValue(getSpremaName(sprema), sprema)).toArray(NameValue[]::new)));
        if (zaposlen != null) fillInput(zaposlen);

        bonusPeriodTxt.addKeyListener(new NumericKeyAdapter(false));
        bonusOfWhatTxt.addKeyListener(new NumericKeyAdapter(true));

        tipBox.addActionListener(e -> enabledKozmeticarPanel());
        removeBonusRuleBtn.addActionListener(e -> removeBonusRule());
        saveBonusRuleBtn.addActionListener(e -> saveBonusRule());
        bonusRuleBox.addActionListener(e -> fillBonusRule());

        saveBtn.addActionListener(e -> Save(zaposlen, update));

        enabledKozmeticarPanel();
        fillBonusRule();

        setVisible(true);
    }

    public Zaposlen getData() {
        return new Zaposlen(imeTxt.getText(), prezimeTxt.getText(), (EPol) getSelectedValue(polBox), telefonTxt.getText(), adresaTxt.getText(), usernameTxt.getText(),
                            lozinkaTxt.getText(), (ETipZaposlenog) getSelectedValue(tipBox), (ENivoSpreme) getSelectedValue(spremaBox), txtToFloat(kSpremaTxt), txtToInt(stazTxt),
                            txtToFloat(kStazTxt), txtToFloat(osnovicaTxt), saveBonusRule(), getZaduzeniTipovi());
    }

    public void Close(Runnable update) {
        update.run();
        setVisible(false);
        dispose();
    }

    public void Save(Zaposlen zaposlen, Runnable update) {
        if (!isInputValid(imeTxt, prezimeTxt, polBox, telefonTxt, adresaTxt, usernameTxt, lozinkaTxt)) showMessageDialog(this, "Neispravan unos!");
        else if (zaposlen == null) {
            if (!new Zaposleni().add(getData())) showMessageDialog(this, "Greška sa čuvanjem tipa!");
            else Close(update);
        } else {
            if (!new Zaposleni().edit(zaposlen, getData())) showMessageDialog(this, "Greška sa čuvanjem tipa!");
            else Close(update);
        }
    }

    public int[] getZaduzeniTipovi() {
        ArrayList<Integer> tipovi = new ArrayList<>();
        for (int i = 0; i < tipoviTretmanaLst.getModel().getSize(); i++) {
            CheckListTip item = tipoviTretmanaLst.getModel().getElementAt(i);
            if (item.Selected) tipovi.add(item.Tip.ID);
        }
        return tipovi.stream().mapToInt(i -> i).toArray();
    }

    public void fillInput(Zaposlen zaposlen) {
        fillInputKorisnik(zaposlen, imeTxt, prezimeTxt, polBox, telefonTxt, adresaTxt, usernameTxt, lozinkaTxt);
        tipBox.setSelectedItem(new NameValue("", zaposlen.TipZaposlenog));
        spremaBox.setSelectedItem(new NameValue("", zaposlen.Sprema));
        kSpremaTxt.setText(String.valueOf(zaposlen.KoeficijentSprema));
        stazTxt.setText(String.valueOf(zaposlen.GodineStaza));
        kStazTxt.setText(String.valueOf(zaposlen.KoeficijentStaz));
        osnovicaTxt.setText(String.valueOf(zaposlen.PlataOsnova));
        fillBonusRules(zaposlen.Bonus.split(SP3));

        for (int i = 0; i < tipoviTretmanaLst.getModel().getSize(); i++) {
            CheckListTip item = tipoviTretmanaLst.getModel().getElementAt(i);
            if (Arrays.stream(zaposlen.ZaduzeniTipoviTretmana).anyMatch(id -> id == item.Tip.ID)) item.Selected = true;
            tipoviTretmanaLst.repaint(tipoviTretmanaLst.getCellBounds(i, i));
        }
    }

    public String getBonusRuleData() {
        if (bonusPeriodBox.getSelectedItem() == null || bonusOfWhatBox.getSelectedItem() == null) return null;
        return ((NameValue) bonusPeriodBox.getSelectedItem()).Value + SP4 + bonusPeriodTxt.getText() + SP4 +
               ((NameValue) bonusOfWhatBox.getSelectedItem()).Value + SP4 + bonusOfWhatTxt.getText();
    }

    public String saveBonusRule() {
        bonusPeriodTxt.setText(simplifyFloatToString(txtToFloat(bonusPeriodTxt)));
        bonusOfWhatTxt.setText(simplifyFloatToString(txtToFloat(bonusOfWhatTxt)));
        ArrayList<String> rules = new ArrayList<>();
        for (int i = 0; i < bonusRuleBox.getItemCount(); i++)
            if (i == bonusRuleBox.getSelectedIndex())
                rules.add(getBonusRuleData());
            else if (bonusRuleBox.getItemAt(i).Value != null)
                rules.add((String) bonusRuleBox.getItemAt(i).Value);

        int oldPos = bonusRuleBox.getSelectedIndex();
        fillBonusRules(rules.toArray(new String[0]));
        bonusRuleBox.setSelectedIndex(oldPos);

        return String.join(SP3, rules);
    }

    public void removeBonusRule() {
        if (bonusRuleBox.getSelectedIndex() == bonusRuleBox.getItemCount() - 1) return;
        ArrayList<String> rules = new ArrayList<>();
        for (int i = 0; i < bonusRuleBox.getItemCount() - 1; i++)
            if (i != bonusRuleBox.getSelectedIndex() && bonusRuleBox.getItemAt(i).Value != null)
                rules.add((String) bonusRuleBox.getItemAt(i).Value);

        fillBonusRules(rules.toArray(new String[0]));
    }

    public void fillBonusRules(String[] rules) {
        bonusRuleBox.removeAllItems();
        for (int i = 0; i < rules.length; i++) bonusRuleBox.addItem(new NameValue("Bonus pravilo " + (i + 1), rules[i]));
        bonusRuleBox.addItem(new NameValue("Novo pravilo", null));
    }

    public void fillBonusRule() {
        if (bonusRuleBox.getSelectedItem() == null) return;
        String rule = (String) ((NameValue) bonusRuleBox.getSelectedItem()).Value;
        if (rule == null || rule.equals("")) {
            bonusPeriodBox.setSelectedIndex(0);
            bonusOfWhatBox.setSelectedIndex(0);
            bonusPeriodTxt.setText("0");
            bonusOfWhatTxt.setText("0");
        } else {
            String[] parts = rule.split(SP4);
            bonusPeriodBox.setSelectedItem(new NameValue("", parts[0]));
            bonusPeriodTxt.setText(parts[1]);
            bonusOfWhatBox.setSelectedItem(new NameValue("", parts[2]));
            bonusOfWhatTxt.setText(parts[3]);
        }
    }

    public void enabledKozmeticarPanel() {
        boolean isEnabled = tipBox.getSelectedItem() == null || ((NameValue) tipBox.getSelectedItem()).Value == KOZMETICAR;
        for (Component component : kozmeticarPanel.getComponents()) component.setEnabled(isEnabled);
        tipoviTretmanaLst.setEnabled(isEnabled);
    }

    private void createUIComponents() {
        DefaultListModel<CheckListTip> model = new DefaultListModel<>();
        for (TipTretmana tip : new TipoviTretmana().get()) model.addElement(new CheckListTip(tip));
        tipoviTretmanaLst = new JList<>(model);
        tipoviTretmanaLst.setCellRenderer(new CheckBoxListRenderer());
        tipoviTretmanaLst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tipoviTretmanaLst.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (tipoviTretmanaLst.isEnabled()) {
                    int index = tipoviTretmanaLst.locationToIndex(event.getPoint());
                    if (index >= 0) {
                        CheckListTip item = tipoviTretmanaLst.getModel().getElementAt(index);
                        item.Selected = !item.Selected;
                        tipoviTretmanaLst.repaint(tipoviTretmanaLst.getCellBounds(index, index));
                    }
                }
            }
        });
    }
}
