package rs.moma;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import rs.moma.gui.WelcomePage;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatDarculaLaf.install();

        new WelcomePage();
    }
}