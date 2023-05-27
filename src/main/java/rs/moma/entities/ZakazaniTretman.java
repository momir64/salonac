package rs.moma.entities;

import rs.moma.DataTools.EStanjeTermina;
import rs.moma.managers.ZakazaniTretmani;

import java.time.LocalDateTime;

import static rs.moma.DataTools.EStanjeTermina.OTKAZAO_KLIJENT;
import static rs.moma.DataTools.EStanjeTermina.OTKAZAO_SALON;
import static rs.moma.DataTools.SP1;

public class ZakazaniTretman {
    public final int            ID;
    public final int            TretmanID;
    public final float          Cena;
    public final int            Trajanje;
    public final LocalDateTime  Vreme;
    public final EStanjeTermina Stanje;
    public final int            KlijentID;
    public final int            KozmeticarID;
    public final boolean        KarticaLojalnosti;

    public ZakazaniTretman(int id, int tretmanID, float cena, int trajanje, LocalDateTime vreme, EStanjeTermina stanje, int klijentID, int kozmeticarID, boolean karticaLojalnosti) {
        ID                = id;
        TretmanID         = tretmanID;
        Cena              = cena;
        Trajanje          = trajanje;
        Vreme             = vreme;
        Stanje            = stanje;
        KlijentID         = klijentID;
        KozmeticarID      = kozmeticarID;
        KarticaLojalnosti = karticaLojalnosti;
    }

    public ZakazaniTretman(int tretmanID, float cena, int trajanje, LocalDateTime vreme, EStanjeTermina stanje, int klijentID, int kozmeticarID, boolean karticaLojalnosti) {
        this(new ZakazaniTretmani().getNewID(), tretmanID, cena, trajanje, vreme, stanje, klijentID, kozmeticarID, karticaLojalnosti);
    }

    public ZakazaniTretman(String line) {
        String[] data = line.split(SP1);
        ID                = Integer.parseInt(data[0]);
        TretmanID         = Integer.parseInt(data[1]);
        Cena              = Float.parseFloat(data[2]);
        Trajanje          = Integer.parseInt(data[3]);
        Vreme             = LocalDateTime.parse(data[4]);
        Stanje            = EStanjeTermina.valueOf(data[5]);
        KlijentID         = Integer.parseInt(data[6]);
        KozmeticarID      = Integer.parseInt(data[7]);
        KarticaLojalnosti = Boolean.parseBoolean(data[8]);
    }

    @Override
    public String toString() {
        return Integer.toString(ID) + SP1 +
               TretmanID + SP1 +
               Cena + SP1 +
               Trajanje + SP1 +
               Vreme + SP1 +
               Stanje + SP1 +
               KlijentID + SP1 +
               KozmeticarID + SP1 +
               KarticaLojalnosti;
    }

    @Override
    public boolean equals(Object zakazaniTretman) {
        if (this == zakazaniTretman) return true;
        if (!(zakazaniTretman instanceof ZakazaniTretman)) return false;
        return ID == ((ZakazaniTretman) zakazaniTretman).ID;
    }

    // Specijalne get metode
    public float getPopust() {
        return KarticaLojalnosti ? 0.9f : 1;
    }

    public float getPlaceniIznos() {
        return Stanje == OTKAZAO_SALON ? 0 : Stanje == OTKAZAO_KLIJENT ? Cena * getPopust() * 0.9f : Cena * getPopust();
    }
}
