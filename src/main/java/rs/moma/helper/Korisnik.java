package rs.moma.helper;

import static rs.moma.helper.DataTools.SP1;

public class Korisnik implements ClassWithID {
    public       int            ID;
    public final String         Ime;
    public final String         Prezime;
    public final DataTools.EPol Pol;
    public final String         Telefon;
    public final String         Adresa;
    public final String         Username;
    public final String         Lozinka;

    public Korisnik(int id, String ime, String prezime, DataTools.EPol pol, String telefon, String adresa, String username, String lozinka) {
        ID       = id;
        Ime      = ime;
        Prezime  = prezime;
        Pol      = pol;
        Telefon  = telefon;
        Adresa   = adresa;
        Username = username;
        Lozinka  = lozinka;
    }

    public Korisnik(String line) {
        String[] data = line.split(SP1);
        ID       = Integer.parseInt(data[0]);
        Ime      = data[1];
        Prezime  = data[2];
        Pol      = DataTools.EPol.valueOf(data[3]);
        Telefon  = data[4];
        Adresa   = data[5];
        Username = data[6];
        Lozinka  = data[7];
    }

    @Override
    public String toString() {
        return ID + SP1 +
               Ime + SP1 +
               Prezime + SP1 +
               Pol + SP1 +
               Telefon + SP1 +
               Adresa + SP1 +
               Username + SP1 +
               Lozinka;
    }

    @Override
    public boolean equals(Object korisnik) {
        if (this == korisnik) return true;
        if (!(korisnik instanceof Korisnik)) return false;
        return ID == ((Korisnik) korisnik).ID;
    }

    @Override
    public void setID(int id) {
        ID = id;
    }

    @Override
    public int getID() {
        return ID;
    }
}
