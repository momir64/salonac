package rs.moma;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import rs.moma.entities.Salon;
import rs.moma.gui.WelcomePage;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatDarculaLaf.install();

        Salon salon = new Salon();
//        salon.korisnik  = new Zaposleni().prijava("daccca", "lozinka000");
//        salon.isKlijent = false;
//        new KozmeticarPage(salon);
        new WelcomePage(salon);
    }
}