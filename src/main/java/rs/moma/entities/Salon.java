package rs.moma.entities;

import rs.moma.managers.ZakazaniTretmani;
import rs.moma.managers.Zaposleni;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

public class Salon {
    public float    MinIznosLojalnosti;
    public int      PocetakRadnogVremena;
    public int      KrajRadnogVremena;
    public boolean  isKlijent;
    public Korisnik korisnik;
    public String   Naziv;

    public Salon(String naziv, int pocetakRadnogVremena, int krajRadnogVremena, float minIznosLojalnosti) {
        MinIznosLojalnosti   = minIznosLojalnosti;
        PocetakRadnogVremena = pocetakRadnogVremena;
        KrajRadnogVremena    = krajRadnogVremena;
        Naziv                = naziv;
    }

    private boolean nemaZakazanihTokom(ArrayList<ZakazaniTretman> tretmani, LocalDateTime vreme, int trajanje) {
        for (int i = 1; i < (int) Math.ceil(trajanje / 60f); i++) {
            int hours = i;
            if (tretmani.stream().anyMatch(tretman -> tretman.Vreme.isEqual(vreme.plusHours(hours))))
                return false;
        }
        return true;
    }

    public LocalDateTime prvoSlobodnVreme(Tretman tretman, Zaposlen kozmeticar) {
        ArrayList<ZakazaniTretman> zakazani = new ZakazaniTretmani().getKozmeticar(kozmeticar, null, null, false);
        int                        vreme    = LocalTime.now().getHour() >= kozmeticar.KrajRadnoVreme ? kozmeticar.PocetakRadnoVreme : LocalTime.now().getHour();
        LocalDate                  dan      = LocalTime.now().getHour() >= kozmeticar.KrajRadnoVreme ? LocalDateTime.now().toLocalDate().plusDays(1) : LocalDateTime.now().toLocalDate();
        LocalDateTime              termin   = null;

        while (termin == null) {
            LocalDateTime             testVreme = dan.atTime(vreme, 0);
            Optional<ZakazaniTretman> zakazan   = zakazani.stream().filter(tmp -> tmp.Vreme.isEqual(testVreme)).findFirst();

            if (!zakazan.isPresent() && testVreme.isAfter(LocalDateTime.now()) && nemaZakazanihTokom(zakazani, testVreme, tretman.Trajanje))
                termin = testVreme;
            else if (zakazan.isPresent() && zakazan.get().Vreme.getHour() + (int) Math.ceil(zakazan.get().Trajanje / 60f) >= kozmeticar.KrajRadnoVreme) {
                vreme = kozmeticar.PocetakRadnoVreme;
                dan   = dan.plusDays(1);
            } else vreme += zakazan.map(tmp -> (int) Math.ceil(tmp.Trajanje / 60f)).orElse(1);
        }

        return termin;
    }

    public KozmeticarVreme prviSlobodniTermin(Tretman tretman) {
        ArrayList<Zaposlen> kozmeticari = new Zaposleni().getRadi(tretman);
        KozmeticarVreme     termin      = new KozmeticarVreme(kozmeticari.get(0), prvoSlobodnVreme(tretman, kozmeticari.get(0)));

        for (int i = 1; i < kozmeticari.size(); i++) {
            LocalDateTime vreme = prvoSlobodnVreme(tretman, kozmeticari.get(i));
            if (vreme.isBefore(termin.Vreme))
                termin = new KozmeticarVreme(kozmeticari.get(i), vreme);
        }

        return termin;
    }
}
