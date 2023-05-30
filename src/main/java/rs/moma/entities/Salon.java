package rs.moma.entities;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static rs.moma.DataTools.SP1;
import static rs.moma.DataTools.fileSalon;

public class Salon {
    public final String   Naziv;
    public final int      PocetakRadnogVremena;
    public final int      KrajRadnogVremena;
    public final float    MinIznosLojalnosti;

    public Salon(String naziv, int pocetakRadnogVremena, int krajRadnogVremena, float minIznosLojalnosti) {
        Naziv                = naziv;
        PocetakRadnogVremena = pocetakRadnogVremena;
        KrajRadnogVremena    = krajRadnogVremena;
        MinIznosLojalnosti   = minIznosLojalnosti;
        save();
    }

    public Salon() {
        String[] data = new String[]{"", "", "", ""};
        try {
            data = Files.readAllLines(Paths.get(fileSalon), StandardCharsets.UTF_8).get(0).split(SP1);
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja salona!");
        }
        Naziv                = data[0];
        PocetakRadnogVremena = Integer.parseInt(data[1]);
        KrajRadnogVremena    = Integer.parseInt(data[2]);
        MinIznosLojalnosti   = Float.parseFloat(data[3]);
    }

    private void save() {
        try {
            Files.write(Paths.get(fileSalon), (Naziv + SP1 + PocetakRadnogVremena + SP1 + KrajRadnogVremena + SP1 + MinIznosLojalnosti).getBytes());
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čuvanja slaona!");
        }
    }

}
