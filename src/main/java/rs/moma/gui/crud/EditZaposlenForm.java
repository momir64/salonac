package rs.moma.gui.crud;

import rs.moma.entities.TipTretmana;
import rs.moma.entities.Zaposlen;
import rs.moma.gui.crud.helper.CheckBoxListRenderer;
import rs.moma.gui.crud.helper.CheckListTip;
import rs.moma.gui.helper.NameValue;
import rs.moma.gui.helper.NumericKeyAdapter;
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
        $$$setupUI$$$();
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
    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        final JPanel       spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 9;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Ime:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 1;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 1;
        gbc.gridy      = 1;
        gbc.gridheight = 11;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 20;
        mainPanel.add(spacer2, gbc);
        imeTxt        = new JTextField();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 1;
        gbc.gridwidth = 5;
        gbc.weightx   = 3.0;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(imeTxt, gbc);
        final JPanel spacer3 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 8;
        gbc.gridy      = 1;
        gbc.gridheight = 13;
        gbc.weightx    = 3.0;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer3, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Prezime:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 2;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label2, gbc);
        prezimeTxt    = new JTextField();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 2;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(prezimeTxt, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Pol:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 3;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label3, gbc);
        polBox        = new JComboBox();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 3;
        gbc.gridwidth = 5;
        gbc.weightx   = 3.0;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(polBox, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Telefon:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 4;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label4, gbc);
        telefonTxt    = new JTextField();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 4;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(telefonTxt, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Adresa:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 5;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label5, gbc);
        adresaTxt     = new JTextField();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 5;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(adresaTxt, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Korisničko ime:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 6;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label6, gbc);
        usernameTxt   = new JTextField();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 6;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(usernameTxt, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Lozinka:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 7;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label7, gbc);
        lozinkaTxt    = new JTextField();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 7;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(lozinkaTxt, gbc);
        final JPanel spacer4 = new JPanel();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 13;
        gbc.weightx = 2.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer4, gbc);
        saveBtn = new JButton();
        saveBtn.setMaximumSize(new Dimension(78, 50));
        saveBtn.setMinimumSize(new Dimension(78, 50));
        saveBtn.setPreferredSize(new Dimension(78, 50));
        saveBtn.setText("Sačuvaj");
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 13;
        gbc.gridwidth = 6;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.ipadx     = 300;
        mainPanel.add(saveBtn, gbc);
        final JPanel spacer5 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 14;
        gbc.gridwidth = 9;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 12;
        gbc.gridwidth = 8;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 20;
        mainPanel.add(spacer6, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Staž:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 10;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label8, gbc);
        stazTxt    = new JTextField();
        gbc        = new GridBagConstraints();
        gbc.gridx  = 2;
        gbc.gridy  = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        mainPanel.add(stazTxt, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Tip zaposlenog:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 8;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label9, gbc);
        tipBox        = new JComboBox();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 8;
        gbc.gridwidth = 5;
        gbc.weightx   = 3.0;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(tipBox, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("Sprema:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 9;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label10, gbc);
        spremaBox   = new JComboBox();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 2;
        gbc.gridy   = 9;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spremaBox, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Plata osnova:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 11;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label11, gbc);
        osnovicaTxt   = new JTextField();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 2;
        gbc.gridy     = 11;
        gbc.gridwidth = 5;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        mainPanel.add(osnovicaTxt, gbc);
        final JLabel label12 = new JLabel();
        label12.setText("Koeficijent:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 4;
        gbc.gridy   = 9;
        gbc.weightx = 0.1;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label12, gbc);
        kSpremaTxt  = new JTextField();
        gbc         = new GridBagConstraints();
        gbc.gridx   = 6;
        gbc.gridy   = 9;
        gbc.weightx = 0.5;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        mainPanel.add(kSpremaTxt, gbc);
        final JPanel spacer7 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 5;
        gbc.gridy      = 9;
        gbc.gridheight = 2;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer7, gbc);
        final JLabel label13 = new JLabel();
        label13.setText("Koeficijent:");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 4;
        gbc.gridy   = 10;
        gbc.weightx = 0.1;
        gbc.anchor  = GridBagConstraints.EAST;
        gbc.fill    = GridBagConstraints.VERTICAL;
        gbc.ipady   = 30;
        mainPanel.add(label13, gbc);
        kStazTxt   = new JTextField();
        gbc        = new GridBagConstraints();
        gbc.gridx  = 6;
        gbc.gridy  = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        mainPanel.add(kStazTxt, gbc);
        kozmeticarPanel = new JPanel();
        kozmeticarPanel.setLayout(new GridBagLayout());
        gbc            = new GridBagConstraints();
        gbc.gridx      = 7;
        gbc.gridy      = 1;
        gbc.gridheight = 11;
        gbc.weightx    = 2.0;
        gbc.fill       = GridBagConstraints.BOTH;
        mainPanel.add(kozmeticarPanel, gbc);
        final JPanel spacer8 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 0;
        gbc.gridy      = 0;
        gbc.gridheight = 6;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 40;
        kozmeticarPanel.add(spacer8, gbc);
        removeBonusRuleBtn = new JButton();
        removeBonusRuleBtn.setText("Obriši pravilo");
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 3;
        gbc.gridwidth = 2;
        gbc.weightx   = 1.0;
        gbc.fill      = GridBagConstraints.BOTH;
        gbc.insets    = new Insets(8, 0, 8, 0);
        kozmeticarPanel.add(removeBonusRuleBtn, gbc);
        saveBonusRuleBtn = new JButton();
        saveBonusRuleBtn.setText("Sačuvaj pravilo");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 4;
        gbc.gridy   = 3;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.insets  = new Insets(8, 0, 8, 0);
        kozmeticarPanel.add(saveBonusRuleBtn, gbc);
        bonusRuleBox  = new JComboBox();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 0;
        gbc.gridwidth = 4;
        gbc.weightx   = 4.0;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.BOTH;
        gbc.insets    = new Insets(8, 0, 8, 0);
        kozmeticarPanel.add(bonusRuleBox, gbc);
        bonusOfWhatTxt = new JTextField();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 2;
        gbc.gridy      = 2;
        gbc.weightx    = 1.0;
        gbc.anchor     = GridBagConstraints.WEST;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.insets     = new Insets(8, 0, 8, 0);
        kozmeticarPanel.add(bonusOfWhatTxt, gbc);
        bonusOfWhatBox = new JComboBox();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 4;
        gbc.gridy      = 2;
        gbc.weightx    = 1.0;
        gbc.anchor     = GridBagConstraints.WEST;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.insets     = new Insets(8, 0, 8, 0);
        kozmeticarPanel.add(bonusOfWhatBox, gbc);
        bonusPeriodTxt = new JTextField();
        bonusPeriodTxt.setText("");
        gbc         = new GridBagConstraints();
        gbc.gridx   = 2;
        gbc.gridy   = 1;
        gbc.weightx = 1.0;
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.insets  = new Insets(8, 0, 8, 0);
        kozmeticarPanel.add(bonusPeriodTxt, gbc);
        bonusPeriodBox = new JComboBox();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 4;
        gbc.gridy      = 1;
        gbc.weightx    = 1.0;
        gbc.anchor     = GridBagConstraints.WEST;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.insets     = new Insets(8, 0, 8, 0);
        kozmeticarPanel.add(bonusPeriodBox, gbc);
        final JPanel spacer9 = new JPanel();
        gbc        = new GridBagConstraints();
        gbc.gridx  = 3;
        gbc.gridy  = 3;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        kozmeticarPanel.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 4;
        gbc.gridwidth = 4;
        gbc.fill      = GridBagConstraints.VERTICAL;
        gbc.ipady     = 20;
        kozmeticarPanel.add(spacer10, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc           = new GridBagConstraints();
        gbc.gridx     = 1;
        gbc.gridy     = 5;
        gbc.gridwidth = 4;
        gbc.weightx   = 1.0;
        gbc.weighty   = 1.0;
        gbc.fill      = GridBagConstraints.BOTH;
        kozmeticarPanel.add(scrollPane1, gbc);
        scrollPane1.setViewportView(tipoviTretmanaLst);
        final JLabel label14 = new JLabel();
        label14.setHorizontalAlignment(4);
        label14.setHorizontalTextPosition(4);
        label14.setText("Za");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 1;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10);
        kozmeticarPanel.add(label14, gbc);
        final JLabel label15 = new JLabel();
        label15.setHorizontalAlignment(4);
        label15.setHorizontalTextPosition(4);
        label15.setText("Puta");
        gbc        = new GridBagConstraints();
        gbc.gridx  = 1;
        gbc.gridy  = 2;
        gbc.fill   = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10);
        kozmeticarPanel.add(label15, gbc);
        final JPanel spacer11 = new JPanel();
        gbc            = new GridBagConstraints();
        gbc.gridx      = 3;
        gbc.gridy      = 9;
        gbc.gridheight = 2;
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.ipadx      = 5;
        mainPanel.add(spacer11, gbc);
        label1.setLabelFor(imeTxt);
        label2.setLabelFor(prezimeTxt);
        label3.setLabelFor(polBox);
        label4.setLabelFor(telefonTxt);
        label5.setLabelFor(adresaTxt);
        label6.setLabelFor(usernameTxt);
        label7.setLabelFor(lozinkaTxt);
        label8.setLabelFor(stazTxt);
        label9.setLabelFor(tipBox);
        label10.setLabelFor(spremaBox);
        label11.setLabelFor(osnovicaTxt);
        label12.setLabelFor(kSpremaTxt);
        label13.setLabelFor(kStazTxt);
    }
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {return mainPanel;}
}
