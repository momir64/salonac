package rs.moma.managers;

import rs.moma.entities.RadnikPlata;
import rs.moma.entities.Isplata;
import rs.moma.DataTools;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static rs.moma.DataTools.fileIsplate;
import static rs.moma.DataTools.toArrayList;

public class Isplate {
    private final String poruka = "isplata";

    // CRUD
    public ArrayList<Isplata> get() {
        ArrayList<Isplata> isplate = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileIsplate), StandardCharsets.UTF_8);
            for (String line : lines)
                isplate.add(new Isplata(line));
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja isplata!");
        }
        return isplate;
    }

    public void add(Isplata isplata) {
        DataTools.add(get(), fileIsplate, poruka, isplata);
    }

    public void remove(Isplata isplata) {
        DataTools.remove(get(), fileIsplate, poruka, isplata);
    }

    public void edit(Isplata oldIsplata, Isplata newIsplata) {
        DataTools.edit(get(), fileIsplate, poruka, oldIsplata, newIsplata);
    }

    // Isplate
    public ArrayList<RadnikPlata> generatePlate() {
        return toArrayList(new Zaposleni().get().stream().map(zaposleni -> new RadnikPlata(zaposleni.ID, zaposleni.getPlata())));
    }

    public void isplati() {
        Optional<Isplata> poslednjaIsplata = get().stream().max(Isplata::compareTo);
        LocalDateTime     trenutniMesec    = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        if (!poslednjaIsplata.isPresent())
            add(new Isplata(trenutniMesec, generatePlate()));
        else
            for (LocalDateTime mesec = poslednjaIsplata.get().Mesec.plusMonths(1); mesec.isBefore(trenutniMesec) || mesec.isEqual(trenutniMesec); mesec = mesec.plusMonths(1))
                 add(new Isplata(mesec, generatePlate()));
    }
}
