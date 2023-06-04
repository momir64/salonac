package rs.moma.entities;

import rs.moma.helper.NazivVrednostVreme;
import rs.moma.managers.Isplate;
import rs.moma.managers.ZakazaniTretmani;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

import static rs.moma.helper.DataTools.SP1;
import static rs.moma.helper.DataTools.fileSalon;

public class Salon {
    public String Naziv;
    public int    PocetakRadnogVremena;
    public int    KrajRadnogVremena;
    public float  MinIznosLojalnosti;

    public Salon(String naziv, int pocetakRadnogVremena, int krajRadnogVremena, float minIznosLojalnosti) {
        Naziv                = naziv;
        PocetakRadnogVremena = pocetakRadnogVremena;
        KrajRadnogVremena    = krajRadnogVremena;
        MinIznosLojalnosti   = minIznosLojalnosti;
        save();
    }

    public Salon() {
        try {
            String[] data = Files.readAllLines(Paths.get(fileSalon), StandardCharsets.UTF_8).get(0).split(SP1);
            Naziv                = data[0];
            PocetakRadnogVremena = Integer.parseInt(data[1]);
            KrajRadnogVremena    = Integer.parseInt(data[2]);
            MinIznosLojalnosti   = Float.parseFloat(data[3]);
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja salona!");
        }
    }

    public void edit(String naziv, int pocetakRadnogVremena, int krajRadnogVremena, float minIznosLojalnosti) {
        if (naziv != null && !naziv.trim().equals("")) Naziv = naziv;
        if (pocetakRadnogVremena != -1) PocetakRadnogVremena = pocetakRadnogVremena;
        if (krajRadnogVremena != -1) KrajRadnogVremena = krajRadnogVremena;
        if (minIznosLojalnosti != -1) MinIznosLojalnosti = minIznosLojalnosti;
        save();
    }

    private void save() {
        try {
            Files.write(Paths.get(fileSalon), (Naziv + SP1 + PocetakRadnogVremena + SP1 + KrajRadnogVremena + SP1 + MinIznosLojalnosti).getBytes());
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čuvanja slaona!");
        }
    }

    // Specijalne get metode
    public LocalDateTime getOldestPrihodRashod() {
        ArrayList<NazivVrednostVreme> finansije = getFinansije(null, null);
        if (finansije.isEmpty()) return LocalDateTime.now();
        return finansije.stream().min(Comparator.comparing(f -> f.Vreme)).get().Vreme;
    }

    public ArrayList<NazivVrednostVreme> getFinansije(LocalDateTime from, LocalDateTime to) {
        ArrayList<NazivVrednostVreme> prihodi   = new ZakazaniTretmani().getPrihodi(from, to);
        ArrayList<NazivVrednostVreme> rashodi   = new Isplate().getRashodi(from, to);
        ArrayList<NazivVrednostVreme> finansije = new ArrayList<>(prihodi);
        finansije.addAll(rashodi);
        finansije.sort(Comparator.comparing(nazivVrednostVreme -> nazivVrednostVreme.Vreme));
        return finansije;
    }
}
