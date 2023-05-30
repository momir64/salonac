package rs.moma;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import rs.moma.gui.KlijentPage;
import rs.moma.managers.Klijenti;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatDarculaLaf.install();

        new KlijentPage(new Klijenti().prijava("marija123", "pass456"));
//        new KlijentPage(new Klijenti().prijava("mikula", "lozinka123"));
//        new KozmeticarPage(new Zaposleni().prijava("daccca", "lozinka000"));
//        new WelcomePage();
    }
}