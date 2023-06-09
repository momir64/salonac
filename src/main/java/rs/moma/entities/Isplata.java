package rs.moma.entities;

import rs.moma.entities.helper.ClassWithID;
import rs.moma.entities.helper.RadnikPlata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static rs.moma.helper.DataTools.toArrayList;
import static rs.moma.helper.DataTools.SP1;

public class Isplata implements Comparable<Isplata>, ClassWithID {
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return Mesec.isEqual(((Isplata) obj).Mesec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Mesec);
    }

    @Override
    public int compareTo(Isplata isplata) {
        return Mesec.compareTo(isplata.Mesec);
    }

    @Override
    public void setID(int id) {}

    @Override
    public int getID() {
        return Mesec.getYear() * 12 + Mesec.getMonthValue();
    }
}
