package rs.moma.gui;

import rs.moma.entities.*;
import rs.moma.managers.TipoviTretmana;
import rs.moma.managers.Tretmani;
import rs.moma.managers.ZakazaniTretmani;
import rs.moma.managers.Zaposleni;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class KlijentZakazivanjeForm extends JDialog {
    private JTextField          minTrajanjeTxt;
    private JTextField               maxTrajanjeTxt;
    private JComboBox<ComboKeyValue> kozmeticarBox;
    private JTextField               minCenaTxt;
    private JTextField               maxCenaTxt;
    private JComboBox<ComboKeyValue> tretmanBox;
    private JPanel                   mainPanel;
    private JTextField               datumTxt;
    private JComboBox<ComboKeyValue> vremeBox;
    private JComboBox<ComboKeyValue> tipBox;
    private JButton                  addBtn;

    public KlijentZakazivanjeForm(JFrame parent, Klijent klijent, LocalDate date, Runnable update) {
        super(parent, "Zakazivanje tretmana", true);
        setSize(655, 655);
        setContentPane(mainPanel);
        setLocationRelativeTo(parent);
        setMinimumSize(new Dimension(510, 550));

        ArrayList<TipTretmana> tipovi = new TipoviTretmana().get();
        tipovi.add(new TipTretmana(-1, ""));
        tipovi.sort(Comparator.comparing(tip -> tip.Tip));
        tipBox.setModel(new DefaultComboBoxModel(tipovi.stream().map(tip -> new ComboKeyValue(tip.Tip, tip.ID)).toArray()));
        tretmanBox.addActionListener(e -> fillKozmeticari());
        kozmeticarBox.addActionListener(e -> fillTermini());
        tipBox.addActionListener(e -> fillTretmani());
        if (date == null) date = LocalDate.now();
        datumTxt.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        datumTxt.addKeyListener(new DateKeyAdapter(this, datumTxt));
        minTrajanjeTxt.addKeyListener(new NumericKeyAdapter(this));
        maxTrajanjeTxt.addKeyListener(new NumericKeyAdapter(this));
        minCenaTxt.addKeyListener(new NumericKeyAdapter(this));
        maxCenaTxt.addKeyListener(new NumericKeyAdapter(this));
        addBtn.addActionListener(e -> zakaziTermin(klijent, update));
        addOnChangeFill(minTrajanjeTxt, this::fillTretmani);
        addOnChangeFill(maxTrajanjeTxt, this::fillTretmani);
        addOnChangeFill(minCenaTxt, this::fillTretmani);
        addOnChangeFill(maxCenaTxt, this::fillTretmani);
        addOnChangeFill(datumTxt, this::fillTermini);
        fillTretmani();

        setVisible(true);
    }

    public void zakaziTermin(Klijent klijent, Runnable update) {
        if (vremeBox.getItemCount() == 0) return;
        new ZakazaniTretmani().add(new ZakazaniTretman((Tretman) ((ComboKeyValue) tretmanBox.getSelectedItem()).getValue(),
                                                       getTermin(), klijent, (Zaposlen) ((ComboKeyValue) kozmeticarBox.getSelectedItem()).getValue()));
        fillTermini();
        update.run();
    }

    public void addOnChangeFill(JTextField txt, Runnable what) {
        txt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                what.run();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                what.run();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                what.run();
            }
        });
    }

    public int txt2int(JTextField txt) {
        return txt.getText().trim().equals("") ? -1 : Integer.parseInt(txt.getText());
    }

    public void fillTretmani() {
        ArrayList<Tretman> tretmani = new Tretmani().filter((int) ((ComboKeyValue) tipBox.getSelectedItem()).getValue(), null,
                                                            txt2int(minTrajanjeTxt), txt2int(maxTrajanjeTxt), txt2int(minCenaTxt), txt2int(maxCenaTxt));
        tretmani.sort(Comparator.comparing(tip -> tip.Naziv));
        tretmanBox.setModel(new DefaultComboBoxModel(tretmani.stream().map(t -> new ComboKeyValue(t.Naziv, t)).toArray()));
        fillKozmeticari();
    }

    public void fillKozmeticari() {
        if (tretmanBox.getItemCount() == 0)
            kozmeticarBox.setModel(new DefaultComboBoxModel<>());
        else {
            ArrayList<Zaposlen> kozmeticari = new Zaposleni().getRadi((Tretman) ((ComboKeyValue) tretmanBox.getSelectedItem()).getValue());
            kozmeticari.sort(Comparator.comparing(tip -> tip.Ime));
            kozmeticarBox.setModel(new DefaultComboBoxModel(kozmeticari.stream().map(k -> new ComboKeyValue(k.Ime + " " + k.Prezime, k)).toArray()));
        }
        fillTermini();
    }

    public LocalDate str2date(String datum) {
        String[] data = datum.split("\\.");
        if (datum.contains("_") || datum.trim().equals("") || data.length < 3) return null;
        try {
            return LocalDate.of(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
        } catch (Exception e) {
            return null;
        }
    }

    public LocalDate getDatum() {
        return str2date(datumTxt.getText());
    }

    public LocalDateTime getTermin() {
        return str2date(datumTxt.getText()).atTime((Integer) ((ComboKeyValue) vremeBox.getSelectedItem()).getValue(), 0);
    }

    public void fillTermini() {
        if (getDatum() == null || kozmeticarBox.getItemCount() == 0)
            vremeBox.setModel(new DefaultComboBoxModel<>());
        else
            vremeBox.setModel(new DefaultComboBoxModel(((Zaposlen) ((ComboKeyValue) kozmeticarBox.getSelectedItem()).getValue())
                                                               .getSlobodniTermini(getDatum()).stream().map(i -> new ComboKeyValue(i + "h", i)).toArray()));
    }
}
