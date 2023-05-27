package rs.moma.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static rs.moma.DataTools.toArrayList;
import static rs.moma.DataTools.SP1;

public class Isplata implements Comparable<Isplata>{
    public final LocalDateTime          Mesec;
    public final ArrayList<RadnikPlata> Plate;

    public Isplata(LocalDateTime mesec, ArrayList<RadnikPlata> plate) {
        Mesec = mesec;
        Plate = plate;
    }

    public Isplata(String line) {
        String[] data = line.split(SP1);
        Mesec = LocalDateTime.parse(data[0]);
        Plate = toArrayList(Arrays.stream(Arrays.copyOfRange(data, 1, data.length)).map(RadnikPlata::new));
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(String.valueOf(Mesec));
        if (Plate == null) str.append(",null");
        else for (RadnikPlata plata : Plate)
            str.append(SP1).append(plata);
        return str.toString();
    }

    @Override
    public int compareTo(Isplata isplata) {
        return Mesec.compareTo(isplata.Mesec);
    }
}
