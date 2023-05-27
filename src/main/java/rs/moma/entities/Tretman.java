package rs.moma.entities;

import rs.moma.managers.Tretmani;

import static rs.moma.DataTools.SP1;

public class Tretman {
    public final int    ID;
    public final int    TipID;
    public final String Naziv;
    public final float  Cena;
    public final int    Trajanje;

    public Tretman(int id, int tipID, String naziv, float cena, int trajanje) {
        ID       = id;
        TipID    = tipID;
        Naziv    = naziv;
        Cena     = cena;
        Trajanje = trajanje;
    }

    public Tretman(int tipID, String naziv, float cena, int trajanje) {
        this(new Tretmani().getNewID(), tipID, naziv, cena, trajanje);
    }

    public Tretman(String line) {
        String[] data = line.split(SP1);
        ID       = Integer.parseInt(data[0]);
        TipID    = Integer.parseInt(data[1]);
        Naziv    = data[2];
        Cena     = Float.parseFloat(data[3]);
        Trajanje = Integer.parseInt(data[4]);
    }

    @Override
    public String toString() {
        return Integer.toString(ID) + SP1 +
               TipID + SP1 +
               Naziv + SP1 +
               Cena + SP1 +
               Trajanje;
    }

    @Override
    public boolean equals(Object tretman) {
        if (this == tretman) return true;
        if (!(tretman instanceof Tretman)) return false;
        return ID == ((Tretman) tretman).ID;
    }
}
