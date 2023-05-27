package rs.moma;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import rs.moma.entities.Salon;
import rs.moma.gui.KozmeticarPage;
import rs.moma.managers.Zaposleni;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatDarculaLaf.install();

        Salon salon = new Salon("Salonac", 8, 20, 10000);
        salon.korisnik  = new Zaposleni().prijava("pero", "perica321");
        salon.isKlijent = false;
        new KozmeticarPage(salon);
//        new WelcomePage(salon);
    }
}