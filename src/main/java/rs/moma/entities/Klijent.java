package rs.moma.entities;

import rs.moma.DataTools;
import rs.moma.managers.Klijenti;
import rs.moma.managers.ZakazaniTretmani;

public class Klijent extends Korisnik {
    public Klijent(int id, String ime, String prezime, DataTools.EPol pol, String telefon, String adresa, String username, String lozinka) {
        super(id, ime, prezime, pol, telefon, adresa, username, lozinka);
    }

    public Klijent(String ime, String prezime, DataTools.EPol pol, String telefon, String adresa, String username, String lozinka) {
        super(new Klijenti().getNewID(), ime, prezime, pol, telefon, adresa, username, lozinka);
    }

    public Klijent(String line) {
        super(line);
    }

    // Specijalne get metode
    public float getUkupnoPlatio() {
        return (float) new ZakazaniTretmani().getKlijent(this, null, null, false).stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum();
    }

    public boolean getKarticaLojalnosti(float minIznosLojalnosti) {
        return getUkupnoPlatio() >= minIznosLojalnosti;
    }
}
