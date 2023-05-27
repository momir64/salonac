package rs.moma.gui;

import rs.moma.entities.Salon;

import javax.swing.*;

public class KozmeticarPage extends JFrame {
    private JButton leftBtn;
    private JButton rightBtn;
    private JTable kalendarTbl;
    private JList  tretmaniList;
    private JTable rasporedTbl;
    private JLabel mesecLbl;
    private JPanel kozmeticarPanel;
    private JPanel kalendarPanel;

    public KozmeticarPage(Salon salon) {
        setContentPane(kozmeticarPanel);
        setTitle(salon.korisnik.Ime + " " + salon.korisnik.Prezime);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
