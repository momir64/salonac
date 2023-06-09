package rs.moma.entities;

import rs.moma.helper.DataTools;
import rs.moma.managers.Klijenti;
import rs.moma.entities.helper.Korisnik;
import rs.moma.managers.ZakazaniTretmani;

import java.util.ArrayList;

public class Klijent extends Korisnik {
    public Klijent(int id, String ime, String prezime, DataTools.EPol pol, String telefon, String adresa, String username, String lozinka, boolean aktivan) {
        super(id, ime, prezime, pol, telefon, adresa, username, lozinka, aktivan);
    }

    public Klijent(String ime, String prezime, DataTools.EPol pol, String telefon, String adresa, String username, String lozinka, boolean aktivan) {
        super(new Klijenti().getNewID(), ime, prezime, pol, telefon, adresa, username, lozinka, aktivan);
    }

    public Klijent(String line) {
        super(line);
    }

    // Specijalne get metode
    public ArrayList<ZakazaniTretman> getSviZakazaniTretmani() {
        return new ZakazaniTretmani().getForKlijent(this, null, null);
    }

    public float getUkupnoPlatio() {
        return (float) new ZakazaniTretmani().getForKlijent(this, null, null).stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum();
    }

    public boolean getKarticaLojalnosti() {
        return getUkupnoPlatio() >= new Salon().MinIznosLojalnosti;
    }

    public String getDisplayName() {
        if (new Klijenti().get().stream().filter(klijent -> (klijent.Ime + " " + klijent.Prezime).equals(Ime + " " + Prezime)).count() > 1)
            return Ime + " " + Prezime + ", " + Username;
        else
            return Ime + " " + Prezime;
    }
}
