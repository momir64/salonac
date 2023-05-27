package rs.moma.entities;

import static rs.moma.DataTools.SP2;

public class RadnikPlata {
    public final int   RadnikID;
    public final float Plata;

    public RadnikPlata(int radnikID, float plata) {
        RadnikID = radnikID;
        Plata    = plata;
    }

    public RadnikPlata(String line) {
        String[] data = line.split("\\|");
        RadnikID = Integer.parseInt(data[0]);
        Plata    = Float.parseFloat(data[1]);
    }

    @Override
    public String toString() {
        return Integer.toString(RadnikID) + SP2 + Plata;
    }
}
