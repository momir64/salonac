package rs.moma;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import rs.moma.entities.Zaposlen;
import rs.moma.gui.WelcomePage;
import rs.moma.entities.Salon;

import java.nio.file.Files;
import java.nio.file.Paths;

import static rs.moma.helper.DataTools.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatDarculaLaf.install();

        if (new Salon().Naziv == null)
            SetupDatabase();

        new WelcomePage();
    }

    public static void SetupDatabase() {
        try {
            System.err.println("Pravljenje novih fajlova...");
            new Salon("Salonac", 8, 20, 9999999);
            Files.write(Paths.get(fileZaposleni), new Zaposlen(0, "Admin", "Admin", EPol.MALE, "0000", "-", "admin", "admin", true, ETipZaposlenog.MENADZER,
                                                               ENivoSpreme.AKADEMSKA, 0, 0, 0, 0, "", new int[]{-1}).toString().getBytes());
            Files.write(Paths.get(fileZakazaniTretmani), "".getBytes());
            Files.write(Paths.get(fileTipoviTretmana), "".getBytes());
            Files.write(Paths.get(fileTretmani), "".getBytes());
            Files.write(Paths.get(fileKlijenti), "".getBytes());
            Files.write(Paths.get(fileIsplate), "".getBytes());
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom pravljenje novih fajlova!");
        }
    }
}